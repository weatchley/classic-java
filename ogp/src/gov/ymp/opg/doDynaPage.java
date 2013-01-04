package gov.ymp.opg;

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
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.items.*;
import gov.ymp.csi.UNID.*;
import javax.naming.*;


public class doDynaPage extends HttpServlet {
  // Handle the GET HTTP Method
  public void doGet(HttpServletRequest request,
		    HttpServletResponse response)
	 throws IOException {

    String message = "";
    try {
        message = processRequest(request,response);
        //generateResponse("Invalid 'GET' request", "n/a","n/a", false, response);
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
    HttpSession session = request.getSession();
    String userIDs = (String) session.getAttribute("user.id");
    userIDs = (userIDs != null && userIDs.compareTo(" ") > 0) ? userIDs : "0";
    long userID = Long.parseLong(userIDs);
    String command = request.getParameter("command");
    String template = request.getParameter("template");
    String pageHash = request.getParameter("pagehash");
    String pageTitle = request.getParameter("pagetitle");
    String pageDescription = request.getParameter("pagedescription");

    String outLine = "";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    boolean success = false;

//System.out.println("userid=" + userID + ", id=" + id + ", command=" + command);

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "simple1.jsp";

//    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

    DbConn myConn = null;
    try {

        Context initCtx = new InitialContext();
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        //String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        if (userID != 0) {
            if (command.equals("add")) {
                outLine = "";
            } else if (command.equals("update")) {
                outLine = "";
            } else if (command.equals("updatepage")) {
                UHash uPage = new UHash(pageHash, myConn);
//System.out.println("Got Here 1");
                if (template.equals("simple1")) {
                    TextItem title = new TextItem(uPage.get("title"), myConn);
                    title.setText(pageTitle);
                    title.save(myConn);
                    TextItem description = new TextItem(uPage.get("description"), myConn);
                    description.setText(pageDescription);
                    description.save(myConn);
                } else if (template.equals("simple2")) {
                }

            } else if (command.equals("test")) {
                outLine = "test";
            }
            success = true;
        }

    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, pageHash + " error: '" + outLine + "'");
	    //log(outLine);
    }


    catch (NullPointerException e) {
	    outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, pageHash + " error: '" + outLine + "'");
	    //log(outLine);
    }

    //catch (IOException e) {
 	//    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logActivity(userID, "csi", 0, pageHash + " error: '" + outLine + "'");
	//    //log(outLine);
    //}

    catch (Exception e) {
	    outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, pageHash + " error: '" + outLine + "'");
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
    out.println("<title>DynaPages</title>");
    out.println("</head>");
    out.println("<body BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>");
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
        if (outLine.equals("")) {
            out.println("alert('Error saving dynapage!');\n");
        } else {
            out.println("alert('Error saving dynapage: " + outLine + "');\n");
        }
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
