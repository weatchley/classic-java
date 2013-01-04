<HTML>

<HEAD>
<TITLE>JSP Environment Test</TITLE>
</HEAD>

<BODY BGCOLOR=#DEC68B >


<h3>Environment Test</h3>

<%@ page import="java.io.*,java.util.*,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,org.w3c.dom.*,org.xml.sax.SAXException,org.xml.sax.SAXParseException,gov.ymp.nacrd.*" %>
<% 
// //HttpServletRequest request;
// //HttpSession session = request.getSession();

File[] roots = File.listRoots();
String rootList = "";
String outString = "";

Enumeration enum = null;
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
enum = session.getAttributeNames();
while (enum.hasMoreElements()) {
    String name = (String) enum.nextElement();
    outString += "<b>" + name + ":</b>" + session.getAttribute(name) + "<br>";
}

outString += "<br><b><i>Context Data:</i></b> <br>\n";
enum = getServletContext().getAttributeNames();
while (enum.hasMoreElements()) {
    String name = (String) enum.nextElement();
    outString += "<b>" + name + ":</b>" + getServletContext().getAttribute(name) + "<br>";
}

outString += "<br><b><i>Init Data:</i></b> <br>\n";
enum = getInitParameterNames();
while (enum.hasMoreElements()) {
    String name = (String) enum.nextElement();
    outString += "<b>" + name + ":</b>" + getInitParameter(name) + "<br>";
}

outString += "<br><b><i>Parameter Data:</i></b> <br>\n";
enum = request.getParameterNames();
while (enum.hasMoreElements()) {
    String name = (String) enum.nextElement();
    String values[] = request.getParameterValues(name);
    outString += "<b>" + name + ":</b>" + request.getParameter(name) + "<br>";
    if (values != null) {
        for (int i = 0; i < values.length; i++) {
            outString += name + " (" + i + "): " + values[i] + ", ";
        }
    }
    outString += "<br>";
}



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


</BODY>

</HTML>
