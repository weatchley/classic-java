<jsp:useBean id="HtmlGrab" class="gov.ymp.csi.shuzi.HtmlGrab" scope="page" />
<%--@ page import="gov.ymp.csi.shuzi.*" --%>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import = "java.lang.*"%>
<%
   String tString = "";
   String tUrl = request.getParameter("tUrl");
   HtmlGrab.doGrab(tUrl);
   tString = HtmlGrab.getvReturn();
   tString = replace(tString,"</head>","<base href=\""+tUrl+"\" target=\"_blank\"></head>");
   //tString = replace(tString,"<body ","<body onClick=\"javaScript:mouseEventHandler(event);\" ");
   tString = replace(tString,"<body ","<body onClick=\"javaScript:captureLink(event);\" ");
   tString = replace(tString,"</body>","<DIV ID=\"linkPopup\" STYLE=\"position: absolute; top: 0; left: 0; width:300px; z-index: 9000; text-decoration: none; visibility:hidden;\"><TABLE BGCOLOR=\"#CCCC00\" BORDER=1 bordercolor=\"#888888\"><TR><TD STYLE=\"color:white;\" ID=\"linkPopupText\">DOM Sniffing...</TD></TR></TABLE></DIV>");
   
	//publish in XML
	response.setContentType("text/html");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("pragma","no-cache");	
	response.getWriter().write(tString);	
%>
<script language="javascript">
//new functions 070606
	function captureLink(mEvent){
				var mEvent = mEvent || window.event;
				if (mEvent.srcElement){
				mElement = mEvent.srcElement;
				}else{
				mElement = mEvent.target;
				}
				var href="";
				if (mElement.nodeName=="A") href+=mElement.href;	
				if((href!="")&&(href!="#")){
					window.parent.debugThing("hyperlink found: "+href);
					window.parent.winChooserNew('HTML Cache','hM'+window.parent.winnum,0,1,href);
					if(mEvent.preventDefault)mEvent.preventDefault();	//to cancel out default event in firefox
					mEvent.returnValue = false; //to cancel out default event in ie
				}
	}

/*
// retrieving elements under mouse
	function mouseEventHandler(mEvent)
	{
		// Internet Explorer
		if (mEvent.srcElement)
		{
			showPopup(mEvent, 'linkPopup', mEvent.srcElement);
		}
		// Netscape and Firefox
		else if (mEvent.target)
		{
			showPopup(mEvent, 'linkPopup', mEvent.target);
		}
	}
	function showPopup(myEvent, id, myElement)
	{
		var popup=getPopupObject(id);
			if (popup)
			{	
				popup.style.visibility='hidden';	
				myEvent = myEvent || window.event;
				var x=myEvent.clientX;
				var y=myEvent.clientY;
				x=parseInt(myEvent.clientX+document.body.scrollLeft);
				y=parseInt(myEvent.clientY+document.body.scrollTop);
				var popupText=getPopupObject(id+"Text");
				var href="";
				if (myElement.nodeName=="A") href+=myElement.href;	
				if((href!="")&&(href!="#")){
					window.parent.debugThing("hyperlink found: "+href);
					window.parent.winChooserNew('HTML Cache','hM'+window.parent.winnum,0,1,href);
					myEvent.returnValue = false;
				}
				popupText.firstChild.nodeValue=text;
				popup.style.left=x;
				popup.style.top=y;
				popup.style.visibility='visible';
			}
	}
	function getPopupObject(myId)
	{
	   if (document.getElementById(myId))
	   {
	     return document.getElementById(myId);
	   }
	   else
	   {
	     return window.document[myId]; 
	   }     
	}
	*/
</script>

<%!
private String replace(String str, String problemStr, String replace)


    {	for(int i=str.lastIndexOf(problemStr); i>=0; i=str.lastIndexOf(problemStr, i-1))
    		if(i==0) str = replace+str.substring(i+1, str.length());
    			else str = str.substring(0, i)+replace+str.substring(i+problemStr.length(), str.length());
    				return str;}
%>
