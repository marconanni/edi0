/* ==================================
	Main
 ================================== */
package edi;
public  class edi{

private UserCmd UserCmd;
private Scontrol Scontrol;
private Sensore Sensore;
private Elettrodomestico Elettrodomestico;

	
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
		UserCmd = new UserCmd();
		UserCmd.setName("Subject-UserCmd");
		Scontrol = new Scontrol();
		Scontrol.setName("Subject-Scontrol");
		Sensore = new Sensore();
		Sensore.setName("Subject-Sensore");
		Elettrodomestico = new Elettrodomestico();
		Elettrodomestico.setName("Subject-Elettrodomestico");
 	}

	protected void start(){
		UserCmd.start();
		Scontrol.start();
		Sensore.start();
		Elettrodomestico.start();
 	}
	
 	public static void main(String args[]) throws Exception {
		edi system = new edi( );
		system.doJob();
	}
	
}//edi
