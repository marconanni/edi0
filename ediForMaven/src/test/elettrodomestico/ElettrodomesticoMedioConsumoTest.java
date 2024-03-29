package test.elettrodomestico;

import Edi.elettrodomestico.*;


/**
 * @author Marco
 * @version 1.0
 * @created 28-ott-2010 18:52:22
 */
public class ElettrodomesticoMedioConsumoTest extends junit.framework.TestCase {
	
	private ElettrodomesticoMedioConsumo elettrodomestico;

	public ElettrodomesticoMedioConsumoTest(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param arg0
	 */
	public ElettrodomesticoMedioConsumoTest(String arg0){
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
		elettrodomestico =new ElettrodomesticoMedioConsumo(StatoElettrodomestico.spento, "e1", null);
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
		assertEquals(120, elettrodomestico.getConsumoAvvio());
	}
	
	public final void testGetConsumoEsercizio(){
		assertEquals(60, elettrodomestico.getConsumoEsercizio());
	}
	
}//end ElettrodomesticoMedioConsumoTest