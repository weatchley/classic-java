package gov.ymp.csiTest.spring.ldap;

import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.items.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.spring.ldap.*;

public class MockLDAPUtilsSpring {
	/**
	 * @param args
	 */
	public static void main(String[] args){	
		System.out.println("MockLDAPUtils() called!");
		DbConn myConn = new DbConn("csi");
		String userName = "ServiceCSI";
		
		//ydservice settings
			String domainName = "ydservices";
			String password = "zaq1@WSX";
			
		//rw.doe.gov settings
			//String domainName = "rw.doe.gov";
			//String password = "caT&M0u$3g@m3$";
			
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
			
			isValid = LDAPUtilsSpring.synchCSI(userName, password, domainName, server, domainCode, domainID, myConn);
			if (!isValid) {
				System.out.println(userName+" not validated");
			}else{
				System.out.println("************* synchCSI ended successfully for "+domainName);
			}
			
			
			//the following segment create CSI accounts to match LDAP accounts
			
			isValid = LDAPUtilsSpring.synchLDAP(userName, password, domainName, server, domainCode, domainID, myConn);
			if (!isValid) {
				System.out.println(userName+" not validated");
			}else{
				System.out.println("************* synchLDAP ended successfully");
			}
					
			myConn.release();	
	}
}