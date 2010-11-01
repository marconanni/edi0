/**
 * 
 */
package Edi.messaggi;


import java.util.StringTokenizer;
import java.util.Vector;

import Edi.elettrodomestico.StatoElettrodomestico;

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
	
	/**
	 * Metodo che consente di recuperare un oggetto status da una stringa formata secondo la covenzione 
	 * indicata nel metodo  statusToString, probabilmente ottenuta da un supporto di comunicazione:
	 * 
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
	 * @param string la stringa dalla quale estrarre lo Status inviato. vedere la descrizione del metodo
	 * per comprendere come la stringa debba essere formata.
	 * @return un oggetto IStatus che contiene i dati presenti nella stringa
	 */
	public static IStatus stringToStatus(String string){
		StringTokenizer tokenizer = new StringTokenizer(string,";\n");
		// leggo comunicazione, consumoAttuale, soglia di consumo.
		String comunicazione;
		int consumoAttualeComplessivo;
		int soglia;
		comunicazione= tokenizer.nextToken();
		consumoAttualeComplessivo=Integer.parseInt( tokenizer.nextToken());
		soglia= Integer.parseInt( tokenizer.nextToken());
		
		Vector<IReportElettrodomestico> reports = new Vector<IReportElettrodomestico>();
		// se c'è almeno altro token, significa che c'è un'altra riga (formata da 3 token :id, stato, consumoAttuale), quindi la leggo e aggiungo i dati del dispositivo.
		while (tokenizer.hasMoreTokens()){
			String id = tokenizer.nextToken();
			StatoElettrodomestico stato = StatoElettrodomestico.valueOf(tokenizer.nextToken());
			int consumoAttuale= Integer.parseInt(tokenizer.nextToken());
			ReportElettrodomestico reportElettrodomestico = new ReportElettrodomestico(id, stato, consumoAttuale);
			reports.add(reportElettrodomestico);
			
		}
		
		// impacchetto tutto in un oggetto Status e lo restituisco.
		
		Status status = new Status(comunicazione, reports, soglia);
		return status;		
					
	}
	
	/**
	 * metodo che trasforma un oggetto che implementa {@link IDatiSensore} in una stringa
	 * la stringa è formata nel seguente modo:
	 * idElettrodomestico;consumoAttuale
	 * 
	 * dove IdElettrodomestico è l'id dell'elettrodomestico sul quale è montato il sensore
	 * 
	 * @param datiSensore l'oggetto da trasformare in stringa
	 * @return un striga che rappresenta i dati del sensore passati come parametri:
	 * idElettrodomestico;consumoAttuale
	 * ad esempio
	 * 
	 * e1;10
	 * 
	 */
	public static String datiSensoreToString(IDatiSensore datiSensore){
		String stringona = datiSensore.getId()+";"+datiSensore.getConsumoAttuale();
		
		return stringona;
	}
	
	
	/**
	 * Metodo che consente di recuperare un oggetto datiSensore da una stringa formata secondo la covenzione 
	 * indicata nel metodo  datiSensoreToString, probabilmente ottenuta da un supporto di comunicazione:
	 * 
	 * 
	 * @param un striga che rappresenta i dati del sensore passati come parametri:
	 * idElettrodomestico;consumoAttuale
	 * ad esempio
	 * 
	 * e1;10
	 * @return datiSensore l'oggetto ottenuto dalla stringa
	 */
	public static IDatiSensore stringToDatiSensore(String string){
		StringTokenizer st = new StringTokenizer(string,";");
		String id = st.nextToken();
		int consumoAttuale = Integer.parseInt(st.nextToken());
		DatiSensore datiSensore = new DatiSensore(id, consumoAttuale);
		return datiSensore;
		
	}
	
	/**
	 * metodo che trasforma un oggetto che implementa {@link IComandoUserCmd} in una stringa
	 * che contiene tutte le informazioni presenti nell'oggetto.
	 * 
	 * Nota: anche per i comandi connetti e disconnetti, per i quali l'id elettrodomestico
	 * non ha importanza viene letto il campo dell'oggetto. Pertanto in questi casi si consiglia
	 * di inserire come id una stringa vuota o una non significativa
	 * 
	 * @param comandoUserCmd l'oggetto da trasformare in stringa
	 * @return un striga che rappresenta il comando secondo la struttura:
	 * 
	 * comando;idElettrodomestico
	 * ad esempio
	 * 
	 * spegni;e2
	 * 
	 */
	
	public static String comandoUserCmdToString ( IComandoUserCmd comandoUserCmd){
		String stringona = comandoUserCmd.getComando()+";"+comandoUserCmd.getIdElettrodomestico();
		return stringona;
		
	}
	
	
	/**
	 * Metodo che consente di recuperare un oggetto comandiUserCmd da una stringa formata secondo la covenzione 
	 * indicata nel metodo  comandoUserCmdToString, probabilmente ottenuta da un supporto di comunicazione:
	 * 
	 * 
	 * @param un striga che rappresenta il comando con la struttura elencata sotto:
	 * comando;idElettrodomestico
	 * ad esempio
	 * 
	 * spegni;e2
	 * 
	 * @return l'oggetto IComandiUserCmd ottenuto dalla stringa
	 */
	public static IComandoUserCmd stringToComandoUserCmd(String string){
		StringTokenizer st = new StringTokenizer(string,";");
		ComandiUserCmd comando = ComandiUserCmd.valueOf(st.nextToken());
		String id = st.nextToken();
		ComandoUserCmd comandoUserCmd = new ComandoUserCmd(comando, id);
		return comandoUserCmd;
		
	}
	

}
