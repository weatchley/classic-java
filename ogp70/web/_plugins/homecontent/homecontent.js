function loadPortlets(){
	checkPortletEdit();
}

function checkPortletEdit(){
	if(top.userFullName!="Guest") {
	callAjax('portletEdit', false , false);
	
	}else{
		initPortletContents();
	}
}

function initPortletContents(edit) {
		top.debug("initPortletContents("+edit+") called");
	var pNum = 7; //number of portlets
	for(i=0;i<pNum;i++){
		sUrl = "/cached-content/ogp-new/portlet"+i+".html";
		pullXMLText(sUrl,"portlet"+i,"contentName=portlet"+i);
	}
	if(edit) {
		portletObjId0 = "portlet0Edit";
		portletObjId1 = "portlet1Edit";
		portletObjId2 = "portlet2Edit";
		portletObjId3 = "portlet3Edit";
		portletObjId4 = "portlet4Edit";
		portletObjId5 = "portlet5Edit";		
		portletObjId6 = "portlet6Edit";		
		portletObj0 = document.getElementById(portletObjId0);
		portletObj0.style.display = "block";
		portletObj1 = document.getElementById(portletObjId1);
		portletObj1.style.display = "block";
		portletObj2 = document.getElementById(portletObjId2);
		portletObj2.style.display = "block";
		portletObj3 = document.getElementById(portletObjId3);
		portletObj3.style.display = "block";
		portletObj4 = document.getElementById(portletObjId4);
		portletObj4.style.display = "block";
		portletObj5 = document.getElementById(portletObjId5);
		portletObj5.style.display = "block";
		portletObj6 = document.getElementById(portletObjId6);
		portletObj6.style.display = "block";
		
		rssObjId0 = "rss0Update";		
		rssObj0 = document.getElementById(rssObjId0);
		rssObj0.style.display = "block";
		rssObjId1 = "rss1Update";		
		rssObj1 = document.getElementById(rssObjId1);
		rssObj1.style.display = "block";
		rssObjId2 = "rss2Update";		
		rssObj2 = document.getElementById(rssObjId2);
		rssObj2.style.display = "block";
	}	
}

function portletEdit(pNum){
	//selectPortlet(pName,pURL,top,left,width,height,zIndex,wState)
    selectPortlet('Update Portlet','_plugins/homecontent/homecontentEdit.htm?pNum='+pNum,65,335,455,300,1001,null);	
}

function rssUpdate(rssNum){		
//top.debug("rssUpdate with "+rssNum);
		var url = "_plugins/homecontent/homecontentEcho.jsp";
		var parameters = "rssNum="+ rssNum+"&updateType=rss";
//top.debug(parameters);
	    var ajaxRequest = new window.parent.Ajax.Request(url,
	    {
	           method: 'post',
	           parameters: parameters,
	           onLoading: function() {
	  		 },
	           onSuccess: function(transport){
				 var response = transport.responseText || "no response text";
				 initRSSReadersNew();
				 },
				 onFailure: function(){ window.parent.top.debug('callAjax: Something went wrong...') }
	    }); 	
}

