pipeline {
    agent any
    stages {
        stage('compile') {
	         steps {
                // step1 
                echo 'compiling..'
		            git url: 'https://github.com/lerndevops/PetClinic'
		            sh script: '/opt/maven/bin/mvn compile'
           }
        }
        stage('codereview-pmd') {
	         steps {
                // step2
                echo 'codereview..'
		            sh script: '/opt/maven/bin/mvn -P metrics pmd:pmd'
           }
	         post {
               success {
		             recordIssues enabledForFailure: true, tool: pmdParser(pattern: '**/target/pmd.xml')
               }
           }		
        }
        stage('unit-test') {
	          steps {
                // step3
                echo 'unittest..'
                 git url: 'https://github.com/lerndevops/PetClinic'
		            sh script: '/opt/maven/bin/mvn compile'
            } 
           
	          	
        }
        stage('codecoverage') {

           tools {
              jdk 'java1.8'
           }
	         steps {
                // step4
                echo 'codecoverage..'
		            
           }
	         
        }
        stage('package/build-war') {
	         steps {
                // step5
                echo 'package......'
                 git url: 'https://github.com/lerndevops/PetClinic'
		            sh script: '/opt/maven/bin/mvn compile'
	
           }		
        }
        stage('build & push docker image') {
	         steps {
              echo 'build & push docker image'
              
              	
           }		
        }
    stage('Deploy-App-QA') {
  	   steps {
                   echo 'Deploy to QA'
              
	   }
	   post { 
              always { 
                cleanWs() 
	      }
	   }
	}
     stage('Deploy-App-PROD') {
	         steps {
                // step2
                echo 'Deploy-App-PROD'
		            sh script: '/opt/maven/bin/mvn -P metrics pmd:pmd'
           }
	         post {
               success {
		             recordIssues enabledForFailure: true, tool: pmdParser(pattern: '**/target/pmd.xml')
               }
           }		
        }       
    }
}
