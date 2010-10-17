/* ==================================
	Main
 ================================== */
package Edi;

import Edi.elettrodomestico.Sensore;
import Edi.scontrol.Interruttore;
import Edi.scontrol.Scontrol;
import Edi.userCmd.UserCmd;

public  class Edi{

private UserCmd userCmd;
private Scontrol scontrol;
private Sensore sensore;
private Interruttore interruttore;
	
	public void doJob(){
		init();
		configure();
		start();
	}
	
	protected void init(){
 		System.setProperty("coreTrace", "set");
		System.out.println("*** coreTrace=" + System.getProperty("coreTrace"));
	}

	protected void configure(){
		// TODO : sistemare la configurazione caricandola da un file di configurazione.
		userCmd = (UserCmd) UserCmd.getInstance();
		userCmd.setName("Subject-userCmd");
		scontrol = new Scontrol();
		scontrol.setName("Subject-scontrol");
		sensore = new Sensore();
		sensore.setName("Subject-sensore");
		interruttore = new Interruttore();
		interruttore.setName("Subject-interruttore");
 	}

	protected void start(){
		userCmd.start();
		scontrol.start();
		sensore.start();
		interruttore.start();
 	}
	
 	public static void main(String args[]) throws Exception {
		Edi system = new Edi( );
		system.doJob();
	}
	
}//Edi
