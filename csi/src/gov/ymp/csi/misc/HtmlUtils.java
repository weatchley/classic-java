package gov.ymp.csi.misc;

// Support classes
import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
* Utils is a set of html utility funcitons for CSI
*
* @author   Bill Atchley
*/
public class HtmlUtils {
    /**
    * Creates a colapsable (html) string (string is initially colapsed)
    *
    * @param ID        The unique ID for the string
    * @param in        The input string
    * @param maxLen    The maximum number of characters to display when string is colapsed
    *
    * @return          The html structure for the colapsed string
    */
    public static String colapseString(String ID, String in, int maxLen) {
        return (colapseString(ID, in, maxLen, false));
    }

    /**
    * Creates a colapsable (html) string (string is initially colapsed, not clickable)
    *
    * @param ID        The unique ID for the string
    * @param in        The input string
    * @param maxLen    The maximum number of characters to display when string is colapsed
    * @param isOpen     A boolean describing if the string is initially colapsed or not
    *
    * @return          The html structure for the colapsed string
    */
    public static String colapseString(String ID, String in, int maxLen, boolean isOpen) {
        return (colapseString(ID, in, maxLen, isOpen, false));
    }

    /**
    * Creates a colapsable (html) string
    *
    * @param ID         The unique ID for the string
    * @param in         The input string
    * @param maxLen     The maximum number of characters to display when string is colapsed
    * @param isOpen     A boolean describing if the string is initially colapsed or not
    * @param activeText A boolean describing if the string can be clicked to open or close
    *
    * @return          The html structure for the colapsable string
    */
    public static String colapseString(String ID, String in, int maxLen, boolean isOpen, boolean activeText) {
        int len = in.length();
        String head = "";
        String out = "";
        ID = ID.replaceAll("\\W","_");
        ID = ID.replaceAll("\\-","_");
        if (len > maxLen) {
            //head = in.substring(0,maxLen);
            head = getDisplayString(in, maxLen);
        } else {
            head = in;
            return (head);
        }
        out += "<table border=0  cellpadding=0 cellspacing=0><tr><td valign=top>" +
          "<img src=/common/images/rdc/nolines_" + ((isOpen) ? "minus" : "plus") + ".gif id=ci" + ID + " border=0 onClick=showHide" + ID + "()></td><td" + ((activeText) ? " onClick=showHide"+ ID + "()" : "" ) + " >\n" +
          "<span id=csc" + ID + " style=\"display:'" + ((!isOpen) ? "" : "none") + "'\">" + head + "</span>\n";
        out += "<span id=cso" + ID + " style=\"display:'" + ((isOpen) ? "" : "none") + "'\">\n" +
        in + "</td></tr></table>\n" +
          "</span>\n";
        out += "" +
          "<script language=javascript><!--\n" +
          "    function showHide" + ID + " () {\n" +
          "        myImg = document.getElementById('ci" + ID + "');\n" +
          "        mySectionOpen = document.getElementById('cso" + ID + "');\n" +
          "        mySectionClosed = document.getElementById('csc" + ID + "');\n" +
          "        if (mySectionOpen.style.display=='none') {\n" +
          "            mySectionOpen.style.display='block';\n" +
          "            mySectionClosed.style.display='none';\n" +
          "            myImg.src='/common/images/rdc/nolines_minus.gif';\n" +
          "        } else {\n" +
          "            mySectionOpen.style.display='none';\n" +
          "            mySectionClosed.style.display='block';\n" +
          "            myImg.src='/common/images/rdc/nolines_plus.gif';\n" +
          "        }\n" +
          "    }\n" +
          "    // Firefox hack\n" +
          "    mySectionOpen = document.getElementById('cso" + ID + "');\n" +
          "    mySectionClosed = document.getElementById('csc" + ID + "');\n" +
          "    mySectionOpen.style.display='" + ((isOpen) ? "block" : "none") + "';\n" +
          "    mySectionClosed.style.display='" + ((isOpen) ? "none" : "block") + "';\n" +

          "    //-->\n" +
          "</script>\n";

        return (out);
    }

    /**
    * Creates a colapsable (html) section (section will be initially open)
    *
    * @param ID        The unique ID for the section
    * @param title     The title to be displayed for the section
    * @param in        The input text for the section
    *
    * @return          The html structure for the section
    */
    public static String doSection(String ID, String title, String in) {
        return (doSection(ID, title, in, true));
    }

