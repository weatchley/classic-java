package gov.ymp.iats.control;

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
import gov.ymp.iats.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.items.*;
import javax.naming.*;


public class doTextItem extends HttpServlet {
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
    String id = request.getParameter("id");
    String date1 = request.getParameter("date1");
    String date2 = request.getParameter("date2");
    String text = request.getParameter("text");
    String link = request.getParameter("link");
    String status = request.getParameter("status");
    String outLine = "";
    //String nextScript = "index.jsp";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean success = false;
    String userIDs = (String) session.getAttribute("user.id");
    long userID = Long.parseLong(userIDs);

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "textItems.jsp";

//    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

    DbConn myConn = null;
    try {

        Context initCtx = new InitialContext();
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        if (command.equals("add")) {
            TextItem ti = new TextItem();
            if (date1 != null && date1.compareTo(" ") > 0) {
                ti.setDate1(Utils.toDate(date1));
            }
            if (date2 != null && date2.compareTo(" ") > 0) {
                ti.setDate1(Utils.toDate(date2));
            }
            ti.setText(text);
            if (link != null && link.compareTo(" ") > 0) {
                ti.setLink(link);
            }
            if (status != null && status.compareTo(" ") > 0) {
                ti.setStatus(status);
            }
            ti.save(myConn, userID);
            success = true;
            outLine = "";
            ALog.logActivity(userID, "csi", 1, ti.getID() + "/\"" + ti.getText() + "\" added");

        } else if (command.equals("update")) {
            TextItem ti = new TextItem(Long.parseLong(id), myConn);
            if (date1 != null && date1.compareTo(" ") > 0) {
                ti.setDate1(Utils.toDate(date1));
            } else {
                ti.setDate1(null);
            }
            if (date2 != null && date2.compareTo(" ") > 0) {
                ti.setDate2(Utils.toDate(date2));
            } else {
                ti.setDate2(null);
            }
            ti.setText(text);
            if (link != null && link.compareTo(" ") > 0) {
                ti.setLink(link);
            } else {
                ti.setLink(null);
            }
            if (status != null && status.compareTo(" ") > 0) {
                ti.setStatus(status);
            } else {
                ti.setStatus(null);
            }
            ti.save(myConn, userID);
            success = true;
            outLine = "";
            ALog.logActivity(userID, "csi", 2, ti.getID() + "/\"" + ti.getText() + "\" updated");
        } else if (command.equals("drop")) {
            TextItem ti = new TextItem(Long.parseLong(id), myConn);
            String temp = ti.getID() + "/\"" + ti.getText() + "\"";
            ti.drop(myConn, userID);
            success = true;
            outLine = "TextItem " + temp + " Removed";
            ALog.logActivity(userID, "csi", 3, outLine);
        } else if (command.equals("test")) {
            outLine = "test";
        }

    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "TextItem error: '" + outLine + "'");
	    //log(outLine);
    }


    catch (NullPointerException e) {
	    outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "TextItem error: '" + outLine + "'");
	    //log(outLine);
    }

    //catch (IOException e) {
 	//    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logError(userID, "csi", 0, "TextItem error: '" + outLine + "'");
	//    //log(outLine);
    //}

    catch (Exception e) {
	    outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "TextItem error: '" + outLine + "'");
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
    out.println("<title>Text Items</title>");
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
        out.println("alert('Error saving text item!');\n");
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
