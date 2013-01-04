package gov.ymp.rdc.tags;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import gov.ymp.csi.misc.*;

/** This tag generates a collapsible section on a page with a title  */

public class DoSection extends BodyTagSupport {

    private String ID = null;
    private String sectionTitle = null;
    private boolean setOpen = false;


  public int doStartTag( ) throws JspException{

        try {


    } catch (Exception e) { throw new JspException(e.getMessage( )); }

    return EVAL_BODY_BUFFERED;

  }

  public int doEndTag( ) throws JspException {

      String output = null;

      //this method assumes that attribute properties have been set.
      try {
          String bc = getBodyContent().getString().trim();

          output = HtmlUtils.doSection(ID, sectionTitle, bc, setOpen);

           //Get a JspWriter to produce the tag's output
          JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          out.println(output);

     } catch (Exception e) { System.out.println("DoSection-" + e); throw new JspException(e.getMessage( )); }


      return EVAL_PAGE;

  }//doTag

  //Attribute-related setter methods

  public void setName(String name){

    this.ID = name;

  }

  public void setTitle(String title){

    this.sectionTitle = title;

  }

  public void setIsOpen(String isOpen){

    this.setOpen = (isOpen.toLowerCase().equals("true")) ? true : false;

  }

  public void release( ){

     ID = null;
     sectionTitle = null;
     setOpen = false;

  }// release

}// DoSection