    /**
    * Creates a colapsable (html) section
    *
    * @param ID        The unique ID for the section
    * @param title     The title to be displayed for the section
    * @param in        The input text for the section
    * @param isOpen    A boolean describing if the section is initially colapsed or not
    *
    * @return          The html structure for the section
    */
    public static String doSection(String ID, String title, String in, boolean isOpen) {
        String out = "";
        ID = ID.replaceAll("\\W","_");
        ID = ID.replaceAll("\\-","_");
        out += "<table border=0  cellpadding=0 cellspacing=0><tr><td valign=top>" +
          "<img src=/common/images/rdc/nolines_" + ((isOpen) ? "minus" : "plus") + ".gif id=ci" + ID +" border=0 onClick=showHide" + ID + "()></td><td>\n" +
        "<a href=\"javascript:showHide" + ID + "()\" style=\"decorations:none\">" + title + "</a>\n";
        out += "<span id=cs" + ID + " style=\"display:'" + ((isOpen) ? "" : "none") + "'\">\n" +
        in + "</td></tr></table>\n" +
          "</span>\n";
        out += "" +
          "<script language=javascript><!--\n" +
          "    function showHide" + ID + " () {\n" +
          "        myImg = document.getElementById('ci" + ID + "');\n" +
          "        mySection = document.getElementById('cs" + ID + "');\n" +
          "        if (mySection.style.display=='none') {\n" +
          "            mySection.style.display='block';\n" +
          "            myImg.src='/common/images/rdc/nolines_minus.gif';\n" +
          "        } else {\n" +
          "            mySection.style.display='none';\n" +
          "            myImg.src='/common/images/rdc/nolines_plus.gif';\n" +
          "        }\n" +
          "    }\n" +
          "    // Firefox hack\n" +
          "    mySection = document.getElementById('cs" + ID + "');\n" +
          "    mySection.style.display='" + ((isOpen) ? "" : "none") + "';\n" +
          "    //-->\n" +
          "</script>\n";

        return (out);
    }

    /**
    * Creates a dual select (html) widget (use of this object requires inclusion of the widgets.js file in your jsp page)
    *
    * @param name          The name of the widget
    * @param form          form name widget is contained in
    * @param avail         A HashMap of values and descriptions that are available to select
    * @param selected      A HashMap of values and descriptions that are currently selected
    * @param leftTitle     The title of the left side of the widget
    * @param rightTitle    The title of the right side of the widget
    *
    * @return              The html structure for the widget
    */
    public static String doDualSelect(String name, String form, HashMap avail, HashMap selected, String leftTitle, String rightTitle) {
        String out = "";
        // remove selected items from the available list
        Iterator it = selected.keySet().iterator();
//System.out.println("got here 1");
        while (it.hasNext()) {
            Object key = it.next();
            avail.remove(key);
        }
        out += "\n<!-- set up " + name + " -->\n";
        out += "<table border=0><tr><td valign=top>\n";
        out += leftTitle + "<br>\n";
        out += "<!-- the ondblclick event calls the javascript to move selected element to twin option box -->\n";
        out += "<select name=avail" + name + " size=8 ondblclick=\"process_dual_select_option(document." + form + ".avail" + name + ",document." + form + "." + name + ",'move')\">\n";
        it = avail.keySet().iterator();
        TreeMap availTree = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        while (it.hasNext()) {
            Object key = it.next();
            String value = (String) avail.get(key);
            availTree.put(value, key);
            //out += "<option value=\"" + key + "\">" + value + "</option>\n";
        }
        it = availTree.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            Object value = availTree.get(key);
            out += "<option value=\"" + value + "\">" + key + "</option>\n";
        }
        out += "<!-- this is used to force the size of the option box -->\n";
        out += "<option value=\"\">&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp\n";
        out += "</select>\n";
        out += "</td><td align=center>\n";
        out += "<!-- the select and deselect buttons call the javascript to move selected element to twin option box -->\n";
        out += "<input type=button value=\"Select ->\" name=select" + name + "  onClick=\"process_dual_select_option(document." + form + ".avail" + name + ",document." + form + "." + name + ",'move')\"><br>\n";
        out += "<input type=button value=\"<- Deselect\" name=deselect" + name + "  onClick=\"process_dual_select_option(document." + form + "." + name + ",document." + form + ".avail" + name + ",'move')\"><br>\n";
        out += "</td><td valign=top>\n";
        out += rightTitle + "<br>\n";
        out += "<!-- the ondblclick event calls the javascript to move selected element to twin option box -->\n";
        out += "<select multiple name=" + name + " size=8 ondblclick=\"process_dual_select_option(document." + form + "." + name + ",document." + form + ".avail" + name + ",'move')\">\n";
        it = selected.keySet().iterator();
        TreeMap selectedTree = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        if (!selected.isEmpty()) {
            while (it.hasNext()) {
                Object key = it.next();
                String value = (String) selected.get(key);
                if (value != null) {
                    selectedTree.put(value, key);
                }
                //out += "<option value=\"" + key + "\">" + value + "</option>\n";
            }
            it = selectedTree.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                Object value = selectedTree.get(key);
                out += "<option value=\"" + value + "\">" + key + "</option>\n";
            }
        }
//System.out.println("got here 6");
        out += "<!-- this is used to force the size of the option box -->\n";
        out += "<option value=\"\">&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp\n";
        out += "</select>\n";
        out += "</td></tr></table>\n\n";

        return (out);
    }

    /**
    * Creates a string that is less than or equal to maxLen and broken at a word boundary
    *
    * @param item          The string to process
    * @param maxLen        The maximum length of the string
    *
    * @return              The processed string
    */
    public static String getDisplayString(String item, int maxLen) {
        if (item.length() > maxLen) {
            item = item.substring(0, maxLen);
            item = item.replaceAll("\\s+\\S+$", "");
            item = item.replaceAll("\\s+$", "");
            item += "...";
        }
        return (item);
    }


}
