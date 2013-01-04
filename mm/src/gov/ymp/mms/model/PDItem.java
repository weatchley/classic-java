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
* PDItem is the class for holding PD items in the db
*
* @author   Bill Atchley
*/
public class PDItem {
    public String prnumber = null;
    public int itemNumber = 0;
    public String description = null;
    public String partNumber = null;
    public int quantity = 0;
    public String unitOfIssue = null;
    public double unitPrice = 0.00;
    public String isHazmat = null;
    public int type = 0;
    public String ec = null;
    public int quantityReceived = 0;
    public String substituteOk = null;
    public String techInspection = null;
    public boolean isNew = true;
    public boolean isHistory = false;
    public java.util.Date changeDate = null;
    public double priceChange = 0.00;
    public String changes = null;

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
    public PDItem () {
        init();
    }


    /**
    * Get a list of PDItems from the DB
    *
    * @param myConn     Connection to the database
    * @param pr         prnumber to return items for (String)
    */
    public static PDItem[] getItemList(DbConn myConn, String pr) {
        return getItemList(myConn, pr, false, null, null);
    }


    /**
    * Get a list of history PDItems from the DB
    *
    * @param myConn     Connection to the database
    * @param pr         prnumber to return items for (String)
    */
    public static PDItem[] getHistoryItemList(DbConn myConn, String pr, java.util.Date histDate) {
        return getItemList(myConn, pr, true, histDate, null);
    }


    /**
    * Get a list of PDItems from the DB
    *
    * @param myConn     Connection to the database
    * @param pr         prnumber to return items for (String)
    * @param hist       History flag (boolean)
    * @param histDate   Timestamp for history
    */
    public static PDItem[] getItemList(DbConn myConn, String pr, boolean hist, java.util.Date histDate) {
        return getItemList(myConn, pr, true, histDate, null);
    }


    /**
    * Get a list of PDItems from the DB
    *
    * @param myConn     Connection to the database
    * @param pr         prnumber to return items for (String)
    * @param hist       History flag (boolean)
    * @param histDate   Timestamp for history
    * @param searchText Text to search for
    */
    public static PDItem[] getItemList(DbConn myConn, String pr, boolean hist, java.util.Date histDate, String searchText) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        PDItem[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            //sqlFrom = " FROM " + SCHEMAPATH + ".items ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".items ";
            sqlFrom = " FROM " + mySchemaPath + "." + ((!hist) ? "items" : "item_history") + " ";

            sqlWhere = " WHERE 1=1 ";
            sqlWhere += (pr != null) ? " AND prnumber='" + pr + "' " : "";
            sqlWhere += (searchText != null) ? " AND (LOWER(description) LIKE '%" + searchText.toLowerCase() + "%' OR LOWER(partnumber) LIKE '%" + searchText.toLowerCase() + "%') " : "";
            if (hist) {
//System.out.println("PDItem - " + "Got Here 1");
                sqlWhere += " AND changedate=TO_DATE('" + Utils.dateToString(histDate, "MM/dd/yyyy HH:mm:ss") + "','MM/DD/YYYY HH24:MI:SS') ";
            }

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new PDItem[returnSize];

            sqlcode = "SELECT prnumber, itemnumber, description, partnumber, quantity, unitofissue, unitprice, ";
            sqlcode += "ishazmat, type, ec, quantityreceived, substituteok, techinspection ";
            if (hist) {
                sqlcode += ", changedate, pricechange, changes ";
            }
            sqlcode += sqlFrom + sqlWhere + " ORDER BY itemnumber";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new PDItem();
                item[i].prnumber = rs.getString(1);
                item[i].itemNumber = rs.getInt(2);
                item[i].description = rs.getString(3);
                item[i].partNumber = rs.getString(4);
                item[i].quantity = rs.getInt(5);
                item[i].unitOfIssue = rs.getString(6);
                item[i].unitPrice = rs.getDouble(7);
                item[i].isHazmat = rs.getString(8);
                item[i].type = rs.getInt(9);
                item[i].ec = rs.getString(10);
                item[i].quantityReceived = rs.getInt(11);
                item[i].substituteOk = rs.getString(12);
                item[i].techInspection = rs.getString(13);
                item[i].isNew = false;
                if (hist) {
                    item[i].changeDate = rs.getTimestamp(14);
                    item[i].priceChange = rs.getDouble(15);
                    item[i].changes = rs.getString(16);
                }
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - PDItem lookup");
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

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getIsHazmat() {
        return isHazmat;
    }

    public void setIsHazmat(String isHazmat) {
        this.isHazmat = isHazmat;
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

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public String getPrnumber() {
        return prnumber;
    }

    public void setPrnumber(String prnumber) {
        this.prnumber = prnumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantityReceived() {
        return quantityReceived;
    }

    public void setQuantityReceived(int quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public String getSubstituteOk() {
        return substituteOk;
    }

    public void setSubstituteOk(String substituteOk) {
        this.substituteOk = substituteOk;
    }

    public String getTechInspection() {
        return techInspection;
    }

    public void setTechInspection(String techInspection) {
        this.techInspection = techInspection;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public boolean isIsHistory() {
        return isHistory;
    }

    public java.util.Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(java.util.Date changeDate) {
        this.changeDate = changeDate;
    }



}
