<HTML>

<HEAD>
<TITLE>Tribal Information Exchange - Comment Report</TITLE>
</HEAD>

<BODY BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>
<%@ include file="headerSetup.html" %>
<!-- Begin Main Content -->

<%
String command = request.getParameter("command");
String docnumb = request.getParameter("docnumb");
Doc myDoc = new Doc(docnumb);
CmtRsp[] cmts = CmtRsp.commentList(myDoc.getID());

%>
Document Number: <%= myDoc.getNumber() %><br>
Document Title: <%= myDoc.getTitle() %><br>
Comment Count: <%= cmts.length %><br><br>
<%  for (int i=0; i<cmts.length; i++) { %>

<hr width=25% align=left>
Comment ID: <%= cmts[i].getID() %><br>
Comment Number: <%= cmts[i].getNumber() + "-" + cmts[i].getID() %><br>
Commentor: <%= User.getFullName(cmts[i].getUser()) %><br>
Org: <%= Org.getName(cmts[i].getOrg()) %><br>
<%
String referNumber = "";
if (cmts[i].getRefersTo() !=0) {
    CmtRsp myCmt = new CmtRsp(cmts[i].getRefersTo());
    referNumber = myCmt.getNumber();
}
%>
Refers To: <%= ((cmts[i].getRefersTo() != 0) ? Integer.toString(cmts[i].getRefersTo()) + " (" + referNumber + ")" : "n/a")%><br>
Date Submitted: <%= cmts[i].getDateSubmitted() %><br>
Responder: <%= ((cmts[i].getResponder() != 0) ? User.getFullName(cmts[i].getResponder()) : "n/a") %><br>
<%
String myComment = cmts[i].getComment();
myComment = myComment.replaceAll("\n", "<br>\n");
myComment = myComment.replaceAll("  ", " &nbsp;");
int len = myComment.length();
%>
Comment: <%=  " Length: " + len + ", Text: " + myComment %><br>
Response: <%= ((cmts[i].getResponse() != null) ? cmts[i].getResponse() : "n/a") %><br>

<%  } %>

<br>
<hr width=25% align=left>
Report Finished
<br>

<!-- End Main Content -->

</BODY>

</HTML>
