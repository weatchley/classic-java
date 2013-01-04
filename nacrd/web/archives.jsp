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

%>


<h3>DOCUMENT ARCHIVE</h3>
<table border=1 cellspacing=0 cellpadding=4>
<tr bgcolor=#cccccc><td align=center><b>Doc&nbsp;Number</b></td><td align=center><b>Title</b></td><td align=center><b>Date&nbsp;Archive</b></td><td align=center><b>View&nbsp;Comment/Responses</b></td></tr>
<%
User usr = new User((String) session.getAttribute("user.name"));
count = 0;
for (int i = 0; i<docList.length; i++) {
    Doc myDoc = new Doc(docRoot, docList[i]);
    if (myDoc.getStatus().equals("archived")) {
        count++;
        String bgcolor = "#ffffff";
        if (count%2 == 0) {bgcolor = "#cccccc";}
%>
<tr bgcolor=<%= bgcolor %>>
  <td align=center valign=top><a href="javascript:displayDocument('<%= myDoc.getFilename() %>')"><%= myDoc.getNumber() %></a></td>
  <td align=left valign=top><%= myDoc.getTitle() %></td>
  <td align=center valign=top><%= myDoc.getArchiveDate() %></td>
  <td align=center valign=top><a href="javascript:displayDocument('<%= myDoc.getArchiveFilename() %>')">View</a>
  <% if (isAuthenticated) { %>
      <% if (usr.hasAdmin()) { %>
         <br><a href="javascript:updateDocument('<%= myDoc.getNumber() %>')">Update Document</a>
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
   function updateDocument(id) {
       document.main.id.value = id;
       submitForm('docForm.jsp','updatedocument');
   }
//-->
</script>


<!-- End Main Content -->
<%@ include file="imageFrameStop.html" %>

</BODY>

</HTML>
