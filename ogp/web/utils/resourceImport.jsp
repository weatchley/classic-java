<%@ page import="java.util.*" %>
<%@ page import="com.documentum.fc.client.*" %>
<%@ page import="com.documentum.*" %>
<%@ page import="com.crypt.*" %>
<%@ page import="gov.ymp.csi.items.*" %>
<%@ page import="gov.ymp.csi.db.*"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%
//rebuildTree("09013c6c80007b6f", 1, dfSession, out);

//	out.println(printResources("00013c6c80001100", dfSession, out));
	String queryString = "SELECT lft, rgt FROM resource_entry WHERE resource_type = 'OCRWM' AND title = 'Top'";
	int depth = 1;
	int div = 2;
	int indexEnd;
	int heading = 0;
	boolean print = true;
	String hyperlink;
	String title;
	try {
		dmUtil dm = new dmUtil();
		Crypt crypt = new Crypt();
		String name = getServletConfig().getServletContext().getInitParameter("dm_name");
		String password = getServletConfig().getServletContext().getInitParameter("dm_access");
		String key = getServletConfig().getServletContext().getInitParameter("dm_key");
		String docbase = getServletConfig().getServletContext().getInitParameter("docbase");
		String alt;
		password = crypt.doDecrypt(password, key, 5);

		IDfSession dfSession = dm.getDfcSession(name, password, docbase);

		IDfCollection col;
		col = dm.execQuery(queryString, dfSession);
		col.next();

		// start with an empty right stack
		ArrayList right = new ArrayList();
		// stack for depth of hierachy
		Stack depthTest = new Stack();
		//out.println("SELECT lft, rgt FROM resource_entry WHERE r_object_id ='00013c6c80001100'");
		// now, retrieve all descendants of the root node
		queryString = "SELECT alt, title, hyperlink, lft, rgt FROM resource_entry WHERE lft BETWEEN " + col.getInt("lft") + " AND " + col.getInt("rgt") + " AND resource_type = 'OCRWM' ORDER BY lft ASC";
		//System.out.println(queryString);
		col.close();
		col = dm.execQuery(queryString, dfSession);
		%>
			<!--div style="padding-left: 15px"><a id="" href="javascript:expandAll()"><span style="font-size: 10pt; font-weight: bold" onmouseover="javascript:this.style.backgroundColor='lightblue'" onmouseout="javascript:this.style.backgroundColor='white'">Expand Links</span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:collapseAll()"><span style="font-size: 10pt; font-weight: bold" onmouseover="javascript:this.style.backgroundColor='lightblue'" onmouseout="javascript:this.style.backgroundColor='white'">Collapse Links</span></a></div><br-->
		<%
		
		
		
		// display each row
		while ( col.next() ) {
		   // only check stack if there is one
		   if (!right.isEmpty()) {
			   // check if we should remove a node from the stack
			   while (Integer.valueOf(right.get(right.size()-1).toString()).intValue() < col.getInt("rgt")) {
				   right.remove(0);
			   }
			}
			// Keep track of the depth we are at traversing the tree
			if (!depthTest.empty()) {
				while (col.getInt("rgt") > Integer.valueOf(depthTest.peek().toString()).intValue()) {
					depthTest.pop();
					depth--;
					if (print && div > 2) {
						%>
							</div>
						<%
						print = false;
					}
				}
			}
			hyperlink = col.getString("hyperlink");
			title = col.getString("title");
			//title = title.replaceAll("\?", "&rsquo;");
			alt = col.getString("alt");
			alt = (alt == null || alt.equals("")) ? title : alt;
			print = true;
			
			//initialize csi TextItem object...
			DbConn conn = new DbConn();
			TextItem ti = new TextItem();
									
			if (depth == 2) {
				%>
					<div style="font-weight:bold;"><%=title%></div>
				<%
				heading++;
			}
			else if (depth == 3 && (col.getInt("rgt") - col.getInt("lft") == 1)) {
				ti.setText(title);
				ti.setLink(hyperlink);
				ti.save(conn);
				%>
					<div class="levelA1" onClick="javascript:NewWindow('<%=hyperlink%>','win',800,750,'yes','yes');" title="<%=alt%>"><!--img style="margin-left: 6px" title="Add this resource to your bookmark portlet" onClick="javascript:bmAdd('<%=hyperlink%>','<%=title%>');cancel(event);" src="../../images/bookshelf.gif" alt="bookmark"-->&nbsp;<span class="clA0"><%=title%></span></div>
				<%
			}
			else if (depth == 3 && (col.getInt("rgt") - col.getInt("lft") > 1)) {
				int level = (heading % 6 > 0) ? heading % 6 : 6;
				%>
					<div style="font-style:italic;" class="levelA<%=level%>" onClick="changeState(this,'div<%=div%>');cancel(event);">&nbsp;<span class="clA0"><%=title%></span></div><div style="display: block" id="div<%=div%>">
				<%
				div++;
			}
			else if (depth == 4) {
				ti.setText(title);
				ti.setLink(hyperlink);
				ti.save(conn);
				indexEnd = (title.length() < 34) ? title.length() : 34;
				%>
					<div class="levelB" onClick="javascript:NewWindow('<%=hyperlink%>','win',800,750,'yes','yes');cancel(event);" title="<%=alt%>"><!--img style="margin-left: 6px" title="Add this resource to your bookmark portlet" onClick="javascript:bmAdd('<%=hyperlink%>','<%=title%>');cancel(event);" src="../../images/bookshelf.gif" alt="bookmark"-->&nbsp;&nbsp;&nbsp;<span class="clA1"><%=title.substring(0, indexEnd)%></span></div>
				<%
			}
			if (col.getInt("rgt") - col.getInt("lft") > 1) {
				depthTest.push(new Integer(col.getInt("rgt")));
				depth++;
			}
			right.add(0, col.getString("rgt"));
						
			conn.release();
		}
		col.close();
		dm.ditchDfcSession(dfSession);
	}
	catch (Throwable e) {
		e.printStackTrace();
	}
%>