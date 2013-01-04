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
import java.sql.*;

public class doResources extends HttpServlet {
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
  private String processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String command = request.getParameter("command");
    String id = request.getParameter("id");
    String description = request.getParameter("desc");
    String url = request.getParameter("url");
    String unidS = request.getParameter("unid");
    String date1 = request.getParameter("date1");
    String date2 = request.getParameter("date2");

    unidS = (unidS != null && unidS.compareTo(" ") > 0) ? unidS : "0";
    url = (url != null && url.compareTo(" ") > 0 && !url.equals("n/a")) ? url : null;
    date1 = (date1 != null && date1.compareTo(" ") > 0 && !date1.equals("n/a")) ? date1 : null;
    date2 = (date2 != null && date2.compareTo(" ") > 0 && !date2.equals("n/a")) ? date2 : null;
    long unid = Long.parseLong(unidS);
    long lid = Long.parseLong(id);
    String outLine = "";
    //String nextScript = "home.jsp";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean success = false;
    String resourceType = request.getParameter("resourcetype");
    String userIDs = (String) session.getAttribute("user.id");
    userIDs = (userIDs != null && userIDs.compareTo(" ") > 0) ? userIDs : "0";
    long userID = Long.parseLong(userIDs);
    long ownerID = ((resourceType.equals("favorites")) ? userID : 0);
    String resourceName = "";
    if (resourceType.equals("favorites")) {
        resourceName = "OPG-UserFavorites-" + userID;
    } else if (resourceType.equals("notebook")) {
        resourceName = "OPG-UserNoteBook-" + userID;
    } else if (resourceType.equals("shared")) {
        resourceName = "OPG-UserShared-" + userID;
    } else if (resourceType.equals("people")) {
        resourceName = "OPG-PeopleTab";
    } else if (resourceType.equals("organizations")) {
        resourceName = "OPG-OrganizationsTab";
    } else if (resourceType.equals("resourcelist")) {
        resourceName = "OPG-ResourcesList";
    } else if (resourceType.equals("whatsnew")) {
        resourceName = "OPG-WhatsNew";
    }

//System.out.println("userid=" + userID + ", id=" + id + ", command=" + command);

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : resourceType + ".jsp";

//    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

