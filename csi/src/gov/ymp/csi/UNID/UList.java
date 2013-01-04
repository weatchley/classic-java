package gov.ymp.csi.UNID;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;

/**
* UList is the base class for collections/sets of UNID's in the CSI
*
* @author   Bill Atchley
*/
public class UList extends UStructure {
    private ArrayList items = new ArrayList();

    /**
    * Creates a new empty UList object
    */
    public UList () {
        super();
        //myUID.setType("ulist");
        //myUID.save();
    }

    /**
    * Creates a new empty UList object
    *
    * @param myConn Connection to the database
    */
    public UList (DbConn myConn) {
        super(myConn);
        myUID.setType("ulist");
        myUID.save(myConn);
    }

    /**
    * Creates a new empty UList object
    *
    * @param myConn Connection to the database
    * @param userID  The person ID of the user performing the action
    */
    public UList (DbConn myConn, long userID) {
        super(myConn, userID);
        myUID.setType("ulist");
        myUID.save(myConn);
    }

    /**
    * Creates a UList object and uses the given id to populate it from the db
    *
    * @param id     The id of the UList to lookup from the db (long)
    */
    public UList (long id) {
        super(id);
        lookup(id);
    }

    /**
    * Creates a UList object and uses the given id to populate it from the db
    *
    * @param id     The id of the UList to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public UList (long id, DbConn myConn) {
        super(id, myConn);
        lookup(id, myConn);
    }

    /**
    * Creates a UList object and uses the given UNID to populate it from the db
    *
    * @param uid     The UNID of the UList to lookup from the db (long)
    */
    public UList (UNID uid) {
        super(uid);
        lookup(uid.getID());
    }

    /**
    * Creates a UList object and uses the given UNID to populate it from the db
    *
    * @param uid     The UNID of the UList to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public UList (UNID uid, DbConn myConn) {
        super(uid, myConn);
        lookup(uid.getID(), myConn);
    }

    /**
    * Creates a UList object and uses the given description to populate it from the db
    *
    * @param descr     The Description to lookup from the db (long)
    * @param myConn    Connection to the database
    */
    public UList (String descr, DbConn myConn) {
        super(descr, myConn);
        lookup(uID, myConn);
    }

    /**
    * Retrieves a UList from the db and stores it in the current UList object
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
                items = new ArrayList();
                String sqlcode = "SELECT id, item, rank FROM " + SCHEMAPATH + ".ulists WHERE id=" + uID + " ORDER BY rank";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                while (rs.next()) {
                    long myItem = rs.getLong(2);
                    items.add(new UNID(myItem, conn));
                }

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UList lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UList lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Retrieve the set of items for the current UList
    *
    * @return items    The set of items (unid'S) for the current UList
    */
    public Set getItems() {
        return((Set) items);
    }

    /**
    * Retrieve the array of items for the current UList
    *
    * @return     The array of items (unid id'S) for the current UList
    */
    public long[] getItemsArray() {
        Object[] myItems = items.toArray();
        long[] myList = new long[myItems.length];
        for (int i=0; i<myItems.length; i++) {
            UNID tmp = (UNID)myItems[i];
            myList[i] = tmp.getID();
        }
        return(myList);
    }


    /**
    * Retrieve the array of items for the current UList and all subsets
    *
    * @param myConn Connection to the database
    *
    * @return     The array of items (unid id'S) for the current UList and all subsets
    */
    public long[] getItemsArrayRecursive(DbConn myConn) {
        Object[] myItems = items.toArray();
        ArrayList al = new ArrayList();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        long[] myList = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            items = new ArrayList();
            String sqlcode = "SELECT id,item,rank,LEVEL FROM " + SCHEMAPATH + ".ulists CONNECT BY PRIOR item=id START WITH id=" + uID;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            while (rs.next()) {
                long myItem = rs.getLong(2);
                al.add(new Long(myItem));
            }

            myItems = al.toArray();
//System.out.println("myItems length" + myItems.length);
            myList = new long[myItems.length];
            for (int i=0; i<myItems.length; i++) {
                Long tmp = (Long) myItems[i];
//System.out.println("List: " + i + ", " + tmp.longValue());
                myList[i] = tmp.longValue();
            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UList getItemsArrayRecursive");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UList getItemsArrayRecursive");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
        return(myList);
    }


