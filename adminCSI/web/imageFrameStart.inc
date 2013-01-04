<!-- Begin Image Frame Start ************************************************************************************************ -->
<script language=javascript><!--
   function submitForm(script,command) {
       document.main.command.value = command;
       document.main.target = '_self';
       document.main.action = script;
       document.main.submit();
   }
   function submitFormResults(script,command) {
       document.main.command.value = command;
       document.main.action = script;
       document.main.target = 'results';
       document.main.submit();
   }
//-->
</script>
<script language=javascript><!--
    document.body.style.cursor = 'default';
//-->
</script>

<% if (!request.getScheme().equals("https")) { %>
<!-- Force the use of https -->
<script language=javascript><!--
    document.location= 'https://<%= request.getServerName() %>:8443/<%= getServletContext().getServletContextName()  %>/home.jsp';
//-->
</script>
<% } else {
      %> <rdc:sessionTest /> <%
      if (session.getAttribute("user.name")  != null && !((String) session.getAttribute("user.name")).toLowerCase().equals("guest")) {
           // get information about user: name, privs, etc...
           isAuthenticated = true;
           sUsr = new Person((String) session.getAttribute("user.name"));
           usr = (Person) session.getAttribute("user.person");
           pos = (Position) session.getAttribute("user.position");
           posID = (long) new Long((String) session.getAttribute("user.positionid")).longValue();
           perMap = (HashMap) session.getAttribute("user.permissionmap");
           if (pos.belongsTo(((Long) perMap.get("8-admin")).longValue())) {
                   isAdmin = true;
           }
      }
   } %>


<!-- table for whole page -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <!-- system logo and name display -->
  <tr bgcolor="#006699"> 
    <td colspan="2" height="79" background="/rdc/images/topbkgd.gif"><a href="http://energy.gov"><img src="/rdc/images/topleftskinny.jpg" width="140" height="80" border=0></a></td>
    <td width="505" height="79" valign="middle" background="/rdc/images/topbkgd.gif"> 
      <p><font size="4" face="Arial, Helvetica, sans-serif" color="#FFFFFF">&nbsp;<font size="3"><b>OCRWM</b></font></font><br>
        <font face="Arial, Helvetica, sans-serif" size="5" color="#FFFFFF">&nbsp;<b> <%= SystemName %></b></font></p>
    </td>
  </tr>
  <tr> 
    <td width="251" height="2" bgcolor="#006699"></td>
    <td rowspan="3" height="16" bgcolor="#FFFFFF" valign="top" width="25"><img src="/rdc/images/canto_1.gif" width="25" height="20"></td>
    <td width="505" bgcolor="#CC9900" height="2"></td>
  </tr>
  <tr> 
    <td width="251" bgcolor="#006699" height="14"></td>
    <td bgcolor="#000000" height="14" width="100%" class="toplinks"><!--b>&nbsp;-->
<!-- Start Top Nav bar contents ************************************************************************************************ -->
<rdc:topMenu id="RDC-TopNav" target="_blank" showUser="true" editUser="true" cssStyle="topnav" >
<!--{"text":"test","link":"http://ymp.gov"}-->
</rdc:topMenu >
<!--rdc:topMenu id="<%= Long.parseLong("19923") %>" target="_blank" showUser="true" editUser="true" cssStyle="topnav" /-->
<!-- End Top Nav bar contents ************************************************************************************************ -->
    </td>
  </tr>
  <tr> 
    <td width="251" bgcolor="#CC9900" height="2"></td>
    <td bgcolor="#CC9900" height="2" width="505"></td>
  </tr>
  <tr valign="top"> 
    <td colspan="3"> 
      <table width="100%" border="0" cellspacing="4" cellpadding="0">
        <tr> 
          <td width="192" valign="top"> <br>
            &nbsp;<img src="/rdc/images/menu.gif" width="150" height="15"><br>
<!-- Start Left Nav bar contents ************************************************************************************************ -->

<rdc:leftMenu id="RDC-LeftNav" includeLogin="true" >
<!--JSON Strings 1 per line?-->
<rdc:hasPermission permission="1-viewlog" >
{"text":"ViewActivity Log","link":"viewActivityLog.jsp"}
</rdc:hasPermission>
<rdc:isAuthenticated >
<!-- Manage submenu********************************************* -->
<rdc:leftMenu menuType="submenu" title="Manage..." >
<rdc:hasPermission permission="1-permissionsandroles" >
{"text":"Permissions","link":"permissions.jsp"}
{"text":"Roles","link":"roles.jsp"}
{"text":"Collections","link":"javascript:goToLinkHourglass('collections.jsp')"}
{"text":"Domains","link":"javascript:goToLinkHourglass('domains.jsp')"}
</rdc:hasPermission>
<rdc:hasPermission permission="1-admin" >
{"text":"People","link":"person.jsp"}
</rdc:hasPermission>
<rdc:hasPermission permission="1-textitems" >
{"text":"Text Items","link":"textItems.jsp"}
</rdc:hasPermission>
</rdc:leftMenu>
<!-- New submenu********************************************* -->
<rdc:leftMenu menuType="submenu" title="New..." >
<rdc:hasPermission permission="1-permissionsandroles" >
{"text":"Permission","link":"javascript:submitForm('updatePermission.jsp','add')"}
{"text":"Role","link":"javascript:submitForm('updateRole.jsp','add')"}
{"text":"Domain","link":"javascript:submitForm('updateDomain.jsp','add')"}
{"text":"Person","link":"javascript:submitForm('updatePerson.jsp','add')"}
</rdc:hasPermission>
<rdc:hasPermission permission="1-admin" >
<!--{"text":"People","link":"person.jsp"}-->
{"text":"UList Collection","link":"javascript:submitForm('updateUList.jsp','add')"}
{"text":"UHash Collection","link":"javascript:submitForm('updateUHash.jsp','add')"}
</rdc:hasPermission>
<rdc:hasPermission permission="1-textitems" >
{"text":"Text Item","link":"javascript:submitForm('updateTextItem.jsp','add')"}
</rdc:hasPermission>
</rdc:leftMenu>
</rdc:isAuthenticated>
<rdc:hasPermission permission="1-permissionsandroles" >
{"text":"Create Initial Positions","link":"createPositions.jsp"}
</rdc:hasPermission>
<rdc:hasPermission permission="1-admin" >
{"text":"LDAP User Synchronization","link":"ldapsync.jsp"}
</rdc:hasPermission>
</rdc:leftMenu>
<rdc:isAuthenticated doOpposite="true" >
<%@ include file="loginForm.inc" %>
</rdc:isAuthenticated>
<script language=javascript><!--
   function goToLinkHourglass(link) {
       document.body.style.cursor = 'wait';
       document.location = link;
   }
//-->
</script>
<!-- end Left Nav bar contents ************************************************************************************************ -->
</td><td width="95%" valign="top" bordercolor="#000000">
<p class="toplinks"><br></p>

<!-- Setup form for page -->
<% String formName = "main"; %>
<form <%= ((useFileUpload) ? "enctype=\"multipart/form-data\" " : "") %>name='<%= formName %>' METHOD='POST' action=>

<input type=hidden name=id value='0'>
<input type=hidden name=command value='test'>

<rdc:notProductionWarning size="150%" width="180" />



<!-- End Image Frame Start ************************************************************************************************ -->
