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
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");
usr = new Person((String) session.getAttribute("user.name"));
%>
  

  Welcome<rdc:isAuthenticated >&nbsp;<%= session.getAttribute("user.fullname") %></rdc:isAuthenticated><br>
  <br>

  This is the Core Service Infrastructure (CSI) site administration application, which is used to perform the following functions:</p>
<ul>
<li>Manage permissions, roles, positions (future), and people (or users)</li>
<li>Perform other site administrative functions on those systems or applications that are integrated with the CSI.</li>
</ul>
<p>Currently, any user can log in to the system; however, he or she must be assigned permissions to perform most functions. A general user who does not have any assigned permissions will be able to update his or her contact information.</p>
<p>Systems that are integrated with CSI may have one or more associated permissions to use various features in that system. One or more permissions are grouped together into roles. These roles have positions assigned to them. Each position can have one and only one person assigned to it; however, a person can have more than one position. In the current system, each person has one default position. A future version of the system will allow for multiple positions to reflect the organizational structure.<br>
</p>
<p>&nbsp; </p>
<br>




<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
