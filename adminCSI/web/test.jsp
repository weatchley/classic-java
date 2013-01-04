<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
<script src=/common/javascript/utilities.js></script>
<script src=/common/javascript/widgets.js></script>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->

<% Calendar cal = Calendar.getInstance(); %>

<p>Test 1: <%= cal.getTime() %></p>
<p>Test 2: <%= Utils.addDays(cal.getTime(), 120) %></p>
<p>Test 3: <%= new java.util.Date() %></p>
<p>Test 4: <%= Utils.castDate(new java.util.Date()) %></p>
<p>Test 5: <!--%= DateUtils.addMinutes((new java.util.Date()), 10) %--></p>

			Stuff



<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
