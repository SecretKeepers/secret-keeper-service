pipeline {
    agent any

    tools {
        maven '3.8.8'
    }

    environment {
        MAVEN_OPTS="-Xmx100m -Xms100m"
        DOCKER_IMAGE_NAME = "thepolitician/secret-service"
        ANSIBLE_HOST_KEY_CHECKING="False"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    sh "mvn clean install -Dmaven.test.skip=true"
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    sh "mvn test"
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${env.BUILD_ID} ."
                }
            }
        }

        stage('Publish') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_HUB_USERNAME', passwordVariable: 'DOCKER_HUB_PASSWORD')]) {
                        sh "docker login -u ${DOCKER_HUB_USERNAME} -p ${DOCKER_HUB_PASSWORD}"
                    }
                    sh "docker tag ${DOCKER_IMAGE_NAME}:${env.BUILD_ID} ${DOCKER_IMAGE_NAME}:latest"
                    sh "docker push ${DOCKER_IMAGE_NAME}:${env.BUILD_ID}"
                    sh "docker push ${DOCKER_IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh "ansible-playbook -i /etc/ansible/hosts secret-deploy.yml"
                }
            }
        }
    }

    post {
        always {
            cleanWs()
            script {
                sh "docker rmi -f \$(docker images -q ${DOCKER_IMAGE_NAME}:${env.BUILD_ID})"
            }
        }
    }
}