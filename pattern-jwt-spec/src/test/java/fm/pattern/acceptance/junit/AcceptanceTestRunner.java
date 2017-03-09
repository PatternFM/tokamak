package fm.pattern.acceptance.junit;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class AcceptanceTestRunner extends BlockJUnit4ClassRunner {

	public AcceptanceTestRunner(Class<?> klass) throws InitializationError {
		super(klass);
		TestExecutionMonitor.testStarted();
	}

	public void run(RunNotifier notifier) {
		notifier.addListener(new AfterTestRunListener());
		super.run(notifier);
	}

}
