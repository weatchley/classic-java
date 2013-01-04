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

/** This tag will close the current CSI session.  */

public class SessionClose extends BodyTagSupport {

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
//System.out.println("SessionClose - got here - 1");
          if (session.getAttribute("user.name")  != null && !((String) session.getAttribute("user.name")).toLowerCase().equals("guest")) {
              InitialContext myInitCtx = new InitialContext();
              String productionStatus = (String) myInitCtx.lookup("java:comp/env/ProductionStatus");
              HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
              Cookie cookie = null;
              //Get an array of Cookies associated with this domain
              Cookie[] cookies = request.getCookies( );
              String cookieName = "csiSession-" + productionStatus;
              if (cookies != null){
                  for (int i = 0; i < cookies.length; i++){
                      if (cookies[i].getName( ).equals(cookieName)){
                          cookie = cookies[i];
                      }
                  }//end for
              }//end if
              if (cookie != null) {
                  DbConn myConn = null;
                  try {
                      myConn = new DbConn();
                  } catch (Exception e) { myConn = null; }
                  String sessionID = cookie.getValue( );
                  SessionControler sess = new SessionControler(myConn, sessionID);
                  if (sess.getClosed() == null && myConn != null) {
                      sess.setClosed(new java.util.Date());
                      sess.save(myConn);
                      myConn.release();
                      myConn = null;
                  }
              }
              session.setAttribute("user.id", null);
              session.setAttribute("user.name", null);
              session.setAttribute("user.fullname", null);
              session.setAttribute("user.position", null);
              session.setAttribute("user.positionid", null);
              session.setAttribute("user.person", null);
              session.setAttribute("user.permissionmap", null);
              session.setAttribute("user.authenticationlevel", null);
          }


           //Get a JspWriter to produce the tag's output
          //JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          //out.println(output);

     } catch (Exception e) { System.out.println("SessionClose-" + e); throw new JspException(e.getMessage( )); }


      return EVAL_PAGE;

  }//doTag

  //Attribute-related setter methods


  public void release( ){

     //

  }// release

}// SessionClose
