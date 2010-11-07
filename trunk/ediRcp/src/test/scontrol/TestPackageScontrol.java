package test.scontrol;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestPackageScontrol {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for test.scontrol");
		//$JUnit-BEGIN$
		suite.addTestSuite(SafetyTimerTest.class);
		suite.addTestSuite(ScontrolTest.class);
		suite.addTestSuite(InterruttoreTest.class);
		//$JUnit-END$
		return suite;
	}

}
