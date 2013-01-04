package gov.ymp.csi.auth;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;


/**
* Group is the base class for group's in the CSI
*
* @author   Bill Atchley
*/
public class Group {
    private long uID = 0;
    private String description = null;
    private String status = null;
    private HashSet positions = new HashSet();
    private HashSet subGroups = new HashSet();
    private UNID myUID = null;
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
    * creates a new empty Group object
    */
    public Group () {
        init();
        myUID = new UNID();
        myUID.create("group");
        uID = myUID.getID();
    }

    /**
    * Creates a Group object and retrieves it up from the db
    *
    * @param id     The id of the Group to lookup from the db
    */
    public Group (long id) {
        init();
        lookup (id);
    }

    /**
    * Retrieves a Group from the db and stores it in the current Group object
    *
    * @param id     The id of the Group to lookup from the db
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
    * Retrieves a Group from the db (given a passed in connection) and stores it in the current Group object
    *
    * @param id     The id of the Group to lookup from the db
    * @param conn   A connection to the DB
    */
    public void lookup (long id, Connection conn) {
        uID = 0;
        description = null;
        status = null;
        String outLine = "";
        try {

            // Create a Statement
            Statement stmt = conn.createStatement ();

            ResultSet rs;
            rs = stmt.executeQuery("SELECT id, NVL(description,'n/a'),NVL(status,'n/a') FROM " + SCHEMAPATH + ".groups WHERE id=" + id);
            //rs = stmt.executeQuery("SELECT id, ISNULL(description,'n/a'),ISNULL(status,'n/a') FROM " + SCHEMAPATH + ".groups WHERE id=" + id);
            rs.next();
            long myID = rs.getLong(1);
            String myDescription = rs.getString(2);
            String myStatus = rs.getString(3);
            if (myID == id) {
                uID = myID;
                description = myDescription;
                status = myStatus;
                isNew = false;
                myUID = new UNID(uID);
// get positions
// get subgroups
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
    * Retrieve the unique id for the current Group
    *
    * @return id     The id for the current Group
    */
    public long getID() {
        long id = uID;
        return(id);
    }

    /**
    * Retrieve the description for the current Group
    *
    * @return type     The description for the current Group
    */
    public String getDescription() {
        return(description);
    }

    /**
    * Retrieve the status for the current Group
    *
    * @return status     The status for the current Group
    */
    public String getStatus() {
        return(status);
    }

    /**
    * Retrieve the positions for the current Group
    * Stub - Not yet functional
    *
    * @return      The set of positions for the current Group
    */
    public String getPositions() {
        return(null);
    }

    /**
    * Retrieve the subgroups for the current Group
    * Stub - Not yet functional
    *
    * @return      The set of subgroups for the current Group
    */
    public String getSubgroups() {
        return(null);
    }

    /**
    * Add a position to the set of positions in the group
    * Stub - Not yet functional
    *
    * @param id      The the position to add
    */
    public void addPosition(long id) {
        //
    }

    /**
    * Add a subgroup to the set of subgorups in the group
    * Stub - Not yet functional
    *
    * @param id      The the subgroup to add
    */
    public void addSubgroup(long id) {
        //
    }

    /**
    * Remove the current group from the DB
    * Stub - Not yet functional
    */
    public void drop(long id) {
        //
    }

    /**
    * Remove a position from the group
    * Stub - Not yet functional
    *
    * @param id      The the position to remove
    */
    public void dropPosition(long id) {
        //
    }

    /**
    * Remove a subgroup from the group
    * Stub - Not yet functional
    *
    * @param id      The the subgroup to remove
    */
    public void dropSubgroup(long id) {
        //
    }

    /**
    * Set the description for the current Group
    *
    * @param newDescription     The new description
    */
    public void setDescription(String newDescription) {
        description = newDescription;
    }

    /**
    * Set the status for the current Group
    *
    * @param newStatus     The new status
    */
    public void setStatus(String newStatus) {
        status = newStatus;
    }

    /**
    * Save the current Group to the DB
    * Stub - Not yet functional
    */
    public void save() {
        //save funciton
        String outLine = "";
        DbConn myConn = new DbConn(SCHEMA);
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            Statement stmt = conn.createStatement ();
            ResultSet rs;
            PreparedStatement pstmt = null;
//
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
