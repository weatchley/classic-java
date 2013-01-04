<%@page language="java" import="java.lang.*,java.io.*,java.util.*,javax.naming.*" %>
<%
//String origText = ""; 
String newText =  "";
Context initCtx = new InitialContext();
String pathToRoot = (String) initCtx.lookup("java:comp/env/PathToRoot");
String filePath = pathToRoot + "/cached-content/ogp-new/";
String rssUrl = "https://blogs.ocrwm.doe.gov/crest/feed/entries/rss";
String fileName = "thecrest.xml";
//newText = request.getParameter("newText");
    try {
        Process p = Runtime.getRuntime().exec("java gov.ymp.utils.GetRSS "+ rssUrl +" "+filePath+fileName);		
        //log.debug("PROCESS outputstream : " + p.getInputStream() );
		//log.debug("PROCESS errorstream : " + p.getErrorStream());			
		p.waitFor(); // Wait till the process is finished
        
        /*
        BufferedWriter o = new BufferedWriter(new FileWriter(filePath+fileName));
        o.write(newText);
        o.close();
        out.println("homemessageEcho.jsp: file write successful...");
    	*/
    } catch (IOException e) {
    }

%>