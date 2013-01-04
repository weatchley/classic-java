package gov.ymp.csi.db;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.awt.*;
import java.lang.Class.*;
import gov.ymp.csi.db.*;
import javax.naming.*;
//import oracle.jdbc.*;
import gov.ymp.csi.misc.*;

/**
* DbConn is the class for managing Oracle db connections in the CSI POC
*
* @author   Bill Atchley
*/
public class DbConn {
    public String schema = null;
    public String server = null;
    public Connection conn = null;
    private Properties props;
    private boolean propsLoaded = false;

    /**
    * Creates a new DbConn object given a schema
    *
    * @param schemaIN     The Oracle schema to connect to (String)
    */
    public DbConn (String schemaIN) {
        if (schemaIN.toLowerCase().equals("dummy")) {
            getProps();
        } else {
            schema = schemaIN;
            conn = getNew();
        }
    }

    /**
    * Creates a new DbConn object
    *
    */
    public DbConn () {
        getProps();
        schema = (String) props.getProperty("csi.schema.user");
        conn = getNew();
    }

    /**
    * Creates a new dummy DbConn object
    *
    * @param temp     Temp flag
    */
    public DbConn (boolean isTemp) {
        getProps();
    }

    /**
    * Creates a new DbConn object given a connection
    *
    * @param connIN     The preestablished connection
    */
    public DbConn (Connection connIN) {
        conn = connIN;
        getProps();
    }


    /**
    * Make a coonnection to the db
    * @return myconn     The db connection handle (Connection)
    */
    public Connection getNew() {
        Connection myconn = null;
        try {
            //Context initCtx = new InitialContext();
            //String MainDBServer = (String) initCtx.lookup("java:comp/env/MainDBServer");
            getProps();
            String MainDBServer = (String) props.getProperty("MainDBServer");
            if (MainDBServer.equals("OraDBServer")) {
                myconn = getNew("Oracle");
            } else if (MainDBServer.equals("MSsqlDBServer")) {
                myconn = getNew("MS-SQL");
            }
        }
//        catch (NamingException e) {
//            System.out.println(e + e.getMessage());
//        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }
        return(myconn);
    }

    private void getProps() {
        InputStream in = null;
        try {
            if (!propsLoaded) {
                String ProductionStatus = "development";
                try {
                    Context initCtx = new InitialContext();
                    ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
                    //InputStream in = new Class.getResourceAsStream ("csi.properties");
                }
                catch (Exception e) {}

                String propertiesFile = null;
                if (ProductionStatus.toLowerCase().equals("prod") || ProductionStatus.toLowerCase().equals("production")) {
                    propertiesFile = "csiprod.properties";
                    //System.out.println("connecting to production database...");
                } else {
                    //try to see the Props param is defined in environment variables
                		try {
                        	java.util.Properties p = OSEnvironment.get();
                        		//p.list(System.out);
                            	//System.out.println("the current value of CSIPS is : " +
                            	//p.getProperty("CSIPS"));
                            ProductionStatus = p.getProperty("CSIPS");
                            if (ProductionStatus != null && (ProductionStatus.toLowerCase().equals("prod") || ProductionStatus.toLowerCase().equals("production"))) {
                            	propertiesFile = "csiprod.properties";
                            	//System.out.println("connecting to production database...");
                            }else{
                            	propertiesFile = "csidev.properties";
                            	//System.out.println("connecting to development database...");
                            }
                        }
                        catch (Throwable e) {
                            e.printStackTrace();
                            propertiesFile = "csidev.properties";
                        }
                	//propertiesFile = "csidev.properties";
                }
                	//System.out.println("connection using: " + propertiesFile);
                in = DbConn.class.getResourceAsStream (propertiesFile);
                //in = Class.getResourceAsStream (propertiesFile);
                props = new Properties();
                props.load(in);
                propsLoaded = true;
            }
        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }
        finally {
            try { in.close(); } catch (Exception i) {}
        }

    }

