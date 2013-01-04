package gov.ymp.csi.people;

// Support classes
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import java.math.*;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.misc.*;
import org.apache.commons.lang.time.DateUtils;


/**
* Person is the class for person information in the CSI
*
* @author   Bill Atchley
*/
public class Person {
    private String userName = null;
    private long personID = 0;
    private String lastName = null;
    private String firstName = null;
    private String email = null;
    private String domainName = null;
    private String domainCode = null;
    private int domainID = 0;
    private Domains dom = null;
    private LocalUser lu = null;
    private String phone = null;
    private java.util.Date lastAuthentication = null;
    private HashMap sysUsernamesByID = new HashMap();
    private HashMap sysUsernamesByAcronym = new HashMap();
    private boolean isValid = false;
    //public static String SCHEMA = "csi.csi";
    private static String SCHEMA = null;
    private static String SCHEMAPATH = null;
    private static String DBTYPE = null;
    private UNID myUNID = null;

    /**
    * init new object
    */
    private void init () {
        DbConn tempDB = new DbConn("dummy");
        SCHEMA = tempDB.getSchemaName();
        SCHEMAPATH = tempDB.getSchemaPath();
        DBTYPE = tempDB.getDBType();
    }

    /**
    * Creates a new Person object
    *
    */
    public Person () {
        init();
        //
    }


    /**
    * Creates a new Person object with a username
    *
    * @param user     The username for the new user object (String)
    */
    public Person (String user) {
        init();
        userName = user;
    }


    /**
    * Authenticates a person then adds person to db (if required) and retrieves person info in object
    *
    * @param password     The user's password (String)
    * @param domain     The user's domain (String)
    * @param myConn     A DB connection handle (DbConn)
    * @return isValid     Success or failure (boolean)
    */
    public boolean authenticate(String password, String domain, DbConn myConn) {
        return authenticate(password, domain, myConn, "csi");
    }


    /**
    * Authenticates a person then adds person to db (if required) and retrieves person info in object
    *
    * @param password     The user's password (String)
    * @param domain     The user's domain (String)
    * @param system     The system that is running
    * @param myConn     A DB connection handle (DbConn)
    * @return isValid     Success or failure (boolean)
    */
    public boolean authenticate(String password, String domain, String system, DbConn myConn) {
        return authenticate(password, domain, myConn, system);
    }


    /**
    * Authenticates a person then adds person to db (if required) and retrieves person info in object
    *
    * @param password     The user's password (String)
    * @param domain     The user's domain (String)
    * @param myConn     A DB connection handle (DbConn)
    * @return isValid     Success or failure (boolean)
    */
    public boolean authenticate(String password, String domain, DbConn myConn, String system) {
		dom = new Domains(domain, myConn);
        String server = dom.getServer();
        domainCode = dom.getCode();
        domainID = dom.getID();

        //if (domain.equalsIgnoreCase("ydservices")) {
        //    server = "ydservices.ymp.gov";
        //    //server = "ydntc2.ydservices.ymp.gov";
        //    domainCode = "YD";
        //    domainID = 1;
        //} else if (domain.equalsIgnoreCase("rw.doe.gov")) {
        //    server = "rw.doe.gov"; // ?
        //    domainCode = "HQ";
        //    domainID = 2;
        //} else if (domain.equalsIgnoreCase("ymservices")) {
        //    server = "ymservices.ymp.gov"; // ?
        //    domainCode = "YM";
        //    domainID = 3;
        //} else {
        //    server = "na"; // ?
        //    domainCode = "na";
        //    domainID = 0;
        //}
        domainName = domain;
//System.out.println("Person-authenticate-Got Here 1" + " Domain name: " + domain + ", Domain ID: " + dom.getID() + " userName: " + userName);
        if (!dom.isLocal()) {
            isValid = LDAPUtils.checkLogin(userName, password, domainName, server, domainCode, domainID, myConn);
		} else {
            String testPass = LocalUser.cryptPassword (password);
            if (personID == 0) {
                getInfo(myConn);
			}
			if (lu == null) {
			    lu = new LocalUser (personID, myConn);
			}
			if (testPass.equals(lu.getPassword()) && lu.isActive() && (lu.getLockout() == null || lu.getLockout().compareTo(new java.util.Date()) < 0)) {
				isValid = true;
				lu.setLockout(null);
				lu.setFailedAttempts(0);
				lu.save(myConn);
			} else {
				lu.setFailedAttempts(lu.getFailedAttempts() + 1);
				lu.setLastFailure(new java.util.Date());
				if (lu.getFailedAttempts() > 6) {
					lu.setLockout(DateUtils.addMinutes((new java.util.Date()), 10));
				}
				lu.save(myConn);
			}

		}

        if (!isValid) {
            ALog.logError(myConn, 0, system, 4, "Failed validation under " + domainCode + " user " + userName);
        } else {
            if (personID == 0) {
                getInfo(myConn);
                if (personID == 0) {
                    domainName = domain;
                    add(myConn);
                    //myConn.conn.commit();
                    getInfo(myConn);
                    Position pos = new Position();
                    pos.setTitle("Default position for " + userName + "-" + domainID);
                    pos.setDescription("Position for " + firstName + " " + lastName + " of " + domainName);
                    pos.setPersonID(personID);
                    pos.add(myConn, personID);
                    //Position.createInitial(myConn);
                }
               //the following section change status to "" - sh 09/20/07
               getID();
               long sID = 0;
	           sID = getID();
	           if (sID != 0) {
	           	    UNID tID = new UNID(sID, myConn);
	           		tID.setStatus(""); //clear the status
	           		tID.save(myConn);
	           }
            }
            ALog.logActivity(myConn, personID, system, 4, "Succesful login by " + domainCode + " user " + userName);
            Statement stmt = null;
            ResultSet rs;
            try {
                stmt = myConn.conn.createStatement();
                PreparedStatement pstmt = null;
                String sqlcode = "";
                sqlcode = "UPDATE " + SCHEMAPATH + ".person SET " +
                    "lastauthentication=SYSDATE WHERE id=" + personID;
//System.out.println(sqlcode);
                pstmt = myConn.conn.prepareStatement(sqlcode);
                int rows = pstmt.executeUpdate();

            }
            catch (java.sql.SQLException e) {
                System.out.println(e + e.getMessage());
            }
            finally {
                if (stmt != null)
                    try { stmt.close(); } catch (Exception i) {}
            }
        }

        return isValid;
    }

