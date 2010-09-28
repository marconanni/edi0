package Edi;

	import it.unibo.platform.medcl.*;
import it.unibo.platform.lindaLike.IMessage;
	

public class  Sensore extends Subject{
	
	//Local state of the subject
		private String M = "someContent";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
	
	//if sensore must handle request or response
	
	
	
	

	protected void sensoreEmit() throws Exception{
		support.emit( "sensore", "datiSensore" , M);
		showMsg( "sensore" + " has emitted ... " + "datiSensore" );
	}
	

	
//Local body of the subject
	protected void doJob(){
	try{
 		
//operation sensore




	 sensoreEmit() ;
 	 

	}catch( Exception e ){System.err.println("Errore in Sensore");e.printStackTrace();}
	}
}
