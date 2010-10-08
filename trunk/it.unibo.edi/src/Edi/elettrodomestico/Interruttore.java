package Edi.elettrodomestico;

	import Edi.Subject;
	import Edi.messaggi.*;
import it.unibo.platform.medcl.*;
import it.unibo.platform.lindaLike.IMessage;
	
/**
 * 
 * @author Marco
 *
 *classe che provvede a ricevere le richieste da Scontrol e a modificare lo stato dell'elettrodomestico
 *al quale è collegato 
 */
public class  Interruttore extends Subject implements IInterruttore{
	
	//Local state of the subject
		private String M = "someContent";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
	
	//if interruttore must handle request or response
		
		private IElettrodomestico elettrodomestico;
		
		/**
		 * 
		 * @param elettrodomestico l'elettrodomestico al quale l'interruttore è collegato.
		 */
		public Interruttore (IElettrodomestico elettrodomestico){
			this.elettrodomestico = elettrodomestico;
		}
		
		/**
		 * @see Edi.elettrodomestico.IInterruttore#isOn()
		 */
		public boolean isOn(){
			return elettrodomestico.isOn();
		}
		
		/**
		 * @see Edi.elettrodomestico.IInterruttore#isOff()
		 */
		public boolean isOff(){
			return elettrodomestico.isOff();
		}
		
		/**
		 * @see Edi.elettrodomestico.IInterruttore#turnOn()
		 */
		public void turnOn(){
			elettrodomestico.accendi();
		}
		
		/**
		 * @see Edi.elettrodomestico.IInterruttore#turnOff()
		 */
		public void turnOff(){
			elettrodomestico.spegni();
		}
		
		/**
		 * Metodo che discrimina cosa fare in base al comando ricevuto.
		 * visto che nell'implementazione attuale tutti i comandi arrivano da Scontrol
		 * se il messaggio è un comando di accensione provvede a invocare tunOn
		 * se il messaggio è un comando di spengnimento provvede a invocare turOff
		 * @param m il messaggio ricevuto dal supporto Contact
		 */
		private void discriminaRichiesta(IMessage m){
			String richiesta = m.msgContent();
			IComandoScontrol comando = Util.stringToComandoScontrol(richiesta);
			if (comando.getComando()== ComandiScontrol.accendi){
				this.turnOn();
			}
			else if (comando.getComando()== ComandiScontrol.spegni){
				this.turnOff();
			}
		}
	
	
	
	

		
	protected IMessage interruttoreAccept() throws Exception{
	IMessage m = support.accept( "interruttore" ,  "comandoScontrol"  );
		showMessage( "accepted", m ) ;
		return m;
	}
	
	
//Local body of the subject
	
	/**
	 * metodo invcato dal metodo run della superclasse Subject, di fatto il vero corpo del metodo run
	 * l'interruttore si limita a ricevere messaggi-comando da Scontrol ed a agire di conseguenza.
	 */
	protected void doJob(){
	try{
 		
//operation interruttore



		// l'interruttore sta costantemente in attesa di richieste da Scontrol
		while (true){
			IMessage m = interruttoreAccept();
		}
	//showMessage( m ) ;
	 


	}catch( Exception e ){System.err.println("Errore ricezione messaggio");e.printStackTrace();}
	}
}
