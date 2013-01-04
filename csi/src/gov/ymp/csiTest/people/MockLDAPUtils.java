package gov.ymp.csiTest.people;

import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.items.*;
import gov.ymp.csi.UNID.*;

/**
* MockLDAPUtils is a helper class for LDAPUtils.
* MockLDAPUtils makes calls to methods that are in 
* LDAPUtils to perform CSI/LDAPS synchronization
* task.
* 
* @author   Shuhei Higashi
*/
public class MockLDAPUtils {
	/**
	 * @param args
	 */
	public static void main(String[] args){	
	
		DbConn myConn = new DbConn("csi");
		
		/*****************************************/	
		/*** LDAPUtils.* call testing ***/
		/*****************************************/
	
		System.out.println("~~~ LDAPUtils.* calling...");
		
		String userName = "ServiceCSI";
		
	//ydservice settings
		String domainName = "ydservices";
		String password = "zaq1@WSX";
			
		int domainID = 0; 
		String server;
		String domainCode;
		boolean isValid = false;
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
		
		//the following segment disable CSI accounts that cannot be located in LDAP
		
		System.out.println("Deleting users to CSI for "+ domainName);
		
		isValid = LDAPUtils.synchCSI(userName, password, domainName, server, domainCode, domainID, myConn);
		if (!isValid) {
			System.out.println(userName+" not validated");
		}else{
			System.out.println("************* LDAPUtils.synchCSI ended successfully for "+domainName);
		}
		
		//rw.doe.gov settings
		domainName = "rw.doe.gov";
		password = "caT&M0u$3g@m3$";
		server = "rw.doe.gov"; // ?
	    domainCode = "HQ";
	    domainID = 2;
		
	    System.out.println("Deleting users from CSI for "+ domainName);
		
		isValid = LDAPUtils.synchCSI(userName, password, domainName, server, domainCode, domainID, myConn);
		if (!isValid) {
			System.out.println(userName+" not validated");
		}else{
			System.out.println("************* LDAPUtils.synchCSI ended successfully for "+domainName);
		}
	    
		myConn.release();
	}
}



