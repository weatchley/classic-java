package gov.ymp.rdc.tags;

import java.io.*;import java.lang.*;

import javax.servlet.*;
import javax.servlet.http.*;import javax.servlet.jsp.*;import java.util.*;
import javax.servlet.jsp.tagext.*;
import gov.ymp.csi.misc.*;
import javax.naming.*;

/** This tag generates a display of environment variables.  */

public class EnvDisplay extends BodyTagSupport {


  public int doStartTag( ) throws JspException{

        try {


    } catch (Exception e) { throw new JspException(e.getMessage( )); }

    return EVAL_BODY_BUFFERED;

  }

  public int doEndTag( ) throws JspException {

      String output = "";

      //this method assumes that attribute properties have been set.
      try {
          InitialContext myInitCtx = new InitialContext();
          String myProductionStatus = (String) myInitCtx.lookup("java:comp/env/ProductionStatus");
          myProductionStatus = (myProductionStatus != null) ? myProductionStatus : "test";

          if (!myProductionStatus.equals("prod")) {
              output += "<br>\n<hr width=80% >\n";

              File[] roots = File.listRoots();
              String rootList = "";
              String outString = "";

              Enumeration myEnum = null;
              //String docRoot = session.getServletContext().getRealPath("/");

              for (int myi=0; myi<roots.length; myi++){
                  rootList += roots[myi] + ",\n";
              }

              HttpSession session = pageContext.getSession();
              ServletContext serv = session.getServletContext();
              HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
              //
              outString += "<br><b><i>Session Data:</i></b> <br>\n";
              myEnum = session.getAttributeNames();
              while (myEnum.hasMoreElements()) {
                  String name = (String) myEnum.nextElement();
                  outString += "<b>" + name + ":</b>" + session.getAttribute(name) + "<br>";
              }
              //Person tmpUsr = (Person) session.getAttribute("user.person");
              //outString += "<b>Person's E-Mail:</b>" + tmpUsr.getEmail() + "<br>";

              outString += "<br><b><i>Context Data:</i></b> <br>\n";
              myEnum = serv.getAttributeNames();
              while (myEnum.hasMoreElements()) {
                  String name = (String) myEnum.nextElement();
                  outString += HtmlUtils.colapseString((name + "_string"),"<b>" + name + ":</b>" + session.getServletContext().getAttribute(name), 80) + "<br>";
              }

              outString += "<br><b><i>Init Data:</i></b> <br>\n";
              myEnum = serv.getInitParameterNames();
              while (myEnum.hasMoreElements()) {
                  String name = (String) myEnum.nextElement();
                  outString += "<b>" + name + ":</b>" + serv.getInitParameter(name) + "<br>";
              }

              outString += "<br><b><i>Parameter Data:</i></b> <br>\n";
              myEnum = request.getParameterNames();
              while (myEnum.hasMoreElements()) {
                  String name = (String) myEnum.nextElement();
                  String values[] = request.getParameterValues(name);
                  outString += "<b>" + name + ":</b>" + request.getParameter(name) + "<br>";
                  if (values != null) {
                      String tempStr = "";
                      for (int myi = 0; myi < values.length; myi++) {
                          //tempStr += name + " (" + myi + "): " + values[myi] + ", ";
                          outString += name + " (" + myi + "): " + values[myi] + ", ";
                      }
                      //outString += HtmlUtils.colapseString(("envTestA" + name),tempStr, 60);
                  }
                  outString += "<br>";
              }

              outString += "<br><b><i>ENV Entries:</i></b> <br>\n";
              //Context initCtx = new InitialContext();
              NamingEnumeration myEnum2 = myInitCtx.listBindings("java:comp/env");
              outString += "<table border=1 cellspacing=0 cellpadding=0><tr><td><b>Name</b></td><td><b>Type</b></td><td><b>Value</b></td></tr>";
              // We're using JDK 1.2 methods; that's OK since J2EE requires JDK 1.2
              while (myEnum2.hasMore(  )) {
                  Binding binding = (Binding) myEnum2.next(  );
                  outString += "<tr><td>" + binding.getName(  ) + "</td>";
                  outString += "<td>" + binding.getClassName(  ) + "</td>";
                  outString += "<td>" + binding.getObject(  ) + "</td></tr>";
              }
              outString += "</table>";

              String outString2 = "";

              outString2 += "<b>RootList:</b> " + rootList + "<br>";
              outString2 += "<b>ServerName:</b> " + request.getServerName() + "<br>";
              outString2 += "<b>ServerSoftware:</b> " + session.getServletContext().getServerInfo() + "<br>";
              outString2 += "<b>ServerContextName:</b> " + session.getServletContext().getServletContextName() + "<br>";
              outString2 += "<b>ServerProtocol:</b> " + request.getProtocol() + "<br>";
              outString2 += "<b>ServerScheme:</b> " + request.getScheme() + "<br>";
              outString2 += "<b>ServerPort:</b> " + request.getServerPort() + "<br>";
              outString2 += "<b>RequestMethod:</b> " + request.getMethod() + "<br>";
              outString2 += "<b>PathInfo:</b> " + request.getPathInfo() + "<br>";
              outString2 += "<b>PathTranslated:</b> " + request.getPathTranslated() + "<br>";
              outString2 += "<b>ScriptName:</b> " + request.getServletPath() + "<br>";
              outString2 += "<b>DocumentRoot:</b> " + session.getServletContext().getRealPath("/") + "<br>";
              outString2 += "<b>QueryString:</b> " + request.getQueryString() + "<br>";
              outString2 += "<b>RemoteHost:</b> " + request.getRemoteHost() + "<br>";
              outString2 += "<b>RemoteAddr:</b> " + request.getRemoteAddr() + "<br>";
              outString2 += "<b>AuthType:</b> " + request.getAuthType() + "<br>";
              outString2 += "<b>RemoteUser:</b> " + request.getRemoteUser() + "<br>";
              outString2 += "<b>ContentType:</b> " + request.getContentType() + "<br>";
              outString2 += "<b>ContentLength:</b> " + request.getContentLength() + "<br>";
              outString2 += "<b>HTTPAccept:</b> " + request.getHeader("Accept") + "<br>";
              outString2 += "<b>HTTPUserAgent:</b> " + request.getHeader("User-Agent") + "<br>";
              outString2 += "<b>HTTPReferer:</b> " + request.getHeader("Referer") + "<br>";

//System.out.println("EnvDisplay - Got here 1");
              Cookie cookie = null;
              //Get an array of Cookies associated with this domain
              Cookie[] cookies = request.getCookies( );
              boolean hasCookies = false;
              String outString3 = "";

              //if cookies contains an array and not a null value,
              //then we can display information about the cookies.
                  if (cookies != null) hasCookies = true;

              // display the name/value of each cookie
              if (hasCookies){
                  for (int i = 0; i < cookies.length; i++){
                      cookie = cookies[i];
                      outString3 += "Name: \"" + cookie.getName( ) + "\"";
                      outString3 += " - Value: \"" + cookie.getValue( ) + "\"<br>";
                  }//for

              } else {
                  outString3 += "This request did not include any cookies";
              }
              outString3 = "<br>" + HtmlUtils.doSection("envTest2", "Cookies", outString3, false) + "<br>\n";


              outString = "<table border=0 width=80% ><tr><td>" + outString2 + outString + outString3 + "</td><tr></table>";

              output += HtmlUtils.doSection("envTest1", "Environment Information", outString, false) + "<br>\n";
          }

           //Get a JspWriter to produce the tag's output
          JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          out.println(output);

     } catch (Exception e) { System.out.println("EnvDisplay-" + e); throw new JspException(e.getMessage( )); }


      return EVAL_PAGE;

  }//doEndTag

  public void release( ){

  }// release

}// DoSection
