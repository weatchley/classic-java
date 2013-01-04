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
* UintIssue is the class for unit of issue in the db
*
* @author   Bill Atchley
*/
public class UintIssue {
    public String unit = null;
    public String description = null;
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
    * Creates a new empty UintIssue object
    */
    public UintIssue () {
        unit = null;
        init();
    }


    /**
    * Creates an UintIssue object and uses the given id to populate it from the db
    *
    * @param unt     The unit of the UintIssue to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public UintIssue (String unt, DbConn myConn) {
        init();
        lookup(unt, myConn);
    }


    /**
    * Retrieves a UintIssue from the db and stores it in the current UintIssue object
    *
    * @param unt     The unit  to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void getInfo (String unt, DbConn myConn) {
        lookup(unt, myConn);
    }


    /**
    * Retrieves a UintIssue from the db and stores it in the current UintIssue object
    *
    * @param unt     The unit  to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void lookup (String unt, DbConn myConn) {
        unit = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT unit, description ";
            sqlcode += " FROM " + SCHEMAPATH + ".countries WHERE unit='" + unt +"'";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String myAbr = rs.getString(1);
            if (myAbr.equals(unt)) {
                unit = myAbr;
                description = rs.getString(2);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UintIssue lookup");
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

    public String getDescription() {
        return description;
    }

    public String getUnit() {
        return unit;
    }





}
