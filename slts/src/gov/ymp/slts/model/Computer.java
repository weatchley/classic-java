package gov.ymp.slts.model;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.slts.mssql.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.items.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
* Computer is the class for inventoried computers in the db
*
* @author   Bill Atchley
*/
public class Computer {
    private int computerID = 0;
    private String name = null;
    private String licOsKey = null;
    private String domainName = null;
    private String serialNum = null;
    private String os = null;
    private String manuf = null;
    private java.util.Date lastInventory = null;
    private String modelNum = null;
    private String prodName = null;
    private String ipAddress = null;
    public boolean isNew = true;

    //public static String SCHEMA = "csi";
    public static String SCHEMA = null;
    public static String SCHEMAPATH = null;
    public static String DBTYPE = null;

    /**
    * init new object
    */
    private void init () {
        DbConn tempDB = new DbConn("dummy");
        //SCHEMA = tempDB.getSchemaName();
        //SCHEMAPATH = tempDB.getSchemaPath();
        SCHEMA = "slts";
        SCHEMAPATH = "slts";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty Computer object
    */
    public Computer () {
        //
        init();
    }


    /**
    * Creates a Computer object and uses the given id to populate it from the db
    *
    * @param id     The id of the Computer to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public Computer (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a Computer from the db and stores it in the current Computer object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a Computer from the db and stores it in the current Computer object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, DbConn myConn) {
        //
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT computer_id, name, lic_os_key, domain_name, serial_num, os, manuf, last_inventory, model_num, prod_name, ip_address FROM " + SCHEMAPATH + ".computer WHERE computer_id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);

            if (myID == id) {
                computerID = myID;
                name = rs.getString(2);
                licOsKey = rs.getString(3);
                domainName = rs.getString(4);
                serialNum = rs.getString(5);
                os = rs.getString(6);
                manuf = rs.getString(7);
                lastInventory = rs.getTimestamp(8);
                modelNum = rs.getString(9);
                prodName = rs.getString(10);
                ipAddress = rs.getString(11);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Computer lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }

    public int getComputerID() {
        return computerID;
    }

    public void setComputerID(int computerID) {
        this.computerID = computerID;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public java.util.Date getLastInventory() {
        return lastInventory;
    }

    public void setLastInventory(java.util.Date lastInventory) {
        this.lastInventory = lastInventory;
    }

    public String getLicOsKey() {
        return licOsKey;
    }

    public void setLicOsKey(String licOsKey) {
        this.licOsKey = licOsKey;
    }

    public String getManuf() {
        return manuf;
    }

    public void setManuf(String manuf) {
        this.manuf = manuf;
    }

    public String getModelNum() {
        return modelNum;
    }

    public void setModelNum(String modelNum) {
        this.modelNum = modelNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }



    /**
    * Save the current Computer to the DB
    *
    * @param myConn     Connection to the database
    */
    public void save(DbConn myConn) {
        //save funciton
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sqlcode = "";
        try {

            Connection conn = myConn.conn;


            // Create a Statement
            stmt = conn.createStatement ();
            conn.setAutoCommit(false);
            if (isNew) {

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".computer (computer_id, name, lic_os_key, domain_name, serial_num, os, manuf, last_inventory, model_num, prod_name, ip_address) " +
                    "VALUES (" + computerID + ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, name);
                pstmt.setString(++sqlID, licOsKey);
                pstmt.setString(++sqlID, domainName);
                pstmt.setString(++sqlID, serialNum);
                pstmt.setString(++sqlID, os);
                pstmt.setString(++sqlID, manuf);
                //pstmt.setDate(++sqlID, Utils.castDate(lastInventory));
                pstmt.setTimestamp(++sqlID, (java.sql.Timestamp) lastInventory);
                pstmt.setString(++sqlID, modelNum);
                pstmt.setString(++sqlID, prodName);
                pstmt.setString(++sqlID, ipAddress);
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
            //    sqlcode = "begin UPDATE " + SCHEMAPATH + ".computer " +
            //          "SET domain_name = ?, include = '" + ((include) ? 'T' : 'F') + "', datediscovered = ? WHERE computer_id= ?" +
            //          "; end;";
//System.out.println(sqlcode);
            //    pstmt = conn.prepareStatement(sqlcode);
            //    int sqlID = 0;
            //    pstmt.setString(++sqlID, domainName);
            //    pstmt.setDate(++sqlID, (java.sql.Date) dateDiscovered);
            //    pstmt.setString(++sqlID, domainName);
            //    rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Computer save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Computer save");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
            if (pstmt != null)
                try { pstmt.close(); } catch (Exception i) {}
        }
// do auth stuff
    }


    /**
    * Empty Computer to the DB
    *
    * @param myConn     Connection to the database
    */
    public static void empty(DbConn myConn) {
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            String mySchemaPath = "slts";
            pstmt = conn.prepareStatement(
                "DELETE FROM " + mySchemaPath + ".computer"
                );
            rows = pstmt.executeUpdate();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Computer empty");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Computer empty");
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
    * Get a list of Computer from the DB
    *
    * @param myConn     Connection to the database
    */
    public static Computer[] getItemList(DbConn myConn) {
        return getItemList(myConn, true);
    }


    /**
    * Get a list of Computer from the DB
    *
    * @param myConn     Connection to the database
    * @param doIgnore   flag to skip computers on the ignore list
    */
    public static Computer[] getItemList(DbConn myConn, boolean doIgnore) {
        return getItemList(myConn, doIgnore, null);
    }


    /**
    * Get a list of Computer from the DB
    *
    * @param myConn     Connection to the database
    * @param doIgnore   flag to skip computers on the ignore list
    * @param compList   Array of computers to retrieve
    */
    public static Computer[] getItemList(DbConn myConn, boolean doIgnore, int [] compList) {
        return getItemList(myConn, doIgnore, compList, null);
    }


    /**
    * Get a list of Computer from the DB
    *
    * @param myConn     Connection to the database
    * @param doIgnore   flag to skip computers on the ignore list
    * @param compList   Array of computers to retrieve
    * @param appList    Array of applications installed to test for
    */
    public static Computer[] getItemList(DbConn myConn, boolean doIgnore, int [] compList, String [] appList) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        Computer[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "slts";

            sqlFrom = " FROM " + mySchemaPath + ".computer ";
            sqlWhere = " WHERE 1=1  ";
            if (compList != null && compList.length >0) {
                sqlWhere += " AND computer_id IN (";
                for (int i=0; i<compList.length; i++) {
                    sqlWhere += ((i > 0) ? ", " : "") + compList[i];
                }
                sqlWhere += ") ";
            }
            if (appList != null && appList.length >0) {
                sqlWhere += " AND computer_id IN (SELECT computer_id FROM " + mySchemaPath + ".app_inventory WHERE listname IN (";
                for (int i=0; i<appList.length; i++) {
                    sqlWhere += ((i > 0) ? ", " : "") + "'" + appList[i] + "'";
                }
                sqlWhere += ")) ";
            }
            sqlWhere += (doIgnore) ? " AND computer_id NOT IN (SELECT computer_id FROM " + mySchemaPath + ".computer_ignore) " : "";
            sqlOrderBy = " ORDER BY name,domain_name ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Computer[returnSize];

            sqlcode = "SELECT computer_id, name, lic_os_key, domain_name, serial_num, os, manuf, last_inventory, model_num, prod_name, ip_address " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new Computer();
                item[i].computerID = rs.getInt(1);
                item[i].name = rs.getString(2);
                item[i].licOsKey = rs.getString(3);
                item[i].domainName = rs.getString(4);
                item[i].serialNum = rs.getString(5);
                item[i].os = rs.getString(6);
                item[i].manuf = rs.getString(7);
                item[i].lastInventory = rs.getTimestamp(8);
                item[i].modelNum = rs.getString(9);
                item[i].prodName = rs.getString(10);
                item[i].ipAddress = rs.getString(11);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Computer lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }


    /**
    * Load computers from source DB
    *
    * @param myConn     Connection to the database
    */
    public static void load(DbConn myConn, DbConnM msConn) {
        //init();
        String outLine = "";
        ComputerIn [] items = null;
        try {
//System.out.println("Computer:load-Got Here 1");
            items = ComputerIn.getItemList(msConn, myConn);
//System.out.println("Computer:load-Got Here 2");

            for (int i=0; i<items.length; i++) {
                Computer in = new Computer();
                in.setComputerID(items[i].getComputerID());
                in.setName(((items[i].getName() != null) ? items[i].getName() : null));
                in.setLicOsKey(((items[i].getLicOsKey() != null) ? items[i].getLicOsKey() : null));
                in.setDomainName(((items[i].getDomainName() != null) ? items[i].getDomainName() : null));
                in.setSerialNum(((items[i].getSerialNum() != null) ? items[i].getSerialNum() : null));
                in.setOs(((items[i].getOs() != null) ? items[i].getOs() : null));
                in.setManuf(((items[i].getManuf() != null) ? items[i].getManuf() : null));
                in.setLastInventory(((items[i].getLastInventory() != null) ? items[i].getLastInventory() : null));
                in.setModelNum(((items[i].getModelNum() != null) ? items[i].getModelNum() : null));
                in.setProdName(((items[i].getProdName() != null) ? items[i].getProdName() : null));
                in.setIpAddress(((items[i].getIpAddress() != null) ? items[i].getIpAddress() : null));

                in.save(myConn);
            }
//System.out.println("Computer:load-Got Here 3");


        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine);
        }
        finally {

        }

    }


    /**
    * Get a hash of computers from the DB
    *
    * @param myConn     Connection to the database
    */
    public static HashMap getItemHash(DbConn myConn) {
        Computer [] items = getItemList(myConn);
        HashMap hm = new HashMap((items.length + 1));
        for (int i=0; i<items.length; i++) {
            hm.put(new Integer(items[i].getComputerID()).toString(), items[i].getName());
        }
        return hm;
    }






}
