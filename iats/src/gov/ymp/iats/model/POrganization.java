package gov.ymp.iats.model;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import gov.ymp.iats.model.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
* POrganization is the class to hold all attributes for POrganization object  
*
* @author   Shuhei Higashi
*/
public class POrganization {
    public static String SCHEMA = null;
    public static String SCHEMAPATH = null;
    public static String DBTYPE = null;		
    /**
    * init new object
    */
    
    private void init () {
    	//System.out.println("POrganization initialized");
        DbConn tempDB = new DbConn("dummy");
        SCHEMA = "sat";
        SCHEMAPATH = "sat";
        DBTYPE = tempDB.getDBType();
    }
    
    public POrganization () {
        init();
    }
    protected java.util.List<String[]> plist = new ArrayList<String[]>();   
    public java.util.List<String[]> getlist(DbConn myConn){
    //public ResultSet getlist(DbConn myConn){
    	String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;     
    		
        try{
        Connection conn = myConn.conn;
    	stmt = conn.createStatement ();
    	String sqlcode = null;
    	String wherestring = null;
        String orderby = " order by id ";
       	wherestring = " where isactive = 'T'";
	    sqlcode = "SELECT id, name, acronym, isactive FROM " + SCHEMAPATH + ".P_ORGANIZATION " + wherestring + orderby;
        //System.out.println(sqlcode);
	    rs = stmt.executeQuery(sqlcode);
	    	while(rs.next()){
	    		String[] larray = {rs.getString(1),rs.getString(2),rs.getString(3)};
	    		plist.add(larray);
	    	}
        }catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            System.out.println(outLine);
            //log(outLine);
            //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SelfAssessment lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            //log(outLine);
            //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
        }
        finally {
            if (rs != null){
                try { rs.close(); } catch (Exception i) {}}
            if (stmt != null){
                try { stmt.close(); } catch (Exception i) {}}
        	return plist;
            //return rs;
        }    
    }
    
    
}