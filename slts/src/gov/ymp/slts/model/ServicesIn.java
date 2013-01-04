package gov.ymp.slts.model;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.slts.mssql.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.items.*;
import gov.ymp.slts.model.*;
//import oracle.jdbc.driver.*;
//import oracle.sql.*;

/**
* ServicesIn is the class for inventoried serivces in the db
*
* @author   Bill Atchley
*/
public class ServicesIn {
    private int computerID = 0;
    private String name = null;
    private String displayName = null;
    private String description = null;
    public boolean isNew = true;

    public static String SCHEMA = null;
    public static String SCHEMAPATH = null;
    public static String DBTYPE = null;

    /**
    * init new object
    */
    private void init () {
        DbConnM tempDB = new DbConnM("dummy");
        //SCHEMA = tempDB.getSchemaName();
        //SCHEMAPATH = tempDB.getSchemaPath();
        SCHEMA = "servicealtirisdbreader";
        SCHEMAPATH = "eXpress.dbo";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty ServicesIn object
    */
    public ServicesIn () {
        //
        init();
    }


    /**
    * Creates a ServicesIn object and uses the given id to populate it from the db
    *
    * @param id     The computer id of the ServicesIn to lookup from the db (int)
    * @param nam     The name of the ServicesIn to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public ServicesIn (int id, String nam, DbConnM myConn, DbConn myConnO) {
        init();
        lookup(id, name, myConn, myConnO);
    }


    /**
    * Retrieves a ServicesIn from the db and stores it in the current ServicesIn object
    *
    * @param id     The computer id  to lookup from the db (int)
    * @param nam     The name of the ServicesIn to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, String nam, DbConnM myConn, DbConn myConnO) {
        lookup(id, name, myConn, myConnO);
    }


    /**
    * Retrieves a ServicesIn from the db and stores it in the current ServicesIn object
    *
    * @param id     The computer id to lookup from the db (int)
    * @param nam     The name of the ServicesIn to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, String nam, DbConnM myConn, DbConn myConnO) {
        //
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT computer_id, name, display_name, description FROM " + SCHEMAPATH + ".service WHERE computer_id=" + id + " AND name='" + nam + "'";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            String myName = rs.getString(2);

            if (myID == id && myName.equals(nam)) {
                computerID = myID;
                name = myName;
                displayName = ((rs.getString(3) != null) ? rs.getString(3) : null);
                description = ((rs.getString(4) != null) ? rs.getString(4) : null);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConnO, 0,"slts",0, outLine + " - ServicesIn lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConnO, 0,"slts",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }

    public int getComputerID() {
        return computerID;
    }

    public void setComputerID(int computerID) {
        this.computerID = computerID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    /**
    * Get a list of ServicesIn from the DB
    *
    * @param myConn     Connection to the database
    */
    public static ServicesIn[] getItemList(DbConnM myConn, DbConn myConnO) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        ServicesIn[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "eXpress.dbo";

            String domainNameList = "";
            Domain [] dn = Domain.getItemList(myConnO, true);
            for (int i=0; i<dn.length; i++) {
                if (i>0) { domainNameList += ", "; }
                domainNameList += "'" + dn[i].getDomainName() + "'";
            }

            sqlFrom = " FROM " + mySchemaPath + ".service ";
            sqlWhere = " WHERE computer_id IN (SELECT computer_id FROM " + mySchemaPath + ".computer WHERE domain_name IN (" + domainNameList + ")) ";
            sqlOrderBy = "ORDER BY computer_id, name ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new ServicesIn[returnSize];

            sqlcode = "SELECT computer_id, name, display_name, description " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new ServicesIn();
                item[i].computerID = rs.getInt(1);
                item[i].name = rs.getString(2);
                item[i].displayName = ((rs.getString(3) == null) ? null : rs.getString(3));
                item[i].description = ((rs.getString(4) == null) ? null : rs.getString(4));
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConnO, 0,"slts",0, outLine + " - ServicesIn lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConnO, 0,"slts",0, outLine);
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
