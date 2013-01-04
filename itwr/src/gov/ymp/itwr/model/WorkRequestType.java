package gov.ymp.itwr.model;

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
* Archive is the class for archived reports in the db
*
* @author   Bill Atchley
*/
public class WorkRequestType {
    private int ID = 0;
    private int sort = 0;
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
        SCHEMA = "itwr";
        SCHEMAPATH = "itwr";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty WorkRequest object
    */
    public WorkRequestType () {
        ID = 0;
        init();
    }


    /**
    * Retrieve the id for the current WorkRequestType
    *
    * @return id    The id for the current WorkRequesttype
    */
    public int getID() {
        int id = ID;
        return(id);
    }


    /**
    * Retrieve the description for the current WorkRequesttype
    *
    * @return description    The description for the current WorkRequestType
    */
    public String getDescription() {
        return(description);
    }


    /**
    * Get a list of WorkRequestType from the DB
    *
    * @param myConn     Connection to the database
    */
    public static WorkRequestType[] getItemList(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        WorkRequestType[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "itwr";

            //sqlFrom = " FROM " + SCHEMAPATH + ".work_request ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".work_request ";
            sqlFrom = " FROM " + mySchemaPath + ".work_request_type ";

            sqlcode = "SELECT count(*) " + sqlFrom;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new WorkRequestType[returnSize];

            sqlcode = "SELECT id, description " + sqlFrom + " ORDER BY sort ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new WorkRequestType();
                item[i].ID = rs.getInt(1);
                item[i].description = rs.getString(2);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - WorkRequestType list lookup");
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
    * Get a HashMap of Work Request types from the DB
    *
    * @param myConn     Connection to the database
    */
    public static HashMap getItemHash(DbConn myConn) {
        String outLine = "";
        HashMap hm = new HashMap();
        String sqlcode = "";
        ResultSet rs = null;
        Statement stmt = null;

        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "itwr";
            sqlcode = "SELECT id, description FROM " + mySchemaPath + ".work_request_type";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            while (rs.next()) {
                hm.put(new Integer(rs.getInt(1)), rs.getString(2));
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - WorkRequest type hash lookup");
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

        return hm;
    }



}
