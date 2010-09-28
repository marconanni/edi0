package Edi;

	import it.unibo.platform.medcl.*;
	import it.unibo.platform.lindaLike.IMessage;
	

public class  Scontrol extends Subject{
	
	//Local state of the subject
		private String M = "messaggioScontrol.";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
	
	//if scontrol must handle request or response
	
	public void evalAck(){}
	
	public void evalRequest(IMessageAndContext mCtx) throws Exception{
 		showMessage( " received", mCtx.getReceivedMessage() );		
 		//TODO elaborate the message
  		mCtx.replyToCaller("riinvio " + mCtx.getReceivedMessage().msgContent() );		
	}
	
	
	protected void scontrolAsk() throws Exception{
		
	IAcquireAskReply answer = support.ask( "scontrol", "comandoScontrol", M, "interruttore");
	showMsg(  "has asked ... " + "comandoScontrol" );
	while( !answer.askReplyAvailable() ) {
		showMsg( "no ask yet received ... " );
		Thread.sleep(1);
	}
	showMsg( "ask terminated " );
		}
	
	protected IMessageAndContext scontrolGrant() throws Exception{
	IMessageAndContext m = support.grant( "scontrol" ,  "comandoUserCmd"  );
		showMessage( "acquired", m.getReceivedMessage() ) ;
		return m;
	}
	protected IMessage scontrolSense() throws Exception{
		sleep(100);
		IMessage m = support.sense( "scontrol" ,  "datiSensore", lastMsgNum );
		if( m!=null ) {
			lastMsgNum = m.getMsgSeqNum();
			showMessage( "sensed", m ) ;
		}
		return m;
		}
	
	
//Local body of the subject
	protected void doJob(){
	try{
 		
//operation scontrol

// TODO decommentare ricezione request da usercmd
//	IMessageAndContext m = scontrolGrant();
//	//showMessage(  m.getReceivedMessage() ) ;
//	evalRequest( m  );
	 // TODO: non va l'invio del comando all'interruttore: tuprolog da un'eccezione di malformed goal 
//	 scontrolAsk() ;
	 


	IMessage m2 = scontrolSense();
	showMessage("ricevuto messaggio", m2 ) ;
	 
	}catch( Exception e ){System.err.println("Errore in Scontrol");e.printStackTrace();}
	}
}
