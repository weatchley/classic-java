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
* InvoiceDetail is the class for holding receiving items in the db
*
* @author   Bill Atchley
*/
public class InvoiceDetail {
    public String invoiceID = null;
    public String chargeNumber = null;
    public String ec = null;
    public double tax = 0.0;
    public double amount = 0.0;

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
    public InvoiceDetail () {
        init();
    }


    /**
    * Get a list of InvoiceDetails from the DB
    *
    * @param myConn     Connection to the database
    * @param id         receiving log entry to return items for (String)
    */
    public static InvoiceDetail[] getItemList(DbConn myConn, String id) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        InvoiceDetail[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            //sqlFrom = " FROM " + SCHEMAPATH + ".items ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".items ";
            sqlFrom = " FROM " + mySchemaPath + ".invoice_detail ";

            sqlWhere = " WHERE invoiceid='" + id + "' ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new InvoiceDetail[returnSize];

            sqlcode = "SELECT invoiceid, chargenumber, ec, tax, amount ";
            sqlcode += sqlFrom + sqlWhere + " ORDER BY invoiceid, chargenumber, ec ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new InvoiceDetail();
                item[i].invoiceID = rs.getString(1);
                item[i].chargeNumber = rs.getString(2);
                item[i].ec = rs.getString(3);
                item[i].tax = rs.getDouble(4);
                item[i].amount = rs.getDouble(5);

                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - InvoiceDetail lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - InvoiceDetail lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getChargeNumber() {
        return chargeNumber;
    }

    public void setChargeNumber(String chargeNumber) {
        this.chargeNumber = chargeNumber;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }





}
