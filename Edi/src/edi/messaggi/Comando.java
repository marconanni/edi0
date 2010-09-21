package edi.messaggi;

/**
 * 
 * @author Marco
 *
 *	superclasse dei comandi inviati da Scontrol e di quelli inviati da UserCmd.
 *contiene l'id dell'elettrodomestico oggetto del comando
 */

public abstract class Comando implements IComando {
	
	/**
	 * 
	 * @param idElettrodomestico l'id dell'elettrodomestico ogggetto del comando;
	 */
	
	public Comando(String idElettrodomestico) {
		super();
		this.idElettrodomestico = idElettrodomestico;
	}

	private String idElettrodomestico;

	/**
	 * @return l'id dell'elettrodomestico oggetto del comando
	 */
	public String getIdElettrodomestico() {
		return idElettrodomestico;
	}

}
