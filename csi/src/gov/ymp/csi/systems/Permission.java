package gov.ymp.csi.systems;

// Support classes
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import java.math.*;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.systems.*;


/**
* Person is the class for person information in the CSI
*
* @author   Bill Atchley
*/
public class Permission {
    private long permissionID = 0;
    private long systemID = 0;
    private String key = null;
    private String description = null;
    private Sys system = null;

    //public static String SCHEMA = "csi.csi";
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
    * Creates a new Permission object
    *
    */
    public Permission () {
        init();
        //
    }


    /**
    * Creates a new Permission object given an id
    *
    * @param myConn       A DB connection handle (DbConn)
    * @param id           The id to retrieve for the new permission object (long)
    */
    public Permission (DbConn myConn, long id) {
        init();
        getInfo(myConn, id);
    }


    /**
    * Creates a new Permission object with an unid
    *
    * @param myConn           A DB connection handle (DbConn)
    * @param permissionUnid     The unid to retrieve for the new permission object (UNID)
    */
    public Permission (DbConn myConn, UNID permissionUnid) {
        init();
        long id = permissionUnid.getID();
        getInfo(myConn, id);
    }

    /**
    * Creates a new Permission object with a system id and key value
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param system     The id of the system to lookup
    * @param key        The key to lookup
    */
    public Permission (DbConn myConn, long system, String key) {
        init();
        getInfo(myConn, system, key);
    }


    /**
    * Retrieve Permission info from the db (by Permission id)
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param id         The UNID id of the person to lookup
    */
    public void lookup(DbConn myConn, long id) {
        getInfo(myConn, id);

    }


    /**
    * Retrieve Permission info from the db (by Permission id)
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param system     The id of the system to lookup
    * @param key        The key to lookup
    */
    public void lookup(DbConn myConn, long system, String key) {
        getInfo(myConn, system, key);

    }


    /**
    * Retrieve Permission info from the db (by Permission id)
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param id         The UNID id of the person to lookup
    */
    public void getInfo(DbConn myConn, long id) {
        try {
            String sqlCode = "SELECT p.id ,p.systemid, p.key, p.description " +
                  "FROM " + SCHEMAPATH + ".permissions p " +
                  "WHERE p.id=" + id + "";
//System.out.println(sqlCode);
            getInfo(myConn, sqlCode);
        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }


    }


    /**
    * Retrieve Permission info from the db (by Permission id)
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param system     The id of the system to lookup
    * @param key        The key to lookup
    */
    public void getInfo(DbConn myConn, long system, String key) {
        try {
            String sqlCode = "SELECT p.id ,p.systemid, p.key, p.description " +
                  "FROM " + SCHEMAPATH + ".permissions p " +
                  "WHERE p.systemid=" + system + " AND p.key='" + key +"' ";
//System.out.println(sqlCode);
            getInfo(myConn, sqlCode);
        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }


    }


