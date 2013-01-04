package gov.ymp.opg;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

import gov.ymp.csi.session.*;

public class doSessionTrack extends HttpServlet{
	public void doGet(HttpServletRequest request,
						HttpServletResponse response)
		throws ServletException, IOException{
		String sUrl = request.getParameter("sUrl");
		String uname = request.getParameter("userName");
		String lTime = "";
		String lString = "";
				
		
		//date format section
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		String DATE_FORMAT = "dd/MMM/yyyy:HH:mm:ss";
		//String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		java.text.SimpleDateFormat sdf = 
			new java.text.SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getDefault());
		lTime = sdf.format(cal.getTime());
		
		//http attributes
		String ServerName = request.getServerName(); //i.e. ydappdev.ocrwm.doe.gov
		String ServerSoftware = getServletContext().getServerInfo(); //i.e. Apache Tomcat/5.5.25
		String ServerCintextName = getServletContext().getServletContextName(); //i.e. ogp66
		String ServerProtocol = request.getProtocol(); //i.e. HTTP/1.1
		String ServerScheme = request.getScheme(); //i.e. https
		int ServerPort = request.getServerPort(); //i.e. 443
		String RequestMethod = request.getMethod(); //i.e. GET
		String PathInfo = request.getPathInfo(); //i.e. 
		String PathTranslated = request.getPathTranslated(); //i.e. 
		String ScriptName = request.getServletPath() + "<br>"; //i.e. /doSessiontrack
		String DocumentRoot = getServletContext().getRealPath("/"); //i.e. 
		String QueryString = request.getQueryString(); //i.e. 
		String RemoteHost = request.getRemoteHost(); //i.e. 
		String RemoteAddr = request.getRemoteAddr(); //i.e. browser ip
		String AuthType = request.getAuthType(); //i.e. 
		String RemoteUser = request.getRemoteUser(); //i.e. 
		String ContentType = request.getContentType(); //i.e. 
		int ContentLength = request.getContentLength(); //i.e. 
		String HTTPAccept = request.getHeader("Accept"); //i.e. 
		String HTTPUserAgent = request.getHeader("User-Agent"); //i.e. 
		String HTTPReferer = request.getHeader("Referer"); //i.e. 
		
		//parse out unnecessary sections for internal links
		//
		//java.net.url attributes
		//sample url -> http://java.sun.com:80/docs/books/tutorial/index.html?name=networking#DOWNLOADING
		//protocol = http
		//authority = java.sun.com:80
		//host = java.sun.com
		//port = 80
		//path = /docs/books/tutorial/index.html
		//query = name=networking
		//filename = /docs/books/tutorial/index.html?name=networking
		//ref = DOWNLOADING
		
		String oUrl = "";
		
		URL aURL = new URL(sUrl);
		String protocol = aURL.getProtocol();
		String authority = aURL.getAuthority();
		String host = aURL.getHost();
		int port = aURL.getPort();
		String path = aURL.getPath();
		String query = aURL.getQuery();
		String filename = aURL.getFile();
		String ref = aURL.getRef();
		
		if(ServerName.equals(host)){
			oUrl = path;
		}else{
			oUrl = sUrl;
		}
		
		//sample data format
		//192.12.95.123 - - [10/Mar/2008:00:00:01 -0700] "GET /status-5028/status?XML=true HTTP/1.0" - - 
		// ^ request (properly formatted)
		//format the string correctly in apache access log style
		lString = RemoteHost +" - - ["+lTime+" -0700] \""+RequestMethod+" "+oUrl+" "+ServerProtocol+"\"";
		

		// i/o write a log entry
		// ** control log i/o location in web.xml
		
		/*
		String fPath = null;
		fPath =  createLogFileName("D:\\workspace\\gDev\\config\\gsm.properties"); //to do: read this off externally - sh
			//System.out.println("fPath: "+fPath);
		Ioop ioop = new Ioop(fPath,lString);		
		*/
				
		//commandline / log print
			//System.out.println("doSessionTrack -> "+ lString);
		
		//html print
			//response.setContentType("text/html");
			//PrintWriter out = response.getWriter();
			//out.println("doSessionTrack -> "+ lString);
	}	
		
	private static String createLogFileName(String pPath){
		String pName=null;
		String dName=null;
		String fName=null;
		Properties props = new Properties();
		props = readProps(pPath);
		dName = props.getProperty("log.path");
		fName = props.getProperty("log.filename");
			// apache-gov.doe.ocrwm.ocrwmstageway-access_log-ydappdev-20080321
			// apache-ocrwmgateway-gsm-access_log-20080321
			// date format section
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		String DATE_FORMAT = "yyyyMMdd";
			// String DATE_FORMAT = "dd/MMM/yyyy:HH:mm:ss";
			// String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		String lTime = null;
		java.text.SimpleDateFormat sdf = 
		new java.text.SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getDefault());
		lTime = sdf.format(cal.getTime());
		//pName = dName+"/"+"apache-ocrwmgateway-gsm-access_log-"+lTime;
		pName = dName+"\\"+fName+lTime;
			//System.out.println("pName: "+pName);
		return pName;
	}
	
	private static Properties readProps(String pPath){
	    // Read properties file.
	    Properties properties = new Properties();
	    try {
	        properties.load(new FileInputStream(pPath));
	    } catch (IOException e) {
	    }	   
	    return properties;
	}
	
	private static String formatDateString(){
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		String DATE_FORMAT = "dd/MMM/yyyy:HH:mm:ss";
		String lTime = null;
		java.text.SimpleDateFormat sdf = 
		new java.text.SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getDefault());
		lTime = sdf.format(cal.getTime());
		return lTime;
	}
	
}