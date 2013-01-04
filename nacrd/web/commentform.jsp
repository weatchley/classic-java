<HTML>

<HEAD>
<TITLE>Tribal Information Exchange</TITLE>
</HEAD>

<BODY BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>
<%@ include file="headerSetup.html" %>
<%@ include file="imageFrameStart.html" %>
<!-- Begin Main Content -->
<%
String id = request.getParameter("id");
//String docRoot = getServletContext().getRealPath("/");
Doc myDoc = new Doc(id);


%>


<h3>COMMENT FORM</h3>
<p>Documents are available for your review and comment for 30 days from initial posting.  All comments will be
considered, responded to, and changes incorporated into the document, as appropriate.  When final,
comment responses can be viewed from the document archive portion of this website.</p>

<table border=0>
<tr><td>&nbsp;</td><td valign=top>Document&nbsp;Name:&nbsp;&nbsp;&nbsp;</td><td><%= myDoc.getNumber() %></td></tr>
<tr><td>&nbsp;</td><td valign=top>Description:&nbsp;&nbsp;&nbsp;</td><td><%= myDoc.getTitle() %></td></tr>
<tr><td>&nbsp;</td><td valign=top>Comments&nbsp;Due&nbsp;By:&nbsp;&nbsp;&nbsp;</td><td><%= myDoc.getDueDate() %></td></tr>
<tr><td colspan=3> &nbsp; </td></tr>
<tr><td valign=top>Comment:&nbsp;&nbsp;&nbsp;</td><td colspan=2><textarea name=comment cols=70 rows=15></textarea></td></tr>
<input type=hidden name=docid value='<%= myDoc.getID() %>'>
<input type=hidden name=docnumber value='<%= id %>'>
<tr><td>&nbsp;&nbsp;&nbsp;</td><td colspan=2 align=center>
<a href="javascript:submitForm('doDocument','addcomment')">Submit Comment</a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
<a href="javascript:document.main.reset()">Clear Contents</a></td></tr>
</table>


<!-- End Main Content -->
<%@ include file="imageFrameStop.html" %>

</BODY>

</HTML>
