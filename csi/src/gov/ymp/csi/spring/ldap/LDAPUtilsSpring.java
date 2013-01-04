package gov.ymp.csi.spring.ldap;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;

import gov.ymp.csi.db.*;

/**
* LDAPUtilsSpring is the Spring-driven class for ldap information in the CSI
*
* @author   Shuhei Higashi
*/
public class LDAPUtilsSpring {
    /**
	    * Creates a new LDAPUtils object
	    *
	    */
	    public LDAPUtilsSpring (){}
	    
	    /**
	    * Synching LDAP users with CSI (add LDAP users to CSI - populate email field and other user attributes)
	    *
	    * @param username     The user's username (String)
	    * @param password     The user's password (String)
	    * @param domain     The user's domain (String)
	    * @param server     The server to use for authentication (String)
	    * @param domainCode      DomainCode (String)
	    * @param domainID     Domain ID (int)
	    * @param myConn     DB connection object (DBConn)
	    * @return result     Success or failure (boolean)
	    */
	    public static boolean synchLDAP(String username, String password, String domain, String server, String domainCode, int domainID, DbConn myConn) {
	    	System.out.println("synchLDAP() called");
	    	boolean result = false;
	        return(result);
	    }
	    
	    /**
	    * Synching CSI users with LDAP (change status "disabled" in CSI.UNID for the users that do not exist in LDAP)
	    *
	    * @param username     The user's username (String)
	    * @param password     The user's password (String)
	    * @param domain     The user's domain (String)
	    * @param server     The server to use for authentication (String)
	    * @param domainCode      DomainCode (String)
	    * @param domainID     Domain ID (int)
	    * @param myConn     DB connection object (DBConn)
	    * @return result     Success or failure (boolean)
	    */
	    public static boolean synchCSI(String username, String password, String domain, String server, String domainCode, int domainID, DbConn myConn) {
	        boolean result = false;
	        System.out.println("synchCSI() called");
	        return(result);
	    }
	    
}
