<%@ page import="gov.ymp.csi.shuzi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%
String outLine = "";
String qString = "";
DbConnH myConn = new DbConnH();
try {

		Connection conn = myConn.conn;
        outLine += "Connection Successful!<br>";	   
	    Statement stmt = conn.createStatement();
	    ResultSet rs;
	    qString = "SELECT USER_ID, USERNAME, PASSWORD FROM USER";
	    outLine += qString + "<br>";
	    rs = stmt.executeQuery(qString);
	    outLine += "query executed...<br>";
	    while(rs.next())
	      {
		outLine += rs.getInt("USER_ID") + ", ";
		outLine += rs.getString("USERNAME") + ", ";
		outLine += rs.getString("PASSWORD") + "<BR><BR>";
	      }
		
	    outLine += "Connection Closed and Released<br>";  
		  
	    rs.close();
	    stmt.close();
	    conn.close();
		myConn.release();
}
catch (Exception e) {
    outLine += "Exception caught: " + e.getMessage();
	myConn.release();
}
out.println(outLine);
%>