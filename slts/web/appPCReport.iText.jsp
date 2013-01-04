<%@ page contentType="application/pdf" %><%@ page import="java.lang.*,java.io.*,java.util.*,
                                                                   gov.ymp.csi.db.*,gov.ymp.csi.misc.*,gov.ymp.csi.people.*,gov.ymp.csi.UNID.*,
                                                                   gov.ymp.csi.systems.*,gov.ymp.csi.auth.*,gov.ymp.csi.items.*,
                                                                   gov.ymp.slts.model.*,
                                                                   com.lowagie.text.*,com.lowagie.text.pdf.*,com.lowagie.text.rtf.*,org.json.*" 
%><%

String docRoot = getServletContext().getRealPath("/");

DbConn myConn = new DbConn("csi");
Computer [] list = null;



String command = ((request.getParameter("command")  != null) ? (String) request.getParameter("command") : "view");
String reporttype = ((request.getParameter("reporttype")  != null) ? (String) request.getParameter("reporttype") : "pdf");
String [] appList = request.getParameterValues("applist");

String reportTitle = "";

java.util.Date now = new java.util.Date();
String nowS = Utils.dateToString(now, "MM/dd/yyyy HH:mm:ss");


// step 1: creation of a document-object
//Document document = new Document(PageSize.LETTER.rotate(), 10, 10, 10, 10);
Document document = new Document(PageSize.LETTER, 10, 10, 10, 10);

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

reportTitle = "Computers With Selected Software Installed";
list = Computer.getItemList(myConn, true, null, appList);

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

int numColumns = 6;
//para = new Paragraph(reportTitle + "\n\n", f1);
//para.setAlignment(Element.ALIGN_CENTER);
//document.add(para);

int headerwidths[] = { 10, 20, 20, 30, 10, 30 };
PdfPTable table = new PdfPTable(numColumns);

table.setWidthPercentage(100); // percentage
table.setWidths(headerwidths);
table.getDefaultCell().setBorderWidth(0);
//PdfPCell cell = new PdfPCell(new Paragraph(reportTitle));
PdfPCell cell = null;
//cell.setColspan(numColumns);
//table.addCell(cell);
para = new Paragraph("ID", f2);
table.addCell(para);
para = new Paragraph("Name", f2);
table.addCell(para);
para = new Paragraph("Domain", f2);
table.addCell(para);
para = new Paragraph("Manufacturer", f2);
table.addCell(para);
para = new Paragraph("Model", f2);
table.addCell(para);
para = new Paragraph("Product Name", f2);
table.addCell(para);

if (list.length == 0) {
    cell = new PdfPCell(new Paragraph("Empty Report", f3));
    cell.setColspan(numColumns);
    table.addCell(cell);
}
for (int i=0; i<list.length; i++) {
    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
    table.addCell(new Paragraph(Integer.toString(list[i].getComputerID()), f3));
    table.addCell(new Paragraph(list[i].getName(), f3));
    table.addCell(new Paragraph(list[i].getDomainName(), f3));
    table.addCell(new Paragraph(list[i].getManuf(), f3));
    table.addCell(new Paragraph(((list[i].getModelNum() != null) ? list[i].getModelNum() : " "), f3));
    table.addCell(new Paragraph(list[i].getProdName(), f3));
}
document.add(table);

String temp = "";
// Add list of selected applications
//table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//table.getDefaultCell().setBorderWidth(0);
//cell = new PdfPCell(new Paragraph("\n\n\nSelected Applications", f2));
//cell.setColspan(numColumns);
//table.addCell(cell);

//for (int i=0; i<appList.length; i++) {
//    //table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//    cell = new PdfPCell(new Paragraph(appList[i], f3));
//    cell.setColspan(numColumns);
//    table.addCell(cell);
//}

//document.add(table);

para = new Paragraph("\n\n\nSelected Applications", f2);
document.add(para);

for (int i=0; i<appList.length; i++) {
    //table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
    temp += appList[i] + "\n";
}

para = new Paragraph(temp, f3);
document.add(para);



// step 5: we close the document
document.close();

// step 6: we output the writer as bytes to the response output
DataOutput output = new DataOutputStream( response.getOutputStream() );
byte[] bytes = null;
bytes = buffer.toByteArray();
response.setContentLength(bytes.length);
for( int i = 0; i < bytes.length; i++ ) { output.writeByte( bytes[i] ); }
%>