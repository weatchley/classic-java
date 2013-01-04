package gov.ymp.csi.review;

import java.util.ArrayList;
import java.io.*;
import java.util.*;

/**
* A ReviewRow holds a collection of rows.
*/
public class ReviewRow
{
  /**
    * Constructs a ReviewRow object rowList from the data supplied.
   */
    public ReviewRow(String aName)
    {
	  rowList = new ArrayList();
	  name = aName;

    }

    /**
    * Adds a new Row object.
    * @param aRow the row to add
    */
    public void addRow(Row aRow)
    {
		 rowList.add(aRow);
    }


     /**
     * Displays the ArrayList object.
     * If the Reviewer Name stored in the arraylist matches aName, then there is a
     * link associated with that Reviewer Name, also the delete button is added for
     * that reviewer name
     */
     public String displayReviewRow() {

	 		String test1;
	 		Row row1;
	 		int count = 0;

	 		System.out.println(name);

	 		test1 = "<table width=\"600\" border=\"1\">";
			test1 += "<caption>List Of Reviews Scheduled</caption>";
            test1 += "<tr><th>Review Name</th><th>Reviewer Name</th><th>Review Status</th><th><form><button type=\"button\" name=\"add\" value=\"123\">Add Review</button></form></th></tr>";

	 			for (Iterator iterator =rowList.iterator(); iterator.hasNext();) {
	 				row1 = (Row) iterator.next();
	 				count++;


	 				if (row1.getAttribute("Review Name") != null && !(row1.getAttribute("Review Name").equals(""))) {
	 					//test1 += "<tr><td>row1.getAttribute(\"Review Name\")</td>";
	 					String s1 =  row1.getAttribute("Review Name");
	 					System.out.println(s1);
	 					test1 += "<tr><td>" + s1 + "</td>";

	 				}

	 				if (row1.getAttribute("Reviewer Name") != null && !(row1.getAttribute("Reviewer Name").equals(""))) {
						if (row1.getAttribute("Reviewer Name").equals(name)) {
	 					   test1 += "<td><a href=\"http://www.yahoo.com/\">" + row1.getAttribute("Reviewer Name") + "</a></td>";
					    }
	 					else {
	 					      test1 += "<td>" + row1.getAttribute("Reviewer Name") + "</td>";
					    }
	 				}
	 				if (row1.getAttribute("Status") != null && !(row1.getAttribute("Status").equals(""))) {
	 					test1 += "<td>" + row1.getAttribute("Status") + "</td>";
	 				}
	 				if (row1.getAttribute("Reviewer Name").equals(name)) {
						test1 += "<td><form><button type=\"button\" name=\"delete\" value=\"d2\">Delete</button></form></td>";
					}
					else {
						   test1 += "<td>&nbsp;</td>";
				    }

	 				test1 += "</tr>";
	 			}
	 			test1 += "</table>";


	 		return test1;
	 	}


      private ArrayList rowList;
      private String name;

}