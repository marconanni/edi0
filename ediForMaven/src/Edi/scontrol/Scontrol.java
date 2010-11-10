package Edi.scontrol;

import Edi.Subject;
import Edi.messaggi.*;
import it.unibo.platform.medcl.*;
import it.unibo.platform.lindaLike.IMessage;
import java.util.*;

import Edi.elettrodomestico.*;
import Edi.userCmd.*;
	

public class  Scontrol extends Subject implements IObserver, IScontrol{
	
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
		
		
		
		/**
		 * thread che si mette in ascolto delle richeste di UserCmd, le elabora 
		 * e invia lo stato attuale come rispsta. E' stato necesario inserirlo
		 * perchè l'attesa di una request è bloccante, quindi, se fosse stato
		 * direttamente scontrol a farlo, questi si sarebbe bloccato non potendo
		 * ricevere i messaggi dai sensori, la cosa, unita al sistema di spegnimento
		 * preventivo avrebbe creato molta confusione e, probabilmente, causato lo
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
		public static IScontrol getInstance(Vector<IRappresentazioneElettrodomestico> elettrodomestici, int soglia, long intervalloTimers, Vector<IInterruttore> interruttori){
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



		



		public void setInterruttori(Hashtable<String, IInterruttore> interruttori) {
			this.interruttori = interruttori;
		}
		
		
	/**
	 * @see Edi.scontrol.IScontrol#accendiElettrodomestico(java.lang.String)
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
	 * @see Edi.scontrol.IScontrol#spegniElettrodomestico(java.lang.String)
	 */
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
	 * Infine fa in modo che il timer non gli invii nessuna notifica se, come ovvio, non si ricevono più dati dal sensore
	 * NON PROVVEDE AD INVIARE LO STATO AGGIORNATO A SCONTROL
	 * @param idElettrodomestico: l'id dell'elettrodomestico da disattivare
	 */
	private void disattivaElettrodomestico (String idElettrodomestico){
		IRappresentazioneElettrodomestico elettrodomestico = elettrodomestici.get(idElettrodomestico);
		elettrodomestico.setStato(StatoElettrodomestico.disattivato);
		interruttori.get(elettrodomestico.getIdInterruttore()).turnOff();
		timers.get(idElettrodomestico).removeObserver(this);
	}
	/**
	 * provvede a riattivare l'elettrodomestico indicato: chiama accendiElettrodomestico sull'eletrodomesticoIndicato
	 * e cambia lo stato dell'elettrodomestico in avvio (secondo la logica attuale la riattivazione è di fatto una accensione). 
	 * unica cosa in più: riazzera il consumo dell'elettrodomestrico.
	 * NON PROVVEDE AD INVIARE LO STATO AGGIORNATO A SCONTROL
	 * @param idElettrodomestico: l'id dell'elettrodomestico da riattivare
	 */
	private void riattivaElettrodomestico (String idElettrodomestico){
		this.elettrodomestici.get(idElettrodomestico).setConsumo(0);
		this.accendiElettrodomestico(idElettrodomestico);
		
	}
	
