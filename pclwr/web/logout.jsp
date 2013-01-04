<HTML>

<HEAD>
<TITLE>ITS Software Work Request System</TITLE>
  <LINK href="css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="headerSetup.inc" %>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->

<%

session.setAttribute("user.id", null);
session.setAttribute("user.name", null);
session.setAttribute("user.fullname", null);
session.setAttribute("user.position", null);
session.setAttribute("user.positionid", null);
session.setAttribute("user.person", null);
session.setAttribute("user.permissionmap", null);


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
