<HTML>
<%@ include file="headerSetup.inc" %>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
<script src=/common/javascript/utilities.js></script>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->

<h3>Person Update</h3>
<%
String command = request.getParameter("command");
String id = ((request.getParameter("id") != null && !request.getParameter("id").equals("")) ? (String) request.getParameter("id") : "0");
long systemID = 0;
String key = "";
String description = "";
DbConn myConn = new DbConn("csi");
Sys [] sys = Sys.getSystems(myConn);
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
Person per = new Person();
pos = (Position) session.getAttribute("user.position");
perMap = (HashMap) session.getAttribute("user.permissionmap");
if (command.equals("update")) {
    per.getInfo(myConn, Long.parseLong(id));
}
Sys [] systems = Sys.getSystems(myConn);

myConn.release();
%>

<rdc:isAuthenticated doOpposite="true" >
<script language=javascript><!--
    document.location='index.jsp';
//-->
</script>
</rdc:isAuthenticated>

<table border=0>
<tr><td><b>Username</b></td><td><%= per.getUserName() %></td></tr>
<tr><td><b>Domain</b></td><td><%= per.getDomainName() %></td></tr>
<tr><td><b>First Name</b> &nbsp; &nbsp;</td><td><input type=text name=firstname size=25 maxlength=25 value="<%= per.getFirstName() %>"></td></tr>
<tr><td><b>Last Name</b> &nbsp; &nbsp;</td><td><input type=text name=lastname size=25 maxlength=25 value="<%= per.getLastName() %>"></td></tr>
<tr><td><b>E-Mail</b> &nbsp; &nbsp;</td><td><input type=text name=email size=60 maxlength=60 value="<%= ((per.getEmail() != null) ? per.getEmail() : "") %>"></td></tr>
<tr><td><b>Phone</b> &nbsp; &nbsp;</td><td><input type=text name=phone size=20 maxlength=20 value="<%= ((per.getPhone() != null) ? per.getPhone() : "") %>"></td></tr>
<tr><td valign=top><b>External</b> &nbsp; &nbsp;</td><td>
<table border=0><tr><td><b>System</b></td><td><b>Username</b></td></tr>
<%
for (int i=0; i<systems.length; i++) {
    if (systems[i].getExternalSYS()) {
        String tmp = per.getSystemUsername(systems[i].getID());
        tmp = ((tmp != null) && (!tmp.equals("null"))) ? tmp : "";
        %>
        <tr><td><%= systems[i].getDescription() %> &nbsp; &nbsp; </td>
        <td>
        <% if (pos.belongsTo(((Long) perMap.get("1-userupdate")).longValue())) { %>
        <input type=text name=sys-<%= systems[i].getID() %> value="<%= tmp %>" size=20 maxlength=51></td>
        <% } else { %>
        <input type=hidden name=sys-<%= systems[i].getID() %> value="<%= tmp %>"><%= tmp %></td>
        <% } %>
        </tr>
        <%
    }
}
%>
</table>
</td></tr>
<tr><td colspan=2><input type=button value='Submit' onClick="verifySubmit(document.main)"></td></tr>
</table>


<script language=javascript><!--

function verifySubmit (f){
// javascript form verification routine
    var msg = "";
    if (isblank(f.firstname.value)) {
        msg += "First Name must be entered.\n";
    }
    if (isblank(f.lastname.value)) {
        msg += "Last Name must be entered.\n";
    }
    if (isblank(f.email.value)) {
        msg += "E-Mail must be entered.\n";
    }
    if (isblank(f.phone.value)) {
        msg += "Phone number must be entered.\n";
    }
    if (msg != "") {
      alert (msg);
    } else {
        f.id.value = <%= id %>;
        submitFormResults('doPerson', '<%= command %>');
    }
}

//-->
</script>

<input type=hidden name=nextscript value='person.jsp'>



<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
