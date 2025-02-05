package com.example.DataDisplay.service;

import com.example.DataDisplay.model.CompanyInData;
import com.example.DataDisplay.model.MeetingNote;
import com.example.DataDisplay.repository.CompanyRepository;
import com.example.DataDisplay.repository.MeetingNoteRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

@Service
public class ExportService {

    @Autowired
    private MeetingNoteRepository meetingNoteRepository;
    @Autowired
    private CompanyRepository companyRepository;
    public String getErpToolForCompany(Long companyId) {
        // Fetch the company by its ID

        Optional<CompanyInData> companyOptional = companyRepository.findById(companyId);

        // Check if the company exists
        if (companyOptional.isPresent()) {
            CompanyInData company = companyOptional.get();

            // Assuming the Company entity has a field for the ERP tool, return it
            String erpTool = company.getErpTool();  // This is where the ERP tool info is retrieved

            // If the ERP tool is null or empty, return a default value
            return erpTool != null ? erpTool : "No ERP Tool available";
        } else {
            // If the company does not exist, return a default message
            return "Company not found";
        }
    }

    public void exportData(String format, List<List<String>> tableData, List<Long> reachedOutCompanyIds, HttpServletResponse response) throws IOException, DocumentException {
        if (format.equals("csv")) {
            exportAsCSV(tableData, reachedOutCompanyIds, response);
        } else if (format.equals("xlsx")) {
            exportAsExcel(tableData, reachedOutCompanyIds, response);
        } else if (format.equals("pdf")) {
            exportAsPDF(tableData, reachedOutCompanyIds, response);
        }
    }

    private void exportAsCSV(List<List<String>> tableData, List<Long> reachedOutCompanyIds, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=company_data.csv");

        StringBuilder csvData = new StringBuilder();

        // Add main table data
        for (List<String> row : tableData) {
            csvData.append(String.join(",", row)).append("\n");
        }

        // Fetch and append meeting details
        if (!reachedOutCompanyIds.isEmpty()) {
            csvData.append("\nMeeting Notes for Reached Out Companies:\n");
            csvData.append("ID,Company,Note,Timestamp\n");

            for (Long companyId : reachedOutCompanyIds) {
                List<MeetingNote> notes = meetingNoteRepository.findMeetingNotesWithCompanyName(companyId);
                for (MeetingNote note : notes) {
                    csvData.append(note.getId()).append(",")
                            .append(note.getCompany().getCompany()).append(",")
                            .append(note.getNote().replace(",", " ")).append(",")
                            .append(note.getTimestamp()).append("\n");
                }
            }
        }

        response.getWriter().write(csvData.toString());
    }

    private void exportAsExcel(List<List<String>> tableData, List<Long> reachedOutCompanyIds, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=company_data.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Company Data");

        // Write main table data
        int rowIndex = 0;
        for (List<String> rowData : tableData) {
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < rowData.size(); i++) {
                row.createCell(i).setCellValue(rowData.get(i));
            }
        }

        // Fetch and append meeting details
        if (!reachedOutCompanyIds.isEmpty()) {
            rowIndex++; // Leave one empty row
            Row headerRow = sheet.createRow(rowIndex++);
            String[] headers = {"ID", "Company", "Note", "Timestamp"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            for (Long companyId : reachedOutCompanyIds) {
                List<MeetingNote> notes = meetingNoteRepository.findMeetingNotesWithCompanyName(companyId);
                for (MeetingNote note : notes) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(note.getId());
                    row.createCell(1).setCellValue(note.getCompany().getCompany());
                    row.createCell(2).setCellValue(note.getNote());
                    row.createCell(3).setCellValue(note.getTimestamp().toString());
                }
            }
        }

        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
    }

    private void exportAsPDF(List<List<String>> tableData, List<Long> reachedOutCompanyIds, HttpServletResponse response) throws IOException, DocumentException {
        // Set response content type and attachment filename
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=company_data.pdf");

        // Create Document with landscape orientation
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Title Styling
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Company Data Report\n\n", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Main Table with Company Data
        PdfPTable table = new PdfPTable(tableData.getFirst().size());
        table.setWidthPercentage(100);  // Make table width 100% of page width
        table.setSpacingBefore(10f);  // Space before table

        // Add table headers with bold font
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        for (String header : tableData.get(0)) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(headerCell);
        }

        // Add table data with appropriate alignment and borders
        for (int i = 1; i < tableData.size(); i++) {  // Skip the header row
            List<String> row = tableData.get(i);
            for (int j = 0; j < row.size() - 1; j++) {  // Loop until the second last element
                String cell = row.get(j);
                table.addCell(new PdfPCell(new Phrase(cell)));
            }
        }

        document.add(table);

        // Meeting Notes Section for each company
        if (!reachedOutCompanyIds.isEmpty()) {
            for (Long companyId : reachedOutCompanyIds) {

                List<MeetingNote> notes = meetingNoteRepository.findMeetingNotesWithCompanyName(companyId);

                if (!notes.isEmpty()) {
                    String companyName = notes.getFirst().getCompany().getCompany();

                    String erpTool = getErpToolForCompany(companyId);

                    document.add(new Paragraph("\n" + companyName + " Report\n", titleFont));
                    document.add(new Paragraph("\nERP Tool: " + erpTool + "\n", titleFont));

                    PdfPTable notesTable = new PdfPTable(3);
                    notesTable.setWidthPercentage(100);
                    notesTable.setSpacingBefore(10f);

                    Font notesHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                    String[] notesHeaders = {"ID", "Note", "Timestamp"};
                    for (String header : notesHeaders) {
                        PdfPCell headerCell = new PdfPCell(new Phrase(header, notesHeaderFont));
                        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        notesTable.addCell(headerCell);
                    }

                    // Add meeting notes data for this company
                    for (MeetingNote note : notes) {
                        notesTable.addCell(String.valueOf(note.getId()));
                        notesTable.addCell(note.getNote());
                        notesTable.addCell(note.getTimestamp().toString());
                    }

                    document.add(notesTable);
                }
            }
        }

        document.close();
    }


}
