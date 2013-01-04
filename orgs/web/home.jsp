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
String fullUserName = (String) session.getAttribute("user.fullname");
%>


Welcome <%= ((fullUserName != null) ? fullUserName : "Guest") %>!<br>
<br>
This is an application to hold Organizational pages.<br>
It was probably reached in error.
<br>

<br>
Some Test links:<br>
<ul>
<li><a href="td.jsp?pagehash=OGP-PageContent-OD" target="_blank">Test 1 (OGP-PageContent-OD)</a></li>
<li><a href="td.jsp?pagehash=OGP-PageContent-Sample" target="_blank">Test 2 (OGP-PageContent-Sample)</a></li>
<li><a href="td.jsp?pagehash=OGP-PageContent-Sample-leftnav" target="_blank">Test 3 (OGP-PageContent-Sample-leftnav)</a></li>
<!--li><a href="td.jsp?pagehash=" target="_blank">Test  ()</a></li>
<li><a href="td.jsp?pagehash=" target="_blank">Test  ()</a></li>
<li><a href="td.jsp?pagehash=" target="_blank">Test  ()</a></li>
<li><a href="td.jsp?pagehash=" target="_blank">Test  ()</a></li>
<li><a href="td.jsp?pagehash=" target="_blank">Test  ()</a></li>
<li><a href="td.jsp?pagehash=" target="_blank">Test  ()</a></li>
<li><a href="td.jsp?pagehash=" target="_blank">Test  ()</a></li-->
</ul>



<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
