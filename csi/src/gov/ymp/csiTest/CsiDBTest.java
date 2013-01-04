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
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.items.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.people.*;


/**
* CsiDBTest is a test servlet to connect to the oracle server using the
* specifications that are saved as part of the CSI POC.
*
* @author   Bill Atchley
*/
public class CsiDBTest extends HttpServlet {
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
        String url = "jdbc:oracle:thin:@ydoradev:1521:ydor";
        String outLine = "";
        OutputStream toClient;
        DbConn myConn = new DbConn("CSI");
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            Statement stmt = conn.createStatement ();

// get data
            //ResultSet rsetCount = stmt.executeQuery ("SELECT count(*) FROM atchleyb.users ORDER BY username");
            ResultSet rset = stmt.executeQuery ("SELECT SYSDATE FROM dual"); // for Oracle
            //ResultSet rset = stmt.executeQuery ("SELECT getdate()"); // for MS-SQL

            rset.next();

//System.out.println("Got here - CsiDBTest 1");
            java.util.Date testDate1 =  Utils.toDate("11/29/1960");
//System.out.println("Got here - CsiDBTest 2");
            //java.sql.Date testDate = (java.sql.Date) testDate1;
            java.sql.Date testDate = new java.sql.Date(testDate1.getTime());
//System.out.println("Got here - CsiDBTest 3");

            outLine += rset.getString(1);

            UNID utest1 = new UNID();
            utest1.create();
            utest1.setType("test type");
            utest1.setStatus("test");
            utest1.save();

            UNID utest2 = new UNID();
            utest2.create("test", utest1.getID());
            utest2.save();

            UNID utest3 = new UNID(utest2.getID());
            utest3.setStatus("test2");
            utest3.save();

            outLine += "<br>\nUNID<br>\nID: " + utest3.getID() + "<br>\n";
            outLine += "Type: " + utest3.getType() + "<br>\n";
            //outLine += "Parent: " + utest3.getParent() + "<br>\n";
            outLine += "Status: " + utest3.getStatus() + "<br>\n";

            USet s1 = new USet();
            s1.setDescription("Test uset 1");
            s1.clearItems();
            long id = 1001L;
            s1.addItem(id);
            id = 1002L;
            s1.addItem(id);
            s1.save();
            s1.setDescription("Test uset 1a");
            USet s2 = new USet();
            s2.setDescription("Test uset 2");
            s2.setExpirationDate(testDate1);
            //s2.setParent(s1.getID());
            s2.save();
            s2.setDescription("Test uset 2a");
            USet s3 = new USet();
            s3.setDescription("Test uset 3");
            //s3.setParent(s2.getID());
            s3.save();
            s3.setDescription("Test uset 3a");
            for (int i=1005; i<1010; i++) {
                id = 0L + i;
                s3.addItem(id);
            }
            s2.addItem(s3.getID());
            s1.addItem(s2.getID());

            s1.save(myConn);
            s2.save(myConn);
            s3.save(myConn);

            myConn.conn.commit();

            long [] testList1 = s1.getItemsArray();
            long [] testList2 = s1.getItemsArrayRecursive(myConn);

            outLine += "<br>\nUset list1:<br>\n";
            for (int i=0; i<testList1.length; i++) {outLine += testList1[i] + ", ";}
            outLine += "<br>\n";
            outLine += "<br>\nUset list2:<br>\n";
            for (int i=0; i<testList2.length; i++) {outLine += testList2[i] + ", ";}
            outLine += "<br>\n";

            USet s4 = new USet(s2.getID());

            outLine += "<br>\nUSet<br>\nID: " + s4.getID() + "<br>\n";
            outLine += "Expiration Date: " + s4.getExpirationDate() + "<br>\n";
            outLine += "Description: " + s4.getDescription() + "<br>\n";
            //outLine += "Parent: " + s4.getParent() + "<br>\n";

            UList l1 = new UList();
            l1.setDescription("Test ulist 1");
            l1.clearItems();
            id = 1001L;
            l1.addItem(id);
            id = 1002L;
            l1.addItem(id);
            l1.save();
            l1.setDescription("Test ulist 1a");
            UList l2 = new UList();
            l2.setDescription("Test ulist 2");
            l2.setExpirationDate(testDate1);
            l2.save();
            l2.setDescription("Test ulist 2a");
            UList l3 = new UList();
            l3.setDescription("Test ulist 3");
            l3.save();
            l3.setDescription("Test ulist 3a");
            for (int i=1005; i<1010; i++) {
                id = 0L + i;
                l3.addItem(id);
            }
            l2.addItem(l3.getID());
            l1.addItem(l2.getID());

            l1.save(myConn);
            l2.save(myConn);
            l3.save(myConn);

            testList1 = l1.getItemsArray();
            testList2 = l1.getItemsArrayRecursive(myConn);

            outLine += "<br>\nULiat list1:<br>\n";
            for (int i=0; i<testList1.length; i++) {outLine += testList1[i] + ", ";}
            outLine += "<br>\n";
            outLine += "<br>\nUList list2:<br>\n";
            for (int i=0; i<testList2.length; i++) {outLine += testList2[i] + ", ";}
            outLine += "<br>\n";

            Person per = new Person("atchleyb");
            per.lookup(myConn);
            outLine += "<br>Atchleyb username in Notes: " + per.getSystemUsername(4L) + ", " + per.getSystemUsername(4L) + "<br>\n";


            TextItem ti = new TextItem();
            ti.setText("This is a test text item");
            ti.setLink("http://www.google.com");
            //ti.setDate1(testDate);
            //ti.setDate2(testDate);
            ti.setDate1(Utils.castDate(Utils.toDate("11/29/1960")));
            ti.setDate2(Utils.toDate("12/25/2006"));
            ti.save(myConn);
            long tID = ti.getID();

            TextItem ti2 = new TextItem(tID, myConn);
            outLine += "<br>TextItem test: ID: " + ti2.getID() + ", Date1: " + ti2.getDate1() + ", Date2: " + ti2.getDate2() +
                ", Text: " + ti2.getText() + ", Link: " + ti2.getLink() + ", Status: " + ti2.getStatus() + "<br>\n";


            myConn.release();



            generateResponse(outLine, response);

       }

//   catch (ClassNotFoundException e) {
//       outLine = outLine + "ClassNotFoundException caught: " + e.getMessage();
//       log(outLine);
//   }

       catch (IllegalArgumentException e) {
           outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
           log(outLine);
            myConn.release();
       }

//   catch (ServletException e) {
//       outLine = outLine + "ServletException caught: " + e.getMessage();
//       log(outLine);
//   }

       catch (NullPointerException e) {
           outLine = outLine + "NullPointerException caught: " + e.getMessage();
           log(outLine);
            myConn.release();
       }

       catch (IOException e) {
           outLine = outLine + "IOException caught: " + e.getMessage();
           log(outLine);
            myConn.release();
       }

       catch (SQLException e) {
           outLine = outLine + "SQLException caught: " + e.getMessage();
           log(outLine);
            myConn.release();
       }

       catch (Exception e) {
           outLine = outLine + "Exception caught: " + e.getMessage();
           log(outLine);
            myConn.release();
       }
       finally {
           //log("Test log message\n");
            myConn.release();
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
        out.println("<TITLE>DB Test Servlet</TITLE>");
        out.println("</HEAD>");
        out.println("<BODY BGCOLOR=\"white\">");
        out.println("The test data is: <BR>");
        //out.println("<BLOCKQUOTE><B>");
        out.println(message);
        //out.println("<B></BLOCKQUOTE>");
        out.println("</BODY>");
        out.println("</HTML>");

        out.close();
    }
}
