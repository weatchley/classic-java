package gov.ymp.csiTest.UNID;

import gov.ymp.csi.db.DbConn;
import junit.framework.TestCase;
import gov.ymp.csi.UNID.*;

/**
* UHashTest is the junit test class for gov.ymp.csi.UNID.HashTest class
*
* @author   S. Higashi
*/

public class TestUHash extends TestCase {
	
	private UHash uh;
	
	public void setUp() throws Exception{
		uh = new UHash();
	}
	
	public void tearDown( ) {
		uh = null;
	}
	
    /**
    * Tests UHash lookup method
    *
    */	
	public void testLookup(){
	//real code here
	}
	
}

