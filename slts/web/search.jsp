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
String tempText = null;
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};

String swpSearchList = "";
HashSet hs = new HashSet();


String searchText = ((request.getParameter("searchtext") != null && !request.getParameter("searchtext").equals("")) ? (String) request.getParameter("searchtext") : "");
boolean isFiltered  = ((request.getParameter("searchfiltered") != null && request.getParameter("searchfiltered").equals("T")) ? true : false);

%>


<h3 align=center>Search </h3>

<table align=center cellpadding=1 cellspacing=0 border=0 width=610>
<tr><td align=left>Search&nbsp;Text:&nbsp;</td><td><input type=text name=searchtext size=100 maxlength=200 value=""></td></tr>
<tr><td align=left>Filtered:&nbsp;</td><td><input type=checkbox name=searchfiltered value='T' <%= ((isFiltered) ? "checked" : "") %>></td></tr>
<tr><td colspan=2><table align=center cellpadding=1 cellspacing=0 border=0 width=95%><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>
Searches: Software Products, Installed Applications, and Installed Services</td></tr>
</table></td></tr>
<tr><td align=center colspan=2>
<!--input type=button name=submitbutton value="Search" onClick="submitForm('search.jsp', '')"-->
<input type=submit name=submitbutton value="Search">
<% if (!searchText.equals("")) { %>
    <br>Current Search:<br><%= searchText %>
<% } %>
</td></tr>
</table>
<script language=javascript><!--
       document.main.target = '_self';
       document.main.action = 'search.jsp';
//-->
</script>


