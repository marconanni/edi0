package Edi.elettrodomestico;


/**
 * 
 * @author Marco
 *
 *interfaccia che modella il sensore montato sull'elettrodomestico il quale provvede, quando l'elettrodomestico
 *è acceso ad inviare i dati di consumo a Scontrol.
 */
public interface ISensore {

	/**
	 * metodo che consente di inviare i dati di consumo dell'elettrodoemstico collegato al sensore a Scontrol.
	 */
	public void sendData();

	

}