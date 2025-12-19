pipeline {
    agent any

    tools {
        maven "M3"
    }

    options {
        skipDefaultCheckout(false)
        disableConcurrentBuilds()
        timestamps()
    }

    environment {
        APP_NAME     = "ems"
        GROUP_ID     = "com.example"
        ARTIFACT_ID  = "ems"
        VERSION      = "0.0.1-SNAPSHOT"
        NEXUS_URL    = "http://localhost:8081"
        NEXUS_REPO   = "maven-snapshots"
    }

    stages {

        stage("Checkout") {
            steps {
                echo "Checking out branch: ${env.BRANCH_NAME}"
                checkout scm
            }
        }

        stage("Build & Package") {
            steps {
                echo "Building Maven project..."
                sh 'mvn -B clean package'
            }
        }

        stage("Upload to Nexus") {
            steps {
                sh 'mvn -B deploy -DskipTests'
            }
        }

        stage("Deploy to Tomcat (Auto for develop)") {
    when {
        branch 'develop'
    }
    steps {
        echo "Auto deploying develop branch from Nexus..."

        withCredentials([string(credentialsId: 'nexus-maven-pass', variable: 'NEXUS_PASS')]) {

            ansiblePlaybook(
                playbook: "/opt/ansible/deploy-tomcat.yml",
                inventory: "/opt/ansible/hosts",
                become: true,
                becomeUser: "root",
                extraVars: [
                    nexus_url   : env.NEXUS_URL,
                    nexus_repo  : env.NEXUS_REPO,
                    group_id    : env.GROUP_ID,
                    artifact_id : env.ARTIFACT_ID,
                    version     : env.VERSION,
                    nexus_user  : 'nexus-deploy',
                    nexus_pass  : env.NEXUS_PASS,   // ✅ now defined
                    branch_name : env.BRANCH_NAME
                ]
            )
        }
    }
 }
}
