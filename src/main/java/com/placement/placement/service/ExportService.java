package com.placement.placement.service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExportService {
    void exportStudentsToExcel(HttpServletResponse response)
            throws IOException;
    void exportCompaniesToExcel(HttpServletResponse response)
            throws IOException;
    void exportPlacementsToExcel(HttpServletResponse response)
            throws IOException;
    void exportReportToPdf(HttpServletResponse response)
            throws IOException;
}