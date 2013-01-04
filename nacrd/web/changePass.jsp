<HTML>

<HEAD>
<TITLE>Tribal Information Exchange</TITLE>
</HEAD>

<BODY BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>
<%@ include file="headerSetup.html" %>
<%@ include file="imageFrameStart.html" %>
<!-- Begin Main Content -->



<script language="JavaScript">
<!--
document.main.command.value='changepassword';
//-->
</script>
<h2 align=center>Change Password</h2>
<table border=0 align=center>
<tr height=30><td>&nbsp;</td></tr>
<tr><td valign=top><table border=0 align=center>
<input type=hidden name=username value='<%= session.getAttribute("user.name") %>'>
<tr><td align=right><b>User:</b></td><td><%= session.getAttribute("user.name") %></td></tr>
<tr><td align=right><b>Old Password:</b></td><td><INPUT TYPE='password' NAME='oldpass'></td></tr>
<tr><td align=right><b>New Password:</b></td><td><INPUT TYPE='password' NAME='newpass1'></td></tr>
<tr><td align=right><b>Re-Enter&nbsp;Password:</b></td><td><INPUT TYPE='password' NAME='newpass2'></td></tr>
<tr><td>&nbsp;</td><td align=center><a href="javascript:submitFormResults('doUser','changepassword')"><b>Submit</b></a></td></tr>
</table>
</td><td valign=top><%= User.testPasswordRules() %></td></tr></table>



<!-- End Main Content -->
<%@ include file="imageFrameStop.html" %>

</BODY>

</HTML>
