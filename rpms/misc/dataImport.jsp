<HTML>

<HEAD>
<TITLE>Records Plan Management System</TITLE>
  <LINK href="css/styles.css" type=text/css rel=STYLESHEET>
  <link rel="stylesheet" href="css/prstyle.css" type="text/css">
</HEAD>

<BODY leftmargin=0 topmargin=0>
<%@ include file="headerSetup.inc" %>
<%//@ include file="imageFrameStart.inc" %>
<%@ page import="org.apache.poi.hssf.usermodel.*,org.apache.poi.hssf.util.*,org.apache.poi.*, org.apache.poi.hssf.*,org.apache.poi.poifs.filesystem.POIFSFileSystem" %>
<%@ page import="oracle.sql.*,java.sql.*" %>
<!-- Begin Main Content -->
<p>
    <% 
String docRoot = getServletContext().getRealPath("/");

DbConn myConn = new DbConn("csi");

%>
<h2>RPMS Data Import Script</h2>
<%
POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(docRoot + "/data.xls"));
HSSFWorkbook wb = new HSSFWorkbook(fs);

HSSFSheet sheet = wb.getSheetAt(0);
HSSFSheet coSheet = wb.getSheetAt(1);
HSSFSheet rgfSheet = wb.getSheetAt(2);
HSSFSheet foSheet = wb.getSheetAt(3);
HSSFSheet qaSheet = wb.getSheetAt(4);

HashMap rgfMap = getMap(rgfSheet, (short) 0, (short) 2, (short) 0);
%>
<p>Read in "RGF" Map.</p>
<%
HashMap foMap = getMap(foSheet, (short) 0, (short) 2, (short) 2);
%>
<p>Read in "FO" Map.</p>
<%
HashMap qaMap = getMap(qaSheet, (short) 0, (short) 1, (short) 0);
%>
<p>Read in "QA" Map.</p>

<%
int numbOfRows = (int) sheet.getPhysicalNumberOfRows();
int currRS = 0;
RecordSeries rs = null;
Crosswalk cw = null;
String lastVital = "F";
int lastQA = 1;
String lastFreeze = "F";
String lastFreezeText = "";
for (int i = 1; i<1090; i++) {
    DbConn myConn2 = new DbConn("csi");
    HSSFRow row = sheet.getRow(i);
    if (row != null) {
        int numbOfCols = 10;
        HSSFCell [] cell = new HSSFCell [numbOfCols];
        for (int j = 0; j<numbOfCols; j++) {
            cell[j] = row.getCell((short) j);
        }
        //if (cell[0] != null || cell[1] != null) {
        if (cell[1] != null && !cell[1].toString().equals("") && !cell[1].toString().equals(" ")) {
            rs = new RecordSeries();
            if (cell[1] != null) {
                rs.setDescription(cell[1].toString());
            }
            if (cell[0] != null) {
                rs.setOCRWMCode(cell[0].toString());
            }
            if (cell[3] != null) {
                rs.setCommonFlag((cell[3].toString().equals("Y")) ? "T" : "F");
            }
            if (cell[4] != null) {
                rs.setCutoff(getCutoffID(myConn2, cell[4].toString()));
            }
            if (cell[5] != null) {
//System.out.println("dataImport.jsp - Got Here 2" + " RS Description: '" + rs.getDescription() +"',  cell val: '" + cell[5].toString() +"', foMap val: '" + foMap.get(cell[5].toString()) + "'");
                //rs.setLocation(Integer.parseInt((String) foMap.get(cell[5].toString())));
                rs.setLocation((int) Double.parseDouble((String) foMap.get(cell[5].toString())));
            }
            if (cell[6] != null) {
//System.out.println("dataImport.jsp - Got Here 3" + " RS Description: '" + rs.getDescription() +"',  cell val: '" + cell[6].toString() +"', rgfMap val: '" + rgfMap.get(cell[6].toString()) + "'");
                String tmp = (String) rgfMap.get(cell[6].toString());
                if (tmp != null) {
                    //rs.setRetentionGroupFlag(Integer.parseInt(tmp));
                    rs.setRetentionGroupFlag((int) Double.parseDouble(tmp));
                }
            }
            rs.save(myConn2);
            currRS = rs.getID();
            
        } else if (cell[2] != null && currRS > 0) {
            cw = new Crosswalk();
            cw.setRSID(currRS);
            cw.setName(cell[2].toString());
            if (cell[7] != null) {
                cw.setVitalRecord((cell[7].toString().equals("Y") || cell[7].toString().equals("Yes")) ? "T" : "F");
                lastVital = cell[7].toString();
            } else {
                cw.setVitalRecord(lastVital);
            }
            if (cell[8] != null) {
                //cw.setQADesignation(Integer.parseInt((String) qaMap.get(cell[8].toString())));
                cw.setQADesignation((int) Double.parseDouble((String) qaMap.get(cell[8].toString())));
                //lastQA = Integer.parseInt((String) qaMap.get(cell[8].toString()));
                lastQA = (int) Double.parseDouble((String) qaMap.get(cell[8].toString()));
            } else {
                cw.setQADesignation(lastQA);
            }
            if (cell[9] != null) {
                String tmp = (String) cell[9].toString();
                cw.setFreezeHoldFlag((tmp.equals("No")) ? "F" : "T");
                lastFreeze = (tmp.equals("No")) ? "F" : "T";
                if (!tmp.equals("No")) {
                    cw.setFreezeHoldText(tmp);
                }
            } else {
                cw.setFreezeHoldFlag(lastFreeze);
            }
            if (cw.getName() != null) {
                cw.save(myConn2);
            }
        }
    }
    myConn2.release();
}


