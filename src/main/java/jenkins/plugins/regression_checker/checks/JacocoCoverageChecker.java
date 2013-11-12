package jenkins.plugins.regression_checker.checks;

import hudson.plugins.jacoco.JacocoBuildAction;
import hudson.plugins.jacoco.report.CoverageReport;

public class JacocoCoverageChecker extends CoverageChecker<JacocoBuildAction, CoverageReport> {

    @Override
    protected CoverageReport getResult(JacocoBuildAction buildAction) {
        return buildAction.getResult();
    }

    @Override
    protected float getBranchCoverage(JacocoBuildAction buildAction) {
        return buildAction.getBranchCoverage().getPercentageFloat();
    }

    @Override
    protected float getLineCoverage(JacocoBuildAction buildAction) {
        return buildAction.getLineCoverage().getPercentageFloat();
    }
}
