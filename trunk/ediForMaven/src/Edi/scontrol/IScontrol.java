package Edi.scontrol;

public interface IScontrol {

	/**
	 * provvede ad accendere l'elettrodomestico indicato: chiama turnOn sull' interruttore collegato allleletrodomesticoIndicato
	 * imposta l'ora di accensione, cambia lo stato dell'elettrodomestico in avvio 
	 * e fa partire  e si registra al timer collegato all'interruttore
	 * 
	 * 
	 * NON PROVVEDE AD INVIARE LO STATO AGGIORNATO A SCONTROL
	 * @param idElettrodomestico: l'id dell'elettrodomestico da accendere
	 */
	public void accendiElettrodomestico(String idElettrodomestico);

	/**
	 * provvede a spegnere l'elettrodomestico indicato: chiama turnOff sull' interruttore collegato allleletrodomesticoIndicato
	 * e cambia lo stato dell'elettrodomestico in spento, azzerando il consumo attuale.
	 * Infine fa in modo che il timer non gli invii nessuna notifica se, come ovvio, non si ricevono più dati dal sensore
	 * NON PROVVEDE AD INVIARE LO STATO AGGIORNATO A SCONTROL
	 * @param idElettrodomestico: l'id dell'elettrodomestico da spegnere
	 */
	public void spegniElettrodomestico(String idElettrodomestico);

}