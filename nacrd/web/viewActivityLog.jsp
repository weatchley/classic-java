<HTML>

<HEAD>
<TITLE>Tribal Information Exchange</TITLE>
</HEAD>

<BODY BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>
<%@ include file="headerSetup.html" %>
<%@ page import="java.sql.*" %>
<%@ include file="imageFrameStart.html" %>
<!-- Begin Main Content -->

<%
String command = request.getParameter("command");
int viewCount = ((request.getParameter("viewcount") != null) ? Integer.parseInt((String) request.getParameter("viewcount")) : 10);
String red = "#bb0000";
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};

%>
<h2 align=center>Activity Log</h2>
<table border=0 align=center><tr><td align=center><select name=viewcount>
<option value=10<%= ((viewCount == 10) ? " selected" : "") %>>Last 10 Entries</option>
<option value=100<%= ((viewCount == 100) ? " selected" : "") %>>Last 100 Entries</option>
<option value=1000<%= ((viewCount == 1000) ? " selected" : "") %>>Last 1000 Entries</option>
<option value=5000<%= ((viewCount == 5000) ? " selected" : "") %>>Last 5000 Entries</option>
</select> &nbsp; <input type=button value="Refresh" onClick="javascript:submitForm('viewActivityLog.jsp','none')"></td></tr>
<tr><td>
<table border=1 cellpadding=0 cellspacing=0 width=100%>
<tr bgcolor=#ff77ff><td width=140><b>Date/Time</b></td><td width=110><b>User</b></td><td><b>Activity/<font color=<%= red %>>Error</font> Text</b></td></tr>
<tr><td colspan=3></td></tr>
<%
DbConn myConn = new DbConn();
Statement stmt = myConn.conn.createStatement();
String sqlcode = "SELECT a.user,u.fullname,a.datelogged,a.reference,a.iserror,a.description " +
  "FROM nacrd.activity_log a, nacrd.users u WHERE a.user=u.id ORDER BY a.datelogged DESC";
ResultSet rset = stmt.executeQuery (sqlcode);
int i = 0;
while (rset.next() && i < viewCount) {
    boolean isError = ((rset.getString(5).equals("T")) ? true : false);
    String tTime = rset.getTimestamp(3).toString();
    tTime = tTime.replaceAll(".0$", "");
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>><td valign=top><%= tTime %></td><td valign=top><%= rset.getString(2) %></td>
    <td valign=top><%= ((isError) ? "<font color=" + red + "> " + rset.getString(6) + "</font>" : rset.getString(6)) %></td></tr>
    <%
    i++;
}
rset.close();
myConn.release();



%>
</table>

</td></tr></table>
<script language=javascript><!--

//-->
</script>

<!-- End Main Content -->
<%@ include file="imageFrameStop.html" %>

</BODY>

</HTML>
