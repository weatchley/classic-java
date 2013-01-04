<HTML>

<HEAD>
<TITLE>Tribal Information Exchange</TITLE>
</HEAD>

<BODY BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>
<%@ include file="headerSetup.html" %>
<!-- Begin Main Content -->

<%@ page import="java.io.*,java.util.*,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,org.w3c.dom.*,org.xml.sax.SAXException,org.xml.sax.SAXParseException" %>


<%
String outString = "";

outString += "<!-- Session Data: \n";
Enumeration enum = session.getAttributeNames();
while (enum.hasMoreElements()) {
    String name = (String) enum.nextElement();
    outString += name + ":" + session.getAttribute(name) + "\n";
}

outString += "-->\n";

%>

<%= outString %><br>



<!-- End Main Content -->

</BODY>

</HTML>
