package gov.ymp.rdc.tags;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.items.*;
import gov.ymp.csi.people.*;
import java.util.regex.*;
import javax.naming.*;
import org.json.*;

/** This tag generates an editable content pane on a page including controls for approval work flow.  A special use of this tag can be used for news letters.  */

public class ContentPane extends BodyTagSupport {

    private String name = null;
    private String ID = null;
    private DbConn myConn = null;
    private boolean firstOnPage = true;
    private boolean isNewsletter = false;
    private int newsletterArchiveLength = 6;
    private boolean localConnection = false;
    private boolean newsletterShowDate = true;
    private TextItem[] list = null;

    private TextItem editPriv = null;
    private TextItem appPriv = null;
    private TextItem publishedContent = null;
    private TextItem workingContent = null;
    private String nlDate = Utils.dateToString(new java.util.Date());
    private long nlID = 0;



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
          String published = "";
          String working = "";
          String status = "";
          String command = "";

          boolean buildCache = ((session.getAttribute("page.buildCache") != null && ((String) session.getAttribute("page.buildCache")).toLowerCase().equals("true")) ||
                (request.getParameter("buildcache") != null && request.getParameter("buildcache").toLowerCase().equals("true"))) ? true : false;
//System.out.println("ContentPane - got here - 1 - buildcache: " + request.getParameter("buildcache") + ", page.buildCache: " + session.getAttribute("page.buildCache"));
          boolean hasEdit = false;
          boolean hasApp = false;
          Position pos = null;
          long posID = 0;
          HashMap perMap = null;
          boolean isAuthenticated = false;
          UHash pageContent = null;
//System.out.println("ContentPane - got here - 2");

          if (myConn == null) {
              try {
                  myConn = new DbConn();
                  localConnection = true;
              } catch (Exception e) { myConn = null; }
          }
          if (myConn != null) {
              pageContent = new UHash(ID, myConn);
              editPriv = new TextItem(pageContent.get("editpriv"), myConn);
              appPriv = new TextItem(pageContent.get("approvepriv"), myConn);
              // determine if user has edit or approval priv
              if (session.getAttribute("user.name")  != null && !((String) session.getAttribute("user.name")).toLowerCase().equals("guest") && !buildCache) {
                  isAuthenticated = true;
                  pos = (Position) session.getAttribute("user.position");
                  posID = (long) new Long((String) session.getAttribute("user.positionid")).longValue();
                  perMap = (HashMap) session.getAttribute("user.permissionmap");
                  hasEdit = pos.belongsTo(((Long) perMap.get(editPriv.getText())).longValue());
                  hasApp = pos.belongsTo(((Long) perMap.get(appPriv.getText())).longValue());
              }
              if (isNewsletter) {
                  String nlIDs = ((request.getParameter("id")  != null) ? (String) request.getParameter("id") : "0");
                  command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
                  nlID = Long.parseLong(nlIDs);
                  if (!command.equals("new")) {
                      UList ul = new UList(pageContent.get("list"), myConn);
                      ul.lookup(ul.getID(), myConn);
                      long[] items = ul.getItemsArray();
                      list = TextItem.getItemList(myConn, items, "date1 desc");
                      int current = -1;
                      JSONObject jo = null;
                      // determine the current newsletter either the most recent approved, or the one passed in
                      // "external" newsletters can not be current as they can not be displayed in-line
                      if (list != null) {
                          for (int i=0; i<list.length; i++) {
                              String temp = list[i].getText();
                              String pub = null;
                              String work = null;
                              jo = new JSONObject(list[i].getText());
                              pub = jo.getString("published");
                              work = jo.getString("working");
                              if (nlID != 0 && nlID == list[i].getID()) {
                                  current = i;
                              }
                              if (current == -1 && nlID == 0 && !list[i].getText().equals("external") && pub != null && !pub.equals("")) {
                                  current = i;
                                  nlID = list[i].getID();
                              }
                          }
                      }
                      // get contents of selected newsletter
                      if (current >= 0 && list != null && list.length>0) {
                          nlDate = Utils.dateToString(list[current].getDate1());
                          jo = new JSONObject(list[current].getText());
                          published = jo.getString("published");
                          working = jo.getString("working");
                          status = list[current].getStatus();
                      } else {
                          if (current == -1 && hasEdit) {
                              if (list != null && list.length>0) {
                                  current = 0;
                                  nlID = list[0].getID();
                                  nlDate = Utils.dateToString(list[current].getDate1());
                                  jo = new JSONObject(list[current].getText());
                                  published = jo.getString("published");
                                  working = jo.getString("working");
                                  status = list[current].getStatus();
                              } else {
                                  command = "new";
                              }
                          } else {
                              published = "No Newsletter Available";
                          }
                      }
                  } else {
                      published = "";
                      working = "";
                      nlDate = Utils.dateToString(new java.util.Date());
                  }
              } else {
                  publishedContent = new TextItem(pageContent.get("published"), myConn);
                  published = publishedContent.getText();
                  workingContent = new TextItem(pageContent.get("working"), myConn);
                  working = workingContent.getText();
                  status = workingContent.getStatus();
              }
          }
          String displayContent = null;
          // determine if published or working content is displayed
          if (hasEdit || hasApp) {
              //displayContent = workingContent.getText();
              displayContent = working;
              //status = workingContent.getStatus();
          } else {
              if (displayContent == null) {
                  //displayContent = publishedContent.getText();
                  displayContent = published;
              } else {
                  displayContent = "<h3 align=center>Content currently unavailable</h3>\n";
              }
          }

