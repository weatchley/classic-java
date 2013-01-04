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
* Users is the set of user related functions for NACRD
*
* @author   Bill Atchley
*/
public class User {
    //
    private int userid = 0;
    private String username = "Test";
    private String fullname = "Test";
    private String password = "Test";
    private String org = null;
    private String orgname = null;
    private String pwexpires = null;
    private String enabled = "T";
    private String status = "enabled";
    //private ServletContext context = getServletContext();
    //private String docRoot = context.getRealPath("/");
    private String docRoot = "";
    private static String SCHEMA = "nacrd";
    private boolean isNew = true;

    /**
    * creates a new empty User object
    */
    public User () {
        //
    }

    /**
    * Creates a User object and retrieves it
    *
    * @param uname     The username to lookup
    */
    public User (String uname) {
        lookup (uname);
    }


    /**
    * Retrieves a User and stores it in the current object
    *
    * @param uname     The username to lookup
    */
    public void lookup (String uname) {
        String outLine = "";
        try {
//
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            ResultSet rset = stmt.executeQuery ("SELECT u.id,u.username, u.fullname, u.org, u.password, u.pwexpires, u.enabled, o.name " +
                  "FROM " + SCHEMA + ".users u, " + SCHEMA + ".organizations o " +
                  "WHERE u.org=o.abbreviation AND UPPER(u.username)=UPPER('" + uname + "')");
            while (rset.next()) {
                int userID = rset.getInt(1);
                userid = userID;
                String userName = rset.getString(2);
                username = userName;
                String fullName = rset.getString(3);
                fullname = fullName;
                String Org = rset.getString(4);
                org = Org;
                String Password = rset.getString(5);
                password = Password;
                java.util.Date PWExpires = (java.util.Date) rset.getDate(6);
                pwexpires = Utils.dateToString(PWExpires);
                String Enabled = rset.getString(7);
                enabled = Enabled;
                status = ((enabled.equals("T")) ? "enabled" : "disabled");
                String orgName = rset.getString(8);
                orgname = orgName;
                //System.out.println("Test from DB: " + userID + ", " + userName + ", " + fullName + ", " + Org + ", " + orgName + "");
            }
            if (username.equals(uname)) {
                isNew = false;
            }
            myConn.release();

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
            ALog.logError(e + e.getMessage());
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            //log(outLine);
            ALog.logError(outLine);
        }
    }

