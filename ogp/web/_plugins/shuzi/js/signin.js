try {
	dxmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
} catch (e) {
	try {
  		dxmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
  	} catch (e) {
		try {
			dxmlhttp=new XMLHttpRequest();
		}
		catch (e) {
	   		dxmlhttp = false;
			alert("You're browser does not support AJAX");
		}
  	}
}

function trimIt(str) {
	return str.replace(/^\s*|\s*$/g,"");
}

function signOut() {
	dxmlhttp.open("GET","../../_plugins/signin/signout.jsp",true);
	dxmlhttp.send(null);
	userFullName = "Guest";
	try {
		changeUsername();
	}
	catch(e) {
	}
	if(window.location.href!='./')	window.location.href='../../';
}

function submitSignIn(e) {
	var keycode;
	if (window.event) keycode = window.event.keyCode;
	else if (e) keycode = e.which;
	if (keycode==13) {
		document.signForm.signsubmit.click();
	}
}

function signIn(redirecturl,target) {
	dxmlhttp.open("POST","../../doLogin",false);
	dxmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	dxmlhttp.send("command=login&username="+document.signForm.userName.value+"&pass="+escape(document.signForm.password.value)+"&domain="+document.signForm.domain.value+"&requesttype=simple");
	if (dxmlhttp.responseText) {
		if (trimIt(dxmlhttp.responseText)=="success") {
			document.getElementById('signin').style.visibility='hidden';
			for (i=0; i<document.body.childNodes.length; i++) {
				if ((top.document.body.childNodes[i].id!="signin") && (top.document.body.childNodes[i].nodeType==1)) {
					document.body.childNodes[i].style.filter='none';
					document.body.childNodes[i].style.opacity='1';
					document.body.childNodes[i].style.MozOpacity='1';
					document.body.childNodes[i].style.KhtmlOpacity='1';
				}
			}
			
			try {
				dxmlhttp.open("GET","../../_plugins/signin/checkLogin.jsp",false);
				dxmlhttp.send(null);
				userFullName = trimIt(dxmlhttp.responseText);
				window.location.reload();
			}
			catch(e) {
			}
			
		}
		else {
			alert("Login Failed.\nPlease Try Again.");
		}
	}
	else {
		alert("Login Failed.\nPlease Try Again.");
	}
	$('activity').hide();
}

function cancelSignInNew() {
	document.getElementById('signin').style.visibility='hidden';
		for (i=0; i<document.body.childNodes.length; i++) {
				if ((top.document.body.childNodes[i].id!="signin") && (top.document.body.childNodes[i].nodeType==1)) {
					document.body.childNodes[i].style.filter='alpha(opacity=100)';
					document.body.childNodes[i].style.opacity='1';
					document.body.childNodes[i].style.MozOpacity='1';
					document.body.childNodes[i].style.KhtmlOpacity='1';
					top.debug("cancelSignInNew() called...");
				}
		}	
}

function cancelSignIn() {
	document.getElementById('signin').style.visibility='hidden';
	for (i=0; i<document.body.childNodes.length; i++) {
		if ((top.document.body.childNodes[i].id!="signin") && (top.document.body.childNodes[i].nodeType==1)) {
			document.body.childNodes[i].style.filter='alpha(opacity=100)';
			document.body.childNodes[i].style.opacity='1';
			document.body.childNodes[i].style.MozOpacity='1';
			document.body.childNodes[i].style.KhtmlOpacity='1';
			if(window.location.href!='./')	window.location.href='../../';
		}
	}	
}

function popSignIn(redirecturl,target) {
	if (!document.getElementById('signin')) {
	top.document.body.innerHTML += "<div id='signin'><form name='signForm'><center><b>Please Sign In</b><br><table border=0><tr><td align='left'>Username:</td><td align='left'><input type='text' name='userName' onkeypress='submitSignIn(e)'></td><tr><td align='left'>Password:</td><td align='left'><input type='password' name='password' onkeypress='submitSignIn(e)'></td><tr><td align='left'>Domain:</td><td align='left'><select name='domain'><option value='ydservices'>YDServices</option><option value='rw.doe.gov'>RW.DOE.GOV</option><option value='ymservices'>YMServices</option></select></td></table><br><input type='button' id='signsubmit' name='signsubmit' value='Sign In' onClick=\"$('activity').show();setTimeout('signIn()',1000)\">&nbsp;<input type='button' value='Cancel' onClick='cancelSignInNew()'><br><br><div id='activity' style='display:none;'><img src='../../graphics/activity.gif'><br>sign in in progress...<br><br></div></center></form></div>";
	}
	var signwin = document.getElementById('signin');
	for (i=0; i<top.document.body.childNodes.length; i++) {
		if ((top.document.body.childNodes[i].id!="signin") && (top.document.body.childNodes[i].nodeType==1)) {
			top.document.body.childNodes[i].style.filter='alpha(opacity=30)';
			top.document.body.childNodes[i].style.opacity='.3';
			top.document.body.childNodes[i].style.MozOpacity='.3';
			top.document.body.childNodes[i].style.KhtmlOpacity='.3';
		}
	}
	signwin.style.visibility='visible';
	signwin.style.backgroundColor='#FFFFFF';
	signwin.style.border='2px outset #FFFFFF';
	signwin.style.position='absolute';
	signwin.style.display='block';
	signwin.style.left=(top.document.body.clientWidth - signwin.scrollWidth)/2+"px";
	signwin.style.top=(top.document.body.clientHeight/2) - (signwin.scrollHeight)+"px";
	signwin.style.paddingLeft='10px';
	signwin.style.paddingRight='10px';
	signwin.style.paddingTop='10px';
		signwin.style.zIndex='4';
		//signwin.style.zIndex=Windows.maxZIndex + 10;
	document.signForm.userName.select();
	document.signForm.userName.focus();
	
}

function checkSignIn(redirecturl,target) {
	dxmlhttp.open("GET","../../_plugins/signin/checkLogin.jsp",false);
	dxmlhttp.send(null);
	userFullName = trimIt(dxmlhttp.responseText);
	try {
		changeUsername();
	}
	catch(e) {
	}
	if ((userFullName=="Guest") || (userFullName==null)) {
		if (redirecturl!='undefined') {
			popSignIn(redirecturl,target);
		}
		else {
			popSignIn();
		}
	}
	else {
		if (redirecturl!='undefined') {
			window.open(redirecturl,target);
		}
	}
}