package Edi.userCmd;

import Edi.messaggi.IStatus;

/**
 * 
 * @author Marco
 * interfaccia che rappresenta una generica interfaccia utente che
 * UserCmd deve avvertire quando ci sono cabiamenti nello stato del sistema
 */
public interface IGui {

	/**
	 * questo metodo viene chiamato da UserCmd quando si verifica un 
	 * cambiamento nello stato del sistema
	 * @param newStatus: il nuovo stato del sistema
	 */
	public void update(IStatus newStatus);
}
