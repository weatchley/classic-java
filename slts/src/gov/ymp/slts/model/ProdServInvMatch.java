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
* ProdServInvMatch is the class for product service inventory match in the db
*
* @author   Bill Atchley
*/
public class ProdServInvMatch {
    private int productID = 0;
    private String listName = null;

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
    * Creates a new empty ProdServInvMatch object
    */
    public ProdServInvMatch () {
        init();
    }


    /**
    * Creates a ProdServInvMatch object and uses the given id to populate it from the db
    *
    * @param id     The id of the ProdServInvMatch to lookup from the db (String)
    * @param nam    The name of the ProdServInvMatch to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public ProdServInvMatch (int id,String nam, DbConn myConn) {
        init();
        lookup(id, nam, myConn);
    }


    /**
    * Retrieves a ProdServInvMatch from the db and stores it in the current ProdServInvMatch object
    *
    * @param id     The id of the ProdServInvMatch to lookup from the db (String)
    * @param nam    The name of the ProdServInvMatch to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, String nam, DbConn myConn) {
        lookup(id, nam, myConn);
    }


    /**
    * Retrieves a ProdServInvMatch from the db and stores it in the current ProdServInvMatch object
    *
    * @param id     The id of the ProdServInvMatch to lookup from the db (String)
    * @param nam    The name of the ProdServInvMatch to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void lookup (int id, String nam, DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT productid, listname ";
            sqlcode += "FROM " + SCHEMAPATH + ".product_serv_inv_match WHERE productid=" + id + " AND listname='" + nam + "' ";

//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            String myName = rs.getString(2);
            if (myID == id && myName.equals(nam)) {
                productID = myID;
                listName = myName;
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProdServInvMatch lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProdServInvMatch lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String name) {
        this.listName = name;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }



    /**
    * Save the current ProdServInvMatch to the DB
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

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".product_serv_inv_match (productid, listname) " +
                    "VALUES (" + productID + ", ?" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, listName);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
            //    sqlcode = "begin UPDATE " + SCHEMAPATH + ".product_app_inv_match " +
            //          "SET productid = " + productID + ", name = ? WHERE domain_name= ?" +
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
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProdServInvMatch save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProdServInvMatch save");
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
    * drop ProdServInvMatch from the DB
    *
    * @param myConn     Connection to the database
    * @param prod       ID of the product to clear out of matches
    */
    public static void drop(DbConn myConn, int prod) {
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sqlWhere = "";

        try {

            Connection conn = myConn.conn;

            String mySchemaPath = "slts";
            sqlWhere = " WHERE productid=" + prod  + " ";

            // Create a Statement
            pstmt = conn.prepareStatement(
                "DELETE FROM " + mySchemaPath + ".product_serv_inv_match" + sqlWhere
                );
            rows = pstmt.executeUpdate();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProdServInvMatch drop");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProdServInvMatch drop");
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
    * Get a list of ProdServInvMatch from the DB
    *
    * @param myConn     Connection to the database
    * @param prod       product ID to look up
    */
    public static ProdServInvMatch[] getItemList(DbConn myConn, int prod) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        ProdServInvMatch[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".product_serv_inv_match ";
            sqlWhere = " WHERE productid=" + prod  + " ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new ProdServInvMatch[returnSize];

            sqlcode = "SELECT productid, listname " + sqlFrom + sqlWhere  + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new ProdServInvMatch();
                item[i].productID = rs.getInt(1);
                item[i].listName = rs.getString(2);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProdServInvMatch getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProdServInvMatch getItemList");
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
    * Get a hash of ProdServInvMatch from the DB
    *
    * @param myConn     Connection to the database
    * @param prod       product ID to look up
    */
    public static HashMap getItemHash(DbConn myConn, int prod) {
        ProdServInvMatch[] items = getItemList(myConn, prod);
        HashMap hm = new HashMap((items.length + 1));
        for (int i=0; i<items.length; i++) {
            hm.put(items[i].listName, items[i].listName);
        }
        return hm;
    }


    /**
    * Get a list of computers with product from the DB
    *
    * @param myConn     Connection to the database
    * @param prod       product ID to look up
    */
    public static Computer[] getComputerList(DbConn myConn, int prod) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        Computer[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".service_inventory si, " + mySchemaPath + ".computer c ";

            sqlWhere = " WHERE si.listname IN (SELECT listname FROM " + mySchemaPath + ".product_serv_inv_match WHERE productid=" + prod  + ") " +
                       " AND si.computer_id=c.computer_id ";;
            sqlOrderBy = " ORDER BY c.name ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Computer[returnSize];

            sqlcode = "SELECT si.computer_id " + sqlFrom + sqlWhere + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new Computer(rs.getInt(1), myConn);
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProdServInvMatch getComputerList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ProdServInvMatch getComputerList");
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
    * Update license counts for products
    *
    * @param myConn     Connection to the database
    */
    public static void updateLicenseCounts(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        String sqlcode = "";
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";


            sqlcode = "UPDATE " + mySchemaPath + ".sw_products p SET (p.usedcount)=(SELECT COUNT(*) + p.usedcount FROM " + mySchemaPath + ".service_inventory WHERE listname IN " +
                  "(SELECT listname FROM " + mySchemaPath + ".product_serv_inv_match WHERE productid=p.id))";
//System.out.println(sqlcode);
            int rows = stmt.executeUpdate(sqlcode);

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + "- ProdServInvMatch - updateLicenseCounts");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + "- ProdServInvMatch - updateLicenseCounts");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

    }






}
