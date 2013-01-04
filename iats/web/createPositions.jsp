<%@ page import="java.sql.*" %>
<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->


<%
DbConn myConn = new DbConn("csi");
//String sqlcode = "";
//Statement stmt = myConn.conn.createStatement();
//sqlcode = "SELECT p.id, p.username, p.firstname, p.lastname, p.domainid, d.name FROM csi.person p, csi.domains d " +
//    "WHERE d.id=p.domainid AND p.domainid > 0 AND p.id NOT IN (SELECT NVL(personid, 0) FROM csi.position)";
//ResultSet rs = null;
//rs = stmt.executeQuery (sqlcode);

//int i = 0;
//while (rs.next()) {
//    Position myPos = new Position();
//    myPos.setTitle("Default position for " + rs.getString(2) + "-" + rs.getInt(5));
//    myPos.setDescription("Position for " + rs.getString(3) + " " + rs.getString(4) + " of " + rs.getString(6));
//    myPos.setPersonID(rs.getLong(1));
//    myPos.add(myConn);
//    i++;
//}
//rs.close();
Position.createInitial(myConn);
myConn.release();

%>

This run of "Create Initial Positions" was successful.


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
