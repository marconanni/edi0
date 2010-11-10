package Edi.userCmd;



import Edi.messaggi.IStatus;


/**
 * 
 * @author Marco
 * 
 * Interfaccia che rappersenta la centralina mobile attraverso la quale l'utente, dopo essersi connesso
 * invia i comandi di accensione e spegnimento degli elettrodomestici. Scontrol ( la centralina fissa)
 * provvede ad inviare lo stato del sistema all'atto di ogni richiesta o quando si verificano cambiamenti
 * in esso. E' compito dell'interfaccia utente visualizzare il suddetto stato
 *
 */
public interface IUserCmd {


	/**
	 * 
	 * @return true se UserCmd è connesso a Scontrol
	 */
	public boolean isConnesso();

	/**
	 * 
	 * @return lo stato del sistema in termini di elettrodomestici accesi, consumi ecc
	 * per maggiori info sulla forma dello stato del sistema vedi {@link IStatus}
	 */
	public IStatus getStatus();

	/**
	 * metodo che permette di inviare una richiesta di connessione a Scontrol
	 */
	public void connetti();

	/**
	 * metodo che permette di inviare una richiesta di disconnessione a Scontrol
	 */
	public void disconnetti();

	/**
	 * mettodo che permette di inviare una richiesta di accensione di un elettrodomestico a Scontrol
	 * @param idElettrodomestico: l'identificativo dell'elettrodomestico da accendere
	 */
	public void accendiElettrodomestico(String idElettrodomestico);

	/**
	 * mettodo che permette di inviare una richiesta di accensione di un elettrodomestico a Scontrol
	 * @param idElettrodomestico: l'identificativo dell'elettrodomestico da spegnere
	 */
	public void spegniElettrodomestico(String idElettrodomestico);
	
	/**
	 * metodo che permette di registrare un'interfaccia utente a Scontrol
	 * dal momento della registrazione, ad ogni cambiamento dello status, la gui
	 * vedrà chiamato il suo metodo update e gli sarà passato il nuovo
	 * stato del sistema come parametro
	 * @param gui un oggetto (il metodo è stato pensato per le interfacce utente) che implementi
	 * IGui
	 */
	public void addGui(IGui gui);
	
	/**
	 * metodo che serve per deregistrare un'interfaccia utente. dal momento della deregistrazione in poi
	 * l'interfaccia utente non vedrà piu notificata di cambiamanti dello stato
	 * @param gui un oggetto (il metodo è stato pensato per le interfacce utente) che implementi
	 * IGui
	 */
	public void removeGui(IGui gui);
	
	/**
	 * metodo che provoca la chiamata del metodo update delle gui registrate
	 * alle quali viene inviato lo status passato come parametro
	 * @param newStatus
	 */
	public void notifiyGui(IStatus newStatus);

}