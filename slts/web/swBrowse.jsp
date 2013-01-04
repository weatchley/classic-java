<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");
usr = new Person((String) session.getAttribute("user.name"));
DbConn myConn = new DbConn("csi");
String statusFilter = ((request.getParameter("statusfilter")  != null) ? (String) request.getParameter("statusfilter") : "active");
SWProduct [] items = SWProduct.getItemList(myConn, statusFilter);
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
%>
  

  <h4>Software Products</h4><br>
<% String [] swStatusList = SWProduct.getStatusList(myConn); %>
Status: <select name=statusfilter>
    <% for (int i=0; i<swStatusList.length; i++) { %>
        <option value="<%= swStatusList[i] %>"<%= ((swStatusList[i].equals(statusFilter)) ? " selected" : "" ) %>><%= swStatusList[i] %></option>
    <% } %>
</select> &nbsp; &nbsp; <input type=button name=refresh value="Refresh" onClick="submitForm('swBrowse.jsp', 'browse');">

<table border=0>
<tr><td valign=bottom><b>ID</b></td><td valign=bottom><b>Name</b></td><td valign=bottom><b>Version</b></td>
<td valign=bottom align=center><b>License<br>Count</b></td><td valign=bottom align=center><b>License<br>Used</b></td>
<td valign=bottom align=center><b>License<br>Available</b></td>
</tr>
<%
for (int i=0; i < items.length; i++) {
    
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td valign=top><a href="javascript:doView(<%= items[i].getID() %>)"><%= items[i].getID() %></a>
    <rdc:hasPermission permission="12-mansw" > <a href="javascript:doEdit(<%= items[i].getID() %>)"><font size=-3><i>(edit)</i></font></a></rdc:hasPermission></td>
    <td valign=top><a href="javascript:doView(<%= items[i].getID() %>)"><%= items[i].getName() %></a></td>
    <td valign=top><%= ((items[i].getVersion() != null) ? items[i].getVersion() : "&nbsp;") %></td>
    <td valign=top align=center><%= ((items[i].isUnlimited()) ? "Unlimited" : items[i].getTotalCount()) %></td>
    <td valign=top align=center><%= items[i].getUsedCount() %></td>
    <td valign=top align=center><%= ((items[i].isUnlimited()) ? "Unlimited" : items[i].getAvailCount()) %></td>
    </tr>
    <%
}
%>
</table>

<script language=javascript><!--

    function doView(id) {
        document.main.id.value = id;
        submitForm("swProductForm.jsp","")
    }

    function doEdit(id) {
        document.main.id.value = id;
        submitForm("swProductForm.jsp","update")
    }
//-->
</script>






<%
myConn.release();
%>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
