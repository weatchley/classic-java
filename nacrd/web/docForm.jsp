<HTML>

<HEAD>
<TITLE>Tribal Information Exchange</TITLE>
<script src=utilities.js></script>
</HEAD>

<BODY BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>
<%@ include file="headerSetup.html" %>
<% useFileUpload = true; %>
<%@ include file="imageFrameStart.html" %>
<!-- Begin Main Content -->
<%
String command = request.getParameter("command");
String id = request.getParameter("id");
String docRoot = getServletContext().getRealPath("/");
Doc myDoc = null;
if (command.equals("updatedocument")) {
    myDoc = new Doc(docRoot, id);
} else {
    myDoc = new Doc();
    Date dNow = new Date();
    myDoc.setDueDate(Utils.dateToString(Utils.addDays(dNow, 31)));
    myDoc.setNumber("");
    myDoc.setTitle("");
    myDoc.setType("temp");
    myDoc.setStatus("open");
    myDoc.setArchiveDate("");
}


%>


<h3>DOCUMENT FORM</h3>
<p>This form is for the entry and mantanence of documents.</p>

<table border=0>
<tr><td>&nbsp;</td><td valign=top>Document&nbsp;Name/Number:&nbsp;&nbsp;&nbsp;</td><td>
<% if (command.equals("updatedocument")) { %>
<%= myDoc.getNumber() %><input type=hidden name=docnumber value='<%= myDoc.getNumber() %>'>
<% } else { %>
<input type=text name=docnumber size=20 maxlength=50 value='<%= myDoc.getNumber() %>'>
<% } %>
<input type=hidden name=docid size=20 maxlength=50 value='<%= myDoc.getID() %>'>
</td></tr>
<tr><td>&nbsp;</td><td valign=top>Description:&nbsp;&nbsp;&nbsp;</td><td><textarea name=title cols=50 rows=5><%= myDoc.getTitle() %></textarea></td></tr>
<tr><td>&nbsp;</td><td valign=top>Comments&nbsp;Due&nbsp;By:&nbsp;&nbsp;&nbsp;</td><td><input type=text name=duedate size=10 maxlength=10 value='<%= myDoc.getDueDate() %>'></td></tr>
<tr><td>&nbsp;</td><td valign=top>Type:&nbsp;&nbsp;&nbsp;</td><td><select name=type size=1>
<option value='0'>Please Select a Type</option>
<% String type = myDoc.getType(); %>
<option value='RA'<%= ((type.equals("RA")) ? " selected" : "") %>>Repository Activities</option>
<option value='NRP'<%= ((type.equals("NRP")) ? " selected" : "") %>>Nevada Rail Project</option>
</select></td></tr>
<tr><td>&nbsp;</td><td valign=top>Status:&nbsp;&nbsp;&nbsp;</td><td><select name=status size=1>
<% String status = myDoc.getStatus(); %>
<option value='open'<%= ((status.equals("open")) ? " selected" : "") %>>Open</option>
<option value='archived'<%= ((status.equals("archived")) ? " selected" : "") %>>Archived</option>
</select></td></tr>
<tr><td>&nbsp;</td><td valign=top>Document File:&nbsp;&nbsp;&nbsp;</td><td><input type=file name=docfile size=50>
<%= ((command.equals("updatedocument")) ? "<br>(Current is " + myDoc.getFilename() + ")" : "") %>
<input type = hidden name=olddocfile value='<%= ((command.equals("updatedocument")) ? "<br>(Current is " + myDoc.getFilename() : "none") %>'>
</td></tr>
<tr><td>&nbsp;</td><td valign=top>Archive Date:&nbsp;&nbsp;&nbsp;</td><td><input type=text name=archivedate size=10 maxlength=10 value='<%= myDoc.getArchiveDate() %>'></td></tr>
<tr><td>&nbsp;</td><td valign=top>Comment Response Document File:&nbsp;&nbsp;&nbsp;</td><td><input type=file name=crdfile size=50>
<%= ((command.equals("updatedocument") && myDoc.getArchiveFilename() != null && myDoc.getArchiveFilename().compareTo("   ") > 0) ? "<br>(Current is " + myDoc.getArchiveFilename() + ")" : "") %>
<input type = hidden name=oldcrdfile value='<%= ((command.equals("updatedocument") && myDoc.getArchiveFilename() != null && myDoc.getArchiveFilename().compareTo("   ") > 0) ? myDoc.getArchiveFilename() : "none") %>'>
</td></tr>

<tr><td>&nbsp;&nbsp;&nbsp;</td><td colspan=2 align=center>
<a href="javascript:verifySubmit(document.main)">Submit</a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
<a href="javascript:document.main.reset()">Clear Contents</a></td></tr>
<tr><td>&nbsp;&nbsp;&nbsp;</td><td colspan=2 align=center><br><br><br>Note: Large documents will take a long time to upload, please be patient.</td></tr>
</table>

<script language=javascript><!--

function verifySubmit (f){
// javascript form verification routine
    var msg = "";
    if (isblank(f.docid.value)) {
        msg += "Document Name/Number must be entered.\n";
    }
    if (isblank(f.title.value)) {
        msg += "Document Description must be entered.\n";
    }
    if (isblank(f.duedate.value)) {
        msg += "Comment Due By must be entered.\n";
    }
    if (isblank(f.docfile.value) && f.olddocfile.value=='none') {
        msg += "Document file must be attached.\n";
    }
    if (f.type[0].selected) {
        msg += "Document Type must be selected.\n";
    }
    if (f.status[1].selected && isblank(f.crdfile.value) && f.oldcrdfile.value=='none') {
        msg += "Response Document file must be attached.\n";
    }
    if (msg != "") {
      alert (msg);
    } else {
        submitFormResults('doDocument', '<%= command %>');
    }
}

//-->
</script>


<!-- End Main Content -->
<%@ include file="imageFrameStop.html" %>

</BODY>

</HTML>
