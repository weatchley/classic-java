<%@ page import="java.util.*" %>
<%@ page import="gov.ymp.csi.shuzi.*" %>
<%
String myLog = "";
String fName = getCurrentDate()+".txt";
if(request.getParameter("cContent")!=null){myLog = request.getParameter("cContent");}
FileStuff f = new FileStuff(); 
f.fWrite(myLog,fName,true);
//f.fWrite(myLog,"test.txt");
out.println(myLog);
%>
<%!
private String getCurrentDate(){
	String rDate = "";
	Calendar cal = new GregorianCalendar();
	int year = cal.get(Calendar.YEAR);             // 2002
	int month = cal.get(Calendar.MONTH);           // 0=Jan, 1=Feb, ...
	int day = cal.get(Calendar.DAY_OF_MONTH);      // 1...
	rDate = Integer.toString(month+1)+Integer.toString(day)+Integer.toString(year);
		//System.out.println ("out:"+rDate);
	return rDate;
}
%>