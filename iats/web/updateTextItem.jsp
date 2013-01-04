<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
<script src=/common/javascript/utilities.js></script>
  <script language="javascript" src="/common/javascript/xc2/config/xc2_default.js"></script>
  <script language="javascript" src="/common/javascript/xc2/script/xc2_inpage.js"></script>
  <link rel=stylesheet href="/common/javascript/xc2/css/xc2_default.css" type="text/css">
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->

<h3>Text Item Update</h3>
<%
String command = request.getParameter("command");
String id = ((request.getParameter("id") != null && !request.getParameter("id").equals("")) ? (String) request.getParameter("id") : "0");
long systemID = 0;
String key = "";
String description = "";
DbConn myConn = new DbConn("csi");
Sys [] sys = Sys.getSystems(myConn);
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
TextItem ti = new TextItem();
pos = (Position) session.getAttribute("user.position");
perMap = (HashMap) session.getAttribute("user.permissionmap");
String date1 = "";
String date2 = "";
String status = "";
if (command.equals("update")) {
    ti.getInfo(Long.parseLong(id), myConn);
    date1 = ((ti.getDate1() != null) ? Utils.dateToString(ti.getDate1()) : "");
    date2 = ((ti.getDate2() != null) ? Utils.dateToString(ti.getDate2()) : "");
    status = ((ti.getStatus() != null) ? ti.getStatus() : "");
}
//Sys [] systems = Sys.getSystems(myConn);

myConn.release();
%>

<rdc:isAuthenticated doOpposite="true" >
<script language=javascript><!--
    document.location='index.jsp';
//-->
</script>
</rdc:isAuthenticated>

<table border=0>
<tr><td><b>Date&nbsp;1</b></td><td>
<span id="holder1"><input type=text name=date1 value="<%= date1 %>" size=12 maxlength=10 onfocus="this.blur();showCalendar('',this,this,'','holder1',0,30,1);"></span><br>
</td></tr>
<tr><td><b>Date&nbsp;2</b></td><td>
<span id="holder2"><input type=text name=date2 value="<%= date2 %>" size=12 maxlength=10 onfocus="this.blur();showCalendar('',this,this,'','holder2',0,30,1);"></span><br>
</td></tr>
<tr><td valign=top><b>Text</b> &nbsp; &nbsp;</td><td><textarea name=text cols=80 rows=10><%= ((ti.getText() != null) ? ti.getText() : "") %></textarea></td></tr>
<tr><td><b>Link</b> &nbsp; &nbsp;</td><td><input type=text name=link size=100 maxlength=200 value="<%= ((ti.getLink() != null) ? ti.getLink() : "")  %>">
 <a href="javascript:testLink();">test</a></td></tr>
<tr><td><b>Status</b> &nbsp; &nbsp;</td><td><input type=text name=status size=30 maxlength=30 value="<%= status %>"></td></tr>
<tr><td colspan=2><input type=button value='Submit' onClick="verifySubmit(document.main)"></td></tr>
</table>


<script language=javascript><!--

function verifySubmit (f){
// javascript form verification routine
    var msg = "";
    if (isblank(f.text.value)) {
        msg += "Text Value must be entered.\n";
    }
    if (msg != "") {
      alert (msg);
    } else {
        f.id.value = <%= id %>;
        submitFormResults('doTextItem', '<%= command %>');
    }
}

function testLink () {
    var myDate = new Date();
    var winName = myDate.getTime();
    var newwin = window.open('',winName);
    newwin.creator = self;
    newwin.location = document.<%= formName %>.link.value;
}

//-->
</script>

<input type=hidden name=nextscript value='textItems.jsp'>



<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
