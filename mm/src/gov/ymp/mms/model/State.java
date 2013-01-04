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
* State is the class for states in the db
*
* @author   Bill Atchley
*/
public class State {
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
    * Creates a new empty State object
    */
    public State () {
        abbreviation = null;
        init();
    }


    /**
    * Creates an State object and uses the given id to populate it from the db
    *
    * @param abr     The abbreviation of the State to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public State (String abr, DbConn myConn) {
        init();
        lookup(abr, myConn);
    }


    /**
    * Retrieves a State from the db and stores it in the current State object
    *
    * @param abr     The abbreviation  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (String abr, DbConn myConn) {
        lookup(abr, myConn);
    }


    /**
    * Retrieves a State from the db and stores it in the current State object
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
            sqlcode += " FROM " + SCHEMAPATH + ".states WHERE abbreviation='" + abr +"'";
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
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - State lookup");
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
