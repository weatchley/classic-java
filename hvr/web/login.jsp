<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE>OCRWM Las Vegas Visitor Requests Database - Login Page<rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
<link type="text/css" rel="stylesheet" href="css/hvr.css" />
</HEAD>

<BODY BGCOLOR='white' onload=document.login.username.focus()>

<table align=center bordercolor=#000fab bordercolorlight=#b2b7e5 bordercolordark=#00095f border=20 width=750><tr><td>
<!-- table align=center bordercolor=#000fab bordercolorlight=#fab000 bordercolordark=#000fab border=20 width=750><tr><td -->
<table align=center>
<tr><td align=left>
<h1>OCRWM Las Vegas Visitor Requests Database</h1>
<h2>Login</h2>
<h3>(Log in with your network username and password)</h3>

</td></tr>
</table>


<rdc:notProductionWarning size="150%" width="180" />

<FORM ACTION='hvr' METHOD='POST' name=login>
<input type=hidden name=authenticated value=no>
<input type=hidden name=userid value=0>

<table align=center width=220>
<tr><td class=label>Username: </td><td class=formw><INPUT TYPE='text' NAME='username'> </td></tr>
<tr><td class=label>Password: </td><td class=formw><INPUT TYPE='password' NAME='pass'> </td></tr>
<tr><td class=label>Domain: </td><td class=formw><select name=domain size=1>
<option value='ydservices'>YDServices</option>
<option value='ymservices'>YMServices</option>
<!--option value='rw.doe.gov'>RW.DOE.GOV</option-->
<option value='localdomain'>LocalDomain</option>
</select><br></td></tr>
<!-- tr><td><input type=hidden name=domain value='ydservices'><BR></td></tr  -->
<tr><td colspan=2 align=center><INPUT TYPE='submit' value='Log in'></td></tr>
</table>
<br><br>
<table width=600 align=center>
<tr><td>
This database can be accessed only by OCRWM West and direct support contractors and is used to request escorted or unescorted access for individuals visiting OCRWM, Las Vegas, NV facilities.  
This includes temporary access for DOE badged individuals who currently are not authorized to access OCRWM Las Vegas, NV facilites.
<br><br>
<b>This database cannot be used for Permanent Access Requests.</b> Permanent Access Requests must be submitted to OCRWM Security by Management (DOE, M&O, or DOE Direct Support Contractor).
<br><br>
</td></tr>
</table>
</FORM>
</td></tr></table>
<rdc:envDisplay />
</BODY>

</HTML>

