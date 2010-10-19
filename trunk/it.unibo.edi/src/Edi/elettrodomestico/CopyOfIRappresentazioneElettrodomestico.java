package Edi.elettrodomestico;

import java.util.Date;

public interface CopyOfIRappresentazioneElettrodomestico {

	/**
	 * 
	 * @return l'ora dell'ultima accensione dell'elettrodomestico
	 */
	public Date getOraAccensione();

	/**
	 * 
	 * @param oraAccensione: l'ora dell'ultima accensione dell'elettrodomestico
	 */
	public void setOraAccensione(Date oraAccensione);

}