package gov.ymp.mms.model;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
* ChargeNumber is the class for charge numbers in the db
*
* @author   Bill Atchley
*/
public class ChargeNumber {
    public String cn = null;
    public int fy = 0;
    public int site = 0;
    public String description = null;
    public String wbs = null;
    public double funding = 0.0;
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
        SCHEMA = "mms";
        SCHEMAPATH = "mms";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty ChargeNumber object
    */
    public ChargeNumber () {
        cn = null;
        init();
    }


    /**
    * Creates a ChargeNumber object and uses the given value to populate it from the db
    *
    * @param val     The cn to lookup from the db
    * @param myConn  Connection to the database
    */
    public ChargeNumber (String val, DbConn myConn) {
        init();
        lookup(val, myConn);
    }


    /**
    * Retrieves a ChargeNumber from the db and stores it in the current ChargeNumber object
    *
    * @param val     The cn to lookup from the db
    * @param myConn  Connection to the database
    */
    public void getInfo (String val, DbConn myConn) {
        lookup(val, myConn);
    }


    /**
    * Retrieves a ChargeNumber from the db and stores it in the current ChargeNumber object
    *
    * @param val     The cn to lookup from the db
    * @param myConn  Connection to the database
    */
    public void lookup (String val, DbConn myConn) {
        cn = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT chargenumber, fyscalyear, site, description, wbs, funding ";
            sqlcode += " FROM " + SCHEMAPATH + ".charge_numbers WHERE chargenumber='" + val + "'";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String myCN = rs.getString(1);
            if (myCN.equals(val)) {
                cn = myCN;
                fy = rs.getInt(2);
                site = rs.getInt(3);
                description = rs.getString(4);
                wbs = rs.getString(5);
                funding = rs.getFloat(6);

                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - ChargeNumber lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - ChargeNumber lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get a list of ChargeNumber from the DB
    *
    * @param myConn       Connection to the database
    */
    public static ChargeNumber[] getItemList(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        ChargeNumber[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            sqlFrom = " FROM " + mySchemaPath + ".charge_numbers ";
            sqlWhere = " WHERE 1=1 ";
            sqlOrderBy = " ORDER BY chargenumber ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new ChargeNumber[returnSize];

            sqlcode = "SELECT chargenumber, fyscalyear, site, description, wbs, funding " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new ChargeNumber();
                item[i].cn = rs.getString(1);
                item[i].fy = rs.getInt(2);
                item[i].site = rs.getInt(3);
                item[i].description = rs.getString(4);
                item[i].wbs = rs.getString(5);
                item[i].funding = rs.getFloat(6);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ChargeNumber - getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ChargeNumber - getItemList");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getFunding() {
        return funding;
    }

    public void setFunding(double funding) {
        this.funding = funding;
    }

    public int getFy() {
        return fy;
    }

    public void setFy(int fy) {
        this.fy = fy;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public String getWbs() {
        return wbs;
    }

    public void setWbs(String wbs) {
        this.wbs = wbs;
    }




}
