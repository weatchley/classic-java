<%@page language="java" import="java.lang.*,java.io.*,java.util.*,javax.naming.*,gov.ymp.utils.GetRSSObj" %>
<%
String newText =  "";
String pNum = "";
String rssNum = "";
Context initCtx = new InitialContext();
String pathToRoot = (String) initCtx.lookup("java:comp/env/PathToRoot");
String filePath = "";
String updateType = "";
String sUrl = "";
String sPath = "";
updateType = request.getParameter("updateType");

if(updateType.equals("text")){
	filePath = pathToRoot + "/cached-content/ogp-new/";
	newText = request.getParameter("newText");
	pNum = request.getParameter("pNum");
	String fileName = "portlet"+pNum+".html";
		try {
		    BufferedWriter o = new BufferedWriter(new FileWriter(filePath+fileName));
		    o.write(newText);
		    o.close();
		    out.println("homecontentEcho.jsp: file write successful...");
		} catch (IOException e) {
		}
}else if(updateType.equals("rss")){
	filePath = pathToRoot + "/cached-content/ogp-new/";
	rssNum = request.getParameter("rssNum");
	if(rssNum.equals("0")){ //the crest feed
		sUrl = "https://blogs.ocrwm.doe.gov/crest/feed/entries/rss";
		sPath = filePath + "thecrest.xml";
	}else if(rssNum.equals("1")){ //director feed
		sUrl = "https://blogs.ocrwm.doe.gov/director/feed/entries/rss";
		sPath = filePath + "director.xml";
	}else if(rssNum.equals("2")){ //f.y.i.
		sUrl = "https://blogs.ocrwm.doe.gov/fyi/feed/entries/rss?cat=%2Fcurrent";
		sPath = filePath + "fyi.xml";
	}else{
	//do nothing
	}
	
	GetRSSObj gro = new GetRSSObj();
	gro.save(sUrl,sPath);
}else{
	//do nothing...
}


%>