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
* CCategory is the class for Citation Categories in the db
*
* @author   Bill Atchley
*/
public class CCategory {
    private int ID = 0;
    private String text = null;
    private boolean inUse = false;
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
    * Creates a new empty CCategory object
    */
    public CCategory () {
        ID = 0;
        init();
    }


    /**
    * Creates a CCategory object and uses the given id to populate it from the db
    *
    * @param id     The id of the CCategory to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public CCategory (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a CCategory from the db and stores it in the current CCategory object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a CCategory from the db and stores it in the current CCategory object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, DbConn myConn) {
        ID = 0;
        text = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, description FROM " + SCHEMAPATH + ".citation_categories WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            String myText = rs.getString(2);
            if (myID == id) {
                ID = myID;
                text = myText;
                sqlcode = "SELECT count(*) FROM " + SCHEMAPATH + ".record_series WHERE citation=" + id;
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                inUse = ((rs.getInt(1) > 0) ? true : false);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - CCategory lookup");
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
    * Retrieve the id for the current CCategory
    *
    * @return id    The id for the current CCategory
    */
    public int getID() {
        int id = ID;
        return(id);
    }



    /**
    * Retrieve the text for the current CCategory
    *
    * @return text    The text for the current CCategory
    */
    public String getDescription() {
        return(text);
    }


    /**
    * Retrieve the first (maxLen) characters of text from the current CCategory
    *
    * @param maxLen   The max number of characters to retrieve from the front of the string (int)
    * @return text    The text for the current CCategory
    */
    public String getDescriptionBrief(int maxLen) {
        String myText = "";
        myText = text;
        return(HtmlUtils.getDisplayString(myText, maxLen));
    }


    /**
    * Determin if the current item is in use
    *
    * @return flag    If the item is in use
    */
    public boolean getInUse() {
        return(inUse);
    }


    /**
    * Set the text for the current CCategory
    *
    * @param txt     The new text of the CCategory (String)
    */
    public void setText(String txt) {
        text = txt;
    }


    /**
    * Set the text for the current CCategory
    *
    * @param txt     The new text of the CCategory (String)
    */
    public void setDescription(String txt) {
        text = txt;
    }


    /**
    * Save the current CCategory to the DB
    *
    * @param myConn     Connection to the database
    */
    public void save(DbConn myConn) {
        save(myConn, 0);
    }


    /**
    * Save the current CCategory to the DB
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
                sqlcode = "SELECT " + SCHEMAPATH + ".CITATION_CATEGORIES_ID_SEQ.NEXTVAL FROM dual";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID;

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".citation_categories (id, description) " +
                    "VALUES (" + ID + ", " +
                    ((text == null || text == "n/a") ? "NULL" : "?") +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                if (text != null && text != "n/a") {
                    pstmt.setString(++sqlID, text);
                }
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".citation_categories SET " +
                    "description = " + ((text == null || text == "n/a") ? "NULL" : "?") + " " +
                    "WHERE id = " + ID + "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                if (text != null && text != "n/a") {
                    pstmt.setString(++sqlID, text);
                }
                rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - CCategory save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - CCategory save");
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
    * Remove the current CCategory from the DB
    *
    * @param myConn     Connection to the database
    */
    public void drop(DbConn myConn) {
        drop(myConn, 0);
    }


    /**
    * Remove the current CCategory from the DB
    *
    * @param myConn     Connection to the database
    * @param userID     The id of the user performing the action (long)
    */
    public void drop(DbConn myConn, long userID) {
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
//            stmt = conn.createStatement ();
// do auth stuff
            pstmt = conn.prepareStatement(
                "DELETE FROM " + SCHEMAPATH + ".citation_categories WHERE id = " + ID
                );
            rows = pstmt.executeUpdate();


            ID = 0;
            text = null;
            isNew = true;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - CCategory drop");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - CCategory drop");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
            if (pstmt != null)
                try { pstmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get a list of CCategory from the DB
    *
    */
    public static CCategory[] getItemList() {
        DbConn myConn = new DbConn();
        CCategory[] myList = getItemList(myConn, (int[]) null, "description", "all");
        myConn.release();
        return myList;
    }


    /**
    * Get a list of CCategory from the DB
    *
    * @param myConn     Connection to the database
    */
    public static CCategory[] getItemList(DbConn myConn) {
        return getItemList(myConn, (int[]) null, "description", "all");
    }


    /**
    * Get a list of CCategory from the DB
    *
    * @param myConn     Connection to the database
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static CCategory[] getItemList(DbConn myConn, String sort) {
        return getItemList(myConn, (int[]) null, sort, "all");
    }


    /**
    * Get a list of CCategory from the DB
    *
    * @param myConn     Connection to the database
    * @param list       An int array of id's to be fetched from the db
    */
    public static CCategory[] getItemList(DbConn myConn, int[] list) {
        return getItemList(myConn, list, "none", "subset");
    }


    /**
    * Get a list of CCategory from the DB
    *
    * @param myConn     Connection to the database
    * @param list       An int array of id's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static CCategory[] getItemList(DbConn myConn, int[] list, String sort) {
        return getItemList(myConn, list, sort, "subset");
    }


    /**
    * Get a list of CCategory from the DB
    *
    * @param myConn     Connection to the database
    * @param list       An int array of id's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    * @param flag       A string defining if all records are returned or just a subset
    */
    public static CCategory[] getItemList(DbConn myConn, int[] list, String sort, String flag) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        ResultSet rs2 = null;
        Statement stmt2 = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlcode2 = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        CCategory[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            stmt2 = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "rpms";

            //sqlFrom = " FROM " + SCHEMAPATH + ".citation_categories ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".citation_categories ";
            sqlFrom = " FROM " + mySchemaPath + ".citation_categories ";
            if (flag.equals("subset")) {
                sqlWhere = " WHERE id IN (";
                for (int i = 0; i<list.length; i++) {
                    sqlWhere += (i!=0) ? ", " : "";
                    sqlWhere += list[i];
                }
                sqlWhere += ")";
            }

            sqlOrderBy = (!sort.equals("none")) ? " ORDER BY " + sort + " " : "";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new CCategory[returnSize];

//            sqlcode = "SELECT id, description " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
            sqlcode = "SELECT id, description " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new CCategory();
                item[i].ID = rs.getInt(1);
                item[i].text = rs.getString(2);
                sqlcode2 = "SELECT count(*) FROM " + SCHEMAPATH + ".record_series WHERE citation= " + item[i].ID;
//System.out.println(sqlcode);
                rs2 = stmt2.executeQuery(sqlcode2);
                rs2.next();
                item[i].inUse = ((rs2.getInt(1) > 0) ? true : false);
                rs2.close();
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - CCategory lookup");
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
            if (rs2 != null)
                try { rs2.close(); } catch (Exception i) {}
            if (stmt2 != null)
                try { stmt2.close(); } catch (Exception i) {}
        }

        return item;
    }




}
