package gov.ymp.itwr.model;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import gov.ymp.itwr.model.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
* Archive is the class for archived reports in the db
*
* @author   Bill Atchley
*/
public class WorkRequest {
    int ID = 0;
    private String requester = null;
    private String contact = null;
    private String email = null;
    private String organization = null;
    private String phone = null;
    private int type = 0;
    private String typeDescription = null;
    private String otherType = null;
    private String details = null;
    private String benefits = null;
    private String involvedOrgs = null;
    private String requestedDelivery = null;
    private String comments = null;
    private java.util.Date submitDate = null;
    private String disposition = null;
    private String dispositionSave = null;
    private java.util.Date dispositionDate = null;
    private String reasonRej = null;
    private Attachment [] attachments = null;
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
        SCHEMA = "itwr";
        SCHEMAPATH = "itwr";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty WorkRequest object
    */
    public WorkRequest () {
        ID = 0;
        init();
    }


    /**
    * Creates an WorkRequest object and uses the given id to populate it from the db
    *
    * @param id     The id of the WorkRequest to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public WorkRequest (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Creates an WorkRequest object and uses the given date to populate it and save it to the db
    *
    * \\@param txt     The name of the WorkRequest (String)
    * \\@param myConn  Connection to the database
    * \\@param userID  The id of the user performing the action (long)
    public WorkRequest (String txt, DbConn myConn, long userID) {
        init();
        java.util.Date dNow = new java.util.Date();
        name = txt;
        dateAdded = dNow;
        save(myConn, userID);
    }
    */


    /**
    * Retrieves a WorkRequest from the db and stores it in the current WorkRequest object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a WorkRequest from the db and stores it in the current WorkRequest object
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
            HashMap wrTypes = WorkRequestType.getItemHash(myConn);

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, requester, contact, email, organization, phone, type, other_type, details, " +
                "benefits, involved_orgs, requested_delivery, comments, submit_date, disposition, disposition_date, " +
                "reason_rej FROM " + SCHEMAPATH + ".work_request WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            if (myID == id) {
                ID = myID;
                requester = rs.getString(2);
                contact = rs.getString(3);
                email = rs.getString(4);
                organization = rs.getString(5);
                phone = rs.getString(6);
                type = rs.getInt(7);
                typeDescription = (String) wrTypes.get(new Integer(type));
                otherType = rs.getString(8);
                details = rs.getString(9);
                benefits = rs.getString(10);
                involvedOrgs = rs.getString(11);
                requestedDelivery = rs.getString(12);
                comments = rs.getString(13);
                submitDate = rs.getTimestamp(14);
                disposition = rs.getString(15);
                dispositionSave = disposition;
                dispositionDate = rs.getTimestamp(16);
                reasonRej = rs.getString(17);
                attachments = Attachment.getItemList(myConn, ID);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - WorkRequest lookup");
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
    * Retrieve the id for the current WorkRequest
    *
    * @return id    The id for the current WorkRequest
    */
    public int getID() {
        int id = ID;
        return(id);
    }


    /**
    * Retrieve the requester for the current WorkRequest
    *
    * @return requester    The requester for the current WorkRequest
    */
    public String getRequester() {
        return(requester);
    }

    /**
    * Retrieve the contact for the current WorkRequest
    *
    * @return  contact   The contact for the current WorkRequest
    */
    public String getContact() {
        return(contact);
    }

    /**
    * Retrieve the email for the current WorkRequest
    *
    * @return  email   The email for the current WorkRequest
    */
    public String getEmail() {
        return(email);
    }

    /**
    * Retrieve the organization for the current WorkRequest
    *
    * @return organization    The organization for the current WorkRequest
    */
    public String getOrganization() {
        return(organization);
    }

    /**
    * Retrieve the phone for the current WorkRequest
    *
    * @return  phone   The phone for the current WorkRequest
    */
    public String getPhone() {
        return(phone);
    }

    /**
    * Retrieve the type for the current WorkRequest
    *
    * @return type    The type for the current WorkRequest
    */
    public int getType() {
        return(type);
    }

    /**
    * Retrieve the type description for the current WorkRequest
    *
    * @return typeDescription    The typeDescription for the current WorkRequest
    */
    public String getTypeDescription() {
        return(typeDescription);
    }

    /**
    * Retrieve the otherType for the current WorkRequest
    *
    * @return otherType    The otherType for the current WorkRequest
    */
    public String getOtherType() {
        return(otherType);
    }

    /**
    * Retrieve the details for the current WorkRequest
    *
    * @return details    The details for the current WorkRequest
    */
    public String getDetails() {
        return(details);
    }

    /**
    * Retrieve the benefits for the current WorkRequest
    *
    * @return benefits    The benefits for the current WorkRequest
    */
    public String getBenefits() {
        return(benefits);
    }

    /**
    * Retrieve the involvedOrgs for the current WorkRequest
    *
    * @return   involvedOrgs  The involvedOrgs for the current WorkRequest
    */
    public String getInvolvedOrgs() {
        return(involvedOrgs);
    }

