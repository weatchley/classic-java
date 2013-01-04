<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="java.lang.*,java.io.*,java.util.*,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,org.w3c.dom.*" %>
<%@ page import="org.xml.sax.SAXException,org.xml.sax.SAXParseException,javax.naming.*,javax.naming.*,javax.net.ssl.*,java.net.*" %>
<%@ page import="gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,gov.ymp.csi.systems.*" %>
<%@ page import="gov.ymp.csi.auth.*,gov.ymp.csi.items.*" %>
<%@ taglib uri="gov.ymp.rdc.tags" prefix="rdc" %>
<rdc:sessionTest />

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
Context initCtx = new InitialContext();
Position pos = null;
HashMap perMap = null;
String ProductionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
String tempS = ((session.getAttribute("user.id")  != null) ? (String) session.getAttribute("user.id") : "0");
long userID = ((session.getAttribute("user.id")  != null) ? Long.parseLong((String) session.getAttribute("user.id")) : 0);
String urlS = ((request.getParameter("cacheurl")  != null) ? (String) request.getParameter("cacheurl") : "n/a");
String fileWithPath = ((request.getParameter("cachefilewithpath")  != null) ? (String) request.getParameter("cachefilewithpath") : "n/a");
String cacheContent = ((request.getParameter("cachecontent")  != null) ? (String) request.getParameter("cachecontent") : "n/a");
String doSecondCache = ((request.getParameter("dosecondcache")  != null) ? (String) request.getParameter("dosecondcache") : "F");
String secondCacheFile = ((request.getParameter("secondcachefile")  != null) ? (String) request.getParameter("secondcachefile") : "n/a");

DbConn myConn = new DbConn("csi");

if (userID != 0) {
    pos = new Position(myConn, Long.parseLong((String) session.getAttribute("user.positionid")));
    perMap = (HashMap) session.getAttribute("user.permissionmap");
}

myConn.release();

%>
<HTML>

<HEAD>
<TITLE>Update Cache</TITLE>
</HEAD>
<BODY leftmargin=0 topmargin=0 style="background-color : #ffffff;">
<form name='main' METHOD='POST' action=>

<input type=hidden name=id value=''>
<input type=hidden name=cacheurl value='<%= urlS %>'>
<input type=hidden name=cachefilewithpath value='<%= fileWithPath %>'>
<input type=hidden name=dosecondcache value='<%= doSecondCache %>'>
<input type=hidden name=secondcachefile value='<%= secondCacheFile %>'>
<input type=hidden name=cachecontent value=''>

<script type="text/javascript">
var xmlhttp

function loadXMLDoc(url)
{
xmlhttp=null
// code for Mozilla, etc.
if (window.XMLHttpRequest)
  {
  xmlhttp=new XMLHttpRequest()
  }
// code for IE
else if (window.ActiveXObject)
  {
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP")
  }
if (xmlhttp!=null)
  {
  xmlhttp.onreadystatechange=state_Change
  xmlhttp.open("GET",url,true)
  xmlhttp.send(null)
  }
else
  {
  alert("Your browser does not support XMLHTTP.")
  }
}

function state_Change()
{
// if xmlhttp shows "loaded"
if (xmlhttp.readyState==4)
  {
  // if "OK"
  if (xmlhttp.status==200)
    {
    // ...some code here...
        var content = xmlhttp.responseText;
        storeCache(content);
    }
  else
    {
    alert("Problem retrieving XML data")
    }
  }
}
</script>

<script language=javascript><!--
<% 
    if (pos != null && pos.belongsTo(((Long) perMap.get("3-updatehtmlcache")).longValue())) {
        %>
            loadXMLDoc('<%= (urlS + "?buildcache=true") %>');
        <%
    } else {
        %>
            alert('You do not have the rights to update the cache');
        <%
    }
%>
   
   function storeCache(content) {
       document.main.action = 'cacheStore.jsp';
       document.main.cachecontent.value = content;
       document.main.submit();
   }


    //-->
</script>



</form>

</BODY>

</HTML>
