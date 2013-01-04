package gov.ymp.slts.model;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.slts.mssql.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;
import gov.ymp.slts.model.*;

/**
* Domain is the class for domains in the db
*
* @author   Bill Atchley
*/
public class Domain {
    private String domainName = null;
    private boolean include = false;
    private java.util.Date dateDiscovered = null;

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
    * Creates a new empty Domain object
    */
    public Domain () {
        init();
    }


    /**
    * Creates a Domain object and uses the given id to populate it from the db
    *
    * @param name     The name of the Domain to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public Domain (String name, DbConn myConn) {
        init();
        lookup(name, myConn);
    }


    /**
    * Retrieves a Domain from the db and stores it in the current Domain object
    *
    * @param name     The name to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void getInfo (String name, DbConn myConn) {
        lookup(name, myConn);
    }


    /**
    * Retrieves a Domain from the db and stores it in the current Domain object
    *
    * @param name     The name to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void lookup (String name, DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT domain_name, include, datediscovered ";
            sqlcode += "FROM " + SCHEMAPATH + ".domains WHERE domain_name='" + name + "' ";

//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String myName = rs.getString(1);
            if (myName.equals(name)) {
                domainName = myName;
                include = ((rs.getString(2).equals("T")) ? true : false);
                dateDiscovered = rs.getTimestamp(3);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Domain lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }

    public java.util.Date getDateDiscovered() {
        return dateDiscovered;
    }

    public void setDateDiscovered(java.util.Date dateDiscovered) {
        this.dateDiscovered = dateDiscovered;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public boolean isInclude() {
        return this.include;
    }

    public void setInclude(boolean include) {
        this.include = include;
    }




    /**
    * Save the current Domain to the DB
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

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".domains (domain_name, include, datediscovered) " +
                    "VALUES (?, '" + ((include) ? 'T' : 'F') + "', SYSDATE" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, domainName);
                //pstmt.setDate(++sqlID, (java.sql.Date) dateDiscovered);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".domains " +
                      "SET domain_name = ?, include = '" + ((include) ? 'T' : 'F') + "' WHERE domain_name= ?" +
                      "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, domainName);
                pstmt.setString(++sqlID, domainName);
                rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Domain save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Domain save");
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
    * Get a list of Domain from the DB
    *
    * @param myConn     Connection to the database
    */
    public static Domain[] getItemList(DbConn myConn) {
        return getItemList(myConn, false);
    }


    /**
    * Get a list of Domain from the DB
    *
    * @param myConn     Connection to the database
    */
    public static Domain[] getItemList(DbConn myConn, boolean includeOnly) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        Domain[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".domains ";
            sqlWhere = ((includeOnly) ? " WHERE include='T' " : "  ");
            sqlOrderBy = " ORDER BY domain_name ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Domain[returnSize];

            sqlcode = "SELECT domain_name, include, datediscovered " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new Domain();
                item[i].domainName = rs.getString(1);
                item[i].include = ((rs.getString(2).equals("T")) ? true : false);
                item[i].dateDiscovered = rs.getTimestamp(3);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Domain lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine);
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
    * Load domains from source DB
    *
    * @param myConn     Connection to the database
    */
    public static void load(DbConn myConn, DbConnM msConn) {
        //init();
        String outLine = "";
        String[] items = null;
        try {
            items = ComputerIn.getDomainList(msConn, myConn);

            for (int i=0; i<items.length; i++) {
                Domain test = new Domain (items[i], myConn);
                if (test.getDomainName() == null) {
                    Domain in = new Domain();
                    in.setDomainName(items[i]);
                    in.save(myConn);
                }
            }


        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine);
        }
        finally {

        }

    }






}
