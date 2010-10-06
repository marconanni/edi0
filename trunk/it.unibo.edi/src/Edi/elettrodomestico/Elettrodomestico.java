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
	
	public boolean isOn(){
		return isAvvio()||isEsercizio();
	}
	public boolean isAvvio(){
		return this.stato==StatoElettrodomestico.avvio;
	}
	public boolean isEsercizio(){
		return this.stato==StatoElettrodomestico.esercizio;
	}
	public boolean isOff(){
		return this.isDisattivato()||stato==StatoElettrodomestico.spento;
	}
	
	public boolean isDisattivato(){
		return this.stato==StatoElettrodomestico.disattivato;
	}
	
	public StatoElettrodomestico getStato(){
		return this.stato;
	}

	public String getId() {
		return id;
	}

	public int getConsumoEsercizio() {
		return consumoEsercizio;
	}

	public int getConsumoAvvio() {
		return consumoAvvio;
	}

	public Date getOraAccensione() {
		return oraAccensione;
	}
	
	public int getConsumoAttuale(){
		if(this.stato==StatoElettrodomestico.avvio)
			return this.consumoAvvio;
		else 
			if(this.stato==StatoElettrodomestico.esercizio)
				return this.consumoEsercizio;
			else		
				return 0;
	}

}
