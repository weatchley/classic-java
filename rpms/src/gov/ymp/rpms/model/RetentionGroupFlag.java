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
* RetentionPeriod is the class for RetentionPeriods in the db
*
* @author   Bill Atchley
*/
public class RetentionGroupFlag {
    private int ID = 0;
    private String code = null;
    private String text = null;
    private int sortOrder = 0;
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
    * Creates a new empty RetentionGroupFlag object
    */
    public RetentionGroupFlag () {
        ID = 0;
        init();
    }


    /**
    * Creates a RetentionGroupFlag object and uses the given id to populate it from the db
    *
    * @param id     The id of the RetentionGroupFlag to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public RetentionGroupFlag (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a RetentionGroupFlag from the db and stores it in the current RetentionGroupFlag object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a RetentionGroupFlag from the db and stores it in the current RetentionGroupFlag object
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

            String sqlcode = "SELECT id, code, description, sortorder FROM " + SCHEMAPATH + ".retention_group_flags WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            String myCode = rs.getString(2);
            String myText = rs.getString(3);
            int mySortOrder = rs.getInt(4);
            if (myID == id) {
                ID = myID;
                code = myCode;
                text = myText;
                sortOrder = mySortOrder;
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - RetentionGroupFlag lookup");
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
    * Retrieve the id for the current RetentionGroupFlag
    *
    * @return id    The id for the current RetentionGroupFlag
    */
    public int getID() {
        int id = ID;
        return(id);
    }



    /**
    * Retrieve the code for the current RetentionGroupFlag
    *
    * @return     The code for the current RetentionGroupFlag
    */
    public String getCode() {
        return(code);
    }


    /**
    * Retrieve the text for the current RetentionGroupFlag
    *
    * @return text    The text for the current RetentionGroupFlag
    */
    public String getDescription() {
        return(text);
    }


    /**
    * Retrieve the first (maxLen) characters of text from the current RetentionGroupFlag
    *
    * @param maxLen   The max number of characters to retrieve from the front of the string (int)
    * @return text    The text for the current RetentionGroupFlag
    */
    public String getDescriptionBrief(int maxLen) {
        String myText = "";
        myText = text;
        return(HtmlUtils.getDisplayString(myText, maxLen));
    }


    /**
    * Retrieve the sort order for the current RetentionGroupFlag
    *
    * @return     The sort order for the current RetentionGroupFlag
    */
    public int getSortOrder() {
        return(sortOrder);
    }


    /**
    * Set the code for the current RetentionGroupFlag
    *
    * @param txt     The new code of the RetentionGroupFlag (String)
    */
    public void setCode(String txt) {
        code = txt;
    }


    /**
    * Set the text for the current RetentionGroupFlag
    *
    * @param txt     The new text of the RetentionGroupFlag (String)
    */
    public void setText(String txt) {
        text = txt;
    }


    /**
    * Set the text for the current RetentionGroupFlag
    *
    * @param txt     The new text of the RetentionGroupFlag (String)
    */
    public void setDescription(String txt) {
        text = txt;
    }


    /**
    * Set the sort order for the current RetentionGroupFlag
    *
    * @param order     The new sort order of the RetentionGroupFlag (String)
    */
    public void setSortOrder(int order) {
        sortOrder = order;
    }


    /**
    * Save the current RetentionGroupFlag to the DB
    *
    * @param myConn     Connection to the database
    */
    public void save(DbConn myConn) {
        save(myConn, 0);
    }


    /**
    * Save the current RetentionGroupFlag to the DB
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
                sqlcode = "SELECT MAX(id) FROM " + SCHEMAPATH + ".retention_group_flags";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID + 1;

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".retention_group_flags (id, code, description, sortorder) " +
                    "VALUES (" + ID + ", " +
                    ((code == null || code == "n/a") ? "NULL" : "?") +
                    ((text == null || text == "n/a") ? "NULL" : "?") +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                if (code != null && code != "n/a") {
                    pstmt.setString(++sqlID, code);
                }
                if (text != null && text != "n/a") {
                    pstmt.setString(++sqlID, text);
                }
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".retention_group_flags SET " +
                    "code = " + ((code == null || code == "n/a") ? "NULL" : "?") + ", " +
                    "description = " + ((text == null || text == "n/a") ? "NULL" : "?") + ", " +
                    "sortorder = " + sortOrder + " " +
                    "WHERE id = " + ID + "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                if (code != null && code != "n/a") {
                    pstmt.setString(++sqlID, code);
                }
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
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - RetentionGroupFlag save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - RetentionGroupFlag save");
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
    * Remove the current RetentionGroupFlag from the DB
    *
    * @param myConn     Connection to the database
    */
    public void drop(DbConn myConn) {
        drop(myConn, 0);
    }


