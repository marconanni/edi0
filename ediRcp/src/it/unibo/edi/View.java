package it.unibo.edi;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Layout;

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

	
	private IStatus status;
	
	private final Color coloreAvvio = Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
	private final Color coloreEsercizio = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
	private final Color coloreDisattivato = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
	private final Color coloreSpento = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	private Composite parent;
 
	
	
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
		
		
		
		
		// faccio caricare le impostazioni da file, creare e far partire gli oggetti da Edi
		Edi edi = new Edi();
		edi.doJob();
		// ottengo il riferimento a usercmd;
		
		usercmd = UserCmd.getInstance();
		//  mi registro a userCmd
		usercmd.addGui(this);
		
		
	}
	
	/**
	 * aggiorno la visibilità di tutti i controlli tranne quella dei pulstanti di connessione e disconnessione
	 * per questi vale una regola a parte: se si è connessi deve essere abilitato il pulsante di disconnessione e viceversa
	 */
	private void aggiornaVisibilità(){
		boolean connesso = usercmd.isConnesso();
		System.out.println("Connesso ="+connesso);
	
		for (Button bottone : getBottoniElettrodomestici()) {
			bottone.setVisible(connesso);
		}
		this.lblConsumo.setVisible(connesso);
		this.lblIndicazioneConsumoComplessivo.setVisible(connesso);
		this.lblIndicazioneSogliaDiConsumo.setVisible(connesso);
		this.lblSoglia.setVisible(connesso);
		this.btnConnetti.setEnabled(!connesso);
		this.btnDisconnetti.setEnabled(connesso);
		
		
	}
	
	private void hideAndDisposeOldStatusControls(){
		for (Button bottone : this.getBottoniElettrodomestici() ) {
			if (bottone!=null)
			bottone.setVisible(false);
		}
		
		if (lblConsumo!=null)
			lblConsumo.setVisible(false);
		if (lblSoglia!=null)
			lblSoglia.setVisible(false);
		if (txtComunicazione!=null)
			txtComunicazione.setVisible(false);
			
	}
	
	/**
	 * metodo che aggiorna i controlli sulla base del nuovo status
	 * in particolare per ogni pulsante ne aggiorna il testo con il nome dell'elettrodomestico
	 * e sotto il consumo, ne cambia il colore in base allo stao: rosso>spento, verde>esercizio, azzurro>avvio, arancione>disattivato.
	 * aggiorna le etichette di consumoComplessivo e soglia e mette nella casella di testo l'eventuale comunicazione
	 */
	private void refresh(IStatus newStatus){
		
		
		
		
		hideAndDisposeOldStatusControls();
		
		this.drawStatusControls(newStatus);
		aggiornaVisibilità();
	}
	
	/**
	 * metodo che invoca usercmd quando riceve un nuovo status.
	 * si aggiornano le visibilità dei controlli e, se si è connessi
	 * si aggiornano i controlli che hanno a che fare con lo status
	 */
	public void update(IStatus newStatus) {
		this.status = newStatus;
		System.out.println("ricevutoStatus");
		
		
			Display.getDefault().asyncExec(new Runnable() {
				 		            public void run() {
				 		            	refresh(status);
				  		            }
				 		     });
			
//		this.aggiornaVisibilità();
		
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
	 * 
	 * @return un vettore contentente tutti i bottoni degli elettrodomestici
	 */
	private Vector<Button> getBottoniElettrodomestici(){
		Vector <Button> vettore = new Vector<Button>();
		vettore.add(btn1);
		vettore.add(btn2);
		vettore.add(btn3);
		vettore.add(btn4);
		vettore.add(btn5);
		vettore.add(btn6);
		vettore.add(btn7);
		vettore.add(btn8);
		vettore.add(btn9);
		return vettore;
		
	}
	
	private Color getColorForStatus(StatoElettrodomestico stato){
		if (stato == StatoElettrodomestico.avvio)
			return this.coloreAvvio;
		if (stato == StatoElettrodomestico.disattivato)
			return this.coloreDisattivato;
		if (stato == StatoElettrodomestico.esercizio)
			return this.coloreEsercizio;
		if (stato == StatoElettrodomestico.spento)
			return this.coloreSpento;
		else 
			return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
	}
	
	
	
	private Button createButtonFromReport(IReportElettrodomestico report,int x,int y, int width, int height){
		Button bottone = new Button(parent,SWT.NONE);
		bottone.setText(report.getIdElettrodomestico()+"  ("+report.getConsumoAttuale()+")");
		bottone.setBackground(getColorForStatus(report.getStato()));
		bottone.setData(report);
		bottone.setBounds(x, y, width, height);
		return bottone;
	}
	
	private void drawStatusControls(IStatus status){
		// creo i bottoni
		btn1=createButtonFromReport(status.getReports().get(0), 172, 138, 52, 40);
		btn2=createButtonFromReport(status.getReports().get(1), 230, 138, 52, 40);
		btn3=createButtonFromReport(status.getReports().get(2), 288, 138, 52, 40);
		btn4=createButtonFromReport(status.getReports().get(3), 172, 184, 52, 40);
		btn5=createButtonFromReport(status.getReports().get(4), 230, 184, 52, 40);
		btn6=createButtonFromReport(status.getReports().get(5), 288, 184, 52, 40);
		btn7=createButtonFromReport(status.getReports().get(6), 172, 230, 52, 40);
		btn8=createButtonFromReport(status.getReports().get(7), 230, 230, 52, 40);
		btn9=createButtonFromReport(status.getReports().get(8), 288, 230, 52, 40);
		
		// collego gli handlers ai bottoni 
		btn1.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(btn1);
		 	}
		 });
		btn2.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(btn2);
		 	}
		 });
		btn3.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(btn3);
		 	}
		 });
		btn4.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(btn4);
		 	}
		 });
		btn5.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(btn5);
		 	}
		 });
		btn6.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(btn6);
		 	}
		 });
		btn7.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(btn7);
		 	}
		 });
		btn8.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(btn8);
		 	}
		 });
		btn9.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseDown(MouseEvent e) {
		 		btnElettrodomesticoMouseDown(btn9);
		 	}
		 });
		
		
		lblConsumo = new Label(parent, SWT.NONE);
		lblConsumo.setFont(SWTResourceManager.getFont("Courier", 14, SWT.BOLD));
		lblConsumo.setAlignment(SWT.RIGHT);
		lblConsumo.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		lblConsumo.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblConsumo.setBounds(172, 303, 60, 22);
		lblConsumo.setText(String.valueOf(status.getConsumoAttualeComplessivo()));
		
		 lblIndicazioneConsumoComplessivo = new Label(parent, SWT.NONE);
		lblIndicazioneConsumoComplessivo.setBounds(23, 304, 143, 22);
		lblIndicazioneConsumoComplessivo.setText("Consumo Complessivo");
		
		 lblSoglia = new Label(parent, SWT.NONE);
		lblSoglia.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblSoglia.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		lblSoglia.setAlignment(SWT.RIGHT);
		lblSoglia.setFont(SWTResourceManager.getFont("Courier", 14, SWT.BOLD));
		lblSoglia.setBounds(379, 303, 60, 22);
		lblSoglia.setText(String.valueOf(status.getSoglia()));
		
		 lblIndicazioneSogliaDiConsumo = new Label(parent, SWT.NONE);
		lblIndicazioneSogliaDiConsumo.setBounds(259, 303, 114, 22);
		lblIndicazioneSogliaDiConsumo.setText("Soglia di Consumo");
		
		txtComunicazione = new Text(parent, SWT.BORDER);
		txtComunicazione.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtComunicazione.append(status.getComunicazione());
		txtComunicazione.setEditable(false);
		txtComunicazione.setBounds(103, 354, 336, 70);
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
		this.parent = parent;
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
		
		
		
		 
		
			this.configuraEAvvia();

		
	
	}

	protected void btnElettrodomesticoMouseDown(Button bottone) {
		IReportElettrodomestico report = (IReportElettrodomestico) bottone.getData();
		StatoElettrodomestico stato = report.getStato();
		String idElettrodomestico =report.getIdElettrodomestico();
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