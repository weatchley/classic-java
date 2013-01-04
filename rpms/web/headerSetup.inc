<!-- Header file for csi JSP files -->
<!-- Import required libraries -->
<%@ page import="java.lang.*,java.io.*,java.util.*,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,org.w3c.dom.*" %>
<%@ page import="org.xml.sax.SAXException,org.xml.sax.SAXParseException,javax.naming.*,javax.naming.*" %>
<%@ page import="gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,gov.ymp.csi.systems.*" %>
<%@ page import="gov.ymp.csi.auth.*,gov.ymp.csi.items.*,org.json.*,gov.ymp.rpms.model.*" %>
<!-- Common javascript utilities -->
<script language=javascript><!--
   function submitForm(script,command) {
       document.main.command.value = command;
       document.main.action = script;
       document.main.submit();
   }
   function submitFormResults(script,command) {
       document.main.command.value = command;
       document.main.action = script;
       var saveTarget = document.main.target;
       document.main.target = 'results';
       document.main.submit();
       document.main.target = saveTarget;
   }
   function submitFormNewWindow(script,command) {
       document.main.command.value = command;
       document.main.action = script;
       var saveTarget = document.main.target;
       document.main.target = '_blank';
       document.main.submit();
       document.main.target = saveTarget;
   }
//   function displayDocument(name) {
//       var myDate = new Date();
//       var winName = myDate.getTime();
//       var newwin = window.open('',winName);
//       newwin.creator = self;
//       newwin.location = '/<%= getServletContext().getServletContextName().toLowerCase()  %>/docFiles/' + name;
//   }
//-->
</script>

<%
// set up initial variables
boolean useFileUpload = false;
boolean isAuthenticated = false;
boolean isAdmin = false; 
Person sUsr = null;
Person usr = null;
Position pos = null;
long posID = 0;
HashMap perMap = null;
%>

<%
// set up initial environment
Context initCtx = new InitialContext();
String SystemName = (String) initCtx.lookup("java:comp/env/SystemName");
//String SystemName = "this is a test";
String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
String ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
//String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
DbConn tmpConn = new DbConn("dummy");
String csiSchema = tmpConn.getSchemaPath();
String appSchema = (String) initCtx.lookup("java:comp/env/AppSchema");
//Context envContext = (Context) initCtx.lookup("java:comp/env");
//String DeploymentStatus = (String) envContext.lookup("DeploymentStatus");
//String DeploymentStatus = (String) initCtx.lookup("java:comp/env/DeploymentStatus");

%>
