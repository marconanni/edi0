package Edi;

	import it.unibo.platform.medcl.*;
	import it.unibo.platform.lindaLike.IMessage;
	

public class  UserCmd extends Subject{
	
	//Local state of the subject
		private String M = "someContent";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
	
	//if userCmd must handle request or response
	public void evalResponse(IAcquireDemandReply answer) throws Exception{
		while( !answer.demandReplyAvailable() ) {
			showMsg( "no response yet received ... " );
			Thread.sleep(1);
		}
		showMsg( "received " + answer.acquireDemandReply().msgContent());	
	}
	
	
	
	
	protected IAcquireDemandReply userCmdDemand() throws Exception{
		
	IAcquireDemandReply answer = support.demand( "userCmd", "comandoUserCmd", M, "scontrol");
	showMsg(  "has demanded ... " + "comandoUserCmd" );
	return answer;
		}
	

	
//Local body of the subject
	protected void doJob(){
	try{
 		
//operation userCmd
	IAcquireDemandReply m = userCmdDemand() ;
	evalResponse( m );
	 





	}catch( Exception e ){}
	}
}
