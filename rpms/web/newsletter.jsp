<HTML>

<HEAD>
<TITLE>Records Plan Management System</TITLE>
  <LINK href="css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="css/prstyle.css" type="text/css">
  <link rel=stylesheet href="javascript/xc2/css/xc2_default.css" type="text/css">
  <script language="javascript" src="javascript/xc2/config/xc2_default.js"></script>
  <script language="javascript" src="javascript/xc2/script/xc2_inpage.js"></script>
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="headerSetup.inc" %>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");

DbConn myConn = new DbConn("csi");
// setup, get page structure and content
String newsletterName = "RPMS-Newsletter";
UHash newsletter = new UHash(newsletterName, myConn);
UList ul = new UList(newsletter.get("list"), myConn);
ul.lookup(ul.getID(), myConn);
long[] items = ul.getItemsArray();
TextItem[] list = TextItem.getItemList(myConn, items, "date1 desc");
TextItem editPriv = new TextItem(newsletter.get("editpriv"), myConn);
TextItem appPriv = new TextItem(newsletter.get("approvepriv"), myConn);
boolean hasEdit = false;
boolean hasApp = false;

// determine if user has edit or approval priv
if (isAuthenticated) {
    hasEdit = pos.belongsTo(((Long) perMap.get(editPriv.getText())).longValue());
    hasApp = pos.belongsTo(((Long) perMap.get(appPriv.getText())).longValue());
}
// get selected newsletter (if any)
String nlIDs = ((request.getParameter("id")  != null) ? (String) request.getParameter("id") : "0");
String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
long nlID = Long.parseLong(nlIDs);
long newsletterID = newsletter.getID();
int current = -1;
String lnDate = "";
String lnText = "";
String status = "";
if (!command.equals("new")) {
    // determine the current newsletter either the most recent approved, or the one passed in 
    // "external" newsletters can not be current as they can not be displayed in-line 
    for (int i=0; i<list.length; i++) {
//System.out.println("nl.jsp - Got Here 1" +"- current: " + current + ", nlID: " + nlID + " command: " + command);
        String temp = list[i].getStatus();
        if (nlID != 0 && nlID == list[i].getID()) {
            current = i;
        }
        if (current == -1 && nlID == 0 && temp != null && temp.equals("approved") && !list[i].getText().equals("external")) {
            current = i;
            nlID = list[i].getID();
        }
    }
    // get contents of selected newsletter 
    if (current >= 0 && list != null && list.length>0) {
        lnDate = Utils.dateToString(list[current].getDate1());
        lnText = list[current].getText();
        status = list[current].getStatus();
    } else {
        if (current == -1 && hasEdit) {
            if (list.length>0) {
                current = 0;
                nlID = list[0].getID();
                lnDate = Utils.dateToString(list[current].getDate1());
                lnText = list[current].getText();
                status = list[current].getStatus();
            } else {
                command = "new";
            }
        } else {
            lnText = "No Newsletter Available";
        }
    }
}

myConn.release();

%>
<!-- include code for WYSIWYG editor -->
<!-- tinyMCE -->
<script language="javascript" type="text/javascript" src="javascript/tiny_mce/tiny_mce.js"></script>
<script language="javascript" type="text/javascript">
	tinyMCE.init({
	mode : "textareas",
	theme : "advanced",
	plugins : "style,table,searchreplace,contextmenu,paste,fullscreen,nonbreaking,layer",
	theme_advanced_buttons1 : "bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,outdent,indent,hr,bullist,numlist,undo,redo,link,unlink,styleselect,fontselect,fontsizeselect",
	theme_advanced_buttons2 : "forecolor,backcolor,|,cut,copy,paste,pastetext,pasteword,|,sub,sup,separator,search,replace,separator,tablecontrols,separator,fullscreen<%= ((!ProductionStatus.equals("prod")) ? ",code" : "") %>,|,insertlayer,moveforward,movebackward,absolute",
	theme_advanced_buttons3 : "",
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "left",
	theme_advanced_statusbar_location : "bottom",
	theme_advanced_resizing : true,
	theme_advanced_path : false,
	extended_valid_elements : "a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]"
	});
</script>
<!-- /tinyMCE -->
  

  <h3>Information Exchange</h3><br>

<span id=noneditarea>
<!-- display newsletter content -->
  <%= lnDate %><br>
  <%= lnText %><br>

<p>&nbsp; </p>

