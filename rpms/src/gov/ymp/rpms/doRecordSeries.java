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
import gov.ymp.rpms.model.*;


public class doRecordSeries extends HttpServlet {
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
//System.out.println("doRecordSeries - Got Here 1" +"- command: " + command);
    String rsIDs = request.getParameter("rsid");
    int rsID = Integer.parseInt(rsIDs);
    String description = request.getParameter("description");
    String commonFlag = request.getParameter("commonflag");
    String locationS = request.getParameter("location");
    int location = Integer.parseInt(locationS);
    String ocrwmCode = request.getParameter("ocrwmcode");
    String citationS = request.getParameter("citation");
    int citation = Integer.parseInt(citationS);
    String cutoffS = request.getParameter("cutoff");
    int cutoff = Integer.parseInt(cutoffS);
    String retentionPeriodS = request.getParameter("retentionperiod");
    int retentionPeriod = Integer.parseInt(retentionPeriodS);
    String retentionGroupFlagS = request.getParameter("retentiongroupflag");
    int retentionGroupFlag = Integer.parseInt(retentionGroupFlagS);
    String retentionCode = ((request.getParameter("retentioncode") != null && !request.getParameter("retentioncode").equals("0")) ? request.getParameter("retentioncode") : null);

    String itemCountS = request.getParameter("itemcount");
    int itemCount = Integer.parseInt(itemCountS);

    String outLine = "";
    //String nextScript = "recordSeries.jsp";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean success = false;
    String userIDs = (String) session.getAttribute("user.id");
    long userID = Long.parseLong(userIDs);

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "home.jsp";

//    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

    DbConn myConn = null;
    try {

        Context initCtx = new InitialContext();
        String ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        RecordSeries item = null;
        if (command.equals("add")) {
            item = new RecordSeries();
        } else {
            item = new RecordSeries(rsID, myConn);
        }
        if (command.equals("add") || command.equals("update")) {
            item.setDescription(description);
//System.out.println("doRecordSeries - Got Here II.1" +"- command: " + command + ", rsID: " + rsID);
            item.setCommonFlag(commonFlag);
            item.setLocation(location);
            item.setOCRWMCode(ocrwmCode);
            item.setCitation(citation);
            item.setCutoff(cutoff);
            item.setRetentionPeriod(retentionPeriod);
            item.setRetentionGroupFlag(retentionGroupFlag);
            item.setRetentionCode(retentionCode);
            item.save(myConn, userID);
            for (int i=0; i< itemCount; i++) {
                String cwIDs = request.getParameter("cwid" + i);
                int cwID = Integer.parseInt(cwIDs);
                String name = request.getParameter("name" + i);
                if (name != null) {
                    Crosswalk cw = null;
                    if (cwID == 0) {
                        cw = new Crosswalk ();
                    } else {
                        cw = new Crosswalk (cwID, myConn);
                    }
                    cw.setRSID(item.getID());
                    cw.setName(name);
                    String QADesignationS = request.getParameter("qadesignation" + i);
                    int QADesignation = Integer.parseInt(QADesignationS);
                    cw.setQADesignation(QADesignation);
                    String accessRestrictions = request.getParameter("accessrestrictions" + i);
                    cw.setAccessRestrictions(accessRestrictions);
                    String unscheduled = request.getParameter("unscheduled" + i);
                    unscheduled = (unscheduled != null) ? unscheduled : "F";
                    cw.setUnscheduled(unscheduled);
                    //String requirementsS = request.getParameter("requirements" + i);
                    //int requirements = Integer.parseInt(requirementsS);
                    //cw.setRequirements(requirements);
                    String [] req = request.getParameterValues("requirements" + i);
                    if (req != null && req.length > 0) {
                        Requirement [] reqList = new Requirement [req.length];
                        for (int j=0; j<req.length; j++) {
                            reqList[j] = new Requirement(Integer.parseInt(req[j]), myConn);
                        }
                        cw.setRequirements(reqList);
                    } else {
                        cw.setRequirements(null);
                    }
                    String requirementsOther = request.getParameter("requirementsother" + i);
                    cw.setRequirementsOther(requirementsOther);
                    String freezeHoldFlag = request.getParameter("freezeholdflag" + i);
                    cw.setFreezeHoldFlag(freezeHoldFlag);
                    String freezeHoldText = request.getParameter("freezeholdtext" + i);
                    cw.setFreezeHoldText(freezeHoldText);
                    String vitalRecord = request.getParameter("vitalrecord" + i);
                    cw.setVitalRecord(vitalRecord);
                    cw.save(myConn, userID);
                }
            }
            success = true;
            outLine = "";
            ALog.logActivity(userID, "rpms", ((command.equals("add")) ? 1 : 2), "doRecordSeries " + item.getID() + "/\"" + HtmlUtils.getDisplayString(item.getDescription(), 100) + "\" " + ((command.equals("add")) ? "added" : "updated"));

        } else if (command.equals("drop")) {
            String temp = item.getID() + "/\"" + HtmlUtils.getDisplayString(item.getDescription(), 100) + "\"";
            item.drop(myConn, userID);

            success = true;
            outLine = "RecordSeries " + temp + " Removed";
            ALog.logActivity(userID, "rpms", 3, outLine);
        } else if (command.equals("dropcw")) {
            String cwIDs = request.getParameter("cwid");
            int cwID = Integer.parseInt(cwIDs);
            Crosswalk cw = new Crosswalk (cwID, myConn);
            String temp = cw.getID() + "/\"" + HtmlUtils.getDisplayString(cw.getName(), 100) + "\"";
            cw.drop(myConn, userID);

            success = true;
            outLine = "Crosswalk " + temp + " Removed";
            ALog.logActivity(userID, "rpms", 3, outLine);
        } else if (command.equals("test")) {
            outLine = "test";
        }

    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "RecordSeries error: '" + outLine + "'");
	    //log(outLine);
    }


    catch (NullPointerException e) {
	    outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "RecordSeries error: '" + outLine + "'");
	    //log(outLine);
    }

    //catch (IOException e) {
 	//    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logError(userID, "csi", 0, "RecordSeries error: '" + outLine + "'");
	//    //log(outLine);
    //}

    //catch (MessagingException e) {
	//    outLine = outLine + "Messaging Exception caught: " + e.getMessage();
    //    ALog.logError(userID, "csi", 0, "RecordSeries error: '" + outLine + "'");
	//    //log(outLine);
    //}
    catch (Exception e) {
        outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logError(userID, "csi", 0, "RecordSeries error: '" + outLine + "'");
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
            String tempOut = ((outLine).replaceAll("\n", " "));
            tempOut = tempOut.replaceAll("\r", " ");
            tempOut = tempOut.replaceAll("'", "");
            out.println("alert('" + tempOut + "');\n");
        }
        out.println("parent.document.location='" + nextScript + "';\n");
    } else {
        out.println("alert('Error processing Record Series!');\n");
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }


}
