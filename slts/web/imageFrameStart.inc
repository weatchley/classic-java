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
   function submitFormNewWindow(script,command) {
       document.main.command.value = command;
       document.main.action = script;
       var saveTarget = document.main.target;
       document.main.target = '_blank';
       document.main.submit();
       document.main.target = saveTarget;
   }
//-->
</script>
<script src=/common/javascript/widgets.js></script>
<script src=/common/javascript/utilities.js></script>
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
{"text":"Contact","link":"contact.jsp"}
<rdc:hasPermission permission="12-viewlog" >
{"text":"ViewActivity Log","link":"viewActivityLog.jsp"}
</rdc:hasPermission>
<!-- Browse submenu********************************************* -->
<rdc:leftMenu menuType="submenu" title="Browse..." >
{"text":"SW Products","link":"swBrowse.jsp"}
{"text":"Computers","link":"computerBrowse.jsp"}
{"text":"SW Inventory","link":"swInvBrowse.jsp"}
</rdc:leftMenu>
{"text":"Search","link":"search.jsp"}
<!-- Reports submenu********************************************* -->
<rdc:leftMenu menuType="submenu" title="Reports..." >
{"text":"PC App Report","link":"appPCReport.jsp"}
{"text":"Multi Search Report","link":"multiSearch.jsp"}
</rdc:leftMenu>
<rdc:isAuthenticated >
<!-- Manage submenu********************************************* -->
<rdc:hasPermission permission="12-mansw" >
<rdc:leftMenu menuType="submenu" title="Manage..." >
<rdc:hasPermission permission="12-admin" >
{"text":"Domains","link":"domains.jsp"}
{"text":"Ignored Applications","link":"ignoredApps.jsp"}
{"text":"Ignored Services","link":"ignoredServices.jsp"}
{"text":"Ignored Computers","link":"ignoredComputers.jsp"}
</rdc:hasPermission>
</rdc:leftMenu>
</rdc:hasPermission>
<!-- New submenu********************************************* -->
<rdc:hasPermission permission="12-mansw" >
<rdc:leftMenu menuType="submenu" title="New..." >
{"text":"SW Product","link":"javascript:submitForm('swProductForm.jsp','new')"}
</rdc:leftMenu>
</rdc:hasPermission>
<!-- Utilities submenu********************************************* -->
<rdc:leftMenu menuType="submenu" title="Utilities..." >
<rdc:hasPermission permission="12-admin" >
{"text":"Refresh Altiris DS Data","link":"refreshDSData.jsp"}
{"text":"Update Counts","link":"updateCounts.jsp"}
<rdc:hasPermission permission="1-admin" >
{"text":"Initial SW Import -only run once","link":"initialSWImport.jsp"}
</rdc:hasPermission>
</rdc:hasPermission>
</rdc:leftMenu>
</rdc:isAuthenticated>
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
