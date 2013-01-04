<HTML>

<HEAD>
<TITLE>Tribal Information Exchange</TITLE>
</HEAD>

<BODY BGCOLOR=#DEC68B background=images/background.png leftmargin=0 topmargin=0>
<%@ include file="headerSetup.html" %>
<%@ include file="imageFrameStart.html" %>
<!-- Begin Main Content -->

<%
String docRoot = getServletContext().getRealPath("/");
String[] docList = Doc.getDocList(docRoot);
int count = 0;
String dueDate = null;
String testDate = null;

%>
<h3>CURRENT DOCUMENTS</h3>
<h4>REPOSITORY ACTIVITIES - <a href=ProgrammaticAgreement.jsp>PROGRAMMATIC AGREEMENT</a></h4>
<table border=1 cellspacing=0 cellpadding=4>
<tr bgcolor=#cccccc><td align=center><b>Doc&nbsp;Number</b></td><td align=center><b>Title</b></td><td align=center><b>Comments&nbsp;Due</b></td><td align=center><b>Submit&nbsp;Comments</b></td></tr>
<%
User usr = new User((String) session.getAttribute("user.name"));
count = 0;
for (int i = 0; i<docList.length; i++) {
    Doc myDoc = new Doc(docRoot, docList[i]);
    if (myDoc.getStatus().equals("open") && myDoc.getType().equals("RA")) {
        count++;
        String bgcolor = "#ffffff";
        if (count%2 == 0) {bgcolor = "#cccccc";}
%>
<tr bgcolor=<%= bgcolor %>>
  <td align=center valign=top><a href="javascript:displayDocument('<%= myDoc.getFilename() %>')"><%= myDoc.getNumber() %></a></td>
  <td align=left valign=top><%= myDoc.getTitle() %></td>
  <td align=center valign=top><%= myDoc.getDueDate() %></td>
  <td align=center valign=top>
  <% Date now = new Date();
     dueDate = Utils.dateToCompareString(Utils.addDays(myDoc.getDueDateAsDate(),1));
     testDate = Utils.dateToCompareString(now);
     if (isAuthenticated) {
         if (dueDate.compareTo(testDate) > 0) {
             if (usr.canComment()) { %>
                 <a href="javascript:submitComment('<%= myDoc.getNumber() %>')">Submit Comments</a>
          <% } else { %>
              &nbsp;
          <% } %>
      <% } else { %>
              Closed for Comment
      <% } %>
  <% } %>
  <% if (isAuthenticated) { %>
      <% if (usr.hasAdmin()) { %>
         <br><a href="javascript:updateDocument('<%= myDoc.getNumber() %>')">Update Document</a>
         <br><a href="javascript:processComments('<%= myDoc.getNumber() %>')">Process Comments</a>
      <% } %>
  <% } %>
   </td>
</tr>
<%
    } 
}
%>
</table>
<br>
<h4>NEVADA RAIL PROJECT - <a href=ProgrammaticAgreement.jsp>PROGRAMMATIC AGREEMENT</a></h4>
<table border=1 cellspacing=0 cellpadding=4>
<tr bgcolor=#cccccc><td align=center><b>Doc&nbsp;Number</b></td><td align=center><b>Title</b></td><td align=center><b>Comments&nbsp;Due</b></td><td align=center><b>Submit&nbsp;Comments</b></td></tr>
<%
count = 0;
for (int i = 0; i<docList.length; i++) {
    Doc myDoc = new Doc(docRoot, docList[i]);
    if (myDoc.getStatus().equals("open") && myDoc.getType().equals("NRP")) {
        count++;
        String bgcolor = "#ffffff";
        if (count%2 == 0) {bgcolor = "#cccccc";}
%>
<tr bgcolor=<%= bgcolor %>>
  <td align=center valign=top><a href="javascript:displayDocument('<%= myDoc.getFilename() %>')"><%= myDoc.getNumber() %></a></td>
  <td align=left valign=top><%= myDoc.getTitle() %></td>
  <td align=center valign=top><%= myDoc.getDueDate() %></td>
  <td align=center valign=top>
  <% Date now = new Date();
     dueDate = Utils.dateToCompareString(Utils.addDays(myDoc.getDueDateAsDate(),1));
     testDate = Utils.dateToCompareString(now);
     if (isAuthenticated) {
         if (dueDate.compareTo(testDate) > 0) {
             if (usr.canComment()) { %>
                 <a href="javascript:submitComment('<%= myDoc.getNumber() %>')">Submit Comments</a>
          <% } else { %>
              &nbsp;
          <% } %>
      <% } else { %>
              Closed for Comment
      <% } %>
  <% } %>
  <% if (isAuthenticated) { %>
      <% if (usr.hasAdmin()) { %>
             <br><a href="javascript:updateDocument('<%= myDoc.getNumber() %>')">Update Document</a>
             <br><a href="javascript:processComments('<%= myDoc.getNumber() %>')">Process Comments</a>
      <% } %>
  <% } %>
  </td>
</tr>
<%
    } 
}
%>
</table>


<script language=javascript><!--
   function submitComment(id) {
       document.main.id.value = id;
       submitForm('commentform.jsp','none');
   }

   function updateDocument(id) {
       document.main.id.value = id;
       submitForm('docForm.jsp','updatedocument');
   }

   function processComments(id) {
       document.main.id.value = id;
       submitForm('selectComment.jsp','processcomments');
   }
//-->
</script>


<!-- End Main Content -->
<%@ include file="imageFrameStop.html" %>

</BODY>

</HTML>