    /**
     * Identifies a person then adds person to db (if required) and retrieves person info in object
     *
     * @param domain     The user's domain (String)
     * @param myConn     A DB connection handle (DbConn)
     * @return isValid     Success or failure (boolean)
     * @param system     The system that is running
     */
     public boolean identify(String domain, String system, DbConn myConn) {
	 	 dom = new Domains(domain, myConn);
         String server = dom.getServer();
         domainCode = dom.getCode();
         domainID = dom.getID();

         //if (domain.equalsIgnoreCase("ydservices")) {
         //    server = "ydservices.ymp.gov";
         //    //server = "ydntc2.ydservices.ymp.gov";
         //    domainCode = "YD";
         //    domainID = 1;
         //} else if (domain.equalsIgnoreCase("rw.doe.gov")) {
         //    server = "rw.doe.gov"; // ?
         //    domainCode = "HQ";
         //    domainID = 2;
         //} else if (domain.equalsIgnoreCase("ymservices")) {
         //    server = "ymservices.ymp.gov"; // ?
         //    domainCode = "YM";
         //    domainID = 3;
         //} else {
         //    server = "na"; // ?
         //    domainCode = "na";
         //    domainID = 0;
         //}
         domainName = domain;

         //isValid = LDAPUtils.checkLogin(userName, password, domainName, server, domainCode, domainID, myConn);
         isValid = true;

         if (!isValid) {
             ALog.logError(myConn, 0, system, 4, "Failed validation under " + domainCode + " user " + userName);
         } else {
             if (personID == 0) {
                 getInfo(myConn);
                 if (personID == 0) {
                     domainName = domain;
                     add(myConn);
                     //myConn.conn.commit();
                     getInfo(myConn);
                     Position pos = new Position();
                     pos.setTitle("Default position for " + userName + "-" + domainID);
                     pos.setDescription("Position for " + firstName + " " + lastName + " of " + domainName);
                     pos.setPersonID(personID);
                     pos.add(myConn, personID);
                     //Position.createInitial(myConn);
                 }
             }
             ALog.logActivity(myConn, personID, system, 4, "Succesful login by " + domainCode + " user " + userName);
             Statement stmt = null;
             ResultSet rs;
             try {
                 stmt = myConn.conn.createStatement();
                 PreparedStatement pstmt = null;
                 String sqlcode = "";
                 sqlcode = "UPDATE " + SCHEMAPATH + ".person SET " +
                     "lastauthentication=SYSDATE WHERE id=" + personID;
// System.out.println(sqlcode);
                 pstmt = myConn.conn.prepareStatement(sqlcode);
                 int rows = pstmt.executeUpdate();

             }
             catch (java.sql.SQLException e) {
                 System.out.println(e + e.getMessage());
             }
             finally {
                 if (stmt != null)
                     try { stmt.close(); } catch (Exception i) {}
             }
         }

         return isValid;
     }

