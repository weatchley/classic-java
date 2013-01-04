<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
<script src=/common/javascript/utilities.js></script>
<script src=/common/javascript/prototype.js></script>
  <script language="javascript" src="/common/javascript/xc2/config/xc2_default.js"></script>
  <script language="javascript" src="/common/javascript/xc2/script/xc2_inpage.js"></script>
  <link rel=stylesheet href="/common/javascript/xc2/css/xc2_default.css" type="text/css">
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->

<h3>UHash Update</h3>
<%
String command = request.getParameter("command");
String id = ((request.getParameter("id") != null && !request.getParameter("id").equals("")) ? (String) request.getParameter("id") : "0");
//long systemID = 0;
String key = "";
String description = "";
DbConn myConn = new DbConn("csi");
//Sys [] sys = Sys.getSystems(myConn);
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
UHash uh = new UHash();
//pos = (Position) session.getAttribute("user.position");
//perMap = (HashMap) session.getAttribute("user.permissionmap");
//String date1 = "";
//String date2 = "";
String status = "";
Set keys = null;
if (command.equals("update")) {
    uh.lookup(Long.parseLong(id), myConn);
    status = ((uh.getStatus() != null) ? uh.getStatus() : "");
    keys = uh.getKeys();
}
//Sys [] systems = Sys.getSystems(myConn);

%>
<rdc:isAuthenticated doOpposite="true" >
<script language=javascript><!--
    document.location='index.jsp';
//-->
</script>
</rdc:isAuthenticated>

<table border=0>
<tr><td><b>Description</b></td><td>
<input type=text name=description size=60 maxlength=80 value="<%= uh.getDescription() %>"></td></tr>
<tr><td><b>Status</b> &nbsp; &nbsp;</td><td><input type=text name=status size=30 maxlength=30 value="<%= status %>"></td></tr>
<tr><td valign=top><b>Contents</b> &nbsp; &nbsp;</td><td><table border=0>
<% 
int itemCount = 0;
if (keys != null) {
//   String [] items = (String []) keys.toArray();
   Object [] items = keys.toArray();
   for (int i=0; i<items.length; i++) { 
       key = (String) items[i];
       long item = uh.get(key);
       UNID uid = new UNID(item, myConn);
       %><tr><td valign=top>Item<br><input type=text name=keyname<%= i %> size=15 maxlength=50 value="<%= key %>">: </td><td valign=top><%
       String myType = uid.getType();
       myType = (myType != null) ? myType : "";%>
       <input type=hidden name=itemtype<%= i %> value="<%= myType %>">
       <%
       if (myType.equals("ulist")) {
           UList ulTmp = new UList(item, myConn);
           %></td><td valign=top>Collection:  &nbsp; UList - Description<br><input type=text size=50 maxlength=50 name=collectiondescription<%= i %> value="<%= ulTmp.getDescription() %>"> &nbsp; <a href="javascript:updateUList(<%= item %>)"> &nbsp; <font size=-1>update</font></a><br>
           <input type=hidden name=itemtype<%= i %> value="ulist">
           <input type=hidden name=collectionid<%= i %> value=<%= ulTmp.getID() %>><%
       } else if (myType.equals("uhash")) {
           UHash uhTmp = new UHash(item, myConn);
           %></td><td valign=top>Collection:  &nbsp; UHash - Description<br><input type=text size=50 maxlength=50 name=collectiondescription<%= i %> value="<%= uhTmp.getDescription() %>"> &nbsp; <a href="javascript:updateUHash(<%= item %>)"> &nbsp; <font size=-1>update</font></a><br>
           <input type=hidden name=itemtype<%= i %> value="uhash">
           <input type=hidden name=collectionid<%= i %> value=<%= uhTmp.getID() %>><%
       } else if (myType.equals("textitem")) {
           TextItem tiTmp = new TextItem(item, myConn);
           %></td><td valign=top><input type=hidden name=textitemid<%= i %> value=<%= tiTmp.getID() %>>
           TextItem - Date 1: <span id="holder-tidate1-<%= i %>"><input type=text name=textitemdate1-<%= i %> value="<%= ((tiTmp.getDate1() != null) ? Utils.dateToString(tiTmp.getDate1()) : "") %>" size=12 maxlength=10 onfocus="this.blur();showCalendar('',this,this,'','holder-tidate1-<%= i %>',0,30,1);"></span><br>
           TextItem - Date 2: <span id="holder-tidate2-<%= i %>"><input type=text name=textitemdate2-<%= i %> value="<%= ((tiTmp.getDate2() != null) ? Utils.dateToString(tiTmp.getDate2()) : "") %>" size=12 maxlength=10 onfocus="this.blur();showCalendar('',this,this,'','holder-tidate2-<%= i %>',0,30,1);"></span><br>
           TextItem - Text<br><textarea rows=3 cols=70 name=textitem<%= i %>><%= tiTmp.getText() %></textarea><br>
           TextItem - Link<br><input type=text size=100 maxlength=200 name=textitemlink<%= i %> value="<%= ((tiTmp.getLink() != null) ? tiTmp.getLink() : "" ) %>">
           <%
           
       } else {
           %></td><td valign=top>Type: <%= myType %>, ID: <%= uid.getID() %><%
       }
       %></td></tr><%
   }
   itemCount = items.length;
   %>
   <%
}
%>
<input type=hidden name=itemcount value=<%= itemCount %>>
<tr id=postItemRow><td colspan=3><table cellpadding=0 cellspacing=0 border=0 id=postItemTable><tr><td><a href=javascript:addTextItem()>Add TextItem</a> &nbsp; <a href=javascript:addUList()>Add UList</a> &nbsp; <a href=javascript:addUHash()>Add UHash</a></td></tr></table></td></tr>
<script language=javascript><!--
function addTextItem() {
// add an entry to the item table
    var newItemRow = "";
    var items = document.main.itemcount.value;
    items;
    document.main.itemcount.value = (items - 0) + 1;
    newItemRow += "<tr><td valign=top>Item<br><input type=text name=keyname" + items + " size=15 maxlength=50 value=\"\">: </td><td valign=top>\n";
    newItemRow += "   <input type=hidden name=itemtype" + items + " value=\"textitem\">\n";
    newItemRow += "   </td><td valign=top><input type=hidden name=textitemid" + items + " value=0>\n";
    newItemRow += "       TextItem - Date 1: <span id=\"holder-tidate1-" + items + "\"><input type=text name=textitemdate1-" + items + " value=\"\" size=12 maxlength=10 onfocus=\"this.blur();showCalendar('',this,this,'','holder-tidate1-" + items + "',0,30,1);\"></span><br>\n";
    newItemRow += "       TextItem - Date 2: <span id=\"holder-tidate2-" + items + "\"><input type=text name=textitemdate2-" + items + " value=\"\" size=12 maxlength=10 onfocus=\"this.blur();showCalendar('',this,this,'','holder-tidate2-" + items + "',0,30,1);\"></span><br>\n";
    newItemRow += "       TextItem - Text<br><textarea rows=3 cols=70 name=textitem" + items + "></textarea><br>\n";
    newItemRow += "       TextItem - Link<br><input type=text size=100 maxlength=200 name=textitemlink" + items + " value=\"\">\n";
    newItemRow += "       </td></tr>\n";
    Element.insert("postItemRow", { before: newItemRow });
    
}

