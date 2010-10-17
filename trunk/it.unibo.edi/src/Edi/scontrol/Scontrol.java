package Edi.scontrol;

import Edi.Subject;
import Edi.messaggi.*;
import it.unibo.platform.medcl.*;
import it.unibo.platform.lindaLike.IMessage;
import java.util.*;
import Edi.elettrodomestico.*;
import Edi.userCmd.*;
	

public class  Scontrol extends Subject implements IObserver{
	
	//Local state of the subject
		private String M = "messaggioScontrol.";	
		private IMedCl09 support = new MedCl09();
		private int lastMsgNum = 0; 
//	 	private  IOutputView view;
		private static Scontrol instance;
		private Hashtable<String, IElettrodomestico> elettrodomestici = new Hashtable<String, IElettrodomestico>();
		private Hashtable<String, IResettableTimer> timers = new Hashtable<String, IResettableTimer>();
		private int soglia;
		private long intervalloTimers;
		
		//istanze di intetturrori e usercmd messi visto che le due invitation non funzionano.
		
		private IUserCmd userCmd;
		private Hashtable <String, IInterruttore> interruttori = new Hashtable<String, IInterruttore>();
		
		/**
		 * Restituisce un'istanza di Scontrol configurata mediante i parametri passati.
		 * crea le tabelle degli elettrodomestici gestiti e quella dei relativi timer
		 * ovviamente fa partire solo  i timer relativi agli elettrodomestici accesi 
		 * @param elettrodomestici la lista degli elettrodomestici controllati da Scontrol.
		 * @param soglia
		 * @param intervalloTimers
		 * @return
		 */
		public static Scontrol getInstance(List<IElettrodomestico> elettrodomestici, int soglia, int intervalloTimers){
			if (instance==null){
				instance = new Scontrol();
			}
			instance.setSoglia(soglia);
			instance.setIntervalloTimers(intervalloTimers);
			for (IElettrodomestico elettrodomestico : elettrodomestici) {
				instance.getElettrodomestici().put(elettrodomestico.getId(), elettrodomestico);
				// creo un nuovo safety timer, ma non mi registro come listener, lo faccio quando
				// accendo l'elettrodomestico (e faccio partire il timer)
				SafetyTimer timer = new SafetyTimer();
				timer.setIdElettrodomestico(elettrodomestico.getId());
				timer.setEventTime(instance.getIntervalloTimers());
				
				instance.getTimers().put(elettrodomestico.getId(), timer);
				if (elettrodomestico.isOn()){
					timer.addObserver(instance);
					timer.start();
				}
			}
			return instance;
		}
	
		
		
		// TODO getters 
		
		public Hashtable<String, IElettrodomestico> getElettrodomestici() {
			return elettrodomestici;
		}



		public Hashtable<String, IResettableTimer> getTimers() {
			return timers;
		}



		public int getSoglia() {
			return soglia;
		}



		public long getIntervalloTimers() {
			return intervalloTimers;
		}



		public IUserCmd getUserCmd() {
			return userCmd;
		}



		public Hashtable<String, IInterruttore> getInterruttori() {
			return interruttori;
		}

		// TODO setters
		


		

	
	public void setElettrodomestici(
				Hashtable<String, IElettrodomestico> elettrodomestici) {
			this.elettrodomestici = elettrodomestici;
		}



		public void setTimers(Hashtable<String, IResettableTimer> timers) {
			this.timers = timers;
		}



		public void setSoglia(int soglia) {
			this.soglia = soglia;
		}



		public void setIntervalloTimers(long intervalloTimers) {
			this.intervalloTimers = intervalloTimers;
		}



		public void setUserCmd(IUserCmd userCmd) {
			this.userCmd = userCmd;
		}



		public void setInterruttori(Hashtable<String, IInterruttore> interruttori) {
			this.interruttori = interruttori;
		}



	public void evalAck(){}
	
	
	public void evalRequest(IMessageAndContext mCtx) throws Exception{
 		showMessage( " received", mCtx.getReceivedMessage() );		
 		//TODO elaborate the message
  		mCtx.replyToCaller("riinvio " + mCtx.getReceivedMessage().msgContent() );		
	}
	
	
	protected void scontrolAskStatus() throws Exception{
		
	IAcquireAskReply answer = support.ask( "scontrol", "status", M, "userCmd");
	showMsg(  "has asked ... " + "status" );
	while( !answer.askReplyAvailable() ) {
		showMsg( "no ask yet received ... " );
		Thread.sleep(100);
	}
	showMsg( "ask terminated " );
		}
	protected void scontrolAskComandoScontrol() throws Exception{
		
	IAcquireAskReply answer = support.ask( "scontrol", "comandoScontrol", M, "interruttore");
	showMsg(  "has asked ... " + "comandoScontrol" );
	while( !answer.askReplyAvailable() ) {
		showMsg( "no ask yet received ... " );
		Thread.sleep(100);
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
 		
//operation scontrol : per le prove commenta e decommenta le comunicazioni di interesse

//	IMessageAndContext m = scontrolGrant();
//	//showMessage(  m.getReceivedMessage() ) ;
//	evalRequest( m  );
//	 
	 scontrolAskStatus() ;
//	 
//
//	 scontrolAskComandoScontrol() ;
	 


//	IMessage m2 = scontrolSense();
	//showMessage( m ) ;
	 
	}catch( Exception e ){System.err.println("Errore in Scontrol");e.printStackTrace();}
	}



	@Override
	public void update(IResettableTimer safetyTimer, String idElettrodomestico) {
		// TODO Auto-generated method stub
		
	}
}
