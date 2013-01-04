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
	//top.debug("Making call to signOut() ..... @@@@ ");
	top.initHomeMessage();
	
 	dxmlhttp.open("GET","_plugins/signin/signout.jsp",false);
	dxmlhttp.send(null);
	userFullName = "Guest";
	top.userName = "Guest";
	
	//top.debug("signOut(): "+ top.userName);
	document.signForm.userName.value = "";document.signForm.password.value = "";
	
	//top.window.frames.userlogin = false;
	//top.window.frames.islogin();
	//top.window.frames.mainiframe.checkFavoriteTabCookies();
    //top.window.frames.mainiframe.initFavoriteTabs();
    //window.location.href='./index.jsp";
    
    //  refresh page or dynamically put default tabs back in place 
    //  alert("What is the value in signin.js for xml calll " +  dxmlhttp.responseText);
    
                
	try
	{ 
	    ////top.debug("Calling signout ........ ");
	    
	   // alert("making call to setDefaultTabSignout() .... ");
	    
	    setDefaultTabSignout(); // sets the default setting back 
	                            // for each of the tabs 
	  setTimeout('changeUsername()',1000);
	  
	  	
	}
	catch(e) {
	
	 //alert("An error occured in sign.js --> " + e.message );
	 
	}
	//if(window.location.href!='./')	window.location.href='./';
}


function signOutNew() {
 	dxmlhttp.open("GET","_plugins/signin/signout.jsp",false);
	dxmlhttp.send(null);
	userFullName = "Guest";
	top.userName = "Guest";
	document.signForm.userName.value = "";document.signForm.password.value = "";    
	if(window.location.href!='./')	window.location.href='./';
}

function submitSignIn(e) {
	var keycode;
	if (window.event) keycode = window.event.keyCode;
	else if (e) keycode = e.which;
	if (keycode==13) {
		document.signForm.signsubmit.click();
	}
}

function signInNew(redirecturl,target) {
top.debug("signInNew called!");
  	dxmlhttp.open("POST","doLogin",false);
	dxmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	dxmlhttp.send("command=login&auto=0&username="+document.signForm.userName.value+"&pass="+escape(document.signForm.password.value)+"&domain="+document.signForm.domain.value+"&requesttype=simple");
	if (dxmlhttp.responseText) 
	{
		if (trimIt(dxmlhttp.responseText)=="success") 
		{
	       //top.debug("response text equal to sucesss ..... ");
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
			
			try 
			{
				dxmlhttp.open("GET","_plugins/signin/checkLogin.jsp",false);
				dxmlhttp.send(null);
				userFullName = trimIt(dxmlhttp.responseText);
				 top.userName = document.signForm.userName.value;
				 top.debug(top.userName);
	          	 changeUsernameNew();				  
				 	setTimeout("top.callAjax('getUID', false , false)",1000); 
				 	setTimeout("top.callAjax('titlemessage', false , false)",1000); 
			}
			catch(e) 
			{
			}
			var tString = new String(""); 
			try
			{ 
			    
					//if home 
					// if favorites
					// if somenew section 
					// determines what to display or load when the user signin their favorites for example 			
					//setTimeout('sectionCheck()',1000);
			}	
			catch(e)
			{
			   top.debug("An error occured when reading ...in /plugins/signin/signin.js: tString ---> $$$ " + e.message );
			}
		window.location.href='./';			
		}
		else {
		       // top.window.frames.userlogin = false;
		      //top.window.userlogin = false;
		      //alert("Login Failed.\nPlease Try Again."); modified 2/20/2007 per Dave J.
			  alert("Your Username or Password was not recognized. \n" +
                    "Be sure you are using your Windows log-in ID and password.\n" +
                    "If you continue to have problems accessing the Gateway, please call your helpdesk at 202-586-5153  for (HQ - OCRWM ITS Help Desk) and 702-794-1335 for (LV - DOE Technical Support).");
                  
		}
	}
	else { 
	        //top.window.frames.userlogin = false;
	       //top.window.userlogin = false;
		   //alert("Login Failed.\nPlease Try Again."); modified 2/20/2007 per Dave J.
		   alert("Your Username or Password was not recognized. \n" +
                    "Be sure you are using your Windows log-in ID and password.\n" +
                    "If you continue to have problems accessing the Gateway, please call your helpdesk at 202-586-5153  for (HQ - OCRWM ITS Help Desk) and 702-794-1335 for (LV - DOE Technical Support).");
                  
	}
	$('activity').hide();
}

