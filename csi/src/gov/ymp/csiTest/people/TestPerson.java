package gov.ymp.csiTest.people;

import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import java.util.ArrayList;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;

import junit.framework.TestCase;

public class TestPerson extends TestCase {
	/**TestPerson is a class that adds a Person and retrieves
	 * Person information for a row added to the CSI.PERSON table
	 * 
	 * @author Rajesh Patel
	 * 
	 */
	  private Person Person;
	  private String jString;
	  private DbConn myConn;
	  
	  protected void setUp() {
		  
		long pID;
	    jString = "";
	    
		//create Person object
		Person = new Person();
		
	  try{			
		//myConn = new DbConn("CSI");

		/*Person.setLastName("Smith");
		Person.setFirstName("John");
		Person.setEmail("nobody@nobody.com");
		Person.setDomainID(1);
		Person.setPhone("000-000-0000");
		
		Person.add(myConn);*/
		
		//Retrieve person information
		 /*pID = Person.getID();
	     Person.getInfo (myConn, pID);

		 jString = Person.getLastName();*/
		  
		 }
		 catch(Exception e){
			System.out.println(e);
		 }
	  }

      protected void tearDown() {
    	  //myConn.release();
      }
		  
  	public void testLocation(){  
		  assertEquals(jString, "Smith");
		System.out.println(jString);
  }
}
	