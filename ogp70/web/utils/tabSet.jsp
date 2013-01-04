<jsp:useBean id="tsb" class="gov.ymp.opg.mypages.TabSetBean" scope="session" />
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%
   //retrieve
   		tsb.retrieve("OPG-TabSet-Home-Default");
   		tsb.retrieve("OPG-TabSet-Favorites-Default");
   //save
   		tsb.save((long)1214,"OPG-TabSet-Home");
   //delete
   		//tsb.delete((long)1214,(long)5375);
   //update
   		tsb.update((long)1214,(long)5700);

%>
