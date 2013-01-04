package gov.ymp.rpms;

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
import gov.ymp.rpms.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.auth.*;
import gov.ymp.csi.items.*;
import gov.ymp.csi.UNID.*;
import javax.naming.*;
import com.darwinsys.mail.*;
import org.json.*;


public class doFAQ extends HttpServlet {
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
    String question = request.getParameter("question");
    String answer = request.getParameter("answer");
    String status = request.getParameter("status");
    // Get the name of the UHash that defines the page
    String faqName = request.getParameter("faqname");
    UHash faq = null;
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

    DbConn myConn = null;
    try {

//System.out.println("Got Here 1" +"- command: " + command + " id: " + id + " faqName: " + faqName);
        Context initCtx = new InitialContext();
        String ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        // Create a new newsletter item with the data from the form and add it to the list of newsletters
        if (command.equals("add")) {
            TextItem ti = new TextItem();
            JSONObject jo = new JSONObject();
            jo.put("question", question);
            jo.put("answer", answer);
            ti.setText(jo.toString());
            ti.save(myConn, userID);
            success = true;
            outLine = "";
            faq = new UHash(faqName, myConn);
            UList ul = new UList(faq.get("list"), myConn);
            ul.addItem(ti.getID());
            ul.save(myConn, userID);
            ALog.logActivity(userID, "rpms", 1, ti.getID() + "/\"" + ti.getText() + "\" added");

        // Update the content with the contents of the submitted form and clear the status
        } else if (command.equals("update")) {
            TextItem ti = new TextItem(Long.parseLong(id), myConn);
            JSONObject jo = new JSONObject();
            jo.put("question", question);
            jo.put("answer", answer);
            ti.setText(jo.toString());
            ti.setStatus("edited");
            ti.save(myConn, userID);
            success = true;
            outLine = "";
            ALog.logActivity(userID, "rpms", 2, ti.getID() + "/\"" + ti.getText() + "\" updated");
        // delete the selected item
        } else if (command.equals("drop")) {
            TextItem ti = new TextItem(Long.parseLong(id), myConn);
            JSONObject jo = new JSONObject(ti.getText());
            String temp = ti.getID() + "/\"" + jo.getString("question") + "\"";
            faq = new UHash(faqName, myConn);
            UList ul = new UList(faq.get("list"), myConn);
            ul.dropItem(ti.getID());
            ul.save(myConn, userID);
            ti.drop(myConn, userID);

            success = true;
            outLine = "TextItem " + temp + " Removed";
            ALog.logActivity(userID, "rpms", 3, outLine);
        // Request approval via e-mail and set the status to approvalrequested
        } else if (command.equals("approvalrequest")) {
            TextItem ti = new TextItem(Long.parseLong(id), myConn);
            Permission perm = new Permission (myConn, 7, "appfaq");
            Person [] per = GlobalMembership.getPersonListWithMembership(myConn, perm.getID());
            Mailer mb = new Mailer();
            for (int i=0; i<per.length; i++) {
                mb.addTo(per[i].getEmail());
            }
            String message = "";
            if (!ProductionStatus.toLowerCase().equals("prod") && !ProductionStatus.toLowerCase().equals("production")) {
                message += "Test message, please ignore.\n\n";
            }
            JSONObject jo = new JSONObject(ti.getText());
            message += "An approval has been requested for faq: " + jo.getString("question") + ", please review at:\n";
            ServletContext ctx = getServletContext();
            message += "    " + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + ctx.getServletContextName() + "/faq.jsp\n";
            mb.setFrom("intranetwebmaster@ymp.gov");
            mb.setSubject("RPMS-FAQ approval request");
            mb.setBody(message);
            mb.setServer("gatewaysmtp.ymp.gov");
            mb.doSend();
            ti.setStatus("approvalrequested");
            ti.save(myConn, userID);
            success = true;
            outLine = "";
            ALog.logActivity(userID, "rpms", 2, ti.getID() + "/\"" + ti.getText() + "\" approval requested");
        // Approve update, set the status to approved and copy approved working content to the published content
        } else if (command.equals("approve")) {
            TextItem ti = new TextItem(Long.parseLong(id), myConn);
            success = true;
            ti.setStatus("approved");
            ti.save(myConn, userID);
            success = true;
            outLine = "";
            ALog.logActivity(userID, "rpms", 2, ti.getID() + "/\"" + ti.getText() + "\" updated/approved");
        // Move the selected item up one in the list
        } else if (command.equals("moveup")) {
            faq = new UHash(faqName, myConn);
            long lid = Long.parseLong(id);
            UList ul = new UList(faq.get("list"), myConn);
            ul.moveItemUp(lid, myConn);
            ul.save(myConn, userID);
            outLine = "";
            success = true;
            ALog.logActivity(userID, "rpms", 2, "FAQ " + id + " moved up");
        // Move the selected item to the top of the list
        } else if (command.equals("movetotop")) {
            faq = new UHash(faqName, myConn);
            long lid = Long.parseLong(id);
            UList ul = new UList(faq.get("list"), myConn);
            ul.moveItemToTop(lid, myConn);
            ul.save(myConn, userID);
            outLine = "";
            success = true;
            ALog.logActivity(userID, "rpms", 2, "FAQ " + id + " moved to top");
        } else if (command.equals("test")) {
            outLine = "test";
        }

    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "FAQ error: '" + outLine + "'");
	    //log(outLine);
    }


    catch (NullPointerException e) {
	    outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "FAQ error: '" + outLine + "'");
	    //log(outLine);
    }

    //catch (IOException e) {
 	//    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logError(userID, "csi", 0, "FAQ error: '" + outLine + "'");
	//    //log(outLine);
    //}

    //catch (MessagingException e) {
	//    outLine = outLine + "Messaging Exception caught: " + e.getMessage();
    //    ALog.logError(userID, "csi", 0, "FAQ error: '" + outLine + "'");
	//    //log(outLine);
    //}
    catch (Exception e) {
        outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "FAQ error: '" + outLine + "'");
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
    out.println("<title>FAQ</title>");
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
        out.println("alert('Error processing faq!');\n");
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
