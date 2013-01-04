package gov.ymp.adminCSI;

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
//import gov.ymp.csi.systems.*;
//import gov.ymp.csi.auth.*;
//import gov.ymp.csi.items.*;
//import gov.ymp.csi.UNID.*;
import javax.naming.*;


public class doDomains extends HttpServlet {
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
    String idS = request.getParameter("id");
    int id = Integer.parseInt(idS);
    String name = request.getParameter("name");
    String description = request.getParameter("description");
    boolean isLocal = (request.getParameter("islocal") != null && request.getParameter("islocal").equals("T")) ? true : false;
    String server = request.getParameter("server");
    String code = request.getParameter("code");
//System.out.println("doDomains - Got Here 1" +"- command: " + command);


    String outLine = "";
    //String nextScript = "domains.jsp";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean success = false;
    String userIDs = (String) session.getAttribute("user.id");
    userIDs = ((userIDs != null) ? userIDs : "0");
    long userID = Long.parseLong(userIDs);

    command = (command != null && command.compareTo(" ") > 0) ? command : "new";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "home.jsp";


    DbConn myConn = null;
    try {

        Context initCtx = new InitialContext();
        String ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        Domains dom = null;
        String csiSchema = myConn.getSchemaPath();
        if (command.equals("update") || command.equals("add")) {
			if (command.equals("update")) {
			    dom = new Domains (id, myConn);
			} else {
				dom = new Domains ();
			}
			dom.setName(name);
			dom.setDescription(description);
			dom.setLocal(isLocal);
			dom.setServer(server);
			dom.setCode(code);
			dom.save(myConn);
            success = true;
            outLine = "";
            ALog.logActivity(userID, "admincsi", 2, "Domains updated.");

        } else if (command.equals("drop")) {

            //success = true;
            //outLine = "item" + temp + " Removed";
            //ALog.logActivity(userID, "admincsi", 3, outLine);
        } else if (command.equals("test")) {
            outLine = "test";
        }

    }


    catch (IllegalArgumentException e) {
        outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logError(userID, "admincsi", 0, "doDomains error: '" + outLine + "'");
        //log(outLine);
    }


    catch (NullPointerException e) {
        outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logError(userID, "admincsi", 0, "doDomains error: '" + outLine + "'");
        //log(outLine);
    }

    //catch (IOException e) {
    //    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logError(userID, "admincsi", 0, "doDomains error: '" + outLine + "'");
    //    //log(outLine);
    //}

    //catch (MessagingException e) {
    //    outLine = outLine + "Messaging Exception caught: " + e.getMessage();
    //    ALog.logError(userID, "admincsi", 0, "doDomains error: '" + outLine + "'");
    //    //log(outLine);
    //}
    catch (Exception e) {
        outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logError(userID, "admincsi", 0, "doDomains error: '" + outLine + "'");
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
    out.println("<title>Domains</title>");
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
        out.println("alert('Error processing doDomains!');\n");
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }


  private String testNull(String text, String def) {
      if (text != null) {
          return text;
      } else {
          return def;
      }
  }



}
