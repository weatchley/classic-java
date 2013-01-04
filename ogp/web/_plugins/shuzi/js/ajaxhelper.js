var uid = 0;

function runFx(f){
	f();
}

function callAjax(target,option1,option2){
			  
			  
			//alert("In function callAjax .... ");
			  
			  //debug("callAjax() called with personID = " + uid);			  
			  var aTarget = target;
			  if(aTarget == 'getPNamesMod') aTarget='getPNames';
			  if(aTarget == 'retrieveMod') aTarget='retrieve';  
	          var url = "";     
	          if((aTarget=='getUID')||(aTarget=='titlemessage')||(aTarget=='portletEdit')){
	        	  url = "utils/sessionManager.jsp";
			  }else{
	              url = "../../utils/saveWinStateOra.jsp"; 
	          }        
	              var parameters = "action="+encodeURIComponent(aTarget);
	              	  if (uid>0) parameters += "&personID="+uid; //uid is globally declared
	              	  if (option1) parameters += "&option1="+encodeURIComponent(option1);
			  		  if (option2) parameters += option2;
			  		  //top.debug("callAjax() called with url="+url+", parameters="+ parameters);
	              var ajaxRequest = new Ajax.Request(url,
	              {
	                     method: 'post',
	                     parameters: parameters,
	                     onLoading: function() {
	                	 //top.debug("calling AJAX...");
	            		 },
	                     onSuccess: function(transport){
	      				 var response = transport.responseText || "no response text";
	      				 top.debug("callAjax: Success! -> " + response);
	      				 xml2Obj(target,transport);
	    				 },
	    				 onFailure: function(){ debug('callAjax: Something went wrong...') }
	              });
}


function ajaxRefresh(sUrl){			  
			  //debug("ajaxRefresh() called with sUrl = " + sUrl);			  
	              var parameters = "";
	              var ajaxRequest = new Ajax.Request(sUrl,
	              {
	                     method: 'get',
	                     parameters: parameters,
	                     onLoading: function() {
	                	 //debug("calling AJAX...");
	            		 },
	                     onSuccess: function(transport){
	      				 var response = transport.responseText || "no response text";
	      				 	//debug("ajaxRefresh: Success! \n\n" + response);
	    				 },
	    				 onFailure: function(){ debug('ajaxRefresh: Something went wrong...') }
	              });
}

function xml2Obj(target,req){
	top.debug("xml2Obj called with target = "+target+", req = "+req.responseText);
	if (target=="login"){
		userId = callNValue("userId",req);
		userName = callNValue("userName",req);
		winnum = callNValue("winnum",req);
	}else if (target=="getPNames"){
		pagenum = callNValue("pagenum",req);
		for (var x=0; x<pagenum; x++){
			eval("lDesc"+x+" = callNValue('lDesc"+x+"',req);");
			eval("lNum"+x+" = callNValue('lNum"+x+"',req);");
			eval("addPages2Box(lDesc"+x+",lNum"+x+","+x+");");
		}
		//new Autocompleter.SelectBox('mypages', {width : '5000'});
	}else if (target=="getPNamesMod"){
		pagenum = callNValue("pagenum",req);
			//debug("pagenum: "+pagenum);
		for (var x=0; x<pagenum; x++){
			if(x==0){
			eval("lNum"+x+" = callNValue('lNum"+x+"',req);");
			//debug("default page id: "+lNum0);
				if((lNum0)&&(lNum0!=0)){
					var lag = 100;
					var x = cWins.length*3;
					setTimeout("callAjax('retrieveMod',"+lNum0+")",x*lag);
				}
			}
		}
	}else if (target=="retrieve"){
		layid = callNValue("layid",req);
		winnum = callNValue("winnum",req);
			//pwcount = callNValue("pwcount",req);
		lDesc = callNValue("lDesc",req);
		for (var y=0; y<winnum; y++){
			eval("cn"+y+" = callNValue('cn"+y+"',req);");
			eval("cu"+y+" = callNValue('cu"+y+"',req);");
			eval("origw"+y+" = callNValue('origw"+y+"',req);");
			eval("origh"+y+" = callNValue('origh"+y+"',req);");
			eval("origx"+y+" = callNValue('origx"+y+"',req);");
			eval("origy"+y+" = callNValue('origy"+y+"',req);");
			eval("zIndex"+y+" = callNValue('zIndex"+y+"',req);");
			eval("state"+y+" = callNValue('state"+y+"',req);");
			eval("bc"+y+" = callNValue('bc"+y+"',req);");
			eval("selectPortlet(cn"+y+",cu"+y+",origy"+y+",origx"+y+",origw"+y+",origh"+y+",zIndex"+y+",state"+y+");")
			
		}
	}else if (target=="retrieveMod"){
		layid = callNValue("layid",req);
		winnum = callNValue("winnum",req);
		lDesc = callNValue("lDesc",req);
		for (var y=0; y<winnum; y++){
			eval("cn"+y+" = callNValue('cn"+y+"',req);");
			eval("cu"+y+" = callNValue('cu"+y+"',req);");
			eval("origw"+y+" = callNValue('origw"+y+"',req);");
			eval("origh"+y+" = callNValue('origh"+y+"',req);");
			eval("origx"+y+" = callNValue('origx"+y+"',req);");
			eval("origy"+y+" = callNValue('origy"+y+"',req);");
			eval("zIndex"+y+" = callNValue('zIndex"+y+"',req);");
			eval("state"+y+" = callNValue('state"+y+"',req);");
			eval("bc"+y+" = callNValue('bc"+y+"',req);");
			//eval("selectPortlet(cn"+y+",cu"+y+",origy"+y+",origx"+y+",origw"+y+",origh"+y+",zIndex"+y+",state"+y+");")
			eval("selectPortlet(cn"+y+",cu"+y+",null,origy"+y+",origx"+y+",origw"+y+",origh"+y+",zIndex"+y+",state"+y+",'alphacube',0,0,0,0,0);")
		}
	}else if (target=="save"){
	runFx(AjaxHelper.getPNames);
	}else if (target=="delete"){
	runFx(AjaxHelper.getPNames);
	}else if (target=="getUID"){
	uid = req.responseText;uid=uid.replace(/\n/g,"");uid=uid.replace(/\r/g,"");
	}else if (target=="titlemessage"){
		if(req.responseText==1){
			top.initHomeMessage(1);
		}else{
			top.debug("no title message editing capability");
		}
	}else if (target=="portletEdit"){
		if(req.responseText==1){
			top.initPortletContents(1);
		}else{
			top.debug("no portlet editing capability");
		}
	}else{
	top.debug("callXPath error: invalid target");
	}
}