function addUList() {
// add an entry to the item table
    var newItemRow = "";
    var items = document.main.itemcount.value;
    items;
    document.main.itemcount.value = (items - 0) + 1;
    newItemRow += "<tr><td valign=top>Item<br><input type=text name=keyname" + items + " size=15 maxlength=50 value=\"\">: </td><td valign=top>\n";
    newItemRow += "   <input type=hidden name=itemtype" + items + " value=\"ulist\">\n";
    newItemRow += "   </td><td valign=top><input type=hidden name=collectionid" + items + " value=0>\n";
    newItemRow += "    Collection:  &nbsp; UList - Description<br><input type=text size=50 maxlength=50 name=collectiondescription" + items + " value=\"\">\n";
    newItemRow += "       </td></tr>\n";
    Element.insert("postItemRow", { before: newItemRow });
    
}

function addUHash() {
// add an entry to the item table
    var newItemRow = "";
    var items = document.main.itemcount.value;
    items;
    document.main.itemcount.value = (items - 0) + 1;
    newItemRow += "<tr><td valign=top>Item<br><input type=text name=keyname" + items + " size=15 maxlength=50 value=\"\">: </td><td valign=top>\n";
    newItemRow += "   <input type=hidden name=itemtype" + items + " value=\"uhash\">\n";
    newItemRow += "   </td><td valign=top><input type=hidden name=collectionid" + items + " value=0>\n";
    newItemRow += "    Collection:  &nbsp; UHash - Description<br><input type=text size=50 maxlength=50 name=collectiondescription" + items + " value=\"\">\n";
    newItemRow += "       </td></tr>\n";
    Element.insert("postItemRow", { before: newItemRow });
    
}
    //-->
</script>
</table></td></tr>
<tr><td colspan=2><input type=button value='Submit' onClick="verifySubmit(document.main)"></td></tr>
</table>


<script language=javascript><!--

function verifySubmit (f){
// javascript form verification routine
    var msg = "";
    if (isblank(f.description.value)) {
        msg += "Description Value must be entered.\n";
    }
    for (i=0; i < f.itemcount.value; i++) {
        var code = ""
            + "if (f.itemtype" + i + ".value == 'textitem') {\n"
            + "    if (isblank(f.keyname" + i +".value) && f.textitemid" + i + ".value > 0) {\n"
            + "        msg += \"Item name can not be deleted.\\n\";\n"
            + "    }\n"
            + "    if (isblank(f.keyname" + i +".value) && (!isblank(f.textitem" + i +".value) || !isblank(f.textitemlink" + i +".value))) {\n"
            + "        msg += \"Text Item must have name.\\n\";\n"
            + "    }\n"
            + "    if (!isblank(f.keyname" + i +".value) && isblank(f.textitem" + i +".value)) {\n"
            + "        msg += \"TextItem Text must be entered for '\" + f.keyname" + i +".value + \"'.\\n\";\n"
            + "    }\n"
            + "}\n"
            + "";
        //alert(code);
        eval(code);
    }
    if (msg != "") {
      alert (msg);
    } else {
        f.id.value = <%= id %>;
        submitFormResults('doUHash', '<%= command %>');
    }
}

//-->
</script>

<script language=javascript><!--

function updateUList (val){
    document.main.id.value = val;
    submitForm('updateUList.jsp', 'update');
}

function updateUHash (val){
    document.main.id.value = val;
    submitForm('updateUHash.jsp', 'update');
}


//-->
</script>

<input type=hidden name=nextscript value='collections.jsp'>
<% 
myConn.release();
%>


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
