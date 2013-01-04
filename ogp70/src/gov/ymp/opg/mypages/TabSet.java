package gov.ymp.opg.mypages;

import java.util.*;
import java.io.*;
import java.sql.*;
import gov.ymp.csi.UNID.UList;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;

/**
* TabSet is the child class of CSI UList class extended for the specialized use
* @author   S. Higashi
*/

public class TabSet extends UList {
	
	public TabSet() {
		super();
	}	
	
    public TabSet (DbConn myConn, long userID) {
        super(myConn, userID);
        myUID.setType("tabset");
        myUID.save(myConn);
    }
	
    public TabSet (String descr, DbConn myConn) {
        super(descr, myConn);
        lookup(uID, myConn);
    }
    
    /**
     * Creates a TabSet object and uses the given id to populate it from the db
     *
     * @param id     The id of the UList to lookup from the db (long)
     * @param myConn Connection to the database
     */
     public TabSet (long id, DbConn myConn) {
         super(id, myConn);
         lookup(id, myConn);
     }
    
     /**
      * Rearrange a TabSet object 
      *
      * @param myConn Connection to the database
      * @param userID    The id of the user calling the method
      * @param orderArray  an integer array containing the new tab order
      */
    public void rearrange(DbConn myConn, long userID, long[] idArray, int[] orderArray){
    	//System.out.println("TabSet.rearrange() called... with tabsetID: "+this.getID()+", created by "+this.getCreator());   
    long items[] = this.getItemsArray(); 
    String outLine = "";
    int rows;
    Statement stmt = null;
    ResultSet rs = null;
    String sStatement;
    PreparedStatement pstmt = null;
    //check if the tabset belongs to the user who is modifying...
    if (userID == this.getCreator()){
	    //then update RANK field for the tabset
	    try {
	    	Connection conn = myConn.conn;
		    	for(int i=0;i<items.length;i++){
		    		sStatement = "UPDATE " + SCHEMAPATH + ".ulists SET rank = " + (i+1) + " WHERE ((item = "+idArray[orderArray[i]]+") AND (id = "+this.getID()+"))" ;
		    			//System.out.println(sStatement);    		
		    		pstmt = conn.prepareStatement(sStatement);
		            rows = pstmt.executeUpdate();	    		
		    	}
	    	}catch (SQLException e) {
	    		outLine = outLine + "SQLException caught: " + e.getMessage();
	    		System.out.println(outLine);
	    	}catch (Exception e) {
	    		outLine = outLine + "Exception caught: " + e.getMessage();
	    		System.out.println(outLine);
	    	} finally {
	            if (rs != null)
	                try { rs.close(); } catch (Exception i) {}
	            if (stmt != null)
	                try { stmt.close(); } catch (Exception i) {}
	            if (pstmt != null)
	                try { pstmt.close(); } catch (Exception i) {}
	        }
	    }else{
			System.out.println("this tabset was not created by the user");
		}
	}
}
