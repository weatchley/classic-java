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
import gov.ymp.adminCSI.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import javax.naming.*;


public class doPerson extends HttpServlet {
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
    String userName = request.getParameter("username");
    String domain = request.getParameter("domain");
    int domainID = ((domain != null) ? Integer.parseInt(domain) : 0);
    String firstName = request.getParameter("firstname");
    String lastName = request.getParameter("lastname");
    String email = request.getParameter("email");
    String phone = request.getParameter("phone");
    boolean isActive = ((request.getParameter("isactive") != null && request.getParameter("isactive").equals("T")) ? true : false);
    String outLine = "";
    //String nextScript = "home.jsp";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean success = false;
    String userIDs = (String) session.getAttribute("user.id");
    long userID = Long.parseLong(userIDs);
    Sys [] systems = null;

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "person.jsp";

    DbConn myConn = null;
    try {

        Context initCtx = new InitialContext();
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        systems = Sys.getSystems(myConn);
//System.out.println("doPerson - Got Here 1" +"- command: " + command);
        if (command.equals("add")) {
            Person per = new Person();
            per.setUserName(userName);
            per.setDomainID(domainID);
            per.getInfo(myConn);
            if (per.getID() == 0) {
                per.setFirstName(firstName);
                per.setLastName(lastName);
                per.setEmail(email);
                per.setPhone(phone);
                per.add(myConn, userID);
                String testPass = "";
                while(!LocalUser.testPassword(testPass, userName)) {
                    testPass = LocalUser.genRandPassword(8);
                }
                LocalUser lu = new LocalUser();
                lu.setPersonID(per.getID());
                lu.setActive(true);
                lu.setPassword(jcrypt.crypt("database", testPass));
                lu.setPwExpiresOn(new java.util.Date());
                lu.save(myConn);
                success = true;
                outLine = "User " + userName + " created with initial password of " + testPass;
            } else {
                success = false;
                outLine = "Username: " + userName + " already exists for selected domain";
            }

        } else if (command.equals("update")) {
            Person per = new Person();
            per.getInfo(myConn, Long.parseLong(id));
            per.setFirstName(firstName);
            per.setLastName(lastName);
            per.setEmail(email);
            per.setPhone(phone);
            for (int i=0; i<systems.length; i++) {
                if (systems[i].getExternalSYS()) {
                    String tmp = request.getParameter(("sys-" + systems[i].getID()));
                    tmp = ((tmp != null) && (!tmp.equals("null"))) ? tmp : "none";
                    if (!tmp.equals("none")) {
                        per.setSystemUsername(systems[i], tmp);
                    }
                }
            }
            per.save(myConn, userID);
            per.getLocalUser().setActive(isActive);
            per.getLocalUser().save(myConn);
            //per.save(myConn);
            success = true;
            outLine = "";
        } else if (command.equals("resetpassword")) {
            Person per = new Person();
            per.getInfo(myConn, Long.parseLong(id));
            String newPass = per.getLocalUser().resetPassword();
            per.getLocalUser().save(myConn);

            outLine = "User " + per.getUserName() + " reset to: "+ newPass;
            success = true;
        } else if (command.equals("test")) {
            outLine = "test";
        }

    }


    catch (IllegalArgumentException e) {
        outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "Argument error: '" + outLine + "'");
        //log(outLine);
    }


    catch (NullPointerException e) {
        outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "Null Pointer error: '" + outLine + "'");
        //log(outLine);
    }

    //catch (IOException e) {
    //    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logActivity(userID, "csi", 0, "Permission error: '" + outLine + "'");
    //    //log(outLine);
    //}

    catch (Exception e) {
        outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, "Misc error: '" + outLine + "'");
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
    out.println("<title>Permissions</title>");
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
        out.println("alert('Error saving person!" + ((outLine != null) ? " " + outLine : "") + "');\n");
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
