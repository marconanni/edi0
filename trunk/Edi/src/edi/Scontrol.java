package edi;

	import it.unibo.platform.medcl.*;
	import it.unibo.platform.lindaLike.IMessage;
	

public class  Scontrol extends Subject{
	
	//Local state of the subject
		private String M = "someContent";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
	
	//if Scontrol must handle request or response
	
	public void evalAck(){}
	
	public void evalRequest(IMessageAndContext mCtx) throws Exception{
 		showMessage( " received", mCtx.getReceivedMessage() );		
 		//TODO elaborate the message
  		mCtx.replyToCaller("apt of " + mCtx.getReceivedMessage().msgContent() );		
	}
	
	
	protected void ScontrolAsk() throws Exception{
		
	IAcquireAskReply answer = support.ask( "Scontrol", "ComandoScontrol", M, "Elettrodomestico");
	showMsg(  "has asked ... " + "ComandoScontrol" );
	while( !answer.askReplyAvailable() ) {
		showMsg( "no ask yet received ... " );
		Thread.sleep(1);
	}
	showMsg( "ask terminated " );
		}
	
	protected IMessageAndContext ScontrolGrant() throws Exception{
	IMessageAndContext m = support.grant( "Scontrol" ,  "ComandoUserCmd"  );
		showMessage( "acquired", m.getReceivedMessage() ) ;
		return m;
	}
	protected IMessage ScontrolSense() throws Exception{
		IMessage m = support.sense( "Scontrol" ,  "DatiSensore", lastMsgNum );
		if( m!=null ) {
			lastMsgNum = m.getMsgSeqNum();
			showMessage( "sensed", m ) ;
		}
		return m;
		}
	
	
//Local body of the subject
	protected void doJob(){
	try{
 		
//operation Scontrol

	IMessageAndContext m = ScontrolGrant();
	//showMessage(  m.getReceivedMessage() ) ;
	evalRequest( m  );
	 
	 ScontrolAsk() ;
	 


	IMessage m = ScontrolSense();
	//showMessage( m ) ;
	 
	}catch( Exception e ){}
	}
}
