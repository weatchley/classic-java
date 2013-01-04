<%@ page language="java" import="java.util.*"%>
<%@ include file="include/headerSetup.inc" %>
<%@ include file = "include/ipFilter.jspf"%>
<%-- <rdc:sessionTest />  --%> 
<%@ include file = "include/checkSignIn.jsp"%>
<%
	String fontsize = "small";
	String theme = "default";
	String target = "_self";
	Cookie cookies [] = request.getCookies ();
	Cookie myCFontSize = null;
	Cookie myCTheme = null;
	Cookie myCTarget = null;

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
					 if(cookies [i].getName().equals ("target")){
					myCTarget = cookies[i];
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
				
				if (myCTarget != null) {
				target = myCTarget.getValue();
				}else{
				target = "_self";
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
	}else{
		cssName = "index-de";
	}
	
	String nId = "";
	String newsUrl = "";
	
	if(request.getParameter("nId")!=null){
	nId = request.getParameter("nId");
	newsUrl = "https://www.tracerlock.com/news-feed.cgi?id=" + nId;
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
        <meta name="description" content="OCRWM Gateway">
        <meta http-equiv="refresh" content="1800">
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
        <script src="_plugins/homecontent/homecontent.js" type="text/javascript"></script>
        
        <!-- login related -->
        <script src="_plugins/signin/signin.js" type="text/javascript"></script> 
           
        <!-- debug related -->
		<!-- script type="text/javascript" src="_plugins/shuzi/js/cookie_helper.js"></script-->
		<!-- script type="text/javascript" src="_plugins/shuzi/js/effects.js"></script-->
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
                
        	var userFullName = '<%=userFullName%>';
        	
        	var ie5=document.all && !window.opera
			var ns6=document.getElementById
			document.onclick=onclickaction
			function onclickaction(evt){
				try{
					//if((event.srcElement.href)&&(event.srcElement.innerHTML)){
						//top.debug("event.srcElement.innerText: "+event.srcElement.innerText);
						//top.debug("event.srcElement.href: "+event.srcElement.href);
						track(event.srcElement);
					//}else{
					//}
				}catch(e){
					try{
						//if((evt.target.href)&&(evt.target.textContent)){ 
							//top.debug("evt.target.textContent: "+evt.target.textContent);
							//top.debug("evt.target.href: "+evt.target.href);
							track(evt.target);
						//}
					}catch(e){}	
				}
			}

			
        
        var userFullName = '<%=userFullName%>';
		var userName =  '<%=uname%>';
                
        function exitAlert(url){        
        myUrl = window.location.protocol + "//" + window.location.host + "" + window.location.pathname;
       		debug(myUrl+"<br>"+url+"<br>"+"<%=target%>");
        if((url.indexOf(myUrl)>-1)&&(url.indexOf("dynaPages")<0)){
        	window.open(url,'_self');
        }else if(url.indexOf("https://blogs.ocrwm.doe.gov")>-1){
        	window.open(url,'<%=target%>');
        }else{        
	        if(url!="http:///"){
	        	if((url.indexOf("ocrwm.doe.gov")>-1)||(url.indexOf("ymp.gov")>-1)||(url.indexOf("localhost")>-1)){  
	        		window.open(url,'<%=target%>');	        		
	        	}else{
	        		var answer = confirm("You are leaving the Gateway. Are you sure to proceed?");
	        		if(answer){
	        			window.open(url,'<%=target%>');
	        		}
		        }
	         }else{
	         	alert("The link is either not activated or non-existent. Please contact Gateway administrators.");
	         }
	   }
	         return false;
        }
        
        function userLoginNew(){
        	top.debug("userLoginNew() called!");
        	checkSignInNew();
        }
        
        function resizeFont(){
        var fontsize="<%=fontsize%>";
        var fontObjs = document.getElementsByTagName("font");
        var fontsizepx = "";
                        
			if(fontsize=="x-small"){
			fontsizepx="8px";
			}else if(fontsize=="small"){
			 fontsizepx="10px";
			}else if(fontsize=="large"){
			 fontsizepx="14px";
			}else if(fontsize=="x-large"){
			 fontsizepx="16px";
			}else if(fontsize=="default"){
			 fontsizepx="12px";
			}
						
	        for(var i = 0; i < fontObjs.length; i++){
	        	fontObjs[i].style.fontSize = fontsizepx;
	        }
			document.body.style.fontSize =  fontsizepx;
        }
        
        function submitfm(sUrl){
        	//ee();
			document.sform.target='<%=target%>';			
        	document.sform.action=sUrl;
        	document.sform.submit();
        }
        
        /*
        function ee() {
		if(document.signForm.userName.value!=""){
			top.userName = document.signForm.userName.value;
		}	
		document.sform.sUserName.value = top.userName;
		}
		*/
		
		function fformsend(){				
		top.debug("fformsend() called!");
		var comments = document.fform.comments.value;
		var name = document.fform.name.value;
		var email = document.fform.email.value;
		var phone = document.fform.phone.value;
		var ipaddress ="<%=request.getRemoteAddr()%>";
		//request.getRemoteHost() 
		
		top.debug( "ipaddress: "+ ipaddress);
		
		var parameters = "comments="+comments+"&name="+name+"&email="+email+"&phone="+phone+"&ipaddress="+ipaddress;
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
				
		function loadnews(nId, lId, obj){
	 		document.nform.nId.value = nId;
	  		document.nform.submit();
		}
		
		function feedswitch(){
			tObj = document.getElementById("ocrwmmessage");
			tObj2 = document.nform.ocrwmmessageflag.value;
			iObj = document.getElementById("switch0");
			if(tObj2 == "true") {
				tObj.style.display = "none";
				document.nform.ocrwmmessageflag.value = "false";
				iObj.style.backgroundImage="url(graphics/minus-l.gif)";	
			}else{
				tObj.style.display = "block";
				document.nform.ocrwmmessageflag.value = "true"
				iObj.style.backgroundImage="url(graphics/plus-l.gif)";	
			}
		}
		
		function updateLinks(){
//top.debug("updateLinks() called!!!!!!!!!!!!");
			var url;
			var links=document.getElementsByTagName('a');
			var myUrl = window.location.protocol + "//" + window.location.host +  window.location.pathname + "/#";
			for (var i = 0; i < links.length; i++){
				//if ((links[i].href.indexOf('mailto:')==-1)&&(links[i].name!="signinlink")&&(links[i].name!="signoutlink")&&(links[i].name.indexOf('editcontent')==-1)&&(links[i].name.indexOf('news')==-1)&&(links[i].name.indexOf('rssUpdate')==-1)){
				if ((links[i].href.indexOf('mailto:')==-1)&&(links[i].name!="signinlink")&&(links[i].name!="signoutlink")&&(links[i].name.indexOf('editcontent')==-1)&&(links[i].name.indexOf('rssUpdate')==-1)){	
					links[i].onclick=function(){
					url = this.href;
						//top.debug(this.alt);
					exitAlert(url);
					return false;
					}
					links[i].title=links[i].innerHTML;
					links[i].alt=links[i].innerHTML;
				}
			}
		
		}
						
		function init(){
			loadPortlets();
			initRSSReadersNew();
			resizeFont();
			setTimeout('updateLinks()', 1000);
		}
		
		window.onload = init;
				
        </script>
        <style>
        	#ocrwmmesaageswitch
			{
			margin-left: 0px;
			padding-left: 0px;
			list-style: none;
			padding: 0px;
			margin: 0px;
			}
			
			#ocrwmmesaageswitch li
			{
			padding-left: 14px;
			padding-top: 7px;
			background-image: url('graphics/plus-l.gif');
			background-repeat: no-repeat;
			background-position: 0 .5em;
			cursor: pointer;
			cursor: hand;
			}
        </style>
</head>
<body>
<script>
//showDebug(); //comment this line to disable debug screen...
top.debug(userFullName);
top.debug("theme: <%=theme%>");
</script>

<table width="100%" cellpadding="0" cellspacing="0">
<!-- header begins -->
	<tr>
		<td>
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td width="205" class=headerlogo>
						<a href="./">
							<img alt="Refresh the Portal" id="header-logo" src="graphics/header-logo-transparent.gif" width="205" height="44" border="0" />
						</a>
						<!-- a href=#>
							<img alt="Home" id="header-logo" src="graphics/header-logo-g.gif" width="205" height="44" border="0" />
						</a-->
					</td>
					<td class=headerbg align=center>			
						<script>
							document.write(dayarray[day]+", "+montharray[month]+" "+daym+", "+year)
						</script>
					</td>
 
					<td class=headerbg align=right>
					<%
					//System.out.println(userFullName);
					if(userFullName!="Guest"){
					%>
										<%= session.getAttribute("user.fullname") %> | <a href="#" onclick="signOutNew();return false;" name="signinlink"  class="headerlinktext">Sign out</a><br>
					<% } else { %>
					<a href="#" onclick="userLoginNew();return false;" name="signoutlink" class="headerlinktext">Sign in</a><br>
					<% } %>


<%-- 
					<td class=headerbg align=right>
					<rdc:isAuthenticated ><%= session.getAttribute("user.fullname") %> | <a href=# onclick="signOutNew();return false;" name="signinlink"  class="headerlinktext">Sign out</a><br></rdc:isAuthenticated>
					<rdc:isAuthenticated doOpposite="true"><a href="#" onclick="userLoginNew();return false;" name="signoutlink" class="headerlinktext">Sign in</a><br></rdc:isAuthenticated>
 --%>
			
						<a href="preferences.jsp?theme=<%=theme%>&fontsize=<%=fontsize%>&target=<%=target%>"  class="headerlinktext">Preferences</a>
					</td>
					<td width="21" valign=top class=headercorners>
						<!-- img src="graphics/header-corners-g.gif" width="21" height="44" /-->
					</td>
				</tr>			
			</table>
		</td>
	</tr>
<!-- header ends -->
<!-- portlet panel begins -->
	<tr>
		<td>
			<table cellpadding="0" cellspacing="0" width="99%">
				<tr>
					<td width="30%" class=ppanel>
						<table cellpadding="5" cellspacing="0" width="100%">
							<tr>
								<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="">
										<tr>
											<td width="20" class=tl><!--img src="graphics/tl.gif" width="20" height="20"--></td>
											<td class=pbgt>
											Search
											</td>
											<td width="20" class=tr><!-- img src="graphics/tr.gif" width="20" height="20"--></td>
										</tr>
										<tr>
											<!--td style="background-image: url('graphics/lborder.gif');background-repeat: repeat-y;"-->
											<td style="" class=pbgl>
											<!-- Blank left section --></td>	
											<!--form name=aform action='/search/' onsubmit='ee();'-->			
											<form name=sform action=''>												
												<td>
													<p>
													<input type=hidden name=sUserName value='Guest'>
													<input type=hidden name=sUserFullName value='Guest'>
													<input type=hidden name=adFlag value=0>
													<input type='hidden' name='mResultsDisplay' value='yes'>
													<input type='hidden' name='qDatabase' value=''>
													<input type='hidden' name='actiontype' value='display'>
													<input type='hidden' name='searchname' value=''>
													<input type='hidden' name='agentresults' value='no'>
													<input type='hidden' name='crumbtrail' value=''>
													<input type='hidden' name='query_lang' value='en'>
													<input type='hidden' name='weight' value='50'>
													<input type='hidden' name='ADVTOGGLE' value='o'>
													<input type='hidden' name='from_month' value='1'>
													<input type='hidden' name='from_day' value='1'>
													<input type='hidden' name='from_year' value='1996'>
													<input type='hidden' name='to_month' value='12'>
													<input type='hidden' name='to_day' value='31'>
													<input type='hidden' name='to_year' value='2009'>
													<input type='hidden' name='KATTOGGLE' value='o'>
													<input type='hidden' name='cAll' value='c'>
													<input type='hidden' name='mSearchType' value='simple'>
															<fieldset>
																<legend>Site Search</legend>
																<input type='text' size='15' name='searchtext' id='searchtext'/>&nbsp;
																<input type='button' value='Search' onClick='submitfm("/search/");' style="font: bold 84%'trebuchet ms',helvetica,sans-serif;"  class=btn />
																<!-- input type='submit' class='submit' value='Search' onClick='document.aform.adFlag.value=0' style="font: bold 84%'trebuchet ms',helvetica,sans-serif;" /-->
																<!--input type=submit value=Search class=btn style="font: bold 84%'trebuchet ms',helvetica,sans-serif;" /-->
															</fieldset>
													<!-- /form>
													<form name=gform action="http://www.google.com/search" method='POST'-->
															<br>
															<fieldset>
																<legend>Google Internet Search</legend>
																<input type="text"   name="q" size="15" maxlength="255" value="" />&nbsp;
																<!-- input type=submit value=Search  class=btn style="font: bold 84%'trebuchet ms',helvetica,sans-serif;"  /-->
																<input type=button onclick="submitfm('http://www.google.com/search')" value=Search  class=btn style="font: bold 84%'trebuchet ms',helvetica,sans-serif;"  />
																<!-- <br><input type="checkbox" class="checkbox"  name="sitesearch"  value="ocrwm.doe.gov" checked /> only search ocrwm site  -->
															</fieldset>
													</td>
												</form>
											<!--td style="background-image: url('graphics/rborder.gif');background-repeat: repeat-y;"-->
											<td style="" class=pbgr>
											<!--Blank right section -->
											</td>
										</tr>
										<tr>
											<td width="20" class=bl><!--img src="graphics/bl.gif" width="20" height="20"--></td>
											<!--td style="background-image: url('graphics/bborder.gif');background-repeat: repeat-x;"-->
											<td style="" class=pbgb>
											<!--Blank bottom section -->
											</td>
											<td width="20" class=br><!--img src="graphics/br.gif" width="20" height="20"--></td>		
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="">
										<tr>
											<td width="20" class=tl></td>
											<td class=pbgt>
											Quick Links
											</td>
											<td width="20" class=tr></td>
										</tr>
										<tr>
											<td class=pbgl><!-- Blank left section --></td>
											<td>
													<div id="portlet0" name="portlet0"></div>
													<div id="portlet0Edit" name="portlet0Edit" style="text-align:right;display:none;"><a href="#" onclick="portletEdit(0)" name="editcontent0">(edit content)</a></div>
											</td>
											<td class=pbgr>
											<!--Blank right section -->
											</td>
										</tr>
										<tr>
											<td width="20" class=bl></td>
											<td class=pbgb>
											<!--Blank bottom section -->
											</td>
											<td width="20" class=br></td>
										</tr>
									</table>
								</td>
							</tr>	
							<tr>
								<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="">
										<tr>
											<td width="20" class=tl></td>
											<td class=pbgt>
											Administrative Tools
											</td>
											<td width="20" class=tr></td>
										</tr>
										<tr>
											<td class=pbgl><!-- Blank left section --></td>
											<td>
											<div id="portlet1" name="portlet1"></div>
											<div id="portlet1Edit" name="portlet1Edit" style="text-align:right;display:none;"><a href="#" onclick="portletEdit(1)" name="editcontent1">(edit content)</a></div>
											</td>
											<td class=pbgr>
											<!--Blank right section -->
											</td>
										</tr>
										<tr>
											<td width="20" class=bl></td>
											<td class=pbgb>
											<!--Blank bottom section -->
											</td>
											<td width="20" class=br></td>
										</tr>
									</table>
								
								</td>
							</tr>
							<tr>
								<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="">
										<tr>
											<td width="20" class=tl></td>
											<td class=pbgt>
											Environment, Safety and Health
											</td>
											<td width="20" class=tr></td>
										</tr>
										<tr>
											<td class=pbgl><!-- Blank left section --></td>
											<td>
											<div id="portlet2" name="portlet2"></div>
											<div id="portlet2Edit" name="portlet2Edit" style="text-align:right;display:none;"><a href="#" onclick="portletEdit(2)" name="editcontent2">(edit content)</a></div>
											</td>
											<td class=pbgr>
											<!--Blank right section -->
											</td>
										</tr>
										<tr>
											<td width="20" class=bl></td>
											<td class=pbgb>
											<!--Blank bottom section -->
											</td>
											<td width="20" class=br></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="">
										<tr>
											<td width="20" class=tl></td>
											<td class=pbgt>
											OCRWM Security
											</td>
											<td width="20" class=tr></td>
										</tr>
										<tr>
											<td class=pbgl><!-- Blank left section --></td>
											<td>
											<div id="portlet6" name="portlet6"></div>
											<div id="portlet6Edit" name="portlet6Edit" style="text-align:right;display:none;"><a href="#" onclick="portletEdit(6)" name="editcontent6">(edit content)</a></div>
											</td>
											<td class=pbgr>
											<!--Blank right section -->
											</td>
										</tr>
										<tr>
											<td width="20" class=bl></td>
											<td class=pbgb>
											<!--Blank bottom section -->
											</td>
											<td width="20" class=br></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
								
								<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="">
											<tr>
												<td width="20" class=tl></td>
												<td class=pbgt>
												Contact Us
												</td>
												<td width="20" class=tr></td>
											</tr>
											<tr>
												<td class=pbgl><!-- Blank left section --></td>
												<form action=# name=fform>
												<td align=center>
												<a href="mailto:OCRWM_Gateway_Administrators@ymp.gov">Send email</a> to the OCRWM Gateway Administrator
															<table>
																<tr>
																	<td colspan=2  align=left>
																	Comment:
																	</td>
																</tr>
																<tr>
																	<td colspan=2>
																		<textarea align=center name=comments rows=2 cols=20></textarea><br>
																	</td>
																</tr>
																<tr>
																	<td>Name</td><td><input type=text name=name size=15></td>
																</tr>
																<tr>
																	<td>eMail</td><td><input type=text name=email size=15></td>
																</tr>
																<tr>
																	<td>Phone</td><td><input type=text name=phone size=15 maxlength=15></td>
																</tr>
																<tr>
																	<td colspan=2 align=right><input type=button value=Submit name=submit_button onClick="fformsend();" class=btn  style="font: bold 84%'trebuchet ms',helvetica,sans-serif;"></td>
																</tr>
															</table>
												</td>
												</form>
												<td class=pbgr>
												<!--Blank right section -->
												</td>
											</tr>
											<tr>
												<td width="20" class=bl></td>
												<td class=pbgb>
												<!--Blank bottom section -->
												</td>
												<td width="20" class=br></td>
											</tr>
										</table>
								
								</td>
							</tr>
						</table>
					</td>
					<td width="40%" class=ppanel>
						<table cellpadding="5" cellspacing="0" width="100%">
							<tr>
								<td>
								
								<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="">
											<tr>
												<td width="20" class=tl></td>
												<td class=pbgt>
												From Director
												</td>
												<td width="20" class=tr></td>
											</tr>
											<tr>
												<td class=pbgl><!-- Blank left section --></td>
												<td>
												<br>
												    <fieldset>
															<legend><a href="https://blogs.ocrwm.doe.gov/director/en/">Weekly Message</a></legend>
															<!-- table border=0 width=100%>
																<tr>
																	<td align=left valign=top-->
																		<span id='directorsmessage' style="">
																		From Director RSS Feed Loading...
																		</span>	
																		<div id="rss1Update" name="rss1Update" style="text-align:right;display:none;"><a href="#" onclick="rssUpdate('1');return false;" name="rssUpdate1">(update content)</a></div>
																	<!-- /td -->
																	<!-- td valign="top">
																		&nbsp;
																	</td-->
																<!-- /tr>
															</table-->
													</fieldset>
												</td>
												<td class=pbgr>
												<!--Blank right section -->
												</td>
											</tr>
											<tr>
												<td width="20" class=bl></td>
												<td class=pbgb>
												<!--Blank bottom section -->
												</td>
												<td width="20" class=br></td>
											</tr>
										</table>
								
								</td>
							</tr>
							<tr>
								<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="">
											<tr>
												<td width="20" class=tl></td>
												<td class=pbgt>
												News Feeds
												</td>
												<td width="20" class=tr></td>
											</tr>
											<form action="./" name=nform id=nform method=post>
											<tr>
												<td class=pbgl><!-- Blank left section --></td>
												<td>
													<br>
													<fieldset>
															<legend><a href="https://blogs.ocrwm.doe.gov/crest/">The Crest</a></legend>
															<span id='crestmessage' >CREST RSS Feed Loading..</span>	
															<!-- div align=right><a target=_blank href=https://blogs.ocrwm.doe.gov/crest/>(View All Entries)</a></div-->
															<div id="rss0Update" name="rss0Update" style="text-align:right;display:none;"><a href="#" onclick="rssUpdate('0');return false;" name="rssUpdate0">(update content)</a></div>
													</fieldset>													
													<br>
													<fieldset>
															<legend><a href="http://ocrwm.doe.gov/ocrwmnews/index.shtml">OCRWM in News</a></legend>
																	<!--div><a href=# onclick="loadnews(54618);return false;" name=news0>Nevada Papers</a></div>
																	<div><a href=# onclick="loadnews(54619);return false;" name=news1>National Newspapers</a></div>
																	<div><a href=# onclick="loadnews(54620);return false;" name=news2>Broadcast/Cable</a></div>
																	<div><a href=# onclick="loadnews(54621);return false;" name=news3>Trade Organizations / Affected Organizations</a></div>
																	<div><a href=# onclick="loadnews(54777);return false;" name=news4>Transportation</a></div-->
																	
																	<div><a href="http://ocrwm.doe.gov/ocrwmnews/Nevada_News_Media_Coverage_of_OCRWM.shtml#n" name=news0>Nevada Papers</a></div>
																	<div><a href="http://ocrwm.doe.gov/ocrwmnews/National_Newspapers_Feed.shtml" name=news1>National Newspapers</a></div>
																	<div><a href="http://ocrwm.doe.gov/ocrwmnews/Broadcast_Cable_News_Feed.shtml" name=news2>Broadcast/Cable</a></div>
																	<div><a href="http://ocrwm.doe.gov/ocrwmnews/Trade_Organizations_Affected_Organizations_News_Feed.shtml" name=news3>Trade Organizations / Affected Organizations</a></div>
																	<div><a href="http://ocrwm.doe.gov/ocrwmnews/Transportation_News_Feed.shtml" name=news4>Transportation</a></div>
<%
	if (!newsUrl.equals("")){
		out.println("<ul id='ocrwmmesaageswitch'  onclick='feedswitch();return false'><li id=switch0><a href=#>Toggle News Feed</a></li></ul>");
		out.println("<input name=ocrwmmessageflag id=ocrwmmessageflag type=hidden value=true>");
	}
%>																		
															
															<span id='ocrwmmessage' >
																<script id=newsscript src="<%=newsUrl%>"></script>
															</span>	
													</fieldset>
													<input type=hidden name=nId>													
													<br>
													<fieldset>
															<legend><a href="https://blogs.ocrwm.doe.gov/fyi/">F.Y.I.</a></legend>			
															<span id='fyimessage' >FYI RSS Feed Loading..</span>	
															<!-- div align=right><a target=_blank href=https://blogs.ocrwm.doe.gov/fyi/>(View All Entries)</a></div-->
															<div id="rss2Update" name="rss2Update" style="text-align:right;display:none;"><a href="#" onclick="rssUpdate('2');return false;" name="rssUpdate2">(update content)</a></div>
													</fieldset>
												</td>
												<td class=pbgr>
												<!--Blank right section -->
												</td>
											</tr>
											</form>
											<tr>
												<td width="20" class=bl></td>
												<td class=pbgb>
												<!--Blank bottom section -->
												</td>
												<td width="20" class=br></td>
											</tr>
										</table>
								</td>
							</tr>
							<tr>
								<td>
								</td>
							</tr>	
						</table>
				    </td>
					<td width="30%" class=ppanel>
						<table cellpadding="5" cellspacing="0" width="100%">
							<tr>
								<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="">
											<tr>
												<td width="20" class=tl></td>
												<td class=pbgt>
												Federal Links
												</td>
												<td width="20" class=tr></td>
											</tr>
											<tr>
												<td class=pbgl><!-- Blank left section --></td>
												<td>
												<div id="portlet3" name="portlet3"></div>
												<div id="portlet3Edit" name="portlet3Edit" style="text-align:right;display:none;"><a href="#" onclick="portletEdit(3)" name="editcontent3">(edit content)</a></div>
												</td>
												<td class=pbgr>
												<!--Blank right section -->
												</td>
											</tr>
											<tr>
												<td width="20" class=bl></td>
												<td class=pbgb>
												<!--Blank bottom section -->
												</td>
												<td width="20" class=br></td>
											</tr>
										</table>
								</td>
							</tr>
							<tr>
								<td>										
																	
								<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="">
											<tr>
												<td width="20" class=tl></td>
												<td class=pbgt>
												Organizations
												</td>
												<td width="20" class=tr></td>
											</tr>
											<tr>
												<td class=pbgl><!-- Blank left section --></td>
												<td>
												<div id="portlet4" name="portlet4"></div>
												<div id="portlet4Edit" name="portlet4Edit" style="text-align:right;display:none;"><a href="#" onclick="portletEdit(4)" name="editcontent4">(edit content)</a></div>
												
												</td>
												<td class=pbgr>
												<!--Blank right section -->
												</td>
											</tr>
											<tr>
												<td width="20" class=bl></td>
												<td class=pbgb>
												<!--Blank bottom section -->
												</td>
												<td width="20" class=br></td>
											</tr>
										</table>
										
								</td>
							</tr>	
							<tr>
								<td>
													
										
										
										<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="">
											<tr>
												<td width="20" class=tl></td>
												<td class=pbgt>
												Documents/Data
												</td>
												<td width="20" class=tr></td>
											</tr>
											<tr>
												<td class=pbgl><!-- Blank left section --></td>
												<td>
												<div id="portlet5" name="portlet5"></div>
												<div id="portlet5Edit" name="portlet5Edit" style="text-align:right;display:none;"><a href="#" onclick="portletEdit(5)" name="editcontent5">(edit content)</a></div>
												
												</td>
												<td class=pbgr>
												<!--Blank right section -->
												</td>
											</tr>
											<tr>
												<td width="20" class=bl></td>
												<td class=pbgb>
												<!--Blank bottom section -->
												</td>
												<td width="20" class=br></td>
											</tr>
										</table>		
										
								</td>
							</tr>
							<tr>
								<td>								
														
										
										<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="">
											<tr>
												<td width="20" class=tl></td>
												<td class=pbgt>
												Weather
												</td>
												<td width="20" class=tr></td>
											</tr>
											<tr>
												<td class=pbgl><!-- Blank left section --></td>
												<td>
												<a href="http://www.weather.com/weather/local/USNV0049?lswe=Las%20Vegas,%20NV&lwsa=WeatherLocalUndeclared&from=searchbox_typeahead">Las Vegas Weather</a><br>
												<a href="http://www.weather.com/weather/local/USDC0001?lswe=Washington,%20DC&from=searchbox_localwx">Washington, DC Weather</a>
												</td>
												<td class=pbgr>
												<!--Blank right section -->
												</td>
											</tr>
											<tr>
												<td width="20" class=bl></td>
												<td class=pbgb>
												<!--Blank bottom section -->
												</td>
												<td width="20" class=br></td>
											</tr>
										</table>
										
								</td>
							</tr>
						</table>
					</td>
				</tr>																
			</table>
		</td>
	</tr>
<!-- portlet panel ends -->
</table>
<div id='signin' style='display:none;'><form name='signForm'><center><b>Please Sign In</b><br><table border=0><tr><td align='left'>Username:</td><td align='left'><input type='text' name='userName' onkeypress='submitSignIn(e)'></td><tr><td align='left'>Password:</td><td align='left'><input type='password' name='password' onkeypress='submitSignIn(e)'></td><tr id='selectrow'><td align='left'>Domain:</td><td align='left'><select name='domain'><option value='ydservices'>YDServices</option><option value='rw.doe.gov'>RW.DOE.GOV</option><option value='ymservices'>YMServices</option></select></td></table><br><input type=hidden name=rurl id=rurl value='test url'><input type='button' id='signsubmit'  name='signsubmit' value='Sign In' onClick="$('activity').show();setTimeout('signInNew()',1000)" class=btn>&nbsp;<input class=btn type='button' value='Cancel' onClick='cancelSignInNew();'><br><div id='activity' style='display:none;'><img src='graphics/activity.gif'><br>sign in in progress...</div></center></form></div>
<%-- <rdc:envDisplay /> --%>
<rdc:notProductionWarning size="150%" width="180" />
</body>
</html>