<HTML>

<HEAD>
<TITLE>Records Plan Management System</TITLE>
  <LINK href="css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="headerSetup.inc" %>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");

DbConn myConn = new DbConn("csi");
RecordSeries [] list = null;
    FOrganization loc = null;


String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
String [] locString = request.getParameterValues("location");
//String locString = request.getParameterValues("location");
//System.out.println("report1.jsp - Got Here 1" + " command: " + command + ", locString: " + locString[0]);

String reportTitle = "";

int locID = 0;

if (locString[0] != null && !locString[0].equals("")) {
    locID = Integer.parseInt(locString[0]);
    if (locID != 0) {
        loc = new FOrganization(locID, myConn);
    }
}

System.out.println("report1.jsp - Got Here 2" + " command: " + command);
if (command.equals("annualfileplan")) {
    list = RecordSeries.getItemList(myConn, locID, true);
    reportTitle = "Annual File Plan" + ((locID > 0) ? " for " + loc.getDescription() : "");
} else if (command.equals("annualshortterm1")) {
    list = RecordSeries.getItemList(myConn, locID, true, "description", "S");
    reportTitle = "Annual Short-term Records for Disposition" + ((locID > 0) ? " for " + loc.getDescription() : "");
} else if (command.equals("annualshortterm2")) {
    list = RecordSeries.getItemList(myConn, locID, true, "cdescription, description", "S");
    reportTitle = "Annual Short-term Records for Disposition" + ((locID > 0) ? " for " + loc.getDescription() + " by Citation" : "");
} else if (command.equals("annuallongterm")) {
    list = RecordSeries.getItemList(myConn, 0, false, "retentioncode, description", "L");
    reportTitle = "Annual Long-term Records for Disposition";
}

myConn.release();
%>
  

  <%= reportTitle %><br>
  <br>
<p>&nbsp; </p>
<table border=1 cellpadding=0 cellspacing=0>
<tr><td>Record Series</td><td>Common Name</td><td>Quality Designation</td><td>Access Restrictions</td><td>Citation</td><td>Requirement</td><td>Flags</td></tr>

<%
if (list.length == 0) {
    %><tr><td colspan=7>Empty Report</td></tr><%
}
for (int i=0; i<list.length; i++) {
    Crosswalk [] cw = list[i].getCrosswalkSet();
    if (cw.length == 0) {
    %><tr><td vailign=top><%= list[i].getDescription() %></td><td vailign=top>n/a</td><td>n/a</td><td>n/a</td>
    <td><%= list[i].getCitationDescription() %>, <%= list[i].getCutoffDescription() %>, <%= list[i].getRetentionPeriodDescription() %></td><td>n/a</td>
    <td><%= ((list[i].getCommonFlag().equals("T")) ? "Common, " : "") %><%= list[i].getRetentionGroupFlagDescription() %></td></tr>
    <%
    } else {
        for (int j=0; j<cw.length; j++) {
            %><tr><td vailign=top><%= list[i].getDescription() %></td><td vailign=top><%= cw[j].getName() %></td><td vailign=top><%= cw[j].getQADesignation() %></td>
            <td><%= cw[j].getAccessRestrictions() %></td><td><%= list[i].getCitationDescription() %>, <%= list[i].getCutoffDescription() %>, <%= list[i].getRetentionPeriodDescription() %></td>
            <td><%= cw[j].getRequirements() %><%= ((cw[j].getRequirementsOther() != null) ? "<br>" + cw[j].getRequirementsOther() : "") %></td>
            <td><%= ((list[i].getCommonFlag().equals("T")) ? "Common, " : "") %><%= list[i].getRetentionGroupFlagDescription() %><%= ((cw[j].getUnscheduled().equals("T")) ? ", Unscheduled" : "") %>
            <%= ((cw[j].getFreezeHoldFlag().equals("T")) ? ", Freeze Hold: " + cw[j].getFreezeHoldFlagText() : "") %></td></tr>
            <%
        }
    }
}
%>
</table>



<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
