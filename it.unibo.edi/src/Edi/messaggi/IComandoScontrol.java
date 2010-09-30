package Edi.messaggi;

/**interfaccia che rappresenta il comando inviato da Scontrol all'interruttore;
* contiene il comando in questione come enumerativo  (ComandiScontrol)
*/
public interface IComandoScontrol {
	
	
	/**
	 * 
	 * @return il comando inviato all'interruttore
	 */

	public abstract ComandiScontrol getComando();

}