package Edi.elettrodomestico;

import java.util.Date;

/**
 * 
 * @author Marco
 *elettrodomestico che consuma 30 uc in esercizio, 60 in avvio
 */
public class ElettrodomesticoBassoConsumo extends Elettrodomestico {

	
	/**
	* @param stato lo stato dell'elettrodomestico vedi {@link StatoElettrodomestico}
	 * @param id l'identificativo dell'elettrodomestico è una stringa di al massimo 10 caratteri
	 * @param oraAccensione l'ora di accensione dell'elettrodomestico. Va impostata a null 
	 *  se l'elettrodomestico è spento o disattivato
	 */
	public ElettrodomesticoBassoConsumo(StatoElettrodomestico stato, String id,
			 Date oraAccensione) {
		super(stato, id, 30, 60, oraAccensione);
		// TODO Auto-generated constructor stub
	}
	
	

}
