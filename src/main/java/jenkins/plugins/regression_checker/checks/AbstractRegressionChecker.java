package jenkins.plugins.regression_checker.checks;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Result;

import java.lang.reflect.ParameterizedType;

/**
 * AbstractRegressionChecker is the base class used for building custom regression checkers for different metrics
 *
 * @param <A> the Action type
 * @param <R> the Result type (for the Action A)
 */
abstract class AbstractRegressionChecker<A extends Action, R> {
    protected final Class<A> actionType;

    protected AbstractRegressionChecker() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        this.actionType = (Class<A>) parameterizedType.getActualTypeArguments()[0];
    }

    /**
     * Check for regressions
     *
     * @param build    the build to check
     * @param listener the build listener, used for logging
     */
    public void checkRegressions(AbstractBuild<?, ?> build, BuildListener listener) {
        if (isResultReported(build)) {
            AbstractBuild<?, ?> lastSuccessfulBuild = getLatestSuccessfulBuild(build);
            if (isResultReported(lastSuccessfulBuild)) {
                listener.getLogger().println("Running Code quality regression checks for " + getBuildAction(build).getDisplayName());
                checkRegressionsForAction(build, listener, lastSuccessfulBuild);
            }
        }
    }

    /**
     * Check for regressions for different implementations
     *
     * @param build    the build to check
     * @param listener the build listener, used for logging
     */
    protected abstract void checkRegressionsForAction(AbstractBuild<?, ?> build, BuildListener listener, AbstractBuild<?, ?> lastSuccessfulBuild);

    /**
     * @param buildAction the buildAction to get the result from
     * @return the result for the passed buildAction
     */
    protected abstract R getResult(A buildAction);

    /**
     * @param build the current build
     * @return the latest successful build, or <b>null</b> if no such build exists
     */
    protected AbstractBuild<?, ?> getLatestSuccessfulBuild(AbstractBuild<?, ?> build) {
        AbstractBuild<?, ?> previousBuild = build.getPreviousBuild();
        while (previousBuild != null && previousBuild.getResult() != Result.SUCCESS)
            previousBuild = previousBuild.getPreviousBuild();
        return previousBuild;
    }

    /**
     * @param build the build to check for reports
     * @return <b>true</b> if the build parameter har reported results for the action type A
     */
    protected boolean isResultReported(AbstractBuild<?, ?> build) {
        if (build == null) {
            return false;
        }
        A buildAction = getBuildAction(build);
        return buildAction == null ? false : getResult(buildAction) != null;
    }

    /**
     * Set the build to Result.FAILURE
     *
     * @param build the build to fail
     */
    protected void failBuild(AbstractBuild<?, ?> build) {
        build.setResult(Result.FAILURE);
    }

    /**
     * @param build the build
     * @return the Action for this instance (A)
     */
    protected A getBuildAction(AbstractBuild<?, ?> build) {
        return build.getAction(actionType);
    }

}
