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
import org.apache.commons.lang.StringUtils;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
* ServiceInventory is the class for inventoried serivces in the db
*
* @author   Bill Atchley
*/
public class ServiceInventory {
    private int computerID = 0;
    private String name = null;
    private String displayName = null;
    private String description = null;
    private String listName = null;
    private String computerName = null;
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
    * Creates a new empty ServiceInventory object
    */
    public ServiceInventory () {
        //
        init();
    }


    /**
    * Creates a ServiceInventory object and uses the given id to populate it from the db
    *
    * @param id     The computer id of the ServiceInventory to lookup from the db (int)
    * @param nam     The name of the ServiceInventory to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public ServiceInventory (int id, String nam, DbConn myConn) {
        init();
        lookup(id, name, myConn);
    }


    /**
    * Retrieves a ServiceInventory from the db and stores it in the current ServiceInventory object
    *
    * @param id     The computer id  to lookup from the db (int)
    * @param nam     The name of the ServiceInventory to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, String nam, DbConn myConn) {
        lookup(id, name, myConn);
    }


    /**
    * Retrieves a ServiceInventory from the db and stores it in the current ServiceInventory object
    *
    * @param id     The computer id to lookup from the db (int)
    * @param nam     The name of the ServiceInventory to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, String nam, DbConn myConn) {
        //
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT computer_id, name, display_name, description, listname FROM " + SCHEMAPATH + ".service_inventory WHERE computer_id=" + id + " AND name='" + nam + "'";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            String myName = rs.getString(2);

            if (myID == id && myName.equals(nam)) {
                computerID = myID;
                name = myName;
                displayName = rs.getString(3);
                description = rs.getString(4);
                listName = rs.getString(5);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ServiceInventory lookup");
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

    public int getComputerID() {
        return computerID;
    }

    public void setComputerID(int computerID) {
        this.computerID = computerID;
    }

    public String getComputerName() {
        return computerName;
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

    public String getListName() {
        return listName;
    }



    /**
    * Save the current ServiceInventory to the DB
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

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".service_inventory (computer_id, name, display_name, description) " +
                    "VALUES (" + computerID + ", ?, ?, ?" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, name);
                pstmt.setString(++sqlID, displayName);
                pstmt.setString(++sqlID, description);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
            //    sqlcode = "begin UPDATE " + SCHEMAPATH + ".service_inventory " +
            //          "SET computer_id = " + computerID + ", name = ?, display_name = ?, description = ? WHERE domain_name= ?" +
            //          "; end;";
//System.out.println(sqlcode);
            //    pstmt = conn.prepareStatement(sqlcode);
            //    int sqlID = 0;
            //    pstmt.setString(++sqlID, name);
            //    pstmt.setString(++sqlID, displayName);
            //    pstmt.setString(++sqlID, description);
            //    rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ServiceInventory save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ServiceInventory save");
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
    * Empty  ServiceInventory to the DB
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

            // Create a Statement
            String mySchemaPath = "slts";
            pstmt = conn.prepareStatement(
                "DELETE FROM " + mySchemaPath + ".service_inventory"
                );
            rows = pstmt.executeUpdate();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ServiceInventory empty");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ServiceInventory empty");
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
    * Get a list of ServiceInventory from the DB
    *
    * @param myConn     Connection to the database
    */
    public static ServiceInventory[] getItemList(DbConn myConn) {
        return getItemList(myConn, 0, null, false, null, false);
    }


    /**
    * Get a list of ServiceInventory from the DB
    *
    * @param myConn     Connection to the database
    * @param compID     Computer to select (0 for all)
    */
    public static ServiceInventory[] getItemList(DbConn myConn, int compID) {
        return getItemList(myConn, compID, null, false, null, false);
    }


    /**
    * Get a list of ServiceInventory from the DB
    *
    * @param myConn     Connection to the database
    * @param compID     Computer to select (0 for all)
    * @param filtered   Filter out services flagged to ignore
    */
    public static ServiceInventory[] getItemList(DbConn myConn, int compID, boolean filtered) {
        return getItemList(myConn, compID, null, filtered, null, false);
    }


