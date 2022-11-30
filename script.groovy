def buildJar() {
    echo "building the application..."
    sh 'mvn package'
} 

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'Dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t nikila99/java-maven-app:jma-2.0 .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push nikila99/java-maven-app:jma-2.0'
    }
} 

def deployApp() {
    steps {
        script {
            def dockerCMD = "docker -p 3080:3080 -d run nikila99/java-maven-appma-2.0:jma-2.0"
            sshagent(['azurevm1']) {
               sh "ssh -o StrictHostKeyChecking=no azureuser@20.120.152.114 ${dockerCMD}"
            }
        }
    }
} 

return this

//commet for testing





