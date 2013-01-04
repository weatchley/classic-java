package gov.ymp.rdc.tags;

import java.io.*;
import java.lang.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.auth.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.systems.*;
import javax.naming.*;

/** This tag tests to see if a CSI session is active for the browser on the current server.  */

public class SessionTest extends BodyTagSupport {

    //

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
          boolean buildCache = ((request.getParameter("buildcache") != null && request.getParameter("buildcache").toLowerCase().equals("true"))) ? true : false;
          if (buildCache) {
              session.setAttribute("page.buildCache", "true");
          }
          SessionControler.loader(request);


           //Get a JspWriter to produce the tag's output
          //JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          //out.println(output);

     } catch (Exception e) { System.out.println("SessionTest-" + e); throw new JspException(e.getMessage( )); }


      return EVAL_PAGE;

  }//doTag

  //Attribute-related setter methods


  public void release( ){

     //

  }// release

}// SessionTest
