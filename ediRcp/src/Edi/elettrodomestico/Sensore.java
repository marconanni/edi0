package Edi.elettrodomestico;

	import Edi.Subject;
import it.unibo.platform.medcl.*;
import it.unibo.platform.lindaLike.IMessage;
import Edi.messaggi.*;
import Edi.scontrol.Scontrol;
	
/**
 * 
 * @author Marco
 *
 *classe che modella il sensore montato sull'elettrodomestico il quale provvede, quando l'elettrodomestico
 *� acceso ad inviare i dati di consumo a Scontrol.
 */
public class  Sensore extends Subject implements ISensore{
	
	//Local state of the subject
		private String M = "someContent";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
		private long intervalloInvio;
		private IElettrodomestico elettrodomestico;
		
		// visto che contact non funziona
		private Scontrol scontrol;
		
		public void setScontrol (Scontrol scontrol){
			this.scontrol= scontrol;
		}
	
	
		
	/**
	 * 	
	 * @param intervalloInvio: l'intervallo di invio dei dati di consumo quando l'elettrodomestico
	 * � acceso
	 * @param elettrodomestico: l'elettrodometico collegato al sensore.
	 */
	public Sensore(long intervalloInvio, IElettrodomestico elettrodomestico) {
		super();
		this.intervalloInvio = intervalloInvio;
		this.elettrodomestico = elettrodomestico;
	}
	
	/**
	 * @see Edi.elettrodomestico.ISensore#sendData()
	 */
	public void sendData(){
		IDatiSensore datiSensore =new DatiSensore(this.elettrodomestico.getId(), this.elettrodomestico.getConsumoAttuale());
		
		/*
		 * commentato visto che contact non fuziona viene eseguito il codice sotto che invoca direttamente
		 * i metodi di Scontrol
		 */
//		M= Util.datiSensoreToString(datiSensore);
//		try {
//			this.sensoreEmit();
//		} 
//		catch( Exception e ){System.err.println("Errore in Sensore");e.printStackTrace();}
		
		scontrol.riceviDatiSensoreSC(Util.datiSensoreToString(datiSensore));
	}
	
	/**
	 * metodo del supporto contact che provvede ad inviare la stringa M 
	 * a Scontrol.
	 * @throws Exception
	 */
	protected void sensoreEmit() throws Exception{
		support.emit( "sensore", "datiSensore" , M);
		showMsg( "sensore" + " has emitted ... " + "datiSensore" );
	}
	

	




	/**
	 * dojob � il metodo eseguito dal metodo run della superclasse Subject, di fatto p il vero corpo del
	 * metodo run. 
	 * il sensore valuta se l'elettrodomestico a cui � collegato � acceso, in tal caso invia i dati e aspetta
	 * un intervallo di tempo a pari a intervalloInvio millisecondi.
	 */
	
	
	/*
	 *  valuta la possibilit� di fare aspettare il thrad solo se c'� l'invio di dati. cio renderebbe il modello
	 *  pi� vicino a quello reale, ma temo che l'eccesivo polling sullo stato quando l'elettrodomestico � spento
	 *  sia troppo pesante, in tal caso valuta la possibilita di introrurre un pattern update per il cambiamento
	 *  dello stato dell'elettrodomestico.
	 */
	
	protected void doJob(){
	
		while (true){
			if (this.elettrodomestico.isOn()){
				this.sendData();				
			}
			try {
				sleep(this.intervalloInvio);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
 	 

		}
	
	}

	/**
	 * metodo usato per fare test, visto che il metodo sendData originale
	 * chiamerebbe il supporto contact
	 * @return la stringa che verrebbe inviata da sendData tramite il supporto contact
	 */
	public String sendDataFT(){
		IDatiSensore datiSensore =new DatiSensore(this.elettrodomestico.getId(), this.elettrodomestico.getConsumoAttuale());
		return Util.datiSensoreToString(datiSensore);
	
	}
	
	/**
	 * questo metodo � stato introdotto per il testing della classe.
	 * non potendo far partire il thread e inviare i messaggi tramite contact
	 * questo metodo simula l'esecuzione di una sola ripetizione del ciclo infinito del metodo
	 * originale.
	 * @return  se l'elettrodomestico � acceso ritorna, tramite sendDataFT
	 * la stringa che verrebbe inviata tramite contact, se � spento ritorna null.
	 */
	public String doJobFT(){
		
		
			if (this.elettrodomestico.isOn()){
				
				return this.sendDataFT();				
			}
			else
				return null;
			
 	 

		
	
	}
}
