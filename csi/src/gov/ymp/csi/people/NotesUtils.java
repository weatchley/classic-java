package gov.ymp.csi.people;

import java.io.*;
import java.util.*;
import java.math.*;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;

import lotus.domino.Session;
import gov.ymp.csi.db.*;
import gov.ymp.csi.db.ALog.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.systems.*;
import gov.ymp.lotus.core.lotusapi;
import gov.ymp.csi.spring.ldap.*;
/**
* NotesUtils is the class for retrieving information from Lotus Notes.
* Notes addresslist contains the addresslist data from a Lotus Notes database.
* 
*
* @author   Rajesh Patel
*/
public class NotesUtils {
    private UNID myUNID = null;
    private static String SCHEMA = null;
    private static String SCHEMAPATH = null;
    private static String DBTYPE = null;
    
	private lotusapi lotusapi;
	private LDAPUtils ldaputils;
	
	ArrayList aList = new ArrayList();
	ArrayList aList2 = new ArrayList();
	private Session s;
	
	private String personUserName;
	private String personCompanyName;
	private String personDisplayMailAddress;
	private String personDisplayName;
	private String personFirstName;
	private String personMiddleInitial;
    private String personLastName;
    private String personLocation;
    private String personOfficePhoneNumber;
    private long personID = 0;
	  
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
     * Creates a new Location object
     *
     */
    public NotesUtils () {
    	   init();
    }
    
