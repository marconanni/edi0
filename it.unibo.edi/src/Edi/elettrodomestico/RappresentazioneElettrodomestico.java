package Edi.elettrodomestico;

import java.util.Date;

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
	private int consumo;
	private String idInterruttore;
	private Date oraAccensione;
	
	/**
	 * 
	 * @param id : una stringa di al massimo 10 caratteri che identifica univocamente l'elettrodomestico
	 * @param stato : lo stato dell'elettrodomestico
	 * @param consumoAttuale: il consumo attuale dell'elettrodomestico ( se acceso  l'ultimo consumo registrato, se spento 0)
	 * 						se l'elettrodomestico è disattivato contiene l'ultimo dato di consumo ricevuto
	 * @param idInterruttore : l'identificativo dell'interruttore al quale l'elettrodomestcico è collegato
	 * @param oraAccensione: l'ora dell'ultima accensione dell'elettrodomestico
	 */
	public RappresentazioneElettrodomestico(String id,
			StatoElettrodomestico stato, int consumo,
			String idInterruttore, Date oraAccensione) {
		super();
		this.id = id;
		this.stato = stato;
		this.consumo = consumo;
		this.idInterruttore = idInterruttore;
		this.oraAccensione = oraAccensione;
	}
	


	/**
	 * @see Edi.elettrodomestico.IRappresentazioneElettrodomestico#getStato()
	 */
	public StatoElettrodomestico getStato() {
		return stato;
	}


	


	/**
	 * @see Edi.elettrodomestico.IRappresentazioneElettrodomestico#setStato(Edi.elettrodomestico.StatoElettrodomestico)
	 */
	public void setStato(StatoElettrodomestico stato) {
		this.stato = stato;
	}


	/**
	 * @see Edi.elettrodomestico.IRappresentazioneElettrodomestico#getConsumoAttuale()
	 */
	public int getConsumo() {
		return consumo;
	}


	/**
	 * @see Edi.elettrodomestico.IRappresentazioneElettrodomestico#setConsumoAttuale(int)
	 */
	public void setConsumo(int consumo) {
		this.consumo = consumo;
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



	/**
	 * @see Edi.elettrodomestico.IRappresentazioneElettrodomestico#getOraAccensione()
	 */
	public Date getOraAccensione() {
		return oraAccensione;
	}



	/**
	 * @see Edi.elettrodomestico.IRappresentazioneElettrodomestico#setOraAccensione(java.util.Date)
	 */
	public void setOraAccensione(Date oraAccensione) {
		this.oraAccensione = oraAccensione;
	}
	
	
	
	
	
	

}
