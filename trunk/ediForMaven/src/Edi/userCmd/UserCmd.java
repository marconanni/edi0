package Edi.userCmd;



import Edi.Subject;
import Edi.messaggi.*;
import Edi.scontrol.Scontrol;
import it.unibo.platform.medcl.*;
import it.unibo.platform.lindaLike.IMessage;
import java.util.*;


/**
 * 
 * @author Marco
 * 
 * Classe che rappersenta la centralina mobile attraverso la quale l'utente, dopo essersi connesso
 * invia i comandi di accensione e spegnimento degli elettrodomestici. Scontrol ( la centralina fissa)
 * provvede ad inviare lo stato del sistema all'atto di ogni richiesta o quando si verificano cambiamenti
 * in esso. E' compito dell'interfaccia utente visualizzare il suddetto stato
 *
 */
public class  UserCmd extends Subject implements IUserCmd {
	
	// variabili che servono al supporto contact
	//Local state of the subject
		private String M = "someContent";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
		
		// UserCmd è un singleton.
		private static UserCmd instance;
		// variabile che dice se si è connessi a Scontrol o meno
		private boolean connesso;
		// lo stato del sistema: è null se non si è connessi.
		private IStatus status;
		// le interfacce utente che si sono registrate 
		private Vector<IGui> guis;
		
		// visto che contact non va ho bisogno di comunicare direttamente con scontrol
		private Scontrol scontrol;
				
	
	public void setScontrol(Scontrol scontrol) {
			this.scontrol = scontrol;
		}


	private UserCmd() {
			this.connesso= false;
			status = null;
			this.guis=new Vector<IGui>();
			
		}
	
	
	public static  IUserCmd getInstance(){
		if (instance ==null){
			instance = new UserCmd();
		}
		return instance;
	}
	
	
	
	
	/**
	 * @see Edi.userCmd.IUserCmd#isConnesso()
	 */
	public boolean isConnesso() {
		return connesso;
	}

	
	/**
	 * @see Edi.userCmd.IUserCmd#getStatus()
	 */
	public IStatus getStatus() {
		return status;
	}

	
	/**
	 * @see Edi.userCmd.IUserCmd#connetti()
	 */
	public void connetti(){
		this.connesso=true;
		this.mandaComandoAScontrol(ComandiUserCmd.connetti, "");
				
	}
	/**
	 * @see Edi.userCmd.IUserCmd#disconnetti()
	 */
	public void disconnetti (){
		this.connesso=false;
		mandaComandoAScontrol(ComandiUserCmd.disconnetti, "");
		
		
		
	}
	
	
	/**
	 * @see Edi.userCmd.IUserCmd#accendiElettrodomestico(java.lang.String)
	 */
	public void accendiElettrodomestico (String idElettrodomestico ){
		if (connesso)
		mandaComandoAScontrol(ComandiUserCmd.accendi, idElettrodomestico);
		else
			System.err.println ("impossibile accendere un elettrodomestico se non si è connessi a Scontrol");
		
	}
	
	
	/**
	 * @see Edi.userCmd.IUserCmd#spegniElettrodomestico(java.lang.String)
	 */
	public void spegniElettrodomestico (String idElettrodomestico ){
		if (connesso)
		mandaComandoAScontrol(ComandiUserCmd.spegni, idElettrodomestico);
		else
			System.err.println ("impossibile spegnere un elettrodomestico se non si è connessi a Scontrol");
		
	}
	/**
	 * @see Edi.userCmd.IUserCmd#addGui()
	 */
	@Override
	public void addGui(IGui gui) {
		guis.add(gui);
		
	}

	/**
	 * @see Edi.userCmd.IUserCmd#notifiyGui()
	 */
	@Override
	public void notifiyGui(IStatus newStatus) {
		for (IGui gui : guis) {
			gui.update(newStatus);
			
		}
		
	}

	/**
	 * @see Edi.userCmd.IUserCmd#removeGui()
	 */
	@Override
	public void removeGui(IGui gui) {
		guis.remove(gui);
		
	}
	
	/**
	 * metodo di utilità che consente di mandare un generico messaggio a Scontrol:
	 * lo impacchetta come request e la invia tramite il metodo userCmdDemand.
	 * attende la risposta tramite UserCmdEvalResponse e, sempre grazie a questo metodo
	 * aggiorna lo status. quest'ultima cosa viene fatta anche per il messaggio di disconnessione
	 * è il metodo chiamante (disconnetti)che provvede a mettere correttamente lo status a null.
	 * @param cmd il comando da inviare vedi {@link ComandiUserCmd}
	 * @param idElettrodomestico l'id dell'elettrodomestico da inviare
	 */
	private void mandaComandoAScontrol (ComandiUserCmd cmd,String idElettrodomestico ){
		ComandoUserCmd comando = new ComandoUserCmd(cmd, idElettrodomestico);
		String cmdString = Util.comandoUserCmdToString(comando);
		System.out.println("*****Inviato comando: "+cmdString);
		/*
		 * visto che contact non funziona ho commentato il codice sotto
		 * ora  chiamo direttamente il metodo riceviEdElaboraComandoUserCmdFT di Scontrol
		 * e aggiorno lo staus
		 */
//		// impacchetto il messaggio per contact
//		this.M=cmdString;
//		// invio messaggio e ricezione della risposta. 
//		// nota evalResponse aggiorna lo status
//		try {
//			IAcquireDemandReply m = userCmdDemand();
//			evalResponse( m );
//			System.out.println("*****ricevuto Status : "+status);
//		} catch (Exception e) {
//			System.err.println("problemi nell'invio del messaggio: "+cmdString+" a Scontrol");
//			e.printStackTrace();
//		}
		
		IStatus nuovoStatus = scontrol.riceviEdElaboraComandoUserCmdFT(cmdString);
		this.updateStatus(nuovoStatus);
		
	}
	
