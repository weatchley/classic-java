<%@ page contentType="application/vnd.ms-excel" %><%@ page import="java.lang.*,java.io.*,java.util.*,
                                                                   gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,
                                                                   gov.ymp.csi.systems.*,gov.ymp.csi.auth.*,gov.ymp.csi.items.*,
                                                                   gov.ymp.rpms.model.*,
                                                                   com.lowagie.text.*,com.lowagie.text.pdf.*,com.lowagie.text.rtf.*,org.json.*" 
%><%

String docRoot = getServletContext().getRealPath("/");

DbConn myConn = new DbConn("csi");
RecordSeries rec = null;



String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
String reporttype = ((request.getParameter("reporttype")  != null) ? (String) request.getParameter("reporttype") : "pdf");
String archiveflag = ((request.getParameter("archiveflag")  != null) ? (String) request.getParameter("archiveflag") : "false");
boolean doArchive = ((archiveflag.equals("true")) ? true : false);
String displayflag = ((request.getParameter("displayflag")  != null) ? (String) request.getParameter("displayflag") : "true");
boolean doDisplay = ((displayflag.equals("true")) ? true : false);
String commentText = ((request.getParameter("commenttext")  != null) ? (String) request.getParameter("commenttext") : "Archive");
String IDs = ((request.getParameter("id")  != null) ? (String) request.getParameter("id") : "0");
int id = Integer.parseInt(IDs);

String reportTitle = "";

java.util.Date now = new java.util.Date();
String nowS = Utils.dateToString(now, "MM/dd/yyyy HH:mm:ss");


// step 1: creation of a document-object
Document document = new Document(PageSize.LETTER.rotate(), 10, 10, 10, 10);

// step 2:
// we create a writer that listens to the document
// and directs a PDF-stream to a temporary buffer

ByteArrayOutputStream buffer = new ByteArrayOutputStream();
if (reporttype.equals("pdf")) {
    response.setContentType( "application/pdf" );
    PdfWriter.getInstance( document, buffer );
} else if (reporttype.equals("rtf")) {
    response.setContentType( "application/rtf" );
    response.setContentType( "application/msword" );
    RtfWriter2.getInstance( document, buffer );
}

Font f1 = new Font(Font.HELVETICA, 14, Font.BOLD);
Font f2 = new Font(Font.HELVETICA, 10, Font.NORMAL);
Font f3 = new Font(Font.HELVETICA, 10, Font.BOLD);
Font f4 = new Font(Font.HELVETICA, 8, Font.NORMAL);

Paragraph para = null;

//System.out.println("report1.jsp - Got Here 2" + " command: " + command);
rec = new RecordSeries(id, myConn);
reportTitle = "Record Series Report for " + rec.getDescription();

HeaderFooter header = new HeaderFooter(new Phrase("\n" + reportTitle, f1), false);
header.setAlignment(Element.ALIGN_CENTER);
document.setHeader(header);

HeaderFooter footer = new HeaderFooter(new Phrase("Report generated on: " + nowS + "\nPage number ",f4), new Phrase(".", f4));
footer.setAlignment(Element.ALIGN_CENTER);
document.setFooter(footer);

// step 3: we open the document
document.open();


// step 4: we add a paragraph to the document
//document.add(new Paragraph("Hello World"));

myConn.release();

int numColumns = 2;

PdfPTable table = new PdfPTable(numColumns);
PdfPTable table2 = new PdfPTable(numColumns);

int headerwidths[] = { 20, 90 }; // percentage
table.setWidthPercentage(100); // percentage
table.setWidths(headerwidths);
table.getDefaultCell().setBorderWidth(0);

table2.setWidthPercentage(100); // percentage
table2.setWidths(headerwidths);
table2.getDefaultCell().setBorderWidth(0);

