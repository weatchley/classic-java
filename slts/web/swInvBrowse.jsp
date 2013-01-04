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
String listNotFiltered = ((request.getParameter("nofilterlist")  != null) ? (String) request.getParameter("nofilterlist") : "F");
String [] appInv = AppInventory.getAppNameList(myConn, ((listNotFiltered.equals("T")) ? false : true));
String [] servInv = ServiceInventory.getServiceNameList(myConn, ((listNotFiltered.equals("T")) ? false : true));

String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
String tempText = "";
%>
<% //System.out.println("swProduct.jsp-Got Here 1-" + Utils.genDateID()); %>
  

  <h3>Installed Software:</h3><br>

<b>Show All Applications and Services</b> <input type=checkbox name=nofilterlist value='T' <%= ((listNotFiltered.equals("T")) ? "checked" : "") %>> &nbsp; <a href="javascript:doRefreshLists()"><i>Refresh View</i></a><br><br>
<script language=javascript><!--

    function doRefreshLists() {
        submitForm("swInvBrowse.jsp","")
    }
//-->
</script>

<% tempText = "<b>Installed Applications</b> (" + appInv.length + ")"; %>
<rdc:doSection name="applist" title="<%= tempText %>" isOpen="false" >
<table border=0>
<tr><td valign=bottom><b>Name</b></td>
</tr>

<%
for (int i=0; i < appInv.length; i++) {

    
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td valign=top><a href="javascript:doViewApp('<%= appInv[i] %>')"><%= appInv[i] %></a></td>
    </tr>
    <%
}
%>
</table>
</rdc:doSection >

<% tempText = "<b>Installed Services</b> (" + servInv.length + ")"; %>
<rdc:doSection name="servlist" title="<%= tempText %>" isOpen="false" >
<table border=0>
<tr><td valign=bottom><b>Name</b></td>
</tr>
<%
for (int i=0; i < servInv.length; i++) {
    
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td valign=top><a href="javascript:doViewServ('<%= servInv[i] %>')"><%= servInv[i] %></a></td>
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
