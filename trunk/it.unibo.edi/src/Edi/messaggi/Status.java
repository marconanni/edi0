package Edi.messaggi;
import java.util.Vector;


/**
 * 
 * @author Marco
 *	classe che rappresenta lo stato complessivo di un sistema e viene mandata da Scontrol a UserCmd
 * affinchè lo stato venga visualizzato all'utente finale.
 */
public class Status implements IStatus {
	
	private String comunicazione;
	private int consumoAttualeComplessivo;
	private Vector<IReportElettrodomestico> reports;
	private int soglia;
	
	/**
	 * 
	 * @param comunicazione : un'eventuale messaggio testuale da mandare a UserCmd ( e quindi all'utente)
	 * @param reports:  un Vector di Report degli elettrodomestici presenti nel sistema vedi {@link IReportElettrodomestico} e {@link ReportElettrodomestico}
	 * @param soglia: la soglia di consumo massimo del sistema.
	 * il costruttore inizializza il consumoAttualeComplessivo sommando i consumi attuali di tutti gli elettrodomestici
	 * 
	 */
	public Status(String comunicazione, 
			Vector<IReportElettrodomestico> reports, int soglia) {
		super();
		this.comunicazione = comunicazione;
		this.reports = reports;
		this.soglia = soglia;
		consumoAttualeComplessivo=0;
		
		for (int k =0; k<reports.size();k++){
			consumoAttualeComplessivo = consumoAttualeComplessivo + reports.get(k).getConsumoAttuale();
			
		}
		
		
	}

	/**
	 * @see Edi.messaggi.IStatus#getComunicazione()
	 */
	public String getComunicazione() {
		return comunicazione;
	}

	/**
	 * @see Edi.messaggi.IStatus#getConsumoAttualeComplessivo()
	 */
	public int getConsumoAttualeComplessivo() {
		return consumoAttualeComplessivo;
	}

	/**
	 * @see Edi.messaggi.IStatus#getReports()
	 */
	public Vector<IReportElettrodomestico> getReports() {
		return reports;
	}

	/**
	 * @see Edi.messaggi.IStatus#getSoglia()
	 */
	public int getSoglia() {
		return soglia;
	}

	@Override
	public String toString() {
		return "Status [comunicazione=" + comunicazione
				+ ", consumoAttualeComplessivo=" + consumoAttualeComplessivo
				+ ", reports=" + reports + ", soglia=" + soglia + "]";
	}
	
	
	
	
	
	
	

}
