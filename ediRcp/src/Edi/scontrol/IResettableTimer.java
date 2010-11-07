package Edi.scontrol;

public interface IResettableTimer {

	/**
	 * 
	 * @return true se il timer è stato resettato
	 */
	public boolean isResettato();

	/**
	 * 
	 * @return l'id dell'elettrodomestico al quale il timer è collegato
	 */
	public String getIdElettrodomestico();

	/**
	 * 
	 * @param idElettrodomestico l'id dell'elettrodomestico al quale il timer è collegato
	 */
	public void setIdElettrodomestico(String idElettrodomestico);

	/**
	 * 
	 * @return l'intervallo dopo il quale viene lanciato l'evento di timeout scaduto
	 */
	public long getEventTime();

	/**
	 * 
	 * @param eventTime l'intervallo dopo il quale viene lanciato l'evento di timeout scaduto
	 */
	public void setEventTime(long eventTime);

	/**
	 * 
	 * @param observer un oggetto che verrà avvisato qualora scatti il timeout
	 */
	public void addObserver(IObserver observer);

	/**
	 * 
	 * @param observer un oggetto che verrà avvisato qualora scatti il timeout
	 */
	public void removeObserver(IObserver observer);

	/**
	 * avvisa tutti gli oggetti registrati che è scattato il timeout
	 */
	public void notifyObservers();

	/**
	 * resetta il timeout.
	 */
	public void resetta();

	/**
	 * fa partire il conto alla rovescia
	 */
	public void avvia();

}