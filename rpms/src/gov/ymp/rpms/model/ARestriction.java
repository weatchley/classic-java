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
* ARestriction is the class for Access Restrictions in the db
*
* @author   Bill Atchley
*/
public class ARestriction {
    private String code = "";
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
    * Creates a new empty ARestriction object
    */
    public ARestriction () {
        code = "";
        init();
    }


    /**
    * Creates a ARestriction object and uses the given code to populate it from the db
    *
    * @param myCode The code of the ARestriction to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public ARestriction (String myCode, DbConn myConn) {
        init();
        lookup(myCode, myConn);
    }


    /**
    * Retrieves a ARestriction from the db and stores it in the current ARestriction object
    *
    * @param myCode The code  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (String myCode, DbConn myConn) {
        lookup(myCode, myConn);
    }


    /**
    * Retrieves a ARestriction from the db and stores it in the current ARestriction object
    *
    * @param myCode The code to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (String myCode, DbConn myConn) {
        code = "";
        text = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT code, description FROM " + SCHEMAPATH + ".access_restrictions WHERE code='" + myCode + "'";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String lCode = rs.getString(1);
            String myText = rs.getString(2);
            if (lCode.equals(myCode)) {
                code = lCode;
                text = myText;
                sqlcode = "SELECT count(*) FROM " + SCHEMAPATH + ".crosswalk WHERE accessrestrictions='" + code + "'";
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
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - ARestriction lookup");
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
    * Retrieve the code for the current ARestriction
    *
    * @return code    The code for the current ARestriction
    */
    public String getCode() {
        String myCode = code;
        return(myCode);
    }



    /**
    * Retrieve the text for the current ARestriction
    *
    * @return text    The text for the current ARestriction
    */
    public String getDescription() {
        return(text);
    }


    /**
    * Retrieve the first (maxLen) characters of text from the current ARestriction
    *
    * @param maxLen   The max number of characters to retrieve from the front of the string (int)
    * @return text    The text for the current ARestriction
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
    * Set the code for the current ARestriction
    *
    * @param myCode     The new code of the ARestriction (String)
    */
    public void setCode(String myCode) {
        code = myCode;
    }


    /**
    * Set the text for the current ARestriction
    *
    * @param txt     The new text of the ARestriction (String)
    */
    public void setText(String txt) {
        text = txt;
    }


    /**
    * Set the text for the current ARestriction
    *
    * @param txt     The new text of the ARestriction (String)
    */
    public void setDescription(String txt) {
        text = txt;
    }


    /**
    * Save the current ARestriction to the DB
    *
    * @param myConn     Connection to the database
    */
    public void save(DbConn myConn) {
        save(myConn, 0);
    }


    /**
    * Save the current ARestriction to the DB
    *
    * @param myConn     Connection to the database
    * @param userID     The code of the user performing the action (long)
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

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".access_restrictions (code, description) " +
                    "VALUES ('" + code + "', " +
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
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".access_restrictions SET " +
                    "description = " + ((text == null || text == "n/a") ? "NULL" : "?") + " " +
                    "WHERE code = '" + code + "'; end;";
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
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - ARestriction save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - ARestriction save");
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
    * Remove the current ARestriction from the DB
    *
    * @param myConn     Connection to the database
    */
    public void drop(DbConn myConn) {
        drop(myConn, 0);
    }


    /**
    * Remove the current ARestriction from the DB
    *
    * @param myConn     Connection to the database
    * @param userID     The code of the user performing the action (long)
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
                "DELETE FROM " + SCHEMAPATH + ".access_restrictions WHERE code = '" + code + "'"
                );
            rows = pstmt.executeUpdate();


            code = "";
            text = null;
            isNew = true;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - ARestriction drop");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - ARestriction drop");
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
    * Get a list of ARestriction from the DB
    *
    */
    public static ARestriction[] getItemList() {
        DbConn myConn = new DbConn();
        ARestriction[] myList = getItemList(myConn, (String[]) null, "code", "all");
        myConn.release();
        return myList;
    }


    /**
    * Get a list of ARestriction from the DB
    *
    * @param myConn     Connection to the database
    */
    public static ARestriction[] getItemList(DbConn myConn) {
        return getItemList(myConn, (String[]) null, "code", "all");
    }


    /**
    * Get a list of ARestriction from the DB
    *
    * @param myConn     Connection to the database
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static ARestriction[] getItemList(DbConn myConn, String sort) {
        return getItemList(myConn, (String[]) null, sort, "all");
    }


    /**
    * Get a list of ARestriction from the DB
    *
    * @param myConn     Connection to the database
    * @param list       A String array of code's to be fetched from the db
    */
    public static ARestriction[] getItemList(DbConn myConn, String[] list) {
        return getItemList(myConn, list, "none", "subset");
    }


    /**
    * Get a list of ARestriction from the DB
    *
    * @param myConn     Connection to the database
    * @param list       A String array of code's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static ARestriction[] getItemList(DbConn myConn, String[] list, String sort) {
        return getItemList(myConn, list, sort, "subset");
    }


    /**
    * Get a list of ARestriction from the DB
    *
    * @param myConn     Connection to the database
    * @param list       A String array of code's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    * @param flag       A string defining if all records are returned or just a subset
    */
    public static ARestriction[] getItemList(DbConn myConn, String[] list, String sort, String flag) {
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
        ARestriction[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            stmt2 = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "rpms";

            //sqlFrom = " FROM " + SCHEMAPATH + ".access_restrictions ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".access_restrictions ";
            sqlFrom = " FROM " + mySchemaPath + ".access_restrictions ";
            if (flag.equals("subset")) {
                sqlWhere = " WHERE code IN (";
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
            item = new ARestriction[returnSize];

//            sqlcode = "SELECT code, description " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
            sqlcode = "SELECT code, description " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new ARestriction();
                item[i].code = rs.getString(1);
                item[i].text = rs.getString(2);
                sqlcode2 = "SELECT count(*) FROM " + SCHEMAPATH + ".crosswalk WHERE accessrestrictions='" + item[i].code + "'";
//System.out.println(sqlcode);
                rs2 = stmt2.executeQuery(sqlcode2);
                rs2.next();
                item[i].inUse = ((rs2.getInt(1) > 0) ? true : false);
                rs2.close();
                item[i].isNew = false;
//System.out.println(item[i].code + " - " + item[i].text);
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - ARestriction lookup");
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
