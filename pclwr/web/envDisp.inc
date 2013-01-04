<% 

File[] roots = File.listRoots();
String rootList = "";
String outString = "";

Enumeration myEnum = null;
//String docRoot = getServletContext().getRealPath("/");

for (int myi=0; myi<roots.length; myi++){
    rootList += roots[myi] + ",\n";
}

//
outString += "<br><b><i>Session Data:</i></b> <br>\n";
myEnum = session.getAttributeNames();
while (myEnum.hasMoreElements()) {
    String name = (String) myEnum.nextElement();
    outString += "<b>" + name + ":</b>" + session.getAttribute(name) + "<br>";
}
//Person tmpUsr = (Person) session.getAttribute("user.person");
//outString += "<b>Person's E-Mail:</b>" + tmpUsr.getEmail() + "<br>";

outString += "<br><b><i>Context Data:</i></b> <br>\n";
myEnum = getServletContext().getAttributeNames();
while (myEnum.hasMoreElements()) {
    String name = (String) myEnum.nextElement();
    outString += HtmlUtils.colapseString((name + "_string"),"<b>" + name + ":</b>" + getServletContext().getAttribute(name), 80) + "<br>";
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
        String tempStr = "";
        for (int myi = 0; myi < values.length; myi++) {
            //tempStr += name + " (" + myi + "): " + values[myi] + ", ";
            outString += name + " (" + myi + "): " + values[myi] + ", ";
        }
        //outString += HtmlUtils.colapseString(("envTestA" + name),tempStr, 60);
    }
    outString += "<br>";
}

outString += "<br><b><i>ENV Entries:</i></b> <br>\n";
//Context initCtx = new InitialContext();
NamingEnumeration myEnum2 = initCtx.listBindings("java:comp/env");
outString += "<table border=1 cellspacing=0 cellpadding=0><tr><td><b>Name</b></td><td><b>Type</b></td><td><b>Value</b></td></tr>";
// We're using JDK 1.2 methods; that's OK since J2EE requires JDK 1.2
while (myEnum2.hasMore(  )) {
    Binding binding = (Binding) myEnum2.next(  );
    outString += "<tr><td>" + binding.getName(  ) + "</td>";
    outString += "<td>" + binding.getClassName(  ) + "</td>";
    outString += "<td>" + binding.getObject(  ) + "</td></tr>";
}
outString += "</table>";

String outString2 = "";

outString2 += "<b>RootList:</b> " + rootList + "<br>";
outString2 += "<b>ServerName:</b> " + request.getServerName() + "<br>";
outString2 += "<b>ServerSoftware:</b> " + getServletContext().getServerInfo() + "<br>";
outString2 += "<b>ServerCintextName:</b> " + getServletContext().getServletContextName() + "<br>";
outString2 += "<b>ServerProtocol:</b> " + request.getProtocol() + "<br>";
outString2 += "<b>ServerScheme:</b> " + request.getScheme() + "<br>";
outString2 += "<b>ServerPort:</b> " + request.getServerPort() + "<br>";
outString2 += "<b>RequestMethod:</b> " + request.getMethod() + "<br>";
outString2 += "<b>PathInfo:</b> " + request.getPathInfo() + "<br>";
outString2 += "<b>PathTranslated:</b> " + request.getPathTranslated() + "<br>";
outString2 += "<b>ScriptName:</b> " + request.getServletPath() + "<br>";
outString2 += "<b>DocumentRoot:</b> " + getServletContext().getRealPath("/") + "<br>";
outString2 += "<b>QueryString:</b> " + request.getQueryString() + "<br>";
outString2 += "<b>RemoteHost:</b> " + request.getRemoteHost() + "<br>";
outString2 += "<b>RemoteAddr:</b> " + request.getRemoteAddr() + "<br>";
outString2 += "<b>AuthType:</b> " + request.getAuthType() + "<br>";
outString2 += "<b>RemoteUser:</b> " + request.getRemoteUser() + "<br>";
outString2 += "<b>ContentType:</b> " + request.getContentType() + "<br>";
outString2 += "<b>ContentLength:</b> " + request.getContentLength() + "<br>";
outString2 += "<b>HTTPAccept:</b> " + request.getHeader("Accept") + "<br>";
outString2 += "<b>HTTPUserAgent:</b> " + request.getHeader("User-Agent") + "<br>";
outString2 += "<b>HTTPReferer:</b> " + request.getHeader("Referer") + "<br>";

outString = "<table border=0 width=80% ><tr><td>" + outString2 + outString + "</td><tr></table>";
//String myTestCString = "1234567890abcdefghijklmnopqrstuvwxyz";
//myTestCString += myTestCString + myTestCString + myTestCString + myTestCString + myTestCString + myTestCString;
//outString += "<br>" + Utils.colapseString("TestCstring", myTestCString, 60);

%>

<%= HtmlUtils.doSection("envTest1", "Environment Information", outString, false) %><br>