function signIn(redirecturl,target) {

//top.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& Call to signIn &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ");
//top.debug("value of redirecturl and target " + redirecturl + " " + target );
  
	dxmlhttp.open("POST","doLogin",false);
	dxmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	dxmlhttp.send("command=login&auto=0&username="+document.signForm.userName.value+"&pass="+escape(document.signForm.password.value)+"&domain="+document.signForm.domain.value+"&requesttype=simple");
	if (dxmlhttp.responseText) 
	{
		if (trimIt(dxmlhttp.responseText)=="success") 
		{
	       //top.debug("response text equal to sucesss ..... ");
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
			
			try 
			{
				dxmlhttp.open("GET","_plugins/signin/checkLogin.jsp",false);
				dxmlhttp.send(null);
				userFullName = trimIt(dxmlhttp.responseText);
			    top.userName = document.signForm.userName.value;
	          	 changeUsername();				  
				 	setTimeout("top.callAjax('getUID', false , false)",1000); 
				 	setTimeout("top.callAjax('titlemessage', false , false)",1000); 
			}
			catch(e) 
			{
			}
			var tString = new String(""); 
			try
			{ 
			    
					//if home 
					// if favorites
					// if somenew section 
					// determines what to display or load when the user signin their favorites for example 			
					setTimeout('sectionCheck()',1000);
			}	
			catch(e)
			{
			   top.debug("An error occured when reading ...in /plugins/signin/signin.js: tString ---> $$$ " + e.message );
			}
			
			
		}
		else {
		       // top.window.frames.userlogin = false;
		      //top.window.userlogin = false;
		      //alert("Login Failed.\nPlease Try Again."); modified 2/20/2007 per Dave J.
			  alert("Your Username or Password was not recognized. \n" +
                    "Be sure you are using your Windows log-in ID and password.\n" +
                    "If you continue to have problems accessing the Gateway, please call your helpdesk at 202-586-5153  for (HQ - OCRWM ITS Help Desk) and 702-794-1335 for (LV - DOE Technical Support).");
                  
		}
	}
	else { 
	        //top.window.frames.userlogin = false;
	       //top.window.userlogin = false;
		   //alert("Login Failed.\nPlease Try Again."); modified 2/20/2007 per Dave J.
		   alert("Your Username or Password was not recognized. \n" +
                    "Be sure you are using your Windows log-in ID and password.\n" +
                    "If you continue to have problems accessing the Gateway, please call your helpdesk at 202-586-5153  for (HQ - OCRWM ITS Help Desk) and 702-794-1335 for (LV - DOE Technical Support).");
                  
	}
	$('activity').hide();
}

function sectionCheck(){
		
		 tString = window.frames.mainiframe.sub_function();	//this function will be contained
					                                                       						//in each html page within the iframe 
					                                                        					//so that we can determine the current page within the iframe 
					                                                        					//or mainiframe 
			    //top.debug("What is the value of tString ... --->* " + tString);			 
				//top.debug("sectionCheck() called with tString="+tString);
				var section = null;
				while(section==null){
				section = tString.lastIndexOf( "/" );
			}	
  			     	
			//if(section != -1 ) 
			if(section>0 ) 
			{
			     if (browsertype=='Microsoft Internet Explorer') setDefaultTabSignout();
			     try{
  			     fname = tString.substring(tString.lastIndexOf( "/" ) + 1,tString.length);
  			     //top.debug("What is the value of fname during signin " + fname );
  			     }catch(e){
  			     }
  	
  			     if(fname == 'home.html')
  			     {
  			     
  			           //alert("********************** calling home.html signoutdefault function *************************");
  			           if (browsertype=='Microsoft Internet Explorer') setDefaultTabSignout();
	  	         }
  			     else if(fname == 'favorites.html')
  			     {
  			        //top.debug("Inside favorites.html");
  			        //window.frames.mainiframe.initTabsMod();
  			        //top.debug("..... HERE .... ");
  			        window.frames.mainiframe.initRtMenu();
				    //top.debug("End of else if clause ...... ");	  
  			     }
  			     else if(fname == 'workbook2.html')
  			     {
  			        top.window.frames.mainiframe.tabsNew();
        	     }
  			    
			}
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
			
		}
	}
		
    window.location.href='./';
}

