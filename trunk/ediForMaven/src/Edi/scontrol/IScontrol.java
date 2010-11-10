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
	 * Infine si deregistra dal timer, in modo che  quando scatta non si riceve l'evento 
	 * NON PROVVEDE AD INVIARE LO STATO AGGIORNATO A SCONTROL
	 * @param idElettrodomestico: l'id dell'elettrodomestico da spegnere
	 */
	// TODO:  vedi se c'è il modo di forzare la terminazione del thread in modo che l'evento non venga creato del tutto
	public void spegniElettrodomestico(String idElettrodomestico);

}