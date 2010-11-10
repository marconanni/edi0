package test.messaggi;

import Edi.messaggi.*;

/**
 * @author Marco
 * @version 1.0
 * @created 28-ott-2010 18:53:23
 */
public class ComandoUserCmdTest extends junit.framework.TestCase {
	private String idElettrodomestico = "e1";
	private ComandiUserCmd tipoComando = ComandiUserCmd.accendi;
	private ComandoUserCmd comando;

	public ComandoUserCmdTest(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	/**
	 * 
	 * @param arg0
	 */
	public ComandoUserCmdTest(String arg0){
		super(arg0);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args){

	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void setUp()
	  throws Exception{
		super.setUp();
		this.comando = new ComandoUserCmd(tipoComando, idElettrodomestico);
	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void tearDown()
	  throws Exception{
		super.tearDown();
	}

	public final void testGetComando(){
		assertEquals(tipoComando, comando.getComando());
	}
	public final void testGetIdElettrodomestico(){
		assertEquals(idElettrodomestico, comando.getIdElettrodomestico());
	}
}//end ComandoUserCmdTest