package Edi.elettrodomestico;

import java.util.Date;

public interface IRappresentazioneElettrodomestico {

	/**
	 * 
	 * @return lo stato dell'elettrodomestico
	 */
	public StatoElettrodomestico getStato();

	/**
	 * 
	 * @param stato: lo stato dell'elettrodomestico
	 */
	public void setStato(StatoElettrodomestico stato);

	/**
	 * 
	 * @return il consumo attale dell'elettrodomestico ( se acceso l'ultimo consumo registrato, se spento 0)
	 *	se l'elettrodomestico è disattivato contiene l'ultimo dato di consumo ricevuto
	 */
	public int getConsumo();

	/**
	 * 
	 * @param consumoAttuale il consumo attale dell'elettrodomestico ( se acceso l'ultimo consumo registrato, se spento 0)
	 */
	public void setConsumo(int consumoAttuale);

	/**
	 * 
	 * @return l'identificativo dell'interruttore al quale l'elettrodomestcico è collegato
	 */
	public String getIdInterruttore();

	/**
	 * 
	 * @param idInterruttore l'identificativo dell'interruttore al quale l'elettrodomestcico è collegato
	 */
	public void setIdInterruttore(String idInterruttore);

	/**
	 * 
	 * @return l'identificativo dell'elettrodomestico.
	 */
	public String getId();

	/**
	 * 
	 * @return l'ora dell'ultima accensione dell'elettrodomestico
	 */
	public Date getOraAccensione();

	/**
	 * 
	 * @param oraAccensione: l'ora dell'ultima accensione dell'elettrodomestico
	 */
	public void setOraAccensione(Date oraAccensione);

}