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
String tempText = null;
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
%>
  

<p>Welcome to the ITSS Software License Tracking System.</p>
<p>This system performs the following functions:</p>
<ul>
<li>Manage software licenses, both purchased and open source/freeware.</li>
<li>Review software on a PC or what PC's may have a given software package installed.</li>
</ul>
<p>Any user can log in to the system.  He or she must be assigned permissions to perform most functions. 
A general user who does not have any assigned permissions may update his or her contact information and view information in the system.</p>
<p>&nbsp; </p>
<br>

<% //System.out.println("home.jsp-Got Here 1"); %>
<!-- **************    Global Counts      **************** -->
<h4>Global Counts</h4>
<p>Total Active Software Products: <%= SWProduct.getCountTotalActive(myConn) %></p>
<p>Total Licenses Exceeding Authorized Count: <%= SWProduct.getCountLicenseShort(myConn) %></p>

<!-- **************     Exception Lists     **************** -->
<h4>Exception Lists</h4>

<% //System.out.println("home.jsp-Got Here 2"); %>
<!-- **************    Over Used Software      **************** -->
    <% SWProduct [] swpOverUsed = SWProduct.getItemList(myConn, false, true); %>
    <% tempText = "<b>Software Products Exceeding Authorized Count</b> (" + swpOverUsed.length + ")"; %>
    <rdc:doSection name="ouslist" title="<%= tempText %>" isOpen="false" >
        <table border=0>
        <tr><td valign=bottom><b>ID</b></td><td valign=bottom><b>Name</b></td><td valign=bottom><b>Version</b></td>
        <td valign=bottom align=center><b>License<br>Count</b></td><td valign=bottom align=center><b>License<br>Used</b></td>
        <td valign=bottom align=center><b>License<br>Available</b></td>
        </tr>
        <%
        for (int i=0; i < swpOverUsed.length; i++) {
            
            %>
            <tr bgcolor=<%= bgcolors[i%2] %>>
            <td valign=top><a href="javascript:doView(<%= swpOverUsed[i].getID() %>)"><%= swpOverUsed[i].getID() %></a>
            <rdc:hasPermission permission="12-mansw" > <a href="javascript:doEdit(<%= swpOverUsed[i].getID() %>)"><font size=-3><i>(edit)</i></font></a></rdc:hasPermission></td>
            <td valign=top><a href="javascript:doView(<%= swpOverUsed[i].getID() %>)"><%= swpOverUsed[i].getName() %></a></td>
            <td valign=top><%= ((swpOverUsed[i].getVersion() != null) ? swpOverUsed[i].getVersion() : "&nbsp;") %></td>
            <td valign=top align=center><%= ((swpOverUsed[i].isUnlimited()) ? "Unlimited" : swpOverUsed[i].getTotalCount()) %></td>
            <td valign=top align=center><%= swpOverUsed[i].getUsedCount() %></td>
            <td valign=top align=center><%= ((swpOverUsed[i].isUnlimited()) ? "Unlimited" : swpOverUsed[i].getAvailCount()) %></td>
            </tr>
            <%
        }
        %>
        </table>
    </rdc:doSection >

<% //System.out.println("home.jsp-Got Here 3"); %>
<!-- **************    Installed applications not matched to a software product      **************** -->
    <% //AppInventory [] iaNoMatch = AppInventory.getItemList(myConn, 0, null, true, null, true); %>
    <% //AppInventory [] iaNoMatch = AppInventory.getItemList(myConn, 0, "No Such record", true, null, true); %>
    <% String [] iaNoMatch = AppInventory.getAppNameList(myConn,true, true); %>
<% //System.out.println("home.jsp-Got Here 3.1"); %>
    <% tempText = "<b>Installed software applications not matched to a software product</b> (" + iaNoMatch.length + ")"; %>
    <rdc:doSection name="ianmsplist" title="<%= tempText %>" isOpen="false" >
        <table border=0>
        <tr><td valign=bottom><b>Name</b></td>
        </tr>

        <%
        for (int i=0; i < iaNoMatch.length; i++) {
    
            %>
<% //System.out.println("home.jsp-Got Here 3.2, " + iaNoMatch[i].getListName()); %>
            <tr bgcolor=<%= bgcolors[i%2] %>>
            <td valign=top><a href="javascript:doViewApp('<%= iaNoMatch[i] %>')"><%= iaNoMatch[i] %></a></td>
            </tr>
            <%
        }
        %>
        </table>
    </rdc:doSection >

