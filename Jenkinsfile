pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'workhub'
    }

    stages {
        stage('Build') {
            steps {
                script {
                    sh 'mvn clean package'
                    docker.build(env.DOCKER_IMAGE)
                }
            }
        }

        stage('Test') {
            steps {
                // Add your test commands here
                sh 'mvn test'
            }
        }

    }
}
