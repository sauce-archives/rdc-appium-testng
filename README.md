# RDC Appium TesNG [![Build Status](https://travis-ci.org/saucelabs/rdc-appium-testng.svg?branch=master)](https://travis-ci.org/saucelabs/rdc-appium-testng)

Sauce Labs **R**eal **D**evice **C**loud Appium TestNG Client Library for updating test status on RDC.

## How to update test status on RDC?
I you want to see *SUCCESS* or *FAILURE* instead of *UNKNOWN* in your test reports on RDC website, you need to use the `RdcTestResultWatcher` like the following:

```java
@Listeners(RdcTestResultWatcher.class)
public class RdcTestResultWatcherTest implements RdcWatcherProvider {

	private RdcListenerProvider provider = RdcListenerProvider.newInstance();
	private AndroidDriver driver;

	@BeforeMethod
	public void setUp() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(API_KEY, "Your Project API key");

		// Initializing Appium driver and setting the watcher
		driver = new AndroidDriver(EU_APPIUM_ENDPOINT, capabilities);
		provider.setDriver(driver);
	}

	@Test
	public void aTestThatUpdatesTestResultStatusOnRdcAfterItFinishes() {
		// driver.testAllTheThings();
	}

	@Override
	public RdcListenerProvider getProvider() {
		return provider;
	}
	
	// No need to close the driver. The library does that automatically.
}
```