package test.elettrodomestico;



import Edi.elettrodomestico.*;
/**
 * @author Marco
 * @version 1.0
 * @created 28-ott-2010 18:52:19
 */
public class ElettrodomesticoBassoConsumoTest extends junit.framework.TestCase {
	
	ElettrodomesticoBassoConsumo elettrodomestico;

	public ElettrodomesticoBassoConsumoTest(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param arg0
	 */
	public ElettrodomesticoBassoConsumoTest(String arg0){
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
		elettrodomestico = new ElettrodomesticoBassoConsumo(StatoElettrodomestico.spento, "e1", null);
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
		assertEquals(60, elettrodomestico.getConsumoAvvio());
	}
	
	public final void testGetConsumoEsercizio(){
		assertEquals(30, elettrodomestico.getConsumoEsercizio());
	}
	
}//end ElettrodomesticoBassoConsumoTest