<%@ include file="headerSetup.inc" %>
<% 
String pageHash = ((request.getParameter("pagehash")  != null) ? (String) request.getParameter("pagehash") : "OGP-PageContent-OD");

DbConn myConn = new DbConn("csi");
UHash pageContent = new UHash(pageHash, myConn);

TextItem tiPageTitle = new TextItem(pageContent.get("title"), myConn);
String pageTitle = tiPageTitle.getText();

UList ulLeftNav = new UList(pageContent.get("leftnav"), myConn);
String leftNav = ulLeftNav.getDescription();

%>
<HTML>

<HEAD>
<TITLE><%= pageTitle %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>

<!-- Begin Image Frame Start ************************************************************************************************ -->
<!-- Common javascript utilities -->
<script language=javascript><!--
   function submitForm(script,command) {
       document.main.command.value = command;
       document.main.target = '_self';
       document.main.action = script;
       document.main.submit();
   }
   function submitFooterForm(script,command) {
       document.footerform.command.value = command;
       document.footerform.action = script;
       document.footerform.submit();
   }
   function submitFormResults(script,command) {
       document.main.command.value = command;
       document.main.action = script;
       var saveTarget = document.main.target;
       document.main.target = 'results';
       document.main.submit();
       document.main.target = saveTarget;
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
        <font face="Arial, Helvetica, sans-serif" size="5" color="#FFFFFF">&nbsp;<b> <%= pageTitle %></b></font></p>
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
<rdc:topMenu id="RDC-TopNav" target="_blank" showUser="false" editUser="false" cssStyle="topnav" >
<!--{"text":"test","link":"http://ymp.gov"}-->
</rdc:topMenu >
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

<rdc:leftMenu id="<%= ulLeftNav.getID() %>" includeLogin="false" >
</rdc:leftMenu>

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

<!-- Begin Main Content ************************************************************************************************ -->
<p>

<rdc:contentPane name="contentPane1" id="<%= pageHash %>"  />


<% 
myConn.release();
%>


<!-- End Main Content ************************************************************************************************ -->

<!-- Begin Image Frame Stop *********************************************************** -->
<rdc:envDisplay />
</TD>

		<TD>
			<IMG SRC="/rdc/images/spacer.gif" WIDTH=1 HEIGHT=383></TD>
	</TR>
      </table>
    </td>
  </tr>
  <tr> 
    <td width="251" height="2" bgcolor="#CC9900"></td>
    <td rowspan="3" height="2" valign="top" width="25"><img src="/rdc/images/canto_2.gif" width="25" height="20"></td>
    <td width="505" bgcolor="#CC9900" height="2"></td>
  </tr>
  <tr> 
    <td width="251" height="14" bgcolor="#000000" align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#FFFFFF">&nbsp; 
      </font></td>
    <td width="100%" bgcolor="#006699" height="14" align="center"><font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif">
    &nbsp;</font></td>
  </tr>
  <tr> 
    <td width="251" bgcolor="#CC9900" height="2"></td>
    <td width="505" bgcolor="#006699" height="2"></td>
  </tr>
  <tr> 
    <td width="251" bgcolor="#006699">&nbsp;</td>
    <td width="25" bgcolor="#006699">&nbsp;</td>
    <td width="505" bgcolor="#006699" valign="bottom" align="right"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">&nbsp;&nbsp;&nbsp;</font></td>
  </tr>
</table>
</form>
<!-- set up iframe for submitting changes to the database -->
<rdc:productionTest onMatch="true" status="prod" >
    <iframe src="/rdc/blank.jsp" width=1 height=1 align=left name=results border=0 frameborder=0></iframe>
</rdc:productionTest>
<rdc:productionTest onMatch="false" status="prod"  >
    <iframe src="/rdc/blank.jsp" width=60 height=60 align=left name=results border=0 frameborder=0></iframe>
</rdc:productionTest>

<form name='footerform' METHOD='POST' action=>
<input type=hidden name=command value="test">
</form>
<!-- End Image Frame Stop *********************************************************** -->

</BODY>

</HTML>
