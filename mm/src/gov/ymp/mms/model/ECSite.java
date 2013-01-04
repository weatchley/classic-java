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
* ECSite is the class for ecsites in the db
*
* @author   Bill Atchley
*/
public class ECSite {
    public String ec = null;
    public int site = 0;
    public boolean taxable = false;
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
    * Creates a new empty ECSite object
    */
    public ECSite () {
        ec = null;
        init();
    }


    /**
    * Creates a ECSite object and uses the given id to populate it from the db
    *
    * @param val     The ec of the ECSite to lookup from the db
    * @param sit     The site of the ECSite to lookup from the db
    * @param myConn  Connection to the database
    */
    public ECSite (String val, int sit, DbConn myConn) {
        init();
        lookup(val, sit, myConn);
    }


    /**
    * Retrieves a ECSite from the db and stores it in the current ECSite object
    *
    * @param val     The ec  to lookup from the db
    * @param sit     The site of the ECSite to lookup from the db
    * @param myConn  Connection to the database
    */
    public void getInfo (String val, int sit, DbConn myConn) {
        lookup(val,sit, myConn);
    }


    /**
    * Retrieves a ECSite from the db and stores it in the current ECSite object
    *
    * @param val     The ec to lookup from the db
    * @param sit     The site of the ECSite to lookup from the db
    * @param myConn  Connection to the database
    */
    public void lookup (String val, int sit, DbConn myConn) {
        ec = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT ec, site, taxable ";
            sqlcode += " FROM " + SCHEMAPATH + ".ec_site WHERE ec='" + val + "' AND site=" + sit;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String myEC = rs.getString(1);
            if (myEC.equals(val)) {
                ec = myEC;
                site = rs.getInt(2);
                taxable = ((rs.getString(3).equals("T")) ? true : false);

                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - ECSite lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - ECSite lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get a list of ECSite from the DB
    *
    * @param myConn       Connection to the database
    */
    public static ECSite[] getItemList(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        ECSite[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            sqlFrom = " FROM " + mySchemaPath + ".ec_site ";
            sqlWhere = " WHERE 1=1 ";
            sqlOrderBy = " ORDER BY ec ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new ECSite[returnSize];

            sqlcode = "SELECT ec, site, taxable " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new ECSite();
                item[i].ec = rs.getString(1);
                item[i].site = rs.getInt(2);
                item[i].taxable = ((rs.getString(3).equals("T")) ? true : false);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ECSite - getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ECSite - getItemList");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }




}
