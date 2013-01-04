<HTML>

<HEAD>
<TITLE>Records Plan Management System</TITLE>
  <LINK href="css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="headerSetup.inc" %>
<!-- Begin Main Content -->

<%@ page import="java.io.*,java.util.*,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,org.w3c.dom.*,org.xml.sax.SAXException,org.xml.sax.SAXParseException" %>


<%
String outString = "";
 
outString += "<!-- Session Data: \n";
Enumeration enm = session.getAttributeNames();
while (enm.hasMoreElements()) {
    String name = (String) enm.nextElement();
    outString += name + ":" + session.getAttribute(name) + "\n";
}

outString += "-->\n";

%>

<%= outString %><br>



<!-- End Main Content -->

</BODY>

</HTML>
