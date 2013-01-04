var menuMouseOverColor = "#CCC";
var buttonHTML = "<center style='font-size: 8pt;'>Quick Links";
var allowSliding = 0;
//sliderButtonIn = "&#62;";
//sliderButtonOut = "&#60;";
sliderButtonIn = "<img src='graphics/right.gif' alt='Slide In'>";
sliderButtonOut = "<img src='graphics/left.gif' alt='Slide Out'>";
var startSlidIn = 0;
var allowIconOnOff = 0;
var allowOverlayOnOff = 0;
var allowHeaderOnOff = 0;
var allowSliderOnOff = 0;
var allowClock = 1;
onbuttonImg = "graphics/green.bullet.gif";
offbuttonImg = "graphics/grey.bullet.gif";

try {
	exmlhttp = new ActiveXObject("Msxml2.XMLHTTP"); //ie only
} catch (e) {
	try {
  		exmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
  	} catch (e) {
		try {
			exmlhttp=new XMLHttpRequest(); //firefox
		}
		catch (e) {
	   		exmlhttp = false;
			alert("You're browser does not support AJAX");
		}
  	}
}

/* NOT USED - ACTS FUNNY
function fadeIt(id,opacity) {
	var oMenu = document.getElementById(id).style;
	oMenu.opacity = (opacity / 100); 
	oMenu.MozOpacity = (opacity / 100); 
	oMenu.KhtmlOpacity = (opacity / 100); 
	oMenu.filter = "alpha(opacity=" + opacity + ")";
}
*/

function ee() {
	if (document.searchForm.searchtext.value=="games") {
		document.searchForm.target='mainiframe';
		document.searchForm.action='containers/ee.html';
	}
	else {
		//document.searchForm.action='containers/dynamic.html?0?Search?blank.html';
		document.searchForm.action='portlets/AutonomySearch/index.jsp';
		document.searchForm.target='_blank';
	}
}

function slideIt(twidth) {
var oToolbar = document.getElementById('toolbartable').style;
oToolbar.width=twidth+'%'
}

function toolbarIn() {
	timer=0;
	for (i=100; i>0; i--) {
		setTimeout("slideIt('"+i+"')",timer*5);
		timer++;
	}
	document.getElementById('menuButton').style.display='none';
	document.getElementById('toolbarSearch').style.display='none';
	document.getElementById('onoffButtons').style.display='none';
	setTimeout("document.getElementById('sliderButton').innerHTML = sliderButtonOut",350);
	document.getElementById('sliderButton').onclick = function anonymous() { toolbarOut() };
	setTimeout("document.getElementById('toolbar').style.position='absolute'",350);
}

function toolbarOut() {
	document.getElementById('toolbar').style.position='static';
	setTimeout("document.getElementById('menuButton').style.display='block'",350);
	setTimeout("document.getElementById('toolbarSearch').style.display='block'",350);
	setTimeout("document.getElementById('onoffButtons').style.display='block'",350);
	timer=0;
	for (i=1; i<=100; i++) {
		setTimeout("slideIt('"+i+"')",timer*5);
		timer++;
	}
	setTimeout("document.getElementById('sliderButton').innerHTML = sliderButtonIn",350);
	document.getElementById('sliderButton').onclick = function anonymous() { toolbarIn() };
}

function iconOnOff(oImg,action) {
	if (action=="off") {
		oImg.src=offbuttonImg;
		oImg.onclick=function anonymous(){iconOnOff(this,'on')}
		document.getElementById('icons').style.visibility='hidden';
		document.getElementById('icons').style.display='none';
		document.images['overlayonoff'].src=offbuttonImg;
	}
	if (action=="on") {
		oImg.src=onbuttonImg;
		oImg.onclick=function anonymous(){iconOnOff(this,'off')}
		document.getElementById('icons').style.visibility='visible';
		document.getElementById('icons').style.display='block';
		if (allowOverlay==1) {
			document.images['overlayonoff'].src=onbuttonImg;
		}
	}
}

function overlayOnOff(oImg,action) {
	if (document.getElementById('icons').style.visibility!='hidden') {
		if (action=="off") {
			oImg.src=offbuttonImg;
			oImg.onclick=function anonymous(){overlayOnOff(this,'on')}
			allowOverlay = 0;
			initIcons();
		}
		if (action=="on") {
			oImg.src=onbuttonImg;
			oImg.onclick=function anonymous(){overlayOnOff(this,'off')}
			allowOverlay = 1;
			initIcons();
		}
	}
}

