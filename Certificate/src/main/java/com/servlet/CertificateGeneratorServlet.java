package com.servlet;

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/pp")
public class CertificateGeneratorServlet extends HttpServlet {
	
	private PdfPCell createCell(String text, Font font, int alignment) {
	    PdfPCell cell = new PdfPCell(new Paragraph(text, font));
	    cell.setHorizontalAlignment(alignment);
	    cell.setBorder(Rectangle.NO_BORDER); // Remove cell border if needed
	    return cell;
	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String course = request.getParameter("course");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"certificate.pdf\"");
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=\"certificate.pdf\"");

        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            
            // Add watermark to each page
            writer.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    try {
                        // Load watermark image
                        Image watermark = Image.getInstance(getServletContext().getRealPath("/img2/water.jpg"));
                        
                        // Set watermark position and transparency
                        watermark.setAbsolutePosition(200, 200); // Adjust position as needed
                        watermark.scaleToFit(500, 400); // Adjust size as needed
                        watermark.setTransparency(new int[]{0xFF, 0xFF, 0xFF, 0xDD, 0xDD, 0xDD});
                        PdfContentByte under = writer.getDirectContentUnder();
                        
                        // Add watermark to the page
                        under.addImage(watermark);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            document.open();
         
            Font titleFont = new Font(Font.FontFamily.HELVETICA,26,Font.BOLD);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 14); // Increase normal font size
            
            Image img = Image.getInstance(getServletContext().getRealPath("/img2/first.jpg"));
            img.scaleAbsolute(100f, 90f); // Set image size as needed
            
            document.add(img);
           
            Image img2 = Image.getInstance(getServletContext().getRealPath("/img2/second.jpg"));
            img2.scaleAbsolute(100f, 90f); // Set image size as needed
            img2.setAbsolutePosition(document.right() - 100f, document.top() - 90f);
            document.add(img2);
            
            
            
            // Add title to the document
            Paragraph title = new Paragraph("CERTIFICATE\n\n", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add content to the document
            Paragraph content1 = new Paragraph();
            content1.setAlignment(Element.ALIGN_CENTER);
            content1.add(new Paragraph("This Certificate is awarded to",normalFont));
       
            content1.add(new Paragraph(name, boldFont));
           
            content1.add(new Paragraph("For successfully attending an awareness program on",normalFont));
            content1.add(new Paragraph(course, boldFont));
            content1.add(new Paragraph("which provided comprehensive knowledge and understanding of ", normalFont));
            content1.add(new Paragraph("cybercrimes, their impact on individuals and society, and ", normalFont));
            content1.add(new Paragraph("measures to protect against them.", normalFont));
            content1.setSpacingBefore(30f); // Increase spacing before the paragraph
            document.add(content1);

            Paragraph content2 = new Paragraph();
            content2.setAlignment(Element.ALIGN_CENTER);
            content2.add(new Paragraph("A joint initiative by ", normalFont));
            content2.add(new Paragraph("Central Detective Training Institute, Jaipur and CyberVajra. ", normalFont));
            content2.add(new Paragraph("Organised on 17th July 2023 at St. Anselm's Sr. Sec. School, Mansarover, Jaipur.", normalFont));
            content2.setSpacingBefore(30f); // Increase spacing before the paragraph
            content2.setSpacingAfter(60f);
            document.add(content2);
            

            
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            // Add left-aligned and right-aligned content for each row
            table.addCell(createCell("Sh. B.L Sankhla, DySP", boldFont, Element.ALIGN_LEFT));
            table.addCell(createCell("Sh. Rajesh Kumar", boldFont, Element.ALIGN_RIGHT));

            table.addCell(createCell("Program Coordinator", normalFont, Element.ALIGN_LEFT));
            table.addCell(createCell("Vice Principal", normalFont, Element.ALIGN_RIGHT));

            table.addCell(createCell("C.D.T.I. Jaipur (Raj)", normalFont, Element.ALIGN_LEFT));
        
            table.addCell(createCell("C.D.T.I. Jaipur (Raj)", normalFont, Element.ALIGN_RIGHT));

            // Add the table to the document
            document.add(table);

            
           

            Paragraph content5 = new Paragraph();
            content5.setAlignment(Element.ALIGN_CENTER);
            content5.add(new Paragraph("CERTID:W-CAC-SA045", normalFont));
            content5.setSpacingBefore(10f); // Increase spacing before the paragraph
            document.add(content5);

            // Add border around the content
            float margin = -20f;
            Rectangle border = new Rectangle(document.left() + margin, document.bottom() + margin, document.right() - margin, document.top() - margin);
            border.setBorder(Rectangle.BOX);
            border.setBorderWidth(5);
            border.setBorderColor(com.itextpdf.text.BaseColor.PINK);
            border.setUseVariableBorders(true);
            document.add(border);
            
            
       

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
