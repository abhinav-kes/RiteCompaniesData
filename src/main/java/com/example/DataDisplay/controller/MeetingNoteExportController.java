package com.example.DataDisplay.controller;

import com.example.DataDisplay.service.MeetingNoteExportService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/export-meeting-data")
public class MeetingNoteExportController {

    private final MeetingNoteExportService exportService;

    @Autowired
    public MeetingNoteExportController(MeetingNoteExportService exportService) {
        this.exportService = exportService;
    }

    // Handle POST request for CSV, Excel, PDF, etc.
    @PostMapping
    public void exportMeetingNotes(@RequestParam("format") String format,
                                   @RequestParam("companyId") Long companyId,
                                   @RequestBody String data,
                                   HttpServletResponse response) throws IOException, DocumentException {
        switch (format.toLowerCase()) {
            case "csv":
                exportService.exportAsCSV(companyId, response);
                break;
            case "xlsx":
                exportService.exportAsExcel(companyId, response);
                break;
            case "pdf":
                exportService.exportAsPDF(companyId, response);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid format");
        }
    }
}

