<HTML>

<HEAD>
<TITLE>Tribal Information Exchange</TITLE>
</HEAD>

<BODY BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>
<%@ include file="headerSetup.html" %>
<%@ include file="imageFrameStart.html" %>
<!-- Begin Main Content -->
<% 
String docRoot = getServletContext().getRealPath("/");
User usr = new User((String) session.getAttribute("user.name"));
%>


Welcome <%= session.getAttribute("user.fullname") %>!<br>
<br>
The information exchange is a clearinghouse and contact point for
information on the Yucca MountainProject's Cultural Resources
Management Program.  Though this site, you can read and comment
on cultural resource documents currently under review, as
will as visit the document archive.<br>
<br>
Hardcopies of the documents are available upon request by calling<br>
1-800-22506972 or writing:<br>
<br>
&nbsp;&nbsp;&nbsp;U.S. Department of Energy<br>
&nbsp;&nbsp;&nbsp;Office of Repository Development<br>
&nbsp;&nbsp;&nbsp;1551 Hillshire Drive<br>
&nbsp;&nbsp;&nbsp;Las Vegas, NV 89134<br>
<br>
Cultural Resources Manager: Lee Bishop<br>
<br>
Intergovernmental Relations: Robert Lupton<br>
<br>
Web Contact:<br>
...<br>


<!-- End Main Content -->
<%@ include file="imageFrameStop.html" %>

</BODY>

</HTML>
