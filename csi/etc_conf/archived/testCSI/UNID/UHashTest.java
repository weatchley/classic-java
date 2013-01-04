package gov.ymp.csiTest.UNID;

import gov.ymp.csi.db.DbConn;
import junit.framework.TestCase;
import gov.ymp.csi.UNID.*;

/**
* UHashTest is the junit test class for gov.ymp.csi.UNID.HashTest class
*
* @author   S. Higashi
*/

public class UHashTest extends TestCase {
	
	private UHash uh;
	
	protected void setUp() throws Exception{
		uh = new UHash();
	}
	
	protected void tearDown( ) {
		uh = null;
	}
}

