package Edi.userCmd;

import Edi.Subject;
import Edi.messaggi.*;
import it.unibo.platform.medcl.*;
import it.unibo.platform.lindaLike.IMessage;
	

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
public class  UserCmd extends Subject{
	
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
		
		
		
		
	
	private UserCmd() {
			this.connesso= false;
			status = null;
			
		}
	
	// TODO cambiare il tipo di ritorno da UserCmd a IUserCmd
	public static UserCmd getInstance(){
		if (instance ==null){
			instance = new UserCmd();
		}
		return instance;
	}
	
	/**
	 * metodo che consente di connettersi a Scontrol. viene inviato a scontrol un messaggio
	 * di richesta di connessione. questi lo accetta e ritorna lo stato aggiornato del sistema
	 * che viene salvato nella variabile status.
	 */
	public void connetti(){
		// invio la richiesta di connessione a Scontrol
		ComandoUserCmd comando = new ComandoUserCmd(ComandiUserCmd.connetti, "");
		String cmdString = Util.comandoUserCmdToString(comando);
		// impacchetto il messaggio per contact
		this.M=cmdString;
		// invio messaggio e ricezione della risposta. 
		// nota evalResponse aggiorna lo status
		try {
			IAcquireDemandReply m = userCmdDemand();
			evalResponse( m );
		} catch (Exception e) {
			System.err.println("problemi nell'invio del messaggio di connessione a Scontrol");
			e.printStackTrace();
		}
		this.connesso=true;		
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
		this.status=Util.stringToStatus(strStatus);
		
	}
	
	
	
	
	protected IAcquireDemandReply userCmdDemand() throws Exception{
		
	IAcquireDemandReply answer = support.demand( "userCmd", "comandoUserCmd", M, "scontrol");
	showMsg(  "has demanded ... " + "comandoUserCmd" );
	return answer;
		}
	

	
//Local body of the subject
	protected void doJob(){
	try{
 		
//operation userCmd
		// TODO decommentare invio richiesta.
//	IAcquireDemandReply m = userCmdDemand() ;
//	evalResponse( m );
	 





	}catch( Exception e ){System.err.println("Errore in UserCmd");e.printStackTrace();}
	}
}
