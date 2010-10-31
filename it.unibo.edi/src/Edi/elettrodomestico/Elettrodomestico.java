package Edi.elettrodomestico;

import java.util.Date;

/**
 * 
 * @author Marco
 * classe che rappresenta l'elettrodomestico.
 * Si ricorda che è un dato sencondo il modello EBC,
 *  quindi è un oggetto classico e non un thread
 */
public class Elettrodomestico implements IElettrodomestico {
	
	private StatoElettrodomestico stato;
	private String id;
	private int consumoEsercizio;
	private int consumoAvvio;
	private Date oraAccensione;
	
	/**
	 * Costruttore per la classe elettrodomestico che consente di crearne uno
	 * anche già acceso
	 * @param stato lo stato dell'elettrodomestico vedi {@link StatoElettrodomestico}
	 * @param id l'identificativo dell'elettrodomestico è una stringa di al massimo 10 caratteri
	 * @param consumoEsercizio è il consumo dell'elettrodomestico durante la fase di esercizio
	 * @param consumoAvvio  è il consumo dell'elettrodomestico nei primi 6 secondi di accensione,
	 *  è il doppio del consumo di esercizio
	 * @param oraAccensione l'ora di accensione dell'elettrodomestico. Va impostata a null 
	 *  se l'elettrodomestico è spento o disattivato
	 */
	public Elettrodomestico(StatoElettrodomestico stato, String id,
			int consumoEsercizio, int consumoAvvio, Date oraAccensione) {
		super();
		this.stato = stato;
		this.id = id;
		this.consumoEsercizio = consumoEsercizio;
		this.consumoAvvio = consumoAvvio;
		if (stato == StatoElettrodomestico.avvio || stato == StatoElettrodomestico.esercizio) {
			this.oraAccensione = oraAccensione;
		}
		/* volontariamente non ho messo la creazione del sensore e dell'interruttore correlati
		 * all'elettrodomestico. Questi sono processi. E' bene che siano creati a mano da chi crea
		 * l'elettrodomestico. Ciò consente che Scontrol abbia oggetti elettrodomestici che rappresentano
		 * dati puri relativi all'elettrodomestico controllato senza dover creare un sensore ed un 
		 * interruttore.
		 * 
		 */
	}
	
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#isOn()
	 */
	public boolean isOn(){
		return isAvvio()||isEsercizio();
	}
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#isAvvio()
	 */
	public boolean isAvvio(){
		return this.stato==StatoElettrodomestico.avvio;
	}
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#isEsercizio()
	 */
	public boolean isEsercizio(){
		return this.stato==StatoElettrodomestico.esercizio;
	}
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#isOff()
	 */
	public boolean isOff(){
		return this.isDisattivato()||stato==StatoElettrodomestico.spento;
	}
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#isDisattivato()
	 */
	public boolean isDisattivato(){
		return this.stato==StatoElettrodomestico.disattivato;
	}
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#isSpento()
	 */
	public boolean isSpento(){
		return this.stato==StatoElettrodomestico.spento;
	}
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#getStato()
	 */
	public StatoElettrodomestico getStato(){
		return this.stato;
	}
	
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#getId()
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#getConsumoEsercizio()
	 */
	public int getConsumoEsercizio() {
		return consumoEsercizio;
	}

	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#getConsumoAvvio()
	 */
	public int getConsumoAvvio() {
		return consumoAvvio;
	}

	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#getOraAccensione()
	 */
	public Date getOraAccensione() {
		return oraAccensione;
	}
	
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#getConsumoAttuale()
	 */
	public int getConsumoAttuale(){
		if(this.stato==StatoElettrodomestico.avvio)
			return this.consumoAvvio;
		else 
			if(this.stato==StatoElettrodomestico.esercizio)
				return this.consumoEsercizio;
			else		
				return 0;
	}
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#accendi()
	 */
	public void accendi(){
		if (this.stato == StatoElettrodomestico.spento || this.stato== StatoElettrodomestico.disattivato){
			Thread th1 = new Thread ()
			{public void run(){try {
				sleep(6000);
				stato= StatoElettrodomestico.esercizio;
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}}};
			this.stato=StatoElettrodomestico.avvio;
			this.setOraAccensione(new Date(System.currentTimeMillis()));
			th1.start();
		}
	}
	
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#spegni()
	 */
	public void spegni(){		
			this.stato = StatoElettrodomestico.spento;
			this.oraAccensione=null;
		
	}
	
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#disattiva()
	 */
	public void disattiva() {
			this.stato = StatoElettrodomestico.disattivato;
			this.oraAccensione= null;
		
	}
	
	/**
	 * @see Edi.elettrodomestico.IElettrodomestico#riattiva()
	 */
	public void riattiva(){
		this.accendi();
	}
	
	

	/**
	 * imposta l'ora di accensione dell'elettrodomestico.
	 * @param oraAccensione l'ora di accensione dell'elettrodomestico.
	 */
	private void setOraAccensione(Date oraAccensione) {
		this.oraAccensione = oraAccensione;
	}
	
	
	
	

}