<% if (!searchText.equals("")) { %>
    
    <% 
    Attachment [] attList = Attachment.getItemList(myConn, 0, 0, searchText);
    for (int i=0; i<attList.length; i++) {
        hs.add(attList[i].getProductID());
    }
    Comments [] commList = Comments.getItemList(myConn, 0, 0, searchText);
    for (int i=0; i<commList.length; i++) {
        hs.add(commList[i].getProductID());
    }
    SWProduct [] swpList = SWProduct.getItemList(myConn, false, false, null, searchText);
    for (int i=0; i<swpList.length; i++) {
        hs.add(swpList[i].getID());
    }
    SWTransactions [] swtList = SWTransactions.getItemList(myConn, 0, searchText);
    for (int i=0; i<swtList.length; i++) {
        hs.add(swtList[i].getProductID());
    }
    int j = 0;
//System.out.println("search.jsp - Got Here 1, " + swpSearchList);
    for (Iterator i = hs.iterator(); i.hasNext();) {
        int tmp = ((Integer) i.next()).intValue();
        swpSearchList += ((j > 0) ? ", " : "") + tmp;
        j++;
    }
    swpList = SWProduct.getItemList(myConn, false, false, null, null, swpSearchList);
    
    %>
    <table border=0><tr><td>
    <% tempText = "<b>Software Products Found </b> (" + swpList.length + ")"; %>
    <rdc:doSection name="swplist" title="<%= tempText %>" isOpen="false" >
        <!--p align=center>Search Results: Found <%= ((swpList.length != 1) ? swpList.length + " Items" : swpList.length + " Item") %> For<br><%= searchText %></p-->

        <table cellpadding=1 cellspacing=0 border=1>
        <tr bgcolor=#a0e0c0><td valign=bottom><b>ID</b></td><td valign=bottom><b>Name</b></td><td valign=bottom><b>Version</b></td>
        <td valign=bottom align=center><b>License<br>Count</b></td><td valign=bottom align=center><b>License<br>Used</b></td>
        <td valign=bottom align=center><b>License<br>Available</b></td>
        </tr>

        <%
        for (int i=0; i< swpList.length; i++) {
        %>
            <tr bgcolor=<%= bgcolors[i%2] %>>
            <td valign=top><a href="javascript:doView(<%= swpList[i].getID() %>)"><%= swpList[i].getID() %></a>
            <rdc:hasPermission permission="12-mansw" > <a href="javascript:doEdit(<%= swpList[i].getID() %>)"><font size=-3><i>(edit)</i></font></a></rdc:hasPermission></td>
            <td valign=top><a href="javascript:doView(<%= swpList[i].getID() %>)"><%= swpList[i].getName() %></a></td>
            <td valign=top><%= ((swpList[i].getVersion() != null) ? swpList[i].getVersion() : "&nbsp;") %></td>
            <td valign=top align=center><%= ((swpList[i].isUnlimited()) ? "Unlimited" : swpList[i].getTotalCount()) %></td>
            <td valign=top align=center><%= swpList[i].getUsedCount() %></td>
            <td valign=top align=center><%= ((swpList[i].isUnlimited()) ? "Unlimited" : swpList[i].getAvailCount()) %></td>
            </tr>
            <%
        }
        %>
        </table>
    </rdc:doSection >


    <% 
        LinkedHashSet lhs = new LinkedHashSet();
        AppInventory [] appList = AppInventory.getItemList(myConn, 0, null, isFiltered, "ai.listname,c.name", false, searchText);
        for (int i=0; i<appList.length; i++) {
            lhs.add(appList[i].getListName());
        }
    %>
    <% tempText = "<b>Installed Applications Found </b> (" + lhs.size() + ")"; %>
    <rdc:doSection name="applist" title="<%= tempText %>" isOpen="false" >
        <table cellpadding=1 cellspacing=0 border=1>
        <tr bgcolor=#a0e0c0><td valign=bottom><b>Name</b></td>
        </tr>
        <%
    
        j = 0;
        for (Iterator i = lhs.iterator(); i.hasNext();) {
            String tmp = (String) i.next();
            %>
                <tr bgcolor=<%= bgcolors[j%2] %>>
                <td valign=top><a href="javascript:doViewApp('<%= tmp %>')"><%= tmp %></a></td>
                </tr>

            <%
            j++;
        }
        %>
        </table>
    </rdc:doSection >
    <%
    
        lhs = new LinkedHashSet();
        ServiceInventory [] servList = ServiceInventory.getItemList(myConn, 0, null, isFiltered, "si.listname,c.name", false, searchText);
        for (int i=0; i<servList.length; i++) {
            lhs.add(servList[i].getListName());
        }
    %>
    <% tempText = "<b>Installed Services Found </b> (" + lhs.size() + ")"; %>
    <rdc:doSection name="servlist" title="<%= tempText %>" isOpen="false" >
        <table cellpadding=1 cellspacing=0 border=1>
        <tr bgcolor=#a0e0c0><td valign=bottom><b>Name</b></td>
        </tr>
        <%
        j = 0;
        for (Iterator i = lhs.iterator(); i.hasNext();) {
            String tmp = (String) i.next();
            %>
                <tr bgcolor=<%= bgcolors[j%2] %>>
                <td valign=top><a href="javascript:doViewServ('<%= tmp %>')"><%= tmp %></a></td>
                </tr>

            <%
            j++;
        }
        %>
        </table>
    </rdc:doSection >
    
    <% tempText = "<b>Installed Applications/Computers Found </b> (" + appList.length + ")"; %>
    <rdc:doSection name="app-complist" title="<%= tempText %>" isOpen="false" >
        <table cellpadding=1 cellspacing=0 border=1>
        <tr bgcolor=#a0e0c0><td valign=bottom><b>Computer</b></td><td valign=bottom><b>Application</b></td>
        <td valign=bottom align=center><b>Version</b></td>
        </tr>
        </tr>
        <%
        for (int i=0; i<appList.length; i++) {
            %>
                <tr bgcolor=<%= bgcolors[i%2] %>>
                <td valign=top><a href="javascript:doViewComputer('<%= appList[i].getComputerID() %>')"><%= appList[i].getComputerName() %></a></td>
                <td valign=top><a href="javascript:doViewServ('<%= appList[i].getListName() %>')"><%= appList[i].getListName() %></a></td>
                <td valign=top><%= ((appList[i].getVersion() != null) ? appList[i].getVersion() : "&nbsp;") %></td>
                </tr>

            <%
        }
        %>
        </table>
    </rdc:doSection >
    
    <% tempText = "<b>Installed Services/Computers Found </b> (" + servList.length + ")"; %>
    <rdc:doSection name="serv-complist" title="<%= tempText %>" isOpen="false" >
        <table cellpadding=1 cellspacing=0 border=1>
        <tr bgcolor=#a0e0c0><td valign=bottom><b>Computer</b></td><td valign=bottom><b>Service</b></td>
        </tr>
        </tr>
        <%
        for (int i=0; i<servList.length; i++) {
            %>
                <tr bgcolor=<%= bgcolors[i%2] %>>
                <td valign=top><a href="javascript:doViewComputer('<%= servList[i].getComputerID() %>')"><%= servList[i].getComputerName() %></a></td>
                <td valign=top><a href="javascript:doViewServ('<%= servList[i].getListName() %>')"><%= servList[i].getListName() %></a></td>
                </tr>

            <%
        }
        %>
        </table>
    </rdc:doSection >
    </td></tr></table>

<script language=javascript><!--

    function doView(id) {
        document.main.id.value = id;
        submitForm("swProductForm.jsp","")
    }

    function doEdit(id) {
        document.main.id.value = id;
        submitForm("swProductForm.jsp","update")
    }

    function doViewApp(id) {
        document.main.id.value = id;
        submitForm("swInvAppDisplay.jsp","")
    }

    function doViewServ(id) {
        document.main.id.value = id;
        submitForm("swInvServDisplay.jsp","")
    }

    function doViewComputer(id) {
        document.main.id.value = id;
        submitForm("computer.jsp","")
    }

//-->
</script>

<% } %>





<%
myConn.release();
%>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
