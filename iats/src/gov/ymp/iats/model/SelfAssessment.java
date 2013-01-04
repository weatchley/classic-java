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
* SelfAssessment is the class to hold all attributes for SelfAssessment object  
*
* @author   Shuhei Higashi
*/
public class SelfAssessment {	    
	int ID = 0;
	private String sanumber = "";
    private String title = "";
    private String scope = "";
    private int orgid = 0;
    private String orgname = "";
    private String orgacronym = "";
    private int teamleadid = 0;
    private int year = 0;
    private int sequenceid = 0;
    private java.util.Date scheduleddate = null;    
    private java.util.Date rescheduleddate = null;
    private java.util.Date signeddate = null;
    private String comments = "";
    private java.util.Date cancelleddate = null;
    private String cancelledrationale = "";
    private int supportteamleadid = 0;
    private Blob pdf = null;
    private String hascirs = "";
    private String type = "";
    private int divid = 0;
    private String divname = "";
    private String divacronym = "";
    private String asstype = "";
    private String assobj = "";
    private String ll = "";
    private String crlevels = "";
    private String crnums = "";
    private String llnums = "";
    private String pdffilename = "";
    private int porgid = 0;
    
    private SAAttachment [] attachments = null;
    
    public boolean isNew = true;

    public static String SCHEMA = null;
    public static String SCHEMAPATH = null;
    public static String DBTYPE = null;

    /**
    * init new object
    */
    private void init () {
        DbConn tempDB = new DbConn("dummy");
        SCHEMA = "sat";
        SCHEMAPATH = "sat";
        DBTYPE = tempDB.getDBType();
    }
    
    /**
    * Creates a new empty SelfAssessment object
    */
    public SelfAssessment () {
        ID = 0;
        init();
		//System.out.println("SelfAssessment object created");
    }

    /**
    * Creates an SelfAssessment object and uses the given id to populate it from the db
    *
    * @param id     The id of the SelfAssessment to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public SelfAssessment (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a SelfAssessment from the db and stores it in the current SelfAssessment object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a SelfAssessment from the db and stores it in the current SelfAssessment object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, DbConn myConn) {
        ID = 0;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, title, scope, orgid, teamleadid, year, sequenceid, scheduleddate, rescheduleddate, signeddate, " +
                "comments, cancelleddate, cancelledrationale, supportteamleadid, pdf, hascirs,type, divisionid, asstype, assobj, ll, crlevels, " +
                "crnums, llnums, pdffilename, porgid FROM " + SCHEMAPATH + ".self_assessment WHERE id=" + id;
            //System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            if (myID == id) {
                ID = myID;
                title = rs.getString(2);
                scope = rs.getString(3);
                orgid = rs.getInt(4);
                teamleadid = rs.getInt(5);
                year = rs.getInt(6);
                sequenceid = rs.getInt(7);
                scheduleddate = rs.getDate(8);
                rescheduleddate = rs.getDate(9);
                signeddate = rs.getDate(10);
                comments = rs.getString(11);
                cancelleddate = rs.getDate(12);
                cancelledrationale = rs.getString(13);
                supportteamleadid = rs.getInt(14);
                pdf = rs.getBlob(15);
                hascirs = rs.getString(16);
                type = rs.getString(17);
                divid = rs.getInt(18);
                asstype = rs.getString(19);
                assobj = rs.getString(20);
                ll = rs.getString(21);
                crlevels = rs.getString(22);
                crnums = rs.getString(23);
                llnums = rs.getString(24);
                pdffilename = rs.getString(25);
                porgid = rs.getInt(26);                
                attachments = SAAttachment.getSAList(myConn, ID);
                isNew = false;

            }
            
            
            //get organization name and acronym 
            
            	stmt = conn.createStatement ();
    	        sqlcode = "SELECT id, name, acronym FROM " + SCHEMAPATH + ".ORGANIZATION where id=" + orgid;
    	        rs = stmt.executeQuery(sqlcode);
    	        rs.next();
    	        int myOrgID = rs.getInt(1);
    	        if (myOrgID == orgid) {
    	        	ID = myOrgID;
    	        	orgname = rs.getString(2);
                    orgacronym = rs.getString(3);
    	        }
    	   
    	    //get division name and acronym 
                	        
    	    if(divid>0){
            	stmt = conn.createStatement ();
    	        sqlcode = "SELECT id, name, acronym FROM " + SCHEMAPATH + ".DIVISION where id=" + divid;
    	        rs = stmt.executeQuery(sqlcode);
    	        rs.next();
    	        int myDivID = rs.getInt(1);
    	        if (myDivID == divid) {
    	        	ID = myDivID;
    	        	divname = rs.getString(2);
    	        	divacronym = rs.getString(3);
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
    * Get a list of SelfAssessment from the DB
    *
    * @param myConn     Connection to the database
    */
      
