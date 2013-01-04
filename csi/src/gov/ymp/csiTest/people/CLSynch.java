package gov.ymp.csiTest.people;

import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.items.*;
import gov.ymp.csi.UNID.*;

/**
* CLSynch is a helper class for LDAPUtils.
* CLSynch makes calls to methods that are in 
* LDAPUtils to perform CSI/LDAPS synchronization
* task.
* 
* @author   Shuhei Higashi
*/
public class CLSynch {
	/**
    * Check to see if a user can log in to the domain
    *
    * @param args[0]  Name of the Target Domain (String)
    * 
    */
	public static void main(String[] args){
		DbConn myConn = new DbConn("csi");
		String userName = "ServiceCSI";
		int domainID = 0; 
		String server;
		String domainCode;
		boolean isValid = false;
		String domainName;
		String password = "";
		domainName = args[0];
		if (domainName.equalsIgnoreCase("ydservices")) {
		    server = "ydservices.ymp.gov";
		    password = "zaq1@WSX";
		    domainCode = "YD";
		    domainID = 1;
		} else if (domainName.equalsIgnoreCase("rw.doe.gov")) {
		    server = "rw.doe.gov"; 
				//server = "rwfcd1.rw.doe.gov"; 
				//server = "192.84.216.20"; 
				//server = "192.84.216.48"; 
		    password = "caT&M0u$3g@m3$";
		    domainCode = "HQ";
		    domainID = 2;
		} else if (domainName.equalsIgnoreCase("ymservices")) {
		    server = "ymservices.ymp.gov"; 
		    domainCode = "YM";
		    domainID = 3;
		} else {
		    server = "na"; 
		    domainCode = "na";
		    domainID = 0;
		}
				
		java.util.Properties p = OSEnvironment.get();
			//p.list(System.out);
    	System.out.println("the current value of CSIPS is : " +
    	p.getProperty("CSIPS"));
		
		System.out.println("Adding users to CSI for "+ domainName);
				try{
					LDAPUtils lu = new LDAPUtils();
					isValid = lu.synchLDAP(userName, password, domainName, server, domainCode, domainID, myConn);
				}catch(Throwable e){
					//e.printStackTrace();
				}
		/*
		if (!isValid) {
			//System.out.println(userName+" not validated");
			System.out.println("************* LDAPUtils.synchLDAP ended successfully with no matches for "+domainName);
		}else{
			System.out.println("************* LDAPUtils.synchLDAP ended successfully with some matches for "+domainName);
		}
		*/
		
		System.out.println("************* LDAPUtils.synchLDAP ended successfully for "+domainName);
		
		//if(isValid){
			System.out.println("Deleting users from CSI for "+ domainName);
			
				try{
					isValid = LDAPUtils.synchCSI(userName, password, domainName, server, domainCode, domainID, myConn);
				}catch(Throwable e){
					//e.printStackTrace();
				}
			
			/*
			if (!isValid) {
				//System.out.println(userName+" not validated");
				System.out.println("************* LDAPUtils.synchCSI ended successfully with no matches for "+domainName);
			}else{
				System.out.println("************* LDAPUtils.synchCSI ended successfully with some matches for "+domainName);
			}
			*/
			
			System.out.println("************* LDAPUtils.synchCSI ended successfully for "+domainName);
			
		//}
		
		myConn.release();
	}

		
}