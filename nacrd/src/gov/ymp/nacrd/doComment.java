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


public class doComment extends HttpServlet {
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
    int commentID = Integer.parseInt(request.getParameter("commentid"));
    int iDocID = ((docID!=null) ? Integer.parseInt(docID) : 0);
    Doc myDoc = new Doc(iDocID);

    HttpSession session = request.getSession();
    User user = new User((String) session.getAttribute("user.name"));
    int userID = user.getUserID();
    String nextScript = "home.jsp";
    OutputStream toClient;
    boolean validated = false;
    String comment = "";
    String resp = "";

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
//System.out.println("Command: " + command + "\n");

    try {

        if (command.equals("updatecomment")) {
            nextScript = "home.jsp";
            outLine = "none";
            int subCommentCount = Integer.parseInt(request.getParameter("subcommentcount"));
            CmtRsp cmt = new CmtRsp(commentID);
            resp = request.getParameter("response");
            cmt.setResponse((resp != null && resp.compareTo("             ") > 0) ? resp : null);
            cmt.setResponder(userID);
            cmt.update();
            ALog.logActivity(userID, "Comment " + cmt.getNumber() + " was updated for document # " + myDoc.getID() + " (" + myDoc.getNumber() + ").", cmt.getID());
            if (subCommentCount > 0) {
                for (int i=0; i<subCommentCount; i++) {
                    int subComID = Integer.parseInt(request.getParameter("commentid" + i));
                    comment = request.getParameter("comment" + i);
                    resp = request.getParameter("response" + i);
                    CmtRsp sCmt = null;
                    if (subComID == 0) {
                        sCmt = new CmtRsp();
                        sCmt.setUser(userID);
                        sCmt.setOrg((String) session.getAttribute("user.org"));
                        sCmt.setDocument(iDocID);
                        sCmt.setRefersTo(commentID);
                    } else {
                        sCmt = new CmtRsp(subComID);
                    }
                    sCmt.setResponder(userID);
                    sCmt.setComment((comment != null && comment.compareTo("             ") > 0) ? comment : null);
                    sCmt.setResponse((resp != null && resp.compareTo("             ") > 0) ? resp : null);
                    if (sCmt.getIsNew()) {
                        sCmt.add();
                        ALog.logActivity(userID, "Comment " + sCmt.getNumber() + " was added for document # " + myDoc.getID() + " (" + myDoc.getNumber() + ").", i);
                    } else {
                        sCmt.update();
                        ALog.logActivity(userID, "Comment " + sCmt.getNumber() + " was updated for document # " + myDoc.getID() + " (" + myDoc.getNumber() + ").", sCmt.getID());
                    }

                }
            }


        } else if (command.equals("test")) {
            outLine = "none";
        }

        generateResponse(outLine, command, nextScript, validated, response);

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
  private void generateResponse(String outLine, String command, String nextScript, boolean validated, HttpServletResponse response)
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
        out.println("parent.document.location='" + nextScript + "';\n");
    }

    //out.println("parent.document.location='" + nextScript + "';\n");

    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
