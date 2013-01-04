package gov.ymp.rdc;

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
//import gov.ymp.adminCSI.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.auth.*;
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
    //String nextScript = "home.jsp";
    String nextScript = request.getParameter("nextscript");
    String requestType = request.getParameter("requesttype");
    requestType = (requestType!=null) ? requestType : "frame";
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean validated = false;

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "home.jsp";

    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

    try {

        Context initCtx = new InitialContext();
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        DbConn myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
//System.out.println("rdc/doLogin - Got Here 1" + "-Command: " + command);
        if (command.equals("login") || command.equals("changepassword")) {
            Cookie cookie = null;
            //Get an array of Cookies associated with this domain
            Cookie[] cookies = request.getCookies( );
            String productionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
            String cookieName = "csiSession-" + productionStatus;
            if (cookies != null){
                for (int i = 0; i < cookies.length; i++){
                    if (cookies[i].getName( ).equals(cookieName)){
                        cookie = cookies[i];
                    }
                }//end for
            }//end if
            Person user = new Person(un.toLowerCase());
            validated = user.authenticate(pass, domain, acronym, myConn);
            if (validated && command.equals("changepassword")) {
                String newPassword = request.getParameter("newpass");
                if (newPassword != null && user.getLocalUser().testNewPassword(newPassword, user)) {
                    user.getLocalUser().changePassword(newPassword);
                    user.getLocalUser().save(myConn);
                } else {
                    outLine += "Password not changed, does not meet password guidelines";
                    validated = false;
                }
            }
            if (validated) {
                SessionControler sess = new SessionControler();
                sess.setPerson(user.getID());
                sess.setTcpAddress(request.getRemoteAddr());
                if (user.getLocalUser() != null && user.getLocalUser().getPwExpiresOn().compareTo(new java.util.Date()) < 0) {
                    session.setAttribute("tmp.user.name", user.getUserName());
                    session.setAttribute("tmp.user.id", Long.toString(user.getID()));
                    session.setAttribute("tmp.user.person", user);
                    session.setAttribute("tmp.user.domain", domain);
                    session.setAttribute("tmp.user.isexpired", "T");
                    String appPath = request.getParameter("apppath");
                    outLine += "Password has expired, it must be changed to continue logging in";
                    nextScript = appPath + "/changePassword.jsp";
                    sess.setMustChangePassword(true);
                } else {
                    if (user.getDomain().isLocal()) {
                        Calendar cal = Calendar.getInstance();
                        int today = cal.get(Calendar.YEAR) * 1000 + cal.get(Calendar.DAY_OF_YEAR);
                        cal.setTime(user.getLocalUser().getPwExpiresOn());
                        int expDay = cal.get(Calendar.YEAR) * 1000 + cal.get(Calendar.DAY_OF_YEAR);
                        int daysRemaining = expDay - today;
                        if (daysRemaining < 14) {
                            outLine += "Password will expire in " + daysRemaining + " days ";
                        }
                    }
                    Position pos = new Position(user.getID(), myConn);
                    HashMap perm = Permission.getPermissionMap(myConn);
                    session.setAttribute("user.id", Long.toString(user.getID()));
                    session.setAttribute("user.name", user.getUserName());
                    session.setAttribute("user.fullname", user.getFirstName() + ' ' + user.getLastName());
                    session.setAttribute("user.position", pos);
                    session.setAttribute("user.positionid", Long.toString(pos.getID()));
                    session.setAttribute("user.person", user);
                    session.setAttribute("user.permissionmap", perm);
                    session.setAttribute("user.authenticationlevel", "2");
                    session.setAttribute("user.islocaldomain", ((user.getDomain().isLocal()) ? "T" : "F"));
                    sess.setMustChangePassword(false);
                }
                sess.add(myConn, user.getID());
                cookie = new Cookie(cookieName, "" + sess.getID());
                cookie.setPath("/");
                cookie.setMaxAge(-1);
                response.addCookie(cookie);
                ALog.logActivity(user.getID(), acronym, 4,"User " + user.getFirstName() + ' ' + user.getLastName() + " logged in.");


            } else {
                if (command.equals("changepassword")) {
                    if (outLine.equals("")) {
                        ALog.logError(0, acronym, 4,"Failed password change, old password not correct for " + un);
                        outLine = "Failed password change, old password not correct for " + un;
                    } else {
                        ALog.logError(0, acronym, 4,"Password not changed, does not meet password guidelines for " + un);
                    }
                } else {
                    ALog.logError(0, acronym, 4,"Failed login attempt on username " + un);
                    outLine = "Failed login attempt on username " + un;
                }
            }

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
        out.println("<link rel=\"stylesheet\" href=\"css/prstyle.css\" type=\"text/css\">");
        out.println("<title>Login</title>");
        out.println("</head>");
        out.println("<body leftmargin=0 topmargin=0>");
        out.println("<form name=result method=post>");
        out.println("");
        out.println("<input type=hidden name=command value='" + command + "'>");
        out.println("<script language=\"JavaScript\">\n");
        out.println("<!--\n");
        if (validated) {
            if (requestType.equals("frame")) {
                if (!outLine.equals("")) {
                    out.println("alert('" + outLine + "');\n");
                }
                out.println("parent.document.location='" + nextScript + "';\n");
            } else if (requestType.equals("self")) {
                if (!outLine.equals("")) {
                    out.println("alert('" + outLine + "');\n");
                }
                out.println("document.location='" + nextScript + "';\n");
            } else if (requestType.equals("popup")) {
                out.println("alert('Successful Login');\n");
                out.println("window.close();\n");
            } else if (requestType.equals("other")) {
                out.println("alert('Successful Login');\n");
            }
        } else {
            if (!command.equals("changepassword")) {
                out.println("alert('Username or Password invalid!');\n");
            } else if (!outLine.equals("")) {
                out.println("alert('" + outLine + "');\n");
            } else {
                out.println("alert('Unable to validate credentials');\n");
            }
        }
        out.println("//-->\n");
        out.println("</script>\n");
        out.println("");
        out.println("</form>\n</body>\n</html>");
    }

    out.close();
  }
}
