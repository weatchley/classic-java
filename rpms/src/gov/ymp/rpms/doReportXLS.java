package gov.ymp.rpms;

import javax.servlet.*;
import javax.servlet.http.*;
// Support classes
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import java.math.*;
import java.awt.*;
import java.lang.*;
import java.text.*;
import gov.ymp.rpms.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import javax.naming.*;
import gov.ymp.rpms.model.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;


public class doReportXLS extends HttpServlet {
  // Handle the GET HTTP Method
  public void doGet(HttpServletRequest request,
            HttpServletResponse response)
     throws IOException {

    String message = "";
    try {
        //message = processRequest(request,response);
        generateResponse("Invalid 'GET' request", "n/a", response);
    }
    finally {
    }
  }

  // Handle the POST HTTP Method
  public void doPost(HttpServletRequest request,
             HttpServletResponse response)
     throws IOException {

    String message = "";
    try {
        message = processRequest(request,response);
    }
    finally {
    }
  }


  // Process the request
  private String processRequest(HttpServletRequest request, HttpServletResponse response) {

    System.setProperty( "java.awt.headless" , "true" );

    String outLine = "";
    RecordSeries [] list = null;
    FOrganization loc = null;

    String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
    String [] locString = request.getParameterValues("location");

    String reportTitle = "";

    String outline = "";

    int locID = 0;
    int numColumns = 14;


    OutputStream toClient;
    HttpSession session = request.getSession();

    DbConn myConn = null;
    try {

        myConn = new DbConn();
        boolean dispLocation = true;

        if (locString[0] != null && !locString[0].equals("")) {
            locID = Integer.parseInt(locString[0]);
            if (locID != 0) {
                loc = new FOrganization(locID, myConn);
                dispLocation = false;
            }
        }

        String csiSchema = myConn.getSchemaPath();
// get data based on selected filters
        if (command.equals("annualfileplan")) {
            list = RecordSeries.getItemList(myConn, locID, true, "commonflag DESC, description");
            reportTitle = "Annual File Plan" + ((locID > 0) ? " for " + loc.getDescription() : "");
        } else if (command.equals("annualfileplan2")) {
            list = RecordSeries.getItemList(myConn, locID, true, "commonflag DESC, fodescription, description");
            reportTitle = "Annual File Plan" + ((locID > 0) ? " for " + loc.getDescription() : " by Location");
        } else if (command.equals("annualshortterm1")) {
            list = RecordSeries.getItemList(myConn, locID, true, "description", "S");
            reportTitle = "Annual Short-term Records for Disposition" + ((locID > 0) ? " for " + loc.getDescription() : "");
        } else if (command.equals("annualshortterm2")) {
            list = RecordSeries.getItemList(myConn, locID, true, "cdescription, description", "S");
            reportTitle = "Annual Short-term Records for Disposition" + ((locID > 0) ? " for " + loc.getDescription() + " by Citation" : "");
        } else if (command.equals("annuallongterm")) {
            list = RecordSeries.getItemList(myConn, 0, false, "retentioncode, description", "L");
            reportTitle = "Annual Long-term Records for Disposition";
            dispLocation = true;
        }
        if (dispLocation) {
            numColumns = 15;
        }
// Create XLS workbook
        HSSFWorkbook wb = new HSSFWorkbook();

// create styles for workbook
        HashMap csm = generateStyles(wb);
// initialize work sheet
        HSSFSheet sheet = wb.createSheet("new sheet");
        HSSFRow row = null;
        HSSFCell cell = null;
        short rowNumb = -1;
        short cellNumb = -1;

// do headers
        row = sheet.createRow(++rowNumb);
        sheet.createFreezePane( 0, 3, 0, 3 );
        sheet.addMergedRegion(new Region(rowNumb,(short)0,rowNumb,(short)(numColumns-1)));
        //cell = row.createCell(++cellNumb);
        //cell.setCellValue(reportTitle);
        //cell.setCellStyle( (HSSFCellStyle) csm.get("csBoldCenter") );
        cell = generateCell(row, ++cellNumb, reportTitle, (HSSFCellStyle) csm.get("csBoldCenter"));
// -
        row = sheet.createRow(++rowNumb);
        cellNumb = -1;
        sheet.addMergedRegion(new Region(rowNumb,(short)0,rowNumb,(short)(numColumns-8)));
        sheet.addMergedRegion(new Region(rowNumb,(short)(numColumns-7),rowNumb,(short)(numColumns-1)));
        row.createCell(++cellNumb).setCellValue(" ");
        cell = row.createCell(++cellNumb);
        //cell = row.createCell((short)(numColumns-7));
        //cell.setCellValue("Flags");
        //cell.setCellStyle( (HSSFCellStyle) csm.get("csBoldCenter") );
        cell = generateCell(row, (short)(numColumns-7), "Flags", (HSSFCellStyle) csm.get("csBoldCenter"));
// set worksheet name
        wb.setSheetName(0, "File Plan Report");
// -
        row = sheet.createRow(++rowNumb);
        cellNumb = -1;
// output header info
        if (dispLocation) {
            cell = generateCell(row, ++cellNumb, "Location", (HSSFCellStyle) csm.get("csBold"));
        }
        cell = generateCell(row, ++cellNumb, "Retention Code\n(Pending)", (HSSFCellStyle) csm.get("csBold"));
        cell = generateCell(row, ++cellNumb, "Common Name", (HSSFCellStyle) csm.get("csBold"));
        cell = generateCell(row, ++cellNumb, "Record Series", (HSSFCellStyle) csm.get("csBold"));
        cell = generateCell(row, ++cellNumb, "Quality Designation", (HSSFCellStyle) csm.get("csBold"));
        cell = generateCell(row, ++cellNumb, "Access Restrictions", (HSSFCellStyle) csm.get("csBold"));
        cell = generateCell(row, ++cellNumb, "Citation", (HSSFCellStyle) csm.get("csBold"));
        cell = generateCell(row, ++cellNumb, "Requirement", (HSSFCellStyle) csm.get("csBold"));
        cell = generateCell(row, ++cellNumb, "General", (HSSFCellStyle) csm.get("csBold"));
        cell = generateCell(row, ++cellNumb, "Perm", (HSSFCellStyle) csm.get("csBold"));
        cell = generateCell(row, ++cellNumb, "Long-Term", (HSSFCellStyle) csm.get("csBold"));
        cell = generateCell(row, ++cellNumb, "Short-Term", (HSSFCellStyle) csm.get("csBold"));
        cell = generateCell(row, ++cellNumb, "Vital", (HSSFCellStyle) csm.get("csBold"));
        cell = generateCell(row, ++cellNumb, "Unscheduled\nDo Not Destroy", (HSSFCellStyle) csm.get("csBoldCenter"));
        cell = generateCell(row, ++cellNumb, "Freeze", (HSSFCellStyle) csm.get("csBoldCenter2"));

        if (list.length == 0) {
            row = sheet.createRow(++rowNumb);
            cellNumb = -1;
            row.createCell(++cellNumb).setCellValue("Empty Report");
        }

// output record series, crosswalk and flag info
        for (int i=0; i<list.length; i++) {
            Crosswalk [] cw = list[i].getCrosswalkSet();
            if (cw.length == 0) {
                // no crosswalk, just flags
                row = sheet.createRow(++rowNumb);
                cellNumb = -1;
                if (dispLocation) {
                    cell = generateCell(row, ++cellNumb, list[i].getLocationDescription(), (HSSFCellStyle) csm.get("cs"));
                }
                cell = generateCell(row, ++cellNumb, list[i].getRetentionCode(), (HSSFCellStyle) csm.get("cs"));
                cell = generateCell(row, ++cellNumb, "n/a", (HSSFCellStyle) csm.get("cs"));
                cell = generateCell(row, ++cellNumb, list[i].getDescription(), (HSSFCellStyle) csm.get("cs"));
                cell = generateCell(row, ++cellNumb, "n/a", (HSSFCellStyle) csm.get("cs"));
                cell = generateCell(row, ++cellNumb, "n/a", (HSSFCellStyle) csm.get("cs"));
                cell = generateCell(row, ++cellNumb, list[i].getCitationDescription() + ", Cutoff: " + list[i].getCutoffDescription() + ", Retention: " + list[i].getRetentionPeriodDescription(), (HSSFCellStyle) csm.get("cs"));
                cell = generateCell(row, ++cellNumb, "n/a", (HSSFCellStyle) csm.get("cs"));
                cell = generateCell(row, ++cellNumb, ((list[i].getCommonFlag().equals("T")) ? "X" : ""), (HSSFCellStyle) csm.get("csCenter"));
                cell = generateCell(row, ++cellNumb, ((list[i].getRetentionGroupFlagCode().equals("P")) ? "X" : ""), (HSSFCellStyle) csm.get("csCenter"));
                cell = generateCell(row, ++cellNumb, ((list[i].getRetentionGroupFlagCode().equals("L")) ? "X" : ""), (HSSFCellStyle) csm.get("csCenter"));
                cell = generateCell(row, ++cellNumb, ((list[i].getRetentionGroupFlagCode().equals("S")) ? "X" : ""), (HSSFCellStyle) csm.get("csCenter"));
            } else {
                // crosswalk and flags
                for (int j=0; j<cw.length; j++) {
                    row = sheet.createRow(++rowNumb);
                    cellNumb = -1;
                    if (dispLocation) {
                        cell = generateCell(row, ++cellNumb, list[i].getLocationDescription(), (HSSFCellStyle) csm.get("cs"));
                    }
                    cell = generateCell(row, ++cellNumb, list[i].getRetentionCode(), (HSSFCellStyle) csm.get("cs"));
                    cell = generateCell(row, ++cellNumb, cw[j].getName(), (HSSFCellStyle) csm.get("cs"));
                    cell = generateCell(row, ++cellNumb, list[i].getDescription(), (HSSFCellStyle) csm.get("cs"));
                    cell = generateCell(row, ++cellNumb, cw[j].getQADesignation(), (HSSFCellStyle) csm.get("cs"));
                    cell = generateCell(row, ++cellNumb, ((cw[j].getAccessRestrictions() != null) ? cw[j].getAccessRestrictions() : "None"), (HSSFCellStyle) csm.get("cs"));
                    cell = generateCell(row, ++cellNumb, list[i].getCitationDescription() + ", Cutoff: " + list[i].getCutoffDescription() + ", Retention: " + list[i].getRetentionPeriodDescription(), (HSSFCellStyle) csm.get("cs"));
                    cell = generateCell(row, ++cellNumb, cw[j].getRequirements() + ((cw[j].getRequirementsOther() != null) ? "\n" + cw[j].getRequirementsOther() : ""), (HSSFCellStyle) csm.get("cs"));
                    cell.setCellType( HSSFCell.CELL_TYPE_STRING );
                    cell = generateCell(row, ++cellNumb, ((list[i].getCommonFlag().equals("T")) ? "X" : ""), (HSSFCellStyle) csm.get("csCenter"));
                    cell = generateCell(row, ++cellNumb, ((list[i].getRetentionGroupFlagCode().equals("P")) ? "X" : ""), (HSSFCellStyle) csm.get("csCenter"));
                    cell = generateCell(row, ++cellNumb, ((list[i].getRetentionGroupFlagCode().equals("L")) ? "X" : ""), (HSSFCellStyle) csm.get("csCenter"));
                    cell = generateCell(row, ++cellNumb, ((list[i].getRetentionGroupFlagCode().equals("S")) ? "X" : ""), (HSSFCellStyle) csm.get("csCenter"));
                    cell = generateCell(row, ++cellNumb, ((cw[j].getVitalRecord().equals("T")) ? "X" : ""), (HSSFCellStyle) csm.get("csCenter"));
                    cell = generateCell(row, ++cellNumb, ((cw[j].getUnscheduled().equals("T")) ? "X" : ""), (HSSFCellStyle) csm.get("csCenter"));
                    //cell = generateCell(row, ++cellNumb, ((cw[j].getFreezeHoldFlag().equals("T")) ? "X" : ""), (HSSFCellStyle) csm.get("csCenter"));
                    cell = generateCell(row, ++cellNumb, ((cw[j].getFreezeHoldFlag().equals("T")) ? cw[j].getFreezeHoldFlagText() : ""), (HSSFCellStyle) csm.get("csCenter"));
                }
            }
        }
        for (short i=0; i<numColumns; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=report.xls");
        //PrintWriter out = response.getWriter();
        wb.write(response.getOutputStream());


    }


    catch (IllegalArgumentException e) {
        outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logError(0, "csi", 0, "Argument error: '" + outLine + "'");
        try { generateResponse(outLine, command, response); } catch (Exception i) {}
        //log(outLine);
    }


    catch (NullPointerException e) {
        outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logError(0, "csi", 0, "Null Pointer error: '" + outLine + "'");
        try { generateResponse(outLine, command, response); } catch (Exception i) {}
        //log(outLine);
    }

    //catch (IOException e) {
    //    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logActivity(0, "csi", 0, "Permission error: '" + outLine + "'");
    //    try { generateResponse(outLine, command, response); } catch (Exception i) {}
    //    //log(outLine);
    //}

    catch (Exception e) {
        outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logActivity(0, "csi", 0, "Misc error: '" + outLine + "'");
        try { generateResponse(outLine, command, response); } catch (Exception i) {}
        //log(outLine);
    }
    finally {
        //try { generateResponse(outLine, command, response); } catch (Exception i) {}

        myConn.release();
       //log("Test log message\n");
    }


    return outLine;

  }


    // Generate an XLS cell
    private HSSFCell generateCell(HSSFRow row, short cellNumb, String contents, HSSFCellStyle cs) {
        HSSFCell cell = null;
        cell = row.createCell(cellNumb);
        cell.setCellValue(contents);
        cell.setCellStyle( cs );

        return cell;
    }


    // Generate an XLS cell
    private HSSFCell generateCell(HSSFRow row, short cellNumb, int contents, HSSFCellStyle cs) {
        HSSFCell cell = null;
        cell = row.createCell(cellNumb);
        cell.setCellValue(contents);
        cell.setCellStyle( cs );

        return cell;
    }


    // Generate an XLS cell
    private HSSFCell generateCell(HSSFRow row, short cellNumb, double contents, HSSFCellStyle cs) {
        HSSFCell cell = null;
        cell = row.createCell(cellNumb);
        cell.setCellValue(contents);
        cell.setCellStyle( cs );

        return cell;
    }


    // Generate an XLS cell
    private HSSFCell generateCellFormula(HSSFRow row, short cellNumb, String contents, HSSFCellStyle cs) {
        HSSFCell cell = null;
        cell = row.createCell(cellNumb);
        cell.setCellFormula(contents);
        cell.setCellStyle( cs );

        return cell;
    }


    // Generate the XLS styles
    private HashMap generateStyles(HSSFWorkbook wb) {
        HashMap hm = new HashMap();

        // create fonts
        HSSFFont f = wb.createFont();
        HSSFFont fBold = wb.createFont();
        fBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        // create styles
        HSSFCellStyle cs = wb.createCellStyle();
        cs.setFont( f );
        cs.setWrapText( true );
        cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        hm.put("cs", cs);

        HSSFCellStyle csCenter = wb.createCellStyle();
        csCenter.setFont( f );
        csCenter.setWrapText( true );
        csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        hm.put("csCenter", csCenter);

        HSSFCellStyle csBold = wb.createCellStyle();
        csBold.setFont( fBold );
        csBold.setWrapText( true );
        //csBold.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        hm.put("csBold", csBold);

        HSSFCellStyle csBoldCenter = wb.createCellStyle();
        csBoldCenter.setFont( fBold );
        csBoldCenter.setWrapText( true );
        csBoldCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        hm.put("csBoldCenter", csBoldCenter);

        HSSFCellStyle csBoldCenter2 = wb.createCellStyle();
        csBoldCenter2.setFont( fBold );
        csBoldCenter2.setWrapText( true );
        //csBoldCenter2.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csBoldCenter2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        hm.put("csBoldCenter2", csBoldCenter2);

        // ----
        return (hm);
    }



  // Generate the HTML response
  private void generateResponse(String outLine, String command, HttpServletResponse response)
      throws IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    out.println("<html>");
    out.println("<head>");
    out.println("  <LINK href=\"css/styles.css\" type=text/css rel=STYLESHEET>");
    out.println("<title>RPMS Reports</title>");
    out.println("</head>");
    out.println("<body BGCOLOR=#DEC68B leftmargin=0 topmargin=0>");
    out.println("<form name=result method=post>");
    out.println(outLine);
    out.println("<input type=hidden name=command value='" + command + "'>");
    out.println("<script language=\"JavaScript\">\n");
    out.println("<!--\n");
//    if (success) {
//        if (!outLine.equals("")) {
//            out.println("alert('" + outLine + "');\n");
//        }
//    } else {
//        out.println("alert('Error saving permission!');\n");
//    }
    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }
}