	/**
	 * stabilisce quale elettrodomestico disattivare tra quelli in esercizio.
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
		// creo il peggiore elettrodomestico, acceso adesso e con consumo pari al maggior valore per un int: verrà per forza sovrescritto
		IRappresentazioneElettrodomestico prescelto = new RappresentazioneElettrodomestico("", StatoElettrodomestico.esercizio, Integer.MAX_VALUE, "", new Date());
		for (IRappresentazioneElettrodomestico elettrodomestico : elettrodomestici.values()) {
			if (elettrodomestico.getStato()==StatoElettrodomestico.disattivato&&(elettrodomestico.getConsumo()<prescelto.getConsumo()|| (elettrodomestico.getConsumo()== prescelto.getConsumo()&& elettrodomestico.getOraAccensione().before(prescelto.getOraAccensione()))))
				prescelto = elettrodomestico;
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
			if(elettrodomestico.getStato()== StatoElettrodomestico.disattivato && elettrodomestico.getConsumo()<=margine)
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
	 * dagli elettrodomestici in fase di avvio, quindi, come da specifiche il consumoAttualeComplessivo
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
				reports.add(new ReportElettrodomestico(elettrodomestico.getId(), elettrodomestico.getStato(), elettrodomestico.getConsumo()));
						
		}
		/*
		 *  non so perchè ma il vettore dei report è in ordine contrario: ad esempio se gli elettrodomestici
		 *  sono e1..e9, il primo è e9, provo ad invertirli
		 */
		Vector <IReportElettrodomestico> reportsInveriti = new Vector<IReportElettrodomestico>();
		int k = reports.size();
		for (int i =0; i<reports.size();i++) {
			reportsInveriti.add(reports.get(k-1));// i vettori iniziano da 0, non da 1 quindi se hanno 9 elementi il loro ultimo el è l'ottavo
			k--;
		}
		return new  Status(comunicazione, reportsInveriti, soglia);
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
		System.out.println( "contenutoMessaggio"+ m.getReceivedMessage().msgContent() ) ;
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
	/**
	 * metodo che rappresenta l'esecuzione di Scontrol: per prima cosa lancia il thread che
	 * deve prendersi carico delle richieste di UserCmd, poi si preoccupa
	 * di ricevere in  i dati provenienti dai sensori, stabilendo quando un elettrodomestico
	 * entra in fase di esercizio, provvedendo, se è il caso ad una disattivazione. in questi
	 * casi provvede inoltre ad inviare a UserCmd il nuovo stato del sitema
	 */
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
				// se è la prima volta che ricevo i dati di consumo mando lo stato aggiornato a userCmd,
				// così l'utente sa quanto consuma l'elettrodomestico in fase di avvio. Se non lo facessi
				// avrebbe la prossima comunicazione solo quando l'elettrodomestico entra in esercizio
				if (elettrodomestico.getStato()==StatoElettrodomestico.avvio && elettrodomestico.getConsumo()==0){
					IStatus newStatus =(this.preparaStatus(""));
					// impacchetto lo status e lo mando a userCmd
					M= Util.statutsToString(newStatus);
					this.scontrolAskStatus();
				}
				// se l'elettrodomestico era in accensione ed invia un consumo pari alla metà delleo scorso vuol dire che 
				// è passato nella fase di esercizio. ciò significa che bisogna verificare se è necessario disattivare
				// qualche elettrodomestico, qualora fosse necessario viene fatto e ne viene mandata comunicazione all'utente
				if((elettrodomestico.getStato()== StatoElettrodomestico.avvio)&& datiSensore.getConsumoAttuale()== (elettrodomestico.getConsumo()/2)){
					elettrodomestico.setStato(StatoElettrodomestico.avvio);
					elettrodomestico.setConsumo(datiSensore.getConsumoAttuale());
					if(valutaNecessitàDisattivazione()==true){
						String daDisattivare = this.stabilisciElettrodomesticoDaDisattivare();
						this.disattivaElettrodomestico(this.stabilisciElettrodomesticoDaDisattivare());
						IStatus newStatus =this.preparaStatus("disattivato elettrodomestico "+ daDisattivare);
						// mando il nuovo status a userCmd
						M= Util.statutsToString(newStatus);
						this.scontrolAskStatus();
					}// fine if disattivazione elettrodomestico
				}// fine if inizio fase di esercizio
				else{
					elettrodomestico.setConsumo(datiSensore.getConsumoAttuale());
				}
			}// fine if messaggio diverso da null
		}
 	}catch( Exception e ){System.err.println("Errore in Scontrol");e.printStackTrace();}
	
	
	

		
	
	}// fine doJob


	
	@Override
	// qui ci vado se scatta il timeotu di un elettrodomestico: lo devo spegnere e mando comunicazione 
	// a userCmd
	/**
	 * metodo chiamatato da un timer per segnalare che è scattato: significa quindi che non si sono
	 * ricevuti dati di consumo dal sensore montato sull'elettrodomestico per più di 2 secondi
	 * (secondo la configurazione attuale). bisogna quindi dpegnere l'elettrodomestico e dane comunicazione
	 * a userCmd.
	 * @param safetyTimer il timer che ha lanciato l'evento
	 * @param idElettrodomestico l'id dell'elettrodomestico associato al timer
	 */
	public void update(IResettableTimer safetyTimer, String idElettrodomestico) {
		this.spegniElettrodomestico(idElettrodomestico);
		IStatus newStatus =this.preparaStatus("non si ricevevano comunicazioni sul consumo da più di 2 secondi dall'elettrodomestico "+idElettrodomestico+". L'elettrodomestico è stato spento per ragioni di sicurezza");
		// impacchetto il nuovo stato e lo mando tramite contact
		M = Util.statutsToString(newStatus);
		try {
			this.scontrolAskStatus();
		} catch (Exception e) {
			System.err.println("problemi nell'invio dello status aggiornato a userCmd");
			e.printStackTrace();
		}
	}
	
	
	//TODO: metodi aggiunti per i test.
	/**
	 * metodo che serve a poter visualizzare il consumo di tutti gli elettrdomestici in fase di esercizio
	 * */
	public int calcolaConsumoAttualeFT(){
		return this.calcolaConsumoAttuale();
		
	}
	/**
	 * metodo che serve a visualizzare il consumo totale di tutti gli elettrdomestici accesi nei test
	 * @return
	 */
	public int calcolaConsumoAttualeComplessivoFT(){
		return this.calcolaConsumoAttualeComplessivo();
	}
	/**
	 * metodo che serve pre rompere l'incapsulamento col fine di verificare nei test il
	 * il risultato dell'omonimo metodo privato 
	 * @return il risultato del metodo omonimo privato
	 */
	public boolean valutaNecessitàSpegnimentoPreventivoFT(){
		return this.valutaNecessitàSpegnimentoPreventivo();
	}
	/**
	 * metodo che serve pre rompere l'incapsulamento col fine di verificare nei test il
	 * il risultato dell'omonimo metodo privato 
	 * @return il risultato del metodo omonimo privato
	 */
	public boolean valutaNecessitàDisattivazioneFT(){
		return this.valutaNecessitàDisattivazione();
	}
	/**
	 * metodo che serve pre rompere l'incapsulamento col fine di verificare nei test il
	 * il risultato dell'omonimo metodo privato 
	 * @return il risultato del metodo omonimo privato
	 */
	public boolean valutaNecessitàRiattivazioneFT(){
		return this.valutaNecessitàRiattivazione();
	}
	/**
	 * metodo che serve pre rompere l'incapsulamento col fine di verificare nei test il
	 * il risultato dell'omonimo metodo privato 
	 * @return il risultato del metodo omonimo privato
	 */
	public String stabilisciElettrodomesticoDaDisattivareFT(){
		return this.stabilisciElettrodomesticoDaDisattivare();
	}
	/**
	 * metodo che serve pre rompere l'incapsulamento col fine di verificare nei test il
	 * il risultato dell'omonimo metodo privato 
	 * @return il risultato del metodo omonimo privato
	 */
	public String stabilisciElettrodomesticoDaRiattivareFT(){
		return this.stabilisciElettrodomesticoDaRiattivare();
	}
	/**
	 * metodo che serve pre rompere l'incapsulamento col fine di verificare nei test 
	 * l'omonimo metodo privato 
	 */
	public void disattivaElettrodomesticoFT(String idElettrodomestico){
		this.disattivaElettrodomestico(idElettrodomestico);
	}
	/**
	 * metodo che serve pre rompere l'incapsulamento col fine di verificare nei test 
	 * l'omonimo metodo privato 
	 */
	public void riattivaElettrodomesticoFT(String idElettrodomestico){
		this.riattivaElettrodomestico(idElettrodomestico);
	}
	
	/**
	 * 
	 * metodo che permette di simulare la ricezione e l'esecuzione di un comando utente
	 * @param comandoString: la versione in stringa del comando che scontrol deve eseguire
	 * @return un oggetto Status che verrebbe stringhificato e mandato tramite il supporto
	 * contact a userCmd.
	 */
	public IStatus riceviEdElaboraComandoUserCmdFT(String comandoString){
		
			
			IComandoUserCmd comando = Util.stringToComandoUserCmd(comandoString);
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
			
			return statusDaInviare;
		
	}
	
	/**
	 * questo metodo permette di simulare ciò che accade quando Scontrol riceve i dati di consumo
	 * da un sensore.
	 * @param datiSensoreString la stringa dei dati sensore che Scontrol riceverebbe dal supporto contact.
	 */
	public void riceviDatiSensoreFT(String datiSensoreString){
		
		
	
			IDatiSensore  datiSensore = Util.stringToDatiSensore(datiSensoreString);
			IRappresentazioneElettrodomestico elettrodomestico = elettrodomestici.get(datiSensore.getId());
			// resetto il timer di sicurezza
			this.timers.get(datiSensore.getId()).resetta();
			
			// se l'elettrodomestico era in accensione ed invia un consumo pari alla metà delleo scorso vuol dire che 
			// è passato nella fase di esercizio. ciò significa che bisogna verificare se è necessario disattivare
			// qualche elettrodomestico, qualora fosse necessario viene fatto e ne viene mandata comunicazione all'utente
			if((elettrodomestico.getStato()== StatoElettrodomestico.avvio)&& datiSensore.getConsumoAttuale()== (elettrodomestico.getConsumo()/2)){
				elettrodomestico.setStato(StatoElettrodomestico.esercizio);
				elettrodomestico.setConsumo(datiSensore.getConsumoAttuale());
				if(valutaNecessitàDisattivazione()==true){
					
					this.disattivaElettrodomestico(this.stabilisciElettrodomesticoDaDisattivare());
					
				}// fine if disattivazione elettrodomestico
			}// fine if inizio fase di esercizio
			else{
				elettrodomestico.setConsumo(datiSensore.getConsumoAttuale());
			}
	}
	

	
}
