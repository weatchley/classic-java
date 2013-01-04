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
Computer [] items = Computer.getItemList(myConn);
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
%>
  

  <h4>Computers</h4><br>

<table border=0>
<tr><td><b>ID</b></td><td><b>Name</b></td><td><b>Domain</b></td><td><b>Manufacturer</b></td><td><b>Model</b></td><td><b>Product Name</b></td>
</tr>
<%
for (int i=0; i < items.length; i++) {
    
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td valign=top><a href="javascript:doView(<%= items[i].getComputerID() %>)"><%= items[i].getComputerID() %></a></td>
    <td valign=top><a href="javascript:doView(<%= items[i].getComputerID() %>)"><%= items[i].getName() %></a></td>
    <td valign=top><%= items[i].getDomainName() %></td>
    <td valign=top><%= items[i].getManuf() %></td>
    <td valign=top><%= ((items[i].getModelNum() != null) ? items[i].getModelNum() : "&nbsp;") %></td>
    <td valign=top><%= items[i].getProdName() %></td>
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




<%
myConn.release();
%>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
