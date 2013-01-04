<%@ page import="gov.ymp.csi.shuzi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%
String outLine = "";
String qString = "";
String SCHEMA = "higashis";
DbConn myConn = new DbConn(SCHEMA);
try {
        Connection conn = myConn.conn;
	    outLine += "Connection Successful<br>";
	    
	    Statement stmt = conn.createStatement();
	    ResultSet rs;
	    qString = "SELECT USERID, USERNAME, USERPASS FROM " + SCHEMA + ".SH_USERS";
	    outLine += qString + "<br>";
	    rs = stmt.executeQuery(qString);
	    while(rs.next())
	      {
		outLine += rs.getInt("USERID") + "<BR>";
		outLine += rs.getString("USERNAME") + "<BR>";
		outLine += rs.getString("USERPASS") + "<BR><BR>";
	      }
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