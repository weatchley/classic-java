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

/** This tag generates a vertical menu for the left side of the page.  */

public class LeftMenu extends BodyTagSupport {

    private String ID = null;
    private boolean includeLogin = false;
    private String cssStyle = null;
    private DbConn myConn = null;
    private boolean localConnection = false;
    private String menuType = "standard";
    private String title = null;
    private boolean isOpen = false;

    private String menuItemBegin = "";
    private String menuItemEnd = "";
    private String myName = "";


  public int doStartTag( ) throws JspException{

        try {


    } catch (Exception e) { throw new JspException(e.getMessage( )); }

    return EVAL_BODY_BUFFERED;

  }

  public int doEndTag( ) throws JspException {

      String output = "";

      //this method assumes that attribute properties have been set.
      try {
          if (myConn == null && ID != null) {
              try {
                  myConn = new DbConn();
                  localConnection = true;
              } catch (Exception e) { myConn = null; }
          }
//System.out.println("LeftMenu - got here - setup - Menu ID: " + ID + ", myConn: " + myConn);
          if (menuType.toLowerCase().equals("standard")) {
              menuItemBegin = "<tr>\n<td width=\"27\" bgcolor=\"#FFFFFF\" valign=top><img src=\"/common/images/rdc/seta.gif\" width=\"7\" height=\"9\" align=\"right\"></td>\n<td width=\"123\" bgcolor=\"#FFFFFF\">";
              menuItemEnd = "</td></tr>\n";
              output += "<table width=\"150\" border=\"0\" cellspacing=\"1\" cellpadding=\"3\" bgcolor=\"#CCCCCC\">";
          }
          if (menuType.toLowerCase().equals("submenu")) {
              menuItemBegin = "";
              menuItemEnd = "<br>";
              myName = title;
              myName = myName.replaceAll("[^a-zA-Z0-9]", "");
          }

          if (ID != null && myConn != null) {
              UList myMenu = null;
              // determine ID type
              Pattern myPattern = Pattern.compile("\\D");
              Matcher myMatcher;
              myMatcher = myPattern.matcher(ID);
              //if (ID.matches("\\D")) {
              if (myMatcher.find()) {
                  myMenu = new UList(ID, myConn);
              } else {
                  myMenu = new UList(Long.parseLong(ID), myConn);
              }
              long[] items = myMenu.getItemsArray();
              for (int i=0; i<items.length; i++) {
//System.out.println("LeftMenu - got here - Menu: " + ID + ", Item: " + items[i]);
                  TextItem ti = new TextItem(items[i], myConn);
                  output += doMenuItem(ti.getText(), ti.getLink());
              }
          }

          String bc = getBodyContent().getString().trim();
          String[] menuItems  = bc.split("\n");
          for (int i=0; i<menuItems.length; i++) {
              output += doMenuItem(menuItems[i], null);
          }

          if (includeLogin) {
              HttpSession session = pageContext.getSession();
              boolean buildCache = (session.getAttribute("page.buildCache")  != null && ((String) session.getAttribute("page.buildCache")).toLowerCase().equals("true")) ? true : false;
              if (session.getAttribute("user.name")  == null || ((String) session.getAttribute("user.name")).toLowerCase().equals("guest") || buildCache) {
                  output += menuItemBegin + "<a class=\"" + ((cssStyle != null) ? "" + cssStyle + "" : "leftnav") + "\" href=\"javascript:showLoginBox1()\">Login</a>" + menuItemEnd;
              } else {
                  if (session.getAttribute("user.islocaldomain") != null && ((String) session.getAttribute("user.islocaldomain")).equals("T")) {
                      output += menuItemBegin + "<a class=\"" + ((cssStyle != null) ? "" + cssStyle + "" : "leftnav") + "\" href=\"changePassword.jsp\">Change Password</a>" + menuItemEnd;
                  }
                  output += menuItemBegin + "<a class=\"" + ((cssStyle != null) ? "" + cssStyle + "" : "leftnav") + "\" href=\"logout.jsp\">Logout</a>" + menuItemEnd;
              }

          }

          if (menuType.toLowerCase().equals("standard")) {
              output += "</table>";
          }
          if (menuType.toLowerCase().equals("submenu")) {
//              output += "</td></tr></table></span>\n";

              output = output.replaceAll("\n","");
              output = "{\"text\":\"" + title +"\",\"link\":\"javascript:showMenubox" + myName + "();\",\"submenu\":" + JSONObject.quote(output) + "}\n";
          }

           //Get a JspWriter to produce the tag's output
          JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          out.println(output);

          if (localConnection) {
              myConn.release();
              myConn = null;
          }

     } catch (Exception e) { System.out.println("LeftMenu-" + e); throw new JspException(e.getMessage( )); }


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

  public void setIncludeLogin(String val) {

    this.includeLogin = (val.toLowerCase().equals("true")) ? true : false;

  }

  public void setCssStyle(String val) {

    this.cssStyle = val;

  }

  public void setDbConnection(DbConn conn) {

    this.myConn = conn;

  }

  public void setMenuType(String val) {

    this.menuType = val;

  }

  public void setTitle(String val) {

    this.title = val;

  }

  public void setIsOpen(String val) {

    this.isOpen = (val.toLowerCase().equals("true")) ? true : false;

  }

  private String doMenuItem(String menuText, String menuLink) throws JspException{
      String output = "";
      try {
//System.out.println("LeftMenu - got here - doMenuItem - 1");
          if (menuText.startsWith("{")) {
              JSONObject jo = new JSONObject(menuText);
              String text = jo.getString("text");
              String link = "";
              String target = "";
              if (jo.has("link")) {
                  link = jo.getString("link");
              }
              if (jo.has("target")) {
                  target = " target=\"" + jo.getString("target") + "\"";
              }
              if (jo.has("submenu")) {
                  myName = text;
                  myName = myName.replaceAll("[^a-zA-Z0-9]", "");
                  output += menuItemBegin + "<a class=\"" + ((cssStyle != null) ? "" + cssStyle + "" : "leftnav") + "\" href=\"" + link + "\">" + text + "</a>";
                  output += "<span id=menubox" + myName + " style=\"display:" + ((isOpen) ? "'block'" : "'none'") + "\">\n";
                  output += "<table border=0 align=left class=\"submenu\"><tr><td>&nbsp;&nbsp;</td><td>\n";
                  output += jo.getString("submenu");
                  output += "\n</td></tr></table>\n</span>" + menuItemEnd + "\n";
                  output += "<script language=javascript><!--\n";
                  output += "     // function to show/hide the browse menu\n";
                  output += "    function showMenubox" + myName + " () {\n";
                  output += "        mySection = document.getElementById('menubox" + myName + "');\n";
                  output += "        if (mySection.style.display=='none') {\n";
                  output += "            mySection.style.display='block';\n";
                  output += "        } else {\n";
                  output += "            mySection.style.display='none';\n";
                  output += "        }\n";
                  output += "    }\n";
                  output += "    // Firefox hack\n";
                  output += "    mySection = document.getElementById('menubox" + myName + "');\n";
                  output += "    mySection.style.display=" + ((isOpen) ? "'block'" : "'none'") + ";\n";
                  output += "\n";
                  output += "    //-->\n";
                  output += "</script>\n";
              } else if (link != null && !link.toLowerCase().equals("none") && !link.equals("")) {
                  output += menuItemBegin + "<a class=\"" + ((cssStyle != null) ? "" + cssStyle + "" : "leftnav") + "\" href=\"" + link + "\"" + target + ">" + text + "</a>" + menuItemEnd;
              } else {
                  output += menuItemBegin + "<p class=\"" + ((cssStyle != null) ? "" + cssStyle + "" : "leftnav") + "\" >" + text + "</p>" + menuItemEnd;
              }
          } else if (!menuText.startsWith("<!--") && !menuText.trim().equals("")) {
              if (menuLink != null && !menuLink.toLowerCase().equals("none") && !menuLink.equals("")) {
                  output += menuItemBegin + "<a class=\"" + ((cssStyle != null) ? "" + cssStyle + "" : "leftnav") + "\" href=\"" + menuLink + "\">" + menuText + "</a>" + menuItemEnd;
              } else {
                  output += menuItemBegin + "<p class=\"" + ((cssStyle != null) ? "" + cssStyle + "" : "leftnav") + "\" >" + menuText + "</p>" + menuItemEnd;
              }
          }
      } catch (Exception e) { throw new JspException(e.getMessage( )); }
      return (output);
  }

  public void release( ){

     ID = null;
     includeLogin = false;
     myConn = null;
     menuType = "standard";
     title = null;
     isOpen = false;

  }// release

}// LeftMenu
