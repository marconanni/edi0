package Edi.scontrol;

import java.util.Vector;

/**
 * 
 * @author Marco
 *classe che rappresenta un thread resettabile. Una volta avviato, provvede ad avvisare gli oggetti
 *che si registrano (che devono implementare IObserver) di quando è trascorso un tempo eventTime. 
 *Se prima della scadenza del timeout il timer viene resettato il countdown riparte da capo.
 */
public class SafetyTimer extends Thread implements IResettableTimer {
	
	private boolean resettato;
	private String idElettrodomestico;
	private long eventTime;
	private Vector <IObserver> observers;
	
	public SafetyTimer() {
		super();
		observers = new Vector<IObserver>();
		
	}

	/* (non-Javadoc)
	 * @see Edi.scontrol.IResettableTimer#isResettato()
	 */
	public boolean isResettato() {
		return resettato;
	}

	/**
	 * 
	 * @param resettato 
	 */
	private void setResettato(boolean resettato) {
		this.resettato = resettato;
	}

	/* (non-Javadoc)
	 * @see Edi.scontrol.IResettableTimer#getIdElettrodomestico()
	 */
	public String getIdElettrodomestico() {
		return idElettrodomestico;
	}

	/* (non-Javadoc)
	 * @see Edi.scontrol.IResettableTimer#setIdElettrodomestico(java.lang.String)
	 */
	public void setIdElettrodomestico(String idElettrodomestico) {
		this.idElettrodomestico = idElettrodomestico;
	}

	/* (non-Javadoc)
	 * @see Edi.scontrol.IResettableTimer#getEventTime()
	 */
	public long getEventTime() {
		return eventTime;
	}

	/* (non-Javadoc)
	 * @see Edi.scontrol.IResettableTimer#setEventTime(int)
	 */
	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}
	
	/* (non-Javadoc)
	 * @see Edi.scontrol.IResettableTimer#addObserver(Edi.scontrol.IObserver)
	 */
	public void addObserver (IObserver observer){
		observers.add(observer);
	}
	
	/* (non-Javadoc)
	 * @see Edi.scontrol.IResettableTimer#removeObserver(Edi.scontrol.IObserver)
	 */
	public void removeObserver (IObserver observer){
		observers.remove(observer);
	}
	
	/* (non-Javadoc)
	 * @see Edi.scontrol.IResettableTimer#notifyObservers()
	 */
	public void notifyObservers(){
		for (int i = 0; i < observers.size(); i++) {
			observers.get(i).update(this, this.getIdElettrodomestico());
			
		}
		
	}
	
	/**
	 * @see Edi.scontrol.IResettableTimer#resetta()
	 */
	public synchronized void resetta (){
		this.resettato=true;
		
	}
	
	/**
	 * @see Edi.scontrol.IResettableTimer#avvia()
	 */
	public void avvia(){
		this.start();
	}
	
	public void run(){
		do {
			this.setResettato(false);
			try {
				sleep(eventTime);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		} while (resettato);
		// se arrivo qui significa che sono passati eventTime millisecondi e il timer non è stato resettato
		// devo lanciare l'allarme
		this.notifyObservers();
	}


}
