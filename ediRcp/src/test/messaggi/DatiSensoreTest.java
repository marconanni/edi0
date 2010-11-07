package test.messaggi;
import Edi.messaggi.*;

/**
 * @author Marco
 * @version 1.0
 * @created 28-ott-2010 18:53:25
 */
public class DatiSensoreTest extends junit.framework.TestCase {
	private String idElettrodomestico = "e1";
	private int consumo = 30;
	private DatiSensore datiSensore;

	public DatiSensoreTest(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param arg0
	 */
	public DatiSensoreTest(String arg0){
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
		this.datiSensore = new DatiSensore(idElettrodomestico, consumo);
	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void tearDown()
	  throws Exception{
		super.tearDown();
		this.datiSensore = new DatiSensore(idElettrodomestico, consumo);
	}

	public final void testGetId(){
		assertEquals(idElettrodomestico, datiSensore.getId());
	}
	public final void testGetConsumoAttuale(){
		assertEquals(consumo, datiSensore.getConsumoAttuale());
	}
}//end DatiSensoreTest