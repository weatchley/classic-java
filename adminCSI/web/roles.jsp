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

<h3>Role Management</h3>
<%
DbConn myConn = new DbConn("csi");
Role [] rls = Role.getRoleSet(myConn);
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};

%>

<a href="javascript:submitForm('updateRole.jsp', 'add')">Add New Role</a><br><br>

<table border=1 cellpadding=0 cellspacing=0>
<tr bgcolor=#ff77ff><td>&nbsp;</td><td><b>Description</b></td><td><b>Status</b></td></tr>
<tr><td colspan=3></td></tr>


<%
int i = 0;
while (i < rls.length) {
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td>
    <a href="javascript:updateRole(<%= rls[i].getID() %>)"><font size=-1>update</font></a><br>
    <a href="javascript:dropRole(<%= rls[i].getID() %>)"><font size=-1>drop</font></a>
    </td>
    <td valign=top><%= rls[i].getDescription() %></td>
    <td valign=top><%= ((rls[i].getStatus() != null) ? rls[i].getStatus() : "&nbsp;") %></td>
    </tr>
    <%
    i++;
}

%>

</table>
<input type=hidden name=systemid value=0>

<script language=javascript><!--

function updateRole (val){
// update a permission
    document.main.id.value = val;
    submitForm('updateRole.jsp', 'update');
}

function dropRole (val){
// update a permission
    document.main.id.value = val;
    submitFormResults('doRoles', 'drop');
}

//-->
</script>



<% myConn.release(); %>


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
