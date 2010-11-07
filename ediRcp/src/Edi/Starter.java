package Edi;
import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class Starter {
	private final String path = "file:lib/bundles/";
	private BundleContext context;

	public void doJob() throws Exception {
		loadMySystem();
		loadBundle(path + "it.unibo.is.interfaces_1.0.0.jar");
 		loadBundle(path + "it.unibo.tuprolog_1.0.0.jar");
 		loadBundle(path + "it.unibo.platform.lindalike_1.0.0.jar");
		loadBundle(path + "it.unibo.platform.medcl_1.0.0.jar");
		loadBundle(path + "it.unibo.Edi_1.0.0.jar");
	}

	protected void loadMySystem() throws Exception {
		String[] equinoxArgs = new String[]{"-console","-clean"};//(1)
		context = EclipseStarter.startup(equinoxArgs, null);
		System.out.println("*** Starter osgi.bundlestore=" + 
							context.getProperty("osgi.bundlestore") );
	}

	protected void loadBundle(String locationBundle)  {
		Bundle bundle;
		try {
			bundle = context.installBundle(locationBundle);
			bundle.start();
		} catch (BundleException e) {
			System.out.println("ERROR " + e);
		}
	}

	public static void main(String args[]) throws Exception {
		Starter starter = new Starter();
		starter.doJob();
	}
}
