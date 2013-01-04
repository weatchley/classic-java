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
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;


/**
* SAReportCL is the class to hold all attributes for SAReportCL object  
*
* @author   Shuhei Higashi
*/
public class SAReport {
	
    public static String SCHEMA = null;
    public static String SCHEMAPATH = null;
    public static String DBTYPE = null;
        
    String sanumber = null;
	  String title = null;
	  String scope = null;
	  String purpose = null;
	  int orgid = 0;
	  int teamleadid = 0;
	  int year = 0;
	  int sequenceid = 0;
	  java.util.Date scheduleddate = null;    
	  java.util.Date rescheduleddate = null;
	  java.util.Date signeddate = null;
	  String comments = null;
	  java.util.Date cancelleddate = null;
	  String cancelledrationale = null;
	  int supportteamleadid = 0;
	  String hascirs = null;
	  String type = null;
	  int divid = 0;
	  String asstype = null;
	  String assobj = null;
	  String ll = null;
	  String crlevels = null;
	  String crnums = null;
	  String llnums = null;
	
		
	public SAReport(int orgid, int year, DbConn myConn) throws IOException, 
	StronglyTypedPropertiesSet.IncompletePropertyFileException
    {
		init();
		lookup(orgid, year, myConn);
    }
	
    private void init () {
        DbConn tempDB = new DbConn("dummy");
        SCHEMA = "sat";
        SCHEMAPATH = "sat";
        DBTYPE = tempDB.getDBType();
    }
		
    public void lookup (int orgid, int year, DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;        
        //get *active* organizations
        //get organization name and acronym
        try {
            Connection conn = myConn.conn;
        	stmt = conn.createStatement ();
        	String sqlcode = null;
        	String wherestring = null;
            String orderby = " order by name ";
            if(orgid > 0){
            	wherestring = " where isactive = 'T' AND id=" + orgid;
    	        sqlcode = "SELECT id, name, acronym, isactive, porgid FROM " + SCHEMAPATH + ".ORGANIZATION " + wherestring;
            }else{
            	wherestring = " where isactive = 'T'";
            	sqlcode = "SELECT id, name, acronym, isactive, porgid FROM " + SCHEMAPATH + ".ORGANIZATION " + wherestring + orderby;
            }
    	        //System.out.println(sqlcode);
            	rs = stmt.executeQuery(sqlcode);
    	        while(rs.next()){
    	        	System.out.println("/************** " + rs.getString("name")+", "+rs.getInt("id") + " **************/");
    	        	SelfAssessment sa = new SelfAssessment();
    	        	SelfAssessment[] item = sa.getSAList(myConn, "id", 0, orgid, year);
    	        	for (int i=0; i< item.length; i++){
    	        	  System.out.println("/**************" + "**************/");
    	        	  sanumber = item[i].getSANumber(item[i].getOrgid(),item[i].getDivid(),myConn);
    	          	  System.out.println("sanumber: "+sanumber);    	          	  
    	          	  teamleadid = Integer.valueOf(item[i].getTeamleadid());
    	          	  String teamleadname = null;
    	          	  teamleadname = item[i].getTeamleadname(teamleadid, myConn);
    	          	  System.out.println("teamleadname: "+teamleadname);	  
    	          	  title = item[i].getTitle();	  
    	          	  System.out.println("title: "+title);
    	          	  purpose = item[i].getPurpose();
    	          	  System.out.println("purpose: "+purpose);
    	          	  scheduleddate = item[i].getScheduleddate();
    	          	  System.out.println("scheduleddate: "+scheduleddate);
    	          	  rescheduleddate = item[i].getRescheduleddate();
    	          	  System.out.println("rescheduleddate: "+rescheduleddate);
    	          	  signeddate = item[i].getSigneddate();
    	          	  System.out.println("signeddate: "+signeddate);
    	          	  cancelleddate = item[i].getCancelleddate();
    	          	  System.out.println("cancelleddate: "+cancelleddate);
    	          	  cancelledrationale = item[i].getCancelledrationale();
    	          	  System.out.println("cancelledrationale: "+cancelledrationale);  
    	          	  comments = item[i].getComments();
    	          	  System.out.println("comments: "+comments); 
    	          	  hascirs = item[i].getHascirs();
    	          	  System.out.println("hascirs: "+hascirs); 
    	          	  asstype = item[i].getAsstype();
    	          	  System.out.println("asstype: "+asstype); 
    	          	  assobj = item[i].getAssobj();
    	          	  System.out.println("assobj: "+assobj); 
    	          	  crlevels = item[i].getCrlevels();
    	          	  System.out.println("crlevels: "+crlevels); 
    	          	  crnums = item[i].getCrnums();
    	          	  System.out.println("crnums: "+crnums); 
    	          	  llnums = item[i].getLlnums();
    	          	  System.out.println("llnums: "+llnums); 
    	                
    	            }
    	        }
    	        
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SelfAssessment lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
        
    }
    
    
	/**
     * Configuration properties.
     */
    //private SAReportCLProperties _props = null;
	
	
	

}