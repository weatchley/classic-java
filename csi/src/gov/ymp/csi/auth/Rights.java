package gov.ymp.csi.auth;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;


/**
* Rights is the base class for rights's in the CSI
*
* @author   Bill Atchley
*/
public class Rights {
    private long item = 0;
    private HashSet access = new HashSet();
    private boolean isNew = true;

    //private static String SCHEMA = "csi";
    private static String SCHEMA = null;
    private static String SCHEMAPATH = null;
    private static String DBTYPE = null;

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
    * creates a new empty Rights object
    */
    public Rights () {
        init();
        item = 0;
    }

    /**
    * Creates a Rights object and retrieves it up from the db
    *
    * @param id     The id of the Rights to lookup from the db
    */
    public Rights (long id) {
        init();
        lookup (id);
    }

    /**
    * Retrieves a Rights from the db and stores it in the current Rights object
    *
    * @param id     The id of the Rights to lookup from the db
    */
    public void lookup (long id) {
        String outLine = "";
        DbConn myConn = new DbConn(SCHEMA);
        try {

            Connection conn = myConn.conn;

            this.lookup (id, conn);

            myConn.release();
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(0,"N/A",0, outLine);
            myConn.release();
        }
    }

    /**
    * Retrieves a Rights from the db (given a passed in connection) and stores it in the current Rights object
    *
    * @param id     The id of the Rights to lookup from the db
    * @param conn   A connection to the DB
    */
    public void lookup (long id, Connection conn) {
        item = 0;
        String outLine = "";
        try {

            item = id;

            // Create a Statement
            Statement stmt = conn.createStatement ();

            ResultSet rs;
            rs = stmt.executeQuery("SELECT item, groupid, type, negate, NVL(constraints, 'n/a') FROM " + SCHEMAPATH + ".rights WHERE item=" + id);
            //rs = stmt.executeQuery("SELECT item, groupid, type, negate, ISNULL(constraints, 'n/a') FROM " + SCHEMAPATH + ".rights WHERE item=" + id);
            while (rs.next()) {
                long myItem = rs.getLong(1);
                //items.add(new UNID(myItem, conn));
                long myGroupID = rs.getLong(2);
                String myType = rs.getString(3);
                String myNeg = rs.getString(4);
                boolean negation = ((myNeg == "T") ? true : false);
                String myCon = rs.getString(5);
                if (myCon == null || myCon == "n/a") {
                    access.add(new RAccess(myItem, myGroupID, myType, negation));
                } else {
                    access.add(new RAccess(myItem, myGroupID, myType, negation, myCon));
                }

// create RAccess set
            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(0,"N/A",0, outLine);
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(0,"N/A",0, outLine);
        }
    }

    /**
    * Retrieve the unique id for the current Rights item
    *
    * @return id     The id for the current Rights item
    */
    public long getItemID() {
        long id = item;
        return(id);
    }

    /**
    * Determine if a user has a right using the current Rights object
    * Stub - Not yet functional (always true)
    *
    * @param userid  User id to test
    * @return      True or false as to if the user has right
    */
    public boolean hasRight(long userid) {
        return(true);
    }

    /**
    * Determine if a user has a right using a passed in Rights object
    * Stub - Not yet functional (always true)
    *
    * @param userid  User id to test
    * @param right Rights to test
    * @return      True or false as to if the user has rights
    */
    static public boolean hasRight(long userid, long right) {
        return(true);
    }

    /**
    * Add an access right for a group
    *
    * @param rGroup      The the group to add
    * @param rType       The type of access to add
    */
    public void add(long rGroup, String rType) {
        access.add(new RAccess(item, rGroup, rType, false));
    }

    /**
    * Negate an access right for a group (change true to false or false to true)
    *
    * @param rGroup      The the group to add
    * @param rType       The type of access to add
    */
    public void negate(long rGroup, String rType) {
        for (Iterator i = access.iterator(); i.hasNext();) {
            RAccess myRA = (RAccess) i.next();
            if (myRA.getGroupID() == rGroup && myRA.getType() == rType) {
                access.remove(myRA);
                myRA.setNegation(((myRA.getNegation()) ? false : true));
                access.add(myRA);
                break;
            }
        }
    }

    /**
    * Negate an access right for a group using a given negation value
    *
    * @param rGroup      The the group to add
    * @param rType       The type of access to add
    * @param rNeg        The negation value to set
    */
    public void negate(long rGroup, String rType, boolean rNeg) {
        for (Iterator i = access.iterator(); i.hasNext();) {
            RAccess myRA = (RAccess) i.next();
            if (myRA.getGroupID() == rGroup && myRA.getType() == rType) {
                access.remove(myRA);
                myRA.setNegation(rNeg);
                access.add(myRA);
                break;
            }
        }
    }

    /**
    * Drop an access right for a group
    *
    * @param rGroup      The the group to add
    * @param rType       The type of access to add
    */
    public void drop(long rGroup, String rType) {
        for (Iterator i = access.iterator(); i.hasNext();) {
            RAccess myRA = (RAccess) i.next();
            if (myRA.getGroupID() == rGroup && myRA.getType() == rType) {
                access.remove(myRA);
                break;
            }
        }
    }

    /**
    * Save the current Rights to the DB
    */
    public void save() {
        //save funciton
        String outLine = "";
        DbConn myConn = new DbConn(SCHEMA);
        int rows;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            Statement stmt = conn.createStatement ();
            ResultSet rs;
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement(
                "DELETE FROM " + SCHEMA + ".access_rights WHERE item = " + item
                );
            rows = pstmt.executeUpdate();
            for (Iterator i = access.iterator(); i.hasNext();) {
                RAccess myRA = (RAccess) i.next();
                pstmt = conn.prepareStatement(
                    "INSERT INTO " + SCHEMAPATH + ".access_rights (item, groupid, type, negate, constraints) " +
                    "VALUES (" + item + ", " + myRA.getGroupID() + ", '" + myRA.getType() + "', " +
                    ((myRA.getNegation()) ? "'T'" : "'F'") + ", " +
                    ((myRA.getConstraint() == null || myRA.getConstraint() == "n/a") ? "NULL" : "'" + myRA.getConstraint() + "'") + ")"
                    );
                rows = pstmt.executeUpdate();
            }
            myConn.release();
            isNew = false;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(0,"N/A",0, outLine);
            myConn.release();
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(0,"N/A",0, outLine);
            myConn.release();
        }
    }

}
