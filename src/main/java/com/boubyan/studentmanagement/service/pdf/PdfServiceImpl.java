package com.boubyan.studentmanagement.service.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.boubyan.studentmanagement.entity.Course;

@Service
public class PdfServiceImpl implements PdfService {

    private static final float MARGIN = 50;
    private static final float Y_START = 750;
    private static final float CELL_MARGIN = 5;
    private static final float TABLE_ROW_HEIGHT = 20;
    private static final float[] COLUMN_WIDTHS = { 100, 200, 120, 60, 60 };
    private static final String[] HEADERS = { "Course Name", "Description", "Instructor Name", "Duration", "Date" };

    public ByteArrayInputStream generateCoursePdf(List<Course> courses) {

        if (CollectionUtils.isEmpty(courses)) {
            return new ByteArrayInputStream(new byte[0]);
        }
        try {
            PDDocument document = new PDDocument();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(font, 12);
            float yPosition = Y_START;

            // Draw header
            drawRow(contentStream, yPosition, HEADERS);
            yPosition -= TABLE_ROW_HEIGHT;

            contentStream.setFont(font, 12);

            // Draw rows
            for (Course course : courses) {
                String[] row = {
                        course.getName(),
                        course.getDescription(),
                        course.getInstructorName(),
                        String.valueOf(course.getDuration()),
                        course.getDate().toString()
                };

                if (yPosition < MARGIN) {
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = Y_START;
                    drawRow(contentStream, yPosition, HEADERS);
                    yPosition -= TABLE_ROW_HEIGHT;
                    contentStream.setFont(font, 12);
                }

                drawRow(contentStream, yPosition, row);
                yPosition -= TABLE_ROW_HEIGHT;
            }
            contentStream.close();
            document.save(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void drawRow(PDPageContentStream contentStream, float yPosition, String[] cells) throws IOException {
        float xPosition = MARGIN;

        for (int i = 0; i < cells.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(xPosition + CELL_MARGIN, yPosition - 15);
            contentStream.showText(cells[i]);
            contentStream.endText();
            xPosition += COLUMN_WIDTHS[i];
        }
    }
}
