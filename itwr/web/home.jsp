<HTML>

<HEAD>
<TITLE>IT Work Request System</TITLE>
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
String wrtypeS = ((request.getParameter("wrtype")  != null && !request.getParameter("wrtype").equals("")) ? (String) request.getParameter("wrtype") : "0");
int wrtype = Integer.parseInt(wrtypeS);

DbConn myConn = new DbConn("csi");
// set up, get page content

WorkRequest [] pending = WorkRequest.getItemList(myConn, "id DESC", wrtype, "Pending");
WorkRequest [] review = WorkRequest.getItemList(myConn, "id DESC", wrtype, "In Review");
WorkRequest [] approved = WorkRequest.getItemList(myConn, "id DESC", wrtype, "Approved");
WorkRequest [] rejected = WorkRequest.getItemList(myConn, "id DESC", wrtype, "Rejected");
WorkRequest [] closed = WorkRequest.getItemList(myConn, "id DESC", wrtype, "Closed");
WorkRequest [] withdrawn = WorkRequest.getItemList(myConn, "id DESC", wrtype, "Withdrawn");

WorkRequestType [] wrTypeList = WorkRequestType.getItemList(myConn);

myConn.release();

%>
<table forder=0 width=650><tr><td>
<h3 align=center> Browse Work Requests </h3>
Work Request Type: <select name=wrtype size=1><option value=0>All</option>
<% for (int i=0; i<wrTypeList.length; i++) { %>
    <option value=<%= wrTypeList[i].getID() %>
    <%= ((wrtype == wrTypeList[i].getID()) ? " selected" : "") %>>
    <%= wrTypeList[i].getDescription() %></option>
<% } %>
</select>
 &nbsp; &nbsp; <input type=button value=Refresh onClick="submitForm('home.jsp', 'none')"><br><br>

<%= HtmlUtils.doSection("pending1", "Pending - " + ((pending != null) ? pending.length : "0"), buildWRList(pending, pos, perMap), false) %><br>
<%= HtmlUtils.doSection("review1", "In Review - " + ((review != null) ? review.length : "0"), buildWRList(review, pos, perMap), false) %><br>
<%= HtmlUtils.doSection("approved1", "Approved - " + ((approved != null) ? approved.length : "0"), buildWRList(approved, pos, perMap), false) %><br>
<%= HtmlUtils.doSection("rejected1", "Rejected - " + ((rejected != null) ? rejected.length : "0"), buildWRList(rejected, pos, perMap), false) %><br>
<%= HtmlUtils.doSection("closed1", "Closed - " + ((closed != null) ? closed.length : "0"), buildWRList(closed, pos, perMap), false) %><br>
<%= HtmlUtils.doSection("withdrawn1", "Withdrawn - " + ((withdrawn != null) ? withdrawn.length : "0"), buildWRList(withdrawn, pos, perMap), false) %><br>

<script language="javascript">
<!--
     function submitEditRequest(script, command, id) {
         document.main.id.value = id;
         submitForm(script, command);
     }
-->
</script>

</td></tr></table>


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
<%!
private String buildWRList(WorkRequest [] wrs, Position pos, HashMap perMap) {
    boolean editable = false;
    String out = "";
    out += "<table cellpadding=4 cellspacing=0 border=1 align=center width=600>\n";
    out += "<tr bgcolor=#f0f0f0>" +
      "<td align=center><font color=#000099>Id</font></td>" +
      "<td align=center><font color=#000099>Contact</font></td>" +
      "<td align=center><font color=#000099>Work&nbsp;Request&nbsp;Type</font></td>" +
      "<td align=center><font color=#000099>Details</font></td></tr>\n";
    if (wrs != null) {
        for (int i=0; i<wrs.length; i++) {
            String disp = wrs[i].getDisposition();
            editable = false;
            if (pos != null) {
                if (disp.equals("Pending") && pos.belongsTo(((Long) perMap.get("8-updatepending")).longValue())) {
                    editable = true;
                }
                if (disp.equals("Pending") && pos.belongsTo(((Long) perMap.get("8-updatetype")).longValue())) {
                    editable = true;
                }
                if (disp.equals("In Review") && pos.belongsTo(((Long) perMap.get("8-updatereview")).longValue())) {
                    editable = true;
                }
                if (disp.equals("In Review") && pos.belongsTo(((Long) perMap.get("8-updatetype")).longValue())) {
                    editable = true;
                }
                if (disp.equals("Approved") && pos.belongsTo(((Long) perMap.get("8-updateapproved")).longValue())) {
                    editable = true;
                }
            }
            out += "   <tr bgcolor=#f0f0f0>" +
              "<td valign=top><font color=#000099><a href=\"javascript:submitEditRequest('wrForm.jsp','" + ((editable) ? "update" : "view") + 
                    "'," + wrs[i].getID() + ")\"><b>WR" + lPad(Integer.toString(wrs[i].getID()), "0", 4) + "</b></a></font></td>" +
              "<td valign=top><font color=#000099><font color=black>" + wrs[i].getContact() + "</font></font></td>" +
              "<td valign=top><font color=#000099><font color=black>" + wrs[i].getTypeDescription() + "</font></font></td>" +
              "<td valign=top><font color=#000099><font color=black>" + HtmlUtils.getDisplayString(wrs[i].getDetails(),200) + "</font></font></td></tr>\n";
        }
    }
    out += "</table>\n";
    
    return (out);
}
%>


