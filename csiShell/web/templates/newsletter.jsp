<%@ include file="headerSetup.inc" %>
<%@ page import="gov.ymp.rdc.misc.*" %>
<% 
String myUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + session.getServletContext().getServletContextName() + request.getServletPath();
String newsletterID = request.getParameter("newsletterid");
newsletterID = ((newsletterID != null) ? newsletterID : "0");
boolean useCurrent = (!newsletterID.equals("0")) ? true : false;
String myFileLocation = PathToRoot + "/cached-content/rdc/newsletter-" + newsletterID + ".jsp_cache";
String newsletterName = "RDC-Newsletter";
%>
<rdc:cacheControl fileLocation="<%= myFileLocation %>" >
<%
DbConn myConn = new DbConn();
long currentID = RDCUtils.getCurrentNewsletterID(newsletterName, myConn);
String myFileLocationCurrent = PathToRoot + "/cached-content/rdc/newsletter-" + currentID + ".jsp_cache";
if (currentID == Long.parseLong(newsletterID) || newsletterID.equals("0")) {
    useCurrent = true;
    myFileLocation = PathToRoot + "/cached-content/rdc/newsletter-" + 0 + ".jsp_cache";
}
myConn.release();
%>
<HTML>
<!-- newsletterid: '<%= newsletterID %>' -->
<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
  <link rel=stylesheet href="/common/javascript/xc2/css/xc2_default.css" type="text/css">
  <script language="javascript" src="/common/javascript/xc2/config/xc2_default.js"></script>
  <script language="javascript" src="/common/javascript/xc2/script/xc2_inpage.js"></script>
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<input type=hidden name=dosecondcache value='<%= ((useCurrent) ? "T" : "F") %>'>
<input type=hidden name=secondcachefile value='<%= myFileLocationCurrent %>'>
<p>
    <% 

//DbConn myConn = new DbConn("csi");

//myConn.release();

%>
<rdc:contentPane name="contentPane1" id="<%= newsletterName %>" isNewsletter="true" newsletterArchiveLength="6" />


< rdc:buildCacheControl url="<%= myUrl %>" fileLocation="<%= myFileLocation %>" />

<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
</rdc:cacheControl>
