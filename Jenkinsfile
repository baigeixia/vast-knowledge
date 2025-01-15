pipeline {
    agent any

    stages {
        stage('克隆代码') {
            steps {
              git credentialsId: '29f354e3-e3db-4ed5-98c8-460e03110e53', url: 'https://github.com/baigeixia/vast-knowledge.git'
            }
        }

        stage('编译公共模块') {
            steps {
                sh 'mvn clean install -pl vast-knowledge-common,vast-knowledge-api -am'
            }
        }
           stage('编译并部署选择的服务') {
                    steps {
                          script {
                                 echo "模块路径 ${server_name} 。"
                          }
                    }
                }


    }
}