    /**
    * Update a User password
    *
    */
    public void updatePassword () {
        String outLine = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            PreparedStatement pstmt = null;
            String pwexp2 = Utils.dateToString(Utils.toDate(pwexpires), "yyyy-MM-dd");
            pstmt = myConn.conn.prepareStatement(
                "UPDATE " + SCHEMA + ".users SET password='" + password + "', pwexpires='" + pwexp2 + "' " +
                "WHERE id='" + userid + "'"
                );
            int rows = pstmt.executeUpdate();
            myConn.release();
            //ALog.logActivity(userid, "Password Changed.");

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
            ALog.logError(userid, e + e.getMessage());
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            ALog.logError(userid, outLine);
        }
    }

    /**
    * reset a User password and store to disk
    *
    */
    public void resetPassword () {
        String outLine = "";
        try {
            User defUsr = new User("default");
            resetPassword(defUsr.getPassword());
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            ALog.logError(outLine);
        }
    }

    /**
    * reset a User password and store to disk
    *
    */
    public void resetPassword (String newPass) {
        String outLine = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            PreparedStatement pstmt = null;
            java.util.Date dNow = new java.util.Date();
            setDatePWExpires(Utils.dateToString(Utils.addDays(dNow, -2)));
            String pwexp2 = Utils.dateToString(Utils.toDate(pwexpires), "yyyy-MM-dd");
            pstmt = myConn.conn.prepareStatement(
                "UPDATE " + SCHEMA + ".users SET password='" + newPass + "', pwexpires='" + pwexp2 + "' " +
                "WHERE id='" + userid + "'"
                );
            int rows = pstmt.executeUpdate();
            myConn.release();
            //ALog.logActivity("Password reset for " + username);

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
            ALog.logError(e + e.getMessage());
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            ALog.logError(outLine);
        }
    }

    /**
    * Update a User and store to disk
    *
    */
    public void update () {
        String outLine = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            PreparedStatement pstmt = null;
            pstmt = myConn.conn.prepareStatement(
                "UPDATE " + SCHEMA + ".users SET username='" + username + "', fullname='" + fullname + "', org='" +  org+ "', " +
                "enabled='" + ((status.equals("enabled")) ? "T" : "F") + "' " +
                "WHERE id='" + userid + "'"
                );
            int rows = pstmt.executeUpdate();
            myConn.release();
            //ALog.logActivity("User " + username + " updated");

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
            ALog.logError(e + e.getMessage());
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            ALog.logError(outLine);
        }
    }

    /**
    * Add a User and store to disk
    *
    */
    public void add () {
        String outLine = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            PreparedStatement pstmt = null;
            User defUsr = new User("default");
            String pwexp = Utils.dateToString(Utils.toDate(defUsr.getDatePWExpires()), "yyyy-MM-dd");
            String sqlcode = "INSERT INTO " + SCHEMA + ".users (username,fullname,org,password,pwexpires,enabled) " +
                "VALUES ('" + username + "', '" + fullname + "', '" +  org+ "', '" + defUsr.getPassword() + "', " +
                "'" + pwexp + "', '" + ((status.equals("enabled")) ? "T" : "F") + "')";
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            int rows = pstmt.executeUpdate();
            myConn.release();
            //ALog.logActivity("User " + username + " added");
            isNew = false;

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
            ALog.logError(e + e.getMessage());
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            ALog.logError(outLine);
        }
    }

    /**
    * Retrieve the userid for the current user
    *
    * @return id     The user id for the current user
    */
    public int getUserID() {
        return(userid);
    }

    /**
    * Retrieve the username for the current user
    *
    * @return username     The user name for the current user
    */
    public String getUsername() {
        return(username);
    }

    /**
    * Retrieve the fullname for the current user
    *
    * @return fullname     The full name for the current user
    */
    public String getFullName() {
        return(fullname);
    }

    /**
    * Retrieve the fullname for the given user ID
    *
    * @param  uID          The user ID to lookup
    * @return fullname     The full name for the current user
    */
    public static String getFullName(int uID) {
        String outLine = "";
        String value = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            ResultSet rset = stmt.executeQuery ("SELECT id,fullname FROM " + SCHEMA + ".users " +
                "WHERE id=" + uID);
            rset.next();
            value = rset.getString(2);

        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            ALog.logError(outLine);
        }

        return(value);
    }

    /**
    * Retrieve the org for the current user
    *
    * @return org     The org for the current user
    */
    public String getOrg() {
        return(org);
    }

    /**
    * Retrieve the pwexpires for the current user
    *
    * @return pwexpires     The date password expires for the current user
    */
    public String getDatePWExpires() {
        return(pwexpires);
    }

    /**
    * Retrieve the password for the current user
    *
    * @return password     The password for the current user
    */
    public String getPassword() {
        return(password);
    }

    /**
    * Retrieve the isNew flag
    *
    * @return      The status for the current document
    */
    public boolean getIsNew() {
        return(isNew);
    }

    /**
    * Set the username for the current user
    *
    * @param un     The user name for the current user
    */
    public void setUsername(String un) {
        username = un;
    }

    /**
    * Set the fullname for the current user
    *
    * @param fn     The full name for the current user
    */
    public void setFullName(String fn) {
        fullname = fn;
    }

    /**
    * Set password
    *
    * @param pass  Password to test
    */
    public void setPassword(String pass) {
        password = pass;
    }

    /**
    * Set the org for the current user
    *
    * @param orgn     The org for the current user
    */
    public void setOrg(String orgn) {
        org = orgn;
    }

    /**
    * Set the pwexpires for the current user
    *
    * @param pwDate     The date password expires for the current user
    */
    public void setDatePWExpires(String pwDate) {
        pwexpires = pwDate;
    }

    /**
    * Set the user status to enabled
    *
    */
    public void enable() {
        status = "enabled";
    }

    /**
    * Set the user status to disabled
    *
    */
    public void disable() {
        status = "disabled";
    }

    /**
    * Test the user if they are enabled
    *
    * @return      Status of user being enabled
    */
    public boolean isEnabled() {
        boolean value = ((status.equals("enabled")) ? true : false);
        return (value);
    }



    /**
    * Verify password
    *
    * @param pass  Password to test
    * @return      Status of password matching
    */
    public boolean verifyPassword(String pass) {
        boolean status = false;
        String testPass = jcrypt.crypt ("da", pass);
        if (password.equals(testPass)) {
            status = true;
        }
        return(status);
    }


    /**
    * test password if it meets standards
    *
    * @param pass  Password to test
    * @return      Status of password matching
    */
    public boolean testPassword(String pass) {
        boolean status = false;
        String [] commonStrings = {"1234","abcd","qwert","asdfg","zxcvb","poiuy","lkjh","password", username};
        boolean foundCommonString = false;
        for (int i=0; i<commonStrings.length; i++) {
            if (pass.matches("(?i).*" + commonStrings[i] + ".*")) {
                foundCommonString = true;
            }
            StringBuffer test1 = new StringBuffer(commonStrings[i]).reverse();
            String test = test1.toString();
            if (pass.matches("(?i).*" + test + ".*")) {
                foundCommonString = true;
            }
        }
        int testCount = 0;
        if (pass.matches(".*\\p{Upper}.*")) {testCount++;}
        if (pass.matches(".*\\p{Lower}.*")) {testCount++;}
        if (pass.matches(".*[0-9].*")) {testCount++;}
        if (pass.matches(".*\\W.*")) {testCount++;}
        if ((pass.length() >= 8)
                //&& (!pass.matches("(?i).*" + username + ".*")) // case insensive search for username in password
                && (!foundCommonString) // case insensive search for common strings in password
                && (testCount >= 3) // three or more test cases must match
                && (!pass.matches("^[0-9].*")) // Can not start with a number
                && (!pass.matches(".*[0-9]$")) // Can not end with a number

            ) {
            status = true;
        }
        return(status);
    }

    /**
    * description for rules used to test password if it meets standards
    *
    * @return      Rule list
    */
    public static String testPasswordRules() {
        String out = "";

        out += "<ul>Password Guidelines<br>";
        out += "<li>- 8 or more characters</li>";
        out += "<li>- can not contain form of username<br> &nbsp; &nbsp; (forward or reverse)</li>";
        out += "<li>- can not use some common strings</li>";
        out += "<li>- can not start or end with a digit</li>";
        out += "<li>- must have three of the following four:<ul>";
        out += "<li> - a special character</li>";
        out += "<li> - a digit<br>";
        out += "<li> - an uppercase letter</li>";
        out += "<li> - a lowercase letter</li></ul></li>";
        //out += "<li>- can not reuse the last six passwords</li>";
        out += "</ul>\n";

        return(out);
    }


    /**
    * test current user for admin rights
    *
    * @return      Status of admin rights
    */
    public boolean canComment() {
        boolean status = false;
        //if (!org.equals("DRI") && !org.equals("DOE") && !org.equals("SN") && !org.equals("SHPO") && !org.equals("ITS")) {
        if (!org.equals("DRI") && !org.equals("SN") && !org.equals("SHPO")) {
            status = true;
        }
        return(status);
    }

    /**
    * test current user for admin rights
    *
    * @return      Status of admin rights
    */
    public boolean hasAdmin() {
        boolean status = false;
        if (org.equals("ITS")) {
            status = true;
        }
        if (org.equals("DOE")) {
            status = true;
        }
        if (org.equals("DRI")) {
            status = true;
        }
        return(status);
    }

    /**
    * Retrieve a list of users
    *
    * @return      A user list
    */
    public static String[] getUserList() {
        String[] users = null;
        String outLine = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            ResultSet rset = stmt.executeQuery ("SELECT count(*) FROM " + SCHEMA + ".users WHERE id<>0 ");
            rset.next();
            int aSize = rset.getInt(1);
            rset = stmt.executeQuery ("SELECT id,username,fullname,org,password,pwexpires,enabled " +
                "FROM " + SCHEMA + ".users " +
                "WHERE id<>0 ORDER BY username");
            users = new String[aSize];
            int i = 0;
            while (rset.next()) {
                users[i] = rset.getString(2);
                i++;
            }
            myConn.release();
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
        }
        return(users);
    }




}