function headerOnOff(oImg,action) {
	if (action=="off") {
		oImg.src=offbuttonImg;
		oImg.onclick=function anonymous(){headerOnOff(this,'on')}
		document.getElementById('header').style.visibility='hidden';
		document.getElementById('header').style.display='none';
	}
	if (action=="on") {
		oImg.src=onbuttonImg;
		oImg.onclick=function anonymous(){headerOnOff(this,'off')}
		document.getElementById('header').style.visibility='visible';
		document.getElementById('header').style.display='block';
	}
}

function sliderOnOff(oImg,action) {
	if (action=="off") {
		oImg.src=offbuttonImg;
		oImg.onclick=function anonymous(){sliderOnOff(this,'on')}
		allowSliding = 0;
		if (document.getElementById('menuButton').style.display=='none') {
			startSlidIn=1;
		}
		else {
			startSlidIn=0;
		}
		initToolbar();
	}
	if (action=="on") {
		oImg.src=onbuttonImg;
		oImg.onclick=function anonymous(){sliderOnOff(this,'off')}
		allowSliding = 1;
		if (document.getElementById('menuButton').style.display=='none') {
			startSlidIn=1;
		}
		else {
			startSlidIn=0;
		}
		initToolbar();
	}
}

function doMenu() {
	document.body.style.cursor='pointer';
	document.getElementById('menuButton').style.borderStyle='inset';
	document.getElementById('menuOverlay').style.visibility='visible';
	document.getElementById('menuOverlay').style.zIndex=Windows.maxZIndex + 10; //function to overlay on top of other win obj...
	/* NOT USED - ACTS FUNNY
	var timer=0;
	for (i=0; i<=100; i++) {
		setTimeout("fadeIt('menuOverlay',"+i+")",timer*50);
		timer++;
	}
	*/
}

function undoMenu() {
	document.body.style.cursor='default';
	document.getElementById('menuButton').style.borderTopStyle='solid';
	document.getElementById('menuButton').style.borderLeftStyle='solid';
	document.getElementById('menuButton').style.borderRightStyle='outset';
	document.getElementById('menuButton').style.borderBottomStyle='outset';
	document.getElementById('menuOverlay').style.visibility='hidden';
}

function doSubMenu(oSpan,num) {
	document.body.style.cursor='pointer';
	oSpan.style.backgroundColor=menuMouseOverColor;
	if (num!=null) {
		document.getElementById('menuSub'+num).style.visibility='visible';
	}
}

function undoSubMenu(oSpan,num) {
	document.body.style.cursor='default';
	oSpan.style.backgroundColor=oSpan.parentNode.style.backgroundColor;
	if (num!=null) {
		document.getElementById('menuSub'+num).style.visibility='hidden';
	}
}

var timerID = null
var timerRunning = false

function stopclock(){
    if(timerRunning)
        clearTimeout(timerID)
    timerRunning = false
}

function initClock(){
    stopclock()
    showtime()
}

function showtime(){
    var now = new Date()
    var hours = now.getHours()
    var minutes = now.getMinutes()
    var seconds = now.getSeconds()
    var timeValue = "" + ((hours > 12) ? hours - 12 : hours)
    timeValue  += ((minutes < 10) ? ":0" : ":") + minutes
    timeValue  += ((seconds < 10) ? ":0" : ":") + seconds
    timeValue  += (hours >= 12) ? " pm" : " am"
    if (document.getElementById('clock')) {
        document.getElementById('clock').innerHTML = timeValue
    }
    timerID = setTimeout("showtime()",1000)
    timerRunning = true
}

