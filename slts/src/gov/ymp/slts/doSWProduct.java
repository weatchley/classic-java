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


public class doSWProduct extends HttpServlet {
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
    String idS = request.getParameter("id");
    int id = Integer.parseInt(idS);
//System.out.println("doSWProduct - Got Here 1" +"- command: " + command + ", Timestamp: " + Utils.genDateID());


    String outLine = "";
    String nextScript = request.getParameter("nextscript");
    OutputStream toClient;
    HttpSession session = request.getSession();
    boolean success = false;
    String userIDs = (String) session.getAttribute("user.id");
    userIDs = ((userIDs != null) ? userIDs : "0");
    long userID = Long.parseLong(userIDs);

    command = (command != null && command.compareTo(" ") > 0) ? command : "new";
    nextScript = (nextScript != null && nextScript.compareTo(" ") > 0) ? nextScript : "swBrowse.jsp";


    DbConn myConn = null;
    try {

        Context initCtx = new InitialContext();
        String ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
        //String csiSchema = (String) initCtx.lookup("java:comp/env/csi-schema-path");
        String acronym = (String) initCtx.lookup("java:comp/env/SystemAcronym");
        myConn = new DbConn();
        String csiSchema = myConn.getSchemaPath();
        SWProduct sw = null;
        if (command.equals("new") || command.equals("update")) {
        //if (command.equals("update")) {
// process product
            if (command.equals("new")) {
                sw = new SWProduct();
            } else {
                sw = new SWProduct(id, myConn);
            }
            String name = request.getParameter("name");
            sw.setName(name);
            String version = request.getParameter("version");
            sw.setVersion(((version != null) ? version : null));
            String manufacturer = request.getParameter("manufacturer");
            sw.setManufacturer(((manufacturer != null) ? manufacturer : null));
            String platform = request.getParameter("platform");
            sw.setPlatform(((platform != null) ? platform : null));
            String unlimited = request.getParameter("unlimited");
            sw.setUnlimited(((unlimited != null && unlimited.equals("T")) ? true : false));
            String status = request.getParameter("status");
            sw.setStatus(((status != null) ? status : null));
            String relatedProductS = request.getParameter("relatedproduct");
            if (relatedProductS != null && relatedProductS.compareTo("       ") > 0) {
                int relatedProduct = Integer.parseInt(relatedProductS);
                sw.setRelatedProduct(relatedProduct);
            }
            String respOrg = request.getParameter("resporg");
            sw.setRespOrg(((respOrg != null) ? respOrg : null));
            sw.save(myConn);
            id = sw.getID();
            String comments = request.getParameter("comments");
// process comments
            if (comments != null && comments.compareTo("                         ") > 0) {
                Comments comm = new Comments(id, 0, userID, comments, myConn);
            }
//Process attachments
            String attachCountS = request.getParameter("attachcount");
            int attachCount = Integer.parseInt(attachCountS);
            for (int i=1; i<=attachCount; i++) {
                Object fileObject = request.getAttribute("attachment" + i);
                if (fileObject != null && !(fileObject instanceof FileUploadException)) {
                    String aTypeS = request.getParameter("attachmenttype" + i);
                    int aType = Integer.parseInt(aTypeS);
                    String aDesc = request.getParameter("attachmentdesc" + i);
                    FileItem fileItem = (FileItem) fileObject;
                    String fileName = fileItem.getName();
                    aDesc = ((aDesc != null  && aDesc.compareTo("                                ") > 0) ? aDesc : fileName);
                    fileName = trimFilePath(fileName);
                    InputStream inStream = fileItem.getInputStream();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    int myByte = inStream.read();
                    while (myByte >= 0) {
                        outStream.write(myByte);
                        myByte = inStream.read();
                    }
                    Attachment at = new Attachment(id, 0, fileName, aType, aDesc, myConn);
                    at.setImage(myConn, outStream);
                }
            }
// process applist
            String [] appList = request.getParameterValues("applist");
            // process applist
            ProdAppInvMatch.drop(myConn, id);
            if (appList != null) {
                for (int i=0; i<appList.length; i++) {
                    ProdAppInvMatch item = new ProdAppInvMatch();
                    item.setListName(appList[i]);
                    item.setProductID(id);
                    item.save(myConn);
                }
            }
            success = true;
            outLine = "";


// process servlist
            String [] servList = request.getParameterValues("servlist");
            // process applist
            ProdServInvMatch.drop(myConn, id);
            if (servList != null) {
                for (int i=0; i<servList.length; i++) {
                    ProdServInvMatch item = new ProdServInvMatch();
                    item.setListName(servList[i]);
                    item.setProductID(id);
                    item.save(myConn);
                }
            }

            success = true;
            outLine = "";
            ALog.logActivity(userID, "slts", 2, "Product updated.");

        } else if (command.equals("drop")) {

            //success = true;
            //outLine = "Product " + temp + " Removed";
            //ALog.logActivity(userID, "slts", 3, outLine);
        } else if (command.equals("test")) {
            outLine = "test";
        }

    }


    catch (IllegalArgumentException e) {
        outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logError(userID, "slts", 0, "doSWProduct error: '" + outLine + "'");
        //log(outLine);
    }


    catch (NullPointerException e) {
        outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logError(userID, "slts", 0, "doSWProduct error: '" + outLine + "'");
        //log(outLine);
    }

    //catch (IOException e) {
    //    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logError(userID, "slts", 0, "doSWProduct error: '" + outLine + "'");
    //    //log(outLine);
    //}

    //catch (MessagingException e) {
    //    outLine = outLine + "Messaging Exception caught: " + e.getMessage();
    //    ALog.logError(userID, "slts", 0, "doSWProduct error: '" + outLine + "'");
    //    //log(outLine);
    //}
    catch (Exception e) {
        outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logError(userID, "slts", 0, "doSWProduct error: '" + outLine + "'");
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
    out.println("<title>Products</title>");
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
        out.println("alert('Error processing Product!');\n");
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
