package edi;
import it.unibo.platform.lindaLike.IMessage;
//import it.unibo.is.gui.view.OutputViewAbstract;
import it.unibo.is.interfaces.IOutputView;
import it.unibo.is.common.IS09Util;

public abstract class Subject extends Thread{
private IOutputView view;

public void run(){
	view = null; //OutputViewAbstract.getOutputView();
	doJob();
}

abstract protected void doJob();

/* ==================================
	UTILS
 ================================== */
	public  void showMsg( String m){
		IS09Util.showMsg(view, this.getName() + " " + m );
	}

	public  void showMessage( String comment,  IMessage m){
	if( m != null) {
		String msgEmitter 	= m.msgEmitter();
		String msgId 		= m.msgId();
		String msgContent 	= m.msgContent();
		String msgNum 		= m.msgNum();

		IS09Util.showMsg(view, this.getName() + " " + comment + ":" +
			"msgEmitter="+ msgEmitter  + " msgId="+ msgId +
			" msgContent=" + m.msgContent() + " msgNum=" + msgNum);
	}		  
	}  
}
