package Edi.elettrodomestico;

import java.util.Date;

/**
 * 
 * @author Marco
 * interfaccia che rappresenta l'elettrodomestico.
 * Si ricorda che è un dato sencondo il modello EBC,
 *  quindi è sempre realizzata da un oggetto passivo
 */
public interface IElettrodomestico {

	/**
	 * 
	 * @return true se l'elettrodomestico è in funzione, ossia 
	 *  se il suo stato è avvio o esercizio
	 */
	public boolean isOn();

	/**
	 * 
	 * @return true se l'elettrodomestico è in fase di avvio
	 */
	public boolean isAvvio();

	/**
	 * 
	 * @return true se l'elettrodomestico è in fase di esercizio
	 */
	public boolean isEsercizio();

	/**
	 * 
	 * @return true se l'elettrodomestico è spento o disattivato
	 */
	public boolean isOff();

	/**
	 * 
	 * @return true se l'elettrodomestico stato disattivato per evitare 
	 * il superamento della soglia di consumo.
	 */
	public boolean isDisattivato();
	/**
	 * 
	 * @return true se l'elettrodomestico è spento.
	 */
	public boolean isSpento();

	/**
	 * 
	 * @return lo stato dell'elettrodomestico vedi {@link StatoElettrodomestico}
	 */
	public StatoElettrodomestico getStato();

	/**
	 * 
	 * @return una stringa di al massimo 10 caratteri che identifica
	 * univocamente l'elettrodomestico nel sistema.
	 */
	public String getId();

	/**
	 * 
	 * @return il consumo dell'elettrodomestico durante la fase di esercizio
	 */
	public int getConsumoEsercizio();

	/**
	 * 
	 * @return il consumo dell'elettrodomestico nei primi 6 secondi di accensione,
	 *  è il doppio del consumo di esercizio
	 */
	public int getConsumoAvvio();

	/**
	 * 
	 * @return l'ora dell'ultima volta in cui l'elettrodomestico è stato acceso o riattivato
	 */
	public Date getOraAccensione();

	/**
	 * 
	 * @return il consumo attuale dell'elettrodomestico. Può essere il consumo di avvio, quello di esercizio
	 * o zero se l'elettrodomestico è spento o disattivato.
	 */
	public int getConsumoAttuale();

	/**
	 * accende l'eletrodomestico: imposta l'ora di accensione, eventualmente sovrascrivendola
	 * parte in fase di avvio poi, dopo 6 secondi, cambia 
	 * lo stato in esercizio. 
	 */
	public void accendi();

	/**
	 * spegne l'elettrodomestico impostanto il suo stato a spento
	 */
	public void spegni();

	/**
	 * distattiva l'elettrodomestico impostando il suo stato a disattivato.
	 * lascia l'ora di accensioni inalterata in modo che sia disponibile per una
	 * eventuale politica di riattivazione che ne necessiti.
	 * 
	 * NOTA: non viene tenuto conto del fatto che un elettrodomestico in fase di avvio
	 * non possa essere disattivato: fa parte delle politiche di Scontrol ed è giusto che
	 * l'elettrodomestico ne sia all'oscuro.
	 */
	public void disattiva();

	/**
	 * riattiva l'elettrodomestico, con l'implementazione attuale
	 * è equivalente alla chiamata accendi
	 */
	public void riattiva();

}