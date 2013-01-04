package gov.ymp.rpms;

import javax.servlet.*;
import javax.servlet.http.*;
// Support classes
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import java.math.*;
import java.awt.*;
import java.lang.*;
import java.text.*;
import gov.ymp.rpms.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.auth.*;
import gov.ymp.csi.items.*;
import gov.ymp.csi.UNID.*;
import javax.naming.*;
import gov.ymp.rpms.model.*;


public class doRetentionCode extends HttpServlet {
  // Handle the GET HTTP Method
  public void doGet(HttpServletRequest request,
		    HttpServletResponse response)
	 throws IOException {

    String message = "";
    try {
        //message = processRequest(request,response);
        generateResponse("Invalid 'GET' request", "n/a","n/a", false, response);
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


  // Process the request
  private String processRequest(HttpServletRequest request, HttpServletResponse response) {
    String command = request.getParameter("command");
    String code = request.getParameter("code");
    String description = request.getParameter("description");
    String outLine = "";
    //String nextScript = "home.jsp";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean success = false;
    String userIDs = (String) session.getAttribute("user.id");
    long userID = Long.parseLong(userIDs);

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "home.jsp";

//    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

    DbConn myConn = null;
    try {

        Context initCtx = new InitialContext();
        String ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        if (command.equals("add")) {
            RetentionCode item = new RetentionCode();
//System.out.println("Got Here 1" +"- command: " + command + ", code: " + code + ", Description: " + description);
            item.setCode(code);
            item.setDescription(description);
            item.save(myConn, userID);
            success = true;
            outLine = "";
            ALog.logActivity(userID, "rpms", 1, "RetentionCode " + item.getCode() + "/\"" + HtmlUtils.getDisplayString(item.getDescription(), 100) + "\" added");

        } else if (command.equals("update")) {
            RetentionCode item = new RetentionCode(code, myConn);
            item.setDescription(description);
            item.save(myConn, userID);
            success = true;
            outLine = "";
            ALog.logActivity(userID, "rpms", 2, item.getCode() + "/\"" + HtmlUtils.getDisplayString(item.getDescription(), 100) + "\" updated");
        } else if (command.equals("drop")) {
            RetentionCode item = new RetentionCode(code, myConn);
            String temp = item.getCode() + "/\"" + HtmlUtils.getDisplayString(item.getDescription(), 100) + "\"";
            item.drop(myConn, userID);

            success = true;
            outLine = "Access Restriction " + temp + " Removed";
            ALog.logActivity(userID, "rpms", 3, outLine);
        } else if (command.equals("test")) {
            outLine = "test";
        }

    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "RetentionCode error: '" + outLine + "'");
	    //log(outLine);
    }


    catch (NullPointerException e) {
	    outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "RetentionCode error: '" + outLine + "'");
	    //log(outLine);
    }

    //catch (IOException e) {
 	//    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logError(userID, "csi", 0, "RetentionCode error: '" + outLine + "'");
	//    //log(outLine);
    //}

    //catch (MessagingException e) {
	//    outLine = outLine + "Messaging Exception caught: " + e.getMessage();
    //    ALog.logError(userID, "csi", 0, "RetentionCode error: '" + outLine + "'");
	//    //log(outLine);
    //}
    catch (Exception e) {
        outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "RetentionCode error: '" + outLine + "'");
        //log(outLine);
    }
    finally {
        try { generateResponse(outLine, command, nextScript, success, response); } catch (Exception i) {}

        myConn.release();
	   //log("Test log message\n");
    }


    return outLine;

  }

  // Generate the HTML response
  private void generateResponse(String outLine, String command, String nextScript, boolean success, HttpServletResponse response)
	  throws IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    out.println("<html>");
    out.println("<head>");
    out.println("  <LINK href=\"css/styles.css\" type=text/css rel=STYLESHEET>");
    out.println("<title>Access Restriction</title>");
    out.println("</head>");
    out.println("<body BGCOLOR=#FFFFFF leftmargin=0 topmargin=0>");
    out.println("<form name=result method=post>");
    out.println("");
    out.println("<input type=hidden name=command value='" + command + "'>");
    out.println("<script language=\"JavaScript\">\n");
    out.println("<!--\n");
    if (success) {
        if (!outLine.equals("")) {
            out.println("alert('" + outLine + "');\n");
        }
        out.println("parent.document.location='" + nextScript + "';\n");
    } else {
        out.println("alert('Error processing Access Restriction!');\n");
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
