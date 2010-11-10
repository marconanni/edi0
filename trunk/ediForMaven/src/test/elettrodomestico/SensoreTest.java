package test.elettrodomestico;

import Edi.elettrodomestico.*;
import Edi.messaggi.Util;


/**
 * @author Marco
 * @version 1.0
 * @created 28-ott-2010 18:52:27
 * 
 * nota: testare la classe Sensore, in quanto un thread è estrememente difficile con le test units,
 * per questo il sensore viene construito, ma non avviato. è stato introdotto nella classe sensore
 * il metodo sendDataFT(ForTest), questo metodo restituisce la stringa che invierebbe il sensore tramite contact
 * è stato anche inserito il metodo doJobFT, questo replica una sola ripetizione del ciclo infinito del 
 * metodo dojob del sensore. nel metodo il "non fare nulla" è stato trasformato con il ritornare null.
 */
public class SensoreTest extends junit.framework.TestCase {
	
	private IElettrodomestico elettrodomestico;
	private Sensore sensore;
	private String idElettrodomestico = "e1";
	private int ConsumoAvvio=60;
	private int ConsumoEsercizio=30;
	

	public SensoreTest(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param arg0
	 */
	public SensoreTest(String arg0){
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
		elettrodomestico= new Elettrodomestico(StatoElettrodomestico.spento, idElettrodomestico, ConsumoEsercizio, ConsumoAvvio, null);
		sensore = new Sensore(500, elettrodomestico);
	
	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void tearDown()
	  throws Exception{
		super.tearDown();
	}
	
	public final void testSendData(){
		elettrodomestico.accendi();
		String data = sensore.sendDataFT();
		System.out.println(data);
		String idData = Util.stringToDatiSensore(data).getId();
		int consumoData =Util.stringToDatiSensore(data).getConsumoAttuale();
		assertEquals(elettrodomestico.getConsumoAttuale(), consumoData);
		assertEquals(elettrodomestico.getId(),idData);
	}
	
	public final void testDoJob(){
		elettrodomestico.spegni();
		String result = sensore.doJobFT();
		assertNull(result);
		
		elettrodomestico.accendi();
		String data = sensore.doJobFT();
		String idData = Util.stringToDatiSensore(data).getId();
		int consumoData =Util.stringToDatiSensore(data).getConsumoAttuale();
		assertEquals(elettrodomestico.getConsumoAttuale(), consumoData);
		assertEquals(elettrodomestico.getId(),idData);
	}

	
}//end SensoreTest