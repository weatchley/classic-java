<%@page import="java.util.*" %>
<%
	String otype = request.getParameter("type");
%>
<%
if (otype.equals("html")){
%>
create table
<% }else if(otype.equals("pdf")){ %>
pdf report
<% }else if(otype.equals("xsl")){ %>
xsl report
<% }else{ %>
<% } %>