    /**
    * Make a coonnection to the db
    * @return myconn     The db connection handle (Connection)
    */
    public Connection getNewOracle() {
        Connection myconn = null;
        myconn = getNew("Oracle");
        return(myconn);
    }

    /**
    * Make a coonnection to the db
    * @return myconn     The db connection handle (Connection)
    */
    public Connection getNewMSSQL() {
        Connection myconn = null;
        myconn = getNew("MS-SQL");
        return(myconn);
    }

    /**
    * Make a coonnection to the db
    * @return myconn     The db connection handle (Connection)
    */
    public Connection getNew(String dbType) {
        String schemapass = null;
        String key = null;
        Connection myconn = null;
        String url = null;
        try {
            //Context initCtx = new InitialContext();
            getProps();
            if (dbType.equals("Oracle")) {
                // Load the Oracle JDBC driver
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                ///server = (String) initCtx.lookup("java:comp/env/OraDBServer");
                server = (String) props.getProperty("OraDBServer");
                String sid = (String) props.getProperty("OraDBSid");
                url = "jdbc:oracle:thin:@" + server + ":1521:"+ sid;
            } else if (dbType.equals("MS-SQL")) {
                // Load the Oracle JDBC driver
                //DriverManager.registerDriver(new com.microsoft.jdbc.sqlserver.SQLServerDriver());
                Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
                //server = (String) initCtx.lookup("java:comp/env/MSsqlDBServer");
                server = (String) props.getProperty("MSsqlDBServer");
                //url = "jdbc:microsoft:sqlserver://" + server + ":1433";
                url = "jdbc:microsoft:sqlserver://" + server + ":1433;databaseName=" + schema.toLowerCase() + ";selectMethod=";
            }

            //schemapass = (String) initCtx.lookup("java:comp/env/csi-schema");
            schemapass = (String) props.getProperty("csi.schema");
            //key = (String) initCtx.lookup("java:comp/env/csi-k");
            key = (String) props.getProperty("csi.k");
            //server = "ydoradev";
            //String pass = gov.ymp.csi.db.abcCrypt.doDecrypt(schemapass, key, 5);
            String pass = AbcCrypt.doDecrypt(schemapass, key, 5);
            myconn = DriverManager.getConnection (url, schema.toLowerCase(), pass);
        }
//        catch (NamingException e) {
//            System.out.println(e + e.getMessage());
//        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }
        catch (java.lang.ClassNotFoundException e) {
            System.out.println(e + e.getMessage());
        }
        catch (Exception e) {
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
            //System.out.println("Connection Closed");
        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }
    }

    /**
    * Get connection
    *
    * @return connection
    */
    public Connection getConn() {
        return(conn);
    }

    /**
    * Get schema name
    *
    * @return Schema name
    */
    //public static String getSchemaName() {
    public String getSchemaName() {
        String name = null;
        try {
            //Context initCtx = new InitialContext();
            //name = (String) initCtx.lookup("java:comp/env/csi-schema-user");
            getProps();
            name = (String) props.getProperty("csi.schema.user");
        }
        //catch (NamingException e) {
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }
        return(name);
    }

    /**
    * Get schema path
    *
    * @return Schema path
    */
    //public static String getSchemaPath() {
    public String getSchemaPath() {
        String path = null;
        try {
            //Context initCtx = new InitialContext();
            //path = (String) initCtx.lookup("java:comp/env/csi-schema-path");
            getProps();
            path = (String) props.getProperty("csi.schema.path");
        }
        //catch (NamingException e) {
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }
        return(path);
    }

    /**
    * Get db type
    *
    * @return db type
    */
    //public static String getDBType() {
    public String getDBType() {
        String dbType = null;
        try {
            //Context initCtx = new InitialContext();
            //dbType = (String) initCtx.lookup("java:comp/env/MainDBServer");
            getProps();
            dbType = (String) props.getProperty("MainDBServer");
        }
        //catch (NamingException e) {
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }
        return(dbType);
    }

}
