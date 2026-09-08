pipeline {
    agent any
    environment {
        IMAGE_NAME     = 'shipping'
        ECR_REPO_NAME  = 'roboshop/shipping'
        AWS_ACCOUNT_ID = '484056256762'
        AWS_REGION     = 'us-east-1'             // update if your ECR repo is in a different region
        ECR_REGISTRY   = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
    }
    tools {
        maven 'Maven3'   // Name configured in Jenkins > Global Tool Configuration
        jdk 'JDK17'      // Name configured in Jenkins > Global Tool Configuration
    }
    stages {
        stage('Read Version') {
            steps {
                script {
                    def pom = readMavenPom file: 'pom.xml'
                    env.APP_VERSION = pom.version
                    echo "Building version ${env.APP_VERSION}"
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    sh """
                        mvn -B clean compile
                    """
                }
            }
        }
        stage('Unit Tests') {
            steps {
                script {
                    sh """
                        mvn -B test
                    """
                }
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('sonarqube-server') {
                        sh """
                            mvn -B sonar:sonar \
                                -Dsonar.projectKey=catalogue \
                                -Dsonar.projectVersion=${APP_VERSION}
                        """
                    }
                }
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Package') {
            steps {
                script {
                    sh """
                        mvn -B package -DskipTests
                    """
                }
            }
        }
        stage('Docker Image Build') {
            steps {
                script {
                    sh """
                        docker build -t ${IMAGE_NAME}:${APP_VERSION} .
                    """
                }
            }
        }
        stage('Trivy Image Scan') {
            steps {
                script {
                    sh """
                        trivy image \
                            --severity HIGH,CRITICAL \
                            --exit-code 1 \
                            --format table \
                            --scanners vuln \
                            -o trivy-report.txt \
                            ${IMAGE_NAME}:${APP_VERSION}
                    """
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'trivy-report.txt', allowEmptyArchive: true
                }
            }
        }
        stage('Push to ECR') {
            steps {
                script {
                    sh """
                        aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}
                        docker tag ${IMAGE_NAME}:${APP_VERSION} ${ECR_REGISTRY}/${ECR_REPO_NAME}:${APP_VERSION}
                        docker push ${ECR_REGISTRY}/${ECR_REPO_NAME}:${APP_VERSION}
                    """
                }
            }
        }
    }
}
