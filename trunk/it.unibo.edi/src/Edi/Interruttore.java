package Edi;

	import it.unibo.platform.medcl.*;
import it.unibo.platform.lindaLike.IMessage;
	

public class  Interruttore extends Subject{
	
	//Local state of the subject
		private String M = "someContent";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
	
	//if interruttore must handle request or response
	
	
	
	

	protected IMessage interruttoreAccept() throws Exception{
	IMessage m = support.accept( "interruttore" ,  "comandoScontrol"  );
		showMessage( "accepted", m ) ;
		return m;
	}
	
	
//Local body of the subject
	protected void doJob(){
	try{
 		
//operation interruttore



	IMessage m = interruttoreAccept();
	//showMessage( m ) ;
	 


	}catch( Exception e ){System.err.println("Errore ricezione messaggio");e.printStackTrace();}
	}
}
