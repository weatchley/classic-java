<%@ page language="java" import="java.util.*"%>
<%
	String fontsize = "";
	String theme = "";
	String target = "";
	Cookie cookies [] = request.getCookies ();
	Cookie myCFontSize = null;
	Cookie myCTheme = null;
	Cookie myCTarget = null;
	
	if(request.getParameter("status")!=null){ //form submited
		fontsize = request.getParameter("fontsize");
		theme = request.getParameter("theme");
		target = request.getParameter("target");
		//write cookie
		Cookie cFontSize = new Cookie ("fontsize",fontsize);
		cFontSize.setMaxAge(365 * 24 * 60 * 60);
		response.addCookie(cFontSize);
		Cookie cTheme = new Cookie ("theme",theme);
		cTheme.setMaxAge(365 * 24 * 60 * 60);
		response.addCookie(cTheme);
		Cookie cTarget = new Cookie ("target",target);
		cTarget.setMaxAge(365 * 24 * 60 * 60);
		response.addCookie(cTarget);
		response.sendRedirect("./");
	}else{	
			//read cookie		
			if (cookies != null)
			{
				for (int i = 0; i < cookies.length; i++) 
				{
					if (cookies [i].getName().equals ("fontsize"))
					{
					myCFontSize = cookies[i];
					}
					 if(cookies [i].getName().equals ("theme")){
					myCTheme = cookies[i];
					}
					 if(cookies [i].getName().equals ("target")){
					myCTarget = cookies[i];
					}
				}
								
				if (myCFontSize != null) {
				fontsize = myCFontSize.getValue();
				}else{
				fontsize = "small";
				}
				
				if (myCTheme != null) {
				theme = myCTheme.getValue();
				}else{
				theme = "default";
				}	
				
				if (myCTarget != null) {
				target = myCTarget.getValue();
				}else{
				target = "_self";
				}
				
				if(request.getParameter("theme")!=null){
					fontsize = request.getParameter("fontsize");
					theme = request.getParameter("theme");
					target = request.getParameter("target");
				}
			}			
		}
%>
<html>
	<head>
		<title>Gateway Preferences</title>
		 <link rel="stylesheet" type="text/css" href="css/preferences.css"></link>
		 <script>
		 function formSubmit(){
		 	document.form.submit();
		 }
		 
		 function populateCheckBoxes(){
		 	for(i=0;i<document.form.fontsize.length;i++){
		 		if(document.form.fontsize[i].value=="<%=fontsize%>"){
		 			document.form.fontsize[i].checked=true;
		 		}
		 	}
		 	for(i=0;i<document.form.theme.length;i++){
		 		if(document.form.theme[i].value=="<%=theme%>"){
		 			document.form.theme[i].checked=true;
		 		}
		 	}
		 	for(i=0;i<document.form.target.length;i++){
		 		if(document.form.target[i].value=="<%=target%>"){
		 			document.form.target[i].checked=true;
		 		}
		 	}
		 }

		 	//alert('<%=theme%>');
		 
		 window.onload = init;
		
		function init(){
		populateCheckBoxes();
		}
		 </script>
	</head>
<body>
<center>
	<table width="75%" border=0 cellspan=25>
	<form name=form action="preferences.jsp" method=get>
	<tr><td>
		<h2>Gateway Preferences</h2>
	</td></tr>
	<tr><td>
		<p>
			<fieldset>
				<legend>Font Size</legend>
				<span style='font-size:8px;'><input type="radio" name="fontsize" value="x-small">&nbsp;x-small</span>
				<span style='font-size:10px;'><input type="radio" name="fontsize" value="small">&nbsp;small</span>
				<span style='font-size:12px;'><input type="radio" name="fontsize" value="default">&nbsp;default</span>
				<span style='font-size:14px;'><input type="radio" name="fontsize" value="large">&nbsp;large</span>
				<span style='font-size:16px;'><input type="radio" name="fontsize" value="x-large">&nbsp;x-large</span>
			</fieldset>
		</p>
	</td></tr>
	<tr><td>
		<p>
			<fieldset>
				<legend>Themes</legend>
				<table border=0 cellpadding=10 width=100%>
					<tr>
						<td><input type="radio" name="theme" value="default"><br><br>default<br><br><img alt="default" title="default" src="graphics/tpreview-d.gif"/></td>
						<td><input type="radio" name="theme" value="gray"><br><br>pastel&nbsp;-&nbsp;gray<br><br><img alt="gray" title="gray" src="graphics/tpreview-gra.gif"/></td>
						<td><input type="radio" name="theme" value="light"><br><br>pastel&nbsp;-&nbsp;green<br><br><img alt="green" title="green" src="graphics/tpreview-gre.gif"/></td>
						<td><input type="radio" name="theme" value="dark"><br><br>dark<br><br><img alt="dark" title="dark" src="graphics/tpreview-da.gif"/></td>
						<td><input type="radio" name="theme" value="508"><br><br>bright&nbsp;-&nbsp;red<br><br><img alt="red" title="red" src="graphics/tpreview-r.gif"/></td>
						<td><input type="radio" name="theme" value="508b"><br><br>bright&nbsp;-&nbsp;yellow<br><br><img alt="yellow" title="yellow" src="graphics/tpreview-y.gif"/></td>
					</tr>
				</table>
			</fieldset>
		</p>
	</td></tr>
	<tr><td>
		<p>
			<fieldset>
				<legend>Target</legend>
				<input type="radio" name="target" value="_self">&nbsp;Every link opens in the same browser&nbsp;&nbsp;
				<input type="radio" name="target" value="_blank">&nbsp;Every link opens in a new browser&nbsp;&nbsp;		
			</fieldset>
		</p>
	</td></tr>
	<tr><td align=right>
		<p><input class=btn type=button onclick="window.location = './'" value="Cancel">&nbsp;&nbsp;<input class=btn type=button onclick="formSubmit();" value="Save"></p>
	</td></tr>
	<input type=hidden name=status value=1>
	<form>
	</table>
</center>
<rdc:notProductionWarning size="150%" width="180" />
<body>
</html>