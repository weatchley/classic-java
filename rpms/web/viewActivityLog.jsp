<HTML>

<HEAD>
<TITLE>Records Plan Management System</TITLE>
  <LINK href="css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="headerSetup.inc" %>
<%@ page import="java.sql.*" %>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->

<%
String command = request.getParameter("command");
int viewCount = ((request.getParameter("viewcount") != null) ? Integer.parseInt((String) request.getParameter("viewcount")) : 10);
int activityType = ((request.getParameter("activitytype") != null) ? Integer.parseInt((String) request.getParameter("activitytype")) : 0);
String systemID = ((request.getParameter("systemid") != null) ? (String) request.getParameter("systemid") : "RPMS");
String onlyErr = ((request.getParameter("onlyerr") != null) ? (String) request.getParameter("onlyerr") : "F");
int userID = ((request.getParameter("userid") != null) ? Integer.parseInt((String) request.getParameter("userid")) : 0);

String red = "#bb0000";
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};

DbConn myConn = new DbConn("csi");
AType [] aTypes = AType.getATypes(myConn);
Sys [] sys = Sys.getSystems(myConn);
Person [] usrs = ALog.getUsersInLog(myConn);
int i = 0;
%>
<% if (!isAuthenticated || !isAdmin) { %>
    <script language=javascript><!--
        document.location='home.jsp';
    //-->
    </script>
<% } %>
<h2 align=center>Activity Log</h2>
<table border=0 align=center><tr><td align=center>
<table border=0 align=center><tr>
<td valign=bottom><b>User:</b></td><td>&nbsp;</td>
<td valign=bottom><b>Type:</b></td><td>&nbsp;</td>
<td valign=bottom><b>View:</b></td><td>&nbsp;</td>
<td valign=bottom align=center><b>Only<br>Errors:</b></td><td>&nbsp; &nbsp;</td>
<td>&nbsp;</td></tr><tr>
<td>
<select name=userid>
<option value='0' <%= ((userID == 0) ? " selected" : "") %>>All</option>
<%
i = 0;
while (i < usrs.length) {
    if (usrs[i] != null && usrs[i].getID() > 0) {
        %> <option value='<%= usrs[i].getID() %>' <%= ((userID == usrs[i].getID()) ? " selected" : "") %>><%= (usrs[i].getLastName() + ", " + usrs[i].getFirstName()) %></option> <%
    }
    i++;
}
%>
</select>
</td><td>&nbsp;</td><td>
<select name=activitytype>
<option value=0 <%= ((activityType == 0) ? " selected" : "") %>>All</option>
<%
i = 0;
while (i < aTypes.length) {
    if (aTypes[i].id > 0) {
        %> <option value=<%= aTypes[i].id %> <%= ((activityType == aTypes[i].id) ? " selected" : "") %>><%= aTypes[i].description %></option> <%
    }
    i++;
}
%>
</select>
</td><td>&nbsp;</td><td>
<select name=viewcount>
<option value=10 <%= ((viewCount == 10) ? " selected" : "") %>>Last 10 Entries</option>
<option value=100 <%= ((viewCount == 100) ? " selected" : "") %>>Last 100 Entries</option>
<option value=1000 <%= ((viewCount == 1000) ? " selected" : "") %>>Last 1000 Entries</option>
<option value=5000 <%= ((viewCount == 5000) ? " selected" : "") %>>Last 5000 Entries</option>
</select> &nbsp; 
</td><td>&nbsp;</td><td align=center>
<input type=checkbox name=onlyerr value='T'<%= ((onlyErr.equals("T")) ? " checked" : "") %>>
</td><td>&nbsp;</td><td>
<input type=button value="Refresh" onClick="javascript:submitForm('viewActivityLog.jsp','none')">
</td></tr>
</table></td></tr>
<tr><td>
<table border=1 cellpadding=0 cellspacing=0 width=100%>
<tr bgcolor=#ff77ff><td width=140><b>Date/Time</b></td><td width=110><b>User</b></td><td width=55><b>System</b></td>
<td width=90><b>Activity&nbsp;Type</b></td>
<td><b>Activity/<font color=<%= red %>>Error</font> Text</b></td></tr>
<tr><td colspan=5></td></tr>
<%
boolean errflag = (onlyErr.equals("T")) ? true : false;
//ALog [] log = ALog.getALog(myConn, userID, systemID, activityType, viewCount, errflag);
ALog [] log = ALog.getALog(myConn, userID, "RPMS", activityType, viewCount, errflag);
i = 0;
while (i < log.length) {
    boolean isError = log[i].isError;
    String tTime = log[i].timeStamp.toString();
    tTime = tTime.replaceAll(".0$", "");
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>><td valign=top><%= tTime %></td><td valign=top><%= log[i].user %></td>
    <td valign=top><%= log[i].system %></td><td valign=top><%= log[i].type %></td>
    <td valign=top><%= ((isError) ? "<font color=" + red + "> " + log[i].description + "</font>" : log[i].description) %></td></tr>
    <%
    i++;
}
myConn.release();



%>
</table>


</td></tr></table>
<script language=javascript><!--

//-->
</script>

<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
