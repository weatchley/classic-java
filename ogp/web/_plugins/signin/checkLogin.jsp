<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<%
Object nameObj = (Object)session.getValue("user.fullname");
if (nameObj == null) {
	session.setAttribute("user.fullname","Guest");
}
String userFullName = session.getAttribute("user.fullname").toString();
if (userFullName=="") {
	session.setAttribute("user.fullname","Guest");
	userFullName="Guest";
}
//System.out.println("checkLogin.jsp:"+userFullName);
%>
<%=userFullName%>