<% //System.out.println("Got Here 1" +"- current: " + current + ", nlID: " + nlID + " command: " + command + " Status: " + status); %>
<% if (isAuthenticated) { %>
    <% if (list != null && list.length>0) { %>
        <!-- add management links -->
        <% if (hasEdit || hasApp) { %>
            <hr width=80% >
        <% } %>
        <% if (hasEdit) { %>
            <p><% if (nlID != 0 && !command.equals("new")) { %><a href="javascript:showHideEdit()">Edit</a><% } %>
            <% if (nlID != 0 && !command.equals("new") && (status == null || (!status.equals("approved")&&!status.equals("approvalrequested")))) { %>&nbsp; &nbsp; <a href="javascript:doRequestApproval()">Request Approval</a><% } %> 
            &nbsp; &nbsp; <a href="javascript:doNew()">New</a></p>
        <% } %>
        <% if (hasApp && list != null && list.length>0 && status != null && status.equals("approvalrequested") && nlID != 0) { %>
            <p><a href="javascript:doApprove()">Approve</a></p>
        <% } %>
    <% } %>

<!-- add management javascript functions -->
<script language=javascript><!--
    function showHideEdit () {
        mySection = document.getElementById('noneditarea');
        mySection2 = document.getElementById('editarea');
        if (mySection.style.display=='none') {
            mySection.style.display='block';
            mySection2.style.display='none';
            document.main.reset();
            hideCalendars();
        } else {
            mySection.style.display='none';
            mySection2.style.display='block';
        }
    }

    function doNew() {
        document.main.id.value = 0;
        submitForm("newsletter.jsp","new")
    }

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

    function doSave() {
        if (isblank(document.main.date1.value) || isblank(document.main.text.value)) {
            alert('Both Date and Text must be entered.');
        } else {
            document.main.id.value = <%= nlID %>;
            submitFormResults("doNewsletter",<%= ((command.equals("new")) ? "'add'" : "'update'") %>);
        }
    }
    
    function doRequestApproval() {
        document.main.id.value = <%= nlID %>;
        //alert('Not Ready Yet');
        submitFormResults("doNewsletter","approvalrequest")
    }
    
    function doApprove() {
        document.main.id.value = <%= nlID %>;
        submitFormResults("doNewsletter","approve")
    }
    
    //-->
</script>
<% } %>
<script language=javascript><!--

    function doView(id) {
        document.main.id.value = id;
        submitForm("newsletter.jsp","view")
    }
//-->
</script>
<p>Archive:<br>
<%
int archiveCount = 0;
// display a list of newsletters, show only approved ones unless logged in with admin priv
for (int i=0; i<list.length; i++) {
    String myStatus = list[i].getStatus();
    if (myStatus == null || !myStatus.equals("approved")) {
        if (hasEdit || (hasApp && myStatus.equals("approvalrequested"))) {
            %><i><%= ((archiveCount == 0) ? "" : ", ") %><a href="javascript:doView(<%= list[i].getID() %>)"><%= Utils.dateToString(list[i].getDate1()) %>*</a></i><%
            archiveCount++;
        }
    } else {
        %><%= ((archiveCount == 0) ? "" : ", ") %><%
        if (!list[i].getText().equals("external")) {
            %><a href="javascript:doView(<%= list[i].getID() %>)"<%
        } else {
            // external newsletters are displayed in a new window
            %><a href="<%= list[i].getLink() %>" target="_blank" <%
        }
        %>><%= Utils.dateToString(list[i].getDate1()) %></a><%
        archiveCount++;
    }
}
%>
</p>
</span>
<% if (isAuthenticated) { %>
<!-- create edit form -->
<span id=editarea style="display:'none'">
<input type=hidden name=nlid value="<%= nlID %>">
<input type=hidden name=newslettername value="<%= newsletterName %>">
<input type=hidden name=nextscript value="newsletter.jsp">
<table border=0>
<tr><td>Date: <span id="holder1"><input type=text name=date1 value="<%= lnDate %>" size=12 maxlength=10 onfocus="this.blur();showCalendar('',this,this,'','holder1',0,30,1);"></span></td>
<td align=right><a href="javascript:doSave()">Save</a> <%= ((command.equals("new")) ? "" : "&nbsp; &nbsp; <a href=\"javascript:showHideEdit()\">Cancel</a>") %></td></tr>
<tr><td colspan=2>Text:
<textarea name=text cols=80 rows=30>
<%= lnText %>
</textarea></td></tr>
<tr><td colspan=2><a href="javascript:doSave()">Save</a> <%= ((command.equals("new")) ? "" : "&nbsp; &nbsp; <a href=\"javascript:showHideEdit()\">Cancel</a>") %></td></tr></table>
</span>
<script language=javascript><!--
    mySection2 = document.getElementById('editarea');
    mySection2.style.display='none';
    <%= ((command.equals("new")) ? "showHideEdit();" : "") %>
//-->
</script>
<% } %>
<%
%>




<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