<% //System.out.println("home.jsp-Got Here 4"); %>
<!-- **************     Installed services not matched to a software product     **************** -->
    <% //ServiceInventory [] isNoMatch = ServiceInventory.getItemList(myConn, 0, null, true, null, true); %>
    <% //ServiceInventory [] isNoMatch = ServiceInventory.getItemList(myConn, 0, "No Such record", true, null, true); %>
    <% String [] isNoMatch = ServiceInventory.getServiceNameList(myConn, true, true); %>
    <% tempText = "<b>Installed software services not matched to a software product</b> (" + isNoMatch.length + ")"; %>
    <rdc:doSection name="isnmsplist" title="<%= tempText %>" isOpen="false" >
        <table border=0>
        <tr><td valign=bottom><b>Name</b></td></td>
        </tr>
        <%
        for (int i=0; i < isNoMatch.length; i++) {
    
            %>
            <tr bgcolor=<%= bgcolors[i%2] %>>
            <td valign=top><a href="javascript:doViewServ('<%= isNoMatch[i] %>')"><%= isNoMatch[i] %></a></td>
            </tr>
            <%
        }
        %>
        </table>
    </rdc:doSection >
        <script language=javascript><!--

            function doViewApp(id) {
                document.main.id.value = id;
                submitForm("swInvAppDisplay.jsp","")
            }

            function doViewServ(id) {
                document.main.id.value = id;
                submitForm("swInvServDisplay.jsp","")
            }
        //-->
        </script>
<% //System.out.println("home.jsp-Got Here 5"); %>
<!-- **************     Software Products with no matching installed applications or services     **************** -->
    <% SWProduct [] swpNoMatch = SWProduct.getItemList(myConn, true); %>
    <% tempText = "<b>Software Products with no matching installed software applications or services</b> (" + swpNoMatch.length + ")"; %>
    <rdc:doSection name="spwnmilist" title="<%= tempText %>" isOpen="false" >
        <table border=0>
        <tr><td valign=bottom><b>ID</b></td><td valign=bottom><b>Name</b></td><td valign=bottom><b>Version</b></td>
        <td valign=bottom align=center><b>License<br>Count</b></td><td valign=bottom align=center><b>License<br>Used</b></td>
        <td valign=bottom align=center><b>License<br>Available</b></td>
        </tr>
        <%
        for (int i=0; i < swpNoMatch.length; i++) {
            
            %>
            <tr bgcolor=<%= bgcolors[i%2] %>>
            <td valign=top><a href="javascript:doView(<%= swpNoMatch[i].getID() %>)"><%= swpNoMatch[i].getID() %></a>
            <rdc:hasPermission permission="12-mansw" > <a href="javascript:doEdit(<%= swpNoMatch[i].getID() %>)"><font size=-3><i>(edit)</i></font></a></rdc:hasPermission></td>
            <td valign=top><a href="javascript:doView(<%= swpNoMatch[i].getID() %>)"><%= swpNoMatch[i].getName() %></a></td>
            <td valign=top><%= ((swpNoMatch[i].getVersion() != null) ? swpNoMatch[i].getVersion() : "&nbsp;") %></td>
            <td valign=top align=center><%= ((swpNoMatch[i].isUnlimited()) ? "Unlimited" : swpNoMatch[i].getTotalCount()) %></td>
            <td valign=top align=center><%= swpNoMatch[i].getUsedCount() %></td>
            <td valign=top align=center><%= ((swpNoMatch[i].isUnlimited()) ? "Unlimited" : swpNoMatch[i].getAvailCount()) %></td>
            </tr>
            <%
        }
        %>
        </table>

    </rdc:doSection >
        <script language=javascript><!--

            function doView(id) {
                document.main.id.value = id;
                submitForm("swProductForm.jsp","")
            }

            function doEdit(id) {
                document.main.id.value = id;
                submitForm("swProductForm.jsp","update")
            }
        //-->
        </script>





<%
myConn.release();
%>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
