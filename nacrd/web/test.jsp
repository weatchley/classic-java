<HTML>

<HEAD>
<TITLE>Test - Tribal Information Exchange</TITLE>
</HEAD>

<BODY BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>
<%@ include file="headerSetup.html" %>
<% session.setAttribute("user.name", "atchleyb"); %>
<%@ include file="imageFrameStart.html" %>
<!-- Begin Main Content -->


<h3>Environment Test</h3>

<%@ page import="java.io.*,java.util.*,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,org.w3c.dom.*,org.xml.sax.SAXException,org.xml.sax.SAXParseException,gov.ymp.nacrd.*,javax.naming.*,gov.ymp.util.*,java.sql.*" %>
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
session.setAttribute("user.name", "atchleyb");
session.setAttribute("user.fullname", "Bill Atchley");
session.setAttribute("user.test", "atchleyb");
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


//
// ------------------------------------------------------------------------------------
//
// Begin NACRD specific tests
//
outString += "<br><i>Begin NACRD specific tests</i><br>\n";

// create test document directory in not there
//String testDir = docRoot + "data/documents/doc0001";
//File d1 = new File(testDir);
//if (!d1.exists()) {
//    new File(testDir).mkdirs();
//}

// generate a new date and random id
String testID = Utils.genNewID();

//  write out a test file
//String testFile = docRoot + "data/documents/doc0001/" + testID + ".txt";
//FileWriter f1 = new FileWriter(testFile);
//f1.write("Test Text\nDid it work?\n");
//f1.close();

// read a file to a string
//String docInfo = docRoot + "data/documents/doc0001/info.xml";
//FileReader f2 = new FileReader(docInfo);
//StringBuffer sb = new StringBuffer();
//char[] b = new char[1024];
//int n;
//while ((n = f2.read(b)) > 0) {
//    sb.append(b, 0, n);
//}
//f2.close();
//String inStr = sb.toString();

// read document xml info file
//Doc myDoc = new Doc(docRoot, "doc0001");
//outString += "<br>Number: " + myDoc.getNumber() + ", Filename: " + myDoc.getFilename() + ", DueDate: " + myDoc.getDueDate() + "<br>\n";
//outString += "Title: " + myDoc.getTitle() + "<br>\n";

// read user xml fle
//User usr = new User("atchleyb");
//outString += "<br>Username: " + usr.getUsername() + ", Full Name: " + usr.getFullName() + ", Org: " + usr.getOrg() + ", Date PW Expires: " + usr.getDatePWExpires() + "<br>\n";

//outString += "<b>Password:</b> " + jcrypt.crypt ("da", "PASSWORD") + ", Validated: " + usr.verifyPassword("PASSWORD") + "<br>\n";

//outString += "<br>ID = " + testID + "<br>\n";

//outString += "<br>Dir Listing<br>\n";
//String[] dir = new java.io.File(docRoot + "data/documents").list();
//int next = 0;
//for (int i=0; i<dir.length; i++) {
//    outString += dir[i] + "<br>\n";
//}
String SCHEMA = "nacrd";
DbConn myConn = new DbConn();
Statement stmt = myConn.conn.createStatement();
ResultSet rset = stmt.executeQuery ("SELECT count(*) FROM " + SCHEMA + ".documents");
rset.next();
int aSize = rset.getInt(1);
rset = stmt.executeQuery ("SELECT id,number,filename,archivefilename,duedate,archivedate,type,status,title " +
    "FROM " + SCHEMA + ".documents " +
    "ORDER BY number");
String[] mydir = new String[aSize];
int i = 0;
while (rset.next()) {
    mydir[i] = rset.getString(2);
    i++;
}
outString += "<br>Dir Listing 2<br>\n";
for (i=0; i<mydir.length; i++) {
    outString += mydir[i] + "<br>\n";
}
myConn.release();


//
// End NACRD specific tests
//
// ------------------------------------------------------------------------------------
//


%>

<%= "<b>RootList:</b> " + rootList %><br>
<%= "<b>ServerName:</b> " + request.getServerName() %><br>
<%= "<b>ServerSoftware:</b> " + getServletContext().getServerInfo() %><br>
<%= "<b>ServerContextName:</b> " + getServletContext().getServletContextName() %><br>
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

<%= " test " //inStr %><br>


<!-- End Main Content -->
<%@ include file="imageFrameStop.html" %>

</BODY>

</HTML>
