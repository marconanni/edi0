package Edi;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.out.println("*** Edi Activator  starts; bundlestore=" + 
					context.getProperty("osgi.bundlestore") );
		doJob();
	}
	
	protected void doJob(){
		try{
			new Edi().doJob();
		}catch( Exception e){
			System.out.println("*** Edi Activator  ERROR " + e );		
		}
	}
	public void stop(BundleContext context) throws Exception {
		System.out.println("*** Edi Activator stops"   );
	}
}
