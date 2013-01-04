//package gov.ymp.csi.db;
package gov.ymp.csi.shuzi;

// Support classes

//import gov.ymp.csi.db.*;
//import java.io.*;
//import java.util.*;
//import java.math.*;
//import java.awt.*;
//import javax.naming.*;
//import oracle.jdbc.*;
import java.sql.*;

/**
* DbConn is the class for managing Oracle db connections in the CSI POC
*
* @author   Bill Atchley / mod'ed by shuzi
*/
public class DbConn {
    public String schema = null;
    public String server = null;
    public Connection conn = null;

    /**
    * Creates a new DbConn object given a schema
    *
    * @param schemaIN     The Oracle schema to connect to (String)
    */
    public DbConn (String schemaIN) {
        this.schema = schemaIN;
        this.conn = getNew();
    }

    /**
    * Make a coonnection to the db
    * @return myconn     The db connection handle (Connection)
    */
    public Connection getNew() {
        //String schemapass = null;
        //String key = null;
        Connection myconn = null;
        try {
            // Load the Oracle JDBC driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //Context initCtx = new InitialContext();
            //schemapass = (String) initCtx.lookup("java:comp/env/csi-schema");
            //key = (String) initCtx.lookup("java:comp/env/csi-k");
            //this.server = (String) initCtx.lookup("java:comp/env/OraDBServer");
            //server = "ydoradev";
            //String pass = gov.ymp.csi.db.abcCrypt.doDecrypt(schemapass, key, 5);
            //String pass = AbcCrypt.doDecrypt(schemapass, key, 5);
            //String url = "jdbc:oracle:thin:@" + this.server + ":1521:ydor";
            //myconn = DriverManager.getConnection (url, this.schema, pass);
	    //String url = "jdbc:oracle:thin:@" + this.server + ":1521:ydor";
            //myconn = DriverManager.getConnection (url, this.schema, pass);
	    myconn = DriverManager.getConnection ("jdbc:oracle:thin:@ydoradev:1521:ydor", this.schema, "Autonomy)");
        }
        /*
	catch (NamingException e) {
            System.out.println(e + e.getMessage());
        }
	*/
        catch (java.sql.SQLException e) {
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