    DbConn myConn = null;
    try {

        Context initCtx = new InitialContext();
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        //String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        if (userID != 0) {
            if (command.equals("add") || command.equals("additemwithdates")) {
                UList ul = new UList(resourceName, myConn);
                if ((ul == null || ul.getID() == 0) && resourceType.equals("favorites")) {
                    UList ulDef = new UList("OPG-DefaultUserFavorites", myConn);
                    ul = new UList(myConn, userID);
                    ul.setDescription(resourceName);
                    ul.setCreator(ownerID);
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
                        ti.setLink(((url!=null) ? url : "n/a"));
                        if (date1 != null) {
                            ti.setDate1(Utils.toDate(date1));
                        }
                        if (date2 != null) {
                            ti.setDate2(Utils.toDate(date2));
                        }
                        ti.setOwner(ownerID);
                        ti.save(myConn, userID);
                        TextItem tiTest = new TextItem(ti.getID(), myConn);
                        if (tiTest.getID() != 0) {
                            ul.addItem(ti.getID(), myConn);
                            ul.save(myConn);
						}
					}
                }
                if (command.equals("additemwithdates")) {
                    sortItemList(myConn, ul, "date1 desc");
                }
                outLine = "";
                success = true;

            } else if (command.equals("update")) {
                outLine = "";
            } else if (command.equals("drop")) {
//System.out.println("Got Here 1 - userID=" + userID + ", ti=" + lid);
                UList ul = new UList(resourceName, myConn);
                TextItem ti = new TextItem(lid, myConn);
                ul.dropItem(ti.getID());
                ul.save(myConn, userID);
                if (ti.getOwner() == ownerID) {
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
                if (ti.getOwner() == ownerID && !UList.doesItemExist(myConn, lid)) {
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
                if (ulChild.getCreator() == ownerID && !UList.doesItemExist(myConn, lid)) {
                    ulChild.drop(myConn, userID);
                }
                outLine = "";
                success = true;
            } else if (command.equals("rename")) {
//System.out.println("Got Here 1");
                TextItem ti = new TextItem(lid, myConn);
                outLine = "";
                //if (ti.getOwner() != 0) {
                if ((resourceType.equals("favorites") && ti.getOwner() != 0) || !resourceType.equals("favorites")) {
                    ti.setText(request.getParameter("item" + id));
                    ti.save(myConn, userID);
                } else {
                    UList ul = new UList(resourceName, myConn);
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
                //if (ti.getOwner() != 0) {
                if (resourceType.equals("favorites") && ti.getOwner() != 0) {
                    ti.setText(request.getParameter("itemtextedit"));
                    ti.setLink(request.getParameter("itemlinkedit"));
                    ti.save(myConn, userID);
                } else if (resourceType.equals("favorites") && ti.getOwner() == 0) {
                    UList ul = new UList(resourceName, myConn);
                    TextItem tiTmp = new TextItem();
                    tiTmp.setText(request.getParameter("itemtextedit"));
                    tiTmp.setLink(request.getParameter("itemlinkedit"));
                    tiTmp.setOwner(userID);
                    tiTmp.save(myConn, userID);
                    ul.addItem(tiTmp.getID(), myConn);
                    ul.dropItem(ti.getID());
                    ul.save(myConn);
                } else {
                    ti.setText(request.getParameter("itemtextedit"));
                    ti.setLink(request.getParameter("itemlinkedit"));
                    ti.setOwner(userID);
                    ti.save(myConn, userID);
                }
                success = true;
            } else if (command.equals("updateitemwithdates")) {
//System.out.println("Got Here 1, " + lid);
                TextItem ti = new TextItem(lid, myConn);
                outLine = "";
                    ti.setText(request.getParameter("itemwithdatestextedit"));
                    String tmpLink = request.getParameter("itemwithdateslinkedit");
                    tmpLink = (tmpLink != null && tmpLink.compareTo(" ") > 0 && !tmpLink.equals("n/a")) ? tmpLink : "n/a";
                    ti.setLink(tmpLink);
                    if (date1 != null) {
                        ti.setDate1(Utils.toDate(date1));
                    } else {
                        ti.setDate1(null);
                    }
                    if (date2 != null) {
                        ti.setDate2(Utils.toDate(date2));
                    } else {
                        ti.setDate2(null);
                    }
                    ti.setOwner(userID);
                    ti.save(myConn, userID);
                    UList ul = new UList(resourceName, myConn);
                    sortItemList(myConn, ul, "date1 desc");
                    ul.save(myConn,userID);
                success = true;
            } else if (command.equals("updatenote")) {
                TextItem ti = new TextItem(lid, myConn);
                outLine = "";
                //if (ti.getOwner() != 0) {
                //if ((resourceType.equals("favorites") && ti.getOwner() != 0) || (!resourceType.equals("favorites")) {
                if ((ti.getOwner() != 0 && resourceType.equals("notebook")) || (!resourceType.equals("favorites") && !resourceType.equals("notebook"))) {
                    ti.setText(request.getParameter("notetextedit"));
                    ti.save(myConn, userID);
                } else if (ti.getOwner() == 0 && resourceType.equals("notebook")) {
                    UList ul = new UList(resourceName, myConn);
                    TextItem tiTmp = new TextItem();
                    tiTmp.setText(request.getParameter("notetextedit"));
                    tiTmp.setOwner(userID);
                    tiTmp.save(myConn, userID);
                    ul.addItem(tiTmp.getID(), myConn);
                    ul.dropItem(ti.getID());
                    ul.save(myConn);
                } else {
                    ti.setText(request.getParameter("notetextedit"));
                    ti.setOwner(userID);
                    ti.save(myConn, userID);
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
                UList ul = new UList(resourceName, myConn);
                if ((ul == null || ul.getID() == 0) && resourceType.equals("favorites")) {
                    UList ulDef = new UList("OPG-DefaultUserFavorites", myConn);
                    ul = new UList(myConn, userID);
                    ul.setDescription(resourceName);
                    ul.setCreator(ownerID);
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
                    ti.setOwner(ownerID);
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
                UList ul = new UList(resourceName, myConn);
                UList ul2 = new UList(myConn, userID);
                ul2.setDescription(request.getParameter("foldername"));
                ul2.setCreator(ownerID);
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
//System.out.println("curr: " + currFolder + " new: " + newFolder + ", item: " + lid);
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
            } else if (command.equals("moveup")) {
//System.out.println("Got here 1, " + lid);
                String scurrFolder = request.getParameter("currfolder");
                long currFolder = Long.parseLong(scurrFolder);
                UList ul = new UList(currFolder, myConn);
                ul.moveItemUp(lid, myConn);
                ul.save(myConn, userID);
                outLine = "";
                success = true;
            } else if (command.equals("movetotop")) {
//System.out.println("Got here 1, " + lid);
                String scurrFolder = request.getParameter("currfolder");
                long currFolder = Long.parseLong(scurrFolder);
                UList ul = new UList(currFolder, myConn);
                ul.moveItemToTop(lid, myConn);
                ul.save(myConn, userID);
                outLine = "";
                success = true;
            } else if (command.equals("pushitem")) {
                PreparedStatement pstmt = null;
                int rows;
                outLine = "";
                success = true;
                UNID uid = new UNID(lid, myConn);
                TextItem ti = new TextItem(lid, myConn);
                if (uid.getType().equals("textitem") && ti.getOwner() == userID) {
                    String sqlcode = "";
                    UList ul = new UList(resourceName, myConn);
                    long [] itemList = ul.getItemsArrayRecursive(myConn);
                    String sItemList = "";
                    for (int i=0; i<itemList.length; i++) {
                        sItemList += itemList[i] + ", ";
                    }
                    sItemList += ul.getID();
                    String [] per = request.getParameterValues("persons");
                    sqlcode = "DELETE FROM " + csiSchema + ".ulists WHERE item=" + id + " AND id NOT IN (" + sItemList + ")";
//System.out.println("Got here 4.2, " + lid + ", " + sqlcode);
                    pstmt = myConn.conn.prepareStatement(sqlcode);
                    rows = pstmt.executeUpdate();
                    pstmt.close();
                    if (per != null) {
                        for (int i=0; i<per.length; i++) {
                            long lper = Long.parseLong(per[i]);
                            ul = new UList("OPG-UserShared-" + lper, myConn);
                            if ((ul == null || ul.getID() == 0)) {
                                UList ulDef = new UList("OPG-DefaultUserShared", myConn);
                                ul = new UList(myConn, lper);
                                ul.setDescription("OPG-UserShared-" + lper);
                                ul.setCreator(lper);
                                long [] itemsTmp = ulDef.getItemsArray();
                                for (int j=0; j<itemsTmp.length; j++) {
                                    ul.addItem(itemsTmp[j], myConn);
                                }
                            }
                            ul.addItem(lid, myConn);
                            ul.save(myConn);
                        }
                    }
                    outLine = "";
                    success = true;
                } else {
                    outLine = "Can only share items that you have created and are the owner of.";
                    success = false;
                }

            } else if (command.equals("test")) {
                outLine = "test";
            }
        }

    }


    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, resourceType + " error: '" + outLine + "'");
	    //log(outLine);
    }


    catch (NullPointerException e) {
	    outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, resourceType + " error: '" + outLine + "'");
	    //log(outLine);
    }

