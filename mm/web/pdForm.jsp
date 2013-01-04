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
String tempText2 = null;
String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
java.util.Date dNow = new java.util.Date();
int curFY = Integer.parseInt(Utils.dateToString(dNow, "yyyy"));
String [] statusPD = PDStatus.getStatusArray(myConn);

String command = ((request.getParameter("command") != null && !request.getParameter("command").equals("")) ? (String) request.getParameter("command") : "view");
String id = ((request.getParameter("id") != null && !request.getParameter("id").equals("")) ? (String) request.getParameter("id") : "n/a");


String spaces20 = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

PurchaseDocument pd = null;

//System.out.println("pdForm.jsp - Got Here 1 - ID: " + id + ", command: " + command);
//System.out.println("pdForm.jsp - Got Here 1.5 - ID: " + request.getParameter("id") + ", command: " + request.getParameter("command"));
if (command.equals("viewpo")) {
    pd = new PurchaseDocument(PurchaseDocument.lookupPRFromPO(id, myConn), myConn);
} else if (command.equals("view")) {
    pd = new PurchaseDocument(id, myConn);
}

if (pd == null || pd.getPrnumber() == null) {
%>
        <script language=javascript><!--
            alert('Purchase Document <%= id %> Not Found');
            document.location='browsePD.jsp';
        //-->
        </script>
<%
} else {
%>


    <h3 align=center>Browse Purchase Documents</h3>

    <table align=center border=0>
<!-- ### Top       #########  -->
    <tr><td vailign=top><b>PR #:</b></td><td><%= pd.getPrnumber() %></td></tr>
    <tr><td vailign=top><b>PO #:</b></td><td><%= ((pd.getPonumber() != null) ? pd.getPonumber() : "") %><%= ((pd.getAmendment() != null) ? pd.getAmendment() : "") %></td></tr>
    <tr><td vailign=top><b>Reference #:</b></td><td><%= ((pd.getRefnumber() != null) ? pd.getRefnumber() : "") %></td></tr>
    <tr><td vailign=top><b>Status:</b></td><td><%= statusPD[pd.getStatus()] %></td></tr>
    <% User author = new User(pd.getAuthor(), myConn); %>
    <tr><td vailign=top><b>Author:</b></td><td><%= ((author.getID() != 0) ? author.getFullName() : "") %></td></tr>
<% //System.out.println("pdForm.jsp - Got Here 2"); %>
<!-- ###  Requester      #########  -->
    <% User requester = new User(pd.getRequester(), myConn); %>
    <tr><td vailign=top><b>Requester:</b></td><td><%= ((requester.getID() != 0) ? requester.getFullName() : "") %></td></tr>
<!-- ### buyer       #########  -->
    <% User buyer = new User(pd.getBuyer(), myConn); %>
    <tr><td vailign=top><b>Buyer:</b></td><td><%= ((buyer.getID() != 0) ? buyer.getFullName() : "") %></td></tr>
<!-- ### Department       #########  -->
    <% Department dept = new Department(pd.getDeptID(), myConn); %>
    <tr><td vailign=top><b>Department:</b></td><td><%= dept.getName() %></td></tr>
<!-- ### Charge Number       #########  -->
    <tr><td vailign=top><b>Charge Number:</b></td><td><%= pd.getChargeNumber() %></td></tr>
<!-- ### Brief Description       #########  -->
    <tr><td vailign=top><b>Brief Description:</b></td><td><%= pd.getBriefDescription() %></td></tr>
<!-- ### Vendor       #########  -->
    <tr><td vailign=top><b>Vendor:</b></td><td><%= ((pd.vendor != null) ? pd.vendor.getName() : "") %></td></tr>
<!-- ### PO Type       #########  -->
    <% String [] poTypes = {"", "Normal", "Blanket", "PO Maintenance/Partial Maintenance"}; %>
    <tr><td vailign=top><b>PO Type:</b></td><td><%= poTypes[pd.getPoType()] %></td></tr>
<!-- ### PO Date       #########  -->
    <tr><td vailign=top><b>PO Date:</b></td><td><%= ((pd.getPoDate() != null) ? Utils.dateToString(pd.getPoDate()) : "") %></td></tr>
<!-- ### Blanket Release       #########  -->
    <tr><td vailign=top><b>Blanket Release PO: &nbsp; &nbsp; </b></td><td><%= ((pd.getContractType() == 2) ? pd.getRelatedPRInfo().getPonumber() : "Not a blanket release") %></td></tr>
<!-- ### Prority       #########  -->
    <tr><td vailign=top><b>Prority: <%= ((pd.getPriority().equals("T")) ? "T" : "F") %></b></td>
        <td><b>Date Required:</b> <%= Utils.dateToString(pd.getDateRequired()) %> &nbsp; &nbsp; &nbsp; <b>Due/Delivery Date:</b> <%= ((pd.getDueDate() != null) ? Utils.dateToString(pd.getDueDate()) : "") %></td></tr>
    <!--tr><td vailign=top><b></b></td><td></td></tr-->
<!-- ### Start & End Dates       #########  -->
    <% tempText = "Start & End Dates (Blanket & Maintenance Contracts Only)"; %>
    <% tempText2 = "<b>Start Date:</b> " + ((pd.getStartDate() != null) ? Utils.dateToString(pd.getStartDate()) : "") + 
        " &nbsp; &nbsp; <b>End Date:</b> " + ((pd.getEndDate() != null) ? Utils.dateToString(pd.getEndDate()) : ""); %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec1" title="<%= tempText %>" isOpen="false" ><%= tempText2 %></rdc:doSection ></td></tr>
<!-- ### Approval List       #########  -->
    <% tempText = "Approval List (Blanket Contracts Only)-tbd"; %>
    <% tempText2 = "test"; %>
    <!--tr><td vailign=top colspan=2><rdc:doSection name="sec2" title="<%= tempText %>" isOpen="false" ><%= tempText2 %></rdc:doSection ></td></tr-->
<!-- ### Credit Card       #########  -->
    <% User ccHolder = new User(pd.getCreditCardHolder(), myConn); %>
    <% tempText = "Credit Card Purchases"; %>
    <% //tempText2 = "test"; %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec3" title="<%= tempText %>" isOpen="false" >
        <table border=0>
            <tr><td><b>Credit Card Holder: </b><%= ((ccHolder != null && ccHolder.getID() != 0) ? ccHolder.getFirstName() + " " + ccHolder.getLastName() : "None") %>
        </table>
    </rdc:doSection ></td></tr>
<!-- ### Questions       #########  -->
    <% tempText = "Oversite Routing Questions-tbd"; %>
    <% tempText2 = "test"; %>
    <!--tr><td vailign=top colspan=2><rdc:doSection name="sec4" title="<%= tempText %>" isOpen="false" ><%= tempText2 %></rdc:doSection ></td></tr-->
<!-- ### Justification       #########  -->
    <% tempText = "Justification"; %>
    <% 
        String just = pd.getJustification();
        tempText2 = just.replaceAll("\n","<br>"); 
    %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec5" title="<%= tempText %>" isOpen="false" ><%= tempText2 %></rdc:doSection ></td></tr>
<!-- ### Items       #########  -->
    <% tempText = "Item List (" + pd.items.length + ")"; %>
    <% //tempText2 = "test"; %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec6" title="<%= tempText %>" isOpen="false" >
        <table cellpadding=1 cellspacing=0 border=1 style="font-family: verdana; font-size: 7pt;">
	<tr bgcolor=#a0e0c0><td valign=bottom width=70><b><a href="javascript:alert('Item Number');" title='Item Number (must be unique)' style="text-decoration:none; color:#000099; cursor: help">#</a>/</b><br>
	<b><a href="javascript:alert('Part Number');" title='Part Number' style="text-decoration:none; color:#000099; cursor: help">Part #</a></b></td>
	<td valign=bottom width=130><b><a href="javascript:alert('Product Description');" title='Product Description' style="text-decoration:none; color:#000099; cursor: help">Description</a></b></td>
	<td valign=bottom width=40><b><a href="javascript:alert('Item Quantity');" title='Item Quantity' style="text-decoration:none; color:#000099; cursor: help">Qty</a></b></td>
        <% if (pd.getStatus() > 17) { %>
        	<td valign=bottom width=40><b><a href="javascript:alert('Received');" title='Received' style="text-decoration:none; color:#000099; cursor: help">Received</a></b></td>
        <% } %>
	<td valign=bottom width=60><b><a href="javascript:alert('Unit of Issue');" title='Unit of Issue' style="text-decoration:none; color:#000099; cursor: help">Unit</a></b></td>
	<td valign=bottom width=45><b><a href="javascript:alert('Price per unit');" title='Price per unit' style="text-decoration:none; color:#000099; cursor: help">Price</a></b></td>
	<td valign=bottom width=30><b><a href="javascript:alert('Substitution OK');" title='Substitution OK' style="text-decoration:none; color:#000099; cursor: help">Sub</a>/</b><br>
	<b><a href="javascript:alert('Hazardous Material (Powder Paste or Liquid)');" title='Hazardous Material (Powder paste or Liquid)' style="text-decoration:none; color:#000099; cursor: help">Haz</a></b></td>
	<td valign=bottom width=210><b><a href="javascript:alert('Element Code');" title='Element Code' style="text-decoration:none; color:#000099; cursor: help">EC</a>/</b><br>
	<b><a href="javascript:alert('Type of item to be purchased');" title='Type of item to be purchased' style="text-decoration:none; color:#000099; cursor: help">Type</a></b>
	 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<b><a href="javascript:alert('Technical Inspection Required');" title='Technical Inspection Required' style="text-decoration:none; color:#000099; cursor: help">Technical Inspection</a></b></td>
	<td valign=bottom width=60><b><a href="javascript:alert('Extended Price');" title='Extended Price' style="text-decoration:none; color:#000099; cursor: help">Ext</a></b></td>
	</tr>
	<% double subTotal = 0.0; %>
	<% String [] itemType = {"", "Tangible Goods", "Software", "Service"}; %>
        <% for (int i=0; i<pd.items.length; i++) { %>
              <tr bgcolor=#ffffff><td valign=top><%= pd.getItems()[i].getItemNumber() %><br><%= ((pd.getItems()[i].getPartNumber() != null) ? pd.getItems()[i].getPartNumber() : "") %></td>
              <% String itemDescr = pd.getItems()[i].getDescription().replaceAll("\n","<br>"); %>
              <td valign=top><%= itemDescr %></td>
              <td valign=top><%= pd.getItems()[i].getQuantity() %></td>
              <% if (pd.getStatus() > 17) { %>
                    <td valign=top><%= pd.getItems()[i].getQuantityReceived() %></td>
              <% } %>
              <td valign=top><%= pd.getItems()[i].getUnitOfIssue() %></td><td valign=top><%= PurchaseDocument.dollarFormat(pd.getItems()[i].getUnitPrice()) %></td>
              <td valign=top><%= pd.getItems()[i].getSubstituteOk() %><br><%= pd.getItems()[i].getIsHazmat() %></td><td valign=top><%= pd.getItems()[i].getEc() %><br>
              <%= itemType[pd.getItems()[i].getType()] %> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <%= pd.getItems()[i].getTechInspection() %></td>
              <% double extPrice = (pd.getItems()[i].getQuantity() * pd.getItems()[i].getUnitPrice()); %>
              <td valign=top><%= PurchaseDocument.dollarFormat(extPrice) %></td></tr>
              <% subTotal += extPrice; %>
              
        <% } %>
        
        </table>
        <table cellpadding=1 cellspacing=0 border=1 style=\"font-family: verdana; font-size: 7pt;\"><tr bgcolor=#a0e0c0><td style=\"font-family: verdana; font-size: 7pt;\" align=center width=100><font size=-2><b>Sub Total</b></font></td>
	<td style=\"font-family: verdana; font-size: 7pt;\" align=center width=100><font size=-2><b>Shipping</b></font></td>
	<td style=\"font-family: verdana; font-size: 7pt;\" align=center width=100><font size=-2><b>Tax</b></font></td>
	<td style=\"font-family: verdana; font-size: 7pt;\" align=center width=100><font size=-2><b>Total</b></font></td></tr><tr bgcolor=#ffffff>
	<td style=\"font-family: verdana; font-size: 7pt;\" align=center><font size=-2><div id=subTotal><%= PurchaseDocument.dollarFormat(subTotal) %></div></font></td>
	<td style=\"font-family: verdana; font-size: 7pt;\" align=center><font size=-2><div id=shipping><%= PurchaseDocument.dollarFormat(pd.getShipping()) %></div></td>
	<td style=\"font-family: verdana; font-size: 7pt;\" align=center><font size=-2><div id=tax><%= PurchaseDocument.dollarFormat(pd.getTax()) %></div></font></td>
	<td style=\"font-family: verdana; font-size: 7pt;\" align=center><font size=-2><div id=total><%= PurchaseDocument.dollarFormat((subTotal + pd.getShipping() + pd.getTax())) %></div></font></td>
	</tr></table>

    </rdc:doSection ></td></tr>
<!-- ### Sole Source       #########  -->
    <% tempText = "Sole Source"; %>
    <% 
        String soleSource = pd.getSoleSource();
        String soleSourceJust = pd.getSsJustification();
        soleSourceJust = ((soleSourceJust != null) ? soleSourceJust.replaceAll("\n","<br>") : ""); 
    %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec7" title="<%= tempText %>" isOpen="false" >
        <table border=0>
        <tr><td><b>Is Sole Source: </b><%= soleSource %></td></tr>
        <tr><td><b>Sole Source Justification: </b><br>
        <%= soleSourceJust %>
        </td></tr>
        </table>
    </rdc:doSection ></td></tr>
<!-- ### Vendor       #########  -->
    <% Vendor [] vendors = Vendor.getItemList(myConn, pd.getPrnumber()); %>
    <% tempText = "Vendor List (" + vendors.length + ")"; %>
    <%// tempText2 = "test"; %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec8" title="<%= tempText %>" isOpen="false" >
        <table border=0>
        <% for (int i=0; i<vendors.length; i++) { %>
            <tr><td><%= (vendors[i].getName() + " - " + vendors[i].getCity() + ", " + vendors[i].getState()) %></td></tr>
        <% } %>
        <% if (pd.getVendor() != null) { %>
            <tr><td>--------------</td></tr><tr><td>Winning Vendor: &nbsp; <%= ((pd.getVendor() != null) ? pd.getVendor().getName() : "") %>
            <br><%= spaces20 %>Point of Contact:  <%= pd.getVendor().getPointOfContact() %>
            <%= ((pd.getVendor().getPhone() != null) ? ("<br>" + spaces20 + "Phone: " + pd.getVendor().getPhone() + ((pd.getVendor().getExtension() != null) ? (" - " + pd.getVendor().getExtension()) : "")) : "") %>
            <%= ((pd.getVendor().getFax() != null) ? ("<br>" + spaces20 + "FAX: " + pd.getVendor().getFax()) : "") %>
            <%= ((pd.getVendor().getEmail() != null) ? ("<br>" + spaces20 + pd.getVendor().getEmail()) : "") %>
            </td></tr>
        <% } %>
        </table>
    </rdc:doSection ></td></tr>
<!-- ### RFP       #########  -->
    <% tempText = "RFP"; %>
    <% 
        String [] fobList = {"","Origin","Destination","N/A"};
        tempText2 = "<b>FOB:</b> " + ((pd.getFob() != 0) ? fobList[pd.getFob()] : "") + "<br>" +
            "Delivery Date " + ((pd.getRfpDeliveryDays()  != 0) ? pd.getRfpDeliveryDays() : "") + " Days<br>" +
            "Offer Valid For " + ((pd.getRfpDaysValid()  != 0) ? pd.getRfpDaysValid() : "") + " Days<br>" +
            "RFP Due On or Before " + ((pd.getRfpDueDate() != null) ? Utils.dateToString(pd.getRfpDueDate()) : "") + "<br>" +
            "<b>Ship Via: </b>" + ((pd.getShipVia() != null) ? pd.getShipVia() : "") + "<br>" +
            "<b>Terms: </b>" + ((pd.getPaymentTerms() != null) ? pd.getPaymentTerms() : "") + ""; 
    %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec9" title="<%= tempText %>" isOpen="false" ><%= tempText2 %></rdc:doSection ></td></tr>
<!-- ### ClauseList       #########  -->
    <% ClauseList [] cl = ClauseList.getItemList(myConn, pd.getPrnumber()); %>
    <% tempText = "Clauses (" + cl.length + ")"; %>
    <% //tempText2 = "test"; %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec10" title="<%= tempText %>" isOpen="false" >
        <table border=0>
        <% for (int i=0; i< cl.length; i++) { %>
              <tr><td><b>Precedence: </b><%= cl[i].getPrecedence() %><%= spaces20 %>
              <b>Type: </b><%= ((cl[i].getType().equals("H")) ? "Header" : "Footer") %><%= spaces20 %>
              <b>RFP: </b><%= ((cl[i].isRfp()) ? "T" : "F") %><%= spaces20 %><b>PO: </b><%= ((cl[i].isPo()) ? "T" : "F") %>
              </td></tr>
              <% String clause = cl[i].getClause(); %>
              <% clause = clause.replaceAll("RFPDaysValid", pd.getRfpDaysValid() + ""); %>
              <% clause = clause.replaceAll("RFPDueDate", Utils.dateToString(pd.getRfpDueDate()) + ""); %>
              <% clause = clause.replaceAll("RFPDeliverDays", pd.getRfpDeliveryDays() + ""); %>
              <% clause = clause.replaceAll("\n", "<br>\n"); %>
              <tr><td><%= clause %><br>&nbsp;</td></tr>
        <% } %>
        </table>
    </rdc:doSection ></td></tr>
<!-- ### Selection and Summary Memo       #########  -->
    <% tempText = "Selection and Summary Memo"; %>
    <% 
        String selectionMemo = pd.getSelectionMemo();
        tempText2 = ((selectionMemo != null) ? selectionMemo.replaceAll("\n","<br>") : "Not Available"); 
    %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec11" title="<%= tempText %>" isOpen="false" ><%= tempText2 %></rdc:doSection ></td></tr>
<!-- ### Enclosures       #########  -->
    <% tempText = "Enclosures"; %>
    <% 
        String enclosures = pd.getEnclosures();
        tempText2 = ((enclosures != null) ? enclosures.replaceAll("\n","<br>") : "Not Available"); 
    %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec12" title="<%= tempText %>" isOpen="false" ><%= tempText2 %></rdc:doSection ></td></tr>
<!-- ### ReceivingLog       #########  -->
    <% ReceivingLog [] rLog = ReceivingLog.getItemList(myConn, pd.getPrnumber()); %>
    <% tempText = "Receiving Log (" + rLog.length + ")"; %>
    <% //tempText2 = "test"; %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec13" title="<%= tempText %>" isOpen="false" >
        <table border=0>
        <% for (int i=0; i< rLog.length; i++) { %>
            <tr><td><b>Date Received: </b></td><td><%= Utils.dateToString(rLog[i].getDateReceived()) %></td></tr>
            <tr><td><b>Shipped Via: </b></td><td><%= rLog[i].getShipVia() %></td></tr>
            <tr><td><b>Shiment Number: </b></td><td><%= ((rLog[i].getShipmentNumber() != null) ? rLog[i].getShipmentNumber() : "") %></td></tr>
            <tr><td><b>Delivered To: </b></td><td><%= ((rLog[i].getDeliverdTo() != null) ? rLog[i].getDeliverdTo() : "") %></td></tr>
            <tr><td><b>Date Delivered: </b></td><td><%= ((rLog[i].getDateDelivered() != null) ? Utils.dateToString(rLog[i].getDateDelivered()) : "") %></td></tr>
            <% String rLogComments = rLog[i].getComments(); %>
            <% if (rLogComments != null) { %>
                <% rLogComments = rLogComments.replace("\n","<br>\n"); %>
                <% rLogComments = rLogComments.replace("  "," &nbsp;"); %>
            <% } else { %>
                <% rLogComments = ""; %>
            <% } %>
            <tr><td><b>Comments: </b></td><td><%= ((rLogComments != null) ? rLogComments : "")  %></td></tr>
            <tr><td colspan=2><table border=0 cellpadding=1 cellspacing=0><tr><td> &nbsp; &nbsp;</td><td><table cellpadding=1 cellspacing=0 border=1>
            <tr bgcolor=#a0e0c0><td align=center><b>Item Number</b></td><td align=center><b>Quantity Received</b></td><td align=center><b>Quality Code</b></td></tr>
            <% for (int j=0; j<rLog[i].items.length; j++) { %>
                <tr bgcolor=#ffffff><td align=center><%= rLog[i].items[j].getItemNumber() %></td>
                <td align=center><%= rLog[i].items[j].getQuantityReceived() %></td>
                <td align=center><%= ((rLog[i].items[j].getQualityCode() != null) ? rLog[i].items[j].getQualityCode() : "") %></td></tr>
            <% } %>
            </table></td></tr>
            </table>
            <hr width=75%>
                
        <% } %>
        </table>
    </rdc:doSection ></td></tr>
<!-- ### Charge Distribution List       #########  -->
    <% POChargeNumber [] pocn = POChargeNumber.getItemList(myConn, pd.getPrnumber()); %>
    <% tempText = "Charge Distribution List (" + pocn.length + ")"; %>
    <% //tempText2 = "test"; %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec14" title="<%= tempText %>" isOpen="false" >
        <table border=1 cellpadding=1 cellspacing=0 style="font-family: verdana; font-size: 7pt;">
	<tr bgcolor=#a0e0c0><td><b>Charge Number</b></td><td><b>EC</b></td><td align=center><b>Amount</b></td><td align=center><b>Invoiced</b></td><td align=center><b>Balance</b></td></tr>
        <% for (int i=0; i< pocn.length; i++) { %>
            <tr bgcolor=#ffffff><td><%= pocn[i].getCn() %></td><td><%= pocn[i].getEc() %></td><td><%= PurchaseDocument.dollarFormat(pocn[i].getAmount()) %></td>
            <td><%= PurchaseDocument.dollarFormat(pocn[i].getInvoiced()) %></td><td><%= PurchaseDocument.dollarFormat(pocn[i].getAmount() - pocn[i].getInvoiced()) %></td></tr>
        <% } %>
        </table>
    </rdc:doSection ></td></tr>
<!-- ### Accounts Payable       #########  -->
    <% Invoice [] inv = Invoice.getItemList(myConn, pd.getPrnumber()); %>
    <% String [] apStatus = {"", "Initial", "Approval Pending", "Approved", "Closed"}; %>
    <% tempText = "Accounts Payable (" + inv.length + ")"; %>
    <% //tempText2 = "test"; %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec15" title="<%= tempText %>" isOpen="false" >
        <table border=0>
        <% for (int i=0; i< inv.length; i++) { %>
            <tr><td><b>AP ID: </b></td><td><%= inv[i].getID() %></td></tr>
            <tr><td><b>Invoice Number: </b></td><td><%= inv[i].getInvoiceNumber() %></td></tr>
            <tr><td><b>Date Received: </b></td><td><%= Utils.dateToString(inv[i].getDateReceived()) %></td></tr>
            <tr><td><b>Invoice Date: </b></td><td><%= Utils.dateToString(inv[i].getInvoiceDate()) %></td></tr>
            <tr><td><b>Tax Paid: </b></td><td><%= ((inv[i].isTaxPaid()) ? "Yes" : "No") %></td></tr>
            <tr><td><b>Date Paid: </b></td><td><%= ((inv[i].getDatePaid() != null) ? Utils.dateToString(inv[i].getDatePaid()) : "") %></td></tr>
            <tr><td><b><%= SYSClient %> Billed: </b></td><td><%= ((inv[i].getClientBilled() != null) ? Utils.dateToString(inv[i].getClientBilled()) : "") %></td></tr>
            <tr><td><b>Status: </b></td><td><%= apStatus[inv[i].getStatus()] %></td></tr>
            <% String apComments = ((inv[i].getComments() != null) ? inv[i].getComments() : ""); %>
            <% apComments.replace("\n","<br>\n"); %>
            <% apComments.replace("  ", " &nbsp;"); %>
            <tr><td><b>Comments: </b></td><td><%= apComments %></td></tr>
            <tr><td colspan=2><table border=0 cellpadding=1 cellspacing=0><tr><td> &nbsp; &nbsp;</td><td><table cellpadding=1 cellspacing=0 border=1>
            <tr bgcolor=#a0e0c0><td align=center><b>Charge Number</b></td><td align=center><b>Element Code</b></td>
            <td align=center><b>Amount</b></td><td align=center><b>Tax</b></td><td align=center><b>Total</b></td></tr>
            <% for (int j=0; j<inv[i].items.length; j++) { %>
                <tr bgcolor=#ffffff><td align=center><%= inv[i].items[j].getChargeNumber() %></td>
                <td align=center><%= inv[i].items[j].getEc() %></td>
                <td align=center><%= PurchaseDocument.dollarFormat(inv[i].items[j].getAmount()) %></td>
                <td align=center><%= PurchaseDocument.dollarFormat(inv[i].items[j].getTax()) %></td>
                <% double invTotal = (inv[i].items[j].getTax() + inv[i].items[j].getAmount()); %>
                <td align=center><%= PurchaseDocument.dollarFormat(invTotal) %></td></tr>
            <% } %>
            </table></td></tr>
            </table>
            <hr width=75%>

        <% } %>
        </table>
    </rdc:doSection ></td></tr>
<!-- ### Attachment       #########  -->
    <% Attachment [] attach = Attachment.getItemList(myConn, pd.getPrnumber()); %>
    <% tempText = "Attachments (" + attach.length + ")"; %>
    <% //tempText2 = "test"; %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec16" title="<%= tempText %>" isOpen="false" >
        <table border=1 cellpadding=0 cellspacing=0>
        <tr bgcolor=#a0e0c0><td>PR</td><td>RFP</td><td>PO</td><td>Name</td></tr>
        <% for (int i=0; i<attach.length; i++) { %>
            <tr bgcolor=#ffffff><td><%= ((attach[i].isPr()) ? "T" : "F") %></td><td><%= ((attach[i].isRfp()) ? "T" : "F") %></td><td><%= ((attach[i].isPo()) ? "T" : "F") %></td>
            <td><a href=javascript:viewAttachment(<%= attach[i].getID() %>)><%= attach[i].getFileName() %></a></td><tr>
        <% } %>
        </table>
            <script language=javascript><!--
	    
	        function viewAttachment(id) {
	            document.main.id.value = id;
	            submitFormNewWindow("viewAttachment.jsp","view")
	        }
	    //-->
	    </script>

    </rdc:doSection ></td></tr>
<!-- ### Remarks       #########  -->
    <% Remarks [] rem = Remarks.getItemList(myConn, pd.getPrnumber()); %>
    <% tempText = "Remarks (" + rem.length + ")"; %>
    <% //tempText2 = "test"; %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec17" title="<%= tempText %>" isOpen="false" >
        <table border=1 cellspacing=0 cellpadding=0>
        <% for (int i=0; i<rem.length; i++) { %>
            <tr><td><%= rem[i].getDateEntered() %> - <%= (rem[i].getFirstName() + " " + rem[i].getLastName()) %><br>
            <% String remText = rem[i].getText().replaceAll("\n", "<br>\n"); %>
            <%= remText %></td></tr>
        <% } %>
        </table>
    </rdc:doSection ></td></tr>
<!-- ### Archive       #########  -->
    <% Archive [] arch = Archive.getItemList(myConn, pd.getPrnumber()); %>
    <% tempText = "History/Archive (" + arch.length + ")"; %>
    <% //tempText2 = "test"; %>
    <tr><td vailign=top colspan=2><rdc:doSection name="sec18" title="<%= tempText %>" isOpen="false" >
        <table border=0 cellspacing=0 cellpadding=0>
        <% for (int i=0; i<arch.length; i++) { %>
            <tr><td><a href="javascript:viewArchive('<%= arch[i].getPrnumber() %>', '<%= (Utils.dateToString(arch[i].getDateArchived(), "MM/dd/yyyy HH:mm:ss")) %>')"><%= arch[i].getDateArchived() %> - <%= arch[i].getDescription() %></a></td></tr>
        <% } %>
        </table>
            <script language=javascript><!--
	    
	        function viewArchive(pr, ldate) {
	            document.main.id.value = pr;
	            document.main.lookupdate.value = ldate;
	            submitFormNewWindow("viewArchive.jsp","view")
	        }
	    //-->
	    </script>
	    <input type=hidden name=lookupdate value=''>
    </rdc:doSection ></td></tr>
    
    
    </table>







    <%
}
myConn.release();
%>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
