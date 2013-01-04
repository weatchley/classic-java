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
<input type=hidden name=nextscript value="<%= ("/" + getServletContext().getServletContextName() + request.getServletPath()) %>">
<!--input type=hidden name=nextscript value="home.jsp"-->
<input type=hidden name=requesttype value="frame">
<input type=hidden name=command value="test">
<!--<tr><td>&nbsp;</td><td align=center><a href="javascript:submitFormResults('doLogin','login')" class="leftnav">Sign In</a></td></tr>-->
<tr><td>&nbsp;</td><td align=center><input type=submit value='Sign In' class="leftnav" onClick="submitFormLoginResults('/rdc/doLogin','login')"></td></tr>
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
            document.loginform.action="javascript:submitFormLoginResults('/rdc/doLogin','login')";
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
