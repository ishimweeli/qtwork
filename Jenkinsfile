def appname ='gitinspired-rw'
def s3_bucket ='capstone-rw-codedeploy'
def s3_filename = 'git-inspired-asst4-bn-rw-src'
def deploy_group = 'ass-4-backend'
// def deploy_group_prod = ''

//Slack Notification Integration
def gitName = env.GIT_BRANCH
def jobName = env.JOB_NAME
def branchName = env.BRANCH_NAME
def main_branch = ['staging', 'develop']


// Environments Declaration
environment {
  jobName = env.JOB_NAME
  branchName = env.BRANCH_NAME
}

// Successful Build
def buildSuccess = [
  [text: "Git-Inspired-nov backend Build Successful on ${branchName}",
  fallback: "Git-Inspired-nov backend Build Successful on ${branchName}",
  color: "#00FF00"
  ]
]

// Failed Build
def buildError = [
  [text: "Git-Inspired-nov backend Build Failed on ${branchName}",
  fallback: "Git-Inspired-nov backend Build Failed on ${branchName}",
  color: "#FF0000"
  ]
]

pipeline {
  agent any
  

  stages {
    

    stage('Build') {
        // when {
        // anyOf {
        //     branch 'staging';
        //     branch 'develop';
        //     }
        // // }f {
        //     branch 'staging';
        //     branch 'develop';
        //     }
        // // }
        steps {
            sh './mvnw wrapper:wrapper'
        }
    }
    stage('Clean WS') {
      // steps {
      //   cleanWs()
      // 	}
         steps {
         withAWS(region:'eu-west-1',credentials:'aws-cred') {
           script {
             def gitsha = sh(script: 'git log -n1 --format=format:"%H"', returnStdout: true)
             s3_filename = "${s3_filename}-${gitsha}"
             sh """
                 aws deploy push \
                 --application-name ${appname} \
                 --description "This is a revision for the ${appname}-${gitsha}" \
                 --s3-location s3://${s3_bucket}/${s3_filename}.zip \
                 --source .
               """
           }
         }
       }
     }
	 stage('Deploy to Development') {
         when {
             branch 'develop'
         }
       steps {
         withAWS(region:'eu-west-1',credentials:'aws-cred') {
           script {
             sh """
                 aws deploy create-deployment \
                 --application-name ${appname} \
                 --deployment-config-name CodeDeployDefault.OneAtATime \
                 --deployment-group-name ${deploy_group} \
                 --file-exists-behavior OVERWRITE \
                 --s3-location bucket=${s3_bucket},key=${s3_filename}.zip,bundleType=zip
               """
           }
         }
	   }
	 }
    
  	}
 
 post {
    always {
      echo 'One way or another, I have finished'
      cleanWs()
    }
    success {
      script {
        if (BRANCH_NAME in main_branch) {
            slackSend(channel:"assign-front-team-rw", attachments: buildSuccess)
          }
      }
        echo 'I passed successfully'
    }
    unstable {
      echo 'I am unstable :/'
    }
    failure {
    script {
      if (BRANCH_NAME in main_branch) {
          slackSend(channel:"assign-front-team-rw", attachments: buildError)
          }
    }
        echo 'I have failed'
    }
    changed {
      echo 'Things were different before..'
    	}
  }
}