    /**
    * Get a list of ServiceInventory from the DB
    *
    * @param myConn     Connection to the database
    * @param compID     Computer to select (0 for all)
    * @param filtered   Filter out services flagged to ignore
    * @param orderBy    SQL Order By clause
    */
    public static ServiceInventory[] getItemList(DbConn myConn, int compID, boolean filtered, String orderBy) {
        return getItemList(myConn, compID, null, filtered, null, false);
    }


    /**
    * Get a list of ServiceInventory from the DB
    *
    * @param myConn     Connection to the database
    * @param compID     Computer to select (0 for all)
    * @param ln         List name to select (null for all)
    * @param filtered   Filter out services flagged to ignore
    * @param orderBy    SQL Order By clause
    */
    public static ServiceInventory[] getItemList(DbConn myConn, int compID, String ln, boolean filtered, String orderBy) {
        return getItemList(myConn, compID, ln, filtered, orderBy, false);
    }


    /**
    * Get a list of ServiceInventory from the DB
    *
    * @param myConn     Connection to the database
    * @param compID     Computer to select (0 for all)
    * @param ln         List name to select (null for all)
    * @param filtered   Filter out services flaged to ignore
    * @param orderBy    SQL Order By clause
    * @param notMatched Filter for only apps not matched to a product
    */
    public static ServiceInventory[] getItemList(DbConn myConn, int compID, String ln, boolean filtered, String orderBy, boolean notMatched) {
        return getItemList(myConn, compID, ln, filtered, orderBy, notMatched, null);
    }


