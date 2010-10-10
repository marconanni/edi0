package Edi.elettrodomestico;

	import Edi.Subject;
import it.unibo.platform.medcl.*;
import it.unibo.platform.lindaLike.IMessage;
import Edi.messaggi.*;
	
/**
 * 
 * @author Marco
 *
 *classe che modella il sensore montato sull'elettrodomestico il quale provvede, quando l'elettrodomestico
 *è acceso ad inviare i dati di consumo a Scontrol.
 */
public class  Sensore extends Subject implements ISensore{
	
	//Local state of the subject
		private String M = "someContent";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
		private long intervalloInvio;
		private IElettrodomestico elettrodomestico;
		
	
	
		
	/**
	 * 	
	 * @param intervalloInvio: l'intervallo di invio dei dati di consumo quando l'elettrodomestico
	 * è acceso
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
		M= Util.datiSensoreToString(datiSensore);
		try {
			this.sensoreEmit();
		} 
		catch( Exception e ){System.err.println("Errore in Sensore");e.printStackTrace();}
		
		
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
	 * dojob è il metodo eseguito dal metodo run della superclasse Subject, di fatto p il vero corpo del
	 * metodo run. 
	 * il sensore valuta se l'elettrodomestico a cui è collegato è acceso, in tal caso invia i dati e aspetta
	 * un intervallo di tempo a pari a intervalloInvio millisecondi.
	 */
	
	
	/*
	 *  valuta la possibilità di fare aspettare il thrad solo se c'è l'invio di dati. cio renderebbe il modello
	 *  più vicino a quello reale, ma temo che l'eccesivo polling sullo stato quando l'elettrodomestico è spento
	 *  sia troppo pesante, in tal caso valuta la possibilita di introrurre un pattern update per il cambiamento
	 *  dello stato dell'elettrodomestico.
	 */
	
	protected void doJob(){
	
		while (true){
			if (this.elettrodomestico.isOn()){
				this.sendData();				
			}
			try {
				wait(this.intervalloInvio);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
 	 

		}
	
	}
}
