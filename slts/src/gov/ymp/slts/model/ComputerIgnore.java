package gov.ymp.slts.model;

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
* ComputerIgnore is the class for apps to ignore for an inventory in the db
*
* @author   Bill Atchley
*/
public class ComputerIgnore {
    private int computerID = 0;
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
        SCHEMA = "slts";
        SCHEMAPATH = "slts";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty ComputerIgnore object
    */
    public ComputerIgnore () {
        //
        init();
    }

    public int getComputerID() {
        return computerID;
    }

    public void setComputerID(int val) {
        this.computerID = val;
    }



    /**
    * Save the current ComputerIgnore to the DB
    *
    * @param myConn     Connection to the database
    */
    public void save(DbConn myConn) {
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

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".computer_ignore (computer_id) " +
                    "VALUES (" + computerID +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
            //    sqlcode = "begin UPDATE " + SCHEMAPATH + ".domains " +
            //          "SET domain_name = ?, include = '" + ((include) ? 'T' : 'F') + "', datediscovered = ? WHERE domain_name= ?" +
            //          "; end;";
//System.out.println(sqlcode);
            //    pstmt = conn.prepareStatement(sqlcode);
            //    int sqlID = 0;
            //    pstmt.setString(++sqlID, domainName);
            //    pstmt.setDate(++sqlID, (java.sql.Date) dateDiscovered);
            //    pstmt.setString(++sqlID, domainName);
            //    rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ComputerIgnore save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ComputerIgnore save");
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
    * Empty ComputerIgnore to the DB
    *
    * @param myConn     Connection to the database
    */
    public static void empty(DbConn myConn) {
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            Connection conn = myConn.conn;
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            // Create a Statement
            pstmt = conn.prepareStatement(
                "DELETE FROM " + mySchemaPath + ".computer_ignore"
                );
            rows = pstmt.executeUpdate();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ComputerIgnore empty");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ComputerIgnore empty");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
            if (pstmt != null)
                try { pstmt.close(); } catch (Exception i) {}
        }

    }




    /**
    * Get a list of ComputerIgnore from the DB
    *
    * @param myConn     Connection to the database
    */
    public static ComputerIgnore[] getItemList(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        ComputerIgnore[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".computer_ignore ";
            sqlWhere = " ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new ComputerIgnore[returnSize];

            sqlcode = "SELECT computer_id " + sqlFrom + sqlWhere  + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new ComputerIgnore();
                item[i].computerID = rs.getInt(1);
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ComputerIgnore lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ComputerIgnore lookup");
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
    * Get a hash of ignored computers from the DB
    *
    * @param myConn     Connection to the database
    */
    public static HashMap getItemHash(DbConn myConn) {
        ComputerIgnore[] items = getItemList(myConn);
        HashMap hm = new HashMap((items.length + 1));
        for (int i=0; i<items.length; i++) {
            Computer tmp = new Computer(items[i].computerID, myConn);
            hm.put(new Integer(tmp.getComputerID()).toString(), tmp.getName());
        }
        return hm;
    }



}
