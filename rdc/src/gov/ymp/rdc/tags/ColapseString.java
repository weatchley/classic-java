package gov.ymp.rdc.tags;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import gov.ymp.csi.misc.*;

/** This tag generates a collapsible string, a string that is too long can be shortened and opened up to its full length when needed  */

public class ColapseString extends BodyTagSupport {

    private String ID = null;
    private int length = 0;
    private boolean setOpen = false;
    private boolean activeText = true;


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

          output = HtmlUtils.colapseString(ID, bc, length, setOpen, activeText);

           //Get a JspWriter to produce the tag's output
          JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          out.println(output);

     } catch (Exception e) { System.out.println("ColapseString-" + e); throw new JspException(e.getMessage( )); }


      return EVAL_PAGE;

  }//doTag

  //Attribute-related setter methods

  public void setName(String name){

    this.ID = name;

  }

  public void setMaxLen(String maxLen){

    this.length = Integer.parseInt(maxLen);

  }

  public void setIsOpen(String isOpen){

    this.setOpen = (isOpen.toLowerCase().equals("true")) ? true : false;

  }

  public void setActiveText(String val){

    this.activeText = (val.toLowerCase().equals("true")) ? true : false;

  }

  public void release( ){

     ID = null;
     length = 0;
     setOpen = false;
     activeText = true;

  }// release

}// ColapseString
