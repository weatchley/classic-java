<HTML>

<HEAD>
<TITLE>ITS Software Software Work Request System</TITLE>
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
// set up, get page content

SoftwareWorkRequest [] pendingS = SoftwareWorkRequest.getItemList(myConn, "id DESC", "Pending");
SoftwareWorkRequest [] reviewS = SoftwareWorkRequest.getItemList(myConn, "id DESC", "In Review");
SoftwareWorkRequest [] approvedS = SoftwareWorkRequest.getItemList(myConn, "id DESC", "Approved");
SoftwareWorkRequest [] rejectedS = SoftwareWorkRequest.getItemList(myConn, "id DESC", "Rejected");
SoftwareWorkRequest [] closedS = SoftwareWorkRequest.getItemList(myConn, "id DESC", "Closed");
SoftwareWorkRequest [] withdrawnS = SoftwareWorkRequest.getItemList(myConn, "id DESC", "Withdrawn");

myConn.release();

%>

<h3 align=center> Browse Work Requests </h3>
<br><br>

<%= HtmlUtils.doSection("pending1", "Pending - " + ((pendingS != null) ? pendingS.length : "0"), buildWRList(pendingS, pos, perMap), false) %><br>
<%= HtmlUtils.doSection("review1", "In Review - " + ((reviewS != null) ? reviewS.length : "0"), buildWRList(reviewS, pos, perMap), false) %><br>
<%= HtmlUtils.doSection("approved1", "Approved - " + ((approvedS != null) ? approvedS.length : "0"), buildWRList(approvedS, pos, perMap), false) %><br>
<%= HtmlUtils.doSection("rejected1", "Rejected - " + ((rejectedS != null) ? rejectedS.length : "0"), buildWRList(rejectedS, pos, perMap), false) %><br>
<%= HtmlUtils.doSection("closed1", "Closed - " + ((closedS != null) ? closedS.length : "0"), buildWRList(closedS, pos, perMap), false) %><br>
<%= HtmlUtils.doSection("withdrawn1", "Withdrawn - " + ((withdrawnS != null) ? withdrawnS.length : "0"), buildWRList(withdrawnS, pos, perMap), false) %><br>

<script language="javascript">
<!--
     function submitEditRequest(script, command, id) {
         document.main.id.value = id;
         submitForm(script, command);
     }
-->
</script>




<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
<%!
private String buildWRList(SoftwareWorkRequest [] wrs, Position pos, HashMap perMap) {
    boolean editable = false;
    String out = "";
    out += "<table cellpadding=4 cellspacing=0 border=1 align=center width=600>\n";
    out += "<tr bgcolor=#f0f0f0>" +
      "<td align=center><font color=#000099>Id</font></td>" +
      "<td align=center><font color=#000099>Contact</font></td>" +
      "<td align=center><font color=#000099>Details</font></td></tr>\n";
    if (wrs != null) {
        for (int i=0; i<wrs.length; i++) {
            String disp = wrs[i].getDisposition();
            editable = false;
            if (pos != null) {
                if (disp.equals("Pending") && pos.belongsTo(((Long) perMap.get("10-updatepending")).longValue())) {
                    editable = true;
                }
                if (disp.equals("In Review") && pos.belongsTo(((Long) perMap.get("10-updatereview")).longValue())) {
                    editable = true;
                }
                if (disp.equals("Approved") && pos.belongsTo(((Long) perMap.get("10-updateapproved")).longValue())) {
                    editable = true;
                }
            }
            out += "   <tr bgcolor=#f0f0f0>" +
              "<td><font color=#000099><a href=\"javascript:submitEditRequest('swrForm.jsp','" + ((editable) ? "update" : "view") + 
                    "'," + wrs[i].getID() + ")\"><b>SWR" + lPad(Integer.toString(wrs[i].getID()), "0", 4) + "</b></a></font></td>" +
              "<td><font color=#000099><font color=black>" + wrs[i].getContact() + "</font></font></td>" +
              "<td><font color=#000099><font color=black>" + HtmlUtils.getDisplayString(wrs[i].getRequirements(),200) + "</font></font></td></tr>\n";
        }
    }
    out += "</table>\n";
    
    return (out);
}
%>


