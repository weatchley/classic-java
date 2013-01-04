package gov.ymp.rdc.misc;

// Support classes
import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.items.*;
import org.json.*;


/**
* Utils is a set of utility funcitons for RDC
*
* @author   Bill Atchley
*/
public class RDCUtils {
    //
    /**
    * Get the id of the current newsletter from the passed in uhash
    *
    * @param nlName   The name of the newsletter uHash
    * @param db     db connection (DbConn)
    *
    * @return       The id
    */
    public static long getCurrentNewsletterID(String nlName, DbConn db) {
        DbConn myConn = db;
        UHash pageContent = null;
        TextItem[] list = null;
        long nlID = 0;
        try {
//System.out.println("RDCUtils - Got here 1 - nlName: " + nlName);
            pageContent = new UHash(nlName, myConn);
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
                    long myID = list[i].getID();
                    //TextItem ti = list[i];
                    TextItem ti = new TextItem(myID, myConn);
                    //ti.lookup(myID, myConn);
                    String temp = ti.getText();
                    String pub = null;
                    String work = null;
                    //jo = new JSONObject(list[i].getText());
                    jo = new JSONObject(temp);
                    pub = jo.getString("published");
                    work = jo.getString("working");
                    if (current == -1 && !ti.getText().equals("external") && pub != null && !pub.equals("")) {
                        current = i;
                        nlID = list[i].getID();
                    }
                }
            }
         } catch (Exception e) { System.out.println("getCurrentNewsletterID-" + e); }

        return (nlID);
    }



}
