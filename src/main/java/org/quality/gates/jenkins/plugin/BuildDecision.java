package org.quality.gates.jenkins.plugin;

import hudson.model.BuildListener;
import org.json.JSONException;
import org.quality.gates.sonar.api.QualityGatesProvider;

public class BuildDecision {

    private QualityGatesProvider qualityGatesProvider;

    public BuildDecision(SonarInstance sonarInstance) {
        qualityGatesProvider = new QualityGatesProvider(sonarInstance);
    }

    public BuildDecision(QualityGatesProvider qualityGatesProvider) {
        this.qualityGatesProvider = qualityGatesProvider;
    }

    public boolean getStatus(SonarInstance sonarInstance, JobConfigData jobConfigData, BuildListener listener)
            throws QGException {

        try {
            return qualityGatesProvider
                    .getAPIResultsForQualityGates(jobConfigData, sonarInstance, listener)
                    .hasStatusGreen();
        } catch (JSONException | InterruptedException e) {
            throw new QGException("Please check your credentials or your Project Key", e);
        }
    }

    SonarInstance chooseSonarInstance(GlobalSonarQualityGatesConfiguration globalConfig, JobConfigData jobConfigData) {

        SonarInstance sonarInstance;

        if (globalConfig.fetchSonarInstances().isEmpty()) {
            sonarInstance = noSonarInstance(jobConfigData);
        } else if (globalConfig.fetchSonarInstances().size() == 1) {
            sonarInstance = singleSonarInstance(globalConfig, jobConfigData);
        } else {
            sonarInstance = multipleSonarInstances(jobConfigData.getSonarInstanceName(), globalConfig);
        }

        return sonarInstance;
    }

    private SonarInstance noSonarInstance(JobConfigData jobConfigData) {

        jobConfigData.setSonarInstanceName("");
        return new SonarInstance();
    }

    private SonarInstance singleSonarInstance(
            GlobalSonarQualityGatesConfiguration globalConfig, JobConfigData jobConfigData) {

        SonarInstance sonarInstance = globalConfig.fetchSonarInstances().get(0);
        jobConfigData.setSonarInstanceName(sonarInstance.getName());
        return sonarInstance;
    }

    public SonarInstance multipleSonarInstances(
            String instanceName, GlobalSonarQualityGatesConfiguration globalConfig) {

        SonarInstance sonarInstance = globalConfig.getSonarInstanceByName(instanceName);

        if (sonarInstance != null) {
            return sonarInstance;
        }

        return null;
    }
}
