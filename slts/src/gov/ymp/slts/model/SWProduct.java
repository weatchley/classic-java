package gov.ymp.slts.model;

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
* SWProduct is the class for software products in the db
*
* @author   Bill Atchley
*/
public class SWProduct {
    private int ID = 0;
    private String name = null;
    private String version = null;
    private String manufacturer = null;
    private String platform = null;
    private boolean unlimited = false;
    private int totalCount = 0;
    private int usedCount = 0;
    private int availCount = 0;
    private String status = null;
    private int relatedProduct = 0;
    private java.util.Date dateAdded = null;
    private String respOrg = null;
    private java.util.Date dateModified = null;

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
    * Creates a new empty SWProduct object
    */
    public SWProduct () {
        init();
    }


    /**
    * Creates an SWProduct object and uses the given id to populate it from the db
    *
    * @param val     The id of the SWProduct to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public SWProduct (int val, DbConn myConn) {
        init();
        lookup(val, myConn);
    }


    /**
    * Retrieves a SWProduct from the db and stores it in the current SWProduct object
    *
    * @param val     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int val, DbConn myConn) {
        lookup(val, myConn);
    }


    /**
    * Retrieves a SWProduct from the db and stores it in the current SWProduct object
    *
    * @param val    The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int val, DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;
//System.out.println("SWProduct.java-lookup - Got Here 1");

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id,name,version,manufacturer,platform,unlimited,totalcount,usedcount,availcount,status,relatedproduct,dateadded, resporg, datemodified ";
            sqlcode += "FROM " + SCHEMAPATH + ".sw_products WHERE id='" + val + "'";

//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
//System.out.println("SWProduct.java-lookup - Got Here 2");
            if (myID == val) {
                ID = myID;
                name = rs.getString(2);
                version = rs.getString(3);
                manufacturer = rs.getString(4);
                platform = rs.getString(5);
                unlimited = ((rs.getString(6).equals("T")) ? true : false);
                totalCount = rs.getInt(7);
                usedCount = rs.getInt(8);
                availCount = rs.getInt(9);
                status = rs.getString(10);
                relatedProduct = rs.getInt(11);
                dateAdded = rs.getTimestamp(12);
                respOrg = rs.getString(13);
                dateModified = rs.getTimestamp(14);
                isNew = false;

            }
//System.out.println("SWProduct.java-lookup - Got Here 3");
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    public int getAvailCount() {
        return availCount;
    }

    public void setAvailCount(int availCount) {
        this.availCount = availCount;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getRelatedProduct() {
        return relatedProduct;
    }

    public void setRelatedProduct(int relatedProduct) {
        this.relatedProduct = relatedProduct;
    }

    public String getRespOrg() {
        return respOrg;
    }

    public void setRespOrg(String respOrg) {
        this.respOrg = respOrg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isUnlimited() {
        return unlimited;
    }

    public void setUnlimited(boolean unlimited) {
        this.unlimited = unlimited;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public java.util.Date getDateAdded() {
        return dateAdded;
    }

    public java.util.Date getDateModified() {
        return dateModified;
    }



    /**
    * Save the current SWProduct to the DB
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
                sqlcode = "SELECT " + SCHEMAPATH + ".SW_PRODUCTS_ID_SEQ.NEXTVAL FROM dual";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID;

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".sw_products (id,name,version,manufacturer,platform,unlimited,totalcount,usedcount,availcount,status,relatedproduct,dateadded, resporg, datemodified) " +
                    "VALUES (" + ID + ", ?, ?, ?, ?, '" + ((unlimited) ? 'T' : 'F') + "', " + totalCount + ", " + usedCount + ", " + availCount + ", ?, " + relatedProduct + ", SYSDATE, ?, SYSDATE" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, name);
                pstmt.setString(++sqlID, version);
                pstmt.setString(++sqlID, manufacturer);
                pstmt.setString(++sqlID, platform);
                pstmt.setString(++sqlID, status);
                pstmt.setString(++sqlID, respOrg);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".sw_products " +
                      "SET name=?,version=?,manufacturer=?,platform=?, unlimited = '" + ((unlimited) ? 'T' : 'F') + "'," +
                      "totalcount=" + totalCount + ",usedcount=" + usedCount + ",availcount=" + availCount + ",status=?,relatedproduct=" + relatedProduct + ", " +
                      "resporg=?, datemodified=SYSDATE WHERE id= " + ID +
                      "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
//System.out.println("SWProduct.java-Save - Got Here 1");
                pstmt.setString(++sqlID, name);
                pstmt.setString(++sqlID, version);
                pstmt.setString(++sqlID, manufacturer);
                pstmt.setString(++sqlID, platform);
                pstmt.setString(++sqlID, status);
                pstmt.setString(++sqlID, respOrg);
                rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct save");
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
    * Get a list of SWProduct from the DB
    *
    * @param myConn       Connection to the database
    */
    public static SWProduct[] getItemList(DbConn myConn) {
        return getItemList(myConn, false, false, null);
    }


