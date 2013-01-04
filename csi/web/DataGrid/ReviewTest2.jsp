<%@ page import="gov.ymp.csi.review.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>


<HTML>
<HEAD>
<TITLE>Testing The Review Class</TITLE>
<script language="Javascript">
<!--

function showForm(){
                alert("!");
                document.form.hidVal.value = "new";
		document.form.submit();
}

function addRecord() {
                alert("!!!");
                document.myform.submitVal.value = "add";
}

function deleteRow() {
                alert("!!!!");
                document.form1.hidDelete.value = "delete";
                document.form1.submit();
}
                
//-->
</script>

<link rel="stylesheet" href="sitestyle.css" type="text/css">

</HEAD>
<BODY>
<%
 
 String docid = request.getParameter("docid");
 String username = request.getParameter("username");
 
 session = request.getSession();
  DisplayReview review1 = (DisplayReview)session.getAttribute("review1");
  ReviewRow1 review2 = (ReviewRow1)session.getAttribute("review2");
  if ((review1 == null) && (review2 == null)) {
       out.println("<H1>Testing The Review Class</H1>");
       
       review1 = new DisplayReview(docid, username);
       review2 = new ReviewRow1(username);
       session.setAttribute("review1", review1);
       session.setAttribute("review2", review2);
       ArrayList str = review1.getReviewInfo();
       //String command = "";
       String str1 = review1.organizeData(str, review2);
       
        
       out.println(str1);
 
       
  
  }
    
  String name1 = request.getParameter("name");
  //out.println(name1);
  if (name1 != null) {
      out.println("<H1>Testing Row</H1>");
      String value = review2.retrieveReviewRow(name1);
      out.println(value);
      
 }
 
 
 if (request.getParameter("hidVal") != null){
      out.println("Yet Another Test");
      if (request.getParameter("hidVal").equals("new")){
  	//display the form
  	
          out.println("This is a Test");
          DisplayForm form1 = new DisplayForm();
          String value = form1.displayFormRow();
          out.println(value);
       }
   }
   
   
    //if (request.getParameter("hidDelete") == null){
        //out.println("It Cannot be Null!!");
    //}
   
   if (request.getParameter("hidDelete") != null){
        out.println("Deletion Test");
        if (request.getParameter("hidDelete").equals("delete")){
    	//delete the row
    	
            String name2 = request.getParameter("testname");
            out.println(name2);
            
            review1.deleteReview(name2);
            //ArrayList str = review1.getReviewInfo();
           // String command = "delete";
            review2.deleteRow(name2);
            
                      
            //review2.deleteRow(name2);
            String value = review2.displayReviewRow();
            out.println(value);
         }
   }
       
  String reviewName = request.getParameter("ReviewName");
  String reviewerId = request.getParameter("ReviewerId");
  String status = request.getParameter("Status");
  String date = request.getParameter("date");
  String docname = review1.getDocname();
  
  if ((reviewName != null) && (reviewerId != null) && (status != null)) {
      //review2.addRow(new Row(docname, reviewName, status, reviewerName));
      //String value1 = review2.displayReviewRow();
      //out.println(value1); 
      
      out.println(reviewName);
      out.println(reviewerId);
      out.println(docname);
      
      review1.addReviewInfo(reviewName, status, reviewerId, docname);
      ArrayList newstr = review1.getReviewInfo();
      //String command = "insert";
      String newstr1 = review1.organizeData(newstr, review2);
      //String newstr1 = review2.displayReviewRow();
             
      out.println(newstr1);
 
      
  }
  
  	       
  //if (request.getParameter("submitVal") != null){ 
  //    out.println("Addition Test");
  //    if (request.getParameter("submitVal").equals("add")){
  	       //add the record
  //	       out.println("This is the record addition test");
  //	       String reviewName = request.getParameter("ReviewName");
  //	       String reviewerName = request.getParameter("ReviewerName");
  //	       String status = request.getParameter("Status");
  //	       String date = request.getParameter("date");
  //	       review1.addRow(new Row(reviewName, reviewerName, status));
  //	       String value1 = review1.displayReviewRow();
  //	       out.println(value1);
  //	}
  //}
 
 
 

  
 
   
 
 %>
 
</BODY></HTML>

