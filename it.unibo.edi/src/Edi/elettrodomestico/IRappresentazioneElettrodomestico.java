package Edi.elettrodomestico;

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
	 */
	public int getConsumoAttuale();

	/**
	 * 
	 * @param consumoAttuale il consumo attale dell'elettrodomestico ( se acceso l'ultimo consumo registrato, se spento 0)
	 */
	public void setConsumoAttuale(int consumoAttuale);

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

}