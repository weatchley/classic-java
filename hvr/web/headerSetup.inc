<!-- Header file for csi JSP files -->
<!-- Import required libraries -->
<%@ page import="java.lang.*,java.io.*,java.util.*,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,org.w3c.dom.*" %>
<%@ page import="org.xml.sax.SAXException,org.xml.sax.SAXParseException,javax.naming.*,javax.naming.*" %>
<%@ page import="gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,gov.ymp.csi.systems.*" %>
<%@ page import="gov.ymp.csi.auth.*,gov.ymp.csi.items.*,org.json.*" %>
<%@ taglib uri="gov.ymp.rdc.tags" prefix="rdc" %>

<%
// set up initial variables
boolean useFileUpload = false;
boolean isAuthenticated = false;
boolean isAdmin = false; 
Person sUsr = null;
Person cUser = null;
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
//String appSchema = (String) initCtx.lookup("java:comp/env/AppSchema");
//Context envContext = (Context) initCtx.lookup("java:comp/env");
//String DeploymentStatus = (String) envContext.lookup("DeploymentStatus");
//String DeploymentStatus = (String) initCtx.lookup("java:comp/env/DeploymentStatus");

%>
