package test;

import test.elettrodomestico.ElettrodomesticoAltoConsumoTest;
import test.elettrodomestico.ElettrodomesticoBassoConsumoTest;
import test.elettrodomestico.ElettrodomesticoMedioConsumoTest;
import test.elettrodomestico.ElettrodomesticoTest;
import test.elettrodomestico.RappresentazioneElettrodomesticoTest;
import test.elettrodomestico.SensoreTest;
import test.messaggi.ComandoUserCmdTest;
import test.messaggi.DatiSensoreTest;
import test.messaggi.ReportElettrodomesticoTest;
import test.messaggi.StatusTest;
import test.messaggi.UtilTest;
import test.scontrol.InterruttoreTest;
import test.scontrol.SafetyTimerTest;
import test.scontrol.ScontrolTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestAll {

	public static Test suite() {
		TestSuite suite = new TestSuite("TestAll!");
		//$JUnit-BEGIN$
		suite.addTestSuite(SafetyTimerTest.class);
		suite.addTestSuite(ScontrolTest.class);
		suite.addTestSuite(InterruttoreTest.class);
		
		suite.addTestSuite(ElettrodomesticoMedioConsumoTest.class);
		suite.addTestSuite(ElettrodomesticoAltoConsumoTest.class);
		suite.addTestSuite(ElettrodomesticoBassoConsumoTest.class);
		suite.addTestSuite(ElettrodomesticoTest.class);
		suite.addTestSuite(RappresentazioneElettrodomesticoTest.class);
		suite.addTestSuite(SensoreTest.class);
		
		suite.addTestSuite(ReportElettrodomesticoTest.class);
		suite.addTestSuite(DatiSensoreTest.class);
		suite.addTestSuite(StatusTest.class);
		suite.addTestSuite(ComandoUserCmdTest.class);
		suite.addTestSuite(UtilTest.class);

		//$JUnit-END$
		return suite;
	}

}
