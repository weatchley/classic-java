<%@ include file="headerSetup.inc" %>
<HTML>
<HEAD>
<TITLE><%= SystemName %>: Reports <rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
  <script type="text/javascript" src="javascript/utils.js"></script>
  <script type="text/javascript" src="javascript/ajax/prototype.js"></script>  
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
  <rdc:isAuthenticated >You are logged in as &nbsp;<%= session.getAttribute("user.fullname") %></rdc:isAuthenticated>
<table border=0 width=""cellspacing=5 cellpaddding=2 >
	<tr>
		<td colspan=3>
			<span style="font: bold 10pt">Report Columns</span><span style="font: italic 10pt"><br>(Select or de-select the columns to include on the report)</span>
		</td></tr>
	<tr>
		<td><input type=checkbox name=sanumber checked>&nbsp;<span style="font: bold 10pt">SA Number</span></td><td><input type=checkbox name=teamlead checked>&nbsp;<span style="font: bold 10pt">Team Lead</span></td><td><input type=checkbox name=satitle checked>&nbsp;<span style="font: bold 10pt">Title</span></td></tr>
	<tr>
	<td>
	<input type=checkbox name=purpose checked>&nbsp;<span style="font: bold 10pt">Purpose</span></td><td><input type=checkbox name=scheduledate checked>&nbsp;<span style="font: bold 10pt">Scheduled&nbsp;Date</span></td><td><input type=checkbox name=rescheduledate checked>&nbsp;<span style="font: bold 10pt">Re-Scheduled&nbsp;Date</span></td></tr>
	<tr>
	<td><input type=checkbox name=signdate checked>&nbsp;<span style="font: bold 10pt">Signed Date</span></td><td>
	<input type=checkbox name=canceldate checked>&nbsp;<span style="font: bold 10pt">Cancelled Date</span></td><td><input type=checkbox name=cancelledrationale checked>&nbsp;<span style="font: bold 10pt">Cancelled&nbsp;Rationale</span></td></tr>
	<tr>
	<td><input type=checkbox name=comment checked>&nbsp;<span style="font: bold 10pt">Comments</span></td><td>
	<input type=checkbox name=condreport checked>&nbsp;<span style="font: bold 10pt">Condition&nbsp;Report</span></td><td>
	<input type=checkbox name=asstype checked>&nbsp;<span style="font: bold 10pt">Assessment Type</span></td></tr>
	<tr>
	<td>
	<input type=checkbox name=assobj checked>&nbsp;<span style="font: bold 10pt">Assessment Objective</span></td><td>
	<input type=checkbox name=crlevels checked>&nbsp;<span style="font: bold 10pt">CR Level</span></td><td>
	<input type=checkbox name=crnums checked>&nbsp;<span style="font: bold 10pt">CR Number</span></td></tr>
	<tr>
	<td>
	<input type=checkbox name=llnums checked>&nbsp;<span style="font: bold 10pt">Lessons&nbsp;Learned&nbsp;Number</span></td><td>
	 &nbsp; </td><td>
	 &nbsp; </td></tr>
	<tr>
	<td><span style="font: bold 10pt">Organizations&nbsp;</span>
	</td><td colspan=2>
		<select name=porg id=porg>
		</select>
	</td>
	</tr>
	 
	<tr>
	<td>
	<span style="font: bold 10pt">Divisions&nbsp;</span>
	</td><td colspan=2>
		<select name="org" id=org>
		</select>	
	</td>
	</tr>
	<tr><td align=left>
	<span style="font: bold 10pt">Assessment Report for Fiscal Year&nbsp;</span>
	</td><td  colspan=2>	
		<select name="year" id=year>
		</select>
	</td></tr>
	<tr><td align=center colspan=3><!--input type="button" name="report" value="Generate Web Report" onClick="javascript:submitFormNew('reports','all','html');"-->&nbsp;&nbsp;<input type="button" name="report" value="Generate PDF Report" onClick="javascript:submitFormNew('reports','all','pdf');">&nbsp;&nbsp;<input type="button" name="report" value="Generate Excel Report*" onClick="javascript:submitFormNew('reports','all','xsl');"></td>
	</tr>
	<tr>
		<td colspan=3 style="font: bold 10pt">*To access the excel report, your browser's security setting will need to be changed as follows:<ol><li>Go to Tools -> Internet Options -> Security<li>Click on "Trusted Sites" -> Sites<li>Add "https://ocrwmgateway.ocrwm.doe.gov". Hit OK<li>In "Custom Level", under "Downloads", be sure that "Automatic prompting for files downloads" to be "Enabled"</ol></td>
	</tr>
</table>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>
</BODY>
</HTML>