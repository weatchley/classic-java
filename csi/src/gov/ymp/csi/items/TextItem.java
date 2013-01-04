package gov.ymp.csi.items;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
* TextItem is the class for generic text items with optional dates and link
*
* @author   Bill Atchley
*/
public class TextItem {
    private long uID = 0;
    private java.sql.Date date1 = null;
    private java.sql.Date date2 = null;
    private String text = null;
    private String link = null;
    private String textLob = null;
    public String status = null;
    public long owner = 0;
    public UNID myUID = null;
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
        SCHEMA = tempDB.getSchemaName();
        SCHEMAPATH = tempDB.getSchemaPath();
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty TextItem object
    */
    public TextItem () {
        uID = 0;
        init();
    }


    /**
    * Creates a TextItem object and uses the given id to populate it from the db
    *
    * @param id     The id of the TextItem to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public TextItem (long id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Creates a TextItem object and uses the given UNID to populate it from the db
    *
    * @param uid     The UNID of the TextItem to lookup from the db (long)
    * @param myConn  Connection to the database
    */
    public TextItem (UNID uid, DbConn myConn) {
        init();
        lookup(uid.getID(), myConn);
    }


    /**
    * Retrieves a TextItem from the db and stores it in the current TextItem object
    *
    * @param id     The id of the UNID to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public void getInfo (long id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a TextItem from the db and stores it in the current TextItem object
    *
    * @param id     The id of the UNID to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public void lookup (long id, DbConn myConn) {
        uID = 0;
        date1 = null;
        date2 = null;
        text = null;
        link = null;
        status = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            myUID = new UNID(id, myConn);
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, date1, date2, text, link, status, owner, textlob FROM " + SCHEMAPATH + ".text_items WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            long myID = rs.getLong(1);
            java.sql.Date myDate1 = rs.getDate(2);
            java.sql.Date myDate2 = rs.getDate(3);
            String myText = rs.getString(4);
            String myLink = rs.getString(5);
            String myStatus = rs.getString(6);
            long myOwner = rs.getLong(7);
            String myTextLob = rs.getString(8);
            if (myID == id) {
                uID = myID;
                date1 = myDate1;
                date2 = myDate2;
                text = myText;
                link = myLink;
                status = myStatus;
                owner = myOwner;
                isNew = false;
                textLob = myTextLob;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - TextItem lookup");
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
    * Retrieve the UNID id for the current TextItem
    *
    * @return id    The UNID id for the current TextItem
    */
    public long getID() {
        long id = uID;
        return(id);
    }

    /**
    * Retrieve the UNID for the current TextItem
    *
    * @return myUNID    The UNID id for the current TextItem
    */
    public UNID getUNID() {
        UNID myUNID = myUID;
        return(myUNID);
    }


    /**
    * Retrieve the date1 for the current TextItem as a java.sql.Date
    *
    * @return date1    The creation date for the current TextItem
    */
    public java.sql.Date getDate1SQL() {
        return(date1);
    }


    /**
    * Retrieve the date1 for the current TextItem
    *
    * @return date1    The creation date for the current TextItem
    */
    public java.util.Date getDate1() {
        java.util.Date tmpDate = ((date1!=null) ? new java.util.Date(date1.getTime()) : null);
        return(tmpDate);
    }


    /**
    * Retrieve the date2 for the current TextItem as a java.sql.Date
    *
    * @return date2    The creation date for the current TextItem
    */
    public java.sql.Date getDate2SQL() {
        return(date2);
    }


    /**
    * Retrieve the date2 for the current TextItem
    *
    * @return date2    The creation date for the current TextItem
    */
    public java.util.Date getDate2() {
        java.util.Date tmpDate = ((date2!=null) ? new java.util.Date(date2.getTime()) : null);
        return(tmpDate);
    }


    /**
    * Retrieve the text for the current TextItem
    *
    * @return text    The text for the current TextItem
    */
    public String getText() {
        if (text != null && text.indexOf("<isLob>") > -1) {
            return(textLob);
        } else {
            return(text);
        }
    }


    /**
    * Retrieve the first (maxLen) characters of text from the current TextItem
    *
    * @param maxLen   The max number of characters to retrieve from the front of the string (long)
    * @return text    The text for the current TextItem
    */
    public String getTextBrief(int maxLen) {
        String myText = "";
        if (text != null && text.indexOf("<isLob>") > -1) {
            myText = textLob;
        } else {
            myText = text;
        }
        if (myText.length() > maxLen) {
            myText = myText.substring(0, maxLen);
        }
        return(myText);
    }


    /**
    * Retrieve the link for the current TextItem
    *
    * @return link    The link for the current TextItem
    */
    public String getLink() {
        return(link);
    }