    public static SelfAssessment[] getSAList(DbConn myConn) {
        return getSAList(myConn, "id", 0, 0, 0);
    }
    
    /**
    * Get a list of SelfAssessment from the DB
    *
    * @param myConn     Connection to the database
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    * @param orgId  
    * @param year    
    */
        
    public static SelfAssessment[] getSAList(DbConn myConn, String sort, int porgid, int orgid, int year) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        SelfAssessment[] item = null;
        
        try {
            Connection conn = myConn.conn;
            stmt = conn.createStatement ();
            String mySchemaPath = "sat";

            sqlFrom = "FROM " + mySchemaPath + ".self_assessment";
            sqlWhere = " WHERE 1=1 ";
            if (orgid>0) {
                sqlWhere += "AND orgId = " + orgid + " ";
            }
            if (porgid>0) {
                sqlWhere += "AND porgId = " + porgid + " ";
            }
            if (year>0) {
                sqlWhere += "AND year = " + year + " ";
            }

            sqlOrderBy = (!sort.equals("none")) ? "ORDER BY " + sort + " " : "";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
//System.out.println("returnSize: "+returnSize);
            rs.close();
            item = new SelfAssessment[returnSize];

            sqlcode = "SELECT id, title, scope, orgid, teamleadid, year, sequenceid, scheduleddate, rescheduleddate, signeddate, " +
            "comments, cancelleddate, cancelledrationale, supportteamleadid, pdf, hascirs,type, divisionid, asstype, assobj, ll, crlevels, " +
            "crnums, llnums, pdffilename, porgid " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
System.out.println("********* "+sqlcode+" *********");

            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new SelfAssessment();
                item[i].ID = rs.getInt(1);
                item[i].title = rs.getString(2);
                item[i].scope = rs.getString(3);
                item[i].orgid = rs.getInt(4);
                item[i].teamleadid = rs.getInt(5);
                item[i].year = rs.getInt(6);
                item[i].sequenceid = rs.getInt(7);
                item[i].scheduleddate = rs.getDate(8);
                item[i].rescheduleddate = rs.getDate(9);
                item[i].signeddate = rs.getDate(10);
                item[i].comments = rs.getString(11);
                item[i].cancelleddate = rs.getDate(12);
                item[i].cancelledrationale = rs.getString(13);
                item[i].supportteamleadid = rs.getInt(14);
                item[i].pdf = rs.getBlob(15);
                item[i].hascirs = rs.getString(16);
                item[i].type = rs.getString(17);
                item[i].divid = rs.getInt(18);
                item[i].asstype = rs.getString(19);
                item[i].assobj = rs.getString(20);
                item[i].ll = rs.getString(21);
                item[i].crlevels = rs.getString(22);
                item[i].crnums = rs.getString(23);
                item[i].llnums = rs.getString(24);
                item[i].pdffilename = rs.getString(25);
                item[i].porgid = rs.getInt(26);                
                item[i].attachments = SAAttachment.getSAList(myConn, item[i].ID);
                i++;
                
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SelfAssessment list lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }
    
    




    /**
    * Retrieve the id for the current SelfAssessment
    *
    * @return id    The id for the current SelfAssessment
    */
    public int getID() {
        int id = ID;
        return(id);
    }
    
