package gov.ymp.iats.control;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.*;
import java.util.*;
import java.text.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;

import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.html.HtmlWriter;

*/

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;

import gov.ymp.csi.db.*;

import gov.ymp.iats.model.SAReportPDF;

public class doPdfReport extends HttpServlet {

    /**
     * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
     */
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    	String outLine = "";
        try {
            
        	String porgId = request.getParameter("porgId");
        	String orgId = request.getParameter("orgId");
        	String year = request.getParameter("year");
        	
        	String sanumber = request.getParameter("sanumber");
        	String teamlead = request.getParameter("teamlead");
        	String satitle = request.getParameter("satitle");
        	String purpose = request.getParameter("purpose");
        	String scheduledate = request.getParameter("scheduledate");
        	String rescheduledate = request.getParameter("rescheduledate");
        	String signdate = request.getParameter("signdate");
        	String canceldate = request.getParameter("canceldate");
        	String cancelledrationale = request.getParameter("cancelledrationale");
        	String comment = request.getParameter("comment");
        	String condreport = request.getParameter("condreport");
        	String asstype = request.getParameter("asstype");
        	String assobj = request.getParameter("assobj");
        	String crlevels = request.getParameter("crlevels");
        	String crnums = request.getParameter("crnums");
        	String llnums = request.getParameter("llnums");
        	
        	if (porgId == null || porgId.trim().length() == 0) {
            	porgId = "0";
            }
            if (orgId == null || orgId.trim().length() == 0) {
            	orgId = "0";
            }
            if (year == null || year.trim().length() == 0) {
            	year = "0";
            }
            
        	//int collength = 16;
        	int collength = 0;
        	java.util.List<String> list = new ArrayList<String>();
        	
        	if (sanumber == null || sanumber.trim().length() == 0 || sanumber.equals("false")) {
        	}else{
        		list.add("sanumber");
        	}
        	if (teamlead == null || teamlead.trim().length() == 0 || teamlead.equals("false")) {
        	}else{
        		list.add("teamlead");
        	}
        	if (satitle == null || satitle.trim().length() == 0 || satitle.equals("false")) {
        	}else{
        		list.add("satitle");
        	}
        	if (purpose == null || purpose.trim().length() == 0 || purpose.equals("false")) {
        	}else{
        		list.add("purpose");
        	}
        	if (scheduledate == null || scheduledate.trim().length() == 0 || scheduledate.equals("false")) {
        	}else{
        		list.add("scheduledate");
        	}
        	if (rescheduledate == null || rescheduledate.trim().length() == 0 || rescheduledate.equals("false")) {
        	}else{
        		list.add("rescheduledate");
        	}
        	if (signdate == null || signdate.trim().length() == 0 || signdate.equals("false")) {
        	}else{
        		list.add("signdate");
        	}
        	if (canceldate == null || canceldate.trim().length() == 0 || canceldate.equals("false")) {
        	}else{
        		list.add("canceldate");
        	}
        	if (cancelledrationale == null || cancelledrationale.trim().length() == 0 || cancelledrationale.equals("false")) {
        	}else{
        		list.add("cancelledrationale");
        	}
        	if (comment == null || comment.trim().length() == 0 || comment.equals("false")) {
        	}else{
        		list.add("comment");
        	}
        	if (condreport == null || condreport.trim().length() == 0 || condreport.equals("false")) {
        	}else{
        		list.add("condreport");
        	}
        	if (asstype == null || asstype.trim().length() == 0 || asstype.equals("false")) {
        	}else{
        		list.add("asstype");
        	}
        	if (assobj == null || assobj.trim().length() == 0 || assobj.equals("false")) {
        	}else{
        		list.add("assobj");
        	}
        	if (crlevels == null || crlevels.trim().length() == 0 || crlevels.equals("false")) {
        	}else{
        		list.add("crlevels");
        	}
        	if (crnums == null || crnums.trim().length() == 0 || crnums.equals("false")) {
        	}else{
        		list.add("crnums");
        	}
        	if (llnums == null || llnums.trim().length() == 0 || llnums.equals("false")) {
        	}else{
        		list.add("llnums");
        	}
        	collength = list.size();
//System.out.println("list.size() = "+list.size());
        	Object ia[] = list.toArray();
//System.out.println("ia[0] = "+ia[0]);
        	
        	
            //Document document = new Document();
            Document document = new Document(PageSize.A4.rotate());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setPageEvent(new gov.ymp.iats.model.PdfHeaderFooter());
            
            document.open();
            PdfPTable table = new PdfPTable(collength);
        	DbConn myConn = new DbConn("csi");

        	try{
        		SAReportPDF sar = new SAReportPDF();		  
        		//table = sar.createTable(Integer.valueOf(porgId),Integer.valueOf(orgId),Integer.valueOf(year), myConn,table,ia);
        		document = sar.createContent(Integer.valueOf(porgId),Integer.valueOf(orgId),Integer.valueOf(year), myConn,table,ia,document);
        	}catch(Exception e){
        		outLine = outLine + "Exception caught: " + e.getMessage();
                System.out.println(outLine);
                //log(outLine);
                //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
        	}
        	
            //System.out.println("Status as of "+todaysdate);	//footer text
        	//document.add(table);
				//document.add(new Paragraph(new Date().toString()));
            document.close();
            
            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control",
                "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            // setting the content type
            response.setContentType("application/pdf");
            // the contentlength
            response.setContentLength(baos.size());
            // write ByteArrayOutputStream to the ServletOutputStream
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();
        }
        catch(DocumentException e) {
            //throw new IOException(e.getMessage());
        	outLine = outLine + "DocumentException caught: " + e.getMessage();
            System.out.println(outLine);
            //log(outLine);
            //gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
        }
    }

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 6067021675155015602L;
    
    /**
     * 
     */
    
}