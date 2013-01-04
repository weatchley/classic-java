package gov.ymp.csi.UNID;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;

/**
* UHash is the base class for collections/sets of UNID's in the CSI
*
* @author   Bill Atchley
*/
public class UHash extends UStructure {
    private HashMap items = new HashMap();

    /**
    * Creates a new empty UHash object
    */
    public UHash () {
        super();
        //myUID.setType("uhash");
        //myUID.save();
    }

    /**
    * Creates a new empty UHash object
    *
    * @param myConn Connection to the database
    */
    public UHash (DbConn myConn) {
        super(myConn);
        myUID.setType("uhash");
        myUID.save(myConn);
    }

    /**
    * Creates a new empty UHash object
    *
    * @param myConn Connection to the database
    * @param userID  The person ID of the user performing the action
    */
    public UHash (DbConn myConn, long userID) {
        super(myConn, userID);
        myUID.setType("uhash");
        myUID.save(myConn);
    }

    /**
    * Creates a UHash object and uses the given id to populate it from the db
    *
    * @param id     The id of the UHash to lookup from the db (long)
    */
    public UHash (long id) {
        super(id);
        lookup(id);
    }

    /**
    * Creates a UHash object and uses the given id to populate it from the db
    *
    * @param id     The id of the UHash to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public UHash (long id, DbConn myConn) {
        super(id, myConn);
        lookup(id, myConn);
    }

    /**
    * Creates a UHash object and uses the given UNID to populate it from the db
    *
    * @param uid     The UNID of the UHash to lookup from the db (long)
    */
    public UHash (UNID uid) {
        super(uid);
        lookup(uid.getID());
    }

    /**
    * Creates a UHash object and uses the given UNID to populate it from the db
    *
    * @param uid     The UNID of the UHash to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public UHash (UNID uid, DbConn myConn) {
        super(uid, myConn);
        lookup(uid.getID(), myConn);
    }

    /**
    * Creates a UHash object and uses the given description to populate it from the db
    *
    * @param descr     The Description to lookup from the db (long)
    * @param myConn    Connection to the database
    */
    public UHash (String descr, DbConn myConn) {
        super(descr, myConn);
        lookup(uID, myConn);
    }

    /**
    * Retrieves a UHash from the db and stores it in the current UHash object
    *
    * @param id     The id of the UNID to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public void lookup (long id, DbConn myConn) {
        super.usLookup(id, myConn);
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            if (uID == id) {
                items = new HashMap();
                String sqlcode = "SELECT id, key, item FROM " + SCHEMAPATH + ".uhash WHERE id=" + uID;
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                while (rs.next()) {
                    String key = rs.getString(2);
                    long myItem = rs.getLong(3);
                    items.put(key, new UNID(myItem, conn));
                }

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UHash lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UHash lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Retrieve an item from the current UHash
    *
    * @param key      The key value to look up (String)
    * @return item    The long value (unid) using the key in the current UHash
    */
    public long get(String key) {
        UNID tmp = (UNID) items.get(key);
        return(tmp.getID());
    }


    /**
    * Retrieve an item from the current UHash
    *
    * @param key      The key value to look up (String)
    * @return item    The long value (unid) using the key in the current UHash
    */
    public UNID getItemUNID(String key) {
        return((UNID) items.get(key));
    }


    /**
    * Retrieve the set of items for the current UHash
    *
    * @return items    The set of items (unid'S) for the current UHash
    */
    public Set getItems() {
        return((Set) items.entrySet());
    }


    /**
    * Retrieve the set of keys for the current UHash
    *
    * @return items    The set of keys (Strings) for the current UHash
    */
    public Set getKeys() {
        return((Set) items.keySet());
    }



    /**
    * Retrieve the array of UHashs for the given set of creators
    *
    * @param myConn   Connection to the database
    *
    * @return     The array of UHashs
    */
    public static UHash[] getList(DbConn myConn) {
        long [] idList = null;
        return(getList(idList, myConn, true));
    }


    /**
    * Retrieve the array of UHashs for the given set of creators
    *
    * @param myConn   Connection to the database
    * @param getItems Retrieve items for UHash
    *
    * @return     The array of UHashs
    */
    public static UHash[] getList(DbConn myConn, boolean getItems) {
        long [] idList = null;
        return(getList(idList, myConn, getItems));
    }


    /**
    * Retrieve the array of UHashs for the given set of creators
    *
    * @param list     A long array of creator id's to be fetched from the db
    * @param myConn   Connection to the database
    * @param getItems Retrieve items for UHash
    *
    * @return     The array of UHashs
    */
    public static UHash[] getList(long[] list, DbConn myConn, boolean getItems) {
        long [] idList = getIDList("uhash", list, myConn);
        UHash [] myList = new UHash[idList.length];

        for (int i=0; i<idList.length; i++) {
            if (getItems) {
                myList[i] = new UHash(idList[i], myConn);
            } else {
                myList[i] = new UHash();
                myList[i].usLookup(idList[i], myConn);
            }
        }

        return(myList);
    }


