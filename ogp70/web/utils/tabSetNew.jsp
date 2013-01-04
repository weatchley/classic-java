<jsp:useBean id="tsb" class="gov.ymp.opg.mypages.TabSetBean" scope="session" />
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%
   //retrieve
   	String homeSection	   = tsb.retrieve("OPG-TabSet-Home-Default"); // refers to the different section for tabsettings 
    String favoriteSection = tsb.retrieve("OPG-TabSet-Favorites-Default");
   //save
   		long tabsetID = tsb.save((long)3676,"OPG-TabSet-Home");
   //delete
   		//tsb.delete((long)1214,(long)5375);
   //update
   		tsb.update((long)3676,tabsetID);
   
   // get the content as a string 
   //    out.println("<font color=green size = 4>");
   //    out.println("Home-Default Tabs " + homeSection);
   //    out.println("</font>");
       
    // create the json string 
    // out.println(myResponse); 
    // write the content to the browser 
    // send the response to the XMLHTTPObject 
     
    System.out.println(homeSection); //final result 
    
  
%>




