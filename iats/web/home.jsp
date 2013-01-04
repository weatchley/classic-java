<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>

<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
  <script type="text/javascript" src="javascript/utils.js"></script>
  <meta http-equiv="refresh" content="1;url=.">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");
usr = new Person((String) session.getAttribute("user.name"));
%>
Loading...
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
