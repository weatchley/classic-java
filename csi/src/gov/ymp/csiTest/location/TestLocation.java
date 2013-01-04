package gov.ymp.csiTest.location;

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

public class TestLocation extends TestCase {
	/**TestLocation is a class that adds a Location and retrieves
	 * Location information for a row added to the CSI.LOCATION table
	 * 
	 * @author Rajesh Patel
	 * 
	 */
	  private Location Location;
	  private String jString;
	  private DbConn myConn;
	  
	  protected void setUp() {
		  
		long lID;
	    jString = "";
	    
		//create Location object
		Location = new Location();
		
	  try{			
/*		myConn = new DbConn("CSI");
		
		Location.setName("testname");
		Location.addLocation(myConn);
		
		lID = Location.getID();
		//Retrieve location information
	    Location.getLocationInfo (myConn, lID);

		 jString = Location.getName();*/
		  
		 }
		 catch(Exception e){
			System.out.println(e);
		 }
	  }

      protected void tearDown() {
    	  //myConn.release();
      }
		  
  	public void testLocation(){  
		  assertEquals(jString, "testname");
		System.out.println(jString);
  }
}
	