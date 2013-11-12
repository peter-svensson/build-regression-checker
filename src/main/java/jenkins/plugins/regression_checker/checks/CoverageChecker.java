package jenkins.plugins.regression_checker.checks;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.BuildListener;
import jenkins.plugins.regression_checker.Messages;
import jenkins.plugins.regression_checker.PercentageFormatter;

import java.io.PrintStream;

public abstract class CoverageChecker<A extends Action, R> extends AbstractRegressionChecker<A, R> {

    private final int COVERAGE_THRESHOLD = 85;
    private final String LINE_COVERAGE_TEXT = "linecoverage";
    private final String BRANCH_COVERAGE_TEXT = "branchcoverage";

    @Override
    protected void checkRegressionsForAction(AbstractBuild<?, ?> build, BuildListener listener, AbstractBuild<?, ?> lastSuccessfulBuild) {
        A buildAction = getBuildAction(build);
        A lastBuildAction = getBuildAction(lastSuccessfulBuild);

        float lineCoverage = getLineCoverage(buildAction);
        float branchCoverage = getBranchCoverage(buildAction);
        float lineCoverageLastSuccessfulBuild = getLineCoverage(lastBuildAction);
        float branchCoverageLastSuccessfulBuild = getBranchCoverage(lastBuildAction);

        checkCoverageMetric(build, lastSuccessfulBuild.getNumber(), lineCoverage, lineCoverageLastSuccessfulBuild, LINE_COVERAGE_TEXT, listener.getLogger());
        checkCoverageMetric(build, lastSuccessfulBuild.getNumber(), branchCoverage, branchCoverageLastSuccessfulBuild, BRANCH_COVERAGE_TEXT, listener.getLogger());
    }

    /**
     * @param buildAction the build to check
     * @return the branch coverage for a certain build
     */
    protected abstract float getBranchCoverage(A buildAction);

    /**
     * @param buildAction the build to check
     * @return the line coverage for a certain build
     */
    protected abstract float getLineCoverage(A buildAction);

    private void checkCoverageMetric(AbstractBuild<?, ?> build, int latestBuildNumber, float coverage, float lastCoverage, String metric, PrintStream logger) {
        float diff = lastCoverage - coverage;
        if (diff > 0) {
            logger.println(Messages.RegressionChecker_CoverageRegression(latestBuildNumber, PercentageFormatter.format(diff), metric));

            if (coverage > COVERAGE_THRESHOLD) {
                logger.println(Messages.RegressionChecker_CoverageRegressionThresholdOk(latestBuildNumber, COVERAGE_THRESHOLD));
            } else {
                failBuild(build);
            }
        }
    }

}
