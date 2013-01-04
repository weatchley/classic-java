<%@ page contentType="application/vnd.ms-excel" %><%@ page import="java.lang.*,java.io.*,java.util.*,
                                                                   gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,
                                                                   gov.ymp.csi.systems.*,gov.ymp.csi.auth.*,gov.ymp.csi.items.*,
                                                                   gov.ymp.mms.model.*" 
%><%

String docRoot = getServletContext().getRealPath("/");

DbConn myConn = new DbConn("csi");

String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
String ID = ((request.getParameter("id")  != null) ? (String) request.getParameter("id") : "0");
String ldate = ((request.getParameter("lookupdate")  != null) ? (String) request.getParameter("lookupdate") : "0");

String outline = "";

int locID = 0;

ByteArrayOutputStream buffer = new ByteArrayOutputStream();

Archive item = new Archive(ID,Utils.toDate(ldate, "MM/dd/yyyy HH:mm:ss"), myConn);

response.setContentType( FilesItem.lookupMimeType(myConn, "pdf") );
//response.setHeader("Content-Disposition", "inline; filename=" + item.getFileName() );
response.setHeader("Content-Disposition", "attachment; filename=mms-" + ID + ".pdf" );

DataOutput output = new DataOutputStream( response.getOutputStream() );
byte[] bytes = item.getImageBytes(myConn);
response.setContentLength(bytes.length);
for( int i = 0; i < bytes.length; i++ ) { output.writeByte( bytes[i] ); }

myConn.release();

%>