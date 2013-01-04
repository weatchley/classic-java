package gov.ymp.rdc.tags;

import java.io.*;
import java.lang.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.people.*;
import javax.naming.*;

/** This tag determines if the cached version of the page should be used or the "Live" one  */

public class CacheControl extends BodyTagSupport {

    private String fileLocation = null;

    private boolean cacheAvailable = false;
    private boolean useCache = false;


  public int doStartTag( ) throws JspException{

        try {


    } catch (Exception e) { throw new JspException(e.getMessage( )); }

    return EVAL_BODY_BUFFERED;

  }

  public int doEndTag( ) throws JspException {

      String output = "";

      //this method assumes that attribute properties have been set.
      try {
          HttpSession session = pageContext.getSession();
          HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
          String buildCache = request.getParameter("buildcache");
//System.out.println("CacheControl - got here - 1");
          String bc = getBodyContent().getString().trim();
          if ((session.getAttribute("user.name")  != null && !((String) session.getAttribute("user.name")).toLowerCase().equals("guest")) ||
                (buildCache != null && buildCache.equals("true"))) {
              useCache = false;
              if (buildCache != null && buildCache.equals("true")) {
                  session.setAttribute("page.buldCache", "true");
              } else {
                  session.setAttribute("page.buldCache", "false");
              }
          } else {
              File f = new File(fileLocation);
              if (f.exists() && f.isFile() && f.canRead()) {
                  useCache = true;
              }
          }

          if (useCache) {
              output = "";
              BufferedReader in = new BufferedReader(new FileReader(fileLocation));
              String line;
              while ((line = in.readLine()) != null) {
                  output += line + "\n";
              }
              in.close();

          } else {
              output = bc;
          }


           //Get a JspWriter to produce the tag's output
          JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          out.println(output);
          session.setAttribute("page.buildCache", "false");

     } catch (Exception e) { System.out.println("CacheControl-" + e); throw new JspException(e.getMessage( )); }


      return EVAL_PAGE;

  }//doTag

  //Attribute-related setter methods

  public void setFileLocation(String val){

    this.fileLocation = val;

  }

  public void release( ){

     fileLocation = null;

  }// release

}// CacheControl
