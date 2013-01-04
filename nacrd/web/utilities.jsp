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
User usr = new User((String) session.getAttribute("user.name"));
%>

<h3>Utilities</h3>
<% if (isAuthenticated) { %>
  <% if (usr.hasAdmin()) { %>
         <a href="javascript:submitForm('docForm.jsp', 'adddocument')">Add Document</a><br>
         <a href="javascript:submitForm('userForm.jsp', 'adduser')">Add User</a><br>
         Update User: <select name=username><option value=0>Select user to update</option>
    <% 
       String[] users = User.getUserList();
       for (int i=0; i<users.length; i++) {
           %><option value=<%= users[i] %>><%= users[i] %></option> <%
       }
    %>
         </select> &nbsp; <input type=button value='Go' onClick=doUserUpdate()><br>
  <% } %>
         Report for document: <select name=docnumb><option value=0>Select document</option>
    <% 
       String[] docList = Doc.getDocList();
       for (int i=0; i<docList.length; i++) {
           %><option value='<%= docList[i] %>'><%= docList[i] %></option> <%
       }
    %>
         </select> &nbsp; Format: <input type=radio name=reporttype value='pdf'>PDF &nbsp; <input type=radio name=reporttype value='rtf' checked>RTF (Word)
          &nbsp; <input type=button value='Go' onClick=doDocReport()><br>
  <% if (usr.hasAdmin()) { %>
         <a href="javascript:submitForm('viewActivityLog.jsp', 'none')">View Activity Log</a><br>

  <% } %>
<% } %>

         <script language=javascript><!--

         function doUserUpdate (){
         // javascript 
             var msg = "";
             if (document.main.username[0].selected) {
                 msg += "Username must be selected.\n";
             }
             if (msg != "") {
               alert (msg);
             } else {
                 submitForm('userForm.jsp', 'updateuser');
             }
         }

         function doDocReport (){
         // javascript 
             var msg = "";
             if (document.main.docnumb[0].selected) {
                 msg += "Document number must be selected.\n";
             }
             if (msg != "") {
               alert (msg);
             } else {
                 var myDate = new Date();
                 var winName = myDate.getTime();
                 document.main.command.value = 'none';
                 document.main.action = 'simpleCommentRpt.jsp';
                 var winself = document.main.target;
                 document.main.target = winName;
                 var newwin = window.open('',winName);
                 newwin.creator = self;
                 document.main.submit();
                 document.main.target = winself;
             }
         }

         //-->
         </script>

<!-- End Main Content -->
<%@ include file="imageFrameStop.html" %>

</BODY>

</HTML>
