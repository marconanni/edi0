package Edi.messaggi;

public class DatiSensore implements IDatiSensore {
	
	private String id;
	private int consumoAttuale;
	
	
	
	/**
	 * 
	 * @param id l'identificativo dell'elettrodomestico
	 * 
	 * @param consumoAttuale il consumo attuale dell'elettrodomestico;
	 */

	public DatiSensore(String id, int consumoAttuale) {
		super();
		this.id = id;
		this.consumoAttuale = consumoAttuale;
	}
	
	/**
	 * 
	 * @return l'id dell'elettrodomestico che sta mandando i dati di consumo
	 */

	@Override
	public int getConsumoAttuale() {
		
		return consumoAttuale;
	}

	
	/**
	 * 
	 * @return il consumo attuale dell'elettrodomestico che sta mandando i dati di consumo
	 */
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

}
