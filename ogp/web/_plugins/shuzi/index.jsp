<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ include file = "../../_plugins/session/sessMgr.jsp"%>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.math.*" %>
<%@ page import="java.io.*" %>
<%@ include file = "../../include/ipFilter.jspf"%>
<%
//csi login check
	Object nameObj = (Object)session.getValue("user.name");
	if (nameObj == null) {
		session.setAttribute("user.name","Guest");
	}
	String userName = session.getAttribute("user.name").toString();
	if (userName=="") {
		session.setAttribute("user.name","Guest");
		userName="Guest";
	}
	String uid = "0";
	Object idObj = (Object)session.getValue("user.id");
	if (idObj == null) {
	}else{
		uid= session.getAttribute("user.id").toString();
	}
%>
<%
//shuzi login check begins
session.setAttribute("userName",userName);
userName = session.getAttribute("userName").toString();	
//shuzi login check ends
%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
request.setCharacterEncoding("utf-8");
response.setContentType("text/html; charset=utf-8");

  String  pAction = "";
  String  aTarget = "";
  String  aUrl = "";

  if (request.getParameter("pAction")!=null){
  pAction = request.getParameter("pAction");
  }
%>
<html xmlns="http://www.w3.org/1999/xhtml" version="-//W3C//DTD XHTML 1.1//EN" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>		
	<title>My Pages: Reloaded</title>		
	<style>
	* {
		margin: 0px;
		padding: 0px;		
	}
	body {
		background:#DDD;
		font-family: verdana, arial, sans-serif;
		font-size:14px;
		margin:0px;
	}
	#container {
	  float:left;
		margin-top:100px;
		font-family: Tahoma, Arial, sans-serif;
  	font-size: 10px;
	}
	</style>
	
	<!-- prototype.js window class related begins -->
	
		<script type="text/javascript" src="js/prototype.js"> </script>
		<script type="text/javascript" src="js/effects.js"> </script>
		<script type="text/javascript" src="js/window.js"> </script>
		<!--script type="text/javascript" src="js/window_ext.js"> </script-->
		<script type="text/javascript" src="js/cookie_helper.js"> </script>
		
		<!-- debug related begins -->
		<script type="text/javascript" src="js/debug.js"> </script>
			<!--script type="text/javascript" src="js/extended_debug.js"> </script-->			
		<link href="skins/debug.css" rel="stylesheet" type="text/css" >	 </link>	
		<!-- debug related ends -->
		
		<link href="skins/default.css" rel="stylesheet" type="text/css" >	 </link>
		<link href="skins/alphacube.css" rel="stylesheet" type="text/css" >	 </link>
		<link href="skins/darkX.css" rel="stylesheet" type="text/css" >	 </link>
		<link href="skins/spread.css" rel="stylesheet" type="text/css" >	 </link>
		<link href="skins/theme1.css" rel="stylesheet" type="text/css" >	 </link>
				
	<!-- prototype.js window class related ends -->
	
	<!-- toolbar related begins -->
	<script type="text/javascript" src="js/toolbar.js"></script>
	<script type="text/javascript" src="js/signin.js"></script>
	<link rel="stylesheet" type="text/css" href="css/toolbar.css">
	<link rel="stylesheet" type="text/css" href="css/index.css">
	<!-- toolbar related ends -->
	<script language=JavaScript>
	var userName = '<%=userName%>';	
	var personID = 0;
	window.onload = init;
	window.onunload = close;
	function init() { //configure window.onload events here
		initToolbar();
		//closeAll();
		if(userName == 'Guest') {
		setTimeout('checkSignIn()',1000);
			//checkSignIn();
		}else{
		getuid(userName);
		//shuziLogin('<%=userName%>',1);
		runFx(AjaxHelper.getPNames);
		checkCookie();
		}
	}
	function close(){
		//sessionRefresh('<%=userName%>');
	}	
	function getuid(userName){
		uid = <%=uid%>;
		debug(userName+'('+uid+'): logged in');	
	}
	function changeUsername() {
		exmlhttp.open("GET","../../_plugins/signin/checkLogin.jsp",false);
		exmlhttp.send(null);
		userFullName = trimIt(exmlhttp.responseText);
		if ((!userFullName) || (userFullName==null)) {
			userFullName="Guest";
		}
		if (userFullName=="Guest") {
			document.getElementById('showusername').innerHTML="Guest<br><a href='javascript:checkSignIn()'>Sign In</a>";
		}
		else {
			document.getElementById('showusername').innerHTML=userFullName+"<br><a href='javascript:signOut()'>Sign Out</a>";
		}
	}	
	function getAPage(){
	if($('mypages').options[$('mypages').selectedIndex].value>0){
				var lag = 100;
				var x = cWins.length*3;
				closeAll(lag);
				setTimeout("callAjax('retrieve',"+$('mypages').options[$('mypages').selectedIndex].value+")",x*lag);
			}	
	}
	
	function closeAll(lag) {
	    var x = 0;
	    if(!lag) lag=250;
	    	//alert(lag);
	    		//alert(cWins.length);
		for(var i=(cWins.length-1);  i>=0;  i--) {
		  setTimeout('cWins['+i+'].hide()',(x*lag));
		  x++;
		  erasePortletCookie(x);
		  //alert(x);
		}
		setTimeout('cWins.length=0; idx=0; cascade=0;',x*lag);
		clearDebug();
	}
	</script>
	<script>	
	//function setPortletCookie(pName,pURL,top,left,width,height,zIndex,pId,wId){
	function setPortletCookie(pName,pURL,top,left,width,height,zIndex,pId,wState){
		debug("setPortletCookie: "+pName+", "+pURL+", "+top+", "+left+", "+width+", "+height+", "+zIndex+", "+pId+", "+wState);
		
		/*
		createCookie('wCount',pId,7);
		createCookie('pName_'+pId,pName,7);
		createCookie('pURL_'+pId,pURL,7);
		createCookie('top_'+pId,top,7);
		createCookie('left_'+pId,left,7);
		createCookie('width_'+pId,width,7);
		createCookie('height_'+pId,height,7);
		createCookie('zIndex_'+pId,zIndex,7);
		createCookie('wState_'+pId,wState,7);
		*/
	}
	function erasePortletCookie(portletID){
		debug("erasePortletCookie() called!");
		alert(portletID);
		/*
		eraseCookie('wCount');
		createCookie('pName_'+portletID);
		createCookie('pURL_'+portletID);
		createCookie('top_'+portletID);
		createCookie('left_'+portletID);
		createCookie('width_'+portletID);
		createCookie('height_'+portletID);
		createCookie('zIndex_'+portletID);
		*/
	}
	
	</script>
	
	<script language="JavaScript" type="text/javascript" src="js/ajaxhelper.js"> </script>	
	<!--link rel="stylesheet" href="css/autocomplete.css" type="text/css" media="screen" charset="utf-8" /-->
	<script src="js/prototype.js" type="text/javascript"></script>
	<script src="js/scriptaculous.js" type="text/javascript"></script>
	<!--script src="js/select.js" type="text/javascript"></script-->

	<script>
	var date = new Date();
	date.setYear(date.getYear()+1903);
	// Martys cascade test
	var idx = 0;
	var xidx = 0;
	var cascade = 0;
	var cOffset = 23;
	var cWins = new Array();
	</script>
	<script>
	function selectPortlet(pName,pURL,top,left,width,height,zIndex,wState) {
		top = (!top) ? 50 + (cascade*cOffset) : top;
		left = (!left) ? 20 + (cascade*cOffset): left;
		width =(!width) ? 550 : width;
		height = (!height) ?	400 : height;
		zIndex = (!zIndex) ? (Windows) ? parseInt(Windows.maxZIndex) + 20 : 100 + xidx : parseInt(zIndex);
		wState = (!wState) ? 'reg' : wState;
		shuzi = new Window('dialog'+idx, {className: 'dialog', title: pName, top:top, left:left, width:width, height:height, zIndex:zIndex, opacity:1, resizable: true, url: pURL, wState:wState});
	    	if (wState=='min') shuzi.minimize();
	    	if (wState=="max") shuzi.maximize();
	    shuzi.show();
	    //shuzi.toFront();
		shuzi.setCookie(idx,date);
		shuzi.setDestroyOnClose(); //The window will be destroy by clicking on close button instead of being hidden
	    cascade++;
	    idx++;
	    xidx++;
		cWins.push(shuzi);
		setPortletCookie(pName,pURL,top,left,width,height,zIndex,idx,wState);
	}
	function checkCookie(){
		wCount = readCookie('wCount');
		debug('checkCookie: wCount = '+wCount);
		if(wCount){
			retrieveWinsFromCookie(wCount);
		}
	}
	function retrieveWinsFromCookie(wCount){
		for(var i=0;i<wCount;i++){
			try{
				cpName = readCookie('pName_'+(i+1));
				cpURL = readCookie('pURL_'+(i+1));
				ctop = readCookie('top_'+(i+1));
				cleft = readCookie('left_'+(i+1));
				cwidth = readCookie('width_'+(i+1));
				cheight = readCookie('height_'+(i+1));
				czIndex = readCookie('zIndex_'+(i+1));
				cwinState = readCookie('wState_'+(i+1)); 
				//alert(cwinState);
					if((cpName)&&(cpURL)) {
						selectPortlet(cpName,cpURL,ctop,cleft,cwidth,cheight,czIndex,cwinState);
					}else{
						debug('win_'+(i+1)+' failed to load...');
					}
			}catch(e){debug('retrieveWinsFromCookie failed: '+e.description)}finally{}
		}
	}
	
	</script>