%>

<%
myConn.release();

%>

<p>Finished</p>



<!-- End Main Content -->
<%//@ include file="imageFrameStop.inc" %>

</BODY>

</HTML>


<%!
private String cleanString(String in) {
    String out = in;
    out = out.replaceAll("\n", " ");
    out = out.replaceAll("^ ", "");
    return (out);
}

private HashMap getMap(HSSFSheet sheet, short key, short val, short skip) {
    HashMap hm = new HashMap();
    short numbOfRows = (short) sheet.getPhysicalNumberOfRows();
    for (short i = skip; i<numbOfRows-1; i++) {
        HSSFRow row = sheet.getRow(i);
        if (row != null) {
            HSSFCell cellKey = row.getCell(key);
            HSSFCell cellVal = row.getCell(val);
            if (cellKey != null && cellVal != null) {
                String tmp = (String) cellKey.toString();
                String tmp2 = (String) cellVal.toString();
                if (tmp != null && !tmp.equals(" ") && !tmp.equals("") && tmp2 != null && !tmp2.equals(" ") && !tmp2.equals("")) {
//System.out.println("dataImport.jsp - Got Here 1" + " key: '" + tmp +"', val: '" + tmp2 +"'");
                    hm.put(tmp, (String) cellVal.toString());
                }
            }
        }
    }
    
    
    return (hm);
}

private int getCutoffID(DbConn myConn, String co) {
    int coID = 0;
    String outLine = "";
    ResultSet rs = null;
    Statement stmt = null;
    String tmp = cleanString(co);
    int myID = 0;
    try {
        String sqlcode = "SELECT id, description FROM rpms.cutoff_list WHERE description='" + tmp + "'";
//System.out.println(sqlcode);
        // Create a Statement
        stmt = myConn.conn.createStatement ();
        rs = stmt.executeQuery(sqlcode);
        rs.next();
        myID = rs.getInt(1);
    }
    catch (SQLException e) {
        outLine = outLine + "SQLException caught: " + e.getMessage();
        //log(outLine);
        gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - CutoffList lookup");
    }

    catch (Exception e) {
        outLine = outLine + "Exception caught: " + e.getMessage();
        //log(outLine);
        gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
    }
    finally {
        if (rs != null)
            try { rs.close(); } catch (Exception i) {}
        if (stmt != null)
            try { stmt.close(); } catch (Exception i) {}
    }
    if (myID > 0) {
        coID = myID;
    } else {
        CutoffList cl = new CutoffList();
        cl.setDescription(tmp);
        cl.save(myConn);
        coID = cl.getID();
    }
    return (coID);
}



%>