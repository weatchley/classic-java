<HTML>

<HEAD>
<TITLE>Tribal Information Exchange</TITLE>
<script src=utilities.js></script>
</HEAD>

<BODY BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>
<%@ include file="headerSetup.html" %>
<% useFileUpload = false; %>
<%@ include file="imageFrameStart.html" %>
<!-- Begin Main Content -->
<%
String command = request.getParameter("command");
String id = request.getParameter("username");
String docRoot = getServletContext().getRealPath("/");
User myUser = null;
if (command.equals("updateuser")) {
    myUser = new User(id);
} else {
    myUser = new User("default");
    Date dNow = new Date();
    myUser.setDatePWExpires(Utils.dateToString(Utils.addDays(dNow, -1)));
    myUser.setUsername("");
    myUser.setFullName("");
}

Org[] orgList = Org.orgLookup();

%>


<h3>User FORM</h3>
<p>This form is for the entry and mantanence of users.</p>

<table border=0>
<tr><td>&nbsp;</td><td valign=top>User&nbsp;Name:&nbsp;&nbsp;&nbsp;</td><td>
<% if (command.equals("updateuser")) { %>
<%= myUser.getUsername() %><input type=hidden name=username value='<%= myUser.getUsername() %>'>
<% } else { %>
<input type=text name=username size=10 maxlength=25 value='<%= myUser.getUsername() %>'>
<% } %>
</td></tr>
<tr><td>&nbsp;</td><td valign=top>Full Name:&nbsp;&nbsp;&nbsp;</td><td><input type=text name=fullname size=20 maxlength=50 value='<%= myUser.getFullName() %>'></td></tr>
<tr><td>&nbsp;</td><td valign=top>Organization:&nbsp;&nbsp;&nbsp;</td><td><select name=org><option value=0>Please Select One</option>
<% for (int i=0; i<orgList.length; i++) { %>
    <option value='<%= orgList[i].getAbbreviation() %>'<%= ((myUser.getOrg().equals(orgList[i].getAbbreviation())) ? " selected" : "") %>><%= orgList[i].getName() %>
<% } %>

</select></td></tr>
<tr><td>&nbsp;</td><td valign=top>Status:&nbsp;&nbsp;&nbsp;</td><td><select name=status>
<option value='enabled'<%= ((myUser.isEnabled()) ? " selected" : "") %>>Enabled</option>
<option value='disabled'<%= ((!myUser.isEnabled()) ? " selected" : "") %>>Disabled</option>
</select></td></tr>

<tr><td>&nbsp;&nbsp;&nbsp;</td><td colspan=2 align=center>
<a href="javascript:verifySubmit(document.main)">Submit</a>
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
<a href="javascript:document.main.reset()">Clear Contents</a>
<% if (command.equals("updateuser")) { %>
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
<a href="javascript:submitFormResults('doUser','resetpassword')">Reset Password</a>
<% } %>
</td></tr>
</table>

<script language=javascript><!--

function verifySubmit (f){
// javascript form verification routine
    var msg = "";
    if (isblank(f.username.value)) {
        msg += "User Name must be entered.\n";
    }
    if (isblank(f.fullname.value)) {
        msg += "Full Name must be entered.\n";
    }
    if (f.org[0].selected) {
        msg += "Organization must be selected.\n";
    }
    if (msg != "") {
      alert (msg);
    } else {
        submitFormResults('doUser', '<%= command %>');
    }
}

//-->
</script>


<!-- End Main Content -->
<%@ include file="imageFrameStop.html" %>

</BODY>

</HTML>