    /**
    * Remove the current RetentionGroupFlag from the DB
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
                "DELETE FROM " + SCHEMAPATH + ".retention_group_flags WHERE id = " + ID
                );
            rows = pstmt.executeUpdate();


            ID = 0;
            text = null;
            isNew = true;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - RetentionGroupFlag drop");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - RetentionGroupFlag drop");
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
    * Get a list of RetentionGroupFlag from the DB
    *
    */
    public static RetentionGroupFlag[] getItemList() {
        DbConn myConn = new DbConn();
        RetentionGroupFlag[] myList = getItemList(myConn, (int[]) null, "sortorder, description", "all");
        myConn.release();
        return myList;
    }


    /**
    * Get a list of RetentionGroupFlag from the DB
    *
    * @param myConn     Connection to the database
    */
    public static RetentionGroupFlag[] getItemList(DbConn myConn) {
        return getItemList(myConn, (int[]) null, "sortorder, description", "all");
    }


    /**
    * Get a list of RetentionGroupFlag from the DB
    *
    * @param myConn     Connection to the database
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static RetentionGroupFlag[] getItemList(DbConn myConn, String sort) {
        return getItemList(myConn, (int[]) null, sort, "all");
    }


    /**
    * Get a list of RetentionGroupFlag from the DB
    *
    * @param myConn     Connection to the database
    * @param list       An int array of id's to be fetched from the db
    */
    public static RetentionGroupFlag[] getItemList(DbConn myConn, int[] list) {
        return getItemList(myConn, list, "none", "subset");
    }


    /**
    * Get a list of RetentionGroupFlag from the DB
    *
    * @param myConn     Connection to the database
    * @param list       An int array of id's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static RetentionGroupFlag[] getItemList(DbConn myConn, int[] list, String sort) {
        return getItemList(myConn, list, sort, "subset");
    }


    /**
    * Get a list of RetentionGroupFlag from the DB
    *
    * @param myConn     Connection to the database
    * @param list       An int array of id's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    * @param flag       A string defining if all records are returned or just a subset
    */
    public static RetentionGroupFlag[] getItemList(DbConn myConn, int[] list, String sort, String flag) {
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
        RetentionGroupFlag[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            stmt2 = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "rpms";

            //sqlFrom = " FROM " + SCHEMAPATH + ".retention_group_flags ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".retention_group_flags ";
            sqlFrom = " FROM " + mySchemaPath + ".retention_group_flags ";
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
            item = new RetentionGroupFlag[returnSize];

//            sqlcode = "SELECT id, code, description, sortorder " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
            sqlcode = "SELECT id, code, description, sortorder " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new RetentionGroupFlag();
                item[i].ID = rs.getInt(1);
                item[i].code = rs.getString(2);
                item[i].text = rs.getString(3);
                item[i].sortOrder = rs.getInt(4);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - RetentionGroupFlag lookup");
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


    /**
    * Get a hashmap of RetentionGroupFlag from the DB
    *
    * @param myConn     Connection to the database
    */
    public static HashMap getItemHM(DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        HashMap item = new HashMap();
        RetentionGroupFlag rgf = null;

        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "rpms";

            sqlcode = "SELECT id, code, description, sortorder FROM " + mySchemaPath + ".retention_group_flags";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            while (rs.next()) {
                //item.put(rs.getString(1), rs.getString(3));
                rgf = new RetentionGroupFlag();
                rgf.ID = rs.getInt(1);
                rgf.code = rs.getString(2);
                rgf.text = rs.getString(3);
                rgf.sortOrder = rs.getInt(4);
                item.put(rgf.ID, rgf);
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - RecordSeries getRetentionGroupFlagsHM");
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
