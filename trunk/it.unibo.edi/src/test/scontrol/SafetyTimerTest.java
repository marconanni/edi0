package test.scontrol;

import Edi.scontrol.*;

/**
 * @author Marco
 * @version 1.0
 * @created 28-ott-2010 18:54:50
 */
public class SafetyTimerTest extends junit.framework.TestCase implements IObserver {
		private SafetyTimer timer;
		private long eventTime = 2000;
		private String idElettriodomestico ="e1";
		private long currentTime;
		
		
	public SafetyTimerTest(){
		

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param arg0
	 */
	public SafetyTimerTest(String arg0){
		super(arg0);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		// test  manuale per il thread: creo un timer, mi metto all'ascolto e lo faccio partire:
		// poi controllo che il timeout arrivi per tempo, in un secondo tentativo lo resetto dopo mezzo secondo
		SafetyTimerTest test = new SafetyTimerTest();
		try {
			test.setUp();
			test.currentTime= System.currentTimeMillis();
			test.timer.addObserver(test);
			test.timer.avvia();
			// provo a cancellarmi prima che scatti il timer, non dovrei ricever nulla
			
			test.timer.removeObserver(test);
			System.out.println("nessuna comunicazione, tutto ok");
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void setUp()
	  throws Exception{
		super.setUp();
		timer = new SafetyTimer();
		timer.setEventTime(eventTime);
		timer.setIdElettrodomestico(idElettriodomestico);
		
	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void tearDown()
	  throws Exception{
		super.tearDown();
	}

	public final void testNotifyObservers(){
		timer.addObserver(this);
		timer.notifyObservers();
	}

	@Override
	public void update(IResettableTimer safetyTimer, String idElettrodomestico) {
		assertEquals(this.timer, safetyTimer);
		assertEquals (this.idElettriodomestico, idElettrodomestico);
		
	}
}//end SafetyTimerTest