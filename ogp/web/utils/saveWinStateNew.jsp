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
   int userId = 0;
	if (request.getParameter("action").equals("retrieve")){ // retrieve option
		//***** page retrieve section *****//		
			String pageNum = request.getParameter("pageNum");
			try{
				int winNum  = Integer.parseInt(session.getAttribute("wCount"+pageNum).toString().trim());
				int pwCount  = Integer.parseInt(session.getAttribute("pwCount"+pageNum).toString().trim());
				int layId  = Integer.parseInt(session.getAttribute("layId"+pageNum).toString().trim());
			String lDesc = session.getAttribute("lDesc"+pageNum).toString();
			tString = "<pageInfo>";
			tString += "<layid>"+layId+"</layid>";
			tString += "<winnum>"+winNum+"</winnum>";
			tString += "<pwCount>"+pwCount+"</pwCount>";
			tString += "<lDesc>"+lDesc.toString().trim()+"</lDesc>";	
			for(int i=0;i<winNum;i++){
					tString += "<cn"+i+">"+session.getAttribute(pageNum+"cn"+i).toString().trim()+"</cn"+i+">"; 
					tString += "<cu"+i+">"+session.getAttribute(pageNum+"cu"+i).toString().trim()+"</cu"+i+">"; 
					tString += "<origw"+i+">"+session.getAttribute(pageNum+"origw"+i).toString().trim()+"</origw"+i+">"; 
					tString += "<origh"+i+">"+session.getAttribute(pageNum+"origh"+i).toString().trim()+"</origh"+i+">";
					tString += "<origx"+i+">"+session.getAttribute(pageNum+"origx"+i).toString().trim()+"</origx"+i+">";
					tString += "<origy"+i+">"+session.getAttribute(pageNum+"origy"+i).toString().trim()+"</origy"+i+">";
					tString += "<zIndex"+i+">"+session.getAttribute(pageNum+"zIndex"+i).toString().trim()+"</zIndex"+i+">";
					tString += "<state"+i+">"+session.getAttribute(pageNum+"state"+i).toString().trim()+"</state"+i+">";
					tString += "<bc"+i+">"+session.getAttribute(pageNum+"bc"+i).toString().trim()+"</bc"+i+">";
			}
				//for each each popup window
					for(int l=0;l<pwCount;l++){	
						int pWinNum  = Integer.parseInt(session.getAttribute("winnum"+pageNum+"_"+l).toString().trim());
						tString += "<winnum_"+l+">"+pWinNum+"</winnum_"+l+">";
						for(int k=0;k<pWinNum;k++){//for each window in main window
							try{
							tString += "<cn"+k+"_"+l+">"+session.getAttribute(pageNum+"cn"+k+"_"+l).toString().trim()+"</cn"+k+"_"+l+">"; 
							tString += "<cu"+k+"_"+l+">"+session.getAttribute(pageNum+"cu"+k+"_"+l).toString().trim()+"</cu"+k+"_"+l+">";
							tString += "<origw"+k+"_"+l+">"+session.getAttribute(pageNum+"origw"+k+"_"+l).toString().trim()+"</origw"+k+"_"+l+">"; 
							tString += "<origh"+k+"_"+l+">"+session.getAttribute(pageNum+"origh"+k+"_"+l).toString().trim()+"</origh"+k+"_"+l+">";
							tString += "<origx"+k+"_"+l+">"+session.getAttribute(pageNum+"origx"+k+"_"+l).toString().trim()+"</origx"+k+"_"+l+">";
							tString += "<origy"+k+"_"+l+">"+session.getAttribute(pageNum+"origy"+k+"_"+l).toString().trim()+"</origy"+k+"_"+l+">";
							tString += "<zIndex"+k+"_"+l+">"+session.getAttribute(pageNum+"zIndex"+k+"_"+l).toString().trim()+"</zIndex"+k+"_"+l+">";
							tString += "<state"+k+"_"+l+">"+session.getAttribute(pageNum+"state"+k+"_"+l).toString().trim()+"</state"+k+"_"+l+">";
							tString += "<bc"+k+"_"+l+">"+session.getAttribute(pageNum+"bc"+k+"_"+l).toString().trim()+"</bc"+k+"_"+l+">";
							}catch(Exception e){
							System.out.println(e);
							}
						}
					}	
			tString += "</pageInfo>";
			}catch(Exception e){
				System.out.println(e);
			}
	} else if (request.getParameter("action").equals("getPNames")) { // get Page Name Option
		tString = "<pageInfo>";
			int tPageCount = Integer.parseInt(request.getParameter("pCount"));
			tString += "<pagenum>"+tPageCount+"</pagenum>";
				for(int i=1;i<=tPageCount;i++){ 
					tString += "<lDesc"+i+">";
					tString += session.getAttribute("lDesc"+i).toString().trim();		
					tString += "</lDesc"+i+">";
					tString += "<lNum"+i+">";
					tString += Integer.parseInt(session.getAttribute("layId"+i).toString().trim());	
					tString += "</lNum"+i+">";
				}
		tString += "</pageInfo>";
	} else if (request.getParameter("action").equals("delete"))  {
		String lId = request.getParameter("lId");
		//***** page count section *****//
		String pCounter = (String)session.getAttribute("pCounter");
		int iCounter;
			if (pCounter == null){
				iCounter = 0;
			} else {
				iCounter = Integer.parseInt(pCounter);
				iCounter--;
			}
		pCounter = Integer.toString(iCounter);
		session.setAttribute("pCounter",pCounter);
		//*** DB Deletion ***//
		qString = "delete from LAYOUT where LAYOUT_ID = "+ lId;
		int cFlag = saveInDb(qString,"","");
			qString = "delete from WINDOW where LAYOUT_ID = "+ lId;
			saveInDb(qString,"","");
		tString = "<pageInfo>";
		if(cFlag > 0){
		tString += "<layId>"+lId+"</layId>";
		}else{
		tString += "<message>Error Occured While Processing DB</message>";
		}
		tString += "</pageInfo>";
	} else if (request.getParameter("action").equals("save"))  { // save option
		//***** page count section *****//
		String pCounter = (String)session.getAttribute("pCounter");
		int iCounter;
			if (pCounter == null){
				iCounter = 0;
			} else {
				iCounter = Integer.parseInt(pCounter);
			}
		iCounter++;
		pCounter = Integer.toString(iCounter);
		session.setAttribute("pCounter",pCounter);
		int wCount = Integer.parseInt(request.getParameter("winnum"));
		int pwCount = Integer.parseInt(request.getParameter("popnum"));
		String lDesc = request.getParameter("currentDT");
		//*** DB insertion (1st round: into LAYOUT) ***//
		qString = "insert into LAYOUT values (NEXT VALUE FOR LAYOUT_SEQ, '"+lDesc+"',"+wCount+","+pwCount+","+session.getAttribute("userId")+")";
		qString2 = "select LAYOUT_ID from LAYOUT where NAME = '" +lDesc+ "'";
		int layId = saveInDb(qString,qString2,"LAYOUT_ID");
		tString = "<pageInfo>";
		tString += "<pageNum>"+pCounter+"</pageNum>";
		tString += "<wCount>"+wCount+"</wCount>";
		session.setAttribute("wCount"+pCounter,Integer.toString(wCount));
		tString += "<pwCount>"+pwCount+"</pwCount>";
		session.setAttribute("pwCount"+pCounter,Integer.toString(pwCount));
		tString += "<lDesc>"+lDesc+"</lDesc>";
		session.setAttribute("lDesc"+pCounter,lDesc);
			for(int i=0;i<wCount;i++){
				//*** DB insertion (2nd round: into WINDOW) ***//
				qString = "insert into WINDOW values (NEXT VALUE FOR WINDOW_SEQ,";
				qString += "'"+request.getParameter("cn"+i)+"','"+request.getParameter("cu"+i)+"',";
				qString += request.getParameter("origw"+i)+","+request.getParameter("origh"+i)+",";
				qString += request.getParameter("origx"+i)+","+request.getParameter("origy"+i)+",";
				qString += request.getParameter("zIndex"+i)+",'"+request.getParameter("state"+i)+"','"; //maxFlag, minFlag
				qString += request.getParameter("bc"+i)+"'";
				qString += ","+layId;
				qString += ",-1)";
				saveInDb(qString,"","");
				//save the attributes in session
				session.setAttribute(pCounter+"cn"+i,request.getParameter("cn"+i));
				session.setAttribute(pCounter+"cu"+i,request.getParameter("cu"+i)); 
				session.setAttribute(pCounter+"origw"+i,request.getParameter("origw"+i));
				session.setAttribute(pCounter+"origh"+i,request.getParameter("origh"+i));
				session.setAttribute(pCounter+"origx"+i,request.getParameter("origx"+i));
				session.setAttribute(pCounter+"origy"+i,request.getParameter("origy"+i));
				session.setAttribute(pCounter+"zIndex"+i,request.getParameter("zIndex"+i));
				session.setAttribute(pCounter+"state"+i,request.getParameter("state"+i));
				session.setAttribute(pCounter+"bc"+i,request.getParameter("bc"+i));
				//publish attributes in XML
				tString += "<cn"+i+">"+session.getAttribute(pCounter+"cn"+i)+"</cn"+i+">"; 
				tString += "<cu"+i+">"+session.getAttribute(pCounter+"cu"+i)+"</cu"+i+">";
				tString += "<origw"+i+">"+session.getAttribute(pCounter+"origw"+i)+"</origw"+i+">"; 
				tString += "<origh"+i+">"+session.getAttribute(pCounter+"origh"+i)+"</origh"+i+">";
				tString += "<origx"+i+">"+session.getAttribute(pCounter+"origx"+i)+"</origx"+i+">";
				tString += "<origy"+i+">"+session.getAttribute(pCounter+"origy"+i)+"</origy"+i+">";
				tString += "<zIndex"+i+">"+session.getAttribute(pCounter+"zIndex"+i)+"</zIndex"+i+">";
				tString += "<state"+i+">"+session.getAttribute(pCounter+"state"+i)+"</state"+i+">";
				tString += "<bc"+i+">"+session.getAttribute(pCounter+"bc"+i)+"</bc"+i+">";
			}
			for(int j=0;j<pwCount;j++){ //do it for popup windows
				int pwwCount = Integer.parseInt(request.getParameter("winnum_"+j));
				//*** DB insertion (3rd round: into WINDOW) ***//
				session.setAttribute("winnum"+pCounter+"_"+j,Integer.toString(pwwCount));
				tString += "<winnum_"+j+">"+pwwCount+"</winnum_"+j+">";
				for(int k=0;k<pwwCount;k++){
					qString = "insert into WINDOW values (NEXT VALUE FOR WINDOW_SEQ,";
					qString += "'"+request.getParameter("cn"+k+"_"+j)+"','"+request.getParameter("cu"+k+"_"+j)+"',";
					qString += request.getParameter("origw"+k+"_"+j)+","+request.getParameter("origh"+k+"_"+j)+",";
					qString += request.getParameter("origx"+k+"_"+j)+","+request.getParameter("origy"+k+"_"+j)+",";
					qString += request.getParameter("zIndex"+k+"_"+j)+",'"+request.getParameter("state"+k+"_"+j)+"','"; //maxFlag, minFlag
					qString += request.getParameter("bc"+k+"_"+j)+"'";
					qString += ","+layId;
					qString += ","+j+")";
					saveInDb(qString,"","");
					//save the attributes in session
					session.setAttribute(pCounter+"cn"+k+"_"+j,request.getParameter("cn"+k+"_"+j));
					session.setAttribute(pCounter+"cu"+k+"_"+j,request.getParameter("cu"+k+"_"+j)); 
					session.setAttribute(pCounter+"origw"+k+"_"+j,request.getParameter("origw"+k+"_"+j));
					session.setAttribute(pCounter+"origh"+k+"_"+j,request.getParameter("origh"+k+"_"+j));
					session.setAttribute(pCounter+"origx"+k+"_"+j,request.getParameter("origx"+k+"_"+j));
					session.setAttribute(pCounter+"origy"+k+"_"+j,request.getParameter("origy"+k+"_"+j));
					session.setAttribute(pCounter+"zIndex"+k+"_"+j,request.getParameter("zIndex"+k+"_"+j));
					session.setAttribute(pCounter+"state"+k+"_"+j,request.getParameter("state"+k+"_"+j));
					session.setAttribute(pCounter+"bc"+k+"_"+j,request.getParameter("bc"+k+"_"+j));
					//publish attributes in XML
					tString += "<cn"+k+"_"+j+">"+session.getAttribute(pCounter+"cn"+k+"_"+j)+"</cn"+k+"_"+j+">"; 
					tString += "<cu"+k+"_"+j+">"+session.getAttribute(pCounter+"cu"+k+"_"+j)+"</cu"+k+"_"+j+">";
					tString += "<origw"+k+"_"+j+">"+session.getAttribute(pCounter+"origw"+k+"_"+j)+"</origw"+k+"_"+j+">"; 
					tString += "<origh"+k+"_"+j+">"+session.getAttribute(pCounter+"origh"+k+"_"+j)+"</origh"+k+"_"+j+">";
					tString += "<origx"+k+"_"+j+">"+session.getAttribute(pCounter+"origx"+k+"_"+j)+"</origx"+k+"_"+j+">";
					tString += "<origy"+k+"_"+j+">"+session.getAttribute(pCounter+"origy"+k+"_"+j)+"</origy"+k+"_"+j+">";
					tString += "<zIndex"+k+"_"+j+">"+session.getAttribute(pCounter+"zIndex"+k+"_"+j)+"</zIndex"+k+"_"+j+">";
					tString += "<state"+k+"_"+j+">"+session.getAttribute(pCounter+"state"+k+"_"+j)+"</state"+k+"_"+j+">";
					tString += "<bc"+k+"_"+j+">"+session.getAttribute(pCounter+"bc"+k+"_"+j)+"</bc"+k+"_"+j+">";
				}
			}
		tString += "</pageInfo>";
		//jax server-end for saving page action
		String tCount = pCounter;
			if (tCount != null) {
			    String tString2;
			    tString2 = "<paging>";
			    tString2 += "<tCount>" + tCount + "</tCount>";
			    tString2 += tString;
			    tString2 += "</paging>";
			    tString = tString2;
			} else {
			   //nothing to show
			   response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			}
} else if (request.getParameter("action").equals("refresh"))  { // session refresh option
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
						//System.out.println(wList.get(k).toString());
						String [] wTemp = wList.get(k).toString().split(",");
						//System.out.println(Integer.toString(j+1)+"cn"+k);
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
			session.setAttribute("userId",Integer.toString(userId));
			session.setAttribute("userName",userName);
			tString += "<userInfo>";
			tString += "<userId>"+session.getAttribute("userId")+"</userId>";
			tString += "<userName>"+userName+"</userName>";
			tString += "<winnum>"+session.getAttribute("pCounter")+"</winnum>";
			tString += "</userInfo>";
	} else if (request.getParameter("action").equals("login"))  { // login option
			String userName = request.getParameter("userName");
			qString = "select USER_ID from USER where USERNAME = '"+userName+"'";
			userId = userNameCheck(qString);	
			//System.out.println(userId);
			tString = "";
			if (userId==0) {
				qString = "insert into USER values (NEXT VALUE FOR USER_SEQ, '"+userName+"','')";
				qString2 = "select USER_ID from USER where USERNAME = '" +userName+ "'";
				userId = saveInDb(qString,qString2,"USER_ID");
			} else { //if user exists, look for saved layout information...
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
						//System.out.println(Integer.toString(j+1)+"cn"+k);
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
			}
			session.setAttribute("userId",Integer.toString(userId));
			session.setAttribute("userName",userName);
			tString += "<userInfo>";
			tString += "<userId>"+session.getAttribute("userId")+"</userId>";
			tString += "<userName>"+userName+"</userName>";
			tString += "<winnum>"+session.getAttribute("pCounter")+"</winnum>";
			tString += "</userInfo>";
	}
	//publish in XML
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("pragma","no-cache");	
	response.getWriter().write(tString);	
%>
<%!
//private methods...
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
		/*** JDBC connectivity ***/
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
private void saveAsXML(){
}
private void saveAsCookies(){
}
%>
