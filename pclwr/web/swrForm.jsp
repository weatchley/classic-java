<HTML>

<HEAD>
<TITLE>ITS Software Software Work Request System</TITLE>
<script src=javascript/utilities.js></script>
<script src=javascript/widgets.js></script>
  <LINK href="css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="css/prstyle.css" type="text/css">
</HEAD>
<BODY leftmargin=0 topmargin=0>
<%@ include file="headerSetup.inc" %>
<% useFileUpload = true; %>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");

DbConn myConn = new DbConn("csi");

String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "new");
//System.out.println("workRequestForm.jsp - Got Here 1" + " command: " + command);
String IDs = ((request.getParameter("id")  != null && !request.getParameter("id").equals("")) ? (String) request.getParameter("id") : "0");
int id = Integer.parseInt(IDs);


SoftwareWorkRequest wr = new SoftwareWorkRequest();
if (command.equals("new")) {
    wr = new SoftwareWorkRequest();
} else {
    wr = new SoftwareWorkRequest(id, myConn);
}

myConn.release();

%>
<!--// ------------------------------------------------------------------------------------------ -->

<style type="text/css">

    .rowHeader {
        font-family: Helvetica, Arial;
        font-size: 14px;
        font-weight: bold;
        background: #eeeeee;
        padding: 3px;
        width: 650px;
    }

    .rowAttachment {
        font-family: Helvetica, Arial;
        font-size: 12px;
        padding: 3px;
        width: 400px;

    }

    .rowNav {
        font-family: Helvetica, Arial;
        font-size: 12px;
        padding: 3px;
        text-align: center;
    }

    .rowNav2 {
        font-family: Helvetica, Arial;
        font-size: 12px;
        padding: 3px;
        text-align: left;
    }


    a:hover,a:link,a:visited,a:active {
        color: #0000dd;
    }

</style>

