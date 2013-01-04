<%@ include file="headerSetup.inc" %>
<%@ page import="gov.ymp.rdc.misc.*" %>
<% 
String myUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + session.getServletContext().getServletContextName() + request.getServletPath();
//String newsletterID = request.getParameter("newsletterid");
String newsletterID = request.getParameter("id");
newsletterID = ((newsletterID != null) ? newsletterID : "0");
boolean useCurrent = (!newsletterID.equals("0")) ? true : false;
String myFileLocation = PathToRoot + "/cached-content/ogp/director/newsletter-" + newsletterID + ".jsp_cache";
String newsletterName = "Directors-Newsletter";
%>
<rdc:cacheControl fileLocation="<%= myFileLocation %>" >
<%
DbConn myConn = new DbConn();
long currentID = RDCUtils.getCurrentNewsletterID(newsletterName, myConn);
String myFileLocationCurrent = PathToRoot + "/cached-content/ogp/director/newsletter-" + currentID + ".jsp_cache";
if (currentID == Long.parseLong(newsletterID) || newsletterID.equals("0")) {
    useCurrent = true;
    myFileLocation = PathToRoot + "/cached-content/ogp/director/newsletter-" + 0 + ".jsp_cache";
}
myConn.release();
%>
<HTML>
<!-- newsletterid: '<%= newsletterID %>' -->
<HEAD>
<title>From The Director</title>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
  <link rel=stylesheet href="/common/javascript/xc2/css/xc2_default.css" type="text/css">
  <script language="javascript" src="/common/javascript/xc2/config/xc2_default.js"></script>
  <script language="javascript" src="/common/javascript/xc2/script/xc2_inpage.js"></script>
<style type="text/css">
<!--
.style1 {font-style: italic}
.style2 {color: #000000}
.style3 {
	font-size: 14pt;
	font-weight: bold;
}
-->
</style>
</HEAD>

<body bgcolor="#ffffff" text="#000000" link="#ff0000" vlink="#800000" alink="#ff00ff">
<!--%@ include file="imageFrameStart.inc" %-->
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
<!-- Setup form for page -->
<% String formName = "main"; %>
<form <%= ((useFileUpload) ? "enctype=\"multipart/form-data\" " : "") %>name='<%= formName %>' METHOD='POST' action=>

<input type=hidden name=id value='0'>
<input type=hidden name=command value='test'>

<rdc:notProductionWarning size="100%" width="180" />


<!--  *********************************************************** -->
<!-- Begin Main Content -->
<input type=hidden name=dosecondcache value='<%= ((useCurrent) ? "T" : "F") %>'>
<input type=hidden name=secondcachefile value='<%= myFileLocationCurrent %>'>
  <table border=0 width="650">
    <tr><td style="background-image:url(/director/images/YuccaMtnWide.png);background-repeat: no-repeat;text-align:right;vertical-align:bottom;">
    <br><br><table style="color:#000000;background-color:#ffffff;filter:alpha(opacity=60);-moz-opacity:.60;opacity:.60;"><tr><td><li><a href="/documents/director/keyBehaviors2.pdf" target="_blank" style=""><span style="text-decoration:underline;color:#000000;"><span class="style2">Cultural Vision Statement &amp;<br>
      Key Behavioral Competencies </span></span></a></td></tr></table><!--img src="/director/images/YuccaMtnWide.png" width="750" height="61" border="0"--></td>
    </tr>
	<tr><td align=center><span class="style3">Cultural Vision Statement</span></td>
    <tr>
      <td bgcolor="#FFFFCC" align=center>
<strong>We support each other to be self –accountable and self-critical to continuously improve safety, quality, and performance.</strong> </td>
    </tr>
    <tr><td>&nbsp;<br></td></tr>
    <tr><td>
<p>
    <% 

//DbConn myConn = new DbConn("csi");

//myConn.release();

%>
<rdc:contentPane name="contentPane1" id="<%= newsletterName %>" isNewsletter="true" newsletterArchiveLength="6" newsletterShowDate="false" />
</td></tr>
  </table>


<rdc:isAuthenticated >
<rdc:hasPermission permission="3-updatehtmlcache" >
< rdc:buildCacheControl url="<%= myUrl %>" fileLocation="<%= myFileLocation %>" />
</rdc:hasPermission>
</rdc:isAuthenticated>

<!-- End Main Content -->
<!--%@ include file="imageFrameStop.inc" %-->
<!--  *********************************************************** -->
<rdc:envDisplay />
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

</BODY>

</HTML>
</rdc:cacheControl>
