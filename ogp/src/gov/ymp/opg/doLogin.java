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
import gov.ymp.csi.auth.*;
import javax.naming.*;
import java.net.*;


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
    String auto = "0";
    auto = request.getParameter("auto");
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
        //String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        String acronym = "OPG";
        if (command.equals("login")) {
            String docRoot = getServletContext().getRealPath("/");
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
            if(auto.equals("1")){
                //System.out.println("auto logon");
                validated = user.identify(domain, acronym, myConn);
            }else{
                //String testString = getServletConfig().getServletContext().getInitParameter("myicons");
                //System.out.println("myicons:"+testString);
                //System.out.println("manual logon");
                validated = user.authenticate(pass, domain, acronym, myConn);
            }
            if (validated) {
                Position pos = new Position(user.getID(), myConn);
                HashMap perm = Permission.getPermissionMap(myConn);

                //set session attributes...
                session.setAttribute("user.id", Long.toString(user.getID()));
                session.setAttribute("user.name", user.getUserName());
                session.setAttribute("user.fullname", user.getFirstName() + ' ' + user.getLastName());
                //session.setAttribute("user.position", pos);
                session.setAttribute("user.positionid", Long.toString(pos.getID()));
                //session.setAttribute("user.person", user);
                session.setAttribute("user.permissionmap", perm);
                session.setAttribute("user.authenticationlevel", "2");
                SessionControler sess = new SessionControler();
                sess.setPerson(user.getID());
                sess.setTcpAddress(request.getRemoteAddr());
                sess.add(myConn, user.getID());
                cookie = new Cookie(cookieName, "" + sess.getID());
                cookie.setPath("/");
                cookie.setMaxAge(-1);
                response.addCookie(cookie);
                ALog.logActivity(user.getID(), acronym, 4,"User " + user.getFirstName() + ' ' + user.getLastName() + " logged in.");

                //set cookies

                /*
                Cookie userIdCookie =
                    new LongLivedCookie("user.id", Long.toString(user.getID()));
                response.addCookie(userIdCookie);
                Cookie userNameCookie =
                    new LongLivedCookie("user.name", user.getUserName());
                response.addCookie(userNameCookie);
                */

            } else {
                ALog.logActivity(0, acronym, 4,"Failed login attempt on username " + un);
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

  //generating cookies...
  public class LongLivedCookie extends Cookie {
	  public static final int SECONDS_PER_YEAR = 60*60*24*365;

	  public LongLivedCookie(String name, String value) {
	    super(name, value);
	    setMaxAge(SECONDS_PER_YEAR);
	  }
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
        	out.println("");
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
        //out.println("alert('"+requestType+"');");
        out.println("//-->\n");
        out.println("</script>\n");
        out.println("");
        out.println("</form>\n</body>\n</html>");
    }

    out.close();
  }

}
