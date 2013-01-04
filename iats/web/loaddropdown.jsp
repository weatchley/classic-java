<%@ page import="gov.ymp.csi.db.*,gov.ymp.iats.model.*,java.sql.*,java.util.*,java.io.*" %>
<% 
String output = "error has occured";
String type;
type = request.getParameter("type");
if (type != null || type.trim().length() > 0) {
	List<String[]> list = new ArrayList<String[]>();
	DbConn myConn = new DbConn("csi");
	output = "dobj = {\"dropdown\":[{\"nvalue\":0,\"tvalue\":\"All\",\"svalue\":\"true\"},";
	if(type.equals("porg")){
		POrganization po = new POrganization();
		list = po.getlist(myConn);
		Iterator<String[]> iterator = list.iterator();
		  while ( iterator.hasNext() ){
			  int j=0; 
			  for (String item : iterator.next()) {
		            //System.out.println(item);
		      	if(j==0){
		          output += "{\"nvalue\":"+item;
		          j++;
		          }else if(j==1){
		          output += ",\"tvalue\":\""+item;
		          j++;
		          }else if(j==2){
		          output += "\",\"svalue\":\"false\"},";
		          j=0;
		      	  }
			  }
		  }
	}else if(type.equals("org")){
		Organization o = new Organization();
		list = o.getlist(myConn);
		Iterator<String[]> iterator = list.iterator();
		  while ( iterator.hasNext() ){
			  int j=0; 
			  for (String item : iterator.next()) {
		            //System.out.println(item);
		      	if(j==0){
		          output += "{\"nvalue\":"+item;
		          j++;
		          }else if(j==1){
		          output += ",\"tvalue\":\""+item;
		          j++;
		          }else if(j==2){
		          output += "\",\"svalue\":\"false\"},";
		          j=0;
		      	  }
			  }
		  }
	}else if(type.equals("year")){
		FiscalYear fy = new FiscalYear();
		list = fy.getlist(myConn);
		Iterator<String[]> iterator = list.iterator();
		  while ( iterator.hasNext() ){
			  for (String item : iterator.next()) {
		          //System.out.println(item);
		          output += "{\"nvalue\":"+item;
		          output += ",\"tvalue\":\""+item;
		          output += "\",\"svalue\":\"false\"},";
		      	  }
			  }
	}else{
	}
	output += "]}";
}else{
}
out.println(output);
%>
