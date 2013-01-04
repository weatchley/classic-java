<%@ page import="gov.ymp.csi.db.*,gov.ymp.iats.model.*,java.sql.*,java.util.*,java.io.*,java.text.SimpleDateFormat" %>
<%@ include file="headerSetup.inc" %>
<HTML>
<HEAD>
<TITLE><%= SystemName %>: Assessment <rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
  <script type="text/javascript" src="javascript/utils.js"></script>
  <script type="text/javascript" src="javascript/ajax/prototype.js"></script>  
  <rdc:isAuthenticated doOpposite="true" >
	  <script>
	  window.onload = init;
	  function init(){
		  for (var i=0; i < document.forms[1].elements.length; i++) {
			   var element = document.forms[1].elements[i];
			   element.disabled = true;
		  }
	  }
	  </script>
  </rdc:isAuthenticated>
  <script>
  function loaddropdown(type){		
		var parameters = "type="+type;
		var url="./loaddropdown.jsp?"+parameters;
		var ajaxRequest = new Ajax.Request(url,
	              {
	                     method: 'get',
	                     parameters: parameters,
	                     onLoading: function() {
	            		 },
	                     onSuccess: function(transport){
	      				 var response = transport.responseText || "no response text";
						 //alert(response);
						 var jsobj = {};
						 jsobj = eval(transport.responseText);
 						 if(jsobj!=null){
 							 for(i=0;i<dobj.dropdown.length;i++){ 							 
	 							var option = document.createElement("option"); 
	 			      			var text = document.createTextNode(dobj.dropdown[i].tvalue);
	 			      			option.appendChild(text);
	 			      			option.setAttribute("value",dobj.dropdown[i].nvalue);
	 			      			var obj = document.getElementById(type);	
	 			      			obj.appendChild(option);
 							 }
 	 	 	 			 }
						 	
		    			 },
	    				 onFailure: function(){ alert('callAjax: Something went wrong...') }
	              });
  }

  function verifyForm(formObj,actiontype){
	alert("verifyForm: "+actiontype);
  }
  </script>
</HEAD>
<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
    DbConn myConn = new DbConn("csi");
	String type = request.getParameter("type");
	String action = request.getParameter("action");
	
	boolean disabled = false;
	
	if (action.equals("view")){
		disabled = true;
	}
	String id = "0";
	if(request.getParameterMap().containsKey("id")){
	id = request.getParameter("id");
	}
	String docRoot = getServletContext().getRealPath("/");
	usr = new Person((String) session.getAttribute("user.name"));
	
	SelfAssessment sa = new SelfAssessment();
	sa.lookup(Integer.parseInt(id),myConn);
	
	String sequenceid = Integer.toString(sa.getSequenceid());
	String title = sa.getTitle();
	String purpose = sa.getPurpose();
	String cancelledrationale = sa.getCancelledrationale();
	String comments = sa.getComments();
	
	String sanumber = "";
	int orgid, divid;
	
	orgid = sa.getOrgid();
	divid = sa.getDivid();
	if(request.getParameterMap().containsKey("id")){
	sanumber = sa.getSANumber(orgid,divid,myConn);
	}
	//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	
	String scheduleddate = "";    
	String rescheduleddate = "";
	String signeddate = "";
	String cancelleddate = "";
	
	if(sa.getScheduleddate()!=null)	scheduleddate = formatter.format(sa.getScheduleddate());    
	if(sa.getRescheduleddate()!=null)	rescheduleddate = formatter.format(sa.getRescheduleddate());
	if(sa.getSigneddate()!=null)	signeddate = formatter.format(sa.getSigneddate());
	if(sa.getCancelleddate()!=null)	cancelleddate = formatter.format(sa.getCancelleddate());
