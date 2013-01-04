package gov.ymp.csi.UNID;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import java.lang.*;


/**
* UNID is the base class for universal id's in the CSI
*
* @author   Bill Atchley
*/
public class UNID {
    private long uID = 0;
    private String type = null;
    private long parent = 0;
    private String status = null;
    private boolean isNew = true;

    //private static String SCHEMA = "csi";
    private static String SCHEMA = null;
    private static String SCHEMAPATH = null;
    private static String DBTYPE = null;

    /**
    * creates a new empty UNID object
    */
    public UNID () {
        uID = 0;
        init();
    }

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
    * Creates a UNID object and retrieves it up from the db
    *
    * @param id     The id of the UNID to lookup from the db
    */
    public UNID (long id) {
        init();
        lookup (id);
    }

    /**
    * Creates a UNID object and retrieves it up from the db (given a DB connection)
    *
    * @param id     The id of the UNID to lookup from the db
    * @param myConn Connection to the db
    */
    public UNID (long id, DbConn myConn) {
        init();
        lookup (id, myConn.conn);
    }

    /**
    * Creates a UNID object and retrieves it up from the db (given a DB connection)
    *
    * @param id     The id of the UNID to lookup from the db
    * @param conn   Connection to the db
    */
    public UNID (long id, Connection conn) {
        init();
        lookup (id, conn);
    }

    /**
    * Retrieves a new unique id from the db and stores it in the current UNID object
    *
    * @return id     The new UNID id from the db
    */
    private long getNewID() {
        long id = 0;
        String outLine = "";
        DbConn myConn = new DbConn(SCHEMA);
        try {

            Connection conn = myConn.conn;

            id = this.getNewID(conn);

            myConn.release();
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
            myConn.release();
        }
        return (id);
    }

    /**
    * Retrieves a new unique id from the db (given a passed in connection) and stores it in the current UNID object
    *
    * @param conn    A connection to the DB
    * @return id     The new UNID id from the db
    */
    private long getNewID(Connection conn) {
        long id = 0;
        DbConn myConn = new DbConn(conn);
        return (getNewID(myConn, 0));
    }

    /**
    * Retrieves a new unique id from the db (given a passed in connection) and stores it in the current UNID object
    *
    * @param myConn  A connection to the DB
    * @return id     The new UNID id from the db
    */
    private long getNewID(DbConn myConn) {
        return (getNewID(myConn, 0));
    }

    /**
    * Retrieves a new unique id from the db (given a passed in connection) and stores it in the current UNID object
    *
    * @param myConn  A connection to the DB
    * @param userID  The person ID of the user performing the action
    *
    * @return id     The new UNID id from the db
    */
    private long getNewID(DbConn myConn, long userID) {
        long id = 0;
        Connection conn = myConn.conn;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        try {

            // Create a Statement
            stmt = conn.createStatement ();
            String testID = "";

            if (DBTYPE.equals("OraDBServer")) {
                rs = stmt.executeQuery("SELECT " + SCHEMAPATH + ".UNID_ID_SEQ.NEXTVAL FROM dual");
                rs.next();
                id = rs.getLong(1);
            } else if (DBTYPE.equals("MSsqlDBServer")) {
                testID = Utils.genNewID();
                pstmt = conn.prepareStatement(
                    "INSERT INTO " + SCHEMAPATH + ".unid (status) " + "VALUES ('" + testID + "')"
                    );
                int rows = pstmt.executeUpdate();
                rs = stmt.executeQuery("SELECT id FROM " + SCHEMAPATH + ".unid WHERE status='" + testID + "'");
                rs.next();
                id = rs.getLong(1);
                //id = rs.getInt(1);
                isNew = false;
            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - UNID getNewID");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - UNID getNewID");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
            if (pstmt != null)
                try { pstmt.close(); } catch (Exception i) {}
        }
        return (id);
    }

