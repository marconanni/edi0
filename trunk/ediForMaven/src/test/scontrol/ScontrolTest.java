package test.scontrol;

import java.util.Date;
import java.util.Vector;

import Edi.scontrol.*;
import Edi.elettrodomestico.*;
import Edi.messaggi.*;

/**
 * @author Marco
 * @version 1.0
 * @created 28-ott-2010 18:54:53
 */
public class ScontrolTest extends junit.framework.TestCase {
	private Scontrol scontrol;
	private Vector<IRappresentazioneElettrodomestico> elettrodomestici;
	private Vector <IInterruttore> interruttori;
	private int soglia = 150;
	private long intervalloTimers = 30000;

	public ScontrolTest(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param arg0
	 */
	public ScontrolTest(String arg0){
		super(arg0);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args){

	}

	/**
	 * creo Scontrol con 3 elettrodomestici a basso consumo, tre a medio e tre ad alto, come 
	 * nello scenario di riferimento. La soglia di consumo è di 150 e l'intervallo da aspettare
	 * perchè scattino i timeout è di 30 secondi ( non voglio che scatti niente)
	 * @exception Exception
	 */
	protected void setUp()
	  throws Exception{
		super.setUp();
		// creo gli interruttori;
		interruttori = new Vector<IInterruttore>();
		interruttori.add(new Interruttore("i1"));
		interruttori.add(new Interruttore("i2"));
		interruttori.add(new Interruttore("i3"));
		interruttori.add(new Interruttore("i4"));
		interruttori.add(new Interruttore("i5"));
		interruttori.add(new Interruttore("i6"));
		interruttori.add(new Interruttore("i7"));
		interruttori.add(new Interruttore("i8"));
		interruttori.add(new Interruttore("i9"));
		// associo ad ogni interruttore il suo elettrodomestico ( nota che questi elettrodomestici non hanno
		// nessun sensore ad essi collegati.
		interruttori.get(0).setElettrodomesticoCollegato(new ElettrodomesticoBassoConsumo(StatoElettrodomestico.spento, "e1", null));
		interruttori.get(1).setElettrodomesticoCollegato(new ElettrodomesticoBassoConsumo(StatoElettrodomestico.spento, "e2", null));
		interruttori.get(2).setElettrodomesticoCollegato(new ElettrodomesticoBassoConsumo(StatoElettrodomestico.spento, "e3", null));
		
		interruttori.get(3).setElettrodomesticoCollegato(new ElettrodomesticoMedioConsumo(StatoElettrodomestico.spento, "e4", null));
		interruttori.get(4).setElettrodomesticoCollegato(new ElettrodomesticoMedioConsumo(StatoElettrodomestico.spento, "e5", null));
		interruttori.get(5).setElettrodomesticoCollegato(new ElettrodomesticoMedioConsumo(StatoElettrodomestico.spento, "e6", null));
		
		interruttori.get(6).setElettrodomesticoCollegato(new ElettrodomesticoAltoConsumo(StatoElettrodomestico.spento, "e7", null));
		interruttori.get(7).setElettrodomesticoCollegato(new ElettrodomesticoMedioConsumo(StatoElettrodomestico.spento, "e8", null));
		interruttori.get(8).setElettrodomesticoCollegato(new ElettrodomesticoMedioConsumo(StatoElettrodomestico.spento, "e9", null));
		// creo le rappresentazioni per gli elettrodomestici
		elettrodomestici= new Vector<IRappresentazioneElettrodomestico>();
		elettrodomestici.add(new RappresentazioneElettrodomestico("e1", StatoElettrodomestico.spento, 0, "i1", null));
		elettrodomestici.add(new RappresentazioneElettrodomestico("e2", StatoElettrodomestico.spento, 0, "i2", null));
		elettrodomestici.add(new RappresentazioneElettrodomestico("e3", StatoElettrodomestico.spento, 0, "i3", null));
		elettrodomestici.add(new RappresentazioneElettrodomestico("e4", StatoElettrodomestico.spento, 0, "i4", null));
		elettrodomestici.add(new RappresentazioneElettrodomestico("e5", StatoElettrodomestico.spento, 0, "i5", null));
		elettrodomestici.add(new RappresentazioneElettrodomestico("e6", StatoElettrodomestico.spento, 0, "i6", null));
		elettrodomestici.add(new RappresentazioneElettrodomestico("e7", StatoElettrodomestico.spento, 0, "i7", null));
		elettrodomestici.add(new RappresentazioneElettrodomestico("e8", StatoElettrodomestico.spento, 0, "i8", null));
		elettrodomestici.add(new RappresentazioneElettrodomestico("e9", StatoElettrodomestico.spento, 0, "i9", null));
		// creo Scontrol
		scontrol = (Scontrol) Scontrol.getInstance(elettrodomestici, soglia, intervalloTimers, interruttori);
		
		
	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void tearDown()
	  throws Exception{
		super.tearDown();
	}
	
	/*
	 *  TODO NOTA: nel corso dei test seguenti si mantencono gli elettrodomestici dal 1 al 3 a basso consumo,
	 *  dal 4 al 6 a medio e da 7 a 9 ad alto. Ciò è fatto per chiarezza e per pulizia concettuale, in realtà scontrol
	 *  non associa nulla al nome dell'elettrodomestico o a quello dell'interruttore collegato; prova di ciò sono i
	 *  test sullo spegnimento preventivo dove gli elettrodomestici dall'1 al 5 sono tutti a basso consumo.
	 */
	
	/**
	 * questo test simula la accensione di un solo elettrodomestico, quindi è impossibile che ci sia il superamento
	 * della soglia preventiva.
	 */
	public final void testAccensioneSemplice(){
		scontrol.accendiElettrodomestico("e8");
		assertTrue(scontrol.getInterruttori().get("i8").isOn());
		assertTrue(scontrol.getElettrodomestici().get("e8").getStato()==StatoElettrodomestico.avvio);
		assertTrue(scontrol.getElettrodomestici().get("e8").getConsumo()==0);
		assertEquals(scontrol.getElettrodomestici().get("e8").getOraAccensione().getTime(),System.currentTimeMillis(),50);
		// socontrol non sa i consumi, se non gli arrivano dai sensori
		assertEquals(0, scontrol.calcolaConsumoAttualeComplessivoFT());
		assertEquals(0, scontrol.calcolaConsumoAttualeFT());
	}
	
	// TODO: aggiungere qui accensione con spegnimento preventivo e disattivazione di un elettrdomestico a seguito dell'entrata in esercizio di un elettrodomestico con consumi minori.
	
	public final void testAccensioneConSpegnimentoPreventivo(){
		// metto cinque elettrodomestici a basso consumo accesi
		scontrol.getElettrodomestici().remove("e1");
		scontrol.getElettrodomestici().put("e1", new RappresentazioneElettrodomestico("e1", StatoElettrodomestico.esercizio, 30, "i1", new Date(1))); 
		scontrol.getElettrodomestici().remove("e2");
		scontrol.getElettrodomestici().put("e2", new RappresentazioneElettrodomestico("e2", StatoElettrodomestico.esercizio, 30, "i2", new Date(1))); 
		scontrol.getElettrodomestici().remove("e3");
		scontrol.getElettrodomestici().put("e3", new RappresentazioneElettrodomestico("e3", StatoElettrodomestico.esercizio, 30, "i3", new Date(1))); 
		scontrol.getElettrodomestici().remove("e4");
		scontrol.getElettrodomestici().put("e4", new RappresentazioneElettrodomestico("e4", StatoElettrodomestico.esercizio, 30, "i4", new Date(1))); 
		scontrol.getElettrodomestici().remove("e5");
		scontrol.getElettrodomestici().put("e5", new RappresentazioneElettrodomestico("e5", StatoElettrodomestico.esercizio, 30, "i5", new Date(1)));
		// prova ad accendere un altro elettrodomestico, ma in realtà qualcosa dovrebbe andare storto.
		

	}
	
	/**
	 * c'è solo un elettrodomestico acceso e decido di spegnerlo.
	 */
	public final void testSpegnimentoSemplice(){
		scontrol.getElettrodomestici().remove("e1");
		scontrol.getElettrodomestici().put("e1", new RappresentazioneElettrodomestico("e1", StatoElettrodomestico.esercizio, 30, "i1", new Date(2010, 10, 10)));
		scontrol.getInterruttori().get("i1").turnOn();
		scontrol.spegniElettrodomestico("e1");
		assertTrue(scontrol.getElettrodomestici().get("e1").getStato()==StatoElettrodomestico.spento);
		assertEquals(0,scontrol.getElettrodomestici().get("e1").getConsumo());
		assertTrue(scontrol.getInterruttori().get("i1").isOff());
		assertNull(scontrol.getElettrodomestici().get("e1").getOraAccensione());
		assertEquals(0, scontrol.calcolaConsumoAttualeComplessivoFT());
		assertEquals(0, scontrol.calcolaConsumoAttualeFT());
		
	}
	
	// TODO aggiungi uno spegnimento con riattivazione
	
	/**
	 * questo test controlla la funzionalità di scontrol che stabilisce se è necessario impdire a priori l'accensione di 
	 * un elettrodomestico. questo può apparire necessario solo quando sono accesi solo degli elettrodomestici a basso consumo
	 * e l'accensione di un ulteriore elettrodomestico(anche a basso consumo) porterebbe al superamento della soglia
	 * di consumo.
	 */
	public final void testValutaNecessitàSpegnimentoPreventivo(){
		assertFalse(scontrol.valutaNecessitàSpegnimentoPreventivoFT());
		// perchè ci sia lo spegnimento preventivo ci devono essere 5 elettrodomestici a basso consumo già accesi
		scontrol.getElettrodomestici().remove("e1");
		scontrol.getElettrodomestici().put("e1", new RappresentazioneElettrodomestico("e1", StatoElettrodomestico.esercizio, 30, "i1", new Date(1))); 
		scontrol.getElettrodomestici().remove("e2");
		scontrol.getElettrodomestici().put("e2", new RappresentazioneElettrodomestico("e2", StatoElettrodomestico.esercizio, 30, "i2", new Date(1))); 
		scontrol.getElettrodomestici().remove("e3");
		scontrol.getElettrodomestici().put("e3", new RappresentazioneElettrodomestico("e3", StatoElettrodomestico.esercizio, 30, "i3", new Date(1))); 
		scontrol.getElettrodomestici().remove("e4");
		scontrol.getElettrodomestici().put("e4", new RappresentazioneElettrodomestico("e4", StatoElettrodomestico.esercizio, 30, "i4", new Date(1))); 
		scontrol.getElettrodomestici().remove("e5");
		scontrol.getElettrodomestici().put("e5", new RappresentazioneElettrodomestico("e5", StatoElettrodomestico.esercizio, 30, "i5", new Date(1)));
		
		
		assertTrue(scontrol.valutaNecessitàSpegnimentoPreventivoFT());



	}
	
	/**
	 * questo test controlla la funzione di Scontrol che stabilisce quando è necessario disattivare un elettrodomestico,
	 * ossia quando il consumo attuale è superiore alla soglia di consumo.
	 */
	public final void testValutaNecessitàDisattivazione(){
		assertFalse(scontrol.valutaNecessitàDisattivazioneFT());
		// perchè ci sia la disattivazione il consumno deve essere superiore a 150, due elettrodomestici ad alto consumo son più che sufficienti
		scontrol.getElettrodomestici().remove("e9");
		scontrol.getElettrodomestici().put("e9", new RappresentazioneElettrodomestico("e9", StatoElettrodomestico.esercizio, 90, "i9", new Date(1))); 
		scontrol.getElettrodomestici().remove("e8");
		scontrol.getElettrodomestici().put("e8", new RappresentazioneElettrodomestico("e8", StatoElettrodomestico.esercizio, 90, "i8", new Date(1)));
		assertEquals(180, scontrol.calcolaConsumoAttualeFT());
		assertTrue(scontrol.valutaNecessitàDisattivazioneFT());
		
	}
	
	/**
	 * questo test controlla la funzione di Scontrol che stabilisce quando è necessario riattivare un elettrodomestico,
	 * ciò capita quando tra gli elettrodomestici disattivati ce ne è uno con un consumo pari o sinferiore alla
	 * differenza tra l consumo attuale e la soglia di consumo
	 * 
	 */
	public final void testValutaNecessitàRiattivazione(){
		assertFalse(scontrol.valutaNecessitàRiattivazioneFT()); // se non c'è un elettrodomestico disattivato, non c'è nulla da riattivare
		
		scontrol.getElettrodomestici().remove("e4");
		scontrol.getElettrodomestici().put("e4", new RappresentazioneElettrodomestico("e4", StatoElettrodomestico.esercizio, 60, "i4", new Date(1))); 
		scontrol.getElettrodomestici().remove("e5");
		scontrol.getElettrodomestici().put("e5", new RappresentazioneElettrodomestico("e5", StatoElettrodomestico.esercizio, 60, "i5", new Date(1)));
		assertEquals(120, scontrol.calcolaConsumoAttualeFT());
		scontrol.getElettrodomestici().remove("e9");
		scontrol.getElettrodomestici().put("e9", new RappresentazioneElettrodomestico("e9", StatoElettrodomestico.disattivato, 90, "i9", new Date(1)));
		// un elettrodomestico ad alto consumo non ci sta nei 30 di margine
		assertFalse(scontrol.valutaNecessitàRiattivazioneFT());
		// ma uno a basso consumo sì
		scontrol.getElettrodomestici().remove("e1");
		scontrol.getElettrodomestici().put("e1", new RappresentazioneElettrodomestico("e1", StatoElettrodomestico.disattivato, 30, "i1", new Date(1)));
		assertTrue(scontrol.valutaNecessitàRiattivazioneFT());

	}
	
	/**
	 * questo test controlla la funzione di Scontrol che stabilisce quale elettrodomestico disattivare
	 * questo è l'elettrodomestico acceso per ultimo tra quelli che consumano di più
	 * 
	 */
	public final void testStabilisciElettrodomesticoDaDisattivare(){
		
		scontrol.getElettrodomestici().remove("e4");
		scontrol.getElettrodomestici().put("e4", new RappresentazioneElettrodomestico("e4", StatoElettrodomestico.esercizio, 60, "i4", new Date(1))); 
		scontrol.getElettrodomestici().remove("e8");
		scontrol.getElettrodomestici().put("e8", new RappresentazioneElettrodomestico("e8", StatoElettrodomestico.esercizio, 90, "i8", new Date())); // acceso adesso
		scontrol.getElettrodomestici().remove("e9");
		scontrol.getElettrodomestici().put("e9", new RappresentazioneElettrodomestico("e8", StatoElettrodomestico.esercizio, 90, "i8", new Date(1))); // acceso nel 1970
		assertEquals("e8",scontrol.stabilisciElettrodomesticoDaDisattivareFT());		
		
	}
	
	/**
	 * questo test controlla la funzione di Scontrol che stabilisce quale elettrodomestico riattivare
	 * questo è l'elettrodomestico acceso per primo tra quelli che consumano di meno
	 * 
	 */
	public final void testStabilisciElettrodomesticoDaRiattivare(){
		
		scontrol.getElettrodomestici().remove("e4");
		scontrol.getElettrodomestici().put("e4", new RappresentazioneElettrodomestico("e4", StatoElettrodomestico.disattivato, 60, "i4", new Date(1))); // acceso nel 1970
		scontrol.getElettrodomestici().remove("e5");
		scontrol.getElettrodomestici().put("e5", new RappresentazioneElettrodomestico("e5", StatoElettrodomestico.disattivato, 60, "i5", new Date())); // acceso adesso
		scontrol.getElettrodomestici().remove("e9");
		scontrol.getElettrodomestici().put("e9", new RappresentazioneElettrodomestico("e9", StatoElettrodomestico.disattivato, 90, "i9", new Date(1))); 
		assertEquals("e4",scontrol.stabilisciElettrodomesticoDaRiattivareFT());		
		
	}
	
	/**
	 * testa il metodo di Scontrol che permette di disattivare un elettrodomestico
	 */
	public final void testDisattivaElettrodomestico(){
		scontrol.getElettrodomestici().remove("e4");
		scontrol.getElettrodomestici().put("e4", new RappresentazioneElettrodomestico("e4", StatoElettrodomestico.esercizio, 60, "i4", new Date(1))); // acceso nel 1970
		scontrol.getInterruttori().get("i4").turnOn();
		scontrol.disattivaElettrodomesticoFT("e4");
		assertTrue(scontrol.getElettrodomestici().get("e4").getStato()==StatoElettrodomestico.disattivato);
		assertEquals(60,scontrol.getElettrodomestici().get("e4").getConsumo());
		assertTrue(scontrol.getInterruttori().get("i4").isOff());
		assertEquals(0,scontrol.calcolaConsumoAttualeComplessivoFT());
		assertEquals(0,scontrol.calcolaConsumoAttualeFT());
		
	}
	
	/**
	 * testa il metodo di Scontrol che permette di disattivare un elettrodomestico
	 */
	public final void testRiattivaElettrodomestico(){
		
		scontrol.getElettrodomestici().remove("e4");
		scontrol.getElettrodomestici().put("e4", new RappresentazioneElettrodomestico("e4", StatoElettrodomestico.disattivato, 60, "i4", new Date(1))); // acceso nel 1970
		scontrol.riattivaElettrodomesticoFT("e4");
		assertTrue(scontrol.getElettrodomestici().get("e4").getStato()==StatoElettrodomestico.avvio);
		assertTrue(scontrol.getInterruttori().get("i4").isOn());
		
		
	}

	/**
	 * metodo che verifica la ricezione di un comando di connessione e verifica la corrispondenza dello stato inviato con quello del sistema
	 */
	public final void testComandoConnessione(){
		IStatus statusRicevuto =scontrol.riceviEdElaboraComandoUserCmdFT(Util.comandoUserCmdToString(new ComandoUserCmd(ComandiUserCmd.connetti, ""))) ;
		this.verificaStatus(statusRicevuto);
		
	}
	
	/**
	 * metodo che verifica la ricezione di un comando di accensione.
	 * nello scenario preparato non succede altro che l'accensione dell'elettrodomestico indicato 
	 */
	public final void testComandoAccensioneSemplice(){
		IStatus statusRicevuto =scontrol.riceviEdElaboraComandoUserCmdFT(Util.comandoUserCmdToString(new ComandoUserCmd(ComandiUserCmd.accendi, "e8"))) ;
		this.verificaStatus(statusRicevuto);
		assertTrue(scontrol.getInterruttori().get("i8").isOn());
		assertTrue(scontrol.getElettrodomestici().get("e8").getStato()==StatoElettrodomestico.avvio);
		assertTrue(scontrol.getElettrodomestici().get("e8").getConsumo()==0);
		assertEquals(scontrol.getElettrodomestici().get("e8").getOraAccensione().getTime(),System.currentTimeMillis(),50);
		// socontrol non sa i consumi, se non gli arrivano dai sensori
		assertEquals(0, scontrol.calcolaConsumoAttualeComplessivoFT());
		assertEquals(0, scontrol.calcolaConsumoAttualeFT());
		
	}
	
	/**
	 * metodo che verifica la ricezione di un comando di accensione.
	 * nello scenario preparato non è possibile accendere l'elettrodomestico, c'è uno spengimento preventivo
	 */
	public final void testComandoAccensioneConSpegnimentoPreventivo(){
		// perchè ci sia lo spegnimento preventivo ci devono essere 5 elettrodomestici a basso consumo già accesi
		scontrol.getElettrodomestici().remove("e1");
		scontrol.getElettrodomestici().put("e1", new RappresentazioneElettrodomestico("e1", StatoElettrodomestico.esercizio, 30, "i1", new Date(1))); 
		scontrol.getElettrodomestici().remove("e2");
		scontrol.getElettrodomestici().put("e2", new RappresentazioneElettrodomestico("e2", StatoElettrodomestico.esercizio, 30, "i2", new Date(1))); 
		scontrol.getElettrodomestici().remove("e3");
		scontrol.getElettrodomestici().put("e3", new RappresentazioneElettrodomestico("e3", StatoElettrodomestico.esercizio, 30, "i3", new Date(1))); 
		scontrol.getElettrodomestici().remove("e4");
		scontrol.getElettrodomestici().put("e4", new RappresentazioneElettrodomestico("e4", StatoElettrodomestico.esercizio, 30, "i4", new Date(1))); 
		scontrol.getElettrodomestici().remove("e5");
		scontrol.getElettrodomestici().put("e5", new RappresentazioneElettrodomestico("e5", StatoElettrodomestico.esercizio, 30, "i5", new Date(1)));		
		
		IStatus statusRicevuto =scontrol.riceviEdElaboraComandoUserCmdFT(Util.comandoUserCmdToString(new ComandoUserCmd(ComandiUserCmd.accendi, "e8"))) ;
		this.verificaStatus(statusRicevuto);
		assertTrue(scontrol.getInterruttori().get("i8").isOff());
		assertTrue(scontrol.getElettrodomestici().get("e8").getStato()==StatoElettrodomestico.spento);
		assertTrue(scontrol.getElettrodomestici().get("e8").getConsumo()==0);
		assertNull(scontrol.getElettrodomestici().get("e8").getOraAccensione());
		assertEquals(150, scontrol.calcolaConsumoAttualeComplessivoFT());
		assertEquals(150, scontrol.calcolaConsumoAttualeFT());
		
	}
	/**
	 * verifica la ricezione di un comando di spegnimento.
	 * nello scenario si spegne il solo elettrodomestico acceso e null'altro.
	 */
	public final void testComandoSpengimentoSemplice(){
		scontrol.getElettrodomestici().remove("e1");
		scontrol.getElettrodomestici().put("e1", new RappresentazioneElettrodomestico("e1", StatoElettrodomestico.esercizio, 30, "i1", new Date(1)));
		scontrol.getInterruttori().get("i1").turnOn();
		
		IStatus statusRicevuto =scontrol.riceviEdElaboraComandoUserCmdFT(Util.comandoUserCmdToString(new ComandoUserCmd(ComandiUserCmd.spegni, "e1"))) ;
		this.verificaStatus(statusRicevuto);
		
		assertTrue(scontrol.getElettrodomestici().get("e1").getStato()==StatoElettrodomestico.spento);
		assertEquals(0,scontrol.getElettrodomestici().get("e1").getConsumo());
		assertTrue(scontrol.getInterruttori().get("i1").isOff());
		assertNull(scontrol.getElettrodomestici().get("e1").getOraAccensione());
		assertEquals(0, scontrol.calcolaConsumoAttualeComplessivoFT());
		assertEquals(0, scontrol.calcolaConsumoAttualeFT());
		
		
	}
	
	/**
	 *  * verifica la ricezione di un comando di spegnimento.
	 * nello scenario si spegne un elettrodomestico e lo spegnimento provoca la riaccensione
	 * di un elettrodomestico disattivato
	 */
	public final void testComandoSpengimentoConRiattivazione(){
		/*
		 *  sono accesi un eletttrodomestico a basso consumo e uno ad medio (consumo Totale 90), è disattivato un elettrodomestico
		 *  ad alto consumo. lo spegnimento dell'elettrodomestico a basso consumo provoca la riattivazione di quello ad alto:
		 *  
		 */
		scontrol.getElettrodomestici().remove("e1");
		scontrol.getElettrodomestici().put("e1", new RappresentazioneElettrodomestico("e1", StatoElettrodomestico.esercizio, 30, "i1", new Date(1))); // acceso nel 1970
		scontrol.getElettrodomestici().remove("e4");
		scontrol.getElettrodomestici().put("e4", new RappresentazioneElettrodomestico("e4", StatoElettrodomestico.esercizio, 60, "i4", new Date(1))); // acceso nel 1970

		scontrol.getElettrodomestici().remove("e7");
		scontrol.getElettrodomestici().put("e7", new RappresentazioneElettrodomestico("e7", StatoElettrodomestico.disattivato, 90, "i7", new Date(1))); // acceso nel 1970
		
		IStatus statusRicevuto =scontrol.riceviEdElaboraComandoUserCmdFT(Util.comandoUserCmdToString(new ComandoUserCmd(ComandiUserCmd.spegni, "e1"))) ;
		this.verificaStatus(statusRicevuto);
		
		// e1 è spento
		assertTrue(scontrol.getElettrodomestici().get("e1").getStato()==StatoElettrodomestico.spento);
		assertEquals(0,scontrol.getElettrodomestici().get("e1").getConsumo());
		assertTrue(scontrol.getInterruttori().get("i1").isOff());
		assertNull(scontrol.getElettrodomestici().get("e1").getOraAccensione());
		
		// e7 è acceso
		assertTrue(scontrol.getInterruttori().get("i7").isOn());
		assertTrue(scontrol.getElettrodomestici().get("e7").getStato()==StatoElettrodomestico.avvio);
		assertEquals(scontrol.getElettrodomestici().get("e7").getOraAccensione().getTime(),System.currentTimeMillis(),50);
		
		assertEquals(60, scontrol.calcolaConsumoAttualeFT());
				
	}
	
	public final void testRiceviComandoDisconnessione(){
		IStatus statusRicevuto =scontrol.riceviEdElaboraComandoUserCmdFT(Util.comandoUserCmdToString(new ComandoUserCmd(ComandiUserCmd.disconnetti, ""))) ;
		this.verificaStatus(statusRicevuto);
	}
	
	/**
	 * metodo che serve per testare ciò che accade quando c'è la ricezione dei dati di consumo
	 * in questo scenario si aggiorna il consumo di un elettrodomestico appana acceso.
	 */
	public final void testRiceviDatiSensoreSemplice(){
		scontrol.getElettrodomestici().remove("e1");
		scontrol.getElettrodomestici().put("e1", new RappresentazioneElettrodomestico("e1", StatoElettrodomestico.avvio, 0, "i1", new Date())); // acceso nel 1970
		
		scontrol.riceviDatiSensoreFT(Util.datiSensoreToString(new DatiSensore("e1", 60)));
		
		assertEquals(60, scontrol.getElettrodomestici().get("e1").getConsumo());
		assertEquals(60, scontrol.calcolaConsumoAttualeComplessivoFT());
		assertEquals(0, scontrol.calcolaConsumoAttualeFT());
		
		
	}
	

	/**
	 * metodo che serve per testare ciò che accade quando c'è la ricezione dei dati di consumo
	 * in questo scenario arrivano dei dati di consumo per un elettrodomestico in avvio
	 *  che son la metà del consumo precedente ciò fa capire al sistema che l'elettrodomestico
	 *  è entrato nella fase di esercizio e anche il suo consumo va calcolato per garantire il 
	 *  rispetto della soglia di consumo
	 */
	public final void testRiceviDatiSensoreInizioFaseEsercizio(){
		scontrol.getElettrodomestici().remove("e1");
		scontrol.getElettrodomestici().put("e1", new RappresentazioneElettrodomestico("e1", StatoElettrodomestico.avvio, 0, "i1", new Date())); 
		
		scontrol.riceviDatiSensoreFT(Util.datiSensoreToString(new DatiSensore("e1", 60)));
		
		scontrol.riceviDatiSensoreFT(Util.datiSensoreToString(new DatiSensore("e1", 30)));
		
		assertTrue (scontrol.getElettrodomestici().get("e1").getStato()== StatoElettrodomestico.esercizio);
		assertEquals(30, scontrol.getElettrodomestici().get("e1").getConsumo());
		assertEquals(30, scontrol.calcolaConsumoAttualeComplessivoFT());
		assertEquals(30, scontrol.calcolaConsumoAttualeFT());

	}
	
	/**
	 * metodo che serve per testare ciò che accade quando c'è la ricezione dei dati di consumo
	 * in questo scenario un elettrodomestico entra in fase di esercizio, ciò comporta
	 * il superamento della soglia di consumo e la disattivazione di un'altro elettrodomestico
	 */
	public final void testRiceviDatiSensoreDisattivazioneNecessaria(){
		/*
		 * sono in esercizio un elettrodomestico a basso e uno ad altro consumo (150)
		 * l' entrata in esercizio di un elettrodomestico a medio consumo provocherà
		 * la disattivazione di uno ad alto consumo.
		 */
		
		scontrol.getElettrodomestici().remove("e1");
		scontrol.getElettrodomestici().put("e1", new RappresentazioneElettrodomestico("e1", StatoElettrodomestico.esercizio, 30, "i1", new Date())); 
		
		scontrol.getElettrodomestici().remove("e4");
		scontrol.getElettrodomestici().put("e4", new RappresentazioneElettrodomestico("e4", StatoElettrodomestico.avvio, 120, "i4", new Date())); 
		scontrol.getElettrodomestici().remove("e7");
		scontrol.getElettrodomestici().put("e7", new RappresentazioneElettrodomestico("e7", StatoElettrodomestico.esercizio, 90, "i7", new Date())); 
		
		scontrol.riceviDatiSensoreFT(Util.datiSensoreToString(new DatiSensore("e4", 60)));
		
		assertTrue (scontrol.getElettrodomestici().get("e4").getStato()== StatoElettrodomestico.esercizio);
		assertEquals(60, scontrol.getElettrodomestici().get("e4").getConsumo());
		
		assertTrue (scontrol.getElettrodomestici().get("e7").getStato()== StatoElettrodomestico.disattivato);
		assertEquals(90, scontrol.getElettrodomestici().get("e7").getConsumo()); // il sistema tiene memoria di quanto consumava un elettrodomestico per decidere quale riattivare

		assertEquals(90, scontrol.calcolaConsumoAttualeComplessivoFT());
		assertEquals(90, scontrol.calcolaConsumoAttualeFT());


	}
	
	
	
	public void verificaStatus(IStatus status){
		assertNotNull(status);
		assertEquals(scontrol.calcolaConsumoAttualeComplessivoFT(), status.getConsumoAttualeComplessivo());
		assertEquals(scontrol.getSoglia(), status.getSoglia());
		for (IReportElettrodomestico report : status.getReports()) {
			IRappresentazioneElettrodomestico el = scontrol.getElettrodomestici().get(report.getIdElettrodomestico());
			assertEquals(el.getConsumo(), report.getConsumoAttuale());
			assertEquals(el.getStato(), report.getStato());			
		}
	}
	
	
	
			
		
	
}//end ScontrolTest