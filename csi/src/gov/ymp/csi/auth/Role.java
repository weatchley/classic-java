package gov.ymp.csi.auth;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.UNID.*;


/**
* Role is the base class for role's in the CSI
*
* @author   Bill Atchley
*/
public class Role {
    private long roleID = 0;
    private String description = null;
    private String status = null;
    private HashSet groups = new HashSet();
    private HashSet positions = new HashSet();
    private HashSet permissions = new HashSet();
    private UNID myUNID = null;
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
    * creates a new empty Role object
    */
    public Role () {
        init();

    }

    /**
    * Creates a Role object and retrieves it up from the db
    *
    * @param id     The id of the Role to lookup from the db
    */
    public Role (long id) {
        init();
        lookup (id);
    }

    /**
    * Creates a Role object and retrieves it up from the db
    *
    * @param id       The id of the Role to lookup from the db
    * @param myConn   A connection to the DB (DbConn)
    */
    public Role (DbConn myConn, long id) {
        init();
        lookup (myConn, id);
    }

    /**
    * Retrieves a Role from the db and stores it in the current Role object
    *
    * @param id     The id of the Role to lookup from the db
    */
    public void getInfo (long id) {
        lookup(id);
    }

    /**
    * Retrieves a Role from the db and stores it in the current Role object
    *
    * @param conn   A connection to the DB
    * @param id     The id of the Role to lookup from the db
    */
    public void getInfo (DbConn myConn, long id) {
        lookup(myConn, id);
    }

    /**
    * Retrieves a Role from the db and stores it in the current Role object
    *
    * @param id     The id of the Role to lookup from the db
    */
    public void lookup (long id) {
        String outLine = "";
        DbConn myConn = new DbConn(SCHEMA);
        try {

            lookup (myConn, id);

            myConn.release();
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
            myConn.release();
        }
    }