    /**
    * Retrieve the requestedDelivery for the current WorkRequest
    *
    * @return  requestedDelivery   The requestedDelivery for the current WorkRequest
    */
    public String getRequestedDelivery() {
        return(requestedDelivery);
    }

    /**
    * Retrieve the comments for the current WorkRequest
    *
    * @return comments    The comments for the current WorkRequest
    */
    public String getComments() {
        return(comments);
    }

    /**
    * Retrieve the submitDate for the current WorkRequest
    *
    * @return submitDate    The submitDate for the current WorkRequest
    */
    public java.util.Date getSubmitDate() {
        return(submitDate);
    }

    /**
    * Retrieve the disposition for the current WorkRequest
    *
    * @return disposition    The disposition for the current WorkRequest
    */
    public String getDisposition() {
        return(disposition);
    }

    /**
    * Retrieve the dispositionSave for the current WorkRequest
    *
    * @return dispositionSave    The dispositionSave for the current WorkRequest
    */
    public String getDispositionSave() {
        return(dispositionSave);
    }

    /**
    * Retrieve the dispositionDate for the current WorkRequest
    *
    * @return dispositionDate    The dispositionDate for the current WorkRequest
    */
    public java.util.Date getDispositionDate() {
        return(dispositionDate);
    }

    /**
    * Retrieve the reasonRej for the current WorkRequest
    *
    * @return reasonRej    The rejection reason for the current WorkRequest
    */
    public String getReasonRej() {
        return(reasonRej);
    }

    /**
    * Retrieve the attachments for the current WorkRequest
    *
    * @return attachments    The attachments for the current WorkRequest
    */
    public Attachment [] getAttachments() {
        return(attachments);
    }


    /**
    * Set the requester for the current WorkRequest
    *
    * @param txt     The new requester of the WorkRequest (String)
    */
    public void setRequester(String txt) {
        requester = txt;
    }

    /**
    * Set the contact for the current WorkRequest
    *
    * @param txt     The new contact of the WorkRequest (String)
    */
    public void setContact(String txt) {
        contact = txt;
    }

    /**
    * Set the email for the current WorkRequest
    *
    * @param txt     The new email of the WorkRequest (String)
    */
    public void setEmail(String txt) {
        email = txt;
    }

    /**
    * Set the organization for the current WorkRequest
    *
    * @param txt     The new organization of the WorkRequest (String)
    */
    public void setOrganization(String txt) {
        organization = txt;
    }

    /**
    * Set the phone for the current WorkRequest
    *
    * @param txt     The new phone of the WorkRequest (String)
    */
    public void setPhone(String txt) {
        phone = txt;
    }

    /**
    * Set the type for the current WorkRequest
    *
    * @param typ     The new type of the WorkRequest (int)
    */
    public void setType(int typ) {
        type = typ;
    }

    /**
    * Set the otherType for the current WorkRequest
    *
    * @param txt     The new otherType of the WorkRequest (String)
    */
    public void setOtherType(String txt) {
        otherType = txt;
    }

    /**
    * Set the details for the current WorkRequest
    *
    * @param txt     The new details of the WorkRequest (String)
    */
    public void setDetails(String txt) {
        details = txt;
    }

    /**
    * Set the benefits for the current WorkRequest
    *
    * @param txt     The new benefits of the WorkRequest (String)
    */
    public void setBenefits(String txt) {
        benefits = txt;
    }

    /**
    * Set the involvedOrgs for the current WorkRequest
    *
    * @param txt     The new involvedOrgs of the WorkRequest (String)
    */
    public void setInvolvedOrgs(String txt) {
        involvedOrgs = txt;
    }

    /**
    * Set the requestedDelivery for the current WorkRequest
    *
    * @param txt     The new requestedDelivery of the WorkRequest (String)
    */
    public void setRequestedDelivery(String txt) {
        requestedDelivery = txt;
    }

    /**
    * Set the comments for the current WorkRequest
    *
    * @param txt     The new comments of the WorkRequest (String)
    */
    public void setComments(String txt) {
        comments = txt;
    }

    /**
    * Set the submitDate for the current WorkRequest
    *
    * @param dt     The new submitDate of the WorkRequest (Date)
    */
    public void setSubmitDate(java.util.Date dt) {
        submitDate = dt;
    }

    /**
    * Set the disposition for the current WorkRequest
    *
    * @param txt     The new disposition of the WorkRequest (String)
    */
    public void setDisposition(String txt) {
        disposition = txt;
    }

    /**
    * Set the dispositionDate for the current WorkRequest
    *
    * @param dt     The new dispositionDate of the WorkRequest (Date)
    */
    public void setDispositionDate(java.util.Date dt) {
        dispositionDate = dt;
    }

    /**
    * Set the reasonRej for the current WorkRequest
    *
    * @param txt     The new rejection reason of the WorkRequest (String)
    */
    public void setReasonRej(String txt) {
        reasonRej = txt;
    }


