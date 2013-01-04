package gov.ymp.hvr;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import javax.servlet.http.*;
// Support classes
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.awt.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.db.ALog.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.auth.*;
import java.text.DateFormat;
import java.util.Date;
import java.lang.String.*;
import javax.naming.*;
import org.apache.commons.lang.StringEscapeUtils; 


public class Hvr extends HttpServlet {

    DbConn tmpConn = new DbConn("dummy");
        private String SCHEMA = tmpConn.getSchemaName();
    private String SCHEMAPATH = tmpConn.getSchemaPath();
    public boolean added;

    // Handle the GET HTTP Method
    public void doGet(HttpServletRequest request,
              HttpServletResponse response)
       throws IOException {

      String message = "";
      try {
          message = processRequest(request,response);
          //if (message.length() > 0) {
          //    generateResponse(message, response);
          //}
      }
      finally {
      }
    }

    // Handle the POST HTTP Method
    public void doPost(HttpServletRequest request,
               HttpServletResponse response)
        throws ServletException, IOException {

         String message = "";
         String page = request.getParameter("page");
                response.setContentType ("text/html");
                PrintWriter out = response.getWriter ();
         try {
             if (page == null || page.equals ("2")) {
                 message = processRequest(request,response);
                 return;
             }
             else if (page.equals ("1")) {
                 addRequest (request, response);
                 return;
             }
             else if (page.equals ("3")) {
                 updateRequest (request, response);
             }


          //if (message.length() > 0) {
          //    generateResponse(message, response);
          //}
         }
         finally {
         }
    }


