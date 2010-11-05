/* ==================================
	Main
 ================================== */
package Edi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import Edi.elettrodomestico.*;
import Edi.scontrol.*;
import Edi.userCmd.*;
import Edi.messaggi.*;

public  class Edi{

private UserCmd userCmd;
private Scontrol scontrol;
private Vector<IInterruttore> interruttori = new Vector<IInterruttore>();
private Vector<IElettrodomestico> elettrodomestici = new Vector<IElettrodomestico>();
private Vector<IRappresentazioneElettrodomestico> rappresentazioniElettrodomestici = new Vector<IRappresentazioneElettrodomestico>();
private Vector<Sensore> sensori = new Vector<Sensore>();

String pathname = "C:/Users/Marco/eclipseProjects/it.unibo.edi/ediConfig.txt";

	
	public void doJob(){
		init();
		configure();
		start();
	}
	
	protected void init(){
 		System.setProperty("coreTrace", "set");
		System.out.println("*** coreTrace=" + System.getProperty("coreTrace"));
	}

	protected void configure(){
		// TODO : sistemare la configurazione caricandola da un file di configurazione.
//		userCmd = (UserCmd) UserCmd.getInstance();
//		userCmd.setName("Subject-userCmd");
//		scontrol = new Scontrol();
//		scontrol.setName("Subject-scontrol");
//		sensore = new Sensore();
//		sensore.setName("Subject-sensore");
//		interruttore = new Interruttore();
//		interruttore.setName("Subject-interruttore");
		StringTokenizer st;
		
	
		Vector <String>righe = Edi.getRighe(pathname);
		// soglia
		st = new StringTokenizer(righe.get(1), ":");
		st.nextToken();
		int soglia = Integer.parseInt(st.nextToken());
		// intervallo invio dati consumo
		st = new StringTokenizer(righe.get(2), ":");
		st.nextToken();
		long intervalloInvio = Long.parseLong(st.nextToken());
		// intervallo spengimento di sicurezza
		st = new StringTokenizer(righe.get(3), ":");
		st.nextToken();
		long intervalloSicurezza = Long.parseLong(st.nextToken());
		// numero interruttori
		st = new StringTokenizer(righe.get(4), ":");
		st.nextToken();
		int numeroInterruttori = Integer.parseInt(st.nextToken());
		// numero elettrodomestici
		st = new StringTokenizer(righe.get(5), ":");
		st.nextToken();
		int numeroElettrodomestici = Integer.parseInt(st.nextToken());
		// creo gli interruttori
		for (int i = 7; i < 7+numeroInterruttori; i++) {
			String id = righe.get(i);
			interruttori.add(new Interruttore(id));			
		}
		
		// TODO creo gli elettrodomestici con i relativi sensori e la sua  rappresentazione
		
		for (int i = 0; i <numeroElettrodomestici ; i++) {
			int k = i+7+numeroInterruttori+1;
			Elettrodomestico e = this.creaEassociaElettrodomestico(righe.get(k), interruttori);
			
			sensori.add(new Sensore(intervalloInvio, e));
			elettrodomestici.add(e);
			rappresentazioniElettrodomestici.add(this.CreaRappresentazioneElettrodomestico(righe.get(k)));
		}
		
		// creo userCmd
		
		this.userCmd =  (UserCmd)UserCmd.getInstance();
		// creo Scontrol
		scontrol = Scontrol.getInstance(rappresentazioniElettrodomestici, soglia, intervalloSicurezza, interruttori, (UserCmd)userCmd);
		
		for (Sensore sensore : sensori) {
			sensore.setScontrol(scontrol);
			
		}
		
		
		// configuro i nomi, anche se contact non funziona comunque
		userCmd.setName("Subject-userCmd");
		scontrol.setName("Subject-scontrol");
		for (Sensore sensore : sensori) {
			sensore.setName("Subject-sensore");
		}
		
	}

	protected void start(){
		 userCmd.start();
		scontrol.start();
		for (int k = 0; k < sensori.size(); k++) {
			 sensori.get(k).start();
			
		}
		
		userCmd.connetti();
		userCmd.accendiElettrodomestico("e1");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userCmd.disconnetti();
 	}
	
 	public static void main(String args[]) throws Exception {
		Edi system = new Edi( );
		system.doJob();
	}
 	
 	private static Vector<String> getRighe(String pathname) {
 		Vector<String> righe = new Vector<String>();
 		try {
 		
 			BufferedReader reader = new BufferedReader ( new FileReader(pathname));
 			String linea=reader.readLine();

 			while(linea!=null) {
 			      
 			       righe.add(linea);
 			       linea=reader.readLine();
 			       
 			}
 			reader.close();

 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		return righe;
 	}
 	
 	private Elettrodomestico creaEassociaElettrodomestico(String descrizione,Vector<IInterruttore> interruttori2){
 		StringTokenizer st = new StringTokenizer(descrizione, ";");
 		String id = st.nextToken();
 		String tipo = st.nextToken();
 		StatoElettrodomestico stato = StatoElettrodomestico.valueOf(st.nextToken());
 		String idInterruttore = st.nextToken();
 		Date oraAccensione = null;
 		if (stato != StatoElettrodomestico.spento)
 			oraAccensione = new Date(Long.parseLong(st.nextToken()));
 		// creo l'elettrodomestico:
 		Elettrodomestico elettrodomestico=null;
 		if (tipo.contains("basso"))
 			elettrodomestico= new ElettrodomesticoBassoConsumo(stato,id,oraAccensione);
 		else if (tipo.contains("medio"))
			elettrodomestico= new ElettrodomesticoMedioConsumo(stato,id,oraAccensione);
 		else if (tipo.contains("alto"))
			elettrodomestico= new ElettrodomesticoAltoConsumo(stato,id,oraAccensione);
 		// collego l'interruttore con l'elettrodomestico
 		for (int k = 0; k < interruttori2.size(); k++) {
			IInterruttore interruttore = interruttori2.get(k);
			if (interruttore.getId().equals( idInterruttore))
				interruttore.setElettrodomesticoCollegato(elettrodomestico);
			
		}
 		return elettrodomestico;
 	}
 	
 	private RappresentazioneElettrodomestico CreaRappresentazioneElettrodomestico (String rigaDescrizione ){
 		RappresentazioneElettrodomestico rappresentazione = null;
 		StringTokenizer st = new StringTokenizer(rigaDescrizione, ";");
 		String id = st.nextToken();
 		String tipo = st.nextToken();
 		StatoElettrodomestico stato = StatoElettrodomestico.valueOf(st.nextToken());
 		String idInterruttore = st.nextToken();
 		Date oraAccensione = new Date();
 		if (stato != StatoElettrodomestico.spento)
 			oraAccensione = new Date(Long.parseLong(st.nextToken()));
 		
 		if (tipo.contains("basso"))
 			rappresentazione= new RappresentazioneElettrodomestico(id, stato, 30, idInterruttore, oraAccensione);
 		else if (tipo.contains("medio"))
 			rappresentazione= new RappresentazioneElettrodomestico(id, stato, 60, idInterruttore, oraAccensione);
 		else if (tipo.contains("alto"))
 			rappresentazione= new RappresentazioneElettrodomestico(id, stato, 30, idInterruttore, oraAccensione);
 		
 		if (rappresentazione.getStato()== StatoElettrodomestico.spento)
 			rappresentazione.setConsumo(0);
 		if (rappresentazione.getStato() == StatoElettrodomestico.avvio)
 			rappresentazione.setConsumo(rappresentazione.getConsumo()*2);
 		
 		return rappresentazione;
 	}

 	

	
}//Edi

