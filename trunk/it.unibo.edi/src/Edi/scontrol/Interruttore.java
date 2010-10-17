package Edi.scontrol;

	import Edi.Subject;
import Edi.elettrodomestico.IElettrodomestico;
import Edi.messaggi.*;

	
/**
 * 
 * @author Marco
 *
 *classe che provvede a ricevere le richieste da Scontrol e a modificare lo stato dell'elettrodomestico
 *al quale è collegato 
 */
public class  Interruttore  implements IInterruttore{
	
	
		
		private IElettrodomestico elettrodomesticoCollegato;
		private String id;
		
		/**
		 * 
		 * @param l'identificativo dell'interruttore.
		 */
		public Interruttore (String id){
			this.id= id;
		}
		
		/**
		 * @see Edi.scontrol.IInterruttore#isOn()
		 */
		public boolean isOn(){
			return elettrodomesticoCollegato.isOn();
		}
		
		/**
		 * @see Edi.scontrol.IInterruttore#isOff()
		 */
		public boolean isOff(){
			return elettrodomesticoCollegato.isOff();
		}
		
		/**
		 * @see Edi.scontrol.IInterruttore#turnOn()
		 */
		public void turnOn(){
			elettrodomesticoCollegato.accendi();
		}
		
		/**
		 * @see Edi.scontrol.IInterruttore#turnOff()
		 */
		public void turnOff(){
			elettrodomesticoCollegato.spegni();
		}
		
		/**
		 * 
		 * @return true se l'interruttore è collegato ad un elettrodomestico
		 */
		public boolean isCollegato(){
			if(this.elettrodomesticoCollegato==null)
				return true;
			else
				return false;
		}
		
		/**
		 * 
		 * @return l'identificativo dell'elettrodomestico
		 */
		public String getId(){
			return this.id;
		}
		
		/**
		 * 
		 * @return l'identificativo dell'elettrodomestico collegato all'interruttore
		 */
		public String getIdElettrodomesticoCollegato(){
			return this.elettrodomesticoCollegato.getId();
		}
		
		
		/**
		 * metodo che consente di impostare l'elettrodomestico attualemte collegato all'interruttore
		 * @param elettrodomesticoCollegato l'elettrodomestico da collegare all'interruttore 8 quello al quale invierà
		 * i comandi di accensione e soegnimento.
		 */
		public void setElettrodomesticoCollegato(IElettrodomestico elettrodomesticoCollegato) {
			this.elettrodomesticoCollegato = elettrodomesticoCollegato;
		}
		
		

		@Override
		public String toString() {
			return "Interruttore [elettrodomesticoCollegato="
					+ elettrodomesticoCollegato + ", id=" + id + "]";
		}
		
		
		
		

}
