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
// setup, get page structure and content
String faqName = "RPMS-FAQ";
UHash faq = new UHash(faqName, myConn);
UList ul = new UList(faq.get("list"), myConn);
ul.lookup(ul.getID(), myConn);
long[] items = ul.getItemsArray();
TextItem[] list = new TextItem[items.length];
//TextItem[] list = TextItem.getItemList(myConn, items, "date1 desc");
TextItem editPriv = new TextItem(faq.get("editpriv"), myConn);
TextItem appPriv = new TextItem(faq.get("approvepriv"), myConn);
boolean hasEdit = false;
boolean hasApp = false;

// determine if user has edit or approval priv
if (isAuthenticated) {
    hasEdit = pos.belongsTo(((Long) perMap.get(editPriv.getText())).longValue());
    hasApp = pos.belongsTo(((Long) perMap.get(appPriv.getText())).longValue());
}
// get TextItems for all FAQ's
for (int i=0; i<items.length; i++) {
    list[i] = new TextItem(items[i], myConn);
}

// qID and faqID are not currently being used
String qIDs = ((request.getParameter("id")  != null) ? (String) request.getParameter("id") : "0");
String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
long qID = Long.parseLong(qIDs);
long faqID = faq.getID();
int current = -1;
String lnDate = "";
String lnText = "";
String status = "";

myConn.release();

%>
<script language="javascript">
    var questionStore = new Object();
    questionStore.item0 = "";
    var answerStore = new Object();
    answerStore.item0 = "";

</script>
<!-- include code for WYSIWYG editor -->
<!-- tinyMCE -->
<script language="javascript" type="text/javascript" src="javascript/tiny_mce/tiny_mce.js"></script>
<script language="javascript" type="text/javascript">
	tinyMCE.init({
	mode : "<%= ((command.equals("new")) ? "textareas" : "none") %>",
	theme : "advanced",
	plugins : "style,table,searchreplace,contextmenu,paste,fullscreen,nonbreaking",
	theme_advanced_buttons1 : "bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,outdent,indent,hr,bullist,numlist,undo,redo,link,unlink,fontselect,fontsizeselect",
	theme_advanced_buttons2 : "cut,copy,paste,pastetext,|,sub,sup,separator,search,replace,separator,fullscreen<%= ((!ProductionStatus.equals("prod")) ? ",code" : "") %>",
	theme_advanced_buttons3 : "",
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "left",
	theme_advanced_statusbar_location : "bottom",
	theme_advanced_resizing : true,
	theme_advanced_path : false,
	extended_valid_elements : "a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]"
	});

    function toggleEditor(id) {
        if (!tinyMCE.getInstanceById(id))
            tinyMCE.execCommand('mceAddControl', false, id);
        else
            tinyMCE.execCommand('mceRemoveControl', false, id);
    }

    
</script>
<!-- /tinyMCE -->
  

  <h3>FAQ's</h3><br>

<span id=noneditarea>
<%
int dispCount = 0;
if (!command.equals("new")) {
//System.out.println("faq.jsp - Got Here 1" + " command: " + command);
    %><table border=0><%
    // generate list of FAQ's
    for (int i=0; i<list.length; i++) {
        // get status
        String tempStatus = list[i].getStatus();
        // get question and answer from JSON object
        JSONObject jo = new JSONObject(list[i].getText());

        // Clean up question and answer then store in a javascript array for later editing
        String quest = jo.getString("question");
        String ans = jo.getString("answer");
        quest = quest.replaceAll("'", "\\\\'");
        quest = quest.replaceAll("[\\n\\r]","\\\\n");
        //String tempText2 = tempText.replaceAll("[\\n\\r]","\\\\n");
        ans = ans.replaceAll("'", "\\\\'");
        ans = ans.replaceAll("[\\n\\r]","\\\\n");
        boolean doDisp = false;
        %>
        <script language="javascript" type="text/javascript">
            questionStore.item<%= list[i].getID() %> = '<%= quest %>';
            answerStore.item<%= list[i].getID() %> = '<%= ans %>';
        </script>
        <%

        // determine if question/answer should be displayed (approved or has admin privs)
        if (tempStatus != null && tempStatus.equals("approved")) {
            doDisp = true;
        } else if (isAuthenticated && hasEdit) {
            doDisp = true;
        } else if (isAuthenticated && hasApp && tempStatus != null && tempStatus.equals("approvalrequested")) {
            doDisp = true;
        }
        
        // display question/answer, if required, and add management links when appropriate
        if (doDisp) {
            dispCount++;
            %>
            <%= ((dispCount > 1) ? "<tr><td>&nbsp;</td></tr>" : "") %>
            <tr><td valign=top><%= ((tempStatus == null || !tempStatus.equals("approved")) ? "*" : "") %>Q: &nbsp;</td><td><%= jo.getString("question") %></td><td valign=top>
            <% 
            if (isAuthenticated && hasEdit) {
                %><a href="javascript:doEdit(<%= list[i].getID() %>)">Edit</a>&nbsp;&nbsp;&nbsp;<a href="javascript:doDrop(<%= list[i].getID() %>)">Delete</a>&nbsp;&nbsp;&nbsp;Move&nbsp;<%
                if (i>0) {
                    %><a href="javascript:doMoveUp(<%= list[i].getID() %>)">Up</a>/<a href="javascript:doMoveToTop(<%= list[i].getID() %>)">Top</a><%
                }
                if (tempStatus == null || (!tempStatus.equals("approved") && !tempStatus.equals("approvalrequested"))) {
                    %>&nbsp;&nbsp;&nbsp;<a href="javascript:doRequestApproval(<%= list[i].getID() %>)">Request&nbsp;Approval</a><%
                }
            }
            if (isAuthenticated && hasApp && tempStatus != null && tempStatus.equals("approvalrequested")) {
                %><%= ((hasEdit) ? "&nbsp;&nbsp;&nbsp;" : "") %><a href="javascript:doApprove(<%= list[i].getID() %>)">Approve</a><%
            }
            %>
            </td></tr>
            <tr><td valign=top>A: &nbsp;</td><td><%= jo.getString("answer") %></td></tr>
            <%
        }
    }
    if (dispCount == 0) {
        %>
        <tr><td>No FAQ's Available</td></tr>
        <%
    }
    %></table><%
}

