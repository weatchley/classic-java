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

public class TestPosition extends TestCase {
	/**TestPosition is a class that adds a Position and retrieves
	 * Position information for a row added to the CSI.POSITION table
	 * 
	 * @author Rajesh Patel
	 * 
	 */
	  private Position Position;
	  private String jString;
	  private DbConn myConn;
	  
	  protected void setUp() {
		  
		long pID;
	    jString = "";
	    
		//create Position object
		Position = new Position();
		
	  try{			
		/*myConn = new DbConn("CSI");
		
		Position.setTitle("testposition");
		Position.add(myConn);
		
		//Retrieve position information
		pID = Position.getID();
	    Position.getInfo (myConn, pID);

		 jString = Position.getTitle();*/
		  
		 }
		 catch(Exception e){
			System.out.println(e);
		 }
	  }

      protected void tearDown() {
    	  //myConn.release();
      }
		  
  	public void testLocation(){  
		  assertEquals(jString, "testposition");
		System.out.println(jString);
  }
}
	