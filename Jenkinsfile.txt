pipeline {
    agent any

    tools {
        maven "M3"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean package'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                sh 'mvn -B clean deploy'
            }
        }

        stage('Deploy to Tomcat via Ansible') {
            when {
                branch 'develop'    // develop deploys to DEV (your current flow)
            }
            steps {
                ansiblePlaybook playbook: '/opt/ansible/deploy-tomcat.yml',
                                  inventory: '/opt/ansible/hosts',
                                  become: true,
                                  becomeUser: 'root'
            }
        }
    }
}
