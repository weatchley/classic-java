package gov.ymp.csi.UNID;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;

/**
* USet is the base class for collections/sets of UNID's in the CSI
*
* @author   Bill Atchley
*/
public class USet extends UStructure {
    private HashSet items = new HashSet();

    /**
    * Creates a new empty uSet object
    */
    public USet () {
        super();
        myUID.setType("uset");
        myUID.save();
    }

    /**
    * Creates a new empty uSet object
    *
    * @param myConn Connection to the database
    */
    public USet (DbConn myConn) {
        super(myConn);
        myUID.setType("uset");
        myUID.save(myConn);
    }

    /**
    * Creates a new empty uSet object
    *
    * @param myConn Connection to the database
    * @param userID  The person ID of the user performing the action
    */
    public USet (DbConn myConn, long userID) {
        super(myConn, userID);
        myUID.setType("uset");
        myUID.save(myConn);
    }

    /**
    * Creates a uSet object and uses the given id to populate it from the db
    *
    * @param id     The id of the uSet to lookup from the db (long)
    */
    public USet (long id) {
        super(id);
    }

    /**
    * Creates a uSet object and uses the given id to populate it from the db
    *
    * @param id     The id of the uSet to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public USet (long id, DbConn myConn) {
        super(id, myConn);
    }

    /**
    * Creates a uSet object and uses the given UNID to populate it from the db
    *
    * @param uid     The UNID of the uSet to lookup from the db (long)
    */
    public USet (UNID uid) {
        super(uid);
    }

    /**
    * Creates a uSet object and uses the given UNID to populate it from the db
    *
    * @param uid     The UNID of the uSet to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public USet (UNID uid, DbConn myConn) {
        super(uid, myConn);
    }

    /**
    * Creates a uSet object and uses the given description to populate it from the db
    *
    * @param descr     The Description to lookup from the db (long)
    * @param myConn    Connection to the database
    */
    public USet (String descr, DbConn myConn) {
        super(descr, myConn);
    }

    /**
    * Retrieves a uSet from the db and stores it in the current uSet object
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
                items = new HashSet();
                String sqlcode = "SELECT id, item FROM " + SCHEMAPATH + ".usets WHERE id=" + uID;
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
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - USet lookup");
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
    * Retrieve the set of items for the current uSet
    *
    * @return items    The set of items (unid'S) for the current uSet
    */
    public Set getItems() {
        return((Set) items);
    }

    /**
    * Retrieve the array of items for the current uSet
    *
    * @return     The array of items (unid id'S) for the current uSet
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
    * Retrieve the array of items for the current uSet and all subsets
    *
    * @param myConn Connection to the database
    *
    * @return     The array of items (unid id'S) for the current uSet and all subsets
    */
    public long[] getItemsArrayRecursive(DbConn myConn) {
        Object[] myItems = items.toArray();
        ArrayList al = new ArrayList();
        for (int i=0; i<myItems.length; i++) {
            UNID tmp = (UNID)myItems[i];
            al.add((Object) new Long(tmp.getID()));
            if (tmp.getType().equals("uset")) {
                USet us = new USet(tmp.getID(), myConn);
                us.lookup(tmp.getID(), myConn);
                long[] la = us.getItemsArrayRecursive(myConn);
                for (int j=0; j<la.length; j++) {
                    al.add((Object) new Long(la[j]));
                }
            }
        }
        myItems = al.toArray();
        long[] myList = new long[myItems.length];
        for (int i=0; i<myItems.length; i++) {
            Long tmp = (Long) myItems[i];
            myList[i] = tmp.longValue();
        }
        return(myList);
    }

    /**
    * Add an item to the current uSet
    *
    * @param id     The UNID id of item to add to the current uSet (long)
    */
    public void addItem(long id) {
        items.add(new UNID(id));
    }

    /**
    * Add an item to the current uSet
    *
    * @param id     The UNID id of item to add to the current uSet (long)
    * @param myConn Connection to the database
    */
    public void addItem(long id, DbConn myConn) {
        items.add(new UNID(id, myConn));
    }

    /**
    * Add an item to the current uSet
    *
    * @param item     The UNID to add to the current uSet (UNID)
    */
    public void addItem(UNID item) {
        items.add(item);
    }

    /**
    * Drop an item from the current uSet
    *
    * @param id     The UNID id of item to remove from the current uSet (long)
    */
    public void dropItem(long id) {
        items.remove(new UNID(id));
    }

    /**
    * Drop an item from the current uSet
    *
    * @param id     The UNID id of item to remove from the current uSet (long)
    * @param myConn Connection to the database
    */
    public void dropItem(long id, DbConn myConn) {
        items.remove(new UNID(id, myConn));
    }

    /**
    * Drop an item from the current uSet
    *
    * @param item     The UNID to remove from the current uSet (UNID)
    */
    public void dropItem(UNID item) {
        items.remove(item);
    }

    /**
    * Clear all items from the current uSet
    */
    public void clearItems() {
        if (!items.isEmpty()) {
            items.clear();
        }
    }

    /**
    * Save the current USet to the DB
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

            type = "uset";
            usSave(myConn, userID);
            Connection conn = myConn.conn;

            pstmt = conn.prepareStatement(
                "DELETE FROM " + SCHEMAPATH + ".usets WHERE id = " + uID
                );
            rows = pstmt.executeUpdate();
            for (Iterator i = items.iterator(); i.hasNext();) {
                UNID myID = (UNID) i.next();
                pstmt = conn.prepareStatement(
                    "INSERT INTO " + SCHEMAPATH + ".usets (id, item) " + "VALUES (" + uID + ", " + myID.getID() + ")"
                    );
                rows = pstmt.executeUpdate();
            }
            gov.ymp.csi.db.ALog.logActivity(myConn, userID, "N/A", ((isNew) ? 1 : 2), ((isNew) ? "Created" : "Updated") + " USet " + uID);
            isNew = false;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",((isNew) ? 1 : 2), outLine + " - USet save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",((isNew) ? 1 : 2), outLine);
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
    * Remove the current USet from the DB
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
                "DELETE FROM " + SCHEMAPATH + ".usets WHERE id = " + uID
                );
            rows = pstmt.executeUpdate();

            long tmpID = uID;
            super.usDrop(myConn, userID);
            gov.ymp.csi.db.ALog.logActivity(myConn, userID,"N/A",3, "Dropped USet " + tmpID);

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - USet drop");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine);
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


}
