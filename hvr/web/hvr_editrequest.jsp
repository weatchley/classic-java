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
<link type="text/css" rel="stylesheet" href="css/hvr.css" />
<script src=js/hvr.js></script>
</head>

<body>
<rdc:notProductionWarning size="150%" width="180" />
<rdc:sessionTest />
<form method='POST' action='hvr' name=edit>
<table align=center bordercolor=#000fab bordercolorlight=#b2b7e5 bordercolordark=#00095f border=20 width=750><tr><td>
<table align=center cellspacing=0>
<tr><td align=left colspan=2>
<h1>OCRWM Las Vegas Visitor Requests Database</h1>
<h2>Edit Request</h2>
</td></tr>
<%
String cUserID = request.getParameter("UserID");
long cUserIDLong = Long.parseLong(cUserID);
String cUserName = request.getParameter("username");
String cUserAuth = request.getParameter("authenticated");
String visitID2 = request.getParameter("visitid");
String rType = request.getParameter("rtype");
//String rType = (request.getParameter("rtype") != null) ? request.getParameter("rtype") : "Current";

cUserName = (cUserName != null) ? cUserName : "";
cUserAuth = (cUserAuth != null) ? cUserAuth : "no";
int count = 0;

try {
    DbConn myConn = new DbConn("CSI");
    Connection conn = myConn.conn;

    //int repCount = 0;
    //String repCheckQ = "select count (*) from csi.hvr_reportview where id = " + request.getParameter("UserID") + "";
    //Statement rcqstmt = conn.createStatement();
    //ResultSet rcqrset = rcqstmt.executeQuery (repCheckQ);
    //while (rcqrset.next()) {
    //    repCount = rcqrset.getInt(1);
    //}
    String theQuery = "select to_char(r.startdate, 'MM/DD/YY'), to_char(r.enddate, 'MM/DD/YY'), " + 
                          "r.id, r.secondarycontact, r.purpose, " + 
                          "p.firstname || ' ' || p.lastname, p.phone, p.email, " +
                          "to_char(r.requestdate, 'MM/DD/YY'), comments " +
                      "from csi.hvr_visitrequest r, csi.person p " +
                      "where r.id = " + visitID2 + " and r.requester = p.id";

    Date todayis = new Date();
    String alloftoday = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(todayis);
    String justtoday = DateFormat.getDateInstance(DateFormat.LONG).format(todayis);

//out.println (theQuery);
    Statement stmt = conn.createStatement();
    ResultSet rset = stmt.executeQuery (theQuery);

    while (rset.next()) {
	String startDate = rset.getString(1);
	String endDate = rset.getString(2);
	int visitID = rset.getInt(3);
	String ep = "";
	String purpose = rset.getString(5);
	String requesterContactSecondary = rset.getString(6) + ", " + rset.getString(7) + ((rset.getString(4) != null) ? ", " + rset.getString(4) : "");
	String dateRequested = rset.getString(9);
	String comment = (rset.getString(10) != null) ? rset.getString(10) : "&nbsp;";

   	String visitorsQuery = "select p.firstname || ' ' || p.lastname, v.iscitizen, v.escortorproxy, " +
                                   "v.affiliation, v.personid, v.badgenumber, v.timein, v.timeout, v.destination " +
                               "from csi.hvr_personvisit v, csi.person p " +
                               "where v.visitrequestid = " + visitID + " and v.personid = p.id";
//out.println (visitorsQuery);
	Statement vstmt = conn.createStatement();
	ResultSet vrs = vstmt.executeQuery (visitorsQuery);
        String printVisitors = "";
	String printVisitorsFirstRow = "";
	String printVisitorsRest = "<table width=100% border=1 cellspacing=0>\n" +
                                   "<tr align=center bgcolor=#dddddd><td>Visitor Name</td><td>Citizen</td>\n" +
                                   "<td>E/B</td><td>Affiliation</td><td>Badge Number</td><td>Destination</td>\n" +
                                   "<td>Date In</td><td>Date Out</td></tr>\n";

	while (vrs.next()) {
	    count++;
	    String visitor = vrs.getString(1);
            String isCitizen = vrs.getString(2);
	    int escortProxy = vrs.getInt(3);
            String affiliation = (vrs.getString(4) != null) ? vrs.getString(4) : "N/A";
    	    isCitizen = (isCitizen.equals("T")) ? "Yes" : "No";
            ep = (escortProxy == 1) ? "Badge" : "Escort";
	    String visitorID = vrs.getString(5);
	    String badgeNumber = (vrs.getString(6) != null) ? "'" + vrs.getString(6) + "'" : "''";
	    String timeIn = (vrs.getString(7) != null) ? "'" + vrs.getString(7) + "'" : "''";
	    String timeOut = (vrs.getString(8) != null) ? "'" + vrs.getString(8) + "'" : "''";
	    String destination = (vrs.getString(9) != null) ? "'" + vrs.getString(9) + "'" : "''";
            printVisitors = "<tr><td nowrap>" + visitor + "</td>\n" +
                            "<td width=45>" + isCitizen + "</td>\n" +
                            "<td>" + ep + "</td>\n" +
                            "<td>" + affiliation + "&nbsp;</td>\n" +
                            "<td><input type=text name=badgenumber" + count + " value=" + badgeNumber + "></td>\n" +
                            "<td><input type=text name=destination" + count + " value=" + destination + "></td>\n" +
                            "<td><input type=text name=timein" + count + " value=" + timeIn + "></td>\n" +
                            "<td><input type=text name=timeout" + count + " value=" + timeOut + ">\n" +
                            "<input type=hidden name=visitorid" + count + " value=" + visitorID + "></td></tr>\n"; 
     	    printVisitorsRest += printVisitors;
	}
        vrs.close();
	printVisitorsRest += "</table>\n";
        out.println ("<tr align=left><td colspan=2>" + printVisitorsRest + "</td></tr>\n" +
                     "<tr align=left><td>Requester & Contact:</td><td>" + requesterContactSecondary + "</td></tr>" + 
                     "<tr align=left><td>Visit Start Date:</td><td>" + startDate + "</td></tr>" + 
                     "<tr align=left><td>Visit End Date:</td><td>" + endDate + "</td></tr>" + 
                     "<tr align=left><td>Purpose of Visit:</td><td>" + purpose + "</td></tr>" +
                     "<tr align=left><td>Date Requested:</td><td>" + dateRequested + "</td></tr>\n" + 
                     "<tr align=left><td valign=top>Comments:</td><td><textarea name=comment rows=3 cols=60>" + comment + "</textarea></td></tr>\n" + 
                     "<tr><td colspan=2><input type=submit value=Submit></td></tr>\n");
    }
    rset.close();
    myConn.release();
}
catch (SQLException e) {
    ALog.logError(cUserIDLong, "HVR", 0, "HVR error: " + e + e.getMessage());
}
catch (Exception e) {
    ALog.logError(cUserIDLong, "HVR", 0, "HVR error: " + e + e.getMessage());
}

%>


</table>
<br></td></tr></table>
<input type=hidden name=UserID value=<%= cUserID %>>
<input type=hidden name=username value=<%= cUserName %>>
<input type=hidden name=authenticated value=<%= cUserAuth %>>
<input type=hidden name=rtype value=Current>
<input type=hidden name=page value=3>
<input type=hidden name=howmany value=<%= count %>>
<input type=hidden name=requestid value=<%= visitID2 %>>

<br>
<br><br>
</form>

<rdc:envDisplay />
</body>
</html>
