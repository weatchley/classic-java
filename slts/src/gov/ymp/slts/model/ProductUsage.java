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
* ProductUsage is the class for product usage in the db
*
* @author   Bill Atchley
*/
public class ProductUsage {
    private int productID = 0;
    private java.util.Date activityDate = null;
    private int totalcount = 0;
    private int usedcount = 0;
    private int availcount = 0;

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
    * Creates a new empty ProductUsage object
    */
    public ProductUsage () {
        init();
    }


    /**
    * Creates an ProductUsage object and uses the given id to populate it from the db
    *
    * @param id     The id of the ProductUsage to lookup from the db (int)
    * @param ad     The date of the ProductUsage to lookup from the db (java.util.Date)
    * @param myConn Connection to the database
    */
    public ProductUsage (int id, java.util.Date ad, DbConn myConn) {
        init();
        lookup(id,ad, myConn);
    }


    /**
    * Retrieves a ProductUsage from the db and stores it in the current ProductUsage object
    *
    * @param id     The id  to lookup from the db (int)
    * @param ad     The date of the ProductUsage to lookup from the db (java.util.Date)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, java.util.Date ad, DbConn myConn) {
        lookup(id, ad, myConn);
    }


    /**
    * Retrieves a ProductUsage from the db and stores it in the current ProductUsage object
    *
    * @param id     The id to lookup from the db (int)
    * @param ad     The date of the ProductUsage to lookup from the db (java.util.Date)
    * @param myConn Connection to the database
    */
    public void lookup (int id, java.util.Date ad, DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String dateFormat = "MM/DD/YYYY HH24:MI:SS";
            String sqlcode = "SELECT productid,activitydate,totalcount,usedcount,availcount ";
            sqlcode += "FROM " + SCHEMAPATH + ".product_usage WHERE productID='" + id + "' AND activitydate=TO_DATE('$args{date}','dateFormat')";

//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            if (myID == id) {
                productID = myID;
                activityDate = rs.getTimestamp(2);
                totalcount = rs.getInt(3);
                usedcount = rs.getInt(4);
                availcount = rs.getInt(5);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProductUsage lookup");
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


    public java.util.Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(java.util.Date activityDate) {
        this.activityDate = activityDate;
    }

    public int getAvailcount() {
        return availcount;
    }

    public void setAvailcount(int availcount) {
        this.availcount = availcount;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public int getUsedcount() {
        return usedcount;
    }

    public void setUsedcount(int usedcount) {
        this.usedcount = usedcount;
    }



    /**
    * Save the current ProductUsage to the DB
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

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".product_usage (productid,activitydate,totalcount,usedcount,availcount) " +
                    "VALUES (" + productID + ", ?, " + totalcount + ", " + usedcount + ", " + availcount +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setTimestamp(++sqlID, (new java.sql.Timestamp((new java.util.Date()).getTime())));
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
            //    sqlcode = "begin UPDATE " + SCHEMAPATH + ".product_usage " +
            //          "SET productid = " + productID + ", activitydate = ?, totalcount = " + totalcount + ", usedcount= " + usedcount + ", availcount=" + availcount +
            //          " WHERE productid= " + productID + " AND activitydate=?" +
            //          "; end;";
//System.out.println(sqlcode);
            //    pstmt = conn.prepareStatement(sqlcode);
            //    int sqlID = 0;
            //    //pstmt.setString(++sqlID, domainName);
            //    pstmt.setDate(++sqlID, (java.sql.Date) dateDiscovered);
            //    pstmt.setDate(++sqlID, (java.sql.Date) dateDiscovered);
            //    rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProductUsage save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProductUsage save");
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
    * Get a list of ProductUsage from the DB
    *
    * @param myConn     Connection to the database
    */
    public static ProductUsage[] getItemList(DbConn myConn) {
        return getItemList(myConn, 0);
    }


    /**
    * Get a list of ProductUsage from the DB
    *
    * @param myConn     Connection to the database
    # @param prod       Product ID to lookup
    */
    public static ProductUsage[] getItemList(DbConn myConn, int prod) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        ProductUsage[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".product_usage ";
            sqlWhere = " WHERE 1=1 ";
            sqlWhere += (prod > 0) ? " AND productid=" + prod + " " : "";
            sqlOrderBy = " ORDER BY productid, activitydate DESC ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new ProductUsage[returnSize];

            sqlcode = "SELECT productid,activitydate,totalcount,usedcount,availcount " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new ProductUsage();
                item[i].productID = rs.getInt(1);
                item[i].activityDate = rs.getTimestamp(2);
                item[i].totalcount = rs.getInt(3);
                item[i].usedcount = rs.getInt(4);
                item[i].availcount = rs.getInt(5);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProductUsage getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProductUsage getItemList");
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
    * Update product usage the DB
    *
    * @param myConn     Connection to the database
    */
    public static void updateUsage(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rsP = null;
        ResultSet rs = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlcodeProd = "";
        String sqlFrom = "";
        String sqlFromProd = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".product_usage ";
            sqlFromProd = " FROM " + mySchemaPath + ".sw_products ";

            sqlcodeProd = "SELECT id,datemodified,totalcount,usedcount,availcount " + sqlFromProd;
//System.out.println(sqlcodeProd);
            sqlcode = "SELECT productid,activitydate,totalcount,usedcount,availcount " + sqlFrom + " WHERE productid=? ORDER BY activitydate DESC ";
//System.out.println(sqlcode);
            pstmt = conn.prepareStatement(sqlcode);
            rsP = stmt.executeQuery(sqlcodeProd);
            while (rsP.next()) {
                int myID = rsP.getInt(1);
                int sqlID = 0;
                pstmt.setInt(++sqlID, myID);
                boolean doInsert = false;
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (rsP.getInt(3) != rs.getInt(3) || rsP.getInt(4) != rs.getInt(4) || rsP.getInt(5) != rs.getInt(5)) {
                        doInsert = true;
                    }
                } else {
                    doInsert = true;
                }
                if (doInsert) {
                    ProductUsage item = new ProductUsage();
                    item.productID = myID;
                    item.activityDate = rsP.getTimestamp(2);
                    item.totalcount = rsP.getInt(3);
                    item.usedcount = rsP.getInt(4);
                    item.availcount = rsP.getInt(5);
                    item.save(myConn);
                }

            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProductUsage updateUsage");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProductUsage updateUsage");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (rsP != null)
                try { rsP.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
            if (pstmt != null)
                try { pstmt.close(); } catch (Exception i) {}
        }

    }





}
