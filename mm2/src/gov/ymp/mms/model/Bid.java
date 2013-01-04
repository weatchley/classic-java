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
* Bid is the class for archived reports in the db
*
* @author   Bill Atchley
*/
public class Bid {
    public int ID = 0;
    public String prnumber = null;
    public int vendorID = 0;
    public Vendor vendor = null;
    public java.util.Date dateBidReceived = null;
    public java.util.Date dueDate = null;
    public double shipping = 0.00;
    public String terms = null;
    public int fob = 0;
    public String shipVia = null;
    public int response = 0;
    public BidItem [] items = null;
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
    * Creates a new empty Bid object
    */
    public Bid () {
        ID = 0;
        init();
    }


    /**
    * Creates an Bid object and uses the given id to populate it from the db
    *
    * @param id     The id of the Bid to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public Bid (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a Bid from the db and stores it in the current Bid object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a Bid from the db and stores it in the current Bid object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, DbConn myConn) {
        ID = 0;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, prnumber, vendor, datebidreceived, duedate, shipping, terms, fob, shipvia, response FROM " + SCHEMAPATH + ".bids WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            String myPR = rs.getString(2);
            int myVendor = rs.getInt(3);
            java.util.Date myDateBidReceived = rs.getTimestamp(4);
            java.util.Date myDueDate = rs.getTimestamp(5);
            double myShipping = rs.getDouble(6);
            String myTerms = rs.getString(7);
            String myShipVia = rs.getString(8);
            int myResponse = rs.getInt(9);
            if (myID == id) {
                ID = myID;
                prnumber = myPR;
                vendorID = myVendor;
                vendor = new Vendor(vendorID, myConn);
                dateBidReceived = myDateBidReceived;
                dueDate = myDueDate;
                shipping = myShipping;
                terms = myTerms;
                shipVia = myShipVia;
                response = myResponse;
                isNew = false;
                // get items
                items = BidItem.getItemList(myConn, ID);

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Bid lookup");
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
    }


    /**
    * Get a list of Bid from the DB
    *
    * @param myConn     Connection to the database
    * @param pr         prnumber to return bids for (String)
    */
    public static Bid[] getItemList(DbConn myConn, String pr) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        Bid[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            //sqlFrom = " FROM " + SCHEMAPATH + ".bids ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".bids ";
            sqlFrom = " FROM " + mySchemaPath + ".bids ";

            sqlWhere = " WHERE prnumber= '" + pr + "' ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Bid[returnSize];

            sqlcode = "SELECT id, prnumber, vendor, datebidreceived, duedate, shipping, terms, fob, shipvia, response " + sqlFrom + sqlWhere + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new Bid();
                item[i].ID = rs.getInt(1);
                item[i].prnumber  = rs.getString(2);
                item[i].vendorID  = rs.getInt(3);
                item[i].vendor = new Vendor(item[i].vendorID, myConn);
                item[i].dateBidReceived  = rs.getTimestamp(4);
                item[i].dueDate  = rs.getTimestamp(5);
                item[i].shipping  = rs.getDouble(6);
                item[i].terms  = rs.getString(7);
                item[i].fob  = rs.getInt(8);
                item[i].shipVia  = rs.getString(9);
                item[i].response  = rs.getInt(10);
                item[i].items = BidItem.getItemList(myConn, item[i].ID);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Bid lookup");
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

    public java.util.Date getDateBidReceived() {
        return dateBidReceived;
    }

    public void setDateBidReceived(java.util.Date dateBidReceived) {
        this.dateBidReceived = dateBidReceived;
    }

    public java.util.Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(java.util.Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getFob() {
        return fob;
    }

    public void setFob(int fob) {
        this.fob = fob;
    }

    public BidItem[] getItems() {
        return items;
    }

    public void setItems(BidItem[] items) {
        this.items = items;
    }

    public String getPrnumber() {
        return prnumber;
    }

    public void setPrnumber(String prnumber) {
        this.prnumber = prnumber;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getShipVia() {
        return shipVia;
    }

    public void setShipVia(String shipVia) {
        this.shipVia = shipVia;
    }

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public int getVendorID() {
        return vendorID;
    }

    public void setVendorID(int vendorID) {
        this.vendorID = vendorID;
    }

    public int getID() {
        return ID;
    }

    public Vendor getVendor() {
        return vendor;
    }




}
