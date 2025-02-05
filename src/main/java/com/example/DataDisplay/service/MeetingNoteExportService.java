package com.example.DataDisplay.service;

import com.example.DataDisplay.model.MeetingNote;
import com.example.DataDisplay.repository.MeetingNoteRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class MeetingNoteExportService {

    @Autowired
    private MeetingNoteRepository meetingNoteRepository;

    public void exportAsCSV(Long companyId, HttpServletResponse response) throws IOException {
        List<MeetingNote> notes = meetingNoteRepository.findMeetingNotesWithCompanyName(companyId);

        if (notes.isEmpty()) {
            System.out.println("No notes found for companyId: " + companyId);
            return;
        }

        String companyName = notes.getFirst().getCompany().getCompany(); // Get the company name from the first note

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" + companyName.replace(" ", "_") + "_meeting_notes.csv");

        StringBuilder csvData = new StringBuilder();

        // Add Title (Company Report)
        csvData.append("\"").append(companyName).append(" Report\"\n\n");

        // Table Headers
        csvData.append("\"ID\",\"Note\",\"Timestamp\"\n");

        // Table Data
        for (MeetingNote note : notes) {
            csvData.append("\"").append(note.getId()).append("\",")
                    .append("\"").append(escapeCSV(note.getNote())).append("\",")
                    .append("\"").append(note.getTimestamp()).append("\"\n");
        }

        response.getWriter().write(csvData.toString());
    }

    // Helper method to escape CSV values correctly
    private String escapeCSV(String value) {
        if (value == null) return "";
        value = value.replace("\"", "\"\""); // Escape double quotes
        return value.contains(",") || value.contains("\n") || value.contains("\"") ? "\"" + value + "\"" : value;
    }



    public void exportAsExcel(Long companyId, HttpServletResponse response) throws IOException {
        List<MeetingNote> notes = meetingNoteRepository.findMeetingNotesWithCompanyName(companyId);

        if (notes.isEmpty()) {
            System.out.println("No notes found for companyId: " + companyId);
            return;
        }

        String companyName = notes.getFirst().getCompany().getCompany(); // Get the company name from the first note

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + companyName.replace(" ", "_") + "_meeting_notes.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Meeting Notes");

        // Header Row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Note", "Timestamp"}; // Removed Company column
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Data Rows
        int rowIndex = 1;
        for (MeetingNote note : notes) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(note.getId());
            row.createCell(1).setCellValue(note.getNote());
            row.createCell(2).setCellValue(note.getTimestamp().toString());
        }

        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
    }

    public void exportAsPDF(Long companyId, HttpServletResponse response) throws IOException, DocumentException {

        List<MeetingNote> notes = meetingNoteRepository.findMeetingNotesWithCompanyName(companyId);

        if (notes.isEmpty()) {
            System.out.println("No notes found for companyId: " + companyId);
            return;
        }

        String companyName = notes.getFirst().getCompany().getCompany();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=meeting_notes.pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Title with Company Name
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph(companyName + " Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Table setup (3 columns: ID, Note, Timestamp)
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.setWidths(new float[]{1.5f, 5f, 3f}); // Adjusted widths

        // Header Font
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

        // Table Headers
        String[] headers = {"ID", "Note", "Timestamp"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);
        }

        // Table Data Font
        Font dataFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

        // Add rows
        for (MeetingNote note : notes) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(note.getId()), dataFont)));

            // Wrapping long text for "Note"
            PdfPCell noteCell = new PdfPCell(new Phrase(note.getNote(), dataFont));
            noteCell.setNoWrap(false);
            table.addCell(noteCell);

            // Formatting timestamp
            PdfPCell timestampCell = new PdfPCell(new Phrase(note.getTimestamp().toString(), dataFont));
            timestampCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(timestampCell);
        }

        document.add(table);

        // Footer
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);
        Paragraph footer = new Paragraph("Generated on: " + new java.util.Date(), footerFont);
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);

        document.close();
    }
}