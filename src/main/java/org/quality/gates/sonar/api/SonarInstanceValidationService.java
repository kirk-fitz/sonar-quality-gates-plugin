package org.quality.gates.sonar.api;

import hudson.util.Secret;
import org.apache.commons.lang.StringUtils;
import org.quality.gates.jenkins.plugin.SonarInstance;

public class SonarInstanceValidationService {

    String validateUrl(SonarInstance sonarInstance) {

        String url;

        if (sonarInstance.getUrl().isEmpty()) {
            url = SonarInstance.DEFAULT_URL;
        } else {
            url = sonarInstance.getUrl();
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
        }

        return url;
    }

    String validateUsername(SonarInstance sonarInstance) {

        String sonarUsername;

        if (sonarInstance.getUsername().isEmpty()) {
            sonarUsername = SonarInstance.DEFAULT_USERNAME;
        } else {
            sonarUsername = sonarInstance.getUsername();
        }

        return sonarUsername;
    }

    private Secret validatePassword(SonarInstance sonarInstance) {

        Secret sonarPassword;

        if (sonarInstance.getPass().getPlainText().isEmpty()) {
            sonarPassword = Secret.fromString(SonarInstance.DEFAULT_PASS);
        } else {
            sonarPassword = sonarInstance.getPass();
        }

        return sonarPassword;
    }

    SonarInstance validateData(SonarInstance sonarInstance) {

        if (StringUtils.isNotEmpty(sonarInstance.getToken().getPlainText())) {
            return new SonarInstance(
                    sonarInstance.getName(),
                    validateUrl(sonarInstance),
                    sonarInstance.getToken(),
                    sonarInstance.getTimeToWait(),
                    sonarInstance.getMaxWaitTime());
        } else {
            return new SonarInstance(
                    sonarInstance.getName(),
                    validateUrl(sonarInstance),
                    validateUsername(sonarInstance),
                    validatePassword(sonarInstance),
                    sonarInstance.getTimeToWait(),
                    sonarInstance.getMaxWaitTime());
        }
    }
}
