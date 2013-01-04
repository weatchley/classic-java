package gov.ymp.nacrd;

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
import gov.ymp.nacrd.*;
import gov.ymp.util.*;
import gov.ymp.util.db.*;


public class doLogin extends HttpServlet {
  // Handle the GET HTTP Method
  public void doGet(HttpServletRequest request,
		    HttpServletResponse response)
	 throws IOException {

    String message = "";
    try {
        message = processRequest(request,response);
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
    String inputstring = request.getParameter("inputstring");
    String outLine = "";
    String nextScript = "home.jsp";
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean validated = false;

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";

    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

    try {

        if (command.equals("login")) {
            String docRoot = getServletContext().getRealPath("/");
            User user = new User(un.toLowerCase());
            validated = user.verifyPassword(pass);
            if (validated) {
                session.setAttribute("user.id", Integer.toString(user.getUserID()));
                session.setAttribute("user.name", user.getUsername());
                session.setAttribute("user.fullname", user.getFullName());
                session.setAttribute("user.org", user.getOrg());
                Date now = new Date();
                String testDate = Utils.dateToCompareString(now);
                String pwExp = Utils.dateToCompareString(Utils.toDate(user.getDatePWExpires()));
                ALog.logActivity(user.getUserID(),"User " + user.getFullName() + " logged in.");
                if (testDate.compareTo(pwExp) > 0) {
                    nextScript = "changePass.jsp";
                }
            } else {
                ALog.logActivity(0,"Failed login attempt on username " + un);
            }
            //outLine = "test";
            String testPass = jcrypt.crypt ("da", pass);
            //outLine = "[" + user.getUsername() + ", " + user.getOrg() + ", " + user.getPassword() + ", " + testPass + "]";
            outLine = "";

        } else if (command.equals("test")) {
            outLine = "test";
        } else if (command.equals("test")) {
            outLine = "test";
        }

        generateResponse(outLine, command, nextScript, validated, response);

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
  private void generateResponse(String outLine, String command, String nextScript, boolean validated, HttpServletResponse response)
	  throws IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    out.println("<html>");
    out.println("<head>");
    out.println("<title>Login</title>");
    out.println("</head>");
    out.println("<body BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>");
    out.println("<form name=result method=post>");
    out.println("");
    out.println("<input type=hidden name=command value='" + command + "'>");
    out.println("<script language=\"JavaScript\">\n");
    out.println("<!--\n");
    if (validated) {
        out.println("parent.document.location='" + nextScript + "';\n");
    } else {
        out.println("alert('Username or Password invalid!');\n");
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