    /**
    * Retrieve the status for the current TextItem
    *
    * @return status    The status for the current TextItem
    */
    public String getStatus() {
        return(status);
    }


    /**
    * Retrieve the UNID id for the owner of the current TextItem
    *
    * @return id    The UNID id for thw owner of the current TextItem
    */
    public long getOwner() {
        long id = owner;
        return(id);
    }


    /**
    * Set the date1 date for the current TextItem
    *
    * @param inDate     The new Date1 date of the TextItem (java.sql.Date)
    */
    public void setDate1(java.sql.Date inDate) {
        date1 = inDate;
    }

    /**
    * Set the date1 date for the current TextItem
    *
    * @param inDate     The new Date1 date of the TextItem (java.util.Date)
    */
    public void setDate1(java.util.Date inDate) {
        java.sql.Date tmpDate = new java.sql.Date(inDate.getTime());
        date1 = tmpDate;
    }

    /**
    * Set the date2 date for the current TextItem
    *
    * @param inDate     The new Date2 date of the TextItem (java.sql.Date)
    */
    public void setDate2(java.sql.Date inDate) {
        date2 = inDate;
    }

    /**
    * Set the date2 date for the current TextItem
    *
    * @param inDate     The new Date2 date of the TextItem (java.util.Date)
    */
    public void setDate2(java.util.Date inDate) {
        java.sql.Date tmpDate = new java.sql.Date(inDate.getTime());
        date2 = tmpDate;
    }


    /**
    * Set the text for the current TextItem
    *
    * @param txt     The new text of the TextItem (String)
    */
    public void setText(String txt) {
        int maxSize = 4000;
        int len = txt.length();
        if (len > maxSize) {
            int maxLen = (len > maxSize/2) ? maxSize/2 : len;
            text = txt.substring(0,maxLen);
            text += "<isLob>";
            textLob = txt;
        } else {
            text = txt;
        }
    }


    /**
    * Set the link for the current TextItem
    *
    * @param lnk     The new link of the TextItem (String)
    */
    public void setLink(String lnk) {
        int len = 0;
        if (lnk != null) {
            len = lnk.length();
        }
        int maxLen = (len > 200) ? 200 : len;
        lnk = (lnk != null && lnk.compareTo(" ") > 0 && !lnk.equals("n/a")) ? lnk : null;
        link = (lnk != null) ? lnk.substring(0,maxLen) : null;
    }


    /**
    * Set the status for the current TextItem
    *
    * @param stat     The new status of the TextItem (String)
    */
    public void setStatus(String stat) {
        status = stat;
    }


    /**
    * Set the owner for the current TextItem
    *
    * @param own     The new owner of the TextItem (long)
    */
    public void setOwner(long own) {
        owner = own;
    }


    /**
    * Save the current TextItem to the DB
    *
    * @param myConn     Connection to the database
    */
    public void save(DbConn myConn) {
        save(myConn, 0);
    }


    /**
    * Save the current TextItem to the DB
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
        CLOB textLobPtr = null;
        Writer output = null;
        try {

            Connection conn = myConn.conn;


            // Create a Statement
            // stmt = conn.createStatement ();
            conn.setAutoCommit(false);
            if (isNew) {
                myUID = new UNID();
                myUID.create("textitem", myConn);
                myUID.save(conn);
                uID = myUID.getID();

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".text_items (id, date1, date2, text, link, status, owner, textlob) " +
                    "VALUES (" + uID + ", " +
                    ((date1 == null) ? "NULL" : "?") + ", " +
                    ((date2 == null) ? "NULL" : "?") + ", " +
                    ((text == null || text == "n/a") ? "NULL" : "?") + ", " +
                    ((link == null || link == "n/a") ? "NULL" : "'" + link + "'") + ", " +
                    ((status == null || status == "n/a") ? "NULL" : "'" + status + "'") + ", " +
                    owner + "," +
                    ((textLob == null || textLob == "n/a") ? "NULL" : "?") +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                if (date1 != null) {
                    pstmt.setDate(++sqlID, date1);
                }
                if (date2 != null) {
                    pstmt.setDate(++sqlID, date2);
                }
                if (text != null && text != "n/a") {
                    pstmt.setString(++sqlID, text);
                }
                if (textLob != null && textLob != "n/a") {
                    pstmt.setString(++sqlID, textLob);
                }
                rows = pstmt.executeUpdate();
// do auth stuff
            } else {
                myUID.save(conn);
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".text_items SET " +
                    "date1 = " + ((date1 == null) ? "NULL" : "?") + ", " +
                    "date2 = " + ((date2 == null) ? "NULL" : "?") + ", " +
                    "text = " + ((text == null || text == "n/a") ? "NULL" : "?") + ", " +
                    "link = " + ((link == null || link == "n/a") ? "NULL" : "'" + link + "'") + ", " +
                    "owner = " + owner + ", " +
                    "status = " + ((status == null || status == "n/a") ? "NULL" : "'" + status + "'") + ", " +
                    "textlob = " + ((textLob == null || textLob == "n/a") ? "NULL" : "?") + " " +
                    "WHERE id = " + uID + "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                if (date1 != null) {
                    pstmt.setDate(++sqlID, date1);
                }
                if (date2 != null) {
                    pstmt.setDate(++sqlID, date2);
                }
                if (text != null && text != "n/a") {
                    pstmt.setString(++sqlID, text);
                }
                if (textLob != null && textLob != "n/a") {
                    pstmt.setString(++sqlID, textLob);
                }
                rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - TextItem save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - TextItem save");
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
    * Remove the current TextItem from the DB
    *
    * @param myConn     Connection to the database
    */
    public void drop(DbConn myConn) {
        drop(myConn, 0);
    }


