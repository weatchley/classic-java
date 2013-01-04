<%@ include file="headerSetup.inc" %>
<HTML>

<HEAD>
<TITLE><%= SystemName %><rdc:productionTest onMatch="false" > - <%= ProductionStatus %></rdc:productionTest></TITLE>
  <LINK href="/common/css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="/common/css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="imageFrameStart.inc" %>
<!-- Begin Main Content -->
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");
usr = new Person((String) session.getAttribute("user.name"));
DbConn myConn = new DbConn("csi");

String[] bgcolors = new String [] {"#ffffff", "#dddddd"};
int i = 0;

%>
  

  Welcome<rdc:isAuthenticated >&nbsp;<%= session.getAttribute("user.fullname") %></rdc:isAuthenticated><br>
  <br>

  <p>Initial SW Import:</p>

<br>
<%@ page import="java.sql.*" %>

<%
//System.out.println("initialSWImport.jsp-Got Here 1" + Utils.genDateID());
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        String outLine = "";
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        Statement stmt = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        String mySchemaPath = "slts";
        String prevSW = "--No Such SW--";
        int rows = 0;
try {

        sqlFrom = " FROM " + mySchemaPath + ".sw_in ";
        sqlWhere = "  ";
        sqlOrderBy = " ORDER BY software_name, ver_rel_no, received ";

        
        sqlcode = "SELECT sw_bar_code, sw_serial_no, platform, software_name, ver_rel_no, manufacturer, pr_no, documentation, comments, verified, otherbarcode, licensetype, " +
                    "numberoflicenses, location, received " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
        stmt = myConn.conn.createStatement ();
        stmt2 = myConn.conn.createStatement ();
        stmt3 = myConn.conn.createStatement ();
        rs = stmt.executeQuery(sqlcode);

        SWProduct prod = null;
        sqlcode = "INSERT INTO " + mySchemaPath + ".sw_transactions (id,productid,tag,serialnumber,purchaseorder,dateadded,datemodified,dateverified,datereceived,dateexpires," +
                    "location,licensetype,licensecount,transactiontype,relatedtransaction,documentation) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ?" +
                    ")";
//System.out.println(sqlcode);
        pstmt = myConn.conn.prepareStatement(sqlcode);
        
        sqlcode = "INSERT INTO " + mySchemaPath + ".sw_products (id,name,version,manufacturer,platform,unlimited,totalcount,usedcount,availcount,status,relatedproduct,dateadded) " +
                    "VALUES (?, ?, ?, ?, ?, 'F', 0, 0, 0, 'active', 0, SYSDATE" +
                    ")";
//System.out.println(sqlcode);
        pstmt2 = myConn.conn.prepareStatement(sqlcode);
        
        String sqlcode2 = "SELECT " + mySchemaPath + ".SW_TRANSACTIONS_ID_SEQ.NEXTVAL FROM dual";
        String sqlcode3 = "SELECT " + mySchemaPath + ".SW_PRODUCTS_ID_SEQ.NEXTVAL FROM dual";
        
        int prodID = 0;
        int sqlID = 0;

        while (rs.next()) {
            //
            SWTransactions trans = null;
            if (!prevSW.equals(rs.getString(4))) {
                
                prevSW = rs.getString(4);
                
                rs3 = stmt3.executeQuery(sqlcode3);
                rs3.next();
                prodID = rs3.getInt(1);
                sqlID = 0;
                pstmt2.setInt(++sqlID, prodID);
                pstmt2.setString(++sqlID, rs.getString(4));
                pstmt2.setString(++sqlID, rs.getString(5));
                pstmt2.setString(++sqlID, rs.getString(6));
                pstmt2.setString(++sqlID, rs.getString(3));
                rows = pstmt2.executeUpdate();
            }
                rs2 = stmt2.executeQuery(sqlcode2);
                rs2.next();
                int transID = rs2.getInt(1);
            
                sqlID = 0;
                pstmt.setInt(++sqlID, transID);
                pstmt.setInt(++sqlID, prodID);
                pstmt.setString(++sqlID, rs.getString(1));
                pstmt.setString(++sqlID, rs.getString(2));
                pstmt.setString(++sqlID, rs.getString(7));
                pstmt.setDate(++sqlID, ((rs.getTimestamp(15) != null) ? Utils.castDate((java.util.Date) rs.getTimestamp(15)) : Utils.castDate(new java.util.Date())));
                pstmt.setDate(++sqlID, Utils.castDate(new java.util.Date()));
                pstmt.setDate(++sqlID, ((rs.getTimestamp(10) != null) ? Utils.castDate((java.util.Date) rs.getTimestamp(10)) : null));
                pstmt.setDate(++sqlID, ((rs.getTimestamp(15) != null) ? Utils.castDate((java.util.Date) rs.getTimestamp(15)) : Utils.castDate(new java.util.Date())));
                pstmt.setDate(++sqlID, null);
                pstmt.setString(++sqlID, rs.getString(14));
                pstmt.setString(++sqlID, rs.getString(12));
                pstmt.setInt(++sqlID, ((rs.getString(13) != null) ? Integer.parseInt(rs.getString(13)) : 0));
                pstmt.setString(++sqlID, "initial load");
                pstmt.setString(++sqlID, rs.getString(8));
                rows = pstmt.executeUpdate();
            
            if (rs.getString(9) != null) {
                Comments com = new Comments(prodID, transID, 0, rs.getString(9), myConn);
            }
        }
        rs.close();
}
catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " initialSWImport.jsp");
}

%>

<%
myConn.release();
%>


<!-- End Main Content -->
<%@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>
