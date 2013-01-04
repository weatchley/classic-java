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

<h3>Collection Management</h3>
<%
DbConn myConn = new DbConn("csi");
//Person usr = (Person) session.getAttribute("user.person");
//long[] lTmp = null;
//TextItem [] ti =  TextItem.getItemList(myConn, lTmp, "text");
UList [] ul =  UList.getList(myConn, false);
UHash [] uh =  UHash.getList(myConn, false);
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
//pos = (Position) session.getAttribute("user.position");
perMap = (HashMap) session.getAttribute("user.permissionmap");

%>

<script language=javascript><!--
    document.body.style.cursor = 'wait';
//-->
</script>
<rdc:isAuthenticated doOpposite="true" >
<script language=javascript><!--
    document.body.style.cursor = 'default';
    document.location='index.jsp';
//-->
</script>
</rdc:isAuthenticated>

<a href="javascript:submitForm('updateUList.jsp', 'add')">Add New UList Collection</a><br><br>
<a href="javascript:submitForm('updateUHash.jsp', 'add')">Add New UHash Collection</a><br><br>

<table border=1 cellpadding=0 cellspacing=0>
<tr bgcolor=#ff77ff><td>&nbsp;</td><td><b>Type&nbsp;/&nbsp;Subtype</b></td><td><b>Description</b></td><td><b>Creation/Expiration&nbsp;Date</b></td><td><b>Status</b></td></tr>
<tr><td colspan=4></td></tr>

<%
int i = 0;
while (i < ul.length) {
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td valign=top>
    <a href="javascript:updateUList(<%= ul[i].getID() %>)"><font size=-1>update</font></a><br>
    <a href="#"><font size=-1>copy</font></a><br>
    <!--<a href="javascript:dropTextItem(<%= ul[i].getID() %>)"><font size=-1>drop</font></a> -->
    </td>
    <td valign=top><%= ((ul[i].getType()!=null) ? (ul[i].getType()) : "&nbsp;") %><br>
    <%= ((ul[i].getSubType()!=null) ? (ul[i].getSubType()) : "&nbsp;") %></td>
    <td valign=top><%= ((ul[i].getDescription()!=null) ? (ul[i].getDescription()) : "&nbsp;") %></td>
    
    <td valign=top><%= ((ul[i].getCreationDate()!=null) ? (Utils.dateToString(ul[i].getCreationDate())) : "&nbsp;") %><br>
    <%= ((ul[i].getExpirationDate()!=null) ? (Utils.dateToString(ul[i].getExpirationDate())) : "&nbsp;") %></td>
    <td valign=top><%= ((ul[i].getStatus()!=null) ? (ul[i].getStatus().toString()) : "&nbsp;") %></td>
    </tr>
    <%
    i++;
}
i = 0;
while (i < uh.length) {
    %>
    <tr bgcolor=<%= bgcolors[i%2] %>>
    <td valign=top>
    <a href="javascript:updateUHash(<%= uh[i].getID() %>)"><font size=-1>update</font></a><br>
    <a href="javascript:copyUHash(<%= uh[i].getID() %>)"><font size=-1>copy</font></a><br>
    <!--<a href="javascript:dropTextItem(<%= uh[i].getID() %>)"><font size=-1>drop</font></a> -->
    </td>
    <td valign=top><%= ((uh[i].getType()!=null) ? (uh[i].getType()) : "&nbsp;") %><br>
    <%= ((uh[i].getSubType()!=null) ? (uh[i].getSubType()) : "&nbsp;") %></td>
    <td valign=top><%= ((uh[i].getDescription()!=null) ? (uh[i].getDescription()) : "&nbsp;") %></td>
    
    <td valign=top><%= ((uh[i].getCreationDate()!=null) ? (Utils.dateToString(uh[i].getCreationDate())) : "&nbsp;") %><br>
    <%= ((uh[i].getExpirationDate()!=null) ? (Utils.dateToString(uh[i].getExpirationDate())) : "&nbsp;") %></td>
    <td valign=top><%= ((uh[i].getStatus()!=null) ? (uh[i].getStatus().toString()) : "&nbsp;") %></td>
    </tr>
    <%
    i++;
}

%>

</table>
<input type=hidden name=systemid value=0>
<input type=hidden name=copytoname value=''>

<script language=javascript><!--

function updateUList (val){
// update a UList
    document.main.id.value = val;
    submitForm('updateUList.jsp', 'update');
}

function updateUHash (val){
// update a UHash
    document.main.id.value = val;
    submitForm('updateUHash.jsp', 'update');
}

function copyUHash (val){
// update a UHash
    name = prompt("Name for new UHash: ", "");
    if (name != "") {
        document.main.id.value = val;
        document.main.copytoname.value = name;
        submitFormResults('doUHash', 'copy');
    } else {
        alert('Name must be entered to make copy.');
    }
}

function dropUList (val){
// Drop a UList
    document.main.id.value = val;
    //submitForm('do?', 'drop');
}


//-->
</script>



<% myConn.release(); %>


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>
<script language=javascript><!--
    document.body.style.cursor = 'default';
//-->
</script>

</BODY>

</HTML>
