try {
	axmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
} catch (e) {
	try {
  		axmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
  	} catch (e) {
		try {
			axmlhttp=new XMLHttpRequest();
		}
		catch (e) {
	   		axmlhttp = false;
			alert("You're browser does not support xmlhttp");
		}
  	}
}

var timerid=0;
var scrollerid=0;
var divheight=0;
var firstRun=true;

function scrollIt(homeobjid,direction) {
	var homeobj = document.getElementById(homeobjid);
	if (direction=="up") {
		homeobj.scrollTop--;
		if (homeobj.scrollTop==0) {
			homeobj.scrollTop=homeobj.scrollHeight-divheight;
		}
	}
	else {
		homeobj.scrollTop++;
		if (homeobj.scrollTop+divheight==homeobj.scrollHeight) {
			homeobj.scrollTop=0;
		}
	}
	timerid = setTimeout("scrollIt('"+homeobjid+"','"+direction+"')",100);
}

function stopScroll(homeobjid) {
	clearTimeout(timerid);
	if (homeobjid!='null') {
		setTimeout("scrollIt('"+homeobjid+"','down')",30000);
	}
}

function renderHome(){
	var textObject;var textValue;
	if(browsertype=='Microsoft Internet Explorer'){
			textValue = tinyMCE.get('elm1').getContent();
			//textObj = tinyMCE.get('elm1');
			//textValue = textObj.getContent();
				//tinyMCE.remove('elm1');
	}else{
	textObject = document.getElementById("elm1");
	textValue = textObject.value;
	}
	top.debug(textValue);
	var homeobj = document.getElementById("homemessage");
	var origTextObj = document.getElementById("origText");
	var origTextValue = origTextObj.value;
	homeobj.innerHTML = textValue;		
		
		var url = "_plugins/homemessage/homemessageEcho.jsp";
		var parameters = "origText="+encodeURIComponent(origTextValue);
		  	parameters += "&newText="+encodeURIComponent(textValue);
	    var ajaxRequest = new Ajax.Request(url,
	    {
	           method: 'post',
	           parameters: parameters,
	           onLoading: function() {
	  		 },
	           onSuccess: function(transport){
				 var response = transport.responseText || "no response text";
				 top.debug(response);
				 },
				 onFailure: function(){ debug('callAjax: Something went wrong...') }
	    });
		homeobj.onclick = homeedit;
}

	var idx = 0;
	var xidx = 0;
	var cascade = 0;
	var cOffset = 23;
	var cWins = new Array();
	var shuzi;
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
		//shuzi.setCookie(idx,date);
		shuzi.setDestroyOnClose(); //The window will be destroy by clicking on close button instead of being hidden
	    cascade++;
	    //idx++;
	    //xidx++;
		cWins.push(shuzi);
	}

function homeedit(){
	top.debug('homeedit() called!');
	if(browsertype=='Microsoft Internet Explorer'){
       		selectPortlet('Update Title Message','_plugins/homemessage/homemessageEdit.htm',65,335,455,300,1001,null);
	}else{
			var homeobjid = "homemessage";
			var homeobj = document.getElementById(homeobjid);
			var homeobjtxt = homeobj.innerHTML;
			homeobj.innerHTML = "";
			var f = document.createElement("form");
			var t = document.createElement("textarea");
			var i = document.createElement("input");
			var b;
			f.id = "hometextform";f.name = "hometextform";
			f.method = "post";f.action = "#";
			t.id = "elm1";t.name = "elm1";
			i.id = "origText";i.name = "origText";
			i.type = "hidden";
			t.style.backgroundColor = "#ffffff";
			t.style.color = "#666666";
			if(browsertype=='Microsoft Internet Explorer'){
				/* 
				t.rows = 4;
				t.cols = 75;
				b = document.createElement("input");
				b.type = "button";
				b.id = "saveBttn";
				b.value = "Save";
				b.onclick = renderHome;
				*/
			}else{
				//t.style.width = "80%";
				//t.style.height = "80%";
				t.rows = 2;
				t.cols = 75;
			}
			f.style.textAlign="center";
			f.style.verticalAlign="middle";
			t.onblur = renderHome;
			t.value = homeobjtxt;
			i.value = homeobjtxt;
			f.appendChild(t);
			f.appendChild(i);
			//if(browsertype=='Microsoft Internet Explorer') f.appendChild(b);
			homeobj.appendChild(f);
			homeobj.onclick = null;
	}
}

function homepreviewclear(){
var homeobjid = "homemessage";
var homeobj = document.getElementById(homeobjid);
homeobj.title = "" ;
}

function homepreview(){
var homeobjid = "homemessage";
var homeobj = document.getElementById(homeobjid);
homeobj.title = "[Click here to edit this message]" ;
}

function checkHomeMessage(){
	if(top.uid!=0) {
	callAjax('titlemessage', false , false);
	}else{
		initHomeMessage();
	}
}

function initRSSReaders(){
	//top.debug("initRSSReaders() called");
	/*
	var crestobj = document.getElementById("crestmessage");
	var ocrwmobj = document.getElementById("ocrwmmessage");
	crestobj.style.height="55px";
	ocrwmobj.style.height="55px";
	var sUrl = "utils/getRSS.jsp";
	pullXMLText(sUrl,"crestmessage","feedName=thecrest");
	pullXMLText(sUrl,"ocrwmmessage","feedName=ocrwmwhatsnew");
	*/
}

function initRSSReadersNew(){
	//top.debug("initRSSReaders() called");
	
	/*
	var crestobj = document.getElementById("crestmessage");
	var directorobj = document.getElementById("directorsmessage");
	var fyiobj = document.getElementById("fyimessage");
	crestobj.style.height="55px";
	directorobj.style.height="55px";
	var sUrl = "utils/getRSS.jsp";
	pullXMLText(sUrl,"crestmessage","feedName=thecrestpreview");
	pullXMLText(sUrl,"fyimessage","feedName=fyi");
	pullXMLText(sUrl,"directorsmessage","feedName=director");
	*/
	
	//setTimeout("pullXMLText('utils/getRSS.jsp','fyimessage','feedName=fyi')",1000);
	//setTimeout("pullXMLText('utils/getRSS.jsp','directorsmessage','feedName=director')",1000);
	//pullXMLText(sUrl,"ocrwmmessage","feedName=ocrwmwhatsnew");
}

function initHomeMessage(edit) {
	//top.debug("initHomeMessage("+edit+") called");
	var homeobjid = "homemessage";
	var homeobj = document.getElementById(homeobjid);
	try{
	divheight = document.getElementById("header").scrollHeight;
	}catch(e){}
	homeobj.style.height="55px";
	pullXMLText("/documents/misc/OPG-Announcement.html","homemessage","");
	if(edit) {
		homeobj.onclick = homeedit;
		homeobj.onmouseover = homepreview;
	}else{
		homeobj.onclick = homepreview;
		homeobj.onmouseover = homepreviewclear;
	}			
	
}

function updatedContent(target){
	targetObj = $(target);
	if(targetObj) targetObj.innerHTML = pullXMLText();
}

function pullXMLText(sUrl,target,parameters){
	var tText = "";
	var url = sUrl;
	var ajaxRequest = new Ajax.Request(url,
		    {
		           method: 'post',
		           parameters: parameters,
		           onLoading: function() {
		  		 },
		           onSuccess: function(transport){
					 tText = transport.responseText || "no response text";
					 	//top.debug(tText);		
					 var homeobj = document.getElementById(target);
					 var rText = "";
					 homeobj.innerHTML = tText;
					 },
					 onFailure: function(){ debug('callAjax: Something went wrong...') }
		    });
}