package gov.ymp.csi.auth;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.people.*;


/**
* GlobalMembership is a misc class for auth in the CSI
*
* @author   Bill Atchley
*/
public class GlobalMembership {
    private long positionID = 0;
    private HashMap membership = new HashMap();

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
    * creates a new empty GlobalMembership object
    */
    public GlobalMembership () {
        init();

    }

    /**
    * Creates a GlobalMembership object and retrieves it up from the db
    *
    * @param id       The id of the Position to lookup from the db
    * @param myConn   A connection to the DB (DbConn)
    */
    public GlobalMembership (DbConn myConn, long id) {
        init();
        lookup (myConn, id);
    }

    /**
    * Retrieves a GlobalMembership from the db and stores it in the current GlobalMembership object
    *
    * @param id     The id of the Position to lookup from the db
    */
    public void getInfo (long id) {
        lookup(id);
    }

    /**
    * Retrieves a GlobalMembership from the db and stores it in the current GlobalMembership object
    *
    * @param conn   A connection to the DB
    * @param id     The id of the postion to lookup from the db
    */
    public void getInfo (DbConn myConn, long id) {
        lookup(myConn, id);
    }

    /**
    * Retrieves a GlobalMembership from the db and stores it in the current GlobalMembership object
    *
    * @param id     The id of the position to lookup from the db
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
    * Retrieves a GlobalMembership from the db (given a passed in connection) and stores it in the current GlobalMembership object
    *
    * @param id     The id of the position to lookup from the db
    * @param conn   A connection to the DB
    */
    public void lookup (DbConn myConn, long id) {
        positionID = id;

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

            sqlcode = "SELECT position, has, type FROM " + SCHEMAPATH + ".global_membership WHERE position=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            while (rs.next()) {
                long myID = rs.getLong(1);
                long has = rs.getLong(2);
                Long hasL = new Long(has);
                String type = rs.getString(3);
                membership.put(hasL, type);
            }
            rs.close();
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
    * Determin if the current position has membership in the given id (role, permission, group, etc)
    *
    * @param  id   The id to test for membership
    * @return      The result of the membership test
    */
    public boolean belongsTo(long id) {
        Long tmp = new Long(id);
        return(membership.containsKey(tmp));
    }



    /**
    * Refresh the global membership
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public static void refresh(DbConn myConn) {
        Statement stmt = null;
        Statement stmt2 = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtDel = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        int rows = 0;
        String sqlcode = "";
        try {
            stmt = myConn.conn.createStatement();
            stmt2 = myConn.conn.createStatement();
            Connection conn = myConn.conn;
            pstmtDel = conn.prepareStatement(
                "DELETE FROM " + myConn.getSchemaPath() + ".global_membership WHERE position = ?"
                );
            pstmt = conn.prepareStatement(
                "INSERT INTO " + myConn.getSchemaPath() + ".global_membership (position, has, type) values (?, ?, ?)"
                );
            sqlcode = "SELECT id FROM " + myConn.getSchemaPath() + ".position";
            rs = stmt.executeQuery(sqlcode);
            String myType = "";
            while (rs.next()) {
                long myID = rs.getLong(1);
                HashMap gm = new HashMap();
                HashSet neg = new HashSet();
                // process roles
                sqlcode = "SELECT position, role FROM " + myConn.getSchemaPath() + ".position_roles WHERE position=" + myID;
//System.out.println(sqlcode);
                rs2 =stmt2.executeQuery(sqlcode);
                myType = "Role";
                while (rs2.next()) {
                    Long tmp = new Long(rs2.getLong(2));
                    gm.put(tmp, myType);
                }
                rs2.close();
                //stmt2.close();
                // process permissions
                sqlcode = "SELECT pr.position,rp.permission,rp.negate " +
                    "FROM csi.position_roles pr, csi.role_permissions rp " +
                    "WHERE pr.role=rp.role AND pr.position=" + myID;
//System.out.println(sqlcode);
                rs2 =stmt2.executeQuery(sqlcode);
                myType = "Permission";
                while (rs2.next()) {
                    Long tmp = new Long(rs2.getLong(2));
                    boolean isNegate = ((rs2.getString(3).equals("T")) ? true : false);
                    if (!isNegate) {
                        gm.put(tmp, myType);
                    } else {
                        neg.add(tmp);
                    }
                }
                rs2.close();
                //stmt2.close();
// process groups, group roles, & group permissions

                // process negations
                Object myItems[] = neg.toArray();
                for (int i=0; i<myItems.length; i++) {
                    gm.remove(myItems[i]);
                }

                // store membership to db
                pstmtDel.setLong(1, myID);
                rows = pstmtDel.executeUpdate();
                for (Iterator i = gm.keySet().iterator(); i.hasNext();) {
                    Long tmp = (Long) i.next();
                    myType = (String) gm.get(tmp);
//System.out.println("Position: "+ myID + ", Has: " + tmp + ", Type: " + myType);
                    pstmt.setLong(1, myID);
                    pstmt.setLong(2, tmp.longValue());
                    pstmt.setString(3, myType);
                    rows = pstmt.executeUpdate();
                }
            }
        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (rs2 != null)
                try { rs2.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
            if (stmt2 != null)
                try { stmt2.close(); } catch (Exception i) {}
            if (pstmt != null)
                try { pstmt.close(); } catch (Exception i) {}
            if (pstmtDel != null)
                try { pstmtDel.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get a list of People who have the given role/priv from the DB
    *
    * @param myConn     Connection to the database
    * @param id         the id of the priv or role to look up
    */
    public static Person[] getPersonListWithMembership(DbConn myConn, long id) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlcode = "";

        Person [] per = null;

        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            sqlFrom = " FROM " + myConn.getSchemaPath() + ".global_membership gm, position p ";
            sqlWhere = " WHERE gm.position=p.id AND gm.has=" + id;

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            per = new Person[returnSize];

            sqlcode = "SELECT p.id, p.personid " + sqlFrom + sqlWhere + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                per[i] = new Person();
                per[i].lookup(myConn, rs.getLong(2));
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - getPersonListWithMembership lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - getPersonListWithMembership lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return per;
    }


    /**
    * Get a list of Positions that have the given role/priv from the DB
    *
    * @param myConn     Connection to the database
    * @param id         the id of the priv or role to look up
    */
    public static Position[] getPositionListWithMembership(DbConn myConn, long id) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlcode = "";

        Position [] pos = null;

        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            sqlFrom = " FROM " + myConn.getSchemaPath() + ".global_membership gm, position p ";
            sqlWhere = " WHERE gm.position=p.id AND gm.has=" + id;

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            pos = new Position[returnSize];

            sqlcode = "SELECT p.id, p.personid " + sqlFrom + sqlWhere + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                pos[i] = new Position(myConn, rs.getLong(1));
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - getPersonListWithMembership lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - getPersonListWithMembership lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return pos;
    }


}
