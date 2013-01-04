<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
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
String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
//SWProduct prod = new SWProduct(prodID,myConn);
SWProduct prod = null;
if (command.equals("new") || prodID == 0) {
    prod = new SWProduct();
} else {
    prod = new SWProduct(prodID, myConn);
}
boolean editForm = (((command.equals("new") || command.equals("update")) && pos.belongsTo(((Long) perMap.get("12-mansw")).longValue())) ? true : false);

SWTransactions [] swTrans = SWTransactions.getItemList(myConn, prodID);
Computer [] compList = ProdAppInvMatch.getComputerList(myConn, prodID);
Computer [] compList2 = ProdServInvMatch.getComputerList(myConn, prodID);
Comments [] comms = Comments.getItemList(myConn, prodID, -1);
String [] aType = Attachment.getTypeList(myConn);

String aTypeOptionList = "";

for (int i=1; i<aType.length; i++) {
    aTypeOptionList += "<option value=" + i + ">" + aType[i] + "</option>";
}

String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
int i=0;
String tempText = "";

%>
<% //System.out.println("swProduct.jsp-Got Here 1"); %>
  

  <h4>Software Product <%= ((editForm) ? "Entry/Update Form" : "Display") %></h4><br>

<table border=0>
<% if (editForm) { %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>ID:</td><td><%= ((prodID != 0) ? prodID : "&nbsp;") %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Name:</td><td><input type=text name=name size=50 maxLength=50 value='<%= ((prodID != 0) ? prod.getName() : "") %>'></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Version:</td><td><input type=text name=version size=20 maxLength=20 value='<%= ((prod.getVersion() != null) ? prod.getVersion() : "") %>'></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Manufacturer:</td><td><input type=text name=manufacturer size=50 maxLength=50 value='<%= ((prod.getManufacturer() != null && !prod.getManufacturer().equals("null")) ? prod.getManufacturer() : "") %>'></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Platform:</td><td><input type=text name=platform size=50 maxLength=50 value='<%= ((prod.getPlatform() != null) ? prod.getPlatform() : "") %>'></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Responsible Organization:</td><td><input type=text name=resporg size=50 maxLength=50 value='<%= ((prod.getRespOrg() != null) ? prod.getRespOrg() : "") %>'></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Unlimited:</td><td><input type=checkbox name=unlimited value='T' <%= ((prod.isUnlimited()) ? "checked" : "") %>></td></tr>
    <% String [] swStatusList = SWProduct.getStatusList(myConn); %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Status:</td><td><select name=status size=1>
    <% for (i=0; i<swStatusList.length; i++) { %>
        <option value="<%= swStatusList[i] %>"<%= ((swStatusList[i].equals(prod.getStatus())) ? " selected" : "" ) %>><%= swStatusList[i] %></option>
    <% } %>
    </select></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Related Product ID:</td><td><input type=text name=relatedproduct size=4 maxLength=4 value='<%= ((prod.getRelatedProduct() != 0) ? prod.getRelatedProduct() : "") %>'></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td valign=top>Comments:</td><td><textarea name=comments rows=4 cols=80></textarea></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td valign=top>Attachments:</td><td>
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
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td valign=top colspan=2>Matching Inventory Applications:<br>
        <% if (!(command.equals("new") && prodID != 0)) { %>
                <%
                HashMap items = AppInventory.getAppNameHash(myConn, true, true);
                HashMap matches = ProdAppInvMatch.getItemHash(myConn, prodID);
                %>
                <%= HtmlUtils.doDualSelect("applist", formName, items, matches, "<b>Available</b>", "<b>Matches</b>") %>
        <% } %>
    </td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td valign=top colspan=2>Matching Inventory Services:<br>
        <% if (!(command.equals("new") && prodID != 0)) { %>
                <%
                HashMap items = ServiceInventory.getServiceNameHash(myConn, true, true);
                HashMap matches = ProdServInvMatch.getItemHash(myConn, prodID);
                %>
                <%= HtmlUtils.doDualSelect("servlist", formName, items, matches, "<b>Available</b>", "<b>Matches</b>") %>
        <% } %>
    </td></tr>
    <input type=hidden name=nextscript value="swBrowse.jsp">
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td colspan=2 align=center><input type=button name=submitform value="Submit" onclick="doFormSubmit()"</td></tr>
    <script language=javascript><!--

        function doFormSubmit() {
            // javascript form verification routine
            var msg = "";
            if (isblank(<%= formName %>.name.value)) {
                msg += "Software Name is Required.\n";
            }
            if (!isblank(<%= formName %>.relatedproduct.value) && !isnumeric(<%= formName %>.relatedproduct.value)) {
                msg += "Related Product must be a whole number grater than or equal to '0'.\n";
            }
            for (index=0; index < <%= formName %>.applist.length-1;index++) {
                <%= formName %>.applist.options[index].selected = true;
            }
            for (index=0; index < <%= formName %>.servlist.length-1;index++) {
                <%= formName %>.servlist.options[index].selected = true;
            }
            if (msg != "") {
                alert (msg);
            } else {
                <%= formName %>.id.value = <%= prodID %>;
                //alert('Not yet implemented');
                submitFormResults("doSWProduct","<%= ((command.equals("edit")) ? "update" : command) %>")
            }
        }

    //-->
    </script>

<% } else { %>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>ID:</td><td><%= prod.getID() %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Name:</td><td><%= prod.getName() %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Version:</td><td><%= ((prod.getVersion() != null) ? prod.getVersion() : " &nbsp;") %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Manufacturer:</td><td><%= ((prod.getManufacturer() != null) ? prod.getManufacturer() : " &nbsp;") %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Platform:</td><td><%= ((prod.getManufacturer() != null) ? prod.getPlatform() : " &nbsp;") %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Responsible Organization:</td><td><%= ((prod.getRespOrg() != null) ? prod.getRespOrg() : " &nbsp;") %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Unlimited:</td><td><%= prod.isUnlimited() %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Total Count:</td><td><%= ((prod.isUnlimited()) ? "Unlimited" : prod.getTotalCount()) %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Used Count:</td><td><%= prod.getUsedCount() %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Available Count:</td><td><%= ((prod.isUnlimited()) ? "Unlimited" : prod.getAvailCount()) %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Status:</td><td><%= ((prod.getStatus() != null) ? prod.getStatus() : "&nbsp; ") %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Related Product ID:</td><td><%= ((prod.getRelatedProduct() != 0) ? prod.getRelatedProduct() : "&nbsp;") %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Date Added:</td><td><%= prod.getDateAdded() %></td></tr>
    <tr bgcolor=<%= bgcolors[(i++)%2] %>><td>Date Modified:</td><td><%= prod.getDateModified() %></td></tr>
<% } %>
</table>

