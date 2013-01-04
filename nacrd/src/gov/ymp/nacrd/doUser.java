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


public class doUser extends HttpServlet {
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
    String uname = request.getParameter("username");
    String docRoot = getServletContext().getRealPath("/");
    HttpSession session = request.getSession();
    User user = new User(uname);
    int rUserID = Integer.parseInt((String) session.getAttribute("user.id"));
    String nextScript = "home.jsp";
    String outLine = "";
    OutputStream toClient;
    boolean validated = false;

//System.out.println("Got here douser, Command: " + command);
    command = (command != null && command.compareTo(" ") > 0) ? command : "form";

    try {

        if (command.equals("changepassword")) {
            String oldPass = request.getParameter("oldpass");
            String newPass1 = request.getParameter("newpass1");
            String newPass2 = request.getParameter("newpass2");
            String testPass = jcrypt.crypt ("da", newPass1);
            if (!user.verifyPassword(oldPass)) {
                nextScript = "none";
                outLine = "Old Password is invalid";
            } else if (!newPass1.equals(newPass2)) {
                nextScript = "none";
                outLine = "New Password entries do not match";
            } else if (oldPass.equals(newPass1)) {
                nextScript = "none";
                outLine = "New Password Matches Old Password";
            } else if (!user.testPassword(newPass1)) {
                nextScript = "none";
                outLine = "New Password does not meet security requirements";
            } else {
                nextScript = "home.jsp";
                outLine = "Password Successfully Changed.";
                user.setPassword(testPass);
                Date dNow = new Date();
                user.setDatePWExpires(Utils.dateToString(Utils.addDays(dNow, 180)));
                user.updatePassword();
                ALog.logActivity(user.getUserID(), "Password Changed.");
            }

        } else if (command.equals("resetpassword")) {
            user.resetPassword();
            nextScript = "home.jsp";
            outLine = "Password Reset for " + uname;
            ALog.logActivity(rUserID, outLine);
        } else if (command.equals("updateuser")) {
            user.setFullName(request.getParameter("fullname"));
            user.setOrg(request.getParameter("org"));
            String tmpStatus = request.getParameter("status");
            if (tmpStatus.equals("enabled")) {
                user.enable();
            } else {
                user.disable();
            }
            user.update();
            nextScript = "home.jsp";
            outLine = "User " + uname + " updated";
            ALog.logActivity(rUserID, outLine);
        } else if (command.equals("adduser")) {
            user.setFullName(request.getParameter("fullname"));
            user.setOrg(request.getParameter("org"));
            String tmpStatus = request.getParameter("status");
            if (tmpStatus.equals("enabled")) {
                user.enable();
            } else {
                user.disable();
            }
            if (user.getUserID() != 0) {
                nextScript = "none";
                outLine = "Username " + uname + " already in use";
            } else {
                user.setUsername(uname);
                user.add();
                nextScript = "home.jsp";
                outLine = "User " + uname + " added";
                ALog.logActivity(rUserID, outLine);
            }
        } else if (command.equals("test")) {
            outLine = "test";
        }

        generateResponse(outLine, command, nextScript, validated, response);

    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
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

    if (outLine != null) {
        out.println("alert('" + outLine + "');\n");
    }

    if (!nextScript.equals("none")) {
        out.println("parent.document.location='" + nextScript + "';\n");
    }

    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
