<!-- Begin Image Frame Start ************************************************************************************************ -->

<% if (!request.getScheme().equals("https")) { %>
<!-- Force the use of https -->
<script language=javascript><!--
    document.location= 'https://<%= request.getServerName() %>:8443/<%= getServletContext().getServletContextName()  %>/home.jsp';
//-->
</script>
<% } else if (session.getAttribute("user.name")  != null) {
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
   } %>


<!-- table for whole page -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <!-- system logo and name display -->
  <tr bgcolor="#006699"> 
    <td colspan="2" height="79" background="images/topbkgd.gif"><a href="http://energy.gov"><img src="images/topleftskinny.jpg" width="140" height="80" border=0></a></td>
    <td width="505" height="79" valign="middle" background="images/topbkgd.gif"> 
      <p><font size="4" face="Arial, Helvetica, sans-serif" color="#FFFFFF">&nbsp;<font size="3"><b>OCRWM</b></font></font><br>
        <font face="Arial, Helvetica, sans-serif" size="5" color="#FFFFFF">&nbsp;<b> <%= SystemName %></b></font></p>
    </td>
  </tr>
  <tr> 
    <td width="251" height="2" bgcolor="#006699"></td>
    <td rowspan="3" height="16" bgcolor="#FFFFFF" valign="top" width="25"><img src="images/canto_1.gif" width="25" height="20"></td>
    <td width="505" bgcolor="#CC9900" height="2"></td>
  </tr>
  <tr> 
    <td width="251" bgcolor="#006699" height="14"></td>
    <td bgcolor="#000000" height="14" width="100%" class="toplinks"><b>&nbsp;
<!-- Start Top Nav bar contents ************************************************************************************************ -->
    <!-- common links -->
    <a href="https://ocrwmgateway.ymp.gov/ocrwmgateway" target="_blank"><font color="#CC9900">OCRWM Gateway</font></a>&nbsp;<font color="#CC9900">-</font>&nbsp;
    <a class="topnav" href="https://ocrwmgateway.ocrwm.doe.gov/ocrwm_concerns/" target="_blank">OCRWM Concerns </a>&nbsp;-&nbsp;
    <a class="topnav" href="http://connect.ymp.gov/index.shtml" target="_blank">USA RS Connect</a>&nbsp;-&nbsp;
    <a class="topnav" href="http://ocrwm.doe.gov" target="_blank">OCRWM.doe.gov</a></b>&nbsp;
<% if (isAuthenticated) { %>
    <!-- welcome message for logged in users -->
    &nbsp;-&nbsp;<b class="topnav"><a href="javascript:doUpdatePerson()"><font color="#ffffff">Welcome <%= session.getAttribute("user.fullname") %>!</font></a></b>
<% } %>
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
            &nbsp;<img src="images/menu.gif" width="150" height="15"><br>
<!-- Start Left Nav bar contents ************************************************************************************************ -->
<table width="150" border="0" cellspacing="1" cellpadding="3" bgcolor="#CCCCCC">
  <tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td width="123" bgcolor="#FFFFFF"><a class="leftnav" href="home.jsp">Home Page</a></td>
  </tr>
  <tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td width="123" bgcolor="#FFFFFF"><a class="leftnav" href="javascript:submitFooterForm('wrForm.jsp','new')">New&nbsp;Work&nbsp;Request&nbsp;&nbsp;</a></td>
  </tr>
  <!--tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td width="123" bgcolor="#FFFFFF"><p><a class="leftnav" href="#">Stuff</a></p></td>
  </tr-->
  <tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td width="123" bgcolor="#FFFFFF"><p><a class="leftnav" href="contact.jsp">Contact</a></p></td>
  </tr>
  <tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td bgcolor="#FFFFFF"><p><a class="leftnav" href="javascript:showHelpBox1()">Help ...</a>
<!-- hidden new items menu, shows up when new option is clicked -->
<span id=helpBox1 style="display:'none'">
<table border=0 align=center class="submenu">
<tr><td>
      <a href="/apps/itwr/help/NewWorkRequest.ppt" target="_blank">...entering&nbsp;a&nbsp;request</a><br>
      <a href="/apps/itwr/help/ReviewApprove.ppt" target="_blank">...with&nbsp;review/approval</a><br>
