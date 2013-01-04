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
* Country is the class for countries in the db
*
* @author   Bill Atchley
*/
public class Country {
    public String abbreviation = null;
    public String name = null;
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
    * Creates a new empty Country object
    */
    public Country () {
        abbreviation = null;
        init();
    }


    /**
    * Creates an Country object and uses the given id to populate it from the db
    *
    * @param abr     The abbreviation of the Country to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public Country (String abr, DbConn myConn) {
        init();
        lookup(abr, myConn);
    }


    /**
    * Retrieves a Country from the db and stores it in the current Country object
    *
    * @param abr     The abbreviation  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (String abr, DbConn myConn) {
        lookup(abr, myConn);
    }


    /**
    * Retrieves a Country from the db and stores it in the current Country object
    *
    * @param abr     The abbreviation  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (String abr, DbConn myConn) {
        abbreviation = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT abbreviation, name ";
            sqlcode += " FROM " + SCHEMAPATH + ".countries WHERE abbreviation='" + abr +"'";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String myAbr = rs.getString(1);
            if (myAbr.equals(abr)) {
                abbreviation = myAbr;
                name = rs.getString(2);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Country lookup");
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


    public String getAbbreviation() {
        return abbreviation;
    }

    public String getName() {
        return name;
    }





}
