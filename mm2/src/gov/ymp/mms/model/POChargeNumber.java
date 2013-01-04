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
* POChargeNumber is the class for PO charge numbers in the db
*
* @author   Bill Atchley
*/
public class POChargeNumber {
    public String prnumber = null;
    public String cn = null;
    public String ec = null;
    public double amount = 0.0;
    public double invoiced = 0.0;
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
    * Creates a new empty POChargeNumber object
    */
    public POChargeNumber () {
        cn = null;
        init();
    }


    /**
    * Creates a POChargeNumber object and uses the given value to populate it from the db
    *
    * @param pr      The pr to lookup from the db
    * @param charge  The cn to lookup from the db
    * @param elm     The ec to lookup from the db
    * @param myConn  Connection to the database
    */
    public POChargeNumber (String pr, String charge, String elm, DbConn myConn) {
        init();
        lookup(pr, charge, elm, myConn);
    }


    /**
    * Retrieves a POChargeNumber from the db and stores it in the current POChargeNumber object
    *
    * @param pr      The pr to lookup from the db
    * @param charge  The cn to lookup from the db
    * @param elm     The ec to lookup from the db
    * @param myConn  Connection to the database
    */
    public void getInfo (String pr, String charge, String elm, DbConn myConn) {
        lookup(pr, charge, elm, myConn);
    }


    /**
    * Retrieves a POChargeNumber from the db and stores it in the current POChargeNumber object
    *
    * @param pr      The pr to lookup from the db
    * @param charge  The cn to lookup from the db
    * @param elm     The ec to lookup from the db
    * @param myConn  Connection to the database
    */
    public void lookup (String pr, String charge, String elm, DbConn myConn) {
        cn = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT prnumber, chargenumber, ec, amount, invoiced ";
            sqlcode += " FROM " + SCHEMAPATH + ".po_chargenumbers WHERE prnumber='" + pr + "' AND chargenumber='" + charge + "' AND ec='" + elm + "'";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String myPR = rs.getString(1);
            if (myPR.equals(pr)) {
                prnumber = myPR;
                cn = rs.getString(2);
                ec = rs.getString(3);
                amount = rs.getDouble(4);
                invoiced = rs.getDouble(5);

                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - POChargeNumber lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - POChargeNumber lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get a list of POChargeNumber from the DB
    *
    * @param myConn       Connection to the database
    */
    public static POChargeNumber[] getItemList(DbConn myConn, String pr) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        POChargeNumber[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            sqlFrom = " FROM " + mySchemaPath + ".po_chargenumbers ";
            sqlWhere = " WHERE 1=1 ";
            sqlWhere += (pr != null) ? " AND prnumber='" + pr + "' " : "";
            sqlOrderBy = " ORDER BY prnumber, chargenumber, ec ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new POChargeNumber[returnSize];

            sqlcode = "SELECT prnumber, chargenumber, ec, amount, invoiced " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new POChargeNumber();
                item[i].prnumber = rs.getString(1);
                item[i].cn = rs.getString(2);
                item[i].ec = rs.getString(3);
                item[i].amount = rs.getDouble(4);
                item[i].invoiced = rs.getDouble(5);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - POChargeNumber - getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - POChargeNumber - getItemList");
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

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public double getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(double invoiced) {
        this.invoiced = invoiced;
    }

    public String getPrnumber() {
        return prnumber;
    }

    public void setPrnumber(String prnumber) {
        this.prnumber = prnumber;
    }




}
