<%@ page language="java" import="java.util.*"%>
<%@ include file="include/headerSetup.inc" %>
<%@ include file = "include/ipFilter.jspf"%>
<rdc:sessionTest />
<%@ include file = "include/checkSignIn.jsp"%>
<%
	String fontsize = "";
	String theme = "";
	Cookie cookies [] = request.getCookies ();
	Cookie myCFontSize = null;
	Cookie myCTheme = null;


	if (cookies != null)
			{
				for (int i = 0; i < cookies.length; i++) 
				{
					if (cookies [i].getName().equals ("fontsize"))
					{
					myCFontSize = cookies[i];
					}
					 if(cookies [i].getName().equals ("theme")){
					myCTheme = cookies[i];
					}
				}
				
				if (myCFontSize != null) {
				fontsize = myCFontSize.getValue();
				}else{
				fontsize = "small";
				}
				
				if (myCTheme != null) {
				theme = myCTheme.getValue();
				}else{
				theme = "default";
				}			
			}

	String cssName = "index-g";
	if (theme.equals("508")){
		cssName = "index-5";
  	 }else if (theme.equals("508b")){
		cssName = "index-5b";
	}else if (theme.equals("light")){
		cssName = "index-l";
	}else if (theme.equals("dark")){
		cssName = "index-d";
	}else if (theme.equals("gray")){
		cssName = "index-g";
	}else if (theme.equals("default")){
		cssName = "index-de";
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
        <meta name="description" content="OCRWM Gateway">
        <title>OCRWM Gateway</title>
        
        <!-- main css -->
        	<link rel="stylesheet" type="text/css" href="css/<%=cssName%>.css"></link>
        <!--  utilities -->
        <script type="text/javascript" src="_plugins/shuzi/js/prototype.js"></script>
        <script src="_plugins/utils/scriptaculous.js" type="text/javascript"></script>
        <script language="JavaScript" type="text/javascript" src="_plugins/shuzi/js/ajaxhelper.js"></script>
        <script src="_plugins/browser/browser.js" type="text/javascript"></script>
        
        <!-- linkcapture -->
        <script language="javascript" src="/linkcapture/js/track.js" type="text/javascript"></script>
        
        <!-- RSS reader related -->
        <script src="_plugins/homemessage/homemessage.js" type="text/javascript"></script>
        
        <!-- login related -->
        <script src="_plugins/signin/signin.js" type="text/javascript"></script> 
           
        <!-- debug related -->
		<script type="text/javascript" src="_plugins/shuzi/js/window.js"></script>
		<script type="text/javascript" src="_plugins/shuzi/js/debug.js"></script>		
		
		<link href="_plugins/shuzi/skins/debug.css" rel="stylesheet" type="text/css" ></link>
         <link href="_plugins/shuzi/skins/default.css" rel="stylesheet" type="text/css" ></link>
		<link href="_plugins/shuzi/skins/alphacube.css" rel="stylesheet" type="text/css" ></link>
		<link href="_plugins/shuzi/skins/darkX.css" rel="stylesheet" type="text/css" ></link>
		<link href="_plugins/shuzi/skins/spread.css" rel="stylesheet" type="text/css" ></link>
		<link href="_plugins/shuzi/skins/theme1.css" rel="stylesheet" type="text/css" ></link>
		<link href="_plugins/shuzi/skins/alert.css" rel="stylesheet" type="text/css" ></link>
		
        <!-- embeded js for the development  -->
        <script>
        
        	var ie5=document.all && !window.opera
			var ns6=document.getElementById
			document.onclick=onclickaction
			function onclickaction(evt){
				try{
					if((event.srcElement.href)&&(event.srcElement.innerHTML)){
					//top.debug(event.srcElement.innerHTML);
						track(event.srcElement.innerHTML);
					}
				}catch(e){
					try{
						if((evt.target.href)&&(evt.target.innerHTML)){ 
						top.debug(evt.target.innerHTML);
						track(evt.target.innerHTML);
						}
					}catch(e){}	
				}
			}
        
        var userFullName = '<%=userFullName%>';
		var userName =  '<%=uname%>';
                
        function exitAlert(url){
	        if((url)&&(url.length>0)){
	        		var answer = confirm("Sure to leave the Gateway?");
	        		if(answer){
	        			window.location = url
	        		}
	         }else{
	         	alert("The link is either not activated or non-existent. Please contact Gateway administrators.");
	         }
        }
        
        function userLoginNew(){
        	top.debug("userLoginNew() called!");
        	checkSignInNew();
        }
        
        function resizeFont(){
        fontsize="<%=fontsize%>";
			if(fontsize=="x-small"){
			document.body.style.fontSize="8px";
			}else if(fontsize=="small"){
			document.body.style.fontSize="10px";
			}else if(fontsize=="large"){
			document.body.style.fontSize="14px";
			}else if(fontsize=="x-large"){
			document.body.style.fontSize="16px";
			}else if(fontsize=="default"){
			document.body.style.fontSize="12px";
			}
        }
        
        function submitfm(sUrl){
        	ee();
			document.sform.target='_self';			
        	document.sform.action=sUrl;
        	document.sform.submit();
        }
        
        function ee() {
		/*
		if(document.signForm.userName.value!=""){
			top.userName = document.signForm.userName.value;
		}	
		document.sform.sUserName.value = top.userName;
		*/
		}
		
		function fformsend(){				
		var comments = document.fform.comments.value;
		var name = document.fform.name.value;
		var email = document.fform.email.value;
		var phone = document.fform.phone.value;
		var parameters = "comments="+comments+"&name="+name+"&email="+email+"&phone="+phone;
		var url="./feedback";
		 var ajaxRequest = new Ajax.Request(url,
	              {
	                     method: 'post',
	                     parameters: parameters,
	                     onLoading: function() {
	                	 top.debug("calling AJAX...");
	            		 },
	                     onSuccess: function(transport){
	      				 var response = transport.responseText || "no response text";
	      				 top.debug("callAjax: Success! -> " + response);
	      				 alert('Your feedback has been submitted');	
	    				 },
	    				 onFailure: function(){ debug('callAjax: Something went wrong...') }
	              });
			
		}
        
        
        //current date related		
		var mydate=new Date()
		var year=mydate.getYear()
		if (year < 1000)
		year+=1900
		var day=mydate.getDay()
		var month=mydate.getMonth()
		var daym=mydate.getDate()
		if (daym<10)
		daym="0"+daym
		var dayarray=new Array("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")
		var montharray=new Array("January","February","March","April","May","June","July","August","September","October","November","December")
		
		//window.onload = init;
		
		function init(){
			initRSSReaders();
			checkHomeMessage();
			resizeFont();
		}
		
        </script>
        <style>
			
			#header {
			  margin-left: auto;
			  margin-right: auto;
			  width: 100%;  
			   height: 58px;
			   background-image: url('graphics/header-new-bg.gif'); 
			}
			#header .headerlogo {
			width: 208px; height: 58px;
 			background-image: url('graphics/header-new-logo.gif');
 			background-repeat: no-repeat; 
 			 float: left;
			}
			#header .headerDate {
			background-image: url('graphics/header-new-bg.gif');
		    background-repeat: repeat-x;
			 float:left;
			 height: 58px;
			 width: 35%;
			 	margin: 0;
				font-weight: bold;
				font-size: 1em;
				line-height: 58px;
				text-align: right;
				color: #FFFFFF;
			}
			
			#header #headerright{
			float:right; 
			}
			
			#headerright .headerLinks {
			background-image: url('graphics/header-new-bg.gif');
		    background-repeat: repeat-x;
			 float:left; 
			 height: 58px;
			 width: 20em;
			 	margin: 0;
				font-weight: bold;
				font-size: 1em;
				line-height: 58px;
				text-align: right;
				color:#ffffff;
			}
						
		   #headerright .headerLinks a {
			color:#FFFFFF;
			font-family: Verdana;
			text-decoration: None;
			font-weight:100;
		    }
	
		  #headerright .headerLinks a:hover {
			color:#666666;
			font-family: Verdana;
		   }
			
			#headerright .headercorners {
			width: 32px; height: 58px;
 			background-image: url('graphics/header-new-corners.gif');
 			background-repeat: no-repeat; 
			 float: left;
			}
			
			/*******************************************/		
			
			#portletcontainer {
			  margin-left: auto;
			  margin-right: auto;
			  width: 100%;
			  float: left;
			}
					
			#portletcontainer .portlet {
				margin: 0 auto;
				/* width: 17em; */
				width: 33%;
				background: url(graphics/sbbody-r.gif) no-repeat bottom right;
				font-size: 100%;
				float: left;
			}
			
			.portlethead {
			   background: url(graphics/sbhead-r.gif) no-repeat top right; 
				margin: 0;
				padding: 0;
				text-align: center;
			}
			.portlethead h2 {
				background: url(graphics/sbhead-l.gif) no-repeat top left; 
				margin: 0;
				padding: 22px 30px 5px;
				color: white;
				font-weight: bold;
				font-size: 1.2em;
				line-height: 1em;
				text-shadow: rgba(0,0,0,.4) 0px 2px 5px; /* Safari-only, but cool */
			}
			.portletbody {
				background: url(graphics/sbbody-l.gif) no-repeat bottom left; 
				margin: 0;
				padding: 5px 30px 31px;
			}
			
			    
			
	   </style>
