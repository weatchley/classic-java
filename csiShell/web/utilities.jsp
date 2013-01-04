<%@ include file="headerSetup.inc" %>
<HTML>
<%@ include file="headerSetup.inc" %>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<% 
String docRoot = getServletContext().getRealPath("/");

%>

<h3>Utilities</h3>
<% if (isAuthenticated) { %>
  <% if (isAdmin) { %>
      <% if (pos.belongsTo(((Long) perMap.get("1-viewlog")).longValue())) { %>
         <a href="javascript:submitForm('viewActivityLog.jsp', 'none')">View Activity Log</a><br>
      <% } %>
      <% if (pos.belongsTo(((Long) perMap.get("1-permissionsandroles")).longValue())) { %>
         <a href="javascript:submitForm('permissions.jsp', 'none')">Manage Permissions</a><br>
         <a href="javascript:submitForm('roles.jsp', 'none')">Manage Roles</a><br>
         <a href="javascript:submitForm('createPositions.jsp', 'none')">Create Initial Positions</a><br>
      <% } %>
      <!--<a href="javascript:submitForm('positions.jsp', 'none')">Manage Positions</a><br>-->
      Manage Positions<br>
      <!--Manage People<br>-->
      <a href="javascript:submitForm('person.jsp', 'none')">Manage People</a><br>

  <% } %>
<% } %>

         <script language=javascript><!--
//
//
         //-->
         </script>

<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
