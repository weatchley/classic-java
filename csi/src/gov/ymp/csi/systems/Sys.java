package gov.ymp.csi.systems;

// Support classes
import java.io.*;
import java.util.*;
//import java.util.Date.*;
import java.sql.*;
import gov.ymp.csi.db.*;
//import gov.ymp.csi.misc.*;
//import oracle.jdbc.*;

/**
* System is the class for managing system information in the CSI
*
* @author   Bill Atchley
*/
public class Sys {
    //public static String SCHEMA = "csi";
    private static String SCHEMA = null;
    private static String SCHEMAPATH = null;
    private static String DBTYPE = null;
    public long id = 0;
    public String acronym = null;
    public String description = null;
    public boolean globalRead = false;
    public boolean externalSYS = false;


    /**
    * init new object
    */
    private void init () {
        DbConn tempDB = new DbConn("dummy");
        SCHEMA = tempDB.getSchemaName();
        SCHEMAPATH = tempDB.getSchemaPath();
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new Sys object
    *
    */
    public Sys () {
        init();

    }


    /**
    * Creates a new Sys object
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param sysid      ID of system to retrieve
    */
    public Sys (DbConn myConn, long sysid) {
        init();
        lookup(myConn, sysid);
    }


    /**
    * Retrieves a system from the db (given a passed in connection) and stores it in the current Sys object
    * Stub - Not yet fully functional
    *
    * @param id     The id of the System to lookup from the db
    * @param conn   A connection to the DB
    */
    public void lookup (DbConn myConn, long myID) {
        id = 0;
        description = null;
        globalRead = false;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = myConn.conn;
        try {

            // Create a Statement
            stmt = conn.createStatement ();
            String sqlcode = "";
            sqlcode = "SELECT NVL(id, 0), NVL(acronym, 'n/a'), NVL(description,'n/a'),globalread,externalsys FROM " + SCHEMAPATH + ".systems WHERE id=" + myID;
            //sqlcode = "SELECT ISNULL(id, 0), ISNULL(acronym, 'n/a'), ISNULL(description,'n/a'),globalread,externalsys FROM " + SCHEMAPATH + ".systems WHERE id=" + myID
//System.out.println(sqlcode);

            rs = stmt.executeQuery(sqlcode);
            rs.next();
            long testID = rs.getLong(1);
            String myAcronym = rs.getString(2);
            String myDescription = rs.getString(3);
            String myGlobalRead = rs.getString(4);
            String myExternalSYS = rs.getString(5);
            if (testID == myID) {
                id = testID;
                acronym = myAcronym;
                description = myDescription;
                globalRead = (myGlobalRead.equals("T")) ? true : false;;
                externalSYS = (myExternalSYS.equals("T")) ? true : false;;
            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"CSI",0, outLine);
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"CSI",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }



    /**
    * Method to retrieve a set of Syss
    *
    */
    public static Sys[] getSystems(DbConn myConn) {
        String sqlcode = "";
        String sqlWhere = "";
        String sqlFrom = "";
        int returnSize = 0;
        ResultSet rset = null;
        Statement stmt = null;
        Sys[] sys = null;
        try {
            stmt = myConn.conn.createStatement();

            sqlWhere = "1=1";

            sqlFrom = myConn.getSchemaPath() + ".systems ";

            sqlcode = "SELECT count(*) FROM " + sqlFrom + " WHERE " + sqlWhere;
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);
            rset.next();
            returnSize = rset.getInt(1);
            rset.close();

            sqlcode = "SELECT id, acronym, description, globalread, externalsys " +
                "FROM " + sqlFrom + "WHERE " + sqlWhere + " ORDER BY description";
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);

            sys = new Sys[returnSize];
            int i = 0;
            while (rset.next()) {
                sys[i] = new Sys();
                sys[i].id = rset.getLong(1);
                sys[i].acronym = rset.getString(2);
                sys[i].description = rset.getString(3);
                String temp = rset.getString(4);
                sys[i].globalRead = (temp.equals("T")) ? true : false;
                temp = rset.getString(5);
                sys[i].externalSYS = (temp.equals("T")) ? true : false;
                i++;
            }
            rset.close();

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }
        finally {
            if (rset != null)
                try { rset.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return sys;
    }


    /**
    * Retrieve the id for the current system
    *
    * @return      The ID for the current system
    */
    public long getID() {
        return(id);
    }


    /**
    * Retrieve the acronym for the current system
    *
    * @return      The acronym for the current system
    */
    public String getAcronym() {
        return(acronym);
    }


    /**
    * Retrieve the description for the current system
    *
    * @return      The description for the current system
    */
    public String getDescription() {
        return(description);
    }


    /**
    * Retrieve the globalRead for the current system
    *
    * @return      The globalRead for the current system
    */
    public boolean getGlobalRead() {
        return(globalRead);
    }


    /**
    * Retrieve the externalSYS for the current system
    *
    * @return      The externalSYS for the current system
    */
    public boolean getExternalSYS() {
        return(externalSYS);
    }



}
