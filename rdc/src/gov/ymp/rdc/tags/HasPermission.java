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

/** This tag displays the enclosed section only if the user has the given permission  */

public class HasPermission extends BodyTagSupport {

    private String permission = null;


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
          if (session.getAttribute("user.name")  != null && !((String) session.getAttribute("user.name")).toLowerCase().equals("guest")) {
              Position pos = (Position) session.getAttribute("user.position");
              HashMap perMap = (HashMap) session.getAttribute("user.permissionmap");
//System.out.println("HasPermission - got here - 1");
              long perm = ((Long) perMap.get(permission)).longValue();
              if (pos.belongsTo(perm)) {
                  String bc = getBodyContent().getString().trim();
                  output = bc;
              }
          }


           //Get a JspWriter to produce the tag's output
          JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          out.println(output);

     } catch (Exception e) { System.out.println("HasPermission-" + e); throw new JspException(e.getMessage( )); }


      return EVAL_PAGE;

  }//doTag

  //Attribute-related setter methods

  public void setPermission(String val){

    this.permission = val;

  }

  public void release( ){

     permission = null;

  }// release

}// HasPermission