</td></tr>
</table>
</span>
<script language=javascript><!--
     // function to show/hide the new menu
    function showHelpBox1 () {
        mySection = document.getElementById('helpBox1');
        if (mySection.style.display=='none') {
            mySection.style.display='block';
        } else {
            mySection.style.display='none';
        }
    }
    // Firefox hack
    mySection = document.getElementById('helpBox1');
    mySection.style.display='none';

    //-->
</script>
    </p>    </td>
  </tr>
<% if (!isAuthenticated) { %>
  <!-- Menu optons that only show up if not logged in -->
  <tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td width="123" bgcolor="#FFFFFF"><p><a class="leftnav" href="javascript:showLoginBox1()">Login</a></p></td>
  </tr>
<% } %>
<% if (isAuthenticated) { %>
  <!-- Menu optons that only show up if logged in and an admin -->
    <% if (isAdmin) { %>
  <tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td bgcolor="#FFFFFF"><p><a class="leftnav" href="viewActivityLog.jsp">View Logs</a></p>    </td>
  </tr>
    <% } %>
  <tr>
    <td width="27" bgcolor="#FFFFFF"><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td bgcolor="#FFFFFF"><p><a class="leftnav" href="logout.jsp">Logout</a></p>    </td>
  </tr>
<% } %>

</table>

<% if (!isAuthenticated) { %>
<!-- hidden login form, shows up when login option is clicked -->
<span id=loginBox1 style="display:'none'">
<table border=0 align=center class="leftnav">
<form name='loginform' METHOD='POST' action=>
<tr><td align=right>Username</td><td><INPUT TYPE='text' NAME='username' class="leftnav" size=15></td></tr>
<tr><td align=right>Password</td><td><INPUT TYPE='password' NAME='pass' class="leftnav" size=15></td></tr>
<tr><td align=right>Domain</td><td><select name=domain size=1 class="leftnav">
<option value='ydservices'>YDServices</option>
<option value='rw.doe.gov'>RW.DOE.GOV</option>
<option value='ymservices'>YMServices</option>
</select></td></tr>
<!--input type=hidden name=nextscript value="<%= ("/" + getServletContext().getServletContextName() + request.getServletPath()) %>"-->
<input type=hidden name=nextscript value="home.jsp">
<input type=hidden name=requesttype value="frame">
<input type=hidden name=command value="test">
<!--<tr><td>&nbsp;</td><td align=center><a href="javascript:submitFormResults('doLogin','login')" class="leftnav">Sign In</a></td></tr>-->
<tr><td>&nbsp;</td><td align=center><input type=submit value='Sign In' class="leftnav" onClick="submitFormLoginResults('doLogin','login')"></td></tr>
</form>
</table>
</span>
<script language=javascript><!--
     // function to show/hide the login form
    function showLoginBox1 () {
        mySection = document.getElementById('loginBox1');
        if (mySection.style.display=='none') {
            mySection.style.display='block';
            document.loginform.username.focus();
            document.loginform.action="javascript:submitFormLoginResults('doLogin','login')";
        } else {
            mySection.style.display='none';
            document.loginform.action="";
        }
    }
    // Firefox hack
    mySection = document.getElementById('loginBox1');
    mySection.style.display='none';

   function submitFormLoginResults(script,command) {
       document.loginform.command.value = command;
       document.loginform.action = script;
       document.loginform.target = 'results';
       document.loginform.submit();
   }
    //-->
</script>
<% } else { %>
<script language=javascript><!--
    
    function doUpdatePerson() {
        document.main.id.value = <%= usr.getID() %>;
        submitForm("updatePerson.jsp","update")
    }
    //-->
</script>
<% } %>
<!-- end Left Nav bar contents ************************************************************************************************ -->
</td><td width="95%" valign="top" bordercolor="#000000">
<p class="toplinks"><br></p>

<!-- Setup form for page -->
<% String formName = "main"; %>
<form <%= ((useFileUpload) ? "enctype=\"multipart/form-data\" " : "") %>name='<%= formName %>' METHOD='POST' action=>

<input type=hidden name=id value='0'>
<input type=hidden name=command value='test'>

<%@ include file="notProductionWarning.inc" %>


<!-- End Image Frame Start ************************************************************************************************ -->
