package test.elettrodomestico;
import java.util.Date;

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
		this.elettrodomestico = new ElettrodomesticoAltoConsumo(StatoElettrodomestico.spento, "e1",	 new Date());
		
	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void tearDown()
	  throws Exception{
		super.tearDown();
	}
	/**
	 * non potendo fare una sleep quesato metodo controlla solo l'accensione e non 
	 * l'andare a regime, per fare questa prova, dopo aver aspettato sei secondi
	 * invocare il metodo testEsercizio.
	 */
	public final void testAccendi(){
		elettrodomestico.accendi();
		long oraAccensione = System.currentTimeMillis();
		assertEquals(elettrodomestico.getConsumoAttuale(),180);
		assertTrue(elettrodomestico.getStato()== StatoElettrodomestico.avvio);
		assertEquals(elettrodomestico.getOraAccensione().getTime(),oraAccensione,50);// metto mezzo decimo di secondo di tolleranza, non posso pensare che le due ore siano esatte al millisecondo.
		
	}
	
	public void testEsercizio(){
		assertTrue(elettrodomestico.getStato()== StatoElettrodomestico.esercizio);
		assertEquals(elettrodomestico.getConsumoAttuale(), 90);
		assertTrue(System.currentTimeMillis()-6000>=elettrodomestico.getOraAccensione().getTime());
	}
}//end ElettrodomesticoAltoConsumoTest