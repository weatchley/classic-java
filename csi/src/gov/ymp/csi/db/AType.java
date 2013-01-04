package gov.ymp.csi.db;

// Support classes
import java.io.*;
import java.util.*;
//import java.util.Date.*;
import java.sql.*;
import gov.ymp.csi.db.*;
//import gov.ymp.csi.misc.*;
//import oracle.jdbc.*;

/**
* AType is the class for managing log type information in the CSI
*
* @author   Bill Atchley
*/
public class AType {
    //public static String SCHEMA = "csi";
    //private static String SCHEMA = DbConn.getSchemaName();
    //private static String SCHEMAPATH = DbConn.getSchemaPath();
    public int id = 0;
    public String description = null;

    /**
    * Creates a new AType object
    *
    */
    public AType () {

    }



    /**
    * Method to retrieve a set of log types
    *
    */
    public static AType[] getATypes(DbConn myConn) {
        String sqlcode = "";
        String sqlWhere = "";
        String sqlFrom = "";
        int returnSize = 0;
        ResultSet rset = null;
        AType[] types = null;
        Statement stmt = null;
        try {
            stmt = myConn.conn.createStatement();

            sqlWhere = "1=1";

            sqlFrom = myConn.getSchemaPath() + ".activity_type ";

            sqlcode = "SELECT count(*) FROM " + sqlFrom + " WHERE " + sqlWhere;
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);
            rset.next();
            returnSize = rset.getInt(1);
            rset.close();

            sqlcode = "SELECT id, description " +
                "FROM " + sqlFrom + "WHERE " + sqlWhere + " ORDER BY description";
//System.out.println(sqlcode);
            rset = stmt.executeQuery (sqlcode);

            types = new AType[returnSize];
            int i = 0;
            while (rset.next()) {
                types[i] = new AType();
                types[i].id = rset.getInt(1);
                types[i].description = rset.getString(2);
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

        return types;
    }

}
