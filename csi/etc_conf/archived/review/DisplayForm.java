package gov.ymp.csi.review;



import java.io.*;
import java.util.*;

/**
* A ReviewRow holds a collection of rows.
*/
public class DisplayForm
{
  /**
    * Constructs a ReviewRow object rowList from the data supplied.
   */
    public DisplayForm()
    {

    }



     /**
     * Displays the Add Review form.
     */
     public String displayFormRow() {

		 String test;
		 test = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"><title>Form Example</title>";
		 test += "<link rel=\"stylesheet\" href=\"../CSS/sitestyle.css\" type=\"text/css\"></head>";
		 test += "<body>";
		 test += "<h1 align=\"center\">REVIEW DETAIL FORM</h1><hr>";
		 test += "<form name=\"myform\" id=\"myform\" action=\"ReviewTest2.jsp\" method=\"post\" onSubmit=\"return addRecord()\">";
		 test += "<table width=\"550\" border=\"1\">";
		 test += "<tr>";
		 test += "<td><b>Review Name:</b></td>";
		 test += "<td><input type=\"text\" name=\"ReviewName\" size=\"25\" maxlength=\"35\"></td></tr>";
		 test += "<tr>";
		 test += "<td><b>Reviewer Id:</b></td>";
		 test += "<td><input name=\"ReviewerId\" type=\"text\"  size=\"25\" maxlength=\"13\"></td></tr>";
		 test += "<tr>";
		 test += "<td><b>Review Status:</b></td>";
		 test += "<td><input type=\"text\" name=\"Status\" value=\"\" size=\"25\" maxlength=\"30\"></td></tr>";
		 test += "<tr>";
		 test += "<td><b>Due Date:</b></td>";
		 test += "<td><input type=\"text\" name=\"date\" size=\"25\" maxlength=\"10\"></td></tr>";
		 test += "<tr>";
		 test += "<td colspan=\"2\" align=\"center\"><input type=\"submit\" value=\"Add Review\"name=\"SubmitButton\">";
	     test += "<input type=\"reset\" value=\"Reset Form\" name=\"ResetButton\"></td></tr>";
		 test += "</table>";
		 test += "<input type=\"hidden\" name=\"submitVal\" id=\"submitVal\" value=\"\"></form></body></html>";


         return test;
 }


}