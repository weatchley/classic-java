<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %>: Home <rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
  
  <script type="text/javascript" src="javascript/utils.js"></script>
  <script type="text/javascript" src="javascript/ajax/prototype.js"></script>  
  <script src="_plugins/browser/browser.js" type="text/javascript"></script>
	<script>
	  window.onload = init;
	  function init(){
		loaddropdown("porg");
		loaddropdown("org");
		loaddropdown("year");
	  }
	  function loaddropdown(type){		
			var parameters = "type="+type;
			var url="./loaddropdown.jsp?"+parameters;
			var ajaxRequest = new Ajax.Request(url,
		              {
		                     method: 'get',
		                     parameters: parameters,
		                     onLoading: function() {
		            		 },
		                     onSuccess: function(transport){
		      				 var response = transport.responseText || "no response text";
							 //alert(response);
							 var jsobj = {};
							 jsobj = eval(transport.responseText);
	 						 if(jsobj!=null){
	 							 for(i=0;i<dobj.dropdown.length;i++){ 							 
		 							var option = document.createElement("option"); 
		 			      			var text = document.createTextNode(dobj.dropdown[i].tvalue);
		 			      			option.appendChild(text);
		 			      			option.setAttribute("value",dobj.dropdown[i].nvalue);
		 			      			var obj = document.getElementById(type);	
		 			      			obj.appendChild(option);
	 							 }
	 	 	 	 			 }
							 	
			    			 },
		    				 onFailure: function(){ alert('callAjax: Something went wrong...') }
		              });
	  }
	  </script>
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");
usr = new Person((String) session.getAttribute("user.name"));
%>
  <rdc:isAuthenticated >Welcome&nbsp;<%= session.getAttribute("user.fullname") %></rdc:isAuthenticated>
<ul>
<table border=0 cellpadding=5 cellspacing=5 width="65%">
<tr>
<td colspan=2 align=left>
<table border=0 width=100%>
<tr>
	<td width=10%>
	<li><span style="font: bold 10pt">View&nbsp;</span></li>
	</td>
	<td width=10%><span style="font: bold 10pt">Organizations&nbsp;</span>
	</td><td colspan=2  width=80%>
		<select name=porg id=porg>
		</select>
	</td>
	<td>&nbsp;</td>
	</tr>
	 
	<tr>
	<td>&nbsp;</td>
	<td>
	<span style="font: bold 10pt">Divisions&nbsp;</span>
	</td><td colspan=2>
		<select name="org" id=org>
		</select>	
	</td>
	<td>&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td align=left>
	<span style="font: bold 10pt">Fiscal Year&nbsp;</span>
	</td>
	<td  colspan=2>	
		<select name="year" id=year>
		</select>
	</td>
	<td align=right>&nbsp;<input type="button" name="browse" value="Go" onClick="submitFormNew('browse','all','org');">
	</td>
</tr>
</table>
<!-- input type="button" name="browse" value="Go" onClick="submitFormNew('browse','all','org');"--></td>
</tr>
<tr>
<td colspan=2>
<li><b><a href="reports.jsp">Assessment Reports</a></b></li></td>
</tr>
</table>
</ul>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
