package gov.ymp.csi.users;

import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import java.util.*;

/**
* Organization is the class for organizations information in CSI.
*
* Organizations are groups of positions working in a specific capacity.
*
* Example 1: ITS is an organization for direct DOE IT support.
*
* Example 2: OQA is an organization within DOE.
*
* @author   Anna Naydenova 
*/
public class Organization {
    private UNID orgID = null;
    private String orgName = null;
    private Location orgLocation = null;
    private UNID orgManagerID = null;

    /**
     * Creates a new Organization object
     *
     */
    public Organization () {
    
    }
    
    
    /**
    * Retrieve organization info from the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param oID        The organization ID
    *
    * @return           Organization info
    *
    */
    public ArrayList getOrganizationInfo (DbConn myConn, long oID) {
	ArrayList organizationInfo = null;
	return organizationInfo;
    }


    /**
    * Add organization to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void addOrganization (DbConn myConn) {
    }
    
}
