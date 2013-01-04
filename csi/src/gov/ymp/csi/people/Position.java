package gov.ymp.csi.people;

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
import gov.ymp.csi.misc.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.auth.*;


/**
* Person is the class for person information in the CSI
*
* @author   Bill Atchley
*/
public class Position {
    private long positionID = 0;
    private String title = null;
    private String description = null;
    private long organizationID = 0;
    private long supervisorID = 0;
    private Person supervisor = null;
    private long personID = 0;
    private Person person = null;
    private int domainID = 0;
    private boolean isValid = false;
    private GlobalMembership membership = null;
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
    * Creates a new Position object
    *
    */
    public Position () {
        init();
        //
    }


    /**
    * Creates a new Position object given an id
    *
    * @param myConn       A DB connection handle (DbConn)
    * @param id           The id to retrieve for the new position object (long)
    */
    public Position (DbConn myConn, long id) {
        init();
        getInfo(myConn, id);
    }


    /**
    * Creates a new Position object with an unid
    *
    * @param myConn           A DB connection handle (DbConn)
    * @param positionUnid     The unid to retrieve for the new position object (UNID)
    */
    public Position (DbConn myConn, UNID positionUnid) {
        init();
        long id = positionUnid.getID();
        getInfo(myConn, id);
    }


    /**
    * Creates a new Position object given a person id
    *
    * @param personID     The person id to lookup and retrieve the matching position object (long)
    * @param myConn       A DB connection handle (DbConn)
    */
    public Position (long personID, DbConn myConn) {
        ResultSet rset = null;
        Statement stmt = null;
        long id = 0;
        init();
        String sqlCode = "SELECT id FROM " + SCHEMAPATH + ".position WHERE personid =" + personID + "";
        try {
            stmt = myConn.conn.createStatement();
            rset = stmt.executeQuery (sqlCode);
            if (rset.next()) {
                id = rset.getLong(1);
                getInfo(myConn, id);
            }
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
    * Retrieve Position info from the db (by Position id)
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param id         The UNID id of the person to lookup
    */
    public void lookup(DbConn myConn, long id) {
        getInfo(myConn, id);

    }


    /**
    * Retrieve Position info from the db (by Position id)
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param id         The UNID id of the person to lookup
    */
    public void getInfo(DbConn myConn, long id) {
        try {
            String sqlCode = "SELECT id,title, description, organizationid, supervisorid, personid " +
                  "FROM " + SCHEMAPATH + ".position " +
                  "WHERE id=" + id + "";
//System.out.println(sqlCode);
            getInfo(myConn, sqlCode, id);
        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }


    }


    /**
    * Retrieve Position info from the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param sqlCode    The sql statement needed to perform the lookup
    */
    private void getInfo(DbConn myConn, String sqlCode, long id) {
        ResultSet rset = null;
        Statement stmt = null;
        try {
            stmt = myConn.conn.createStatement();
            rset = stmt.executeQuery (sqlCode);
            while (rset.next()) {
                positionID = rset.getLong(1);
                title = rset.getString(2);
                description = rset.getString(3);
                organizationID = rset.getLong(4);
                supervisorID = rset.getLong(5);
                personID = rset.getLong(6);
            }
            membership = new GlobalMembership(myConn, id);
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
    * Method to retrieve a set of Positions
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public static Position[] getPositionSet(DbConn myConn) {
        String sqlcode = "";
        String sqlFrom = "";
        int returnSize = 0;
        ResultSet rset = null;
        Statement stmt = null;
        Position[] pos = null;
        try {
            stmt = myConn.conn.createStatement();

            sqlFrom = myConn.getSchemaPath() + ".position ";

            sqlcode = "SELECT count(*) FROM " + sqlFrom;
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);
            rset.next();
            returnSize = rset.getInt(1);
            rset.close();

            sqlcode = "SELECT id, title, description, organizationid, supervisorid, personid " +
                "FROM " + sqlFrom + " ORDER BY description";
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);

            pos = new Position[returnSize];
            int i = 0;
            while (rset.next()) {
                pos[i] = new Position();
                pos[i].positionID = rset.getLong(1);
                pos[i].title = rset.getString(2);
                pos[i].description = rset.getString(3);
                pos[i].organizationID = rset.getLong(4);
                pos[i].supervisorID = rset.getLong(5);
                pos[i].personID = rset.getLong(6);
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

        return pos;
    }


    /**
    * Add Position to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void add(DbConn myConn) {
        add(myConn, 0);
    }


    /**
    * Add Position to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param userID     Id of Person doing operation
    */
    public void add(DbConn myConn, long userID) {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            stmt = myConn.conn.createStatement();
            String sqlcode = "";
            UNID myUNID = new UNID();
            myUNID.create("position", myConn, userID);
            positionID = myUNID.getID();
            myUNID.save(myConn, userID);
            sqlcode = "INSERT INTO " + SCHEMAPATH + ".position (id,title, description, organizationid, supervisorid, personid) " +
                "VALUES (" + positionID + ", ?, " +
                ((description != null) ? "?" : "NULL") + ", " +
                ((organizationID != 0) ? organizationID + " " : "NULL") + ", " +
                ((supervisorID != 0) ? supervisorID + " " : "NULL") + ", " +
                ((personID != 0) ? personID + " " : "NULL") + ")";
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            int sqlID = 0;
            if (title != null && title != "n/a") {
                pstmt.setString(++sqlID, title);
            }
            if (description != null && description != "n/a") {
                pstmt.setString(++sqlID, description);
            }
            int rows = pstmt.executeUpdate();
            ALog.logActivity(myConn, userID, "csi", 1, "Position '" + title + "' added");

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
    * Save person to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void save(DbConn myConn) {
        save (myConn, 0);
    }


    /**
    * Save position to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param userID     Id of Person doing operation
    */
    public void save(DbConn myConn, long userID) {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            stmt = myConn.conn.createStatement();
            String sqlcode = "";
            sqlcode = "UPDATE " + SCHEMAPATH + ".position SET " +
                "title=?, " +
                "description=" + ((description != null) ? "?" : "NULL") + ", " +
                "organizationid=" + ((organizationID != 0) ? organizationID + " " : "NULL") + ", " +
                "supervisorid=" + ((supervisorID != 0) ? supervisorID + " " : "NULL") + ", " +
                "personid=" + ((personID != 0) ? personID + " " : "NULL") + " " +
                "WHERE id=" + positionID;
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            int sqlID = 0;
            if (title != null && title != "n/a") {
                pstmt.setString(++sqlID, title);
            }
            if (description != null && description != "n/a") {
                pstmt.setString(++sqlID, description);
            }
            int rows = pstmt.executeUpdate();
            //ALog.logActivity(myConn, personID, "csi", 2, "Position '" + title + "' updated");
            ALog.logActivity(myConn, 0, "csi", 2, "Position '" + title + "' updated");

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
    * Create initial Positions in the db
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public static void createInitial(DbConn myConn) {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            stmt = myConn.conn.createStatement();
            String sqlcode = "";
            sqlcode = "SELECT p.id, p.username, p.firstname, p.lastname, p.domainid, d.name FROM csi.person p, csi.domains d " +
                "WHERE d.id=p.domainid AND p.domainid > 0 AND p.id NOT IN (SELECT NVL(personid, 0) FROM csi.position)";
            rs = stmt.executeQuery (sqlcode);

            int i = 0;
            while (rs.next()) {
                Position myPos = new Position();
                myPos.setTitle("Default position for " + rs.getString(2) + "-" + rs.getInt(5));
                myPos.setDescription("Position for " + rs.getString(3) + " " + rs.getString(4) + " of " + rs.getString(6));
                myPos.setPersonID(rs.getLong(1));
                myPos.add(myConn);
                i++;
            }
            //ALog.logActivity(myConn, personID, "csi", 2, "Position '" + title + "' updated");
            //ALog.logActivity(myConn, 0, "csi", 2, "Position '" + title + "' updated");

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
    * Get positionID
    *
    * @return      The value of positionID.
    */
    public long getID() {
         return(positionID);
    }


    /**
    * Get title
    *
    * @return      The value of title.
    */
    public String getTitle() {
         return(title);
    }


    /**
    * Get description
    *
    * @return      The value of lastName.
    */
    public String getDescription() {
         return(description);
    }


    /**
    * Get organizationID
    *
    * @return      The value of organizationID.
    */
    public long getOrganizationID() {
         return(organizationID);
    }


    /**
    * Get supervisorID
    *
    * @return      The value of supervisorID.
    */
    public long getSupervisorID() {
         return(supervisorID);
    }


    /**
    * Get personID
    *
    * @return      The value of personID.
    */
    public long getPersonID() {
         return(personID);
    }


    /**
    * Set title
    *
    * @param value     The value to set title to
    */
    public void setTitle(String value) {
         title = value;
    }


    /**
    * Set description
    *
    * @param value     The value to set description to
    */
    public void setDescription(String value) {
         description = value;
    }


    /**
    * Set organizationID
    *
    * @param value     The value to set organizationID to
    */
    public void setOrganizationID(long value) {
         organizationID = value;
    }


    /**
    * Set supervisorID
    *
    * @param value     The value to set supervisorID to
    */
    public void setSupervisorID(long value) {
         supervisorID = value;
    }


    /**
    * Set personID
    *
    * @param value     The value to set personID to
    */
    public void setPersonID(long value) {
         personID = value;
    }


    /**
    * Determin if the current position has membership in the given id (role, permission, group, etc)
    *
    * @param  id   The id to test for membership
    * @return      The result of the membership test
    */
    public boolean belongsTo(long id) {
        if (id != 0 && membership != null) {
            return(membership.belongsTo(id));
        } else {
            return(false);
        }
    }


}
