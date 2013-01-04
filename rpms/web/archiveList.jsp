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
Archive [] list = null;


String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
//System.out.println("archiveList.jsp - Got Here 1" + " command: " + command);
list = Archive.getItemList(myConn);

myConn.release();

%>
<script language="javascript">
    var itemValueStore = new Object();
    itemValueStore.item0 = "";

</script>
  

  <h3>Archived Reports</h3>
  

<%
int dispCount = 0;
if (isAdmin) {
    %><table border=1 cellpadding=2 cellspacing=0>
    <tr><td align=left>Title</td><td align=center>Date</td><td align=left>Comments</td></tr><%
    for (int i=0; i<list.length; i++) {
        %>
        <% dispCount++; %>
        <tr><td valign=top><a href="javascript:doView(<%= list[i].getID() %>)"><%= list[i].getName() %></a></td>
        <td valign=top width=130><%= Utils.dateToString(list[i].getDateTime(), "MM/dd/yyyy HH:mm:ss") %></td>
        <td valign=top><%= list[i].getCommentText() %></td>
        </tr>
        <%
    }
    if (dispCount == 0) {
        %>
        <tr><td>No Archive Available</td></tr>
        <%
    }
    %></table><%
}

%>

<script language=javascript><!--

    function doView(id) {
        document.main.id.value = id;
        submitFormNewWindow("displayArchive.jsp","view")
    }
//-->
</script>
<%
%>




<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
