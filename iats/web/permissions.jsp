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

<h3>Permission Management</h3>
<%
DbConn myConn = new DbConn("csi");
Permission [] per = Permission.getPermissionSet(myConn);
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};

%>

<rdc:isAuthenticated doOpposite="true" >
<script language=javascript><!--
    document.location='index.jsp';
//-->
</script>
</rdc:isAuthenticated>

<a href="javascript:submitForm('updatePermission.jsp', 'add')">Add New Permission</a><br><br>

<table border=1 cellpadding=0 cellspacing=0>
<tr bgcolor=#ff77ff><td>&nbsp;</td><td width=55><b>System</b></td><td><b>Key</b></td><td><b>Description</b></td></tr>
<tr><td colspan=4></td></tr>


<%
int i = 0;
while (i < per.length) {
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td>
    <a href="javascript:updatePermission(<%= per[i].getID() %>)"><font size=-1>update</font></a><br>
    <a href="javascript:dropPermission(<%= per[i].getID() %>)"><font size=-1>drop</font></a>
    </td>
    <td valign=top><%= per[i].getSystem().getAcronym() %></td>
    <td valign=top><%= per[i].getKey() %></td>
    <td valign=top><%= per[i].getDescription() %></td>
    </tr>
    <%
    i++;
}

%>

</table>
<input type=hidden name=systemid value=0>

<script language=javascript><!--

function updatePermission (val){
// update a permission
    document.main.id.value = val;
    submitForm('updatePermission.jsp', 'update');
}

function dropPermission (val){
// update a permission
    document.main.id.value = val;
    submitFormResults('doPermissions', 'drop');
}

//-->
</script>



<% myConn.release(); %>


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
