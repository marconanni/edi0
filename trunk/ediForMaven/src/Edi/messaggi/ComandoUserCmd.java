package Edi.messaggi;

/**
 * 
 * @author Marco
 *
 *	classe che rappresenta il comando inviato da UserCmd a Scontrol
 * contiene il comando in questione come enumerativo  (ComandiUserCmd)
 * e l'id dell'elettrodomestico da azionare
 * Nota: nel caso di comando connetti o disconnetti l'id Elettrodomestico non viene preso in 
 * considerazione e quindi è una buona scelta metterlo a null o a 0;
 */

public class ComandoUserCmd implements IComandoUserCmd {
	
	private ComandiUserCmd comando;
	private String idElettrodomestico;
	
	
	/**
	 * 
	 * @param comando il comando da inviare a Scontrol come enumerativo {@link ComandiUserCmd}
	 * @param idElettrodomestico l'id dell'elettrodomestico ogggetto del comando, non viene usato se il comando è connetti o disconnetti, in tal caso è consigliabile lasciarlo a null;
	 */
	
	public ComandoUserCmd(ComandiUserCmd comando,String idElettrodomestico) {
		super();
		this.idElettrodomestico = idElettrodomestico;
		this.comando= comando;
	}

	

	/**
	 * @return l'id dell'elettrodomestico oggetto del comando, potrebbe essere null nel caso il comando inviato sia connetti o disconnetti.
	 */
	public String getIdElettrodomestico() {
		return idElettrodomestico;
	}


	/**
	 * 
	 * @return il comando inviato a Scontrol  come enumerativo {@link ComandiUserCmd}
	 */
	public ComandiUserCmd getComando() {
		return comando;
	}



	



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ComandoUserCmd))
			return false;
		ComandoUserCmd other = (ComandoUserCmd) obj;
		if (comando == null) {
			if (other.comando != null)
				return false;
		} else if (!comando.equals(other.comando))
			return false;
		if (idElettrodomestico == null) {
			if (other.idElettrodomestico != null)
				return false;
		} else if (!idElettrodomestico.equals(other.idElettrodomestico))
			return false;
		return true;
	}



	


	
	
	



	



	

	
}
