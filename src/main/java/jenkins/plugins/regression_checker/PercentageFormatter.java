package jenkins.plugins.regression_checker;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PercentageFormatter {
    public static String format(float diff) {
        NumberFormat percentInstance = DecimalFormat.getPercentInstance();
        percentInstance.setMinimumFractionDigits(1);
        percentInstance.setMaximumFractionDigits(1);
        double ceilingValue = Math.ceil(diff * 10) / 1000;
        String diffPercentString = percentInstance.format(ceilingValue);
        return diffPercentString;
    }
}
