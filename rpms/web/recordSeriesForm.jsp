<HTML>

<HEAD>
<TITLE>Records Plan Management System</TITLE>
<script src=javascript/utilities.js></script>
<script src=javascript/widgets.js></script>
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

DbConn myConn = new DbConn("csi");

int maxLen = 4000;


String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
//System.out.println("recordSeriesForm.jsp - Got Here 1" + " command: " + command);
String IDs = ((request.getParameter("id")  != null) ? (String) request.getParameter("id") : "0");
int id = Integer.parseInt(IDs);

RecordSeries rec = null;
if (command.equals("new")) {
    rec = new RecordSeries();
} else {
    rec = new RecordSeries(id, myConn);
}

RetentionGroupFlag[] retentionGroupFlags = RetentionGroupFlag.getItemList(myConn);

if (!isAuthenticated || !isAdmin) {
    command = "view";
}
String controls = "<a href=\"javascript:doSave()\">Save</a>";
Crosswalk [] cw = rec.getCrosswalkSet();
controls += (!command.equals("new") && cw.length == 0) ? " &nbsp; &nbsp; <a href=\"javascript:doDrop(" + rec.getID() + ")\">Delete</a>" : "";
controls += " &nbsp; &nbsp; <a href=\"javascript:doReset()\">Cancel</a>";
%>
<script language="javascript">
    var itemValueStore = new Object();
    itemValueStore.item0 = "";

</script>
  

  <h3>Record Series <%= ((command.equals("view")) ? "View" : "Edit") %> Page</h3><br>

<input type=hidden name=nextscript value='recordSeries.jsp'>
<input type=hidden name=commenttext value='Archive'>
<table border=0>
<% //System.out.println("recordSeriesForm.jsp - Got Here II.1" + " command: " + command); %>
<% if (!command.equals("view")) { %>
<tr><td colspan=3 align=right><%= controls %></td></tr>
<% } %>

