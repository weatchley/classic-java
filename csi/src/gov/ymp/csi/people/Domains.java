package gov.ymp.csi.people;

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
* Domain is the class for domains in the db
*
* @author   Bill Atchley
*/
public class Domains {
    private int ID = 0;
    private String name = null;
    private String description = null;
    private boolean local = false;
    private String server = null;
    private String code = null;

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
        SCHEMA = "csi";
        SCHEMAPATH = "csi";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty Domains object
    */
    public Domains () {
        init();
    }


    /**
    * Creates a Domains object and uses the given id to populate it from the db
    *
    * @param id     The id of the Domains to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public Domains (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Creates a Domains object and uses the given id to populate it from the db
    *
    * @param dname   The name of the Domains to lookup from the db
    * @param myConn Connection to the database
    */
    public Domains (String dname, DbConn myConn) {
        init();
        lookup(dname, myConn);
    }


    /**
    * Retrieves a Domains from the db and stores it in the current Domains object
    *
    * @param id     The id to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, null, myConn);
    }


    /**
    * Retrieves a Domains from the db and stores it in the current Domains object
    *
    * @param dname   The name to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void getInfo (String dname, DbConn myConn) {
        lookup(0, name, myConn);
    }


    /**
    * Retrieves a Domains from the db and stores it in the current Domains object
    *
    * @param id     The id to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void lookup (int id, DbConn myConn) {
        lookup(id, null, myConn);
    }


    /**
    * Retrieves a Domains from the db and stores it in the current Domains object
    *
    * @param dname   The name to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void lookup (String dname, DbConn myConn) {
        lookup(0, dname, myConn);
    }


    /**
    * Retrieves a Domains from the db and stores it in the current Domains object
    *
    * @param id     The id to lookup from the db (String)
    * @param dname   The name to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void lookup (int id, String dname, DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        String sqlWhere = "";
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            if (id != 0) {
                sqlWhere = "id=" + id + " ";
            } else if (dname != null) {
                sqlWhere = "LOWER(name)='" + dname.toLowerCase() + "' ";
            } else {
                sqlWhere = "1==1";
            }

            String sqlcode = "SELECT id, name, description, islocal, server, code ";
            sqlcode += "FROM " + SCHEMAPATH + ".domains WHERE " + sqlWhere;

//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            //rs.next();
            //int myID = rs.getInt(1);
            //if (myID == id) {
            if (rs.next()) {
                //ID = myID;
                ID = rs.getInt(1);
                name = rs.getString(2);
                description = rs.getString(3);
                local = (rs.getString(4).equals("T")) ? true : false;
                server = rs.getString(5);
                code = rs.getString(6);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage() + " - Domains lookup";
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"csi",0, outLine);
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage() + " - Domains lookup";
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"csi",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName (String val) {
        name = val;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription (String val) {
        description = val;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean val) {
        local = val;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String val) {
        server = val;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String val) {
        code = val;
    }



    /**
    * Save the current Domains to the DB
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
                sqlcode = "SELECT " + SCHEMAPATH + ".DOMAINS_ID_SEQ.NEXTVAL FROM dual";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID;

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".domains (id, name, description, islocal, server, code) " +
                    "VALUES (" + ID + ", ?, ?, '" + ((local) ? 'T' : 'F') + "', ?, ?" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, name);
                pstmt.setString(++sqlID, description);
                pstmt.setString(++sqlID, server);
                pstmt.setString(++sqlID, code);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".domains " +
                      "Set name=?, description=?, islocal='" + ((local) ? 'T' : 'F') + "', server=?, code=? WHERE id = " + ID +
                      "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, name);
                pstmt.setString(++sqlID, description);
                pstmt.setString(++sqlID, server);
                pstmt.setString(++sqlID, code);
                rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage() + " - Domains save";
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"csi",0, outLine);
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage() + " - Domains save";
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"csi",0, outLine);
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
    * Get a list of Domains from the DB
    *
    * @param myConn     Connection to the database
    */
    public static Domains[] getItemList(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        Domains[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "csi";

            sqlFrom = " FROM " + mySchemaPath + ".domains ";
            sqlWhere = "";
            sqlOrderBy = " ORDER BY description ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Domains[returnSize];

            sqlcode = "SELECT id, name, description, islocal, server, code " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new Domains();
                item[i].ID = rs.getInt(1);
                item[i].name = rs.getString(2);
                item[i].description = rs.getString(3);
                item[i].local = (rs.getString(4).equals("T")) ? true : false;
                item[i].server = rs.getString(5);
                item[i].code = rs.getString(6);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage() + " - Domains getItemList";
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"csi",0, outLine);
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage() + " - Domains getItemList";
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"csi",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }






}
