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
* Classification is the class for Classifications in the db
*
* @author   Bill Atchley
*/
public class Classification {
    public int ID = 0;
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
    * Creates a new empty Classification object
    */
    public Classification () {
        ID = 0;
        init();
    }


    /**
    * Creates an Classification object and uses the given id to populate it from the db
    *
    * @param id     The id of the Classification to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public Classification (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a Classification from the db and stores it in the current Classification object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a Classification from the db and stores it in the current Classification object
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

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, name ";
            sqlcode += " FROM " + SCHEMAPATH + ".classifications WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            if (myID == id) {
                ID = myID;
                name = rs.getString(2);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Classification lookup");
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
    * Retrieves all Classifications from the db and stores it in a Classification array object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public Classification [] getList (DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        Classification [] item = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, name ";
            sqlcode += " FROM " + SCHEMAPATH + ".classifications ORDER BY name";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new Classification();
                item[i].ID = rs.getInt(1);
                item[i].name = rs.getString(2);
                i++;
            }
            rs.close();
            int myID = rs.getInt(1);
            if (myID == ID) {
                ID = myID;
                name = rs.getString(2);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Classification lookup");
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
