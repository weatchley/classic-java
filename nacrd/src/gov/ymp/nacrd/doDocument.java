package gov.ymp.nacrd;

import javax.servlet.*;
import javax.servlet.http.*;
// Support classes
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import java.math.*;
//import java.awt.*;
import java.lang.*;
import java.text.*;
import gov.ymp.nacrd.*;
import gov.ymp.util.db.*;
import org.apache.commons.fileupload.*;


public class doDocument extends HttpServlet {
  // Handle the GET HTTP Method
  public void doGet(HttpServletRequest request,
		    HttpServletResponse response)
	 throws IOException {

    String message = "";
    try {
        message = processRequest(request,response);
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
    String outLine = "";
    String command = request.getParameter("command");
    String docID = request.getParameter("docid");
    String docNumber = request.getParameter("docnumber");
    HttpSession session = request.getSession();
    User user = new User((String) session.getAttribute("user.name"));
    int userID = user.getUserID();
    String docRoot = getServletContext().getRealPath("/");
    String oldDocFilename = "";
    String oldCRDFilename = "";
    Doc myDocTemp = new Doc();
    Doc myDoc = new Doc();
    boolean isIFrame = true;
    boolean isMultipart = FileUpload.isMultipartContent(request);
    int docFileSize = 0;
    int crdFileSize = 0;
    List /* FileItem */ items = null;
    Iterator iter = null;
    if (isMultipart) {
        try {
            // Create a new file upload handler
            DiskFileUpload upload = new DiskFileUpload();

            // Parse the request
            /* FileItem */ items = upload.parseRequest(request);
            // Process the uploaded items
            iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                String name = item.getFieldName();
                if (item.isFormField()) {
                    //processFormField(item);
                    String value = item.getString();
                    if (name.equals("command")) {
                        command = value;
                    }
                    if (name.equals("docid")) {
                        docID = value;
                        myDocTemp.setID(value.toUpperCase());
                    }
                    if (name.equals("docnumber")) {
                        docNumber = value;
                        myDocTemp.setNumber(value.toUpperCase());
                    }
                    if (name.equals("title")) {
                        myDocTemp.setTitle(value);
                    }
                    if (name.equals("duedate")) {
                        myDocTemp.setDueDate(value);
                    }
                    if (name.equals("type")) {
                        myDocTemp.setType(value);
                    }
                    if (name.equals("status")) {
                        myDocTemp.setStatus(value);
                    }
                    if (name.equals("archivedate")) {
                        myDocTemp.setArchiveDate(value);
                    }
                    if (name.equals("olddocfile")) {
                        oldDocFilename = value;
                    }
                    if (name.equals("oldcrdfile")) {
                        oldCRDFilename = value;
                    }
                } else {
                    //processUploadedFile(item);
                    int fileSize = (int)item.getSize();
                    if (name.equals("docfile")) {
                        docFileSize = fileSize;
                    }
                    if (name.equals("crdfile")) {
                        crdFileSize = fileSize;
                    }
                }
            }
        }
        //catch (FileUploadException e) {
        //    outLine = outLine + "FileUploadException caught: " + e.getMessage();
        //    log(outLine);
        //}
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            log(outLine);
        }

    }
    int iDocID = (int) ((docID!=null) ? Integer.parseInt(docID) : 0);
    if (command.equals("updatedocument")) {
        myDoc = new Doc(docRoot, docNumber);
    }
    myDoc.setID(myDocTemp.getID());
    myDoc.setNumber(myDocTemp.getNumber());
    myDoc.setTitle(myDocTemp.getTitle());
    myDoc.setDueDate(myDocTemp.getDueDate());
    myDoc.setType(myDocTemp.getType());
    myDoc.setStatus(myDocTemp.getStatus());
    myDoc.setArchiveDate(myDocTemp.getArchiveDate());
    String docDir = docRoot + "data/documents/" + docNumber.toUpperCase();
    //HttpSession session = request.getSession();
    //User user = new User((String) session.getAttribute("user.name"));
    String nextScript = "home.jsp";
    OutputStream toClient;
    boolean validated = false;

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
//System.out.println("Command: " + command + "\n");

