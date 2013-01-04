package gov.ymp.nacrd;

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
* Utils is a set of utility funcitons for NACRD
*
* @author   Bill Atchley
*/
public class Utils {
    //
    public static String genNewID() {
        String value = genDateID() + "-" + genRandID(4);
        return (value);
    }

    public static String genNewID(String suffix) {
        String value = genDateID() + "-" + suffix;
        return (value);
    }

    public static String genDateID() {
        Date dNow = new Date();

        // Use a SimpleDateFormat to print the date our way.
        SimpleDateFormat formatter
            = new SimpleDateFormat ("yyyy.MM.dd.hh.mm.ss");
        String value = formatter.format(dNow);

        return(value);
    }

    public static String genRandID(int size) {
        //String testVals = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //int countOfTestVals = 62;
        String testVals = "0123456789abcdefghijklmnopqrstuvwxyz";
        int countOfTestVals = 36;
        int keyLength = size;
        java.util.Random generator = new java.util.Random(System.currentTimeMillis());
        String KeyID = "";
        for (int pos = 0; (pos < keyLength); pos++) {
            int loc = generator.nextInt(countOfTestVals);
            KeyID = KeyID + testVals.charAt(loc);
        }
        String value = KeyID;
        return (value);
    }

    public static String getNodeValue(Node n) {
        String value = "";
        if (n.hasChildNodes()) {
            NodeList nodes = n.getChildNodes();
            Node nd = (Node)nodes.item(0);
            value = nd.getNodeValue();
        }
        return (value);
    }

    public static Date toDate(String inDate, String format) {
        Date d = null;
        SimpleDateFormat formatter = new SimpleDateFormat (format);
        try {
            d = formatter.parse(inDate);
        } catch (ParseException e) {
            System.out.println("unparseable using " + formatter);
        }
        return(d);
    }

    public static Date toDate(String inDate) {
        return(toDate(inDate, "MM/dd/yyyy"));
    }

    public static Date addDays(Date inDate, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        cal.add(Calendar.DATE, days);
        Date outDate = cal.getTime();
        return(outDate);
    }

    public static String dateToString(Date inDate, String format) {
        String d = null;
        SimpleDateFormat formatter = new SimpleDateFormat (format);
        d = formatter.format(inDate);
        return(d);
    }

    public static String dateToString(Date inDate) {
        return(dateToString(inDate, "MM/dd/yyyy"));
    }

    public static String dateToCompareString(Date inDate) {
        return(dateToString(inDate, "yyyy-MM-dd"));
    }


    public static String colapseString(String ID, String in, int maxLen) {
        return (colapseString(ID, in, maxLen, false));
    }

    public static String colapseString(String ID, String in, int maxLen, boolean isOpen) {
        int len = in.length();
        String head = "";
        String out = "";
        if (len > maxLen) {
            head = in.substring(0,maxLen);
        } else {
            head = in;
        }
        out += "<table border=0  cellpadding=0 cellspacing=0><tr><td valign=top>" +
          "<img src=images/nolines_" + ((isOpen) ? "minus" : "plus") + ".gif id=ci" + ID + " border=0 onClick=showHide" + ID + "()></td><td>\n" +
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
          "            myImg.src='images/nolines_minus.gif';\n" +
          "        } else {\n" +
          "            mySectionOpen.style.display='none';\n" +
          "            mySectionClosed.style.display='block';\n" +
          "            myImg.src='images/nolines_plus.gif';\n" +
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

    public static String doSection(String ID, String title, String in) {
        return (doSection(ID, title, in, true));
    }

    public static String doSection(String ID, String title, String in, boolean isOpen) {
        String out = "";
        out += "<table border=0  cellpadding=0 cellspacing=0><tr><td valign=top>" +
          "<img src=images/nolines_" + ((isOpen) ? "minus" : "plus") + ".gif id=ci" + ID +" border=0 onClick=showHide" + ID + "()></td><td>\n" +
        title + "\n";
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
          "            myImg.src='images/nolines_minus.gif';\n" +
          "        } else {\n" +
          "            mySection.style.display='none';\n" +
          "            myImg.src='images/nolines_plus.gif';\n" +
          "        }\n" +
          "    }\n" +
          "    // Firefox hack\n" +
          "    mySection = document.getElementById('cs" + ID + "');\n" +
          "    mySection.style.display='" + ((isOpen) ? "" : "none") + "';\n" +
          "    //-->\n" +
          "</script>\n";

        return (out);
    }





}