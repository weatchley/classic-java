package gov.ymp.mms.model;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.items.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
* Archive is the class for archive in the db
*
* @author   Bill Atchley
*/
public class Archive {
    private String prnumber = null;
    private Blob pdf = null;
    private java.util.Date dateArchived = null;
    private String description = null;
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
    * Creates a new empty Archive object
    */
    public Archive () {
        init();
    }


    /**
    * Creates an Archive object and uses the given id to populate it from the db
    *
    * @param pr     The id of the Archive to lookup from the db
    * @param ldate  lookup date
    * @param myConn Connection to the database
    */
    public Archive (String pr, java.util.Date ldate, DbConn myConn) {
        init();
        lookup(pr, ldate, myConn);
    }


    /**
    * Retrieves a Archive from the db and stores it in the current Archive object
    *
    * @param pr     The id of the Archive to lookup from the db
    * @param ldate  lookup date
    * @param myConn Connection to the database
    */
    public void getInfo (String pr, java.util.Date ldate, DbConn myConn) {
        lookup(pr, ldate, myConn);
    }


    /**
    * Retrieves a Archive from the db and stores it in the current Archive object
    *
    * @param pr     The id of the Archive to lookup from the db
    * @param ldate  lookup date
    * @param myConn Connection to the database
    */
    public void lookup (String pr, java.util.Date ldate, DbConn myConn) {

        //
        String outLine = "";
        ResultSet rs = null;
        ResultSet rs2 = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            String dateLookup = Utils.dateToString(ldate, "MM/dd/yyyy HH:mm:ss");

            String sqlcode = "SELECT prnumber, pdf, datearchived, description FROM " + SCHEMAPATH + ".archive WHERE prnumber='" + pr +
                  "' AND datearchived=TO_DATE('" + dateLookup + "', 'MM/DD/YYYY HH24:MI:SS')";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String myPR = rs.getString(1);

            if (myPR.equals(pr)) {
                prnumber = myPR;
                pdf = rs.getBlob(2);
                dateArchived = (java.util.Date) rs.getTimestamp(3);
                description = rs.getString(4);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"mms",0, outLine + " - Archive lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"mms",0, outLine + " - Archive lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); rs2.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get the image from the DB
    *
    * @param myConn     Connection to the database
    */
    public byte[] getImageBytes(DbConn myConn) {
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        String sqlcode = "";
        byte [] bytes = null;
        try {

            Connection conn = myConn.conn;


            // Create a Statement
            stmt = conn.createStatement ();
            conn.setAutoCommit(false);
            String dateLookup = Utils.dateToString(dateArchived, "MM/dd/yyyy HH:mm:ss");
            sqlcode = "SELECT pdf FROM " + SCHEMAPATH + ".archive WHERE prnumber='" + prnumber +
                  "' AND datearchived=TO_DATE('" + dateLookup + "', 'MM/DD/YYYY HH24:MI:SS') FOR UPDATE";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            pdf = rs.getBlob(1);
//System.out.println("Archive - getImageBytes - Got Here 1, data.length: " + data.length());
            bytes = pdf.getBytes((long) 1, (int) pdf.length());
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"mms",0, outLine + " - Archive image bytes retrieve");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"mms",0, outLine + " - Archive image bytes retrieve");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
        return(bytes);
    }






    /**
    * Get a list of Archive from the DB
    *
    * @param myConn     Connection to the database
    * @param pr         purchase document to look up
    */
    public static Archive[] getItemList(DbConn myConn, String pr) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        Archive[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            sqlFrom = " FROM " + mySchemaPath + ".archive  ";
            sqlWhere = " WHERE prnumber='" + pr + "' ";
            sqlOrderBy = " ORDER BY datearchived DESC ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Archive[returnSize];

            sqlcode = "SELECT prnumber, pdf, datearchived, description " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new Archive();
                item[i].prnumber = rs.getString(1);
                item[i].pdf = rs.getBlob(2);
                item[i].dateArchived = (java.util.Date) rs.getTimestamp(3);
                item[i].description = rs.getString(4);

                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"mms",0, outLine + " - getItemList lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"mms",0, outLine + " - getItemList lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }

    public java.util.Date getDateArchived() {
        return dateArchived;
    }

    public void setDateArchived(java.util.Date dateArchived) {
        this.dateArchived = dateArchived;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrnumber() {
        return prnumber;
    }

    public void setPrnumber(String prnumber) {
        this.prnumber = prnumber;
    }





}
