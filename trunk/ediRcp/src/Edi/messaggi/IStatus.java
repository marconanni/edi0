package Edi.messaggi;

import java.util.Vector;

public interface IStatus {

	/**
	 * 
	 * @return  comunicazione: un'eventuale messaggio testuale da mandare a UserCmd ( e quindi all'utente)
	 */
	public abstract String getComunicazione();

	/**
	 * 
	 * @return consumoAttualeComplessivo: il consumo complessivo del sistema, ottenuto sommando il consumo attuale di tutti gli elettrodomestici.
	 */
	public abstract int getConsumoAttualeComplessivo();

	/**
	 * 
	 * @return reports  un Vector di Report degli elettrodomestici presenti nel sistema vedi {@link IReportElettrodomestico} e {@link ReportElettrodomestico}
	 */
	public abstract Vector<IReportElettrodomestico> getReports();

	/**
	 * 
	 * @return soglia: la soglia di consumo massimo del sistema.
	 */
	public abstract int getSoglia();

}