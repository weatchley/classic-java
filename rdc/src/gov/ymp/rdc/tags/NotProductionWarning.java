package gov.ymp.rdc.tags;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import gov.ymp.csi.misc.*;
import javax.naming.*;

/** This tag generates a warning when the page is displayed in an environment other that production.  */

public class NotProductionWarning extends BodyTagSupport {

    private String fontSize = "150%";
    private String myWidth = "180";


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
          if (!myProductionStatus.equals("prod")) {
              output += "<div id=\"FloatProdStatusBox\" style=\"position: absolute; width: " + myWidth + "px; hight: 50px; border-width: 0; background-color:#FFFFFF; cursor:pointer; filter:alpha(opacity=50); -moz-opacity:.50; opacity:.50;\" onClick=\"hideFloatProdStatusBox(this)\">\n";
              output += "<p style=\"color:#FF0000;font-weight:900;font-size:" + fontSize + ";\" align=center>Warning!<br>This&nbsp;is&nbsp;set&nbsp;up&nbsp;for development/review.</p>\n";
              output += "</div>\n";
              output += "<script>\n";
              output += "var hX = -200;\n";
              output += "var vY = 10;\n";
              output += "\n";
              output += "/* Portions of this are from: Floating Mail-This-Link Script C.2004 by CodeLifter.com */\n";
              output += "/* Used by permission from javascripts.com */\n";
              output += "var nn=(navigator.appName.indexOf(\"Netscape\")!=-1);\n";
              output += "var dD=document,dH=dD.html,dB=dD.body,px=dD.layers?'':'px';\n";
              output += "function floatProdStatusBox(iX,iY,id){\n";
              output += "    var L=dD.getElementById?dD.getElementById(id):dD.all?dD.all[id]:dD.layers[id];\n";
              output += "    this[id+'O']=L;if(dD.layers)L.style=L;L.nX=L.iX=iX;L.nY=L.iY=iY;\n";
              output += "    L.P=function(x,y){this.style.left=x+px;this.style.top=y+px;};L.Fm=function(){var pX, pY;\n";
              output += "    pX=(this.iX >=0)?0:nn?innerWidth:nn&&dH.clientWidth?dH.clientWidth:dB.clientWidth;\n";
              output += "    pY=nn?pageYOffset:nn&&dH.scrollTop?dH.scrollTop:dB.scrollTop;\n";
              output += "    if(this.iY<0)pY+=nn?innerHeight:nn&&dH.clientHeight?dH.clientHeight:dB.clientHeight;\n";
              output += "    this.nX+=.1*(pX+this.iX-this.nX);this.nY+=.1*(pY+this.iY-this.nY);this.P(this.nX,this.nY);\n";
              output += "    setTimeout(this.id+'O.Fm()',33);};\n";
              output += "    return L;\n";
              output += "}\n";
              output += "function hideFloatProdStatusBox(box) {\n";
              output += "    box.style.display='none';\n";
              output += "}\n";
              output += "floatProdStatusBox(hX,vY,'FloatProdStatusBox').Fm();\n";
              output += "</script>\n";
              output += "\n";
          }

           //Get a JspWriter to produce the tag's output
          JspWriter out = pageContext.getOut( );

          //display the oputput in the page
          out.println(output);

     } catch (Exception e) { System.out.println("NotProductionWarning-" + e); throw new JspException(e.getMessage( )); }


      return EVAL_PAGE;

  }//doTag

  //Attribute-related setter methods

  public void setSize(String size){

    this.fontSize = size;

  }

  public void setWidth(String width){

    this.myWidth = width;

  }

  public void release( ){

     fontSize = null;

  }// release

}// DoSection
