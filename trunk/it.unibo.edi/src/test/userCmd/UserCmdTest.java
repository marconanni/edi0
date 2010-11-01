package test.userCmd;
import java.util.Vector;

import Edi.elettrodomestico.StatoElettrodomestico;
import Edi.messaggi.ComandiUserCmd;
import Edi.messaggi.ComandoUserCmd;
import Edi.messaggi.IReportElettrodomestico;
import Edi.messaggi.ReportElettrodomestico;
import Edi.messaggi.Status;
import Edi.userCmd.*;

/**
 * @author Marco
 * @version 1.0
 * @created 28-ott-2010 18:55:24
 */
public class UserCmdTest extends junit.framework.TestCase {
	
	private UserCmd userCmd ;

	public UserCmdTest(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param arg0
	 */
	public UserCmdTest(String arg0){
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
		this.userCmd= (UserCmd) UserCmd.getInstance();
	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void tearDown()
	  throws Exception{
		super.tearDown();
	}
	
	public final void testConnetti(){
		ComandoUserCmd expected = new ComandoUserCmd(ComandiUserCmd.connetti, "");
		assertEquals(expected, userCmd.connettiFT());
	}
	public final void testDisconnetti(){
		userCmd.connettiFT();
		ComandoUserCmd expected = new ComandoUserCmd(ComandiUserCmd.disconnetti, "");
		assertEquals(expected, userCmd.disconnettiFT());
	}
	public final void testAccendiElettrodomestico(){
		userCmd.connettiFT();
		ComandoUserCmd expected = new ComandoUserCmd(ComandiUserCmd.accendi, "e1");
		assertEquals(expected, userCmd.accendiElettrodomesticoFT("e1"));
	}
	public final void testSpegniElettrodomestico(){
		userCmd.connettiFT();
		ComandoUserCmd expected = new ComandoUserCmd(ComandiUserCmd.spegni, "e1");
		assertEquals(expected, userCmd.spegniElettrodomesticoFT("e1"));
	}
	public final void testIsConnesso(){
		userCmd.connettiFT();
		assertTrue(userCmd.isConnesso());
		userCmd.disconnettiFT();
		assertFalse(userCmd.isConnesso());
	}
	public final void testDoJob(){
		// creo un nuovo status da inviare a usercmd
		userCmd.connettiFT();
		Vector <IReportElettrodomestico> reports = new Vector<IReportElettrodomestico>();
		reports.add( new ReportElettrodomestico("e1", StatoElettrodomestico.spento, 0));
		reports.add( new ReportElettrodomestico("e2", StatoElettrodomestico.esercizio, 60));
		reports.add( new ReportElettrodomestico("e3", StatoElettrodomestico.avvio, 120));
		Status status = new Status("hello,world!", reports, 300);
		this.userCmd.doJobFT(Edi.messaggi.Util.statutsToString(status));
		assertEquals(status, userCmd.getStatus());
	}
}//end UserCmdTest