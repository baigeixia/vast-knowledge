pipeline {
    agent any

    stages {
        stage('克隆代码') {
            steps {
                git credentialsId: '29f354e3-e3db-4ed5-98c8-460e03110e53', url: 'https://github.com/baigeixia/vast-knowledge.git', branch: 'main'
            }
        }

        stage('构建 Maven 项目') {
            steps {
                dir('vast-knowledge-gateway') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('构建 Docker 镜像') {
            steps {
                dir('vast-knowledge-gateway') {
                    script {
                        def image = docker.build("vk-dev")
                        docker.withRegistry('https://registry.cn-hangzhou.aliyuncs.com', 'aliyun-credentials-id') {
                            image.push()
                        }
                    }
                }
            }
        }

        stage('部署到服务器') {
            steps {
                sshagent(credentials: ['ssh-credentials-id']) {
                    sh '''
                        ssh user@server "docker stop vk-dev || true && docker rm vk-dev || true"
                        ssh user@server "docker pull registry.cn-hangzhou.aliyuncs.com/vk-25/vk-dev && docker run -d --name vk-dev registry.cn-hangzhou.aliyuncs.com/vk-25/vk-dev"
                    '''
                }
            }
        }
    }
}