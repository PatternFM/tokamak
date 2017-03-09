package fm.pattern.acceptance.junit;

public class TestExecutionMonitor {

	private static Integer testClassesToRun = 0;
	private static Integer testClassesExecuted = 0;

	private TestExecutionMonitor() {

	}

	public static void testStarted() {
		testClassesToRun += 1;
	}

	public static void testCompleted() {
		testClassesExecuted += 1;
	}

	public static boolean allTestsHaveRun() {
		return testClassesToRun == testClassesExecuted;
	}

}