    /**
    * Retrieves a UNID from the db and stores it in the current UNID object
    *
    * @param id     The id of the UNID to lookup from the db
    */
    public void lookup (long id) {
        String outLine = "";
        DbConn myConn = new DbConn(SCHEMA);
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            Statement stmt = conn.createStatement ();

            this.lookup (id, conn);

            myConn.release();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UNID lookup");
            myConn.release();
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UNID lookup");
            myConn.release();
        }
    }

    /**
    * Retrieves a UNID from the db (given a passed in connection) and stores it in the current UNID object
    *
    * @param id     The id of the UNID to lookup from the db
    * @param conn   A connection to the DB
    */
    public void lookup (long id, Connection conn) {
        DbConn myConn = new DbConn(conn);
        lookup(id, myConn);
    }

    /**
    * Retrieves a UNID from the db (given a passed in connection) and stores it in the current UNID object
    *
    * @param id     The id of the UNID to lookup from the db
    * @param conn   A connection to the DB
    */
    public void lookup (long id, DbConn myConn) {
        uID = 0;
        Connection conn = myConn.conn;
        type = null;
        parent = 0;
        status = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, NVL(type,'n/a'), NVL(parent, 0), NVL(status,'n/a') FROM " + SCHEMAPATH + ".unid WHERE id=" + id;
            //String sqlcode = "SELECT id, ISNULL(type,'n/a'), ISNULL(parent, 0), ISNULL(status,'n/a') FROM " + SCHEMAPATH + ".unid WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            long myID = rs.getLong(1);
            String myType = rs.getString(2);
            long myParent = rs.getLong(3);
            String myStatus = rs.getString(4);
            if (myID == id) {
                uID = myID;
                type = myType;
                parent = myParent;
                status = myStatus;
                isNew = false;
            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UNID lookup 2");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UNID lookup 2");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }

    /**
    * Creates a new UNID with no type or parent and stores it in the current UNID object
    */
    public void create() {
        uID = getNewID();
        type = null;
        parent = 0;
        status = null;
    }

    /**
    * Creates a new UNID with no type or parent and stores it in the current UNID object
    *
    * @param myConn  database connection
    */
    public void create(DbConn myConn) {
        uID = getNewID(myConn);
        type = null;
        parent = 0;
        status = null;
    }

    /**
    * Creates a new UNID with no type or parent and stores it in the current UNID object
    *
    * @param myConn  database connection
    * @param userID  The person ID of the user performing the action
    */
    public void create(DbConn myConn, long userID) {
        uID = getNewID(myConn, userID);
        type = null;
        parent = 0;
        status = null;
    }

    /**
    * Creates a new UNID with type and no parent and stores it in the current UNID object
    *
    * @param ddType     The type from the data dictionary
    */
    public void create(String ddType) {
        uID = getNewID();
        type = ddType;
        parent = 0;
        status = null;
    }

    /**
    * Creates a new UNID with type and no parent and stores it in the current UNID object
    *
    * @param ddType     The type from the data dictionary
    * @param myConn  database connection
    */
    public void create(String ddType, DbConn myConn) {
        uID = getNewID(myConn);
        type = ddType;
        parent = 0;
        status = null;
    }

    /**
    * Creates a new UNID with type and no parent and stores it in the current UNID object
    *
    * @param ddType     The type from the data dictionary
    * @param myConn  database connection
    * @param userID  The person ID of the user performing the action
    */
    public void create(String ddType, DbConn myConn, long userID) {
        uID = getNewID(myConn, userID);
        type = ddType;
        parent = 0;
        status = null;
    }

    /**
    * Creates a new UNID with type and parent and stores it in the current UNID object
    *
    * @param ddType       The type from the data dictionary
    * @param parentID     The id of the parent UNID
    */
    public void create(String ddType, long parentID) {
        uID = getNewID();
        type = ddType;
        parent = parentID;
        status = null;
    }

    /**
    * Creates a new UNID with type and parent and stores it in the current UNID object
    *
    * @param ddType       The type from the data dictionary
    * @param parentID     The id of the parent UNID
    * @param myConn       database connection
    */
    public void create(String ddType, long parentID, DbConn myConn) {
        uID = getNewID(myConn);
        type = ddType;
        parent = parentID;
        status = null;
    }

    /**
    * Creates a new UNID with type and parent and stores it in the current UNID object
    *
    * @param ddType       The type from the data dictionary
    * @param parentID     The id of the parent UNID
    * @param myConn       database connection
    * @param userID  The person ID of the user performing the action
    */
    public void create(String ddType, long parentID, DbConn myConn, long userID) {
        uID = getNewID(myConn, userID);
        type = ddType;
        parent = parentID;
        status = null;
    }

    /**
    * Retrieve the unique id for the current UNID
    *
    * @return id     The id for the current UNID
    */
    public long getID() {
        long id = uID;
        return(id);
    }

    /**
    * Retrieve the data dictionary type for the current UNID
    *
    * @return type     The data dictionary type for the current UNID
    */
    public String getType() {
        return(type);
    }

    /**
    * Retrieve the parent unique id for the current UNID
    *
    * @return parent     The parent UNID id for the current UNID
    */
    public long getParent() {
        return(parent);
    }

    /**
    * Retrieve the status for the current UNID
    *
    * @return status     The status for the current UNID
    */
    public String getStatus() {
        return(status);
    }

    /**
    * Retrieve the genealogy (creation path or 'bread crumbs') for the current UNID
    * Stub - Not yet functional
    *
    * @return genealogy     The creation path for the current UNID
    */
    public String getGenealogy() {
        String genealogy = null;
        return(genealogy);
    }

    /**
    * Retrieve the genealogy (creation path or 'bread crumbs') for a given UNID
    * Stub - Not yet functional
    *
    * @param id             The UNID id to lookup
    * @return genealogy     The creation path for the current UNID
    */
    public String getGenealogy(long id) {
        String genealogy = null;
        return(genealogy);
    }

    /**
    * Retrieve the children for the current UNID
    * Stub - Not yet functional
    *
    * @return kids     The immediate children of the current UNID
    */
    public Set getChildren() {
        Set kids = null;
        return(kids);
    }

    /**
    * Retrieve the children for a given UNID
    * Stub - Not yet functional
    *
    * @param id             The UNID id to lookup
    * @return kids     The immediate children of the current UNID
    */
    public Set getChildren(long id) {
        Set kids = null;
        return(kids);
    }

    /**
    * Set the parent unique id for the current UNID
    *
    * @param newParent     The new parent id
    */
    public void setParent(long newParent) {
        parent = newParent;
    }

    /**
    * Set the status for the current UNID
    *
    * @param newStatus     The new status
    */
    public void setStatus(String newStatus) {
        status = newStatus;
    }

    /**
    * Set the type for the current UNID
    *
    * @param newType     The new type
    */
    public void setType(String newType) {
        type = newType;
    }

    /**
    * Save the current UNID to the DB
    */
    public void save() {
        //save funciton
        String outLine = "";
        DbConn myConn = new DbConn(SCHEMA);
        try {

            Connection conn = myConn.conn;

            this.save(conn);

            myConn.release();
            isNew = false;
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UNID save");
            myConn.release();
        }
    }

    /**
    * Save the current UNID to the DB
    *
    * @param conn   Database connection
    */
    public void save(Connection conn) {
        DbConn myConn = new DbConn(conn);
        save(myConn);
    }

    /**
    * Save the current UNID to the DB
    *
    * @param conn   Database connection
    */
    public void save(DbConn myConn) {
        save(myConn, 0);
    }

    /**
    * Save the current UNID to the DB
    *
    * @param conn   Database connection
    * @param userID  The person ID of the user performing the action
    */
    public void save(DbConn myConn, long userID) {
        //save funciton
        Connection conn = myConn.conn;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        try {

            // Create a Statement
            stmt = conn.createStatement ();
            if (isNew) {
                pstmt = conn.prepareStatement(
                    "INSERT INTO " + SCHEMAPATH + ".unid (id, type, parent, status) " + "VALUES (" + uID + ", " +
                    ((type == null || type == "n/a") ? "NULL" : "'" + type + "'") + ", " +
                    ((parent == 0) ? "NULL" : "" + parent + "") + ", " +
                    ((status == null || status == "n/a") ? "NULL" : "'" + status + "'") + ")"
                    );
                int rows = pstmt.executeUpdate();
// do auth stuff
            } else {
                pstmt = conn.prepareStatement(
                    "UPDATE " + SCHEMAPATH + ".unid SET " +
                    "type = " + ((type == null || type == "n/a") ? "NULL" : "'" + type + "'") + ", " +
                    "parent = " + ((parent == 0) ? "NULL" : "" + parent + "") + ", " +
                    "status = " + ((status == null || status == "n/a") ? "NULL" : "'" + status + "'") + " " +
                    "WHERE id = " + uID
                    );
                int rows = pstmt.executeUpdate();
            }
            isNew = false;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - UNID save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - UNID save");
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