function changeUsername() {
	//alert('changeUsername');
	exmlhttp.open("GET","js/checkLogin.jsp",false);
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

function initToolbar() {
	var onoffHTML = "<table border=0 cellspacing=0 cellpadding=0><tr>";
	if (allowClock==1) {
		onoffHTML += "<td colspan='4' valign='top'><center><!--div id='clock'></div--></td><tr>";
		//initClock();
	}
	if (allowSliding==1) {
		sliderHTML = "<div id='sliderButton' onClick='toolbarIn()' onmouseover=\"style.cursor='pointer'\" onmouseout=\"style.cursor='default'\">"+sliderButtonIn+"</div>";
		document.body.style.overflowX='hidden';
		if (allowSliderOnOff==1) {
			onoffHTML += "<td><center><img alt='Toolbar Slider' src='"+onbuttonImg+"' onClick=\"sliderOnOff(this,'off')\" onmouseover=\"style.cursor='pointer'\" onmouseout=\"style.cursor='default'\" name='slideronoff'></td>";
		}
	}
	else {
		sliderHTML = "&nbsp;";
		if (allowSliderOnOff==1) {
			onoffHTML += "<td><center><img alt='Toolbar Slider' src='"+offbuttonImg+"' onClick=\"sliderOnOff(this,'on')\" onmouseover=\"style.cursor='pointer'\" onmouseout=\"style.cursor='default'\" name='slideronoff'></td>";
		}
	}
	if (allowIconOnOff==1) {
		if (document.getElementById('icons').style.visibility=='hidden') {
			onoffHTML += "<td><center><img alt='Icons' src='"+offbuttonImg+"' onClick=\"iconOnOff(this,'on')\" onmouseover=\"style.cursor='pointer'\" onmouseout=\"style.cursor='default'\" name='icononoff'></td>";
		}
		else {
			onoffHTML += "<td><center><img alt='Icons' src='"+onbuttonImg+"' onClick=\"iconOnOff(this,'off')\" onmouseover=\"style.cursor='pointer'\" onmouseout=\"style.cursor='default'\" name='icononoff'></td>";
		}
	}
	if (allowOverlayOnOff==1) {
		if ((allowOverlay==1) && (document.getElementById('icons').style.visibility!='hidden')) {
			onoffHTML += "<td><center><img alt='Icon Menus' src='"+onbuttonImg+"' onClick=\"overlayOnOff(this,'off')\" onmouseover=\"style.cursor='pointer'\" onmouseout=\"style.cursor='default'\" name='overlayonoff'></td>";
		}
		else {
			onoffHTML += "<td><center><img alt='Icon Menus' src='"+offbuttonImg+"' onClick=\"overlayOnOff(this,'on')\" onmouseover=\"style.cursor='pointer'\" onmouseout=\"style.cursor='default'\" name='overlayonoff'></td>";
		}
	}
	if (allowHeaderOnOff==1) {
		if (document.getElementById('header').style.visibility=='hidden') {
			onoffHTML += "<td><center><img alt='Header' src='"+offbuttonImg+"' onClick=\"headerOnOff(this,'on')\" onmouseover=\"style.cursor='pointer'\" onmouseout=\"style.cursor='default'\" name='headeronoff'></td>";
		}
		else {
			onoffHTML += "<td><center><img alt='Header' src='"+onbuttonImg+"' onClick=\"headerOnOff(this,'off')\" onmouseover=\"style.cursor='pointer'\" onmouseout=\"style.cursor='default'\" name='headeronoff'></td>";
		}
	}
	onoffHTML += "</table>";
	//document.getElementById('toolbar').innerHTML = "<form name='searchForm' method='POST' action='containers/dynamic.html?0?Search?blank.html' target='_blank' onsubmit='ee()'><table id='toolbartable' class='toolbar'><tr><td align='left' nowrap><div id='menuButton' onmouseover='doMenu()' onmouseout='undoMenu()'><table border=0><tr><td valign='center'>"+buttonHTML+"</td></table></div></td><td align='center' nowrap><div id='toolbarSearch'><input type='text' size='50' name='searchtext'><input type='submit' class='submit' value='Search'></div></td><td align='right' nowrap><div id='onoffButtons'>"+onoffHTML+"</div></td><td align='right'>"+sliderHTML+"</td></table></form>";
	//var toolBarContent = "<form name='searchForm' method='POST' action='portlets/AutonomySearch/index.jsp' target='_blank' onsubmit='ee()'><table id='toolbartable' class='toolbar'><tr><td align='left' nowrap><div id='menuButton' onmouseover='doMenu()' onmouseout='undoMenu()'><table border=0><tr><td valign='center'>"+buttonHTML+"</td></table></div></td><td><input type='button' onclick='saveWin()' value='saveWin()'>&nbsp;<input type='button' onclick='closeAll()' value='closeAll()'>&nbsp;<input type='button' onclick='selectPortlet(\"OCRWM Home\",\"http://www.ocrwm.doe.gov\")' value='create()'></td><td valign='center' nowrap><center><div id='showusername'></div></td><td align='center' valign='bottom' nowrap><div id='toolbarSearch'><!--input type='text' size='50' name='searchtext' id='searchtext'><input type='hidden' name='mSearchType' value='simple'><input type='hidden' name='mResultsDisplay' value='yes'><input type='submit' class='submit' value='Site Search'> <a href='javascript:document.searchForm.submit();'>Advanced Search</a--></div></td><td align='right' valign='bottom' nowrap><div id='onoffButtons'>"+onoffHTML+"</div></td><td align='right'>"+sliderHTML+"</td></table></form>";
	var toolBarContent = "<table id='toolbartable' class='toolbar' border=0><tr><td align='left' nowrap><div id='menuButton' onmouseover='doMenu()' onmouseout='undoMenu()'><table border=0><tr><td valign='center'>"+buttonHTML+"</td></table></div></td><td valign=middle><input type='button' id='saveWin' value='saveWin()'>&nbsp;<input type='button' id='deleteWin' value='deleteWin()'>&nbsp;<input type='button' onclick='closeAll()' value='closeAll()'></td><td><select style='visibility:visible;' id=mypages name=mypages onchange='getAPage();'><option value=0 selected=selected>My Pages</option></select>&nbsp;</td><td valign='center' nowrap><center><div id='showusername'>&nbsp;</div></td><td align='center' valign='bottom' nowrap></td><td align='right' valign='bottom' nowrap><div id='onoffButtons'>"+onoffHTML+"</div></td><td align='right'>"+sliderHTML+"</td></table>";
	document.getElementById('toolbar').innerHTML = toolBarContent;
		//prototype listeners for the buttons in toolBarContent...
		//Event.observe('login', 'click', function() {
		//	runFx(AjaxHelper.login)
		//});
		//Event.observe('getPNames', 'click', function() {
		//	runFx(AjaxHelper.getPNames)
		//});
		//Event.observe('retrieveWins', 'click', function() {
		//	runFx(AjaxHelper.retrieveWins)
		//});
		Event.observe('saveWin', 'click', function() {
			runFx(AjaxHelper.saveWin)
		});
		Event.observe('deleteWin', 'click', function() {
			runFx(AjaxHelper.deleteWin)
		});
		//refer to ajaxhelper.js...								
	
	changeUsername();
	var oList = document.getElementById('menuList');
	exmlhttp.open("Get", "/cached-content/opg/portlets/resourceList/resourcesAsListMP.html", false);
	exmlhttp.send(null);
		//alert(exmlhttp.responseText);
	//oList.innerHTML += "<li><a href='containers/home.html?3' target='mainiframe'>Resources</a>"+exmlhttp.responseText+"</li>";
	oList.innerHTML += "<li><a href='#' onclick='return false' target='mainiframe'>Resources</a>"+exmlhttp.responseText+"</li>";
	var spanContent = "<span id='menuOverlay'><table border=0 cellspacing='0' cellpadding='0'>";
	x=0;
	for (i=0; i<oList.childNodes.length; i++) {
		if (oList.childNodes[i].nodeName=="LI") {
			if ((oList.childNodes[i].childNodes.length<3) && (oList.childNodes[i].childNodes[1].childNodes.length<1)) {
				spanContent += "<tr><td onmouseover='doSubMenu(this)' onmouseout='undoSubMenu(this)' align='left' nowrap><table border=0 width='100%' cellspacing=1 cellpadding=1><tr><td align='left' width='100%' nowrap>"+oList.childNodes[i].innerHTML+"</td></table></td>";
			}
			else {
				var tmpName = oList.childNodes[i].innerHTML.split("\n");
				spanContent += "<tr><td onmouseover=\"doSubMenu(this,'"+x+"')\" onmouseout=\"undoSubMenu(this,'"+x+"')\" align='left' nowrap><table border=0 width='100%' cellspacing=1 cellpadding=1><tr><td align='left' width='100%' nowrap>"+tmpName[0]+"</td><td align='right'>></td><td valign='top' nowrap><span id='menuSub"+x+"' class='subMenu'><table border=0>";
				if (oList.childNodes[i].childNodes[1].childNodes.length>0) {
					for (y=0; y<oList.childNodes[i].childNodes[1].childNodes.length; y++) {
						if (oList.childNodes[i].childNodes[1].childNodes[y].nodeName=="LI") {
							if (oList.childNodes[i].childNodes[1].childNodes[y].childNodes.length>1) {
								var tmpName3 = oList.childNodes[i].childNodes[1].childNodes[y].innerHTML.split("\n");
								spanContent += "<tr><td onmouseover=\"doSubMenu(this,'"+x+y+"')\" onmouseout=\"undoSubMenu(this,'"+x+y+"')\" align='left' nowrap><table border=0 width='100%' cellspacing=1 cellpadding=1><tr><td align='left' width='100%' nowrap>"+tmpName3[0]+"</td><td align='right'>></td><td valign='top' nowrap><span id='menuSub"+x+y+"' class='subMenu'><table border=0>";
								for (z=0; z<oList.childNodes[i].childNodes[1].childNodes[y].childNodes[1].childNodes.length; z++) {
									if (oList.childNodes[i].childNodes[1].childNodes[y].childNodes[1].childNodes[z].nodeName=="LI") {
										if (oList.childNodes[i].childNodes[1].childNodes[y].childNodes[1].childNodes[z].childNodes.length>1) {
											var tmpName2 = oList.childNodes[i].childNodes[1].childNodes[y].childNodes[1].childNodes[z].innerHTML.split("\n");
											spanContent += "<tr><td onmouseover=\"doSubMenu(this,'"+x+y+z+"')\" onmouseout=\"undoSubMenu(this,'"+x+y+z+"')\" align='left' nowrap><table border=0 width='100%' cellspacing=1 cellpadding=1><tr><td align='left' width='100%' nowrap>"+tmpName2[0]+"</td><td align='right'>></td><td valign='top' nowrap><span id='menuSub"+x+y+z+"' class='subMenu'><table border=0>";
											for (a=0; a<oList.childNodes[i].childNodes[1].childNodes[y].childNodes[1].childNodes[z].childNodes[1].childNodes.length; a++) {
												if (oList.childNodes[i].childNodes[1].childNodes[y].childNodes[1].childNodes[z].childNodes[1].childNodes[a].nodeName=="LI") {
													spanContent += "<tr><td onmouseover='doSubMenu(this)' onmouseout='undoSubMenu(this)' align='left' nowrap>"+oList.childNodes[i].childNodes[1].childNodes[y].childNodes[1].childNodes[z].childNodes[1].childNodes[a].innerHTML+"</td>";
												}
											}
											spanContent += "</table></span></td></table></td>";
										}
										else {
											spanContent += "<tr><td onmouseover='doSubMenu(this)' onmouseout='undoSubMenu(this)' align='left' nowrap>"+oList.childNodes[i].childNodes[1].childNodes[y].childNodes[1].childNodes[z].innerHTML+"</td>";
										}
									}
								}
								spanContent += "</table></span></td></table></td>";
							}
							else {
								spanContent += "<tr><td onmouseover='doSubMenu(this)' onmouseout='undoSubMenu(this)' align='left' nowrap>"+oList.childNodes[i].childNodes[1].childNodes[y].innerHTML+"</td>";
							}
						}
					}
				}
				else {
					for (y=0; y<oList.childNodes[i].childNodes[2].childNodes.length; y++) {
						if (oList.childNodes[i].childNodes[2].childNodes[y].nodeName=="LI") {
							spanContent += "<tr><td onmouseover='doSubMenu(this)' onmouseout='undoSubMenu(this)' align='left' nowrap>"+oList.childNodes[i].childNodes[2].childNodes[y].innerHTML+"</td>";
						}
					}
				}
				spanContent += "</span></table></td></table></td>";
				x=x+1;
			}
		}
	}
	spanContent += "</table></span>";
	document.getElementById('menuButton').innerHTML+=spanContent;
	if (startSlidIn==1) {
		slideIt('1');
		document.getElementById('menuButton').style.display='none';
		if (document.getElementById('sliderButton')) {
			document.getElementById('sliderButton').innerHTML = sliderButtonOut;
			document.getElementById('sliderButton').onclick = function anonymous() { toolbarOut() };
		}
	}
}