    //catch (IOException e) {
 	//    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logActivity(userID, "csi", 0, resourceType + " error: '" + outLine + "'");
	//    //log(outLine);
    //}

    catch (Exception e) {
	    outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logActivity(userID, "csi", 0, resourceType + " error: '" + outLine + "'");
	    //log(outLine);
    }
    finally {
        try { generateResponse(outLine, command, nextScript, success, response); 
        
        
        
        } catch (Exception i)  
        {
        	PrintWriter out = response.getWriter();
        	out.println("An error has occured in generateResponse " + i.getMessage());	
        	
        }

        myConn.release();
	   //log("Test log message\n");
    }


    return outLine;

  }


  // sort an item list
  private void sortItemList (DbConn myConn, UList ul, String sortBy) {
      TextItem [] tia = TextItem.getItemList(myConn, ul.getItemsArray(), sortBy, "subset");
      ul.clearItems();
      for (int i=0; i<tia.length; i++) {
          //ul.addItem(tia[i].getUNID());
          ul.addItem(tia[i].getID(), myConn);
      }
      ul.save(myConn);
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
        if (!nextScript.equals("noRefresh")) {
            out.println("parent.document.location='" + nextScript + "';\n");
        }
    } else {
        if (!outLine.equals("")) {
            out.println("alert('Error saving resource!');\n");
        } else {
            out.println("alert('Error saving resource: " + outLine + "');\n");
        }
    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
