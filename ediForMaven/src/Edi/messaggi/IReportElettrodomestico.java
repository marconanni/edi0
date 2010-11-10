package Edi.messaggi;

import Edi.elettrodomestico.StatoElettrodomestico;

/**
 * 
 * @author Marco
 *interfaccia che rappresenta il report per un singolo elettrodomestico; contiene 
 *l'identificativo dell'elettrodomestico, il suo stato e il suo consumo attuale;
 *
 */

public interface IReportElettrodomestico {

	/**
	 * 
	 * @return l'identificativo dell'elettrodomestico
	 */
	public abstract String getIdElettrodomestico();

	/**
	 * 
	 * @return lo stato attuale del'elettrodomestico come  {@link StatoElettrodomestico}
	 */

	public abstract StatoElettrodomestico getStato();

	/**
	 * 
	 * @return il cosumo attuale dell'elettrodomestico
	 */
	public abstract int getConsumoAttuale();

}