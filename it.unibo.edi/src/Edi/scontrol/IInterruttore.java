package Edi.scontrol;

import Edi.elettrodomestico.IElettrodomestico;

/**
 * 
 * @author Marco
 *
 *interfaccia dell'interruttore. L'interruttore collegato ad  un elettrodoemstico provvede
 * a ricevere le richieste da Scontrol e a modificare lo stato dell'elettrodomestico
 *al quale è collegato 
 */
public interface IInterruttore {

	/**
	 * 
	 * @return true se l'interruttore è acceso e se, di conseguenza l'elettrodomestico è
	 *  acceso (in fase di avvio o di esercizio)
	 */
	public boolean isOn();

	/**
	 * 
	 * @return true se l'interruttore è acceso e se, di conseguenza l'elettrodomestico è
	 *  spento o disattivato
	 */
	public boolean isOff();

	/**
	 * accende l'interruttore e l'elettrodomestico collegato
	 */
	public void turnOn();

	/**
	 * spenge l'interruttore e l'elettrodomestico collegato
	 */
	public void turnOff();
	
	/**
	 * 
	 * @return true se l'interruttore è collegato ad un elettrodomestico
	 */
	public boolean isCollegato();
	
	/**
	 * 
	 * @return l'identificativo dell'elettrodomestico
	 */
	public String getId();
	
	/**
	 * 
	 * @return l'identificativo dell'elettrodomestico collegato all'interruttore
	 */
	public String getIdElettrodomesticoCollegato();
	
	
	/**
	 * metodo che consente di impostare l'elettrodomestico attualemte collegato all'interruttore
	 * @param elettrodomesticoCollegato l'elettrodomestico da collegare all'interruttore 8 quello al quale invierà
	 * i comandi di accensione e soegnimento.
	 */
	public void setElettrodomesticoCollegato(IElettrodomestico elettrodomesticoCollegato) ;
	
	

}