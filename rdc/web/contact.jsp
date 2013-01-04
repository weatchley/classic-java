<%@ include file="headerSetup.inc" %>
<% 
String myUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + session.getServletContext().getServletContextName() + request.getServletPath();
String myFileLocation = PathToRoot + "/cached-content/rdc/contact.jsp_cache";
%>
<rdc:cacheControl fileLocation="<%= myFileLocation %>" >
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 

//DbConn myConn = new DbConn("csi");

//myConn.release();

%>
<rdc:contentPane name="contentPane1" id="RDC-Contacts" />


<rdc:buildCacheControl url="<%= myUrl %>" fileLocation="<%= myFileLocation %>" />

<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
</rdc:cacheControl>
