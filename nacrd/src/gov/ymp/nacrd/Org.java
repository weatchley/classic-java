package gov.ymp.nacrd;

import javax.servlet.*;
import javax.servlet.http.*;
// Support classes
import java.io.*;
import java.util.*;
import java.lang.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import gov.ymp.nacrd.*;
import gov.ymp.util.*;
import gov.ymp.util.db.*;
import java.sql.*;


/**
* Users is the set of organization related functions for NACRD
*
* @author   Bill Atchley
*/
public class Org {
    //
    private int userid = 0;
    private String abbreviation = null;
    private String name = null;
    //private ServletContext context = getServletContext();
    //private String docRoot = context.getRealPath("/");
    private static String SCHEMA = "nacrd";
    private boolean isNew = true;

    /**
    * creates a new empty Org object
    */
    public Org () {
        //
    }

    /**
    * Creates a Org object and retrieves it
    *
    * @param oname     The org to lookup
    */
    public Org (String oname) {
        lookup (oname);
    }

    /**
    * Retrieves an Org and stores it in the current object
    *
    * @param oname     The org to lookup
    */
    public void lookup (String oname) {
        String outLine = "";
        try {
//
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            ResultSet rset = stmt.executeQuery ("SELECT  abbreviation,name " +
                  "FROM " + SCHEMA + ".organizations  " +
                  "WHERE UPPER(abbreviation)=UPPER('" + oname + "')");
            while (rset.next()) {
                abbreviation = rset.getString(1);
                name = rset.getString(2);
            }
            isNew = false;
            myConn.release();

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
        }
    }

    /**
    * Update an Org and store it
    *
    */
    public void update () {
        String outLine = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            PreparedStatement pstmt = null;
            pstmt = myConn.conn.prepareStatement(
                "UPDATE " + SCHEMA + ".organizations SET abbreviation='" + abbreviation + "', name='" + name + "'" +
                "WHERE abbreviation='" + abbreviation + "'"
                );
            int rows = pstmt.executeUpdate();
            isNew = false;
            myConn.release();

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
        }
    }

    /**
    * Retrieve the abbreviation for the current org
    *
    * @return abbreviation     The abbreviation for the current org
    */
    public String getAbbreviation() {
        return(abbreviation);
    }

    /**
    * Retrieve the name for the current org
    *
    * @return name     The name for the current org
    */
    public String getName() {
        return(name);
    }

    /**
    * Retrieve the name for a given org abbreviation
    *
    * @abbr       The org abbreviation to lookup
    * @return     The name for the current org
    */
    public static String getName(String abbr) {
        String outLine = "";
        String value = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            ResultSet rset = stmt.executeQuery ("SELECT abbreviation,name FROM " + SCHEMA + ".organizations " +
                "WHERE abbreviation='" + abbr + "'");
            rset.next();
            value = rset.getString(2);

        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
        }

        return(value);
    }


    /**
    * Retrieve the isNew flag
    *
    * @return      The status for the current org
    */
    public boolean getIsNew() {
        return(isNew);
    }

    /**
    * Set the abbreviation for the current org
    *
    * @param abb     The abbreviation for the current org
    */
    public void setAbbreviation(String abb) {
        abbreviation = abb;
    }

    /**
    * Set the fullname for the current org
    *
    * @param on     The name for the current org
    */
    public void setName(String on) {
        name = on;
    }


    /**
    * Retrieves a List of organizations as an array of orgs
    *
    * @return      A organization list
    */
    public static Org[] orgLookup () {
        String outLine = "";
        Org[] orgs = null;
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            ResultSet rset = stmt.executeQuery ("SELECT count(*) FROM " + SCHEMA + ".organizations");
            rset.next();
            int aSize = rset.getInt(1);
            rset = stmt.executeQuery ("SELECT abbreviation,name " +
                "FROM " + SCHEMA + ".organizations ORDER BY name");
            orgs = new Org[aSize];
            int i = 0;
            while (rset.next()) {
                orgs[i] = new Org();
                orgs[i].setAbbreviation(rset.getString(1));
                orgs[i].setName(rset.getString(2));
                i++;
            }
            myConn.release();

        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
        }
        return (orgs);
    }




}