          InitialContext myInitCtx = new InitialContext();
          String productionStatus = (String) myInitCtx.lookup("java:comp/env/ProductionStatus");
          productionStatus = (productionStatus != null) ? productionStatus : "test";

          if (firstOnPage && isAuthenticated && hasEdit) {
              output += "<!-- include code for WYSIWYG editor -->\n";
              output += "<!-- tinyMCE -->\n";
              output += "<script language=\"javascript\" type=\"text/javascript\" src=\"/common/javascript/tiny_mce/tiny_mce.js\"></script>\n";
              output += "<script language=\"javascript\" type=\"text/javascript\">\n";
              output += "  tinyMCE.init({\n";
              output += "  mode : \"textareas\",\n";
              output += "  theme : \"advanced\",\n";
              output += "  plugins : \"style,table,searchreplace,contextmenu,paste,fullscreen,nonbreaking\",\n";
              output += "  theme_advanced_buttons1 : \"bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,outdent,indent,hr,bullist,numlist,undo,redo,link,unlink,styleselect,fontselect,fontsizeselect\",\n";
              output += "  theme_advanced_buttons2 : \"cut,copy,paste,pastetext,pasteword,|,sub,sup,separator,search,replace,separator,tablecontrols,separator,fullscreen" + ((!productionStatus.equals("prod")) ? ",code" : "") + "\",\n";
              output += "  theme_advanced_buttons3 : \"\",\n";
              output += "  theme_advanced_toolbar_location : \"top\",\n";
              output += "  theme_advanced_toolbar_align : \"left\",\n";
              output += "  theme_advanced_statusbar_location : \"bottom\",\n";
              output += "  theme_advanced_resizing : true,\n";
              output += "  theme_advanced_path : false,\n";
              output += "  extended_valid_elements : \"a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]\"\n";
              output += "  });\n";
              output += "</script>\n";
              output += "<!-- /tinyMCE -->\n";
              output += "\n";
          }
          output += "<span id=" + name + "noneditarea>\n";

          output += "<!-- display appropriate content -->\n";
          if (isNewsletter && newsletterShowDate) {
              output += "Date: " + nlDate + "<br>";
          }
          output += displayContent;

          output += "<!-- Add edit, request approval, or approve links as appropriate -->\n";
          if (isAuthenticated && (hasEdit || hasApp)) {
                  if (hasEdit || hasApp) {
                      output += "<hr width=80% >\n";
                  }
                  output += "<p>";
                  if (hasEdit) {
                      output += "<a href=\"javascript:showHideEdit()\">Edit</a>\n";
                      if (status == null || (!status.equals("approved") && !status.equals("approvalrequested"))) { output += "&nbsp; &nbsp; <a href=\"javascript:doRequestApproval()\">Request Approval</a>\n"; }
                  }
                  if (hasEdit && isNewsletter) {
                      output += "&nbsp; &nbsp; <a href=\"javascript:doNew()\">New</a>\n";
                  }
                  if (hasApp && status != null && status.equals("approvalrequested")) {
                      output += "&nbsp; &nbsp; <a href=\"javascript:doApprove()\">Approve</a>\n";
                  }
                  if (hasApp && hasEdit && (status == null || (!status.equals("approvalrequested") && !status.equals("approved")))) {
                      output += "&nbsp; &nbsp; <a href=\"javascript:doApprove()\">Publish</a>\n";
                  }
                  output += "</p>\n";
          }

