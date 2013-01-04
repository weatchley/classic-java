<HTML>

<HEAD>
<TITLE>JSP Environment Test</TITLE>
</HEAD>

<BODY BGCOLOR=#DEC68B >


<h3>Environment Test</h3>

<%@ page import="java.io.*,java.util.*,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,org.w3c.dom.*,org.xml.sax.SAXException,org.xml.sax.SAXParseException,javax.naming.*" %>
<%@ page import="gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,gov.ymp.csi.systems.*" %>
<%@ page import="gov.ymp.mms.model.*" %>
<% 
// //HttpServletRequest request;
// //HttpSession session = request.getSession();

File[] roots = File.listRoots();
String rootList = "";
String outString = "";

Enumeration myEnum = null;
String docRoot = getServletContext().getRealPath("/");

for (int i=0; i<roots.length; i++){
    rootList += roots[i] + ",\n";
}

//
session.setAttribute("user.name", "TestUserName");
session.setAttribute("user.fullname", "Test User Fullname");
session.setAttribute("user.test", "testTestTEST");
session.setAttribute("user.test", null);
outString += "<br><b><i>Session Data:</i></b> <br>\n";
myEnum = session.getAttributeNames();
while (myEnum.hasMoreElements()) {
    String name = (String) myEnum.nextElement();
    outString += "<b>" + name + ":</b>" + session.getAttribute(name) + "<br>";
}

outString += "<br><b><i>Context Data:</i></b> <br>\n";
myEnum = getServletContext().getAttributeNames();
while (myEnum.hasMoreElements()) {
    String name = (String) myEnum.nextElement();
    outString += "<b>" + name + ":</b>" + getServletContext().getAttribute(name) + "<br>";
}

outString += "<br><b><i>Init Data:</i></b> <br>\n";
myEnum = getInitParameterNames();
while (myEnum.hasMoreElements()) {
    String name = (String) myEnum.nextElement();
    outString += "<b>" + name + ":</b>" + getInitParameter(name) + "<br>";
}

outString += "<br><b><i>Parameter Data:</i></b> <br>\n";
myEnum = request.getParameterNames();
while (myEnum.hasMoreElements()) {
    String name = (String) myEnum.nextElement();
    String values[] = request.getParameterValues(name);
    outString += "<b>" + name + ":</b>" + request.getParameter(name) + "<br>";
    if (values != null) {
        for (int i = 0; i < values.length; i++) {
            outString += name + " (" + i + "): " + values[i] + ", ";
        }
    }
    outString += "<br>";
}

outString += "<br><b><i>Cookie Data:</i></b> <br>\n";
Cookie[] cookies = request.getCookies();
if (cookies != null) {
    for (int i = 0; i<cookies.length; i++) {
        outString += cookies[i].getName() + ":</b> " + cookies[i].getValue() + "<br>";
    }
}

outString += "<br><b><i>ENV Entries:</i></b> <br>\n";
Context initCtx = new InitialContext();
NamingEnumeration myEnum2 = initCtx.listBindings("java:comp/env");
outString += "<table border=1><tr><td><b>Name</b></td><td><b>Type</b></td><td><b>Value</b></td></tr>";
// We're using JDK 1.2 methods; that's OK since J2EE requires JDK 1.2
while (myEnum2.hasMore(  )) {
    Binding binding = (Binding) myEnum2.next(  );
    outString += "<tr><td>" + binding.getName(  ) + "</td>";
    outString += "<td>" + binding.getClassName(  ) + "</td>";
    outString += "<td>" + binding.getObject(  ) + "</td></tr>";
}
outString += "</table>";

//myEnum = initCtx.getAttributeNames();
//while (myEnum.hasMoreElements()) {
//    String name = (String) myEnum.nextElement();
////    String test = (String) initCtx.lookup("java:comp/env/SystemName");
//    String test = (String) initCtx.lookup(test);
//    outString += "<b>" + name + ":</b> " + test + "<br>";
//}

%>

<%= "<b>RootList:</b> " + rootList %><br>
<%= "<b>ServerName:</b> " + request.getServerName() %><br>
<%= "<b>ServerSoftware:</b> " + getServletContext().getServerInfo() %><br>
<%= "<b>ServerCintextName:</b> " + getServletContext().getServletContextName() %><br>
<%= "<b>ServerProtocol:</b> " + request.getProtocol() %><br>
<%= "<b>ServerScheme:</b> " + request.getScheme() %><br>
<%= "<b>ServerPort:</b> " + request.getServerPort() %><br>
<%= "<b>RequestMethod:</b> " + request.getMethod() %><br>
<%= "<b>PathInfo:</b> " + request.getPathInfo() %><br>
<%= "<b>PathTranslated:</b> " + request.getPathTranslated() %><br>
<%= "<b>ScriptName:</b> " + request.getServletPath() %><br>
<%= "<b>DocumentRoot:</b> " + getServletContext().getRealPath("/") %><br>
<%= "<b>QueryString:</b> " + request.getQueryString() %><br>
<%= "<b>RemoteHost:</b> " + request.getRemoteHost() %><br>
<%= "<b>RemoteAddr:</b> " + request.getRemoteAddr() %><br>
<%= "<b>AuthType:</b> " + request.getAuthType() %><br>
<%= "<b>RemoteUser:</b> " + request.getRemoteUser() %><br>
<%= "<b>ContentType:</b> " + request.getContentType() %><br>
<%= "<b>ContentLength:</b> " + request.getContentLength() %><br>
<%= "<b>HTTPAccept:</b> " + request.getHeader("Accept") %><br>
<%= "<b>HTTPUserAgent:</b> " + request.getHeader("User-Agent") %><br>
<%= "<b>HTTPReferer:</b> " + request.getHeader("Referer") %><br>
<%= outString %><br>

<hr>

Testing mm code:<br><br>

<% DbConn myConn = new DbConn("csi"); %>
<% String testPO = "2009EENV130A"; %>
Test PR lookup using <%= testPO %>: <%= PurchaseDocument.lookupPRFromPO(testPO, myConn) %>.<br>
<br>
Done
<%
myConn.release();
%>

</BODY>

</HTML>
