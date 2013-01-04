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

<h3>Domain Add/Update</h3>
<%
String command = request.getParameter("command");
String idS = ((request.getParameter("id") != null && !request.getParameter("id").equals("")) ? (String) request.getParameter("id") : "0");
int id = Integer.parseInt(idS);
long systemID = 0;
String key = "";
String description = "";
DbConn myConn = new DbConn("csi");
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
Domains dom = null;
//System.out.println("updateDomain.jsp - Got Here 1" +"- command: " + command + ", ID: " + id);
if (command.equals("update")) {
    dom = new Domains(id, myConn);
    dom.lookup(id, myConn);
} else {
    dom = new Domains();
}

myConn.release();
%>

<rdc:isAuthenticated doOpposite="true" >
<script language=javascript><!--
    document.location='home.jsp';
//-->
</script>
</rdc:isAuthenticated>

<table border=0>
<tr><td><b>Name</b> &nbsp; &nbsp;</td><td><input type=text name=name size=20 maxlength=40 value="<%= ((command.equals("update")) ? dom.getName() : "") %>"></td></tr>
<tr><td><b>Description</b> &nbsp; &nbsp;</td><td><input type=text name=description size=80 maxlength=200 value="<%= ((dom.getDescription() != null) ? dom.getDescription() : "") %>"></td></tr>
<tr><td><b>Is Local</b> &nbsp; &nbsp;</td><td><input type=checkbox name=islocal value="T" <%= ((dom.isLocal()) ? " checked" : "") %>></td></tr>
<tr><td><b>Server</b> &nbsp; &nbsp;</td><td><input type=text name=server size=40 maxlength=60 value="<%= ((dom.getServer() != null) ? dom.getServer() : "") %>"></td></tr>
<tr><td><b>code</b> &nbsp; &nbsp;</td><td><input type=text name=code size=4 maxlength=4 value="<%= ((dom.getCode() != null) ? dom.getCode() : "") %>"></td></tr>
<tr><td colspan=2><input type=button value='Submit' onClick="verifySubmit(document.main)"></td></tr>
</table>


<script language=javascript><!--

function verifySubmit (f){
// javascript form verification routine
    var msg = "";
    if (isblank(f.name.value)) {
        msg += "Name must be entered.\n";
    }
    if (isblank(f.description.value)) {
        msg += "Description must be entered.\n";
    }
    if (isblank(f.server.value) && !f.islocal.checked) {
        msg += "Server must be entered.\n";
    }
    if (!isblank(f.server.value) && f.islocal.checked) {
        msg += "Server not entered for local domains.\n";
    }
    if (isblank(f.code.value)) {
        msg += "Code must be entered.\n";
    }
    if (msg != "") {
      alert (msg);
    } else {
        f.id.value = <%= id %>;
        submitFormResults('doDomains', '<%= command %>');
    }
}

//-->
</script>

<input type=hidden name=nextscript value='domains.jsp'>



<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
