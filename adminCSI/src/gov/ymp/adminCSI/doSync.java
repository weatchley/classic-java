package gov.ymp.adminCSI;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import java.math.*;
import java.awt.*;
import java.lang.*;
import java.text.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import javax.naming.*;

public class doSync extends HttpServlet {
  // Handle the GET HTTP Method
  public void doGet(HttpServletRequest request,
		    HttpServletResponse response)
	 throws IOException {

    String message = "";
    try {
    	String domainName = "ydservices";
    	if(request.getParameter("domainName")!=null){
    	domainName = request.getParameter("domainName");
    	}
    	clSyncResponse("Invalid 'GET' request", "n/a","n/a", "n/a", false, response,domainName);
    	
    }
    finally {
    }
  }

  // Handle the POST HTTP Method
  public void doPost(HttpServletRequest request,
		     HttpServletResponse response)
	 throws IOException {

    String message = "";
    try {
        message = processRequest(request,response);
    }
    finally {
    }
  }

  // Generate the HTML response
  private void clSyncResponse(String outLine, String command, String nextScript, String requestType, boolean validated, HttpServletResponse response, String domainName)
  	  throws IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    //out.println("doSync servlet called!");
	    
	    DbConn myConn = new DbConn("csi");
		String userName = "ServiceCSI";
		int domainID = 0; 
		String server;
		String domainCode;
		boolean isValid = false;
		String password = "";
		if (domainName.equalsIgnoreCase("ydservices")) {
		    server = "ydservices.ymp.gov";
		    password = "zaq1@WSX";
		    domainCode = "YD";
		    domainID = 1;
		} else if (domainName.equalsIgnoreCase("rw.doe.gov")) {
		    server = "rw.doe.gov"; 
				//server = "rwfcd1.rw.doe.gov"; 
				//server = "192.84.216.20"; 
				//server = "192.84.216.48"; 
		    password = "caT&M0u$3g@m3$";
		    domainCode = "HQ";
		    domainID = 2;
		} else if (domainName.equalsIgnoreCase("ymservices")) {
		    server = "ymservices.ymp.gov"; 
		    domainCode = "YM";
		    domainID = 3;
		} else {
		    server = "na"; 
		    domainCode = "na";
		    domainID = 0;
		}
				
		
		out.println("Adding users to CSI for "+ domainName+"<br>");
				try{
					LDAPUtils lu = new LDAPUtils();
					isValid = lu.synchLDAP(userName, password, domainName, server, domainCode, domainID, myConn);
				}catch(Throwable e){
					//e.printStackTrace();
				}		
		/*
		if (!isValid) {
				//out.println(userName+" not validated"+"<br>");
			out.println("*****************************"+"<br>");
		}else{
			out.println("************* LDAPUtils.synchLDAP ended successfully"+"<br>");
		}
		*/
		out.println("************* LDAPUtils.synchLDAP ended successfully"+"<br>");
		//if(isValid){
			out.println("Deleting users from CSI for "+ domainName+"<br>");
			
				try{
					isValid = LDAPUtils.synchCSI(userName, password, domainName, server, domainCode, domainID, myConn);
				}catch(Throwable e){
					//e.printStackTrace();
				}
			/*
			if (!isValid) {
				//out.println(userName+" not validated");
				out.println("*****************************"+"<br>");
			}else{
				out.println("************* LDAPUtils.synchCSI ended successfully for "+domainName+"<br>");
			}
			*/
			out.println("************* LDAPUtils.synchCSI ended successfully for "+domainName+"<br>");
		//}
		
		myConn.release();
    
    out.close();
  }

  // Process the request
  private String processRequest(HttpServletRequest request, HttpServletResponse response) {
	String outLine = "";
    try {
    }catch (Exception e) {
	   outLine = outLine + "Exception caught: " + e.getMessage()+"<br>";
	   //log(outLine);
    }
    finally {
    }
    return outLine;
  }
}
