package edi.messaggi;

/**
 * 
 * @author Marco
 * Interfaccia del generico comando inviato da UserCmd e da Scontrol.
 */

public  interface IComando {

	/**
	 * @return l'id dell'elettrodomestico oggetto del comando
	 */
	public abstract String getIdElettrodomestico();

}