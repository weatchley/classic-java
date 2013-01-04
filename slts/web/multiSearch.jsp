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

searchText = searchText.replaceAll("\n|\t|\r|\f", ",");
searchText = searchText.replaceAll(",,", ",");

%>

<table border=0><tr><td>
<h3 align=center>Multi-Search Report</h3>

<table align=center cellpadding=1 cellspacing=0 border=0 width=610>
<tr><td align=left valign=top>Search&nbsp;Text:&nbsp;<br><br><i>Enter items one per line or separated by commas(,)</i></td><td><textarea name=searchtext cols=60 rows=5></textarea></td></tr>
<tr><td align=left>Filtered:&nbsp;</td><td><input type=checkbox name=searchfiltered value='T' <%= ((isFiltered) ? "checked" : "") %>></td></tr>
<tr><td colspan=2><table align=center cellpadding=1 cellspacing=0 border=0 width=95%><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>
Searches: Installed Applications</td></tr>
</table></td></tr>

<tr><td align=center colspan=2>
<input type=radio name=reporttype value='html' checked>HTML &nbsp; <input type=radio name=reporttype value='excel'>MS-Excel<br>
<!--input type=button name=submitbutton value="Search" onClick="submitForm('search.jsp', '')"-->
<!--input type=submit name=submitbutton value="Search"-->
<input type=button name=submitform value="Search" onClick="javascript:doFormSubmit()">
<% if (!searchText.equals("")) { %>
    <br>Current Search:<br><%= searchText %>
<% } %>
</td></tr>
</table>
</td></tr></table>
<script language=javascript><!--
//       document.main.target = '_self';
//       document.main.action = 'multiSearch.jsp';
//-->
</script>

<script language=javascript><!--

    function doFormSubmit() {
        //alert('Not yet implemented');
        var msg = "";
        if (isblank(<%= formName %>.searchtext.value)) {
            msg += "Search Text is Required.\n";
        }
        
        if (msg != "") {
            alert (msg);
        } else {
            if (<%= formName %>.reporttype[0].checked) {
                submitForm("multiSearch.jsp","");
            } else if (<%= formName %>.reporttype[1].checked) {
                submitFormNewWindow("doPCSearchReportXLS","");
            }
        }
    }

//-->
</script>


<% if (!searchText.equals("")) { %>
    
    <% 
//System.out.println("multiSearch.jsp - Got Here 1" + " - " + Utils.genDateID());
    String [] searchSet = searchText.split(",");
    for (int i=0; i< searchSet.length; i++) {
        searchSet[i] = searchSet[i].trim();
    }
    HashSet compList = new HashSet();
    HashSet itemList = new HashSet();
    HashMap [] data = new HashMap [searchSet.length];
    for (int i=0; i<searchSet.length; i++) {
        data[i] = new HashMap();
        AppInventory [] appList = AppInventory.getItemList(myConn, 0, null, isFiltered, "ai.listname,c.name", false, searchSet[i]);
        for (int j=0; j<appList.length; j++) {
            String tmp = "";
            if (data[i] != null && data[i].get(appList[j].getComputerName()) != null) {
                tmp = (String) data[i].get(appList[j].getComputerName()) + "<br>";
            }
            data[i].put(appList[j].getComputerName(), (tmp + appList[j].getListName() + ((appList[j].getVersion() != null) ? "-" + appList[j].getVersion() : "")));
            compList.add(appList[j].getComputerName());
            itemList.add(appList[j].getListName() + ((appList[j].getVersion() != null) ? "-" + appList[j].getVersion() : ""));
        }
        ServiceInventory [] servList = ServiceInventory.getItemList(myConn, 0, null, isFiltered, "si.listname,c.name", false, searchSet[i]);
        for (int j=0; j<servList.length; j++) {
            String tmp = "";
            if (data[i] != null && data[i].get(servList[j].getComputerName()) != null) {
                tmp = (String) data[i].get(servList[j].getComputerName()) + "<br>";
            }
            data[i].put(servList[j].getComputerName(), (tmp + servList[j].getListName()));
            compList.add(servList[j].getComputerName());
            itemList.add(servList[j].getListName());
        }
    
    }
    
    String comps [] = (String []) compList.toArray (new String [compList.size ()]);
    Arrays.sort(comps, String.CASE_INSENSITIVE_ORDER);
    
    String items [] = (String []) itemList.toArray (new String [itemList.size ()]);
    Arrays.sort(items, String.CASE_INSENSITIVE_ORDER);
    
    
    
    %>

    <table border=0><tr><td>

    <% tempText = "<b>Computers With Items Found </b> (" + comps.length + ")"; %>
    <rdc:doSection name="comp-applist" title="<%= tempText %>" isOpen="false" >
        <table cellpadding=1 cellspacing=0 border=1>
        <tr bgcolor=#a0e0c0><td valign=bottom><b>Computer</b></td>
        <%
        for (int i=0; i<searchSet.length; i++) {
            %>
            <td valign=bottom><%= searchSet[i] %></td>
            <%
        }
        %>
        </tr>
        <%
        for (int i=0; i<comps.length; i++) {
            %>
            <tr bgcolor=<%= bgcolors[i%2] %>><td valign=top><%= comps[i] %></td>
            <%
            for (int j=0; j<searchSet.length; j++) {
                %>
                <td valign=top><%= ((data[j].get(comps[i]) != null) ? data[j].get(comps[i]) : "&nbsp;") %></td>
                <%
            }
            %>
            </tr>
            <%
        }
        %>
        </table>
    </rdc:doSection >
        

    <% tempText = "<b>Items Found </b> (" + items.length + ")"; %>
    <rdc:doSection name="applist" title="<%= tempText %>" isOpen="false" >
        <table cellpadding=1 cellspacing=0 border=1>
        <tr bgcolor=#a0e0c0><td valign=bottom><b>Item</b></td></tr>
        <%
        for (int i=0; i<items.length; i++) {
            %>
            <tr bgcolor=<%= bgcolors[i%2] %>><td valign=top><%= items[i] %></td></tr>
            <%
        }
        %>
        </table>
    </rdc:doSection >
        
    
    </td></tr></table>

<% } %>





<%
myConn.release();
%>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
