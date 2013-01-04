<%@ page contentType="application/vnd.ms-excel" %><%@ page import="java.lang.*,java.io.*,java.util.*,
                                                                   gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,
                                                                   gov.ymp.csi.systems.*,gov.ymp.csi.auth.*,gov.ymp.csi.items.*,
                                                                   gov.ymp.rpms.model.*,
                                                                   com.lowagie.text.*,com.lowagie.text.pdf.*,com.lowagie.text.rtf.*,org.json.*" 
%><%

String docRoot = getServletContext().getRealPath("/");

DbConn myConn = new DbConn("csi");
RecordSeries [] list = null;
FOrganization loc = null;



String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
String IDs = ((request.getParameter("id")  != null) ? (String) request.getParameter("id") : "0");
int ID = Integer.parseInt(IDs);

String outline = "";

int locID = 0;

ByteArrayOutputStream buffer = new ByteArrayOutputStream();
response.setContentType( "application/pdf" );


Archive arc = new Archive(ID, myConn);
DataOutput output = new DataOutputStream( response.getOutputStream() );
byte[] bytes = arc.getImageBytes(myConn);
response.setContentLength(bytes.length);
for( int i = 0; i < bytes.length; i++ ) { output.writeByte( bytes[i] ); }

myConn.release();

%>