    /**
    * Get a list of SWProduct from the DB
    *
    * @param myConn       Connection to the database
    * @param swStatus     Filer for specific software status (null or 'all' for All)
    */
    public static SWProduct[] getItemList(DbConn myConn, String swStatus) {
        return getItemList(myConn, false, false, swStatus);
    }


    /**
    * Get a list of SWProduct from the DB
    *
    * @param myConn       Connection to the database
    * @param notMatched   Flag to only retrieve items that have no matching app or service
    */
    public static SWProduct[] getItemList(DbConn myConn, boolean notMatched) {
        return getItemList(myConn, notMatched, false, null);
    }


    /**
    * Get a list of SWProduct from the DB
    *
    * @param myConn       Connection to the database
    * @param notMatched   Flag to only retrieve items that have no matching app or service
    * @param isOverUsed   Flag to only retrieve items that are over used
    */
    public static SWProduct[] getItemList(DbConn myConn, boolean notMatched, boolean isOverUsed) {
        return getItemList(myConn, notMatched, isOverUsed, null);
    }


    /**
    * Get a list of SWProduct from the DB
    *
    * @param myConn       Connection to the database
    * @param notMatched   Flag to only retrieve items that have no matching app or service
    * @param isOverUsed   Flag to only retrieve items that are over used
    * @param swStatus     Filer for specific software status (null or 'all' for All)
    */
    public static SWProduct[] getItemList(DbConn myConn, boolean notMatched, boolean isOverUsed, String swStatus) {
        return getItemList(myConn, notMatched, isOverUsed, swStatus, null);
    }


    /**
    * Get a list of SWProduct from the DB
    *
    * @param myConn       Connection to the database
    * @param notMatched   Flag to only retrieve items that have no matching app or service
    * @param isOverUsed   Flag to only retrieve items that are over used
    * @param swStatus     Filer for specific software status (null or 'all' for All)
    * @param searchText   Text to search for
    */
    public static SWProduct[] getItemList(DbConn myConn, boolean notMatched, boolean isOverUsed, String swStatus, String searchText) {
        return getItemList(myConn, notMatched, isOverUsed, swStatus, searchText, null);
    }


