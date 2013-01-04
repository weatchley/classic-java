package gov.ymp.rdc.tags;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.items.*;
import java.util.regex.*;
import org.json.*;

/** This tag generates a horizontal menu for the top of the page and will optionally display the current user.  */

public class TopMenu extends BodyTagSupport {

    private String ID = "RDC-TopNav";
    private boolean showUser = false;
    private boolean editUser = false;
    private String cssStyle = null;
    private DbConn myConn = null;
    private String target = "_blank";
    private boolean localConnection = false;


  public int doStartTag( ) throws JspException{

        try {


    } catch (Exception e) { throw new JspException(e.getMessage( )); }

    return EVAL_BODY_BUFFERED;

  }

  public int doEndTag( ) throws JspException {

      String output = "";

      //this method assumes that attribute properties have been set.
      try {
          if (myConn == null) {
              try {
                  myConn = new DbConn();
                  localConnection = true;
              } catch (Exception e) { myConn = null; }
          }
          UList myMenu = null;
          // determine ID type
          Pattern myPattern = Pattern.compile("\\D");
          Matcher myMatcher;
          myMatcher = myPattern.matcher(ID);
          output += "&nbsp;<b>";
          if (myConn != null) {
              //if (ID.matches("\\D")) {
              if (myMatcher.find()) {
                  myMenu = new UList(ID, myConn);
              } else {
                  myMenu = new UList(Long.parseLong(ID), myConn);
              }
              long[] items = myMenu.getItemsArray();
              for (int i=0; i<items.length; i++) {
//System.out.println("got here - Menu: " + ID + ", Item: " + items[i]);
                  TextItem ti = new TextItem(items[i], myConn);
                  output += "<a" + ((cssStyle != null) ? " class=\"" + cssStyle + "\"" : "") + " href=" + ti.getLink() + ((target != null) ? " target='" + target + "'" : "" ) +">" + ti.getText() + "</a>&nbsp;&nbsp;";
              }
          } else {
              output += "<a" + ((cssStyle != null) ? " class=\"" + cssStyle + "\"" : "") + " href=# >Menu Unavailable</a>&nbsp;&nbsp;";
          }

          String bc = getBodyContent().getString().trim();
          String[] menuItems  = bc.split("\n");
          for (int i=0; i<menuItems.length; i++) {
              if (menuItems[i].startsWith("{")) {
                  JSONObject jo = new JSONObject(menuItems[i]);
                  String text = jo.getString("text");
                  String link = jo.getString("link");
                  output += "<a class=\"" + ((cssStyle != null) ? "" + cssStyle + "" : "") + "\" href=\"" + link + "\" " + ((target != null) ? " target='" + target + "'" : "" ) +">" + text + "</a>&nbsp;&nbsp;";
              }
          }

          if (showUser) {
              HttpSession session = pageContext.getSession();
              boolean buildCache = (session.getAttribute("page.buildCache")  != null && ((String) session.getAttribute("page.buildCache")).toLowerCase().equals("true")) ? true : false;
              if (session.getAttribute("user.name")  != null && !((String) session.getAttribute("user.name")).toLowerCase().equals("guest") && !buildCache) {
                  if (editUser) {
                      output += "<a" + ((cssStyle != null) ? " class=\"" + cssStyle + "\"" : "") + " href=\"javascript:doUpdatePerson()\">Welcome " + session.getAttribute("user.fullname") + "!</a>\n";
                      output += "<script language=javascript><!--\n";
                      output += "    \n";
                      output += "    function doUpdatePerson() {\n";
                      output += "        document.main.id.value = " + session.getAttribute("user.id") + ";\n";
                      output += "        submitForm(\"updatePerson.jsp\",\"update\");\n";
                      output += "    }\n";
                      output += "    //-->\n";
                      output += "</script>\n";
                  } else {
                      output += "<b" + ((cssStyle != null) ? " class=\"" + cssStyle + "\"" : "") + " >Welcome " + session.getAttribute("user.fullname") + "!</b>";
                  }
              }

          }

          output += "</b>";

           //Get a JspWriter to produce the tag's output
          JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          out.println(output);

          if (localConnection) {
              myConn.release();
              myConn = null;
          }

     } catch (Exception e) { System.out.println("TopMenu-" + e); throw new JspException(e.getMessage( )); }


      return EVAL_PAGE;

  }//doTag

  //Attribute-related setter methods

  public void setId(String val) {

    this.ID = ((!val.toLowerCase().equals("none")) ? val : null);

  }

  public void setId(long val) {

    this.ID = Long.toString(val);

  }

  public void setId(int val) {

    this.ID = Integer.toString(val);

  }

  public void setShowUser(String val) {

    this.showUser = (val.toLowerCase().equals("true")) ? true : false;

  }

  public void setEditUser(String val) {

    this.editUser = (val.toLowerCase().equals("true")) ? true : false;

  }

  public void setCssStyle(String val) {

    this.cssStyle = val;

  }

  public void setDbConnection(DbConn conn) {

    this.myConn = conn;

  }

  public void setTarget(String val) {

    this.target = ((!val.toLowerCase().equals("none")) ? val : null);

  }

  public void release( ){

     ID = "RDC-TopNav";
     showUser = false;
     editUser = false;
     myConn = null;
     target = "_blank";

  }// release

}// TopMenu
