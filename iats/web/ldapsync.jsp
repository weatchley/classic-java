<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
<script src=/common/javascript/utilities.js></script>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
<script type="text/javascript" src="javascript/ajax/prototype.js"> </script>
<script type="text/javascript" src="javascript/ajax/ajaxhelper.js"> </script>
<script>
	function syncStart(statusID){
		var index = document.main.sBox.selectedIndex;
		var val = document.main.sBox.options[index].value;
		var statusObj = document.getElementById(statusID);
		ajaxCall(val,statusObj);
	}
</script>
</HEAD>
<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->

<h3>LDAP User Synchronization</h3>
<table border=0 cellpadding=5 cellspacing=5><tr><td>
	<select name=sBox id=sBox>
		<option value="ydservices">ydservices
		<option value="rw.doe.gov">rw.doe.gov
	</select>
</td>
<td>
	<input type=button value=Sync onclick="javascript:syncStart('statusPane');">
</td>
</tr>
<tr><td colspan=2>
</td></tr>
</table>
<div name=statusPane id=statusPane>Synchronization has not started</div>
<input type=hidden name=systemid value=0>

<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>