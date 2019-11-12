package com.sjkb.service;

import java.awt.Color;
import java.io.IOException;

import com.dropbox.core.v2.files.UploadUploader;
import com.sjkb.entities.ContactEntity;
import com.sjkb.entities.InvoiceEntity;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Writes PDF objects
 */

public class PdfDbxWriter {

	public void createInvoice(InvoiceEntity invoice, ContactEntity customer, ContactEntity agent, UploadUploader output) throws IOException {

		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);

		PDFont font = PDType1Font.HELVETICA_BOLD;

		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// get customer info


		String citystate = String.format("%s %s %s", customer.getCity(), customer.getState().toUpperCase(), customer.getZip());
		contentStream.setLeading(20);
		contentStream.setLineWidth(1);
		contentStream.beginText();
		contentStream.setFont(font, 12);
		contentStream.newLineAtOffset(25, 700);
		contentStream.showText(customer.getFirstname()+" "+customer.getLastname());
		contentStream.newLine();
		contentStream.showText(customer.getStreet());
		if (customer.getStreet2() != null && customer.getStreet2().length() > 2) {
		contentStream.newLine();
		contentStream.showText(customer.getStreet2());
		}
		contentStream.newLine();
		contentStream.showText(citystate);
		contentStream.newLine();
		contentStream.showText("Designer: "+agent.getFirstname());
		contentStream.endText();
		contentStream.setStrokingColor(Color.DARK_GRAY);
		contentStream.addRect(10, 420, 600, 200);
		contentStream.stroke();
	



		// Make sure that the content stream is closed:
		contentStream.close();

		// Save the results and ensure that the document is properly closed:
		document.save(output.getOutputStream());
		document.close();

	}

}