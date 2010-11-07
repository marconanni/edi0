package it.unibo.edi;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import com.swtdesigner.SWTResourceManager;
import org.eclipse.swt.widgets.Text;

import Edi.elettrodomestico.StatoElettrodomestico;
import Edi.messaggi.IStatus;
import Edi.messaggi.IReportElettrodomestico;
import Edi.userCmd.*;
import Edi.Edi;
import java.util.*;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class View extends ViewPart implements IGui  {
	
	private Vector<Button> bottoni;
	private Vector <StatoElettrodomestico> stati;
	private IUserCmd usercmd;
	
	public static final String ID = "it.unibo.edi.view";
	private Text txtComunicazione;
	private Button btnConnetti;
	private Button btnDisconnetti;
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;
	private Button btn6;
	private Button btn7;
	private Button btn8;
	private Button btn9;
	private Label lblConsumo;
	private Label lblIndicazioneConsumoComplessivo;
	private Label lblSoglia;
	private Label lblIndicazioneSogliaDiConsumo;
	
	private final int numPulsantiElettrodomestici =9;
	
	private final Color coloreAvvio = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN);
	private final Color coloreEsercizio = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
	private final Color coloreDisattivato = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW);
	private final Color coloreSpento = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
 
	
	
	public View() {
	}
	
	/**
	 * Questo metodo esegue la configurazione iniziale del sistema, per la parte che non è stata
	 * generata automaticamente e viene chiamato esclusivamente da createPartControl.
	 * In paritcolare raccoglie i 9 pulsanti degli elettrodomestici nel vettore bottoni,
	 * inizializza il vettore accesi (che, ha un elementro true per ogni elettrodomestico acceso)
	 * con nove valori a false;
	 * 
	 * chiama il metodo doJob della classe Edi, che, caricando il file di configurazione, crea
	 * tutti gli oggetti della businness logic, li configura e fa partire i processi dei sensori, di Scontrol 
	 * e di Usercmd;
	 * 
	 * ottiene un riferimento a userCmd e si registra come observer per ottnere lo status aggiornato;
	 * 
	 * chiama il metodo aggiornaVisibilità per nscondere (visto che nel momento in cui configuraEAvvia viene invocato
	 * UserCmd non  è connessa) tutti i controlli tranne i due pulsanti di connessione e disconnessione.
	 *  
	 */
	private void configuraEAvvia(){
		// raccolgo i pulsanti degli elettrodomestici nel vettore
		
		bottoni = new Vector<Button>();
		bottoni.add(btn1);
		bottoni.add(btn2);
		bottoni.add(btn3);
		bottoni.add(btn4);
		bottoni.add(btn5);
		bottoni.add(btn6);
		bottoni.add(btn7);
		bottoni.add(btn8);
		bottoni.add(btn9);
		
		stati = new Vector<StatoElettrodomestico>();
		for (int k = 0; k < this.numPulsantiElettrodomestici; k++) {
				stati.add(StatoElettrodomestico.spento);		
		}
		
		// faccio caricare le impostazioni da file, creare e far partire gli oggetti da Edi
		Edi edi = new Edi();
		edi.doJob();
		// ottengo il riferimento a usercmd;
		
		usercmd = UserCmd.getInstance();
		//  mi registro a userCmd
		usercmd.addGui(this);
		
		// aggiorno la visibilità degli elementi del sistema ( in questo caso li nascondo, visto che non sono connesso)
		
		this.aggiornaVisibilità();
	}
	
	/**
	 * aggiorno la visibilità di tutti i controlli tranne quella dei pulstanti di connessione e disconnessione
	 * per questi vale una regola a parte: se si è connessi deve essere abilitato il pulsante di disconnessione e viceversa
	 */
	private void aggiornaVisibilità(){
		boolean connesso = usercmd.isConnesso();
		// aggiorno la visibilità dei bottoni degli elettrodomestici
		for (Button bottone : bottoni) {
			bottone.setVisible(connesso);
		}
		this.lblConsumo.setVisible(connesso);
		this.lblIndicazioneConsumoComplessivo.setVisible(connesso);
		this.lblIndicazioneSogliaDiConsumo.setVisible(connesso);
		this.lblSoglia.setVisible(connesso);
		this.txtComunicazione.setVisible(connesso);
		
		this.btnConnetti.setEnabled(!connesso);
		this.btnDisconnetti.setEnabled(connesso);
		
		
	}
	
	/**
	 * metodo che aggiorna i controlli sulla base del nuovo status
	 * in particolare per ogni pulsante ne aggiorna il testo con il nome dell'elettrodomestico
	 * e sotto il consumo, ne cambia il colore in base allo stao: rosso>spento, verde>esercizio, azzurro>avvio, arancione>disattivato.
	 * aggiorna le etichette di consumoComplessivo e soglia e mette nella casella di testo l'eventuale comunicazione
	 */
	private void refresh(IStatus newStatus){
		
		for (int k = 0; k < bottoni.size(); k++) {
			Button bottone = bottoni.get(k);
			IReportElettrodomestico report = newStatus.getReports().get(k);
			System.out.println(report.getIdElettrodomestico()+" ("+report.getConsumoAttuale()+")");
			bottone.setText(report.getIdElettrodomestico()+" ("+report.getConsumoAttuale()+")");
			if(report.getStato()== StatoElettrodomestico.avvio){
				bottone.setBackground(this.coloreAvvio);
				stati.remove(k);
				stati.add(k,StatoElettrodomestico.avvio);
			}
			else if(report.getStato()== StatoElettrodomestico.esercizio){
				bottone.setBackground(this.coloreEsercizio);
				stati.remove(k);
				stati.add(k,StatoElettrodomestico.esercizio);
			}
			else if(report.getStato()== StatoElettrodomestico.disattivato){
				bottone.setBackground(this.coloreDisattivato);
				stati.remove(k);
				stati.add(k,StatoElettrodomestico.disattivato); 
			}
			else if(report.getStato()== StatoElettrodomestico.spento){
				bottone.setBackground(this.coloreSpento);
				stati.remove(k);
				stati.add(k,StatoElettrodomestico.spento);
			}
			
			
		}// fine ciclo pulsanti
		
		this.lblConsumo.setText(String.valueOf(newStatus.getConsumoAttualeComplessivo()));
		this.lblSoglia.setText(String.valueOf(newStatus.getSoglia()));
		this.txtComunicazione.setText(newStatus.getComunicazione());
		
		
	}
	
	/**
	 * metodo che invoca usercmd quando riceve un nuovo status.
	 * si aggiornano le visibilità dei controlli e, se si è connessi
	 * si aggiornano i controlli che hanno a che fare con lo status
	 */
	public void update(IStatus newStatus) {
		System.out.println("ricevutoStatus");
		if(usercmd.isConnesso())
			this.refresh(newStatus);
		this.aggiornaVisibilità();
		
	}
	
	/**
	 * 
	 * i pulanti hanno nel testo l'identificativo dell'elettrodomestico e ,dopo un a capo li consumo tra parentesi
	 * è utile reperire l'id dell'elettrodomestico dal testo del pulsante,
	 * questo metodo fa proprio questo.
	 * 
	 * @param textBottone il testo del bottone da cui estrrre l'identificativo dell'elettrodomestico.
	 * il testo del bottone deve essere una stringa tipo "id\n(consumo)"
	 * @return l'identificativo dell'elettrodomestico
	 */
	private String getIdElettrodomesticoFromTextBottone(String textBottone){
		String idElettrodomestico = textBottone.substring(0, textBottone.indexOf(" "));
		return idElettrodomestico;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof Object[]) {
				return (Object[]) parent;
			}
	        return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(null);
		
		 btnConnetti = new Button(parent, SWT.NONE);
		 btnConnetti.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnConnettiMouseDown();
		 	}
		 });
		btnConnetti.setBounds(103, 43, 98, 40);
		btnConnetti.setText("Connetti");
		
		 btnDisconnetti = new Button(parent, SWT.NONE);
		 btnDisconnetti.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnDisconnettiMouseDown();
		 	}
		 });
		btnDisconnetti.setEnabled(false);
		btnDisconnetti.setBounds(341, 43, 98, 40);
		btnDisconnetti.setText("Disconnetti");
		
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setBounds(10, 89, 574, 2);
		
		 btn1 = new Button(parent, SWT.NONE);
		 btn1.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(0);
		 	}
		 });
		btn1.setText("1");
		btn1.setBounds(172, 138, 52, 40);
		
		
		 btn2 = new Button(parent, SWT.NONE);
		 btn2.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(1);
		 	}
		 });
		btn2.setBounds(230, 138, 52, 40);
		btn2.setText("2");
		
		 btn3 = new Button(parent, SWT.NONE);
		 btn3.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(2);
		 	}
		 });
		btn3.setBounds(288, 138, 52, 40);
		btn3.setText("3");
		
		 btn4 = new Button(parent, SWT.NONE);
		 btn4.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(3);
		 	}
		 });
		btn4.setBounds(172, 184, 52, 40);
		btn4.setText("4");
		
		 btn5 = new Button(parent, SWT.NONE);
		 btn5.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(4);
		 	}
		 });
		btn5.setBounds(230, 184, 52, 40);
		btn5.setText("5");
		
		 btn6 = new Button(parent, SWT.NONE);
		 btn6.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(5);
		 	}
		 });
		btn6.setBounds(288, 184, 52, 40);
		btn6.setText("6");
		
		 btn7 = new Button(parent, SWT.NONE);
		 btn7.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(6);
		 	}
		 });
		btn7.setBounds(172, 230, 52, 40);
		btn7.setText("7");
		
		 btn8 = new Button(parent, SWT.NONE);
		 btn8.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(7);
		 	}
		 });
		btn8.setBounds(230, 230, 52, 40);
		btn8.setText("8");
		
		 btn9 = new Button(parent, SWT.NONE);
		 btn9.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(8);
		 	}
		 });
		btn9.setBounds(288, 230, 52, 40);
		btn9.setText("9");
		
		 lblConsumo = new Label(parent, SWT.NONE);
		lblConsumo.setFont(SWTResourceManager.getFont("Courier", 14, SWT.BOLD));
		lblConsumo.setAlignment(SWT.RIGHT);
		lblConsumo.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		lblConsumo.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblConsumo.setBounds(172, 303, 60, 22);
		lblConsumo.setText("***");
		
		 lblIndicazioneConsumoComplessivo = new Label(parent, SWT.NONE);
		lblIndicazioneConsumoComplessivo.setBounds(23, 304, 143, 22);
		lblIndicazioneConsumoComplessivo.setText("Consumo Complessivo");
		
		 lblSoglia = new Label(parent, SWT.NONE);
		lblSoglia.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblSoglia.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		lblSoglia.setAlignment(SWT.RIGHT);
		lblSoglia.setFont(SWTResourceManager.getFont("Courier", 14, SWT.BOLD));
		lblSoglia.setBounds(379, 303, 60, 22);
		lblSoglia.setText("***");
		
		 lblIndicazioneSogliaDiConsumo = new Label(parent, SWT.NONE);
		lblIndicazioneSogliaDiConsumo.setBounds(259, 303, 114, 22);
		lblIndicazioneSogliaDiConsumo.setText("Soglia di Consumo");
		
		txtComunicazione = new Text(parent, SWT.BORDER);
		txtComunicazione.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtComunicazione.setText("some text");
		txtComunicazione.setEditable(false);
		txtComunicazione.setBounds(103, 354, 336, 70);
		
		this.configuraEAvvia();
		
	}

	protected void btnElettrodomesticoMouseDown(int index) {
		StatoElettrodomestico stato = stati.get(index);
		Button bottone = bottoni.get(index);
		String idElettrodomestico = this.getIdElettrodomesticoFromTextBottone(bottone.getText());
		if(stato == StatoElettrodomestico.spento)
			usercmd.accendiElettrodomestico(idElettrodomestico);
		else if(stato == StatoElettrodomestico.avvio||stato == StatoElettrodomestico.esercizio||stato == StatoElettrodomestico.disattivato)
			usercmd.spegniElettrodomestico(idElettrodomestico);
		
		
	}

	protected void btnDisconnettiMouseDown() {
		usercmd.disconnetti();
		
	}

	protected void btnConnettiMouseDown() {
		usercmd.connetti();
		
	}

	@Override
	public void setFocus() {
		;
		
	}
}