function callNValue(tagName,req){
	var rValue;
	rValue = req.responseXML.getElementsByTagName(tagName)[0].firstChild.nodeValue;
	//debug("callNValue(): " + tagName + " = " + rValue);
	return rValue;
}


//target specific helper functions begin
var AjaxHelper = {
	name: 'AjaxHelper',
	login: function(){
	//debug("AjaxHelper.login() called!");
	void callAjax('login');
	},
	getUID: function(){
	//top.debug("AjaxHelper.getUID() called!");
	void callAjax('getUID');
	},
	titlemessage: function(){
		void callAjax('titlemessage');
	},
	portletEdit: function(){
		void callAjax('portletEdit');
	},
	getPNames: function(){
	//debug("AjaxHelper.getPNames() called!");
	void callAjax('getPNames');
	},
	getPNamesMod: function(){
	//debug("AjaxHelper.getPNamesMod() called!");
	void callAjax('getPNamesMod');
	},
	retrieveWins: function(){
	//debug("AjaxHelper.retrieveWins() called!");
	void callAjax('retrieve',5); // for testing
	},
	refreshGwSession: function(){
	//debug("AjaxHelper.refreshGwSession() called!");
	ajaxRefresh("utils/sessionManager.jsp");
	},
	refreshAuSession: function(){
	//debug("AjaxHelper.refreshAuSession() called!");
	ajaxRefresh("include/autonomySession.jsp");
	},
	//doSessionTrack: function(){
	//sUrl = "test";
	//userName="test";
	//debug("AjaxHelper.doSessionTrack() called!");
	//ajaxRefresh("doSessionTrack?sUrl="+sUrl+"&userName="+userName);
	//},
	saveWin: function(){
	//debug("AjaxHelper.saveWin() called!");
	pageName = prompt('Enter a layout name','');
		if ( (pageName==' ') || (pageName==null) ) 
			 { 
			   pageName="New Layout"; 
			 } else {
		winString = formWinString();
		void callAjax('save',pageName,winString);
		}
	},
	deleteWin: function(layoutID){
	//debug("AjaxHelper.deleteWin() called!");
		//alert(layoutID);
	if(!layoutID) layoutID = prompt('Enter a layout ID','');
		if ( (layoutID==' ') || (layoutID==null) ) 
			 { 
			   layoutID=0; 
			 } else {
		void callAjax('delete',layoutID);
		}
	}
}
//layouts related helper functions end

function formWinString(){
	//debug('formWinString() called - winnum = '+idx);
	var tString = '&winNum='+idx;
	for(var i=0;i<idx;i++){
		wState = 'reg';
		if(cWins[i].isMinimized()) wState = 'min';
		if(cWins[i].isMaximized()) wState = 'max';
		ttop = new String (""); 
		tleft = new String (""); 	
		ttop = cWins[i].element.getStyle('top');
		tleft = cWins[i].element.getStyle('left');
		sttop = ttop.split("px"); 
		stleft = tleft.split("px");
		tpid = new String("");
		tpid = cWins[i].element.id;
		stpid = tpid.split("dialog");
		sstpid = null;
		if(stpid[1]) sstpid = (parseInt(stpid[1])+1);
		
		tString += "&winTitle"+i+"="+encodeURIComponent(cWins[i].title);
		tString += "&winUrl"+i+"="+encodeURIComponent(cWins[i].url);
		tString += "&winTop"+i+"="+sttop[0];
		tString += "&winLeft"+i+"="+stleft[0];
		tString += "&winWidth"+i+"="+cWins[i].width;
		tString += "&winHeight"+i+"="+cWins[i].height;
		tString += "&winZ"+i+"="+parseInt(cWins[i].element.getStyle('zIndex'));
		tString += "&winState"+i+"="+wState;
				
		/*  
		debug(cWins[i].title+', '+
			cWins[i].url+', '+
			sttop[0]+', '+
			stleft[0]+', '+
			cWins[i].width+', '+
			cWins[i].height+', '+
			parseInt(cWins[i].element.getStyle('zIndex'))+', '+
			sstpid+', '+
			wState)		
		*/
	}
	return tString;
}

function addPages2Box(desc,val,id){
	//debug("addPages2Box: "+desc+","+val+","+id);
	$('mypages').options.length=id+2;
	$('mypages').options[id+1] = new Option(desc+': '+val,val);
}

	function getUID(){
    	runFx(AjaxHelper.getUID);
    }
	
    function loadLastSavedLayout(){
    	runFx(AjaxHelper.getPNamesMod);
    }
