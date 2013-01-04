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
	dxmlhttp.open("GET","_plugins/signin/signout.jsp",false);
	dxmlhttp.send(null);
	userFullName = "Guest";
	try {
		setTimeout('changeUsername()',1000);
	}
	catch(e) {
	}
	//if(window.location.href!='./')	window.location.href='./';
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
	dxmlhttp.open("POST","doLogin",false);
	//alert("login.do?userName="+document.signForm.userName.value+"&password="+document.signForm.password.value+"&domain="+document.signForm.domain.value);
	//dxmlhttp.send("userName="+document.signForm.userName.value+"&password="+document.signForm.password.value+"&domain="+document.signForm.domain.value);
	dxmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	//alert("command=login&username="+document.signForm.userName.value+"&pass="+document.signForm.password.value+"&domain="+document.signForm.domain.value+"&requesttype=simple");
	dxmlhttp.send("command=login&auto=0&username="+document.signForm.userName.value+"&pass="+escape(document.signForm.password.value)+"&domain="+document.signForm.domain.value+"&requesttype=simple");
	if (dxmlhttp.responseText) {
		if (trimIt(dxmlhttp.responseText)=="success") {
		
			document.getElementById('signin').style.visibility='hidden';
			for (i=0; i<document.body.childNodes.length; i++) {
				if ((top.document.body.childNodes[i].id!="signin") && (top.document.body.childNodes[i].nodeType==1)) {
					document.body.childNodes[i].style.filter='none';
					document.body.childNodes[i].style.opacity='1';
					document.body.childNodes[i].style.MozOpacity='1';
					document.body.childNodes[i].style.KhtmlOpacity='1';
					//if(redirecturl=='favorites-redux.html') alert(redirecturl);
				}
			}
			
			try {
				dxmlhttp.open("GET","_plugins/signin/checkLogin.jsp",false);
				dxmlhttp.send(null);
				userFullName = trimIt(dxmlhttp.responseText);
				changeUsername();
				
				//createCookie('ogpLoginName',document.signForm.userName.value,30);
			}
			catch(e) {
			}
			var tString = new String(""); 
			try{ tString = window.frames.mainiframe.sub_function();	}	
			catch(e){}
			
			if(tString.lastIndexOf( "/" ) != -1 ) 
			{
  			    fname = tString.substring( tString.lastIndexOf( "/" ) + 1,tString.length );
  				//alert(fname);
  				if(fname=='favorites-redux.html')
  				{
  				    //top.debug("************* Authenicated user with dev signin ****************** ");
  				   	window.frames.mainiframe.initTabsMod();
				   	window.frames.mainiframe.initRtMenu()
  				  
  		       }
			}
			
		}
		else {
		      //alert("Login Failed.\nPlease Try Again."); modified 2/20/2007 per Dave J.
			  alert("Your Username or Password was not recognized. \n" +
                    "Be sure you are using your Windows log-in ID and password.\n" +
                    "If you continue to have problems accessing the Gateway, please call your helpdesk at 202-586-5153  for (HQ - OCRWM ITS Help Desk) and 702-794-1335 for (LV - DOE Technical Support).");
                  
		}
	}
	else {
		   //alert("Login Failed.\nPlease Try Again."); modified 2/20/2007 per Dave J.
		   alert("Your Username or Password was not recognized. \n" +
                    "Be sure you are using your Windows log-in ID and password.\n" +
                    "If you continue to have problems accessing the Gateway, please call your helpdesk at 202-586-5153  for (HQ - OCRWM ITS Help Desk) and 702-794-1335 for (LV - DOE Technical Support).");
                  
	}
	$('activity').hide();
}

	var CompName = "";
	var uName = "";
	var uDomain = "";
	
