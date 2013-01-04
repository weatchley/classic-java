package gov.ymp.iats.model;
 
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.text.*;
 
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
 
public class PdfHeaderFooter extends PdfPageEventHelper {
 
	/** The Phrase that will be added as the header of the document. */
	protected Phrase header;
 
	/** The PdfPTable that will be added as the footer of the document. */
	//protected PdfPTable footer;
	protected Phrase footer;
 
	/** The PdfTemplate that contains the total number of pages. */
	protected PdfTemplate total;
 
	/** The font that will be used. */
	protected BaseFont helv;

	
	/**
	 * Constructs an Event that adds a Header and a Footer.
	 */
	public PdfHeaderFooter() {
		header = new Phrase("");
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	String dateStr = "";
    	//get today's date
    	Calendar calendar = Calendar.getInstance();
        String todaysdate = "";
        try {
            todaysdate = formatter.format(calendar.getTime());
            footer = new Phrase(todaysdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
		/*
		footer = new PdfPTable(4);
		footer.setTotalWidth(300);
		footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		footer.addCell(new Phrase(new Chunk("First Page")
				.setAction(new PdfAction(PdfAction.FIRSTPAGE))));
		footer.addCell(new Phrase(new Chunk("Prev Page")
				.setAction(new PdfAction(PdfAction.PREVPAGE))));
		footer.addCell(new Phrase(new Chunk("Next Page")
				.setAction(new PdfAction(PdfAction.NEXTPAGE))));
		footer.addCell(new Phrase(new Chunk("Last Page")
				.setAction(new PdfAction(PdfAction.LASTPAGE))));
		*/
	}
 
	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onOpenDocument(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(100, 100);
		total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
		try {
			helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
					BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}
	
	
	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onEndPage(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		Phrase pageNumber = new Phrase("Page: " + writer.getPageNumber());
		if (document.getPageNumber() > 1) {
			ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header,
					(document.right() - document.left()) / 2
							+ document.leftMargin(), document.top() + 10, 0);
		}
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,
				(document.left() + document.leftMargin()), document.bottom() - 10, 0);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, pageNumber,
				(document.right() + document.rightMargin()) - 50, document.bottom() - 10, 0);
		/*
		footer.writeSelectedRows(0, -1,
				(document.right() - document.left() - 300) / 2
						+ document.leftMargin(), document.bottom() - 10, cb);
		*/
	}

	/*
	
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		cb.saveState();
		String text = "Page " + writer.getPageNumber() + " of ";
		float textBase = document.bottom() - 20;
		float textSize = helv.getWidthPoint(text, 12);
		cb.beginText();
		cb.setFontAndSize(helv, 12);
		if ((writer.getPageNumber() % 2) == 1) {
			cb.setTextMatrix(document.left(), textBase);
			cb.showText(text);
			cb.endText();
			cb.addTemplate(total, document.left() + textSize, textBase);
		}
		// for even numbers, show the footer at the right
		else {
			float adjust = helv.getWidthPoint("0", 12);
			cb.setTextMatrix(document.right() - textSize - adjust, textBase);
			cb.showText(text);
			cb.endText();
			cb.addTemplate(total, document.right() - adjust, textBase);
		}
		cb.restoreState();
	}
	
	*/
	
	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onCloseDocument(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	/*
	public void onCloseDocument(PdfWriter writer, Document document) {
		total.beginText();
		total.setFontAndSize(helv, 12);
		total.setTextMatrix(0, 0);
		total.showText(String.valueOf(writer.getPageNumber() - 1));
		total.endText();
	}
	*/
	
	
	/**
	 * Generates a file with a header and a footer.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 14: Header Footer Example");
		System.out.println("-> Creates a PDF file with a header and a footer.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   header_footer.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("header_footer.pdf"));
			writer.setViewerPreferences(PdfWriter.PageLayoutTwoColumnLeft);
			writer.setPageEvent(new PdfHeaderFooter());
			document.setMargins(36, 36, 54, 72);
			// step 3:
			document.open();
			// step 4: we grab the ContentByte and do some stuff with it
			for (int k = 1; k <= 300; ++k) {
				document.add(new Phrase(					"Quick brown fox jumps over the lazy dog. "));
			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}
}