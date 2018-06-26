package com.saucelabs.rdc;

import com.saucelabs.rdc.helper.IntermediateReporter;
import com.saucelabs.rdc.helper.RdcListenerProvider;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class RdcTestResultWatcher extends TestListenerAdapter {

	private IntermediateReporter reporter;

	@Override
	public void onTestStart(ITestResult testResult) {
		super.onTestStart(testResult);

		Object instance = testResult.getInstance();

		if (instance instanceof RdcWatcherProvider) {
			RdcWatcherProvider watcherProvider = ((RdcWatcherProvider) instance);
			RdcListenerProvider provider = watcherProvider.getProvider();
			reporter = new IntermediateReporter(provider);
		} else {
			throw new IllegalStateException("Test must implement com.saucelabs.rdc.RdcWatcherProvider");
		}
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		super.onTestSuccess(tr);
		reporter.processAndReportResult(true);
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		super.onTestFailure(tr);
		reporter.processAndReportResult(false);
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		super.onTestSkipped(tr);
		reporter.processAndReportResult(false);
	}

	@Override
	public void onFinish(ITestContext testContext) {
		super.onFinish(testContext);
		if (reporter != null) {
			reporter.close();
		}
	}
}