%>

<p>&nbsp; </p>

<% //System.out.println("faq.jsp - Got Here 5" + ", qID: " + qID + " command: " + command + " Status: " + status); %>
<% if (isAuthenticated) { %>
    <!-- Add "New" link if needed -->
    <% if (list != null && list.length>0) { %>
        <% if (hasEdit || hasApp) { %>
            <hr width=80% >
        <% } %>
        <% if (hasEdit) { %>
            <p><a href="javascript:doNew()">New</a></p>
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

    function doEdit(id) {
        document.main.id.value = id;
        //toggleEditor('question');
        //toggleEditor('answer');
        eval('document.main.question.value = questionStore.item' + id + ';');
        eval('document.main.answer.value = answerStore.item' + id + ';');
        toggleEditor('question');
        toggleEditor('answer');
        showHideEdit();
    }

    function doNew() {
        document.main.id.value = 0;
        submitForm("faq.jsp","new");
    }

    function doSave() {
        if (isblank(document.main.question.value) || isblank(document.main.answer.value)) {
            alert ('both question and Answer must be entered.');
        } else {
            submitFormResults("doFAQ",<%= ((command.equals("new")) ? "'add'" : "'update'") %>);
        }
    }
    
    function doRequestApproval(id) {
        document.main.id.value = id;
        //alert('Not Ready Yet');
        submitFormResults("doFAQ","approvalrequest");
    }
    
    function doMoveUp(id) {
        document.main.id.value = id;
        //alert('Not Ready Yet');
        submitFormResults("doFAQ","moveup");
    }
    
    function doMoveToTop(id) {
        document.main.id.value = id;
        //alert('Not Ready Yet');
        submitFormResults("doFAQ","movetotop");
    }
    
    function doDrop(id) {
        document.main.id.value = id;
        eval('var message = questionStore.item' + id + ';');
        if (confirm('Delete FAQ ' + message + '?')) {
            submitFormResults("doFAQ","drop");
        }
    }
    
    function doApprove(id) {
        document.main.id.value = id;
        submitFormResults("doFAQ","approve")
    }
    
    //-->
</script>
<% } %>
<script language=javascript><!--

    function doView(id) {
        document.main.id.value = id;
        submitForm("faq.jsp","view")
    }
//-->
</script>
</span>

<% if (isAuthenticated) { %>
<!-- create edit form -->
<span id=editarea style="display:'none'">
<input type=hidden name=qID value="<%= qID %>">
<input type=hidden name=faqname value="<%= faqName %>">
<input type=hidden name=nextscript value="faq.jsp">
<table border=0>
<tr><td>&nbsp;</td>
<td align=right><a href="javascript:doSave()">Save</a> <%= ((command.equals("new")) ? "" : "&nbsp; &nbsp; <a href=\"javascript:showHideEdit()\">Cancel</a>") %></td></tr>
<tr><td colspan=2>Question:
<textarea id=question name=question cols=80 rows=10>

</textarea></td></tr>
<tr><td colspan=2><br>Answer:
<textarea id=answer name=answer cols=80 rows=10>

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
