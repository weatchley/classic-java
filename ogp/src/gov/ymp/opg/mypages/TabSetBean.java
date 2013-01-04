package gov.ymp.opg.mypages;

import gov.ymp.csi.db.DbConn;
import java.io.Serializable;
import java.util.*;
import  java.io.*;

/**
* TabSetBean is a bean class for TabSet class
*
* @author   S. Higashi
*/

public class TabSetBean implements Serializable {
	
	private String jString; //tmp string for json string
	private long personID;
	private TabSet ts = null;
	private long[] items = null;
	private long[] items2 = null;
		
    /**
    * Retrieve an array of a tabset -> convert it into json string
    *
    * @param tabSetName name of the desired tabset
    */	
	

	public String retrieve(String tabSetName){
		DbConn myConn = new DbConn("csi");
		System.out.println("TabSetBean.retrieve() called");
		System.out.println("~~~ retrieving "+tabSetName);
		String rString = "";
		String jString = "";
	ts = new TabSet(tabSetName,myConn);
	items = ts.getItemsArray();
	for (int i=0; i<items.length; i++) {
		Tab t =	null;
		t = new Tab(items[i],myConn);
			System.out.println(t.getText()+", "+t.getLink());
			rString += t.getText() + ", ";
			rString += t.getLink();
			
			System.out.println("What is the value of tab count " + items.length);
			
			String tabText = t.getText();
			int single_quotes = tabText.indexOf("'");
			
			System.out.println("Value of single quotes " + single_quotes);
			
			if(single_quotes > 0)
			{
		       tabText = tabText.replaceAll("'","\'");
		       System.out.println("Found single quotes in the string ");
			}
			
			
			
			if(i == 0) // beginning
			{
				// if contains special character encoding 
			  jString = "{"+ "\"bindings\":[{\"tabName\":\"" + tabText + "\"," + "\"tabLink\":\"" + t.getLink() + "\"},"; 
			  System.out.println("Goes here when i = " + i + " tabName is " + t.getText() );
         	}
			else if(i > 0 && i != (items.length-1)) // middle
			{ 
				//String tabText = t.getText();
				//tabText = tabText.replace('\'', '\'');
			  System.out.println("Goes here when i = " + i + " tabName is " + t.getText() );
			  
			 jString +=	"{\"tabName\":\"" + tabText + "\"," + "\"tabLink\":\"" + t.getLink() + "\"},"; 					
			}
			else if ( i == (items.length-1) ) // last item 
			{
				//String tabText = t.getText();
				//tabText = tabText.replace('\'', '\'');
				System.out.println("Goes here to the end when i = " + i + " tabName is " + t.getText() );
				jString += "{\"tabName\":\"" + tabText + "\"," + "\"tabLink\":\"" + t.getLink() + "\"}";
			}		
	}	
	jString += "]}"; // end of jstring 
	
	myConn.release();
    //jString = rString;
	return jString;
}
		
    /**
    * Create and save a new tabset 
    *
    * @param personID
    * @param tabSetName name of the desired tabset
    */	
	public long save(long personID,String tabSetName){
		DbConn myConn = new DbConn("csi");
		long tabSetID = 0;
			System.out.println("TabSetBean.save() called");
			System.out.println("~~~ creating a new custom tabset tabs");
		//personID = (long)1214; //shuhei's id
		long [] itemsTmp = null;
		ts = new TabSet(tabSetName+"-"+personID,myConn);
		if (ts == null || ts.getID() == 0) {
			TabSet tsDef = new TabSet(tabSetName + "-Default", myConn);
            ts = new TabSet(myConn, personID);
            ts.setDescription(tabSetName + "-" + personID);
            ts.setCreator(personID);
            itemsTmp = tsDef.getItemsArray();
            for (int i=0; i<itemsTmp.length; i++) {
                ts.addItem(itemsTmp[i], myConn);
            }
            ts.save(myConn, personID);
        }else{
        	items = ts.getItemsArray();
        	if(items.length==0){
        		TabSet tsDef = new TabSet(tabSetName + "-Default", myConn);
        		itemsTmp = tsDef.getItemsArray();
        		for (int i=0; i<itemsTmp.length; i++) {
                    ts.addItem(itemsTmp[i], myConn);
                }
        		ts.save(myConn, personID);
        	}
        	System.out.println("contents of " + tabSetName + "-" + personID + ": "+ts.getID());
        	tabSetID = ts.getID();
        	items = ts.getItemsArray();
    		for (int i=0; i<items.length; i++) {
    			Tab t = null;
    			t = new Tab(items[i],myConn);
    			System.out.println(t.getText()+", "+t.getLink());
    		}
        }
		
		myConn.release();
		return tabSetID;	
	}

