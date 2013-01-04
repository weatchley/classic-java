<jsp:useBean id="LB" class="gov.ymp.opg.mypages.LayoutsBean" scope="session" />
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>	
<%@ page import="gov.ymp.csi.shuzi.*" %>
<%
   String tString = "";
   String qString = "";
   String qString2 = "";
   long personID = 0;
   int userId; //
   int layoutCtr = 0;
   long layoutID = 0;
   int winNum = 0;
   
   Object idObj = (Object)session.getValue("user.id");
	if (idObj == null) {
		session.setAttribute("user.id","0");
		personID =  (long)Integer.parseInt(request.getParameter("personID"));
	}else{
		personID = (long)Integer.parseInt(session.getAttribute("user.id").toString());
	}
	System.out.println("personID: "+personID);
	//System.out.println(request.getParameter("action"));
   if (request.getParameter("action").equals("login"))  { // login option
				//String userName = request.getParameter("userName");
			if (personID==0) {
			} else { //if user exists, look for saved layout information...
				tString += LB.login(personID);
			}
	} 
	else if (request.getParameter("action").equals("getPNames")) { // get Page Name Option
		tString += LB.getPNames(personID);	
	}
	else if (request.getParameter("action").equals("retrieve")){ // retrieve option
		//***** page retrieve section *****//		
			long pageNum = (long)Integer.parseInt(request.getParameter("option1"));
			tString += LB.retrieve(personID,pageNum);			
	} 
	else if (request.getParameter("action").equals("save"))  { // save option
		//new save code here 2/13/7 higashi
		layoutID = LB.saveLayout(request.getParameter("option1"),0,0,personID);
		winNum = Integer.parseInt(request.getParameter("winNum"));	
		boolean sts = true;
			for (int i=0;i<winNum;i++){
				sts = LB.saveWindow(request.getParameter("winTitle"+i),
									layoutID,
									request.getParameter("winUrl"+i),
									Integer.parseInt(request.getParameter("winWidth"+i)),
									Integer.parseInt(request.getParameter("winHeight"+i)),
									Integer.parseInt(request.getParameter("winLeft"+i)),
									Integer.parseInt(request.getParameter("winTop"+i)),
									Integer.parseInt(request.getParameter("winZ"+i)),
									request.getParameter("winState"+i),
									-1,
									"divWin0||");				
			}	
		if (sts) {tString = "<pageInfo><layid>"+layoutID+"</layid></pageInfo>";}
		else{tString = "<pageInfo><message>error has occurd while saving windows</message></page>";}
	}
	 else if (request.getParameter("action").equals("delete"))  {
	 // new delete code here - 2/7/7 higashi
	 
	 layoutID = (long)Integer.parseInt(request.getParameter("option1"));
	 tString = "<pageInfo><status>"+LB.layoutDelete(layoutID)+"</status></pageInfo>";
	 
	} 
	else if (request.getParameter("action").equals("refresh"))  { // session refresh option - this should be obsolete 2/14/7
	/*
			String userName = request.getParameter("userName");
			qString = "select USER_ID from USER where USERNAME = '"+userName+"'";
			userId = userNameCheck(qString);	
			tString = "";
				List lList = new ArrayList();
				lList = dbRetrieval("select * from LAYOUT where USER_ID = "+userId,"LAYOUT");
				String pCounter = Integer.toString(lList.size());
				session.setAttribute("pCounter",pCounter);
				for (int j=0;j<lList.size();j++){ //for each page
					String [] lTemp = lList.get(j).toString().split(",");
					session.setAttribute("pwCount"+(j+1),lTemp[3]);
					session.setAttribute("wCount"+(j+1),lTemp[2]);
					session.setAttribute("lDesc"+(j+1),lTemp[1]);
					session.setAttribute("layId"+(j+1),lTemp[0]);
					//for each window in main window
					List wList = dbRetrieval("select * from WINDOW where LAYOUT_ID = "+lTemp[0]+" and POP_ID = -1 ","WINDOW");
					for(int k=0;k<wList.size();k++){
						String [] wTemp = wList.get(k).toString().split(",");
						try{
							session.setAttribute(Integer.toString(j+1)+"cn"+k,wTemp[1]);
							session.setAttribute(Integer.toString(j+1)+"cu"+k,wTemp[2]); 
							session.setAttribute(Integer.toString(j+1)+"origw"+k,wTemp[3]);
							session.setAttribute(Integer.toString(j+1)+"origh"+k,wTemp[4]);
							session.setAttribute(Integer.toString(j+1)+"origx"+k,wTemp[5]);
							session.setAttribute(Integer.toString(j+1)+"origy"+k,wTemp[6]);
							session.setAttribute(Integer.toString(j+1)+"zIndex"+k,wTemp[7]);	
							session.setAttribute(Integer.toString(j+1)+"state"+k,wTemp[8]);
							session.setAttribute(Integer.toString(j+1)+"bc"+k,wTemp[9]);
						}catch(Exception e){
							System.out.println("ERROR: setting session attributes: " + e );
						}
					}
					//for each each popup window
					for(int l=0;l<Integer.parseInt(lTemp[3].trim());l++){
						List wList2 = dbRetrieval("select * from WINDOW where LAYOUT_ID = "+lTemp[0]+" and POP_ID = "+l,"WINDOW");
						session.setAttribute("winnum"+(j+1)+"_"+l,Integer.toString(wList2.size()));
						for(int k=0;k<wList2.size();k++){//for each window in main window
							String [] wTemp = wList2.get(k).toString().split(",");
							try{
								session.setAttribute(Integer.toString(j+1)+"cn"+k+"_"+l,wTemp[1]);
								session.setAttribute(Integer.toString(j+1)+"cu"+k+"_"+l,wTemp[2]); 
								session.setAttribute(Integer.toString(j+1)+"origw"+k+"_"+l,wTemp[3]);
								session.setAttribute(Integer.toString(j+1)+"origh"+k+"_"+l,wTemp[4]);
								session.setAttribute(Integer.toString(j+1)+"origx"+k+"_"+l,wTemp[5]);
								session.setAttribute(Integer.toString(j+1)+"origy"+k+"_"+l,wTemp[6]);
								session.setAttribute(Integer.toString(j+1)+"zIndex"+k+"_"+l,wTemp[7]);	
								session.setAttribute(Integer.toString(j+1)+"state"+k+"_"+l,wTemp[8]);
								session.setAttribute(Integer.toString(j+1)+"bc"+k+"_"+l,wTemp[9]);
							}catch(Exception e){
								System.out.println("ERROR: setting session attributes: " + e );
							}
						}
					}	
				}
			//session.setAttribute("userId",Integer.toString(userId));
			//session.setAttribute("userName",userName);
			tString += "<userInfo>";
			tString += "<userId>"+session.getAttribute("userId")+"</userId>";
			tString += "<userName>"+userName+"</userName>";
			tString += "<winnum>"+session.getAttribute("pCounter")+"</winnum>";
			tString += "</userInfo>";
	*/
	} 
	//publish in XML
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("pragma","no-cache");	
	response.getWriter().write(tString);	
