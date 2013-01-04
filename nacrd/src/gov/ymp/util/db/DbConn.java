package gov.ymp.util.db;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.awt.*;
import gov.ymp.util.db.*;
import javax.naming.*;
//import com.mysql.jdbc.Driver;

/**
* DbConn is the class for managing MySQL db connections in the CSI POC
*
* @author   Bill Atchley
*/
public class DbConn {
    public String schema = null;
    public String server = null;
    public Connection conn = null;

    /**
    * Creates a new DbConn object
    *
    */
    public DbConn () {
        try {
            Context initCtx = new InitialContext();
            schema = (String) initCtx.lookup("java:comp/env/csi-db");
            conn = getNew();
        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }
    }

    /**
    * Creates a new DbConn object given a schema
    *
    * @param schemaIN     The MySQL schema to connect to (String)
    */
    public DbConn (String schemaIN) {
        schema = schemaIN;
        conn = getNew();
    }

    /**
    * Make a coonnection to the db
    * @return myconn     The db connection handle (Connection)
    */
    public Connection getNew() {
        String schemapass = null;
        String key = null;
        Connection myconn = null;
        try {
            // Load the MySQL JDBC driver
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            Context initCtx = new InitialContext();
            schemapass = (String) initCtx.lookup("java:comp/env/csi-schema");
            key = (String) initCtx.lookup("java:comp/env/csi-k");
            server = (String) initCtx.lookup("java:comp/env/DBServer");
            String pass = AbcCrypt.doDecrypt(schemapass, key, 5);
            String url = "jdbc:mysql://" + server + "/" + schema + "?user=" + schema + "&password=" + pass + "&zeroDateTimeBehavior=convertToNull";
            myconn = DriverManager.getConnection (url);
        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }
        catch (NamingException e) {
            System.out.println(e + e.getMessage());
        }
        return(myconn);
    }

    /**
    * Release a coonnection to the db
    */
    public void release() {
        try {
            conn.close();
        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }
    }

}
