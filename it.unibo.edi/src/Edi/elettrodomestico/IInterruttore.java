package Edi.elettrodomestico;

/**
 * 
 * @author Marco
 *
 *interfaccia dell'interruttore. L'interruttore collegato ad  un elettrodoemstico provvede
 * a ricevere le richieste da Scontrol e a modificare lo stato dell'elettrodomestico
 *al quale � collegato 
 */
public interface IInterruttore {

	/**
	 * 
	 * @return true se l'interruttore � acceso e se, di conseguenza l'elettrodomestico �
	 *  acceso (in fase di avvio o di esercizio)
	 */
	public boolean isOn();

	/**
	 * 
	 * @return true se l'interruttore � acceso e se, di conseguenza l'elettrodomestico �
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

}