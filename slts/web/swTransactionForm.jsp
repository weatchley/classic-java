<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
  <link rel=stylesheet href="/common/javascript/xc2/css/xc2_default.css" type="text/css">
  <script language="javascript" src="/common/javascript/xc2/config/xc2_default.js"></script>
  <script language="javascript" src="/common/javascript/xc2/script/xc2_inpage.js"></script>
</HEAD>

<BODY leftmargin=0 topmargin=0>
<% useFileUpload = true; %>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");
usr = new Person((String) session.getAttribute("user.name"));
DbConn myConn = new DbConn("csi");

String prodIDs = ((request.getParameter("id")  != null) ? (String) request.getParameter("id") : "0");
int prodID = Integer.parseInt(prodIDs);
SWProduct prod = new SWProduct(prodID,myConn);
String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
String transIDs = ((request.getParameter("transid")  != null) ? (String) request.getParameter("transid") : "0");
int transID = Integer.parseInt(transIDs);
SWTransactions  trans = null;
if (command.equals("new") || transID == 0) {
    trans = new SWTransactions();
} else {
    trans = new SWTransactions(prodID, transID, myConn);
}
boolean editForm = (((command.equals("new") || command.equals("edit")) && pos.belongsTo(((Long) perMap.get("12-mansw")).longValue())) ? true : false);

Comments [] comms = ((transID != 0) ? Comments.getItemList(myConn, prodID, transID) : null);
String [] aType = Attachment.getTypeList(myConn);

String aTypeOptionList = "";

for (int i=1; i<aType.length; i++) {
    aTypeOptionList += "<option value=" + i + ">" + aType[i] + "</option>";
}

String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
int i=0;
String tempText = "";

%>
<% //System.out.println("swProduct.jsp-Got Here 1-" + Utils.genDateID()); %>
  
<input type=hidden name=prodid value=<%= prodID %>>
<input type=hidden name=transid value=<%= transID %>>

  <h4>Software Transaction <%= ((command.equals("edit")) ? "Edit" : "Display") %></h4><br>

<table border=0>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Product ID:</td><td><%= prod.getID() %></td></tr>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Product Name:</td><td><%= prod.getName() %></td></tr>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Product Version:</td><td><%= ((prod.getVersion() != null) ? prod.getVersion() : " &nbsp;") %></td></tr>
<tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Product Manufacturer:</td><td><%= ((prod.getManufacturer() != null) ? prod.getManufacturer() : " &nbsp;") %></td></tr>