    // Process the request
    private String processRequest(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("username");
        String un = userName;
        String password = request.getParameter("pass");
        String pass = password;
        String domain = request.getParameter("domain");
        String userID = (request.getParameter("UserID") != null) ? request.getParameter("UserID") : "0";
        String Userid = "";
        long userIDLong = Long.parseLong(userID);
        String loginCheck = request.getParameter("logincheck");
        String authenticated = request.getParameter("authenticated");
        //String visitorCount = (request.getParameter("visitorcount") != null) ? request.getParameter("visitorcount") : "1";
        HttpSession session = request.getSession();
        boolean validated = false;

        String outLine = "";
        OutputStream toClient;
        added = false;

        try {
//System.out.println("Hvr.java processRequest - Got Here - 1");
            Context initCtx = new InitialContext();
            //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
            DbConn myConn = new DbConn();
            String csiSchema = myConn.getSchemaPath();
            String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
            //if (command.equals("login")) {
                Cookie cookie = null;
                //Get an array of Cookies associated with this domain
                Cookie[] cookies = request.getCookies( );
                String productionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
                String cookieName = "csiSession-" + productionStatus;
                if (cookies != null){
                    for (int i = 0; i < cookies.length; i++){
                        if (cookies[i].getName( ).equals(cookieName)){
                            cookie = cookies[i];
                        }
                    }//end for
                }//end if
                Person user = new Person(un.toLowerCase());
                validated = user.authenticate(pass, domain, acronym, myConn);
                if (validated) {
                    Position pos = new Position(user.getID(), myConn);
                    HashMap perm = Permission.getPermissionMap(myConn);
                    session.setAttribute("user.id", Long.toString(user.getID()));
                    session.setAttribute("user.name", user.getUserName());
                    session.setAttribute("user.fullname", user.getFirstName() + ' ' + user.getLastName());
                    session.setAttribute("user.position", pos);
                    session.setAttribute("user.positionid", Long.toString(pos.getID()));
                    session.setAttribute("user.person", user);
                    session.setAttribute("user.permissionmap", perm);
                    session.setAttribute("user.authenticationlevel", "2");
                    SessionControler sess = new SessionControler();
                    sess.setPerson(user.getID());
                    sess.setTcpAddress(request.getRemoteAddr());
                    sess.add(myConn, user.getID());
                    cookie = new Cookie(cookieName, "" + sess.getID());
                    cookie.setPath("/");
                    cookie.setMaxAge(-1);
                    response.addCookie(cookie);
                    ALog.logActivity(user.getID(), acronym, 4,"User " + user.getFirstName() + ' ' + user.getLastName() + " logged in.");
                    outLine = "<script language=\"javascript\">\n<!--\n    document.location = 'hvr_enterrequest.jsp';\n-->\n</script>\n";


                } else {
                    ALog.logError(0, acronym, 4,"Failed login attempt on username " + un);
                    outLine = "<script language=\"javascript\">\n<!--\n    alert('Failed login attempt on username " + un + "');\n    document.location = 'login.jsp';\n-->\n</script>\n";
                }

            //} else if (command.equals("test")) {
            //    outLine = "test";
            //}

            generateResponse(outLine, response);

            myConn.release();
        }
        catch (IllegalArgumentException e) {
            outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
            log(outLine);
            ALog.logError(userIDLong, "HVR", 0, "HVR error: " + outLine);
        }
        catch (NullPointerException e) {
            outLine = outLine + "NullPointerException caught: " + e.getMessage();
            log(outLine);
            ALog.logError(userIDLong, "HVR", 0, "HVR error: " + outLine);
        }
        catch (IOException e) {
            outLine = outLine + "IOException caught: " + e.getMessage();
            log(outLine);
            ALog.logError(userIDLong, "HVR", 0, "HVR error: " + outLine);
        }
//        catch (SQLException e) {
//            outLine = outLine + "SQLException caught: " + e.getMessage();
//            log(outLine);
//            ALog.logError(userIDLong, "HVR", 0, "HVR error: " + outLine);
//        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            log(outLine);
            ALog.logError(userIDLong, "HVR", 0, "HVR error: " + outLine);
        }
        finally {
            //log("Test log message\n");
        }
        return outLine;
    }

    /* *******************************************************
       Adds record to database and prints thank you message
    ******************************************************* */
    private void addRequest (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userID = (request.getParameter("UserID") != null) ? request.getParameter("UserID") : "0";
        long userIDLong = Long.parseLong(userID);
        String userName = request.getParameter("username");
        Person requester = new Person (userName);
        String requesterFirst = "";
        String requesterLast = "";
        String requesterSecondaryContact = request.getParameter ("rsecondary");
        String requesterPhone = request.getParameter ("rphone");
        String visitorfirstname1 = request.getParameter("visitorfirstname1");
        String visitorlastname1 = request.getParameter("visitorlastname1");
        String visitorname1 = visitorfirstname1 + " " + visitorlastname1;
        String visitoraffiliation1 = request.getParameter ("visitoraffiliation1");
        String otherchecked1;
        String destination1;
        String iscitizen1 = request.getParameter ("iscitizen1");
        String escortorproxy1 = request.getParameter ("escortorproxy1");
        String authenticated = request.getParameter ("authenticated");
        String purpose = request.getParameter("purpose");
        String startdate = request.getParameter("smonth") + "/" + request.getParameter("sday") + "/" + request.getParameter ("syear");
        String enddate = request.getParameter("emonth") + "/" + request.getParameter("eday") + "/" + request.getParameter ("eyear");
        String comment = request.getParameter("comment");

        added = false;


        long requestID = 0;
        String personVisit = "";
        String checkAffiliation = "select id from csi.employer where name = 'visitoraffiliation1' or acronym = 'visitoraffiliation1";
        String addAffiliation = "";
        String visitorName = visitorlastname1 + visitorfirstname1.charAt(0);
        Person visitor1 = new Person (visitorName);
        String addperson = "";
        String sqlcode = "";
        String personvisitsql = "";
        String visitor1ID = "0";
        long visitor1IDLong = 0;

        String howmany = request.getParameter ("howmany");
        //String howmany = request.getParameter ("visitorcount");
        int inthowmany = 0;
        String printPeople = "";

        response.setContentType ("text/html");
        PrintWriter out = response.getWriter ();
        //out.println ("<input type=hidden name=page value=2>\n");

//System.out.println("Hvr.java addRequest - Got Here - 1, userName: " + userName + ", userIDLong: " + userIDLong + ", visitorname1: " + visitorname1 + ", added: " + added);
        if(purpose.length() < 1000 && comment.length() < 1000)
        {
          try {
            DbConn myConn = new DbConn("CSI");

            Statement stmt = myConn.conn.createStatement();
            PreparedStatement pstmt = null;
            ResultSet rs;
            PreparedStatement pstmtperson = null;
            ResultSet rs2;
            PreparedStatement pstmtpv = null;
            ResultSet rs3;

            inthowmany = Integer.parseInt(howmany);

            UNID myUNID = new UNID();
            myUNID.create("visitrequest");
            requestID = myUNID.getID();
            myUNID.save(myConn.conn);

            if(!added)
            {
             added = true;
             sqlcode = "INSERT INTO " + SCHEMAPATH + ".hvr_visitrequest (id, requestdate, purpose, " +
                       "startdate, enddate, requester, secondarycontact, comments) " +
                       "VALUES (" + requestID + ", sysdate, '" + StringEscapeUtils.escapeSql(purpose) + "', to_date('" +
                       startdate + "','MM/DD/YYYY'), to_date('" + enddate + "','MM/DD/YYYY'), " + userID + ", " +
                       ((requesterSecondaryContact != null) ? "'" + requesterSecondaryContact + "'" : "NULL") + ", " +
                       ((comment != null) ? "'" + StringEscapeUtils.escapeSql(comment) + "'" : "NULL") + ")";
             //out.println("Insert visit record: " + sqlcode + "<br>\n");
             pstmt = myConn.conn.prepareStatement(sqlcode);
             int rows = pstmt.executeUpdate();

	             //if(requesterPhone!= null){
	             String sqlupdatestr;
	             sqlupdatestr = "UPDATE csi.person SET phone = '"+requesterPhone+"' where id = "+userID+"";
	             PreparedStatement pstmt2 = null;
	             pstmt2 = myConn.conn.prepareStatement(sqlupdatestr);
	             int rowsupdate = pstmt2.executeUpdate();
	             //}

             for (int i = 1; i <= inthowmany; i++) {
                 String istr = Integer.toString (i);

                 visitorfirstname1 = (request.getParameter("visitorfirstname" + istr) != null) ? request.getParameter("visitorfirstname" + istr) : "";
                 visitorlastname1 = (request.getParameter("visitorlastname" + istr) != null) ? request.getParameter("visitorlastname" + istr) : "";
                 visitorname1 = visitorfirstname1 + " " + visitorlastname1;

                 //out.println ("Visitor Name: " + visitorname1 + "<br>\n");
                 visitoraffiliation1 = StringEscapeUtils.escapeSql(request.getParameter ("visitoraffiliation" + istr));
                 otherchecked1 = request.getParameter ("otherchecked" + istr);
                 if(otherchecked1.length() > 0)
                	  destination1 = request.getParameter ("destinationother" + istr);
                    else
                        destination1 = request.getParameter ("destination" + istr);

                 iscitizen1 = request.getParameter ("iscitizen" + istr);
                 escortorproxy1 = request.getParameter ("escortorproxy" + istr);
                 visitorName = visitorlastname1 + visitorfirstname1.charAt(0);
                 visitor1 = new Person (visitorName);

                 String Userid ="";
                 String userName2 = userName.toLowerCase();
                 String realID = "select id from csi.person WHERE LOWER(username) LIKE '" + userName2 + "'";
                 Statement realIDstmt = myConn.conn.createStatement();
                 ResultSet realIDrset = realIDstmt.executeQuery (realID);
                 while(realIDrset.next()) {
                     Userid = realIDrset.getString(1);
                 }
                 realIDrset.close();
                 long userIDLong2 = Long.parseLong(Userid);
                 visitor1.getInfo(myConn, userIDLong2);
                 visitor1IDLong = visitor1.getID();

                 //out.println ("Possible visitor ID: " + visitor1IDLong + "<br>\n");

                 long realVisitorID = 0;
                 if (visitor1IDLong != 0) {
                     String realVIDQ = "select id from csi.person where firstname = '" + StringEscapeUtils.escapeSql(visitorfirstname1) + "' and lastname = '" + StringEscapeUtils.escapeSql(visitorlastname1) + "' and domainid <> 1";
                     Statement realVIDstmt = myConn.conn.createStatement();
                     ResultSet realVIDrset = realVIDstmt.executeQuery (realVIDQ);
                     if (realVIDrset.next()) {
                         realVisitorID = realVIDrset.getLong(1);
                     }
                     realVIDrset.close();
                 }
                 //out.println("The true return visitor's ID is: " + realVisitorID + "<br>\n");
                 //if (visitor1IDLong == 0 || (visitor1.getFirstName() != visitorfirstname1)) {

                 if (realVisitorID == 0) {
                     UNID visitor1UNID = new UNID();
                     visitor1UNID.create("person");
                     realVisitorID = visitor1UNID.getID();
                     //visitor1IDLong = visitor1UNID.getID();
                     visitor1UNID.save(myConn.conn);
                     addperson = "insert into csi.person (id, username, firstname, lastname, email, domainid) values (" + realVisitorID +", '" + StringEscapeUtils.escapeSql(visitorName) + "', '" + StringEscapeUtils.escapeSql(visitorfirstname1) + "', '" + StringEscapeUtils.escapeSql(visitorlastname1) + "', 'nobody@nobody.com', 0)";
                     //addperson = "insert into csi.person (id, username, firstname, lastname, email, domainid) values (" + visitor1IDLong +", '" + visitorName + "', '" + visitorfirstname1 + "', '" + visitorlastname1 + "', 'nobody@nobody.com', 0)";
                     //out.println("Add person: " + addperson + "\n");
                     pstmtperson = myConn.conn.prepareStatement(addperson);
                     int rows1 = pstmtperson.executeUpdate();
                 }

                 personvisitsql = "insert into csi.hvr_personvisit (visitrequestid, personid, iscitizen, escortorproxy, affiliation, destination) values (" + requestID + ", " + realVisitorID + ", " + ((iscitizen1 != null) ? "'" + iscitizen1 + "'" : "'F'") + ", '" + escortorproxy1 + "', '" + visitoraffiliation1 + "', '" + destination1+ "')";
                 //personvisitsql = "insert into csi.hvr_personvisit (visitrequestid, personid, iscitizen, escortorproxy, affiliation) values (" + requestID + ", " + visitor1IDLong + ", " + ((iscitizen1 != null) ? "'" + iscitizen1 + "'" : "'F'") + ", '" + escortorproxy1 + "', '" + visitoraffiliation1 + "')";
                 //out.println ("Person visit record: " + personvisitsql + "\n");
                 pstmtpv = myConn.conn.prepareStatement(personvisitsql);
                 int rows3 = pstmtpv.executeUpdate();

                 printPeople += "<tr><td colspan=2>Visitor" + istr + "'s Full Name: &nbsp;&nbsp;<b>" + visitorfirstname1 + " " + visitorlastname1 + "</b>";
                 printPeople += "<ul><li>Affiliation: &nbsp;&nbsp;" + visitoraffiliation1 + "";
                 printPeople += "<li>Visitor Requires Escort or Visitor Badge: &nbsp;&nbsp;" + ((escortorproxy1.equals("0")) ? "Escort" : "Visitor Badge") + "";
                 printPeople += "<li>Visitor " + istr + " Is a US Citizen: &nbsp;&nbsp;" + ((iscitizen1 != null) ? "Yes" : "No") + "</ul></td></tr>";
             } // **** end for loop ****
            }
            //String Userid ="";
            //String userName2 = userName.toLowerCase();
            //String realID = "select id from csi.person WHERE LOWER(username) LIKE '" + userName2 + "'";
            //Statement realIDstmt = myConn.conn.createStatement();
            //ResultSet realIDrset = realIDstmt.executeQuery (realID);
            //      while(realIDrset.next()) {
            //        Userid = realIDrset.getString(1);
            //       }
            //    realIDrset.close();
            //long userIDLong2 = Long.parseLong(Userid);
            //requester.getInfo(myConn, userIDLong2);
            requester.getInfo(myConn, userIDLong);

            requesterFirst = requester.getFirstName();
            requesterLast = requester.getLastName();
            ALog.logActivity(requester.getID(), "HVR", 1, "HVR: Visitor request "+ requestID +" submitted");
            myConn.release();
          }
          catch (java.sql.SQLException e) {
              System.out.println(e + e.getMessage());
              ALog.logError(userIDLong, "HVR", 0, "HVR error: " + e + e.getMessage());
          }
          catch (java.lang.NumberFormatException e) {
              ALog.logError(userIDLong, "HVR", 0, "HVR error: " + e + e.getMessage());
          }


          out.println ("<html>\n<head>\n<title>Hillshire Visitor Requests Database - Thank you for your submission!</title>\n");
          out.println ("<script src=js/hvr.js></script>\n");
          out.println ("<script language=javascript>header('Thank you for your submission!');</script>\n");
          out.println ("<rdc:notProductionWarning size=\"150%\" width=\"180\" />\n");
          out.println ("<rdc:sessionTest />\n");
          out.println ("<form method=post name=add action='hvr_enterrequest.jsp'>\n");
          out.println ("<input type=hidden name=page value=2>\n");
          out.println ("<input type=hidden name=username value=" + userName + ">\n");
          out.println ("<input type=hidden name=UserID value=" + userID + ">\n");
          out.println ("<input type=hidden name=authenticated value=" + authenticated + ">\n");
          out.println ("<tr align=right><td><a href=javascript:document.add.submit()>Enter Another Request</a></td></tr></table>\n<br>\n");
          out.println ("<table align=center><tr><td colspan=2><b>Thank you, " + requesterFirst + " " + requesterLast + ", for submitting the following request: </td></tr><tr><td colspan=2>&nbsp;</td></tr>");

          out.println (printPeople);
          out.println ("<tr><td>Visit Start Date: </td><td>" + startdate + "</td></tr>");
          out.println ("<tr><td>Visit End Date:</td><td> " + enddate + "</td></tr>");

          out.println ("<tr><td>Purpose of Visit: </td><td>" + purpose + "</td></tr>");
          out.println ("<tr><td>Comments: </td><td>" + comment + "</td></tr>");
          out.println ("</form>");
          out.println ("<rdc:envDisplay />");
          out.println ("<script language=javascript>footer();</script>\n");
        }
    }

    /* ****************************************************
       Updates visit record and redirects back to report
    **************************************************** */
    private void updateRequest (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userID = (request.getParameter("UserID") != null) ? request.getParameter("UserID") : "0";
        long userIDLong = Long.parseLong(userID);
        String userName = request.getParameter("username");
        String authenticated = request.getParameter ("authenticated");
        String visitorbadgenum = request.getParameter("badgenumber1");
        String visitordestination = request.getParameter("destination1");
        String comment = request.getParameter("comment");

        String requestID = request.getParameter("requestid");

        String howmany = request.getParameter ("howmany");
        int inthowmany = 0;
        String printPeople = "";


        response.setContentType ("text/html");
        PrintWriter out = response.getWriter ();

        try {
            DbConn myConn = new DbConn("CSI");

            Statement stmt = myConn.conn.createStatement();
            PreparedStatement pstmt = null;
            ResultSet rs;
            PreparedStatement pstmtperson = null;
            ResultSet rs2;
            PreparedStatement pstmtpv = null;
            ResultSet rs3;

            inthowmany = Integer.parseInt(howmany);

            for (int i=1; i<=inthowmany; i++) {
                String visitorID = request.getParameter("visitorid" + i);
                String badgeNumber = (request.getParameter("badgenumber" + i) != null) ? request.getParameter("badgenumber" + i) : "";
                String destination = (request.getParameter("destination" + i) != null) ? request.getParameter("destination" + i) : "";
                String timeIn = (request.getParameter("timein" + i) != null) ? request.getParameter("timein" + i) : "";
                String timeOut = (request.getParameter("timeout" + i) != null) ? request.getParameter("timeout" + i) : "";
                String visitorinfo = "select firstname || ' ' || lastname from csi.person where id = " + visitorID;
                String visitorName = "";
                ResultSet rset = stmt.executeQuery (visitorinfo);
                while (rset.next()) {
                    visitorName = rset.getString(1);
                }
                rset.close();
                String personvisitupdate = "update csi.hvr_personvisit set badgenumber = '" + badgeNumber + "', destination = '" + destination + "', timein = '" + timeIn + "', timeout = '" + timeOut + "', lastupdated = SYSDATE, updatedby = "+ userID +" where visitrequestid = " + requestID + " and personid = " + visitorID;
                //out.println(personvisitupdate + "\n");
                pstmtpv = myConn.conn.prepareStatement(personvisitupdate);
                int rows3 = pstmtpv.executeUpdate();
                printPeople += "<tr><td>Visitor: " + visitorName + "<ul><li>Badge Number: " + badgeNumber + "<li>Time In: " + timeIn + "<li>Time Out: " + timeOut + "</ul><br></td></tr>\n";
            }
            pstmtpv = myConn.conn.prepareStatement("update hvr_visitrequest set comments = '" + StringEscapeUtils.escapeSql(comment) + "', lastupdated = SYSDATE, updatedby = " + userID + " where id = " + requestID);
            int rows3 = pstmtpv.executeUpdate();

            ALog.logActivity(Integer.parseInt(userID), "HVR", 2, "HVR: Visit request " + requestID + " updated");

            myConn.release();
        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
            ALog.logError(userIDLong, "HVR", 0, "HVR error: " + e + e.getMessage());
        }
        catch (java.lang.NumberFormatException e) {
            ALog.logError(userIDLong, "HVR", 0, "HVR error: " + e + e.getMessage());
        }

        out.println ("<html><head><title>HVR - Request updated</title>");
        out.println ("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/hvr.css\" />");
        out.println ("<script language=javascript>\n function view_report(type) {\n document.update.rtype.value = type; \ndocument.update.action = 'hvr_displayreport.jsp';\n document.update.submit();\n}\n</script>\n");
        out.println ("</head><body>");
        out.println ("<rdc:notProductionWarning size=\"150%\" width=\"180\" />\n");
        out.println ("<rdc:sessionTest />\n");
        out.println ("<form method=post name=update action='hvr_enterrequest.jsp'>");
        out.println ("<input type=hidden name=rtype value=Current>");
        out.println ("<input type=hidden name=UserID value=" + userID + ">");
        out.println ("<input type=hidden name=username value=" + userName + ">");
        out.println ("<input type=hidden name=authenticated value=" + authenticated + ">");
        out.println ("<table border=20 align=center bordercolor=#000fab bordercolorlight=#b2b7e5 bordercolordark=#00095f><tr><td>\n");
        out.println ("<table align=center><tr><td><h1>Hillshire Visitor Requests Database</h1>\n");
        out.println ("<h2>Thank you for your update!</h2></td></tr>\n");
        out.println ("<tr align=right><td><a href=javascript:document.update.submit()>Enter New Request</a><br><br>\n");
        out.println ("<a href=javascript:view_report('Current');>View Report</a></td></tr>\n");
        out.println (printPeople);
        out.println ("<tr><td>Comments: &nbsp;&nbsp;" + comment + "</td></tr>\n");
        out.println ("</table></td></tr>\n");
        out.println ("</table><br>");
        out.println ("</form>");
        out.println ("</body></html>");
    }

  // Generate the HTML response
    private void generateResponse(String message, HttpServletResponse response)
        throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Hillshire Visitor Requests Database - Entry Form</TITLE>");
        out.println("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/hvr.css\" />");
        out.println("</HEAD>");
        out.println("<BODY BGCOLOR=\"white\">");
        //out.println("The test data is: <BR>");
        //out.println("<BLOCKQUOTE><B>");
        out.println(message);
        //out.println("<B></BLOCKQUOTE>");
        out.println("</BODY>");
        out.println("</HTML>");

        out.close();
    }

    private String generateDate (String defaultDate, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String dateDropdown = "";
        return dateDropdown;
    }
}


