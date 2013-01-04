package gov.ymp.csiTest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
// Support classes
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.awt.*;
//import oracle.jdbc.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.db.ALog.*;
//import gov.ymp.csi.aaa.*;
import gov.ymp.csi.people.*;


/**
* CsiTest is a test servlet to try and log in using the CSI POC, it must
* have a username, domain that is passed in
* as part of the servlet request, allong with the password to the username,
* this requries an html form to use this servlet as its result target.
*
* @author   Bill Atchley
*/
public class CsiTest extends HttpServlet {
    // Handle the GET HTTP Method
    public void doGet(HttpServletRequest request,
              HttpServletResponse response)
       throws IOException {

      String message = "";
      try {
          message = processRequest(request,response);
          //if (message.length() > 0) {
          //    generateResponse(message, response);
          //}
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
          //if (message.length() > 0) {
          //    generateResponse(message, response);
          //}
      }
      finally {
      }
    }


    // Process the request
    private String processRequest(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("username");
        String password = request.getParameter("pass");
        String domain = request.getParameter("domain");
        String outLine = "";
        OutputStream toClient;
        try {
            //User cUser = new User(userName);
            Person cUser = new Person(userName);
            DbConn myConn = new DbConn("CSI");
            if (cUser.authenticate(password, domain, myConn) != true) {
//                ALog.logError(0, "N/A", 0, "Failed login under user " + userName);
                outLine += "Login Failed<br>\n";
            } else {
                outLine += "Login Successful<br>\n";
                outLine += "User ID = " + cUser.getID() + "<br>\n";
                outLine += "User Name = " + cUser.getUserName() + "<br>\n";
                outLine += "Fisrt Name = " + cUser.getFirstName() + "<br>\n";
                outLine += "Last Name = " + cUser.getLastName() + "<br>\n";
                outLine += "E-Mail = " + cUser.getEmail() + "<br>\n";
                outLine += "Phone = " + cUser.getPhone() + "<br>\n";
            }
            outLine += "<br>\n";

            Connection conn = myConn.conn;

            // Create a Statement
            Statement stmt = conn.createStatement();

// get date
            //ResultSet rsetCount = stmt.executeQuery ("SELECT count(*) FROM atchleyb.users ORDER BY username");
            ResultSet rset = stmt.executeQuery ("SELECT SYSDATE FROM dual");
            //ResultSet rset = stmt.executeQuery ("SELECT getDate()");

            rset.next();

            outLine += rset.getString(1);
            generateResponse(outLine, response);

            myConn.release();
       }

//   catch (ClassNotFoundException e) {
//       outLine = outLine + "ClassNotFoundException caught: " + e.getMessage();
//       log(outLine);
//   }

       catch (IllegalArgumentException e) {
           outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
           log(outLine);
       }

//   catch (ServletException e) {
//       outLine = outLine + "ServletException caught: " + e.getMessage();
//       log(outLine);
//   }

       catch (NullPointerException e) {
           outLine = outLine + "NullPointerException caught: " + e.getMessage();
           log(outLine);
       }

       catch (IOException e) {
           outLine = outLine + "IOException caught: " + e.getMessage();
           log(outLine);
       }

       catch (SQLException e) {
           outLine = outLine + "SQLException caught: " + e.getMessage();
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
    private void generateResponse(String message,
                  HttpServletResponse response)
        throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>CSI AD-DB Test Servlet</TITLE>");
        out.println("</HEAD>");
        out.println("<BODY BGCOLOR=\"white\">");
        //out.println("The test data is: <BR>");
        //out.println("<BLOCKQUOTE><B>");
        out.println(message);
        //out.println("<B></BLOCKQUOTE>");
        out.println("</BODY>");
        out.println("</HTML>");

        out.close();
    }
}
