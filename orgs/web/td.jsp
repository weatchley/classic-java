<%@ include file="headerSetup.inc" %>
<% 
String pageHash = ((request.getParameter("pagehash")  != null) ? (String) request.getParameter("pagehash") : "OGP-PageContent-OD");

DbConn myConn = new DbConn("csi");
UHash pageContent = new UHash(pageHash, myConn);

TextItem tiPageTitle = new TextItem(pageContent.get("template"), myConn);
String template = tiPageTitle.getText();

//UList ulLeftNav = new UList(pageContent.get("leftnav"), myConn);
//String leftNav = ulLeftNav.getDescription();
String templatePage = "templates/" + template + ".jsp";
%>
<jsp:include page="<%= templatePage %>" >
    <jsp:param name="pagehash" value="<%= pageHash %>" />
</jsp:include>
