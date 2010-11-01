package Edi.messaggi;
import Edi.elettrodomestico.StatoElettrodomestico;

/**
 * 
 * @author Marco
 *Classe che rappresenta il report per un singolo elettrodomestico; contiene 
 *l'identificativo dell'elettrodomestico, il suo stato e il suo consumo attuale;
 *
 */
public class ReportElettrodomestico implements IReportElettrodomestico {
	
	private String idElettrodomestico;
	private StatoElettrodomestico stato;
	private int ConsumoAttuale;
	
	
	/**
	 * 
	 * @param idElettrodomestico l'id dell'elettrodomestico
	 * @param stato lo stato attuale del'elettrodomestico come  {@link StatoElettrodomestico}
	 * @param consumoAttuale il cosumo attuale dell'elettrodomestico
	 */
	public ReportElettrodomestico(String idElettrodomestico,
			StatoElettrodomestico stato, int consumoAttuale) {
		super();
		this.idElettrodomestico = idElettrodomestico;
		this.stato = stato;
		ConsumoAttuale = consumoAttuale;
	}

	/**
	 * @see Edi.messaggi.IReportElettrodomestico#getIdElettrodomestico()
	 */
	public String getIdElettrodomestico() {
		return idElettrodomestico;
	}
	
	/**
	 * @see Edi.messaggi.IReportElettrodomestico#getStato()
	 */


	public StatoElettrodomestico getStato() {
		return stato;
	}

	/**
	 * @see Edi.messaggi.IReportElettrodomestico#getConsumoAttuale()
	 */
	public int getConsumoAttuale() {
		return ConsumoAttuale;
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportElettrodomestico other = (ReportElettrodomestico) obj;
		if (ConsumoAttuale != other.ConsumoAttuale)
			return false;
		if (idElettrodomestico == null) {
			if (other.idElettrodomestico != null)
				return false;
		} else if (!idElettrodomestico.equals(other.idElettrodomestico))
			return false;
		if (stato == null) {
			if (other.stato != null)
				return false;
		} else if (!stato.equals(other.stato))
			return false;
		return true;
	}
	
	
	

}
