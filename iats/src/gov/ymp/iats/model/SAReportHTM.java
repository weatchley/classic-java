package gov.ymp.iats.model;

// Support classes
import java.io.*;
import java.util.*;
import java.text.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import gov.ymp.iats.model.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

import gov.ymp.iats.control.*;

/**
* SAReportHTM is the class to hold all attributes for SAReportHTM object  
*
* @author   Shuhei Higashi
*/
public class SAReportHTM {
	
    protected static String SCHEMA = null;
    protected static String SCHEMAPATH = null;
    protected static String DBTYPE = null;
    
    protected String orgName = null;
    
    String sanumber = null;
	  String title = null;
	  String scope = null;
	  String purpose = null;
	  int id = 0;
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
		
	public SAReportHTM() throws IOException, 
	StronglyTypedPropertiesSet.IncompletePropertyFileException
    {
		init();
		//System.out.println("SAReportHTM created");
    }
	
    private void init () {
        DbConn tempDB = new DbConn("dummy");
        SCHEMA = "sat";
        SCHEMAPATH = "sat";
        DBTYPE = tempDB.getDBType();
    }

    public String getOrgName(){
    	return(orgName);
    }
    
    protected void setOrgName(String name) {
        orgName = name;
    }

    //public Document createContent (int porgid, int orgid, int year, DbConn myConn, PdfPTable table, Object colarray[], Document document) {
    public String createContent (int porgid, int orgid, int year, DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;  
        String output = "no record found.";
        //get organization name and acronym
        try {
        	Connection conn = myConn.conn;
        	stmt = conn.createStatement ();
        	String sqlcode = null;
        	String wherestring = null;
            String orderby = " order by name ";
            if(orgid > 0){
            	wherestring = " where isactive = 'T' AND id=" + orgid;
    	        sqlcode = "SELECT id, name, acronym, isactive, porgid FROM " + SCHEMAPATH + ".ORGANIZATION " + wherestring + orderby;
            }else if (porgid > 0){
            	wherestring = " where isactive = 'T' AND porgid=" + porgid;
    	        sqlcode = "SELECT id, name, acronym, isactive, porgid FROM " + SCHEMAPATH + ".ORGANIZATION " + wherestring + orderby;
            }else{
            	wherestring = " where isactive = 'T'";
            	sqlcode = "SELECT id, name, acronym, isactive, porgid FROM " + SCHEMAPATH + ".ORGANIZATION " + wherestring + orderby;
            }
System.out.println(sqlcode);
            	rs = stmt.executeQuery(sqlcode);
            	
            	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            	String dateStr = "";
            	int iCounter = 0;
                while(rs.next()){

            		orgName = rs.getString(2);     
            		setOrgName(orgName);
            		//System.out.println(orgName+" for "+year); 		//header text
                	output = "<table width=100% style=\"border-collapse: collapse;\" cellspan=0 cellpadding=5>";
    	        	output += "<tr><td colspan=8 style=\"background-color:#2A401C;color:#ffffff;font-weight:bold;\">"+orgName+"</td></tr>";
    	        	output += "<tr style=\"background-color:#ffffff;color:#000000;font-weight:bold;\">";
    	        	output += "<th style=\"border: 1px solid #000000;\">Number&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>";
    	        	output += "<th style=\"border: 1px solid #000000;\">Scheduled Date</th>";
    	        	output += "<th style=\"border: 1px solid #000000;\">Re-Sched. Date</th>";
    	        	output += "<th style=\"border: 1px solid #000000;\">Signed Date</th>";
    	        	output += "<th style=\"border: 1px solid #000000;\">Cancelled Date</th>";
    	        	output += "<th style=\"border: 1px solid #000000;\">Title</th>";
    	        	output += "<th style=\"border: 1px solid #000000;\">Purpose</th>";
    	        	output += "<th style=\"border: 1px solid #000000;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>";
    	        	output += "</tr>";
    	        
    	        	SelfAssessment sa = new SelfAssessment();
    	        	SelfAssessment[] item = sa.getSAList(myConn, "id", porgid, orgid, year);
    	        	    	        	
            		for (int i=0; i< item.length; i++){
            		
            		output += "<tr>";
            		id = item[i].getID();
            		System.out.println("ID: "+id);   
            		sanumber = item[i].getSANumber(item[i].getOrgid(),item[i].getDivid(),myConn);
	  	        	if (sanumber == null) { 
	  	        		  sanumber = "";
	  	        	}else{
	  	        		  sanumber = item[i].getSANumber(item[i].getOrgid(),item[i].getDivid(),myConn);
	  	        	}
            		output += "<td style=\"border: 1px solid #000000;\"><a href=\"#\" onclick=\"getItem('assessment','update',"+id+");return false;\">" + sanumber +"</td>";
            		
        		  	scheduleddate = item[i].getScheduleddate();
	    	        System.out.println("scheduleddate: "+scheduleddate);    	        
		    	    if (scheduleddate == null) {
	    	        dateStr = "";
	    	        }else{
	    	        dateStr = formatter.format(scheduleddate);
	    	        }
            		output += "<td style=\"border: 1px solid #000000;\">" + dateStr +"</td>";
            			
        			rescheduleddate = item[i].getRescheduleddate();
	    	        System.out.println("rescheduleddate: "+rescheduleddate);
    	        	if (rescheduleddate == null) {
	    	        dateStr = "";
	    	        }else{
	    	        dateStr = formatter.format(rescheduleddate);
	    	        }
            		output += "<td style=\"border: 1px solid #000000;\">" + dateStr +"</td>";
            		
        			signeddate = item[i].getSigneddate();
	    	        System.out.println("signeddate: "+signeddate);
		    	    if (signeddate == null) {
			    	dateStr = "";
			    	}else{
			    	dateStr = formatter.format(signeddate);
			    	}
            		output += "<td style=\"border: 1px solid #000000;\">" + dateStr +"</td>";
            		
        			cancelleddate = item[i].getCancelleddate();
	    	        System.out.println("cancelleddate: "+cancelleddate);
	    	        if (cancelleddate == null) {
		    	    dateStr = "";
		    	    }else{
		    	    dateStr = formatter.format(cancelleddate);
		    	    }
            		output += "<td style=\"border: 1px solid #000000;\">" + dateStr +"</td>";
            		
            		title = item[i].getTitle();	 
    	          	if (title == null) { 
    	          		title = "";
	  	        	}else{
	  	        		title= item[i].getTitle();
	  	        	}
            		output += "<td style=\"border: 1px solid #000000;\">" + title +"</td>";
            		
        			purpose = item[i].getPurpose();
    	          	if (purpose == null) { 
    	          		purpose = "";
	  	        	}else{
	  	        		purpose = item[i].getPurpose();
	  	        	}
            		output += "<td style=\"border: 1px solid #000000;\">" + purpose +"</td>";
            		
            		output += "<td style=\"border: 1px solid #000000;\"><input type=button value=Delete id=delbutton"+iCounter+" onclick='javascript:deleteItem(\""+id+"\");'></td>";
            		
            		output += "<tr>";
    	        	iCounter++;
            		
    	        	}
    	        	//document.add(table);
    	        	output += "</table>";
    	        	output += "<input name=itemlength id=itemlength type=hidden value="+iCounter+">";
    	        }
            //document.add(table);    
            
        }
        catch (SQLException e) {
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
            //return table;  
            //return document;
            return output;
        }        
    }

}