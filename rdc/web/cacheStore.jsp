<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="java.lang.*,java.io.*,java.util.*,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,org.w3c.dom.*" %>
<%@ page import="org.xml.sax.SAXException,org.xml.sax.SAXParseException,javax.naming.*,javax.naming.*,javax.net.ssl.*,java.net.*" %>
<%@ page import="gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,gov.ymp.csi.systems.*" %>
<%@ page import="gov.ymp.csi.auth.*,gov.ymp.csi.items.*" %>
<%@ taglib uri="gov.ymp.rdc.tags" prefix="rdc" %>
<rdc:sessionTest />

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
Context initCtx = new InitialContext();
Position pos = null;
HashMap perMap = null;
String ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
String tempS = ((session.getAttribute("user.id")  != null) ? (String) session.getAttribute("user.id") : "0");
long userID = ((session.getAttribute("user.id")  != null) ? Long.parseLong((String) session.getAttribute("user.id")) : 0);
String urlS = ((request.getParameter("url")  != null) ? (String) request.getParameter("url") : "n/a");
String fileWithPath = ((request.getParameter("cachefilewithpath")  != null) ? (String) request.getParameter("cachefilewithpath") : "n/a");
String doSecondCache = ((request.getParameter("dosecondcache")  != null) ? (String) request.getParameter("dosecondcache") : "F");
String fileWithPath2 = ((request.getParameter("secondcachefile")  != null) ? (String) request.getParameter("secondcachefile") : "n/a");
String cacheContent = ((request.getParameter("cachecontent")  != null) ? (String) request.getParameter("cachecontent") : "n/a");

DbConn myConn = new DbConn("csi");
if (userID != 0) {
    pos = new Position(myConn, Long.parseLong((String) session.getAttribute("user.positionid")));
    perMap = (HashMap) session.getAttribute("user.permissionmap");
}
myConn.release();

%>
<HTML>
<HEAD>
<TITLE>Update Cache</TITLE>
</HEAD>
<BODY leftmargin=0 topmargin=0 style="background-color : #ffffff;">
<script language=javascript><!--
<% 
    if (pos != null && pos.belongsTo(((Long) perMap.get("3-updatehtmlcache")).longValue())) {
        if (!cacheContent.equals("n/a") && !fileWithPath.equals("n/a")) {

            try {
                FileWriter outFile = new FileWriter(fileWithPath);
                outFile.write(cacheContent);
                outFile.close();
                if (doSecondCache.equals("T")) {
                    outFile = new FileWriter(fileWithPath2);
                    outFile.write(cacheContent);
                    outFile.close();
                }
                %>
                    alert('Cache Updated');
                <%
            }

            catch (Exception e) {
                String outLine = "Cache Not Updated, Exception caught: " + e.getMessage();
                %>
                    alert('<%= outLine %>');
                <%
            }
        } else {
            %>
                alert('Cache Not Updated, Incorrect Parameters');
            <%
        }
    } else {
            %>
                alert('You do not have the rights to update the cache');
            <%
    }
%>
    //-->
</script>
</BODY>
</HTML>