<!--table border=0 width=750 align=center><tr><td-->
<% if (!command.equals("new")) { %>
<input type=hidden name=wrid value=<%= id %>>
<% } else { %>
<input type=hidden name=wrid value=0>
<% } %>
<% if (command.equals("new") || command.equals("add") || command.equals("update")) { %>
    <table cellpadding=4 cellspacing=0 border=0 width=700>
    <tr><td colspan=4><h2 align=center>Software Work Request</h2></td></tr>
    <tr><td colspan=4 align=center><font color=#000099 size=2><font size=+1><%= ((!command.equals("new")) ? "SWR" + lPad(Integer.toString(wr.getID()), "0", 4) : "") %></font></font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2><font color=black><b>Contact Information</font></font></td></tr>
    <tr>
        <td><font color=#000099 size=2>Project Sponsor or System Owner:</font></td>
        <td><font color=#000099 size=2><input type=text name=owner size=35 maxlength=99 value="<%= testNull(wr.getOwner(), "") %>"></font></td>
        <td><font color=#000099 size=2>Department:</font></td>
        <td><font color=#000099 size=2><input type=text name=department size=25 maxlength=199 value="<%= testNull(wr.getOrganization(), "") %>"></font></td></tr>
    <tr>
          <td><font color=#000099 size=2>Contact:</font></td>
          <td><font color=#000099 size=2><input type=text name=contact size=35 maxlength=99 value="<%= testNull(wr.getContact(), "") %>"></font></td>
          <td><font color=#000099 size=2>Phone Number:</font></td>
          <td><font color=#000099 size=2><input type=text name=phone size=25 maxlength=12 value="<%= testNull(wr.getPhone(), "") %>"></font></td>
    </tr>
    <tr><td><font color=#000099 size=2>Email:</font></td>
        <td colspan=3><font color=#000099 size=2><input type=text name=email size=45 value="<%= testNull(wr.getEmail(), "") %>"></font></td></tr>
    <% if (!command.equals("new")) { %>
        <tr><td><font color=#000099 size=2>Disposition:</font></td>
              <td><font color=#000099 size=2><select  onChange="if(this.options[this.selectedIndex].value=='Rejected'){reasonRej.style.display=''}else{reasonRej.style.display='none'; document.main.reason_rej.value='';}" name=disposition>
        <option value="Pending"<%= ((wr.getDisposition().equals("Pending")) ? " selected" : "") %>>Pending</option>
        <option value="In Review"<%= ((wr.getDisposition().equals("In Review")) ? " selected" : "") %>>In Review</option>
        <option value="Approved"<%= ((wr.getDisposition().equals("Approved")) ? " selected" : "") %>>Approved</option>
        <option value="Rejected"<%= ((wr.getDisposition().equals("Rejected")) ? " selected" : "") %>>Rejected</option>
        <option value="Closed"<%= ((wr.getDisposition().equals("Closed")) ? " selected" : "") %>>Closed</option>
        <option value="Withdrawn"<%= ((wr.getDisposition().equals("Withdrawn")) ? " selected" : "") %>>Withdrawn</option>
        </select>
        </font></td><td colspan=2><font color=#000099 size=2>Disposition Date:&nbsp;<%= (Utils.dateToString(wr.getDispositionDate())) %></font></td></tr>
    <% } else { %>
        <input type=hidden name=disposition value="Pending">
    <% } %>
    <tr><td colspan=4><font color=#000099 size=2> <span id=reasonRej name=reasonRej style=display:none; >Rejected Reason :<br><textarea name=reason_rej cols=80 rows=3 onBlur=checkLength(value,999,this);></textarea></span></font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2><font color=black>The following information is requested to assist OGS/IT in reviewing and processing your request. Please provide as much information as you currently have available.</font></font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2><font color=black><b>Software Requirements</b></font></font></td></tr>
    <tr><td colspan=2><font color=#000099 size=2>Is this request to modify an existing system?</font></td>
        <td colspan=2><font color=#000099 size=2><input type=radio name=existing value=1 <%= ((testNull(wr.getModifyExisting(), "n/a").equals("Yes")) ? "checked" : "") %>>Yes&nbsp;&nbsp;&nbsp;<input type=radio name=existing value=0 <%= ((testNull(wr.getModifyExisting(), "n/a").equals("No")) ? "checked" : "") %>>No</font></td></tr>
    <tr><td><font color=#000099 size=2>System Name:</font></td><td colspan=3><font color=#000099 size=2><input type=text name=existingsystem size=45 value="<%= testNull(wr.getExistingSystem(), "") %>" maxlength=99></font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2>What do you want the software to do (Requirements)?</font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2><textarea name=requirements cols=80 rows=3 onBlur=checkLength(value,1999,this);><%= testNull(wr.getRequirements(), "") %></textarea></font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2><font color=black><b>Business Case for the Work Request</b></font></font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2>Describe the problems and conditions this Work Request will address.</font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2><textarea name=reason cols=80 rows=3 onBlur=checkLength(value,999,this);><%= testNull(wr.getReason(), "") %></textarea></font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2>Describe how this will benefit your organization.</font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2><textarea name=benefits cols=80 rows=3 onBlur=checkLength(value,999,this);><%= testNull(wr.getBenefits(), "") %></textarea></font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2>Who is involved with this issue?</font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2><textarea name=involvedOrgs cols=80 rows=3 onBlur=checkLength(value,499,this);><%= testNull(wr.getInvolvedOrgs(), "") %></textarea></font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2>What business processes are affected by this problem?</font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2><textarea name=businessProcesses cols=80 rows=3 onBlur=checkLength(value,999,this);><%= testNull(wr.getBusinessProcess(), "") %></textarea></font></td></tr>
    <tr><td colspan=2><font color=#000099 size=2>When is this required?</font></td>
        <td colspan=2><font color=#000099 size=2><input type=text name=daterequired size=25 value="<%= testNull(wr.getRequestedDelivery(), "") %>" maxlength=49></font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2><br><font color=black><b>Comments/Notes</b></font><br>Please include any information about the system that was not covered by the previous questions</font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2><textarea name=comments cols=80 rows=3 onBlur=checkLength(value,999,this);><%= testNull(wr.getComments(), "") %></textarea></font></td></tr>
    <tr><td colspan=4><font color=#000099 size=2><br /><br />
    <table class="attachment" border="1" cellspacing="0">
    <tr><td colspan="2" class="rowHeader">Attachment(s):</td></tr>
    <tr><td colspan="2" class="rowAttachment">Please attach any supporting documents below</td></tr>
    <tr><td colspan="2" class="rowAttachment">
    <input type=hidden name=attachcount value=1>
    <%
    SoftwareAttachment [] att = wr.getAttachments();
    if (att != null) {
        for (int i=0; i<att.length; i++) { %>
            <a href="javascript:viewAttachment(<%= att[i].getID() %>)"><%= att[i].getFilename() %></a><br>
        <% }
    }
    %>
    <input type="file" name="attachment1"><br><div id="attachments"></div></td></tr>
    <tr><td colspan="2" class="rowNav2"><a href="javascript:addNewAttachmentWidget();">Create New Attachment</td></tr>
    </table></td></tr>

    </font><!--/td-->
    </table>
    <br>
    <center>
    <input type=button name=submitEdit value=Submit onClick="validateRequest()"><br>

    </center>

    <script language="javascript">
    <!--
        function addNewAttachmentWidget() {
            //** only works in IE, need to fix
            var count = document.main.attachcount.value;
            count++;
            document.main.attachcount.value = count;
            var insertText = '<input type=file name=attachment' + count + '><br>';
            document.all.attachments.insertAdjacentHTML("BeforeBegin", "" + insertText + "");
        }
    
        function checkLength(val,maxlen,e) {
            var len = val.length;
            var diff = len - maxlen;
            if (diff > 0) {
                alert ("The text you have entered is " + diff + " characters too long.");
                e.focus();
            }
        }

        function validateRequest() {
            var errors = "";
            var msg;

            if (isblank(document.main.owner.value) ||
                (document.main.owner.value == null || document.main.owner.value == "")) {
                errors += "\The Requester must be identified.\n";
            }
            if (isblank(document.main.department.value) ||
                (document.main.department.value == null || document.main.department.value == "")) {
                errors += "\The Department or Organization of the Requester must be identified.\n";
            }
            if (isblank(document.main.contact.value) ||
                (document.main.contact.value == null || document.main.contact.value == "")) {
                errors += "\The Contact information must be supplied.\n";
            }
            if (isblank(document.main.phone.value) ||
                (document.main.phone.value == null || document.main.phone.value == "")) {
                errors += "\A Phone Number for the Contact person must be provided.\n";
            }
            if (isblank(document.main.email.value) ||
                (document.main.email.value == null || document.main.email.value == "")) {
                errors += "\An email address for the Contact person must be provided.\n";
            }
            if (!document.main.existing[0].checked && !document.main.existing[1].checked) {
                errors += "\No option set for Modify an existing system.\n";
            }
            if (document.main.existing[0].checked && isblank(document.main.existingsystem.value)) {
                errors += "\No existing system identified.\n";
            }
            if (isblank(document.main.requirements.value) ||
                (document.main.requirements.value == null || document.main.requirements.value == "")) {
                errors += "\No information supplied regarding requirements.\n";
            }
            if (isblank(document.main.reason.value) ||
                (document.main.reason.value == null || document.main.reason.value == "")) {
                errors += "\No information supplied regarding problems addressed.\n";
            }
            if (isblank(document.main.benefits.value) ||
                (document.main.benefits.value == null || document.main.benefits.value == "")) {
                errors += "\No information supplied regarding the benefits to your organization.\n";
            }
            if (isblank(document.main.involvedOrgs.value) ||
                (document.main.involvedOrgs.value == null || document.main.involvedOrgs.value == "")) {
                errors += "\No information supplied regarding the organizations involved with this issue.\n";
            }
            if (isblank(document.main.businessProcesses.value) ||
                (document.main.businessProcesses.value == null || document.main.businessProcesses.value == "")) {
                errors += "\No information supplied regarding any business processes affected.\n";
            }
            if (isblank(document.main.daterequired.value) ||
                (document.main.daterequired.value == null || document.main.daterequired.value == "")) {
                errors += "\No information supplied regarding the date required.\n";
            }
            if (document.main.wrid.value != 0 && document.main.disposition.options[document.main.disposition.selectedIndex].value == 'Rejected' &&
                  isblank(document.main.reason_rej.value)) {
                errors += "\Rejection reason must be entered.\n";
            }
            if (errors != "") {
                msg  = "______________________________________________________\n\n";
                msg += "The form was not submitted because of the following error(s).\n";
                msg += "Please correct these errors(s) and re-submit.\n";
                msg += "______________________________________________________\n";
                msg += "\n" + errors;
                alert(msg);
                return false;
            } else {
                submitFormResults('doSoftwareWorkRequest','<%= command %>');
            }
        }

    -->
    </script>

    <!--// ------------------------------------------------------------------------------------------ -->
<% } else { %>
    <table cellpadding=1 cellspacing=0 border=0 align=center width=500>
    <tr bgcolor=#a0e0c0><td colspan=2><b>Work Request SWR<%= lPad(Integer.toString(wr.getID()), "0", 4) %></b></td></tr>
    <tr><td valign=top>Owner: </td><td><%= testNull(wr.getOwner(), "") %></td></tr>
    <tr><td valign=top>Department: </td><td><%= testNull(wr.getOrganization(), "") %></td></tr>
    <tr><td valign=top>Contact: </td><td><%= testNull(wr.getContact(), "") %></td></tr>
    <tr><td valign=top>Phone Number: </td><td><%= testNull(wr.getPhone(), "") %></td></tr>
    <tr><td valign=top>Email: </td><td><%= testNull(wr.getEmail(), "") %><br>
    <tr><td valign=top>Existing System: </td><td><%= ((wr.modifyExisting()) ? testNull(wr.getExistingSystem(), "") : "None") %><br>
    <tr><td valign=top>Requirements: </td><td><%= formatText(testNull(wr.getRequirements(), "")) %></td></tr>
    <tr><td valign=top>Problems&nbsp;and&nbsp;Conditions: </td><td><%= formatText(testNull(wr.getReason(), "")) %><br>
    <tr><td valign=top>Benefits: </td><td><%= formatText(testNull(wr.getBenefits(), "")) %><br>
    <tr><td valign=top>Involved&nbsp;Organizations: </td><td><%= formatText(testNull(wr.getInvolvedOrgs(), "")) %></td></tr>
    <tr><td valign=top>Business Process: </td><td><%= formatText(testNull(wr.getBusinessProcess(), "")) %><br>
    <tr><td valign=top>When Required: </td><td><%= testNull(wr.getRequestedDelivery(), "") %></td></tr>
    <tr><td valign=top>Comments: </td><td><%= formatText(testNull(wr.getComments(), "")) %></td></tr>
    <tr><td valign=top>Date Submitted: </td><td><%= Utils.dateToString(wr.getSubmitDate()) %></td></tr>
    <tr><td valign=top>Disposition: </td><td><%= testNull(wr.getDisposition(), "") %></td></tr>
    <tr><td valign=top>Disposition Date: </td><td><%= Utils.dateToString(wr.getDispositionDate()) %></td></tr>
    <tr><td valign=top>Reason for Rejection: </td><td><%= formatText(testNull(wr.getReasonRej(), "")) %></td></tr>
    <tr><td valign=top>Attachments: </td><td>
    <%
    SoftwareAttachment [] att = wr.getAttachments();
    if (att != null) {
        for (int i=0; i<att.length; i++) { %>
            <a href="javascript:viewAttachment(<%= att[i].getID() %>)"><%= att[i].getFilename() %></a><br>
        <% }
    }
    %>
    </td></tr>
    </table>    

<% } %>

    <script language="javascript">
    <!--
       function viewAttachment(id) {
           document.main.id.value = id;
           submitFormNewWindow('viewAttachment.jsp','none')
       }

    -->
    </script>



<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
<%!
private String formatText(String text) {
    String txt = "";
    txt = text.replaceAll("\n", "<br>");
    txt = txt.replaceAll("  ", " &nbsp;");
    return (txt);
}
%>
