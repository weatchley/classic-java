<%@ page import="java.util.*" %>
<%@ page import="gov.ymp.csi.mypages.*" %>
<%
	String rString = "";
	int uId = Integer.parseInt(request.getParameter("uid"));
	rString = getPages(uId);
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("pragma","no-cache");	
	response.getWriter().write(rString);	
%>
<%!
//private methods...
private String getPages(int uId){
	String oString = "";
	try{
		Layouts layouts = new Layouts (uId);
		oString += layouts.getUserXML();
			//System.out.println(layouts.getUserXML());
		//oString += layouts.getLayoutsXML();
			//System.out.println(layouts.getLayoutsXML());
		//oString += layouts.getPagesNamesXML();
			//System.out.println(layouts.getPagesNamesXML());
		long personID = layouts.getPersonID();
	}catch(Exception e){
		System.out.println(e);
	}
	return oString;
}
%>