          if (isNewsletter) {
              output += "<!-- Display archive list --><br>\n";
              int archiveSize = newsletterArchiveLength;
              boolean archiveColapse = (list.length > archiveSize) ? true : false;
              int archiveCount = 0;
              String archiveOut = "";
              archiveOut += "<table border=0 cellpadding=0 cellspacing=0><tr>" + ((archiveColapse) ? "<td valign=top><img src=/common/images/rdc/nolines_plus.gif id=ciarchive border=0 onClick=showHideArchive()></td>" : "" ) + "<td valign=top>Archive:<br>\n";
              // display a list of newsletters, show only approved ones unless logged in with admin priv
              if (list != null) {
                  for (int i=0; i<list.length; i++) {
                      String myStatus = list[i].getStatus();
                      boolean displayedNL = (nlID == list[i].getID()) ? true : false;
                      if (myStatus == null || !myStatus.equals("approved")) {
                          if (hasEdit || (hasApp && myStatus.equals("approvalrequested"))) {
                              archiveOut += ((archiveCount == 0) ? "" : ", ") + ((archiveCount == archiveSize) ? "<span id=csoarchive style=\"display:'none'\">" : "") + "<i>";
                              archiveOut += ((displayedNL) ? "<b>" : "") + "<a href=\"javascript:doView(" + list[i].getID() + ")\">" + Utils.dateToString(list[i].getDate1()) + "*</a>" + ((displayedNL) ? "</b>" : "") + "</i>\n";
                              archiveCount++;
                          }
                      } else {
                          archiveOut += ((archiveCount == 0) ? "" : ", ") + " " + ((archiveCount == archiveSize) ? "<span id=csoarchive style=\"display:'none'\">" : "");
                          if (!list[i].getText().equals("external")) {
                              archiveOut += "<a href=\"javascript:doView(" + list[i].getID() + ")\"";
                          } else {
                              // external newsletters are displayed in a new window
                              archiveOut += "<a href=\"" + list[i].getLink() + "\" target=\"_blank\" ";
                          }
                          archiveOut += ">" + ((displayedNL) ? "<b>" : "") + Utils.dateToString(list[i].getDate1()) + ((displayedNL) ? "</b>" : "") + "</a>\n";
                          archiveCount++;
                      }
                  }
                  archiveOut += ((archiveColapse) ? "</span>" : "");
              } else {
                  archiveOut += " &nbsp; &nbsp; No Archive Available ";
              }
              archiveOut += "</td></table>\n";
              //output += HtmlUtils.colapseString("archive-list", archiveOut, 400, false, false);
              output += archiveOut;

              output += "<script language=javascript><!--\n";
              if (archiveColapse && archiveCount >= archiveSize) {
                  output += "" +
                    "    function showHideArchive () {\n" +
                    "        myImg = document.getElementById('ciarchive');\n" +
                    "        mySectionOpen = document.getElementById('csoarchive');\n" +
                    "        if (mySectionOpen.style.display=='none') {\n" +
                    "            mySectionOpen.style.display='block';\n" +
                    "            myImg.src='/common/images/rdc/nolines_minus.gif';\n" +
                    "        } else {\n" +
                    "            mySectionOpen.style.display='none';\n" +
                    "            myImg.src='/common/images/rdc/nolines_plus.gif';\n" +
                    "        }\n" +
                    "    }\n" +
                    "    // Firefox hack\n" +
                    "    mySectionOpen = document.getElementById('csoarchive');\n" +
                    "    mySectionOpen.style.display='none';\n";
              } else if (archiveColapse) {
                  output += "" +
                    "    function showHideArchive () {\n" +
                    "    }\n" +
                    "    myImg = document.getElementById('ciarchive');\n" +
                    "    myImg.src='/common/images/rdc/spacer.gif';\n";
              }
              output += "    function doView(id) {\n";
              output += "        document.main.id.value = id;\n";
              output += "        submitForm(\"/" + session.getServletContext().getServletContextName() + request.getServletPath() + "\",\"view\")\n";
              output += "    }\n";
              output += "    //-->\n";
              output += "</script>\n";

          }
          output += "</span>\n";
          output += "\n";