    public String getSANumber(){
    	String sorgacro = "";
    	String syear = "";
    	String sseqnum = "";
    	String sdivacro = "";
    	String stype = "";
    	sorgacro = this.getOrgacronym(); 
    	syear = Integer.toString(this.getYear());
    	sseqnum = Integer.toString(this.getSequenceid());
    	stype=(this.getType()!=null)?this.getType():"";
    	sdivacro=(this.getDivacronym().length()>0)?this.getDivacronym()+"-":"";
    	sanumber = sorgacro + "-" + sdivacro + syear + "-" +stype+ lpadzero(sseqnum,2);
    	return(sanumber);
    }
    
    /*
    public String getSANumber(){
    	String sorgacro = "";
    	String syear = "";
    	String sseqnum = "";
    	String sdivacro = "";
    	String stype = "";
    	sorgacro = this.getOrgacronym(); 
    	syear = Integer.toString(this.getYear());
    	sseqnum = Integer.toString(this.getSequeceid());
    	stype=(this.getType()!=null)?this.getType():"";
    	sdivacro=(this.getDivacronym().length()>0)?this.getDivacronym()+"-":"";
    	sanumber = sorgacro + "-" + sdivacro + syear + "-" +stype+ lpadzero(sseqnum,2);
    	return(sanumber);
    }
    */
    
    public String getSANumber(int orgId, int divId, DbConn myConn){
    	String sorgacro = "";
    	String syear = "";
    	String sseqnum = "";
    	String sdivacro = "";
    	String stype = "";
    	sorgacro = this.getOrgacronym(orgId,myConn); 
    	syear = Integer.toString(this.getYear());
    	sseqnum = Integer.toString(this.getSequenceid());
    	stype=(this.getType()!=null)?this.getType():"";
    	sdivacro=(this.getDivacronym(divId,myConn).length()>0)?this.getDivacronym(divId,myConn)+"-":"";
    	sanumber = sorgacro + "-" + sdivacro + syear + "-" +stype+ lpadzero(sseqnum,2);
    	return(sanumber);
    }
    
    public String lpadzero(String instring, int strlength){
    	String outstring = "";
    	for(int i=0;i<strlength-instring.length();i++){
    		outstring += "0";
    	}
    	outstring = outstring+instring;
    	return (outstring);
    }
    
    public String getTitle() {
        return(title);
    }
    
    public String getScope() {
        return(scope);
    }
    
    public String getPurpose() {
        return(scope);
    }
   
    public int getOrgid() {
        return(orgid);
    }
   
    public String getOrgacronym(){
    	return(orgacronym);
    }
    
    public String getOrgacronym(int orgid, DbConn myConn){
    	ResultSet rs = null;
        Statement stmt = null;
        Connection conn = myConn.conn;
    	String outLine = null;
        try{
	        stmt = conn.createStatement ();
	        String sqlcode = "SELECT id, name, acronym FROM " + SCHEMAPATH + ".ORGANIZATION where id=" + orgid;
	        rs = stmt.executeQuery(sqlcode);
	        rs.next();
	        int myOrgID = rs.getInt(1);
	        if (myOrgID == orgid) {
	        	ID = myOrgID;
	        	orgname = rs.getString(2);
	            orgacronym = rs.getString(3);
	        }   
        }catch(SQLException e) {
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
    	return(orgacronym);
    }
    
    public String getDivacronym(int divid, DbConn myConn){
    	ResultSet rs = null;
        Statement stmt = null;
        Connection conn = myConn.conn;
        String outLine = null;
        try{
	        stmt = conn.createStatement ();
	        String sqlcode = "SELECT id, name, acronym FROM " + SCHEMAPATH + ".DIVISION where id=" + divid;
	        rs = stmt.executeQuery(sqlcode);
	        rs.next();
	        int myDivID = rs.getInt(1);
	        if (myDivID == divid) {
	        	ID = myDivID;
	        	divname = rs.getString(2);
	        	divacronym = rs.getString(3);
	        } 
        }catch(SQLException e) {
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
    	return(divacronym);
    }
    
    public String getDivacronym(){
    	return(divacronym);
    }
    
    
    
    public int getTeamleadid() {
        return(teamleadid);
    }

    String teamleadname = null;
    
    public String getTeamleadname(int id, DbConn myConn) {
    	int ID = 0;
    	String outLine = "";
    	String username = null;
    	String firstname = null;
    	String lastname = null;
    	String teamleadname = null;
    	ResultSet rs = null;
        Statement stmt = null;
    	try{
    		Connection conn = myConn.conn;
            stmt = conn.createStatement ();
	        String sqlcode = "SELECT id, username, firstname, lastname FROM " + SCHEMAPATH + ".USERS where id=" + id;
//System.out.println(sqlcode);
	        rs = stmt.executeQuery(sqlcode);
	        //rs.next();
	        while (rs.next()) {
		        int myID = rs.getInt(1);
		        if (myID == id) {
		        	ID = myID;
		        	username = rs.getString(2);
	                firstname = rs.getString(3);
	                lastname = rs.getString(4);
	                teamleadname = firstname + " " + lastname;
		        }
	        }
        } catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            System.out.println(outLine);
            //log(outLine);
            //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SelfAssessment lookup");
        } catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            //log(outLine);
            //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
        } finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    	return(teamleadname);
    }
    