    /**
    * Retrieve the array from tree traversal for the current UList and all subsets
    *
    * @param myConn Connection to the database
    *
    * @return     The array of objects (each an object array with 0=id, 1=item, 2=rank, 3=level) for the current UList
    */
    public Object[] getTree(DbConn myConn) {
        Object[] myItems = null;
        ArrayList al = new ArrayList();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        long[] myList = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            items = new ArrayList();
            String sqlcode = "SELECT id,item,rank,LEVEL FROM " + SCHEMAPATH + ".ulists CONNECT BY PRIOR item=id START WITH id=" + uID;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            while (rs.next()) {
                Object [] tmp = new Object[4];
                tmp[0] = (Object) new Long(rs.getLong(1));
                tmp[1] = (Object) new Long(rs.getLong(2));
                tmp[2] = (Object) new Integer(rs.getInt(3));
                tmp[3] = (Object) new Integer(rs.getInt(4));
                al.add(tmp);
            }

            myItems = al.toArray();
//System.out.println("myItems length" + myItems.length);
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UList getTree");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UList getTree");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
        return(myItems);
    }


    /**
    * Retrieve the hashmap of descriptions for the current UList and all subsets
    *
    * @param myConn Connection to the database
    *
    * @return     The HashMap of descriptions for the current UList and all subsets
    */
    public HashMap getDescriptionsRecursive(DbConn myConn) {
        HashMap hm = new HashMap();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        long[] myList = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            items = new ArrayList();
            String sqlcode = "SELECT s.id,s.description " +
                "FROM " + SCHEMAPATH + ".ustructure s, " + SCHEMAPATH + ".ulists l " +
                "WHERE s.id=l.item CONNECT BY PRIOR l.item=l.id START WITH l.id=" + uID;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            while (rs.next()) {
                long myItem = rs.getLong(1);
                String desc = rs.getString(2);
                hm.put(new Long(myItem), desc);
            }

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UList getDescriptionsRecursive");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UList getDescriptionsRecursive");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
        return(hm);
    }


    /**
    * Retrieve the array of ULists for the given set of creators
    *
    * @param myConn   Connection to the database
    *
    * @return     The array of ULists
    */
    public static UList[] getList(DbConn myConn) {
        long [] idList = null;
        return(getList(idList, myConn, true));
    }


    /**
    * Retrieve the array of ULists for the given set of creators
    *
    * @param myConn   Connection to the database
    * @param getItems Retrieve items for UList
    *
    * @return     The array of ULists
    */
    public static UList[] getList(DbConn myConn, boolean getItems) {
        long [] idList = null;
        return(getList(idList, myConn, getItems));
    }


    /**
    * Retrieve the array of ULists for the given set of creators
    *
    * @param list     A long array of creator id's to be fetched from the db
    * @param myConn   Connection to the database
    * @param getItems Retrieve items for UList
    *
    * @return     The array of ULists
    */
    public static UList[] getList(long[] list, DbConn myConn, boolean getItems) {
        long [] idList = getIDList("ulist", list, myConn);
        UList [] myList = new UList[idList.length];

        for (int i=0; i<idList.length; i++) {
            if (getItems) {
                myList[i] = new UList(idList[i], myConn);
            } else {
                myList[i] = new UList();
                myList[i].usLookup(idList[i], myConn);
            }
        }

        return(myList);
    }


    /**
    * Retrieve the array of ULists for the given subtype
    *
    * @param subtype  Sub type to be fetched from the db
    * @param myConn   Connection to the database
    * @param getItems Retrieve items for UList
    *
    * @return     The array of ULists
    */
    public static UList[] getList(String subtype, DbConn myConn, boolean getItems) {
        long [] idList = getSubTypeIDList("ulist", subtype, myConn);
        UList [] myList = new UList[idList.length];

        for (int i=0; i<idList.length; i++) {
            if (getItems) {
                myList[i] = new UList(idList[i], myConn);
            } else {
                myList[i] = new UList();
                myList[i].usLookup(idList[i], myConn);
            }
        }

        return(myList);
    }


    /**
    * Retrieve the array of ULists that contain the given id
    *
    * @param myConn   Connection to the database
    * @param id       The UNID id to test for
    *
    * @return     The array of ULists
    */
    public static UList[] getListWithID(DbConn myConn, long id) {
        UList [] myList = null;
        ResultSet rs = null;
        Statement stmt = null;
        String sqlcode = "";
        String outLine = "";
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            sqlcode = "SELECT count(*) FROM " + SCHEMAPATH + ".ulists WHERE item=" + id;
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            myList = new UList[rs.getInt(1)];
            sqlcode = "SELECT id FROM " + SCHEMAPATH + ".ulists WHERE item=" + id;
            rs = stmt.executeQuery(sqlcode);
            int i=0;
            while (rs.next()) {
                long myID = rs.getLong(1);
                myList[i] = new UList(myID, myConn);
                i++;
            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UList getListWithID");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UList getListWithID");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
        return(myList);
    }


    /**
    * Add an item to the current UList
    *
    * @param id     The UNID id of item to add to the current UList (long)
    */
    public void addItem(long id) {
        items.add(new UNID(id));
    }

    /**
    * Add an item to the current UList
    *
    * @param id     The UNID id of item to add to the current UList (long)
    * @param myConn Connection to the database
    */
    public void addItem(long id, DbConn myConn) {
        items.add(new UNID(id, myConn));
    }

    /**
    * Add an item to the current UList
    *
    * @param item     The UNID to add to the current UList (UNID)
    */
    public void addItem(UNID item) {
        items.add(item);
    }

    /**
    * insert an item into the current UList
    *
    * @param id       The UNID id of item to insert into the current UList (long)
    * @param index    The index of the item to insert before
    */
    public void insertItem(long id, int index) {
        items.add(index, new UNID(id));
    }

    /**
    * insert an item into the current UList
    *
    * @param id       The UNID id of item to insert into the current UList (long)
    * @param index    The index of the item to insert before
    * @param myConn   Connection to the database
    */
    public void insertItem(long id, int index, DbConn myConn) {
        items.add(index, new UNID(id, myConn));
    }

    /**
    * insert an item into the current UList
    *
    * @param item     The UNID to add to the current UList (UNID)
    * @param index    The index of the item to insert before
    */
    public void insertItem(UNID item, int index) {
        items.add(index, item);
    }

    /**
    * Drop an item from the current UList
    *
    * @param index     The index of the item to remove from the current UList
    */
    public void dropItem(int index) {
        items.remove(index);
    }

    /**
    * Drop an item from the current UList
    *
    * @param uid     The UNID id of the item to remove from the current UList
    */
    public void dropItem(long uid) {
        //UNID un = new UNID(uid);
//System.out.println("Got Here UL-1, uid=" + uid);
        //items.remove(items.indexOf(un));
        for (int i=0; i<items.size(); i++) {
            UNID un = (UNID) items.get(i);
            if (un.getID() == uid) {items.remove(i);}
        }
    }

    /**
    * Clear all items from the current UList
    */
    public void clearItems() {
        if (!items.isEmpty()) {
            items.clear();
        }
    }

    /**
    * Move item up one in the list
    */
    public void moveItemUp(long item) {
        DbConn myConn = new DbConn(SCHEMA);
        moveItemUp(item, myConn);
        myConn.release();
    }

    /**
    * Move item up one in the list
    */
    public void moveItemUp(long item, DbConn myConn) {
        Object[] ua = items.toArray();
        long[] la = new long[ua.length];
        int loc = 0;
        for (int i=0; i<ua.length; i++) {
            la[i] = ((UNID) ua[i]).getID();
            if (item == la[i]) {
                loc = i;
            }
        }
        long tmp = 0;
        int prev = loc - 1;

        if (loc != 0) {
            tmp = la[loc];
            la[loc] = la[prev];
            la[prev] = tmp;
        }
        items.clear();
        for (int i=0; i<ua.length; i++) {
            items.add(new UNID(la[i], myConn));
        }
    }

    /**
    * Move item to top of the list
    */
    public void moveItemToTop(long item) {
        DbConn myConn = new DbConn(SCHEMA);
        moveItemToTop(item, myConn);
        myConn.release();
    }

    /**
    * Move item to top of the list
    */
    public void moveItemToTop(long item, DbConn myConn) {
        Object[] ua = items.toArray();
        long[] la = new long[ua.length - 1];
        items.clear();
        items.add(new UNID(item, myConn));
        for (int i=0; i<ua.length; i++) {
            long tmp = ((UNID) ua[i]).getID();
            if (item != tmp) {
                items.add(new UNID(tmp, myConn));
            }
        }
    }

    /**
    * Save the current UList to the DB
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

            type = "ulist";
            usSave(myConn, userID);
            Connection conn = myConn.conn;

            pstmt = conn.prepareStatement(
                "DELETE FROM " + SCHEMAPATH + ".ulists WHERE id = " + uID
                );
            rows = pstmt.executeUpdate();
            int j = 0;
            for (Iterator i = items.iterator(); i.hasNext();) {
                UNID myID = (UNID) i.next();
                j++;
                pstmt = conn.prepareStatement(
                    "INSERT INTO " + SCHEMAPATH + ".ulists (id, item, rank) " + "VALUES (" + uID + ", " + myID.getID() + ", " + j + ")"
                    );
                rows = pstmt.executeUpdate();
            }
            gov.ymp.csi.db.ALog.logActivity(myConn, userID, "N/A", ((isNew) ? 1 : 2), ((isNew) ? "Created" : "Updated") + " UList " + uID);
            isNew = false;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",((isNew) ? 1 : 2), outLine + " - UList save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",((isNew) ? 1 : 2), outLine + " - UList save");
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
    * Remove the current UList from the DB
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
                "DELETE FROM " + SCHEMAPATH + ".ulists WHERE id = " + uID
                );
            rows = pstmt.executeUpdate();

            long tmpID = uID;
            super.usDrop(myConn, userID);
            gov.ymp.csi.db.ALog.logActivity(myConn, userID,"N/A", 3, "Dropped UList " + tmpID);

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - UList drop");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - UList drop");
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
    * Test if item exists in any UList
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
