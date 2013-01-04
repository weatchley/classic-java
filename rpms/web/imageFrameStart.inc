<!-- Begin Image Frame Start ************************************************************************************************ -->
<!-- Setup form for page -->
<% String formName = "main"; %>
<form <%= ((useFileUpload) ? "enctype=\"multipart/form-data\" " : "") %>name='<%= formName %>' METHOD='POST' action=>

<input type=hidden name=id value=''>
<input type=hidden name=command value=''>

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
           if (pos.belongsTo(((Long) perMap.get("7-manfaq")).longValue()) || 
               pos.belongsTo(((Long) perMap.get("7-manhomepage")).longValue()) ||
               pos.belongsTo(((Long) perMap.get("7-mannewsletter")).longValue())) {
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
    <a class="topnav" href="http://connect.ymp.gov" target="_blank">BSCconnect</a>&nbsp;-&nbsp;
    <a class="topnav" href="http://ocrwm.doe.gov" target="_blank">OCRWM.doe.gov</a></b>&nbsp;-&nbsp;
    <!-- lookup selection list (rpms specific) -->
    <select class="topnav" name=titlebarlookup size=1 onChange=doTitleBarLookup()><option id=0>View Selection</option>
    <%
    FOrganization [] foList = FOrganization.getItemList();
    if (foList != null) {
        for (int i=0; i<foList.length; i++) {
            %><option value='<%= foList[i].getID() %>'><%= foList[i].getDescriptionBrief(60) %></option>
            <%
        }
    }
    %>
    </select>
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
    <td width="123" bgcolor="#FFFFFF"><p><a class="leftnav" href="javascript:showBrowseBox1()">Browse ...</a>
<!-- hidden browse menu, shows up when browse option is clicked -->
<span id=browseBox1 style="display:'none'">
<table border=0 align=center class="submenu">
<tr><td>
      <a href="javascript:document.main.id.value = 0;submitForm('recordSeries.jsp','na');">By&nbsp;Record&nbsp;Series</a><br>
      <a href="javascript:submitForm('recordSeries.jsp', 'locationbrowse')">By&nbsp;Location</a><br>
      <a href="javascript:submitForm('browseCN.jsp', 'na')">By&nbsp;Common&nbsp;Name</a><br>
</td></tr>
</table>
</span>
<script language=javascript><!--
     // function to show/hide the browse menu
    function showBrowseBox1 () {
        mySection = document.getElementById('browseBox1');
        if (mySection.style.display=='none') {
            mySection.style.display='block';
        } else {
            mySection.style.display='none';
        }
    }
    // Firefox hack
    mySection = document.getElementById('browseBox1');
    mySection.style.display='none';

    //-->
</script>
    </p></td>
  </tr>
  <tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td width="123" bgcolor="#FFFFFF"><p><a class="leftnav" href="reports.jsp">Reports</a></p></td>
  </tr>
  <tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td width="123" bgcolor="#FFFFFF"><p><a class="leftnav" href="newsletter.jsp">Information Exchange</a></p></td>
  </tr>
  <tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td width="123" bgcolor="#FFFFFF"><p><a class="leftnav" href="faq.jsp">FAQ's</a></p></td>
  </tr>
  <tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td width="123" bgcolor="#FFFFFF"><p><a class="leftnav" href="contactUs.jsp">Contact Us</a></p></td>
  </tr>
  <!--tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td width="123" bgcolor="#FFFFFF"><p><a class="leftnav" href="#">Stuff</a></p></td>
  </tr-->
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
  <tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td width="123" bgcolor="#FFFFFF"><p><a class="leftnav" href="archiveList.jsp">Archive</a></p></td>
  </tr>
  <tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td bgcolor="#FFFFFF"><p><a class="leftnav" href="javascript:showNewBox1()">New ...</a>
<!-- hidden new items menu, shows up when new option is clicked -->
<span id=newBox1 style="display:'none'">
<table border=0 align=center class="submenu">
<tr><td>
      <a href="javascript:document.main.id.value = 0;submitForm('recordSeriesForm.jsp', 'new');">Record Series</a><br>
      <a href="javascript:submitForm('requirements.jsp', 'new')">Requirements</a><br>
      <a href="javascript:submitForm('fOrganizations.jsp', 'new')">Functional&nbsp;Organizations</a><br>
      <a href="javascript:submitForm('cCategories.jsp', 'new')">Citation&nbsp;Categories</a><br>
      <a href="javascript:submitForm('cutoffList.jsp', 'new')">Cutoff&nbsp;List</a><br>
      <a href="javascript:submitForm('retentionPeriod.jsp', 'new')">Retention&nbsp;Periods</a><br>
      <a href="javascript:submitForm('retentionCodes.jsp', 'new')">Retention&nbsp;Codes</a><br>
      <a href="javascript:submitForm('aRestrictions.jsp', 'new')">Access&nbsp;Restrictions</a><br>
</td></tr>
</table>
</span>
<script language=javascript><!--
     // function to show/hide the new menu
    function showNewBox1 () {
        mySection = document.getElementById('newBox1');
        if (mySection.style.display=='none') {
            mySection.style.display='block';
        } else {
            mySection.style.display='none';
        }
    }
    // Firefox hack
    mySection = document.getElementById('newBox1');
    mySection.style.display='none';

    //-->
</script>
    </p>    </td>
  </tr>
  <!--tr>
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td bgcolor="#FFFFFF"><p><a class="leftnav" href="#">Utlities</a></p>    </td>
  </tr>
  <tr-->
    <td width="27" bgcolor="#FFFFFF" valign=top><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td bgcolor="#FFFFFF"><p><a class="leftnav" href="javascript:showManageBox1()">Manage ...</a>
<!-- hidden manage items menu, shows up when manage option is clicked -->
<span id=manageBox1 style="display:'none'">
<table border=0 align=center class="submenu">
<tr><td>
      <a href="javascript:submitForm('recordSeries.jsp', 'none')">Record Series</a><br>
      <a href="javascript:submitForm('requirements.jsp', 'none')">Requirements</a><br>
      <a href="javascript:submitForm('fOrganizations.jsp', 'none')">Functional&nbsp;Organizations</a><br>
      <a href="javascript:submitForm('cCategories.jsp', 'none')">Citation&nbsp;Categories</a><br>
      <a href="javascript:submitForm('cutoffList.jsp', 'none')">Cutoff&nbsp;List</a><br>
      <a href="javascript:submitForm('retentionPeriod.jsp', 'none')">Retention&nbsp;Periods</a><br>
      <a href="javascript:submitForm('retentionCodes.jsp', 'none')">Retention&nbsp;Codes</a><br>
      <a href="javascript:submitForm('aRestrictions.jsp', 'none')">Access&nbsp;Restrictions</a><br>
</td></tr>
</table>
</span>
<script language=javascript><!--
     // function to show/hide the manage menu
    function showManageBox1 () {
        mySection = document.getElementById('manageBox1');
        if (mySection.style.display=='none') {
            mySection.style.display='block';
        } else {
            mySection.style.display='none';
        }
    }
    // Firefox hack
    mySection = document.getElementById('manageBox1');
    mySection.style.display='none';

    //-->
</script>
    </p>    </td>
  </tr>
    <% } %>
  <tr>
    <td width="27" bgcolor="#FFFFFF"><img src="images/seta.gif" width="7" height="9" align="right"></td>
    <td bgcolor="#FFFFFF"><p><a class="leftnav" href="logout.jsp">Logout</a></p>    </td>
  </tr>
