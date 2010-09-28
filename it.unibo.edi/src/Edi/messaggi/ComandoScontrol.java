/**
 * 
 */
package Edi.messaggi;

/**
 * @author Marco
 * 
 * classe che rappresenta il comando inviato da Scontrol ad un elettrodomestico;
 * contiene il comando e l'id dell'elettrodomestico al quale il comando viene inviato.
 *
 */
public class ComandoScontrol extends Comando {
	
	private ComandiScontrol comando;
	
	

	public ComandoScontrol(String idElettrodomestico, ComandiScontrol comando) {
		super(idElettrodomestico);
		this.comando = comando;
	}



	public ComandiScontrol getComando() {
		return comando;
	}
	
	

}
