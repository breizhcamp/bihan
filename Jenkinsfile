pipeline {
    agent {
        docker {
            image 'maven:3.9.11-eclipse-temurin-21-noble'
            args '-v /opt/breizhcamp/ci/maven:/var/maven/ -e MAVEN_CONFIG=/var/maven/ --network=k3d-apps'
            reuseNode true
        }
    }
    environment {
        REGISTRY = 'registry.breizhcamp:5000/bihan'
    }
    stages {
        stage('Compile') {
            steps {
                sh 'mvn -Duser.home=/var/maven compile'
            }
        }
        stage('Build & Push Docker Image') {
            steps {
                script {
                    sh "mvn -Duser.home=/var/maven -Djib.to.image=$REGISTRY -Djib.allowInsecureRegistries=true jib:build"
                }
            }
        }
    }
}