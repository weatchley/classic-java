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

String nameLookup = ((request.getParameter("id")  != null) ? (String) request.getParameter("id") : "0");
AppInventory [] appInv = AppInventory.getItemList(myConn, 0, nameLookup, false, "ai.name, ai.description, c.name");

String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
int i=0;
String tempText = "";

%>
  

  <h4>Inventoried Application Display</h4><br>

<table border=0>
<tr><td valign=bottom><b>Computer</b></td><td valign=bottom><b>Name</b></td><td valign=bottom><b>Version</b></td><td valign=bottom><b>Description</b></td>
<td valign=bottom><b>Publisher</b></td>
</tr>

<%
for (i=0; i < appInv.length; i++) {
    
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td valign=top><a href="javascript:doView(<%= appInv[i].getComputerID() %>)"><%= appInv[i].getComputerName() %></a></td>
    <td valign=top><%= appInv[i].getName() %></td>
    <td valign=top><%= ((appInv[i].getVersion() != null) ? appInv[i].getVersion() : "&nbsp;") %></td>
    <td valign=top><%= ((appInv[i].getDescription() != null) ? appInv[i].getDescription() : "&nbsp;") %></td>
    <td valign=top><%= ((appInv[i].getPublisher() != null) ? appInv[i].getPublisher() : "&nbsp;") %></td>
    </tr>
    <%
}
%>
</table>

<script language=javascript><!--

    function doView(id) {
        document.main.id.value = id;
        submitForm("computer.jsp","")
    }
//-->
</script>


<br>





<%
myConn.release();
%>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
