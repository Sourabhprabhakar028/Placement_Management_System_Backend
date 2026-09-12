package com.placement.placement.controller;

import com.placement.placement.dto.request.StudentRequest;
import com.placement.placement.dto.response.StudentResponse;
import com.placement.placement.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(
            @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.saveStudent(request));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<StudentResponse>> getAllStudentsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(
                studentService.getAllStudentsPaginated(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully");
    }

    // ✅ Eligibility Filtering
    @GetMapping("/eligible/{companyId}")
    public ResponseEntity<List<StudentResponse>> getEligibleStudents(
            @PathVariable Long companyId) {
        return ResponseEntity.ok(
                studentService.getEligibleStudents(companyId));
    }

    // ✅ NEW - Search by name
    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> searchStudents(
            @RequestParam String name) {
        return ResponseEntity.ok(studentService.searchByName(name));
    }

    // ✅ NEW - Filter students
    @GetMapping("/filter")
    public ResponseEntity<List<StudentResponse>> filterStudents(
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) Double minPercentage,
            @RequestParam(required = false) Double maxPercentage) {
        return ResponseEntity.ok(
                studentService.filterStudents(
                        branch, minPercentage, maxPercentage));
    }
}