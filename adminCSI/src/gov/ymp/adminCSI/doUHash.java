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
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.auth.*;
import gov.ymp.csi.items.*;
import javax.naming.*;


public class doUHash extends HttpServlet {
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
    String status = request.getParameter("status");
    status = (status != null && status.compareTo(" ") > 0) ? status : null;
    String outLine = "";
    //String nextScript = "home.jsp";
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
        UHash uh = null;
//System.out.println("doUHash - got here 1 - Command: " + command);
        if (command.equals("add")) {
            uh = new UHash(myConn);
            uh.setDescription(description);
            uh.setStatus(status);
            uh.save(myConn, userID);
            success = true;
            outLine = "";

        } else if (command.equals("update")) {
            uh = new UHash(Long.parseLong(id), myConn);
            uh.setDescription(description);
            uh.setStatus(status);
            uh.save(myConn, userID);
            success = true;
            outLine = "";
        } else if (command.equals("drop")) {
            uh = new UHash(Long.parseLong(id), myConn);
            uh.drop(myConn, userID);
            success = true;
            outLine = "UHash " + uh.getDescription() + " Removed";
            // remove sub items
        } else if (command.equals("copy")) {
            uh = new UHash(Long.parseLong(id), myConn);
            UHash uh2 = new UHash(myConn);
            uh2.setDescription(request.getParameter("copytoname"));
            uh2.setStatus(uh.getStatus());
            Set keys = uh.getKeys();
            for (Iterator i = keys.iterator(); i.hasNext();) {
                String keyName = (String) i.next();
                UNID uItem = uh.getItemUNID(keyName);
                if (uItem.getType().equals("textitem")) {
                    TextItem ti = new TextItem(uItem.getID(), myConn);
                    TextItem ti2 = new TextItem();
                    ti2.setText(ti.getText());
                    ti2.setLink(ti.getLink());
                    ti2.setStatus(ti.getStatus());
                    if (ti.getDate1() != null) {ti2.setDate1(ti.getDate1());}
                    if (ti.getDate2() != null) {ti2.setDate2(ti.getDate2());}
                    ti2.save(myConn, userID);
                    uh2.putItem(keyName, ti2.getID());
                } else {
                    uh2.putItem(keyName, uItem.getID());
                }
            }
            uh2.save(myConn, userID);
            success = true;
        } else if (command.equals("test")) {
            outLine = "test";
        }
        if (command.equals("add") || command.equals("update")) {
            String countS = request.getParameter("itemcount");
            int itemCount = Integer.parseInt(countS);
            for (int i=0; i<itemCount; i++) {
                String itemType = request.getParameter("itemtype" + i);
                String keyName = request.getParameter("keyname" + i);
//System.out.println("doUhash - add/update item - itemType: " + itemType + ", i: " + i + ", itemCount: " + itemCount);
                if (itemType.equals("textitem")) {
                    String idS = request.getParameter("textitemid" + i);
                    long itemID = Long.parseLong(idS);
//System.out.println("doUhash - add/update TI item - idS: " + idS + ", itemID: " + itemID);
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
                        uh.putItem(keyName, ti.getID());
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
                        uh.putItem(keyName, col.getUNID());
//System.out.println("doUhash - add/update UL item - idS: " + idS + ", itemID: " + itemID + ", Save ID: " + col.getID() + ", hash ID: " + uh.get(keyName));
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
                        uh.putItem(keyName, col.getUNID());
//System.out.println("doUhash - add/update UH item - idS: " + idS + ", itemID: " + itemID + ", Save ID: " + col.getID() + ", hash ID: " + uh.get(keyName));
                    }

                } else {
                }
            }
            uh.save(myConn, userID);
        }

    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, "doUHash error: '" + outLine + "'");
	    //log(outLine);
    }


    catch (NullPointerException e) {
	    outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, "doUHash error: '" + outLine + "'");
	    //log(outLine);
    }

    //catch (IOException e) {
 	//    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logActivity(userID, "csi", 0, "doUHash error: '" + outLine + "'");
	//    //log(outLine);
    //}

    catch (Exception e) {
	    outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, "doUHash error: '" + outLine + "'");
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
        out.println("alert('Error saving UHash!');\n");
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
