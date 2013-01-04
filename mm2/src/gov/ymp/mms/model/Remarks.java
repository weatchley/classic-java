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
* PDStatus is the class for pdstatus in the db
*
* @author   Bill Atchley
*/
public class Remarks {
    public String prnumber = null;
    public int userID = 0;
    public java.util.Date dateEntered = null;
    public String text = null;
    public String firstName = null;
    public String lastName = null;
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
    * Creates a new empty Remarks object
    */
    public Remarks () {
        init();
    }


    /**
    * Get a list of Remarks from the DB
    *
    * @param myConn       Connection to the database
    * @param pr           purchase document to request remarks for
    */
    public static Remarks[] getItemList(DbConn myConn, String pr) {
        return getItemList(myConn, pr, null);
    }


    /**
    * Get a list of Remarks from the DB
    *
    * @param myConn       Connection to the database
    * @param pr           purchase document to request remarks for
    * @param searchText Text to search for
    */
    public static Remarks[] getItemList(DbConn myConn, String pr, String searchText) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        Remarks[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            sqlFrom = " FROM " + mySchemaPath + ".remarks r, " + mySchemaPath + ".users u ";
            //sqlWhere = " WHERE prnumber='" + pr + "' AND r.userid=u.id ";
            sqlWhere = " WHERE 1=1 ";
            sqlWhere += (pr != null) ? " AND r.prnumber='" + pr + "' " : "";
            sqlWhere += (searchText != null) ? " AND (LOWER(r.text) LIKE '%" + searchText.toLowerCase() + "%') " : "";
            sqlWhere += " AND r.userid=u.id ";
            sqlOrderBy = " ORDER BY r.dateentered DESC, u.username ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Remarks[returnSize];

            sqlcode = "SELECT r.prnumber, r.userid, r.dateentered, r.text, u.firstname, u.lastname " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new Remarks();
                item[i].prnumber = rs.getString(1);
                item[i].userID = rs.getInt(2);
                item[i].dateEntered = (java.util.Date) rs.getTimestamp(3);
                item[i].text = rs.getString(4);
                item[i].firstName = rs.getString(5);
                item[i].lastName = rs.getString(6);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Remarks - getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Remarks - getItemList");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }

    public java.util.Date getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(java.util.Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrnumber() {
        return prnumber;
    }

    public void setPrnumber(String prnumber) {
        this.prnumber = prnumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }





}
