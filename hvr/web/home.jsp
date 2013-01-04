<%@ include file="headerSetup.inc" %>
<rdc:sessionTest />
<HTML>

<HEAD>
<TITLE>OCRWM Las Vegas Visitor Requests Database - Login Page<rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
<link type="text/css" rel="stylesheet" href="css/hvr.css" />
</HEAD>

<BODY BGCOLOR='white' onload=document.login.username.focus()>
<rdc:notProductionWarning size="150%" width="180" />
<rdc:isAuthenticated >
<script language="javascript">
<!--
    document.location = 'hvr_enterrequest.jsp';
-->
</script>
</rdc:isAuthenticated>
<rdc:isAuthenticated doOpposite="true" >
<script language="javascript">
<!--
    document.location = 'login.jsp';
-->
</script>
</rdc:isAuthenticated>
<rdc:envDisplay />
</BODY>

</HTML>

