package com.placement.placement.controller;

import com.placement.placement.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/export")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    // ✅ Export Students Excel
    @GetMapping("/students/excel")
    public void exportStudentsExcel(
            HttpServletResponse response) throws IOException {
        exportService.exportStudentsToExcel(response);
    }

    // ✅ Export Companies Excel
    @GetMapping("/companies/excel")
    public void exportCompaniesExcel(
            HttpServletResponse response) throws IOException {
        exportService.exportCompaniesToExcel(response);
    }

    // ✅ Export Placements Excel
    @GetMapping("/placements/excel")
    public void exportPlacementsExcel(
            HttpServletResponse response) throws IOException {
        exportService.exportPlacementsToExcel(response);
    }

    // ✅ Export Report PDF
    @GetMapping("/report/pdf")
    public void exportReportPdf(
            HttpServletResponse response) throws IOException {
        exportService.exportReportToPdf(response);
    }
}