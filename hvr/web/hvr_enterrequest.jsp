<%@ include file="headerSetup.inc" %>

<%@ page import="javax.naming.*" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.sql.*" %>
<%@ page import="gov.ymp.csi.db.*" %>
<%@ page import="gov.ymp.csi.people.*" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<HTML>

<HEAD>
<TITLE>OCRWM Las Vegas Visitor Requests Database - Login Page<rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
<link type="text/css" rel="stylesheet" href="css/hvr.css" />
</HEAD>

<!-- <BODY BGCOLOR='white' onload=document.login.username.focus()> -->
<BODY BGCOLOR='white'>


<rdc:notProductionWarning size="150%" width="180" />
<rdc:sessionTest />

<%
      DbConn myConn = new DbConn("CSI");
      Connection conn = myConn.conn;

      if (session.getAttribute("user.name")  != null && !((String) session.getAttribute("user.name")).toLowerCase().equals("guest") ) {
           // get information about user: name, privs, etc...
           isAuthenticated = true;
           sUsr = new Person((String) session.getAttribute("user.name"));
           usr = (Person) session.getAttribute("user.person");
           cUser = usr;
           pos = (Position) session.getAttribute("user.position");
           posID = (long) new Long((String) session.getAttribute("user.positionid")).longValue();
           perMap = (HashMap) session.getAttribute("user.permissionmap");
           if (pos.belongsTo(((Long) perMap.get("8-admin")).longValue())) {
                   isAdmin = true;
           }
      }

                Date todayis = new Date();
                String alloftoday = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(todayis);
                String justtoday = DateFormat.getDateInstance(DateFormat.LONG).format(todayis);
                int daytotay = 0;

                String escortResponsibilities = "";
                String tempAccess = "";
                String nonCitizen = "";
                Statement stmt = conn.createStatement();
                ResultSet rset = stmt.executeQuery ("SELECT name, text from csi.hvr_explanations");
                while (rset.next()) {
                    if (rset.getString(1).equals ("responsibilities")) {
                        escortResponsibilities = rset.getString(2);
                    }
                    else if (rset.getString(1).equals ("tempaccess")) {
                        tempAccess = rset.getString(2);
                    }
                    else if (rset.getString(1).equals ("noncitizen")) {
                        nonCitizen = rset.getString(2);
                    }
                }
                rset.close();
                
                myConn.release();
                
                String outLine = "";

                // start form %>
<script src=js/hvr.js></script>
<script language=javascript>header('Entry Form');</script>
<script language=javascript>
    function view_report(type) {
        document.enter.rtype.value = type;
        document.enter.action = 'hvr_displayreport.jsp';
        document.enter.submit();
    }
</script>
<%

                String pastdateok = "0";
%>
<rdc:hasPermission permission="2-reports" >
<%
    pastdateok = "1";
%>
</rdc:hasPermission >
<form name=enter method=post action='hvr' onSubmit="this.rphone.optional=true; this.rsecondary.optional=true; this.comment.optional=true; return validateinput(this,<%= pastdateok %>);">

<input type=hidden name=authenticated value=yes>
<input type=hidden name=page value=1>
<input type=hidden name=rtype value=>
<input type=hidden name=username value=<%= cUser.getUserName() %>>

<rdc:hasPermission permission="2-reports" >
    <tr><td align=right><a href=javascript:view_report('Current')>View Report</a></td></tr>
</rdc:hasPermission >
</table>

