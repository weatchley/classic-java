package gov.ymp.slts.mssql.db;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.awt.*;
import java.lang.Class.*;
import gov.ymp.csi.db.*;
import gov.ymp.slts.mssql.db.*;
import javax.naming.*;
import net.sourceforge.jtds.jdbc.*;
//import oracle.jdbc.*;
//import gov.ymp.csi.misc.*;

/**
* DbConn is the class for managing MSSQL db connections in the slts
*
* @author   Bill Atchley
*/
public class DbConnM {
    public String schema = null;
    public String server = null;
    public Connection conn = null;
    private Properties props;
    private boolean propsLoaded = false;

    /**
    * Creates a new DbConnM object given a schema
    *
    * @param schemaIN     The Oracle schema to connect to (String)
    */
    public DbConnM (String schemaIN) {
        if (schemaIN.toLowerCase().equals("dummy")) {
            getProps();
        } else {
            schema = schemaIN;
            conn = getNew();
        }
    }

    /**
    * Creates a new DbConnM object
    *
    */
    public DbConnM () {
        getProps();
        //schema = (String) props.getProperty("ms.schema.user");
        schema = "servicealtirisdbreader";
        conn = getNew();
    }

    /**
    * Creates a new dummy DbConnM object
    *
    * @param temp     Temp flag
    */
    public DbConnM (boolean isTemp) {
        getProps();
    }

    /**
    * Creates a new DbConnM object given a connection
    *
    * @param connIN     The preestablished connection
    */
    public DbConnM (Connection connIN) {
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
//System.out.println("DbConnM:getNew1 Got Here 1");
            getProps();
            //String MainDBServer = (String) props.getProperty("MainDBServer");
            String MainDBServer = "MSsqlDBServer";
            if (MainDBServer.equals("OraDBServer")) {
                myconn = getNew("Oracle");
            } else if (MainDBServer.equals("MSsqlDBServer")) {
                myconn = getNew("MS-SQL");
            }
        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }
        return(myconn);
    }

    private void getProps() {
        InputStream in = null;
        try {
//System.out.println("DbConnM:getProps Got Here 1");
            if (!propsLoaded) {
                String ProductionStatus = "development";
                try {
                    Context initCtx = new InitialContext();
                    ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
                }
                catch (Exception e) {}

                String propertiesFile = null;
                if (ProductionStatus.toLowerCase().equals("prod") || ProductionStatus.toLowerCase().equals("production")) {
                    propertiesFile = "gov/ymp/slts/mssql/db/prod.properties";
                    //System.out.println("connecting to production database...");
                } else {
                    //try to see the Props param is defined in environment variables
                		try {
                        	//java.util.Properties p = OSEnvironment.get();
                        	//	//p.list(System.out);
                            //	//System.out.println("the current value of CSIPS is : " +
                            //	//p.getProperty("CSIPS"));
                            //ProductionStatus = p.getProperty("CSIPS");
                            ProductionStatus = "dev";
                            if (ProductionStatus != null && (ProductionStatus.toLowerCase().equals("prod") || ProductionStatus.toLowerCase().equals("production"))) {
                                propertiesFile = "gov/ymp/slts/mssql/db/prod.properties";
                            	//System.out.println("connecting to production database...");
                            }else{
                                propertiesFile = "gov/ymp/slts/mssql/db/dev.properties";
                            	//System.out.println("connecting to development database...");
                            }
                        }
                        catch (Throwable e) {
                            e.printStackTrace();
                            propertiesFile = "dev.properties";
                        }
                }
                	//System.out.println("connection using: " + propertiesFile);
                in = DbConnM.class.getResourceAsStream (propertiesFile);
                props = new Properties();
                //props.load(in);
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
//System.out.println("DbConnM:getNew Got Here 1");
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
                server = (String) props.getProperty("OraDBServer");
                String sid = (String) props.getProperty("OraDBSid");
                url = "jdbc:oracle:thin:@" + server + ":1521:"+ sid;
            } else if (dbType.equals("MS-SQL")) {
                // Load the  JDBC driver
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                //server = (String) props.getProperty("MSsqlDBServer");
                server = (String) "ydaltirisds";
                //url = "jdbc:microsoft:sqlserver://" + server + ":1433;databaseName=" + schema.toLowerCase() + ";selectMethod=";
                url = "jdbc:jtds:sqlserver://" + server + ":1433";
            }

            //schemapass = (String) props.getProperty("ms.schema");
            schemapass = "125014070031006071053018015092018092";
            //key = (String) props.getProperty("ms.k");
            key = "2zmjflyrceqh1bd4jbic3s6fvcmchutk";
            String pass = AbcCrypt.doDecrypt(schemapass, key, 5);
            myconn = DriverManager.getConnection (url, schema.toLowerCase(), pass);
        }
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
            getProps();
            name = (String) props.getProperty("ms.schema.user");
        }
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
    public String getSchemaPath() {
        String path = null;
        try {
            getProps();
            path = (String) props.getProperty("ms.schema.path");
        }
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
    public String getDBType() {
        String dbType = null;
        try {
            //getProps();
            //dbType = (String) props.getProperty("MainDBServer");
            dbType = "MSsqlDBServer";
        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }
        return(dbType);
    }

}