<!-- * -->
<!--tr><td valign=top>ID:</td><td>&nbsp;&nbsp;</td><td valign=top><%= ((id > 0) ? id : "<i>new</i>") %></td></tr-->
<input type=hidden name=rsid value=<%= id %>>
<input type=hidden name=cwid value=0>
<!-- * -->
<tr><td valign=top>Description:</td><td>&nbsp;&nbsp;</td><td valign=top>
<% if (command.equals("view")) { %>
    <%= rec.getDescription() %>
<% } else { %>
    <textarea name=description cols=70 rows=4><%= ((!command.equals("new")) ? rec.getDescription() : "") %></textarea>
<% } %>
</td></tr>
<!-- * -->
<tr><td valign=top>Common:</td><td>&nbsp;&nbsp;</td><td valign=top>
<% if (command.equals("view")) { %>
    <%= ((rec.getCommonFlag().equals("T")) ? "True" : "False") %>
<% } else { %>
    <input type=checkbox name=commonflag value='T' <%= ((rec.getCommonFlag() != null && rec.getCommonFlag().equals("T")) ? " checked" : "") %>>
<% } %>
</td></tr>
<!-- * -->
<tr><td valign=top>Location:</td><td>&nbsp;&nbsp;</td><td valign=top>
<% if (command.equals("view")) { %>
    <%= rec.getLocationDescription() %>
<% } else { %>
    <select name=location>
    <option value=0></option>
    <% 
    FOrganization [] tmp = FOrganization.getItemList(myConn);
    for (int i=0; i<tmp.length; i++) { %>
        <option value=<%= tmp[i].getID() %><%= ((tmp[i].getID() == rec.getLocation()) ? " selected" : "") %>><%= tmp[i].getDescription() %></option><%
    } %>
    </select>
<% } %>
</td></tr>
<!-- * -->
<tr><td valign=top>OCRWM Code:</td><td>&nbsp;&nbsp;</td><td valign=top>
<% if (command.equals("view")) { %>
    <%= rec.getOCRWMCode() %>
<% } else { %>
    <input type=text name=ocrwmcode size=50 maxlength=50 value='<%= ((rec.getOCRWMCode() != null) ? rec.getOCRWMCode() : "") %>'>
<% } %>
</td></tr>
<!-- * -->
<tr><td valign=top>Citation Category:</td><td>&nbsp;&nbsp;</td><td valign=top>
<% if (command.equals("view")) { %>
    <%= rec.getCitationDescription() %>
<% } else { %>
    <select name=citation>
    <option value=0></option>
    <% 
    CCategory [] tmp = CCategory.getItemList(myConn);
    for (int i=0; i<tmp.length; i++) { %>
        <option value=<%= tmp[i].getID() %><%= ((tmp[i].getID() == rec.getCitation()) ? " selected" : "") %>><%= tmp[i].getDescription() %></option><%
    } %>
    </select>
<% } %>
</td></tr>
<!-- * -->
<tr><td valign=top>Cutoff:</td><td>&nbsp;&nbsp;</td><td valign=top>
<% if (command.equals("view")) { %>
    <%= rec.getCutoffDescription() %>
<% } else { %>
    <select name=cutoff>
    <option value=0></option>
    <% 
    CutoffList [] tmp = CutoffList.getItemList(myConn);
    for (int i=0; i<tmp.length; i++) { %>
        <option value=<%= tmp[i].getID() %><%= ((tmp[i].getID() == rec.getCutoff()) ? " selected" : "") %>><%= tmp[i].getDescription() %></option><%
    } %>
    </select>
<% } %>
</td></tr>
<!-- * -->
<tr><td valign=top>Retention Period:</td><td>&nbsp;&nbsp;</td><td valign=top>
<% //System.out.println("recordSeriesForm.jsp - Got Here II.1" + " command: " + command); %>
<% if (command.equals("view")) { %>
    <%= rec.getRetentionPeriodDescription() %>
<% } else { %>
    <select name=retentionperiod>
    <option value=0></option>
    <% 
    RetentionPeriod [] tmp = RetentionPeriod.getItemList(myConn);
    for (int i=0; i<tmp.length; i++) { %>
        <option value=<%= tmp[i].getID() %><%= ((tmp[i].getID() == rec.getRetentionPeriod()) ? " selected" : "") %>><%= tmp[i].getDescription() %></option><%
    } %>
    </select>
<% } %>
</td></tr>
<!-- * -->
<tr><td valign=top>Retention Code:</td><td>&nbsp;&nbsp;</td><td valign=top>
<% if (command.equals("view")) { 
       if (rec.getRetentionCode() != null) { %>
          <%= rec.getRetentionCode() %> - <%= rec.getRetentionCodeDescription() %>
    <% } else { %>
    <% } %>
<% } else { %>
    <select name=retentioncode>
    <option value=''></option>
    <% 
    RetentionCode [] tmp = RetentionCode.getItemList(myConn);
    for (int i=0; i<tmp.length; i++) { %>
        <option value='<%= tmp[i].getCode() %>'<%= ((tmp[i].getCode() == rec.getRetentionCode()) ? " selected" : "") %>><%= tmp[i].getCode() %> - <%= tmp[i].getDescription() %></option><%
    } %>
    </select>
<% } %>
</td></tr>
<!-- * -->
<tr><td valign=top>Retention Group Flag:</td><td>&nbsp;&nbsp;</td><td valign=top>
<% if (command.equals("view")) { %>
    <%= rec.getRetentionGroupFlagDescription() %>
<% } else { %>
    <% for (int i=0; i<retentionGroupFlags.length; i++) { %>
         <input type=radio name=retentiongroupflag value=<%= retentionGroupFlags[i].getID() %> <%= ((rec.getRetentionGroupFlag() == retentionGroupFlags[i].getID()) ? " checked" : "") %>>
         <%= retentionGroupFlags[i].getDescription() %><br>
    <% } %>
<% } %>
</td></tr>
<!-- * -->
<tr><td valign=top>Date Added:</td><td>&nbsp;&nbsp;</td><td valign=top>
<% if (command.equals("new")) { %>
    <i>new</i>
<% } else { %>
    <%= Utils.dateToString(rec.getDateAdded()) %>
<% } %>
</td></tr>
<!-- * -->
<tr><td valign=top>Date Changed:</td><td>&nbsp;&nbsp;</td><td valign=top>
<% if (command.equals("new")) { %>
    <i>new</i>
<% } else { %>
    <%= Utils.dateToString(rec.getDateChanged()) %>
<% } %>
</td></tr>
<!-- * -->
<tr><td valign=top>Crosswalk:</td><td>&nbsp;&nbsp;</td><td valign=top>
<% 
cw = rec.getCrosswalkSet();
Crosswalk cwBlank = new Crosswalk();  // blank crosswalk for generating empty forms
int itemCount=0;
if (cw != null) {
    for (int i=0; i < cw.length; i++) {
        // call function to generate form with cw[i]
        String text = cw[i].getName();
        text = text.replaceAll("'", "\\\\'");
        text = text.replaceAll("[\\n\\r]","\\\\n");
        %>
        <script language="javascript" type="text/javascript">
            itemValueStore.item<%= cw[i].getID() %> = '<%= text %>';
        </script>
        <%= buildItemForm(cw[i], command, "normal", i, myConn) %> <%
        itemCount++;
    }
} else {
    Crosswalk myCW = cwBlank;
    // call function to generate form with cwBlank
    %>
    <%= buildItemForm(myCW, command, "new", 0, myConn) %>
    <%
    itemCount++;
}
%>
<% if (!command.equals("view")) { %>
<table cellpadding=0 cellspacing=0 border=0 id=postItemTable><tr><td><a href=javascript:addItem()>New Crosswalk</a></td></tr></table>
<% } %>
<input type=hidden name=itemcount value=<%= itemCount %>>