<input type=hidden name=transid value="0">
<br>

<% if (prodID != 0) { %>
    <% if (editForm) { %>
        <p><a href="javascript:doNew()">New Transaction</a></p>
    <% } %>

    <% tempText = "<b>Software Transactions</b> (" + ((swTrans != null) ? swTrans.length : "") + ")"; %>
    <rdc:doSection name="swtlist" title="<%= tempText %>" isOpen="false" >
    <table border=0>
    <tr><td valign=bottom><b>ID</b></td><td valign=bottom><b>Tag</b></td><td valign=bottom><b>License Type</b></td><td valign=bottom><b>Count</b></td><td valign=bottom><b>Transaction Type</b></td>

    </tr>

    <%
    for (i=0; i < swTrans.length; i++) {
    
        %>
        <tr bgcolor=<%= bgcolors[(i+1)%2] %>>
        <td valign=top><a href="javascript:doView(<%= swTrans[i].getID() %>)"><%= swTrans[i].getID() %></a>
        <rdc:hasPermission permission="12-mansw" > <a href="javascript:doEdit(<%= swTrans[i].getID() %>)"><font size=-3><i>(edit)</i></font></a></rdc:hasPermission></td>
        <td valign=top><%= ((swTrans[i].getTag() != null) ? "<a href=\"javascript:doView(" + swTrans[i].getID() + ")\">" + swTrans[i].getTag() + "</a>" : "&nbsp;") %></td>
        <td valign=top><%= ((swTrans[i].getLicenseType() != null) ? swTrans[i].getLicenseType() : "&nbsp;") %></td>
        <td valign=top><%= swTrans[i].getLicenseCount() %></td>
        <td valign=top><%= ((swTrans[i].getTransactionType() != null) ? swTrans[i].getTransactionType() : "&nbsp;") %></td>
        </tr>
        <%
    }
    %>
    </table>
    </rdc:doSection >
    <script language=javascript><!--
    
        function doView(id) {
            document.main.id.value = <%= prodID %>;
            document.main.transid.value = id;
            submitForm("swTransactionForm.jsp","view")
        }
    
        function doEdit(id) {
            document.main.id.value = <%= prodID %>;
            document.main.transid.value = id;
            submitForm("swTransactionForm.jsp","edit")
        }
    
        function doNew() {
            document.main.id.value = <%= prodID %>;
            submitForm("swTransactionForm.jsp","new")
        }
    
    //-->
    </script>


<!-- **************     Computers     **************** -->
    <% int compCount = ((compList != null) ? compList.length : 0) + ((compList2 != null) ? compList2.length : 0); %>
    <% tempText = "<b>Computers with this software installed</b> (" + compCount + ")"; %>
    <rdc:doSection name="complist" title="<%= tempText %>" isOpen="false" >
    <table border=0>
    <tr><td valign=bottom><b>ID</b></td><td valign=bottom><b>Name</b></td>

    </tr>

    <%
    int j= 0;
    for (i=0; i < compList.length; i++) {
        
        %>
        <tr bgcolor=<%= bgcolors[(j)%2] %>>
        <td valign=top><a href="javascript:doViewComputer(<%= compList[i].getComputerID() %>)"><%= compList[i].getComputerID() %></a></td>
        <td valign=top><a href="javascript:doViewComputer(<%= compList[i].getComputerID() %>)"><%= compList[i].getName() %></a></td>
        </tr>
        <%
        j++;
    }
    for (i=0; i < compList2.length; i++) {
        
        %>
        <tr bgcolor=<%= bgcolors[(j)%2] %>>
        <td valign=top><a href="javascript:doViewComputer(<%= compList[i].getComputerID() %>)"><%= compList2[i].getComputerID() %></a></td>
        <td valign=top><a href="javascript:doViewComputer(<%= compList[i].getComputerID() %>)"><%= compList2[i].getName() %></a></td>
        </tr>
        <%
        j++;
    }
    %>
    </table>
<script language=javascript><!--

    function doViewComputer(id) {
        document.main.id.value = id;
        submitForm("computer.jsp","")
    }
//-->
</script>
    </rdc:doSection >


<!-- **************      Comments    **************** -->
    <% tempText = "<b>Comments</b> (" + ((comms != null) ? comms.length : "0") + ")"; %>
    <rdc:doSection name="commentlist" title="<%= tempText %>" isOpen="false" >
    <table border=0>
    <tr><td valign=bottom><b>ID</b></td><td valign=bottom><b>Transaction</b></td><td valign=bottom><b>Date Entered</b></td><td valign=bottom><b>Entered By</b></td>

    </tr>

    <%
    for (i=0; i < comms.length; i++) {
    
        %>
        <tr bgcolor=<%= bgcolors[(i+1)%2] %>>
        <td valign=top><%= comms[i].getID() %></td>
        <td valign=top><%= ((comms[i].getTransactionID() != 0) ? comms[i].getTransactionID() : "&nbsp;") %></td>
        <td valign=top><%= Utils.dateToString(comms[i].getDateEntered(), "MM/dd/yyyy hh:mm:ss a") %></td>
        <% Person per = new Person(); per.lookup(myConn, comms[i].getEnteredBy()); %>
        <% tempText = ((comms[i].getEnteredBy() != 0) ? per.getLastName() + ", " + per.getFirstName() : "unknown"); %>
        <td valign=top><%= tempText %></td>
        </tr>
        <tr bgcolor=<%= bgcolors[(i+1)%2] %>>
        <td valign=top></td>
        <% tempText = "comment-id-" + i; %>
        <td valign=top colspan=3><rdc:colapseString name="<%= tempText %>" maxLen="200" isOpen="false" ><%= comms[i].getText().replace("\n", "<br>") %></rdc:colapseString ></td>
        </tr>
        <%
    }
    %>
    </table>
    </rdc:doSection >

<!-- **************     Attachments     **************** -->
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
    //-->
    </script>


<!-- **************    Matching Inventory Applications      **************** -->
    <% ProdAppInvMatch [] items = ProdAppInvMatch.getItemList(myConn, prodID); %>
    <% tempText = "<b>Matching Inventory Applications</b> (" + items.length + ")"; %>
    <rdc:doSection name="matchapplist" title="<%= tempText %>" isOpen="false" >
        <table border=0>
        <tr><td valign=bottom><b>Name</b></td></tr>
        <%
        for (i=0; i<items.length; i++) {
            %>
            <tr bgcolor=<%= bgcolors[(i+1)%2] %>>
            <td valign=top><%= items[i].getListName() %></td>
            </tr>
            <%
        }
        %>
        </table>
    </rdc:doSection >


<!-- **************    Matching Inventory Services      **************** -->
    <% ProdServInvMatch [] sItems = ProdServInvMatch.getItemList(myConn, prodID); %>
    <% tempText = "<b>Matching Inventory Services</b> (" + sItems.length + ")"; %>
    <rdc:doSection name="matchservlist" title="<%= tempText %>" isOpen="false" >
        <table border=0>
        <tr><td valign=bottom><b>Name</b></td></tr>
        <%
        for (i=0; i<sItems.length; i++) {
            %>
            <tr bgcolor=<%= bgcolors[(i+1)%2] %>>
            <td valign=top><%= sItems[i].getListName() %></td>
            </tr>
            <%
        }
        %>
        </table>
    </rdc:doSection >


<!-- **************    Usage History      **************** -->
    <% ProductUsage [] puItems = ProductUsage.getItemList(myConn, prodID); %>
    <% tempText = "<b>Usage History</b> (" + puItems.length + ")"; %>
    <rdc:doSection name="usagehistory" title="<%= tempText %>" isOpen="false" >
        <table border=0>
        <tr><td valign=bottom><b>Activity Date</b></td><td valign=bottom><b>Total Count</b></td><td valign=bottom><b>Used Count</b></td><td valign=bottom><b>Available Count</b></td></tr>
        <%
        for (i=0; i<puItems.length; i++) {
            %>
            <tr bgcolor=<%= bgcolors[(i+1)%2] %>>
            <td valign=top><%= puItems[i].getActivityDate() %></td>
            <td valign=top><%= puItems[i].getTotalcount() %></td>
            <td valign=top><%= puItems[i].getUsedcount() %></td>
            <td valign=top><%= puItems[i].getAvailcount() %></td>
            </tr>
            <%
        }
        %>
        </table>
    </rdc:doSection >

<% } %>



<%
myConn.release();
%>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