    /**
	    * Create and save a new tab 
	    *
	    * @param personID
	    * @param tabSetName name of the desired tabset
	    */	
		public long saveNewTab(long personID,String tabSetName,String output){
			String[] tabdata = output.split("@#");
			
			DbConn myConn = new DbConn("csi");
			long tabSetID = 0;
			System.out.println("TabSetBean.saveNewTab() called");
			System.out.println("~~~ creating a new custom tabset tabs");
	    	Tab t = null;
	    	t = new Tab(5998,myConn);
	    	t.setText(tabdata[0]);
	    	t.setLink(tabdata[1]);
	    	t.save(myConn, personID);
	    	long id = t.getID();

			long [] itemsTmp = null;
			ts = new TabSet(tabSetName+"-"+personID,myConn);
	        ts.addItem(id, myConn);
	        ts.save(myConn, personID);
	        	System.out.println("contents of " + tabSetName + "-" + personID + ": "+ts.getID());
	        	tabSetID = ts.getID();
	        	items = ts.getItemsArray();
	    		for (int i=0; i<items.length; i++) {
	    			Tab t2= null;
	    			t2 = new Tab(items[i],myConn);
	    			System.out.println(t2.getText()+", "+t2.getLink());
	    		}
	    		
			myConn.release();
			return tabSetID;	
		}

	   /**
		  * Delete a tab 
		  *
		  * @param 
		  */	
/*		public long deleteTab(long personID, String tabSetName, int tabrank){
			    System.out.println("TabSetBean.deleteTab() called: "+tabrank);
				DbConn myConn = new DbConn("csi");
				TabSet tst = new TabSet(tabSetName+"-"+personID,myConn);
		        long tabSetID = tst.getID();
		        	
				System.out.println("TabSetBean.deleteTab() called: "+tabSetID);		
				//first, delete tabset contents
				System.out.println("~~~~~~ deleting tabset content");
				items = tst.getItemsArray();

				Tab t = null;
				t = new Tab(items[tabrank],myConn);			
				System.out.println(t.getID()+" will be deleted");
				t.drop(myConn,personID);
				
				//tst.dropItem(items[tabrank]);
				//second, clear tabset associations
				System.out.println("~~~~~~ clearing tabset associations");
				System.out.println(tst.getID()+" will be cleared");
				tst.clearItems();
				//tst.save(myConn, personID);
				//lastly, drop tabset
				//ts.drop(myConn,personID);
				//ts.save(myConn);
				
		        myConn.release();
				return tabSetID;
		}
	*/
		
    /**
    * Delete a tabset 
    *
    * @param 
    */	
	public long delete(long personID, String tabSetName){
		DbConn myConn = new DbConn("csi");
		TabSet tst = new TabSet(tabSetName+"-"+personID,myConn);
		TabSet tsh = new TabSet(tabSetName + "-Default",myConn);
        long tabSetID = tst.getID();
        	
		System.out.println("TabSetBean.delete() called: "+tabSetID);		
		//first, delete tabset contents
		System.out.println("~~~~~~ deleting tabset content");
		items = tst.getItemsArray();
		//default items
		items2 = tsh.getItemsArray();
		for (int i=0; i<items.length; i++) {
			System.out.println("t will be deleted");
			Tab t = null;
			t = new Tab(items[i],myConn);
			//deletes tab only if not in default tabset
			if((i < items2.length && items[i] != items2[i]) || i >= items2.length){
				System.out.println(t.getID()+" will be deleted");
				t.drop(myConn,personID);
			}
		}
			
		//second, clear tabset associations
		System.out.println("~~~~~~ clearing tabset associations");
		System.out.println(tst.getID()+" will be cleared");
		tst.clearItems();
		tst.save(myConn, personID);
		//lastly, drop tabset
		//ts.drop(myConn,personID);
		//ts.save(myConn);
		
        myConn.release();
		return tabSetID;
	}
	
    /**
    * Update a tabset 
    *
    * @param 
    */		
	public long update(long personID, long tabSetID, int[] orderarray){
		long[] items = null;
		DbConn myConn = new DbConn("csi"); 		
        System.out.println("~~~~~~ rearrange tabs");
		System.out.println("TabSetBean.update() called: "+tabSetID);
		
		//call TabSet.rearrange...
		ts = new TabSet(tabSetID,myConn);
        items = ts.getItemsArray();
		ts.rearrange(myConn, personID, items, orderarray);
		
		/*TabSet tsNew = null;
		long[] itemsNew = null;
		long[] idArrayNew = null; 
		tsNew = new TabSet("OPG-TabSet-Home-"+personID,myConn);
		itemsNew = tsNew.getItemsArray();
		idArrayNew = new long[itemsNew.length];
    	for (int i=0; i<items.length; i++) {
			Tab tNew = null;
			tNew = new Tab(itemsNew[i],myConn);
			idArrayNew[i] = tNew.getID();
			System.out.println(idArrayNew[i]+", "+tNew.getText()+", "+tNew.getLink());
    	}*/
    	
    	myConn.release();
		return tabSetID;
	}	
}