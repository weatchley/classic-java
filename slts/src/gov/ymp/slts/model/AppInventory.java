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
* AppInventory is the class for inventoried applications in the db
*
* @author   Bill Atchley
*/
public class AppInventory {
    private int computerID = 0;
    private String name = null;
    private String description = null;
    private String publisher = null;
    private String version = null;
    private String prodID = null;
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
    * Creates a new empty AppInventory object
    */
    public AppInventory () {
        computerID = 0;
        init();
    }


    /**
    * Creates a AppInventory object and uses the given id to populate it from the db
    *
    * @param id     The computer id of the AppInventory to lookup from the db (int)
    * @param nam     The name of the AppInventory to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public AppInventory (int id, String nam, DbConn myConn) {
        init();
        lookup(id, nam, myConn);
    }


    /**
    * Retrieves a AppInventory from the db and stores it in the current AppInventory object
    *
    * @param id     The computer id  to lookup from the db (int)
    * @param nam     The name of the AppInventory to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, String nam, DbConn myConn) {
        lookup(id, nam, myConn);
    }


    /**
    * Retrieves a AppInventory from the db and stores it in the current AppInventory object
    *
    * @param id     The computer id to lookup from the db (int)
    * @param nam     The name of the AppInventory to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, String nam, DbConn myConn) {
        computerID = 0;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT computer_id, name, description, publisher, version, product_id, listname FROM " + SCHEMAPATH + ".app_inventory WHERE computer_id=" + id + " AND name='" + nam + "'";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            String myName = rs.getString(2);

            if (myID == id && myName.equals(nam)) {
                computerID = myID;
                name = myName;
                description = rs.getString(3);
                publisher = rs.getString(4);
                version = rs.getString(5);
                prodID = rs.getString(6);
                listName = rs.getString(7);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - AppInventory lookup");
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getListName() {
        return listName;
    }



    /**
    * Save the current AppInventory to the DB
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

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".app_inventory (computer_id, name, description, publisher, version, product_id) " +
                    "VALUES (" + computerID + ", ?, ?, ?, ?, ?" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, name);
                pstmt.setString(++sqlID, description);
                pstmt.setString(++sqlID, publisher);
                pstmt.setString(++sqlID, version);
                pstmt.setString(++sqlID, prodID);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
            //    sqlcode = "begin UPDATE " + SCHEMAPATH + ".app_inventory " +
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
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - AppInventory save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - AppInventory save");
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
    * Empty AppInventory to the DB
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
                "DELETE FROM " + mySchemaPath + ".app_inventory"
                );
            rows = pstmt.executeUpdate();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - AppInventory empty");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - AppInventory empty");
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
    * Get a list of AppInventory from the DB
    *
    * @param myConn     Connection to the database
    */
    public static AppInventory[] getItemList(DbConn myConn) {
        return getItemList(myConn, 0, (String) null, false, (String) null, false);
    }


    /**
    * Get a list of AppInventory from the DB
    *
    * @param myConn     Connection to the database
    * @param compID     Computer to select (null for all)
    */
    public static AppInventory[] getItemList(DbConn myConn, int compID) {
        return getItemList(myConn, compID, (String) null, false, (String) null, false);
    }


    /**
    * Get a list of AppInventory from the DB
    *
    * @param myConn     Connection to the database
    * @param compID     Computer to select (null for all)
    * @param filtered   Filter out apps flagged to ignore
    */
    public static AppInventory[] getItemList(DbConn myConn, int compID, boolean filtered) {
        return getItemList(myConn, compID, (String) null, filtered, (String) null, false);
    }


    /**
    * Get a list of AppInventory from the DB
    *
    * @param myConn     Connection to the database
    * @param compID     Computer to select (null for all)
    * @param filtered   Filter out apps flagged to ignore
    * @param orderBy    SQL Order By clause
    */
    public static AppInventory[] getItemList(DbConn myConn, int compID, boolean filtered, String orderBy) {
        return getItemList(myConn, compID, (String) null, filtered, orderBy, false);
    }