    /**
    * Retrieve the array of UHashs for the given subtype
    *
    * @param subtype  Sub type to be fetched from the db
    * @param myConn   Connection to the database
    * @param getItems Retrieve items for UHash
    *
    * @return     The array of UHashs
    */
    public static UHash[] getList(String subtype, DbConn myConn, boolean getItems) {
        long [] idList = getSubTypeIDList("uhash", subtype, myConn);
        UHash [] myList = new UHash[idList.length];

        for (int i=0; i<idList.length; i++) {
            if (getItems) {
                myList[i] = new UHash(idList[i], myConn);
            } else {
                myList[i] = new UHash();
                myList[i].usLookup(idList[i], myConn);
            }
        }

        return(myList);
    }


    /**
    * Add an item to the current UHash
    *
    * @param key    The key of the item to add to the current UHash (String)
    * @param id     The UNID id of item to add to the current UHash (long)
    */
    public void addItem(String key, long id) {
        items.put(key, new UNID(id));
    }

    /**
    * Add an item to the current UHash
    *
    * @param key    The key of the item to add to the current UHash (String)
    * @param id     The UNID id of item to add to the current UHash (long)
    * @param myConn Connection to the database
    */
    public void addItem(String key, long id, DbConn myConn) {
        items.put(key, new UNID(id, myConn));
    }

    /**
    * Add an item to the current UHash
    *
    * @param key      The key of the item to add to the current UHash (String)
    * @param item     The UNID to add to the current UHash (UNID)
    */
    public void addItem(String key, UNID item) {
        items.put(key, item);
    }


    /**
    * Put an item to the current UHash
    *
    * @param key    The key of the item to put into the current UHash (String)
    * @param id     The UNID id of item to put into the current UHash (long)
    */
    public void putItem(String key, long id) {
        items.put(key, new UNID(id));
    }

    /**
    * Put an item to the current UHash
    *
    * @param key    The key of the item to put into the current UHash (String)
    * @param id     The UNID id of item to put into the current UHash (long)
    * @param myConn Connection to the database
    */
    public void putItem(String key, long id, DbConn myConn) {
        items.put(key, new UNID(id, myConn));
    }

    /**
    * Put an item to the current UHash
    *
    * @param key      The key of the item to put into the current UHash (String)
    * @param item     The UNID to put into the current UHash (UNID)
    */
    public void putItem(String key, UNID item) {
        items.put(key, item);
    }

    /**
    * Drop an item from the current UHash using its key
    *
    * @param key     The kwy of the item to remove from the current UHash
    */
    public void dropItem(String key) {
        items.remove(key);
    }

    /**
    * Clear all items from the current UHash
    */
    public void clearItems() {
        if (!items.isEmpty()) {
            items.clear();
        }
    }

    /**
    * Save the current UHash to the DB
    *
    * @param myConn     The CSI db connection
    * @param userID     The person ID of the user performing the action
    */
    public void save(DbConn myConn, long userID) {
        //save funciton
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            type = "uhash";
            usSave(myConn, userID);
            Connection conn = myConn.conn;

            pstmt = conn.prepareStatement(
                "DELETE FROM " + SCHEMAPATH + ".uhash WHERE id = " + uID
                );
            rows = pstmt.executeUpdate();
            //HashSet hs = (HashSet) items.keySet();
            Set hs = items.keySet();
            for (Iterator i = hs.iterator(); i.hasNext();) {
                String myID = (String) i.next();
                long itemID = ((UNID) items.get(myID)).getID();
                String sqlcode = "INSERT INTO " + SCHEMAPATH + ".uhash (id, key, item) " + "VALUES (" + uID + ", '" + myID + "', " + itemID + ")";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                rows = pstmt.executeUpdate();
            }
            gov.ymp.csi.db.ALog.logActivity(myConn, userID, "N/A", ((isNew) ? 1 : 2), ((isNew) ? "Created" : "Updated") + " UHash " + uID);
            isNew = false;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",((isNew) ? 1 : 2), outLine + " - UHash save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",((isNew) ? 1 : 2), outLine + " - UHash save");
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
    * Remove the current UHash from the DB
    *
    * @param myConn     The CSI db connection
    * @param userID     The person ID of the user performing the action
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
            stmt = conn.createStatement ();
// do auth stuff
            pstmt = conn.prepareStatement(
                "DELETE FROM " + SCHEMAPATH + ".uhash WHERE id = " + uID
                );
            rows = pstmt.executeUpdate();

            long tmpID = uID;
            super.usDrop(myConn, userID);
            gov.ymp.csi.db.ALog.logActivity(myConn, userID,"N/A", 3, "Dropped UHash " + tmpID);

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - UHash drop");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - UHash drop");
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
    * Test to see if the UHash exists in the DB
    *
    * @param myConn     The CSI db connection
    * @param id         The ID of the item to check
    *
    * @return           The boolean of if the id exists
    */
    static public boolean doesItemExist(DbConn myConn, long id) {
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        boolean ret = false;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
// do auth stuff
            pstmt = conn.prepareStatement(
                "SELECT * FROM " + SCHEMAPATH + ".ulists WHERE item = " + id
                );
            rows = pstmt.executeUpdate();

            if (rows > 0) {ret = true;}

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - doesItemExist");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - doesItemExist");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
            if (pstmt != null)
                try { pstmt.close(); } catch (Exception i) {}
        }
        return (ret);
    }


}
