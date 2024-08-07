FROM jenkins/jenkins:lts-alpine

# Copy necessary files
COPY docker-test/plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY docker-test/jenkins-configuration.yml /var/jenkins_casc_configs/jenkins-configuration.yml

# Set environment variables
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
ENV CASC_JENKINS_CONFIG="/var/jenkins_casc_configs/jenkins-configuration.yml"

# Install Jenkins plugins
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

# Manual deploy of sonar-quality-gates-plugin
COPY target/sonar-quality-gates.hpi /usr/share/jenkins/ref/plugins/sonar-quality-gates.hpi