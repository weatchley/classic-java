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
import gov.ymp.csi.items.*;
import gov.ymp.csi.UNID.*;
import javax.naming.*;


public class doFavorites extends HttpServlet {
  // Handle the GET HTTP Method
  public void doGet(HttpServletRequest request,
		    HttpServletResponse response)
	 throws IOException {

    String message = "";
    try {
        message = processRequest(request,response);
        //generateResponse("Invalid 'GET' request", "n/a","n/a", false, response);
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
    String description = request.getParameter("desc");
    String url = request.getParameter("url");
    String unidS = request.getParameter("unid");

    unidS = (unidS != null && unidS.compareTo(" ") > 0) ? unidS : "0";
    url = (url != null && url.compareTo(" ") > 0 && !url.equals("n/a")) ? url : null;
    long unid = Long.parseLong(unidS);
    long lid = Long.parseLong(id);
    String outLine = "";
    //String nextScript = "home.jsp";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean success = false;
    String userIDs = (String) session.getAttribute("user.id");
    userIDs = (userIDs != null && userIDs.compareTo(" ") > 0) ? userIDs : "0";
    long userID = Long.parseLong(userIDs);
//System.out.println("userid=" + userID + ", id=" + id + ", command=" + command);

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "favorites.jsp";

//    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

    DbConn myConn = null;
    try {

        Context initCtx = new InitialContext();
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        //String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        if (userID != 0) {
            if (command.equals("add")) {
                UList ul = new UList("OPG-UserFavorites-" + userID, myConn);
                if (ul == null || ul.getID() == 0) {
                    UList ulDef = new UList("OPG-DefaultUserFavorites", myConn);
                    ul = new UList(myConn, userID);
                    ul.setDescription("OPG-UserFavorites-" + userID);
                    ul.setCreator(userID);
                    long [] itemsTmp = ulDef.getItemsArray();
                    for (int i=0; i<itemsTmp.length; i++) {
                        ul.addItem(itemsTmp[i], myConn);
                    }
                    ul.save(myConn);
                }
                if (unid != 0) {
                    ul.addItem(unid, myConn);
                    ul.save(myConn);
                } else {
					if (description != null && description.compareTo("  ") > 0) {
                        TextItem ti = new TextItem();
                        ti.setText(description);
                        ti.setLink(url);
                        ti.setOwner(userID);
                        ti.save(myConn, userID);
                        TextItem tiTest = new TextItem(ti.getID(), myConn);
                        if (tiTest.getID() != 0) {
                            ul.addItem(ti.getID(), myConn);
                            ul.save(myConn);
						}
					}
                }
                outLine = "";
                success = true;

            } else if (command.equals("update")) {
                outLine = "";
            } else if (command.equals("drop")) {
//System.out.println("Got Here 1 - userID=" + userID + ", ti=" + lid);
                UList ul = new UList("OPG-UserFavorites-" + userID, myConn);
                TextItem ti = new TextItem(lid, myConn);
                ul.dropItem(ti.getID());
                ul.save(myConn, userID);
                if (ti.getOwner() == userID) {
                    ti.drop(myConn, userID);
                }
                outLine = "";
                success = true;
            } else if (command.equals("deleteitem")) {
                String scurrFolder = request.getParameter("currfolder");
                long currFolder = Long.parseLong(scurrFolder);
//System.out.println("Got Here 1 - id=" + lid + ", currFolder=" + currFolder);
                UList ul = new UList(currFolder, myConn);
                TextItem ti = new TextItem(lid, myConn);
                ul.dropItem(ti.getID());
                ul.save(myConn, userID);
                if (ti.getOwner() == userID && !UList.doesItemExist(myConn, lid)) {
                    ti.drop(myConn, userID);
                }
                outLine = "";
                success = true;
            } else if (command.equals("deletefolder")) {
                String scurrFolder = request.getParameter("currfolder");
                long currFolder = Long.parseLong(scurrFolder);
                UList ul = new UList(currFolder, myConn);
                UList ulChild = new UList(lid, myConn);
                ul.dropItem(ulChild.getID());
                ul.save(myConn, userID);
                if (ulChild.getCreator() == userID && !UList.doesItemExist(myConn, lid)) {
                    ulChild.drop(myConn, userID);
                }
                outLine = "";
                success = true;
            } else if (command.equals("rename")) {
//System.out.println("Got Here 1");
                TextItem ti = new TextItem(lid, myConn);
                outLine = "";
                if (ti.getOwner() != 0) {
                    ti.setText(request.getParameter("item" + id));
                    ti.save(myConn, userID);
                } else {
                    UList ul = new UList("OPG-UserFavorites-" + userID, myConn);
                    TextItem tiTmp = new TextItem();
                    tiTmp.setText(request.getParameter("item" + id));
                    tiTmp.setLink(ti.getLink());
                    tiTmp.setOwner(userID);
                    tiTmp.save(myConn, userID);
                    ul.addItem(tiTmp.getID(), myConn);
                    ul.dropItem(ti.getID());
                    ul.save(myConn);
                }
                success = true;
            } else if (command.equals("updateitem")) {
//System.out.println("Got Here 1");
                TextItem ti = new TextItem(lid, myConn);
                outLine = "";
                if (ti.getOwner() != 0) {
                    ti.setText(request.getParameter("itemtextedit"));
                    ti.setLink(request.getParameter("itemlinkedit"));
                    ti.save(myConn, userID);
                } else {
                    UList ul = new UList("OPG-UserFavorites-" + userID, myConn);
                    TextItem tiTmp = new TextItem();
                    tiTmp.setText(request.getParameter("itemtextedit"));
                    tiTmp.setLink(request.getParameter("itemlinkedit"));
                    tiTmp.setOwner(userID);
                    tiTmp.save(myConn, userID);
                    ul.addItem(tiTmp.getID(), myConn);
                    ul.dropItem(ti.getID());
                    ul.save(myConn);
                }
                success = true;
            } else if (command.equals("updatenote")) {
                TextItem ti = new TextItem(lid, myConn);
                outLine = "";
                if (ti.getOwner() != 0) {
                    ti.setText(request.getParameter("notetextedit"));
                    ti.save(myConn, userID);
                } else {
                    UList ul = new UList("OPG-UserFavorites-" + userID, myConn);
                    TextItem tiTmp = new TextItem();
                    tiTmp.setText(request.getParameter("notetextedit"));
                    tiTmp.setLink(ti.getLink());
                    tiTmp.setOwner(userID);
                    tiTmp.save(myConn, userID);
                    ul.addItem(tiTmp.getID(), myConn);
                    ul.dropItem(ti.getID());
                    ul.save(myConn);
                }
                success = true;
            } else if (command.equals("renamefolder")) {
//System.out.println("Got Here 1");
                UList ul = new UList(lid, myConn);
                outLine = "";
                ul.setDescription(request.getParameter("item" + id));
                ul.save(myConn);
                success = true;
            } else if (command.equals("updatefolder")) {
                UList ul = new UList(lid, myConn);
                outLine = "";
                ul.setDescription(request.getParameter("foldertextedit"));
                ul.save(myConn);
                success = true;
            } else if (command.equals("addnote")) {
                UList ul = new UList("OPG-UserFavorites-" + userID, myConn);
                if (ul == null || ul.getID() == 0) {
                    UList ulDef = new UList("OPG-DefaultUserFavorites", myConn);
                    ul = new UList(myConn, userID);
                    ul.setDescription("OPG-UserFavorites-" + userID);
                    ul.setCreator(userID);
                    long [] itemsTmp = ulDef.getItemsArray();
                    for (int i=0; i<itemsTmp.length; i++) {
                        ul.addItem(itemsTmp[i], myConn);
                    }
                    ul.save(myConn);
                }
                String noteText = request.getParameter("notetext");
                if (noteText != null && noteText.compareTo("  ") > 0) {
                    TextItem ti = new TextItem();
                    ti.setText(noteText);
                    ti.setOwner(userID);
                    ti.save(myConn, userID);
                    TextItem tiTest = new TextItem(ti.getID(), myConn);
                    if (tiTest.getID() != 0) {
                        ul.addItem(ti.getID(), myConn);
                        ul.save(myConn);
                    }
                }

                outLine = "";
                success = true;
            } else if (command.equals("addfolder")) {
                UList ul = new UList("OPG-UserFavorites-" + userID, myConn);
                UList ul2 = new UList(myConn, userID);
                ul2.setDescription(request.getParameter("foldername"));
                ul2.setCreator(userID);
                ul2.save(myConn);
                ul.addItem(ul2.getID(), myConn);
                ul.save(myConn);
                outLine = "";
                success = true;
            } else if (command.equals("movetofolder")) {
                String snewFolder = request.getParameter("newfolder");
                long newFolder = Long.parseLong(snewFolder);
                String scurrFolder = request.getParameter("currfolder");
                long currFolder = Long.parseLong(scurrFolder);
System.out.println("curr: " + currFolder + " new: " + newFolder + ", item: " + lid);
                UList newUl = new UList(newFolder, myConn);
                //newUl.lookup(myConn);
                newUl.addItem(lid, myConn);
                newUl.save(myConn, userID);
                UList currUl = new UList(currFolder, myConn);
                //currUl.lookup(myConn);
                currUl.dropItem(lid);
                currUl.save(myConn, userID);
                outLine = "";
                success = true;
            } else if (command.equals("test")) {
                outLine = "test";
            }
        }

    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, "Favorites error: '" + outLine + "'");
	    //log(outLine);
    }


    catch (NullPointerException e) {
	    outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, "Favorites error: '" + outLine + "'");
	    //log(outLine);
    }

    //catch (IOException e) {
 	//    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logActivity(userID, "csi", 0, "Favorites error: '" + outLine + "'");
	//    //log(outLine);
    //}

    catch (Exception e) {
	    outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, "Favorites error: '" + outLine + "'");
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
        if (!outLine.equals("")) {
            out.println("alert('Error saving favorite!');\n");
        } else {
            out.println("alert('Error saving favorite: " + outLine + "');\n");
        }
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
