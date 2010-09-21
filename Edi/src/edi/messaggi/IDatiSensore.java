package edi.messaggi;

public interface IDatiSensore {
	
	
	/**
	 * 
	 * @return l'id dell'elettrodomestico che sta mandando i dati di consumo
	 */
	public String getId();
	
	/**
	 * 
	 * @return il consumo attuale dell'elettrodomestico che sta mandando i dati di consumo
	 */
	public int getConsumoAttuale();

}
