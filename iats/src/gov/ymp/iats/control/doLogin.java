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
import javax.naming.*;


public class doLogin extends HttpServlet {
  // Handle the GET HTTP Method
  public void doGet(HttpServletRequest request,
		    HttpServletResponse response)
	 throws IOException {

    String message = "";
    try {
        //message = processRequest(request,response);
        generateResponse("Invalid 'GET' request", "n/a","n/a", "n/a", false, response);
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
    String un = request.getParameter("username");
    String pass = request.getParameter("pass");
    String domain = request.getParameter("domain");
    String inputstring = request.getParameter("inputstring");
    String outLine = "";
    //String nextScript = "index.jsp";
    String nextScript = request.getParameter("nextscript");
    String requestType = request.getParameter("requesttype");
    requestType = (requestType!=null) ? requestType : "frame";
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean validated = false;

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "index.jsp";

    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

    try {

        Context initCtx = new InitialContext();
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        DbConn myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        if (command.equals("login")) {
            String docRoot = getServletContext().getRealPath("/");
            Person user = new Person(un.toLowerCase());
            validated = user.authenticate(pass, domain, acronym, myConn);
            if (validated) {
                Position pos = new Position(user.getID(), myConn);
                HashMap perm = Permission.getPermissionMap(myConn);
                session.setAttribute("user.id", Long.toString(user.getID()));
                session.setAttribute("user.name", user.getUserName());
                session.setAttribute("user.fullname", user.getFirstName() + ' ' + user.getLastName());
                session.setAttribute("user.position", pos);
                session.setAttribute("user.positionid", Long.toString(pos.getID()));
                session.setAttribute("user.person", user);
                session.setAttribute("user.permissionmap", perm);
                //ALog.logActivity(user.getID(), acronym, 4,"User " + user.getFirstName() + ' ' + user.getLastName() + " logged in.");


            } else {
                //ALog.logActivity(0, acronym, 4,"Failed login attempt on username " + un);
            }
            outLine = "";

        } else if (command.equals("test")) {
            outLine = "test";
        } else if (command.equals("test")) {
            outLine = "test";
        }

        generateResponse(outLine, command, nextScript, requestType, validated, response);

        myConn.release();
    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
	    log(outLine);
    }


    catch (NullPointerException e) {
	   outLine = outLine + "NullPointerException caught: " + e.getMessage();
	   log(outLine);
    }

    catch (IOException e) {
	   outLine = outLine + "IOException caught: " + e.getMessage();
	   log(outLine);
    }

    catch (Exception e) {
	   outLine = outLine + "Exception caught: " + e.getMessage();
	   log(outLine);
    }
    finally {
	   //log("Test log message\n");
    }


    return outLine;

  }

  // Generate the HTML response
  private void generateResponse(String outLine, String command, String nextScript, String requestType, boolean validated, HttpServletResponse response)
	  throws IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    if (requestType.equals("simple")) {
        if (validated) {
            out.println("success");
        } else {
            out.println("failed");
        }
    } else {
        out.println("<html>");
        out.println("<head>");
        out.println("  <LINK href=\"css/styles.css\" type=text/css rel=STYLESHEET>");
        out.println("<title>Login</title>");
        out.println("</head>");
        out.println("<body BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>");
        out.println("<form name=result method=post>");
        out.println("");
        out.println("<input type=hidden name=command value='" + command + "'>");
        out.println("<script language=\"JavaScript\">\n");
        out.println("<!--\n");
        if (validated) {
            if (requestType.equals("frame")) {
                out.println("parent.document.location='" + nextScript + "';\n");
            } else if (requestType.equals("self")) {
                out.println("document.location='" + nextScript + "';\n");
            } else if (requestType.equals("popup")) {
                out.println("alert('Successful Login');\n");
                out.println("window.close();\n");
            } else if (requestType.equals("other")) {
                out.println("alert('Successful Login');\n");
            }
        } else {
            out.println("alert('Username or Password invalid!');\n");
        }
        out.println("//-->\n");
        out.println("</script>\n");
        out.println("");
        out.println("</form>\n</body>\n</html>");
    }

    out.close();
  }
}