function popSignIn(redirecturl,target) {
	if (browsertype=='Microsoft Internet Explorer'){
		var signwin = document.getElementById('signin');	
		var signinz =  document.getElementById('domain');  
	    signinz.style.display = 'block';
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
		if(top.window.using_adjust_iframes)
		{
		
		  signwin.style.left=((top.document.body.clientWidth/2) - 35) +"px";
		  signwin.style.top=(750/2) - (signwin.scrollHeight)+"px";
		  /*var left = (600 - signwin.scrollWidth)/2;
		  var tops  = (600/2) - (signwin.scrollHeight);
		  //var winh  = top.window.scrollHeight;
		  top.window.debug("left and top " + left + " "  + tops);
		  */
		}
		else
		{
		  var left = signwin.style.left=(top.document.body.clientWidth - signwin.scrollWidth)/2+"px";
		  var tops = signwin.style.top=(top.document.body.clientHeight/2) - (signwin.scrollHeight)+"px";
		  // top.window.debug("left and top " + left + " "  + tops);
		}	
		signwin.style.paddingLeft='10px';
		signwin.style.paddingRight='10px';
		signwin.style.paddingTop='10px';
		signwin.style.zIndex='4';
		document.signForm.userName.select();
		document.signForm.userName.focus();
	}else{ //for mozilla browser
		var signwin = document.getElementById('signin');	
		var signinz =  document.getElementById('domain');  
	    	//signinz.style.display = 'block';
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
		if(top.window.using_adjust_iframes)
		{		
		  signwin.style.left=((top.document.body.clientWidth/2) - 35) +"px";
		  signwin.style.top=(750/2) - (signwin.scrollHeight)+"px";
		}
		else
		{
		  var left = signwin.style.left=(top.document.body.clientWidth - signwin.scrollWidth)/2+"px";
		  var tops = signwin.style.top=(top.document.body.clientHeight/2) - (signwin.scrollHeight)+"px";
		}	
		signwin.style.paddingLeft='10px';
		signwin.style.paddingRight='10px';
		signwin.style.paddingTop='10px';
		signwin.style.zIndex='4';
		document.signForm.userName.select();
		document.signForm.userName.focus();
	}
}

