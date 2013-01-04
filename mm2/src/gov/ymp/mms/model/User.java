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
* User is the class for users in the db
*
* @author   Bill Atchley
*/
public class User {
    public int ID = 0;
    public String username = null;
    public String firstName = null;
    public String lastName = null;
    public String fullName = null;
    public String organization = null;
    public String location = null;
    public String areacode = null;
    public String phonenumber = null;
    public String extension = null;
    public String password = null;
    public String email = null;
    public boolean isActive = false;
    public int accessType = 0;
    public java.util.Date datePasswordExpires = null;
    public int failedAttempts = 0;
    public java.util.Date lastFailure = null;
    public java.util.Date lockout = null;
    public String oldPassword1 = null;
    public String oldPassword2 = null;
    public String oldPassword3 = null;
    public String oldPassword4 = null;
    public String oldPassword5 = null;
    public String oldPassword6 = null;
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
    * Creates a new empty User object
    */
    public User () {
        ID = 0;
        init();
    }


    /**
    * Creates an User object and uses the given id to populate it from the db
    *
    * @param id     The id of the User to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public User (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a User from the db and stores it in the current User object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a User from the db and stores it in the current User object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, DbConn myConn) {
        ID = 0;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, username, firstname, lastname, organization, location, areacode, phonenumber, extension, ";
            sqlcode += "password, email, isactive, accesstype, datepasswordexpires, failedattempts, lastfailure, lockout, ";
            sqlcode += "oldpassword1, oldpassword2, oldpassword3, oldpassword4, oldpassword5, oldpassword6 ";
            sqlcode += " FROM " + SCHEMAPATH + ".users WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            if (myID == id) {
                ID = myID;
                username = rs.getString(2);
                firstName = rs.getString(3);
                lastName = rs.getString(4);
                organization = rs.getString(5);
                location = rs.getString(6);
                areacode = rs.getString(7);
                phonenumber = rs.getString(8);
                extension = rs.getString(9);
                password = rs.getString(10);
                email = rs.getString(11);
                isActive = ((rs.getString(12).equals("T")) ? true : false);
                accessType = rs.getInt(13);
                datePasswordExpires = rs.getTimestamp(14);
                failedAttempts = rs.getInt(15);
                lastFailure = rs.getTimestamp(16);
                lockout = rs.getTimestamp(17);
                oldPassword1 = rs.getString(18);
                oldPassword2 = rs.getString(19);
                oldPassword3 = rs.getString(20);
                oldPassword4 = rs.getString(21);
                oldPassword5 = rs.getString(22);
                oldPassword6 = rs.getString(23);
                isNew = false;
                fullName = lastName + ", " + firstName;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - User lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }

    public int getAccessType() {
        return accessType;
    }

    public void setAccessType(int accessType) {
        this.accessType = accessType;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public java.util.Date getLockout() {
        return lockout;
    }

    public void setLockout(java.util.Date lockout) {
        this.lockout = lockout;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getID() {
        return ID;
    }

    public java.util.Date getDatePasswordExpires() {
        return datePasswordExpires;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public java.util.Date getLastFailure() {
        return lastFailure;
    }

    public String getOldPassword1() {
        return oldPassword1;
    }

    public String getOldPassword2() {
        return oldPassword2;
    }

    public String getOldPassword3() {
        return oldPassword3;
    }

    public String getOldPassword4() {
        return oldPassword4;
    }

    public String getOldPassword5() {
        return oldPassword5;
    }

    public String getOldPassword6() {
        return oldPassword6;
    }




}
