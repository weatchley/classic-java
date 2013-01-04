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
import gov.ymp.rdc.*;
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
    String pageDescription = request.getParameter("pagedescription");
    String systemAcronym = request.getParameter("systemacronym");
    String acronym = null;
    UHash homePage = null;
    String outLine = "";
    //String nextScript = "home.jsp";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    HttpSession session = request.getSession();
    SessionControler.loader(request);
    boolean success = false;
    String userIDs = (String) session.getAttribute("user.id");
    long userID = Long.parseLong(userIDs);
    String sIsNewsletter = request.getParameter("isnewsletter");
    boolean isNewsletter = (sIsNewsletter.toLowerCase().equals("true")) ? true : false;
    String sNewsletterID = request.getParameter("newsletterid");
    long nlID = Long.parseLong(sNewsletterID);

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
    // Determine the script to go to after processing changes
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "home.jsp";
    pageDescription = (pageDescription != null && pageDescription.compareTo(" ") > 0) ? pageDescription : "Home"; // "Contacts", "MyPage", etc...

//    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

    DbConn myConn = null;
    try {

//System.out.println("doUpdatePage - Got Here 1" +"- command: " + command + ", nlID: " + nlID + ", PageID: " + pageName);
        Context initCtx = new InitialContext();
        String ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        acronym = (systemAcronym != null && systemAcronym.compareTo(" ") > 0) ? systemAcronym : acronym;
        myConn = new DbConn();
        // Get the UHash defining the page content
        UHash pageContent = new UHash(pageName, myConn);
        // Get TextItems with published and working content
        TextItem publishedContent = null;
        TextItem workingContent = null;
        TextItem newsletter = null;
        UList list = null;
        String published = "";
        String working = "";
        JSONObject jo = null;
//System.out.println("doUpdatePage - got here - 2");
        if (isNewsletter) {
            list = new UList(pageContent.get("list"), myConn);
            if (nlID == 0) { command = "new"; }
            if (command.equals("new")) {
                newsletter = new TextItem();
                newsletter.setText("temp");
                newsletter.save(myConn, userID);
                nlID = newsletter.getID();
//System.out.println("doUpdatePage - got here - 3, nlID: " + nlID + ",nl UNID: " + newsletter.getUNID().getID());
                list.addItem(newsletter.getUNID());
                list.save(myConn, userID);
                command = "update";
            } else {
                newsletter = new TextItem(nlID, myConn);
                jo = new JSONObject(newsletter.getText());
                published = jo.getString("published");
                working = jo.getString("working");
            }
        } else {
            publishedContent = new TextItem(pageContent.get("published"), myConn);
            workingContent = new TextItem(pageContent.get("working"), myConn);
        }
        String csiSchema = myConn.getSchemaPath();
        // Update the working content with the contents of the submitted form
        if (command.equals("update")) {
            //TextItem ti = new TextItem(Long.parseLong(id), myConn);
            TextItem ti = null;
            if (isNewsletter) {
                ti = newsletter;
                jo = new JSONObject();
                jo.put("published", published);
                jo.put("working", text);
                ti.setText(jo.toString());
                String date1 = request.getParameter("date1");
                ti.setDate1(Utils.toDate(date1));
            } else {
                ti = workingContent;
                ti.setText(text);
            }
            if (status != null && status.compareTo(" ") > 0) {
                ti.setStatus(status);
            } else {
                ti.setStatus(null);
            }
            ti.save(myConn, userID);
            success = true;
            outLine = "";
            ALog.logActivity(userID, acronym, 2, ti.getID() + "/\"" + ti.getText() + "\" updated");
        // Request approval via e-mail and set the status to approvalrequested
        } else if (command.equals("approvalrequest")) {
            TextItem ti = null;
            //TextItem ti = new TextItem(Long.parseLong(id), myConn);
            if (isNewsletter) {
                ti = newsletter;
            } else {
                ti = workingContent;
            }
            TextItem  appPriv = new TextItem(pageContent.get("approvepriv"), myConn);
            String [] appPrivSet = appPriv.getText().split("-");
            //Permission perm = new Permission (myConn, 7, "apphomepage");
            Permission perm = new Permission (myConn, Integer.parseInt(appPrivSet[0]), appPrivSet[1]);
            Person [] per = GlobalMembership.getPersonListWithMembership(myConn, perm.getID());
            Mailer mb = new Mailer();
            for (int i=0; i<per.length; i++) {
                mb.addTo(per[i].getEmail());
            }
            String message = "";
            if (!ProductionStatus.toLowerCase().equals("prod") && !ProductionStatus.toLowerCase().equals("production")) {
                message += "Test message, please ignore.\n\n";
            }
            message += "An approval has been requested for the " + acronym.toUpperCase() + " " + pageDescription + " page" + ((isNewsletter) ? "(" + newsletter.getDate1() + ")" : "") + ", please review at:\n";
            ServletContext ctx = getServletContext();
            //message += "    " + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + ctx.getServletContextName() + "/home.jsp\n";
            message += "    " + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "" + nextScript + ((isNewsletter) ? "?id=" + nlID : "") + "\n";
            mb.setFrom("intranetwebmaster@ymp.gov");
            mb.setSubject(acronym.toUpperCase() + "-Page approval request");
            mb.setBody(message);
            mb.setServer("gatewaysmtp.ymp.gov");
            mb.doSend();
            ti.setStatus("approvalrequested");
            ti.save(myConn, userID);
            success = true;
            outLine = "";
            ALog.logActivity(userID, acronym, 2, ti.getID() + "/\"" + ti.getText() + "\" approval requested");
        // Approve update, set the status to approved and copy approved working content to the published content
        } else if (command.equals("approve")) {
            TextItem ti = null;
            //TextItem ti = new TextItem(Long.parseLong(id), myConn);
            if (isNewsletter) {
                ti = newsletter;
                jo = new JSONObject();
                jo.put("published", working);
                jo.put("working", working);
                ti.setText(jo.toString());
            } else {
                ti = workingContent;
            }
            ti.setStatus("approved");
            ti.save(myConn, userID);
            if (isNewsletter) {
            } else {
                publishedContent.setText(ti.getText());
                publishedContent.save(myConn, userID);
            }
            success = true;
            outLine = "";
            ALog.logActivity(userID, acronym, 2, ti.getID() + "/\"" + ti.getText() + "\" updated/approved");
        } else if (command.equals("test")) {
            outLine = "test";
        }

    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logError(userID, acronym, 0, pageDescription + " page error: '" + outLine + "'");
	    //log(outLine);
    }


    catch (NullPointerException e) {
	    outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logError(userID, acronym, 0, pageDescription + " page error: '" + outLine + "'");
	    //log(outLine);
    }

    //catch (IOException e) {
 	//    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logError(userID, acronym, 0, pageDescription + " page error: '" + outLine + "'");
	//    //log(outLine);
    //}

    //catch (MessagingException e) {
	//    outLine = outLine + "Messaging Exception caught: " + e.getMessage();
    //    ALog.logError(userID, acronym, 0, pageDescription + " page error: '" + outLine + "'");
	//    //log(outLine);
    //}
    catch (Exception e) {
        outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logError(userID, acronym, 0, pageDescription + " page error: '" + outLine + "'");
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
        out.println("alert('Error processing Page!');\n");
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
