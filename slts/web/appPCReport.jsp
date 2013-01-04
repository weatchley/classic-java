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
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};

HashMap apps = AppInventory.getAppNameHash(myConn, true, false);
String [] appList = request.getParameterValues("applist");
HashMap selected = new HashMap(((appList != null) ? (appList.length + 1) : 0));
if (appList != null) {
    for (int i=0; i<appList.length; i++) {
        selected.put(appList[i], appList[i]);
    }
}

%>
<% //System.out.println("Got Here 1"); %>  
<h4>PC Application Report/Selection</h4>

<%= HtmlUtils.doDualSelect("applist", formName, apps, selected, "<b>Available</b>", "<b>Selected</b>") %>

<input type=hidden name=nextscript value="AppPCReport.jsp">

Report Output Format: &nbsp; 

<input type=radio name=reporttype value='html' checked>HTML &nbsp; <input type=radio name=reporttype value='pdf'>PDF &nbsp; <input type=radio name=reporttype value='excel'>MS-Excel<br>

<input type=button name=submitform value="Submit" onClick="javascript:doFormSubmit()">


<script language=javascript><!--

    function doFormSubmit() {
        for (index=0; index < <%= formName %>.applist.length-1;index++) {
            <%= formName %>.applist.options[index].selected = true;
        }
        //alert('Not yet implemented');
        if (<%= formName %>.reporttype[0].checked) {
            submitForm("appPCReport.jsp","");
        } else if (<%= formName %>.reporttype[1].checked) {
            submitFormNewWindow("appPCReport.iText.jsp","");
        } else if (<%= formName %>.reporttype[2].checked) {
            submitFormNewWindow("doAppPCReportXLS","");
        }
    }

//-->
</script>

<% 
if (appList != null) {
    Computer [] items = Computer.getItemList(myConn, true, null, appList);
    %>
  
    <hr 75%>
    <h4>Computers With Selected Software Installed</h4><br>

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
    
} %>

<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