function popSignInNew(redirecturl,target) {
			top.debug("popSignInNew() called!");
			if (browsertype=='Microsoft Internet Explorer'){
						var signwin = document.getElementById('signin');	
						var signinz =  document.getElementById('domain');  
					    signinz.style.display = 'block';
					    
						for (i=0; i<top.document.body.childNodes.length; i++) {
							if ((top.document.body.childNodes[i].id!="signin") && (top.document.body.childNodes[i].nodeType==1)) {
								top.document.body.childNodes[i].style.filter='alpha(opacity=30)';
								top.document.body.childNodes[i].style.opacity='.3';
								top.document.body.childNodes[i].style.MozOpacity='.3';
								top.document.body.childNodes[i].style.KhtmlOpacity='.3';
							}
						}
						
						signwin.style.visibility='visible';
						signwin.style.border='2px outset #FFFFFF';
						signwin.style.position='absolute';
						signwin.style.display='block';	
						if(top.window.using_adjust_iframes)
						{
						
						  signwin.style.left=((top.document.body.clientWidth/2) - 35) +"px";
						  signwin.style.top=(750/2) - (signwin.scrollHeight)+"px";						  
						  signwin.style.top=(top.document.body.scrollHeight/2) - (signwin.scrollHeight)+"px";
						}
						else
						{
						  var left = signwin.style.left=(top.document.body.clientWidth - signwin.scrollWidth)/2+"px";
						  //var tops = signwin.style.top=(top.document.body.clientHeight/2) - (signwin.scrollHeight)+"px";
						 var tops = signwin.style.top=(document.documentElement.clientHeight/2) - (signwin.scrollHeight)+"px";
						}	
						signwin.style.paddingLeft='10px';
						signwin.style.paddingRight='10px';
						signwin.style.paddingTop='10px';
						signwin.style.zIndex='4';
						document.signForm.userName.select();
						document.signForm.userName.focus();
			}else{ //for mozilla browser
				var signwin = document.getElementById('signin');	
				var signinz =  document.getElementById('domain');  
			    	//signinz.style.display = 'block';
				for (i=0; i<top.document.body.childNodes.length; i++) {
						if ((top.document.body.childNodes[i].id!="signin") && (top.document.body.childNodes[i].nodeType==1)) {
							top.document.body.childNodes[i].style.filter='alpha(opacity=30)';
							top.document.body.childNodes[i].style.opacity='.3';
							top.document.body.childNodes[i].style.MozOpacity='.3';
							top.document.body.childNodes[i].style.KhtmlOpacity='.3';
						}
				}
				signwin.style.visibility='visible';
				signwin.style.border='2px outset #FFFFFF';
				signwin.style.position='absolute';
				signwin.style.display='block';	
					if(top.window.using_adjust_iframes)
					{		
					  signwin.style.left=((top.document.body.clientWidth/2) - 35) +"px";
					  signwin.style.top=(750/2) - (signwin.scrollHeight)+"px";
					}
					else
					{
					  var left = signwin.style.left=(top.document.body.clientWidth - signwin.scrollWidth)/2+"px";
					  //var tops = signwin.style.top=(top.document.body.clientHeight/2) - (signwin.scrollHeight)+"px";
					  var tops = signwin.style.top=(window.innerHeight/2) - (signwin.scrollHeight)+"px";
					}	
				signwin.style.paddingLeft='10px';
				signwin.style.paddingRight='10px';
				signwin.style.paddingTop='10px';
				signwin.style.zIndex='4';
				document.signForm.userName.select();
				document.signForm.userName.focus();
			}
}

function cancelSignInNew() {

  
	document.getElementById('signin').style.display='none';
	
	for (i=0; i<document.body.childNodes.length; i++) {
		if ((top.document.body.childNodes[i].id!="signin") && (top.document.body.childNodes[i].nodeType==1)) {
			document.body.childNodes[i].style.filter='alpha(opacity=100)';
			document.body.childNodes[i].style.opacity='1';
			document.body.childNodes[i].style.MozOpacity='1';
			document.body.childNodes[i].style.KhtmlOpacity='1';
			
		}
	}
	window.location.href='./';	
}

function checkSignInNew(redirecturl,target) 
{
top.debug("checkSignInNew() called!");
	dxmlhttp.open("GET","_plugins/signin/checkLogin.jsp",false);
	dxmlhttp.send(null);
	userFullName = trimIt(dxmlhttp.responseText);
		//alert(userFullName);
		
		/* 
		try {
			changeUsername();
		}
		catch(e) {}
		 */
	 
	if ((userFullName=="Guest") || (userFullName==null)) {
			if (redirecturl!='undefined') {
				popSignInNew(redirecturl,target);
			}else {
				popSignInNew();
			}
	}else{
	 Alert("You are already logged in as "+userFullName);
		 /* 
				if (redirecturl!='undefined') {
					window.open(redirecturl,target);
				}
		 */
	}	
}

function checkSignIn(redirecturl,target) 
{
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
	}else{
		if (redirecturl!='undefined') {
			window.open(redirecturl,target);
		}
	}	
}
