package com.example.DataDisplay.controller;

import com.example.DataDisplay.service.ExportService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @PostMapping("/data")
    public void exportData(
            @RequestParam String format,
            @RequestBody ExportRequest request,
            HttpServletResponse response) throws IOException, DocumentException {
        exportService.exportData(format, request.getTableData(), request.getReachedOutCompanyIds(), response);
    }
}

class ExportRequest {
    private List<List<String>> tableData;
    private List<Long> reachedOutCompanyIds;

    public List<List<String>> getTableData() {
        return tableData;
    }

    public void setTableData(List<List<String>> tableData) {
        this.tableData = tableData;
    }

    public List<Long> getReachedOutCompanyIds() {
        return reachedOutCompanyIds;
    }

    public void setReachedOutCompanyIds(List<Long> reachedOutCompanyIds) {
        this.reachedOutCompanyIds = reachedOutCompanyIds;
    }
}