<% if (!command.equals("view")) { %>
<script language=javascript><!--
function addItem() {
// add an entry to the item table
    var newItemRow = "";
    var items = document.main.itemcount.value;
    items;
    document.main.itemcount.value = (items - 0) + 1;
    newItemRow += "<%= "<!-- // call function to generate form with cwBlank -->" %>";
    newItemRow += "<%= buildItemForm(cwBlank, command, "jsinsert", 0, myConn) %>";
    document.all.postItemTable.insertAdjacentHTML("BeforeBegin", "" + newItemRow + "");
    
}
    //-->
</script>
<% } %>
</td></tr>

<% if (!command.equals("view")) { %>
<tr><td align=left colspan=3><%= controls %></td></tr>
<% } %>
</table>

<%

myConn.release();
%>

<% if (isAuthenticated) { %>
        <% if (isAdmin && !command.equals("new")) { %>
            <p><a href="javascript:doNew()">New</a></p>
        <% } %>

<script language=javascript><!--

    // A utility function that returns true if a string contains only
    // whitespace characters.
    function isblank(s) {
        if (s.length == 0) return true;
        for(var i = 0; i < s.length; i++) {
            var c = s.charAt(i);
            if ((c != ' ') && (c != '\n') && (c != '\t') && (c !='\r')) return false;
        }
        return true;
    }

    function doNew() {
        document.main.id.value = 0;
        submitForm("recordSeriesForm.jsp","new");
    }

    function doSave() {
        var msg = '';
        if (isblank(document.main.description.value) || document.main.description.value.length > <%= maxLen %>) {
            msg += 'A description (upto <%= maxLen %> characters) must be entered.\n';
        }
        //if (isblank(document.main.ocrwmcode.value)) {
        //    msg += 'An OCRWM Code must be entered.\n';
        //}
        if (document.main.location.selectedIndex < 1) {
            msg += 'A Location must be selected\n';
        }
        if (document.main.citation.selectedIndex < 1) {
            msg += 'A Citation Category must be selected\n';
        }
        if (document.main.cutoff.selectedIndex < 1) {
            msg += 'A Cutoff must be selected\n';
        }
        if (!document.main.retentiongroupflag[0].checked<% for (int i=1; i<retentionGroupFlags.length; i++) { %> && !document.main.retentiongroupflag[<%= i %>].checked<% } %>) {
            msg += 'A Retention Group Flag must be selected\n';
        }
        //if (document.main.retentioncode.selectedIndex < 1) {
        //    msg += 'A retention code must be selected.\n';
        //}
        var hasInputs = false;
        for (i=0; i < document.main.itemcount.value; i++) {
            var j = i + 1;
            var reqOtherFlag = false;
            var code = ""
                +"for (index=0; index < document.main.requirements"+i+".length-1;index++) { \n"
                +"    if(document.main.requirements"+i+".options[index].value == 1) { reqOtherFlag = true; } \n"
                +"} \n"
                +"if (!isblank(document.main.name"+i+".value) || !isblank(document.main.requirementsother"+i+".value) || \n"
                +"      !isblank(document.main.freezeholdtext"+i+".value) || \n"
                +"      document.main.unscheduled"+i+".checked || document.main.freezeholdflag"+i+".checked || document.main.vitalrecord"+i+".checked || \n"
                +"      document.main.qadesignation"+i+".selectedIndex != 0 || document.main.accessrestrictions"+i+".selectedIndex != 0 \n"
                //+"      || document.main.requirements"+i+".selectedIndex != 0
                +"      ) {\n"
                +"          if (isblank(document.main.name"+i+".value)) {\n"
                +"              //msg += '"+j+" - \"Name\" must be entered\\n';\n"
                +"              document.main.name"+i+".value = document.main.description.value + '" + ((i>0) ? " - " + i : "") + "';\n"
                +"          }\n"
                +"          if (document.main.qadesignation"+i+".selectedIndex == 0) {\n"
                +"              msg += '"+j+" - \"QA Designation\" must be selected\\n';\n"
                +"          }\n"
                +"          //if (document.main.accessrestrictions"+i+".selectedIndex == 0) {\n"
                +"          //    msg += '"+j+" - \"Access Restrictions\" must be selected\\n';\n"
                +"          //}\n"
                +"          //if (document.main.requirements"+i+".selectedIndex == 0) {\n"
                +"          //    msg += '"+j+" - \"Requirement\" must be selected\\n';\n"
                +"          //}\n"
                +"          //if (document.main.requirements"+i+".selectedIndex == 1 && isblank(document.main.requirementsother"+i+".value)) {\n"
                +"          if (reqOtherFlag && isblank(document.main.requirementsother"+i+".value)) {\n"
                +"              msg += '"+j+" - \"Requirements Other\" must be entered when the requirement \"Other\" is selected\\n';\n"
                +"          }\n"
                +"          //if (document.main.requirements"+i+".selectedIndex != 1 && !isblank(document.main.requirementsother"+i+".value)) {\n"
                +"          if (!reqOtherFlag && !isblank(document.main.requirementsother"+i+".value)) {\n"
                +"              msg += '"+j+" - \"Requirements Other\" can not be entered when the requirement \"Other\" is not selected\\n';\n"
                +"          }\n"
                +"          if (document.main.freezeholdflag"+i+".checked && isblank(document.main.freezeholdtext"+i+".value)) {\n"
                +"              msg += '"+j+" - \"Freeze Hold Text\" must be entered\\n';\n"
                +"          }\n"
                +"          if (!document.main.freezeholdflag"+i+".checked && !isblank(document.main.freezeholdtext"+i+".value)) {\n"
                +"              msg += '"+j+" - \"Freeze Hold Text\" can not be entered if \"Freeze Hold Flag\" is not set\\n';\n"
                +"          }\n"
                +"}"
            + "";
            //alert(code);
            eval(code);
        }
        if (msg > '') {
            alert(msg);
        } else {
            //alert('Not yet implemented');
            for (i=0; i < document.main.itemcount.value; i++) {
                var j = i + 1;
                var code = ""
                    +"for (index=0; index < document.main.requirements"+i+".length-1;index++) { \n"
                    +"    document.main.requirements"+i+".options[index].selected = true; \n"
                    +"} \n"
                    + "";
                eval(code);
            }
            submitFormResults("doRecordSeries",<%= ((command.equals("new")) ? "'add'" : "'update'") %>);
        }
    }

    function doReset() {
        document.main.reset();
    }
    <% String tempDescription = ((!command.equals("new")) ? ((rec.getDescription()).replaceAll("\n", " ")) : ""); %>
    <% tempDescription = tempDescription.replaceAll("\r", " "); %>
    <% tempDescription = tempDescription.replaceAll("'", ""); %>
    function doDrop(id) {
        if (confirm('Delete Record Series "<%= tempDescription %>"?')) {
            document.main.commenttext.value = 'Record%20Series%20Deletion';
            archiveDelete(id, "rs");
            //submitFormResults("doRecordSeries","drop");
        }
    }
    
    function doDropCW(id) {
        eval('var message = itemValueStore.item' + id + ';');
        if (confirm('Delete Crosswalk Item "' + message + '"?\n(No save will be done.)')) {
            document.main.commenttext.value = 'Crosswalk%20Deletion';
            archiveDelete(id, "cw");
            //submitFormResults("doRecordSeries","drop");
        }
    }
    
// ************************** begin ajax code to get drop item
//var xmlhttp

function archiveDelete(id, type) {
    var url = 'recordSeriesReport.jsp?id=' + <%= rec.getID() %> + "&commenttext=" + document.main.commenttext.value + "&archiveflag=true";
    xmlhttp=null
    // code for Mozilla, etc.
    if (window.XMLHttpRequest) {
        xmlhttp=new XMLHttpRequest()
    } else if (window.ActiveXObject) {
    // code for IE
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP")
    }
    if (xmlhttp!=null) {
        if(type == 'rs') {
            xmlhttp.onreadystatechange=state_Change_archiveDeleteRS
        } else if (type == 'cw') {
            document.main.cwid.value = id;
            xmlhttp.onreadystatechange=state_Change_archiveDeleteCW
        }
        xmlhttp.open("GET",url,true)
        xmlhttp.send(null)
    } else {
        alert("Your browser does not support XMLHTTP.")
    }
}

function state_Change_archiveDeleteRS() {
// if xmlhttp shows "loaded"
    if (xmlhttp.readyState==4) {
      // if "OK"
      if (xmlhttp.status==200) {
          // ...some code here...
          submitFormResults("doRecordSeries","drop");
      } else {
          alert("Problem Archiving/Deleting Record Series")
      }
    }
}

function state_Change_archiveDeleteCW() {
// if xmlhttp shows "loaded"
    if (xmlhttp.readyState==4) {
      // if "OK"
      if (xmlhttp.status==200) {
          // ...some code here...
          submitFormResults("doRecordSeries","dropcw");
      } else {
          alert("Problem Archiving/Deleting Crosswalk")
      }
    }
}
// ************************** end ajax code to get text item
    //-->
</script>
<% } %>