function signInAuto() {
	var xFlag = readEnv();
	if(xFlag) {
		if((uName!="")&&(uDomain!="")){
		//alert(CompName+', '+uName+', '+uDomain);
				var url = "doLogin";
	              var parameters = "command=login&";
	              parameters += "username="+uName+"&";
	              parameters += "pass=&";
	              parameters += "domain="+uDomain+"&";
	              parameters += "requesttype=simple&auto=1"
	              //alert("parameters="+parameters);
	              var ajaxRequest = new Ajax.Request(url,
	              {
	                     method: 'post',
	                     parameters: parameters,
	                     onLoading: function() { //
	                     //changeUsernameLogin();
	            		 },
	                     onSuccess: function(transport){
	      				 var response = transport.responseText || "no response text";
	      				 //alert("responseText="+response);
	      				 
		      				 if (trimIt(response)=="success") {
									/*
									document.getElementById('signin').style.visibility='hidden';
									for (i=0; i<document.body.childNodes.length; i++) {
										if ((top.document.body.childNodes[i].id!="signin") && (top.document.body.childNodes[i].nodeType==1)) {
											document.body.childNodes[i].style.filter='none';
											document.body.childNodes[i].style.opacity='1';
											document.body.childNodes[i].style.MozOpacity='1';
											document.body.childNodes[i].style.KhtmlOpacity='1';
										}
									}
									*/
									try {
										dxmlhttp.open("GET","_plugins/signin/checkLogin.jsp",false);
										dxmlhttp.send(null);
										userFullName = trimIt(dxmlhttp.responseText);
										changeUsername();
									}
									catch(e) {
									}
								}else{changeUsernameGuest();}	      				 
	    				 },
	    				 onFailure: function(){ //
	    				 //alert("signInAuto: Ajax call failed!");
	    				 }
	              });
	              
	              
	    }
	}

/*
	dxmlhttp.open("POST","doLogin",false);
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
				dxmlhttp.open("GET","_plugins/signin/checkLogin.jsp",false);
				dxmlhttp.send(null);
				userFullName = trimIt(dxmlhttp.responseText);
				changeUsername();
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
*/
}

function readEnv(){
		try{
			var Shell = new ActiveXObject("WScript.Shell") ;
			CompName = Shell.ExpandEnvironmentStrings("%COMPUTERNAME%")
			uName = Shell.ExpandEnvironmentStrings("%USERNAME%")
			uDomain = Shell.ExpandEnvironmentStrings("%USERDOMAIN%")
			return true
			}catch(e){return false}
}

function createCookie(name,value,days) {
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}

function eraseCookie(name) {
	createCookie(name,"",-1);
}

function cancelSignIn() {
	document.getElementById('signin').style.visibility='hidden';
	for (i=0; i<document.body.childNodes.length; i++) {
		if ((top.document.body.childNodes[i].id!="signin") && (top.document.body.childNodes[i].nodeType==1)) {
			document.body.childNodes[i].style.filter='alpha(opacity=100)';
			document.body.childNodes[i].style.opacity='1';
			document.body.childNodes[i].style.MozOpacity='1';
			document.body.childNodes[i].style.KhtmlOpacity='1';
			window.location.href='./';
		}
	}	
}

function popSignIn(redirecturl,target) {
	/*
	if (!document.getElementById('signin')) {
		if (redirecturl!='undefined') {
			top.document.body.innerHTML += "<div id='signin'><form name='signForm'><center><b>Please Sign In</b><br><table border=0><tr><td align='left'>Username:</td><td align='left'><input type='text' name='userName' onkeypress='submitSignIn(e)'></td><tr><td align='left'>Password:</td><td align='left'><input type='password' name='password' onkeypress='submitSignIn(e)'></td><tr><td align='left'>Domain:</td><td align='left'><select name='domain'><option value='ydservices'>YDServices</option><option value='rw.doe.gov'>RW.DOE.GOV</option><option value='ymservices'>YMServices</option></select></td></table><br><input type='button' id='signsubmit' name='signsubmit' value='Sign In' onClick=\"$('activity').show();setTimeout('signIn()',1000)\">&nbsp;<input type='button' value='Cancel' onClick='cancelSignIn()'><br><div id='activity' style='display:none;'><img src='graphics/activity.gif'><br>sign in in progress...</div></center></form></div>";
		}
		else {
			top.document.body.innerHTML += "<div id='signin'><form name='signForm'><center><b>Please Sign In</b><br><table border=0><tr><td align='left'>Username:</td><td align='left'><input type='text' name='userName' onkeypress='submitSignIn(e)'></td><tr><td align='left'>Password:</td><td align='left'><input type='password' name='password' onkeypress='submitSignIn(e)'></td><tr><td align='left'>Domain:</td><td align='left'><select name='domain'><option value='ydservices'>YDServices</option><option value='rw.doe.gov'>RW.DOE.GOV</option><option value='ymservices'>YMServices</option></select></td></table><br><input type='button' id='signsubmit' name='signsubmit' value='Sign In' onClick=\"$('activity').show();setTimeout('signIn()',1000)\">&nbsp;<input type='button' value='Cancel' onClick='cancelSignIn()'><br><div id='activity' style='display:none;'><img src='graphics/activity.gif'><br>sign in in progress...</div></center></form></div>";
		}
		
	}
	*/
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
	document.signForm.userName.select();
	document.signForm.userName.focus();
}

function checkSignIn2(redirecturl,target) {
	dxmlhttp.open("GET","_plugins/signin/checkLogin.jsp",false);
	dxmlhttp.send(null);
	userFullName = trimIt(dxmlhttp.responseText);
	try {
		changeUsername();
		
	}
	catch(e) {}
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
		//alert('3');
			window.open(redirecturl,target);
		}
	}
}