    public int getYear() {
        return(year);
    }
    
    public int getSequenceid() {
        return(sequenceid);
    }
  
    public java.util.Date getScheduleddate() {
        return(scheduleddate);
    }
    
    public java.util.Date getRescheduleddate() {
        return(rescheduleddate);
    }
    
    public java.util.Date getSigneddate() {
        return(signeddate);
    }
        
    public String getComments() {
        return(comments);
    }
    
    public java.util.Date getCancelleddate() {
        return(cancelleddate);
    }
    
    public String getCancelledrationale() {
        return(cancelledrationale);
    }
    
    public int getSupportteamleadid() {
        return(supportteamleadid);
    }
    
    public Blob getPdf() {
        return(pdf);
    }
    
    public String getHascirs() {
        return(hascirs);
    }
    
    public String getType() {
        return(type);
    }
    
    public int getDivid() {
        return(divid);
    }
    
    public String getAsstype() {
        return(asstype);
    }
    
    public String getAssobj() {
        return(assobj);
    }
    
    public String getLl() {
        return(ll);
    }
    
    public String getCrlevels() {
        return(crlevels);
    }
    
    public String getCrnums() {
        return(crnums);
    }
    
    public String getLlnums() {
        return(llnums);
    }
    
    public String getPdffilename() {
        return(pdffilename);
    }

    public int getPorgid() {
        return(porgid);
    }
    
    
    /**
    * Retrieve the attachments for the current SelfAssessment
    *
    * @return attachments    The attachments for the current SelfAssessment
    */
    public SAAttachment [] getAttachments() {
        return(attachments);
    }

    /**
    * Set the owner for the current SelfAssessment
    *
    * @param txt     The new title of the SelfAssessment (String)
    */
    public void setTitle(String txt) {
        title = txt;
    }

    public void setScope(String txt) {
        scope = txt;
    }
    
    public void setOrgid(int id) {
        orgid = id;
    }
   
    public void setTeamleadid(int id) {
    	teamleadid = id;
    }
   
    public void setYear(int id) {
    	year = id;
    }
    
    public void setSequeceid(int id) {
    	sequenceid = id;
    }
    
    public void setScheduleddate(java.util.Date dt) {
    	scheduleddate = dt;
    }
    
    public void setRescheduleddate(java.util.Date dt) {
    	rescheduleddate = dt;
    }
    
    public void setSigneddate(java.util.Date dt) {
    	signeddate = dt;
    }
    
    public void setComments(String txt) {
        comments = txt;
    }
        
    public void setCancelleddate(java.util.Date dt) {
    	cancelleddate = dt;
    }
    
    public void setCancelledrationale(String txt) {
    	cancelledrationale = txt;
    }
    

    public void setSupportteamleadid(int id) {
        supportteamleadid = id;
    }
    
    public void setPdf(Blob file) {
        pdf = file;
    }
    
    public void setHascirs(String txt) {
        hascirs = txt;
    }
    
    public void setType(String txt) {
        type = txt;
    }
    
    public void setdivid(int id) {
        divid = id;
    }
    
    public void setAsstype(String txt) {
        asstype = txt;
    }
    
    public void setAssobj(String txt) {
        assobj = txt;
    }
    
    public void setLl(String txt) {
        ll = txt;
    }
    
    public void setCrlevels(String txt) {
        crlevels = txt;
    }
    
