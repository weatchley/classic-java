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


/**
* NAB is the class for Lotus Notes Name & Address Book processing in the CSI
*
* @author   Bill Atchley
*/
public class NAB {
//    //public static String SCHEMA = "csi.csi";
    //DbConn tempDB = new DbConn("dummy");
    //private String SCHEMA = tempDB.getSchemaName();
    //private String SCHEMAPATH = tempDB.getSchemaPath();

    /**
    * Creates a new NAB object
    *
    */
    public NAB () {
        //
    }



/**
*
*/
    private static String getSection(String uServer, int sSection) {
         String sOutput = "";
         String[] saOutput = uServer.split("\\.");
         sOutput = saOutput[sSection];
         return sOutput;
    }



/**
*
*/
    public static boolean processNAB(DirContext ctx, String server, String domainCode, int domainID, DbConn myConn) {
        boolean status = false;
        //System.out.println("NAB.processNAB called!");
        try {
            Statement stmt = myConn.conn.createStatement();
            PreparedStatement pstmt = null;
            ResultSet rs;
            String sqlcode = "SELECT firstname, middleinitial, lastname, officephonenumber, displaymailaddress, myid FROM " +
                myConn.getSchemaPath() +".person_in WHERE username IS NULL AND INSTR(displayname, '" + domainCode + "/')>1";
            		//SCHEMAPATH +".person_in WHERE username IS NULL AND CHARINDEX('" + domainCode + "/', displayname)>1";
            		//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            while (rs.next()) {
                String firstName = rs.getString(1);
                String lastName = rs.getString(3);
                String phone = rs.getString(4);
                String email = rs.getString(5);
                long myID = rs.getLong(6);
                String uName = null;

                NamingEnumeration answer;
                SearchControls ctls = new SearchControls();
                ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                //String filter = "(cn="+username+")";
                String filter = "(&(sn=" + lastName + ")(givenName=" + firstName + "))";
                //String filter = "(sAMAccountName="+username+")";
                try {
                    answer = ctx.search("DC="+getSection(server,0)+",DC="+getSection(server,1)+",DC="+getSection(server,2), filter, ctls);


                    SearchResult  srchresult = (SearchResult) answer.next();
                    String dn = srchresult.getName();
                    //System.out.println("==========================================\n -- dn= " + dn);
                    Attributes attrs = srchresult.getAttributes();
                    NamingEnumeration ne = attrs.getAll();
                    while (ne.hasMoreElements()) {
                        Attribute attr = (Attribute) ne.next();
                        String attrname = attr.getID() + ": ";
                        Enumeration values = attr.getAll();
                        while ( values.hasMoreElements()) {
                            String value = (String) values.nextElement();
                            //System.out.println(attrname + "=" + value);
                            if (attrname.equals("sAMAccountName: ")) {
                                uName = (String) value;
                            } else {
                                // //values.nextElement();
                            }
                        }
                    }
                    if (uName!=null) {
                        boolean hadChanges = false;
                        Person myPerson = new Person(uName);
                        myPerson.setUserName(uName);
                        myPerson.setDomainID(domainID);
                        myPerson.getInfo(myConn);
                        if (!myPerson.getLastName().equals(lastName) || !myPerson.getFirstName().equals(firstName) ||
                            !myPerson.getPhone().equals(phone) || !myPerson.getEmail().equals(email)) {
                                hadChanges = true;
                        }
                        myPerson.setLastName(lastName);
                        myPerson.setFirstName(firstName);
                        myPerson.setPhone(phone);
                        myPerson.setEmail(email);
                        if (myPerson.getID() == 0) {
                            myPerson.add(myConn);
                            myPerson.getInfo(myConn);
                        } else if (hadChanges) {
                            myPerson.save(myConn);
                        }
                        
                        myConn.conn.commit();
                        PreparedStatement pstmt2 = null;
                        String sqlcode2 = "UPDATE " + myConn.getSchemaPath() + ".person_in SET username='" + uName + "' WHERE myid=" + myID;
                        //System.out.println(sqlcode2);
                        pstmt2 = myConn.conn.prepareStatement(sqlcode2);
                        int rows = pstmt2.executeUpdate();
                        pstmt.close();
                    }

                }
                catch (NamingException e) {
                    //System.out.println(e + e.getMessage());
                }
                catch (NullPointerException e) {
                    //System.out.println(e + e.getMessage());
                }
                catch (Exception e) {
                    //System.out.println(e + e.getMessage());
                }

            }
            rs.close();

        }
        catch (java.sql.SQLException e) {
            //System.out.println(e + e.getMessage());
        }

        return status;
    }



}
