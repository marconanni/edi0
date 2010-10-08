package Edi.elettrodomestico;

import java.util.Date;

/**
 * 
 * @author Marco
 *elettrodomestico che consuma 60 uc in esercizio, 120 in avvio
 */
public class ElettrodomesticoMedioConsumo extends Elettrodomestico {
	
	/**
	* @param stato lo stato dell'elettrodomestico vedi {@link StatoElettrodomestico}
	 * @param id l'identificativo dell'elettrodomestico è una stringa di al massimo 10 caratteri
	 * @param oraAccensione l'ora di accensione dell'elettrodomestico. Va impostata a null 
	 *  se l'elettrodomestico è spento o disattivato
	 */
	public ElettrodomesticoMedioConsumo(StatoElettrodomestico stato, String id,
			 Date oraAccensione) {
		super(stato, id, 60, 120, oraAccensione);
		
	}

}
