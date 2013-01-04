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
<rdc:sessionClose />

<%

session.setAttribute("user.id", null);
session.setAttribute("user.name", null);
session.setAttribute("user.fullname", null);
session.setAttribute("user.position", null);
session.setAttribute("user.positionid", null);
session.setAttribute("user.person", null);
session.setAttribute("user.permissionmap", null);
session.setAttribute("user.authenticationlevel", null);


%>

<script language="JavaScript">
<!--
document.location='home.jsp';
//-->
</script>



<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
