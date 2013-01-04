package gov.ymp.csi.users;

import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import java.util.*;

/**
* Location is the class for locations information in CSI.
* Locations are addresses of organizations, employers, training providers, etc.
* 
*
* @author   Anna Naydenova 
*/
public class Location {
    private UNID locationUNID = null;
    private long locationID = 0;
    private String locationName = null;
    private String locationAddress = null;
    private String locationCity = null;
    private String locationState = null;
    private long locationZIP = 0;
    private String locationCountry = null;
    private String locationPostalCode = null;
    private static String SCHEMA = null;
    private static String SCHEMAPATH = null;
    private static String DBTYPE = null;
    
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
    public Location () {
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
    public void getLocationInfo (DbConn myConn, long lID) {
        ResultSet rset = null;
        Statement stmt = null;
        try {        	
            String sqlCode = "SELECT l.id, l.name, l.address, l.city, l.state, l.zip, " +
            "l.country, l.postalcode " +
            "FROM " + SCHEMAPATH + ".location l, " +
            "WHERE l.id=" + lID";
            
            stmt = myConn.conn.createStatement();
            rset = stmt.executeQuery (sqlCode);
            while (rset.next()) {
            	locationID = rset.getLong(1);
            	locationName = rset.getString(2);
            	locationAddress = rset.getString(3);
            	locationCity = rset.getString(4);
            	locationState = rset.getInt(5);
                locationZIP = rset.getString(6);
                locationCountry = rset.getString(7);
                locationPostalCode = rset.getString(8);
            }
            rset.close();
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
     * Set Name
     *
     * @param value     The value to set Name to
     */
     public void setName(String value) {
          this.locationName = value;
     }

     /**
      * Set Address
      *
      * @param value     The value to set Address to
      */
      public void setAddress(String value) {
           this.locationAddress = value;
      }

      /**
       * Set City
       *
       * @param value     The value to set City to
       */
       public void setCity(String value) {
            this.locationCity = value;
       }
 
       /**
        * Set State
        *
        * @param value     The value to set State to
        */
        public void setState(String value) {
             this.locationState = value;
        }
        
      /**
       * Set Zip
       *
       * @param value     The value to set Zip to
       */
     public void setZip(String value) {
              this.locationZip = value;
      }
 
     /**
      * Set Country
      *
      * @param value     The value to set Country to
      */
    public void setCountry(String value) {
             this.locationCountry = value;
     }
    
    /**
     * Set Postal Code
     *
     * @param value     The value to set Postal Code to
     */
   public void setPostalCode(String value) {
            this.locationPostalCode = value;
    }
   
    /**
     * Add location to the db
     *
     * @param myConn     A DB connection handle (DbConn)
     */
     public void addLocation (DbConn myConn) {
         add(myConn, (long) 0);
     }

     /**
     * Add location to the db
     *
     * @param myConn     A DB connection handle (DbConn)
     * @param userID     ID of person making change
     */
     public void addLocation(DbConn myConn, long userID) {
         ResultSet rs = null;
         Statement stmt = null;
         PreparedStatement pstmt = null;
         
         try {
             UNID myUNID = new UNID();
             myUNID.create("location");
             locationID = myUNID.getID();
             myUNID.save(myConn.conn);
             
             String sqlcode = "INSERT INTO " + SCHEMAPATH + ".location (id, name, address, city, state, zip, country, postalcode) " +
                 "VALUES (" + locationID + ", '" + ((locationName != null) ? locationName : " ")+ "', '" +
                 ((locationAddress != null) ? locationAddress : " ") + "', '" +
                 ((locationCity != null) ? locationCity : " ") + "', '" +
                 ((locationState != null) ? locationState : " ") + "'," +
                 ((locationZip != null) ? locationZip : " ") + "', '" +
                 ((locationCountry != null) ? locationCountry : " ") + "'," +
                 ((locationPostalCode != null) ? locationPostalCode : " ") + ")";
// System.out.println(sqlcode);
             pstmt = myConn.conn.prepareStatement(sqlcode);
             int rows = pstmt.executeUpdate();
             ALog.logActivity(myConn, ((userID != 0) ? userID : "0"), "csi", 1, "Location '" + locationName + "' automatically added");
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
   * Create LOCATION table in db
   *
   * @param myConn     A DB connection handle (DbConn)
 */
      public void addLocationTable (DbConn myConn) {
          Statement stmt = null;
          PreparedStatement pstmt = null;
          
          try { 
              String sqlcode = "CREATE TABLE LOCATION (ID NUMBER(11)NOT NULL, NAME VARCHAR2(100) NOT NULL," +
              		           "ADDRESS VARCHAR2(200), CITY VARCHAR2(80), STATE CHAR(2), ZIP VARCHAR2(10)," +
              		           "COUNTRY VARCHAR2(40), POSTALCODE VARCHAR2(10), " +
              		           "CONSTRAINT LOCATION_PK PRIMARY KEY (ID)USING INDEX TABLESPACE &INDEX_TABLESPACE," +
              		           "CONSTRAINT LOCATION_ID_FK FOREIGN KEY (ID) REFERENCES UNID (ID))";
//  System.out.println(sqlcode);
              pstmt = myConn.conn.prepareStatement(sqlcode);
              pstmt.executeUpdate();
              ALog.logActivity(myConn, ((userID != 0) ? userID : "0"), "csi", 1, "Table LOCATION automatically added");
          }
          catch (java.sql.SQLException e) {
              System.out.println(e + e.getMessage());
          }
          finally {
              if (stmt != null)
                  try { stmt.close(); } catch (Exception i) {}
              if (pstmt != null)
                  try { pstmt.close(); } catch (Exception i) {}
          }
      }
}
