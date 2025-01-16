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
    }

    stages {
        stage('克隆代码') {
            steps {
                git credentialsId: '29f354e3-e3db-4ed5-98c8-460e03110e53', url: 'https://github.com/baigeixia/vast-knowledge.git'
            }
        }

        stage('编译公共模块') {
            steps {
                sh 'mvn clean install -pl common,api -am'
            }
        }

        stage('编译并部署选择的服务') {
            steps {
                script {
                    // 定义server_name的值（示例）
                    def server_name = 'gateway@local' // 根据需要设置
                    echo "模块路径 ${server_name} 。"

                    // 提取容器仓库名称部分
                    def parts = server_name.split('@')
                    if (parts.size() != 2) {
                        error "server_name 格式不正确，应为 'module@part'"
                    }
                    def service = parts[0]
                    def service_part = parts[1]
                    def mirror_name = service.split('-')[-1]

                    // 编译，构建本地镜像
                    sh "mvn -f ${service} clean package dockerfile:build"
                    def imageName = "${mirror_name}:${tag}"
                    sh "docker tag ${imageName} ${ali_url}/${ali_project_name}/${mirror_name}:${tag}"

                    // 输出变量值以便调试
                    echo "service: ${service}"
                    echo "service_part: ${service_part}"
                    echo "mirror_name: ${mirror_name}"
                    echo "imageName: ${imageName}"

                    // 登录阿里云Docker Registry
                    withCredentials([usernamePassword(credentialsId: ali_credentialsId, passwordVariable: 'password', usernameVariable: 'username')]) {
                        sh "docker login --username=${username} --password=${password} ${ali_url}"
                    }

                    // 推送镜像
                    sh "docker push ${ali_url}/${ali_project_name}/${mirror_name}:${tag}"

                    // 删除本地镜像
                    sh "docker rmi -f ${imageName}"

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
                        // 如果是本地服务器，调用本地部署脚本
                        sh "/opt/jenkins_shell/deploy.sh $ali_url $ali_project_name $mirror_name"
                    } else {
                        // 如果是远程服务器，使用 sshPublisher
                        sshPublisher(publishers: [sshPublisherDesc(configName: 'push_server', transfers: [
                            sshTransfer(
                                cleanRemote: false,
                                excludes: '',
                                execCommand: "/opt/jenkins_shell/deploy.sh $ali_url $ali_project_name $mirror_name",
                                execTimeout: 120000,
                                flatten: false,
                                makeEmptyDirs: false,
                                noDefaultExcludes: false,
                                patternSeparator: '[, ]+',
                                remoteDirectory: '',
                                remoteDirectorySDF: false,
                                removePrefix: '',
                                sourceFiles: ''
                            )
                        ], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])
                    }
                }
            }
        }
    }
}
