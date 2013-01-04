package gov.ymp.opg.mypages;

import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.items.*;
import gov.ymp.csi.UNID.*;

public class TabSetsTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("TabSetsTest called!");
				
			DbConn myConn = new DbConn("csi");
			long personID;
			personID = (long)0; //the user has not logged in...
			TabSet ts = null;		
			long[] items = null;
		
		//retrieve home tabs		
		System.out.println("~~~ retrieve home tabs");		
		ts = new TabSet("OPG-TabSet-Home-Default",myConn);
		items = ts.getItemsArray();
		for (int i=0; i<items.length; i++) {
			Tab t =	null;
			t = new Tab(items[i],myConn);
			System.out.println(t.getText()+", "+t.getLink());
		}	
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		
		/*
		//retrieve favorites tabs		
		System.out.println("~~~ retrieve favorites tabs");		
		ts = new TabSet("OPG-TabSet-Favorites-Default",myConn);
		items = ts.getItemsArray();
		for (int i=0; i<items.length; i++) {
			Tab t =	null;
			t = new Tab(items[i],myConn);
			System.out.println(t.getText()+", "+t.getLink());
		}				
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		*/
		
		
		/*
		//create a new tabset for
		System.out.println("~~~ create a new custom tabset tabs");
		personID = (long)1214; //shuhei's id
		long [] itemsTmp = null;
		ts = new TabSet("OPG-TabSet-Home-"+personID,myConn);
		if (ts == null || ts.getID() == 0) {
			TabSet tsDef = new TabSet("OPG-TabSet-Home-Default", myConn);
            ts = new TabSet(myConn, personID);
            ts.setDescription("OPG-TabSet-Home-" + personID);
            ts.setCreator(personID);
            itemsTmp = tsDef.getItemsArray();
            for (int i=0; i<itemsTmp.length; i++) {
                ts.addItem(itemsTmp[i], myConn);
            }
            ts.save(myConn, personID);
        }else{
        	items = ts.getItemsArray();
        	if(items.length==0){
        		TabSet tsDef = new TabSet("OPG-TabSet-Home-Default", myConn);
        		itemsTmp = tsDef.getItemsArray();
        		for (int i=0; i<itemsTmp.length; i++) {
                    ts.addItem(itemsTmp[i], myConn);
                }
        		ts.save(myConn, personID);
        	}
        	System.out.println("contents of OPG-TabSet-Home-" + personID + ": "+ts.getID());
        	items = ts.getItemsArray();
    		for (int i=0; i<items.length; i++) {
    			Tab t = null;
    			t = new Tab(items[i],myConn);
    			System.out.println(t.getText()+", "+t.getLink());
    		}
        }
			
		
		//drop a tabset
		//System.out.println("~~~ drop a tabset");
			//System.out.println(ts.getID()+" wiil be dropped");
			//ts.drop(myConn,personID);
			//ts.save(myConn);
		
		
		//update a tabset
		System.out.println("~~~ update a tabset");
			//first, delete tabset contents
			System.out.println("~~~~~~ deleting tabset content");
			items = ts.getItemsArray();
			for (int i=0; i<items.length; i++) {
				Tab t = null;
				t = new Tab(items[i],myConn);			
				if(t.getOwner()==personID){
					System.out.println(t.getID()+" will be deleted");
					t.drop(myConn,personID);
				}
			}
				
			//second, clear tabset associations
			System.out.println("~~~~~~ clearing tabset associations");
			System.out.println(ts.getID()+" will be cleared");
			ts.clearItems();
			ts.save(myConn, personID);
			
			//finally, create tab objects -> register them to a tabset
			System.out.println("~~~~~~ create tab objects -> register them to a tabset");
				//create definitions
				Object[][] tConf = {	//this part will be rewritten using json...
									{"Director","/director/index.html"},
									{"Favorite New","../portlets/favorites-redux/favorites.jsp"}
								  };
				
				//iterate thru the object
				for(int i=0;i<tConf.length;i++){
						Tab t = new Tab();
						t.setOwner(personID);
						t.setText(tConf[i][0].toString());
						t.setLink(tConf[i][1].toString());
						t.save(myConn,personID);
						ts.addItem(t.getID(), myConn);
					}
				ts.save(myConn, personID);
		*/
				
		//rearrange tabs 
		
		//retrieve (or create) home tabset for higashis (1214)
		
		
			System.out.println("~~~ create a new custom tabset tabs");
			personID = (long)3676; //shuhei's id
			long[] idArray = null; //empty array to hold item ids
			long [] itemsTmp = null;
			ts = new TabSet("OPG-TabSet-Home-"+personID,myConn);
			if (ts == null || ts.getID() == 0) {
				TabSet tsDef = new TabSet("OPG-TabSet-Home-Default", myConn);
	            ts = new TabSet(myConn, personID);
	            ts.setDescription("OPG-TabSet-Home-" + personID);
	            ts.setCreator(personID);
	            itemsTmp = tsDef.getItemsArray();
	            for (int i=0; i<itemsTmp.length; i++) {
	                ts.addItem(itemsTmp[i], myConn);
	            }
	            ts.save(myConn, personID);
	        }else{
	        	items = ts.getItemsArray();
	        	
	        	/*
	        	if(items.length==0){
	        		TabSet tsDef = new TabSet("OPG-TabSet-Home-Default", myConn);
	        		itemsTmp = tsDef.getItemsArray();
	        		for (int i=0; i<itemsTmp.length; i++) {
	                    ts.addItem(itemsTmp[i], myConn);
	                }
	        		ts.save(myConn, personID);
	        	}
	        	*/
	        	
	        	System.out.println("contents of OPG-TabSet-Home-" + personID + ": "+ts.getID());
	        	items = ts.getItemsArray();
	        	idArray = new long[items.length];
	        	for (int i=0; i<items.length; i++) {
	    			Tab t = null;
	    			t = new Tab(items[i],myConn);
	    			idArray[i] = t.getID();
	    			System.out.println("tab text:  " + items[i]);
	    			System.out.println(idArray[i]+", "+t.getText()+", "+t.getLink());
	    		}
	        }			
			
		System.out.println("~~~~~~ rearrange tabs");
		
		int[] oa = {2,1,4,3,6,5};
		//int[] oa = {1,2,3,4,5,6};
		ts.rearrange(myConn, personID,idArray,oa);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");	
		
		TabSet tsNew = null;
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
		}
		
		//release DB connection object
		myConn.release();
	}
	
}