    /**
    * Retrieve user info from the db (by username)
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void lookup(DbConn myConn) {
        getInfo(myConn);
    }


    /**
    * Retrieve user info from the db (by username)
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void getInfo(DbConn myConn) {
        try {
            String sqlCode = "SELECT p.id,p.lastname, p.firstname, p.email, p.domainid, d.name, p.phone, " +
                  "p.lastauthentication, p.username " +
                  "FROM " + SCHEMAPATH + ".person p, " + SCHEMAPATH + ".domains d " +
//                  "WHERE p.domainid=d.id AND UPPER(p.username)=UPPER('" + userName + "')";
                  "WHERE p.domainid=d.id AND UPPER(p.username)=UPPER('" + userName + "') AND p.domainid=" + domainID;
            getInfo(myConn, sqlCode);
        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }


    }

    /**
    * Retrieve user info from the db (by user id)
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param id         The UNID id of the person to lookup
    */
    public void lookup(DbConn myConn, long id) {
        getInfo(myConn, id);
    }


    /**
    * Retrieve user info from the db (by user id)
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param id         The UNID id of the person to lookup
    */
    public void getInfo(DbConn myConn, long id) {
        try {
            String sqlCode = "SELECT p.id,p.lastname, p.firstname, p.email, p.domainid, d.name, p.phone, " +
                  "p.lastauthentication, p.username " +
                  "FROM " + SCHEMAPATH + ".person p, " + SCHEMAPATH + ".domains d " +
                  "WHERE p.id=" + id + " AND p.domainid=d.id";
            getInfo(myConn, sqlCode);
        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }


    }


