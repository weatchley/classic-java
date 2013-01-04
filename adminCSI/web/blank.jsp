<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE>Blank<rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
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
