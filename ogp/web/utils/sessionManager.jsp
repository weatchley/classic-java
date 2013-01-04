<%@ page import="java.lang.*,java.io.*,java.util.*,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,org.w3c.dom.*" %>
<%@ page import="org.xml.sax.SAXException,org.xml.sax.SAXParseException,javax.naming.*,javax.naming.*" %>
<%@ page import="gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,gov.ymp.csi.systems.*" %>
<%@ page import="gov.ymp.csi.auth.*,gov.ymp.csi.items.*" %>
<%
	//System.out.println("sessionManager.jsp called");
	String action = "";
	String uid = "0";
	Object idObj = (Object)session.getValue("user.id");
	if (idObj == null) {
	}else{
		uid= session.getAttribute("user.id").toString();
	}
	if (request.getParameter("action")!=null) action = request.getParameter("action");
		if(action.equals("getUID")){
			out.println(uid);
		}else if (action.equals("titlemessage")){
			Position pos = null;
			HashMap perMap = null;
			DbConn myConn = null;
			myConn = new DbConn("csi");
		    pos = new Position(myConn, Long.parseLong((String) session.getAttribute("user.positionid")));
		    perMap = (HashMap) session.getAttribute("user.permissionmap");
		    if ((!uid.equals("0")) && !((String) session.getAttribute("user.fullname")).toLowerCase().equals("guest")) {
		        if (pos != null && pos.belongsTo(((Long) perMap.get("3-titlemessage")).longValue())) {
		        	out.println(1);
		        }else{
		        	out.println(0);
		        }
		    }
		}else if (action.equals("portletEdit")){
			Position pos = null;
			HashMap perMap = null;
			DbConn myConn = null;
			myConn = new DbConn("csi");
		    pos = new Position(myConn, Long.parseLong((String) session.getAttribute("user.positionid")));
		    perMap = (HashMap) session.getAttribute("user.permissionmap");
		    if ((!uid.equals("0")) && !((String) session.getAttribute("user.fullname")).toLowerCase().equals("guest")) {
		       // if (pos != null && pos.belongsTo(((Long) perMap.get("3-titlemessage")).longValue())) {
		        if (pos != null && pos.belongsTo(((Long) perMap.get("3-editportlet")).longValue())) {
		        	out.println(1);
		        }else{
		        	out.println(0);
		        }
		    }
		}
%>