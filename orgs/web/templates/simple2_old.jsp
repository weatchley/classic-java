<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="java.lang.*,java.io.*,java.util.*,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,org.w3c.dom.*" %>
<%@ page import="org.xml.sax.SAXException,org.xml.sax.SAXParseException,javax.naming.*,javax.naming.*" %>
<%@ page import="gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,gov.ymp.csi.systems.*" %>
<%@ page import="gov.ymp.csi.auth.*,gov.ymp.csi.items.*" %>

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
Context initCtx = new InitialContext();
String ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
String tempS = ((session.getAttribute("user.id")  != null) ? (String) session.getAttribute("user.id") : "0");
long userID = ((session.getAttribute("user.id")  != null) ? Long.parseLong((String) session.getAttribute("user.id")) : 0);
String tempPathS = getServletContext().getRealPath("/");
String lastChar = tempPathS.substring(tempPathS.length()-1);
String scriptRoot = tempPathS.substring(tempPathS.lastIndexOf(lastChar,tempPathS.length()-2)+1);
scriptRoot = scriptRoot.substring(0,scriptRoot.length()-1);
scriptRoot = "/" + scriptRoot;
String pageHash = ((request.getParameter("pagehash")  != null) ? (String) request.getParameter("pagehash") : "OGP-PageContent-OD");
//
boolean canEdit = false;
String pageTitle = "";
String pageDescription = "";
//String nextScript = scriptRoot + request.getServletPath();
String nextScript = ((request.getParameter("nextscript")  != null) ? (String) request.getParameter("nextscript") : scriptRoot + request.getServletPath());
String noEdit = ((request.getParameter("noedit")  != null) ? (String) request.getParameter("noedit") : "false");
String showControls = ((request.getParameter("showcontrols")  != null) ? (String) request.getParameter("showcontrols") : "true");
String cachePath = "";
Position pos = null;
HashMap perMap = null;
String resourceType = "dynapage" + pageHash;

DbConn myConn = new DbConn("csi");
UHash pageContent = new UHash(pageHash, myConn);

TextItem tiPageTitle = new TextItem(pageContent.get("title"), myConn);
pageTitle = tiPageTitle.getText();
TextItem tiPageDescription = new TextItem(pageContent.get("description"), myConn);
pageDescription = tiPageDescription.getText();


//********************************* begin authorize setup ***************

    //cachePath = "";
    if (userID != 0 && !((String) session.getAttribute("user.fullname")).toLowerCase().equals("guest")) {
        pos = new Position(myConn, Long.parseLong((String) session.getAttribute("user.positionid")));
        perMap = (HashMap) session.getAttribute("user.permissionmap");

        if (pos != null && pos.belongsTo(((Long) perMap.get("3-organizationstab")).longValue()) && !noEdit.equals("true")) {
            canEdit = true;
        }
    }
//********************************* end authorize setup ***************

myConn.release();

%>
<html>
<head>
<title><%= pageTitle %></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META name="description" content="<%= pageTitle %>">
<META name="keywords" content="">

<!--link rel="stylesheet" href="prstyle.css" type="text/css"-->
<style type="text/css">
div.float1 {
    position:absolute; width:400px; 
    padding-bottom:1px; 
    padding-left: 3px;
    background-color:#FFF; 
    border:1px solid #317082; 
    left:50px;
    top:50px;
}

div.editcontrols {
    display:;
}

table.editcontrols {
    display:;
}

a.editcontrols {
    display:;
}

</style>
</head>
<!--script src=/javascript/utilities.js></script-->
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<form name='main' METHOD='POST' action=>

<input type=hidden name=command value=''>
<input type=hidden name=nextscript value='<%= nextScript %>'>
<input type=hidden name=pagetitle value=''>
<input type=hidden name=pagedescription value=''>
<input type=hidden name=pagehash value='<%= pageHash %>'>
<input type=hidden name=template value='simple1'>


<!-- *************************** Floating Window Section *************************** -->