          output += "<!-- only include edit form and utility javascript if needed -->\n";
          if (isAuthenticated && (hasEdit || hasApp)) {

              output += "<script language=javascript><!--\n";
              output += "    function showHideEdit () {\n";
              output += "        mySection = document.getElementById('" + name + "noneditarea');\n";
              output += "        mySection2 = document.getElementById('" + name + "editarea');\n";
              output += "        if (mySection.style.display=='none') {\n";
              output += "            mySection.style.display='block';\n";
              output += "            mySection2.style.display='none';\n";
              output += "            document.main.reset();\n";
              output += "        } else {\n";
              output += "            mySection.style.display='none';\n";
              output += "            mySection2.style.display='block';\n";
              output += "        }\n";
              output += "    }\n";
              output += "\n";

              output += "    // A utility function that returns true if a string contains only\n";
              output += "    // whitespace characters.\n";
              output += "    function isblank(s) {\n";
              output += "        if (s.length == 0) return true;\n";
              output += "        for(var i = 0; i < s.length; i++) {\n";
              output += "            var c = s.charAt(i);\n";
              output += "            if ((c != ' ') && (c != '\\n') && (c != '\\t') && (c !='\\r')) return false;\n";
              output += "        }\n";
              output += "        return true;\n";
              output += "    }\n";
              output += "\n";

              output += "    function doSave() {\n";
              output += "        if (isblank(document.main.text.value)) {\n";
              output += "            alert('Text must be entered.');\n";
              output += "        } else {\n";
              output += "            submitFormResults(\"/rdc/doUpdatePage\",'update');\n";
              output += "        }\n";
              output += "    }\n";
              output += "\n";

              output += "    function doNew() {\n";
              output += "        document.main.newsletterid.value = 0;\n";
              output += "        submitForm(\"/" + session.getServletContext().getServletContextName() + request.getServletPath() + "\",'new');\n";
              output += "    }\n";
              output += "\n";

              output += "    function doRequestApproval() {\n";
              output += "        //alert('Not Ready Yet');\n";
              output += "        submitFormResults(\"/rdc/doUpdatePage\",\"approvalrequest\")\n";
              output += "    }\n";
              output += "\n";

              output += "    function doApprove() {\n";
              output += "        submitFormResults(\"/rdc/doUpdatePage\",\"approve\")\n";
              output += "    }\n";
              output += "\n";

              output += "    //-->\n";
              output += "</script>\n";
              output += "\n";

              output += "<!-- Set up edit form -->\n";
              output += "<span id=" + name + "editarea style=\"display:'none'\">\n";
              output += "<input type=hidden name=newsletterid value=\"" + nlID + "\">\n";
              output += "<input type=hidden name=isnewsletter value=\"" + ((isNewsletter) ? "true" : "false") + "\">\n";
              output += "<input type=hidden name=pagename value=\"" + ID + "\">\n";
              output += "<input type=hidden name=nextscript value=\"/" + session.getServletContext().getServletContextName() + request.getServletPath() + "?" + request.getQueryString() + "\">\n";
              output += "<table border=0><tr><td>\n";
              if (isNewsletter) {
                  output += "Date: <span id=\"holder1\"><input type=text name=date1 value='" + nlDate + "' size=12 maxlength=10 onfocus=\"this.blur();showCalendar('',this,this,'','holder1',0,30,1);\"></span>\n";
              } else {
                  output += "&nbsp;";
              }
              output += "</td>";
              output += "<td align=right><a href=\"javascript:doSave()\">Save</a> " + ((!command.equals("new")) ? "&nbsp; &nbsp; <a href=\"javascript:showHideEdit()\">Cancel</a>" : "") + "</td></tr>\n";
              output += "<tr><td colspan=2>Text:\n";
              output += "<textarea name=text cols=80 rows=30>\n";
//              output += workingContent.getText();
              output += working;
              output += "</textarea></td></tr>\n";
              output += "<tr><td colspan=2><a href=\"javascript:doSave()\">Save</a> " + ((!command.equals("new")) ? "&nbsp; &nbsp; <a href=\"javascript:showHideEdit()\">Cancel</a>" : "") + "</td></tr></table>\n";
              output += "</span>\n";
              output += "<script language=javascript><!--\n";
              output += "    mySection2 = document.getElementById('" + name + "editarea');\n";
              output += "    mySection2.style.display='none';\n";
              output += "    " + ((command.equals("new") && isNewsletter) ? "showHideEdit();\n" : "");
              output += "//-->\n";
              output += "</script>\n";
              output += "\n";

          }


           //Get a JspWriter to produce the tag's output
          JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          out.println(output);

          if (localConnection) {
              myConn.release();
              myConn = null;
          }

     } catch (Exception e) { System.out.println("ContentPane-" + e); throw new JspException(e.getMessage( )); }


      return EVAL_PAGE;

  }//doTag

  //Attribute-related setter methods

  public void setName(String val) {

    this.name = val;

  }

  public void setId(String val) {

    this.ID = val;

  }

  public void setDbConnection(DbConn conn) {

    this.myConn = conn;

  }

  public void setFirstOnPage(String val){

    this.firstOnPage = (val.toLowerCase().equals("true")) ? true : false;

  }

  public void setIsNewsletter(String val){

    this.isNewsletter = (val.toLowerCase().equals("true")) ? true : false;

  }

  public void setNewsletterArchiveLength(String val) {

    this.newsletterArchiveLength = Integer.parseInt(val);

  }

  public void setNewsletterShowDate(String val){

    this.newsletterShowDate = (val.toLowerCase().equals("true")) ? true : false;

  }

  public void release( ){

     name = null;
     ID = null;
     myConn = null;
     firstOnPage = false;
     isNewsletter = false;
     newsletterArchiveLength = 6;
     newsletterShowDate = true;

  }// release

}// ContentPane
