package gov.ymp.csi.review;

import java.sql.*;
import java.util.StringTokenizer;
import java.util.ArrayList;


public class DisplayReview
{
  /**
    * Constructs a Review object for a given Documentid
   */
    public DisplayReview(String aDocid, String aUsername)
    {
	  docid = aDocid;
	  username = aUsername;
    }


    /**
     * Get the Review information for this Document Id from the data Engine
     *1. Calls the Data Engine to retrieve the review list for that Id.
	 *
	 *		    Data retrieved from the data engine for that docid:
	 *		       1. Reviewer Names
	 *		       2. Review Name
  	 *	           3. Review Status
  	 *             4. Due Date
  	 *          or if no reviews scheduled yet, data engine returns nothing
  	 */
     public ArrayList getReviewInfo()

     {

         ArrayList str = DataEngine.showReviewerTable(driver, url, user, password, docid);
         //System.out.println("After calling the data engine");
         //System.out.println("Table Not Found");
         //System.out.println(str);
         //organizeData(str);

		 return str;
     }

     public String getDocid()
     {
		 return docid;
     }


     public String getDocname()
     {
		 String Docname = DataEngine.getDocumentName(driver, url, user, password, docid);
		 return Docname;
     }


     public void addReviewInfo(String reviewname, String status, String reviewerid, String docname)

     {

		 DataEngine.insertReviewRow(driver, url, user, password, reviewname, status, reviewerid, docid);
		 //DataEngine.insertReviewRow(driver, url, user, password, reviewname, status, reviewername, docname);
		 //String username = DataEngine.getUserName(driver, url, user, password, reviewerid);
		 //review.addRow(new Row(docname, reviewname, status, username));
     }


     public void deleteReview(String reviewername)

     {
		 DataEngine.deleteReviewRow(driver, url, user, password,  reviewername, docid);

     }



     /**
      * Organize and formats the data returned by the Data Engine
      *
      * Things we know when the user logged in: DocId, User privs on doc, username
      *
      *1. Prepares the data for the display function
	  *
	  *	       1. Determine number of rows and number of columns to pass to the display routine
	  *	          (From the number of records returned by the Data Engine)
	  *
	  *	       2. Determine the names of the column headers (Review Name, Reviewer Name, Status, Due Date)
	  *
	  *	       3. Row Loop: (Number of rows returned by the data engine)
	  *	           1. Pass to the display routine: column 1 = review_name
	  *	           2. If username = reviewer_name, or if review_name = teamlead
	  *               column 2 = reviewer_name(link) --> pass the URL
	  *	              else column2 = reviewer_name(text)
	  *	           3. column3 = review_status
	  *	           4. Does user have privilege to delete?  If yes, pass button, else pass blank
      *        End Loop
      *        if Data Engine returns nothing(number of rows = 0)
      *        then checks if user has permission to add a review.
      *        if yes, then pass to the display routine:
      *            1. (number of columns, 0 rows)
      *            2. Column header information
      *            3. 'Add Review' Button
      */
      public String organizeData(ArrayList myList, ReviewRow1 review)
      {

		  //return "organizeData";
          //StringTokenizer tokenizer = new StringTokenizer(inputStr);
          //ReviewRow1 review1 = new ReviewRow1(username);

          //System.out.println("Inside the organizeData");

         // while (tokenizer.hasMoreTokens())
       /**   {
			  String docname = tokenizer.nextToken();
			  String reviewname = tokenizer.nextToken();
			  String status = tokenizer.nextToken();
			  String reviewername = tokenizer.nextToken();

			  System.out.println(docname);
			  System.out.println(reviewname);
			  System.out.println(status);
			  System.out.println(reviewername);

			  review.addRow(new Row(docname, reviewname, status, reviewername));
	      }**/

	      int i = 0;

		  while (i < myList.size())
		  {
		        String docname = (String)myList.get(i);
		        i++;
		  		String reviewname = (String)myList.get(i);
		  		i++;
		  		String status = (String)myList.get(i);
		  		i++;
		  		String reviewername = (String)myList.get(i);
		  		i++;



                   if (!review.find(docname, reviewname, status, reviewername))
                   review.addRow(new Row(docname, reviewname, status, reviewername));

			    /**else if (command.equals("delete"))
			    {
				   review.deleteRow(docname, reviewname, status, reviewername);
			    }**/
           }

		  String value3 = review.displayReviewRow();
		  return value3;

       }

    /**
     * Shows the scheduled review information
     *1. getReviewInfo;
	 *2. organizeData;
	 *3. Calls the Display routine
     *
     */
    public String displayScheduledReview()
    {
		return "displayScheduledReview";

    }


      private String docid;
      private String username;
      private static String driver = "oracle.jdbc.driver.OracleDriver";
	  private static String url = "jdbc:oracle:thin:@ydoradev:1521:ydor";
	  private static String user = "dattam";
	  private static String password = "Puchke-sona";
}