%>
<!--span style="font: bold 10pt">Assessments by Organization</span-->
<table width=100% border=0>
<tr>
<td>
<!-- form content -->
<table border=0 width=50% align="center" cellspacing="2" cellpadding="2">
<form name="assessmentForm">
<!--tr><td colspan=4><span  id=pdfpane1  style="font: bold 10pt;visibility:hidden;">Add Assessment PDF File:&nbsp;&nbsp;<input type=file name=pdf></span></td></tr>
<tr><td colspan=4><span id=pdfpane2 style="font: bold 10pt"><a href="javascript:openWindow('assessment','displaypdf','377');" title="Click to view Assessment file">View Assessment Plan</a> (OEA-2009-01 Plan.pdf)&nbsp;&nbsp;&nbsp;&nbsp;</span></td></tr>
<tr><td colspan=4><span  id=pdfpane3  style="font: bold 10pt;visibility:hidden;">Add Assessment PDF File:&nbsp;&nbsp;<input type=file name=pdf2></span></td></tr>
<tr><td colspan=4><span id=pdfpane4 style="font: bold 10pt;display:block;"><a href="javascript:openWindow('assessment','displaypdf2','377');" title="Click to view Assessment file">View Assessment Report</a>  (OD-2009-01 Report.pdf)&nbsp;&nbsp;&nbsp;&nbsp;</span></td></tr-->
<tr><td colspan=4 style="font: bold 10pt"><%=sanumber%></td></tr>
<tr><td colspan=4 style="font: 10pt">PDF Attachments</td></tr>
<tr>
<td><span style="font: bold 10pt">Assessment&nbsp;Type:</span></td>
<td><select name="asstype">
<option value=N/A>N/A
<option value=Focused>Focused
<option value=Limited+Scope>On Going
</select>
</td>
<td><!--span style="font: bold 10pt">Assessment&nbsp;Objectives:</span--></td>
<td></td>
</tr>
<tr>
<td><span style="font: bold 10pt">Organization:</span></td>
<td colspan=3><select name="porg" onChange="selectOrganization(this,document.assessment.org);" >
<option value=0>< Select Organization >
<option value=2>Lead Lab
<option value=3>M&O
<option value=1>OCRWM
</select>
</td>
</tr>
<tr>
<td><span style="font: bold 10pt">Office:</span></td>
<td colspan=3><select name="org" onChange="selectDivision(this,document.assessment.div);" >
<option value=0>< Select Office >
<option value=41>Business Operations
<option value=42>Business System
<option value=34>Construction Management & Site Operations Office
<option value=70>Crirticality and Operations Support
<option value=64>Chief Scientist Office
<option value=65>Disruptive Events Performance Confirmation
<option value=31>Disposal Operations Office
<option value=43>Employee Concerns
<option value=66>Engineered System Integration
<option value=67>IT Systems & Software Engineering
<option value=68>Lower Barrier Systems
<option value=69>Licensing Interactions Office
<option value=32>Managment Review Committee
<option value=40>Office of Business Management
<option value=18>Office of Chief Engineer
<option value=22>Office of the Chief Scientist
<option value=38>Office of Construction and Site Management
<option value=1>Office of the Director
<option value=6>Office of the Deputy Director
<option value=30>Office of External Affairs
<option value=29>Office of Government Services
<option value=27>Office of Logistics Management 
<option value=37>Office of Operations Management
<option value=39>Office of Project Management
<option value=35>Office of Project Management & Procurement
<option value=10>Office of Quality Assurance
<option value=36>Office of Technical Management
<option value=23>Regulatory Authority Office
<option value=26>Waste Management Office
</select>
</td>
</tr>
<tr>
<td><span style="font: bold 10pt">Divison:</span></td>
<td colspan=3><select name="div" onFocus="verifyOrg(document.assessment.org);">
<option value=0>< Select Division >
</select>
</td>
</tr>
<tr>
<td><span style="font: bold 10pt">Year:</span></td>
<td><select name="year">
<option value=0>< Select Year >
<option value=2012>2012
<option value=2011>2011
<option value=2010>2010
<option value=2009>2009
<option value=2008>2008
<option value=2007>2007
<option value=2006>2006
<option value=2005>2005
<option value=2004>2004
<option value=2003>2003
<option value=2002>2002
</select>
</td>
<td colspan=2><span style="font: bold 10pt">Number:</span>&nbsp;&nbsp;&nbsp;&nbsp;<select name="type" >
<option value=0> 
<option value=S>S
<option value=I>I
<option value=M>M
</select>
<input type=text name=number size=3 maxlength=3 value="01"></td>
</tr>
<tr>
<td><span style="font: bold 10pt">Title:</span></td>
<td colspan=3><textarea name="satitle" cols=59 rows=2><%=title%></textarea></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt">Purpose:</span><br><textarea name="scope" cols=70 rows=5><%=purpose%></textarea></td>
</tr>
<tr>
<td><span style="font: bold 10pt">OCRWM Team Lead:&nbsp;</span></td>
<td><select name="teamlead" onChange="verifyTeamLead(this,document.assessment.supportteamlead);">
<option value=0>< Select Team Lead>
<option value=46>Albert Williams
<option value=1>Alesia Boone
<option value=118>Alma Romero
<option value=190>Barry Mellor
<option value=111>Bertha Terrell
<option value=26>Bill Boyle
<option value=54>Bill Tunnell
<option value=177>Bill Dam
<option value=191>Bob Clark
<option value=64>Bob Lupton
<option value=83>Bob White
<option value=179>Candice Trummell
<option value=169>Carol Hanlon
<option value=154>Charlotte Zaccone
<option value=7>Chris Payne
<option value=126>Christopher A. White
<option value=176>Claire Sinclair
<option value=86>Corinne Macaluso
<option value=180>Daniel Burke
<option value=171>David Hathcock
<option value=100>David Howell
<option value=40>David Warriner
<option value=55>David Haught
<option value=48>Dean Stucker
<option value=58>Debbie Brooks
<option value=60>Deborah Urban
<option value=187>DiGiovanni Anthony 
<option value=9>Diane Ridolfi
<option value=161>Diane Vigue
<option value=124>Dick Spence
<option value=132>Drew H. Coleman
<option value=10>Emily Cooper
<option value=116>Eric Lundgaard
<option value=89>Ethel Herring
<option value=11>Fran Young
<option value=156>Frank Moussa
<option value=189>Gallagher Jack 
<option value=53>Garald Smith
<option value=143>Gayle Fisher
<option value=85>Glenn Gardner
<option value=59>Harry Leake
<option value=129>Harry C. White Jr.
<option value=131>J.C. De La Garza
<option value=73>Jack Gallagher
<option value=153>Jackie Chestnut
<option value=103>Jaime Gonzalez
<option value=14>Jake Wooley
<option value=194>James Blaylock
<option value=146>James Hollrith
<option value=162>Jay Thompson
<option value=157>Jeff Sciscilo
<option value=178>Jeffrey Walker
<option value=102>Jim Gardiner
<option value=50>Jim Osborne
<option value=130>John Pesek
<option value=43>Jon White
<option value=148>Jozette Booth
<option value=125>Julie Goeckner
<option value=193>Julie J Offner
<option value=79>Karen Pigee
<option value=44>Kay Dennis
<option value=42>Ken Elder
<option value=106>Kerry Grooms
<option value=175>Kim Alston-Akers
<option value=56>Kirk Lachman
<option value=170>Lam Xuan
<option value=51>Lee Bishop
<option value=17>Linda Quering
<option value=155>Linda Desell
<option value=61>Lisa Ray
<option value=145>Marcus Popa
<option value=84>Marian Crawford
<option value=69>Marilyn Kavchak
<option value=34>Mark VanDerPuy
<option value=141>Mark Kolbe
<option value=137>Mark Tynan
<option value=134>Mary E. Bennington
<option value=122>Michael L. Ulshafer
<option value=52>Mike Ruiz
<option value=136>Mike Valentine
<option value=165>Nancy Slater-Thompson
<option value=87>Narendra Mathur
<option value=76>Neal Hunemuller
<option value=21>Nora Gilbert
<option value=133>Paige Russell
<option value=167>Pam McCann
<option value=57>Paul Harrington
<option value=144>Peggy Sanchez-Bartz
<option value=164>Priscilla Bumbaca
<option value=147>Reggie James
<option value=140>Richard Craun
<option value=139>Robert Toro
<option value=152>Robert Pfaff
<option value=186>Ruth LaTulippe
<option value=138>Saralyn Bunch
<option value=49>Sharon Carter
<option value=47>Sharon Pollock
<option value=90>Sharon Skuchko
<option value=182>Sherae Taylor
<option value=92>Sheryl Morris
<option value=65>Stephen Hanauer
<option value=181>Susan Rives
<option value=36>Suzy Mellington
<option value=142>Thomas Orr
<option value=66>Thomas Keiss
<option value=168>Timothy Gunter
<option value=88>Tom Kiess
<option value=45>Toni Chiri
<option value=78>Tony Lucero
<option value=163>Vernita Galloway
<option value=185>Wayne Belcher
<option value=135>William Spezialetti
</select>
</td>
<td><span style="font: bold 10pt">Direct&nbsp;Support Team&nbsp;Lead:&nbsp;</span></td>
<td><select name="supportteamlead" onChange="verifyTeamLead(this,document.assessment.teamlead);">
<option value=0>< Select Direct Support Team Lead >
<option value=113>Almon Rivers
<option value=70>Bob Scott
<option value=63>Christian Palay
<option value=94>Deborah Kirby
<option value=121>Dennis C. Threatt
<option value=62>Don Harris
<option value=67>Ed Opelski
<option value=104>Gary Sequeira
<option value=172>Jack Jekowski
<option value=123>James Harper
<option value=108>Jerome McMahon
<option value=173>Jerry Greene
<option value=72>John Streeter
<option value=174>Larry Kirkman
<option value=2>Michael Eshleman
<option value=160>Mike Fecht
<option value=149>Molly Myers
<option value=120>Phillip Ralphs
<option value=150>Raymond Mele
<option value=91>Robert Bradbury
<option value=71>Robert Pflueger
<option value=68>Robert Hasson
<option value=1008>Shuhei Higashi
<option value=107>William Petrie
</select>
</td>
</tr>
<tr>
<td><span style="font: bold 10pt">Scheduled Date:</span></td>
<td><input type="text" size=10 maxlength=10 name="scheduleddate" onblur="checkDate(this)" value="<%=scheduleddate%>" ></td>
<td><span style="font: bold 10pt">Re-Scheduled Date:</span></td>
<td valign="bottom"><input type="text" size=10 maxlength=10 name="rescheduleddate" onblur="checkDate(this)" value="<%=rescheduleddate%>"></td>
</tr>
<tr>
<td><span style="font: bold 10pt">Signed Date:</span></td>
<td align="left"><input type="text" size=10 maxlength=10 name="signeddate" onblur="checkDate(this)"value="<%=signeddate%>"></td>
<td><span style="font: bold 10pt">Cancelled Date:</span></td>
<td><input type="text" size=10 maxlength=10 name="cancelleddate" onblur="checkDate(this)" value="<%=cancelleddate%>"></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt">Cancelled Rationale:</span><br><textarea name="cancelledrationale" cols=70 rows=5><%=cancelledrationale%></textarea></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt">Has Condition Report?</span>&nbsp;&nbsp;<select name="cirs" onchange='showhidecr();'>
<option value=TBD>TBD
<option value=No>No
<option value=Yes>Yes
</select>
&nbsp;<span style="font: bold 10pt" id=crbspan0></span></td>
</tr>
<input type=hidden name=crsCount  id=crsCount  value=0>
<input type=hidden name=crlevels  id=crlevels value="D,D," >
<input type=hidden name=crnums  id=crnums value="13398,13399," >
<tr>
<td colspan=4><span style="font: bold 10pt" id=crspan0>CR Level:&nbsp;&nbsp;<select name="crlevel0">
<option value=A>A
<option value=B>B
<option value=C>C
<option value=D>D
<option value=TBD>TBD
</select>

