<HTML>

<HEAD>
<TITLE>IT Work Request System</TITLE>
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
// set up, get page content and required privs
String pageName = "ITWR-Contacts";
UHash pageContent = new UHash(pageName, myConn);
TextItem editPriv = new TextItem(pageContent.get("editpriv"), myConn);
TextItem appPriv = new TextItem(pageContent.get("approvepriv"), myConn);
TextItem publishedContent = new TextItem(pageContent.get("published"), myConn);
TextItem workingContent = new TextItem(pageContent.get("working"), myConn);
boolean hasEdit = false;
boolean hasApp = false;
String status = "";
// determine if user has edit or approval priv
if (isAuthenticated) {
    hasEdit = pos.belongsTo(((Long) perMap.get(editPriv.getText())).longValue());
    hasApp = pos.belongsTo(((Long) perMap.get(appPriv.getText())).longValue());
}
String displayContent = null;
// determine if published or working content is displayed
if (hasEdit || hasApp) {
    displayContent = workingContent.getText();
    status = workingContent.getStatus();
} else {
    displayContent = publishedContent.getText();
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
	plugins : "style,table,searchreplace,contextmenu,paste,fullscreen,nonbreaking",
	theme_advanced_buttons1 : "bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,outdent,indent,hr,bullist,numlist,undo,redo,link,unlink,styleselect,fontselect,fontsizeselect",
	theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,sub,sup,separator,search,replace,separator,tablecontrols,separator,fullscreen<%= ((!ProductionStatus.equals("prod")) ? ",code" : "") %>",
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

<span id=noneditarea>
  
<!-- display appropriate content -->
<%= displayContent %> 

<!-- Add edit, request approval, or approve links as appropriate -->
<% if (isAuthenticated && (hasEdit || hasApp)) { %>
        <% if (hasEdit || hasApp) { %>
            <hr width=80% >
        <% } %>
        <% if (hasEdit) { %>
            <p><a href="javascript:showHideEdit()">Edit</a>
            <% if (status == null || (!status.equals("approved")&&!status.equals("approvalrequested"))) { %>&nbsp; &nbsp; <a href="javascript:doRequestApproval()">Request Approval</a><% } %> 
        <% } %>
        <% if (hasApp && status != null && status.equals("approvalrequested")) { %>
            <p><a href="javascript:doApprove()">Approve</a></p>
        <% } %>
<% } %>
</span>

<!-- only include edit form and utility javascript if needed -->
<% if (isAuthenticated && (hasEdit || hasApp)) { %>

<script language=javascript><!--
    function showHideEdit () {
        mySection = document.getElementById('noneditarea');
        mySection2 = document.getElementById('editarea');
        if (mySection.style.display=='none') {
            mySection.style.display='block';
            mySection2.style.display='none';
            document.main.reset();
        } else {
            mySection.style.display='none';
            mySection2.style.display='block';
        }
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
        if (isblank(document.main.text.value)) {
            alert('Text must be entered.');
        } else {
            submitFormResults("doUpdatePage",'update');
        }
    }
    
    function doRequestApproval() {
        //alert('Not Ready Yet');
        submitFormResults("doUpdatePage","approvalrequest")
    }
    
    function doApprove() {
        submitFormResults("doUpdatePage","approve")
    }
    
    //-->
</script>

<!-- Set up edit form -->
<span id=editarea style="display:'none'">
<input type=hidden name=pagename value="<%= pageName %>">
<input type=hidden name=nextscript value="contact.jsp">
<table border=0>
<td align=right><a href="javascript:doSave()">Save</a> &nbsp; &nbsp; <a href="javascript:showHideEdit()">Cancel</a></td></tr>
<tr><td colspan=2>Text:
<textarea name=text cols=80 rows=30>
<%= workingContent.getText() %>
</textarea></td></tr>
<tr><td colspan=2><a href="javascript:doSave()">Save</a> &nbsp; &nbsp; <a href="javascript:showHideEdit()">Cancel</a></td></tr></table>
</span>
<script language=javascript><!--
    mySection2 = document.getElementById('editarea');
    mySection2.style.display='none';
//-->
</script>

<% } %>



<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
