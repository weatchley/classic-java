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

/*
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
*/

import java.awt.Color;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.Font;

import gov.ymp.iats.control.*;

/**
* SAReportPDF is the class to hold all attributes for SAReportPDF object  
*
* @author   Shuhei Higashi
*/
public class SAReportPDF {
	
    protected static String SCHEMA = null;
    protected static String SCHEMAPATH = null;
    protected static String DBTYPE = null;
    
    protected String orgName = null;
    
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
		
	public SAReportPDF() throws IOException, 
	StronglyTypedPropertiesSet.IncompletePropertyFileException
    {
		init();
		//createTable(orgid, year, myConn);
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
    
    public Document createContent (int porgid, int orgid, int year, DbConn myConn, PdfPTable table, Object colarray[], Document document) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;        
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
            	//Font font = new Font(Font.HELVETICA, 6);
            	Phrase p = null;
            	
            	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            	String dateStr = "";
            	//get today's date
            	/*
            	Calendar calendar = Calendar.getInstance();
                String todaysdate = "";
                try {
                    todaysdate = formatter.format(calendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Status as of "+todaysdate);	//footer text
            	*/
            	
                while(rs.next()){

            		orgName = rs.getString(2);     
            		setOrgName(orgName);
					//Phrase header = new Phrase(orgName,font);
            		Phrase headertitle = new Phrase(orgName, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(0, 0, 0)));
            		document.add(headertitle);
            		
            		SelfAssessment sa = new SelfAssessment();
    	        	SelfAssessment[] item = sa.getSAList(myConn, "id", porgid, orgid, year);
    	        	
    	        	System.out.println(orgName+" for "+year); 		//header text
                	                	
            		//int collength = 16;
                	table.setWidthPercentage(100f);
                    table.getDefaultCell().setUseAscender(true);
                    table.getDefaultCell().setUseDescender(true);
                    table.getDefaultCell().setBackgroundColor(Color.LIGHT_GRAY);
                    
                    for (int i=0;i<colarray.length;i++){
                    	if (colarray[i].equals("sanumber")){
                    		//p = new Phrase("SA Number", font);
                    		p = new Phrase("SA Number",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
                    		table.addCell(p);
                    	}
                    	if (colarray[i].equals("teamlead")){
	                    p = new Phrase("Team Lead",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
                    	}
                    	if (colarray[i].equals("satitle")){
	                    p = new Phrase("Title",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
            			}
                    	if (colarray[i].equals("purpose")){
	                    p = new Phrase("Purpose",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
        				}
                    	if (colarray[i].equals("scheduledate")){
	                    p = new Phrase("Scheduled Date",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
    					}
                    	if (colarray[i].equals("rescheduledate")){
	                    p = new Phrase("Re-Sched. Date",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
						}
                    	if (colarray[i].equals("signdate")){
	                    p = new Phrase("Sign Date",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("canceldate")){
	                    p = new Phrase("Cancel Date",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("cancelledrationale")){
	                    p = new Phrase("Cancel Rationale",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("comment")){
	                    p = new Phrase("Comments",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("condreport")){
	                    p = new Phrase("Cond. Report",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("asstype")){
	                    p = new Phrase("Assess. Type",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("assobj")){
	                    p = new Phrase("Assess. Obj.",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("crlevels")){
	                    p = new Phrase("CR Level",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("crnums")){
	                    p = new Phrase("CR Number",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("llnums")){
	                    p = new Phrase("Lessons Learned Number",FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new Color(0, 0, 0)));
	                    table.addCell(p);
	                    }
                    }
                    
                    table.getDefaultCell().setBackgroundColor(null);
            		
    	        	for (int i=0; i< item.length; i++){
    	        	  
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("sanumber")){
                        		
		    	        	  sanumber = item[i].getSANumber(item[i].getOrgid(),item[i].getDivid(),myConn);
		    	        	  if (sanumber == null) { 
		    	        		  sanumber = "";
		    	        	  }else{
		    	        		  sanumber = item[i].getSANumber(item[i].getOrgid(),item[i].getDivid(),myConn);
		    	        	  }
		    	       System.out.println("sanumber: "+sanumber);        	            	        	  
		    	        	  //p = new Phrase(sanumber, font);
		    	       		  p = new Phrase(sanumber,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
		    	              table.addCell(p);
                        	
                        	}
    	        		}
    	              
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("teamlead")){
		    	              teamleadid = Integer.valueOf(item[i].getTeamleadid());
		    	              String teamleadname = null;
		    	          	  teamleadname = item[i].getTeamleadname(teamleadid, myConn);
		    	          	  if (teamleadname == null) { 
			    	        	  teamleadname = "";
			  	        	  }else{
			  	        		teamleadname = item[i].getTeamleadname(teamleadid, myConn);
			  	        	  }
		    	        System.out.println("teamleadname: "+teamleadname);    	          	  
		    	          	  p = new Phrase(teamleadname,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
		    	          	  table.addCell(p);    	    	          	  
                        	}
    	        		}
    	          	  
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("satitle")){	
		    	          	  title = item[i].getTitle();	 
		    	          	  if (title == null) { 
		    	          		title = "";
			  	        	  }else{
			  	        		title= item[i].getTitle();
			  	        	  }
		    	        System.out.println("title: "+title);
		    	          	  p = new Phrase(title,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
		    	          	  table.addCell(p);    	    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("purpose")){
		    	          	  purpose = item[i].getPurpose();
		    	          	  if (purpose == null) { 
		    	          		purpose = "";
			  	        	  }else{
			  	        		purpose = item[i].getPurpose();
			  	        	  }
		    	        System.out.println("purpose: "+purpose);
		    	          	  p = new Phrase(purpose,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
		    	          	  table.addCell(p);    	    
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("scheduledate")){
		    	          	  scheduleddate = item[i].getScheduleddate();
		    	        System.out.println("scheduleddate: "+scheduleddate);    	        
			    	    if (scheduleddate == null) {
			    	          dateStr = "";
			    	          }else{
			    	          dateStr = formatter.format(scheduleddate);
			    	          }
		    	        	  p = new Phrase(dateStr,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("rescheduledate")){                        		
			    	          	  rescheduleddate = item[i].getRescheduleddate();
			    	        System.out.println("rescheduleddate: "+rescheduleddate);
			    	        	  if (rescheduleddate == null) {
				    	          dateStr = "";
				    	          }else{
				    	          dateStr = formatter.format(rescheduleddate);
				    	          }
				        	  	  p = new Phrase(dateStr,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));   	  	          	  
			    	        	  table.addCell(p);   
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("signdate")){
		    	          	  signeddate = item[i].getSigneddate();
		    	        System.out.println("signeddate: "+signeddate);
			    	        	if (signeddate == null) {
				    	          dateStr = "";
				    	          }else{
				    	          dateStr = formatter.format(signeddate);
				    	          }
				        	  	  p = new Phrase(dateStr,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));   	  	          	  
			    	        	  table.addCell(p);    	    	
	                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("canceldate")){  
		    	          	  cancelleddate = item[i].getCancelleddate();
		    	        System.out.println("cancelleddate: "+cancelleddate);
		    	        	if (cancelleddate == null) {
			    	          dateStr = "";
			    	          }else{
			    	          dateStr = formatter.format(cancelleddate);
			    	          }
			        	  	  p = new Phrase(dateStr,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));   	  	          	  
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("cancelledrationale")){
		    	          	  cancelledrationale = item[i].getCancelledrationale();
		    	        System.out.println("cancelledrationale: "+cancelledrationale);  
		    	          	  p = new Phrase(cancelledrationale,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("comment")){
		    	          	  comments = item[i].getComments();
		    	          	  if (comments == null) { 
		    	          		comments = "";
			  	        	  }else{
			  	        		comments = item[i].getComments();
			  	        	  }
		    	        System.out.println("comments: "+comments); 
		    	          	  p = new Phrase(comments,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
		    	          	  table.addCell(p);  
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("condreport")){
		    	          	  hascirs = item[i].getHascirs();
		    	          	  if (hascirs == null) { 
		    	          		hascirs = "";
			  	        	  }else{
			  	        		hascirs = item[i].getHascirs();
			  	        	  }
		    	        System.out.println("hascirs: "+hascirs); 
		    	          	  p = new Phrase(hascirs,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("asstype")){
		    	          	  asstype = item[i].getAsstype();
		    	          	  if (asstype == null) { 
		    	          		asstype = "";
			  	        	  }else{
			  	        		asstype = item[i].getAsstype();
			  	        	  }
		    	        System.out.println("asstype: "+asstype); 
		    	          	  p = new Phrase(asstype,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
		    	          	  table.addCell(p);   
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("assobj")){
		    	          	  assobj = item[i].getAssobj();
		    	          	  if (assobj == null) { 
		    	          	  assobj = "";
			  	        	  }else{
			  	        	  assobj = item[i].getAssobj();
			  	        	  }
		    	        System.out.println("assobj: "+assobj); 
		    	          	  p = new Phrase(assobj,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("crlevels")){
		    	          	  crlevels = item[i].getCrlevels();
		    	          	  if (crlevels == null) { 
		    	          	  crlevels = "";
		  	  	        	  }else{
		  	  	        	  crlevels = item[i].getCrlevels();
		  	  	        	  }
		    	        System.out.println("crlevels: "+crlevels); 
		    	          	  p = new Phrase(crlevels,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("crnums")){
		    	          	  crnums = item[i].getCrnums();
		    	          	  if (crnums == null) { 
		    	          	  crnums = "";
		    	          	  }else{
		    	          	  crnums = item[i].getCrnums();
		    	          	  }
		    	        System.out.println("crnums: "+crnums); 
		    	          	  p = new Phrase(crnums,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("llnums")){
	    	          	  llnums = item[i].getLlnums();
	    	          	  if (llnums == null) { 
	    	          	  llnums = "";
	      	          	  }else{
	      	          	  llnums = item[i].getLlnums();
	      	          	  }
	    	        System.out.println("llnums: "+llnums); 
	    	          	  p = new Phrase(llnums,FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(0, 0, 0)));
	    	          	  table.addCell(p);  
                        	}
    	        		}
    	        		
    	        	}
    	        	document.add(table); 
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
            return document;
        }        
    }    
    
    public PdfPTable createTable (int porgid, int orgid, int year, DbConn myConn, PdfPTable table, Object colarray[]) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;        
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
            	//Font font = new Font(Font.HELVETICA, 6);
            	Phrase p = null;
            	
            	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            	String dateStr = "";
            	//get today's date
            	/*
            	Calendar calendar = Calendar.getInstance();
                String todaysdate = "";
                try {
                    todaysdate = formatter.format(calendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Status as of "+todaysdate);	//footer text
            	*/
            	
                while(rs.next()){
            		String orgName = rs.getString(2);     
            		
            		SelfAssessment sa = new SelfAssessment();
    	        	SelfAssessment[] item = sa.getSAList(myConn, "id", porgid, orgid, year);
    	        	
    	        	System.out.println(orgName+" for "+year); 		//header text
                	                	
            		//int collength = 16;
                	table.setWidthPercentage(100f);
                    table.getDefaultCell().setUseAscender(true);
                    table.getDefaultCell().setUseDescender(true);
                    table.getDefaultCell().setBackgroundColor(Color.LIGHT_GRAY);
                    
                    for (int i=0;i<colarray.length;i++){
                    	if (colarray[i].equals("sanumber")){
                    		//p = new Phrase("SA Number", font);
                    		p = new Phrase("SA Number");
                    		table.addCell(p);
                    	}
                    	if (colarray[i].equals("teamlead")){
	                    p = new Phrase("Team Lead");
	                    table.addCell(p);
                    	}
                    	if (colarray[i].equals("satitle")){
	                    p = new Phrase("Title");
	                    table.addCell(p);
            			}
                    	if (colarray[i].equals("purpose")){
	                    p = new Phrase("Purpose");
	                    table.addCell(p);
        				}
                    	if (colarray[i].equals("scheduledate")){
	                    p = new Phrase("Scheduled Date");
	                    table.addCell(p);
    					}
                    	if (colarray[i].equals("rescheduledate")){
	                    p = new Phrase("Re-Sched. Date");
	                    table.addCell(p);
						}
                    	if (colarray[i].equals("signdate")){
	                    p = new Phrase("Sign Date");
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("canceldate")){
	                    p = new Phrase("Cancel Date");
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("cancelledrationale")){
	                    p = new Phrase("Cancel Rationale");
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("comment")){
	                    p = new Phrase("Comments");
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("condreport")){
	                    p = new Phrase("Cond. Report");
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("asstype")){
	                    p = new Phrase("Assess. Type");
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("assobj")){
	                    p = new Phrase("Assess. Obj.");
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("crlevels")){
	                    p = new Phrase("CR Level");
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("crnums")){
	                    p = new Phrase("CR Number");
	                    table.addCell(p);
	                    }
                    	if (colarray[i].equals("llnums")){
	                    p = new Phrase("Lessons Learned Number");
	                    table.addCell(p);
	                    }
                    }
                    
                    table.getDefaultCell().setBackgroundColor(null);
            		
    	        	for (int i=0; i< item.length; i++){
    	        	  
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("sanumber")){
                        		
		    	        	  sanumber = item[i].getSANumber(item[i].getOrgid(),item[i].getDivid(),myConn);
		    	        	  if (sanumber == null) { 
		    	        		  sanumber = "";
		    	        	  }else{
		    	        		  sanumber = item[i].getSANumber(item[i].getOrgid(),item[i].getDivid(),myConn);
		    	        	  }
		    	       System.out.println("sanumber: "+sanumber);        	            	        	  
		    	        	  //p = new Phrase(sanumber, font);
		    	       		  p = new Phrase(sanumber);
		    	              table.addCell(p);
                        	
                        	}
    	        		}
    	              
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("teamlead")){
		    	              teamleadid = Integer.valueOf(item[i].getTeamleadid());
		    	              String teamleadname = null;
		    	          	  teamleadname = item[i].getTeamleadname(teamleadid, myConn);
		    	          	  if (teamleadname == null) { 
			    	        	  teamleadname = "";
			  	        	  }else{
			  	        		teamleadname = item[i].getTeamleadname(teamleadid, myConn);
			  	        	  }
		    	        System.out.println("teamleadname: "+teamleadname);    	          	  
		    	          	  p = new Phrase(teamleadname);
		    	          	  table.addCell(p);    	    	          	  
                        	}
    	        		}
    	          	  
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("satitle")){	
		    	          	  title = item[i].getTitle();	 
		    	          	  if (title == null) { 
		    	          		title = "";
			  	        	  }else{
			  	        		title= item[i].getTitle();
			  	        	  }
		    	        System.out.println("title: "+title);
		    	          	  p = new Phrase(title);
		    	          	  table.addCell(p);    	    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("purpose")){
		    	          	  purpose = item[i].getPurpose();
		    	          	  if (purpose == null) { 
		    	          		purpose = "";
			  	        	  }else{
			  	        		purpose = item[i].getPurpose();
			  	        	  }
		    	        System.out.println("purpose: "+purpose);
		    	          	  p = new Phrase(purpose);
		    	          	  table.addCell(p);    	    
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("scheduledate")){
		    	          	  scheduleddate = item[i].getScheduleddate();
		    	        System.out.println("scheduleddate: "+scheduleddate);    	        
			    	    if (scheduleddate == null) {
			    	          dateStr = "";
			    	          }else{
			    	          dateStr = formatter.format(scheduleddate);
			    	          }
		    	        	  p = new Phrase(dateStr);
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("rescheduledate")){                        		
			    	          	  rescheduleddate = item[i].getRescheduleddate();
			    	        System.out.println("rescheduleddate: "+rescheduleddate);
			    	        	  if (rescheduleddate == null) {
				    	          dateStr = "";
				    	          }else{
				    	          dateStr = formatter.format(rescheduleddate);
				    	          }
				        	  	  p = new Phrase(dateStr);   	  	          	  
			    	        	  table.addCell(p);   
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("signdate")){
		    	          	  signeddate = item[i].getSigneddate();
		    	        System.out.println("signeddate: "+signeddate);
			    	        	if (signeddate == null) {
				    	          dateStr = "";
				    	          }else{
				    	          dateStr = formatter.format(signeddate);
				    	          }
				        	  	  p = new Phrase(dateStr);   	  	          	  
			    	        	  table.addCell(p);    	    	
	                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("canceldate")){  
		    	          	  cancelleddate = item[i].getCancelleddate();
		    	        System.out.println("cancelleddate: "+cancelleddate);
		    	        	if (cancelleddate == null) {
			    	          dateStr = "";
			    	          }else{
			    	          dateStr = formatter.format(cancelleddate);
			    	          }
			        	  	  p = new Phrase(dateStr);   	  	          	  
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("cancelledrationale")){
		    	          	  cancelledrationale = item[i].getCancelledrationale();
		    	        System.out.println("cancelledrationale: "+cancelledrationale);  
		    	          	  p = new Phrase(cancelledrationale);
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("comment")){
		    	          	  comments = item[i].getComments();
		    	          	  if (comments == null) { 
		    	          		comments = "";
			  	        	  }else{
			  	        		comments = item[i].getComments();
			  	        	  }
		    	        System.out.println("comments: "+comments); 
		    	          	  p = new Phrase(comments);
		    	          	  table.addCell(p);  
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("condreport")){
		    	          	  hascirs = item[i].getHascirs();
		    	          	  if (hascirs == null) { 
		    	          		hascirs = "";
			  	        	  }else{
			  	        		hascirs = item[i].getHascirs();
			  	        	  }
		    	        System.out.println("hascirs: "+hascirs); 
		    	          	  p = new Phrase(hascirs);
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("asstype")){
		    	          	  asstype = item[i].getAsstype();
		    	          	  if (asstype == null) { 
		    	          		asstype = "";
			  	        	  }else{
			  	        		asstype = item[i].getAsstype();
			  	        	  }
		    	        System.out.println("asstype: "+asstype); 
		    	          	  p = new Phrase(asstype);
		    	          	  table.addCell(p);   
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("assobj")){
		    	          	  assobj = item[i].getAssobj();
		    	          	  if (assobj == null) { 
		    	          	  assobj = "";
			  	        	  }else{
			  	        	  assobj = item[i].getAssobj();
			  	        	  }
		    	        System.out.println("assobj: "+assobj); 
		    	          	  p = new Phrase(assobj);
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("crlevels")){
		    	          	  crlevels = item[i].getCrlevels();
		    	          	  if (crlevels == null) { 
		    	          	  crlevels = "";
		  	  	        	  }else{
		  	  	        	  crlevels = item[i].getCrlevels();
		  	  	        	  }
		    	        System.out.println("crlevels: "+crlevels); 
		    	          	  p = new Phrase(crlevels);
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("crnums")){
		    	          	  crnums = item[i].getCrnums();
		    	          	  if (crnums == null) { 
		    	          	  crnums = "";
		    	          	  }else{
		    	          	  crnums = item[i].getCrnums();
		    	          	  }
		    	        System.out.println("crnums: "+crnums); 
		    	          	  p = new Phrase(crnums);
		    	          	  table.addCell(p);    	
                        	}
    	        		}
    	        		
    	        		for (int j=0;j<colarray.length;j++){
                        	if (colarray[j].equals("llnums")){
	    	          	  llnums = item[i].getLlnums();
	    	          	  if (llnums == null) { 
	    	          	  llnums = "";
	      	          	  }else{
	      	          	  llnums = item[i].getLlnums();
	      	          	  }
	    	        System.out.println("llnums: "+llnums); 
	    	          	  p = new Phrase(llnums);
	    	          	  table.addCell(p);  
                        	}
    	        		}
    	        		
    	        	}
    	        }
                
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
            return table;  
        }        
    }
    
	/**
     * Configuration properties.
     */
    //private SAReportCLProperties _props = null;

}