<%@ page contentType="application/vnd.ms-excel" %><%@ page import="java.lang.*,java.io.*,java.util.*,
                                                                   gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,
                                                                   gov.ymp.csi.systems.*,gov.ymp.csi.auth.*,gov.ymp.csi.items.*" 
%><%

String docRoot = getServletContext().getRealPath("/");

DbConn myConn = new DbConn("csi");

String IDs = ((request.getParameter("id")  != null) ? (String) request.getParameter("id") : "0");
int ID = Integer.parseInt(IDs);
String callerIDs = ((request.getParameter("callerid")  != null) ? (String) request.getParameter("callerid") : "0");
int callerID = Integer.parseInt(callerIDs);

String outline = "";

long docID = (long) ID;

FilesItem fi = new FilesItem(docID, myConn);
if ((fi.getStatus() != null && fi.getStatus().equals("approved")) || fi.getOwner() == callerID) {  // if approved or owner=callingid
    ByteArrayOutputStream buffer = fi.getFileOutStream(myConn);


    response.setContentType( fi.getMimeType() );

    OutputStream output = new DataOutputStream( response.getOutputStream() );
    buffer.writeTo(output);
} else {
    response.setContentType( "text/html" );
    %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
<head>
<title>File Not Available</title>
</head>

<body bgcolor="#ffffff" text="#000000" link="#ff0000" vlink="#800000" alink="#ff00ff" background="?">
<h3>File Not Available</h3>
</body>
</html>
    <%
}

myConn.release();

%>