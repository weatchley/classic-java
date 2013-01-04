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
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");
usr = new Person((String) session.getAttribute("user.name"));
//DbConn myConn = new DbConn("csi");
String tempText = null;
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
%>
  

<p>Welcome to the ITSS Materials Management System.</p>
<p>This read only version of the system performs the following functions:</p>
<ul>
<li>Display a list of purchase documents.</li>
<li>Display information about a selected purchase document.</li>
</ul>


<%
//myConn.release();
%>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
