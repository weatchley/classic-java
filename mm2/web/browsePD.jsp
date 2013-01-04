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
java.util.Date dNow = new java.util.Date();
int curFY = Integer.parseInt(Utils.dateToString(dNow, "yyyy"));
String [] statusPD = PDStatus.getStatusArray(myConn);

String fyS = ((request.getParameter("viewfy") != null && !request.getParameter("viewfy").equals("")) ? (String) request.getParameter("viewfy") : curFY + "");
int fy = Integer.parseInt(fyS);
String siteIDS = ((request.getParameter("siteid") != null && !request.getParameter("siteid").equals("")) ? (String) request.getParameter("siteid") : "0");
int siteID = Integer.parseInt(siteIDS);

%>


<h3 align=center>Browse Purchase Documents</h3>

<table align=center border=0><tr>
<td align=center>Fiscal&nbsp;Year:&nbsp;&nbsp;&nbsp;<br><select name=viewfy size=1>
<option value=0<%=  ((0 == fy) ? " selected" : "") %>>All</option>
<%
    for (int i=1999; i<=curFY; i++) {
        %>
        <option value=<%= i %><%= ((i == fy) ? " selected" : "") %>><%= i %></option>
        <%
    }
%>
</select>&nbsp;&nbsp;&nbsp;&nbsp;</td>
<% 
SiteInfo [] siteList = SiteInfo.getItemList(myConn);
%>
<td align=center>Site:<br><select name=siteid size=1><option value='0'>All</option>
<%
    for (int i=0; i<siteList.length; i++) {
        %>
        <option value=<%= siteList[i].getID() %><%= ((siteList[i].getID() == siteID) ? " selected" : "") %>><%= siteList[i].getName() %></option>
        <%
    }
%>
</select></td></tr>
<tr><td align=center valign=bottom colspan=2><input type=button name=refresh value='Refresh' onClick=submitForm('browsePD.jsp','view')></td>
</tr>
</table>
<% 
PurchaseDocument [] pdList = PurchaseDocument.getItemList(myConn, fy, siteID);
%>

<table align=center cellpadding=1 cellspacing=0 border=1>
<tr bgcolor=#a0e0c0>
<td><b>PR Number/<br>Ref Number</b></td><td><b>PO Number</b></td><td><b>Vendor</b></td><td><b>Requester/<br>Buyer</b></td><td><b>Status</b></td><td><b>Description</b></td>
</tr>

<%
for (int i=0; i< pdList.length; i++) {
%>
    <tr bgcolor=#ffffff><td valign=top><a href=javascript:doView('<%= pdList[i].getPrnumber() %>')><%= pdList[i].getPrnumber() %></a><br><%= ((pdList[i].getRefnumber() != null) ? pdList[i].getRefnumber() : "") %></td>
    <td valign=top><a href=javascript:doView('<%= pdList[i].getPrnumber() %>')><%= ((pdList[i].getPonumber() != null) ? pdList[i].getPonumber() : "&nbsp;") %><%= ((pdList[i].getAmendment() != null && !pdList[i].getAmendment().equals(" ")) ? pdList[i].getAmendment() : "") %></a><br>
    <%= ((pdList[i].getPoDate() != null) ? Utils.dateToString(pdList[i].getPoDate()) : "") %></td>
    <td valign=top><%= ((pdList[i].getVendor() != null) ? ((pdList[i].getVendor().getName() != null) ? pdList[i].getVendor().getName() : "&nbsp;") : "&nbsp;") %></td>
    <% 
        User requester = new User(pdList[i].getRequester(), myConn);
        User buyer = new User(pdList[i].getBuyer(), myConn);
    %>

    <td valign=top><%= ((requester.getID() != 0) ? requester.getFullName() : "Unknown") %><br><%= ((buyer.getID() != 0) ? buyer.getFullName() : "") %></td>
    <td valign=top><%= statusPD[pdList[i].getStatus()] %></td>
    <td valign=top><%= pdList[i].getBriefDescription() %> </td>
    </tr>
    <%
}
%>
</table>

        <script language=javascript><!--

            function doView(id) {
                document.main.id.value = id;
                submitForm("pdForm.jsp","view")
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
