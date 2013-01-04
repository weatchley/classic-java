<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
<script src=/common/javascript/utilities.js></script>
<script src=/common/javascript/widgets.js></script>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->

<h3>Role Add/Update</h3>
<%
String command = request.getParameter("command");
String id = ((request.getParameter("id") != null && !request.getParameter("id").equals("")) ? (String) request.getParameter("id") : "0");
long systemID = 0;
String description = "";
String status = "";
DbConn myConn = new DbConn("csi");
Permission [] per = Permission.getPermissionSet(myConn);
Position [] posSet = Position.getPositionSet(myConn);
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
Role rl = null;
if (command.equals("update")) {
    rl = new Role(myConn, Long.parseLong(id));
    description = rl.getDescription();
    status = rl.getStatus();
} else {
    rl = new Role();
}
int i = 0;

myConn.release();
%>

<rdc:isAuthenticated doOpposite="true" >
<script language=javascript><!--
    document.location='index.jsp';
//-->
</script>
</rdc:isAuthenticated>

<table border=0>
<tr><td><b>Description</b> &nbsp; &nbsp;</td><td><input type=text name=description size=60 maxlength=100 value="<%= description %>"></td></tr>
<tr><td><b>Status</b> &nbsp; &nbsp;</td><td><input type=text name=rstatus size=30 maxlength=30 value="<%= ((status != null) ? status : "") %>"></td></tr>

<tr><td colspan=2><b>Permissions<b><br>
<%
HashMap perAvail = new HashMap(per.length);
HashMap perSelected = new HashMap(per.length);
for (int j=0; j<per.length; j++) {
    perAvail.put(new Long(per[j].getID()), (per[j].getSystem().getAcronym() + "/" + per[j].getDescription()));
    if (rl.hasPermission(per[j].getID())) {
        perSelected.put(new Long(per[j].getID()), (per[j].getSystem().getAcronym() + "/" + per[j].getDescription()));
    }
}
String dualSelect = HtmlUtils.doDualSelect("permissions", formName, perAvail, perSelected, "<b>Available</b>", "<b>Selected</b>");
%>
<%= dualSelect %>
</td></tr>

<tr><td colspan=2><b>Positions<b><br>
<%
HashMap posAvail = new HashMap(per.length);
HashMap posSelected = new HashMap(per.length);
for (int j=0; j<posSet.length; j++) {
    posAvail.put(new Long(posSet[j].getID()), posSet[j].getDescription());
    if (rl.hasPosition(posSet[j].getID())) {
        posSelected.put(new Long(posSet[j].getID()), posSet[j].getDescription());
    }
}
dualSelect = HtmlUtils.doDualSelect("positions", formName, posAvail, posSelected, "<b>Available</b>", "<b>Selected</b>");
%>
<%= dualSelect %>

</td></tr>
<tr><td colspan=2><input type=button value='Submit' onClick="verifySubmit(document.main)"></td></tr>
</table>


<script language=javascript><!--

function verifySubmit (f){
// javascript form verification routine
    var msg = "";
    if (isblank(f.description.value)) {
        msg += "Description must be entered.\n";
    }
    if (msg != "") {
      alert (msg);
    } else {
        for (index=0; index < f.permissions.length-1;index++) {
            f.permissions.options[index].selected = true;
        }
        for (index=0; index < f.positions.length-1;index++) {
            f.positions.options[index].selected = true;
        }
        f.id.value = <%= id %>;
        submitFormResults('doRoles', '<%= command %>');
    }
}

//-->
</script>

<input type=hidden name=nextscript value='roles.jsp'>



<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
