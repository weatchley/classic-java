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

<h3>Permission Add/Update</h3>
<%
String command = request.getParameter("command");
String id = ((request.getParameter("id") != null && !request.getParameter("id").equals("")) ? (String) request.getParameter("id") : "0");
long systemID = 0;
String key = "";
String description = "";
DbConn myConn = new DbConn("csi");
Sys [] sys = Sys.getSystems(myConn);
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
if (command.equals("update")) {
    Permission tmp = new Permission(myConn, Long.parseLong(id));
    systemID = tmp.getSystemID();
    key = tmp.getKey();
    description = tmp.getDescription();
}

myConn.release();
%>

<rdc:isAuthenticated doOpposite="true" >
<script language=javascript><!--
    document.location='index.jsp';
//-->
</script>
</rdc:isAuthenticated>

<table border=0>
<tr><td><b>System</b></td><td>
<select name=systemid>
<option value='0' <%= ((systemID == 0) ? " selected" : "") %>>Select A System</option>
<%
int i = 0;
while (i < sys.length) {
    if (sys[i].id > 0) {
        %> <option value='<%= sys[i].id %>' <%= ((systemID == sys[i].id) ? " selected" : "") %>><%= sys[i].acronym %></option> <%
    }
    i++;
}
%>
</select></td></tr>
<tr><td><b>Key</b> &nbsp; &nbsp;</td><td><input type=text name=key size=20 maxlength=20 value="<%= key %>"></td></tr>
<tr><td><b>Description</b> &nbsp; &nbsp;</td><td><input type=text name=description size=60 maxlength=100 value="<%= description %>"></td></tr>
<tr><td colspan=2><input type=button value='Submit' onClick="verifySubmit(document.main)"></td></tr>
</table>


<script language=javascript><!--

function verifySubmit (f){
// javascript form verification routine
    var msg = "";
    if (f.systemid[0].selected) {
        msg += "System must be selected.\n";
    }
    if (isblank(f.key.value)) {
        msg += "Key must be entered.\n";
    }
    if (isblank(f.description.value)) {
        msg += "Description must be entered.\n";
    }
    if (msg != "") {
      alert (msg);
    } else {
        f.id.value = <%= id %>;
        submitFormResults('doPermissions', '<%= command %>');
    }
}

//-->
</script>

<input type=hidden name=nextscript value='permissions.jsp'>



<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