    /**
    * Get a list of AppInventory from the DB
    *
    * @param myConn     Connection to the database
    * @param compID     Computer to select (null for all)
    * @param ln         List name to select (null for all)
    * @param filtered   Filter out apps flagged to ignore
    * @param orderBy    SQL Order By clause
    */
    public static AppInventory[] getItemList(DbConn myConn, int compID, String ln, boolean filtered, String orderBy) {
        return getItemList(myConn, compID, ln, filtered, orderBy, false);
    }


    /**
    * Get a list of AppInventory from the DB
    *
    * @param myConn     Connection to the database
    * @param compID     Computer to select (0 for all)
    * @param ln         List name to select (null for all)
    * @param filtered   Filter out apps flagged to ignore
    * @param orderBy    SQL Order By clause
    * @param notMatched Filter for only apps not matched to a product
    */
    public static AppInventory[] getItemList(DbConn myConn, int compID, String ln, boolean filtered, String orderBy, boolean notMatched) {
        return getItemList(myConn, compID, ln, filtered, orderBy, notMatched, null);
    }


    /**
    * Get a list of AppInventory from the DB
    *
    * @param myConn     Connection to the database
    * @param compID     Computer to select (0 for all)
    * @param ln         List name to select (null for all)
    * @param filtered   Filter out apps flagged to ignore
    * @param orderBy    SQL Order By clause
    * @param notMatched Filter for only apps not matched to a product
    * @param searchText   Text to search for
    */
    public static AppInventory[] getItemList(DbConn myConn, int compID, String ln, boolean filtered, String orderBy, boolean notMatched, String searchText) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        AppInventory[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".app_inventory ai, " + mySchemaPath + ".computer c ";
            sqlWhere = " WHERE ai.computer_id=c.computer_id ";
            sqlWhere += " AND ai.computer_id NOT IN (SELECT computer_id FROM " + mySchemaPath + ".computer_ignore) ";
            sqlWhere += (compID != 0) ? ("AND ai.computer_id = " + compID + " ") : "";
            sqlWhere += (filtered) ? "AND ai.name NOT IN (SELECT listname FROM " + mySchemaPath + ".os_apps_ignore) " : "";
            sqlWhere += (ln != null) ? "AND ai.listname='" + ln + "' " : "";
            sqlWhere += (notMatched) ? "AND ai.listname NOT IN (SELECT listname FROM " + mySchemaPath + ".product_app_inv_match) " : "";
            sqlWhere += (searchText != null) ? " AND (LOWER(ai.name) LIKE '%" + searchText.toLowerCase() + "%' OR LOWER(ai.description) LIKE '%" + searchText.toLowerCase() + "%' " +
                  "OR LOWER(ai.publisher) LIKE '%" + searchText.toLowerCase() + "%') " : "";
            sqlOrderBy = " ORDER BY " + ((orderBy != null) ? orderBy : "ai.computer_id, ai.listname ");

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new AppInventory[returnSize];

            sqlcode = "SELECT ai.computer_id, ai.name, ai.description, ai.publisher, ai.version, ai.product_id, ai.listname, c.name " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new AppInventory();
                item[i].computerID = rs.getInt(1);
                item[i].name = rs.getString(2);
                item[i].description = rs.getString(3);
                item[i].publisher = rs.getString(4);
                item[i].version = rs.getString(5);
                item[i].prodID = rs.getString(6);
                item[i].listName = rs.getString(7);
                item[i].computerName = rs.getString(8);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - AppInventory getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - AppInventory getItemList");
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
    public static String[] getAppNameList(DbConn myConn) {
        return getAppNameList(myConn, false, false);
    }


    /**
    * Get a list of distinct application names from the DB
    *
    * @param myConn     Connection to the database
    * @param filtered     Filter out apps flagged to ignore
    */
    public static String[] getAppNameList(DbConn myConn, boolean filtered) {
        return getAppNameList(myConn, filtered, false);
    }


    /**
    * Get a list of distinct application names from the DB
    *
    * @param myConn       Connection to the database
    * @param filtered     Filter out apps flagged to ignore
    * @param skipMatched  Filter out apps that have been matched to a product
    */
    public static String[] getAppNameList(DbConn myConn, boolean filtered, boolean skipMatched) {
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

            sqlFrom = " FROM " + mySchemaPath + ".app_inventory ";
            sqlWhere = " WHERE 1=1 ";
            sqlWhere += " AND computer_id NOT IN (SELECT computer_id FROM " + mySchemaPath + ".computer_ignore) ";
            sqlWhere += (filtered) ? "AND listname NOT IN (SELECT listname FROM " + mySchemaPath + ".os_apps_ignore) " : "";
            sqlWhere += (skipMatched) ? "AND listname NOT IN (SELECT listname FROM " + mySchemaPath + ".product_app_inv_match) " : "";
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
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - getAppNameList lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - getAppNameList lookup");
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
    * Get a hash of distinct application names from the DB
    *
    * @param myConn     Connection to the database
    * @param filtered   Filter out apps flagged to ignore
    */
    public static HashMap getAppNameHash(DbConn myConn, boolean filtered) {
        return getAppNameHash(myConn, filtered, false);
    }


    /**
    * Get a hash of distinct application names from the DB
    *
    * @param myConn     Connection to the database
    * @param filtered   Filter out apps flagged to ignore
    * @param skipMatched  Filter out apps that have been matched to a product
    */
    public static HashMap getAppNameHash(DbConn myConn, boolean filtered, boolean skipMatched) {
        String[] items = getAppNameList(myConn, filtered, skipMatched);
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
        PreparedStatement pstmt = null;
        String outLine = "";
        ApplicationsIn [] items = null;
        String sqlcode = "";
        try {
//System.out.println("AppInventory:load-Got Here 1");
            items = ApplicationsIn.getItemList(msConn, myConn);
//System.out.println("AppInventory:load-Got Here 2");

            String mySchemaPath = "slts";
            sqlcode = "begin INSERT INTO " + mySchemaPath + ".app_inventory (computer_id, name, description, publisher, version, product_id, listname) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?" +
                    "); end;";
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);

            for (int i=0; i<items.length; i++) {
                //AppInventory in = new AppInventory();
                //in.setComputerID(items[i].getComputerID());
                //in.setName((( != null) ?  : null)items[i].getName());
                //in.setDescription(((items[i].getDescription() != null) ? items[i].getDescription() : null));
                //in.setPublisher(((items[i].getPublisher() != null) ? items[i].getPublisher() : null));
                //in.setVersion(((items[i].getVersion() != null) ? items[i].getVersion() : null));
                //in.setProdID(((items[i].getProdID() != null) ? items[i].getProdID() : null));

                //in.save(myConn);


                int sqlID = 0;
                pstmt.setInt(++sqlID, items[i].getComputerID());
                pstmt.setString(++sqlID, ((items[i].getName() != null) ? items[i].getName() : null));
                pstmt.setString(++sqlID, ((items[i].getDescription() != null) ? items[i].getDescription() : null));
                pstmt.setString(++sqlID, ((items[i].getPublisher() != null) ? items[i].getPublisher() : null));
                pstmt.setString(++sqlID, ((items[i].getVersion() != null) ? items[i].getVersion() : null));
                pstmt.setString(++sqlID, ((items[i].getProdID() != null) ? items[i].getProdID() : null));
                pstmt.setString(++sqlID, ((StringUtils.isNotBlank(items[i].getDescription())) ? items[i].getDescription() : items[i].getName()));
                int rows = pstmt.executeUpdate();
            }
//System.out.println("AppInventory:load-Got Here 3");

            pstmt = myConn.conn.prepareStatement(
                "UPDATE " + mySchemaPath + ".app_inventory SET version=product_id WHERE version IS NULL"
                );
            int rows = pstmt.executeUpdate();
//System.out.println("AppInventory:load-Got Here 4");
            myConn.conn.commit();
//System.out.println("AppInventory:load-Got Here 5");

        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " In AppInventory load");
        }
        finally {
            if (pstmt != null)
                try { pstmt.close(); } catch (Exception i) {}

        }

    }







}
