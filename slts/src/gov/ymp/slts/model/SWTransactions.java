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
* SWTransactions is the class for software transactions in the db
*
* @author   Bill Atchley
*/
public class SWTransactions {
    private int ID = 0;
    private int productID = 0;
    private String tag = null;
    private String serialNumber = null;
    private String purchaseOrder = null;
    private java.util.Date dateAdded = null;
    private java.util.Date dateModified = null;
    private java.util.Date dateVerified = null;
    private java.util.Date dateReceived = null;
    private java.util.Date dateExpires = null;
    private String location = null;
    private String licenseType = null;
    private int licenseCount = 0;
    private String transactionType = null;
    private int relatedTransaction = 0;
    private String documentation = null;

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
    * Creates a new empty SWTransaction object
    */
    public SWTransactions () {
        init();
    }


    /**
    * Creates an SWTransactions object and uses the given id to populate it from the db
    *
    * @param prod     The id of the product to lookup from the db (int)
    * @param trans    The id of the transaction to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public SWTransactions (int prod, int trans, DbConn myConn) {
        init();
        lookup(prod, trans, myConn);
    }


    /**
    * Retrieves a SWTransactions from the db and stores it in the current SWTransactions object
    *
    * @param prod     The id  to lookup from the db (int)
    * @param trans    The id of the transaction to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int prod, int trans, DbConn myConn) {
        lookup(prod, trans, myConn);
    }


    /**
    * Retrieves a SWTransactions from the db and stores it in the current SWTransactions object
    *
    * @param prod     The id to lookup from the db (int)
    * @param trans    The id of the transaction to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int prod, int trans, DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id,productid,tag,serialnumber,purchaseorder,dateadded,datemodified,dateverified,datereceived,dateexpires,";
            sqlcode += "location,licensetype,licensecount,transactiontype,relatedtransaction,documentation ";
            sqlcode += "FROM " + SCHEMAPATH + ".sw_transactions WHERE productid='" + prod + "' AND id=" + trans;

//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            int myProd = rs.getInt(2);
            if (myID == trans && myProd == prod) {
                ID = myID;
                productID = myProd;
                tag = rs.getString(3);
                serialNumber = rs.getString(4);
                purchaseOrder = rs.getString(5);
                dateAdded = rs.getTimestamp(6);
                dateModified = rs.getTimestamp(7);
                dateVerified = rs.getTimestamp(8);
                dateReceived = rs.getTimestamp(9);
                dateExpires = rs.getTimestamp(10);
                location = rs.getString(11);
                licenseType = rs.getString(12);
                licenseCount = rs.getInt(13);
                transactionType = rs.getString(14);
                relatedTransaction = rs.getInt(15);
                documentation = rs.getString(16);

                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWTransactions lookup");
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

    public int getID() {
        return ID;
    }

    public java.util.Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(java.util.Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public java.util.Date getDateExpires() {
        return dateExpires;
    }

    public void setDateExpires(java.util.Date dateExpires) {
        this.dateExpires = dateExpires;
    }

    public java.util.Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(java.util.Date dateModified) {
        this.dateModified = dateModified;
    }

    public java.util.Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(java.util.Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public java.util.Date getDateVerified() {
        return dateVerified;
    }

    public void setDateVerified(java.util.Date dateVerified) {
        this.dateVerified = dateVerified;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public int getLicenseCount() {
        return licenseCount;
    }

    public void setLicenseCount(int licenseCount) {
        this.licenseCount = licenseCount;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productid) {
        this.productID = productid;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public int getRelatedTransaction() {
        return relatedTransaction;
    }

    public void setRelatedTransaction(int relatedTransaction) {
        this.relatedTransaction = relatedTransaction;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }



    /**
    * Save the current SWTransactions to the DB
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
                sqlcode = "SELECT " + SCHEMAPATH + ".SW_TRANSACTIONS_ID_SEQ.NEXTVAL FROM dual";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID;

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".sw_transactions (id,productid,tag,serialnumber,purchaseorder,dateadded,datemodified,dateverified,datereceived,dateexpires," +
                    "location,licensetype,licensecount,transactiontype,relatedtransaction,documentation) " +
                    "VALUES (" + ID + ", " + productID + ", ?, ?, ?, SYSDATE, SYSDATE, ?, ?, ?, ?, ?, " + licenseCount + ", ?," + relatedTransaction + ", ?" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, tag);
                pstmt.setString(++sqlID, serialNumber);
                pstmt.setString(++sqlID, purchaseOrder);
                pstmt.setDate(++sqlID, ((dateVerified != null) ? Utils.castDate(dateVerified) : null));
                pstmt.setDate(++sqlID, ((dateReceived != null) ? Utils.castDate(dateReceived) : null));
                pstmt.setDate(++sqlID, ((dateExpires != null) ? Utils.castDate(dateExpires) : null));
                pstmt.setString(++sqlID, location);
                pstmt.setString(++sqlID, licenseType);
                pstmt.setString(++sqlID, transactionType);
                pstmt.setString(++sqlID, documentation);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".sw_transactions " +
                      "SET productid= " + productID + ", tag=?, serialnumber=?, purchaseorder=?, datemodified=SYSDATE, dateverified=?, datereceived=?, " +
                      "dateexpires=?, location=?, licensetype=?, licensecount=" + licenseCount + ", transactiontype=?, relatedtransaction=" + relatedTransaction + ", documentation=? WHERE id= " + ID +
                      "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, tag);
                pstmt.setString(++sqlID, serialNumber);
                pstmt.setString(++sqlID, purchaseOrder);
                pstmt.setDate(++sqlID, ((dateVerified != null) ? Utils.castDate(dateVerified) : null));
                pstmt.setDate(++sqlID, ((dateReceived != null) ? Utils.castDate(dateReceived) : null));
                pstmt.setDate(++sqlID, ((dateExpires != null) ? Utils.castDate(dateExpires) : null));
                pstmt.setString(++sqlID, location);
                pstmt.setString(++sqlID, licenseType);
                pstmt.setString(++sqlID, transactionType);
                pstmt.setString(++sqlID, documentation);
                rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWTransactions save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWTransactions save");
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
    * Get a list of SWTransactions from the DB
    *
    * @param myConn     Connection to the database
    * @param prod       ID of product to lookup in the database
    */
    public static SWTransactions[] getItemList(DbConn myConn, int prod) {
        return getItemList(myConn, prod, null);
    }


