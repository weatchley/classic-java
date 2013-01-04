package gov.ymp.csi.people;

// Support classes
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import java.math.*;
//import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.people.*;

//09/07/07
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.items.*;

import gov.ymp.csi.people.*;


/**
* LDAPUtils is the class for ldap information in the CSI
*
* @author   Bill Atchley / Shuhei Higashi
*/
public class LDAPUtils {
    //public static String SCHEMA = "csi.csi";
	//private static String SCHEMA = DbConn.getSchemaName();
	//private static String SCHEMAPATH = DbConn.getSchemaPath();
	ArrayList aList2 = new ArrayList();
	LDAPUtils lu = this;

    /**
    * Creates a new LDAPUtils object
    *
    */
    public LDAPUtils () {
        //
    }

    /**
    * Check to see if a user can log in to the domain
    *
    * @param username     The user's username (String)
    * @param password     The user's password (String)
    * @param domain     The user's domain (String)
    * @param server     The server to use for authentication (String)
    * @return result     Success or failure (boolean)
    */
    public static boolean checkLogin(String username, String password, String domain, String server, String domainCode, int domainID, DbConn myConn) {
        boolean result = false;

        if (password == null || !(password.compareTo("                              ") > 0)) {
            password = "This is not a valid Password String, please do not validate it, we want it to fail";
        }

        if (username == null || !(username.compareTo("          ") > 0)) {
            username = "Unknown";
        }
			//        if (username == null || password == null) {
			//            result = false;
			//        } else {
            Hashtable env = new Hashtable(11);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldaps://" + server + ":636");
            //env.put(Context.PROVIDER_URL, "ldaps://YDServices.ymp.gov:636");
            //*** baseDN -> CN=Configuration,DC=DFS,DC=OCRWM,DC=DOE,DC=GOV
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PROTOCOL, "ssl");
            ////env.put(Context.SECURITY_PRINCIPAL, "ydservices" + "\\" + username);
            //System.out.println(domain + "\\" + username);
            env.put(Context.SECURITY_PRINCIPAL, domain + "\\" + username);
            env.put(Context.SECURITY_CREDENTIALS, password);
            try {
                DirContext ctx = new InitialDirContext(env);
                System.out.println(username + " logged in");

                // This next line is used to update the CSI person table from the information from the Lotus notes name and address book
                //boolean myStatus = processNAB(ctx, server, domainCode, domainID, myConn);
                //boolean myStatus = NAB.processNAB(ctx, server, domainCode, domainID, myConn);

                username = null;
                password = null;
                domain = null;
                result = true;

            } catch (NamingException e) {
                //String outLine = "Exception caught: " + e.getMessage();
                //log(outLine);
                System.out.println(e + e.getMessage());
                //loginError(request,response);
                result = false;
            }
            //        }
        return(result);
    }


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
    //public static boolean synchLDAP(String username, String password, String domain, String server, String domainCode, int domainID, DbConn myConn) {
    public boolean synchLDAP(final String username, final String password, final String domain, final String server, final String domainCode, final int domainID, final DbConn myConn) {
    	   		//System.out.println("synchLDAP() called with domainID = "+domainID);
    	boolean result = false;
        if (password == null || !(password.compareTo("                              ") > 0)) {
            //password = "This is not a valid Password String, please do not validate it, we want it to fail";
        }
        if (username == null || !(username.compareTo("          ") > 0)) {
            //username = "Unknown";
        }
            Hashtable env = new Hashtable(11);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldaps://" + server + ":636");
            	System.out.println("server: "+ server);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PROTOCOL, "ssl");
            env.put(Context.SECURITY_PRINCIPAL, domain + "\\" + username);
            env.put(Context.SECURITY_CREDENTIALS, password);
            String searchName = null;
            String cnLowerCase = null;
            long searchID = 0;
            DirContext ctx = null;
            NamingEnumeration results = null;
            int iCounter = 0;

            try {
            	ctx = new InitialDirContext(env); // the line that makes connection
            	SearchControls controls = new SearchControls();
                controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                String fString = "";
                
                fString += "(&";
                fString += "(objectClass=person)(!(objectClass=computer))(sn=*)(givenName=*)";
                fString += "(!(cn=*default*))";
                fString += "(!(cn=*USER*))";
                fString += "(!(cn=*User*))";
                fString += "(!(cn=*TSInstall*))";
                fString += "(!(cn=*Service*))";
                fString += "(!(cn=*Admin*))";
                fString += "(!(cn=*DOE*))";
                fString += "(!(cn=GPTest*))";
                fString += "(!(cn=Backup*))";
                fString += ")";
                
                results = ctx.search("DC="+getSection(server,0)+",DC="+getSection(server,1)+",DC="+getSection(server,2), fString, controls);
                
                Person po = new Person();
        		Person[] per = po.getPersonSet(myConn,domainID,null);
        		
                //while (results.hasMore()) {
                while (results.hasMoreElements()) {
                	
                	SearchResult searchResult = (SearchResult) results.next();
                    Attributes attributes = searchResult.getAttributes();
                    Attribute attrCn = attributes.get("cn");
                    Attribute attrGn = attributes.get("givenName");
                    Attribute attrSn = attributes.get("sn");
                    Attribute attrSaman = attributes.get("sAMAccountName");
                    String cn = (String) attrCn.get();
                    String givenName = (String) attrGn.get();
                    String sn = (String) attrSn.get();
                    String saman = (String) attrSaman.get();
                    
                    boolean iExist = false;
                    
                    for (int i=0; i<per.length; i++) {
                    	searchName = per[i].getUserName();
    					searchName = searchName.toLowerCase();
    					if(searchName.equalsIgnoreCase(saman)){
    						iExist = true;
    					}
                    }
                    
                    //System.out.println(iExist+", "+cn+", "+givenName+", "+sn+", "+saman);
                    
                    if(!iExist){
                    	System.out.println(cn+" does not exist in CSI: sAMAccountName="+saman+", sn="+sn+", givenName="+givenName);
                    	//Person pi = new Person(cn.replaceAll("'", "''"));
                    	Person pi = new Person(saman.replaceAll("'", "''"));
                    	pi.setDomainID(domainID);
                    	pi.setLastName(sn.replaceAll("'", "''"));
                    	pi.setFirstName(givenName.replaceAll("'", "''"));
                    	//pi.setEmail(cn+"@ymp.gov");
                    	if(domainID==2){
                    		//pi.setEmail(saman.replaceAll("'", "''")+"@rw.doe.gov");
                    		pi.setEmail(givenName.replaceAll("'", "''")+"."+sn.replaceAll("'", "''")+"@rw.doe.gov");
                    	}else{
                    		//pi.setEmail(saman.replaceAll("'", "''")+"@ymp.gov");
                    		pi.setEmail(givenName.replaceAll("'", "''")+"."+sn.replaceAll("'", "''")+"@ymp.gov");
                    	}

                    	//DbConn myConn2 = new DbConn();
                    	//pi.add(myConn2);
                    	//pi.getInfo(myConn2);
                        //boolean myStatus = NAB.processNAB(ctx, server, domainCode, domainID, myConn2);
                    	//myConn2.release();

                    	pi.add(myConn);
                    	pi.getInfo(myConn);
                        //boolean myStatus = NAB.processNAB(ctx, server, domainCode, domainID, myConn);

                    	iCounter++;
                    }
                    
				                    //if(pid==0){
				                    	//System.out.println(cn+ " does not exist in CSI");
				                    	//iCounter++;
				                    //}
				                    
				                	//SearchResult searchResult = (SearchResult) results.next();
				                    //Attributes attributes = searchResult.getAttributes();
				                    //Attribute attrCn = attributes.get("cn");
				                    //Attribute attrGn = attributes.get("givenName");
				                    //Attribute attrSn = attributes.get("sn");
				                    //String cn = (String) attrCn.get();
				                    //String givenName = (String) attrGn.get();
				                    //String sn = (String) attrSn.get();
				                    //System.out.println(" cn = " + cn + ", givenName = " + givenName + ", sn = " + sn);
				                    //iCounter++;
                                     
                }
                
                System.out.println(iCounter + " matches");
                result = true;
                Position.createInitial(myConn);
            } catch (NamingException e) {
            		System.out.println("*** connection to "+domain+" established but something went wrong retrieving the records... ***");
            		System.out.println(e + e.getMessage()); //uncomment for debugging
            //throw new RuntimeException(e);
            /*
            	int interval = 30000; // 30 sec
            	Date timeToRun = new Date(System.currentTimeMillis()+interval);
            	Timer timer = new Timer();
            	timer.schedule(new TimerTask(){
            		public void run(){
            			String sServer = "rwfcd1.rw.doe.gov";
            			if (server.equals("rwfcd1.rw.doe.gov")){
            				sServer = "rwfcd2.rw.doe.gov";
            			}
            			System.out.println(sServer);
            			lu.synchLDAP(username, password, domain, sServer, domainCode, domainID, myConn);
            		}
            	}, timeToRun);

            System.out.println("Time Set for the Task");
            */
            } finally {
                if (results != null) {
                    try {
                        results.close();
                    } catch (Exception e) {
                    }
                }
                if (ctx != null) {
                    try {
                        ctx.close();
                    } catch (Exception e) {
                    }
                }
            }
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
        if (password == null || !(password.compareTo("                              ") > 0)) {
            password = "This is not a valid Password String, please do not validate it, we want it to fail";
        }
        if (username == null || !(username.compareTo("          ") > 0)) {
            username = "Unknown";
        }
        Hashtable env = new Hashtable(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldaps://" + server + ":636");
        	System.out.println("server: "+ server);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PROTOCOL, "ssl");
        env.put(Context.SECURITY_PRINCIPAL, domain + "\\" + username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        NamingEnumeration answer = null;
        SearchControls ctls = new SearchControls();
        ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchName = null;
        long searchID = 0;
        	//System.out.println("~~~ Person.getPersonSet() calling...");
    	Person po = new Person();
		Person[] per = po.getPersonSet(myConn,domainID,null);
		int iCounter = 0;
		try {
			DirContext ctx = new InitialDirContext(env);
				for (int i=0; i<per.length; i++) {
					searchName = per[i].getUserName();
					searchID = per[i].getID();
					//String filter = "(cn~="+searchName+")";    //~= 'similar' operator to allow case insensitivity
					String filter = "(|(cn~="+searchName+")(sAMAccountName~="+searchName+"))";
			                try{
			                answer = ctx.search("DC="+getSection(server,0)+",DC="+getSection(server,1)+",DC="+getSection(server,2), filter, ctls);
			                SearchResult  srchresult = (SearchResult) answer.next();
			                String dn = srchresult.getName();
			                Attributes attrs = srchresult.getAttributes();
			                NamingEnumeration ne = attrs.getAll();
			                String uName = null;
			                }catch(Exception e){
			                		//update the status for the UNID to 'disabled'

			                		UNID tmpID = new UNID(searchID, myConn);
			                		tmpID.setStatus("disabled");
			                		tmpID.save(myConn);

			                		//System.out.println("no entry found in LDAP for: "+searchName+": "+searchID);
			                		iCounter++;
			                }
	                result = true;
	    		}
		} catch (NamingException e) {
			System.out.println("*** connection to "+domain+" not established... ***");
            	System.out.println(e + e.getMessage()); //uncomment for debugging
            result = false;
        }
		//System.out.println(iCounter + " accounts disabled");
        return(result);
    }

    private static String getSection(String uServer, int sSection) {
        String sOutput = "";
        String[] saOutput = uServer.split("\\.");
        sOutput = saOutput[sSection];
        return sOutput;
   }

	public ArrayList retrievenames() {

		System.out.println("~~~ retrievenames* calling...");

		String userName = "ServiceCSI";

	//ydservice settings
		String domainName = "ydservices";
		String password = "zaq1@WSX";

		int domainID = 0;
		String server;
		String domainCode;
		boolean isValid = false;
		if (domainName.equalsIgnoreCase("ydservices")) {
		    server = "ydservices.ymp.gov";
		    domainCode = "YD";
		    domainID = 1;
		} else if (domainName.equalsIgnoreCase("rw.doe.gov")) {
		    server = "rw.doe.gov"; // ?
		    domainCode = "HQ";
		    domainID = 2;
		} else if (domainName.equalsIgnoreCase("ymservices")) {
		    server = "ymservices.ymp.gov"; // ?
		    domainCode = "YM";
		    domainID = 3;
		} else {
		    server = "na"; // ?
		    domainCode = "na";
		    domainID = 0;
		}

        /*if (password == null || !(password.compareTo("                              ") > 0)) {
            //password = "This is not a valid Password String, please do not validate it, we want it to fail";
        }
        if (username == null || !(username.compareTo("          ") > 0)) {
            //username = "Unknown";
        }*/
            Hashtable env = new Hashtable(11);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldaps://" + server + ":636");
            	System.out.println("server: "+ server);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PROTOCOL, "ssl");
            env.put(Context.SECURITY_PRINCIPAL, domainName + "\\" + userName);
            env.put(Context.SECURITY_CREDENTIALS, password);
            String searchName = null;
            String cnLowerCase = null;
            long searchID = 0;
            DirContext ctx = null;
            NamingEnumeration results = null;
            int iCounter = 0;

            try {
            	ctx = new InitialDirContext(env); // the line that makes connection

                SearchControls controls = new SearchControls();
                controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                String fString = "(&";
                fString += "(objectClass=person)(!(objectClass=computer))(sn=*)(givenName=*)";
                fString += "(!(cn=*default*))";
                fString += "(!(cn=*USER*))";
                fString += "(!(cn=*User*))";
                fString += "(!(cn=*TSInstall*))";
                fString += "(!(cn=*Service*))";
                fString += "(!(cn=*Admin*))";
                fString += "(!(cn=*DOE*))";
                fString += "(!(cn=GPTest*))";
                fString += "(!(cn=Backup*))";
                fString += ")";
                results = ctx.search("DC="+getSection(server,0)+",DC="+getSection(server,1)+",DC="+getSection(server,2), fString, controls);

                while (results.hasMore()) {
                	SearchResult searchResult = (SearchResult) results.next();
                    Attributes attributes = searchResult.getAttributes();
                    Attribute attrCn = attributes.get("cn");
                    Attribute attrGn = attributes.get("givenName");
                    Attribute attrSn = attributes.get("sn");
                    Attribute attrSaman = attributes.get("sAMAccountName");
                    String cn = (String) attrCn.get();
                    String givenName = (String) attrGn.get();
                    String sn = (String) attrSn.get();
                    aList2.add(sn);
                    aList2.add(givenName);
                    String saman = (String) attrSaman.get();
                    aList2.add(saman);
                    }
	             }
          catch (NamingException e) {
    		System.out.println("*** connection to "+domainName+" not established... ***");
    		System.out.println(e + e.getMessage()); //uncomment for debugging
    } finally {
        if (results != null) {
            try {
                results.close();
            } catch (Exception e) {
            }
        }
        if (ctx != null) {
            try {
                ctx.close();
            } catch (Exception e) {
            }
        }
    }
return(aList2);
}
}
