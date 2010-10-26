package Edi.scontrol;

import Edi.Subject;
import Edi.messaggi.*;
import it.unibo.platform.medcl.*;
import it.unibo.platform.lindaLike.IMessage;
import java.util.*;

import Edi.elettrodomestico.*;
import Edi.userCmd.*;
	

public class  Scontrol extends Subject implements IObserver{
	
	//Local state of the subject
		private String M = "messaggioScontrol.";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
		private static Scontrol instance;
		private Hashtable<String, IRappresentazioneElettrodomestico> elettrodomestici = new Hashtable<String, IRappresentazioneElettrodomestico>();
		private Hashtable<String, IResettableTimer> timers = new Hashtable<String, IResettableTimer>();
		private int soglia;
		private long intervalloTimers;
		private Hashtable <String, IInterruttore> interruttori = new Hashtable<String, IInterruttore>();
		
		//istanza di usercmd messa visto che la invitation non funziona.
		
		private UserCmd userCmd;
		
		/**
		 * thread che si mette in ascolto delle richeste di UserCmd, le elabora 
		 * e invia lo stato attuale come rispsta. E' stato necesario inserirlo
		 * perchè l'attesa di una request è bloccante, quindi, se fosse stato
		 * direttamente scontrol a farlo, questi si sarebbe bloccato non potendo
		 * ricevere i messaggi dai sensori, la cosa, unita al sistema di spegnimento
		 * preventivo avrebbe creato un gran casino e, probabilmente, causato lo
		 * spegnimento totale del sistema.
		 */
		private Thread ricevitoreComandiUserCmd = new Thread(){
			public void run(){
				while (true){
					try {
						sleep(100);
						riceviEdElaboraComandoUserCmd();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					riceviEdElaboraComandoUserCmd();
				}
			}
			
		};
		
		
		
		/**
		 * Restituisce un'istanza di Scontrol configurata mediante i parametri passati.
		 * crea le tabelle degli elettrodomestici gestiti e quella dei relativi timer
		 * ovviamente fa partire solo  i timer relativi agli elettrodomestici accesi 
		 * @param elettrodomestici la lista degli elettrodomestici controllati da Scontrol.
		 * @param soglia
		 * @param intervalloTimers
		 * @return
		 */
		public static Scontrol getInstance(Vector<IRappresentazioneElettrodomestico> elettrodomestici, int soglia, int intervalloTimers, Vector<IInterruttore> interruttori){
			if (instance==null){
			instance = new Scontrol();
		}
		instance.setSoglia(soglia);
		instance.setIntervalloTimers(intervalloTimers);
		for (IInterruttore interruttore : interruttori) {
			instance.interruttori.put(interruttore.getId(), interruttore);
			
		}
		for (IRappresentazioneElettrodomestico elettrodomestico : elettrodomestici) {
			instance.getElettrodomestici().put(elettrodomestico.getId(), elettrodomestico);
			// creo un nuovo safety timer, ma non mi registro come listener, lo faccio quando
			// accendo l'elettrodomestico (e faccio partire il timer)
			SafetyTimer timer = new SafetyTimer();
			timer.setIdElettrodomestico(elettrodomestico.getId());
			timer.setEventTime(instance.getIntervalloTimers());
			
			instance.getTimers().put(elettrodomestico.getId(), timer);
			if (elettrodomestico.getStato()==StatoElettrodomestico.esercizio|| elettrodomestico.getStato()== StatoElettrodomestico.avvio){
				timer.addObserver(instance);
				timer.start();
			}
		}
		return instance;
	}

		public static Scontrol getInstance(Vector<IRappresentazioneElettrodomestico> elettrodomestici, int soglia, int intervalloTimers, Vector<IInterruttore> interruttori, UserCmd userCmd){
			getInstance(elettrodomestici, soglia, intervalloTimers, interruttori);
			instance.userCmd= userCmd;
			return instance;
		}
		

		/*
		 * TODO da rifare in base a come decidi di fare la configurazione iniziale e lo startup
		 */
//		public static Scontrol getInstance(List<IRappresentazioneElettrodomestico> elettrodomestici, int soglia, int intervalloTimers){
//			if (instance==null){
//				instance = new Scontrol();
//			}
//			instance.setSoglia(soglia);
//			instance.setIntervalloTimers(intervalloTimers);
//			for (IRappresentazioneElettrodomestico elettrodomestico : elettrodomestici) {
//				instance.getElettrodomestici().put(elettrodomestico.getId(), elettrodomestico);
//				// creo un nuovo safety timer, ma non mi registro come listener, lo faccio quando
//				// accendo l'elettrodomestico (e faccio partire il timer)
//				SafetyTimer timer = new SafetyTimer();
//				timer.setIdElettrodomestico(elettrodomestico.getId());
//				timer.setEventTime(instance.getIntervalloTimers());
//				
//				instance.getTimers().put(elettrodomestico.getId(), timer);
//				if (elettrodomestico.isOn()){
//					timer.addObserver(instance);
//					timer.start();
//				}
//			}
//			return instance;
//		}
	
		
		
		// TODO getters 
		
		public Hashtable<String, IRappresentazioneElettrodomestico> getElettrodomestici() {
			return elettrodomestici;
		}



		public Hashtable<String, IResettableTimer> getTimers() {
			return timers;
		}



		public int getSoglia() {
			return soglia;
		}



		public long getIntervalloTimers() {
			return intervalloTimers;
		}



		public IUserCmd getUserCmd() {
			return userCmd;
		}



		public Hashtable<String, IInterruttore> getInterruttori() {
			return interruttori;
		}

		// TODO setters
		


		

	
	public void setElettrodomestici(
				Hashtable<String, IRappresentazioneElettrodomestico> elettrodomestici) {
			this.elettrodomestici = elettrodomestici;
		}



		public void setTimers(Hashtable<String, IResettableTimer> timers) {
			this.timers = timers;
		}



		public void setSoglia(int soglia) {
			this.soglia = soglia;
		}



		public void setIntervalloTimers(long intervalloTimers) {
			this.intervalloTimers = intervalloTimers;
		}



		public void setUserCmd(UserCmd userCmd) {
			this.userCmd = userCmd;
		}



		public void setInterruttori(Hashtable<String, IInterruttore> interruttori) {
			this.interruttori = interruttori;
		}
		
		
	/**
	 * provvede ad accendere l'elettrodomestico indicato: chiama turnOn sull' interruttore collegato allleletrodomesticoIndicato
	 * imposta l'ora di accensione, cambia lo stato dell'elettrodomestico in avvio 
	 * e fa partire  e si registra al timer collegato all'interruttore
	 * 
	 * 
	 * NON PROVVEDE AD INVIARE LO STATO AGGIORNATO A SCONTROL
	 * @param idElettrodomestico: l'id dell'elettrodomestico da accendere
	 */	
	public void accendiElettrodomestico (String idElettrodomestico){
		IRappresentazioneElettrodomestico elettrodomestico = elettrodomestici.get(idElettrodomestico);
		IResettableTimer timer = timers.get(idElettrodomestico);
		elettrodomestico.setStato(StatoElettrodomestico.avvio);
		elettrodomestico.setOraAccensione(new Date());
		interruttori.get(elettrodomestico.getIdInterruttore()).turnOn();
		timer.addObserver(this);
		timer.avvia();
		
		
	}
	/**
	 * provvede a spegnere l'elettrodomestico indicato: chiama turnOff sull' interruttore collegato allleletrodomesticoIndicato
	 * e cambia lo stato dell'elettrodomestico in spento, azzerando il consumo attuale.
	 * Infine si deregistra dal timer, in modo che  quando scatta non si riceve l'evento 
	 * NON PROVVEDE AD INVIARE LO STATO AGGIORNATO A SCONTROL
	 * @param idElettrodomestico: l'id dell'elettrodomestico da spegnere
	 */
	// TODO:  vedi se c'è il modo di forzare la terminazione del thread in modo che l'evento non venga creato del tutto
	public void spegniElettrodomestico (String idElettrodomestico){
		IRappresentazioneElettrodomestico elettrodomestico = elettrodomestici.get(idElettrodomestico);
		elettrodomestico.setStato(StatoElettrodomestico.spento);
		elettrodomestico.setConsumo(0);
		elettrodomestico.setOraAccensione(null);
		interruttori.get(elettrodomestico.getIdInterruttore()).turnOff();
		timers.get(idElettrodomestico).removeObserver(this);
	}
	
	/**
	 * provvede a disattivare l'elettrodomestico indicato: chiama turnOff sull'eletrodomesticoIndicato
	 * e cambia lo stato dell'elettrodomestico in disattivato. 
	 * Infine si deregistra dal timer, in modo che  quando scatta non si riceve l'evento 
	 * NON PROVVEDE AD INVIARE LO STATO AGGIORNATO A SCONTROL
	 * @param idElettrodomestico: l'id dell'elettrodomestico da disattivare
	 */
	// TODO:  vedi se c'è il modo di forzare la terminazione del thread in modo che l'evento non venga creato del tutto
	private void disattivaElettrodomestico (String idElettrodomestico){
		IRappresentazioneElettrodomestico elettrodomestico = elettrodomestici.get(idElettrodomestico);
		elettrodomestico.setStato(StatoElettrodomestico.disattivato);
		interruttori.get(elettrodomestico.getIdInterruttore()).turnOff();
		timers.get(idElettrodomestico).removeObserver(this);
	}
	/**
	 * provvede a riattivare l'elettrodomestico indicato: chiama turnOn sull'eletrodomesticoIndicato
	 * e cambia lo stato dell'elettrodomestico in avvio (secondo la logica attuale la riattivazione è di fatto una accensione. 
	 * NON PROVVEDE AD INVIARE LO STATO AGGIORNATO A SCONTROL
	 * @param idElettrodomestico: l'id dell'elettrodomestico da riattivare
	 */
	private void riattivaElettrodomestico (String idElettrodomestico){
		this.accendiElettrodomestico(idElettrodomestico);
		
	}
	
	/**
	 * stabilisce quale elettrodomestico riattivare tra quelli in esercizio.
	 * l'elettrodomestico da disattivare è quello acceso dopo tra quelli che consumano di più.
	 * @return l'identificativo dell'elettrodomestico da riattivare
	 */
	private String stabilisciElettrodomesticoDaDisattivare (){
		// creo il peggiore elettrodomestico, acceso nel 1900 e con consumo pari al minor valore per un int: verrà per forza sovrescritto
		IRappresentazioneElettrodomestico prescelto = new RappresentazioneElettrodomestico("", StatoElettrodomestico.esercizio, Integer.MIN_VALUE, "", new Date(1, 1, 1));
		for (IRappresentazioneElettrodomestico elettrodomestico : elettrodomestici.values()) {
			if (elettrodomestico.getStato()==StatoElettrodomestico.esercizio&&(elettrodomestico.getConsumo()>prescelto.getConsumo()|| (elettrodomestico.getConsumo()== prescelto.getConsumo()&& elettrodomestico.getOraAccensione().after(prescelto.getOraAccensione()))))
				prescelto = elettrodomestico;
		}
		return prescelto.getId();
		
		
	}
	/**
	 * stabilisce quale elettrodomestico riattivare tra quelli disattivati.
	 * l'elettrodomestico da riattivare è quello acceso prima tra quelli che consumano di meno.
	 * @return l'identificativo dell'elettrodomestico da riattivare
	 */
	private String stabilisciElettrodomesticoDaRiattivare (){
		Vector<IRappresentazioneElettrodomestico> elettrodomesticiCheConsumanoMeno = new Vector<IRappresentazioneElettrodomestico>();
		IRappresentazioneElettrodomestico prescelto = null;
		int minimo = Integer.MAX_VALUE;
		for (IRappresentazioneElettrodomestico elettrodomestico : elettrodomestici.values()) {
			if(elettrodomestico.getStato()==StatoElettrodomestico.disattivato && elettrodomestico.getConsumo()<=minimo){
				minimo = elettrodomestico.getConsumo();
				if (elettrodomestico.getConsumo()==minimo)
					elettrodomesticiCheConsumanoMeno.add(elettrodomestico);
				if(elettrodomestico.getConsumo()<minimo){
					elettrodomesticiCheConsumanoMeno.removeAllElements();
					elettrodomesticiCheConsumanoMeno.add(elettrodomestico);
				}
					
			}
			
		}
		
		Date oraAccensioneMinima= new Date(System.currentTimeMillis());
		for (IRappresentazioneElettrodomestico elettrodomestico : elettrodomesticiCheConsumanoMeno) {
			if(elettrodomestico.getOraAccensione().before(oraAccensioneMinima)){
				oraAccensioneMinima = elettrodomestico.getOraAccensione();
				prescelto= elettrodomestico;
			}
		}
		
		return prescelto.getId();
		
	}
	/**
	 * valuta se è necessario disattivare un elettrodomestico ancora prima della sua accensione,
	 * questo può capitare in un solo caso: tutti gli elettrodomestici in esercizio sono a basso consumo
	 *  e l'accensione di un ulteriore elettrodomestico a basso consumo
	 *  ( ed evidentemente anche a consumo medio o alto) provocherebbe il superamento della soglia
	 *   di consumo massimo
	 * @return
	 */
	private boolean valutaNecessitàSpegnimentoPreventivo(){
		int consumoAttuale = this.calcolaConsumoAttuale();
		if ((soglia-consumoAttuale)>=30)
			return false;
		else{
			boolean tuttiBassoConsumo=true;
			for (IRappresentazioneElettrodomestico elettrodomestico : elettrodomestici.values()) {
				if(elettrodomestico.getStato()==StatoElettrodomestico.esercizio && elettrodomestico.getConsumo()!=30)
					tuttiBassoConsumo =false;
				
			}
			return tuttiBassoConsumo;
		}
		
		
	}
	/**
	 * determina se è necessario disattivare un elettrodomestico in quanto 
	 * si è superata la soglia di consumo
	 * @return true se il consumo degli elettrodomestici in esercizio è superiore
	 * alla soglia di consumo 
	 */
	private boolean valutaNecessitàDisattivazione(){
		if (this.calcolaConsumoAttuale()>this.getSoglia())
			return true;
		else 
			return false;
		
	}
	
	/**
	 * Metodo che determina se è possibile riattivare un elettrodomestico precedentemente disattivato:
	 * in particolare guarda se c'è un elettrodomestico disattivato il cui consumo attuale ( che per gli 
	 * elettrodomestici disattivati è l'ultimo dato di consumo rilevato prima della disattivazione) 
	 * sommato al consumo attuale sia minore della soglia di consumo
	 * 
	 * @return true se è possibile riattivare un elettrodomestico
	 */
	private boolean valutaNecessitàRiattivazione(){
		
		int margine = soglia - this.calcolaConsumoAttuale();
		/* ci si potrebbe mettere un controllo sul margine: con le specifiche attuali se è minore di 30
		 * di sicuro non si può riattivare niente. però questo lega troppo ai consumi dati dalle specifiche,
		 * per cui evito di metterlo
		 */
		for (IRappresentazioneElettrodomestico elettrodomestico : elettrodomestici.values()) {
			if(elettrodomestico.getStato()== StatoElettrodomestico.disattivato && elettrodomestico.getConsumo()<=soglia)
				return true;
			
		}
		return false;
		
	}
	
	/**
	 * 
	 * calcola il consumo totale dato dai soli elettrodomestici attualmente in fase di esercizio
	 * basandosi sugli ultimi dati di consumo ricevuti dai sensori
	 * il valore costituito rappresenta il totale di energia da considerare per i calcoli
	 * relativi alla soglia massima di consumo.
	 * @return il consumo totale degli elettrodomestici in fase di esercizo
	 */
	private int calcolaConsumoAttuale(){
		int consumoTotale=0;
		for (IRappresentazioneElettrodomestico elettrodomestico : elettrodomestici.values()) {
			if (elettrodomestico.getStato()== StatoElettrodomestico.esercizio)
				consumoTotale= consumoTotale+ elettrodomestico.getConsumo();
		}
		
		return consumoTotale;
	}
	
	/**
	 * calcola il consumo totale dato dagli elettrodomestici in fase di avvio ed esercizio
	 * basandosi sugli ultimi dati di consumo ricevuti dai sensori
	 * restituisce quindi la domanda di energia totale del sistame
	 * @return il consumo totale del sistema.
	 */
	private int calcolaConsumoAttualeComplessivo(){
		int consumoTotale=0;
		for (IRappresentazioneElettrodomestico elettrodomestico : elettrodomestici.values()) {
			if (elettrodomestico.getStato()== StatoElettrodomestico.avvio||elettrodomestico.getStato()==StatoElettrodomestico.esercizio)
				consumoTotale= consumoTotale+ elettrodomestico.getConsumo();
		}
		
		return consumoTotale;
	}
	
	/**
	 * metodo che prepara un oggetto status da inviare a UserCmd tramite il supporto contact
	 * NOTA: c'è un po' di sporco dovuto al fatto che gli elettrodomestici disattivati consumano 0, ma hanno
	 * nel campo consumo il dato dell'ultimo consumo, utile per la riattivazione
	 * e per il fatto che nel calcolo del consumo totale qui comprendiamo anche quello dato
	 * dagli elettrodomestici in fase di avvio, quindi, come das specifiche il consumoAttualeComplessivo
	 * può essere superiore alla soglia
	 * @param comunicazione una stringa di comunicazione da inviare a UserCmd.
	 * @return un oggetto Status che rappresenta lo stato attuale del sistema.
	 */
	private IStatus preparaStatus(String comunicazione){
		Vector <IReportElettrodomestico> reports = new Vector<IReportElettrodomestico>();
		for (IRappresentazioneElettrodomestico elettrodomestico : elettrodomestici.values()) {
			if(elettrodomestico.getStato()==StatoElettrodomestico.disattivato)
				reports.add(new ReportElettrodomestico(elettrodomestico.getId(), elettrodomestico.getStato(), 0));
			else
				reports.add(new ReportElettrodomestico(elettrodomestico.getId(), elettrodomestico.getStato(), 0));
						
		}
		
		return new  Status(comunicazione, reports, this.calcolaConsumoAttualeComplessivo());
	}
	/**
	 * metodo che prepara un oggetto status da inviare a UserCmd tramite il supporto contact
	 * NOTA: c'è un po' di sporco dovuto al fatto che gli elettrodomestici disattivati consumano 0, ma hanno
	 * nel campo consumo il dato dell'ultimo consumo, utile per la riattivazione
	 * e per il fatto che nel calcolo del consumo totale qui comprendiamo anche quello dato
	 * dagli elettrodomestici in fase di avvio, quindi, come das specifiche il consumoAttualeComplessivo
	 * può essere superiore alla soglia
	 * @return  un oggetto Status che rappresenta lo stato attuale del sistema.
	 */
	private IStatus preparaStatus(){
		return this.preparaStatus("");
	}
	
	/**
	 * metodo che, in base al tipo di comando ricevuto da UserCmd esegue le richieste dell'utente
	 * inviandogli lo stato aggiornato.
	 */
	private void riceviEdElaboraComandoUserCmd(){
		try {
			IMessageAndContext messaggio = scontrolGrant();
			IComandoUserCmd comando = Util.stringToComandoUserCmd(messaggio.getReceivedMessage().msgContent());
			IStatus statusDaInviare=this.preparaStatus();// inizializzo lo status con quello attuale, ma tanto verrà sovrascritto, è giusto per non mettere null.
			if(comando.getComando()==ComandiUserCmd.connetti)
				statusDaInviare = this.preparaStatus("Edi Energy Management, Benvenuti!");
			else if (comando.getComando()== ComandiUserCmd.disconnetti)
				statusDaInviare = this.preparaStatus("Arrivederci!");
			else if (comando.getComando()== ComandiUserCmd.accendi){
				/*
				 *  nota: l'accensione di un elettrodomestico rende ncessario solo il controllo sullo 
				 *  spegnimento preventiva
				 *  
				 */
				if(this.valutaNecessitàSpegnimentoPreventivo())
					statusDaInviare = this.preparaStatus("Impossibile accendere l'elettrodomestico "+comando.getIdElettrodomestico()+" tutta la potenza disponibile è già occupata da elettrodomestici a basso consumo non disattivabili");
				else{
					this.accendiElettrodomestico(comando.getIdElettrodomestico());
					statusDaInviare = this.preparaStatus("elettrodomestico "+comando.getIdElettrodomestico()+" in accensione");
				}
			}// fine if accensione
			else if(comando.getComando()==ComandiUserCmd.spegni){
				this.spegniElettrodomestico(comando.getIdElettrodomestico());
				if(this.valutaNecessitàRiattivazione()){
					String daRiattivare = this.stabilisciElettrodomesticoDaRiattivare();
					this.riattivaElettrodomestico(daRiattivare);
					statusDaInviare = this.preparaStatus("lo spegnimento di  "+comando.getIdElettrodomestico()+" ha causato la riattivazione di "+daRiattivare);
				}
				else
					statusDaInviare= this.preparaStatus("spento l'elettrodomestico "+comando.getIdElettrodomestico());
			}// fine spegnimento
			
			// invio risposta e stato aggiornato
			
			messaggio.replyToCaller(Util.statutsToString(statusDaInviare));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	



	public void evalAck(){}
	
	
	public void evalRequest(IMessageAndContext mCtx) throws Exception{
 		showMessage( " received", mCtx.getReceivedMessage() );		
 		//TODO elaborate the message
  		mCtx.replyToCaller("riinvio " + mCtx.getReceivedMessage().msgContent() );		
	}
	
	
	protected void scontrolAskStatus() throws Exception{
		
	IAcquireAskReply answer = support.ask( "scontrol", "status", M, "userCmd");
	showMsg(  "has asked ... " + "status" );
	while( !answer.askReplyAvailable() ) {
		showMsg( "no ask yet received ... " );
		Thread.sleep(100);
	}
	showMsg( "ask terminated " );
		}
	
	
	protected IMessageAndContext scontrolGrant() throws Exception{
	IMessageAndContext m = support.grant( "scontrol" ,  "comandoUserCmd"  );
		showMessage( "acquired", m.getReceivedMessage() ) ;
		return m;
	}
	protected IMessage scontrolSense() throws Exception{
		sleep(100);
		IMessage m = support.sense( "scontrol" ,  "datiSensore", lastMsgNum );
		if( m!=null ) {
			lastMsgNum = m.getMsgSeqNum();
			showMessage( "sensed", m ) ;
		}
		return m;
		}
	
	
//Local body of the subject
	protected void doJob(){
	try{
		/*
		 * qui ci si occupa solo di ricevere e trattare i dati di consumo ricevuti dai sensori.
		 * il thread RicevutoreComandi Usercmd provvede a ricevere i comandi dell'utente.
		 * qui ci si limita a farlo prtire
		 */
		this.ricevitoreComandiUserCmd.start();
		while(true){
			sleep(50);
			IMessage messaggioSensore = this.scontrolSense();
			if(messaggioSensore != null){
				IDatiSensore  datiSensore = Util.stringToDatiSensore(messaggioSensore.msgContent());
				IRappresentazioneElettrodomestico elettrodomestico = elettrodomestici.get(datiSensore.getId());
				// resetto il timer di sicurezza
				this.timers.get(datiSensore.getId()).resetta();
				// se l'elettrodomestico era in accensione ed invia un consumo pari alla metà delleo scorso vuol dire che 
				// è passato nella fase di esercizio. ciò significa che bisogna verificare se è necessario disattivare
				// qualche elettrodomestico, qualora fosse necessario viene fatto e ne viene mandata comunicazione all'utente
				if((elettrodomestico.getStato()== StatoElettrodomestico.avvio)&& datiSensore.getConsumoAttuale()== (elettrodomestico.getConsumo()/2)){
					elettrodomestico.setStato(StatoElettrodomestico.avvio);
					elettrodomestico.setConsumo(datiSensore.getConsumoAttuale());
					if(valutaNecessitàDisattivazione()==true){
						String daDisattivare = this.stabilisciElettrodomesticoDaDisattivare();
						this.disattivaElettrodomestico(this.stabilisciElettrodomesticoDaDisattivare());
						userCmd.updateStatus(this.preparaStatus("disattivato elettrodomestico "+ daDisattivare));
					}// fine if disattivazione elettrodomestico
				}// fine if inizio fase di esercizio
				else{
					elettrodomestico.setConsumo(datiSensore.getConsumoAttuale());
				}
			}// fine if messaggio diverso da null
		}
 		
//operation scontrol : per le prove commenta e decommenta le comunicazioni di interesse

//	IMessageAndContext m = scontrolGrant();
//	//showMessage(  m.getReceivedMessage() ) ;
//	evalRequest( m  );
//	 
//	 scontrolAskStatus() ;
//	 
//
//	 scontrolAskComandoScontrol() ;
	 


//	IMessage m2 = scontrolSense();
	//showMessage( m ) ;
	 
	}catch( Exception e ){System.err.println("Errore in Scontrol");e.printStackTrace();}
	}



	@Override
	// qui ci vado se scatta il timeotu di un elettrodomestico: lo devo spegnere e mando comunicazione 
	// a userCmd
	public void update(IResettableTimer safetyTimer, String idElettrodomestico) {
		this.spegniElettrodomestico(idElettrodomestico);
		this.preparaStatus("non si ricevevano comunicazioni sul consumo da più di 2 secondi dall'elettrodomestico "+idElettrodomestico+". L'elettrodomestico è stato spento per ragioni di sicurezza");
		
	}
}