<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>

<%!
private String buildItemForm(Crosswalk myCW, String command, String type, int id, DbConn myConn) {
    String out = "";
    
    out +="    <table border=0>\n";
    out +="    <tr><td colspan=3><hr width='50%'></td></tr>\n";
    out +="<!-- * -->\n";
    out +="    <!--tr><td valign=top>ID:</td><td>&nbsp;&nbsp;&nbsp;</td><td valign=top>" + ((myCW.getID() > 0) ? myCW.getID() : "<i>new</i>") + "</td></tr-->\n";
//System.out.println("recordSeriesForm.jsp - Got Here III.1" + " command: " + command);
    out +="    <input type=hidden name=cwid<replaceMeIndex> value=" + myCW.getID() + ">\n";
    out +="<!-- * -->\n";
    out +="    <tr><td valign=top>Name:" + ((myCW.getID() > 0 && !command.equals("view")) ? "<br><i><a href=\"javascript:doDropCW(" + myCW.getID() + ")\">Delete Item</a></i>" : "") + "</td>";
    out +="<td></td><td valign=top>" + ((command.equals("view")) ? myCW.getName() : "<textarea name=name<replaceMeIndex> cols=60 rows=5>" + ((myCW.getName() != null) ? myCW.getName() : "") + "</textarea>") + "</td></tr>\n";
    out +="<!-- * -->\n";
    out +="    <tr><td valign=top>QA Designation:</td><td></td><td valign=top>\n";
    if (command.equals("view")) {
        out += ((myCW.getQADesignation() != null) ? myCW.getQADesignation() : "");
    } else {
        out +="    <select name=qadesignation<replaceMeIndex>>\n";
        out +="    <option value=0></option>\n";
        QADesignator [] tmp = QADesignator.getItemList(myConn);
        for (int i=0; i<tmp.length; i++) {
            out +="    <option value=" + tmp[i].getID() + ((tmp[i].getID() == myCW.getQADesignationID()) ? " selected" : "") + ">" + tmp[i].getDescription() + "</option>\n";
        }
        out +="    </select>\n";
    }
    out +="    </td></tr>\n";
    out +="<!-- * -->\n";
    out +="    <tr><td valign=top>Access Restrictions:</td><td></td><td valign=top>\n";
    if (command.equals("view")) {
        out += ((myCW.getAccessRestrictionsCode() != null) ? myCW.getAccessRestrictionsCode() + " - " + myCW.getAccessRestrictions() : "");
    } else {
        out +="    <select name=accessrestrictions<replaceMeIndex>>\n";
        out +="    <option value=''>None</option>\n";
        ARestriction [] tmp = ARestriction.getItemList(myConn);
        for (int i=0; i<tmp.length; i++) {
            out +="    <option value=" + tmp[i].getCode() + ((myCW.getAccessRestrictionsCode() != null && tmp[i].getCode().equals(myCW.getAccessRestrictionsCode())) ? " selected" : "") + ">" + tmp[i].getCode() + " - " + tmp[i].getDescription() + "</option>\n";
        }
        out +="    </select>\n";
    }
    out +="    </td></tr>\n";
    out +="<!-- * -->\n";
    out +="    <tr><td valign=top>Unscheduled:</td><td></td><td valign=top>\n";
    if (command.equals("view")) {
        out += ((myCW.getUnscheduled().equals("T")) ? "True" : "False");
    } else {
        out +="    <input type=checkbox name=unscheduled<replaceMeIndex> value='T'" + ((myCW.getUnscheduled().equals("T")) ? " checked" : "") + ">\n";
    }
    out +="</td></tr>\n";
    out +="<!-- * -->\n";
    out +="    <tr><td valign=top>Requirements:</td><td></td><td valign=top>\n";
    if (command.equals("view")) {
        //out += ((myCW.getRequirements() != null) ? myCW.getRequirements() : "");
        Requirement [] tmp = myCW.getRequirementList();
        for (int i=0; i<tmp.length; i++) {
             out += ((i>0) ? "<br>" : "") + tmp[i].getDescription() + "\n";
        }
    } else {
        //out +="    <select name=requirements<replaceMeIndex>>\n";
        //out +="    <option value=0></option>\n";
         
        //Requirement [] tmp = Requirement.getItemList(myConn);
        //for (int i=0; i<tmp.length; i++) {
        //     out +="   <option value=" + tmp[i].getID() + ((tmp[i].getID() == myCW.getRequirementsID()) ? " selected" : "") + ">" + tmp[i].getDescription() + "</option>\n";
        //}
        //out +="    </select>\n";
        Requirement [] req = Requirement.getItemList(myConn);
        Requirement [] reqList = myCW.getRequirementList();
        HashMap reqAvail = new HashMap(req.length);
        HashMap reqSelected = new HashMap(req.length);
        for (int j=0; j<req.length; j++) {
            reqAvail.put(new Long(req[j].getID()), (req[j].getDescription()));
            if (!type.equals("jsinsert") && !command.equals("new") && reqList != null) {
                for (int k=0; k<reqList.length; k++) {
                    if (req[j].getID() == reqList[k].getID()) {
                        reqSelected.put(new Long(req[j].getID()), (req[j].getDescription()));
                    }
                }
            }
        }
        String dualSelect = HtmlUtils.doDualSelect("requirements<replaceMeIndex>", "main", reqAvail, reqSelected, "<b>Available</b>", "<b>Selected</b>");
        if (type.equals("jsinsert")) {
            dualSelect = dualSelect.replaceAll("\"","\\\\\"");
        }
        out += dualSelect;
    }
    out +="    </td></tr>\n";
    out +="<!-- * -->\n";
    out +="    <tr><td valign=top>Requirements Other:</td><td></td><td valign=top>\n";
    if (command.equals("view")) {
        out += ((myCW.getRequirementsOther() != null) ? myCW.getRequirementsOther() : "");
    } else {
        out +="    <textarea name=requirementsother<replaceMeIndex> cols=60 rows=5>" + ((myCW.getRequirementsOther() != null) ? myCW.getRequirementsOther() : "") + "</textarea>\n";
    }
    out +="    </td></tr>\n";
    out +="<!-- * -->\n";
    out +="    <tr><td valign=top>Freeze Hold Flag:</td><td></td><td valign=top>\n";
    if (command.equals("view")) {
        out += ((myCW.getFreezeHoldFlag().equals("T")) ? "True" : "False");
    } else {
        out +="    <input type=checkbox name=freezeholdflag<replaceMeIndex> value='T'" + ((myCW.getFreezeHoldFlag().equals("T")) ? " checked" : "") + ">\n";
    }
    out +="    </td></tr>\n";
    out +="<!-- * -->\n";
    out +="    <tr><td valign=top>Freeze Hold Text:</td><td></td><td valign=top>\n";
    if (command.equals("view")) {
        out += ((myCW.getFreezeHoldFlagText() != null) ? myCW.getFreezeHoldFlagText() : "");
    } else {
    out +="        <textarea name=freezeholdtext<replaceMeIndex> cols=60 rows=5>" + ((myCW.getFreezeHoldFlagText() != null) ? myCW.getFreezeHoldFlagText() : "") + "</textarea>\n";
    }
    out +="    </td></tr>\n";
    out +="<!-- * -->\n";
    out +="    <tr><td valign=top>Vital Record Flag:</td><td></td><td valign=top>\n";
    if (command.equals("view")) {
        out += ((myCW.getVitalRecord().equals("T")) ? "True" : "False");
    } else {
        out +="    <input type=checkbox name=vitalrecord<replaceMeIndex> value='T'" + ((myCW.getVitalRecord().equals("T")) ? " checked" : "") + ">\n";
    }
    out +="    </td></tr>\n";
    out +="<!-- * -->\n";
    out +="    <tr><td valign=top>Date Added:</td><td>&nbsp;&nbsp;</td><td valign=top>\n";
    if (command.equals("new") || myCW.getID() == 0) {
        out +="    <i>new</i>\n";
    } else {
        out += Utils.dateToString(myCW.getDateAdded());
    }
    out +="    </td></tr>\n";
    out +="<!-- * -->\n";
    out +="    <tr><td valign=top>Date Changed:</td><td>&nbsp;&nbsp;</td><td valign=top>\n";
    if (command.equals("new") || myCW.getID() == 0) {
        out +="    <i>new</i>\n";
    } else {
        out += Utils.dateToString(myCW.getDateChanged());
    }
    out +="    </td></tr>\n";
    out +="    \n";
    out +="    </table>\n";
    
    if (type.equals("jsinsert")) {
        out = out.replaceAll("<replaceMeIndex>", "\" + items + \"");
        out = out.replaceAll("\n", "\\\\n\";\n    newItemRow += \"");
    } else {
        out = out.replaceAll("<replaceMeIndex>", "" + id);
    }
    
    
    return (out);
}

%>