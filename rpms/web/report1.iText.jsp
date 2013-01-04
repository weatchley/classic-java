<%@ page contentType="application/vnd.ms-excel" %><%@ page import="java.lang.*,java.io.*,java.util.*,
                                                                   gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,
                                                                   gov.ymp.csi.systems.*,gov.ymp.csi.auth.*,gov.ymp.csi.items.*,
                                                                   gov.ymp.rpms.model.*,
                                                                   com.lowagie.text.*,com.lowagie.text.pdf.*,com.lowagie.text.rtf.*,org.json.*" 
%><%

String docRoot = getServletContext().getRealPath("/");

DbConn myConn = new DbConn("csi");
RecordSeries [] list = null;
FOrganization loc = null;



String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
String reporttype = ((request.getParameter("reporttype")  != null) ? (String) request.getParameter("reporttype") : "pdf");
String archiveflag = ((request.getParameter("archiveflag")  != null) ? (String) request.getParameter("archiveflag") : "false");
boolean doArchive = ((archiveflag.equals("true")) ? true : false);
String displayflag = ((request.getParameter("displayflag")  != null) ? (String) request.getParameter("displayflag") : "true");
boolean doDisplay = ((displayflag.equals("true")) ? true : false);
String commentText = ((request.getParameter("commenttext")  != null) ? (String) request.getParameter("commenttext") : "Archive");
String [] locString = request.getParameterValues("location");

String reportTitle = "";

java.util.Date now = new java.util.Date();
String nowS = Utils.dateToString(now, "MM/dd/yyyy HH:mm:ss");

int locID = 0;
boolean dispLocation = true;

if (locString[0] != null && !locString[0].equals("")) {
    locID = Integer.parseInt(locString[0]);
    if (locID != 0) {
        loc = new FOrganization(locID, myConn);
        dispLocation = false;
    }
}

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

Font f1 = new Font(Font.HELVETICA, 12, Font.BOLD);
Font f2 = new Font(Font.HELVETICA, 8, Font.BOLD);
Font f3 = new Font(Font.HELVETICA, 8, Font.NORMAL);
Font f4 = new Font(Font.HELVETICA, 6, Font.NORMAL);

Paragraph para = null;

//System.out.println("report1.jsp - Got Here 2" + " command: " + command);
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

int numColumns = 0;
//para = new Paragraph(reportTitle + "\n\n", f1);
//para.setAlignment(Element.ALIGN_CENTER);
//document.add(para);

int headerwidths[] = null;
int headerwidths1[] = { 20, 20, 20, 20, 25, 16, 20, 20, 12, 8, 8, 8, 8, 20, 9 };
int headerwidths2[] = {     20, 20, 20, 25, 16, 20, 20, 12, 8, 8, 8, 8, 20, 9 };
if (dispLocation) {
    headerwidths = headerwidths1;
    numColumns = 15;
} else {
    headerwidths = headerwidths2;
    numColumns = 14;
}
PdfPTable table = new PdfPTable(numColumns);
table.setWidthPercentage(100); // percentage
table.setWidths(headerwidths);
table.getDefaultCell().setBorderWidth(0);
//PdfPCell cell = new PdfPCell(new Paragraph(reportTitle));
PdfPCell cell = null;
//cell.setColspan(numColumns);
//table.addCell(cell);
if (dispLocation) {
    para = new Paragraph("Location", f2);
    table.addCell(para);
}
para = new Paragraph("Retention Code\n(Pending)", f2);
table.addCell(para);
para = new Paragraph("Common Name", f2);
table.addCell(para);
para = new Paragraph("Record Series", f2);
table.addCell(para);
para = new Paragraph("Citation", f2);
table.addCell(para);
para = new Paragraph("Quality Designation", f2);
table.addCell(para);
para = new Paragraph("Access Restrictions", f2);
table.addCell(para);
para = new Paragraph("Requirement", f2);
table.addCell(para);
para = new Paragraph("General", f2);
table.addCell(para);
para = new Paragraph("Perm", f2);
table.addCell(para);
para = new Paragraph("Long-Term", f2);
table.addCell(para);
para = new Paragraph("Short-Term", f2);
table.addCell(para);
para = new Paragraph("Vital", f2);
table.addCell(para);
para = new Paragraph("Unscheduled\nDo Not Destroy", f2);
table.addCell(para);
table.setHeaderRows(1);
para = new Paragraph("Freeze", f2);
table.addCell(para);