    try {

        if (command.equals("addcomment")) {
            String comment = request.getParameter("comment");
            CmtRsp myComment = new CmtRsp();
            myComment.setUser(user.getUserID());
            myComment.setOrg(user.getOrg());
            myComment.setDocument(iDocID);
            myComment.setComment(comment);
            myComment.add();
            ALog.logActivity(userID, "Comment " + myComment.getNumber() + " was added.");
            nextScript = "commentAccepted.jsp";
            outLine = "none";
            isIFrame = false;

        } else if (command.equals("adddocument") || command.equals("updatedocument")) {
            boolean validInput = true;
            Doc tmpDoc = new Doc(docNumber);
            if ((command.equals("adddocument") && tmpDoc.getID() > 0) || (command.equals("updatedocument") && tmpDoc.getID() != iDocID)) {
                outLine += "Document " + docNumber + " already exists.\\n";
                validInput = false;
            }
            if (docFileSize == 0 && (oldDocFilename == null || oldDocFilename == "" || oldDocFilename.compareTo("   ") <= 0 || oldDocFilename.equals("null") || oldDocFilename.equals("none"))) {
                outLine += "A Document file must be selected.\\n";
                validInput = false;
            }
            if (myDoc.getStatus().equals("archived") && (myDoc.getArchiveDate() == null || myDoc.getArchiveDate().compareTo("   ") <= 0)) {
                outLine += "Archived Status requires an Archived Date to be input.\\n";
                validInput = false;
            }
            if (myDoc.getStatus().equals("archived") && crdFileSize == 0 && (oldCRDFilename == null || oldCRDFilename == "" || oldCRDFilename.compareTo("   ") <= 0 || oldCRDFilename.equals("none"))) {
                outLine += "A Comment Response file must be selected.\\n";
                validInput = false;
            }
             if (!myDoc.getStatus().equals("archived") && myDoc.getArchiveDate() != null && myDoc.getArchiveDate().compareTo("   ") > 0) {
                outLine += "Archived Status must be set to have an Archived Date.\\n";
                validInput = false;
            }
            if (!myDoc.getStatus().equals("archived") && crdFileSize > 0) {
                outLine += "Archived Status must be set to have a Comment Response file.\\n";
                validInput = false;
            }
           if (!validInput) {
                // invalid input do not process
                nextScript = "none";
            } else {
                // input is good proccess it
                // create dir if needed
                //File d1 = new File(docDir);
                //if (!d1.exists()) {
                //    new File(docDir).mkdirs();
                //}
                // Process and write Document files
                iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
                    if (!item.isFormField()) {
                        int size = (int) item.getSize();
                        String name = item.getFieldName();
                        String filename = "";
                        String filetype = "";
                        String outFilename = "";
                        if (size > 0) {
                            filename = item.getName();
                            filetype = filename.substring(filename.lastIndexOf("."));
                        }
                        if (name.equals("docfile")  && size > 0 && filename != null && filename.compareTo("   ") > 0) {
                            outFilename = docNumber.toUpperCase();
                            outFilename = outFilename.replace(' ', '_') + '_' + myDoc.getID();
                            outFilename = outFilename + filetype;
                            myDoc.setFilename(outFilename);
                        }
                        if (name.equals("crdfile")  && size > 0 && filename != null && filename.compareTo("   ") > 0) {
                            outFilename = docNumber.toUpperCase();
                            outFilename = outFilename.replace(' ', '_') + '_' + myDoc.getID();
                            outFilename = outFilename + "-cr" + filetype;
                            myDoc.setArchiveFilename(outFilename);
                        }
                        if (size > 0 && filename != null && filename.compareTo("   ") > 0) {
                            File uploadedFile = new File(docRoot + "/docFiles/" + outFilename);
                            item.write(uploadedFile);
                        }
                    }
                }
                // save doc info
                myDoc.save(docRoot);
                ALog.logActivity(userID, "Document " + myDoc.getNumber() + " was added/updated.");
                outLine = "Document " + docNumber.toUpperCase() + " was added/updated.";
                nextScript = "home.jsp";

            }
        } else if (command.equals("test")) {
            outLine = "none";
        }

        generateResponse(outLine, command, nextScript, validated, isIFrame, response);

    }

//   catch (ClassNotFoundException e) {
//	   outLine = outLine + "ClassNotFoundException caught: " + e.getMessage();
//	   log(outLine);
//   }

    catch (IllegalArgumentException e) {
	    outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
	    log(outLine);
    }

//   catch (ServletException e) {
//	   outLine = outLine + "ServletException caught: " + e.getMessage();
//	   log(outLine);
//   }

//    catch (NullPointerException e) {
//	   outLine = outLine + "NullPointerException caught: " + e.getMessage();
//	   log(outLine);
//    }

    catch (IOException e) {
	   outLine = outLine + "IOException caught: " + e.getMessage();
	   log(outLine);
    }

    catch (Exception e) {
	   outLine = outLine + "Exception caught: " + e.getMessage();
	   log(outLine);
    }
    finally {
	   //log("Test log message\n");
    }


    return outLine;

  }

  // Generate the HTML response
  private void generateResponse(String outLine, String command, String nextScript, boolean validated, boolean isIFrame, HttpServletResponse response)
	  throws IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    out.println("<html>");
    out.println("<head>");
    out.println("<title>Login</title>");
    out.println("</head>");
    out.println("<body BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>");
    out.println("<form name=result method=post>");
    out.println("");
    out.println("<input type=hidden name=command value='" + command + "'>");
    out.println("<script language=\"JavaScript\">\n");
    out.println("<!--\n");

    if (outLine != null && !outLine.equals("none")) {
        out.println("alert('" + outLine + "');\n");
    }

    if (!nextScript.equals("none")) {
        out.println(((isIFrame) ? "parent." : "") + "document.location='" + nextScript + "';\n");
    }

    //out.println("parent.document.location='" + nextScript + "';\n");

    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
