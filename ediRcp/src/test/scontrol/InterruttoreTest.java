package test.scontrol;
import Edi.scontrol.*;
import Edi.elettrodomestico.Elettrodomestico;
import Edi.elettrodomestico.StatoElettrodomestico;

/**
 * @author Marco
 * @version 1.0
 * @created 28-ott-2010 18:54:47
 */
public class InterruttoreTest extends junit.framework.TestCase {
	private Interruttore interruttore;
	private Elettrodomestico elettrodomestico;

	public InterruttoreTest(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param arg0
	 */
	public InterruttoreTest(String arg0){
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
		this.elettrodomestico= new Elettrodomestico(StatoElettrodomestico.spento, "e1", 60, 120, null);
		this.interruttore = new Interruttore("i1");
		interruttore.setElettrodomesticoCollegato(elettrodomestico);
	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void tearDown()
	  throws Exception{
		super.tearDown();
	}

	public void testTurnOn(){
		this.interruttore.turnOn();
		assertTrue(interruttore.isOn());
		assertTrue(elettrodomestico.isOn());
		assertTrue(elettrodomestico.isAvvio());
	}
	public void testTurnOff(){
		this.testTurnOn();
		this.interruttore.turnOff();
		assertTrue(interruttore.isOff());
		assertFalse(interruttore.isOn());
		assertTrue(elettrodomestico.isSpento());
	}
	
	public void testIsOn(){
		interruttore.turnOn();
		assertTrue(interruttore.isOn());
	}
	public void testIsOff(){
		interruttore.turnOff();
		assertTrue(interruttore.isOff());
	}
	public void testIsCollegato(){
		interruttore.setElettrodomesticoCollegato(null);
		assertFalse(interruttore.isCollegato());
		interruttore.setElettrodomesticoCollegato(elettrodomestico);
		assertTrue(interruttore.isCollegato());
	}
	public void testGetId(){
		assertEquals("i1",interruttore.getId());
	}
	public void testGetIdElettrodomesticoCollegato(){
		assertEquals(elettrodomestico.getId(),interruttore.getIdElettrodomesticoCollegato());
	}
	public void testSetElettrodomesticoCollegato(){
		Elettrodomestico nuovoElettrodomestico = new Elettrodomestico(StatoElettrodomestico.spento, "e2", 60, 120, null);
		interruttore.setElettrodomesticoCollegato(nuovoElettrodomestico);
		assertEquals(nuovoElettrodomestico.getId(),interruttore.getIdElettrodomesticoCollegato());
		
	}
	
}//end InterruttoreTest