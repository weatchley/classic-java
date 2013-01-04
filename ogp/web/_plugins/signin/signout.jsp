<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%--  <rdc:sessionClose />  --%>      
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>

<%
session.setAttribute("user.fullname","Guest");
session.setAttribute("user.id",null);
session.setAttribute("user.name",null);
session.setAttribute("user.person",null);
session.setAttribute("user.position",null);
session.setAttribute("user.positionid",null);
session.setAttribute("user.permissionmap",null);

//delete cookies
Cookie userIdCookie =
    new Cookie("user.id", null);
    userIdCookie.setMaxAge(0);
response.addCookie(userIdCookie);
Cookie userNameCookie =
    new Cookie("user.name", null);
    userNameCookie.setMaxAge(0);
response.addCookie(userNameCookie);
%>
signout.jsp