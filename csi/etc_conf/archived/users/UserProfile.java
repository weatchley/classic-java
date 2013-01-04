package gov.ymp.csi.users;

import gov.ymp.csi.users.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import java.util.*;

/**
 * UserProfile handles CSI users.
 *
 * A user record contains personal, HR, and location information.
 *
 * @author Anna Naydenova
 *
*/
public class UserProfile {
    private String userName = "This is a userName";
    private UNID userUNID = null;
    private long userID = 0; 
    private String firstName = null;
    private String lastName = null;   
    private Employer userEmployer = null;
    private Position userPosition = null;
    private Location userLocation = null;
    private String userOfficeNumber = null;
    //    private Training completedTraining = null;

    public UserProfile () {
    }

    /*
    public UNID getUserUNID () {
    }
    public long getUserID () {
    }
    public String getUserName () {
    }
    public String getFirstName () {
    }
    public String getLastName () {
    }
    public Employer getEmployer () {
    }
    public Position getPosition () {
    }
    public Location getLocation () {
    }
    //    public Training getTraining () {
    //    }
    public void setUserName () {
    }
    public void setFirstName () {
    }
    public void setLastName () {
    }
    public void setEmployer () {
    }
    public void setPosition () {
    }
    public void setLocation () {
    }
    public void setTraining () {
    }
    */

    /**
     * Retrieves user information from the database
     *
     * @param myConn    connection handle to the database (DbConn)
     * @param userID    user's ID (long)
     *
     * @return          user info (ArrayList)
    */
    public String getUserInfo (DbConn myConn, long uID) {
	return "This is all the information about your user.";
    }

    /**
     * Add user to the database
     *
     * @param myConn    connection handle to the DB (DbConn)
     *
    */
    public void addUser (DbConn myConn) {

    }
}
  
