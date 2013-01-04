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
DbConnM myConn2 = new DbConnM();

String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
int i = 0;

%>
  

  Welcome<rdc:isAuthenticated >&nbsp;<%= session.getAttribute("user.fullname") %></rdc:isAuthenticated><br>
  <br>

  <p>Updating Counts:</p>

<p>Working....</p>
<p>Start: (<%= Utils.genDateID() %>)</p>
<br>
<%
%>Update Counts: (<%= Utils.genDateID() %>)<br><%

          SWProduct.zeroOutCounts(myConn);
          SWTransactions.updateLicenseCounts(myConn);
          ProdAppInvMatch.updateLicenseCounts(myConn);
          ProdServInvMatch.updateLicenseCounts(myConn);
          SWProduct.calcRemainingCounts(myConn);
          
          ProductUsage.updateUsage(myConn);


          myConn.conn.commit();
          
          ALog.logActivity(usr.getID(), "slts", 2, "SLTS Inventory: Count update run manually");

%>

<p>End: (<%= Utils.genDateID() %>)</p>

<p>Counts have been updated.</p>

<%
myConn.release();
myConn2.release();
%>


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
