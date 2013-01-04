package gov.ymp.csiTest.people;

import gov.ymp.csi.people.*;
//import gov.ymp.lotus.core.lotusapi;
import gov.ymp.csi.db.*;
import java.util.ArrayList;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;

import junit.framework.TestCase;

public class TestNotesUtils extends TestCase {
	/**TestLocation is a class that adds a Location and retrieves
	 * Location information for a row added to the CSI.LOCATION table
	 * 
	 * @author Rajesh Patel
	 * 
	 */
	  private NotesUtils NotesUtils;
	  private String jString;
	  private DbConn myConn;
	  private ArrayList arraylist;
	  
	  protected void setUp() {
		  
		long pID;
	    jString = "";
	    
		//create NotesUtils object
		NotesUtils = new NotesUtils();
		
	  try{			
		
		arraylist = NotesUtils.getNotesInfo("YDLNStaging.ymp.gov:63148","rtest","12345678", "YDLNStaging/YD/RWDOE", "raj/names.nsf");
	  
		 }
		 catch(Exception e){
			System.out.println(e);
		 }
	  }

      protected void tearDown() {
    	  myConn.release();
      }
		  
  	public void testNotesUtils(){  
  		
  }
}
	