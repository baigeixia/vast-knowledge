pipeline {
    agent any
    environment {
        tag = "latest"
        ali_url = "registry.cn-shenzhen.aliyuncs.com"
        ali_project_name = "vk-25"
        ali_credentialsId = "d45925ed-beea-4f73-87ba-432b8e9db465"
    }

    tools {
        maven 'Maven 3.8.8'
        jdk 'JDK 21'
    }

    stages {
       stage('Check JAVA_HOME') {
			steps {
				script {
					// 打印 JAVA_HOME
					sh 'echo $JAVA_HOME'
					// 打印 Java 版本
					sh '$JAVA_HOME/bin/java -version'
				}
			}
		}
        stage('克隆代码') {
            steps {
                git credentialsId: '255712a3-6ca4-4692-851f-5d801b325938', url: 'https://gitee.com/tsitsiharry/vast-knowledge.git'
            }
        }
        stage('编译公共模块') {
            steps {
                sh 'mvn clean package'  // 编译并打包
            }
        }

        stage('编译并部署选择的服务') {
            steps {
                script {
                    // 确保 server_name 被正确赋值
                    if (!server_name || server_name.trim().isEmpty()) {
                        error "server_name 不能为空"
                    }

                    // 处理服务名称
                    def parts = server_name.split('@')
                    if (parts.size() != 2) {
                        error "server_name 格式不正确，应为 'module@part'"
                    }
                    //服务名称
                    def service = parts[0]
                    //服务端口
                    def service_part = parts[1]
                    //服务名称 简称
                    def mirror_name = service.split('-')[-1]

                   //目录结构调整
                    def servicePath = service
					if (service != 'vast-knowledge-gateway') {
						servicePath = "vast-knowledge-service/${service}"
					}

					 // 编译并构建镜像
                    sh "mvn -f ${servicePath} clean package"
                    def imageName = "${mirror_name}:${tag}"

                    sh """
                        # 删除旧镜像（如果存在）
                        docker rmi -f ${imageName} || true
                        # 构建新镜像
                        docker build --no-cache -t ${imageName} -f ${servicePath}/Dockerfile ${servicePath}
                    """

                    sh "docker tag ${imageName} ${ali_url}/${ali_project_name}/${mirror_name}:${tag}"

                    // 登录到阿里云 Docker Registry
                    withCredentials([usernamePassword(credentialsId: ali_credentialsId, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh '''
                            echo "$DOCKER_PASSWORD" | docker login --username="$DOCKER_USERNAME" --password-stdin ${ali_url}
                        '''
                    }

                    // 推送镜像
                    sh "docker push ${ali_url}/${ali_project_name}/${mirror_name}:${tag}"

                    // 删除本地镜像
                    def imageId = sh(script: "docker images -q ${imageName}", returnStdout: true).trim()
                    if (imageId) {
                        sh "docker rmi -f ${imageId}"
                    }

                    // 定义推送服务器映射
                    def pushServerMap = [
                        'gateway':'local',
                        'system':'local',
                        'user':'local',
                        'analyze':'ali_server',
                        'dfs':'ali_server',
                        'behaviour':'ali_server',
                        'article':'tx_server',
                        'search':'tx_server',
                        'comment':'tx_server2',
                        'wemedia':'tx_server2'
                    ]

                    def push_server = pushServerMap.get(mirror_name)
                    echo "push_server: ${push_server}"
                    if (!push_server) {
                        error "push_server 错误，检查pushServerMap是否对应"
                    }

                    // 根据推送服务器类型执行不同的部署命令
                    if (push_server == 'local') {
                        sh "/opt/jenkins_shell/deploy.sh $ali_url $ali_project_name $mirror_name ${tag} ${service_part}"
                    } else {
						echo "publishers_server: ${push_server}"
                        sshPublisher(publishers: [sshPublisherDesc(configName: "${push_server}", transfers: [
                            sshTransfer(
                                cleanRemote: false,
                                execCommand: "/opt/jenkins_shell/deploy.sh $ali_url $ali_project_name $mirror_name ${tag} ${service_part}",
                                execTimeout: 120000,
                                verbose: true
                            )
                        ], usePromotionTimestamp: false, verbose: true)])
                    }
                }
            }
        }
    }

    post {
        always {
            sh 'docker image prune -f'  // 清理无用的 Docker 镜像
        }

        success {
            echo 'Build and Deployment succeeded!'
        }

        failure {
            echo 'Build or Deployment failed.'
        }
    }
}
