/**
 * 
 */
package Edi.messaggi;

/**
 * @author Marco
 * 
 * classe che rappresenta il comando inviato da Scontrol all'interruttore;
 * contiene il comando in questione come enumerativo  (ComandiScontrol)
 *
 */
public class ComandoScontrol implements IComandoScontrol  {
	
	private ComandiScontrol comando;
	
	

	/**
	 * costruisce un oggetto che rappresenta il comando inviato da Scontrol ad un interruttore
	 * @param comando  da inviare all'interruttore
	 */
	public ComandoScontrol(ComandiScontrol comando) {
		
		this.comando = comando;
	}



	/**
	 * @see Edi.messaggi.IComandoScontrol#getComando()
	 */
	public ComandiScontrol getComando() {
		return comando;
	}
	
	

}
