package test.elettrodomestico;


import Edi.elettrodomestico.*;

/**
 * @author Marco
 * @version 1.0
 * @created 28-ott-2010 18:52:05
 */
public class ElettrodomesticoTest extends junit.framework.TestCase {

	private Elettrodomestico elettrodomestico;
	private int consumoAvvio =20;
	private int consumoEsercizio=10;
	
	public ElettrodomesticoTest(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param arg0
	 */
	public ElettrodomesticoTest(String arg0){
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
		this.elettrodomestico = new Elettrodomestico(StatoElettrodomestico.spento, "e1", consumoEsercizio, consumoAvvio, null);
	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void tearDown()
	  throws Exception{
		super.tearDown();
	}
	
	

	public final void testAccendi(){
		elettrodomestico.accendi();
		long oraAccensione = System.currentTimeMillis();
		assertEquals(elettrodomestico.getConsumoAttuale(),this.consumoAvvio);
		assertTrue(elettrodomestico.getStato()== StatoElettrodomestico.avvio);
		assertEquals(elettrodomestico.getOraAccensione().getTime(),oraAccensione,50);// metto mezzo decimo di secondo di tolleranza, non posso pensare che le due ore siano esatte al millisecondo.
		
	}
	
//	public final void testEsercizio(){		
//		assertTrue(elettrodomestico.getStato()== StatoElettrodomestico.esercizio);
//		assertEquals(elettrodomestico.getConsumoAttuale(), 90);
//		assertTrue(System.currentTimeMillis()-6000>=elettrodomestico.getOraAccensione().getTime());
//	}
	
	public final void testSpegni(){
		elettrodomestico.spegni();
		assertTrue(elettrodomestico.getStato()== StatoElettrodomestico.spento);
		assertEquals(elettrodomestico.getConsumoAttuale(), 0);
		assertTrue (elettrodomestico.getOraAccensione()==null);
	}
	
	public final void testDisattiva(){
		elettrodomestico.disattiva();
		assertTrue(elettrodomestico.getStato()== StatoElettrodomestico.disattivato);
		assertEquals(elettrodomestico.getConsumoAttuale(), 0);
		assertTrue (elettrodomestico.getOraAccensione()==null);
	}
	public final void testRiattiva(){
		this.testAccendi();		
	}
	
	public final void testIsOn(){
		elettrodomestico.accendi();
		assertTrue(elettrodomestico.isOn());
	}
	public final void testIsAvvio(){
		elettrodomestico.accendi();
		assertTrue(elettrodomestico.isAvvio());		
		
	}
	public final void testIsOff(){
		elettrodomestico.disattiva();
		assertTrue(elettrodomestico.isOff());
		elettrodomestico.spegni();
		assertTrue(elettrodomestico.isOff());
		
	}
	public final void testIsSpento(){
		elettrodomestico.spegni();
		assertTrue(elettrodomestico.isSpento());
	}
	public final void testGetConsumoEsercizio(){
		assertEquals(elettrodomestico.getConsumoEsercizio(), consumoEsercizio);	
	}
	public final void testGetConsumoAvvio(){
		assertEquals(elettrodomestico.getConsumoAvvio(), consumoAvvio);	
	}
	/*
	 * non è possibile testare il consumo in fase di esercizio perchè non si possono aspettare 6 secondi
	 */
	public final void testGetConsumoAttuale(){
		elettrodomestico.accendi();
		assertEquals(consumoAvvio, elettrodomestico.getConsumoAttuale());
		elettrodomestico.disattiva();
		assertEquals(0, elettrodomestico.getConsumoAttuale());
		elettrodomestico.spegni();
		assertEquals(0, elettrodomestico.getConsumoAttuale());
	}
	public final void testGetId(){
		assertEquals("e1", elettrodomestico.getId());
		
	}
	public final void testGetOraAccensione(){
		elettrodomestico.accendi();
		assertEquals(System.currentTimeMillis(), elettrodomestico.getOraAccensione().getTime(),50);
		elettrodomestico.disattiva();
		assertNull(elettrodomestico.getOraAccensione());
		elettrodomestico.spegni();
	}
}//end ElettrodomesticoTest