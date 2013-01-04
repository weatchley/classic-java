<HTML>

<HEAD>
<TITLE>Records Plan Management System</TITLE>
  <LINK href="css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="headerSetup.inc" %>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");
%>
  

  <h3>Reports</h3><br>
  
<!--p>&nbsp; </p-->
    Location: <select name=location size=1 onChange=doTitleBarLookup()><option value='0'>All</option>
    <%
    //FOrganization [] foList = FOrganization.getItemList();
    if (foList != null) {
        for (int i=0; i<foList.length; i++) {
            %><option value='<%= foList[i].getID() %>'><%= foList[i].getDescriptionBrief(60) %></option>
            <%
        }
    }
    %>
    </select>
<ul>
<li>Annual File Plan <a href="javascript:doAnnualFilePlanXLS()">(Excel)</a> <a href="javascript:doAnnualFilePlanPDF()">(PDF)</a><% if (isAdmin) { %> <a href="javascript:doAnnualFilePlanPDF('true')">(Archive)</a><% } %></li>
<li>Annual File Plan by Location <a href="javascript:doAnnualFilePlan2XLS()">(Excel)</a> <a href="javascript:doAnnualFilePlan2PDF()">(PDF)</a><% if (isAdmin) { %> <a href="javascript:doAnnualFilePlan2PDF('true')">(Archive)</a><% } %></li>
<li>Annual Short-term Records for Disposition <a href="javascript:doAnnualShortTermXLS()">(Excel)</a> <a href="javascript:doAnnualShortTermPDF()">(PDF)</a><% if (isAdmin) { %> <a href="javascript:doAnnualShortTermPDF('true')">(Archive)</a><% } %></li>
<li>Annual Short-term Records for Disposition by Citation <a href="javascript:doAnnualShortTerm2XLS()">(Excel)</a> <a href="javascript:doAnnualShortTerm2PDF()">(PDF)</a><% if (isAdmin) { %> <a href="javascript:doAnnualShortTerm2PDF('true')">(Archive)</a><% } %></li>
<li>Annual Long-term Records for Disposition <a href="javascript:doAnnualLongTermXLS()">(Excel)</a> <a href="javascript:doAnnualLongTermPDF()">(PDF)</a><% if (isAdmin) { %> <a href="javascript:doAnnualLongTermPDF('true')">(Archive)</a><% } %></li>
</ul>

<input type=hidden name=reporttype value="pdf">
<input type=hidden name=archiveflag value="false">
<script language=javascript><!--
    
    //function doAnnualFilePlan() {
    //    submitForm("report1.jsp","annualfileplan");
    //}
    
    function doAnnualFilePlanXLS() {
        submitFormResults("doReportXLS","annualfileplan");
    }
    
    function doAnnualFilePlanPDF(flag) {
        document.main.reporttype.value='pdf';
        if (flag=='true') {
            document.main.archiveflag.value='true';
        } else {
            document.main.archiveflag.value='false';
        }
        submitFormNewWindow("report1.iText.jsp","annualfileplan");
    }
    
    function doAnnualFilePlan2XLS() {
        submitFormResults("doReportXLS","annualfileplan2");
    }
    
    function doAnnualFilePlan2PDF(flag) {
        document.main.reporttype.value='pdf';
        if (flag=='true') {
            document.main.archiveflag.value='true';
        } else {
            document.main.archiveflag.value='false';
        }
        submitFormNewWindow("report1.iText.jsp","annualfileplan2");
    }
    
    //function doAnnualShortTerm() {
    //    submitForm("report1.jsp","annualshortterm1");
    //}
    
    function doAnnualShortTermXLS() {
        submitFormResults("doReportXLS","annualshortterm1");
    }
    
    function doAnnualShortTermPDF(flag) {
        document.main.reporttype.value='pdf';
        if (flag=='true') {
            document.main.archiveflag.value='true';
        } else {
            document.main.archiveflag.value='false';
        }
        submitFormNewWindow("report1.iText.jsp","annualshortterm1");
    }
    
    //function doAnnualShortTerm2() {
    //    submitForm("report1.jsp","annualshortterm2");
    //}
    
    function doAnnualShortTerm2XLS() {
        submitFormResults("doReportXLS","annualshortterm2");
    }
    
    function doAnnualShortTerm2PDF(flag) {
        document.main.reporttype.value='pdf';
        if (flag=='true') {
            document.main.archiveflag.value='true';
        } else {
            document.main.archiveflag.value='false';
        }
        submitFormNewWindow("report1.iText.jsp","annualshortterm2");
    }
    
    //function doAnnualLongTerm() {
    //    //document.main.location.selectedIndex = 0;
    //    submitForm("report1.jsp","annuallongterm");
    //}
    
    function doAnnualLongTermXLS() {
        //document.main.location.selectedIndex = 0;
        submitFormResults("doReportXLS","annuallongterm");
    }
    
    function doAnnualLongTermPDF(flag) {
        document.main.reporttype.value='pdf';
        if (flag=='true') {
            document.main.archiveflag.value='true';
        } else {
            document.main.archiveflag.value='false';
        }
        //document.main.location.selectedIndex = 0;
        submitFormNewWindow("report1.iText.jsp","annuallongterm");
    }

    //-->
</script>


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
