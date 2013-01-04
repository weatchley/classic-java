package gov.ymp.csi.review;




public class Row
{
  /**
    * Constructs a ReviewRow object from the data supplied.
   */
    public Row(String docname, String reviewname, String status, String name)
    {
	  document_name = docname;
	  review_name = reviewname;
	  review_status = status;
	  reviewer_name = name;

	  System.out.println("docname=" + document_name);
    }


    public String getAttribute(String name) {
			String value;

			if (name.equals("Document Name")) {
				value = document_name;
			}
			else if (name.equals("Review Name")) {
				value = review_name;
			}
			else if (name.equals("Status")) {
				value = review_status;
			}
			else if (name.equals("Reviewer Name")) {
				value = reviewer_name;
			}

			else {
				value = null;
			}
			//System.out.println("value=" + value);
			return value;
	}

      private String document_name;
      private String review_name;
      private String review_status;
      private String reviewer_name;
}