<input type=hidden name=UserID value=<%= + cUser.getID() %>>
<table width=700 align=center>
<tr><td class=label>Date of Request:</td>
<td class=formw><%= justtoday %></td></tr>
<tr><td class=label>Name of Requester:</td>
<td class=formw><%= cUser.getFirstName() %>&nbsp;<%= cUser.getLastName() %></td></tr>
<tr><td class=label>Requester Email:</td>
<td class=formw>
<%                if (cUser.getEmail () != null) { %>
                    <%= cUser.getEmail() %>
<%                } else { %>
    <input type=text name=remail size=40 maxlength=60 value="<%= cUser.getEmail ()  %>">
<%                } %>
</td></tr>
<tr><td class=label>Requester Phone Number:</td>
<td class=formw>
<%                if (cUser.getPhone () != null ) { %>
    <input type=text name=rphone value="<%= cUser.getPhone()  %>" class=widget> <font>(xxx-xxx-xxxx)</font>
<%                } else { %>
<input type=text name=rphone class=widget> <font>(xxx-xxx-xxxx)</font>
<%                } %>
</td></tr>
<tr><td class=label>Requester Secondary Contact:</td>
<td class=formw><input type=text name=rsecondary value="" maxlength=80 class=widget> <font>(optional; can be alternate person, phone, or email)</font></td></tr>
<tr><td colspan=2><center><hr width=200></center></td></tr>
</table>

<table width=630 align=center>
<tr><td colspan=2><%= escortResponsibilities %></td></tr>
<tr><td colspan=2><%= tempAccess %></td></tr>
<tr><td colspan=2><%= nonCitizen %></td></tr>
<tr><td colspan=2>&nbsp;</td></tr>
</table>

<table width=700 align=center>
<tr><td class=label>Number of visitors:</td>
<td><input type=text name=howmany value="" onchange=javascript:addvisitor();></td><td><font>Please enter the number of visitors you expect. This will provide entry fields for all visitors.</td></tr>
<tr><td class=label>Visitor(s) Information:</td>

<td class=formw colspan=2>
<table>
<tr><td colspan=3>First Name:&nbsp;&nbsp;<input type=text name=visitorfirstname1 size=20 maxlength=45>&nbsp&nbsp;Last Name:&nbsp&nbsp;<input type=text name=visitorlastname1 size=20 maxlength=45></td></tr>
<tr><td colspan=3>Affiliation:&nbsp;&nbsp;<input type=text name=visitoraffiliation1 size=40 maxlength=60 value=>&nbsp;<font>(organization or employer)</font></td></tr>
<tr><td colspan=3>Destination:&nbsp;&nbsp;<select name='destination1'><option value='DOE Hillshire Facility'>DOE Hillshire Facility</option><option value='M&O Facilities'>M&O Facilities</option><option value='Lead Lab Facilities'>Lead Lab Facilities</option></select>
<input type="checkbox" name="otherbox1" onClick="javascript:otherChecked('1');"><input type='hidden' name='otherchecked1' value=''>Other:&nbsp;<input type=text name=destinationother1 size=25 style="display:none"></td></tr>
<tr><td>US Citizen?&nbsp;<input type=checkbox name=iscitizen1 value=T></td>
<td width=10><li></td><td>Requires&nbsp;&nbsp;(Escort <input type=radio name=escortorproxy1 value=0 checked>)&nbsp;&nbsp;or&nbsp;&nbsp;(Visitor Badge <input type=radio name=escortorproxy1 value=1>)</td></tr>
</table>
<div id=visitorinfo>
</div>
</td>
</tr>
<tr><td class=label>Visit Start Date:</td>
<td class=formw colspan=2><script language=javascript>drawdate ('s','today');</script></td></tr>
<tr><td class=label>Visit End Date:</td>
<td class=formw colspan=2><script language=javascript>drawdate ('e','today');</script></td></tr>
<tr><td class=label>Purpose of Visit:</td>
<td class=formw colspan=2><textarea name=purpose rows=3 cols=60 onchange=javascript:validatelength(this);></textarea></td></tr>
<tr><td class=label>Comment:</td>
<td class=formw valign=top colspan=2><textarea name=comment rows=3 cols=60 onchange=javascript:validatelength(this);></textarea>&nbsp;&nbsp;<font>(optional)</font></td></tr>
<tr><td colspan=2>&nbsp;</td></tr>
<tr><td colspan=3 align=center><input type=submit value=Submit></td></tr>
<tr><td colspan=2>&nbsp;</td></tr>
</form>
<script language=javascript>
    footer();
</script>





<rdc:envDisplay />
</BODY>

</HTML>

