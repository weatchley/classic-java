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
String docnumb = request.getParameter("documentnumber");
String comnumb = request.getParameter("commentnumber");
Doc myDoc = new Doc(docnumb);
CmtRsp cmt = CmtRsp.commentTree(myDoc.getID(), Integer.parseInt(comnumb));
int maxLen = 200;
int subCommentCount = cmt.relatedComments.length;
String tmpText = "";
%>
<table border=0 cellpadding=0 cellspacing=0>
<tr><td valign=bottom>Document &nbsp; &nbsp; &nbsp;</td><td valign=bottom>Title</td><td valign=bottom align=center>Subcomments</td></tr>
<tr><td valign=top><%= myDoc.getNumber() %> &nbsp; &nbsp; &nbsp;</td><td valign=top><%= myDoc.getTitle() %></td><td valign=top align=center><%= subCommentCount %></td></tr>
</table>
Comment # "<%= cmt.getNumber() + "-" + cmt.getID() %>" from "<%= User.getFullName(cmt.getUser()) %>" of "<%= Org.getName(cmt.getOrg()) %>".<br>
Submitted on "<%= cmt.getDateSubmitted() %>". 
The current responder is "<%= ((cmt.getResponder() != 0) ? User.getFullName(cmt.getResponder()) : "n/a") %>".<br>
<%
String myComment = cmt.getComment();
//myComment = myComment.replaceAll("\n", "<br>\n");
//myComment = myComment.replaceAll("  ", " &nbsp;");
//int len = myComment.length();
//if (len > maxLen) {
//    myComment = myComment.substring(0,maxLen) + "<br> &nbsp; &nbsp; &nbsp; &nbsp; <i>(more...)</i>";
//}
%>
<%
tmpText = "";
tmpText += "" +
  "<table border=0  cellpadding=0 cellspacing=0>" +
  "<tr><td colspan=2>Comment:</td><tr>" +
  "<!--<tr><td> &nbsp; &nbsp; &nbsp; </td><td><table border=1 cellpadding=0 cellspacing=0 bgcolor=#ffffff><tr><td>" + myComment + "</td></tr></table></td></tr>-->" +
  "<tr><td> &nbsp; &nbsp; &nbsp; </td><td><textarea name=comment rows=8 cols=80 readonly>" + myComment + "</textarea></td></tr>" +
  "</table>";
%>
<%
String myResponse = cmt.getResponse();
%>
<%
tmpText += "" +
  "<table border=0  cellpadding=0 cellspacing=0>" +
  "<tr><td colspan=2>Response:</td><tr>" +
  "<tr><td> &nbsp; &nbsp; &nbsp; </td><td><textarea name=response rows=8 cols=80>" + ((cmt.getResponse() != null) ? myResponse : "") + "</textarea></td></tr>" +
  "</table>";
%>
<%= Utils.doSection("comResp", "Comment/Response Text", tmpText) %>
<input type=hidden name=docid value=<%= myDoc.getID() %>>
<input type=hidden name=commentid value=<%= cmt.getID() %>>

<!-- ******************** -->
<input type=hidden name=subcommentcount value=<%= subCommentCount %>>

