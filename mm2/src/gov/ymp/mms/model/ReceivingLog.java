package gov.ymp.mms.model;

// Support classes
import java.io.*;
import java.util.*;
import java.text.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
* ReceivingLog is the class for archived reports in the db
*
* @author   Bill Atchley
*/
public class ReceivingLog {
    public String ID = null;
    public String prnumber = null;
    public java.util.Date dateReceived = null;
    public String deliverdTo = null;
    public java.util.Date dateDelivered = null;
    public String shipmentNumber = null;
    public int receivedBy = 0;
    public String vendor = null;
    public String shipVia = null;
    public String comments = null;
    public ReceivingItem [] items = null;

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
    * Creates a new empty ReceivingLog object
    */
    public ReceivingLog () {
        init();
    }


    /**
    * Creates an ReceivingLog object and uses the given id to populate it from the db
    *
    * @param id     The id of the ReceivingLog to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public ReceivingLog (String id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a ReceivingLog from the db and stores it in the current ReceivingLog object
    *
    * @param id     The id to lookup from the db (String)
    */
    public void lookup (String id, DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, prnumber, datereceived, deliveredto, datedelivered, shipmentnumber, receivedby, vendor, shipvia, comments ";
            sqlcode += "FROM " + SCHEMAPATH + ".receiving_log WHERE id='" + id + "'";

//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String myID = rs.getString(1);
            if (myID.equals(id)) {
                ID = myID;
                prnumber = rs.getString(2);
                dateReceived = rs.getTimestamp(3);
                deliverdTo = rs.getString(4);
                dateDelivered = rs.getTimestamp(5);
                shipmentNumber = rs.getString(6);
                receivedBy = rs.getInt(7);
                vendor = rs.getString(8);
                shipVia = rs.getString(9);
                comments = rs.getString(10);

                isNew = false;
                items = ReceivingItem.getItemList(myConn, ID);


            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - ReceivingLog lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - ReceivingLog lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }



    /**
    * Get a list of ReceivingLog from the DB
    *
    * @param myConn       Connection to the database
    * @param pr           purchase document to lookup
    */
    public static ReceivingLog[] getItemList(DbConn myConn, String pr) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        ReceivingLog[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            sqlFrom = " FROM " + mySchemaPath + ".receiving_log ";
            sqlWhere = " WHERE prnumber='" + pr + "' ";
            sqlOrderBy = " ORDER BY datereceived ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new ReceivingLog[returnSize];

            sqlcode = "SELECT id, prnumber, datereceived, deliveredto, datedelivered, shipmentnumber, receivedby, vendor, shipvia, comments" + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
//System.out.println("ReceivingLog-getItemList - Got Here 1");
            while (rs.next()) {
                item[i] = new ReceivingLog();
                item[i].ID = rs.getString(1);
                item[i].prnumber = rs.getString(2);
                item[i].dateReceived = rs.getTimestamp(3);
                item[i].deliverdTo = rs.getString(4);
                item[i].dateDelivered = rs.getTimestamp(5);
                item[i].shipmentNumber = rs.getString(6);
                item[i].receivedBy = rs.getInt(7);
                item[i].vendor = rs.getString(8);
                item[i].shipVia = rs.getString(9);
                item[i].comments = rs.getString(10);
                item[i].items = ReceivingItem.getItemList(myConn, item[i].ID);

                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ReceivingLog - getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ReceivingLog - getItemList");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public java.util.Date getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(java.util.Date dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public java.util.Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(java.util.Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getDeliverdTo() {
        return deliverdTo;
    }

    public void setDeliverdTo(String deliverdTo) {
        this.deliverdTo = deliverdTo;
    }

    public ReceivingItem[] getItems() {
        return items;
    }

    public String getPrnumber() {
        return prnumber;
    }

    public void setPrnumber(String prnumber) {
        this.prnumber = prnumber;
    }

    public int getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(int receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getShipVia() {
        return shipVia;
    }

    public void setShipVia(String shipVia) {
        this.shipVia = shipVia;
    }

    public String getShipmentNumber() {
        return shipmentNumber;
    }

    public void setShipmentNumber(String shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }



}
