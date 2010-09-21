package edi;

	import it.unibo.platform.medcl.*;
	import it.unibo.platform.lindaLike.IMessage;
	

public class  Sensore extends Subject{
	
	//Local state of the subject
		private String M = "someContent";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
	
	//if Sensore must handle request or response
	
	
	
	

	protected void SensoreEmit() throws Exception{
		support.emit( "Sensore", "DatiSensore" , M);
		showMsg( "Sensore" + " has emitted ... " + "DatiSensore" );
	}
	

	
//Local body of the subject
	protected void doJob(){
	try{
 		
//operation Sensore




	 SensoreEmit() ;
 	 

	}catch( Exception e ){}
	}
}
