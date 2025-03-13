pipeline {
    agent any
    environment {
        tag = "env.BUILD_NUMBER"
        ali_url = "registry.cn-shenzhen.aliyuncs.com"
        ali_project_name = "vk-25"
        ali_credentialsId = "2bbf117e-0bfd-406d-95e1-9d9d593474c7"
        git_auth_id = "2b317317-386b-4924-90e3-b3c78eb83c4d"
    }

    tools {
        maven 'maven3.8.8'
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
					// 服务名称@访问端口 common@19008
                    if (!server_name || server_name.trim().isEmpty()) {
                        error "server_name 不能为空"
                    }

                    def parts = server_name.split('@')
                    if (parts.size() != 2) {
                        error "server_name 格式不正确，应为 'module@port'"
                    }

                    def service = parts[0]
                    def service_port = parts[1]

                    def servicePath = service != 'gateway' ?
                        "vast-knowledge-service/vast-knowledge-${service}" :
                        'vast-knowledge-${service}'

                    // Maven打包
                    sh "mvn -f ${servicePath} clean package -Dfile.encoding=UTF-8 -Dmaven.test.skip=true"

                    // Containerd构建镜像
                    def full_image_name = "${ali_url}/${ali_project_name}/${service}:${tag}"
                    sh """
                        ctr image rm ${full_image_name} || true
                        ctr image build -t ${full_image_name} \
                            --build-arg JAR_FILE=target/*.jar \
                            -f ${servicePath}/Dockerfile \
                            ${servicePath}
                    """

                    // 推送镜像
                    withCredentials([usernamePassword(
                        credentialsId: ali_credentialsId,
                        usernameVariable: 'USERNAME',
                        passwordVariable: 'PASSWORD')]) {
                        sh """
                            ctr images ls | grep ${ali_url} | awk '{print \$1}' | xargs -I{} ctr images rm {}
                            ctr images pull --user \$USERNAME:\$PASSWORD ${full_image_name}
                            ctr images push ${full_image_name} --skip-verify
                        """
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
            // 清理containerd镜像
            sh 'ctr images ls -q | xargs -I{} ctr images rm {} || true'
        }

        success {
            slackSend color: 'good', message: "部署成功: ${env.JOB_NAME} [${env.BUILD_NUMBER}]"
        }

        failure {
            slackSend color: 'danger', message: "部署失败: ${env.JOB_NAME} [${env.BUILD_NUMBER}]"
        }
    }
}