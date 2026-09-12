package com.placement.placement.service;

import com.placement.placement.dto.request.StudentRequest;
import com.placement.placement.dto.response.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {
    StudentResponse saveStudent(StudentRequest request);
    List<StudentResponse> getAllStudents();
    Page<StudentResponse> getAllStudentsPaginated(Pageable pageable);
    StudentResponse getStudentById(Long id);
    StudentResponse updateStudent(Long id, StudentRequest request);
    void deleteStudent(Long id);

    // ✅ Eligibility
    List<StudentResponse> getEligibleStudents(Long companyId);

    // ✅ NEW - Search + Filter
    List<StudentResponse> searchByName(String name);
    List<StudentResponse> filterStudents(String branch,
                                         Double minPercentage,
                                         Double maxPercentage);
}