package Edi.elettrodomestico;

/**
 * 
 * @author Marco
 *
 *Questa classe è quella usata da Scontrol per registrare la caratteristiche degli elettrodomestici
 * collegati. In particolare è scontrol che aggiorna lo stato ed il consumo dell'elettrodomestico
 *  sulla base dei dati raccolti dal sensore. La classe consente, inoltre, un reperimento più comodo,
 *   veloce ed efficiente dell' identificativo dell'interruttore collegato all'elettrodomestico.
 *   Costituisce, di fatto, la rappresentazione che Scontrol fa dell'elettrodomestico sulla 
 *   base delle informazioni cui ha accesso.
 */
public class RappresentazioneElettrodomestico implements IRappresentazioneElettrodomestico {
	
	private String id;
	private StatoElettrodomestico stato;
	private int consumoAttuale;
	private String idInterruttore;
	
	/**
	 * 
	 * @param id : una stringa di al massimo 10 caratteri che identifica univocamente l'elettrodomestico
	 * @param stato : lo stato dell'elettrodomestico
	 * @param consumoAttuale: il consumo attale dell'elettrodomestico ( se acceso l'ultimo consumo registrato, se spento 0)
	 * @param idInterruttore : l'identificativo dell'interruttore al quale l'elettrodomestcico è collegato
	 */
	public RappresentazioneElettrodomestico(String id,
			StatoElettrodomestico stato, int consumoAttuale,
			String idInterruttore) {
		super();
		this.id = id;
		this.stato = stato;
		this.consumoAttuale = consumoAttuale;
		this.idInterruttore = idInterruttore;
	}


	/* (non-Javadoc)
	 * @see Edi.elettrodomestico.IRappresentazioneElettrodomestico#getStato()
	 */
	public StatoElettrodomestico getStato() {
		return stato;
	}


	/* (non-Javadoc)
	 * @see Edi.elettrodomestico.IRappresentazioneElettrodomestico#setStato(Edi.elettrodomestico.StatoElettrodomestico)
	 */
	public void setStato(StatoElettrodomestico stato) {
		this.stato = stato;
	}


	/* (non-Javadoc)
	 * @see Edi.elettrodomestico.IRappresentazioneElettrodomestico#getConsumoAttuale()
	 */
	public int getConsumoAttuale() {
		return consumoAttuale;
	}


	/* (non-Javadoc)
	 * @see Edi.elettrodomestico.IRappresentazioneElettrodomestico#setConsumoAttuale(int)
	 */
	public void setConsumoAttuale(int consumoAttuale) {
		this.consumoAttuale = consumoAttuale;
	}


	/**
	 * @see Edi.elettrodomestico.IRappresentazioneElettrodomestico#getIdInterruttore()
	 */
	public String getIdInterruttore() {
		return idInterruttore;
	}


	/**
	 * @see Edi.elettrodomestico.IRappresentazioneElettrodomestico#setIdInterruttore(java.lang.String)
	 */
	public void setIdInterruttore(String idInterruttore) {
		this.idInterruttore = idInterruttore;
	}


	/**
	 * @see Edi.elettrodomestico.IRappresentazioneElettrodomestico#getId()
	 */
	public String getId() {
		return id;
	}
	
	
	
	
	
	

}
