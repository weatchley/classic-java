<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE>test<rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<%@ taglib uri="gov.ymp.rdc.tags" prefix="rdc" %>

<BODY leftmargin=0 topmargin=0>
<!-- Begin Main Content -->

<%@ page import="java.io.*,java.util.*,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,org.w3c.dom.*,org.xml.sax.SAXException,org.xml.sax.SAXParseException" %>

<rdc:colapseString name="test1" maxLen="45" isOpen="false">
This is a test of the colapse string tag, it is only a test.
</rdc:colapseString>
<br><br>
<%
String outString = "";
 
outString += "Session Data: <br>\n";
Enumeration enm = session.getAttributeNames();
while (enm.hasMoreElements()) {
    String name = (String) enm.nextElement();
    outString += name + ":" + session.getAttribute(name) + "<br>\n";
}

outString += "<br>\n";

DbConn myConn = new DbConn();

%>

<rdc:topMenu id="RDC-TopNav" target="_blank" dbConnection="<%= myConn %>" showUser="true" editUser="false" >
</rdc:topMenu>

<rdc:colapseString name="test2" maxLen="45" isOpen="true">
This is a test of the colapse string tag, it is only a test.<br>
This is a test of the colapse string tag, it is only a test.<br>
This is a test of the colapse string tag, it is only a test.<br>
This is a test of the colapse string tag, it is only a test.<br>
<%= outString %><br>
</rdc:colapseString>

<rdc:colapseString name="test2b" maxLen="45" isOpen="true" activeText="false">
This is a second test of the colapse string tag, it is only a test.<br>
This is a second test of the colapse string tag, it is only a test.<br>
This is a second test of the colapse string tag, it is only a test.<br>
This is a second test of the colapse string tag, it is only a test.<br>
<%= outString %><br>
</rdc:colapseString>

<br>

<rdc:doSection name="test3" title="Test Section" isOpen="false">
This is the contents of the test section<br>
There is not much here...<br>
</rdc:doSection>

<rdc:notProductionWarning size="150%" width="180" />

<rdc:productionTest >
This is text for non produciton.<br>
</rdc:productionTest>

<rdc:productionTest onMatch="true" status="prod" >
This is text for produciton.<br>
</rdc:productionTest>

<rdc:isAuthenticated >
This is text for authenticated.<br>
</rdc:isAuthenticated>

<rdc:isAuthenticated doOpposite="true" >
This is text for not authenticated.<br>
</rdc:isAuthenticated>


<rdc:envDisplay />


<!-- End Main Content -->

</BODY>

</HTML>
