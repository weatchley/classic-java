<HTML>

<HEAD>
<TITLE>Records Plan Management System</TITLE>
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

DbConn myConn = new DbConn("csi");
RecordSeries [] list = null;
    FOrganization loc = null;


String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
String [] locString = request.getParameterValues("titlebarlookup");
//System.out.println("recordSeries.jsp - Got Here 1" + " command: " + command + ", locString: " + locString[0]);
int locID = 0; 
String sort = "rs.description";

if (command.equals("locationview")) {
    locID = Integer.parseInt(locString[0]); 
    loc = new FOrganization(locID, myConn);
}
if (command.equals("locationbrowse")) {
    sort = "rs.location";
}
list = RecordSeries.getItemList(myConn, locID, true, sort);

myConn.release();

%>
<script language="javascript">
    var itemValueStore = new Object();
    itemValueStore.item0 = "";

</script>
  

  <h3>Record Series Browse Page</h3>
  <% if (locID > 0) { %>
      Record Series from "<%= loc.getDescription() %>"<br><br><br>
  <% } %>
  
<input type=hidden name=reporttype value="pdf">
<input type=hidden name=archiveflag value="false">
<input type=hidden name=displayflag value="true">
<input type=hidden name=rsid value="0">

<%
int dispCount = 0;
if (!command.equals("new")) {
    %><table border=1 cellpadding=2 cellspacing=0>
    <% if (command.equals("locationbrowse")) { %>
    <tr bgcolor=#cccccc><td><b>Location</b></td><td><b>Description</b></td><td>&nbsp;</td></tr>
    <% } else { %>
    <tr bgcolor=#cccccc><td><b>Description</b></td><td><b>Location</b></td><td>&nbsp;</td></tr>
    <% }
    for (int i=0; i<list.length; i++) {
        String text = list[i].getDescription();
        text = text.replaceAll("'", "\\\\'");
        text = text.replaceAll("[\\n\\r]","\\\\n");
        boolean doDisp = true;
        %>
        <script language="javascript" type="text/javascript">
            itemValueStore.item<%= list[i].getID() %> = '<%= text %>';
        </script>
        <%
        //if (isAuthenticated && isAdmin) {
        //    doDisp = true;
        //}
        if (doDisp) {
            dispCount++;
            %>
            <% if (command.equals("locationbrowse")) { %>
                <tr><td valign=top><%= list[i].getLocationDescription() %></td><td valign=top><a href="javascript:doView(<%= list[i].getID() %>)"><%= list[i].getDescription() %></a></td><td valign=top>
            <% } else { %>
                <tr><td valign=top><a href="javascript:doView(<%= list[i].getID() %>)"><%= list[i].getDescription() %></a></td><td valign=top><%= list[i].getLocationDescription() %></td><td valign=top>
            <% } %>
            <a href="javascript:doReport(<%= list[i].getID() %>)">Report</a>
            <% 
            if (isAuthenticated && isAdmin) {
                %>&nbsp;&nbsp;&nbsp;<a href="javascript:doReport(<%= list[i].getID() %>, 'true')">Archive</a>
                &nbsp;&nbsp;&nbsp;<a href="javascript:doEdit(<%= list[i].getID() %>)">Edit</a>
                <% if (list[i].getCrosswalkSet().length == 0 && 1==2) { %>&nbsp;&nbsp;&nbsp;<a href="javascript:doDrop(<%= list[i].getID() %>)">Delete</a><% }
            }
            %>
            </td></tr>
            <%
        }
    }
    if (dispCount == 0) {
        %>
        <tr><td>No Record Series Available</td></tr>
        <%
    }
    %></table><%
}

%>

<% if (isAuthenticated) { %>
        <% if (isAdmin) { %>
            <p><a href="javascript:doNew()">New</a></p>
        <% } %>

<script language=javascript><!--

    // A utility function that returns true if a string contains only
    // whitespace characters.
    function isblank(s) {
        if (s.length == 0) return true;
        for(var i = 0; i < s.length; i++) {
            var c = s.charAt(i);
            if ((c != ' ') && (c != '\n') && (c != '\t') && (c !='\r')) return false;
        }
        return true;
    }

    function doEdit(id) {
        document.main.id.value = id;
        submitForm("recordSeriesForm.jsp","update");
    }

    function doNew() {
        document.main.id.value = 0;
        submitForm("recordSeriesForm.jsp","new");
    }

    
    //function doDrop(id) {
    //    document.main.rsid.value = id;
    //    eval('var message = itemValueStore.item' + id + ';');
    //    if (confirm('Delete Record Series ' + message + '?')) {
    //        submitFormResults("doRecordSeries","drop");
    //    }
    //}
    
    //-->
</script>
<% } %>
<script language=javascript><!--
    
    function doReport(id, flag) {
        document.main.id.value = id;
        document.main.reporttype.value='pdf';
        document.main.displayflag.value='true';
        if (flag=='true') {
            document.main.archiveflag.value='true';
        } else {
            document.main.archiveflag.value='false';
        }
        submitFormNewWindow("recordSeriesReport.jsp","report");
    }

    function doView(id) {
        document.main.id.value = id;
        submitForm("recordSeriesForm.jsp","view")
    }
//-->
</script>
<%
%>




<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