    /**
    * Retrieves a Role from the db (given a passed in connection) and stores it in the current Role object
    * Stub - Not yet fully functional
    *
    * @param id     The id of the Role to lookup from the db
    * @param conn   A connection to the DB
    */
    public void lookup (DbConn myConn, long id) {
        roleID = 0;
        description = null;
        status = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = myConn.conn;
        String sqlcode = "";
        String sqlFromWhere = "";
        int returnSize = 0;
        try {

            // Create a Statement
            stmt = conn.createStatement ();

            sqlcode = "SELECT NVL(id, 0), NVL(description,'n/a'),NVL(status,'') FROM " + SCHEMAPATH + ".roles WHERE id=" + id;
            //sqlcode = "SELECT ISNULL(id, 0), ISNULL(description,'n/a'),ISNULL(status,'n/a') FROM " + SCHEMAPATH + ".roles WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            long myID = rs.getLong(1);
            String myDescription = rs.getString(2);
            String myStatus = rs.getString(3);
            if (myID == id) {
                roleID = myID;
                description = myDescription;
                status = myStatus;
                isNew = false;
                myUNID = new UNID(roleID);
                rs.close();
// get groups
// get positions
                sqlFromWhere = " FROM " + SCHEMAPATH + ".position_roles WHERE role=" + roleID;
                sqlcode = "SELECT position, role" + sqlFromWhere;
//System.out.println(sqlcode);
                rs = stmt.executeQuery (sqlcode);

                positions = new HashSet();
                while (rs.next()) {
                    positions.add(new Long(rs.getLong(1)));
                }
                rs.close();

// get permissions
                sqlFromWhere = " FROM " + SCHEMAPATH + ".role_permissions WHERE role=" + roleID;
                sqlcode = "SELECT role, permission, negate" + sqlFromWhere;
//System.out.println(sqlcode);
                rs = stmt.executeQuery (sqlcode);

                permissions = new HashSet();
                while (rs.next()) {
                    permissions.add(new Long(rs.getLong(2)));
                }
                rs.close();
            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
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
    * Retrieve the unique id for the current Role
    *
    * @return id     The id for the current Role
    */
    public long getID() {
        long id = roleID;
        return(id);
    }

    /**
    * Retrieve the description for the current Role
    *
    * @return      The description for the current Role
    */
    public String getDescription() {
        return(description);
    }

    /**
    * Retrieve the status for the current Role
    *
    * @return status     The status for the current Role
    */
    public String getStatus() {
        return(status);
    }

    /**
    * Retrieve the groups for the current Role
    * Stub - Not yet functional
    *
    * @return      The set of groups for the current Role
    */
    public String getGroups() {
        return(null);
    }

    /**
    * Retrieve the positions for the current Role
    *
    * @return      The set of positions for the current Role
    */
    public HashSet getPositions() {
        return(positions);
    }

    /**
    * Retrieve the positions for the current Role
    *
    * @return      The set of positions for the current Role
    */
    public long [] getPositionsArray() {
        Object myItems[] = positions.toArray();
        long [] myItems2 = new long[(myItems.length - 1)];
        for (int i=0; i<myItems.length; i++) {
            Long tmp = (Long) myItems[i];
            myItems2[i] = tmp.longValue();
        }
        return(myItems2);
    }

    /**
    * Retrieve the permissions for the current Role
    *
    * @return      The set of permissions for the current Role
    */
    public HashSet getPermissions() {
        return(permissions);
    }

    /**
    * Retrieve the permissions for the current Role
    *
    * @return      The set of permissions for the current Role
    */
    public long[] getPermissionsArray() {
        Object myItems[] = permissions.toArray();
        long [] myItems2 = new long[(myItems.length - 1)];
        for (int i=0; i<myItems.length; i++) {
            Long tmp = (Long) myItems[i];
            myItems2[i] = tmp.longValue();
        }
        return(myItems2);
    }

    /**
    * Determine if a user has a role using the current Role
    * Stub - Not yet functional (always true)
    *
    * @param userid  User id to test
    * @return      True or false as to if the user has role
    */
    public boolean hasRole(long userid) {
        return(true);
    }

    /**
    * Determine if a user has a role using a passed in Role
    * Stub - Not yet functional (always true)
    *
    * @param userid  User id to test
    * @param tRole Role to test
    * @return      True or false as to if the user has role
    */
    static public boolean hasRole(long userid, long tRole) {
        return(true);
    }

    /**
    * Determine if a user has a given permission
    * Stub - Not yet functional (always true)
    *
    * @param userid  User id to test
    * @param perm  Role to test
    * @return      True or false as to if the user has permission
    */
    static public boolean hasPermission(long userid, long perm) {
        return(true);
    }

    /**
    * Determine if a given position is in the current role
    *
    * @param id    Position to test
    * @return      True or false as to if the user has permission
    */
    public boolean hasPosition(long id) {
        return(positions.contains(new Long(id)));
    }

    /**
    * Determine if a given permission is in the current role
    *
    * @param id    Permission to test
    * @return      True or false as to if the user has permission
    */
    public boolean hasPermission(long id) {
        return(permissions.contains(new Long(id)));
    }

    /**
    * Add a group to the set of groups in the role
    * Stub - Not yet functional
    *
    * @param id      The the group to add
    */
    public void addGroup(long id) {
        //
    }

    /**
    * Add a position to the set of positions in the role
    *
    * @param id      The the position to add
    */
    public void addPosition(long id) {
        positions.add(new Long(id));
    }

    /**
    * Add a permission to the set of permissions in the role
    *
    * @param id      The the permission to add
    */
    public void addPermission(long id) {
        permissions.add(new Long(id));
    }

    /**
    * clear all groups in the role
    * Stub - Not yet functional
    *
    */
    public void clearGroups() {
        //groups.clear();
    }

    /**
    * clear all positions in the role
    *
    */
    public void clearPositions() {
        positions.clear();
    }

    /**
    * clear all permissions in the role
    *
    */
    public void clearPermissions() {
        permissions.clear();
    }

    /**
    * Remove the current role from the DB
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param userid     userid performing operation
    */
    public void drop(DbConn myConn, long userid) {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int rows = 0;
        String sqlcode = "";
        try {
            stmt = myConn.conn.createStatement();
            Connection conn = myConn.conn;
            pstmt = conn.prepareStatement(
                "DELETE FROM " + SCHEMAPATH + ".position_roles WHERE role = " + roleID
                );
            rows = pstmt.executeUpdate();
            sqlcode = "DELETE FROM " + SCHEMAPATH + ".role_permissions WHERE role = " + roleID;
//System.out.println(sqlcode);
            pstmt = conn.prepareStatement(sqlcode);
            rows = pstmt.executeUpdate();
            sqlcode = "";
            sqlcode = "DELETE FROM " + SCHEMAPATH + ".roles  " +
                "WHERE id=" + roleID;
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            rows = pstmt.executeUpdate();
            ALog.logActivity(myConn, userid, "csi", 3, "Role '" + description + "' removed");
            UNID myUnid = new UNID(roleID, myConn.conn);
            myUnid.setStatus("deleted");
            myUnid.save(myConn.conn);

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
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
    * Remove a group from the role
    * Stub - Not yet functional
    *
    * @param id      The the group to remove
    */
    public void dropGroup(long id) {
        //
    }

    /**
    * Remove a position from the role
    * Stub - Not yet functional
    *
    * @param id      The the position to remove
    */
    public void dropPosition(long id) {
        //
    }

    /**
    * Remove a permission from the role
    * Stub - Not yet functional
    *
    * @param id      The the permission to remove
    */
    public void dropPermission(long id) {
        //
    }

    /**
    * Set the description for the current Role
    *
    * @param value     The new description
    */
    public void setDescription(String value) {
        description = value;
    }

    /**
    * Set the status for the current Role
    *
    * @param value     The new status
    */
    public void setStatus(String value) {
        status = value;
    }


    /**
    * Add role to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void add(DbConn myConn) {
        add (myConn, 0);
    }


    /**
    * Add role to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param userid     ID of user performing function
    */
    public void add(DbConn myConn, long userid) {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            stmt = myConn.conn.createStatement();
            String sqlcode = "";
            UNID myUNID = new UNID();
            myUNID.create("Role");
            roleID = myUNID.getID();
            myUNID.save(myConn.conn);
            sqlcode = "INSERT INTO " + SCHEMAPATH + ".roles (id, description, status) " +
                "VALUES (" + roleID + ", " +
                ((description != null) ? "'" + description + "'" : "'Undefined " + roleID + "'") + ", " +
                ((status != null) ? "'" + status + "' " : "NULL") + ")";
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            int rows = pstmt.executeUpdate();
            savePositions(myConn);
            savePermissions(myConn);
            ALog.logActivity(myConn, userid, "csi", 1, "Role '" + description + "' added");
            isNew = false;

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
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
    * Save the current Role to the DB
    *
    * @param id     The id of the Role to lookup from the db
    */
    public void save(DbConn myConn) {
        save (myConn, 0);
    }


    /**
    * Save the current Role to the DB
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param userid     ID of user performing function
    */
    public void save(DbConn myConn, long userid) {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            stmt = myConn.conn.createStatement();
            String sqlcode = "";
            sqlcode = "UPDATE " + SCHEMAPATH + ".roles SET " +
                "description=" + ((description != null) ? "'" + description + "'" : "'Undefined " + roleID + "''") + ", " +
                "status=" + ((status != null) ? "'" + status + "' " : "NULL") + " " +
                "WHERE id=" + roleID;
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            int rows = pstmt.executeUpdate();
            savePositions(myConn);
            savePermissions(myConn);
            ALog.logActivity(myConn, userid, "csi", 2, "Role '" + description + "' updated");

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
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
    * Save the current position set to the DB
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void savePositions(DbConn myConn) {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = myConn.conn;
        int rows = 0;
        try {
            pstmt = conn.prepareStatement(
                "DELETE FROM " + SCHEMAPATH + ".position_roles WHERE role = " + roleID
                );
            rows = pstmt.executeUpdate();
            for (Iterator i = positions.iterator(); i.hasNext();) {
                Long tmp = (Long) i.next();
                long myID = tmp.longValue();
                pstmt = conn.prepareStatement(
                    "INSERT INTO " + SCHEMAPATH + ".position_roles (position, role) " + "VALUES (" + myID + ", " + roleID + ")"
                    );
                rows = pstmt.executeUpdate();
//System.out.println(myID + "/" + roleID);
            }
        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
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
    * Save the current permission set to the DB
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void savePermissions(DbConn myConn) {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = myConn.conn;
        int rows = 0;
        String sqlcode = "";
        try {
            sqlcode = "DELETE FROM " + SCHEMAPATH + ".role_permissions WHERE role = " + roleID;
//System.out.println(sqlcode);
            pstmt = conn.prepareStatement(sqlcode);
            rows = pstmt.executeUpdate();
            for (Iterator i = permissions.iterator(); i.hasNext();) {
                Long tmp = (Long) i.next();
                long myID = tmp.longValue();
                sqlcode = "INSERT INTO " + SCHEMAPATH + ".role_permissions (role, permission, negate) " + "VALUES (" + roleID + ", " + myID + ", 'F')";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                rows = pstmt.executeUpdate();
//System.out.println(roleID + "/" + myID);
            }
        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
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
    * Method to retrieve a set of Roles
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public static Role[] getRoleSet(DbConn myConn) {
        String sqlcode = "";
        String sqlFrom = "";
        int returnSize = 0;
        ResultSet rset = null;
        Statement stmt = null;
        Role[] roles = null;
        try {
            stmt = myConn.conn.createStatement();

            sqlFrom = myConn.getSchemaPath() + ".roles ";

            sqlcode = "SELECT count(*) FROM " + sqlFrom;
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);
            rset.next();
            returnSize = rset.getInt(1);
            rset.close();

            sqlcode = "SELECT id, description, status " +
                "FROM " + sqlFrom + " ORDER BY description";
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);

            roles = new Role[returnSize];
            int i = 0;
            while (rset.next()) {
                roles[i] = new Role();
                roles[i].roleID = rset.getLong(1);
                roles[i].description = rset.getString(2);
                roles[i].status = rset.getString(3);
                i++;
            }
            rset.close();

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }
        finally {
            if (rset != null)
                try { rset.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return roles;
    }


}
