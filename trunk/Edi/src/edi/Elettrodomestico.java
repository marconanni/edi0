package edi;

	import it.unibo.platform.medcl.*;
	import it.unibo.platform.lindaLike.IMessage;
	

public class  Elettrodomestico extends Subject{
	
	//Local state of the subject
		private String M = "someContent";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
	
	//if Elettrodomestico must handle request or response
	
	
	
	

	protected IMessage ElettrodomesticoAccept() throws Exception{
	IMessage m = support.accept( "Elettrodomestico" ,  "ComandoScontrol"  );
		showMessage( "accepted", m ) ;
		return m;
	}
	
	
//Local body of the subject
	protected void doJob(){
	try{
 		
//operation Elettrodomestico



	IMessage m = ElettrodomesticoAccept();
	//showMessage( m ) ;
	 


	}catch( Exception e ){}
	}
}
