package test.messaggi;

import java.util.Vector;

import Edi.elettrodomestico.StatoElettrodomestico;
import Edi.messaggi.*;

/**
 * @author Marco
 * @version 1.0
 * @created 28-ott-2010 18:53:32
 */
public class UtilTest extends junit.framework.TestCase {

	public UtilTest(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param arg0
	 */
	public UtilTest(String arg0){
		super(arg0);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args){

	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void setUp()
	  throws Exception{
		super.setUp();
	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void tearDown()
	  throws Exception{
		super.tearDown();
	}

	

	public final void testComandoUserCmdToString(){
		String result = Util.comandoUserCmdToString(new ComandoUserCmd(ComandiUserCmd.accendi, "e1"));
		assertEquals("accendi;e1",result);
		result = Util.comandoUserCmdToString(new ComandoUserCmd(ComandiUserCmd.spegni, "e1"));
		assertEquals("spegni;e1",result);
		result = Util.comandoUserCmdToString(new ComandoUserCmd(ComandiUserCmd.connetti, ""));
		assertEquals("connetti;",result);
		result = Util.comandoUserCmdToString(new ComandoUserCmd(ComandiUserCmd.disconnetti, ""));
		assertEquals("disconnetti;",result);

	}

	public final void testDatiSensoreToString(){
		String result = Util.datiSensoreToString(new DatiSensore("e1", 60));
		assertEquals("e1;60",result);

	}

	public final void testStatusToString(){
		// creazione report elettrodomestici (solo 3)
		Vector <IReportElettrodomestico> reports = new Vector<IReportElettrodomestico>();
		reports.add( new ReportElettrodomestico("e1", StatoElettrodomestico.spento, 0));
		reports.add( new ReportElettrodomestico("e2", StatoElettrodomestico.esercizio, 60));
		reports.add( new ReportElettrodomestico("e3", StatoElettrodomestico.avvio, 120));
		Status status = new Status("hello,world!", reports, 300);
		String result = Util.statutsToString(status);
		assertEquals("hello,world!;180;300\ne1;spento;0\ne2;esercizio;60\ne3;avvio;120",result);
		assertEquals(status, Util.stringToStatus(result));

	}

	public final void testStringTocomandoUserCmd(){
		IComandoUserCmd expected = new ComandoUserCmd(ComandiUserCmd.spegni,"e1");
		IComandoUserCmd result = Util.stringToComandoUserCmd("spegni;e1");
		assertEquals(expected,result);
		

	}

	public final void testStringToDatiSensore(){
		IDatiSensore expected = new DatiSensore("e1", 180);
		IDatiSensore result = Util.stringToDatiSensore("e1;180");
		assertEquals(expected,result);

	}

	public final void testStringToStatus(){
		Vector <IReportElettrodomestico> reports = new Vector<IReportElettrodomestico>();
		reports.add( new ReportElettrodomestico("e1", StatoElettrodomestico.spento, 0));
		reports.add( new ReportElettrodomestico("e2", StatoElettrodomestico.esercizio, 60));
		reports.add( new ReportElettrodomestico("e3", StatoElettrodomestico.avvio, 120));
		IStatus expected = new Status("hello,world!", reports, 300);
		IStatus result = Util.stringToStatus("hello,world!;180;300\ne1;spento;0\ne2;esercizio;60\ne3;avvio;120");
		assertEquals(expected,result);
		

	}
}//end UtiltyTest