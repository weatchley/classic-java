package gov.ymp.csi.review;


public class DisplayList
{
  /**
    * Constructs a Review object for a given Documentid.
   */
    public DisplayList()
    {
	  docid = "MY Document";
    }


    /**
     * Get the Review information for this Document Id from the data Engine.
     *1. Calls the Data Engine to retrieve the review list for that Id.
	 *
	 *		    Data retrieved from the data engine for that docid:
	 *		       1. Reviewer Names
	 *		       2. Review Name
  	 *	           3. Review Status
  	 *             4. Due Date
  	 *          or if no reviews scheduled yet, data engine returns nothing
  	 */
     private String getReviewInfo()
     {
		 return "getReviewInfo";
     }

     public String getDocid()
     {
		 return docid;
     }


     /**
      * Organize and formats the data returned by the Data Engine.
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
      private String organizeData()
      {
		  return "organizeData";

       }

    /**
     * Shows the scheduled review information.
     *1. getReviewInfo;
	 *2. organizeData;
	 *3. Calls the Display routine
     *
     */
    public String displayScheduledReview()
    {
		String test1 = "<table width=\"600\" border=\"1\">";
               test1 += "<caption>List Of Reviews Scheduled</caption>";
               test1 += "<tr><th>Review Name</th><th>Reviewer Name</th><th>Review Status</th><th><form><button type=\"button\" name=\"add\" value=\"123\">Add Review</button></form></th></tr>";
	           test1 += "<tr><td>First Draft Document</td><td><a href=\"http://www.yahoo.com/\">Chris</a></td><td>Review</td><td><form><button type=\"button\" name=\"delete\" value=\"d1\">Delete</button></form></td></tr>";
               test1 += "<tr><td>Peer</td><td><a href=\"http://www.yahoo.com/\">Mita</a></td> <td>Complete</td><td>&nbsp;</td></tr>";
               test1 += "<tr><td>Approval</td><td><a href=\"http://www.yahoo.com/\">Dave</a></td><td>Complete</td><td><form><button type=\"button\" name=\"delete\" value=\"d2\">Delete</button></form></td> </tr>";
               test1 += "<tr><td>Concurrence</td><td><a href=\"http://www.yahoo.com/\">Jodi</a></td> <td>Concurred;</td> <td>&nbsp;</td> </tr>";
               test1 += " </table>";

        return test1;

    }


      private String docid;
}