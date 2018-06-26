package com.saucelabs.rdc.helper;

import com.saucelabs.rdc.resource.AppiumSessionResource;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.ws.rs.core.Response;

import static com.saucelabs.rdc.RdcCapabilities.API_KEY;
import static com.saucelabs.rdc.RdcEndpoints.APPIUM_REST_PATH;

public class IntermediateReporter {

	public void processAndReportResult(boolean passed) {
		processResult(passed);
		uploadResult(passed);
	}

	private RestClient client;

	private RdcListenerProvider provider;

	public IntermediateReporter(RdcListenerProvider provider) {
		this.provider = provider;
		initClient();
	}

	private void initClient() {
		String apiEndpoint = this.provider.getApiUrl().toString();

		RemoteWebDriver remoteWebDriver = provider.getRemoteWebDriver();

		this.client = RestClient.Builder.createClient()
				.withEndpoint(apiEndpoint)
				.withToken((String) remoteWebDriver.getCapabilities().getCapability(API_KEY))
				.path(APPIUM_REST_PATH)
				.build();
	}

	public void close() {
		if (client != null) {
			client.close();
		}
		RemoteWebDriver driver = provider.getRemoteWebDriver();
		if (driver != null) {
			driver.quit();
		}
	}

	public void uploadResult(boolean passed) {
		AppiumSessionResource appiumSessionResource = new AppiumSessionResource(client);
		Response response = appiumSessionResource.updateTestReportStatus(provider.getRemoteWebDriver().getSessionId().toString(), passed);
		if (response.getStatus() != 204) {
			System.out.println("Test report status might not be updated on Sauce Labs RDC (TestObject). Status: " + response.getStatus());
		}
	}

	public void processResult(boolean passed) {
		RemoteWebDriver remoteWebDriver = provider.getRemoteWebDriver();
		if (remoteWebDriver == null) {
			throw new IllegalStateException("Appium driver must be set using setDriver method");
		}

		if (!passed) {
			requestScreenshotAndPageSource();
		}
	}

	public void requestScreenshotAndPageSource() {
		RemoteWebDriver remoteWebDriver = provider.getRemoteWebDriver();
		remoteWebDriver.getPageSource();
		remoteWebDriver.getScreenshotAs(OutputType.FILE);
	}
}
