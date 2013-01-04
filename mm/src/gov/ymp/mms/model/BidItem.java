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
* BidItem is the class for holding bid items in the db
*
* @author   Bill Atchley
*/
public class BidItem {
    public int bidID = 0;
    public int itemNumber = 0;
    public String description = null;
    public String partNumber = null;
    public int quantity = 0;
    public String unitOfIssue = null;
    public double unitPrice = 0.0;
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
    * Creates a new empty Archive object
    */
    public BidItem () {
        bidID = 0;
        init();
    }


    /**
    * Get a list of BidItems from the DB
    *
    * @param myConn     Connection to the database
    * @param id         bid to return bidItems for (int)
    */
    public static BidItem[] getItemList(DbConn myConn, int id) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        BidItem[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            //sqlFrom = " FROM " + SCHEMAPATH + ".bid_items ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".bid_items ";
            sqlFrom = " FROM " + mySchemaPath + ".bid_items ";

            sqlWhere = " WHERE bidid=" + id + " ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new BidItem[returnSize];

            sqlcode = "SELECT bidid, itemnumber, description, partnumber, quantity, unitofissue, unitprice " + sqlFrom + sqlWhere + " ";
            sqlcode += " ORDER BY itemnumber";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new BidItem();
                item[i].bidID = rs.getInt(1);
                item[i].itemNumber = rs.getInt(2);
                item[i].description = rs.getString(3);
                item[i].partNumber = rs.getString(4);
                item[i].quantity = rs.getInt(5);
                item[i].unitOfIssue = rs.getString(6);
                item[i].unitPrice = rs.getDouble(7);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - BidItem lookup");
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

    public int getBidID() {
        return bidID;
    }

    public void setBidID(int bidID) {
        this.bidID = bidID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnitOfIssue() {
        return unitOfIssue;
    }

    public void setUnitOfIssue(String unitOfIssue) {
        this.unitOfIssue = unitOfIssue;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }



}
