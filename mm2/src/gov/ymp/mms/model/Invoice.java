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
* Invoice is the class for archived reports in the db
*
* @author   Bill Atchley
*/
public class Invoice {
    public String ID = null;
    public String prnumber = null;
    public String invoiceNumber = null;
    public java.util.Date dateReceived = null;
    public java.util.Date invoiceDate = null;
    public boolean taxPaid = false;
    public java.util.Date datePaid = null;
    public java.util.Date clientBilled = null;
    public String comments = null;
    public int status = 0;
    public int enteredBy = 0;
    public int approvedBy = 0;
    public java.util.Date dateApproved = null;

    public InvoiceDetail [] items = null;

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
    * Creates a new empty Invoice object
    */
    public Invoice () {
        init();
    }


    /**
    * Creates an Invoice object and uses the given id to populate it from the db
    *
    * @param id     The id of the Invoice to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public Invoice (String id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a Invoice from the db and stores it in the current Invoice object
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

            String sqlcode = "SELECT id, prnumber, invoicenumber, datereceived, invoicedate, taxpaid, datepaid, doebilled, comments, status, enteredby, approvedby, dateapproved ";
            sqlcode += "FROM " + SCHEMAPATH + ".invoices WHERE id='" + id + "'";

//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String myID = rs.getString(1);
            if (myID.equals(id)) {
                ID = myID;
                prnumber = rs.getString(2);
                invoiceNumber = rs.getString(3);
                dateReceived = rs.getTimestamp(4);
                invoiceDate = rs.getTimestamp(5);
                taxPaid = ((rs.getString(6).equals("T")) ? true : false);
                datePaid = rs.getTimestamp(7);
                clientBilled = rs.getTimestamp(8);
                comments = rs.getString(9);
                status = rs.getInt(10);
                enteredBy = rs.getInt(11);
                approvedBy = rs.getInt(12);
                dateApproved = rs.getTimestamp(13);

                isNew = false;
                items = InvoiceDetail.getItemList(myConn, ID);


            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Invoice lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Invoice lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }



    /**
    * Get a list of Invoice from the DB
    *
    * @param myConn       Connection to the database
    * @param pr           purchase document to lookup
    */
    public static Invoice[] getItemList(DbConn myConn, String pr) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        Invoice[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            sqlFrom = " FROM " + mySchemaPath + ".invoices ";
            sqlWhere = " WHERE prnumber='" + pr + "' ";
            sqlOrderBy = " ORDER BY datereceived ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Invoice[returnSize];

            sqlcode = "SELECT id, prnumber, invoicenumber, datereceived, invoicedate, taxpaid, datepaid, doebilled, comments, status, enteredby, approvedby, dateapproved" + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
//System.out.println("Invoice-getItemList - Got Here 1");
            while (rs.next()) {
                item[i] = new Invoice();
                item[i].ID = rs.getString(1);
                item[i].prnumber = rs.getString(2);
                item[i].invoiceNumber = rs.getString(3);
                item[i].dateReceived = rs.getTimestamp(4);
                item[i].invoiceDate = rs.getTimestamp(5);
                item[i].taxPaid = ((rs.getString(6).equals("T")) ? true : false);
                item[i].datePaid = rs.getTimestamp(7);
                item[i].clientBilled = rs.getTimestamp(8);
                item[i].comments = rs.getString(9);
                item[i].status = rs.getInt(10);
                item[i].enteredBy = rs.getInt(11);
                item[i].approvedBy = rs.getInt(12);
                item[i].dateApproved = rs.getTimestamp(13);

                item[i].items = InvoiceDetail.getItemList(myConn, item[i].ID);

                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Invoice - getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Invoice - getItemList");
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

    public int getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(int approvedBy) {
        this.approvedBy = approvedBy;
    }

    public java.util.Date getClientBilled() {
        return clientBilled;
    }

    public void setClientBilled(java.util.Date clientBilled) {
        this.clientBilled = clientBilled;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public java.util.Date getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(java.util.Date dateApproved) {
        this.dateApproved = dateApproved;
    }

    public java.util.Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(java.util.Date datePaid) {
        this.datePaid = datePaid;
    }

    public java.util.Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(java.util.Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public int getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(int enteredBy) {
        this.enteredBy = enteredBy;
    }

    public java.util.Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(java.util.Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public InvoiceDetail[] getItems() {
        return items;
    }

    public String getPrnumber() {
        return prnumber;
    }

    public void setPrnumber(String prnumber) {
        this.prnumber = prnumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isTaxPaid() {
        return taxPaid;
    }

    public void setTaxPaid(boolean taxPaid) {
        this.taxPaid = taxPaid;
    }




}
