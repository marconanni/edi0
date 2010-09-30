/**
 * 
 */
package Edi.messaggi;


import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author Marco
 * 
 * classe che permette di trasformare istanze di classi del package messaggi in stringhe in modo che
 * queste possano essere trasmesse tramite infrastruttura contact. Fornisce anche i metodi per fare
 * l'operazione inversa, ossia trasformare le stringhe ricevute in oggetti.
 *
 */
public class Util {
	
	/**
	 * metodo che permette di trasformare in stringa un oggetto che implementa IStatus;
	 * la stringa è così formata: nella prima riga, separata da punto e virgola ci sono i campi 
	 * comunicazione;consumoAttualeComplessivo;soglia
	 * nelle righe seguemti, una per ogni report elettrodomestico ci sono , riferite al singolo report
	 * idElettrodomestico;stato;consumoAttuale.
	 * 
	 * per chiarire ecco come starebbe una stringa nel caso di due elettrodomestici
	 * 
	 * tutto bene;30;100
	 * e1;spento;0
	 * e2;acceso;30
	 * 
	 * @param status; un'oggetto che implementa IStatus e che contiene le informazioni sul sistema da inviare
	 * @return una stringa che contiene le informazioni di status nella forma indicata sopra
	 */
	public static String statutsToString (IStatus status){
		
		String stringona ="";
		// inserimento nella stringa di comunicazione; consumoAttuale; soglia
		stringona= stringona+status.getComunicazione();
		stringona= stringona+";"+status.getConsumoAttualeComplessivo();
		stringona= stringona+";"+status.getSoglia();
		// per ogni elettrodomestico inserisco una riga con id;stato;consumoAttuale;
		if(status.getReports()!=null){ // controllo inutile: repors può essere vuoto, ma non è mai nullo!
			Vector<IReportElettrodomestico> reports = status.getReports();
			for (int k=0;k<reports.size();k++){
				stringona= stringona+"\n";
				IReportElettrodomestico report = reports.get(k);
				stringona=stringona+report.getIdElettrodomestico()+";"+report.getStato()+";"+report.getConsumoAttuale();
			}
		}
		
		
		return stringona;
	}
	
	public static IStatus stringToStatus(String string){
		StringTokenizer tokenizer = new StringTokenizer(string,";\n");
		// leggo comunicazione, consumoAttuale, soglia di consumo.
					
	}

}
