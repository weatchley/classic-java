package gov.ymp.slts.model;

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
    private int productID = 0;
    private int transactionID = 0;
    private String mimeType = null;
    private String name = null;
    private int type = 0;
    private String typeDescription = null;
    private String description = null;
    private Blob contents = null;
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
        SCHEMA = "slts";
        SCHEMAPATH = "slts";
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
    * Creates an Attachment object and saves it to the db
    *
    * @param prod    The product id of the Attachment (int)
    * @param trans   The transaction id of the Attachment (int)
    * @param fname   The filename of the Attachment (String)
    * @param typ     The attachment type (int)
    * @param descr   The description of the Attachment (String)
    * @param myConn  Connection to the database
    */
    public Attachment (int prod, int trans, String fname, int typ, String descr, DbConn myConn) {
        init();
        productID = prod;
        transactionID = trans;
        name = fname;
        type = typ;
        description = descr;
        save(myConn);
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

            String sqlcode = "SELECT id, productid, transactionid, name, type, description, contents FROM " + SCHEMAPATH + ".attachments WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            int myProd = rs.getInt(2);
            int myTrans = rs.getInt(3);

            if (myID == id) {
                ID = myID;
                productID = rs.getInt(2);
                transactionID = rs.getInt(3);
                name = rs.getString(4);
                type = rs.getInt(5);
                description = rs.getString(6);
                contents = rs.getBlob(7);
                sqlcode = "SELECT type FROM " + SCHEMAPATH + ".attach_type WHERE id=" + type;
                rs2 = stmt.executeQuery(sqlcode);
                rs2.next();
                typeDescription = rs2.getString(1);
                mimeType = FilesItem.lookupMimeType(myConn, (name.substring(name.lastIndexOf(".") + 1)));
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Attachment lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Attachment lookup");
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
            sqlcode = "SELECT contents FROM " + SCHEMAPATH + ".attachments WHERE id=" + ID +" FOR UPDATE";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            contents = rs.getBlob(1);
//System.out.println("Attachment - getImageBytes - Got Here 1, contents.length: " + contents.length());
            bytes = contents.getBytes((long) 1, (int) contents.length());
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Attachment image bytes retrieve");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Attachment image bytes retrieve");
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
    * Retrieve the id for the current Attachment
    *
    * @return id    The id for the current Attachment
    */
    public int getID() {
        int id = ID;
        return(id);
    }

    public int getProductID() {
        return productID;
    }

    public int getTransactionID() {
        return transactionID;
    }


    /**
    * Retrieve the filename for the current Attachment
    *
    * @return name    The filename for the current Attachment
    */
    public String getName() {
        return(name);
    }


    /**
    * Retrieve the mimeType for the current Attachment
    *
    * @return mimeType    The mime type for the current Attachment
    */
    public String getMimeType() {
        return(mimeType);
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public String getTypeDescription() {
        return typeDescription;
    }



    /**
    * Set the filename for the current Attachment
    *
    * @param txt     The new filename of the Attachment (String)
    */
    public void setName(String txt) {
        name = txt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(int type) {
        this.type = type;
    }


    /**
    * Save the current Attachment to the DB
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
                sqlcode = "SELECT " + SCHEMAPATH + ".ATTACHMENTS_ID_SEQ.NEXTVAL FROM dual";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID;

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".attachments (id, productid, transactionid, name, type, description, contents) " +
                    "VALUES (" + ID + ", " + productID + ", " + transactionID + ", ?, " + type + ", ?, EMPTY_BLOB()" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, name);
                pstmt.setString(++sqlID, description);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".attachments " +
                      "SET productid = " + productID + ", transactionid = " + transactionID + ", name = ?, type=" + type + ", description = ? WHERE id= " + ID +
                      "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, name);
                pstmt.setString(++sqlID, description);
                rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Attachment save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Attachment save");
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
    * Save the current Attachment to the DB
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
            sqlcode = "SELECT contents FROM " + SCHEMAPATH + ".attachments WHERE id=" + ID +" FOR UPDATE";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
//System.out.println("Attachment - Got Here 1");
            contents = rs.getBlob(1);
            byte[] bytes = buffer.toByteArray();
            contents.setBytes(1, bytes);
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Attachment set image");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Attachment set image");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get a list of Attachment from the DB
    *
    * @param myConn     Connection to the database
    * @param prod       product ID to look up
    * @param trans      transaction ID to look up
    */
    public static Attachment[] getItemList(DbConn myConn, int prod, int trans) {
        return getItemList(myConn, prod, trans, null);
    }


    /**
    * Get a list of Attachment from the DB
    *
    * @param myConn     Connection to the database
    * @param prod       product ID to look up
    * @param trans      transaction ID to look up
    * @param searchText   Text to search for
    */
    public static Attachment[] getItemList(DbConn myConn, int prod, int trans, String searchText) {
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
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".attachments a, " + mySchemaPath + ".attach_type at ";
            sqlWhere = " WHERE a.type=at.id" ;
            sqlWhere += (prod > 0) ? " AND a.productid=" + prod : "";
            sqlWhere += (trans > 0) ? " AND a.transactionid=" + trans : "";
            sqlWhere += (searchText != null) ? " AND (LOWER(a.name) LIKE '%" + searchText.toLowerCase() + "%' OR LOWER(a.description) LIKE '%" + searchText.toLowerCase() + "%') " : "";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Attachment[returnSize];

            sqlcode = "SELECT a.id, a.productid, a.transactionid, a.name, a.type, a.description, a.contents, at.type " + sqlFrom + sqlWhere  + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new Attachment();
                item[i].ID = rs.getInt(1);
                item[i].productID = rs.getInt(2);
                item[i].transactionID = rs.getInt(3);
                item[i].name = rs.getString(4);
                item[i].type = rs.getInt(5);
                item[i].description = rs.getString(6);
                item[i].typeDescription = rs.getString(8);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Attachment getItemList lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Attachment getItemList lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }


    /**
    * Get a list of Attachment types from the DB
    *
    * @param myConn     Connection to the database
    */
    public static String[] getTypeList(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        String[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".attach_type ";
            sqlWhere = " ";
            sqlOrderBy = " ORDER BY type ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new String[returnSize+1];
            item[0] = "";

            sqlcode = "SELECT id, type " + sqlFrom + sqlWhere + sqlOrderBy + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[rs.getInt(1)] = rs.getString(2);
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - getTypeList lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - getTypeList lookup");
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
