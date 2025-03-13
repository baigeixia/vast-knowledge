pipeline {
    agent any
    environment {
        tag = "latest"
        ali_url = "registry.cn-shenzhen.aliyuncs.com"
        ali_project_name = "vk-25"
        ali_credentialsId = "d45925ed-beea-4f73-87ba-432b8e9db465"
        KUBECONFIG = "/home/jenkins/.kube/config"  // 确保kubeconfig路径正确
    }

    tools {
        maven 'Maven 3.8.8'
        jdk 'JDK 21'
    }

    stages {
        stage('Check Environment') {
            steps {
                script {
                    // 验证containerd和kubectl可用性
                    sh 'ctr version'
                    sh 'kubectl version --client'
                }
            }
        }

        stage('克隆代码') {
            steps {
                git credentialsId: '2b317317-386b-4924-90e3-b3c78eb83c4d',
                    url: 'https://gitee.com/tsitsiharry/vast-knowledge.git'
            }
        }

        stage('编译公共模块') {
            steps {
                sh 'mvn clean package'
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

                    def servicePath = service != 'vast-knowledge-gateway' ?
                        "vast-knowledge-service/${service}" :
                        service

                    // Maven打包
                    sh "mvn -f ${servicePath} clean package"

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
                        usernameVariable: 'REGISTRY_USER',
                        passwordVariable: 'REGISTRY_PASSWORD')]) {
                        sh """
                            ctr images ls | grep ${ali_url} | awk '{print \$1}' | xargs -I{} ctr images rm {}
                            ctr images pull --user \$REGISTRY_USER:\$REGISTRY_PASSWORD ${full_image_name}
                            ctr images push ${full_image_name} --skip-verify
                        """
                    }

                    // Kubernetes部署
                    def yamlDir = "${servicePath}/k8s"
                    def namespace = service

                    sh """
                        # 替换YAML模板参数
                        sed -i 's#{{IMAGE}}#${full_image_name}#g' ${yamlDir}/*.yaml
                        sed -i 's#{{PORT}}#${service_port}#g' ${yamlDir}/*.yaml
                        sed -i 's#{{ENV}}#prod#g' ${yamlDir}/*.yaml

                        # 应用Kubernetes配置
                        kubectl apply -n ${namespace} -f ${yamlDir}/
                    """
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