%>
<%!
//private methods...
/*
private List dbRetrieval(String qString,String tblName){
	List rList = new ArrayList();
	try{
		DbConnH myConn = new DbConnH();
		Connection conn = myConn.conn;
		Statement stmt = conn.createStatement();
		ResultSet rs;
		rs = stmt.executeQuery(qString);
			while(rs.next()){
				if (tblName.equals("LAYOUT")){
					rList.add(rs.getInt("LAYOUT_ID")+", "+rs.getString("NAME")+", "+rs.getInt("WIN_COUNT")+", "+rs.getInt("POP_COUNT"));
				} else if (tblName.equals("WINDOW")){
					rList.add(rs.getInt("WINDOW_ID")+", "+rs.getString("NAME")+", "+rs.getString("URL")+", "+rs.getInt("WIDTH")+", "+rs.getInt("HEIGHT")+", "+rs.getInt("XAXIS")+", "+rs.getInt("YAXIS")+", "+rs.getInt("ZAXIS")+", "+rs.getString("STATE")+", "+rs.getString("BC"));
				}
			}
			rs.close();
			stmt.close();
			conn.close();
			myConn.release(); 
	}catch(SQLException e){
		System.out.println("ERROR: retriving data from a relational dB: " + e );
	}
	return rList;
}
private int userNameCheck(String qString){
	int rValue = 0;
	try{		
		DbConnH myConn = new DbConnH();
		Connection conn = myConn.conn;
		Statement stmt = conn.createStatement();
		ResultSet rs;
		rs = stmt.executeQuery(qString);
			while(rs.next()){		
			rValue = rs.getInt("USER_ID");
			break;
			}
		rs.close();
		stmt.close();
		conn.close();
		myConn.release(); 
	}catch (SQLException e){
		System.out.println("ERROR: updating session data into a relational dB: " + e );
	}
	return rValue; 
}
private int saveInDb(String qString,String qString2,String kName){
	int rValue = 0;
	try{
		DbConnH myConn = new DbConnH();
		Connection conn = myConn.conn;
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(qString);
		rValue = 1;
		if(qString2.length()>0){
			ResultSet rs = stmt.executeQuery(qString2);
			while(rs.next()){rValue = rs.getInt(kName);};
			rs.close();
		}
		stmt.close();
		conn.close();
		myConn.release(); 
	}catch (SQLException e){
		System.out.println("ERROR: updating session data into a relational dB: " + e );	
	}
	return rValue;
}
*/
%>
