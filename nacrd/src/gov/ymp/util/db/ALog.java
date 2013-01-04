package gov.ymp.util.db;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.util.db.*;
//import oracle.jdbc.*;

/**
* ALog is the class for managing log information in the CSI POC
*
* @author   Bill Atchley
*/
public class ALog {
    public static String SCHEMA = "nacrd";

    /**
    * Private method to write an activity log entry
    *
    * @param userid     The userid (int)
    * @param description     The message description (String)
    * @param isError     A flag to signify error status (String)
    */
    private static void logAnyActivity(int userid, String description, String isError, int reference) {
        if (userid < 1) {
            userid = 0;
        }
        if (description == null || !(description.compareTo("  ") > 0)) {
            description = "N/A";
        }

        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            ResultSet rs;
            PreparedStatement pstmt = null;
            int rows;
            pstmt = myConn.conn.prepareStatement(
                "INSERT INTO " + SCHEMA + ".activity_log (user, description, iserror, datelogged, reference)" +
                " VALUES (?,?,?,NOW(), ?)"
                );
            pstmt.setInt(1,userid);
            pstmt.setString(2,description);
            pstmt.setString(3,isError);
            pstmt.setInt(4,reference);
            rows = pstmt.executeUpdate();
        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }

    }

    /**
    * Method to log activity
    *
    * @param description     The message description (String)
    */
    public static void logActivity(String description) {
        logAnyActivity(0, description, "F", 0);
    }

    /**
    * Method to log activity
    *
    * @param description     The message description (String)
    */
    public static void logActivity(String description,  int reference) {
        logAnyActivity(0, description, "F", reference);
    }

    /**
    * Method to log activity
    *
    * @param userid     The userid (int)
    * @param description     The message description (String)
    */
    public static void logActivity(int userid, String description) {
        logAnyActivity(userid, description, "F", 0);
    }

    /**
    * Method to log activity
    *
    * @param userid     The userid (int)
    * @param description     The message description (String)
    */
    public static void logActivity(int userid, String description, int reference) {
        logAnyActivity(userid, description, "F", reference);
    }

    /**
    * Method to log an error
    *
    * @param description     The message description (String)
    */
    public static void logError(String description) {
        logAnyActivity(0, description, "T", 0);
    }

    /**
    * Method to log an error
    *
    * @param userid     The userid (int)
    * @param description     The message description (String)
    */
    public static void logError(int userid, String description) {
        logAnyActivity(userid, description, "T", 0);
    }

}
