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
Crosswalk [] list = null;


String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
//System.out.println("browseCN.jsp - Got Here 1" + " command: " + command);
String sort = "name";

list = Crosswalk.getItemList(myConn, sort);

myConn.release();

%>
<script language="javascript">
    var itemValueStore = new Object();
    itemValueStore.item0 = "";

</script>
  

  <h3>Common Name Browse Page</h3>
  
<input type=hidden name=reporttype value="pdf">
<input type=hidden name=archiveflag value="false">
<input type=hidden name=displayflag value="true">
<input type=hidden name=rsid value="0">

<%
int dispCount = 0;

    %><table border=1 cellpadding=2 cellspacing=0>
    <tr bgcolor=#cccccc><td><b>Common Name</b></td><td>&nbsp;</td></tr>
    <%
    for (int i=0; i<list.length; i++) {
        dispCount++;
        %>
        <tr><td valign=top><a href="javascript:doView(<%= list[i].getRSID() %>)"><%= list[i].getName() %></a></td><td valign=top>
        <a href="javascript:doReport(<%= list[i].getRSID() %>)">Report</a></td></tr>
        <%
    }
    if (dispCount == 0) {
        %>
        <tr><td>No Record Series Available</td></tr>
        <%
    }
    %></table><%


%>
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
