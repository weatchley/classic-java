<%@ include file="headerSetup.inc" %>
<HTML>
<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
<script src=/common/javascript/utilities.js></script>
<script src=/common/javascript/widgets.js></script>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
DbConn myConn = new DbConn();

Person per = null;
boolean isExpired = false;

//System.out.println("changePassword.jsp - Got Here 1");
if (usr != null) {
    per = new Person();
    per.getInfo(myConn, usr.getID());
} else {
    per = (Person) session.getAttribute("tmp.user.person");
    per.getInfo(myConn);
    isExpired = true;
}

%>
<table border=0>
<tr><td colspan=3 align=center>
<b>Change Password</b>
<% if (isExpired) { %>
    <br>(Expired password must be changed to continue to log in)
<% } %>
<br></td></tr>
<tr><td valign=top>
<table border=0>
<tr><td>User: </td><td><%= per.getUserName() %></td></tr>
<tr><td>Old Password: </td><td><input type=password size=30 maxlength=30 name=pass></td></tr>
<tr><td>New Password: </td><td><input type=password size=30 maxlength=30 name=newpass></td></tr>
<tr><td>Retype New Password: </td><td><input type=password size=30 maxlength=30 name=newpass2></td></tr>
<tr><td colspan=2><input type=button name=submitit value="Submit" onClick="verifySubmit(document.main)"></td></tr>
</table>

<input type=hidden name=username value="<%= per.getUserName() %>">
<input type=hidden name=domain value="<%= per.getDomainName() %>">

<input type=hidden name=nextscript value="<%= ("/" + getServletContext().getServletContextName() + "/home.jsp") %>">

</td><td> &nbsp; &nbsp; &nbsp; </td><td valign=top>
<table border=1 cellpadding=0 cellspacing=0><tr><td>
<%= LocalUser.testPasswordRules() %>
</td></tr></table>
</td></tr></table>

<script language=javascript><!--

function verifySubmit (f){
// javascript form verification routine
    var msg = "";
    if (isblank(f.pass.value)) {
        msg += "Current/old password must be entered.\n";
    }
    if (isblank(f.newpass.value) || isblank(f.newpass2.value)) {
        msg += "New password must be entered twice.\n";
    }
    if (!isblank(f.newpass.value) && f.newpass.value != f.newpass2.value) {
        msg += "New passwords do not match.\n";
    }
    if (msg != "") {
      alert (msg);
    } else {
        f.id.value = <%= per.getID() %>;
        <% if (isExpired) { %>
            submitFormResults('/rdc/doLogin', 'changepassword');
        <% } else { %>
            submitFormResults('/rdc/doPerson', 'changepassword');
        <% } %>
    }
}

//-->
</script>




<%
myConn.release();

%>



<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
