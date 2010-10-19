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
		
		//istanze di intetturrori e usercmd messi visto che le due invitation non funzionano.
		
		private IUserCmd userCmd;
		private Hashtable <String, IInterruttore> interruttori = new Hashtable<String, IInterruttore>();
		
		/**
		 * Restituisce un'istanza di Scontrol configurata mediante i parametri passati.
		 * crea le tabelle degli elettrodomestici gestiti e quella dei relativi timer
		 * ovviamente fa partire solo  i timer relativi agli elettrodomestici accesi 
		 * @param elettrodomestici la lista degli elettrodomestici controllati da Scontrol.
		 * @param soglia
		 * @param intervalloTimers
		 * @return
		 */
		
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



		public void setUserCmd(IUserCmd userCmd) {
			this.userCmd = userCmd;
		}



		public void setInterruttori(Hashtable<String, IInterruttore> interruttori) {
			this.interruttori = interruttori;
		}
		
		
	/**
	 * provvede ad accendere l'elettrodomestico indicato: chiama turnOn sull' interruttore collegato allleletrodomesticoIndicato
	 * e cambia lo stato dell'elettrodomestico in avvio.
	 * NON PROVVEDE AD INVIARE LO STATO AGGIORNATO A SCONTROL
	 * @param idElettrodomestico: l'id dell'elettrodomestico da accendere
	 */	
	public void accendiElettrodomestico (String idElettrodomestico){
		IRappresentazioneElettrodomestico elettrodomestico = elettrodomestici.get(idElettrodomestico);
		elettrodomestico.setStato(StatoElettrodomestico.avvio);
		interruttori.get(elettrodomestico.getIdInterruttore()).turnOn();
	}
	/**
	 * provvede a spegnere l'elettrodomestico indicato: chiama turnOff sull' interruttore collegato allleletrodomesticoIndicato
	 * e cambia lo stato dell'elettrodomestico in spento, azzerando il consumo attuale. 
	 * NON PROVVEDE AD INVIARE LO STATO AGGIORNATO A SCONTROL
	 * @param idElettrodomestico: l'id dell'elettrodomestico da spegnere
	 */
	public void spegniElettrodomestico (String idElettrodomestico){
		IRappresentazioneElettrodomestico elettrodomestico = elettrodomestici.get(idElettrodomestico);
		elettrodomestico.setStato(StatoElettrodomestico.spento);
		elettrodomestico.setConsumo(0);
		interruttori.get(elettrodomestico.getIdInterruttore()).turnOff();
	}
	
	/**
	 * provvede a disattivare l'elettrodomestico indicato: chiama turnOff sull'eletrodomesticoIndicato
	 * e cambia lo stato dell'elettrodomestico in disattivato. 
	 * NON PROVVEDE AD INVIARE LO STATO AGGIORNATO A SCONTROL
	 * @param idElettrodomestico: l'id dell'elettrodomestico da disattivare
	 */
	private void disattivaElettrodomestico (String idElettrodomestico){
		IRappresentazioneElettrodomestico elettrodomestico = elettrodomestici.get(idElettrodomestico);
		elettrodomestico.setStato(StatoElettrodomestico.disattivato);
		interruttori.get(elettrodomestico.getIdInterruttore()).turnOff();
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
	protected void scontrolAskComandoScontrol() throws Exception{
		
	IAcquireAskReply answer = support.ask( "scontrol", "comandoScontrol", M, "interruttore");
	showMsg(  "has asked ... " + "comandoScontrol" );
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
 		
//operation scontrol : per le prove commenta e decommenta le comunicazioni di interesse

//	IMessageAndContext m = scontrolGrant();
//	//showMessage(  m.getReceivedMessage() ) ;
//	evalRequest( m  );
//	 
	 scontrolAskStatus() ;
//	 
//
//	 scontrolAskComandoScontrol() ;
	 


//	IMessage m2 = scontrolSense();
	//showMessage( m ) ;
	 
	}catch( Exception e ){System.err.println("Errore in Scontrol");e.printStackTrace();}
	}



	@Override
	public void update(IResettableTimer safetyTimer, String idElettrodomestico) {
		// TODO Auto-generated method stub
		
	}
}
