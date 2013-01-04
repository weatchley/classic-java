package gov.ymp.pclwr.model;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import gov.ymp.pclwr.model.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
* Archive is the class for archived reports in the db
*
* @author   Bill Atchley
*/
public class SoftwareWorkRequest {
    int ID = 0;
    private String owner = null;
    private String contact = null;
    private String email = null;
    private String organization = null;
    private String phone = null;
    private String modifyExisting = null;
    private String existingSystem = null;
    private String reason = null;
    private String benefits = null;
    private String involvedOrgs = null;
    private String businessProcess = null;
    private String requestedDelivery = null;
    private String comments = null;
    private java.util.Date submitDate = null;
    private String disposition = null;
    private String dispositionSave = null;
    private java.util.Date dispositionDate = null;
    private String requirements = null;
    private String reasonRej = null;
    private SoftwareAttachment [] attachments = null;
    public boolean isNew = true;

    //public static String SCHEMA = "csi";
    public static String SCHEMA = null;
    public static String SCHEMAPATH = null;
    public static String DBTYPE = null;

    /**
    * init new object
    */
    private void init () {
        DbConn tempDB = new DbConn("dummy");
        //SCHEMA = tempDB.getSchemaName();
        //SCHEMAPATH = tempDB.getSchemaPath();
        SCHEMA = "pcl";
        SCHEMAPATH = "pcl";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty SoftwareWorkRequest object
    */
    public SoftwareWorkRequest () {
        ID = 0;
        init();
    }


    /**
    * Creates an SoftwareWorkRequest object and uses the given id to populate it from the db
    *
    * @param id     The id of the SoftwareWorkRequest to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public SoftwareWorkRequest (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a SoftwareWorkRequest from the db and stores it in the current SoftwareWorkRequest object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a SoftwareWorkRequest from the db and stores it in the current SoftwareWorkRequest object
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

            String sqlcode = "SELECT id, owner, contact, email, organization, phone, modifyexisting, existing_system, reason, " +
                "benefits, involved_orgs, business_process, requested_delivery, comments, submit_date, disposition, disposition_date, " +
                "requirements, reason_rej FROM " + SCHEMAPATH + ".work_request WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            if (myID == id) {
                ID = myID;
                owner = rs.getString(2);
                contact = rs.getString(3);
                email = rs.getString(4);
                organization = rs.getString(5);
                phone = rs.getString(6);
                modifyExisting = rs.getString(7);
                existingSystem = rs.getString(8);
                reason = rs.getString(9);
                benefits = rs.getString(10);
                involvedOrgs = rs.getString(11);
                businessProcess = rs.getString(12);
                requestedDelivery = rs.getString(13);
                comments = rs.getString(14);
                submitDate = rs.getTimestamp(15);
                disposition = rs.getString(16);
                dispositionSave = disposition;
                dispositionDate = rs.getTimestamp(17);
                requirements = rs.getString(18);
                reasonRej = rs.getString(19);
                attachments = SoftwareAttachment.getItemList(myConn, ID);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SoftwareWorkRequest lookup");
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
    }


    /**
    * Retrieve the id for the current SoftwareWorkRequest
    *
    * @return id    The id for the current SoftwareWorkRequest
    */
    public int getID() {
        int id = ID;
        return(id);
    }


    /**
    * Retrieve the owner for the current SoftwareWorkRequest
    *
    * @return owner    The owner for the current SoftwareWorkRequest
    */
    public String getOwner() {
        return(owner);
    }

    /**
    * Retrieve the contact for the current SoftwareWorkRequest
    *
    * @return  contact   The contact for the current SoftwareWorkRequest
    */
    public String getContact() {
        return(contact);
    }

    /**
    * Retrieve the email for the current SoftwareWorkRequest
    *
    * @return  email   The email for the current SoftwareWorkRequest
    */
    public String getEmail() {
        return(email);
    }

    /**
    * Retrieve the organization for the current SoftwareWorkRequest
    *
    * @return organization    The organization for the current SoftwareWorkRequest
    */
    public String getOrganization() {
        return(organization);
    }

    /**
    * Retrieve the phone for the current SoftwareWorkRequest
    *
    * @return  phone   The phone for the current SoftwareWorkRequest
    */
    public String getPhone() {
        return(phone);
    }

    /**
    * Retrieve the modifyExisting for the current SoftwareWorkRequest
    *
    * @return  modifyExisting   The modifyExisting for the current SoftwareWorkRequest
    */
    public String getModifyExisting() {
        return(modifyExisting);
    }

    /**
    * Retrieve the modifyExisting for the current SoftwareWorkRequest
    *
    * @return  modifyExisting   The modifyExisting for the current SoftwareWorkRequest
    */
    public boolean modifyExisting() {
        return((modifyExisting.equals("Yes")) ? true : false);
    }

    /**
    * Retrieve the existingSystem for the current SoftwareWorkRequest
    *
    * @return  existingSystem   The existingSystem for the current SoftwareWorkRequest
    */
    public String getExistingSystem() {
        return(existingSystem);
    }

    /**
    * Retrieve the reason for the current SoftwareWorkRequest
    *
    * @return reason    The reason for the current SoftwareWorkRequest
    */
    public String getReason() {
        return(reason);
    }

    /**
    * Retrieve the benefits for the current SoftwareWorkRequest
    *
    * @return benefits    The benefits for the current SoftwareWorkRequest
    */
    public String getBenefits() {
        return(benefits);
    }

    /**
    * Retrieve the involvedOrgs for the current SoftwareWorkRequest
    *
    * @return   involvedOrgs  The involvedOrgs for the current SoftwareWorkRequest
    */
    public String getInvolvedOrgs() {
        return(involvedOrgs);
    }

    /**
    * Retrieve the businessProcess for the current SoftwareWorkRequest
    *
    * @return   businessProcess  The businessProcess for the current SoftwareWorkRequest
    */
    public String getBusinessProcess() {
        return(businessProcess);
    }

    /**
    * Retrieve the requestedDelivery for the current SoftwareWorkRequest
    *
    * @return  requestedDelivery   The requestedDelivery for the current SoftwareWorkRequest
    */
    public String getRequestedDelivery() {
        return(requestedDelivery);
    }

    /**
    * Retrieve the comments for the current SoftwareWorkRequest
    *
    * @return comments    The comments for the current SoftwareWorkRequest
    */
    public String getComments() {
        return(comments);
    }

    /**
    * Retrieve the submitDate for the current SoftwareWorkRequest
    *
    * @return submitDate    The submitDate for the current SoftwareWorkRequest
    */
    public java.util.Date getSubmitDate() {
        return(submitDate);
    }

    /**
    * Retrieve the disposition for the current SoftwareWorkRequest
    *
    * @return disposition    The disposition for the current SoftwareWorkRequest
    */
    public String getDisposition() {
        return(disposition);
    }

    /**
    * Retrieve the dispositionSave for the current SoftwareWorkRequest
    *
    * @return dispositionSave    The dispositionSave for the current SoftwareWorkRequest
    */
    public String getDispositionSave() {
        return(dispositionSave);
    }

    /**
    * Retrieve the dispositionDate for the current SoftwareWorkRequest
    *
    * @return dispositionDate    The dispositionDate for the current SoftwareWorkRequest
    */
    public java.util.Date getDispositionDate() {
        return(dispositionDate);
    }

    /**
    * Retrieve the requirements for the current SoftwareWorkRequest
    *
    * @return requirements    The rejection requirements for the current SoftwareWorkRequest
    */
    public String getRequirements() {
        return(requirements);
    }

    /**
    * Retrieve the reasonRej for the current SoftwareWorkRequest
    *
    * @return reasonRej    The rejection reason for the current SoftwareWorkRequest
    */
    public String getReasonRej() {
        return(reasonRej);
    }

    /**
    * Retrieve the attachments for the current SoftwareWorkRequest
    *
    * @return attachments    The attachments for the current SoftwareWorkRequest
    */
    public SoftwareAttachment [] getAttachments() {
        return(attachments);
    }


    /**
    * Set the owner for the current SoftwareWorkRequest
    *
    * @param txt     The new owner of the SoftwareWorkRequest (String)
    */
    public void setOwner(String txt) {
        owner = txt;
    }

    /**
    * Set the contact for the current SoftwareWorkRequest
    *
    * @param txt     The new contact of the SoftwareWorkRequest (String)
    */
    public void setContact(String txt) {
        contact = txt;
    }

    /**
    * Set the email for the current SoftwareWorkRequest
    *
    * @param txt     The new email of the SoftwareWorkRequest (String)
    */
    public void setEmail(String txt) {
        email = txt;
    }

    /**
    * Set the organization for the current SoftwareWorkRequest
    *
    * @param txt     The new organization of the SoftwareWorkRequest (String)
    */
    public void setOrganization(String txt) {
        organization = txt;
    }

    /**
    * Set the phone for the current SoftwareWorkRequest
    *
    * @param txt     The new phone of the SoftwareWorkRequest (String)
    */
    public void setPhone(String txt) {
        phone = txt;
    }

    /**
    * Set the modifyExisting for the current SoftwareWorkRequest
    *
    * @param txt     The new modifyExisting of the SoftwareWorkRequest (String)
    */
    public void setModifyExisting(String txt) {
        modifyExisting = txt;
    }

    /**
    * Set the existingSystem for the current SoftwareWorkRequest
    *
    * @param txt     The new existingSystem of the SoftwareWorkRequest (String)
    */
    public void setExistingSystem(String txt) {
        existingSystem = txt;
    }

    /**
    * Set the reason for the current SoftwareWorkRequest
    *
    * @param txt     The new reason of the SoftwareWorkRequest (String)
    */
    public void setReason(String txt) {
        reason = txt;
    }

    /**
    * Set the benefits for the current SoftwareWorkRequest
    *
    * @param txt     The new benefits of the SoftwareWorkRequest (String)
    */
    public void setBenefits(String txt) {
        benefits = txt;
    }

    /**
    * Set the involvedOrgs for the current SoftwareWorkRequest
    *
    * @param txt     The new involvedOrgs of the SoftwareWorkRequest (String)
    */
    public void setInvolvedOrgs(String txt) {
        involvedOrgs = txt;
    }

    /**
    * Set the businessProcess for the current SoftwareWorkRequest
    *
    * @param txt     The new businessProcess of the SoftwareWorkRequest (String)
    */
    public void setBusinessProcess(String txt) {
        businessProcess = txt;
    }

    /**
    * Set the requestedDelivery for the current SoftwareWorkRequest
    *
    * @param txt     The new requestedDelivery of the SoftwareWorkRequest (String)
    */
    public void setRequestedDelivery(String txt) {
        requestedDelivery = txt;
    }

    /**
    * Set the comments for the current SoftwareWorkRequest
    *
    * @param txt     The new comments of the SoftwareWorkRequest (String)
    */
    public void setComments(String txt) {
        comments = txt;
    }

    /**
    * Set the submitDate for the current SoftwareWorkRequest
    *
    * @param dt     The new submitDate of the SoftwareWorkRequest (Date)
    */
    public void setSubmitDate(java.util.Date dt) {
        submitDate = dt;
    }

    /**
    * Set the disposition for the current SoftwareWorkRequest
    *
    * @param txt     The new disposition of the SoftwareWorkRequest (String)
    */
    public void setDisposition(String txt) {
        disposition = txt;
    }

    /**
    * Set the dispositionDate for the current SoftwareWorkRequest
    *
    * @param dt     The new dispositionDate of the SoftwareWorkRequest (Date)
    */
    public void setDispositionDate(java.util.Date dt) {
        dispositionDate = dt;
    }

    /**
    * Set the requirements for the current SoftwareWorkRequest
    *
    * @param txt     The new requirements of the SoftwareWorkRequest (String)
    */
    public void setRequirements(String txt) {
        requirements = txt;
    }

    /**
    * Set the reasonRej for the current SoftwareWorkRequest
    *
    * @param txt     The new rejection reason of the SoftwareWorkRequest (String)
    */
    public void setReasonRej(String txt) {
        reasonRej = txt;
    }


    /**
    * Save the current SoftwareWorkRequest to the DB
    *
    * @param myConn     Connection to the database
    * @param userID     The id of the user performing the action (long)
    */
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
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID;

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".work_request (id, owner, contact, email, " +
                    "organization, phone, modifyexisting, existing_system, reason, benefits, involved_orgs, business_process, requested_delivery, " +
                    "comments, submit_date, disposition, disposition_date, requirements) " +
                    "VALUES (" + ID + ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, 'Pending', SYSDATE, ?" +
                    "); end;";
//System.out.println(sqlcode);
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
//System.out.println("SoftwareWorkRequest - save - Got here 1");
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
//System.out.println(sqlcode);
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
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - SoftwareWorkRequest save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - SoftwareWorkRequest save");
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


    /**
    * Get a list of SoftwareWorkRequest from the DB
    *
    * @param myConn     Connection to the database
    */
    public static SoftwareWorkRequest[] getItemList(DbConn myConn) {
        return getItemList(myConn, "id", "all");
    }


    /**
    * Get a list of SoftwareWorkRequest from the DB
    *
    * @param myConn     Connection to the database
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    * @param disp       The requested disposition type ('all' to select all types)
    */
    public static SoftwareWorkRequest[] getItemList(DbConn myConn, String sort, String disp) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        SoftwareWorkRequest[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "pcl";

            //sqlFrom = " FROM " + SCHEMAPATH + ".work_request ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".work_request ";
            sqlFrom = " FROM " + mySchemaPath + ".work_request ";
            sqlWhere = " WHERE 1=1 ";
            if (!disp.equals("all")) {
                sqlWhere += "AND disposition = '" + disp + "' ";
            }

            sqlOrderBy = (!sort.equals("none")) ? " ORDER BY " + sort + " " : "";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new SoftwareWorkRequest[returnSize];

            sqlcode = "SELECT id, owner, contact, email, organization, phone, modifyexisting, existing_system, reason, " +
                "benefits, involved_orgs, business_process, requested_delivery, comments, submit_date, disposition, disposition_date, " +
                "requirements, reason_rej " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new SoftwareWorkRequest();
                item[i].ID = rs.getInt(1);
                item[i].owner = rs.getString(2);
                item[i].contact = rs.getString(3);
                item[i].email = rs.getString(4);
                item[i].organization = rs.getString(5);
                item[i].phone = rs.getString(6);
                item[i].modifyExisting = rs.getString(7);
                item[i].existingSystem = rs.getString(8);
                item[i].reason = rs.getString(9);
                item[i].benefits = rs.getString(10);
                item[i].involvedOrgs = rs.getString(11);
                item[i].businessProcess = rs.getString(12);
                item[i].requestedDelivery = rs.getString(13);
                item[i].comments = rs.getString(14);
                item[i].submitDate = rs.getTimestamp(15);
                item[i].disposition = rs.getString(16);
                item[i].dispositionSave = item[i].disposition;
                item[i].dispositionDate = rs.getTimestamp(17);
                item[i].requirements = rs.getString(18);
                item[i].reasonRej = rs.getString(19);
                item[i].attachments = SoftwareAttachment.getItemList(myConn, item[i].ID);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SoftwareWorkRequest list lookup");
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




}
