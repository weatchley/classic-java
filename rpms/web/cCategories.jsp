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
CCategory [] list = CCategory.getItemList(myConn);

int maxLen = 2000;


String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");

myConn.release();

%>
<script language="javascript">
    var itemValueStore = new Object();
    itemValueStore.item0 = "";

</script>
  

  <h3>Citation Category Management Page</h3><br>

<span id=noneditarea>
<%
int dispCount = 0;
if (!command.equals("new")) {
//System.out.println(".jsp - Got Here 1" + " command: " + command);
    %><table border=1 cellpadding=0 cellspacing=0>
    <tr bgcolor=#cccccc><td><b>Description</b></td><td>&nbsp;</td></tr><%
    for (int i=0; i<list.length; i++) {
        String text = list[i].getDescription();
        text = text.replaceAll("'", "\\\\'");
        text = text.replaceAll("[\\n\\r]","\\\\n");
        boolean doDisp = false;
        %>
        <script language="javascript" type="text/javascript">
            itemValueStore.item<%= list[i].getID() %> = '<%= text %>';
        </script>
        <%
        if (isAuthenticated && isAdmin) {
            doDisp = true;
        }
        if (doDisp) {
            dispCount++;
            %>
            <tr><td valign=top><%= list[i].getDescription() %></td><td valign=top>
            <% 
            if (isAuthenticated && isAdmin) {
                %><a href="javascript:doEdit(<%= list[i].getID() %>)">Edit</a>&nbsp;&nbsp;&nbsp;
                <% if(!list[i].getInUse()) { %><a href="javascript:doDrop(<%= list[i].getID() %>)">Delete</a><% }
            }
            %>
            </td></tr>
            <%
        }
    }
    if (dispCount == 0) {
        %>
        <tr><td>No Citation Categories Available</td></tr>
        <%
    }
    %></table><%
}

%>

<% if (isAuthenticated) { %>
    <% if (list != null && list.length>0) { %>
        <% if (isAdmin) { %>
            <p><a href="javascript:doNew()">New</a></p>
        <% } %>
    <% } %>

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
        eval('document.main.description.value = itemValueStore.item' + id + ';');
        showHideEdit();
    }

    function doNew() {
        document.main.id.value = 0;
        submitForm("cCategories.jsp","new");
    }

    function doSave() {
        if (isblank(document.main.description.value) || document.main.description.value.length > <%= maxLen%>) {
            alert ('A description (upto <%= maxLen%> characters) must be entered.');
        } else {
            submitFormResults("doCCategory",<%= ((command.equals("new")) ? "'add'" : "'update'") %>);
        }
    }
    
    function doDrop(id) {
        document.main.id.value = id;
        eval('var message = itemValueStore.item' + id + ';');
        if (confirm('Delete Citation Category "' + message + '"?')) {
            submitFormResults("doCCategory","drop");
        }
    }
    
    //-->
</script>
<% } %>
<script language=javascript><!--

    function doView(id) {
        document.main.id.value = id;
        submitForm("response.jsp","view")
    }
//-->
</script>
</span>
<% if (isAuthenticated) { %>
<span id=editarea style="display:'none'">
<input type=hidden name=nextscript value="cCategories.jsp">
<table border=0>
<tr><td><%= ((command.equals("new")) ? "New" : "&nbsp;") %></td>
<td align=right><a href="javascript:doSave()">Save</a> <%= ((command.equals("new")) ? "" : "&nbsp; &nbsp; <a href=\"javascript:showHideEdit()\">Cancel</a>") %></td></tr>
<tr><td colspan=2><textarea id=description name=description cols=80 rows=10></textarea></td></tr>
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