<!-- **** edit window *** -->
<div id="editWindow" class="float1" style="display:'none';">
<table border=0 cellpadding=0 cellspacing=0>
<tr bgcolor=#eeeeee><td align=center><b>Update Page Content</b></td></tr>
<tr><td>
<b>Page Title:</b><br>
<input type=text size=80 maxlength=80 name=titleedit value="<%= pageTitle %>"><br>
<b>Page Contents:</b><br>
<textarea name=textedit cols=80 rows=6><%= pageDescription %></textarea><br>
<input type=hidden name=notetextid value=0>
<a href="javascript:doUpdateContents();">Submit</a> &nbsp; 
<a href="javascript:document.main.reset();">Reset</a> &nbsp; 
<a href="javascript:document.main.reset();closeAllFloatingWindows();">Cancel</a>
</td></tr></table>
</div>

<script language=javascript><!--
    function closeAllFloatingWindows() {
        section = document.getElementById('editWindow');
        section.style.display='none';
    }
    
    closeAllFloatingWindows();

    //-->
</script>

<!-- *************************** End Floating Window Section *************************** -->

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#006699"> 
    <td height="79" background="images/topbkgd.gif" width="1%"><img src="/common/images/rdc/topleftskinny.jpg" width="140" height="80"></td>
    <td width="100%" height="79" valign="middle" background="images/topbkgd.gif"> 
      <p><font face="Arial, Helvetica, sans-serif" size="5" color="#FFFFFF"><b>&nbsp;<%= pageTitle %> </b></font></p>
    </td>
  </tr>
  <tr><td>&nbsp;</td><td><br>
<%
        pageDescription = pageDescription.replaceAll("\n\r", "<br>");
        pageDescription = pageDescription.replaceAll("\n", "<br>");
        pageDescription = pageDescription.replaceAll("\r", "<br>");
        pageDescription = pageDescription.replaceAll("  ", " &nbsp;");

%>
  <%= pageDescription %>
  </td></tr>
  <tr><td>&nbsp;</td><td>
<script language=javascript><!--
    if (top == self) {
        document.writeln('<br><br><a href="javascript:window.close();"><font size=-2>Close Window</font></a>');
    } else {
        document.writeln('&nbsp;');
    }
    //-->
</script>
  </td></tr>
</table>
<%
    if (canEdit) {
%>
<script language=javascript><!--
    
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

    function updateContents() {
        closeAllFloatingWindows();
        section = document.getElementById('editWindow');
        section.style.display='';
        document.main.titleedit.focus();
    }

    function doUpdateContents() {
        var testTitle = main.titleedit.value;
        var testText = main.textedit.value;
        if (isblank(testTitle)) {
            alert('Please enter the title text');
        } else if (isblank(testText)) {
            alert('Please enter the page contents text');
        } else {
            document.main.pagetitle.value = document.main.titleedit.value;
            document.main.pagedescription.value = document.main.textedit.value;
            submitFormResults('<%= scriptRoot %>/doDynaPage', 'updatepage');
            closeAllFloatingWindows();
        }
    }

   function submitFormResults(script,command) {
       document.main.command.value = command;
       document.main.action = script;
       document.main.target = '<%= resourceType %>results';
       document.main.submit();
   }
    

    //-->
</script>
<table border=0 align=center><tr><td>
<br><br><br><br>
<hr>
<a href="javascript:updateContents();">Update Page</a>
</td></tr></table>

<% if (!ProductionStatus.equals("prod")) { %>
    <br><br><!--%@ include file="envDisp.inc" %-->
 
<% } %>

<% if (ProductionStatus.equals("prod")) { %>
    <iframe src="<%= scriptRoot %>/blank.htm" width=1 height=1 align=left name=<%= resourceType %>results border=0 frameborder=0></iframe>
<% } else { %>
    <iframe src="<%= scriptRoot %>/blank.htm" width=500 height=60 align=left name=<%= resourceType %>results border=0 frameborder=0></iframe>
<% } %>

<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>

<%
    }
%>

</form>

</body>
</html>
