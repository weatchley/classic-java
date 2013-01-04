<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
<script src=/common/javascript/utilities.js></script>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->

<h3>Domain Management</h3>
<%
DbConn myConn = new DbConn("csi");
Domains [] dom = Domains.getItemList(myConn);
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};

%>

<rdc:isAuthenticated doOpposite="true" >
<script language=javascript><!--
    document.location='home.jsp';
//-->
</script>
</rdc:isAuthenticated>

<a href="javascript:submitForm('updateDomain.jsp', 'add')">Add New Domain</a><br><br>

<table border=1 cellpadding=0 cellspacing=0>
<tr bgcolor=#ff77ff><td>&nbsp;</td><td><b>Name</b></td><td><b>Description</b></td><td><b>Is Local</b></td><td><b>Server</b></td><td><b>Code</b></td></tr>
<tr><td colspan=4></td></tr>


<%
int i = 0;
while (i < dom.length) {
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td>
    <a href="javascript:updateDomain(<%= dom[i].getID() %>)"><font size=-1>update</font></a><!--br>
    <a href="javascript:dropDomain(<%= dom[i].getID() %>)"><font size=-1>drop</font></a-->
    </td>
    <td valign=top><%= dom[i].getName() %></td>
    <td valign=top><%= dom[i].getDescription() %></td>
    <td valign=top align=center><%= dom[i].isLocal() %></td>
    <td valign=top><%= ((dom[i].getServer() != null) ? dom[i].getServer() : "&nbsp;") %></td>
    <td valign=top align=center><%= dom[i].getCode() %></td>
    </tr>
    <%
    i++;
}

%>

</table>
<input type=hidden name=systemid value=0>

<script language=javascript><!--

function updateDomain (val){
// update a permission
    document.main.id.value = val;
    submitForm('updateDomain.jsp', 'update');
}

function dropDomain (val){
// update a permission
    document.main.id.value = val;
    submitFormResults('doDomains', 'drop');
}

//-->
</script>



<% myConn.release(); %>


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
