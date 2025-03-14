pipeline {
    agent any
    environment {

        ali_url = "registry.cn-shenzhen.aliyuncs.com"
        ali_project_name = "vk-25"
        ali_credentialsId = "2bbf117e-0bfd-406d-95e1-9d9d593474c7"
        git_auth_id = "2b317317-386b-4924-90e3-b3c78eb83c4d"
    }

    tools {
        maven 'maven3.8.8'
        dockerTool 'docker:stable'
    }

    stages {

        stage('克隆代码') {
            steps {
                git credentialsId: git_auth_id, url: 'https://gitee.com/tsitsiharry/vast-knowledge.git'
            }
        }

        stage('代码编译') {
            steps {
                sh 'mvn clean package -Dmaven.test.skip=true'
            }
        }


        stage('构建并部署服务') {
            steps {
                script {
					// 服务名称 common
                    if (!server_name || server_name.trim().isEmpty()) {
                        error "server_name 不能为空"
                    }

                    def service = server_name
                    def service_port = parts[1]

                    def servicePath = server_name != 'gateway' ?
                        "vast-knowledge-service/vast-knowledge-${service}" :
                        'vast-knowledge-${service}'

                    // Maven打包
                    sh "mvn -f ${servicePath} clean package -Dfile.encoding=UTF-8 -Dmaven.test.skip=true"

                    def tag = env.BUILD_NUMBER
                    def imageName = "${mirror_name}:${tag}"
                    def pushImage = "${ali_url}/${ali_project_name}/${imageName}"

                    sh """
                        # 删除旧镜像（如果存在）
                        crictl rmi ${imageName} || true
                        # 构建新镜像
                        crictl build --no-cache -t ${imageName} -f ${servicePath}/Dockerfile ${servicePath}
                    """

                    sh "crictl tag ${imageName} ${pushImage}"

                    // 登录到阿里云 Docker Registry
                    withCredentials([usernamePassword(credentialsId: ali_credentialsId, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh '''
                            echo "$DOCKER_PASSWORD" | crictl login --username="$DOCKER_USERNAME" --password-stdin ${ali_url}
                        '''
                    }

                    // 推送镜像
                    sh "crictl push ${pushImage}"

                    // 删除本地镜像
                    def imageId = sh(script: "crictl images -q ${imageName}", returnStdout: true).trim()
                    if (imageId) {
                        sh "crictl rmi ${imageId}"
                    }

                  sh """
                      sed -i 's#\${IMAGE_TAG}#${tag}#' '${servicePath}/deploy.yml'
                  """

                  kubernetesDeploy(
                      configs: "${servicePath}/deploy.yml",
                      kubeconfigId: "${k8s_auth}"
                  )

                }
            }
        }
    }

    post {
        always {
            // 清理 containerd 镜像
            sh '''
                # 获取所有镜像ID并逐一删除
                crictl images -q | xargs -I {} crictl rmi {}
            '''
        }

        success {
            slackSend color: 'good', message: "部署成功: ${env.JOB_NAME} [${env.BUILD_NUMBER}]"
        }

        failure {
            slackSend color: 'danger', message: "部署失败: ${env.JOB_NAME} [${env.BUILD_NUMBER}]"
        }
    }
}