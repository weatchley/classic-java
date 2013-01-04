<!-- Header file for csi JSP files -->
<!-- Import required libraries -->
<%@ page import="java.lang.*,java.io.*,java.util.*,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,org.w3c.dom.*" %>
<%@ page import="org.xml.sax.SAXException,org.xml.sax.SAXParseException,javax.naming.*,javax.naming.*" %>
<%@ page import="gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,gov.ymp.csi.systems.*" %>
<%@ page import="gov.ymp.csi.auth.*,gov.ymp.csi.items.*,org.json.*,gov.ymp.pclwr.model.*" %>
<!-- Common javascript utilities -->
<script language=javascript><!--
   function submitForm(script,command) {
       document.main.command.value = command;
       document.main.action = script;
       document.main.submit();
   }
   function submitFooterForm(script,command) {
       document.footerform.command.value = command;
       document.footerform.action = script;
       document.footerform.submit();
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
String PathToRoot = (String) initCtx.lookup("java:comp/env/PathToRoot");
DbConn tmpConn = new DbConn("dummy");
String csiSchema = tmpConn.getSchemaPath();
String appSchema = (String) initCtx.lookup("java:comp/env/AppSchema");
//Context envContext = (Context) initCtx.lookup("java:comp/env");
//String DeploymentStatus = (String) envContext.lookup("DeploymentStatus");
//String DeploymentStatus = (String) initCtx.lookup("java:comp/env/DeploymentStatus");

%>
