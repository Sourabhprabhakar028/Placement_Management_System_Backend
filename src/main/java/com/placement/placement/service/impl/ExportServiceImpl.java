package com.placement.placement.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.placement.placement.entity.Company;
import com.placement.placement.entity.Placement;
import com.placement.placement.entity.Student;
import com.placement.placement.repository.CompanyRepository;
import com.placement.placement.repository.PlacementRepository;
import com.placement.placement.repository.StudentRepository;
import com.placement.placement.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExportServiceImpl implements ExportService {

    private static final Logger log =
            LoggerFactory.getLogger(ExportServiceImpl.class);

    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final PlacementRepository placementRepository;

    public ExportServiceImpl(StudentRepository studentRepository,
                             CompanyRepository companyRepository,
                             PlacementRepository placementRepository) {
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.placementRepository = placementRepository;
    }

    // ✅ Export Students to Excel
    @Override
    public void exportStudentsToExcel(HttpServletResponse response)
            throws IOException {
        log.info("Exporting students to Excel");

        List<Student> students = studentRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        // ✅ Header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(
                IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // ✅ Header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Name", "Email",
                "Branch", "Percentage"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // ✅ Data rows
        int rowNum = 1;
        for (Student student : students) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getId());
            row.createCell(1).setCellValue(student.getName());
            row.createCell(2).setCellValue(student.getEmail());
            row.createCell(3).setCellValue(student.getBranch());
            row.createCell(4).setCellValue(student.getPercentage());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType(
                "application/vnd.openxmlformats-officedocument" +
                        ".spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
                "attachment; filename=students.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
        log.info("Students Excel exported successfully!");
    }

    // ✅ Export Companies to Excel
    @Override
    public void exportCompaniesToExcel(HttpServletResponse response)
            throws IOException {
        log.info("Exporting companies to Excel");

        List<Company> companies = companyRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Companies");

        // ✅ Header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(
                IndexedColors.LIGHT_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // ✅ Header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Name", "Location",
                "Package (LPA)", "Min Percentage", "Eligible Branches"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // ✅ Data rows
        int rowNum = 1;
        for (Company company : companies) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(company.getId());
            row.createCell(1).setCellValue(company.getName());
            row.createCell(2).setCellValue(company.getLocation());
            row.createCell(3).setCellValue(company.getPackageOffered());
            row.createCell(4).setCellValue(
                    company.getMinPercentage() != null
                            ? company.getMinPercentage() : 0.0);
            row.createCell(5).setCellValue(
                    company.getEligibleBranches() != null
                            ? company.getEligibleBranches() : "ALL");
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType(
                "application/vnd.openxmlformats-officedocument" +
                        ".spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
                "attachment; filename=companies.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
        log.info("Companies Excel exported successfully!");
    }

    // ✅ Export Placements to Excel
    @Override
    public void exportPlacementsToExcel(HttpServletResponse response)
            throws IOException {
        log.info("Exporting placements to Excel");

        List<Placement> placements = placementRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Placements");

        // ✅ Header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(
                IndexedColors.LIGHT_YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // ✅ Header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Student Name", "Company Name",
                "Status", "Placement Date", "CTC (LPA)"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // ✅ Data rows
        int rowNum = 1;
        for (Placement placement : placements) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(placement.getId());
            row.createCell(1).setCellValue(
                    placement.getStudent().getName());
            row.createCell(2).setCellValue(
                    placement.getCompany().getName());
            row.createCell(3).setCellValue(placement.getStatus());
            row.createCell(4).setCellValue(
                    placement.getPlacementDate().toString());
            row.createCell(5).setCellValue(placement.getCtc());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType(
                "application/vnd.openxmlformats-officedocument" +
                        ".spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
                "attachment; filename=placements.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
        log.info("Placements Excel exported successfully!");
    }

    // ✅ Export Report to PDF
    @Override
    public void exportReportToPdf(HttpServletResponse response)
            throws IOException {
        log.info("Exporting report to PDF");

        long totalStudents = studentRepository.count();
        long totalCompanies = companyRepository.count();
        long totalPlacements = placementRepository.count();
        double placementPercentage = totalStudents > 0
                ? (double) totalPlacements / totalStudents * 100 : 0;

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=placement_report.pdf");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // ✅ Title
            com.itextpdf.text.Font titleFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD, 20,
                    BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph(
                    "Smart Placement Management System\n", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            com.itextpdf.text.Font subFont = FontFactory.getFont(
                    FontFactory.HELVETICA, 12, BaseColor.GRAY);
            Paragraph subtitle = new Paragraph(
                    "Placement Report\n\n", subFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);

            // ✅ Summary Table
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(60);
            summaryTable.setSpacingBefore(10f);

            addPdfTableHeader(summaryTable, "Metric");
            addPdfTableHeader(summaryTable, "Value");

            // ✅ FIXED - Each cell separately
            addPdfTableRow(summaryTable, "Total Students");
            addPdfTableRow(summaryTable, String.valueOf(totalStudents));
            addPdfTableRow(summaryTable, "Total Companies");
            addPdfTableRow(summaryTable, String.valueOf(totalCompanies));
            addPdfTableRow(summaryTable, "Total Placements");
            addPdfTableRow(summaryTable, String.valueOf(totalPlacements));
            addPdfTableRow(summaryTable, "Placement Percentage");
            addPdfTableRow(summaryTable,
                    String.format("%.1f%%", placementPercentage));

            document.add(summaryTable);

            // ✅ Students Table
            document.add(new Paragraph("\n"));
            com.itextpdf.text.Font sectionFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
            document.add(new Paragraph(
                    "Student List\n\n", sectionFont));

            List<Student> students = studentRepository.findAll();
            PdfPTable studentTable = new PdfPTable(5);
            studentTable.setWidthPercentage(100);
            studentTable.setSpacingBefore(5f);

            String[] studentHeaders = {"ID", "Name", "Email",
                    "Branch", "Percentage"};
            for (String h : studentHeaders) {
                addPdfTableHeader(studentTable, h);
            }

            for (Student s : students) {
                addPdfTableRow(studentTable,
                        String.valueOf(s.getId()));
                addPdfTableRow(studentTable, s.getName());
                addPdfTableRow(studentTable, s.getEmail());
                addPdfTableRow(studentTable, s.getBranch());
                addPdfTableRow(studentTable,
                        s.getPercentage() + "%");
            }
            document.add(studentTable);

            document.close();
            log.info("PDF report exported successfully!");

        } catch (DocumentException e) {
            log.error("PDF generation error: {}", e.getMessage());
            throw new IOException("Failed to generate PDF!");
        }
    }

    private void addPdfTableHeader(PdfPTable table, String text) {
        com.itextpdf.text.Font font = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new BaseColor(70, 130, 180));
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addPdfTableRow(PdfPTable table, String text) {
        com.itextpdf.text.Font font = FontFactory.getFont(
                FontFactory.HELVETICA, 9, BaseColor.BLACK);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(4);
        table.addCell(cell);
    }
}