    /**
    * Retrieve Permission info from the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param sqlCode    The sql statement needed to perform the lookup
    */
    private void getInfo(DbConn myConn, String sqlCode) {
        ResultSet rset = null;
        Statement stmt = null;
        try {
            stmt = myConn.conn.createStatement();
            rset = stmt.executeQuery (sqlCode);
            while (rset.next()) {
                permissionID = rset.getLong(1);
                systemID = rset.getLong(2);
                key = rset.getString(3);
                description = rset.getString(4);
            }
            //system = new Sys(myConn, systemID);
        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }
        finally {
            if (rset != null)
                try { rset.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }


    }



    /**
    * Method to retrieve a set of Permissions (all) in a HashMap
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public static HashMap getPermissionMap(DbConn myConn) {
        Permission[] ps = getPermissionSet(myConn, 0);
        HashMap hm = new HashMap((ps.length + 1));
        for (int i=0; i<ps.length; i++) {
            hm.put((ps[i].getSystemID() + "-" + ps[i].getKey()), new Long(ps[i].getID()));
        }
        return hm;
    }



    /**
    * Method to retrieve a set of Permissions (all)
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public static Permission[] getPermissionSet(DbConn myConn) {
        return getPermissionSet(myConn, 0);
    }


    /**
    * Method to retrieve a set of Permissions (for a given system, or all if system is 0)
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param system     The system to look up (0 for all)
    */
    public static Permission[] getPermissionSet(DbConn myConn, long system) {
        String sqlcode = "";
        String sqlWhere = "";
        String sqlFrom = "";
        int returnSize = 0;
        ResultSet rset = null;
        Statement stmt = null;
        Permission[] per = null;
        try {
            stmt = myConn.conn.createStatement();

            sqlWhere = "p.systemid=s.id";
            sqlWhere += ((system != 0) ? " AND p.systemid=" + system : "");

            sqlFrom = myConn.getSchemaPath() + ".permissions p, " + myConn.getSchemaPath() + ".systems s ";

            sqlcode = "SELECT count(*) FROM " + sqlFrom + " WHERE " + sqlWhere;
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);
            rset.next();
            returnSize = rset.getInt(1);
            rset.close();

            sqlcode = "SELECT p.id, p.systemid, p.key, p.description, s.acronym, s.description, s.globalread " +
                "FROM " + sqlFrom + "WHERE " + sqlWhere + " ORDER BY s.acronym, p.description";
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);

            per = new Permission[returnSize];
            int i = 0;
            while (rset.next()) {
                per[i] = new Permission();
                per[i].permissionID = rset.getLong(1);
                per[i].systemID = rset.getLong(2);
                per[i].key = rset.getString(3);
                per[i].description = rset.getString(4);
                per[i].system = new Sys();
                per[i].system.id = per[i].systemID;
                per[i].system.acronym = rset.getString(5);
                per[i].system.description = rset.getString(6);
                per[i].system.globalRead = (rset.getString(7).equals("T")) ? true : false;
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

        return per;
    }


    /**
    * Add Permission to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void add(DbConn myConn) {
        add(myConn, 0);
    }


    /**
    * Add Permission to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param userid     userid performing operation
    */
    public void add(DbConn myConn, long userid) {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            stmt = myConn.conn.createStatement();
            String sqlcode = "";
            UNID myUNID = new UNID();
            myUNID.create("Permission");
            permissionID = myUNID.getID();
            myUNID.save(myConn.conn);
            sqlcode = "INSERT INTO " + SCHEMAPATH + ".permissions (id, systemid, key, description) " +
                "VALUES (" + permissionID + ", " + systemID + ", " +
                ((key != null) ? "'" + key + "'" : "NULL") + ", " +
                ((description != null) ? "'" + description + "'" : "'Undefined " + permissionID + "'") + ")";
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            int rows = pstmt.executeUpdate();
            Sys sys = new Sys(myConn, systemID);
            ALog.logActivity(myConn, userid, "csi", 1, "Permission '" + sys.getAcronym() + "/" + description + "' added");

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
    * Save permission to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void save(DbConn myConn) {
        save(myConn, 0);
    }


    /**
    * Save permission to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param userid     userid performing operation
    */
    public void save(DbConn myConn, long userid) {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            stmt = myConn.conn.createStatement();
            String sqlcode = "";
            sqlcode = "UPDATE " + SCHEMAPATH + ".permissions SET " +
                "systemid=" + systemID + ", " +
                "key=" + ((key != null) ? "'" + key + "'" : "NULL") + ", " +
                "description=" + ((description != null) ? "'" + description + "'" : "'Undefined " + permissionID + "'") + " " +
                "WHERE id=" + permissionID;
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            int rows = pstmt.executeUpdate();
            Sys sys = new Sys(myConn, systemID);
            ALog.logActivity(myConn, userid, "csi", 2, "Permission '" + sys.getAcronym() + "/" + description + "' updated");

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
    * Remove permission to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param userid     userid performing operation
    */
    public void drop(DbConn myConn, long userid) {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            stmt = myConn.conn.createStatement();
            String sqlcode = "";
            sqlcode = "DELETE FROM " + SCHEMAPATH + ".permissions  " +
                "WHERE id=" + permissionID;
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            int rows = pstmt.executeUpdate();
            Sys sys = new Sys(myConn, systemID);
            ALog.logActivity(myConn, userid, "csi", 3, "Permission '" + sys.getAcronym() + "/" + description + "' removed");
            UNID myUnid = new UNID(permissionID, myConn.conn);
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
    * Get permissionID
    *
    * @return      The value of permissionID.
    */
    public long getID() {
         return(permissionID);
    }


    /**
    * Get systemID
    *
    * @return      The value of systemID.
    */
    public long getSystemID() {
         return(systemID);
    }


    /**
    * Get system
    *
    * @return      The value of system.
    */
    public Sys getSystem() {
         return(system);
    }


    /**
    * Get key
    *
    * @return      The value of key.
    */
    public String getKey() {
         return(key);
    }


    /**
    * Get description
    *
    * @return      The value of description.
    */
    public String getDescription() {
         return(description);
    }


    /**
    * Set systemID
    *
    * @param value     The value to set systemID to
    */
    public void setSystemID(long value) {
         systemID = value;
    }


    /**
    * Set key
    *
    * @param value     The value to set key to
    */
    public void setKey(String value) {
         key = value;
    }


    /**
    * Set description
    *
    * @param value     The value to set description to
    */
    public void setDescription(String value) {
         description = value;
    }


}
