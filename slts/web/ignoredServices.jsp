<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");
usr = new Person((String) session.getAttribute("user.name"));
DbConn myConn = new DbConn("csi");

HashMap items = ServiceInventory.getServiceNameHash(myConn, false, true);
HashMap ignore = ServiceIgnore.getItemHash(myConn);

%>
  
<h4>Manage Ignored Services</h4>

<%= HtmlUtils.doDualSelect("servlist", formName, items, ignore, "<b>View</b>", "<b>Ignore</b>") %>

<input type=hidden name=nextscript value="ignoredServices.jsp">

<input type=button name=submitform value="Submit" onClick="javascript:doFormSubmit()">


<script language=javascript><!--

    function doFormSubmit() {
        for (index=0; index < <%= formName %>.servlist.length-1;index++) {
            <%= formName %>.servlist.options[index].selected = true;
        }
        //alert('Not yet implemented');
        submitFormResults("doServIgnore","update")
    }

//-->
</script>


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
