package gov.ymp.iats.model;

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
* SAAttachment is the class to hold all attributes for SAAttachment object  
*
* @author   Shuhei Higashi
*/
public class SAAttachment {
    int ID = 0;
    
    private int saID = 0;
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
        SCHEMA = "sat";
        SCHEMAPATH = "sat";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty SAAttachment object
    */
    public SAAttachment () {
        ID = 0;
        init();
    }


    /**
    * Creates an SAAttachment object and uses the given id to populate it from the db
    *
    * @param id     The id of the SAAttachment to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public SAAttachment (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Creates an SAAttachment object and saves it to the db
    *
    * @param wr      The work request id of the SAAttachment (int)
    * @param txt     The filename of the SAAttachment (String)
    * @param myConn  Connection to the database
    */
    public SAAttachment (int sa, String txt, DbConn myConn) {
        init();
        saID = sa;
        filename = txt;
        save(myConn);
    }


    /**
    * Retrieves a SAAttachment from the db and stores it in the current SAAttachment object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a SAAttachment from the db and stores it in the current SAAttachment object
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

            String sqlcode = "SELECT id, filename, pdf, said FROM " + SCHEMAPATH + ".pdfs WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            int mySA = rs.getInt(4);
            Blob myImage = (Blob) rs.getBlob(3);
            String myFilename = rs.getString(2);
            if (myID == id) {
                ID = myID;
                saID = mySA;
                filename = myFilename;
                image = myImage;
                //sqlcode = "SELECT mimetype FROM " + SCHEMAPATH + ".mimetypes WHERE filetype='" + myFilename.substring(myFilename.lastIndexOf(".") + 1) + "'";
                //rs = stmt.executeQuery(sqlcode);
                //rs.next();
                //mimeType = rs.getString(1);
                mimeType = "pdf";
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SAAttachment lookup");
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
            sqlcode = "SELECT pdf FROM " + SCHEMAPATH + ".pdfs WHERE id=" + ID +" FOR UPDATE";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            image = rs.getBlob(1);
//System.out.println("SAAttachment - getImageBytes - Got Here 1, image.length: " + image.length());
            bytes = image.getBytes((long) 1, (int) image.length());
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SAAttachment image bytes retrieve");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SAAttachment image bytes retrieve");
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
    * Retrieve the id for the current SAAttachment
    *
    * @return id    The id for the current SAAttachment
    */
    public int getID() {
        int id = ID;
        return(id);
    }


    /**
    * Retrieve the filename for the current SAAttachment
    *
    * @return filename    The filename for the current SAAttachment
    */
    public String getFilename() {
        return(filename);
    }


    /**
    * Retrieve the mimeType for the current SAAttachment
    *
    * @return mimeType    The mime type for the current SAAttachment
    */
    public String getMimeType() {
        return(mimeType);
    }


    /**
    * Set the filename for the current SAAttachment
    *
    * @param txt     The new filename of the SAAttachment (String)
    */
    public void setFilename(String txt) {
        filename = txt;
    }


    /**
    * Save the current SAAttachment to the DB
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
                sqlcode = "SELECT " + SCHEMAPATH + ".PDF_SEQ.NEXTVAL FROM dual";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID;

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".pdfs (id, filename, pdf, said) " +
                    "VALUES (" + ID + ", " + saID + ", EMPTY_BLOB(), ?" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, filename);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".pdfs " +
                      "SET said = " + saID + ", filename = ? WHERE id= " + ID +
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
            //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SAAttachment save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SAAttachment save");
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
    * Save the current SAAttachment to the DB
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
            sqlcode = "SELECT pdf FROM " + SCHEMAPATH + ".pdfs WHERE id=" + ID +" FOR UPDATE";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
//System.out.println("SAAttachment - Got Here 1");
            image = rs.getBlob(1);
//System.out.println("SAAttachment - Got Here 2");
            byte[] bytes = buffer.toByteArray();
//System.out.println("SAAttachment - Got Here 3");
            image.setBytes(1, bytes);
//System.out.println("SAAttachment - Got Here 4");
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SAAttachment set image");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SAAttachment set image");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get a list of SAAttachment from the DB
    *
    * @param myConn     Connection to the database
    */
    public static SAAttachment[] getSAList(DbConn myConn, int sa) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        SAAttachment[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "sat";

            sqlFrom = " FROM " + mySchemaPath + ".pdfs ";
            sqlWhere = " WHERE said=" + sa;

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new SAAttachment[returnSize];

            sqlcode = "SELECT id, filename, pdf, said " + sqlFrom + sqlWhere  + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new SAAttachment();
                item[i].ID = rs.getInt(0);
                item[i].saID = rs.getInt(3);
                item[i].filename = rs.getString(1);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SAAttachment lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
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