    /**
    * Retrieve location info from the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param locationID The ID of a specific location
    *
    * @return Complete information about the specified location
    */
    public ArrayList getNotesInfo (String host, String username, String password, String servername, String filename) {
        ResultSet rset = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
	    String companyname, displayname, location;
	    	 
        try { 	
    		//create lotusapi object
        	lotusapi = new lotusapi();
        	ldaputils = new LDAPUtils();
    		
    		s = lotusapi.connect(host,username,password);
    		aList = lotusapi.browse(s, servername, filename);
    		
    		//retrieves usernames from LDAP
    		aList2 = ldaputils.retrievenames();
    		
    		for(int i = 0; i < aList.size(); i+=7)
    		{
    	     String Lstname ="";
    	     String Fstname ="";
    	     String Mddlinit ="";
    	     String LstnameLDAP ="";
    	     String FstnameMLDAP ="";
    	     String usernameLDAP="";
    	     companyname = "";
    	     displayname = "";
    	     location = "";
    	     
    	     String sqlCode;
    	     boolean exists = false;
    	     
    	    // for(int j=0; j < aList.size(); j++)
    	     //{
               //System.out.println("attribute" + aList.get(j));    	    	 
    	     //}
    	     
    	     Lstname = aList.get(i).toString().substring(1,aList.get(i).toString().length()-1).replace("'","''").trim();    	     
    	     Fstname = aList.get(i+1).toString().substring(1,aList.get(i+1).toString().length()-1).replace("'","''").trim(); 
    	     Mddlinit = aList.get(i+2).toString().substring(1,aList.get(i+2).toString().length()-1);
    	     if(Mddlinit.length() > 0)
    	        Mddlinit = Mddlinit.substring(0,1);
    	       else
    	    	  Mddlinit = "";
    	     
    	     if(Mddlinit.length() != 0){
                 sqlCode = "SELECT * " +
                 "FROM " + SCHEMAPATH + ".PERSON_IN p " +
                 "WHERE p.lastname = '" + Lstname + "' and p.firstname = '" + Fstname + "' and p.middleinitial = '" + Mddlinit +"'";
                 //System.out.println("sqlCode is "+ sqlCode + i);
     	         }
    	      else if(Fstname.length() != 0){
                sqlCode = "SELECT * " +
                "FROM " + SCHEMAPATH + ".PERSON_IN p " +
                "WHERE p.lastname = '" + Lstname + "' and p.firstname = '" + Fstname +"'";
                //System.out.println("sqlCode is "+ sqlCode + i);
    	         }
    	       else {
                   sqlCode = "SELECT * " +
                   "FROM " + SCHEMAPATH + ".PERSON_IN p " +
                   "WHERE p.lastname = '" + Lstname + "'";
                   //System.out.println("sqlCode is "+ sqlCode + i);   	    	 
    	             }
            
    	    DbConn myConn2 = new DbConn("CSI");
    	     
            stmt = myConn2.conn.createStatement();
            rset = stmt.executeQuery (sqlCode);
            while (rset.next()) {
             exists = true;
            }
            rset.close();
                    
            if(!exists){
            	
             companyname = aList.get(i+3).toString().substring(1,aList.get(i+3).toString().length()-1).replace("'","''");
             displayname = aList.get(i+4).toString().split(",")[0].substring(1,aList.get(i+4).toString().split(",")[0].length()).replace("'","''");
             location = aList.get(i+5).toString().substring(1,aList.get(i+5).toString().length()-1).replace("'","''");
       	     if(Lstname.length() != 0 && Fstname.length() < 40 && Lstname.length() < 40 && companyname.length() < 50 && displayname.length() < 50
       	    		 && location.length() < 100){
       	    	 
       	    	 int a, b, c;
                 myUNID = new UNID();
                 myUNID.create("person");
                 personID = myUNID.getID();
                 myUNID.save(myConn2.conn);
                                  
    	        if(Fstname.length() != 0 && Mddlinit.length() != 0){
    	        
                 for(a = 0; a < aList2.size(); a+=3){
                		LstnameLDAP = aList2.get(a).toString();
                		FstnameMLDAP = aList2.get(a+1).toString();
                		usernameLDAP = aList2.get(a+2).toString();
                		if(LstnameLDAP.equalsIgnoreCase(Lstname) && FstnameMLDAP.equalsIgnoreCase(Fstname+Mddlinit))
                			break;
                	}
                if(a == aList2.size())
                	usernameLDAP = "";
               
                sqlCode = "INSERT INTO " + SCHEMAPATH + ".PERSON_IN (USERNAME, LASTNAME, FIRSTNAME, MIDDLEINITIAL, COMPANYNAME," +
                		  "DISPLAYMAILADDRESS, DISPLAYNAME, LOCATION, OFFICEPHONENUMBER, MYID)" +
                "VALUES ('" + usernameLDAP + "','" + Lstname + "','" + Fstname + "','" + Mddlinit + "','" + companyname +
                "','" + Fstname + "_"  + Mddlinit + "_" + Lstname + "@ymp.gov" + "','" + displayname + "','" 
                + location + "','" + aList.get(i+6).toString().substring(1,aList.get(i+6).toString().length()-1).replace("'","''") + "','"
                + personID + "')";

                System.out.println("sqlCode is "+ sqlCode);
    	         }
    	         else if(Fstname.length() != 0){
    	             for(b = 0; b < aList2.size(); b+=3){
    	            		LstnameLDAP = aList2.get(b).toString();
    	            		FstnameMLDAP = aList2.get(b+1).toString();
    	            		usernameLDAP = aList2.get(b+2).toString();
    	            		if(LstnameLDAP.equalsIgnoreCase(Lstname) && FstnameMLDAP.equalsIgnoreCase(Fstname))
    	            			break;
    	                }
    	             
    	             if(b == aList2.size())
    	                usernameLDAP = ""; 
    	             
                 sqlCode = "INSERT INTO " + SCHEMAPATH + ".PERSON_IN (USERNAME, LASTNAME, FIRSTNAME, COMPANYNAME," +
                 "DISPLAYMAILADDRESS, DISPLAYNAME, LOCATION, OFFICEPHONENUMBER, MYID)" +
                 "VALUES ('" + usernameLDAP + "','" + Lstname + "','" + Fstname + "','" + companyname +
                 "','" + Fstname + "_"  + Lstname + "@ymp.gov" + "','" + displayname + "','" 
                 + location + "','" + aList.get(i+6).toString().substring(1,aList.get(i+6).toString().length()-1).replace("'","''") + "','"
                 + personID + "')";
                
                 System.out.println("sqlCode is "+ sqlCode);	
                }
    	         else{
                    for(c = 0; c < aList2.size(); c+=3){
                    		LstnameLDAP = aList2.get(c).toString();
                    		FstnameMLDAP = aList2.get(c+1).toString();
                    		usernameLDAP = aList2.get(c+2).toString();
                    		if(LstnameLDAP.equalsIgnoreCase(Lstname))
                    			break;
                      }
                    
                    if(c == aList2.size())
                    	usernameLDAP = "";
                    
                     sqlCode = "INSERT INTO " + SCHEMAPATH + ".PERSON_IN (USERNAME, LASTNAME, FIRSTNAME, COMPANYNAME," +
                     "DISPLAYMAILADDRESS, DISPLAYNAME, LOCATION, OFFICEPHONENUMBER, MYID)" +
                     "VALUES ('" + usernameLDAP + "','" + Lstname + "',' ','" + companyname +
                     "','" + Lstname + "@ymp.gov" + "','" + displayname + "','" 
                     + location +
                     "','" + aList.get(i+6).toString().substring(1,aList.get(i+6).toString().length()-1).replace("'","''") + "','"
                     + personID + "')";
                     
                     System.out.println("sqlCode is "+ sqlCode); 
    	         }
    	        
            pstmt = myConn2.conn.prepareStatement(sqlCode);
            int rows = pstmt.executeUpdate();
       	     }
       	     System.out.println("usernameLDAP is:" + usernameLDAP);
            }
            myConn2.release();    
        }
       }
        catch (Exception e){
        	System.out.println(e + e.getMessage());
        }
        finally {
            if (rset != null)
                try { rset.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
		return aList;
    }
    
    /**
     * Add location to the db
     *
     * @param myConn     A DB connection handle (DbConn)
     */
 /*    public void addLocation (DbConn myConn) {
         addLocation(myConn, (long) 0);
     }*/

     /**
     * Add location to the db
     *
     * @param myConn     A DB connection handle (DbConn)
     * @param userID     ID of person making change
     */
     /*public void addLocation(DbConn myConn, long userID) {
         ResultSet rs = null;
         Statement stmt = null;
         PreparedStatement pstmt = null;
         
         try {
             UNID myUNID = new UNID();
             myUNID.create("location");
             locationID = myUNID.getID();
             myUNID.save(myConn.conn);
             
             String sqlcode = "INSERT INTO " + SCHEMAPATH + ".LOCATION (id, name, address, city, state, zip, country, postalcode) " +
                 "VALUES (" + locationID + ", '" + ((locationName != null) ? locationName : "")+ "', '" +
                 ((locationAddress != null) ? locationAddress : "") + "', '" +
                 ((locationCity != null) ? locationCity : "") + "', '" +
                 ((locationState != null) ? locationState : "") + "', '" +
                 ((locationZIP != null) ? locationZIP : "") + "', '" +
                 ((locationCountry != null) ? locationCountry : "") + "', '" +
                 ((locationPostalCode != null) ? locationPostalCode : "") + "')";
// System.out.println(sqlcode);
             pstmt = myConn.conn.prepareStatement(sqlcode);
             int rows = pstmt.executeUpdate();
             ALog.logActivity(myConn, ((userID != 0) ? userID : 0), "csi", 1, "Location '" + locationID + "' automatically added");
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
     }*/
}
