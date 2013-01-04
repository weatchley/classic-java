<%@page language="java" import="java.lang.*,java.io.*,java.util.*,javax.naming.*" %>
<%
//String origText = ""; 
String newText =  "";
Context initCtx = new InitialContext();
String pathToRoot = (String) initCtx.lookup("java:comp/env/PathToRoot");
String filePath = pathToRoot + "/documents/misc/";
//use path to root instead...

String fileName = "OPG-Announcement.html";
newText = request.getParameter("newText");
    try {
        BufferedWriter o = new BufferedWriter(new FileWriter(filePath+fileName));
        o.write(newText);
        o.close();
        out.println("homemessageEcho.jsp: file write successful...");
    } catch (IOException e) {
    }

/*
    try {
        BufferedReader in = new BufferedReader(new FileReader(filePath+fileName));
        	//BufferedReader in = new BufferedReader(new FileReader(filePath+"seed.txt"));
        String str;
        while ((str = in.readLine()) != null) {
            //process(str);
            //System.out.println(str);
        }
        in.close();
    } catch (IOException e) {
    }
*/

%>