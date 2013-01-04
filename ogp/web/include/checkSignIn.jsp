<%
//System.out.println("checkSignIn.jsp called");
/*
Cookie[] cookies = request.getCookies();
        boolean foundCookie = false;
        String cUserID;
        String cUsername;
        for(int i = 0; i < cookies.length; i++) { 
            Cookie c = cookies[i];
            if (c.getName().equals("user.id")) {
            	cUserID = c.getValue();
                //System.out.println("user.id = " + cUserID);
                foundCookie = true;
            }
            if (c.getName().equals("user.name")) {
            	cUsername = c.getValue();
                //System.out.println("user.name = " + cUsername);
                foundCookie = true;
            }
        }  
		
        if (!foundCookie) {
        //    Cookie c = new Cookie("ogpLoginName", cUsername);
        //    c.setMaxAge(30*24*60*60);
        //    response.addCookie(c); 
        }
*/        
%>
<%
	Object fnameObj = (Object)session.getValue("user.fullname");
	if (fnameObj == null) {
		session.setAttribute("user.fullname","Guest");
	}
	String userFullName = session.getAttribute("user.fullname").toString();
	if (userFullName=="") {
		session.setAttribute("user.fullname","Guest");
		userFullName="Guest";
	}
		
	String uid = "0";
	Object idObj = (Object)session.getValue("user.id");
	if (idObj == null) {
	}else{
		uid= session.getAttribute("user.id").toString();
	}
	
	String uname = "Guest";
	Object nameObj = (Object)session.getValue("user.name");
	if (nameObj == null) {
	}else{
		uname= session.getAttribute("user.name").toString();
	}
%>