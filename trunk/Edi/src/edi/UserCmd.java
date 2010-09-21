package edi;

	import it.unibo.platform.medcl.*;
	import it.unibo.platform.lindaLike.IMessage;
	

public class  UserCmd extends Subject{
	
	//Local state of the subject
		private String M = "someContent";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
	
	//if UserCmd must handle request or response
	public void evalResponse(IAcquireDemandReply answer) throws Exception{
		while( !answer.demandReplyAvailable() ) {
			showMsg( "no response yet received ... " );
			Thread.sleep(1);
		}
		showMsg( "received " + answer.acquireDemandReply().msgContent());	
	}
	
	
	
	
	protected IAcquireDemandReply UserCmdDemand() throws Exception{
		
	IAcquireDemandReply answer = support.demand( "UserCmd", "ComandoUserCmd", M, "Scontrol");
	showMsg(  "has demanded ... " + "ComandoUserCmd" );
	return answer;
		}
	

	
//Local body of the subject
	protected void doJob(){
	try{
 		
//operation UserCmd
	IAcquireDemandReply m = UserCmdDemand() ;
	evalResponse( m );
	 





	}catch( Exception e ){}
	}
}
