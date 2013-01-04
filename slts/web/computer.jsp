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

String compIDs = ((request.getParameter("id")  != null) ? (String) request.getParameter("id") : "0");
int compID = Integer.parseInt(compIDs);
String listNotFiltered = ((request.getParameter("nofilterlist")  != null) ? (String) request.getParameter("nofilterlist") : "F");
Computer comp = new Computer(compID,myConn);
AppInventory [] appInv = AppInventory.getItemList(myConn, compID, ((listNotFiltered.equals("T")) ? false : true));
ServiceInventory [] servInv = ServiceInventory.getItemList(myConn, compID, ((listNotFiltered.equals("T")) ? false : true));

String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
int i=0;
String tempText = "";

%>
  

  <h4>Computer Display</h4><br>

<table border=0>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>ID:</td><td><%= comp.getComputerID() %></td></tr>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Name:</td><td><%= comp.getName() %></td></tr>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>License OS Key:</td><td><%= comp.getLicOsKey() %></td></tr>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Domain Name:</td><td><%= comp.getDomainName() %></td></tr>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Serial Number:</td><td><%= comp.getSerialNum() %></td></tr>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>OS:</td><td><%= comp.getOs() %></td></tr>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Manufacuterer:</td><td><%= comp.getManuf() %></td></tr>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Last Invtoried:</td><td><%= comp.getLastInventory() %></td></tr>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Model Number:</td><td><%= comp.getModelNum() %></td></tr>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Product Name:</td><td><%= comp.getProdName() %></td></tr>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>IP Address:</td><td><%= comp.getIpAddress() %></td></tr>
</table>

<br>
<b>Show All Applications and Services</b> <input type=checkbox name=nofilterlist value='T' <%= ((listNotFiltered.equals("T")) ? "checked" : "") %>> &nbsp; <a href="javascript:doRefreshLists()"><i>Refresh View</i></a><br><br>
<script language=javascript><!--

    function doRefreshLists() {
        document.main.id.value = <%= compID %>;
        submitForm("computer.jsp","")
    }
//-->
</script>

<% tempText = "<b>Installed Applications</b> (" + appInv.length + ")"; %>
<rdc:doSection name="applist" title="<%= tempText %>" isOpen="false" >
<table border=0>
<tr><td valign=bottom><b>List Name</b></td><td valign=bottom><b>Name</b></td><td valign=bottom><b>Version</b></td><td valign=bottom><b>Description</b></td>
<td valign=bottom><b>Publisher</b></td>
</tr>

<%
for (i=0; i < appInv.length; i++) {
    
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td valign=top><a href="javascript:doViewApp('<%= appInv[i].getListName() %>')"><%= appInv[i].getListName() %></a></td>
    <td valign=top><a href="javascript:doViewApp('<%= appInv[i].getListName() %>')"><%= appInv[i].getName() %></a></td>
    <td valign=top><%= ((appInv[i].getVersion() != null) ? appInv[i].getVersion() : "&nbsp;") %></td>
    <td valign=top><%= ((appInv[i].getDescription() != null) ? appInv[i].getDescription() : "&nbsp;") %></td>
    <td valign=top><%= ((appInv[i].getPublisher() != null) ? appInv[i].getPublisher() : "&nbsp;") %></td>
    </tr>
    <%
}
%>
</table>
</rdc:doSection >

<% tempText = "<b>Installed Services</b> (" + servInv.length + ")"; %>
<rdc:doSection name="servlist" title="<%= tempText %>" isOpen="false" >
<table border=0>
<tr><td valign=bottom><b>List Name</b></td><td valign=bottom><b>Name</b></td><td valign=bottom><b>Display Name</b></td><td valign=bottom><b>Description</b></td>
</tr>
<%
for (i=0; i < servInv.length; i++) {
    
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td valign=top><a href="javascript:doViewServ('<%= servInv[i].getListName() %>')"><%= servInv[i].getListName() %></a></td>
    <td valign=top><a href="javascript:doViewServ('<%= servInv[i].getListName() %>')"><%= servInv[i].getName() %></a></td>
    <td valign=top><%= ((servInv[i].getDisplayName() != null) ? servInv[i].getDisplayName() : "&nbsp;") %></td>
    <td valign=top><%= ((servInv[i].getDescription() != null) ? servInv[i].getDescription() : "&nbsp;") %></td>
    </tr>
    <%
}
%>
</table>
</rdc:doSection >

<script language=javascript><!--

    function doViewApp(id) {
        document.main.id.value = id;
        submitForm("swInvAppDisplay.jsp","")
    }

    function doViewServ(id) {
        document.main.id.value = id;
        submitForm("swInvServDisplay.jsp","")
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
