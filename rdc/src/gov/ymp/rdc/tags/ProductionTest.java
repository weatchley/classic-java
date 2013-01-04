package gov.ymp.rdc.tags;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import gov.ymp.csi.misc.*;
import javax.naming.*;

/** This tag displays the enclosed section only if the environment is production (or the reverse).  */

public class ProductionTest extends BodyTagSupport {

    private String status = "prod";
    private boolean onMatch = false;


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
          if ( (!onMatch && !myProductionStatus.equals(status.toLowerCase())) ||
               (onMatch && myProductionStatus.equals(status.toLowerCase()))) {
              String bc = getBodyContent().getString().trim();
              output = bc;
          }


           //Get a JspWriter to produce the tag's output
          JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          out.println(output);

     } catch (Exception e) { System.out.println("ProductionTest-" + e); throw new JspException(e.getMessage( )); }


      return EVAL_PAGE;

  }//doTag

  //Attribute-related setter methods

  public void setOnMatch(String val){

    this.onMatch = (val.toLowerCase().equals("true")) ? true : false;

  }

  public void setStatus(String val){

    this.status = val;

  }

  public void release( ){

     status = null;
     onMatch = false;

  }// release

}// ProductionTest
