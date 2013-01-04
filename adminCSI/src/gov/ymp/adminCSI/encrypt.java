package gov.ymp.adminCSI;

//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import javax.servlet.http.*;
// Support classes
//import java.io.IOException;
//import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.awt.*;
import java.lang.*;
import java.text.*;
//import oracle.jdbc.*;


public class encrypt extends HttpServlet {
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

// ceasar cipher
  public char caesar(char c, int key) {
      String lower = "abcdefghijklmnopqrstuvwxyz";
      String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      String s;

      if (Character.isLetter(c)) {
          s = (Character.isUpperCase(c)) ? upper : lower;

          int i = 0;
          while (i < 26) {
          	  // extra +26 below because key might be negative
              if (c == s.charAt(i)) return s.charAt((i + key + 26)%26);
                 i++;
          }

      } else {
          return c;
      }
      return c;
  }

// do encrypt
  public String doEncrypt(String text, String key, int depth) {
      depth = (depth > 0) ? depth : 5;
      String outLine = "";
      String testKey = key;
      int charVal;
      NumberFormat form = NumberFormat.getInstance();
      form.setMinimumIntegerDigits(3);
      while (testKey.length() < (text.length() + depth)) {
          testKey += key;
      }
      for (int i=0; i<text.length(); i++) {
          char temp = text.charAt(i);
          for (int j=0; j<depth; j++) {
              char keySeg = testKey.charAt(i + j);
              temp = caesar(temp, (i+1));
              charVal = temp ^ keySeg;
              temp = (char) charVal;
          }
          charVal = (int) temp;
          outLine += form.format(charVal);
      }

      return outLine;
  }

// do decrypt
  public String doDecrypt(String text, String key, int depth) {
      depth = (depth > 0) ? depth : 5;
      String outLine = "";
      String testKey = key;
      int charVal;
      String myText = "";
      for (int i=0; i<text.length(); i += 3) {
          String temp = text.substring(i, i+3);
          int tempInt = Integer.parseInt(temp);
          myText += (char) tempInt;
      }
      while (testKey.length() < (myText.length() + depth)) {
          testKey += key;
      }
      for (int i=0; i<myText.length(); i++) {
          char temp = myText.charAt(i);
          for (int j=0; j<depth; j++) {
              char keySeg = testKey.charAt(i + (depth - j -1));
              charVal = temp ^ keySeg;
              temp = (char) charVal;
              temp = caesar(temp, ((26-(i+1))%26));
          }
          outLine += temp;
      }

      return outLine;
  }

  // Process the request
  private String processRequest(HttpServletRequest request, HttpServletResponse response) {
    String command = request.getParameter("command");
    String key = request.getParameter("key");
    String skeyLength = request.getParameter("keyLength");
    int keyLength = (skeyLength != null && skeyLength.compareTo(" ") > 0) ? Integer.parseInt(skeyLength) : 32;
    String inputstring = request.getParameter("inputstring");
    String outLine = "";
    OutputStream toClient;

    command = (command != null && command.compareTo(" ") > 0) ? command : "form";
    key = (key != null && key.compareTo(" ") > 0) ? key : "";
    inputstring = (inputstring != null && inputstring.compareTo(" ") > 0) ? inputstring : "";

   try {

    if (command.equals("genkey")) {
        String testVals = "0123456789abcdefghijklmnopqrstuvwxyz";
        int countOfTestVals = 36;
        //String testVals = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //int countOfTestVals = 62;
        java.util.Random generator = new java.util.Random(System.currentTimeMillis());
        String KeyID = "";
        for (int pos = 0; (pos < keyLength); pos++) {
            int loc = generator.nextInt(countOfTestVals);
            KeyID = KeyID + testVals.charAt(loc);
//log(loc + " - " + testVals.charAt(loc));
        }
        key = KeyID;
        outLine = key;

    } else if (command.equals("encrypt")) {
        outLine = doEncrypt(inputstring, key, 5);
    } else if (command.equals("decrypt")) {
        outLine = doDecrypt(inputstring, key, 5);
    }

    generateResponse(outLine, command, key, keyLength, inputstring, response);

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

   catch (NullPointerException e) {
	   outLine = outLine + "NullPointerException caught: " + e.getMessage();
	   log(outLine);
   }

   catch (IOException e) {
	   outLine = outLine + "IOException caught: " + e.getMessage();
	   log(outLine);
   }

//   catch (SQLException e) {
//	   outLine = outLine + "SQLException caught: " + e.getMessage();
//	   log(outLine);
//   }

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
  private void generateResponse(String outLine, String command, String key, int keyLength, String inputstring,
				HttpServletResponse response)
	  throws IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    out.println("<html>");
    out.println("<head>");
    out.println("<title>Encryption</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<form name=encryption method=post action=encrypt>");
    out.println("");
    out.println("<input type=hidden name=command value='form'>");
    out.println("");
    out.println("<h1>My Crypt</h1>");
    out.println("<br>");
    out.println("<table>");
    out.println("<tr><td>Key </td><td><input type=text size=50 name=key value='" + key + "'></td></tr>");
    out.println("<tr><td>Key Length </td><td><input type=text size=2 name=keylength value='" + keyLength + "'></td></tr>");
    out.println("<tr><td>Input String </td><td><input type=text size=50 name=inputstring value='" + inputstring + "'></td></tr>");
    if (!command.equals("form")) {
        out.println("<tr><td>Results: </td><td>" + outLine + "</td></tr>");
    }
    out.println("<tr><td><input type=button name=doencrypt value='Gen Key' onClick='javascript:encryption.command.value=\"genkey\";encryption.submit();'></td><td>&nbsp;</td></tr>");
    out.println("<tr><td><input type=button name=doencrypt value='Encrypt' onClick='javascript:encryption.command.value=\"encrypt\";encryption.submit();'></td><td>&nbsp;</td></tr>");
    out.println("<tr><td><input type=button name=dodecrypt value='Decrypt' onClick='javascript:encryption.command.value=\"decrypt\";encryption.submit();'></td><td>&nbsp;</td></tr>");
    out.println("</table>\n</form>\n</body>\n</html>");

    out.close();
  }
}
