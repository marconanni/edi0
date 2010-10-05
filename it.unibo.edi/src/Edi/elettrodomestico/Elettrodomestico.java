package Edi.elettrodomestico;

import java.util.Date;

public class Elettrodomestico {
	
	private StatoElettrodomestico stato;
	private String id;
	private int consumoEsercizio;
	private int consumoAvvio;
	private Date oraAccensione;
	
	public Elettrodomestico(StatoElettrodomestico stato, String id,
			int consumoEsercizio, int consumoAvvio, Date oraAccensione) {
		super();
		this.stato = stato;
		this.id = id;
		this.consumoEsercizio = consumoEsercizio;
		this.consumoAvvio = consumoAvvio;
		this.oraAccensione = oraAccensione;
	}
	
	
	
	

}
