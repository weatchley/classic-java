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
String [] statusPD = PDStatus.getStatusArray(myConn);

String pdSearchList = "";
HashSet hs = new HashSet();

String searchText = ((request.getParameter("searchtext") != null && !request.getParameter("searchtext").equals("")) ? (String) request.getParameter("searchtext") : "");

%>


<h3 align=center>Search Purchase Documents</h3>

<table align=center cellpadding=1 cellspacing=0 border=0 width=610>
<tr><td align=center>Search&nbsp;Text:&nbsp;<input type=text name=searchtext size=100 maxlength=200 value=""></td></tr>
<tr><td><table align=center cellpadding=1 cellspacing=0 border=0 width=95%><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>
Searches: Brief Description, Justification, Item Description, Part Number, Remarks, Sole Source Justification, Selection Memo, Vendor List, and Enclosures.
</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>
</table></td></tr>
<tr><td align=center>
<!--input type=button name=submitbutton value="Search" onClick="submitForm('search.jsp', '')"-->
<input type=submit name=submitbutton value="Search">
</td></tr>
</table>
<script language=javascript><!--
       document.main.target = '_self';
       document.main.action = 'search.jsp';
//-->
</script>


<% if (!searchText.equals("")) { %>
    
    <% 
    PurchaseDocument [] pdList = PurchaseDocument.getItemList(myConn, 0, 0, null, searchText);
    for (int i=0; i<pdList.length; i++) {
        hs.add(pdList[i].getPrnumber());
    }
    PDItem [] itemList = PDItem.getItemList(myConn, null, false, null, searchText);
    for (int i=0; i<itemList.length; i++) {
        hs.add(itemList[i].getPrnumber());
    }
    Remarks [] remList = Remarks.getItemList(myConn, null, searchText);
    for (int i=0; i<remList.length; i++) {
        hs.add(remList[i].getPrnumber());
    }
    int j = 0;
    for (Iterator i = hs.iterator(); i.hasNext();) {
        String tmp = (String) i.next();
        pdSearchList += ((j > 0) ? ", " : "") + "'" + tmp + "'";
        j++;
    }
    pdList = PurchaseDocument.getItemList(myConn, 0, 0, pdSearchList, null);
    
    %>
    <p align=center>Search Results: Found <%= ((pdList.length != 1) ? pdList.length + " Items" : pdList.length + " Item") %> For<br><%= searchText %></p>

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

<% } %>





<%
myConn.release();
%>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