    /**
    * Get a list of SWProduct from the DB
    *
    * @param myConn       Connection to the database
    * @param notMatched   Flag to only retrieve items that have no matching app or service
    * @param isOverUsed   Flag to only retrieve items that are over used
    * @param swStatus     Filer for specific software status (null or 'all' for All)
    * @param searchText   Text to search for
    * @param swpList      List of products to lookup
    */
    public static SWProduct[] getItemList(DbConn myConn, boolean notMatched, boolean isOverUsed, String swStatus, String searchText, String swpList) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        SWProduct[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".sw_products ";
            sqlWhere = " WHERE 1=1 ";
            sqlWhere += (notMatched) ? " AND id NOT IN (SELECT productid FROM " + mySchemaPath + ".product_app_inv_match) AND id NOT IN (SELECT productid FROM " + mySchemaPath + ".product_serv_inv_match) " : "";
            sqlWhere += (isOverUsed) ? " AND availcount<0 AND unlimited='F' " : "";
            sqlWhere += (swStatus != null && !swStatus.toLowerCase().equals("all")) ? " AND status='" + swStatus + "' " : "";
            sqlWhere += (swpList != null) ? "AND id IN (" + ((swpList.equals("")) ? "0" : swpList) + ") " : "";
            sqlWhere += (searchText != null) ? " AND (LOWER(name) LIKE '%" + searchText.toLowerCase() + "%' OR LOWER(version) LIKE '%" + searchText.toLowerCase() + "%' " +
                  "OR LOWER(manufacturer) LIKE '%" + searchText.toLowerCase() + "%' OR LOWER(resporg) LIKE '%" + searchText.toLowerCase() + "%') " : "";
            sqlOrderBy = " ORDER BY name,version ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new SWProduct[returnSize];

            sqlcode = "SELECT id,name,version,manufacturer,platform,unlimited,totalcount,usedcount,availcount,status,relatedproduct,dateAdded,resporg,datemodified " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new SWProduct();
                item[i].ID = rs.getInt(1);
                item[i].name = rs.getString(2);
                item[i].version = rs.getString(3);
                item[i].manufacturer = rs.getString(4);
                item[i].platform = rs.getString(5);
                item[i].unlimited = ((rs.getString(6).equals("T")) ? true : false);
                item[i].totalCount = rs.getInt(7);
                item[i].usedCount = rs.getInt(8);
                item[i].availCount = rs.getInt(9);
                item[i].status = rs.getString(10);
                item[i].relatedProduct = rs.getInt(11);
                item[i].dateAdded = rs.getTimestamp(12);
                item[i].respOrg = rs.getString(13);
                item[i].dateModified = rs.getTimestamp(14);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct - getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct - getItemList");
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
    * Get a list of SWProduct Status types from the DB
    *
    * @param myConn     Connection to the database
    */
    public static String[] getStatusList(DbConn myConn) {
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

            sqlFrom = " FROM " + mySchemaPath + ".sw_status ";
            sqlWhere = " ";
            sqlOrderBy = " ORDER BY status ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new String[returnSize];
            item[0] = "";

            sqlcode = "SELECT status " + sqlFrom + sqlWhere + sqlOrderBy + " ";
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
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct - getStatusList lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct - getStatusList lookup");
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
    * Get SWProduct total active counts from the DB
    *
    * @param myConn     Connection to the database
    */
    public static int getCountTotalActive(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        int count = 0;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".sw_products ";
            sqlWhere = " WHERE status IS NULL OR status='active' ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            count = returnSize;
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct - getCountTotalActive");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct - getCountTotalActive");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return count;
    }


    /**
    * Get SWProduct total license shortfall counts from the DB
    *
    * @param myConn     Connection to the database
    */
    public static int getCountLicenseShort(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        int count = 0;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".sw_products ";
            sqlWhere = " WHERE availcount<0 AND unlimited='F'";

            sqlcode = "SELECT SUM(availcount) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            count = returnSize;
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct - getCountLicenseShort");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct - getCountLicenseShort");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return count;
    }


    /**
    * 0 out all counts
    *
    * @param myConn     Connection to the database
    */
    public static void zeroOutCounts(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        int count = 0;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";


            sqlcode = "UPDATE " + mySchemaPath + ".sw_products SET totalcount=0, usedcount=0, availcount=0";
//System.out.println(sqlcode);
            int rows = stmt.executeUpdate(sqlcode);

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct - zeroOutCounts");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct - zeroOutCounts");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

    }


    /**
    * calulate remaining counts
    *
    * @param myConn     Connection to the database
    */
    public static void calcRemainingCounts(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        int count = 0;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";


            sqlcode = "UPDATE " + mySchemaPath + ".sw_products SET availcount=totalcount-usedcount";
//System.out.println(sqlcode);
            int rows = stmt.executeUpdate(sqlcode);

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct - calcRemainingCounts");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWProduct - calcRemainingCounts");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

    }




}