<tr bgcolor=<%= bgcolors[(i++)%2] %>><td colspan=2><hr width=75%></td></tr>
<% if (editForm) { %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td>Transaction ID:</td><td><%= ((trans.getID() > 0) ? trans.getID() : " &nbsp; ") %></td></tr>

    <% tempText = ((trans.getTag() != null) ? trans.getTag() : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td>Tag:</td><td><input type=text name=tag value="<%= ((command.equals("edit")) ? tempText : "") %>" size=20 maxlength=20></td></tr>

    <% tempText = ((trans.getSerialNumber() != null) ? trans.getSerialNumber() : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td>Serial Number:</td><td><input type=text name=serialnumber value="<%= ((command.equals("edit")) ? tempText : "") %>" size=60 maxlength=200></td></tr>

    <% tempText = ((trans.getPurchaseOrder() != null) ? trans.getPurchaseOrder() : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td>Purchase Order:</td><td><input type=text name=purchaseorder value="<%= ((command.equals("edit")) ? tempText : "") %>" size=20 maxlength=20></td></tr>

    <% tempText = ((!command.equals("new")) ? Utils.dateToString(trans.getDateAdded()) : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td>Date Added:</td><td><%= ((command.equals("edit")) ? tempText : "") %></td></tr>

    <% tempText = ((!command.equals("new")) ? Utils.dateToString(trans.getDateModified()) : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td>Date Modified:</td><td><%= ((command.equals("edit")) ? tempText : "") %></td></tr>

    <% tempText = ((trans.getDateVerified() != null) ? Utils.dateToString(trans.getDateVerified()) : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td>Date Verified:</td><td id='dvholder'><input type=text name=dateverified value="<%= ((command.equals("edit")) ? tempText : "") %>" size=20 maxlength=20 onfocus="showCalendar('', this, this, '', 'dvholder', 0,30, 1)"></td></tr>

    <% tempText = ((trans.getDateReceived() != null) ? Utils.dateToString(trans.getDateReceived()) : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td>Date Received:</td><td id='drholder'><input type=text name=datereceived value="<%= ((command.equals("edit")) ? tempText : "") %>" size=20 maxlength=20 onfocus="showCalendar('', this, this, '', 'drholder', 0,30, 1)"></td></tr>

    <% tempText = ((trans.getDateExpires() != null) ? Utils.dateToString(trans.getDateExpires()) : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td>Date Expires:</td><td id='deholder'><input type=text name=dateexpires value="<%= ((command.equals("edit")) ? tempText : "") %>" size=20 maxlength=20 onfocus="showCalendar('', this, this, '', 'deholder', 0,30, 1)"></td></tr>

    <% tempText = ((trans.getLocation() != null) ? trans.getLocation() : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td>Location:</td><td><input type=text name=location value="<%= ((command.equals("edit")) ? tempText : "") %>" size=20 maxlength=20></td></tr>

    <% tempText = ((trans.getLicenseType() != null) ? trans.getLicenseType() : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td>License Type:</td><td><input type=text name=licensetype value="<%= ((command.equals("edit")) ? tempText : "") %>" size=25 maxlength=25></td></tr>

    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td>License Count:</td><td><input type=text name=licensecount value="<%= ((command.equals("edit")) ? trans.getLicenseCount() : "") %>" size=8 maxlength=8></td></tr>

    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <% String [] swTransTypeList = SWTransactions.getTypeList(myConn); %>
    <td>Transaction Type:</td><td><select name=transactiontype size=1>
    <% for (i=0; i<swTransTypeList.length; i++) { %>
        <option value="<%= swTransTypeList[i] %>"<%= ((swTransTypeList[i].equals(trans.getTransactionType())) ? " selected" : "" ) %>><%= swTransTypeList[i] %></option>
    <% } %>
    </select></td></tr>

    <% tempText = ((trans.getRelatedTransaction() != 0) ? Integer.toString(trans.getRelatedTransaction()) : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td>Related Transaction:</td><td><input type=text name=relatedtransaction value="<%= ((command.equals("edit")) ? tempText : "") %>" size=8 maxlength=8></td></tr>

    <% tempText = ((trans.getDocumentation() != null) ? trans.getDocumentation() : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>>
    <td valign=top>Documentation:</td><td><textarea name=documentation cols=60 rows=4><%= ((command.equals("edit")) ? tempText : "") %></textarea></td></tr>

    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td valign=top>New Comments:</td><td><textarea name=comments rows=4 cols=80></textarea></td></tr>

    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td valign=top>New Attachments:</td><td>
    <input type="file" name="attachment1"> &nbsp; Type: <select name=attachmenttype1><%= aTypeOptionList %></select> &nbsp; Description: <input type=text name=attachmentdesc1 size=50 maxLength=50><br><div id="attachments"></div>
    <input type=hidden name=attachcount value=1>
    <a href="javascript:addNewAttachmentWidget();">Create New Attachment</a>
    <script language="javascript">
    <!--
        function addNewAttachmentWidget() {
            //** only works in IE, need to fix
            var count = document.main.attachcount.value;
            count++;
            document.main.attachcount.value = count;
            var insertText = '<input type=file name=attachment' + count + '> &nbsp; Type: <select name=attachmenttype' + count + '><%= aTypeOptionList %></select> &nbsp; Description: <input type=text name=attachmentdesc' + count + ' size=50 maxLength=50><br>';
            document.all.attachments.insertAdjacentHTML("BeforeBegin", "" + insertText + "");
        }
    -->
    </script>
    </td></tr>
    
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td colspan=2 align=center><input type=button name=submitform value="Submit" onclick="doFormSubmit();"</td></tr>
    <script language=javascript><!--

        function doFormSubmit() {
            // javascript form verification routine
            var msg = "";
            if (isblank(<%= formName %>.licensecount.value) || !isNumber(<%= formName %>.licensecount.value)) {
                msg += "License Count is required and must be a whole number grater than or equal to '0'.\n";
            }
            if (isblank(<%= formName %>.datereceived.value)) {
                msg += "Date Received must be entered.\n";
            }
            if (!isblank(<%= formName %>.relatedtransaction.value) && !isnumeric(<%= formName %>.relatedtransaction.value)) {
                msg += "Related Transaction must be a whole number grater than or equal to '0'.\n";
            }
            if (msg != "") {
                alert (msg);
            } else {
                //<%= formName %>.id.value = <%= prodID %>;
                //alert('Not yet implemented');
                submitFormResults("doSWTransaction","<%= ((command.equals("edit")) ? "update" : command) %>");
            }
        }

        // function that returns true if a string contains only numbers with an optional sign
        function isNumber(s) {
            if (s.length == 0) return false;
            var p=0;
            var m=0;
            for(var i = 0; i < s.length; i++) {
                var c = s.charAt(i);
                if (c == '+') p++;
                if (c == '-') m++;
                if (c == '-' && i > 0) m++;
                if (((c != '.') && (c != '-') && ((c < '0') || (c > '9'))) ||  p > 1 || m > 1 || (p+m) > 1) return false;
            }

            return true;
        }
        
    //-->
    </script>
<% } else { %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Transaction ID:</td><td><%= trans.getID() %></td></tr>

    <% tempText = ((trans.getTag() != null) ? trans.getTag() : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Tag:</td><td><%= tempText %></td></tr>

    <% tempText = ((trans.getSerialNumber() != null) ? trans.getSerialNumber() : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Serial Number:</td><td><%=  tempText %></td></tr>

    <% tempText = ((trans.getPurchaseOrder() != null) ? "<a href=\"javascript:viewPO('" + trans.getPurchaseOrder() + "')\">" + trans.getPurchaseOrder() + "</a>" : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Purchase Order:</td><td><%= tempText %></td></tr>

    <% tempText = ((!command.equals("new")) ? Utils.dateToString(trans.getDateAdded()) : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Date Added:</td><td><%= tempText %></td></tr>

    <% tempText = ((!command.equals("new")) ? Utils.dateToString(trans.getDateModified()) : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Date Modified:</td><td><%= tempText %></td></tr>

    <% tempText = ((trans.getDateVerified() != null) ? Utils.dateToString(trans.getDateVerified()) : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Date Verified:</td><td><%= tempText %></td></tr>

    <% tempText = ((trans.getDateReceived() != null) ? Utils.dateToString(trans.getDateReceived()) : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Date Received:</td><td><%= tempText %></td></tr>

    <% tempText = ((trans.getDateExpires() != null) ? Utils.dateToString(trans.getDateExpires()) : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Date Expires:</td><td><%= tempText %></td></tr>

    <% tempText = ((trans.getLocation() != null) ? trans.getLocation() : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Location:</td><td><%= tempText %></td></tr>

    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>License Type:</td><td><%= ((trans.getLicenseType() != null) ? trans.getLicenseType() : "") %></td></tr>

    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>License Count:</td><td><%= trans.getLicenseCount() %></td></tr>

    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Transaction Type:</td><td><%= trans.getTransactionType() %></td></tr>

    <% tempText = ((trans.getRelatedTransaction() != 0) ? Integer.toString(trans.getRelatedTransaction()) : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Related Transaction:</td><td><%= tempText %></td></tr>

    <% tempText = ((trans.getDocumentation() != null) ? trans.getDocumentation() : ""); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td valign=top>Documentation:</td><td><%= tempText.replace("\n", "<br>") %></td></tr>
<% } %>

</table>


<% if (!(command.equals("new") || prodID == 0)) { %>
    <% tempText = "<b>Comments</b> (" + ((comms != null) ? comms.length : "") + ")"; %>
    <rdc:doSection name="commentlist" title="<%= tempText %>" isOpen="false" >
    <table border=0>
    <tr><td valign=bottom><b>ID</b></td><td valign=bottom><b>Date Entered</b></td><td valign=bottom><b>Entered By</b></td>

    </tr>

    <%
    for (i=0; i < comms.length; i++) {
    
        %>
        <tr bgcolor=<%= bgcolors[(i+1)%2] %>>
        <td valign=top><%= comms[i].getID() %></td>
        <td valign=top><%= Utils.dateToString(comms[i].getDateEntered(), "MM/dd/yyyy hh:mm:ss a") %></td>
        <% Person per = new Person(); per.lookup(myConn, comms[i].getEnteredBy()); %>
        <% tempText = ((comms[i].getEnteredBy() != 0) ? per.getLastName() + ", " + per.getFirstName() : "unknown"); %>
        <td valign=top><%= tempText %></td>
        </tr>
        <tr bgcolor=<%= bgcolors[(i+1)%2] %>>
        <td valign=top></td>
        <% tempText = "comment-id-" + i; %>
        <td valign=top colspan=2><rdc:colapseString name="<%= tempText %>" maxLen="200" isOpen="false" ><%= comms[i].getText().replace("\n", "<br>") %></rdc:colapseString ></td>
        </tr>
        <%
    }
    %>
    </table>
    </rdc:doSection >

    <%
    Attachment [] att = Attachment.getItemList(myConn, prodID, 0);
    %>
    <% tempText = "<b>Attachments</b> (" + att.length + ")"; %>
    <rdc:doSection name="attachmentlist" title="<%= tempText %>" isOpen="false" >
    <table border=0>
    <tr><td valign=bottom><b>ID</b></td><td valign=bottom><b>Transaction</b></td><td valign=bottom><b>Name</b></td><td valign=bottom><b>Type</b></td><td valign=bottom><b>Description</b></td>

    </tr>

    <%
    
    for (i=0; i < att.length; i++) {
    
        %>
        <tr bgcolor=<%= bgcolors[(i+1)%2] %>>
        <td valign=top><%= att[i].getID() %></td>
        <td valign=top><%= att[i].getTransactionID() %></td>
        <td valign=top><a href="javascript:doViewAttachment(<%= att[i].getID() %>)"><%= att[i].getName() %></a></td>
        <td valign=top><%= att[i].getTypeDescription() %></td>
        <td valign=top><%= att[i].getDescription() %></td>
        </tr>
        <%
    }
    %>
    </table>
    </rdc:doSection >

    <script language=javascript><!--


        function doViewAttachment(id) {
            document.main.id.value = id;
            submitFormNewWindow("viewAttachment.jsp","view")
        }


        function viewPO(id) {
            document.main.id.value = id;
            submitFormNewWindow("/mm/pdForm.jsp?command=viewpo&id="+id,"viewpo")
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
