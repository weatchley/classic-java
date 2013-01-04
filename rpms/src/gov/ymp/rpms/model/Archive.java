package gov.ymp.rpms.model;

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
* Archive is the class for archived reports in the db
*
* @author   Bill Atchley
*/
public class Archive {
    int ID = 0;
    private String name = null;
    private java.util.Date dateAdded = null;
    private java.sql.Timestamp time = null;
    private String commentText = "Archive";
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
        SCHEMA = "rpms";
        SCHEMAPATH = "rpms";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty Archive object
    */
    public Archive () {
        ID = 0;
        init();
    }


    /**
    * Creates an Archive object and uses the given id to populate it from the db
    *
    * @param id     The id of the Archive to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public Archive (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Creates an Archive object and uses the given date to populate it and save it to the db
    *
    * @param txt     The name of the Archive (String)
    * @param myConn  Connection to the database
    * @param userID  The id of the user performing the action (long)
    */
    public Archive (String txt, DbConn myConn, long userID) {
        init();
        java.util.Date dNow = new java.util.Date();
        name = txt;
        dateAdded = dNow;
        save(myConn, userID);
    }


    /**
    * Creates an Archive object and uses the given date to populate it and save it to the db
    *
    * @param txt     The name of the Archive (String)
    * @param dt      The date of the Archive (java.util.Date)
    * @param myConn  Connection to the database
    * @param userID  The id of the user performing the action (long)
    */
    public Archive (String txt, java.util.Date dt, DbConn myConn, long userID) {
        init();
        name = txt;
        dateAdded = dt;
        save(myConn, userID);
    }


    /**
    * Creates an Archive object and uses the given date to populate it and save it to the db
    *
    * @param txt     The name of the Archive (String)
    * @param dt      The date of the Archive (java.util.Date)
    * @param comtxt  The comment text of the Archive (String)
    * @param myConn  Connection to the database
    * @param userID  The id of the user performing the action (long)
    */
    public Archive (String txt, java.util.Date dt, String comtxt, DbConn myConn, long userID) {
        init();
        name = txt;
        dateAdded = dt;
        commentText = comtxt;
        save(myConn, userID);
    }


    /**
    * Retrieves a Archive from the db and stores it in the current Archive object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a Archive from the db and stores it in the current Archive object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, DbConn myConn) {
        ID = 0;
        name = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, name, dateadded, commenttext, image FROM " + SCHEMAPATH + ".archive WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            String myName = rs.getString(2);
            java.util.Date myDate = rs.getTimestamp(3);
            java.sql.Timestamp myTime = rs.getTimestamp(3);
//System.out.println("Archive - Date: " + rs.getDate(3) + ", Time: " + rs.getTime(3) + ", Timestamp: " + rs.getTimestamp(3) + ", Time from Date: " + myDate.getTime());
            //myDate.setTime((long) rs.getTime(3));
            String myCommentText = rs.getString(4);
            Blob myImage = (Blob) rs.getBlob(5);
            if (myID == id) {
                ID = myID;
                name = myName;
                dateAdded = myDate;
                time = myTime;
                commentText = myCommentText;
                image = myImage;
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Archive lookup");
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
            sqlcode = "SELECT image FROM " + SCHEMAPATH + ".archive WHERE id=" + ID +" FOR UPDATE";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
//System.out.println("Archive - getImageBytes - Got Here 1");
            image = rs.getBlob(1);
            bytes = image.getBytes((long) 1, (int) image.length());
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Archive image bytes retrieve");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Archive image bytes retrieve");
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
    * Retrieve the id for the current Archive
    *
    * @return id    The id for the current Archive
    */
    public int getID() {
        int id = ID;
        return(id);
    }


    /**
    * Retrieve the name for the current Archive
    *
    * @return name    The name for the current Archive
    */
    public String getName() {
        return(name);
    }


    /**
    * Retrieve the dateAdded for the current Archive
    *
    * @return dateAdded    The date added for the current Archive
    */
    public java.util.Date getDateAdded() {
        return(dateAdded);
    }


    /**
    * Retrieve the time stamp for the current Archive
    *
    * @return time    The time added for the current Archive
    */
    public java.sql.Timestamp getDateTime() {
        return(time);
    }


    /**
    * Retrieve the comment text for the current Archive
    *
    * @return text    The comment text for the current Archive
    */
    public String getCommentText() {
        return(commentText);
    }


    /**
    * Set the name for the current Archive
    *
    * @param txt     The new name of the Archive (String)
    */
    public void setName(String txt) {
        name = txt;
    }


    /**
    * Set the dateAdded for the current Archive
    *
    * @param dt     The new dateAdded of the Archive (String)
    */
    public void setDateAdded(java.util.Date dt) {
        dateAdded = dt;
    }


    /**
    * Set the comment text for the current Archive
    *
    * @param txt     The comment text of the Archive (String)
    */
    public void setCommentText(String txt) {
        commentText = txt;
    }


    /**
    * Save the current Archive to the DB
    *
    * @param myConn     Connection to the database
    * @param userID     The id of the user performing the action (long)
    */
    public void save(DbConn myConn, long userID) {
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
                sqlcode = "SELECT " + SCHEMAPATH + ".ARCHIVE_ID_SEQ.NEXTVAL FROM dual";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID;

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".archive (id, name, dateadded, commenttext, image) " +
                    "VALUES (" + ID + ", ?, ?, ?, EMPTY_BLOB()" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, name);
                pstmt.setTime(++sqlID, new java.sql.Time(dateAdded.getTime()));
                pstmt.setString(++sqlID, commentText);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".archive " +
                      "SET name = ?, commenttext = ? " +
                      "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, name);
                pstmt.setString(++sqlID, commentText);
                rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - Archive save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - Archive save");
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
    * Save the current Archive to the DB
    *
    * @param myConn     Connection to the database
    * @param buffer     byte stream of image contents (ByteArrayOutputStream)
    * @param userID     The id of the user performing the action (long)
    */
    public void setImage(DbConn myConn, ByteArrayOutputStream buffer, long userID) {
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
            sqlcode = "SELECT image FROM " + SCHEMAPATH + ".archive WHERE id=" + ID +" FOR UPDATE";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
//System.out.println("Archive - Got Here 1");
            image = rs.getBlob(1);
            byte[] bytes = buffer.toByteArray();
            image.setBytes(1, bytes);
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - Archive set image");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - Archive set image");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get a list of Archive from the DB
    *
    * @param myConn     Connection to the database
    */
    public static Archive[] getItemList(DbConn myConn) {
        return getItemList(myConn, "name, dateadded");
    }


    /**
    * Get a list of Archive from the DB
    *
    * @param myConn     Connection to the database
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static Archive[] getItemList(DbConn myConn, String sort) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        Archive[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "rpms";

            //sqlFrom = " FROM " + SCHEMAPATH + ".archive ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".archive ";
            sqlFrom = " FROM " + mySchemaPath + ".archive ";

            sqlOrderBy = (!sort.equals("none")) ? " ORDER BY " + sort + " " : "";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Archive[returnSize];

            sqlcode = "SELECT id, name, dateadded, commenttext " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new Archive();
                item[i].ID = rs.getInt(1);
                item[i].name = rs.getString(2);
                item[i].dateAdded = (java.util.Date) rs.getDate(3);
                item[i].time = rs.getTimestamp(3);
                item[i].commentText = rs.getString(4);
                item[i].image = null;
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Archive lookup");
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
