package gov.ymp.csiTest.people;

import gov.ymp.csi.db.DbConn;
import junit.framework.TestCase;
import gov.ymp.csi.people.*;

/**
* LDAPUtilsTest is the junit test class for gov.ymp.csi.people.LDAPUtils class
*
* @author   S. Higashi
*/

public class TestLDAPUtils extends TestCase {
	
	private LDAPUtils lu;
	private DbConn myConn;
	private String userName;
	private String domainName;
	private String password;
	private int domainID; 
	private String server;
	private String domainCode;
	
	public void setUp() throws Exception{
	System.out.println("~~~ LDAPUtilsTest.setUp called...");
		myConn = new DbConn("csi");	
		userName = "ServiceCSI";
		domainName = "ydservices";
		password = "zaq1@WSX";
		domainID = 0; 
		if (domainName.equalsIgnoreCase("ydservices")) {
		    server = "ydservices.ymp.gov";
		    domainCode = "YD";
		    domainID = 1;
		} else if (domainName.equalsIgnoreCase("rw.doe.gov")) {
		    server = "rw.doe.gov"; // ?
		    domainCode = "HQ";
		    domainID = 2;
		} else if (domainName.equalsIgnoreCase("ymservices")) {
		    server = "ymservices.ymp.gov"; // ?
		    domainCode = "YM";
		    domainID = 3;
		} else {
		    server = "na"; // ?
		    domainCode = "na";
		    domainID = 0;
		}
		
		lu = new LDAPUtils();
	}
	
	public void tearDown( ) {
		lu = null;
	}
	
    /**
    * Tests LDAPUtils checklogin method
    *
    */	
	public void testcheckLogin(){
	System.out.println("testcheckLogin() called...");
	assertTrue("checkLogin should return true.", 
			lu.checkLogin(userName, password, domainName, server, domainCode, domainID, myConn)
			);
	}
	
    /**
    * Tests LDAPUtils synchLDAP method
    *
    */	
	public void testsynchLDAP(){
	System.out.println("testsynchLDAP() called...");
	assertTrue("synchLDAP should return true.", 
			lu.synchCSI(userName, password, domainName, server, domainCode, domainID, myConn)
			);
	}	
	
    /**
    * Tests LDAPUtils synchCSI method
    *
    */	
	public void testsynchCSI(){
	System.out.println("testsynchCSI() called...");
	assertTrue("sychCSI should return true.", 
			lu.synchLDAP(userName, password, domainName, server, domainCode, domainID, myConn)
			);
	}	
		
}

