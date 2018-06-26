package com.saucelabs.rdc;

import com.saucelabs.rdc.helper.RdcListenerProvider;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.saucelabs.rdc.RdcCapabilities.API_KEY;
import static com.saucelabs.rdc.RdcEndpoints.EU_APPIUM_ENDPOINT;

@Listeners(RdcTestResultWatcher.class)
public class RdcTestResultWatcherTest implements RdcWatcherProvider {

	private RdcListenerProvider provider = RdcListenerProvider.newInstance();
	private AndroidDriver driver;

	@BeforeMethod
	public void setUp() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(API_KEY, "Your Project API key");
		driver = new AndroidDriver(EU_APPIUM_ENDPOINT, capabilities);

		provider.setDriver(driver);
		printUsefulLinks();
	}

	@Test
	public void aTestThatUpdatesTestResultStatusOnRdcAfterItFinishes() {
		// driver.testAllTheThings();
	}

	@Override
	public RdcListenerProvider getProvider() {
		return provider;
	}

	private void printUsefulLinks() {
		System.out.println("Live view: " + driver.getCapabilities().getCapability(RdcCapabilities.TEST_LIVE_VIEW_URL));
		System.out.println("Test report: " + driver.getCapabilities().getCapability(RdcCapabilities.TEST_REPORT_URL));
	}
}