    /**
    * Retrieve user info from the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param sqlCode    The sql statement needed to perform the lookup
    */
    private void getInfo(DbConn myConn, String sqlCode) {
        ResultSet rset = null;
        Statement stmt = null;
        try {
            stmt = myConn.conn.createStatement();
            rset = stmt.executeQuery (sqlCode);
            while (rset.next()) {
                personID = rset.getLong(1);
                lastName = rset.getString(2);
                firstName = rset.getString(3);
                email = rset.getString(4);
                domainID = rset.getInt(5);
                domainName = rset.getString(6);
                phone = rset.getString(7);
                lastAuthentication = (java.util.Date) rset.getDate(8);  // this line causes hung jdbc connections to Oracle
                userName = rset.getString(9);
            }
            rset.close();
	 	    dom = new Domains(domainID, myConn);
			if (dom.isLocal()) {
			    lu = new LocalUser (personID, myConn);
			}
            String sqlCode2 = "SELECT pu.person,pu.system,pu.username,s.acronym " +
                  "FROM " + SCHEMAPATH + ".person_usernames pu, " + SCHEMAPATH + ".systems s WHERE pu.person=" + personID;
            stmt = myConn.conn.createStatement();
            rset = stmt.executeQuery (sqlCode2);
            while (rset.next()) {
                long myPid = rset.getLong(1);
                long mySys = rset.getLong(2);
                String myUN = rset.getString(3);
                String myAcronym = rset.getString(4);
                sysUsernamesByID.put(new Long(mySys), myUN);
                sysUsernamesByAcronym.put(myAcronym, myUN);
            }
        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }
        finally {
            if (rset != null)
                try { rset.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }


    }


    /**
    * Method to retrieve a set of Persons
    *
    * @param myConn     A DB connection handle (DbConn)
    * @return           A set of users.
    */
    public static Person[] getPersonSet(DbConn myConn) {

        return getPersonSet(myConn, 0, null);
    }


    /**
    * Method to retrieve a set of Persons
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param domain     Domain id to look up, 0 for all domains besides 0
    * @param status     Status of the persons to look up (active, inactive, or null for all)
    * @return           A set of users.
    */
    public static Person[] getPersonSet(DbConn myConn, int domain, String status) {
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        int returnSize = 0;
        ResultSet rset = null;
        Statement stmt = null;
        Person[] per = null;
        try {
            stmt = myConn.conn.createStatement();

            sqlFrom = myConn.getSchemaPath() + ".person p, " + myConn.getSchemaPath() + ".domains d, " + myConn.getSchemaPath() + ".unid u ";

            sqlWhere = "p.domainid=d.id AND u.id=p.id AND p.domainid<>0";

            if (domain != 0) {
                sqlWhere += " AND p.domainid=" + domain;
            }
            if (status != null && !status.equals("")) {
                sqlWhere += " AND u.status" + ((status.equals("active")) ? " IS NULL" : "='" + status + "'");
            }

            sqlcode = "SELECT count(*) FROM " + sqlFrom + " WHERE " + sqlWhere;
//System.out.println(sqlcode);

            rset = stmt.executeQuery (sqlcode);
            rset.next();
            returnSize = rset.getInt(1);
            rset.close();

            sqlcode = "SELECT p.id, p.username, p.firstname, p.lastname, p.email, p.domainid, d.name, p.phone, p.lastauthentication " +
                "FROM " + sqlFrom + " WHERE " + sqlWhere + " ORDER BY lastname, firstname";
//System.out.println(sqlcode);

			rset = stmt.executeQuery (sqlcode);

            per = new Person[returnSize];
            int i = 0;
            while (rset.next()) {
                per[i] = new Person();
                per[i].personID = rset.getLong(1);
                per[i].userName = rset.getString(2);
//System.out.println(per[i].userName);
                per[i].firstName = rset.getString(3);
                per[i].lastName = rset.getString(4);
                per[i].email = rset.getString(5);
                per[i].domainID = rset.getInt(6);
                per[i].domainName = rset.getString(7);
                per[i].phone = rset.getString(8);
                per[i].lastAuthentication = (java.util.Date) rset.getTimestamp(9);
	 	        per[i].dom = new Domains(per[i].domainID, myConn);
			    if (per[i].dom.isLocal()) {
			        per[i].lu = new LocalUser (per[i].personID, myConn);
			    }
                i++;
            }
            rset.close();

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e + e.getMessage());
        }
        finally {
            if (rset != null)
                try { rset.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return per;
    }


    /**
    * Add person to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void add(DbConn myConn) {
        add(myConn, (long) 0);
    }


    /**
    * Add person to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param userID     ID of person making change
    */
    public void add(DbConn myConn, long userID) {
        ResultSet rs = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        try {
            stmt = myConn.conn.createStatement();
            String sqlcode = "";
            if (domainID == 0) {
                sqlcode = "SELECT NVL(id, 0) FROM " + SCHEMAPATH + ".domains WHERE UPPER(name)=UPPER('" + domainName + "')";
                //sqlcode = "SELECT ISNULL(id, 0) FROM " + SCHEMAPATH + ".domains WHERE UPPER(name)=UPPER('" + domainName + "')";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int domainID = rs.getInt(1);
                rs.close();
            } else {
                sqlcode = "SELECT name FROM " + SCHEMAPATH + ".domains WHERE id=" + domainID;
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                domainName = rs.getString(1);
                rs.close();

            }
            UNID myUNID = new UNID();
            myUNID.create("person");
            //myUNID.setType("person");
            personID = myUNID.getID();
            myUNID.save(myConn.conn);
            sqlcode = "INSERT INTO " + SCHEMAPATH + ".person (id, username, lastname, firstname, email, domainid, phone) " +
                "VALUES (" + personID + ", '" + userName + "', " +
                ((lastName != null) ? "?" : "'" + userName + "'") + ", " +
                ((firstName != null) ? "?" : "'" + userName + "'") + ", '" +
                ((email != null) ? email : userName + "@ymp.gov") + "'," + domainID + ", " +
                ((phone != null) ? "'" + phone + "'" : "NULL") + ")";
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            int sqlID = 0;
            if (lastName != null && lastName != "n/a") {
                pstmt.setString(++sqlID, lastName);
            }
            if (firstName != null && firstName != "n/a") {
                pstmt.setString(++sqlID, firstName);
            }
            int rows = pstmt.executeUpdate();
            ALog.logActivity(myConn, ((userID != 0) ? userID : personID), "csi", 1, "User '" + userName + "' automatically added");
            myConn.conn.commit();

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
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
    * Save person to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void save(DbConn myConn) {
        save(myConn, (long) 0);
    }


    /**
    * Save person to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param userID     ID of person making change
    */
    public void save(DbConn myConn, long userID) {
        ResultSet rs = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        try {
            stmt = myConn.conn.createStatement();
            String sqlcode = "";
            sqlcode = "UPDATE " + SCHEMAPATH + ".person SET " +
                "username='" + userName + "', " +
                "lastname=" + ((lastName != null) ? "?" : "'" + userName + "'") + ", " +
                "firstname=" + ((firstName != null) ? "?" : "'" + userName + "'") + ", " +
                "email='" + ((email != null) ? email : userName + "@ymp.gov") + "'," +
                "domainid=" + domainID + ", " +
                "phone=" + ((phone != null) ? "'" + phone + "'" : "NULL") + " " +
                "WHERE id=" + personID;
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            int sqlID = 0;
            if (lastName != null && lastName != "n/a") {
                pstmt.setString(++sqlID, lastName);
            }
            if (firstName != null && firstName != "n/a") {
                pstmt.setString(++sqlID, firstName);
            }
            int rows = pstmt.executeUpdate();
            ALog.logActivity(myConn, ((userID != 0) ? userID : personID), "csi", 2, "User '" + userName + "' updated");

            // store usernames for external systems
            pstmt = myConn.conn.prepareStatement(
                "DELETE FROM " + SCHEMAPATH + ".person_usernames WHERE person = " + personID
                );
            rows = pstmt.executeUpdate();
            Iterator it = sysUsernamesByID.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                pstmt = myConn.conn.prepareStatement(
                    "INSERT INTO " + SCHEMAPATH + ".person_usernames (person, system, username) " +
                    "VALUES (" + personID + ", " + key + ", ?)"
                    );
                pstmt.setString(1, (String) sysUsernamesByID.get(key));
                rows = pstmt.executeUpdate();
            }

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
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
    * Get userName
    *
    * @return      The value of userName.
    */
    public String getUserName() {
         return(userName);
    }


    /**
    * Get personID
    *
    * @return      The value of personID.
    */
    public long getID() {
         return(personID);
    }


    /**
    * Get lastName
    *
    * @return      The value of lastName.
    */
    public String getLastName() {
         return(lastName);
    }


    /**
    * Get firstName
    *
    * @return      The value of firstName.
    */
    public String getFirstName() {
         return(firstName);
    }


    /**
    * Get email
    *
    * @return      The value of email.
    */
    public String getEmail() {
         return(email);
    }


    /**
    * Get domainName
    *
    * @return      The value of domainName.
    */
    public String getDomainName() {
         return(domainName);
    }


    /**
    * Get domainID
    *
    * @return      The value of domainID.
    */
    public int getDomainID() {
         return(domainID);
    }


    /**
    * Get domain object
    *
    * @return      The value of dom.
    */
    public Domains getDomain() {
         return(dom);
    }


    /**
    * Get phone
    *
    * @return      The value of phone.
    */
    public String getPhone() {
         return(phone);
    }


    /**
    * Get local user object
    *
    * @return      The value of lu.
    */
    public LocalUser getLocalUser() {
         return(lu);
    }


    /**
    * Get last authentication
    *
    * @return      The value of lastAuthentication.
    */
    public java.util.Date getLastAuthentication() {
         return(lastAuthentication);
    }


    /**
    * Get Username for a system
    *
    * @param sys       The id of the system to check
    *
    * @return          The username in given system
    */
    public String getSystemUsername(long sys) {
         return((String) sysUsernamesByID.get(new Long(sys)));
    }


    /**
    * Get Username for a system
    *
    * @param sys       The acronym of the system to check
    *
    * @return          The username in given system
    */
    public String getSystemUsername(String sys) {
         return((String) sysUsernamesByAcronym.get(sys));
    }


    /**
    * Set userName
    *
    * @param value     The value to set userName to
    */
    public void setUserName(String value) {
         this.userName = value;
    }


    /**
    * Set personID
    *
    * @param value     The value to set personID to
    */
    public void setID(long value) {
         this.personID = value;
    }


    /**
    * Set lastName
    *
    * @param value     The value to set lastName to
    */
    public void setLastName(String value) {
         this.lastName = value;
    }


    /**
    * Set firstName
    *
    * @param value     The value to set firstName to
    */
    public void setFirstName(String value) {
         this.firstName = value;
    }


    /**
    * Set email
    *
    * @param value     The value to set email to
    */
    public void setEmail(String value) {
         this.email = value;
    }


    /**
    * Set domainName
    *
    * @param value     The value to set domainName to
    */
    public void setDomainName(String value) {
         this.domainName = value;
    }


    /**
    * Set domainID
    *
    * @param value     The value to set domainID to
    */
    public void setDomainID(int value) {
         this.domainID = value;
    }


    /**
    * Set phone
    *
    * @param value     The value to set phone to
    */
    public void setPhone(String value) {
         this.phone = value;
    }


    /**
    * Set System Username
    *
    * @param sy      The system to use
    * @param un      The username to use
    */
    public void setSystemUsername(Sys sy, String un) {
         sysUsernamesByID.put(new Long(sy.getID()), un);
         sysUsernamesByAcronym.put(sy.getAcronym(), un);
    }


}
