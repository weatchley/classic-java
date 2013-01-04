package gov.ymp.csi.review;


import java.util.ArrayList;
import java.io.*;
import java.util.*;

/**
* A ReviewRow holds a collection of rows.
*/
public class ReviewRow1
{
  /**
    * Constructs a ReviewRow object rowList from the data supplied.
   */
    public ReviewRow1(String aName)
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



     public boolean find(String docname, String reviewname, String status, String reviewername)
     {
		Row row1;

		for (Iterator iterator =rowList.iterator(); iterator.hasNext();)
		{
	 		row1 = (Row) iterator.next();
	 		if ((row1.getAttribute("Document Name").equals(docname)) && (row1.getAttribute("Review Name").equals(reviewname))
	 		     && (row1.getAttribute("Status").equals(status)) && (row1.getAttribute("Reviewer Name").equals(reviewername)))
	 		     return true;
	    }
	    return false;    //no match in the entire array list
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

	 		//System.out.println(name);

	 		test1 = "<table width=\"600\" border=\"1\">";
			test1 += "<caption>List Of Reviews Scheduled</caption>";
            test1 += "<tr><th>Document Name</th><th>Review Name</th><th>Review Status</th><th>Reviewer Name</th><th><form name=\"form\" id=\"form\" method=\"post\" action=\"ReviewTest2.jsp\"><input type=\"button\" name=\"addrow\" value=\"AddReview\" onClick=\"showForm()\"><input type=\"hidden\" name=\"hidVal\" id=\"hidVal\" value=\"\"></form></th></tr>";


	 			for (Iterator iterator =rowList.iterator(); iterator.hasNext();) {
	 				row1 = (Row) iterator.next();
	 				count++;

                    if (row1.getAttribute("Document Name") != null && !(row1.getAttribute("Document Name").equals(""))) {
						 					//test1 += "<tr><td>row1.getAttribute(\"Review Name\")</td>";
						 					String s1 =  row1.getAttribute("Document Name");

						 					//System.out.println("s1=" + s1);
						 					//System.out.println(s1);
						 					test1 += "<tr><td>" + s1 + "</td>";
				    }


	 				if (row1.getAttribute("Review Name") != null && !(row1.getAttribute("Review Name").equals(""))) {
	 					//test1 += "<tr><td>row1.getAttribute(\"Review Name\")</td>";
	 					String s2 =  row1.getAttribute("Review Name");
	 					//System.out.println("s2=" + s2);
	 					test1 += "<td>" + s2 + "</td>";

	 				}

	 				if (row1.getAttribute("Status") != null && !(row1.getAttribute("Status").equals(""))) {
						 					test1 += "<td>" + row1.getAttribute("Status") + "</td>";
	 				}

	 				if (row1.getAttribute("Reviewer Name") != null && !(row1.getAttribute("Reviewer Name").equals(""))) {
						if (row1.getAttribute("Reviewer Name").equals(name)) {
						   String testName = row1.getAttribute("Reviewer Name");
						   //System.out.println("Name = " + testName);
	 					   test1 += "<td><a href=\"ReviewTest2.jsp?name=" + testName + "\">" + row1.getAttribute("Reviewer Name") + "</a></td>";
					    }
	 					else {
	 					      test1 += "<td>" + row1.getAttribute("Reviewer Name") + "</td>";
					    }
	 				}

	 				if (row1.getAttribute("Reviewer Name").equals(name)) {
						String testName1 = name;
						test1 += "<td><form name=\"form1\" id=\"form1\" method=\"post\" action=\"ReviewTest2.jsp?testname=" + testName1 + "\"><input type=\"button\" name=\"delete\" value=\"Delete\" onClick=\"deleteRow()\"><input type=\"hidden\" name=\"hidDelete\" id=\"hidDelete\" value=\"\"></form></td>";
					}
					else {
						   test1 += "<td>&nbsp;</td>";
				    }

	 				test1 += "</tr>";
	 			}
	 			test1 += "</table>";


	 		return test1;
	 	}

      public String retrieveReviewRow(String name1) {

		    String test1;
		    Row row1;

            //System.out.println("name = " + name1);
            //test1 = "<table width=\"600\" border=\"1\">";
			//test1 += "<caption>Review Information</caption>";
            test1 = "<b>Reviewer Name:" + name1 + "</b><br>";
            for (Iterator iterator =rowList.iterator(); iterator.hasNext();) {
	 			 row1 = (Row) iterator.next();
	 			 if (row1.getAttribute("Reviewer Name").equals(name1)) {
					 test1 += "<b>Review:" + row1.getAttribute("Review Name") + "</b><br>";
					 test1 += "<b>Review Status:" + row1.getAttribute("Status") + "</b><br>";
			     }
		    }

            //System.out.println("Testing Row");

            return test1;

  }

      public void deleteRow(String name)  {

		  Row row1;

		  System.out.println("INSIDE DELETE ROW");


		       for (int j = 0; j < rowList.size(); j++)
			   {
			   	   row1 = (Row)rowList.get(j);

			   	   if (row1.getAttribute("Reviewer Name").equals(name))
			   	   {
			 	      rowList.remove(j);
			       }
		       }
      }



      private ArrayList rowList;
      private String name;

}