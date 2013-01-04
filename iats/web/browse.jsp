<%@ page import="gov.ymp.csi.db.*,gov.ymp.iats.model.*,java.sql.*,java.util.*,java.io.*" %>
<%@ include file="headerSetup.inc" %>
<% 

%>
<HTML>
<HEAD>
<TITLE><%= SystemName %>: Browse <rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
  <script type="text/javascript" src="javascript/utils.js"></script>
  <script type="text/javascript" src="javascript/ajax/prototype.js"></script>  
<rdc:isAuthenticated doOpposite="true" >
  <script>
  window.onload = init;
  function init(){
	  var itemlength = document.getElementById("itemlength");
	  for(i=0;i<itemlength.value;i++){
		var item = document.getElementById("delbutton"+i);
		item.disabled=true;	
	  }	  
  }
  </script>
 </rdc:isAuthenticated>
 <script>
  function deleteItem(id){
		alert(id);
  }
  </script>
</HEAD>
<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
    DbConn myConn = new DbConn("csi");
	String porgId = request.getParameter("porgId");
	String orgId = request.getParameter("orgId");
	String year = request.getParameter("year");
	String docRoot = getServletContext().getRealPath("/");
	usr = new Person((String) session.getAttribute("user.name"));
%>
<span style="font: bold 10pt">Assessments by Organization</span>
<table width=100% border=0>
<tr>
<td>
<!-- browse content -->
<% 
	SAReportHTM sah = new SAReportHTM();
	String output = "";
	output = sah.createContent(Integer.parseInt(porgId), Integer.parseInt(orgId), Integer.parseInt(year), myConn);
	out.println(output);
%>		
</td>
</tr>
</table>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>
</BODY>
</HTML>