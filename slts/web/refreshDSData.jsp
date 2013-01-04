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

  <p>Altiris DS Data Load/Update:</p>

<p>Working....</p>
<p>Start: (<%= Utils.genDateID() %>)</p>
<% System.out.println("SLTS Manual Data Update - Start " + Utils.genDateID()); %>
<% ALog.logActivity(usr.getID(), "slts", 2, "SLTS Inventory: Manual Data Update Started at " + Utils.genDateID()); %>
<p>&nbsp; </p>
<br>
<%@ page import="java.sql.*" %>
<%

    Statement stmt = null;
    DbConn lockConn = new DbConn("csi");
    stmt = lockConn.conn.createStatement();
    stmt.execute("SELECT * FROM slts.sw_trans_type FOR UPDATE");
    
%>Load Domains: (<%= Utils.genDateID() %>)<br><%
System.out.println("SLTS Manual Data Update - Load Domains " + Utils.genDateID());
Domain.load(myConn, myConn2);

%>Load Computers: (<%= Utils.genDateID() %>)<br><%
System.out.println("SLTS Manual Data Update - Load Computers " + Utils.genDateID());
Computer.empty(myConn);

Computer.load(myConn, myConn2);

%>Load Applications: (<%= Utils.genDateID() %>)<br><%
System.out.println("SLTS Manual Data Update - Load Applications " + Utils.genDateID());
AppInventory.empty(myConn);

AppInventory.load(myConn, myConn2);

%>Load Services: (<%= Utils.genDateID() %>)<br><%
System.out.println("SLTS Manual Data Update - Load Services " + Utils.genDateID());
ServiceInventory.empty(myConn);

ServiceInventory.load(myConn, myConn2);

System.out.println("SLTS Manual Data Update - Do Counts  " + Utils.genDateID());
%>Update Counts: (<%= Utils.genDateID() %>)<br><%

          SWProduct.zeroOutCounts(myConn);
          SWTransactions.updateLicenseCounts(myConn);
          ProdAppInvMatch.updateLicenseCounts(myConn);
          ProdServInvMatch.updateLicenseCounts(myConn);
          SWProduct.calcRemainingCounts(myConn);
          ProductUsage.updateUsage(myConn);

          
          ALog.logActivity(usr.getID(), "slts", 2, "SLTS Inventory: Manual Data Update Finished " + Utils.genDateID());

%>

<p>End: (<%= Utils.genDateID() %>)</p>
<% System.out.println("SLTS Manual Data Update - Finished " + Utils.genDateID()); %>

<p>Data Loaded</p>

<%
myConn.conn.commit();
myConn.release();
myConn2.release();
lockConn.conn.commit();
lockConn.release();
%>


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
<%

%>