package gov.ymp.rdc.tags;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import gov.ymp.csi.misc.*;
import javax.naming.*;

/** This tag displays the enclosed section only if the user is authenticated.  */

public class IsAuthenticated extends BodyTagSupport {

    private boolean doOpposite = false;


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
          boolean buildCache = (session.getAttribute("page.buildCache")  != null && ((String) session.getAttribute("page.buildCache")).toLowerCase().equals("true")) ? true : false;
          if ((!doOpposite && session.getAttribute("user.name")  != null && !((String) session.getAttribute("user.name")).toLowerCase().equals("guest") && !buildCache) ||
                (doOpposite && (session.getAttribute("user.name")  == null || ((String) session.getAttribute("user.name")).toLowerCase().equals("guest") || buildCache))) {
              String bc = getBodyContent().getString().trim();
              output = bc;
          }


           //Get a JspWriter to produce the tag's output
          JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          out.println(output);

     } catch (Exception e) { System.out.println("IsAuthenticated-" + e); throw new JspException(e.getMessage( )); }


      return EVAL_PAGE;

  }//doTag

  //Attribute-related setter methods

  public void setDoOpposite(String val){

    this.doOpposite = (val.toLowerCase().equals("true")) ? true : false;

  }

  public void release( ){

     doOpposite = false;

  }// release

}// IsAuthenticated
