package test.elettrodomestico;
import Edi.elettrodomestico.*;
/**
 * @author Marco
 * @version 1.0
 * @created 28-ott-2010 18:52:11
 */
public class ElettrodomesticoAltoConsumoTest extends junit.framework.TestCase {
	
	private ElettrodomesticoAltoConsumo elettrodomestico; 

	public ElettrodomesticoAltoConsumoTest(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param arg0
	 */
	public ElettrodomesticoAltoConsumoTest(String arg0){
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
		this.elettrodomestico = new ElettrodomesticoAltoConsumo(StatoElettrodomestico.spento, "e1",	 null);
		
	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void tearDown()
	  throws Exception{
		super.tearDown();
	}
	
	public final void testGetConsumoAvvio(){
		assertEquals(180, elettrodomestico.getConsumoAvvio());
	}
	
	public final void testGetConsumoEsercizio(){
		assertEquals(90, elettrodomestico.getConsumoEsercizio());
	}
		
	
}//end ElettrodomesticoAltoConsumoTest