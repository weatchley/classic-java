package gov.ymp.slts;

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
import gov.ymp.slts.model.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import javax.naming.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;


public class doPCSearchReportXLS extends HttpServlet {
  // Handle the GET HTTP Method
  public void doGet(HttpServletRequest request,
            HttpServletResponse response)
     throws IOException {

    String message = "";
    try {
        //message = processRequest(request,response);
        generateResponse("Invalid 'GET' request", response);
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

    String reportTitle = "PCs With Selected Software";

    String outline = "";


    OutputStream toClient;
    HttpSession session = request.getSession();

    DbConn myConn = null;
    try {

        myConn = new DbConn();
        boolean dispLocation = true;
//System.out.println("doPCSearchReportXLS - Got Here 1" + " - " + Utils.genDateID());
        String swpSearchList = "";
        HashSet hs = new HashSet();


        String searchText = ((request.getParameter("searchtext") != null && !request.getParameter("searchtext").equals("")) ? (String) request.getParameter("searchtext") : "");
        boolean isFiltered  = ((request.getParameter("searchfiltered") != null && request.getParameter("searchfiltered").equals("T")) ? true : false);
        searchText = searchText.replaceAll("\n|\t|\r|\f", ",");
        searchText = searchText.replaceAll(",,", ",");

        String [] searchSet = searchText.split(",");
        for (int i=0; i< searchSet.length; i++) {
            searchSet[i] = searchSet[i].trim();
        }
        HashSet compList = new HashSet();
        HashSet itemList = new HashSet();
        HashMap [] data = new HashMap [searchSet.length];

        for (int i=0; i<searchSet.length; i++) {
            data[i] = new HashMap();
            AppInventory [] appList = AppInventory.getItemList(myConn, 0, null, isFiltered, "ai.listname,c.name", false, searchSet[i]);
            for (int j=0; j<appList.length; j++) {
                String tmp = "";
                if (data[i] != null && data[i].get(appList[j].getComputerName()) != null) {
                    tmp = (String) data[i].get(appList[j].getComputerName()) + "\n";
                }
                data[i].put(appList[j].getComputerName(), (tmp + appList[j].getListName() + ((appList[j].getVersion() != null) ? "-" + appList[j].getVersion() : "")));
                compList.add(appList[j].getComputerName());
                itemList.add(appList[j].getListName() + ((appList[j].getVersion() != null) ? "-" + appList[j].getVersion() : ""));
            }
            ServiceInventory [] servList = ServiceInventory.getItemList(myConn, 0, null, isFiltered, "si.listname,c.name", false, searchSet[i]);
            for (int j=0; j<servList.length; j++) {
                String tmp = "";
                if (data[i] != null && data[i].get(servList[j].getComputerName()) != null) {
                    tmp = (String) data[i].get(servList[j].getComputerName()) + "\n";
                }
                data[i].put(servList[j].getComputerName(), (tmp + servList[j].getListName()));
                compList.add(servList[j].getComputerName());
                itemList.add(servList[j].getListName());
            }

        }

        String comps [] = (String []) compList.toArray (new String [compList.size ()]);
        Arrays.sort(comps, String.CASE_INSENSITIVE_ORDER);

        String items [] = (String []) itemList.toArray (new String [itemList.size ()]);
        Arrays.sort(items, String.CASE_INSENSITIVE_ORDER);




// Create XLS workbook
        HSSFWorkbook wb = new HSSFWorkbook();
// create fonts for workbook
        HashMap fontSet = generateFonts(wb);
// create styles for workbook
        HashMap csm = generateStyles(wb, fontSet);

//-----------------------------------------------------

// Initialize work sheet
        HSSFSheet sheet = wb.createSheet("new sheet");
        HSSFRow row = null;
        HSSFCell cell = null;
        short rowNumb = -1;
        short cellNumb = -1;

//-----------------------------------------------------
// Set up report title (span multiple columns
        int tableWidth = searchSet.length + 1;
        row = sheet.createRow(++rowNumb);
        sheet.addMergedRegion(new Region(rowNumb,(short)0,rowNumb,(short)(tableWidth - 1)));
        cell = row.createCell(++cellNumb);
        cell.setCellValue(reportTitle);
        cell.setCellStyle( (HSSFCellStyle) csm.get("csBold14") );
        row.setHeight( (short) (((HSSFFont) fontSet.get("fBold14")).getFontHeight() + 50) );
//-

        wb.setSheetName(0, reportTitle);


// Do main body headers 1
        row = sheet.createRow(++rowNumb);
        cellNumb = -1;
        cell = generateCell(row, ++cellNumb, "Computer", (HSSFCellStyle) csm.get("csCenterB2"));
        for (int i=0; i<searchSet.length; i++) {
            cell = generateCell(row, ++cellNumb, searchSet[i], (HSSFCellStyle) csm.get("csCenterB2"));
        }


// main table values
        int firstRowNumber = 0;
        for (int i=0; i<comps.length; i++) {
            row = sheet.createRow(++rowNumb);
            if (i == 0) { firstRowNumber = rowNumb + 1; }
            cellNumb = -1;
            int rowID = rowNumb + 1;
            cell = generateCell(row, ++cellNumb, comps[i], (HSSFCellStyle) csm.get("csB"));
            for (int j=0; j<searchSet.length; j++) {
                cell = generateCell(row, ++cellNumb, ((String) ((data[j].get(comps[i]) != null) ? data[j].get(comps[i]) : "")), (HSSFCellStyle) csm.get("csB"));
            }


        }

// size columns to fit content
        sheet.autoSizeColumn((short) 0);
        for (short i=0; i<tableWidth; i++) {
            sheet.autoSizeColumn(i);
        }

// Start new tab
//-----------------------------------------------------

// Initialize work sheet
        sheet = wb.createSheet("new sheet");
        row = null;
        cell = null;
        rowNumb = -1;
        cellNumb = -1;
//-----------------------------------------------------
// Set up report title (span multiple columns
        tableWidth = 1;
        row = sheet.createRow(++rowNumb);
        sheet.addMergedRegion(new Region(rowNumb,(short)0,rowNumb,(short)(tableWidth - 1)));
        cell = row.createCell(++cellNumb);
        cell.setCellValue(reportTitle);
        cell.setCellStyle( (HSSFCellStyle) csm.get("csBold14") );
        row.setHeight( (short) (((HSSFFont) fontSet.get("fBold14")).getFontHeight() + 50) );
//-

        wb.setSheetName(1, "Unique Items Found");

// Do main body headers 1
        row = sheet.createRow(++rowNumb);
        cellNumb = -1;
        cell = generateCell(row, ++cellNumb, "Found Item", (HSSFCellStyle) csm.get("csCenterB2"));

// main table values
        firstRowNumber = 0;
        for (int i=0; i<items.length; i++) {
            row = sheet.createRow(++rowNumb);
            if (i == 0) { firstRowNumber = rowNumb + 1; }
            cellNumb = -1;
            int rowID = rowNumb + 1;
            cell = generateCell(row, ++cellNumb, items[i], (HSSFCellStyle) csm.get("csB"));

        }

// size columns to fit content
        sheet.autoSizeColumn((short) 0);
        for (short i=0; i<tableWidth; i++) {
            sheet.autoSizeColumn(i);
        }




//-----------------------------------------------------
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=report.xls");
        //PrintWriter out = response.getWriter();
        wb.write(response.getOutputStream());


    }


    catch (IllegalArgumentException e) {
        outLine = outLine + "IllegalArgumentException caught: " + e.getMessage();
        ALog.logError(0, "csi", 0, "Argument error: '" + outLine + "'");
        try { generateResponse(outLine, response); } catch (Exception i) {}
        //log(outLine);
    }


    catch (NullPointerException e) {
        outLine = outLine + "NullPointerException caught: " + e.getMessage();
        ALog.logError(0, "csi", 0, "Null Pointer error: '" + outLine + "'");
        try { generateResponse(outLine, response); } catch (Exception i) {}
        //log(outLine);
    }

    //catch (IOException e) {
    //    outLine = outLine + "IOException caught: " + e.getMessage();
    //    ALog.logActivity(0, "csi", 0, "Permission error: '" + outLine + "'");
    //    try { generateResponse(outLine, response); } catch (Exception i) {}
    //    //log(outLine);
    //}

    catch (Exception e) {
        outLine = outLine + "Exception caught: " + e.getMessage();
        ALog.logActivity(0, "csi", 0, "Misc error: '" + outLine + "'");
        try { generateResponse(outLine, response); } catch (Exception i) {}
        //log(outLine);
    }
    finally {
        //try { generateResponse(outLine, response); } catch (Exception i) {}

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


    // Generate the XLS fonts
    private HashMap generateFonts(HSSFWorkbook wb) {
        HashMap hm = new HashMap();

        // Create fonts
        HSSFFont f = wb.createFont();
        f.setFontName("Times New Roman");
        hm.put("f", f);
        HSSFFont fBold = wb.createFont();
        fBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fBold.setFontName("Times New Roman");
        hm.put("fBold", fBold);
        HSSFFont fBold14 = wb.createFont();
        fBold14.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fBold14.setFontHeightInPoints((short)14);
        fBold14.setFontName("Times New Roman");
        hm.put("fBold14", fBold14);
        HSSFFont fBold24 = wb.createFont();
        fBold24.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fBold24.setFontHeightInPoints((short)24);
        fBold24.setFontName("Times New Roman");
        hm.put("fBold24", fBold24);
        HSSFFont fItalic = wb.createFont();
        fItalic.setItalic(true);
        fItalic.setFontName("Times New Roman");
        hm.put("fItalic", fItalic);

        // ----
        return (hm);
    }


    // Generate the XLS styles
    private HashMap generateStyles(HSSFWorkbook wb, HashMap fontSet) {
        HashMap hm = new HashMap();

        // Create fonts
        HSSFFont f = (HSSFFont) fontSet.get("f");
        HSSFFont fBold = (HSSFFont) fontSet.get("fBold");
        HSSFFont fBold14 = (HSSFFont) fontSet.get("fBold14");
        HSSFFont fBold24 = (HSSFFont) fontSet.get("fBold24");
        HSSFFont fItalic = (HSSFFont) fontSet.get("fItalic");


        // Create styles
        HSSFCellStyle cs = wb.createCellStyle();
        cs.setFont( f );
        cs.setWrapText( true );
        cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        hm.put("cs", cs);

        HSSFCellStyle cs2 = wb.createCellStyle();
        cs2.setFont( f );
        cs2.setWrapText( true );
        cs2.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        //cs2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        //cs2.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        hm.put("cs2", cs2);

        HSSFCellStyle csB = wb.createCellStyle();
        csB.setFont( f );
        csB.setWrapText( true );
        csB.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csB.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csB.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        csB.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csB.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csB.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csB.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csB", csB);

        HSSFCellStyle csB2 = wb.createCellStyle();
        csB2.setFont( f );
        csB2.setWrapText( true );
        csB2.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csB2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csB2.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        //csB2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //csB2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csB2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //csB2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csB2", csB2);

        HSSFCellStyle csB3 = wb.createCellStyle();
        csB3.setFont( f );
        csB3.setWrapText( true );
        csB3.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csB3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csB3.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        csB3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //csB3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //csB3.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //csB3.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csB3", csB3);

        HSSFCellStyle csB4 = wb.createCellStyle();
        csB4.setFont( f );
        csB4.setWrapText( true );
        csB4.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csB4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csB4.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        csB4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csB4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //csB4.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //csB4.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csB4", csB4);

        HSSFCellStyle csB5 = wb.createCellStyle();
        csB5.setFont( f );
        csB5.setWrapText( true );
        csB5.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csB5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csB5.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        //csB5.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //csB5.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //csB5.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csB5.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csB5", csB5);

        HSSFCellStyle csRight = wb.createCellStyle();
        csRight.setFont( f );
        csRight.setWrapText( true );
        csRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csRight.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        hm.put("csRight", csRight);

        HSSFCellStyle csRightB = wb.createCellStyle();
        csRightB.setFont( f );
        csRightB.setWrapText( true );
        csRightB.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csRightB.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csRightB.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csRightB.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        //csRightB.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csRightB.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //csRightB.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //csRightB.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csRightB", csRightB);

        HSSFCellStyle csItalic = wb.createCellStyle();
        csItalic.setFont( fItalic );
        csItalic.setWrapText( true );
        csItalic.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csItalic.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csItalic.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        //csItalic.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csItalic.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csItalic.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csItalic.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csItalic", csItalic);

        HSSFCellStyle csItalicCenter = wb.createCellStyle();
        csItalicCenter.setFont( fItalic );
        csItalicCenter.setWrapText( true );
        csItalicCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csItalicCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csItalicCenter.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csItalicCenter.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        //csItalicCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csItalicCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csItalicCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csItalicCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csItalicCenter", csItalicCenter);

        HSSFCellStyle csLightYellow = wb.createCellStyle();
        csLightYellow.setFont( f );
        csLightYellow.setWrapText( true );
        csLightYellow.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csLightYellow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csLightYellow.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());
        csLightYellow.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        hm.put("csLightYellow", csLightYellow);

        HSSFCellStyle csCenter = wb.createCellStyle();
        csCenter.setFont( f );
        csCenter.setWrapText( true );
        csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csCenter.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csCenter.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        hm.put("csCenter", csCenter);

        HSSFCellStyle csCenterLT = wb.createCellStyle();
        csCenterLT.setFont( f );
        csCenterLT.setWrapText( true );
        csCenterLT.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csCenterLT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csCenterLT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csCenterLT.setFillForegroundColor(new HSSFColor.LIGHT_TURQUOISE().getIndex());
        hm.put("csCenterLT", csCenterLT);

        HSSFCellStyle csCenterY = wb.createCellStyle();
        csCenterY.setFont( f );
        csCenterY.setWrapText( true );
        csCenterY.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csCenterY.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csCenterY.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csCenterY.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());
        hm.put("csCenterY", csCenterY);

        HSSFCellStyle csCenterB = wb.createCellStyle();
        csCenterB.setFont( f );
        csCenterB.setWrapText( true );
        csCenterB.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csCenterB.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csCenterB.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csCenterB.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        csCenterB.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csCenterB.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csCenterB.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csCenterB.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csCenterB", csCenterB);

        HSSFCellStyle csCenterB2 = wb.createCellStyle();
        csCenterB2.setFont( fBold );
        csCenterB2.setWrapText( true );
        csCenterB2.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csCenterB2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csCenterB2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csCenterB2.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        //csCenterB2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csCenterB2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csCenterB2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csCenterB2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csCenterB2", csCenterB2);

        HSSFCellStyle csCenterB3 = wb.createCellStyle();
        csCenterB3.setFont( f );
        csCenterB3.setWrapText( true );
        csCenterB3.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csCenterB3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csCenterB3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csCenterB3.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        csCenterB3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csCenterB3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csCenterB3.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //csCenterB3.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csCenterB3", csCenterB3);

        HSSFCellStyle csBold = wb.createCellStyle();
        csBold.setFont( fBold );
        csBold.setWrapText( true );
        //csBold.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csBold.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csBold.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        csBold.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csBold.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csBold.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csBold.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csBold", csBold);

        HSSFCellStyle csBold2 = wb.createCellStyle();
        csBold2.setFont( fBold );
        csBold2.setWrapText( true );
        //csBold2.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        //csBold2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        //csBold2.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        hm.put("csBold2", csBold2);


        HSSFCellStyle csBold14 = wb.createCellStyle();
        csBold14.setFont( fBold14 );
        csBold14.setWrapText( true );
        //csBold14.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csBold14.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csBold14.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        //csBold14.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csBold14.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csBold14.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csBold14.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csBold14", csBold14);

        HSSFCellStyle csBoldCenter = wb.createCellStyle();
        csBoldCenter.setFont( fBold );
        csBoldCenter.setWrapText( true );
        csBoldCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csBoldCenter.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csBoldCenter.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        csBoldCenter.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csBoldCenter.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        csBoldCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csBoldCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csBoldCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csBoldCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csBoldCenter", csBoldCenter);

        HSSFCellStyle csBoldCenter24 = wb.createCellStyle();
        csBoldCenter24.setFont( fBold24 );
        csBoldCenter24.setWrapText( true );
        csBoldCenter24.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csBoldCenter24.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csBoldCenter24.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csBoldCenter24.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        csBoldCenter24.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csBoldCenter24.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csBoldCenter24.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //csBoldCenter24.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csBoldCenter24", csBoldCenter24);

        HSSFDataFormat format = wb.createDataFormat();

        HSSFCellStyle csDollar = wb.createCellStyle();
        csDollar.setFont( f );
        csDollar.setWrapText( true );
        csDollar.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csDollar.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csDollar.setDataFormat(format.getFormat("$#,##0.00"));
        csDollar.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csDollar.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        hm.put("csDollar", csDollar);

        HSSFCellStyle csDollarLT = wb.createCellStyle();
        csDollarLT.setFont( f );
        csDollarLT.setWrapText( true );
        csDollarLT.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csDollarLT.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csDollarLT.setDataFormat(format.getFormat("$#,##0.00"));
        csDollarLT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csDollarLT.setFillForegroundColor(new HSSFColor.LIGHT_TURQUOISE().getIndex());
        hm.put("csDollarLT", csDollarLT);

        HSSFCellStyle csDollarY = wb.createCellStyle();
        csDollarY.setFont( f );
        csDollarY.setWrapText( true );
        csDollarY.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csDollarY.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csDollarY.setDataFormat(format.getFormat("$#,##0.00"));
        csDollarY.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csDollarY.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());
        hm.put("csDollarY", csDollarY);

        HSSFCellStyle csDollarB = wb.createCellStyle();
        csDollarB.setFont( f );
        csDollarB.setWrapText( true );
        csDollarB.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csDollarB.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csDollarB.setDataFormat(format.getFormat("$#,##0.00"));
        csDollarB.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csDollarB.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        csDollarB.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csDollarB.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csDollarB.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csDollarB.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csDollarB", csDollarB);

        HSSFCellStyle csDollarBold = wb.createCellStyle();
        csDollarBold.setFont( fBold );
        csDollarBold.setWrapText( true );
        csDollarBold.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csDollarBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csDollarBold.setDataFormat(format.getFormat("$#,##0.00"));
        csDollarBold.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csDollarBold.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        csDollarBold.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csDollarBold.setFillForegroundColor(new HSSFColor.WHITE().getIndex());
        csDollarBold.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csDollarBold.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csDollarBold.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csDollarBold.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hm.put("csDollarBold", csDollarBold);

        HSSFCellStyle csDollarLightGreen = wb.createCellStyle();
        csDollarLightGreen.setFont( f );
        csDollarLightGreen.setWrapText( true );
        csDollarLightGreen.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csDollarLightGreen.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csDollarLightGreen.setDataFormat(format.getFormat("$#,##0.00"));
        csDollarLightGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csDollarLightGreen.setFillForegroundColor(new HSSFColor.LIGHT_GREEN().getIndex());
        hm.put("csDollarLightGreen", csDollarLightGreen);

        // ----
        return (hm);
    }


  // Generate the HTML response
  private void generateResponse(String outLine, HttpServletResponse response)
      throws IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    out.println("<html>");
    out.println("<head>");
    out.println("  <LINK href=\"css/styles.css\" type=text/css rel=STYLESHEET>");
    out.println("<title>SLTS Application PC Worksheet</title>");
    out.println("</head>");
    out.println("<body BGCOLOR=#DEC68B leftmargin=0 topmargin=0>");
    out.println("<form name=result method=post>");
    out.println(outLine);
    out.println("<script language=\"JavaScript\">\n");
    out.println("<!--\n");

    out.println("//-->\n");
    out.println("</script>\n");
    out.println("");
    out.println("</form>\n</body>\n</html>");

    out.close();
  }


}
