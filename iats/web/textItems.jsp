<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
<script src=/common/javascript/utilities.js></script>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->

<h3>TextItem Management</h3>
<%
DbConn myConn = new DbConn("csi");
//usr = (Person) session.getAttribute("user.person");
//long[] lTmp = null;
//TextItem [] ti =  TextItem.getItemList(myConn, lTmp, "text");
TextItem [] ti =  TextItem.getItemList(myConn, "text");
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
//Position pos = (Position) session.getAttribute("user.position");
perMap = (HashMap) session.getAttribute("user.permissionmap");

%>

<a href="javascript:submitForm('updateTextItem.jsp', 'add')">Add New TextItem</a><br><br>

<table border=1 cellpadding=0 cellspacing=0>
<tr bgcolor=#ff77ff><td>&nbsp;</td><td><b>Date&nbsp;1&nbsp;/&nbsp;Date&nbsp;2</b></td><td><b>Text/Link</b></td><td><b>Status</b></td></tr>
<tr><td colspan=4></td></tr>

<%
int i = 0;
while (i < ti.length) {
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td>
    <a href="javascript:updateTextItem(<%= ti[i].getID() %>)"><font size=-1>update</font></a><br>
    <a href="javascript:dropTextItem(<%= ti[i].getID() %>)"><font size=-1>drop</font></a>
    </td>
    <td valign=top><%= ((ti[i].getDate1()!=null) ? (Utils.dateToString(ti[i].getDate1())) : "&nbsp;") %><br>
    <%= ((ti[i].getDate2()!=null) ? (Utils.dateToString(ti[i].getDate2())) : "&nbsp;") %></td>
    <td valign=top><%= ti[i].getText() %><br>
    <% if (ti[i].getLink()!=null) { %>
    <a href=<%= ti[i].getLink() %> target=_blank><%= ti[i].getLink() %></a>
    <% } else { %>
    &nbsp;
    <% } %>
    <td valign=top><%= ((ti[i].getStatus()!=null) ? (ti[i].getStatus().toString()) : "&nbsp;") %></td>
    </tr>
    <%
    i++;
}

%>

</table>
<input type=hidden name=systemid value=0>

<script language=javascript><!--

function updateTextItem (val){
// update a TextItem
    document.main.id.value = val;
    submitForm('updateTextItem.jsp', 'update');
}

function dropTextItem (val){
// update a TextItem
    document.main.id.value = val;
    submitForm('doTextItem', 'drop');
}


//-->
</script>



<% myConn.release(); %>


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
