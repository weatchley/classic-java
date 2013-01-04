<%@ include file="headerSetup.inc" %>
<rdc:sessionTest />
<%@ page import="javax.naming.*" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.sql.*" %>
<%@ page import="gov.ymp.csi.db.*" %>
<%@ page import="gov.ymp.csi.people.*" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>OCRWM Las Vegas Visitor Requests Database - Request Display</title>
<script src=js/hvr.js></script>

<%
String cUserID = request.getParameter("UserID");
long cUserIDLong = Long.parseLong(cUserID);
String cUserName = request.getParameter("username");
String cUserAuth = request.getParameter("authenticated");
String rType = request.getParameter("rtype");
//String rType = (request.getParameter("rtype") != null) ? request.getParameter("rtype") : "Current";

cUserName = (cUserName != null) ? cUserName : "";
cUserAuth = (cUserAuth != null) ? cUserAuth : "no";

try {
    DbConn myConn = new DbConn("CSI");
    Connection conn = myConn.conn;

    if (session.getAttribute("user.name")  != null) {
           // get information about user: name, privs, etc...
           isAuthenticated = true;
           sUsr = new Person((String) session.getAttribute("user.name"));
           usr = (Person) session.getAttribute("user.person");
           pos = (Position) session.getAttribute("user.position");
           posID = (long) new Long((String) session.getAttribute("user.positionid")).longValue();
           perMap = (HashMap) session.getAttribute("user.permissionmap");
           if (pos.belongsTo(((Long) perMap.get("2-reports")).longValue())) {
                   isAdmin = true;
           }
    }
    //int repCount = 0;
    //String repCheckQ = "select count (*) from csi.hvr_reportview where id = " + request.getParameter("UserID") + "";
    //Statement rcqstmt = conn.createStatement();
    //ResultSet rcqrset = rcqstmt.executeQuery (repCheckQ);
    //while (rcqrset.next()) {
    //    repCount = rcqrset.getInt(1);
    //}
    //rcqrset.close();

    //if (cUserAuth.equals("yes") && repCount > 0) {
    if (isAuthenticated && isAdmin) {

	String whichReport = rType.equals("Current") ? "Past" : "Current";
        out.println ("<rdc:notProductionWarning size=\"150%\" width=\"180\" />\n");
        out.println ("<rdc:sessionTest />\n");
        out.println("<script language=javascript>header('Requests Report - ' + '" + rType + "' + ' Visit Requests');</script>");
        out.println("<form method='POST' action='hvr' name=report>\n");
        out.println("<input type=hidden name=visitid value=0>\n");
        out.println("<input type=hidden name=command value=edit>\n");
        out.println ("<tr><td align=right>\n");
	out.println ("<input type=hidden name=rtype value=" + rType + ">\n");
        out.println ("<a href=\"javascript:document.report.action='hvr_enterrequest.jsp';document.report.submit();\">Return To Entry Form</a>\n");
	out.println ("<br><br>\n<a href=javascript:view_report2('" + whichReport + "');>View " + whichReport + " Visits Report</a>\n");
        out.println ("</td></tr></table>\n");
        out.println ("<br>\n");
        out.println ("<table width=1000 align=center><tr><td align=left>\n");
        out.println ("<b>Report Date:</b> <script language=javascript>getTodaysDate('print');</script>\n");
        out.println ("</td></tr>\n");
        out.println ("</table>\n");

        out.println ("<table width=1000 align=center border=1 cellspacing=0 bordercolor=#bbbbbb>\n");
        out.println ("<tr bgcolor=#cccccc>\n");
        out.println ("<td rowspan=2>Visit Start</td>\n");
        out.println ("<td rowspan=2>Visit End</td>\n");
        out.println ("<td colspan=8>Visitor Information</td>\n");
        out.println ("<td rowspan=2 width=200>Purpose</td>\n");
        out.println ("<td rowspan=2>Requester and Contact</td>\n");
        out.println ("<td rowspan=2>Date Requested</td>\n");
        out.println ("<td rowspan=2>Comments</td>\n");
        out.println ("<td rowspan=2>Edit</td>\n");
        //out.println ("<td width=75 rowspan=2>Visit Start</td>\n");
        //out.println ("<td width=75 rowspan=2>Visit End</td>\n");
        ////out.println ("<td width=350 colspan=4>Visitor Information</td>\n");
        //out.println ("<td width=350 colspan=7>Visitor Information</td>\n");
        //out.println ("<td width=200 rowspan=2>Purpose</td>\n");
        //out.println ("<td width=150 rowspan=2>Requester and contact</td>\n");
        //out.println ("<td width=80 rowspan=2>Date Requested</td>\n");
        //out.println ("<td width=150 rowspan=2>Comments</td>\n");
        //out.println ("<td width=40 rowspan=2>Edit</td>\n");
        out.println ("</tr>\n");
        out.println ("<tr bgcolor=#cccccc class=sm><td class=sm>Name</td><td class=sm>Citizen</td><td class=sm>E/B</td><td class=sm>Affiliation</td><td class=sm>Badge</td><td class=sm>Destination</td><td>Date In</td><td>Date Out</td></tr>\n");

	String rDate = (rType.equals("Past")) ? "r.enddate <= sysdate-1 and " : "r.enddate >= sysdate-1 and ";
	String sortOrder = (rType.equals("Past")) ? " desc" : "";

        String theQuery = "select to_char(r.startdate, 'MM/DD/YY'), to_char(r.enddate, 'MM/DD/YY'), " + 
                             "r.id, r.secondarycontact, r.purpose, " + 
                             "p.firstname || '&nbsp;' || p.lastname, p.phone, p.email, " +
                             "to_char(r.requestdate, 'MM/DD/YY'), comments " +
                          "from csi.hvr_visitrequest r, csi.person p " +
                          "where " + rDate + "r.requester = p.id " +
                          "order by startdate" + sortOrder;

    	Date todayis = new Date();
    	String alloftoday = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(todayis);
    	String justtoday = DateFormat.getDateInstance(DateFormat.LONG).format(todayis);

//out.println (theQuery);
    	Statement stmt = conn.createStatement();
    	ResultSet rset = stmt.executeQuery (theQuery);
	int zebra = 0;
	String rowColor = "#ffffff";
    	while (rset.next()) {
	    String startDate = rset.getString(1);
	    String endDate = rset.getString(2);
	    int visitID = rset.getInt(3);
	    String ep = "";
	    String purpose = rset.getString(5);
	    String requesterContactSecondary = rset.getString(6) + ((rset.getString(7) !=null) ?  "<br>" +rset.getString(7):"") + ((rset.getString(4) != null) ? "<br>" + rset.getString(4) : "");
	    //String requesterContactSecondary = rset.getString(6) + "<br>" + rset.getString(7) + ((rset.getString(4) != null) ? "<br>" + rset.getString(4) : "");
	    String dateRequested = rset.getString(9);
	    String comment = (rset.getString(10) != null) ? rset.getString(10) : "&nbsp;";

   	    String visitorsQuery = "select p.firstname || ' ' || p.lastname, v.iscitizen, v.escortorproxy, v.affiliation, " +
                                          "v.badgenumber, v.timein, v.timeout, v.destination " +
                                   "from csi.hvr_personvisit v, csi.person p " +
                                   "where v.visitrequestid = " + visitID + " and v.personid = p.id";
//out.println (visitorsQuery);
	    Statement vstmt = conn.createStatement();
	    ResultSet vrs = vstmt.executeQuery (visitorsQuery);
	    String printVisitors = "";
	    String printVisitorsFirstRow = "";
	    String printVisitorsRest = "";

	    rowColor = ((zebra % 2) == 0) ? "#ffffff" : "#eeeeee";
	    int count = 0;
	    while (vrs.next()) {
	        String visitor = vrs.getString(1);
                String isCitizen = vrs.getString(2);
	        int escortProxy = vrs.getInt(3);
                String affiliation = (vrs.getString(4) != null) ? vrs.getString(4) : "N/A";
    	        isCitizen = (isCitizen.equals("T")) ? "Yes" : "No";
	        ep = (escortProxy == 1) ? "Badge" : "Escort";
	        String badgeNum = (vrs.getString(5) != null) ? vrs.getString(5) : "&nbsp;";
	        String timeIn = (vrs.getString(6) != null) ? vrs.getString(6) : "&nbsp;";
	        String timeOut = (vrs.getString(7) != null) ? vrs.getString(7) : "&nbsp;";
	        String destination = (vrs.getString(8) != null) ? vrs.getString(8) : "&nbsp;";
	        
      	        printVisitors = "<td class=sm nowrap>" + visitor + "</td><td width=45 class=sm>" + isCitizen + "</td><td vidth=45 class=sm>" + ep + "</td><td width=100 class=sm>" + affiliation + "&nbsp;</td><td class=sm nowrap>" + badgeNum + "&nbsp;</td><td class=sm>" + destination + "&nbsp;</td><td class=sm>" +timeIn+"&nbsp;</td><td class=sm>"+timeOut+"&nbsp;</td>\n"; 
	        if (count == 0) {
	    	    printVisitorsFirstRow += printVisitors;
	        }
	        else {
		    printVisitorsRest += "<tr align=left valign=top bgcolor=" + rowColor + ">" + printVisitors + "</tr>";
	        }
	        count++;
	    }
            vrs.close();
            out.println ("<tr align=left valign=top bgcolor=" + rowColor + "><td align=center class=sm rowspan=" + count + ">" + startDate + "</td>" + 
                         "<td class=sm rowspan=" + count + ">" + endDate + "</td>" + printVisitorsFirstRow + 
                         "<td class=sm rowspan=" + count + ">" + purpose + "</td>" +
                         "<td class=sm rowspan=" + count + ">" + requesterContactSecondary + "</td>" + 
                         "<td class=sm rowspan=" + count + ">" + dateRequested + "</td>\n" + 
                         "<td class=sm rowspan=" + count + ">" + comment + "&nbsp;</td>\n" + 
                         "<td class=sm rowspan=" + count + "><a href=javascript:editRequest(" + visitID + ",'edit');>Edit</a></td></tr>" + printVisitorsRest);
	    zebra++;
        }
        rset.close (); 
    }
    else {
        out.println ("<tr><td class=big>You are not authenticated<br> or <br>do not have sufficient privileges to view this report.</td></tr>\n");
        out.println ("<tr><td>&nbsp;</td></tr>\n");
        out.println ("<tr><td>Please log into the system before proceeding or go back to the request entry form.</td></tr>\n");
        out.println ("<tr><td>&nbsp;</td></tr>\n");
        out.println ("<tr><td><input type=button value=\"Click to log in\" onClick=javascript:gotologin();>&nbsp;&nbsp;&nbsp;<input type=button onClick=\"document.report.action='hvr_enterrequest.jsp';document.report.submit();\" value=\"Go to entry form\"></td></tr></table><br>\n</td></tr>");
    }
    myConn.release();
}
catch (SQLException e) {
    ALog.logError(cUserIDLong, "HVR", 0, "HVR error: " + e + e.getMessage());
}
catch (Exception e) {
    ALog.logError(cUserIDLong, "HVR", 0, "HVR error: " + e + e.getMessage());
}

%>

<script language=javascript>footer();</script>
<input type=hidden name=UserID value=<%= request.getParameter("UserID") %>>
<input type=hidden name=username value=<%= cUserName %>>
<input type=hidden name=authenticated value=<%= cUserAuth %>>
<br>
<br><br>
<rdc:envDisplay />
</form>
</body>
</html>
