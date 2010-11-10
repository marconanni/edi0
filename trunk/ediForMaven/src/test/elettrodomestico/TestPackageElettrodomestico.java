package test.elettrodomestico;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestPackageElettrodomestico {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for test.elettrodomestico");
		//$JUnit-BEGIN$
		suite.addTestSuite(ElettrodomesticoMedioConsumoTest.class);
		suite.addTestSuite(ElettrodomesticoAltoConsumoTest.class);
		suite.addTestSuite(ElettrodomesticoBassoConsumoTest.class);
		suite.addTestSuite(ElettrodomesticoTest.class);
		suite.addTestSuite(RappresentazioneElettrodomesticoTest.class);
		suite.addTestSuite(SensoreTest.class);
		
		//$JUnit-END$
		return suite;
	}

}
