package jenkins.plugins.regression_checker.checks;

import hudson.plugins.cobertura.CoberturaBuildAction;
import hudson.plugins.cobertura.targets.CoverageMetric;
import hudson.plugins.cobertura.targets.CoverageResult;

public class CoberturaCoverageChecker extends CoverageChecker<CoberturaBuildAction, CoverageResult> {
    @Override
    protected CoverageResult getResult(CoberturaBuildAction buildAction) {
        return buildAction.getResult();
    }

    @Override
    protected float getBranchCoverage(CoberturaBuildAction buildAction) {
        return buildAction.getResult().getCoverage(CoverageMetric.CONDITIONAL).getPercentageFloat();
    }

    @Override
    protected float getLineCoverage(CoberturaBuildAction buildAction) {
        return buildAction.getResult().getCoverage(CoverageMetric.LINE).getPercentageFloat();
    }
}
