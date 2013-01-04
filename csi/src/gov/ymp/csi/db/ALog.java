package gov.ymp.csi.db;

// Support classes
import java.io.*;
import java.util.*;
//import java.util.Date.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.people.*;
//import gov.ymp.csi.misc.*;
//import oracle.jdbc.*;

/**
* ALog is the class for managing log information in the CSI POC
*
* @author   Bill Atchley
*/
public class ALog {
    //public static String SCHEMA = "csi";
    //private static String SCHEMA = DbConn.getSchemaName();
    //private static String SCHEMAPATH = DbConn.getSchemaPath();
    public long userid = 0;
    public String user = null;
    public java.util.Date timeStamp = null;
    public long systemid = 0;
    public String system = null;
    public long typeid = 0;
    public String type = null;
    public String description = null;
    public boolean isError = false;

    /**
    * Creates a new ALog object
    *
    */
    public ALog () {
        isError = false;
    }

    /**
    * Private method to write an activity log entry
    *
    * @param userid     The CSI userid (long)
    * @param sytem     The CSI system (String)
    * @param type     The CSI message type (int)
    * @param description     The message description (String)
    * @param isError     A flag to signify error status (String)
    */
    private static void logAnyActivity(DbConn myConn, long userid, String system, int type, String description, String isError) {
        if (userid < 1) {
            userid = 0;
        }
        if (system == null || !(system.compareTo("  ") > 0)) {
            system = "N/A";
        }
        if (type < 1) {
            type = 0;
        }
        if (description == null || !(description.compareTo("  ") > 0)) {
            description = "N/A";
        }

        ResultSet rs = null;
        Statement stmt = null;
        try {
            //DbConn myConn = new DbConn();
            stmt = myConn.conn.createStatement();
            PreparedStatement pstmt = null;
            int rows;
            rs = stmt.executeQuery("SELECT NVL(id, 0) FROM " + myConn.getSchemaPath() + ".systems WHERE UPPER(acronym)=UPPER('" + system + "')");
            //rs = stmt.executeQuery("SELECT ISNULL(id, 0) FROM " + myConn.getSchemaPath() + ".systems WHERE UPPER(acronym)=UPPER('" + system + "')");
            rs.next();
            int systemid = rs.getInt(1);
            pstmt = myConn.conn.prepareStatement(
                "INSERT INTO " + myConn.getSchemaPath() + ".activity_log (userid, systemid, typeid, description, iserror, timestamp)" +
                " VALUES (?,?,?,?,?,SYSDATE)"
//                " VALUES (?,?,?,?,?,getdate())"
                );
            //pstmt.setInt(1,userid);
            pstmt.setLong(1,userid);
            pstmt.setInt(2,systemid);
            pstmt.setInt(3,type);
            pstmt.setString(4,description);
            pstmt.setString(5,isError);
            rows = pstmt.executeUpdate();
            rs.close();
        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

    }

    /**
    * Method to log activity
    *
    * @param userid     The CSI userid (long)
    * @param system     The CSI system (String)
    * @param type     The CSI message type (int)
    * @param description     The message description (String)
    */
    public static void logActivity(long userid, String system, int type, String description) {
        DbConn myConn = new DbConn();
        logAnyActivity(myConn, userid, system, type, description, "F");
        myConn.release();
    }

    /**
    * Method to log activity
    *
    * @param myConn     The CSI db connection (DbConn)
    * @param userid     The CSI userid (long)
    * @param system     The CSI system (String)
    * @param type     The CSI message type (int)
    * @param description     The message description (String)
    */
    public static void logActivity(DbConn myConn, long userid, String system, int type, String description) {
        logAnyActivity(myConn, userid, system, type, description, "F");
    }

    /**
    * Method to log an error
    *
    * @param userid     The CSI userid (long)
    * @param system     The CSI system (String)
    * @param type     The CSI message type (int)
    * @param description     The message description (String)
    */
    public static void logError(long userid, String system, int type, String description) {
        DbConn myConn = new DbConn();
        logAnyActivity(myConn, userid, system, type, description, "T");
        myConn.release();
    }

    /**
    * Method to log an error
    *
    * @param myConn     The CSI db connection (DbConn)
    * @param userid     The CSI userid (long)
    * @param system     The CSI system (String)
    * @param type     The CSI message type (int)
    * @param description     The message description (String)
    */
    public static void logError(DbConn myConn, long userid, String system, int type, String description) {
        logAnyActivity(myConn, userid, system, type, description, "T");
    }


    /**
    * Method to retrieve a set of log entries
    *
    * @param userid     The CSI userid (long)
    * @param type       The CSI message type (int)
    * @param viewCount  The count of log entries to return (int)
    */
    public static ALog[] getALog(long userid, int type, int viewCount, boolean err) {
        String sqlcode = "";
        String sqlWhere = "";
        String sqlFrom = "";
        int returnSize = 0;
        ResultSet rset = null;
        ALog[] log = null;
        try {
            String system = null;
            java.util.Date startDate = null;
            java.util.Date endDate = null;

            log = getALog(userid, system, type, startDate, endDate, viewCount, err);

        }
//        catch (java.sql.SQLException e) {
//            System.out.println(e + e.getMessage());
//        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }

        return log;
    }


    /**
    * Method to retrieve a set of log entries
    *
    * @param myConn     The CSI db connection (DbConn)
    * @param userid     The CSI userid (long)
    * @param type       The CSI message type (int)
    * @param viewCount  The count of log entries to return (int)
    */
    public static ALog[] getALog(DbConn myConn, long userid, int type, int viewCount, boolean err) {
        String sqlcode = "";
        String sqlWhere = "";
        String sqlFrom = "";
        int returnSize = 0;
        ResultSet rset = null;
        ALog[] log = null;
        try {
            String system = null;
            java.util.Date startDate = null;
            java.util.Date endDate = null;

            log = getALog(myConn, userid, system, type, startDate, endDate, viewCount, err);

        }
//        catch (java.sql.SQLException e) {
//            System.out.println(e + e.getMessage());
//        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }

        return log;
    }


    /**
    * Method to retrieve a set of log entries
    *
    * @param userid     The CSI userid (long)
    * @param system     The CSI system (String)
    * @param type       The CSI message type (int)
    * @param viewCount  The count of log entries to return (int)
    */
    public static ALog[] getALog(long userid, String system, int type, int viewCount, boolean err) {
        String sqlcode = "";
        String sqlWhere = "";
        String sqlFrom = "";
        int returnSize = 0;
        ResultSet rset = null;
        ALog[] log = null;
        try {
            java.util.Date startDate = null;
            java.util.Date endDate = null;

            log = getALog(userid, system, type, startDate, endDate, viewCount, err);

        }
//        catch (java.sql.SQLException e) {
//            System.out.println(e + e.getMessage());
//        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }

        return log;
    }


    /**
    * Method to retrieve a set of log entries
    *
    * @param myConn     The CSI db connection (DbConn)
    * @param userid     The CSI userid (long)
    * @param system     The CSI system (String)
    * @param type       The CSI message type (int)
    * @param viewCount  The count of log entries to return (int)
    */
    public static ALog[] getALog(DbConn myConn, long userid, String system, int type, int viewCount, boolean err) {
        String sqlcode = "";
        String sqlWhere = "";
        String sqlFrom = "";
        int returnSize = 0;
        ResultSet rset = null;
        ALog[] log = null;
        try {
            java.util.Date startDate = null;
            java.util.Date endDate = null;

            log = getALog(myConn, userid, system, type, startDate, endDate, viewCount, err);

        }
//        catch (java.sql.SQLException e) {
//            System.out.println(e + e.getMessage());
//        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }

        return log;
    }


    /**
    * Method to retrieve a set of log entries
    *
    * @param userid     The CSI userid (long)
    * @param system     The CSI system (String)
    * @param type       The CSI message type (int)
    * @param startDate  The date to start at (Date)
    * @param endDate    The date to end at (Date)
    * @param viewCount  The count of log entries to return (int)
    */
    public static ALog[] getALog(long userid, String system, int type, java.util.Date startDate, java.util.Date endDate, int viewCount, boolean err) {
        String sqlcode = "";
        String sqlWhere = "";
        String sqlFrom = "";
        int returnSize = 0;
        ResultSet rset = null;
        ALog[] log = null;
        try {
            DbConn myConn = new DbConn();

            log = getALog(myConn, userid, system, type, startDate, endDate, viewCount, err);

        }
//        catch (java.sql.SQLException e) {
//            System.out.println(e + e.getMessage());
//        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }

        return log;
    }


    /**
    * Method to retrieve a set of log entries
    *
    * @param myConn     The CSI db connection (DbConn)
    * @param userid     The CSI userid (long)
    * @param system     The CSI system (String)
    * @param type       The CSI message type (int)
    * @param startDate  The date to start at (Date)
    * @param endDate    The date to end at (Date)
    * @param viewCount  The count of log entries to return (int)
    */
    public static ALog[] getALog(DbConn myConn, long userid, String system, int type, java.util.Date startDate, java.util.Date endDate, int viewCount, boolean err) {
        String sqlcode = "";
        String sqlWhere = "";
        String sqlFrom = "";
        int returnSize = 0;
        ResultSet rset = null;
        ALog[] log = null;
        Statement stmt = null;
        try {
            stmt = myConn.conn.createStatement();

            sqlWhere += (userid != 0) ? "a.userid=" + userid + " AND " : "";
            sqlWhere += (system != null && !system.equals("0")) ? "s.acronym=UPPER('" + system + "') AND " : "";
            sqlWhere += (type != 0) ? "a.typeid=" + type + " AND " : "";
            sqlWhere += (err) ? "a.iserror='T' AND " : "";

            sqlWhere += "a.userid=u.id AND a.systemid=s.id AND at.id=a.typeid";
            sqlFrom = myConn.getSchemaPath() + ".activity_log a, " + myConn.getSchemaPath() + ".person u, " + myConn.getSchemaPath() + ".systems s, " + myConn.getSchemaPath() + ".activity_type at ";

            sqlcode = "SELECT count(*) FROM " + sqlFrom + " WHERE " + sqlWhere;
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);
            rset.next();
            returnSize = rset.getInt(1);
            rset.close();

            sqlcode = "SELECT a.userid,u.firstname || ' ' || u.lastname,a.timestamp, a.systemid, s.acronym," +
                "typeid,at.description,a.iserror,a.description " +
                "FROM " + sqlFrom + "WHERE " + sqlWhere + " ORDER BY a.timestamp DESC";
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);

            int myViewCount = (viewCount > returnSize || viewCount <=0) ? returnSize : viewCount;
            log = new ALog[myViewCount];
            int i = 0;
            while (rset.next() && i < myViewCount) {
                log[i] = new ALog();
                log[i].userid = rset.getLong(1);
                log[i].user = rset.getString(2);
                log[i].timeStamp = (java.util.Date) rset.getTimestamp(3);
                log[i].systemid = rset.getInt(4);
                log[i].system = rset.getString(5);
                log[i].typeid = rset.getLong(6);
                log[i].type = rset.getString(7);
                String temp = rset.getString(8);
                log[i].isError = (temp.equals("T")) ? true : false;
                log[i].description = rset.getString(9);
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

        return log;
    }


    /**
    * Method to retrieve a set of users from the log
    *
    */
    public static Person[] getUsersInLog(DbConn myConn) {
        String sqlcode = "";
        String sqlWhere = "";
        String sqlFrom = "";
        int returnSize = 0;
        ResultSet rset = null;
        Person[] obj = null;
        Statement stmt = null;
        try {
            stmt = myConn.conn.createStatement();

            sqlWhere = "id > 0 AND id IN (SELECT userid FROM " + myConn.getSchemaPath() + ".activity_log)";

            sqlFrom = myConn.getSchemaPath() + ".person ";

            sqlcode = "SELECT count(*) FROM " + sqlFrom + " WHERE " + sqlWhere;
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);
            rset.next();
            returnSize = rset.getInt(1);
            rset.close();

            sqlcode = "SELECT id " +
                "FROM " + sqlFrom + "WHERE " + sqlWhere + " ORDER BY LOWER(lastname),LOWER(firstname)";
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);

            Person [] people = Person.getPersonSet(myConn);
            HashMap peopleH = new HashMap();
            for (int i=0; i<people.length; i++) {
                peopleH.put(new Long(people[i].getID()), people[i]);
            }
            obj = new Person[returnSize];
            int i = 0;
            while (rset.next()) {
                //obj[i] = new Person();
                //obj[i].getInfo(myConn, rset.getLong(1));
                obj[i] = (Person) peopleH.get(new Long(rset.getLong(1)));
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

        return obj;
    }


}
