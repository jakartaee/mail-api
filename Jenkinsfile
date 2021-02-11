env.label = "mail-tck-ci-pod-${UUID.randomUUID().toString()}"
pipeline {
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
  }
  agent {
    kubernetes {
      label "${env.label}"
      defaultContainer 'jnlp'
      yaml """
apiVersion: v1
kind: Pod
metadata:
spec:
  hostAliases:
  - ip: "127.0.0.1"
    hostnames:
    - "localhost.localdomain"
    - "james.local"
  containers:
  - name: mail-ci
    image: jakartaee/cts-javamail-base:0.1
    command:
    - cat
    tty: true
    imagePullPolicy: Always
    resources:
      limits:
        memory: "5Gi"
        cpu: "1.0"
  - name: james-mail
    image: jakartaee/cts-mailserver:0.1
    command:
    - cat
    env:
      - name: JAVA_TOOL_OPTIONS
        value: -Xmx1G
    ports:
    - containerPort: 1025
    - containerPort: 1143
    tty: true
    imagePullPolicy: Always
    resources:
      limits:
        memory: "3.0Gi"
        cpu: "1.0"
"""
    }
  }
  parameters {
    string(name: 'JAF_BUNDLE_URL',
           defaultValue: 'https://repo1.maven.org/maven2/com/sun/activation/jakarta.activation/2.0.0-RC3/jakarta.activation-2.0.0-RC3.jar',
           description: 'URL required for downloading JAF implementation jar' )
    string(name: 'MAIL_TCK_BUNDLE_URL',
           defaultValue: 'https://ci.eclipse.org/mail/job/mail-tck/job/2.0.0/lastSuccessfulBuild/artifact/bundles/mail-tck-2.0.0-rc1.zip',
           description: 'URL required for downloading Jakarta Mail TCK zip' )
  }
  environment {
    ANT_OPTS = "-Djavax.xml.accessExternalStylesheet=all -Djavax.xml.accessExternalSchema=all -Djavax.xml.accessExternalDTD=file,http" 
    MAIL_USER="user01@james.local"
  }
  stages {
    stage('mail-build') {
      steps {
        container('mail-ci') {
          sh """
            wget 'https://download.java.net/openjdk/jdk11/ri/openjdk-11+28_linux-x64_bin.tar.gz' -O openjdk-11.tar.gz -q
            tar -xzf openjdk-11.tar.gz
            cd jdk-11
            export JAVA_HOME=`pwd`
            cd ..
            bash -x ${WORKSPACE}/docker/build_jakartamail.sh
          """
          archiveArtifacts artifacts: 'mail/target/*.jar'
          stash includes: 'mail/target/*.jar', name: 'mail-bundles'
        }
      }
    }
  
    stage('mail-tck-run') {
      steps {
        container('james-mail') {
	  sh """
	    cd /root
	    /root/startup.sh | tee /root/mailserver.log &
	    sleep 120
	    bash -x /root/create_users.sh 2>&1 | tee /root/create_users.log
	    echo "Mail server setup complete"
	  """
	}
        container('mail-ci') {
	  unstash name: 'mail-bundles'
          sh """
            bash -x ${WORKSPACE}/docker/run_jakartamailtck.sh
          """
          archiveArtifacts artifacts: "mail-tck-results.tar.gz"
          junit testResults: 'results/junitreports/*.xml', allowEmptyResults: true
        }
      }
    }
  }
}