&nbsp;&nbsp;CR Number:&nbsp;&nbsp;<input type="text" size=5 maxlength=5 name="crnum0" onblur=""value=""></span></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt" id=crspan1>CR Level:&nbsp;&nbsp;<select name="crlevel1">
<option value=A>A
<option value=B>B
<option value=C>C
<option value=D>D
<option value=TBD>TBD
</select>

&nbsp;&nbsp;CR Number:&nbsp;&nbsp;<input type="text" size=5 maxlength=5 name="crnum1" onblur="" value=""></span></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt" id=crspan2>CR Level:&nbsp;&nbsp;<select name="crlevel2">
<option value=A>A
<option value=B>B
<option value=C>C
<option value=D>D
<option value=TBD>TBD
</select>

&nbsp;&nbsp;CR Number:&nbsp;&nbsp;<input type="text" size=5 maxlength=5 name="crnum2" onblur=""value="" ></span></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt" id=crspan3>CR Level:&nbsp;&nbsp;<select name="crlevel3" >
<option value=A>A
<option value=B>B
<option value=C>C
<option value=D>D
<option value=TBD>TBD
</select>

&nbsp;&nbsp;CR Number:&nbsp;&nbsp;<input type="text" size=5 maxlength=5 name="crnum3" onblur=""value="" ></span></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt" id=crspan4>CR Level:&nbsp;&nbsp;<select name="crlevel4" >
<option value=A>A
<option value=B>B
<option value=C>C
<option value=D>D
<option value=TBD>TBD
</select>

