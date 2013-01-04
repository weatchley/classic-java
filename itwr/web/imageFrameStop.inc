<!-- Begin Image Frame Stop *********************************************************** -->
<% if (!ProductionStatus.equals("prod")) { %>
    <!-- Show environment information when not on production -->
    <br>
    <hr width=80% >
    <br><br><%@ include file="envDisp.inc" %>
<% } %>
</TD>

		<TD>
			<IMG SRC="images/spacer.gif" WIDTH=1 HEIGHT=383></TD>
	</TR>
      </table>
    </td>
  </tr>
  <tr> 
    <td width="251" height="2" bgcolor="#CC9900"></td>
    <td rowspan="3" height="2" valign="top" width="25"><img src="images/canto_2.gif" width="25" height="20"></td>
    <td width="505" bgcolor="#CC9900" height="2"></td>
  </tr>
  <tr> 
    <td width="251" height="14" bgcolor="#000000" align="left"><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#FFFFFF">&nbsp; 
      </font></td>
    <td width="100%" bgcolor="#006699" height="14" align="center"><font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif">
    &nbsp;</font></td>
  </tr>
  <tr> 
    <td width="251" bgcolor="#CC9900" height="2"></td>
    <td width="505" bgcolor="#006699" height="2"></td>
  </tr>
  <tr> 
    <td width="251" bgcolor="#006699">&nbsp;</td>
    <td width="25" bgcolor="#006699">&nbsp;</td>
    <td width="505" bgcolor="#006699" valign="bottom" align="right"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">&nbsp;&nbsp;&nbsp;</font></td>
  </tr>
</table>
</form>
<!-- set up iframe for submitting changes to the database -->
<% if (ProductionStatus.equals("prod")) { %>
    <iframe src="blank.jsp" width=1 height=1 align=left name=results border=0 frameborder=0></iframe>
<% } else { %>
    <iframe src="blank.jsp" width=60 height=60 align=left name=results border=0 frameborder=0></iframe>
<% } %>

<%!
private String testNull(String text, String def) {
    if (text != null) {
        return text;
    } else {
        return def;
    }
}
%>

<%!
private String lPad(String text, String pad, int len) {
    while (text.length() < len) {
        text = pad + text;
    }
    return text;
}
%>
<form name='footerform' METHOD='POST' action=>
<input type=hidden name=command value="test">
</form>
<!-- End Image Frame Stop *********************************************************** -->
