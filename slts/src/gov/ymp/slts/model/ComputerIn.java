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
import gov.ymp.slts.model.*;
//import oracle.jdbc.driver.*;
//import oracle.sql.*;

/**
* ComputerIn is the class for inventoried computers in the db
*
* @author   Bill Atchley
*/
public class ComputerIn {
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

    public static String SCHEMA = null;
    public static String SCHEMAPATH = null;
    public static String DBTYPE = null;

    /**
    * init new object
    */
    private void init () {
        DbConnM tempDB = new DbConnM("dummy");
        //SCHEMA = tempDB.getSchemaName();
        //SCHEMAPATH = tempDB.getSchemaPath();
        SCHEMA = "servicealtirisdbreader";
        SCHEMAPATH = "eXpress.dbo";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty ComputerIn object
    */
    public ComputerIn () {
        //
        init();
    }


    /**
    * Creates a ComputerIn object and uses the given id to populate it from the db
    *
    * @param id     The id of the ComputerIn to lookup from the db (int)
    * @param myConn Connection to the database
    * @param myConnO Connection to the Oracle database
    */
    public ComputerIn (int id, DbConnM myConn, DbConn myConnO) {
        init();
        lookup(id, myConn, myConnO);
    }


    /**
    * Retrieves a ComputerIn from the db and stores it in the current ComputerIn object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    * @param myConnO Connection to the Oracle database
    */
    public void getInfo (int id, DbConnM myConn, DbConn myConnO) {
        lookup(id, myConn, myConnO);
    }


    /**
    * Retrieves a ComputerIn from the db and stores it in the current ComputerIn object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    * @param myConnO Connection to the Oracle database
    */
    public void lookup (int id, DbConnM myConn, DbConn myConnO) {
        //
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT c.computer_id, c.name, c.lic_os_key, c.domain_name, c.serial_num, c.os, c.manuf, c.last_inventory, c.model_num, c.prod_name, cd.ip_address FROM " +
                  SCHEMAPATH + ".computer c," + SCHEMAPATH + ".computer_display cd  WHERE c.computer_id=" + id + " AND c.computer_id=cd.computer_id";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);

            if (myID == id) {
                computerID = myID;
                name = ((rs.getString(2) != null) ? rs.getString(2) : null);
                licOsKey = ((rs.getString(3) != null) ? rs.getString(3) : null);
                domainName = ((rs.getString(4) != null) ? rs.getString(4) : null);
                serialNum = ((rs.getString(5) != null) ? rs.getString(5) : null);
                os = ((rs.getString(6) != null) ? rs.getString(6) : null);
                manuf = ((rs.getString(7) != null) ? rs.getString(7) : null);
                lastInventory = ((rs.getTimestamp(8) != null) ? rs.getTimestamp(8) : null);
                modelNum = ((rs.getString(9) != null) ? rs.getString(9) : null);
                prodName = ((rs.getString(10) != null) ? rs.getString(10) : null);
                ipAddress = ((rs.getString(11) != null) ? rs.getString(11) : null);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConnO, 0,"slts",0, outLine + " - ComputerIn lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConnO, 0,"slts",0, outLine);
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
    * Get a list of ComputerIn from the DB
    *
    * @param myConn     Connection to the database
    */
    public static ComputerIn[] getItemList(DbConnM myConn, DbConn myConnO) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        ComputerIn[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "eXpress.dbo";

            String domainNameList = "";
            Domain [] dn = Domain.getItemList(myConnO, true);
            for (int i=0; i<dn.length; i++) {
                if (i>0) { domainNameList += ", "; }
                domainNameList += "'" + dn[i].getDomainName() + "'";
            }
//System.out.println("ComputerIn:getItemList-Got Here ");

            sqlFrom = " FROM " + mySchemaPath + ".computer c," + mySchemaPath + ".computer_display cd ";
            sqlWhere = "WHERE c.computer_id=cd.computer_id AND c.computer_id IN (SELECT computer_id FROM " + mySchemaPath + ".computer WHERE domain_name IN (" + domainNameList + ")) ";
            sqlOrderBy = "ORDER BY c.computer_id, c.name ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new ComputerIn[returnSize];

            sqlcode = "SELECT c.computer_id, c.name, c.lic_os_key, c.domain_name, c.serial_num, c.os, c.manuf, c.last_inventory, c.model_num, c.prod_name, cd.ip_address " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new ComputerIn();
                item[i].computerID = rs.getInt(1);
                item[i].name = ((rs.getString(2) != null) ? rs.getString(2) : null);
                item[i].licOsKey = ((rs.getString(3) != null) ? rs.getString(3) : null);
                item[i].domainName = ((rs.getString(4) != null) ? rs.getString(4) : null);
                item[i].serialNum = ((rs.getString(5) != null) ? rs.getString(5) : null);
                item[i].os = ((rs.getString(6) != null) ? rs.getString(6) : null);
                item[i].manuf = ((rs.getString(7) != null) ? rs.getString(7) : null);
                item[i].lastInventory = ((rs.getTimestamp(8) != null) ? rs.getTimestamp(8) : null);
                item[i].modelNum = ((rs.getString(9) != null) ? rs.getString(9) : null);
                item[i].prodName = ((rs.getString(10) != null) ? rs.getString(10) : null);
                item[i].ipAddress = ((rs.getString(11) != null) ? rs.getString(11) : null);
                item[i].isNew = false;
                i++;
            }
            rs.close();

//System.out.println("ComputerIn:getItemList Got Here ");
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConnO, 0,"slts",0, outLine + " - ComputerIn lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConnO, 0,"slts",0, outLine + " - ComputerIn lookup");
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
    * Get a list of domains from the DB
    *
    * @param myConn     Connection to the database
    */
    public static String[] getDomainList(DbConnM myConn, DbConn myConnO) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        String[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "eXpress.dbo";

            String domainNameList = "";
            //Domain [] dn = Domain.getItemList(myConnO, false);
            //for (int i=0; i<dn.length; i++) {
            //    if (i>0) { domainNameList += ", "; }
            //    domainNameList += "'" + dn[i].getDomainName() + "'";
            //}

            sqlFrom = " FROM " + mySchemaPath + ".computer ";
            //sqlWhere = ((dn.length>0) ? " WHERE computer_id IN (SELECT computer_id FROM " + mySchemaPath + ".computer WHERE domain_name IN (" + domainNameList + ")) " : "");
            sqlOrderBy = " ORDER BY domain_name ";

            sqlcode = "SELECT count(DISTINCT domain_name) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new String[returnSize];

            sqlcode = "SELECT DISTINCT domain_name" + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = rs.getString(1);
                i++;
            }
            rs.close();

//System.out.println("ComputerIn:getDomainList Got Here ");
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConnO, 0,"slts",0, outLine + " - ComputerIn domain lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConnO, 0,"slts",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }






}