&nbsp;&nbsp;CR Number:&nbsp;&nbsp;<input type="text" size=5 maxlength=5 name="crnum4" onblur=""value="" ></span></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt" id=crspan5>CR Level:&nbsp;&nbsp;<select name="crlevel5" >
<option value=A>A
<option value=B>B
<option value=C>C
<option value=D>D
<option value=TBD>TBD
</select>

&nbsp;&nbsp;CR Number:&nbsp;&nbsp;<input type="text" size=5 maxlength=5 name="crnum5" onblur=""value="" ></span></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt" id=crspan6>CR Level:&nbsp;&nbsp;<select name="crlevel6" >
<option value=A>A
<option value=B>B
<option value=C>C
<option value=D>D
<option value=TBD>TBD
</select>

&nbsp;&nbsp;CR Number:&nbsp;&nbsp;<input type="text" size=5 maxlength=5 name="crnum6" onblur=""value="" ></span></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt" id=crspan7>CR Level:&nbsp;&nbsp;<select name="crlevel7" >
<option value=A>A
<option value=B>B
<option value=C>C
<option value=D>D
<option value=TBD>TBD
</select>

&nbsp;&nbsp;CR Number:&nbsp;&nbsp;<input type="text" size=5 maxlength=5 name="crnum7" onblur=""value="" ></span></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt" id=crspan8>CR Level:&nbsp;&nbsp;<select name="crlevel8" >
<option value=A>A
<option value=B>B
<option value=C>C
<option value=D>D
<option value=TBD>TBD
</select>

