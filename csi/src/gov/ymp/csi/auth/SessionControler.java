package gov.ymp.csi.auth;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.auth.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.systems.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;


/**
* SessionControler is the base class for SessionControler's in the CSI
*
* @author   Bill Atchley
*/
public class SessionControler {
    private long person = 0;
    private String ID = null;
    private String tcpAddress = null;
    private java.util.Date firstUsed = null;
    private java.util.Date lastUsed = null;
    private java.util.Date closed = null;
    private int timeout = 15;
    private boolean mustChangePassword = false;
    private boolean isNew = true;

    //private static String SCHEMA = "csi";
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
    * creates a new empty SessionControler object
    */
    public SessionControler () {
        init();

    }

    /**
    * Creates a SessionControler object and retrieves it up from the db
    *
    * @param id     The id of the SessionControler to lookup from the db
    */
    public SessionControler (String id) {
        init();
        lookup (id);
    }

    /**
    * Creates a SessionControler object and retrieves it up from the db
    *
    * @param id       The id of the SessionControler to lookup from the db
    * @param myConn   A connection to the DB (DbConn)
    */
    public SessionControler (DbConn myConn, String id) {
        init();
        lookup (myConn, id);
    }

    /**
    * Retrieves a SessionControler from the db and stores it in the current SessionControler object
    *
    * @param id     The id of the SessionControler to lookup from the db
    */
    public void getInfo (String id) {
        lookup(id);
    }

    /**
    * Retrieves a SessionControler from the db and stores it in the current SessionControler object
    *
    * @param conn   A connection to the DB
    * @param id     The id of the SessionControler to lookup from the db
    */
    public void getInfo (DbConn myConn, String id) {
        lookup(myConn, id);
    }

    /**
    * Retrieves a SessionControler from the db and stores it in the current SessionControler object
    *
    * @param id     The id of the SessionControler to lookup from the db
    */
    public void lookup (String id) {
        String outLine = "";
        DbConn myConn = new DbConn(SCHEMA);
        try {

            lookup (myConn, id);

            myConn.release();
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
            myConn.release();
        }
    }

