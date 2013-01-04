<HTML>

<HEAD>
<TITLE>Tribal Information Exchange</TITLE>
</HEAD>

<BODY BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>
<%@ include file="headerSetup.html" %>
<%@ include file="imageFrameStart.html" %>
<!-- Begin Main Content -->

<%
String command = request.getParameter("command");
String docnumb = request.getParameter("id");
Doc myDoc = new Doc(docnumb);
CmtRsp[] cmts = CmtRsp.commentList(myDoc.getID(), true);
int maxLen = 200;
String tmpText = "";
String tmpText2 = "";

%>
<table border=0  cellpadding=0 cellspacing=0>
<tr><td valign=bottom>Document</td><td> &nbsp; &nbsp; &nbsp;</td><td valign=bottom>Title</td><td valign=bottom align=center>Comments</td></tr>
<tr><td valign=top><%= myDoc.getNumber() %></td><td> &nbsp; &nbsp; &nbsp;</td><td valign=top><%= myDoc.getTitle() %></td><td valign=top align=center><%= cmts.length %></td></tr>
</table>
<%  for (int i=0; i<cmts.length; i++) { %>

<hr width=25% align=left>
<%
        int subCommentCount = ((cmts[i].relatedComments != null) ? cmts[i].relatedComments.length : 0);
%>
Comment # "<%= cmts[i].getNumber() + "-" + cmts[i].getID() %>" from "<%= User.getFullName(cmts[i].getUser()) %>" of "<%= Org.getName(cmts[i].getOrg()) %>".<br>
Submitted on "<%= cmts[i].getDateSubmitted() %>" 
<%= ((cmts[i].getRefersTo() != 0) ? "and its parent comment is #" + cmts[i].getRefersTo() : "") %>
<%= ((subCommentCount != 0) ? "and has " + subCommentCount + " subcomment" + ((subCommentCount == 1) ? "" : "s") : "") %>
.  The current responder is "<%= ((cmts[i].getResponder() != 0) ? User.getFullName(cmts[i].getResponder()) : "n/a") %>".<br>
<%
String myComment = cmts[i].getComment();
myComment = myComment.replaceAll("\n", "<br>\n");
myComment = myComment.replaceAll("  ", " &nbsp;");
int len = myComment.length();
if (len > maxLen) {
    myComment = Utils.colapseString("cm" + i, myComment, maxLen);
}
%>
<table border=0  cellpadding=0 cellspacing=0>
<tr><td colspan=2>Comment:</td><tr>
<tr><td> &nbsp; &nbsp; &nbsp; </td><td><%= myComment %></td></tr>
</table>
<%
String myResponse = cmts[i].getResponse();
if (myResponse != null) {
    myResponse = myResponse.replaceAll("\n", "<br>\n");
    myResponse = myResponse.replaceAll("  ", " &nbsp;");
    len = myResponse.length();
    if (len > maxLen) {
        myResponse = Utils.colapseString("rs" + i, myResponse, maxLen);
    }
}
%>
<table border=0  cellpadding=0 cellspacing=0>
<tr><td colspan=2>Response:</td><tr>
<tr><td> &nbsp; &nbsp; &nbsp; </td><td><%= ((cmts[i].getResponse() != null) ? myResponse : "n/a") %></td></tr>
</table>

<a href="javascript:processComment('<%= cmts[i].getID() %>')">Process Comment # <%= cmts[i].getNumber() %></a><br>

<%  } %>
<input type=hidden name=documentnumber value='<%= myDoc.getNumber() %>'>
<input type=hidden name=commentnumber value=0>

<script language=javascript><!--
   function processComment(id) {
       document.main.commentnumber.value = id;
       submitForm('updateComment.jsp','processcomments');
   }
//-->
</script>

<!-- End Main Content -->
<%@ include file="imageFrameStop.html" %>

</BODY>

</HTML>