<%  for (int i=0; i<subCommentCount; i++) { %>

<hr width=25% align=left>
<input type=hidden name=commentid<%= i %> value=<%= cmt.relatedComments[i].getID() %>>
<%
String title = "";
title = "" +
  "Comment # \"" + cmt.relatedComments[i].getNumber() + "-" + cmt.relatedComments[i].getID() + "\" from \"" + User.getFullName(cmt.relatedComments[i].getUser()) + "\" of \"" + Org.getName(cmt.relatedComments[i].getOrg()) + "\".<br>\n" +
  "Submitted on \"" + cmt.relatedComments[i].getDateSubmitted() + "\". \n" +
  "The current responder is \"" + ((cmt.relatedComments[i].getResponder() != 0) ? User.getFullName(cmt.relatedComments[i].getResponder()) : "n/a") + "\".<br>\n";
%>
<%
myComment = cmt.relatedComments[i].getComment();
%>
<%
tmpText = "";
tmpText += "" +
  "<table border=0  cellpadding=0 cellspacing=0>\n" +
  "<tr><td colspan=2>Comment:</td><tr>\n" +
  "<tr><td> &nbsp; &nbsp; &nbsp; </td><td><textarea name=comment" + i + " rows=8 cols=80>" + myComment + "</textarea></td></tr>\n" +
  "</table>";
%>
<%
myResponse = cmt.relatedComments[i].getResponse();
%>
<%
tmpText += "" +
  "<table border=0  cellpadding=0 cellspacing=0>\n" +
  "<tr><td colspan=2>Response:</td><tr>\n" +
  "<tr><td> &nbsp; &nbsp; &nbsp; </td><td><textarea name=response" + i + " rows=8 cols=80>" + ((cmt.relatedComments[i].getResponse() != null) ? myResponse : "") + "</textarea></td></tr>\n" +
  "</table>\n";
%>
<%= Utils.doSection("comResp" + i, title, tmpText) %>

<%  } %>

<table border=0 cellpadding=0 cellspacing=0><tr><td> &nbsp; &nbsp; &nbsp; </td><td id=newSubcommentListTableParent>
<table cellpadding=0 cellspacing=0 border=0 id=postSubcommentListTable width=100%>
<tr><td><a href="javascript:addSubComment()" >Add Sub Comment</a></td></tr>
</table>
</td></tr></table>

<script language=javascript><!--

function addSubComment() {
// add an entry to the subcomment table
    var cmts = document.main.subcommentcount.value;
    var cmts2 = cmts;
    cmts2++;
    document.main.subcommentcount.value = cmts2;
    var newSCRow = "";
    newSCRow += "<hr width=25% align=left>\n";
    newSCRow += "<input type=hidden name=commentid" + cmts + " value=0>\n";
    newSCRow += "Comment # \"New\" from \"<%= session.getAttribute("user.fullname") %>\".<br>\n";
    newSCRow += "Submitted \"Today\". ";
    newSCRow += "The current responder is \"<%= session.getAttribute("user.fullname") %>\".<br>\n";
    newSCRow += "<table border=0  cellpadding=0 cellspacing=0>\n";
    newSCRow += "<tr><td colspan=2>Comment:</td><tr>\n";
    newSCRow += "<tr><td> &nbsp; &nbsp; &nbsp; </td><td><textarea name=comment" + cmts + " rows=8 cols=80></textarea></td></tr>\n";
    newSCRow += "</table>\n";
    newSCRow += "<table border=0  cellpadding=0 cellspacing=0>\n";
    newSCRow += "<tr><td colspan=2>Response:</td><tr>\n";
    newSCRow += "<tr><td> &nbsp; &nbsp; &nbsp; </td><td><textarea name=response" + cmts + " rows=8 cols=80></textarea></td></tr>\n";
    newSCRow += "</table>\n";
    mySection = document.getElementById('postSubcommentListTable');
    
    //if( document.body.insertAdjacentHTML ) {
    //    mySection.insertAdjacentHTML("BeforeBegin", "" + newSCRow + "");
    //} else {
        //alert('Sorry, this function currently requires InternetExporer');
        myParent = document.getElementById('newSubcommentListTableParent');
        myDiv = document.createElement("DIV");
        myParent.insertBefore(myDiv, mySection);
        myDiv.innerHTML = newSCRow;
    //}
}

//--></script>

<p align=center><input type=button name=submitForm value='Submit' onClick="submitFormResults('doComment', 'updatecomment');"></p>

<script language=javascript><!--

//-->
</script>

<!-- End Main Content -->
<%@ include file="imageFrameStop.html" %>

</BODY>

</HTML>