</head>
<body>
<script>
var mouseTracker = null;
var MouseTracker = Class.create();
MouseTracker.prototype = {
    initialize: function() {    
        this.eventMouseMove = this.mouseMoved.bindAsEventListener(this);
        Event.observe(document, "mousemove", this.eventMouseMove);
    },
    destroy: function() {
      Event.stopObserving(document, "mousemove", this.eventMouseMove);
    },
    mouseMoved: function(event) {
        var pointer = [Event.pointerX(event), Event.pointerY(event)];
        clearDebug();
        debug("Mouse  : " + pointer[0] + "," + + pointer[1]);
    }
}

function showDebugWindow() {
    //if (mouseTracker == null)
        //mouseTracker = new MouseTracker();
    //hideDebug();
    showDebug();
}
	showDebugWindow()
//showDebug();
//showExtendedDebug();

//WindowStore.init();
</script>
<ul id='menuList'>
		<!--li><a href='containers/home.html?0' target='mainiframe'>Home</a>
			<ul>
				<li><a onclick='selectPortlet("From The Director","/director/index.html");return false;'  href='#'>From The Director</a></li>
				<li><a onclick='selectPortlet("What%22s New","../whats_new/index.jsp");return false;'  href='#'>What's New</a></li>
				<li><a onclick='selectPortlet("News","../NewsPaper/index.jsp");return false;'  href='#'>News</a></li>
				<li><a onclick='selectPortlet("Resources","/cached-content/opg/portlets/resourceList/resourcesAsTree.html");return false;'  href='#'>Resources</a></li>
				<li><a onclick='selectPortlet("Organizations","../Organizations/index.html");return false;'  href='#'>Organizations</a></li>
				<li><a onclick='selectPortlet("People","/cached-content/opg/portlets/people/people.html");return false;'  href='#'>People</a></li>
			</ul>
		</li>
		<li><a onclick='selectPortlet("Favorites","../favorites/favorites.jsp");return false;'  href='#'>Favorites</a></li>
		<li><a onclick='selectPortlet("Help","../help/index.jsp");return false;'  href='#'>Help</a></li>
		<li><a onclick='selectPortlet("IT Services","../helpdesk/index.jsp");return false;'  href='#'>IT Services</a></li>
		<li><a onclick='selectPortlet("Feedback","../../fform.htm");return false;'  href='#'>Feedback</a></li-->
</ul>
<div id='toolbar'></div>
<!--input type="button" onclick="closeAll()" value="closeAll()">&nbsp;<input type="button" onclick="showWin()" value="create()"-->

</body>
</html>
