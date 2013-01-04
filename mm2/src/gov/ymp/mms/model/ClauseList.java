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
* ClauseList is the class for clause list in the db
*
* @author   Bill Atchley
*/
public class ClauseList {
    public String prnumber = null;
    public int precedence = 0;
    public String type = null;
    public boolean rfp = false;
    public boolean po = false;
    public String clause = null;

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
    * Creates a new empty ClauseList object
    */
    public ClauseList () {
        prnumber = null;
        init();
    }


    /**
    * Creates a ClauseList object and uses the given value to populate it from the db
    *
    * @param pr      The pr to lookup from the db
    * @param prec    The role to lookup from the db
    * @param myConn  Connection to the database
    */
    public ClauseList (String pr, int prec, DbConn myConn) {
        init();
        lookup(pr, prec, myConn);
    }


    /**
    * Retrieves a ClauseList from the db and stores it in the current ClauseList object
    *
    * @param pr      The pr to lookup from the db
    * @param prec    The role to lookup from the db
    * @param myConn  Connection to the database
    */
    public void getInfo (String pr, int prec, DbConn myConn) {
        lookup(pr, prec, myConn);
    }


    /**
    * Retrieves a ClauseList from the db and stores it in the current ClauseList object
    *
    * @param pr      The pr to lookup from the db
    * @param prec    The role to lookup from the db
    * @param myConn  Connection to the database
    */
    public void lookup (String pr, int prec, DbConn myConn) {
        prnumber = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT prnumber, precedence, type, rfp, po, clause ";
            sqlcode += " FROM " + SCHEMAPATH + ".clause_list WHERE prnumber='" + pr + "' AND precedence=" + prec + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String myPR = rs.getString(1);
            if (myPR.equals(pr)) {
                prnumber = myPR;
                precedence = rs.getInt(2);
                type = rs.getString(3);
                rfp = ((rs.getString(4).equals("T")) ? true : false);
                po = ((rs.getString(5).equals("T")) ? true : false);
                clause = rs.getString(6);

                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - ClauseList lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - ClauseList lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get a list of ClauseList from the DB
    *
    * @param pr           The pr to lookup from the db
    * @param myConn       Connection to the database
    */
    public static ClauseList[] getItemList(DbConn myConn, String pr) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        ClauseList[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            sqlFrom = " FROM " + mySchemaPath + ".clause_list ";
            sqlWhere = " WHERE 1=1 ";
            sqlWhere += (pr != null) ? " AND prnumber='" + pr + "' " : "";
            sqlOrderBy = " ORDER BY prnumber, type DESC, precedence ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new ClauseList[returnSize];

            sqlcode = "SELECT prnumber, precedence, type, rfp, po, clause " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new ClauseList();
                item[i].prnumber = rs.getString(1);
                item[i].precedence = rs.getInt(2);
                item[i].type = rs.getString(3);
                item[i].rfp = ((rs.getString(4).equals("T")) ? true : false);
                item[i].po = ((rs.getString(5).equals("T")) ? true : false);
                item[i].clause = rs.getString(6);

                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ClauseList - getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - ClauseList - getItemList");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }

    public String getClause() {
        return clause;
    }

    public void setClause(String clause) {
        this.clause = clause;
    }

    public boolean isPo() {
        return po;
    }

    public void setPo(boolean po) {
        this.po = po;
    }

    public int getPrecedence() {
        return precedence;
    }

    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }

    public String getPrnumber() {
        return prnumber;
    }

    public void setPrnumber(String prnumber) {
        this.prnumber = prnumber;
    }

    public boolean isRfp() {
        return rfp;
    }

    public void setRfp(boolean rfp) {
        this.rfp = rfp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }




}
