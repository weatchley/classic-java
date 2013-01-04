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
String pageTemplate = "";
String nextScript = scriptRoot + request.getServletPath() + "?pagehash=" + pageHash;
String noEdit = ((request.getParameter("noedit")  != null) ? (String) request.getParameter("noedit") : "false");
String showControls = ((request.getParameter("showcontrols")  != null) ? (String) request.getParameter("showcontrols") : "true");
String cachePath = "";
Position pos = null;
HashMap perMap = null;

DbConn myConn = null;
boolean canConnect = false;
UHash pageContent = null;
TextItem tiPageTitle = null;
TextItem tiPageTemplate = null;

try {
    myConn = new DbConn("csi");
    canConnect = true;
    pageContent = new UHash(pageHash, myConn);

    tiPageTitle = new TextItem(pageContent.get("title"), myConn);
    pageTitle = tiPageTitle.getText();
    tiPageTemplate = new TextItem(pageContent.get("template"), myConn);
    pageTemplate = tiPageTemplate.getText();

    myConn.release();
}
catch (Exception e) {
    //
}


%>
<html>
<head>
<title><%= pageTitle %></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META name="description" content="<%= pageTitle %>">
<META name="keywords" content="">

</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<form name='main' METHOD='POST' action=>
<input type=hidden name=pagehash value="<%= pageHash %>">
<input type=hidden name=nextscript value="<%= nextScript %>">
</form>
<% if (canConnect) { %>
<script language=javascript><!--

   function submitForm() {
       document.main.action = '<%= pageTemplate %>.jsp';
       document.main.submit();
   }

    submitForm();
//-->
</script>
<% } else { %>
    <h2 align=center>Content Currently Unavailable</h2>
    <h3 align=center>Unable to connect to Oracle</h3>
    <h3 align=center>Please call the Help Desk at (702) 794-1335 or (202) 586-5153</h3>
<% } %>
</body>
</html>
