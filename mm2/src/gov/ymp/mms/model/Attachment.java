package gov.ymp.mms.model;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.items.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
* Attachment is the class for attachments in the db
*
* @author   Bill Atchley
*/
public class Attachment {
    private int ID = 0;
    private String prnumber = null;
    private String fileName = null;
    private Blob data = null;
    private boolean pr = false;
    private boolean rfp = false;
    private boolean po = false;
    private String mimeType = null;
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
    * Creates a new empty Attachment object
    */
    public Attachment () {
        ID = 0;
        init();
    }


    /**
    * Creates an Attachment object and uses the given id to populate it from the db
    *
    * @param id     The id of the Attachment to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public Attachment (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a Attachment from the db and stores it in the current Attachment object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a Attachment from the db and stores it in the current Attachment object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, DbConn myConn) {
        ID = 0;
        //
        String outLine = "";
        ResultSet rs = null;
        ResultSet rs2 = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, prnumber, filename, data, pr, rfp, po FROM " + SCHEMAPATH + ".attachments WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);

            if (myID == id) {
                ID = myID;
                prnumber = rs.getString(2);
                fileName = rs.getString(3);
                data = rs.getBlob(4);
                pr = ((rs.getString(5).equals("T")) ? true : false);
                rfp = ((rs.getString(6).equals("T")) ? true : false);
                po = ((rs.getString(7).equals("T")) ? true : false);
                mimeType = FilesItem.lookupMimeType(myConn, (fileName.substring(fileName.lastIndexOf(".") + 1)));
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"mms",0, outLine + " - Attachment lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"mms",0, outLine + " - Attachment lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); rs2.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get the image from the DB
    *
    * @param myConn     Connection to the database
    */
    public byte[] getImageBytes(DbConn myConn) {
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        String sqlcode = "";
        byte [] bytes = null;
        try {

            Connection conn = myConn.conn;


            // Create a Statement
            stmt = conn.createStatement ();
            conn.setAutoCommit(false);
            sqlcode = "SELECT data FROM " + SCHEMAPATH + ".attachments WHERE id=" + ID +" FOR UPDATE";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            data = rs.getBlob(1);
//System.out.println("Attachment - getImageBytes - Got Here 1, data.length: " + data.length());
            bytes = data.getBytes((long) 1, (int) data.length());
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"mms",0, outLine + " - Attachment image bytes retrieve");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"mms",0, outLine + " - Attachment image bytes retrieve");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
        return(bytes);
    }






    /**
    * Get a list of Attachment from the DB
    *
    * @param myConn     Connection to the database
    * @param pr         purchase document to look up
    */
    public static Attachment[] getItemList(DbConn myConn, String pr) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        Attachment[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            sqlFrom = " FROM " + mySchemaPath + ".attachments  ";
            sqlWhere = " WHERE prnumber='" + pr + "' ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Attachment[returnSize];

            sqlcode = "SELECT id, prnumber, filename, data, pr, rfp, po " + sqlFrom + sqlWhere  + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new Attachment();
                item[i].ID = rs.getInt(1);
                item[i].prnumber = rs.getString(2);
                item[i].fileName = rs.getString(3);
                item[i].data = rs.getBlob(4);
                item[i].pr = ((rs.getString(5).equals("T")) ? true : false);
                item[i].rfp = ((rs.getString(6).equals("T")) ? true : false);
                item[i].po = ((rs.getString(7).equals("T")) ? true : false);
                item[i].mimeType = FilesItem.lookupMimeType(myConn, (item[i].fileName.substring(item[i].fileName.lastIndexOf(".") + 1)));

                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"mms",0, outLine + " - getItemList lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"mms",0, outLine + " - getItemList lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isPo() {
        return po;
    }

    public void setPo(boolean po) {
        this.po = po;
    }

    public boolean isPr() {
        return pr;
    }

    public void setPr(boolean pr) {
        this.pr = pr;
    }

    public String getPrnumber() {
        return prnumber;
    }

    public void setPrnumber(String prnumber) {
        this.prnumber = prnumber;
    }

    public boolean isRfp() {
        return rfp;
    }

    public void setRfp(boolean rfp) {
        this.rfp = rfp;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }




}
