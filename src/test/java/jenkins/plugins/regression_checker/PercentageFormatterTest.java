package jenkins.plugins.regression_checker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PercentageFormatterTest {
	@Test
	public void formatReturnProvidedValueFormattedAsAPercentageValueString() {
		assertEquals("95.3%", PercentageFormatter.format(95.27f));
	}

	@Test
	public void formatRoundsTheProvidedValuesUp() {
		assertEquals("0.1%",
				PercentageFormatter.format(0.000000000000000000001f));
	}
}
