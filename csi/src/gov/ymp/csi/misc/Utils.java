package gov.ymp.csi.misc;

// Support classes
import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.sql.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
* Utils is a set of utility funcitons for CSI
*
* @author   Bill Atchley
*/
public class Utils {
    //
    /**
    * Creates an id string using current date/time and a random string
    *
    * @return       The new id
    */
    public static String genNewID() {
        String value = genDateID() + "-" + genRandID(4);
        return (value);
    }

    /**
    * Creates an id string using current date/time and a passed in string suffix
    *
    * @param suffix   The string to append to the end of the date id
    *
    * @return         The new id
    */
    public static String genNewID(String suffix) {
        String value = genDateID() + "-" + suffix;
        return (value);
    }

    /**
    * Creates an id string using current date/time
    *
    * @return       The new id
    */
    public static String genDateID() {
        java.util.Date dNow = new java.util.Date();

        // Use a SimpleDateFormat to print the date our way.
        SimpleDateFormat formatter
            = new SimpleDateFormat ("yyyy.MM.dd.hh.mm.ss");
        String value = formatter.format(dNow);

        return(value);
    }

    /**
    * Creates a random string of given length
    *
    * @param size     The size for the new string
    *
    * @return         The new string
    */
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

    /**
    * Converts a string date to a java date value
    *
    * @param inDate     The input string date
    * @param format     The format of the string date
    *
    * @return           The java date
    */
    public static java.util.Date toDate(String inDate, String format) {
        java.util.Date d = null;
        SimpleDateFormat formatter = new SimpleDateFormat (format);
        try {
            d = formatter.parse(inDate);
        } catch (ParseException e) {
            System.out.println("unparseable using " + formatter);
        }
        return(d);
    }

    /**
    * Converts a string date to a java date value (date formatted as 'MM/dd/yyyy')
    *
    * @param inDate     The input string date
    *
    * @return           The java date
    */
    public static java.util.Date toDate(String inDate) {
        return(toDate(inDate, "MM/dd/yyyy"));
    }

    /**
    * Add a given number of days to a given date
    *
    * @param inDate     The input java date
    * @param days       The number of days to add
    *
    * @return           The new java date
    */
    public static java.util.Date addDays(java.util.Date inDate, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        cal.add(Calendar.DATE, days);
        java.util.Date outDate = cal.getTime();
        return(outDate);
    }

    /**
    * Converts a java date to a string date value
    *
    * @param inDate     The input java date
    * @param format     The format of the string date
    *
    * @return           The string date
    */
    public static String dateToString(java.util.Date inDate, String format) {
        String d = null;
        SimpleDateFormat formatter = new SimpleDateFormat (format);
        d = formatter.format(inDate);
        return(d);
    }

    /**
    * Converts a java date to a string date value (using format 'MM/dd/yyyy')
    *
    * @param inDate     The input java date
    *
    * @return           The string date
    */
    public static String dateToString(java.util.Date inDate) {
        return(dateToString(inDate, "MM/dd/yyyy"));
    }

    /**
    * Converts a java date to a string date value (using format 'yyyy-MM-dd')
    *
    * @param inDate     The input java date
    *
    * @return           The string date
    */
    public static String dateToCompareString(java.util.Date inDate) {
        return(dateToString(inDate, "yyyy-MM-dd"));
    }


    /**
    * Converts a java.util.Date to java.sql.Date
    *
    * @param inDate     The input date
    *
    * @return           The java.sql.Date date
    */
    public static java.sql.Date castDate(java.util.Date inDate) {
        java.sql.Date tmpDate = new java.sql.Date(inDate.getTime());
        return(tmpDate);
    }


    /**
    * Converts a java.util.Date to java.sql.Date
    *
    * @param inDate     The input date
    *
    * @return           The java.util.Date date
    */
    public static java.util.Date castDate(java.sql.Date inDate) {
        java.util.Date tmpDate = new java.util.Date(inDate.getTime());
        return(tmpDate);
    }


    /**
    * tests a string for null and returns a default string if it is null
    *
    * @param text     The string to test
    * @param def      The default string
    *
    * @return         Returns 'text' if it is not null, else it returns 'def'
    */
    private String testNull(String text, String def) {
        if (text != null) {
            return text;
        } else {
            return def;
        }
    }


    /**
    * pads a string to the left with the given string until the result is at least as long as the given length
    *
    * @param text     The string to test
    * @param pad      The string to pad with
    * @param len      The length to pad to
    *
    * @return         The padded string
    */
    private String lPad(String text, String pad, int len) {
        while (text.length() < len) {
            text = pad + text;
        }
        return text;
    }


}
