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

<h3>People Management</h3>
<%
DbConn myConn = new DbConn("csi");
usr = (Person) session.getAttribute("user.person");
Person [] per = Person.getPersonSet(myConn);
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
pos = (Position) session.getAttribute("user.position");
perMap = (HashMap) session.getAttribute("user.permissionmap");

%>

<rdc:isAuthenticated doOpposite="true" >
<script language=javascript><!--
    document.location='index.jsp';
//-->
</script>
</rdc:isAuthenticated>

<!-- <a href="javascript:submitForm('updatePerson.jsp', 'add')">Add New Person</a><br><br> -->

<table border=1 cellpadding=0 cellspacing=0>
<tr bgcolor=#ff77ff><td>&nbsp;</td><td><b>User</b></td><td><b>Name</b></td><td><b>E-Mail</b></td><td><b>Phone</b></td><td><b>Last Authentication</b></td></tr>
<tr><td colspan=4></td></tr>


<%
int i = 0;
while (i < per.length) {
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td>
    <% if (pos.belongsTo(((Long) perMap.get("1-userupdate")).longValue()) || usr.getID() == per[i].getID()) { %>
    <a href="javascript:updatePerson(<%= per[i].getID() %>)"><font size=-1>update</font></a><br>
    <% } else { %>
    &nbsp;
    <% } %>
    </td>
    <td valign=top><%= per[i].getUserName() %></td>
    <td valign=top><%= per[i].getLastName() %>, <%= per[i].getFirstName() %></td>
    <td valign=top><%= ((per[i].getEmail() != null) ? per[i].getEmail() : "&nbsp;") %></td>
    <td valign=top><%= ((per[i].getPhone() != null) ? per[i].getPhone() : "&nbsp;") %></td>
    <td valign=top><%= ((per[i].getLastAuthentication() != null) ? (per[i].getLastAuthentication().toString()) : "&nbsp;") %></td>
    </tr>
    <%
    i++;
}

%>

</table>
<input type=hidden name=systemid value=0>

<script language=javascript><!--

function updatePerson (val){
// update a permission
    document.main.id.value = val;
    submitForm('updatePerson.jsp', 'update');
}


//-->
</script>



<% myConn.release(); %>


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
