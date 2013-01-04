<%@ page import="java.lang.*,java.io.*,java.util.*" %>
<%@ page import="java.net.URL"%>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="com.sun.syndication.feed.synd.SyndFeed" %>
<%@ page import="com.sun.syndication.feed.synd.SyndEntry" %>
<%@ page import="com.sun.syndication.io.SyndFeedInput" %>
<%@ page import="com.sun.syndication.io.XmlReader" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="org.jdom.Element" %>

<%
String feedName = "crest";
String urlStr = "https://blogs.ocrwm.doe.gov/crest/feed/entries/rss";
String sBase = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
//String sBase = "https://ydappdev.ocrwm.doe.gov";
//String sBase = "https://ws-higashis2.ydservices.ymp.gov:8443";
//System.out.println(sBase);
Boolean pref = false;
 int maxNum = 99;
boolean ok = false;
if (request.getParameter("feedName")!=null) {
	feedName = request.getParameter("feedName");
}
if(feedName.equals("thecrest")){
maxNum = 3;
}else if(feedName.equals("thecrestpreview")){
maxNum = 3;
pref = true;
urlStr = sBase + "/cached-content/ogp-new/thecrest.xml";
}else if(feedName.equals("ocrwmwhatsnew")){
urlStr = "http://www.ocrwm.doe.gov/rss.xml";
maxNum = 3;
}else if(feedName.equals("director")){
//urlStr = "https://blogs.ocrwm.doe.gov/director/feed/entries/rss";
urlStr = sBase + "/cached-content/ogp-new/director.xml";
maxNum = 0;
pref = true;
}else if(feedName.equals("fyi")){
//urlStr = "https://blogs.ocrwm.doe.gov/fyi/feed/entries/rss?cat=%2Fcurrent";
urlStr = sBase + "/cached-content/ogp-new/fyi.xml";
pref = true;
}

%>
<%
try {	
    URL feedUrl = new URL(urlStr);
    SyndFeedInput input = new SyndFeedInput();
    SyndFeed feed = input.build(new XmlReader(feedUrl));
    Date dateObj;
    String sDescription="";
    List<SyndEntry> entries = feed.getEntries();
    int i = 0;
    for (SyndEntry entry : entries) {
    	if (i>maxNum) break;    	
    	List<Element> foreignMarkups = (List) entry.getForeignMarkup();
			for (Element foreignMarkup : foreignMarkups) {
				sDescription = foreignMarkup.getValue();
			}			
    	String sTitle = entry.getTitle();
    	int maxLength = 50;
    	if (sTitle.length() > maxLength) sTitle = sTitle.substring(0, maxLength)+"...";
    		dateObj = entry.getPublishedDate();    	
    	if(feedName.equals("director")){
    		out.println("<!--img src='/director/images/director-head.jpg'  align=right hspace=0 vspace=0-->");
    		sDescription = sDescription.replace("'","\'");
    		sDescription = sDescription.replace("\"","\\\"");
    		out.println("»&nbsp;<a title='"+sDescription+"' target=_blank href='"+entry.getLink()+"'>"+sTitle+"</a><br>"); 
    	}else{
    		String sTitleTitle = sTitle.replace("'","\'");
    		sTitleTitle = sTitle.replace("\"","\\\"");
    		//out.println("»&nbsp;<a title='"+sDescription+"' target=_blank href='"+entry.getLink()+"'>"+sTitle+"</a>&nbsp; ("+DateFormat.getInstance().format(dateObj)+")<br>"); 
    		out.println("»&nbsp;<a title='"+sTitleTitle+"' target=_blank href='"+entry.getLink()+"'>"+sTitle+"</a>&nbsp; ("+DateFormat.getInstance().format(dateObj)+")<br>"); 
    	}    	
    	if(pref){ //show preview
	    	if(feedName.equals("fyi")){
	    		out.println(sDescription+"<br><br>"); 
	    	}else{
	    		String sTitleTitle = sTitle.replace("'","\'");
	    		sTitleTitle = sTitle.replace("\"","\\\"");
	    		//out.println(sDescription+"(<a title='"+sDescription+"' target=_blank href='"+entry.getLink()+"'>more</a>)<br><br>"); 
	    		out.println(sDescription+" (<a title='"+sTitleTitle+"' target=_blank href='"+entry.getLink()+"'>more</a>)<br><br>"); 
	    	}
    	}
    	i++;
    }ok = true;
}
catch (Exception ex) {
    ex.printStackTrace();
    System.out.println("ERROR: "+ex.getMessage());
}

if (!ok) {
    System.out.println();
    System.out.println("FeedReader reads and prints any RSS/Atom feed type.");
    System.out.println("The first parameter must be the URL of the feed to read.");
    System.out.println();
}
%>

