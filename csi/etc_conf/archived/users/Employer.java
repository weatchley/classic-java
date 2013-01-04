package gov.ymp.csi.users;

import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import java.util.*;

/**
* Employer is the class for employers information in CSI.
*
* Employers are who signs the paycheck.
*
* Example 1: Anna is employed by SAIC, while Dave is a Catapult employee.
*
* Example 2: Alesia is a fed working for the DOE.
*
* @author   Anna Naydenova 
*/
public class Employer {
    private UNID employerID = null;
    private String employerName = null;
    private Location employerLocation = null;

    /**
     * Creates a new Employer object
     *
     */
    public Employer () {
    
    }
    
    
    /**
    * Retrieve employer info from the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param eID        Specific employer's ID
    *
    * @return           Employer info
    */
    public ArrayList getEmployerInfo (DbConn myConn, long eID) {
        ArrayList employerInfo = null;
	return (employerInfo);
    }


    /**
    * Add organization to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void addEmployer (DbConn myConn) {
    }
    
}
