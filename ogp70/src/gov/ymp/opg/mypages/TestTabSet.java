package gov.ymp.opg.mypages;

import gov.ymp.csi.db.DbConn;
import java.util.*;
import junit.framework.TestCase;

/**
* TabSetTest is the test class for TabSet class
*
* @author   S. Higashi
*/

public class TestTabSet extends TestCase {
	private TabSet ts;
	private DbConn myConn;
	private long personID = 0;
	private long[] items = null;
	private long[] testItems = null;
	private int[] orderArray = null;
	
	protected void setUp() throws Exception{
		myConn = new DbConn("csi");
		personID = (long)0; //the user has not logged in...
	}
	
	protected void tearDown( ) {
		ts = null;
		myConn.release();
	}	
	
	/**
    * test tabSet retrieval
    *
    * @return void
    */	
	public void testRetrieve() throws Exception {
		//System.out.println("TabSetTest.testRetrieve() called...");
		ts = new TabSet("OPG-TabSet-Home-Default",myConn);
		items = ts.getItemsArray();
		for (int i=0; i<items.length; i++) {
			Tab t =	null;
			t = new Tab(items[i],myConn);
			//System.out.println(t.getText()+", "+t.getLink());			
			if(i==0){
				assertEquals("What's New", t.getText());
				assertEquals("../portlets/whats_new/index.jsp", t.getLink());
			}
		}
		items = null; //clear long[]...
	}	
	
	/**
    * test rearrange method in TabSet class
    *
    */	
	public void testRearrange() throws Exception {
		System.out.println("TabSetTest.testRearrange() called...");
		
		personID = (long)1214; //tester's id
		long[] idArray = null; //empty array to hold item ids
		long[] idArrayNew = null; //new empty array for testing
		long [] itemsTmp = null;
		long [] itemsNew = null;
		int[] newArray = {5,4,3,2,1}; //new order array
		int[] oldArray = {1,2,3,4,5}; //new order array
		
		//prepare a dummy tabset to manipulate...
			ts = new TabSet("OPG-TabSet-Home-"+personID,myConn);
			if (ts == null || ts.getID() == 0) { //if the tabset does not exist, create it from the default set
				TabSet tsDef = new TabSet("OPG-TabSet-Home-Default", myConn);
	            ts = new TabSet(myConn, personID);
	            ts.setDescription("OPG-TabSet-Home-" + personID);
	            ts.setCreator(personID);
	            itemsTmp = tsDef.getItemsArray();
	            for (int i=0; i<itemsTmp.length; i++) {
	                ts.addItem(itemsTmp[i], myConn);
	            }
	            ts.save(myConn, personID);
	        }else{ //the tabset exists
	        	items = ts.getItemsArray();
	        	
	        	items = ts.getItemsArray();
	        	idArray = new long[items.length];
	        	for (int i=0; i<items.length; i++) {
	    			Tab t = null;
	    			t = new Tab(items[i],myConn);
	    			idArray[i] = t.getID();
	    		}
	        }			
									
		//rearrange
		ts.rearrange(myConn, personID,idArray,newArray);
		
		//test the changes
		
		TabSet tsNew = new TabSet("OPG-TabSet-Home-"+personID,myConn);
		itemsNew = tsNew.getItemsArray();
		idArrayNew = new long[itemsNew.length];
		for (int j=0; j<itemsNew.length; j++) {
			Tab tn = null;
			tn = new Tab(itemsNew[j],myConn);
			idArrayNew[j] = tn.getID();
		}			
		assertEquals(idArrayNew[0], idArray[4]);
		assertEquals(idArrayNew[4], idArray[0]);
		
		//bring back the original tabset
		ts.rearrange(myConn,personID,idArray,oldArray);		
	}
	
}