<% } %>

</table>
<script language=javascript><!--
    
    function doTitleBarLookup() {
        var val = document.main.titlebarlookup.selectedIndex;
        if (val != 0) {
            //document.main.id.value = val;
            submitForm("recordSeries.jsp","locationview")
        }
    }

    //-->
</script>
<% if (!isAuthenticated) { %>
<!-- hidden login form, shows up when login option is clicked -->
<span id=loginBox1 style="display:'none'">
<table border=0 align=center class="leftnav">
<tr><td align=right>Username</td><td><INPUT TYPE='text' NAME='username' class="leftnav" size=15></td></tr>
<tr><td align=right>Password</td><td><INPUT TYPE='password' NAME='pass' class="leftnav" size=15></td></tr>
<tr><td align=right>Domain</td><td><select name=domain size=1 class="leftnav">
<option value='ydservices'>YDServices</option>
<option value='rw.doe.gov'>RW.DOE.GOV</option>
<option value='ymservices'>YMServices</option>
</select></td></tr>
<input type=hidden name=nextscript value="<%= ("/" + getServletContext().getServletContextName() + request.getServletPath()) %>">
<input type=hidden name=requesttype value="frame">
<!--<tr><td>&nbsp;</td><td align=center><a href="javascript:submitFormResults('doLogin','login')" class="leftnav">Sign In</a></td></tr>-->
<tr><td>&nbsp;</td><td align=center><input type=submit value='Sign In' class="leftnav" onClick="submitFormResults('doLogin','login')"></td></tr>
</table>
</span>
<script language=javascript><!--
     // function to show/hide the login form
    function showLoginBox1 () {
        mySection = document.getElementById('loginBox1');
        if (mySection.style.display=='none') {
            mySection.style.display='block';
            document.<%= formName %>.username.focus();
            document.<%= formName %>.action="javascript:submitFormResults('doLogin','login')";
        } else {
            mySection.style.display='none';
            document.<%= formName %>.action="";
        }
    }
    // Firefox hack
    mySection = document.getElementById('loginBox1');
    mySection.style.display='none';

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


<%@ include file="notProductionWarning.inc" %>


<!-- End Image Frame Start ************************************************************************************************ -->
