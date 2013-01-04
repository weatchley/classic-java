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

DbConn myConn = new DbConn("csi");
// set up, get page content


myConn.release();

%>

<h3 align=center> This application is a repository of components for use in other applications </h3>
<br><br>
<rdc:productionTest onMatch="false" >
<h3 align=center>RDC - Reusable Dynamic Components<br>Developer Information</h3>

<table border=0 align=center>
<tr><td valign=top><b>API:</b></td>
<td> &nbsp; &nbsp; &nbsp;</td>
<td valign=top><a href="docs/api/index.html">HTML</a><!--br>
<a href="docs/javadocs.pdf">PDF</a--><br>
</td></tr>

<tr><td valign=top><b>Library:</b></td>
<td> &nbsp; &nbsp; &nbsp;</td>
<td valign=top><a href="lib/rdc.jar">Jar file</a>
</td></tr>
</table>

</rdc:productionTest>

<script language="javascript">
<!--

-->
</script>




<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>


