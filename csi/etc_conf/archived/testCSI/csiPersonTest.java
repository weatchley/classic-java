package gov.ymp.csiTest;

import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.items.*;
import gov.ymp.csi.UNID.*;

public class csiPersonTest {
	/**
	 * @param args
	 */
	public static void main(String[] args){	
	
		DbConn myConn = new DbConn("csi");
		
		/****************************************/	
		/*** Person.getPersonSet call testing ***/
		/****************************************/
		/*
			 
		System.out.println("csiPersonTest called!@#");
			System.out.println("~~~ Person.getPersonSet() calling...");
			Person po = new Person();
			Person[] per = po.getPersonSet(myConn,1,null); //0 for RW.DOW.GOV, 1 for ydservice domain, 3 for ymservices
			
				for (int i=0; i<per.length; i++) {
					System.out.println(
							per[i].getID()+", "
							+per[i].getUserName()+", "
							+per[i].getDomainName()+", "
							+per[i].getDomainID()
							);	
				}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println(per.length + " records found");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		*/	
		/*****************************************/	
		/*** LDAPUtils.searchLDAP call testing ***/
		/*****************************************/
	
		System.out.println("~~~ LDAPUtils.searchLDAP() calling...");
		

		String userName = "ServiceCSI";
		
		//ydservice settings
		//String domainName = "ydservices";
		//String password = "zaq1@WSX";
		
		//rw.doe.gov settings
		String domainName = "rw.doe.gov";
		String password = "caT&M0u$3g@m3$";
		
		int domainID = 0; 
		String server;
		String domainCode;
		boolean isValid = false;
		if (domainName.equalsIgnoreCase("ydservices")) {
		    server = "ydservices.ymp.gov";
		    //server = "ydntc2.ydservices.ymp.gov";
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
		
		/*
		isValid = LDAPUtils.synchCSI(userName, password, domainName, server, domainCode, domainID, myConn);
		if (!isValid) {
			System.out.println(userName+" not validated");
		}else{
			System.out.println("************* synchCSI ended successfully");
		}
		*/
		
		isValid = LDAPUtils.synchLDAP(userName, password, domainName, server, domainCode, domainID, myConn);
		if (!isValid) {
			System.out.println(userName+" not validated");
		}else{
			System.out.println("************* synchLDAP ended successfully");
		}		
		myConn.release();
	}
}