//PdfPCell cell = new PdfPCell(new Paragraph(reportTitle));
PdfPCell cell = null;
//cell.setColspan(numColumns);
//table.addCell(cell);
para = new Paragraph("Description: ", f2);
table.addCell(para);
para = new Paragraph(rec.getDescription(), f2);
table.addCell(para);
para = new Paragraph("Common: ", f2);
table.addCell(para);
para = new Paragraph(((rec.getCommonFlag().equals("T")) ? "True" : "False"), f2);
table.addCell(para);
para = new Paragraph("OCRWM Code: ", f2);
table.addCell(para);
para = new Paragraph(rec.getOCRWMCode(), f2);
table.addCell(para);
para = new Paragraph("Citation Category: ", f2);
table.addCell(para);
para = new Paragraph(rec.getCitationDescription(), f2);
table.addCell(para);
para = new Paragraph("Cutoff: ", f2);
table.addCell(para);
para = new Paragraph(rec.getCutoffDescription(), f2);
table.addCell(para);
para = new Paragraph("Retention Period: ", f2);
table.addCell(para);
para = new Paragraph(rec.getRetentionPeriodDescription(), f2);
table.addCell(para);
para = new Paragraph("Retention Group Flag: ", f2);
table.addCell(para);
para = new Paragraph(rec.getRetentionGroupFlagDescription(), f2);
table.addCell(para);
para = new Paragraph("Retention Code: ", f2);
table.addCell(para);
para = new Paragraph(rec.getRetentionCode(), f2);
table.addCell(para);
para = new Paragraph("Date Added: ", f2);
table.addCell(para);
para = new Paragraph(Utils.dateToString(rec.getDateAdded()), f2);
table.addCell(para);
para = new Paragraph("Date Changed", f2);
table.addCell(para);
para = new Paragraph(Utils.dateToString(rec.getDateChanged()), f2);
table.addCell(para);
para = new Paragraph("Crosswalk: ", f2);
table.addCell(para);
//para = new Paragraph(, f2);
//table.addCell(para);

Crosswalk [] cw = rec.getCrosswalkSet();

if (cw.length == 0) {
    para = new Paragraph("None entered", f2);
    table.addCell(para);
} else {
    for (int i=0; i<cw.length; i++) {
        para = new Paragraph("Name: ", f2);
        table2.addCell(para);
        para = new Paragraph(cw[i].getName(), f2);
        table2.addCell(para);
        para = new Paragraph("QA Designation: ", f2);
        table2.addCell(para);
        para = new Paragraph(cw[i].getQADesignation(), f2);
        table2.addCell(para);
        para = new Paragraph("Access Restrictions: ", f2);
        table2.addCell(para);
        para = new Paragraph(cw[i].getAccessRestrictions(), f2);
        table2.addCell(para);
        para = new Paragraph("Unscheduled: ", f2);
        table2.addCell(para);
        para = new Paragraph(((cw[i].getUnscheduled().equals("T")) ? "True" : "False"), f2);
        table2.addCell(para);
        para = new Paragraph("Requirements: ", f2);
        table2.addCell(para);
        para = new Paragraph(cw[i].getRequirements(), f2);
        table2.addCell(para);
        para = new Paragraph("Requirements Other: ", f2);
        table2.addCell(para);
        para = new Paragraph(cw[i].getRequirementsOther(), f2);
        table2.addCell(para);
        para = new Paragraph("Freeze Hold Flag: ", f2);
        table2.addCell(para);
        para = new Paragraph(((cw[i].getFreezeHoldFlag().equals("T")) ? "True" : "False"), f2);
        table2.addCell(para);
        para = new Paragraph("Freeze Hold Text: ", f2);
        table2.addCell(para);
        para = new Paragraph(cw[i].getFreezeHoldFlagText(), f2);
        table2.addCell(para);
        para = new Paragraph("Vital Record Flag: ", f2);
        table2.addCell(para);
        para = new Paragraph(((cw[i].getVitalRecord().equals("T")) ? "True" : "False"), f2);
        table2.addCell(para);
        para = new Paragraph("Date Added: ", f2);
        table2.addCell(para);
        para = new Paragraph(Utils.dateToString(cw[i].getDateAdded()), f2);
        table2.addCell(para);
        para = new Paragraph("Date Changed: ", f2);
        table2.addCell(para);
        para = new Paragraph(Utils.dateToString(cw[i].getDateChanged()), f2);
        table2.addCell(para);
        para = new Paragraph(" ", f2);
        table2.addCell(para);
        para = new Paragraph("_____________________________", f2);
        para.setAlignment(Element.ALIGN_CENTER);
        cell = new PdfPCell(para);
        table2.addCell(para);
    }
    table.addCell(table2);
    
}
document.add(table);

// step 5: we close the document
document.close();

// step 6: we output the writer as bytes to the response output
DataOutput output = new DataOutputStream( response.getOutputStream() );
byte[] bytes = null;
if (doArchive && reporttype.equals("pdf")) {
    myConn = new DbConn("csi");
    Archive arc = new Archive(reportTitle, now, commentText, myConn, 0);
    arc.setImage(myConn, buffer, 0);
    bytes = arc.getImageBytes(myConn);
    myConn.release();
} else {
    bytes = buffer.toByteArray();
}
if (doDisplay) {
    response.setContentLength(bytes.length);
    for( int i = 0; i < bytes.length; i++ ) { output.writeByte( bytes[i] ); }
}
%>