package test.elettrodomestico;
import java.util.Date;

import Edi.elettrodomestico.*;

/**
 * @author Marco
 * @version 1.0
 * @created 28-ott-2010 18:52:25
 */
/*
 * visto che ci sono solo getters e setters, creo la rappresentazione di un elettrodomestico
 * acceso, e controllo che i parametri inseriti nel costruttore siano quelli che poi
 * vengono recuperati dai getters
 */
public class RappresentazioneElettrodomesticoTest extends junit.framework.TestCase {
	
	private RappresentazioneElettrodomestico rappresentazioneElettrodomestico;
	private String idElettrodomestico = "e1";
	private int consumo = 30;
	private String idInterruttore = "i1";
	private Date oraAccensione = new Date ();
	private StatoElettrodomestico statoElettrodomestico = StatoElettrodomestico.esercizio;

	public RappresentazioneElettrodomesticoTest(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param arg0
	 */
	public RappresentazioneElettrodomesticoTest(String arg0){
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
		this.rappresentazioneElettrodomestico= new RappresentazioneElettrodomestico(idElettrodomestico, statoElettrodomestico , consumo, idInterruttore, oraAccensione);
	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void tearDown()
	  throws Exception{
		super.tearDown();
	}

	public final void testGetIdElettrodomestico(){
		assertEquals(idElettrodomestico, rappresentazioneElettrodomestico.getId());

	}
	public final void testGetStato(){
		assertEquals(statoElettrodomestico, rappresentazioneElettrodomestico.getStato());

	}
	public final void testGetConsumo(){
		assertEquals(consumo, rappresentazioneElettrodomestico.getConsumo());
	}
	public final void testGetIdInterruttore(){
		assertEquals(idInterruttore, rappresentazioneElettrodomestico.getIdInterruttore());

	}
	public final void testSetStato(){
		rappresentazioneElettrodomestico.setStato(StatoElettrodomestico.disattivato);
		assertEquals(StatoElettrodomestico.disattivato, rappresentazioneElettrodomestico.getStato());
	}
	public final void testSetIdInterruttore(){
		rappresentazioneElettrodomestico.setIdInterruttore("i2");
		assertEquals("i2", rappresentazioneElettrodomestico.getIdInterruttore());
	}
	public final void testSetOraAccensione(){
		rappresentazioneElettrodomestico.setOraAccensione(new Date());
		assertEquals(System.currentTimeMillis(), rappresentazioneElettrodomestico.getOraAccensione().getTime(),20);
	}
}//end RappresentazioneElettrodomesticoTest