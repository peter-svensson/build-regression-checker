package jenkins.plugins.regression_checker;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.plugins.checkstyle.CheckStyleResultAction;
import hudson.plugins.cobertura.CoberturaBuildAction;
import hudson.plugins.findbugs.FindBugsResultAction;
import hudson.plugins.jacoco.JacocoBuildAction;
import hudson.plugins.pmd.PmdResultAction;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import jenkins.plugins.regression_checker.checks.CheckStyleChecker;
import jenkins.plugins.regression_checker.checks.CoberturaCoverageChecker;
import jenkins.plugins.regression_checker.checks.FindbugsChecker;
import jenkins.plugins.regression_checker.checks.JacocoCoverageChecker;
import jenkins.plugins.regression_checker.checks.PMDChecker;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

/**
 * {@link hudson.tasks.Recorder} that captures code quality regression between builds.
 *
 * @author Peter Liljenberg
 */
public class AnalysisRegressionChecker extends Recorder {
    private final boolean checkPMD;
    private final boolean checkFindbugs;
    private final boolean checkCheckstyle;
    private final boolean checkCobertura;
    private final boolean checkJacoco;

    @DataBoundConstructor
    public AnalysisRegressionChecker(Boolean checkPMD, Boolean checkFindbugs,
                                     Boolean checkCheckstyle, Boolean checkCobertura, Boolean checkJacoco) {
        this.checkPMD = evaluateBoolean(checkPMD);
        this.checkCheckstyle = evaluateBoolean(checkCheckstyle);
        this.checkFindbugs = evaluateBoolean(checkFindbugs);
        this.checkCobertura = evaluateBoolean(checkCobertura);
        this.checkJacoco = evaluateBoolean(checkJacoco);
    }

    private boolean evaluateBoolean(Boolean b) {
        return b != null && b;
    }

    public boolean isCheckPMD() {
        return checkPMD;
    }

    public boolean isCheckFindbugs() {
        return checkFindbugs;
    }

    public boolean isCheckCheckstyle() {
        return checkCheckstyle;
    }

    public boolean isCheckCobertura() {
        return checkCobertura;
    }

    public boolean isCheckJacoco() {
        return checkJacoco;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        if (build.getResult().isBetterOrEqualTo(Result.SUCCESS)) {
            listener.getLogger().println("Running Code quality regression checks");
            checkStatisticalCodeAnalysisRegression(build, listener);
            checkCoverageRegression(build, listener);
        }
        // TODO Support different tools, Emma, Clover, CompilerWarnings etc
        return true;
    }

    private void checkStatisticalCodeAnalysisRegression(AbstractBuild<?, ?> build, BuildListener listener) {
        if (checkPMD && DescriptorImpl.hasPMD()) {
            new PMDChecker().checkRegressions(build, listener);
        }
        if (checkFindbugs && DescriptorImpl.hasFindbugs()) {
            new FindbugsChecker().checkRegressions(build, listener);
        }
        if (checkCheckstyle && DescriptorImpl.hasCheckstyle()) {
            new CheckStyleChecker().checkRegressions(build, listener);
        }
    }

    private void checkCoverageRegression(AbstractBuild<?, ?> build, BuildListener listener) {
        if (checkCobertura && DescriptorImpl.hasCobertura()) {
            new CoberturaCoverageChecker().checkRegressions(build, listener);
        }
        if (checkJacoco && DescriptorImpl.hasJacoco()) {
            new JacocoCoverageChecker().checkRegressions(build, listener);
        }
    }

    @Extension(ordinal = -100)
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public static boolean hasFindbugs() {
            try {
                FindBugsResultAction.class.getName();
                return true;
            } catch (LinkageError e) {
                return false;
            }
        }

        public static boolean hasPMD() {
            try {
                PmdResultAction.class.getName();
                return true;
            } catch (LinkageError e) {
                return false;
            }
        }

        public static boolean hasCheckstyle() {
            try {
                CheckStyleResultAction.class.getName();
                return true;
            } catch (LinkageError e) {
                return false;
            }
        }

        public static boolean hasCobertura() {
            try {
                CoberturaBuildAction.class.getName();
                return true;
            } catch (LinkageError e) {
                return false;
            }
        }

        public static boolean hasJacoco() {
            try {
                JacocoBuildAction.class.getName();
                return true;
            } catch (LinkageError e) {
                return false;
            }
        }

        @Override
        public String getDisplayName() {
            return "Fail the build if the code quality decrease";
        }
    }
}