    /**
    * Get a list of ServiceInventory from the DB
    *
    * @param myConn     Connection to the database
    * @param compID     Computer to select (0 for all)
    * @param ln         List name to select (null for all)
    * @param filtered   Filter out services flaged to ignore
    * @param orderBy    SQL Order By clause
    * @param notMatched Filter for only apps not matched to a product
    * @param searchText Text to search for
    */
    public static ServiceInventory[] getItemList(DbConn myConn, int compID, String ln, boolean filtered, String orderBy, boolean notMatched, String searchText) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        ServiceInventory[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".service_inventory si, " + mySchemaPath + ".computer c ";
            sqlWhere = " WHERE si.computer_id=c.computer_id ";
            sqlWhere += " AND si.computer_id NOT IN (SELECT computer_id FROM " + mySchemaPath + ".computer_ignore) ";
            sqlWhere += (compID != 0) ? ("AND si.computer_id = " + compID + " ") : "";
            sqlWhere += (filtered) ? "AND si.name NOT IN (SELECT listname FROM " + mySchemaPath + ".service_ignore) " : "";
            sqlWhere += (ln != null) ? "AND si.listname='" + ln + "' " : "";
            sqlWhere += (notMatched) ? "AND si.listname NOT IN (SELECT listname FROM " + mySchemaPath + ".product_serv_inv_match) " : "";
            sqlWhere += (searchText != null) ? " AND (LOWER(si.name) LIKE '%" + searchText.toLowerCase() + "%' OR LOWER(si.description) LIKE '%" + searchText.toLowerCase() + "%' " +
                  "OR LOWER(si.display_name) LIKE '%" + searchText.toLowerCase() + "%') " : "";
            sqlOrderBy = " ORDER BY " + ((orderBy != null) ? orderBy : "si.computer_id, si.listname ");

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new ServiceInventory[returnSize];

            sqlcode = "SELECT si.computer_id, si.name, si.display_name, si.description, si.listname, c.name " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new ServiceInventory();
                item[i].computerID = rs.getInt(1);
                item[i].name = rs.getString(2);
                item[i].displayName = rs.getString(3);
                item[i].description = rs.getString(4);
                item[i].listName = rs.getString(5);
                item[i].computerName = rs.getString(6);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ServiceInventory getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ServiceInventory getItemList");
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
    * Get a list of distinct application names from the DB
    *
    * @param myConn     Connection to the database
    */
    public static String[] getServiceNameList(DbConn myConn) {
        return getServiceNameList(myConn, false, false);
    }


    /**
    * Get a list of distinct application names from the DB
    *
    * @param myConn       Connection to the database
    * @param filtered     Filter out services flagged to ignore
    */
    public static String[] getServiceNameList(DbConn myConn, boolean filtered) {
        return getServiceNameList(myConn, filtered, false);
    }


    /**
    * Get a list of distinct service names from the DB
    *
    * @param myConn       Connection to the database
    * @param filtered     Filter out services flagged to ignore
    * @param skipMatched  Filter out services that have been matched to a product
    */
    public static String[] getServiceNameList(DbConn myConn, boolean filtered, boolean skipMatched) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        String[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".service_inventory ";
            sqlWhere = " WHERE 1=1 ";
            sqlWhere += " AND computer_id NOT IN (SELECT computer_id FROM " + mySchemaPath + ".computer_ignore) ";
            sqlWhere += (filtered) ? "AND listname NOT IN (SELECT listname FROM " + mySchemaPath + ".service_ignore) " : "";
            sqlWhere += (skipMatched) ? "AND listname NOT IN (SELECT listname FROM " + mySchemaPath + ".product_serv_inv_match) " : "";
            sqlOrderBy = " ORDER BY listname";

            sqlcode = "SELECT count(distinct listname) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new String [returnSize];

            sqlcode = "SELECT distinct listname " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = rs.getString(1);
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - getServiceNameList lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - getServiceNameList lookup");
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
    * Get a hash of distinct service names from the DB
    *
    * @param myConn       Connection to the database
    * @param filtered     Filter out services flagged to ignore
    */
    public static HashMap getServiceNameHash(DbConn myConn, boolean filtered) {
        return getServiceNameHash(myConn, filtered, false);
    }


    /**
    * Get a hash of distinct service names from the DB
    *
    * @param myConn       Connection to the database
    * @param filtered     Filter out services flagged to ignore
    * @param skipMatched  Filter out services that have been matched to a product
    */
    public static HashMap getServiceNameHash(DbConn myConn, boolean filtered, boolean skipMatched) {
        String[] items = getServiceNameList(myConn, filtered, skipMatched);
        HashMap hm = new HashMap((items.length + 1));
        for (int i=0; i<items.length; i++) {
            hm.put(items[i], items[i]);
        }
        return hm;
    }


    /**
    * Load application inventory from source DB
    *
    * @param myConn     Connection to the database
    */
    public static void load(DbConn myConn, DbConnM msConn) {
        //init();
        String outLine = "";
        ServicesIn [] items = null;
        PreparedStatement pstmt = null;
        String sqlcode = "";
        try {
//System.out.println("ServiceInventory:load-Got Here 1");
            items = ServicesIn.getItemList(msConn, myConn);
//System.out.println("ServiceInventory:load-Got Here 2");

            String mySchemaPath = "slts";
            sqlcode = "begin INSERT INTO " + mySchemaPath + ".service_inventory (computer_id, name, display_name, description, listname) " +
                    "VALUES (?, ?, ?, ?, ?" +
                    "); end;";
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);

            for (int i=0; i<items.length; i++) {
                //ServiceInventory in = new ServiceInventory();
                //in.setComputerID(items[i].getComputerID());
                //in.setName(((items[i].getName() != null) ? items[i].getName() : null));
                //in.setDisplayName(((items[i].getDisplayName() != null) ? items[i].getDisplayName() : null));
                //in.setDescription(((items[i].getDescription() != null) ? items[i].getDescription() : null));
                //in.save(myConn);

                int sqlID = 0;
                pstmt.setInt(++sqlID, items[i].getComputerID());
                pstmt.setString(++sqlID, ((items[i].getName() != null) ? items[i].getName() : null));
                pstmt.setString(++sqlID, ((items[i].getDisplayName() != null) ? items[i].getDisplayName() : null));
                pstmt.setString(++sqlID, ((items[i].getDescription() != null) ? items[i].getDescription() : null));
                pstmt.setString(++sqlID, ((StringUtils.isNotBlank(items[i].getDisplayName()) && !StringUtils.startsWith(items[i].getDisplayName(), "##")) ? items[i].getDisplayName() : items[i].getName()));
                int rows = pstmt.executeUpdate();

            }
//System.out.println("ServiceInventory:load-Got Here 3");
            myConn.conn.commit();
//System.out.println("ServiceInventory:load-Got Here 4");


        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " In ServiceInventory load");
        }
        finally {

        }

    }








}