	/*
	 * visto che l'invitation non funziona, scontrol invoca direttamente questo metodo
	 * quando vuole comunicare spontaneamente un nuovo stato del sistema ( ad esempio perchè è stato 
	 * disattivato un elettrodomestico) a usercmd
	 */
	public void updateStatus(IStatus status){
		this.status=status;
		this.notifiyGui(status);
		System.out.println("UserCmd:Ricevuto aggiornamento Status\n"+ status);
	}
	//if userCmd must handle request or response
	/**
	 * 
	 * metodo che permette di elaborare la risposta inviata da Scontrol
	 *  nota: per uniformare e semplificare il tutto, anche all'atto della disconnessione
	 *  viene inviato lo stato del sistema e lo si aggiorna. la variabile status viene correttamente
	 *  messa a null alla fine del metodo disconnetti.
	 */ 
	public void evalResponse(IAcquireDemandReply answer) throws Exception{
		while( !answer.demandReplyAvailable() ) {
			showMsg( "no response yet received ... " );
			Thread.sleep(100);
		}
		showMsg( "received " + answer.acquireDemandReply().msgContent());
		String strStatus = answer.acquireDemandReply().msgContent();
		this.updateStatus(Util.stringToStatus(strStatus));
		
	}
	
	
	
	/**
	 * metodo che consente di inviare una request il cui messaggio è il campo M della classe
	 * a Scontrol
	 * @return un oggetto che contiene le informazioni per recuperare la risposta dal supporto
	 * @throws Exception
	 */
	protected IAcquireDemandReply userCmdDemand() throws Exception{
		showMsg(  "Sto per mandare ... " +M );	
	IAcquireDemandReply answer = support.demand( "userCmd", "comandoUserCmd", M, "scontrol");
	showMsg(  "has demanded ... " +M+ "comandoUserCmd" );
	return answer;
		}
	
	/**
	 * metodo che consente di ricevere messaggi inviati spontaneamente da Scontrol, il metodo dojob
	 * provvede a settare correttamente il campo status. 
	 * @return
	 * @throws Exception
	 */
	protected IMessage userCmdAccept() throws Exception{
	IMessage m = support.accept( "userCmd" ,  "status"  );
		showMessage( "accepted", m ) ;
		return m;
	}
	
	
//Local body of the subject
	protected void doJob(){
	try{
 		
	//operationi di prova del supporto contact
	//	IAcquireDemandReply m = userCmdDemand() ;
	//	evalResponse( m );
	//	 	
	//	IMessage m2 = userCmdAccept();
	//	showMessage( "",m2 ) ;
	 
		// operazioni vere: se sono connesso aspetto eventuali messaggi di aggiornamento delllo
		// stato del sistema da Scontrol, e aggiorno lo status
		// è commentato visto che non vanno le invitation di contact.
//		while(true){
//			if(this.isConnesso()){
//				IMessage m = userCmdAccept();
//				showMessage("ricevuto messaggio ", m);
//				String strStatus= m.msgContent();
//				showMsg("nuovoStato: "+strStatus);
//				// aggioro lo stato
//				this.status=Util.stringToStatus(strStatus);
//			}
//		}




	}catch( Exception e ){System.err.println("Errore in UserCmd");e.printStackTrace();}
	}
	
	/**
	 * metodo usato per il test: non manda il comando a Scontrol attraverso 
	 * il supporto contact
	 * @return il comando che, trasformato in stringa, verrebbe inviato a Scontrol
	 */
	public ComandoUserCmd connettiFT(){
		
		this.connesso=true;
		return new ComandoUserCmd(ComandiUserCmd.connetti, "");	
	}
	/**
	 * metodo usato per il test: non manda il comando a Scontrol attraverso 
	 * il supporto contact
	 * @return il comando che, trasformato in stringa, verrebbe inviato a Scontrol
	 */
	public ComandoUserCmd disconnettiFT (){
		
		
		this.connesso=false;
		this.status=null;
		return new ComandoUserCmd(ComandiUserCmd.disconnetti, "");
		
	}
	
	
	/**
	 * metodo usato per il test: non manda il comando a Scontrol attraverso 
	 * il supporto contact
	 * @return il comando che, trasformato in stringa, verrebbe inviato a Scontrol
	 */
	public ComandoUserCmd accendiElettrodomesticoFT (String idElettrodomestico ){
		
		if (connesso)
			return new ComandoUserCmd(ComandiUserCmd.accendi, idElettrodomestico);
		else
			System.err.println ("impossibile accendere un elettrodomestico se non si è connessi a Scontrol");
		return null;
	}
	
	
	/**
	 * metodo usato per il test: non manda il comando a Scontrol attraverso 
	 * il supporto contact
	 * @return il comando che, trasformato in stringa, verrebbe inviato a Scontrol
	 */
	public ComandoUserCmd spegniElettrodomesticoFT (String idElettrodomestico ){
		if (connesso)
			return new ComandoUserCmd(ComandiUserCmd.spegni, idElettrodomestico);
		else
			System.err.println ("impossibile spegnere un elettrodomestico se non si è connessi a Scontrol");
		return null;
	}
	/**
	 * metodo introdotto per il test: consente di simulare la ricezione 
	 * di un nuovo status sotto forma di stringa senza utilizzare il supporto contact. 
	 * @param status il nuovo status di UserCmd
	 */
	public void doJobFT(String stringStatus){
		this.status= Util.stringToStatus(stringStatus);
		
	}


	
}
