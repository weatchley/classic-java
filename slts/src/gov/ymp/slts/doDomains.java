package gov.ymp.slts;

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
import gov.ymp.slts.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.auth.*;
import gov.ymp.csi.items.*;
import gov.ymp.csi.UNID.*;
import javax.naming.*;
import com.darwinsys.mail.*;
import gov.ymp.slts.model.*;
import org.apache.commons.fileupload.*;


public class doDomains extends HttpServlet {
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
    String itemCounts = ((request.getParameter("itemcount") != null) ? request.getParameter("itemcount") : "0");
    int itemCount = Integer.parseInt(itemCounts);
//System.out.println("doDomains - Got Here 1" +"- command: " + command + ", itemCount: " + itemCount);


    String outLine = "";
    //String nextScript = "domains.jsp";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean success = false;
    String userIDs = (String) session.getAttribute("user.id");
    userIDs = ((userIDs != null) ? userIDs : "0");
    long userID = Long.parseLong(userIDs);

    command = (command != null && command.compareTo(" ") > 0) ? command : "new";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "home.jsp";


    DbConn myConn = null;
    try {

        Context initCtx = new InitialContext();
        String ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        Domain item = null;
        //if (command.equals("add") || command.equals("update")) {
        if (command.equals("update")) {
            // process domains
            String tempText = "";
            for (int i=0; i<itemCount; i++) {
                tempText = "domainname" + i;
                String domainName =  request.getParameter(tempText);
                tempText = "include" + i;
                String includeS = request.getParameter(tempText);
                boolean include = ((includeS != null && includeS.equals("T")) ? true : false);
                item = new Domain(domainName, myConn);
                item.setInclude(include);
                item.save(myConn);
            }
            success = true;
            outLine = "";
            ALog.logActivity(userID, "slts", 2, "Domains updated.");

        } else if (command.equals("drop")) {

            //success = true;
            //outLine = "Domain " + temp + " Removed";
            //ALog.logActivity(userID, "slts", 3, outLine);
        } else if (command.equals("test")) {
            outLine = "test";
        }

    }


    catch (IllegalArgumentException e) {
        outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logError(userID, "slts", 0, "doDomains error: '" + outLine + "'");
        //log(outLine);
    }


    catch (NullPointerException e) {
        outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logError(userID, "slts", 0, "doDomains error: '" + outLine + "'");
        //log(outLine);
    }

    //catch (IOException e) {
    //    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logError(userID, "slts", 0, "doDomains error: '" + outLine + "'");
    //    //log(outLine);
    //}

    //catch (MessagingException e) {
    //    outLine = outLine + "Messaging Exception caught: " + e.getMessage();
    //    ALog.logError(userID, "slts", 0, "doDomains error: '" + outLine + "'");
    //    //log(outLine);
    //}
    catch (Exception e) {
        outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logError(userID, "slts", 0, "doDomains error: '" + outLine + "'");
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
    out.println("<title>Domains</title>");
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
        out.println("alert('Error processing Domain!');\n");
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
