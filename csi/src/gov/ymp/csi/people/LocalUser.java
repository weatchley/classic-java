package gov.ymp.csi.people;

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
* LocalUser is the class for localuser in the db
*
* @author   Bill Atchley
*/
public class LocalUser {
    private long personID = 0;
    private String password = null;
    private java.util.Date pwExpiresOn = null;
    private int failedAttempts = 0;
    private java.util.Date lastFailure = null;
    private java.util.Date lockout = null;
    private boolean active = false;
    private String oldPassword1 = null;
    private String oldPassword2 = null;
    private String oldPassword3 = null;
    private String oldPassword4 = null;
    private String oldPassword5 = null;
    private String oldPassword6 = null;

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
        SCHEMA = "csi";
        SCHEMAPATH = "csi";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty LocalUser object
    */
    public LocalUser () {
        init();
    }


    /**
    * Creates a LocalUser object and uses the given id to populate it from the db
    *
    * @param id     The id of the LocalUser to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public LocalUser (long id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a LocalUser from the db and stores it in the current LocalUser object
    *
    * @param id     The name to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public void getInfo (long id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a LocalUser from the db and stores it in the current LocalUser object
    *
    * @param name     The name to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void lookup (long id, DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT personid, password, pwexpireson, failedattempts, lastfailure, lockout, isactive, oldpassword1, oldpassword2, oldpassword3, oldpassword4, oldpassword5, oldpassword6 ";
            sqlcode += "FROM " + SCHEMAPATH + ".local_user WHERE personid=" + id + " ";

//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            if (rs.next()) {
                long myID = rs.getLong(1);
                if (myID == id) {
                    personID = myID;
                    password = rs.getString(2);
			    	pwExpiresOn = (java.util.Date) rs.getTimestamp(3);
			    	failedAttempts = rs.getInt(4);
			    	lastFailure = (java.util.Date) rs.getTimestamp(5);
			    	lockout = (java.util.Date) rs.getTimestamp(6);
			    	active = (rs.getString(7).equals("T")) ? true : false;
			    	oldPassword1 = rs.getString(8);
			    	oldPassword2 = rs.getString(9);
			    	oldPassword3 = rs.getString(10);
			    	oldPassword4 = rs.getString(11);
			    	oldPassword5 = rs.getString(12);
			    	oldPassword6 = rs.getString(13);

                    isNew = false;
				}

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage() + " - LocalUser lookup";
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"csi",0, outLine);
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage() + " - LocalUser lookup";
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"csi",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }

    public long getPersonID() {
		return personID;
	}

	public void setPersonID(long val) {
		personID = val;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword (String val) {
		password = val;
	}

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public java.util.Date getLastFailure() {
        return lastFailure;
    }

    public void setLastFailure(java.util.Date lastFailure) {
        this.lastFailure = lastFailure;
    }

    public java.util.Date getLockout() {
        return lockout;
    }

    public void setLockout(java.util.Date lockout) {
        this.lockout = lockout;
    }

    public String getOldPassword1() {
        return oldPassword1;
    }

    public void setOldPassword1(String oldPassword1) {
        this.oldPassword1 = oldPassword1;
    }

    public String getOldPassword2() {
        return oldPassword2;
    }

    public void setOldPassword2(String oldPassword2) {
        this.oldPassword2 = oldPassword2;
    }

    public String getOldPassword3() {
        return oldPassword3;
    }

    public void setOldPassword3(String oldPassword3) {
        this.oldPassword3 = oldPassword3;
    }

    public String getOldPassword4() {
        return oldPassword4;
    }

    public void setOldPassword4(String oldPassword4) {
        this.oldPassword4 = oldPassword4;
    }

    public String getOldPassword5() {
        return oldPassword5;
    }

    public void setOldPassword5(String oldPassword5) {
        this.oldPassword5 = oldPassword5;
    }

    public String getOldPassword6() {
        return oldPassword6;
    }

    public void setOldPassword6(String oldPassword6) {
        this.oldPassword6 = oldPassword6;
    }

    public java.util.Date getPwExpiresOn() {
        return pwExpiresOn;
    }

    public void setPwExpiresOn(java.util.Date pwExpiresOn) {
        this.pwExpiresOn = pwExpiresOn;
    }




    /**
    * Save the current LocalUser to the DB
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
                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".local_user (personid, password, pwexpireson, failedattempts, lastfailure, lockout, isactive, " +
                    "oldpassword1, oldpassword2, oldpassword3, oldpassword4, oldpassword5, oldpassword6) " +
                    "VALUES (" + personID + ", ?, SYSDATE, " + failedAttempts + ", NULL, NULL, '" + ((active) ? 'T' : 'F') + "', ?, ?, ?, ?, ?, ?" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, password);
                //pstmt.setDate(++sqlID, ((pwExpiresOn != null) ? Utils.castDate(pwExpiresOn) : null));
                //pstmt.setDate(++sqlID, ((lastFailure != null) ? Utils.castDate(lastFailure) : null));
                //pstmt.setDate(++sqlID, ((lockout != null) ? Utils.castDate(lockout) : null));
                pstmt.setString(++sqlID, "PASS");
                pstmt.setString(++sqlID, "PASS");
                pstmt.setString(++sqlID, "PASS");
                pstmt.setString(++sqlID, "PASS");
                pstmt.setString(++sqlID, "PASS");
                pstmt.setString(++sqlID, "PASS");

                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".local_user " +
                      "Set password=?, pwexpireson=?, failedattempts=" + failedAttempts + ", lastfailure=?, lockout=?, isactive='" + ((active) ? 'T' : 'F') + "', " +
                      "oldpassword1=?, oldpassword2=?, oldpassword3=?, oldpassword4=?, oldpassword5=?, oldpassword6=?  WHERE personid = " + personID +
                      "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                pstmt.setString(++sqlID, password);
                pstmt.setTimestamp(++sqlID, ((pwExpiresOn != null) ? new java.sql.Timestamp(pwExpiresOn.getTime()) : null));
                pstmt.setTimestamp(++sqlID, ((lastFailure != null) ? new java.sql.Timestamp(lastFailure.getTime()) : null));
                pstmt.setTimestamp(++sqlID, ((lockout != null) ? new java.sql.Timestamp(lockout.getTime()) : null));
                pstmt.setString(++sqlID, oldPassword1);
                pstmt.setString(++sqlID, oldPassword2);
                pstmt.setString(++sqlID, oldPassword3);
                pstmt.setString(++sqlID, oldPassword4);
                pstmt.setString(++sqlID, oldPassword5);
                pstmt.setString(++sqlID, oldPassword6);
                rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage() + " - LocalUser save";
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"csi",0, outLine);
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage() + " - LocalUser save";
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"csi",0, outLine);
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
    * Get a list of LocalUser from the DB
    *
    * @param myConn     Connection to the database
    */
    public static LocalUser[] getItemList(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        LocalUser[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "csi";

            sqlFrom = " FROM " + mySchemaPath + ".local_user ";
            sqlWhere = "";
            sqlOrderBy = " ORDER BY description ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new LocalUser[returnSize];

            sqlcode = "SELECT personid, password, pwexpireson, failedattempts, lastfailure, lockout, isactive, oldpassword1, oldpassword2, oldpassword3, oldpassword4, oldpassword5, oldpassword6 " +
                  sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new LocalUser();
                item[i].personID = rs.getInt(1);
                item[i].password = rs.getString(2);
				item[i].pwExpiresOn = (java.util.Date) rs.getTimestamp(3);
				item[i].failedAttempts = rs.getInt(4);
				item[i].lastFailure = (java.util.Date) rs.getTimestamp(5);
				item[i].lockout = (java.util.Date) rs.getTimestamp(6);
				item[i].active = (rs.getString(7).equals("T")) ? true : false;
				item[i].oldPassword1 = rs.getString(8);
				item[i].oldPassword2 = rs.getString(9);
				item[i].oldPassword3 = rs.getString(10);
				item[i].oldPassword4 = rs.getString(11);
				item[i].oldPassword5 = rs.getString(12);
				item[i].oldPassword6 = rs.getString(13);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage() + " - LocalUser getItemList";
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"csi",0, outLine);
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage() + " - LocalUser getItemList";
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"csi",0, outLine);
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
    * test password if it meets standards
    *
    * @param pass     Password to test
    * @param username username to test
    *
    * @return         Status of password matching
    */
    public static boolean testPassword(String pass, String username) {
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
    * test new password if it meets standards
    *
    * @param pass     Password to test
    * @param per      Person to test
    *
    * @return         Status of password matching
    */
    public boolean testNewPassword(String pass, Person per) {
        boolean status = testPassword(pass, per.getUserName());
        if (status) {
			String testPass = cryptPassword(pass);
			if (testPass.equals(password) ||
			    testPass.equals(oldPassword1) ||
			    testPass.equals(oldPassword2) ||
			    testPass.equals(oldPassword3) ||
			    testPass.equals(oldPassword4) ||
			    testPass.equals(oldPassword5) ||
			    testPass.equals(oldPassword6)) {
					status = false;
				}
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
    * Creates a random password of given length
    *
    * @param size     The size for the new string
    *
    * @return         The new string
    */
    public static String genRandPassword(int size) {
        String testVals = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()+";
        int countOfTestVals = 73;
        int keyLength = size;
        java.util.Random generator = new java.util.Random(System.currentTimeMillis());
        String KeyID = "";
        for (int pos = 0; (pos < keyLength); pos++) {
            int loc = generator.nextInt(countOfTestVals);
            KeyID = KeyID + testVals.charAt(loc);
        }
        String value = KeyID;
        return (value);
    }



    /**
    * encrypt a password
    *
    * @param pass     The input password
    *
    * @return         The encrypted password
    */
    public static String cryptPassword(String pass) {
		String newPass = jcrypt.crypt("da", pass);
		if (pass.length() > 8) {
			newPass += jcrypt.crypt("da", pass.substring(8));
		}
		if (pass.length() > 16) {
			newPass += jcrypt.crypt("da", pass.substring(16));
		}
		newPass = newPass.substring(0,((newPass.length() < 30) ? newPass.length() : 30));
		return newPass;
    }



    /**
    * change password
    *
    * @param pass     The input password
    *
    */
    public void changePassword(String pass) {
		changePassword(pass, false);
	}



    /**
    * change password
    *
    * @param pass     The input password
    *
    */
    public void changePassword(String pass, boolean expireNow) {
		String newPass = cryptPassword(pass);
		oldPassword6 = oldPassword5;
		oldPassword5 = oldPassword4;
		oldPassword4 = oldPassword3;
		oldPassword3 = oldPassword2;
		oldPassword2 = oldPassword1;
		oldPassword1 = password;
		password = newPass;
		failedAttempts = 0;
		lastFailure = null;
		lockout=null;
		Calendar cal = Calendar.getInstance();
        pwExpiresOn = (expireNow) ? (cal.getTime()) : Utils.addDays(cal.getTime(), 120);
    }



    /**
    * reset password
    *
    * @param pass     The input password
    *
    * @return         The new password
    */
    public String resetPassword() {
		String newPass = genRandPassword(8);
		changePassword(newPass, true);

		return newPass;
    }




}
