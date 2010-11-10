package test.messaggi;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestPackageMessaggi {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for test.messaggi");
		//$JUnit-BEGIN$
		suite.addTestSuite(ReportElettrodomesticoTest.class);
		suite.addTestSuite(DatiSensoreTest.class);
		suite.addTestSuite(StatusTest.class);
		suite.addTestSuite(ComandoUserCmdTest.class);
		suite.addTestSuite(UtilTest.class);
		//$JUnit-END$
		return suite;
	}

}
