package Edi.messaggi;

/**
 * 
 * @author Marco
 * Interfaccia del generico comando inviato da UserCmd a Scontrol.
 */

public  interface IComandoUserCmd {

	/**
	 * @return l'id dell'elettrodomestico oggetto del comando,  potrebbe essere null nel caso il comando inviato sia connetti o disconnetti.
	 */
	public abstract String getIdElettrodomestico();
	
	/**
	 * 
	 * @return il comando inviato a Scontrol  come enumerativo {@link ComandiUserCmd}
	 */
	public ComandiUserCmd getComando();

}