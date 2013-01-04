package gov.ymp.mms.model;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
* ApprovalList is the class for approval list in the db
*
* @author   Bill Atchley
*/
public class ApprovalList {
    public String prnumber = null;
    public int role = 0;
    public int userID = 0;
    public int pdStatus = 0;
    public java.util.Date dateApproved = null;
    public int approvedBy = 0;
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
        SCHEMA = "mms";
        SCHEMAPATH = "mms";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty ApprovalList object
    */
    public ApprovalList () {
        prnumber = null;
        init();
    }


    /**
    * Creates a ApprovalList object and uses the given value to populate it from the db
    *
    * @param pr      The pr to lookup from the db
    * @param rol     The role to lookup from the db
    * @param user    The userid to lookup from the db
    * @param stat    The pdstatus to lookup from the db
    * @param myConn  Connection to the database
    */
    public ApprovalList (String pr, int rol, int user, int stat, DbConn myConn) {
        init();
        lookup(pr, rol, user, stat, myConn);
    }


    /**
    * Retrieves a ApprovalList from the db and stores it in the current ApprovalList object
    *
    * @param pr      The pr to lookup from the db
    * @param rol     The role to lookup from the db
    * @param user    The userid to lookup from the db
    * @param stat    The pdstatus to lookup from the db
    * @param myConn  Connection to the database
    */
    public void getInfo (String pr, int rol, int user, int stat, DbConn myConn) {
        lookup(pr, rol, user, stat, myConn);
    }


    /**
    * Retrieves a ApprovalList from the db and stores it in the current ApprovalList object
    *
    * @param pr      The pr to lookup from the db
    * @param rol     The role to lookup from the db
    * @param user    The userid to lookup from the db
    * @param stat    The pdstatus to lookup from the db
    * @param myConn  Connection to the database
    */
    public void lookup (String pr, int rol, int user, int stat, DbConn myConn) {
        prnumber = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT prnumber, role, userid, pdstatus, dateapproved, approvedby ";
            sqlcode += " FROM " + SCHEMAPATH + ".approval_list WHERE prnumber='" + pr + "' AND role=" + rol + " AND userid=" + user + " AND pdstatus=" + stat + "";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String myPR = rs.getString(1);
            if (myPR.equals(pr)) {
                prnumber = myPR;
                role = rs.getInt(2);
                userID = rs.getInt(3);
                pdStatus = rs.getInt(4);
                dateApproved = (java.util.Date) rs.getDate(5);
                approvedBy = rs.getInt(6);

                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - ApprovalList lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - ApprovalList lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get a list of ApprovalList from the DB
    *
    * @param pr           The pr to lookup from the db
    * @param stat         The pdstatus to lookup from the db
    * @param myConn       Connection to the database
    */
    public static ApprovalList[] getItemList(DbConn myConn, String pr, int stat) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        ApprovalList[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            sqlFrom = " FROM " + mySchemaPath + ".approval_list ";
            sqlWhere = " WHERE 1=1 ";
            sqlWhere += (pr != null) ? " prnumber='" + pr + "' " : "";
            sqlWhere += (stat != 0) ? " pdstatus=" + stat + " " : "";
            sqlOrderBy = " ORDER BY prnumber, role, pdstatus ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new ApprovalList[returnSize];

            sqlcode = "SELECT prnumber, role, userid, pdstatus, dateapproved, approvedby " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new ApprovalList();
                item[i].prnumber = rs.getString(1);
                item[i].role = rs.getInt(2);
                item[i].userID = rs.getInt(3);
                item[i].pdStatus = rs.getInt(4);
                item[i].dateApproved = (java.util.Date) rs.getDate(5);
                item[i].approvedBy = rs.getInt(6);

                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ApprovalList - getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ApprovalList - getItemList");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }

    public int getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(int approvedBy) {
        this.approvedBy = approvedBy;
    }

    public java.util.Date getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(java.util.Date dateApproved) {
        this.dateApproved = dateApproved;
    }

    public int getPdStatus() {
        return pdStatus;
    }

    public void setPdStatus(int pdStatus) {
        this.pdStatus = pdStatus;
    }

    public String getPrnumber() {
        return prnumber;
    }

    public void setPrnumber(String prnumber) {
        this.prnumber = prnumber;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }




}