    /**
    * Save the current WorkRequest to the DB
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

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".work_request (id, requester, contact, email, " +
                    "organization, phone, type, other_type, details, benefits, involved_orgs, requested_delivery, " +
                    "comments, submit_date, disposition, disposition_date) " +
                    "VALUES (" + ID + ", ?, ?, ?, ?, ?, " + type + ", ?, ?, ?, ?, ?, ?, SYSDATE, 'Pending', SYSDATE" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, requester);
                pstmt.setString(++sqlID, contact);
                pstmt.setString(++sqlID, email);
                pstmt.setString(++sqlID, organization);
                pstmt.setString(++sqlID, phone);
                pstmt.setString(++sqlID, otherType);
                pstmt.setString(++sqlID, details);
                pstmt.setString(++sqlID, benefits);
                pstmt.setString(++sqlID, involvedOrgs);
                pstmt.setString(++sqlID, requestedDelivery);
                pstmt.setString(++sqlID, comments);
                //pstmt.setTime(++sqlID, new java.sql.Time(dispositionDate.getTime()));
                //pstmt.setString(++sqlID, reasonRej);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".work_request " +
                      "SET requester = ?, contact = ?, email = ?, organization = ?, phone = ?, type = " + type + ", ";
                if (type == 1) {
                    sqlcode += "other_type = ?, ";
                } else {
                    sqlcode += "other_type = NULL, ";
                }
                sqlcode += "details = ?, benefits = ?, involved_orgs = ?, requested_delivery = ?, ";
                if (comments != null) {
                    sqlcode += "comments = ? ";
                } else {
                    sqlcode += "comments = NULL ";
                }
                if (!disposition.equals(dispositionSave)) {
                    sqlcode += ", disposition = ?, disposition_date = SYSDATE ";
                }
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
                pstmt.setString(++sqlID, requester);
                pstmt.setString(++sqlID, contact);
                pstmt.setString(++sqlID, email);
                pstmt.setString(++sqlID, organization);
                pstmt.setString(++sqlID, phone);
                if (type == 1) {
                    pstmt.setString(++sqlID, otherType);
                }
                pstmt.setString(++sqlID, details);
                pstmt.setString(++sqlID, benefits);
                pstmt.setString(++sqlID, involvedOrgs);
                pstmt.setString(++sqlID, requestedDelivery);
                if (comments != null) {
                    pstmt.setString(++sqlID, comments);
                }
                if (!disposition.equals(dispositionSave)) {
                    pstmt.setString(++sqlID, disposition);
                }
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
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - WorkRequest save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - WorkRequest save");
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
    * Get a list of WorkRequest from the DB
    *
    * @param myConn     Connection to the database
    */
    public static WorkRequest[] getItemList(DbConn myConn) {
        return getItemList(myConn, "id", 0, "all");
    }


    /**
    * Get a list of WorkRequest from the DB
    *
    * @param myConn     Connection to the database
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    * @param typ        The requested work request type (0 for none)
    * @param disp       The requested disposition type ('all' to select all types)
    */
    public static WorkRequest[] getItemList(DbConn myConn, String sort, int typ, String disp) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        WorkRequest[] item = null;
        try {
            HashMap wrTypes = WorkRequestType.getItemHash(myConn);
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "itwr";

            //sqlFrom = " FROM " + SCHEMAPATH + ".work_request ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".work_request ";
            sqlFrom = " FROM " + mySchemaPath + ".work_request ";
            sqlWhere = " WHERE 1=1 ";
            if (typ > 0) {
                sqlWhere += "AND type = " + typ + " ";
            }
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
            item = new WorkRequest[returnSize];

            sqlcode = "SELECT id, requester, contact, email, organization, phone, type, other_type, details, " +
                "benefits, involved_orgs, requested_delivery, comments, submit_date, disposition, disposition_date, " +
                "reason_rej " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new WorkRequest();
                item[i].ID = rs.getInt(1);
                item[i].requester = rs.getString(2);
                item[i].contact = rs.getString(3);
                item[i].email = rs.getString(4);
                item[i].organization = rs.getString(5);
                item[i].phone = rs.getString(6);
                item[i].type = rs.getInt(7);
                item[i].typeDescription = (String) wrTypes.get(new Integer(item[i].type));
                item[i].otherType = rs.getString(8);
                item[i].details = rs.getString(9);
                item[i].benefits = rs.getString(10);
                item[i].involvedOrgs = rs.getString(11);
                item[i].requestedDelivery = rs.getString(12);
                item[i].comments = rs.getString(13);
                item[i].submitDate = rs.getTimestamp(14);
                item[i].disposition = rs.getString(15);
                item[i].dispositionSave = item[i].disposition;
                item[i].dispositionDate = rs.getTimestamp(16);
                item[i].reasonRej = rs.getString(17);
                item[i].attachments = Attachment.getItemList(myConn, item[i].ID);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - WorkRequest list lookup");
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
