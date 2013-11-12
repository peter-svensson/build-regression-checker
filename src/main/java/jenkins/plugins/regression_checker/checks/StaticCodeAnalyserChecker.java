package jenkins.plugins.regression_checker.checks;


import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.plugins.analysis.core.AbstractResultAction;
import hudson.plugins.analysis.core.BuildResult;
import jenkins.plugins.regression_checker.Messages;

// TODO Not working for Maven project since specific Maven classes are used by Jenkins plugins
public class StaticCodeAnalyserChecker<A extends AbstractResultAction<R>, R extends BuildResult> extends AbstractRegressionChecker<A, R> {

    @Override
    protected void checkRegressionsForAction(AbstractBuild<?, ?> build, BuildListener listener, AbstractBuild<?, ?> lastSuccessfulBuild) {
        int warnings = getBuildAction(build).getResult().getNumberOfWarnings();
        int lastWarnings = getBuildAction(lastSuccessfulBuild).getResult().getNumberOfWarnings();

        int diff = warnings - lastWarnings;
        if (diff > 0) {
            listener.getLogger().println(Messages.RegressionChecker_AnalysisRegression(
                    getBuildAction(build).getDisplayName(),
                    lastSuccessfulBuild.getNumber(),
                    diff));
            failBuild(build);
        }
    }

    @Override
    protected R getResult(A buildAction) {
        return buildAction.getResult();
    }

}