    /**
    * Get a list of SWTransactions from the DB
    *
    * @param myConn     Connection to the database
    * @param prod       ID of product to lookup in the database
    * @param searchText   Text to search for
    */
    public static SWTransactions[] getItemList(DbConn myConn, int prod, String searchText) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        SWTransactions[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".sw_transactions ";
            sqlWhere = " WHERE 1=1 ";
            sqlWhere += (prod > 0) ? " AND productid=" + prod : "";
            sqlWhere += (searchText != null) ? " AND (LOWER(tag) LIKE '%" + searchText.toLowerCase() + "%' OR LOWER(location) LIKE '%" + searchText.toLowerCase() + "%' " +
                  "OR LOWER(documentation) LIKE '%" + searchText.toLowerCase() + "%') " : "";
            sqlOrderBy = " ORDER BY productid, id ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new SWTransactions[returnSize];

            sqlcode = "SELECT id,productid,tag,serialnumber,purchaseorder,dateadded,datemodified,dateverified,datereceived,dateexpires,";
            sqlcode += "location,licensetype,licensecount,transactiontype,relatedtransaction,documentation " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new SWTransactions();
                item[i].ID = rs.getInt(1);
                item[i].productID = rs.getInt(2);
                item[i].tag = rs.getString(3);
                item[i].serialNumber = rs.getString(4);
                item[i].purchaseOrder = rs.getString(5);
                item[i].dateAdded = rs.getTimestamp(6);
                item[i].dateModified = rs.getTimestamp(7);
                item[i].dateVerified = rs.getTimestamp(8);
                item[i].dateReceived = rs.getTimestamp(9);
                item[i].dateExpires = rs.getTimestamp(10);
                item[i].location = rs.getString(11);
                item[i].licenseType = rs.getString(12);
                item[i].licenseCount = rs.getInt(13);
                item[i].transactionType = rs.getString(14);
                item[i].relatedTransaction = rs.getInt(15);
                item[i].documentation = rs.getString(16);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SWTransactions getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine+ " - SWTransactions getItemList");
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
    public static String[] getTypeList(DbConn myConn) {
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

            sqlFrom = " FROM " + mySchemaPath + ".sw_trans_type ";
            sqlWhere = " ";
            sqlOrderBy = " ORDER BY sortorder ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new String[returnSize];
            item[0] = "";

            sqlcode = "SELECT type " + sqlFrom + sqlWhere + sqlOrderBy + " ";
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
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - getTypeList lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - getTypeList lookup");
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


            sqlcode = "UPDATE " + mySchemaPath + ".sw_products p SET (p.totalcount)=(SELECT NVL(SUM(licensecount), 0) FROM " + mySchemaPath + ".sw_transactions WHERE productid=p.id) ";
//System.out.println(sqlcode);
            int rows = stmt.executeUpdate(sqlcode);

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + "- SWTranactions - updateLicenseCounts");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + "- SWTranactions - updateLicenseCounts");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

    }






}
