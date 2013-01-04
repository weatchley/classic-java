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
import gov.ymp.pclwr.model.*;
import org.apache.commons.fileupload.*;


public class doSoftwareWorkRequest extends HttpServlet {
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
    String wrIDs = ((request.getParameter("wrid") != null) ? request.getParameter("wrid") : "0");
    int wrID = Integer.parseInt(wrIDs);
//System.out.println("doSoftwareWorkRequest - Got Here 1" +"- command: " + command + ", wrID: " + wrID);


    String outLine = "";
    //String nextScript = "workRequest.jsp";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean success = false;
    String userIDs = (String) session.getAttribute("user.id");
    userIDs = ((userIDs != null) ? userIDs : "0");
    long userID = Long.parseLong(userIDs);

    command = (command != null && command.compareTo(" ") > 0) ? command : "new";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "home.jsp";
//System.out.println("doSoftwareWorkRequest - Got Here 2" +"- command: " + command + ", wrID: " + wrID);

//    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

    DbConn myConn = null;
    try {

        Context initCtx = new InitialContext();
        String ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        SoftwareWorkRequest item = null;
        if (command.equals("add") || command.equals("new")) {
            command = "add";
            item = new SoftwareWorkRequest();
        } else {
            item = new SoftwareWorkRequest(wrID, myConn);
        }
//System.out.println("doSoftwareWorkRequest - Got Here II.1" +"- command: " + command + ", wrID: " + wrID);
        if (command.equals("add") || command.equals("update")) {
            String dispSave = "";
            String disp = "";
            item.setOwner(request.getParameter("owner"));
            item.setContact(request.getParameter("contact"));
            item.setEmail(request.getParameter("email"));
            item.setOrganization(request.getParameter("department"));
            item.setPhone(request.getParameter("phone"));
            item.setModifyExisting(((request.getParameter("existing").equals("1")) ? "Yes" : "No"));
            item.setExistingSystem(request.getParameter("existingsystem"));
            item.setRequirements(request.getParameter("requirements"));
            item.setReason(request.getParameter("reason"));
            item.setBenefits(request.getParameter("benefits"));
            item.setInvolvedOrgs(request.getParameter("involvedOrgs"));
            item.setBusinessProcess(request.getParameter("businessProcesses"));
            item.setRequestedDelivery(request.getParameter("daterequired"));
            item.setComments(request.getParameter("comments"));
            item.setDisposition(request.getParameter("disposition"));
            disp = item.getDisposition();
            if (!command.equals("add")) {
                dispSave = item.getDispositionSave();
            }
            item.setRequirements(request.getParameter("requirements"));
            item.setReasonRej(request.getParameter("reason_rej"));
            item.save(myConn, userID);
//System.out.println("doSoftwareWorkRequest - Got Here II.1.5" +"- command: " + command + ", wrID: " + wrID);
            wrID = item.getID();
            // process attachments
            String attachCountS = request.getParameter("attachcount");
            int attachCount = Integer.parseInt(attachCountS);
            for (int i=1; i<=attachCount; i++) {
                Object fileObject = request.getAttribute("attachment" + i);
                if (fileObject != null && !(fileObject instanceof FileUploadException)) {
                    FileItem fileItem = (FileItem) fileObject;
                    String fileName = fileItem.getName();
                    fileName = trimFilePath(fileName);
                    InputStream inStream = fileItem.getInputStream();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    int myByte = inStream.read();
                    while (myByte >= 0) {
                        outStream.write(myByte);
                        myByte = inStream.read();
                    }
                    SoftwareAttachment at = new SoftwareAttachment(wrID, fileName, myConn);
                    at.setImage(myConn, outStream);
                }
            }

// Begin Workflow steps -------------------------------------------------------------
            if (command.equals("add") || (dispSave != null && !dispSave.equals(disp))) {
                String message = "";
                String sendTo = "";
                String subject = "";
                Permission perm = new Permission (myConn, 10, "notify");
                Person [] per = GlobalMembership.getPersonListWithMembership(myConn, perm.getID());
                Mailer mb = new Mailer();
                mb.addTo(item.getEmail());
                for (int i=0; i<per.length; i++) {
                    mb.addTo(per[i].getEmail());
                }
                if (!ProductionStatus.toLowerCase().equals("prod") && !ProductionStatus.toLowerCase().equals("production")) {
                    message += "Test message, please ignore.\n\n";
                }
                if (command.equals("add")) {
                    message += "Your Work request has been submitted to the IT Software Work Request System.\n\n";
                    subject = "New IT Software Work Request submitted";
                    // get message
                    // get sendto
                    perm = new Permission (myConn, 10, "approver");
                    per = GlobalMembership.getPersonListWithMembership(myConn, perm.getID());
                    //mb.addTo(item.getEmail());
                    for (int i=0; i<per.length; i++) {
                        mb.addTo(per[i].getEmail());
                    }
                } else if (!dispSave.equals(disp)) {
                    message += "Your Work request has been updated in the IT Software Work Request System.\n\n";
                    subject = "IT Software Work Request status change";
                    // get message
                    // get sendto
                }
                item = new SoftwareWorkRequest(wrID, myConn);
                message += "Software Work Request ID SWR" +  lPad(Integer.toString(item.getID()), "0", 4) +
                           "\nOwner: " +  testNull(item.getOwner(), "") +
                           "\nDepartment: " +  testNull(item.getOrganization(), "") +
                           "\nContact: " +  testNull(item.getContact(), "") +
                           "\nPhone Number: " +  testNull(item.getPhone(), "") +
                           "\nEmail: " +  testNull(item.getEmail(), "") +
                           "\nModify Existing: " +  testNull(item.getModifyExisting(), "") +
                           "\nExisting System: " +  testNull(item.getExistingSystem(), "") +
                           "\nReason: " +  testNull(item.getReason(), "") +
                           "\nBenefits: " +  testNull(item.getBenefits(), "") +
                           "\nInvolved Organizations: " +  testNull(item.getInvolvedOrgs(), "") +
                           "\nbusiness Process: " +  testNull(item.getBusinessProcess(), "") +
                           "\nWhen Required: " +  testNull(item.getRequestedDelivery(), "") +
                           "\nRequirements: " +  testNull(item.getRequirements(), "") +
                           "\nComments: " +  testNull(item.getComments(), "") +
                           "\nDate Submitted: " +  Utils.dateToString(item.getSubmitDate()) +
                           "\nDisposition: " +  testNull(item.getDisposition(), "") +
                           "\nDisposition Date: " +  Utils.dateToString(item.getDispositionDate());
                if (testNull(item.getDisposition(), "").equals("Rejected")) {
                    message += "\nReson for Rejection: " +  testNull(item.getReasonRej(), "");
                }
                ServletContext ctx = getServletContext();
                message += "\n\n    " + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + ctx.getServletContextName() + "/home.jsp\n";
                mb.setFrom("intranetwebmaster@ymp.gov");
                mb.setSubject(subject);
                mb.setBody(message);
                mb.setServer("gatewaysmtp.ymp.gov");
                mb.doSend();

            }
            // do send
// End Workflow steps -------------------------------------------------------------
            success = true;
            outLine = "";
            ALog.logActivity(userID, "pclwr", ((command.equals("add")) ? 1 : 2), "doSoftwareWorkRequest WR-" + item.getID() + "/\"" + HtmlUtils.getDisplayString(item.getRequirements(), 100) + "\" " + ((command.equals("add")) ? "added" : "updated"));
//System.out.println("doSoftwareWorkRequest - Got Here II.4" +"- command: " + command + ", wrID: " + wrID);

        } else if (command.equals("drop")) {
            //String temp = item.getID() + "/\"" + HtmlUtils.getDisplayString(item.getDescription(), 100) + "\"";
            //item.drop(myConn, userID);

            //success = true;
            //outLine = "SoftwareWorkRequest " + temp + " Removed";
            //ALog.logActivity(userID, "pclwr", 3, outLine);
        } else if (command.equals("test")) {
            outLine = "test";
        }

    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "SoftwareWorkRequest error: '" + outLine + "'");
	    //log(outLine);
    }


    catch (NullPointerException e) {
	    outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "SoftwareWorkRequest error: '" + outLine + "'");
	    //log(outLine);
    }

    //catch (IOException e) {
 	//    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logError(userID, "csi", 0, "SoftwareWorkRequest error: '" + outLine + "'");
	//    //log(outLine);
    //}

    //catch (MessagingException e) {
	//    outLine = outLine + "Messaging Exception caught: " + e.getMessage();
    //    ALog.logError(userID, "csi", 0, "SoftwareWorkRequest error: '" + outLine + "'");
	//    //log(outLine);
    //}
    catch (Exception e) {
        outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "SoftwareWorkRequest error: '" + outLine + "'");
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
    out.println("<title>Retention Period</title>");
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
        out.println("alert('Error processing Work Request!');\n");
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


    /**
     * Trim the eventual file path from the given file name. Anything before the last occurred "/"
     * and "\" will be trimmed, including the slash.
     * @param fileName The file name to trim the file path from.
     * @return The file name with the file path trimmed.
     */
    public static String trimFilePath(String fileName) {
        return fileName
            .substring(fileName.lastIndexOf("/") + 1)
            .substring(fileName.lastIndexOf("\\") + 1);
    }


    private String lPad(String text, String pad, int len) {
        while (text.length() < len) {
            text = pad + text;
        }
        return text;
    }



}
