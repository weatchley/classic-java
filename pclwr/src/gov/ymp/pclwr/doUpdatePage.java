package gov.ymp.pclwr;

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
import gov.ymp.pclwr.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.auth.*;
import gov.ymp.csi.items.*;
import gov.ymp.csi.UNID.*;
import javax.naming.*;
import com.darwinsys.mail.*;


public class doUpdatePage extends HttpServlet {
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
    //String id = request.getParameter("id");
    String text = request.getParameter("text");
    String status = request.getParameter("status");
    // Get the name of the UHash that defines the page
    String pageName = request.getParameter("pagename");
    UHash homePage = null;
    String outLine = "";
    //String nextScript = "home.jsp";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean success = false;
    String userIDs = (String) session.getAttribute("user.id");
    long userID = Long.parseLong(userIDs);

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
    // Determine the script to go to after processing changes
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "home.jsp";

//    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

    DbConn myConn = null;
    try {

//System.out.println("Got Here 1" +"- command: " + command + " id: " + id + " PageID: " + pageName);
        Context initCtx = new InitialContext();
        String ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        // Get the UHash defining the page content
        UHash pageContent = new UHash(pageName, myConn);
        // Get TextItems with published and working content
        TextItem publishedContent = new TextItem(pageContent.get("published"), myConn);
        TextItem workingContent = new TextItem(pageContent.get("working"), myConn);
        String csiSchema = myConn.getSchemaPath();
        // Update the working content with the contents of the submitted form
        if (command.equals("update")) {
            //TextItem ti = new TextItem(Long.parseLong(id), myConn);
            TextItem ti = workingContent;
            ti.setText(text);
            if (status != null && status.compareTo(" ") > 0) {
                ti.setStatus(status);
            } else {
                ti.setStatus(null);
            }
            ti.save(myConn, userID);
            success = true;
            outLine = "";
            ALog.logActivity(userID, "pclwr", 2, ti.getID() + "/\"" + ti.getText() + "\" updated");
        // Request approval via e-mail and set the status to approvalrequested
        } else if (command.equals("approvalrequest")) {
            //TextItem ti = new TextItem(Long.parseLong(id), myConn);
            TextItem ti = workingContent;
            Permission perm = new Permission (myConn, 7, "apphomepage");
            Person [] per = GlobalMembership.getPersonListWithMembership(myConn, perm.getID());
            Mailer mb = new Mailer();
            for (int i=0; i<per.length; i++) {
                mb.addTo(per[i].getEmail());
            }
            String message = "";
            if (!ProductionStatus.toLowerCase().equals("prod") && !ProductionStatus.toLowerCase().equals("production")) {
                message += "Test message, please ignore.\n\n";
            }
            message += "An approval has been requested for the ITWR contact page, please review at:\n";
            ServletContext ctx = getServletContext();
            message += "    " + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + ctx.getServletContextName() + "/home.jsp\n";
            mb.setFrom("intranetwebmaster@ymp.gov");
            mb.setSubject("ITWR-Page approval request");
            mb.setBody(message);
            mb.setServer("gatewaysmtp.ymp.gov");
            mb.doSend();
            ti.setStatus("approvalrequested");
            ti.save(myConn, userID);
            success = true;
            outLine = "";
            ALog.logActivity(userID, "pclwr", 2, ti.getID() + "/\"" + ti.getText() + "\" approval requested");
        // Approve update, set the status to approved and copy approved working content to the published content
        } else if (command.equals("approve")) {
            //TextItem ti = new TextItem(Long.parseLong(id), myConn);
            TextItem ti = workingContent;
            ti.setStatus("approved");
            ti.save(myConn, userID);
            publishedContent.setText(ti.getText());
            publishedContent.save(myConn, userID);
            success = true;
            outLine = "";
            ALog.logActivity(userID, "pclwr", 2, ti.getID() + "/\"" + ti.getText() + "\" updated/approved");
        } else if (command.equals("test")) {
            outLine = "test";
        }

    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "Home page error: '" + outLine + "'");
	    //log(outLine);
    }


    catch (NullPointerException e) {
	    outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "Home page error: '" + outLine + "'");
	    //log(outLine);
    }

    //catch (IOException e) {
 	//    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logError(userID, "csi", 0, "Home page error: '" + outLine + "'");
	//    //log(outLine);
    //}

    //catch (MessagingException e) {
	//    outLine = outLine + "Messaging Exception caught: " + e.getMessage();
    //    ALog.logError(userID, "csi", 0, "Home page error: '" + outLine + "'");
	//    //log(outLine);
    //}
    catch (Exception e) {
        outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "Home page error: '" + outLine + "'");
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
    out.println("<title>Home Page</title>");
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
        out.println("alert('Error processing homePage!');\n");
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
