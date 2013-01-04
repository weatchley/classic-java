package gov.ymp.pclwr.model;

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
* SoftwareAttachment is the class for archived reports in the db
*
* @author   Bill Atchley
*/
public class SoftwareAttachment {
    int ID = 0;
    private int wrID = 0;
    private String filename = null;
    private String mimeType = null;
    private Blob image = null;
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
        SCHEMA = "pcl";
        SCHEMAPATH = "pcl";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty SoftwareAttachment object
    */
    public SoftwareAttachment () {
        ID = 0;
        init();
    }


    /**
    * Creates an SoftwareAttachment object and uses the given id to populate it from the db
    *
    * @param id     The id of the SoftwareAttachment to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public SoftwareAttachment (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Creates an SoftwareAttachment object and saves it to the db
    *
    * @param wr      The work request id of the SoftwareAttachment (int)
    * @param txt     The filename of the SoftwareAttachment (String)
    * @param myConn  Connection to the database
    */
    public SoftwareAttachment (int wr, String txt, DbConn myConn) {
        init();
        wrID = wr;
        filename = txt;
        save(myConn);
    }


    /**
    * Retrieves a SoftwareAttachment from the db and stores it in the current SoftwareAttachment object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a SoftwareAttachment from the db and stores it in the current SoftwareAttachment object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, DbConn myConn) {
        ID = 0;
        filename = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT attachmentid, requestid, attachment, filename FROM " + SCHEMAPATH + ".work_request_attachments WHERE attachmentid=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            int myWR = rs.getInt(2);
            Blob myImage = (Blob) rs.getBlob(3);
            String myFilename = rs.getString(4);
            if (myID == id) {
                ID = myID;
                wrID = myWR;
                filename = myFilename;
                image = myImage;
                sqlcode = "SELECT mimetype FROM " + SCHEMAPATH + ".mimetypes WHERE filetype='" + myFilename.substring(myFilename.lastIndexOf(".") + 1) + "'";
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                mimeType = rs.getString(1);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SoftwareAttachment lookup");
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
            sqlcode = "SELECT attachment FROM " + SCHEMAPATH + ".work_request_attachments WHERE attachmentid=" + ID +" FOR UPDATE";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            image = rs.getBlob(1);
//System.out.println("SoftwareAttachment - getImageBytes - Got Here 1, image.length: " + image.length());
            bytes = image.getBytes((long) 1, (int) image.length());
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SoftwareAttachment image bytes retrieve");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SoftwareAttachment image bytes retrieve");
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
    * Retrieve the id for the current SoftwareAttachment
    *
    * @return id    The id for the current SoftwareAttachment
    */
    public int getID() {
        int id = ID;
        return(id);
    }


    /**
    * Retrieve the filename for the current SoftwareAttachment
    *
    * @return filename    The filename for the current SoftwareAttachment
    */
    public String getFilename() {
        return(filename);
    }


    /**
    * Retrieve the mimeType for the current SoftwareAttachment
    *
    * @return mimeType    The mime type for the current SoftwareAttachment
    */
    public String getMimeType() {
        return(mimeType);
    }


    /**
    * Set the filename for the current SoftwareAttachment
    *
    * @param txt     The new filename of the SoftwareAttachment (String)
    */
    public void setFilename(String txt) {
        filename = txt;
    }


    /**
    * Save the current SoftwareAttachment to the DB
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
                sqlcode = "SELECT " + SCHEMAPATH + ".WORK_REQUEST_ATTACH_SEQ.NEXTVAL FROM dual";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID;

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".work_request_attachments (attachmentid, requestid, attachment, filename) " +
                    "VALUES (" + ID + ", " + wrID + ", EMPTY_BLOB(), ?" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, filename);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".work_request_attachments " +
                      "SET reqeustid = " + wrID + ", filename = ? WHERE attachmentid= " + ID +
                      "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, filename);
                rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SoftwareAttachment save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SoftwareAttachment save");
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
    * Save the current SoftwareAttachment to the DB
    *
    * @param myConn     Connection to the database
    * @param buffer     byte stream of image contents (ByteArrayOutputStream)
    */
    public void setImage(DbConn myConn, ByteArrayOutputStream buffer) {
        //save funciton
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        String sqlcode = "";
        try {

            Connection conn = myConn.conn;


            // Create a Statement
            stmt = conn.createStatement ();
            conn.setAutoCommit(false);
            sqlcode = "SELECT attachment FROM " + SCHEMAPATH + ".work_request_attachments WHERE attachmentid=" + ID +" FOR UPDATE";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
//System.out.println("SoftwareAttachment - Got Here 1");
            image = rs.getBlob(1);
//System.out.println("SoftwareAttachment - Got Here 2");
            byte[] bytes = buffer.toByteArray();
//System.out.println("SoftwareAttachment - Got Here 3");
            image.setBytes(1, bytes);
//System.out.println("SoftwareAttachment - Got Here 4");
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SoftwareAttachment set image");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SoftwareAttachment set image");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get a list of SoftwareAttachment from the DB
    *
    * @param myConn     Connection to the database
    */
    public static SoftwareAttachment[] getItemList(DbConn myConn, int wr) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        SoftwareAttachment[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "pcl";

            //sqlFrom = " FROM " + SCHEMAPATH + ".work_request_attachments ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".work_request_attachments ";
            sqlFrom = " FROM " + mySchemaPath + ".work_request_attachments ";
            sqlWhere = " WHERE requestid=" + wr;

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new SoftwareAttachment[returnSize];

            sqlcode = "SELECT attachmentid, requestid, attachment, filename " + sqlFrom + sqlWhere  + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new SoftwareAttachment();
                item[i].ID = rs.getInt(1);
                item[i].wrID = rs.getInt(2);
                //item[i].file = rs.getBlob(3)
                item[i].filename = rs.getString(4);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SoftwareAttachment lookup");
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




}
