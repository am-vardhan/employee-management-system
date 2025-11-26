pipeline {
    agent any

    tools {
        maven "M3"
    }

    options {
        skipDefaultCheckout(false)
        timestamps()
    }

    triggers {
        // No automatic triggers — full manual control
    }

    stages {

        stage("Checkout") {
            steps {
                echo "Checking out branch: ${env.BRANCH_NAME}"
                checkout scm
            }
        }

        stage("Manual Build Trigger") {
            steps {
                script {
                    input message: """
🛠  Build Confirmation Required

Branch selected: ${env.BRANCH_NAME.toUpperCase()}

Click 'Start Build' to continue.
""", ok: "Start Build"
                }
            }
        }

        stage('Build') {
            steps {
                echo "Building Maven project..."
                sh 'mvn -B clean package'
            }
        }

        stage('Upload to Nexus') {
            steps {
                echo "Uploading package to Nexus..."
                sh 'mvn -B clean deploy'
            }
        }

        stage('Manual Deploy Approval') {
            steps {
                script {
                    input message: """
🚀 Deployment Approval Needed

Do you want to deploy this build from branch: ${env.BRANCH_NAME} ?

Click 'Deploy Now' to proceed.
""", ok: "Deploy Now"
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                script {

                    echo "Deploying ${env.BRANCH_NAME} build to Tomcat..."

                    ansiblePlaybook playbook: "/opt/ansible/deploy-tomcat.yml",
                                    inventory: "/opt/ansible/hosts",
                                    become: true,
                                    becomeUser: "root"
                }
            }
        }
    }
}
