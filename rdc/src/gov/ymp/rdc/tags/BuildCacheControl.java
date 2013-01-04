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

/** This tag generates the code to request a cache build  */

public class BuildCacheControl extends BodyTagSupport {

    private String url = null;
    private String fileLocation = null;

    private boolean cacheAvailable = false;
    private boolean useCache = false;


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
          String buildCache = request.getParameter("buildcache");
//System.out.println("BuildCacheControl - got here - 1");
          if (buildCache == null || !buildCache.equals("true")) {
              output += "<input type=hidden name=buildcache value='false'>\n";
              output += "<input type=hidden name=cacheurl value=''>\n";
              output += "<input type=hidden name=cachefilewithpath value=''>\n";
              //if (pos != null && pos.belongsTo(((Long) perMap.get("3-updatehtmlcache")).longValue())) {
                  output += "<br><br><a href=javascript:doCache()>Update Cache</a>\n";

                  output += "<script language=javascript><!--\n";
                  output += "    function doCache() {\n";
                  output += "        document.main.action = '/rdc/cache.jsp';\n";
                  output += "        document.main.target = 'results';\n";
                  output += "        document.main.buildcache.value = 'true';\n";
                  output += "        document.main.cacheurl.value = '" + url + "';\n";
                  output += "        document.main.cachefilewithpath.value = '" + fileLocation + "';\n";
                  output += "        document.main.submit();\n";
                  output += "    }\n";
                  output += "    //-->\n";
                  output += "</script>\n";
              //}
          }


           //Get a JspWriter to produce the tag's output
          JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          out.println(output);

     } catch (Exception e) { System.out.println("BuildCacheControl-" + e); throw new JspException(e.getMessage( )); }


      return EVAL_PAGE;

  }//doTag

  //Attribute-related setter methods

  public void setUrl(String val){

    this.url = val;

  }

  public void setFileLocation(String val){

    this.fileLocation = val;

  }

  public void release( ){

     url = null;
     fileLocation = null;

  }// release

}// BuildCacheControl
