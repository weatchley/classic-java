<jsp:useBean id="tsb" class="gov.ymp.opg.mypages.TabSetBean" scope="session" />

<%@page import="gov.ymp.csi.db.*"%>
<%@page import="gov.ymp.csi.db.ALog.*"%>
<%@page import="gov.ymp.csi.UNID.*"%>
<%@page import="gov.ymp.csi.people.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>

<% 
      String tabString= "";
	  String pagev = request.getParameter("page");
	  String tabtype = request.getParameter("tabtype");
	  tabtype = Character.toUpperCase(tabtype.charAt(0)) + tabtype.substring(1,tabtype.length());
	  
	  long userID = 0;
	  long tabsetID = 0;
	  String userid2 = "";
	  try
	  {
	   userID = ((session.getAttribute("user.id")  != null) ? Long.parseLong((String) session.getAttribute("user.id")) : 0);
	   userid2 = (String)session.getAttribute("user.id");
	  }
	  catch(Exception ex)
	  {	  
	  }	
	if (pagev.equals("0"))   //retrieves default tabset
	    tabString = tsb.retrieve("OPG-TabSet-"+tabtype+"-Default");
	  else if (pagev.equals("1")){    //retrieves userid tabset
		//call retrieve function in the bean and print out the resultset (i.e. id, description, rank)
		tabsetID = tsb.save(userID,"OPG-TabSet-"+tabtype);
		tabString = tsb.retrieve("OPG-TabSet-"+tabtype+"-"+userid2);
	   }
	    else if (pagev.equals("2")){   //rearranges tabs
		  String output[] = request.getParameter("saveOracle").split("~");
	      int[] orderarray = new int[output.length];
	    
	      for (int i = 0; i < output.length; i++)
	    	orderarray[i] = Integer.parseInt(output[i]);
	         //save
	   		 tabsetID = tsb.save(userID,"OPG-TabSet-"+tabtype);
		     System.out.println("tabsid is" + tabsetID);	  
	         //update
	   		 tsb.update(userID,tabsetID,orderarray);
	         //delete
	   		 //tsb.delete((long)1214,(long)5375);    	
	       }
	      else if (pagev.equals("3"))  //saves tabs
	        {
	    	  String output2[] = request.getParameter("saveOracle").split("~");
	    	  //save last tab; this is the newly created tab
			  tabsetID = tsb.saveNewTab(userID,"OPG-TabSet-"+tabtype, output2[output2.length-1]);  	 
	        }  
	       else if (pagev.equals("4"))  //deletes a tab
	        {
	    	  //deletes current tabs
	    	  tabsetID = tsb.delete(userID,"OPG-TabSet-"+tabtype); 
	    	  
	    	  String output3[] = request.getParameter("saveOracle").split("~");
	    	  //save all remaining tabs
	    	  for(int i = 0; i < output3.length; i++)
			     tabsetID = tsb.saveNewTab(userID,"OPG-TabSet-"+tabtype, output3[i]);
	         } 
		    else if (pagev.equals("5")){    //retrieves userid tabset
				//call retrieve function in the bean for the rearrange function
				tabString = tsb.retrieve("OPG-TabSet-"+tabtype+"-"+userid2);
			   }
%>
<%=tabString%>