    public void setCrnums(String txt) {
        crnums = txt;
    }
    
    public void setLlnums(String txt) {
        llnums = txt;
    }
    
    public void setPdffilename(String txt) {
        pdffilename = txt;
    }

    public void setPorgid(int id) {
        porgid = id;
    }
    
    
    /**
    * Save the current SelfAssessment to the DB
    *
    * @param myConn     Connection to the database
    * @param userID     The id of the user performing the action (long)
    */
    
/*    
    
    public void save(DbConn myConn, long userID) {
       //save funciton
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sqlcode = "";
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            conn.setAutoCommit(false);
            if (isNew) {
                sqlcode = "SELECT " + SCHEMAPATH + ".WORKREQUEST_ID_SEQ.NEXTVAL FROM dual";
System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID;

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".work_request (id, owner, contact, email, " +
                    "organization, phone, modifyexisting, existing_system, reason, benefits, involved_orgs, business_process, requested_delivery, " +
                    "comments, submit_date, disposition, disposition_date, requirements) " +
                    "VALUES (" + ID + ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, 'Pending', SYSDATE, ?" +
                    "); end;";
System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, owner);
                pstmt.setString(++sqlID, contact);
                pstmt.setString(++sqlID, email);
                pstmt.setString(++sqlID, organization);
                pstmt.setString(++sqlID, phone);
                pstmt.setString(++sqlID, modifyExisting);
                pstmt.setString(++sqlID, existingSystem);
                pstmt.setString(++sqlID, reason);
                pstmt.setString(++sqlID, benefits);
                pstmt.setString(++sqlID, involvedOrgs);
                pstmt.setString(++sqlID, businessProcess);
                pstmt.setString(++sqlID, requestedDelivery);
                pstmt.setString(++sqlID, comments);
                pstmt.setString(++sqlID, requirements);
                //pstmt.setTime(++sqlID, new java.sql.Time(dispositionDate.getTime()));
                //pstmt.setString(++sqlID, reasonRej);
System.out.println("SelfAssessment - save - Got here 1");
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".work_request " +
                      "SET owner = ?, contact = ?, email = ?, organization = ?, phone = ?, modifyExisting = ?, ";
                if (modifyExisting.equals("Yes")) {
                    sqlcode += "existing_system = ?, ";
                } else {
                    sqlcode += "existing_system = NULL, ";
                }
                sqlcode += "reason = ?, benefits = ?, involved_orgs = ?, business_process = ?, requested_delivery = ?, ";
                if (comments != null) {
                    sqlcode += "comments = ?, ";
                } else {
                    sqlcode += "comments = NULL, ";
                }
                if (!disposition.equals(dispositionSave)) {
                    sqlcode += " disposition = ?, disposition_date = SYSDATE, ";
                }
                sqlcode += "requirements = ? ";
                if (reasonRej != null) {
                    sqlcode += ",reason_rej = ?    ";
                } else {
                    sqlcode += ",reason_rej = NULL    ";
                }
                sqlcode += "WHERE id = " + ID +
                      "; end;";
System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, owner);
                pstmt.setString(++sqlID, contact);
                pstmt.setString(++sqlID, email);
                pstmt.setString(++sqlID, organization);
                pstmt.setString(++sqlID, phone);
                pstmt.setString(++sqlID, modifyExisting);
                if (modifyExisting.equals("Yes")) {
                    pstmt.setString(++sqlID, existingSystem);
                }
                pstmt.setString(++sqlID, reason);
                pstmt.setString(++sqlID, benefits);
                pstmt.setString(++sqlID, involvedOrgs);
                pstmt.setString(++sqlID, businessProcess);
                pstmt.setString(++sqlID, requestedDelivery);
                if (comments != null) {
                    pstmt.setString(++sqlID, comments);
                }
                if (!disposition.equals(dispositionSave)) {
                    pstmt.setString(++sqlID, disposition);
                }
                pstmt.setString(++sqlID, requirements);
                if (reasonRej != null) {
                    pstmt.setString(++sqlID, reasonRej);
                }
                rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - SelfAssessment save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - SelfAssessment save");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
            if (pstmt != null)
                try { pstmt.close(); } catch (Exception i) {}
        }
// do auth stuff
    }

*/

}
