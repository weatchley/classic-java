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
ServiceInventory [] servInv = ServiceInventory.getItemList(myConn, 0, nameLookup, false, "si.name, si.display_name, c.name");

String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
int i=0;
String tempText = "";

%>
  

  <h4>Inventoried Service Display</h4><br>

<table border=0>
<tr><td valign=bottom><b>Computer</b></td><td valign=bottom><b>Name</b></td><td valign=bottom><b>Display Name</b></td><td valign=bottom><b>Description</b></td>
</tr>

<%
for (i=0; i < servInv.length; i++) {
    
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td valign=top><a href="javascript:doView(<%= servInv[i].getComputerID() %>)"><%= servInv[i].getComputerName() %></a></td>
    <td valign=top><%= servInv[i].getName() %></td>
    <td valign=top><%= ((servInv[i].getDisplayName() != null) ? servInv[i].getDisplayName() : "&nbsp;") %></td>
    <td valign=top><%= ((servInv[i].getDescription() != null) ? servInv[i].getDescription() : "&nbsp;") %></td>
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
