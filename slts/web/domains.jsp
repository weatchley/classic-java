<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");
usr = new Person((String) session.getAttribute("user.name"));
DbConn myConn = new DbConn("csi");
Domain [] dom = Domain.getItemList(myConn);

String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
int j=0;
String tempText = "";

%>
  

  <h4>Domain Management</h4><br>

<table border=0>
<tr><td valign=bottom><b>Name</b></td><td valign=bottom><b>Include</b></td><td valign=bottom><b>Date Discovered</b></td>
</tr>

<%
for (int i=0; i < dom.length; i++) {
    
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td valign=top><%= dom[i].getDomainName() %></a></td>
    <input type=hidden name=domainname<%= i %> value="<%= dom[i].getDomainName() %>">
    <td valign=top align=center><input type=checkbox name=include<%= i %> value='T' <%= ((dom[i].isInclude()) ? "checked" : "") %>></td>
    <td valign=top align=center><%= Utils.dateToString(dom[i].getDateDiscovered()) %></td>
    </tr>
    <%
    j=i+1;
}
%>
</table>

<input type=hidden name=itemcount value=<%= j %>>

<input type=hidden name=nextscript value="domains.jsp">

<input type=button name=submitform value="Submit" onClick="submitFormResults('doDomains', 'update')">





<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