</head>
<body>
<script>
//showDebug(); 
</script>
		
		<div id=header>
			<div class=headerlogo><a href="./"><img alt="Refresh the Portal" id="header-logo" src="graphics/header-logo-transparent.gif" width="205" height="44" border="0" /></a></div>
			<div class=headerDate><script>document.write(dayarray[day]+", "+montharray[month]+" "+daym+", "+year);</script></div>
			<div id=headerright>
				<div class=headerLinks>
						<rdc:isAuthenticated ><%= session.getAttribute("user.fullname") %> | <a href=# onclick="signOutNew();return false;">Sign out</a>&nbsp;|</rdc:isAuthenticated><rdc:isAuthenticated doOpposite="true">
						<a href="#" onclick="userLoginNew();return false;" id=linktext1 class=linktext1>Sign in</a>&nbsp;|</rdc:isAuthenticated><a href="preferences.jsp" id=linktext1 class=linktext1>Preferences</a>
				</div>
				<div class=headercorners></div>
			</div>
		</div>
		
		<div id=portletcontainer>
			<div class="portlet">
				<div class="portlethead"><h2>Test Headline</h2></div>
				<div class="portletbody">
					<p>Lorem ipsum dolor sit amet, consectetur adipisicing
								   elit, sed do eiusmod tempor incididunt ut labore et
								   dolore magna aliqua. Ut enim ad minim veniam, quis
								   nostrud exercitation ullamco laboris nisi ut aliquip
								   ex ea commodo consequat. Duis aute irure dolor in
								   reprehenderit in voluptate velit esse cillum dolore eu
								   fugiat nulla pariatur. Excepteur sint occaecat cupidatat
								   non proident, sunt in culpa qui officia deserunt mollit
								   anim id est laborum.</p>
				</div>
			</div>
			<div class="portlet">
				<div class="portlethead"><h2>Test Headline</h2></div>
				<div class="portletbody">
					<p>Lorem ipsum dolor sit amet, consectetur adipisicing
								   elit, sed do eiusmod tempor incididunt ut labore et
								   dolore magna aliqua. Ut enim ad minim veniam, quis
								   nostrud exercitation ullamco laboris nisi ut aliquip
								   ex ea commodo consequat. Duis aute irure dolor in
								   reprehenderit in voluptate velit esse cillum dolore eu
								   fugiat nulla pariatur. Excepteur sint occaecat cupidatat
								   non proident, sunt in culpa qui officia deserunt mollit
								   anim id est laborum.</p>
				</div>
			</div>
			<div class="portlet">
				<div class="portlethead"><h2>Test Headline</h2></div>
				<div class="portletbody">
					<p>Lorem ipsum dolor sit amet, consectetur adipisicing
								   elit, sed do eiusmod tempor incididunt ut labore et
								   dolore magna aliqua. Ut enim ad minim veniam, quis
								   nostrud exercitation ullamco laboris nisi ut aliquip
								   ex ea commodo consequat. Duis aute irure dolor in
								   reprehenderit in voluptate velit esse cillum dolore eu
								   fugiat nulla pariatur. Excepteur sint occaecat cupidatat
								   non proident, sunt in culpa qui officia deserunt mollit
								   anim id est laborum.</p>
				</div>
			</div>
				<div class="portlet">
					<div class="portlethead"><h2>Test Headline</h2></div>
					<div class="portletbody">
						<p>Lorem ipsum dolor sit amet, consectetur adipisicing
									   elit, sed do eiusmod tempor incididunt ut labore et
									   dolore magna aliqua. Ut enim ad minim veniam, quis
									   nostrud exercitation ullamco laboris nisi ut aliquip
									   ex ea commodo consequat. Duis aute irure dolor in
									   reprehenderit in voluptate velit esse cillum dolore eu
									   fugiat nulla pariatur. Excepteur sint occaecat cupidatat
									   non proident, sunt in culpa qui officia deserunt mollit
									   anim id est laborum.</p>
					</div>
				</div>
				<div class="portlet">
					<div class="portlethead"><h2>Test Headline</h2></div>
					<div class="portletbody">
						<p>Lorem ipsum dolor sit amet, consectetur adipisicing
									   elit, sed do eiusmod tempor incididunt ut labore et
									   dolore magna aliqua. Ut enim ad minim veniam, quis
									   nostrud exercitation ullamco laboris nisi ut aliquip
									   ex ea commodo consequat. Duis aute irure dolor in
									   reprehenderit in voluptate velit esse cillum dolore eu
									   fugiat nulla pariatur. Excepteur sint occaecat cupidatat
									   non proident, sunt in culpa qui officia deserunt mollit
									   anim id est laborum.</p>
					</div>
				</div>
				<div class="portlet">
					<div class="portlethead"><h2>Test Headline</h2></div>
					<div class="portletbody">
						<p>Lorem ipsum dolor sit amet, consectetur adipisicing
									   elit, sed do eiusmod tempor incididunt ut labore et
									   dolore magna aliqua. Ut enim ad minim veniam, quis
									   nostrud exercitation ullamco laboris nisi ut aliquip
									   ex ea commodo consequat. Duis aute irure dolor in
									   reprehenderit in voluptate velit esse cillum dolore eu
									   fugiat nulla pariatur. Excepteur sint occaecat cupidatat
									   non proident, sunt in culpa qui officia deserunt mollit
									   anim id est laborum.</p>
					</div>
			</div>
		</div>
		
<div id='signin' style='display:none;'><form name='signForm'><center><b>Please Sign In</b><br><table border=0><tr><td align='left'>Username:</td><td align='left'><input type='text' name='userName' onkeypress='submitSignIn(e)'></td><tr><td align='left'>Password:</td><td align='left'><input type='password' name='password' onkeypress='submitSignIn(e)'></td><tr id='selectrow'><td align='left'>Domain:</td><td align='left'><select name='domain'><option value='ydservices'>YDServices</option><option value='rw.doe.gov'>RW.DOE.GOV</option><option value='ymservices'>YMServices</option></select></td></table><br><input type=hidden name=rurl id=rurl value='test url'><input type='button' id='signsubmit'  name='signsubmit' value='Sign In' onClick="$('activity').show();setTimeout('signInNew()',1000)" class=btn>&nbsp;<input class=btn type='button' value='Cancel' onClick='cancelSignInNew();'><br><div id='activity' style='display:none;'><img src='graphics/activity.gif'><br>sign in in progress...</div></center></form></div>
</body>
</html>