&nbsp;&nbsp;CR Number:&nbsp;&nbsp;<input type="text" size=5 maxlength=5 name="crnum8" onblur=""value="" ></span></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt" id=crspan9>CR Level:&nbsp;&nbsp;<select name="crlevel9" >
<option value=A>A
<option value=B>B
<option value=C>C
<option value=D>D
<option value=TBD>TBD
</select>

&nbsp;&nbsp;CR Number:&nbsp;&nbsp;<input type="text" size=5 maxlength=5 name="crnum9" onblur=""value="" ></span></td>
</tr>
<input type=hidden name=llsCount  id=llsCount value=0>
<input type=hidden name=llnums  id=llnums value=","  >
<tr>
<td colspan=4><span style="font: bold 10pt" id=llspan>Lessons Learned:&nbsp;&nbsp;<select name="ll" onchange='showhidell();' >
<option value=TBD>TBD
<option value=No>No
<option value=Yes>Yes
</select>
</span>&nbsp;<span style="font: bold 10pt" id=llbspan0></span></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt" id=llnspan0>Lessons Learned Number:&nbsp;&nbsp;<input type="text" size=18 maxlength=17 name="llnum0" onblur=""value="" ></span></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt" id=llnspan1>Lessons Learned Number:&nbsp;&nbsp;<input type="text" size=18 maxlength=17 name="llnum1" onblur=""value="" ></span></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt" id=llnspan2>Lessons Learned Number:&nbsp;&nbsp;<input type="text" size=18 maxlength=17 name="llnum2" onblur=""value="" ></span></td>
</tr>
<tr>
<td colspan=4><span style="font: bold 10pt">Comments:</span><br><textarea name="comments" cols=70 rows=5 ><%=comments%></textarea></td>
</tr>
<tr>
<td colspan=4 align="center"><input type="button" name="submitbutton" value="Submit" onClick="verifyForm(document.assessmentForm, '<%=action%>');" ></td>
</tr>
<script language=javascript>
<!--
//document.assessment.id.value = 377;
//selectDivision(document.assessment.org,document.assessment.div);
//-->
</script>
</form>
</table>
	
</td>
</tr>
</table>
<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>
</BODY>
</HTML>