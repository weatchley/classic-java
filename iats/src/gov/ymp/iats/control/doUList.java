package gov.ymp.iats.control;

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
import gov.ymp.iats.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.auth.*;
import gov.ymp.csi.items.*;
import javax.naming.*;


public class doUList extends HttpServlet {
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
    String description = request.getParameter("description");
    String status = request.getParameter("rstatus");
    status = (status != null && status.compareTo(" ") > 0) ? status : null;
    String outLine = "";
    //String nextScript = "index.jsp";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean success = false;
    String userIDs = (String) session.getAttribute("user.id");
    long userID = Long.parseLong(userIDs);

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "collections.jsp";

//    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

    DbConn myConn = null;
    try {

        Context initCtx = new InitialContext();
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        UList ul = null;
        if (command.equals("add")) {
            ul = new UList(myConn);
            ul.setDescription(description);
            ul.setStatus(status);
            ul.save(myConn, userID);
            success = true;
            outLine = "";

        } else if (command.equals("update")) {
            ul = new UList(Long.parseLong(id), myConn);
            ul.setDescription(description);
            ul.setStatus(status);
            ul.save(myConn, userID);
            success = true;
            outLine = "";
        } else if (command.equals("drop")) {
            ul = new UList(Long.parseLong(id), myConn);
            ul.drop(myConn, userID);
            success = true;
            outLine = "UList " + ul.getDescription() + " Removed";
        } else if (command.equals("test")) {
            outLine = "test";
        }
        if (command.equals("add") || command.equals("update")) {
            String countS = request.getParameter("itemcount");
            int itemCount = Integer.parseInt(countS);
            ul.clearItems();
            for (int i=0; i<itemCount; i++) {
                String itemType = request.getParameter("itemtype" + i);
                //String keyName = request.getParameter("keyname" + i);
//System.out.println("doUList - add/update item - itemType: " + itemType + ", i: " + i + ", itemCount: " + itemCount);
                if (itemType.equals("textitem")) {
                    String idS = request.getParameter("textitemid" + i);
                    long itemID = Long.parseLong(idS);
                    String itemText = request.getParameter("textitem" + i);
                    if (itemID != 0 || (itemID ==0 && itemText != null && !itemText.equals(""))) {
                        TextItem ti = null;
                        if (itemID == 0) {
                            ti = new TextItem();
                        } else {
                            ti = new TextItem(itemID, myConn);
                        }
                        String itemLink = request.getParameter("textitemlink" + i);
                        String date1 = request.getParameter("textitemdate1-" + i);
                        String date2 = request.getParameter("textitemdate2-" + i);
                        if (date1 != null && date1.compareTo(" ") > 0) {
                            ti.setDate1(Utils.toDate(date1));
                        } else {
                            ti.setDate1(null);
                        }
                        if (date2 != null && date2.compareTo(" ") > 0) {
                            ti.setDate2(Utils.toDate(date2));
                        } else {
                            ti.setDate2(null);
                        }
                        ti.setText(itemText);
                        ti.setLink(itemLink);
                        ti.save(myConn, userID);
                        ul.addItem(ti.getID());
                    }
                } else if (itemType.equals("ulist")) {
                    String idS = request.getParameter("collectionid" + i);
                    long itemID = Long.parseLong(idS);
                    String colDescription = request.getParameter("collectiondescription" + i);
                    if (itemID != 0 || (itemID ==0 && colDescription != null && !colDescription.equals(""))) {
                        UList col = null;
                        if (itemID == 0) {
                            col = new UList(myConn);
                        } else {
                            col = new UList(itemID, myConn);
                        }
                        col.setDescription(colDescription);
                        col.save(myConn, userID);
                        ul.addItem(col.getUNID());
                    }
                } else if (itemType.equals("uhash")) {
                    String idS = request.getParameter("collectionid" + i);
                    long itemID = Long.parseLong(idS);
                    String colDescription = request.getParameter("collectiondescription" + i);
                    if (itemID != 0 || (itemID ==0 && colDescription != null && !colDescription.equals(""))) {
                        UHash col = null;
                        if (itemID == 0) {
                            col = new UHash(myConn);
                        } else {
                            col = new UHash(itemID, myConn);
                        }
                        col.setDescription(colDescription);
                        col.save(myConn, userID);
                        ul.addItem(col.getUNID());
                    }

                }
            }
            ul.save(myConn, userID);
        }

    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, "Role error: '" + outLine + "'");
	    //log(outLine);
    }


    catch (NullPointerException e) {
	    outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, "Role error: '" + outLine + "'");
	    //log(outLine);
    }

    //catch (IOException e) {
 	//    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logActivity(userID, "csi", 0, "Role error: '" + outLine + "'");
	//    //log(outLine);
    //}

    catch (Exception e) {
	    outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, "Role error: '" + outLine + "'");
	    //log(outLine);
    }
    finally {
        try { generateResponse(outLine, command, nextScript, success, response); } catch (Exception i) {}

        myConn.release();
	   //log("Test log message\n");
    }


    return outLine;

  }


  private void getPermissions(HttpServletRequest request, Role item) {
      String [] per = request.getParameterValues("permissions");
      item.clearPermissions();
      for (int i=0; i<per.length; i++) {
          item.addPermission(Long.parseLong(per[i]));
      }
  }


  private void getPositions(HttpServletRequest request, Role item) {
      String [] pos = request.getParameterValues("positions");
      item.clearPositions();
      for (int i=0; i<pos.length; i++) {
          item.addPosition(Long.parseLong(pos[i]));
      }
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
        out.println("alert('Error saving UList!');\n");
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
