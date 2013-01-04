<%@
page import="java.io.*,
			 com.lowagie.text.*,
			 com.lowagie.text.pdf.*,
			 com.lowagie.text.rtf.*,
			 java.io.*,
			 java.util.*,
			 gov.ymp.nacrd.*,
			 javax.naming.*,gov.
			 ymp.util.db.*"
%><%
//
// Template JSP file for iText
// by Tal Liron
//

//response.setContentType( "application/pdf" );
//response.setContentType( "application/rtf" );
response.setContentType( "application/msword" );

// step 1: creation of a document-object
Document document = new Document();

// step 2:
// we create a writer that listens to the document
// and directs a PDF-stream to a temporary buffer

ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//PdfWriter.getInstance( document, buffer );
RtfWriter.getInstance( document, buffer );

// step 3: we open the document
document.open();

// ---
String command = request.getParameter("command");
String docnumb = request.getParameter("docnumb");
Doc myDoc = new Doc(docnumb);
CmtRsp[] cmts = CmtRsp.commentList(myDoc.getID());


// step 4: we add a paragraph to the document
//document.add(new Paragraph("Hello World"));
document.add(new Paragraph("Document Number: " + myDoc.getNumber() ));
document.add(new Paragraph("Document Title: " + myDoc.getTitle() ));
document.add(new Paragraph("Comment Count: " + cmts.length));
for (int i=0; i<cmts.length; i++) {
    document.add(new Paragraph("-----------------------" ));
    document.add(new Paragraph("Comment ID: " + cmts[i].getID() ));
    document.add(new Paragraph("Comment Number: " + cmts[i].getNumber() + "-" + cmts[i].getID() ));
    document.add(new Paragraph("Commentor: " + User.getFullName(cmts[i].getUser()) ));
    document.add(new Paragraph("Org: " + Org.getName(cmts[i].getOrg()) ));
    String referNumber = "";
    if (cmts[i].getRefersTo() !=0) {
        CmtRsp myCmt = new CmtRsp(cmts[i].getRefersTo());
        referNumber = myCmt.getNumber();
    }
    document.add(new Paragraph("Refers To: " + ((cmts[i].getRefersTo() != 0) ? Integer.toString(cmts[i].getRefersTo()) + " (" + referNumber + ")" : "n/a") ));
    document.add(new Paragraph("Date Submitted: " + cmts[i].getDateSubmitted() ));
    document.add(new Paragraph("Responder: " + ((cmts[i].getResponder() != 0) ? User.getFullName(cmts[i].getResponder()) : "n/a") ));
    String myComment = cmts[i].getComment();
    //myComment = myComment.replaceAll("\n", "<br>\n");
    //myComment = myComment.replaceAll("  ", " &nbsp;");
    int len = myComment.length();
    document.add(new Paragraph("Comment: " + " Length: " + len + ", Text: " + myComment ));
    document.add(new Paragraph("Response: " + ((cmts[i].getResponse() != null) ? cmts[i].getResponse() : "n/a") ));
}
document.add(new Paragraph(" "));
document.add(new Paragraph("-----------------------" ));
document.add(new Paragraph(" "));
document.add(new Paragraph("Report Finished"));
//document.add(new Paragraph("" +  ));


// step 5: we close the document
document.close();

// step 6: we output the writer as bytes to the response output
DataOutput output = new DataOutputStream( response.getOutputStream() );
byte[] bytes = buffer.toByteArray();
response.setContentLength(bytes.length);
for( int i = 0; i < bytes.length; i++ ) { output.writeByte( bytes[i] ); }
%>