if (list.length == 0) {
    cell = new PdfPCell(new Paragraph("Empty Report", f3));
    cell.setColspan(numColumns);
    table.addCell(cell);
}
for (int i=0; i<list.length; i++) {
    Crosswalk [] cw = list[i].getCrosswalkSet();
    if (cw.length == 0) {
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        if (dispLocation) {
            table.addCell(new Paragraph(list[i].getLocationDescription(), f3));
        }
        table.addCell(new Paragraph(list[i].getRetentionCode(), f3));
        para = new Paragraph("n/a", f3);
        table.addCell(para);
        table.addCell(new Paragraph(list[i].getDescription(), f3));
        para = new Paragraph(list[i].getCitationDescription() + "\nCutoff: " + list[i].getCutoffDescription() + "\nRetention: " + list[i].getRetentionPeriodDescription(), f3);
        table.addCell(para);
        para = new Paragraph("n/a", f3);
        table.addCell(para);
        para = new Paragraph("n/a", f3);
        table.addCell(para);
        para = new Paragraph("n/a", f3);
        table.addCell(para);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        para = new Paragraph(((list[i].getCommonFlag().equals("T")) ? "X" : ""), f3);
        table.addCell(para);
        para = new Paragraph(((list[i].getRetentionGroupFlagCode().equals("P")) ? "X" : ""), f3);
        table.addCell(para);
        para = new Paragraph(((list[i].getRetentionGroupFlagCode().equals("L")) ? "X" : ""), f3);
        table.addCell(para);
        para = new Paragraph(((list[i].getRetentionGroupFlagCode().equals("S")) ? "X" : ""), f3);
        table.addCell(para);
        para = new Paragraph("", f3);
        table.addCell(para);
        para = new Paragraph("", f3);
        table.addCell(para);
        para = new Paragraph("", f3);
        table.addCell(para);
    } else {
        for (int j=0; j<cw.length; j++) {
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            if (dispLocation) {
                table.addCell(new Paragraph(((j==0) ? list[i].getLocationDescription() : ""), f3));
            }
            para = new Paragraph(((j==0) ? list[i].getRetentionCode() : ""), f3);
            table.addCell(para);
            para = new Paragraph(cw[j].getName(), f3);
            table.addCell(para);
            para = new Paragraph(((j==0) ? list[i].getDescription() : ""), f3);
            table.addCell(para);
            para = new Paragraph(((j==0) ? list[i].getCitationDescription() + "\nCutoff: " + list[i].getCutoffDescription() + "\nRetention: " + list[i].getRetentionPeriodDescription() : ""), f3);
            table.addCell(para);
            para = new Paragraph(cw[j].getQADesignation(), f3);
            table.addCell(para);
            para = new Paragraph(((cw[j].getAccessRestrictions() != null) ? cw[j].getAccessRestrictions() : "None"), f3);
            table.addCell(para);
            para = new Paragraph(cw[j].getRequirements() + ((cw[j].getRequirementsOther() != null) ? "\n" + cw[j].getRequirementsOther() : ""), f3);
            table.addCell(para);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            para = new Paragraph(((list[i].getCommonFlag().equals("T")) ? "X" : ""), f3);
            table.addCell(para);
            para = new Paragraph(((list[i].getRetentionGroupFlagCode().equals("P")) ? "X" : ""), f3);
            table.addCell(para);
            para = new Paragraph(((list[i].getRetentionGroupFlagCode().equals("L")) ? "X" : ""), f3);
            table.addCell(para);
            para = new Paragraph(((list[i].getRetentionGroupFlagCode().equals("S")) ? "X" : ""), f3);
            table.addCell(para);
            para = new Paragraph(((cw[j].getVitalRecord().equals("T")) ? "X" : ""), f3);
            table.addCell(para);
            para = new Paragraph(((cw[j].getUnscheduled().equals("T")) ? "X" : ""), f3);
            table.addCell(para);
            //para = new Paragraph(((cw[j].getFreezeHoldFlag().equals("T")) ? "X" : ""), f3);
            para = new Paragraph(((cw[j].getFreezeHoldFlag().equals("T")) ? cw[j].getFreezeHoldFlagText() : ""), f3);
            table.addCell(para);
        }
    }
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