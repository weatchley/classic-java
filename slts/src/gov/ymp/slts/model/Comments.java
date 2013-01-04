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
* Comments is the class for comments in the db
*
* @author   Bill Atchley
*/
public class Comments {
    private int ID = 0;
    private int productID = 0;
    private int transactionID = 0;
    private java.util.Date dateEntered = null;
    private long enteredBy = 0;
    private String text = null;
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
    * Creates a new empty Comments object
    */
    public Comments () {
        ID = 0;
        init();
    }


    /**
    * Creates a Comments object and uses the given id to populate it from the db
    *
    * @param id     The id of the Comments to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public Comments (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Creates a Comments object and saves it to the db
    *
    * @param prod    The product id of the Comments (int)
    * @param trans   The transaction id of the Comments (int)
    * @param txt     The text of the Comments (String)
    * @param user    The user who entered the comment (long)
    * @param myConn  Connection to the database
    */
    public Comments (int prod, int trans, long user, String txt, DbConn myConn) {
        init();
        productID = prod;
        transactionID = trans;
        enteredBy = user;
        text = txt;
        save(myConn);
    }


    /**
    * Retrieves a Comments from the db and stores it in the current Comments object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a Comments from the db and stores it in the current Comments object
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

            String sqlcode = "SELECT id, productid, transactionid, dateentered, enteredby, text FROM " + SCHEMAPATH + ".comments WHERE id=" + id;
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
                dateEntered = rs.getTimestamp(4);
                enteredBy = rs.getLong(5);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Comments lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }




    /**
    * Retrieve the id for the current Comments
    *
    * @return id    The id for the current Comments
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

    public java.util.Date getDateEntered() {
        return dateEntered;
    }

    public long getEnteredBy() {
        return enteredBy;
    }

    public String getText() {
        return text;
    }





    /**
    * Save the current Comments to the DB
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
                sqlcode = "SELECT " + SCHEMAPATH + ".COMMENTS_ID_SEQ.NEXTVAL FROM dual";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID;

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".comments (id, productid, transactionid, dateentered, enteredby, text) " +
                    "VALUES (" + ID + ", " + productID + ", " + transactionID + ", SYSDATE, " + enteredBy + ", ?" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                //pstmt.setDate(++sqlID, (java.sql.Date) dateEntered);
//System.out.println("Comments.java-Save-Got Here 1");
                pstmt.setString(++sqlID, text);
//System.out.println("Comments.java-Save-Got Here 2");
                rows = pstmt.executeUpdate();
//System.out.println("Comments.java-Save-Got Here 3");
                isNew = false;
// do auth stuff
            } else {
//                sqlcode = "begin UPDATE " + SCHEMAPATH + ".attachments " +
//                      "SET productid = " + productID + ", transactionid = " + transactionID + ", name = ?, type=" + type + ", description = ? WHERE id= " + ID +
//                      "; end;";
////System.out.println(sqlcode);
//                pstmt = conn.prepareStatement(sqlcode);
//                int sqlID = 0;
//                pstmt.setString(++sqlID, name);
//                pstmt.setString(++sqlID, description);
//                rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Comments save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Comments save");
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
    * Get a list of Comments from the DB
    *
    * @param myConn     Connection to the database
    * @param prod       product ID to look up
    * @param trans      transaction ID to look up
    */
    public static Comments[] getItemList(DbConn myConn, int prod, int trans) {
        return getItemList(myConn, prod, trans, null);
    }


    /**
    * Get a list of Comments from the DB
    *
    * @param myConn     Connection to the database
    * @param prod       product ID to look up
    * @param trans      transaction ID to look up
    * @param searchText Text to search for
    */
    public static Comments[] getItemList(DbConn myConn, int prod, int trans, String searchText) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        Comments[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".comments ";
            sqlWhere = " WHERE 1=1 ";
            sqlWhere += (prod > 0) ? " AND productid=" + prod : "";
            sqlWhere += (trans >= 0) ? " AND transactionid=" + trans : "";
            sqlWhere += (searchText != null) ? " AND (LOWER(text) LIKE '%" + searchText.toLowerCase() + "%') " : "";
            sqlOrderBy = " ORDER BY dateentered DESC ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Comments[returnSize];

            sqlcode = "SELECT id, productid, transactionid, dateentered, enteredBy, text " + sqlFrom + sqlWhere + sqlOrderBy + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new Comments();
                item[i].ID = rs.getInt(1);
                item[i].productID = rs.getInt(2);
                item[i].transactionID = rs.getInt(3);
                item[i].dateEntered = rs.getTimestamp(4);
                item[i].enteredBy = rs.getLong(5);
                item[i].text = rs.getString(6);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Comments getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Comments getItemList");
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