    /**
    * Retrieves a SessionControler from the db (given a passed in connection) and stores it in the current SessionControler object
    *
    * @param id     The id of the SessionControler to lookup from the db
    * @param conn   A connection to the DB
    */
    public void lookup (DbConn myConn, String id) {
        ID = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = myConn.conn;
        String sqlcode = "";
        String sqlFromWhere = "";
        int returnSize = 0;
        try {

            // Create a Statement
            stmt = conn.createStatement ();

            sqlcode = "SELECT person, id, tcpaddress, firstused, lastused, closed, timeout, changepass FROM " + SCHEMAPATH + ".sessions WHERE id='" + id + "'";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String myID = rs.getString(2);
//System.out.println("SessionControler - lookup - id: " + id + ", myID: " + myID);
            if (myID.equals(id)) {
                ID = myID;
                person = rs.getLong(1);
                tcpAddress = rs.getString(3);
                firstUsed = (java.util.Date) rs.getTimestamp(4);
                lastUsed = (java.util.Date) rs.getTimestamp(5);
                closed = (java.util.Date) rs.getTimestamp(6);
                timeout = rs.getInt(7);
                mustChangePassword = rs.getString(8).equals("T") ? true : false;
                isNew = false;
                rs.close();
            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }

    /**
    * Retrieve the unique id for the current SessionControler
    *
    * @return      The id for the current SessionControler
    */
    public String getID() {
        return(ID);
    }

    /**
    * Retrieve the person for the current SessionControler
    *
    * @return      The person for the current SessionControler
    */
    public long getPerson() {
        return(person);
    }

    /**
    * Retrieve the tcpAddress for the current SessionControler
    *
    * @return      The tcpAddress for the current SessionControler
    */
    public String getTcpAddress() {
        return(tcpAddress);
    }


    /**
    * Retrieve the firstUsed for the current SessionControler
    *
    * @return      The set of firstUsed for the current SessionControler
    */
    public java.util.Date getFirstUsed() {
        return(firstUsed);
    }

    /**
    * Retrieve the lastUsed for the current SessionControler
    *
    * @return      The set of lastUsed for the current SessionControler
    */
    public java.util.Date getLastUsed() {
        return(lastUsed);
    }

    /**
    * Retrieve the closed for the current SessionControler
    *
    * @return      The set of closed for the current SessionControler
    */
    public java.util.Date getClosed() {
        return(closed);
    }

    /**
    * Retrieve the timeout for the current SessionControler
    *
    * @return      The timeout for the current SessionControler
    */
    public int getTimeout() {
        return(timeout);
    }

    /**
    * Retrieve the change password flag
    *
    * @return      The mustChangePassword for the current SessionControler
    */
    public boolean isMustChangePassword() {
        return(mustChangePassword);
    }


    /**
    * Set the person for the current SessionControler
    *
    * @param value     The new person
    */
    public void setPerson(long value) {
        person = value;
    }


    /**
    * Set the tcpAddress for the current SessionControler
    *
    * @param value     The new tcpAddress
    */
    public void setTcpAddress(String value) {
        tcpAddress = value;
    }


    /**
    * Set closed for the current SessionControler
    *
    * @param value     The new closed date
    */
    public void setClosed(java.util.Date value) {
        closed = value;
    }

    /**
    * Set the timeout for the current SessionControler
    *
    * @param value     The new timeout
    */
    public void setTimeout(int value) {
        timeout = value;
    }

    /**
    * Set mustChangePassword flag for the current SessionControler
    *
    * @param value     The new value of the mustChangePassword flag
    */
    public void setMustChangePassword(boolean value) {
        mustChangePassword = value;
    }


    /**
    * Add SessionControler to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    */
    public void add(DbConn myConn) {
        add (myConn, person);
    }


    /**
    * Add SessionControler to the db
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param userid     ID of user performing function
    */
    public void add(DbConn myConn, long userid) {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            stmt = myConn.conn.createStatement();
            while (ID == null) {
                String myID = Utils.genRandID(60);
                rs = stmt.executeQuery("SELECT id FROM " + SCHEMAPATH + ".sessions WHERE id='" + myID + "'");
                if (!rs.next()) {
                    ID = myID;
                }
            }
            String sqlcode = "";
            sqlcode = "INSERT INTO " + SCHEMAPATH + ".sessions (person, id, tcpaddress, firstused, lastused, closed, timeout, changepass ) " +
                "VALUES (" + person + ", '" + ID + "', '" + tcpAddress + "', SYSDATE, SYSDATE, null, " + timeout + ", '" + ((mustChangePassword) ? "T" : "F") + "')";
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            int rows = pstmt.executeUpdate();
            ALog.logActivity(myConn, userid, "csi", 1, "SessionControler  added");
            isNew = false;

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
    * Save the current SessionControler to the DB
    *
    * @param id     The id of the SessionControler to lookup from the db
    */
    public void save(DbConn myConn) {
        save (myConn, person);
    }


    /**
    * Save the current SessionControler to the DB
    *
    * @param myConn     A DB connection handle (DbConn)
    * @param userid     ID of user performing function
    */
    public void save(DbConn myConn, long userid) {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            stmt = myConn.conn.createStatement();
            String sqlcode = "";
            sqlcode = "UPDATE " + SCHEMAPATH + ".sessions SET " +
                "lastused=SYSDATE, closed = " + ((closed != null) ? "?" : "NULL") + ", changepass='" + ((mustChangePassword) ? "T" : "F") + "' " +
                "WHERE id='" + ID + "'";
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            if (closed !=null) {pstmt.setTime(1, new java.sql.Time(closed.getTime()));}
            int rows = pstmt.executeUpdate();
            ALog.logActivity(myConn, userid, "csi", 2, "SessionControler updated");

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
    * Load the current session from the DB
    *
    * @param request     The http request from the calling servlet/jsp
    */
    public static void loader(HttpServletRequest request) {
        try {
          //HttpSession session = pageContext.getSession();
          //HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
          HttpSession session = request.getSession();
          if (session.getAttribute("user.name")  == null || ((String) session.getAttribute("user.name")).toLowerCase().equals("guest")) {
              InitialContext myInitCtx = new InitialContext();
              String productionStatus = (String) myInitCtx.lookup("java:comp/env/ProductionStatus");
              Cookie cookie = null;
              //Get an array of Cookies associated with this domain
              Cookie[] cookies = request.getCookies( );
              String cookieName = "csiSession-" + productionStatus;
              if (cookies != null){
                  for (int i = 0; i < cookies.length; i++){
                      if (cookies[i].getName( ).equals(cookieName)){
                          cookie = cookies[i];
                      }
                  }//end for
              }//end if
              if (cookie != null) {
                  DbConn myConn = null;
                  try {
                      myConn = new DbConn();
                  } catch (Exception e) { myConn = null; }
                  if (myConn != null) {
                      String sessionID = cookie.getValue( );
                      SessionControler sess = new SessionControler(myConn, sessionID);
                      if ((!sess.getTcpAddress().equals(request.getRemoteAddr())) ||
                              (System.currentTimeMillis() > (sess.getLastUsed().getTime() + (sess.getTimeout() * 60*60*1000))) ||
                              (sess.getClosed() != null)) {
                          if (sess.getClosed() == null) {
                              sess.setClosed(new java.util.Date());
                              sess.save(myConn);
                          }
                      } else {
                          Person user = new Person();
                          user.getInfo(myConn, sess.getPerson());
                          Position pos = new Position(user.getID(), myConn);
                          HashMap perm = Permission.getPermissionMap(myConn);
                          if (sess.mustChangePassword) {
                              session.setAttribute("tmp.user.name", user.getUserName());
                              session.setAttribute("tmp.user.id", Long.toString(user.getID()));
                              session.setAttribute("tmp.user.person", user);
                              session.setAttribute("tmp.user.domain", user.getDomain().getName());
                              session.setAttribute("tmp.user.isexpired", "T");
                          } else {
                              session.setAttribute("user.id", Long.toString(user.getID()));
                              session.setAttribute("user.name", user.getUserName());
                              session.setAttribute("user.fullname", user.getFirstName() + ' ' + user.getLastName());
                              session.setAttribute("user.position", pos);
                              session.setAttribute("user.positionid", Long.toString(pos.getID()));
                              session.setAttribute("user.person", user);
                              session.setAttribute("user.permissionmap", perm);
                              session.setAttribute("user.authenticationlevel", "2");
                              session.setAttribute("user.islocaldomain", ((user.getDomain().isLocal()) ? "T" : "F"));
                              sess.save(myConn);
                          }
                      }
                  }
              }
          }
        }
        catch (Exception e) {
            System.out.println("SessionTest-" + e + " " + e.getMessage());
        }
    }


}