    /**
    * Remove the current TextItem from the DB
    *
    * @param myConn     Connection to the database
    * @param userID     The id of the ov the user performing the action (long)
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
                "DELETE FROM " + SCHEMAPATH + ".text_items WHERE id = " + uID
                );
            rows = pstmt.executeUpdate();

            myUID.setStatus("removed");
            myUID.save(conn);

            uID = 0;
            date1 = null;
            date2 = null;
            text = null;
            link = null;
            status = null;
            isNew = true;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - TextItem drop");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - TextItem drop");
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
    * Get a list of TextItem from the DB
    *
    * @param myConn     Connection to the database
    * @param list       A USet of items to be fetched from the db
    */
    public static TextItem[] getItemList(DbConn myConn, USet list) {
        return getItemList(myConn, list, "none");
    }


    /**
    * Get a list of TextItem from the DB
    *
    * @param myConn     Connection to the database
    * @param list       A USet of items to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static TextItem[] getItemList(DbConn myConn, USet list, String sort) {
        long[] myList = list.getItemsArray();
        return getItemList(myConn, (long[]) myList, sort, "subset");
    }


    /**
    * Get a list of TextItem from the DB
    *
    * @param myConn     Connection to the database
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static TextItem[] getItemList(DbConn myConn, String sort) {
        return getItemList(myConn, (long[]) null, sort, "all");
    }


    /**
    * Get a list of TextItem from the DB
    *
    * @param myConn     Connection to the database
    * @param list       A long array of UNID id's to be fetched from the db
    */
    public static TextItem[] getItemList(DbConn myConn, long[] list) {
        return getItemList(myConn, list, "none", "subset");
    }


    /**
    * Get a list of TextItem from the DB
    *
    * @param myConn     Connection to the database
    * @param list       A long array of UNID id's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static TextItem[] getItemList(DbConn myConn, long[] list, String sort) {
        return getItemList(myConn, list, sort, "subset");
    }


    /**
    * Get a list of TextItem from the DB
    *
    * @param myConn     Connection to the database
    * @param list       A long array of UNID id's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    * @param flag       A string defining if all records are returned or just a subset
    */
    public static TextItem[] getItemList(DbConn myConn, long[] list, String sort, String flag) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        TextItem[] ti = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            //sqlFrom = " FROM " + SCHEMAPATH + ".text_items ";
            sqlFrom = " FROM " + myConn.getSchemaPath() + ".text_items ";
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
            ti = new TextItem[returnSize];

//            sqlcode = "SELECT id, date1, date2, text, link, status, owner " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
            sqlcode = "SELECT id, date1, date2, text, link, status, owner, textlob " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                ti[i] = new TextItem();
                ti[i].uID = rs.getLong(1);
                ti[i].date1 = rs.getDate(2);
                ti[i].date2 = rs.getDate(3);
                ti[i].text = rs.getString(4);
                ti[i].link = rs.getString(5);
                ti[i].status = rs.getString(6);
                ti[i].owner = rs.getLong(7);
                ti[i].textLob = rs.getString(8);
                ti[i].myUID = new UNID(ti[i].uID, myConn);
                ti[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - TextItem lookup");
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

        return ti;
    }


    /**
    * Get a list of TextItem from the DB as a hashmap
    *
    * @param myConn     Connection to the database
    * @param list       A long array of UNID id's to be fetched from the db
    */
    public static HashMap getItemHashMap(DbConn myConn, long[] list) {
        TextItem[] ti = getItemList(myConn, (long[]) list, "none", "subset");
        HashMap hm = new HashMap(ti.length);
        for (int i=0; i<ti.length; i++) {
            hm.put(Long.toString(ti[i].getID()), (Object) ti[i]);
        }

        return hm;
    }




}
