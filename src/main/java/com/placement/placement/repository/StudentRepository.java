package com.placement.placement.repository;

import com.placement.placement.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<Student> findAll(Pageable pageable);

    // ✅ Used by StudentServiceImpl.searchByName()
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> searchByName(@Param("name") String name);

    // ✅ Used by StudentServiceImpl.filterStudents()
    @Query("SELECT s FROM Student s WHERE " +
            "(:branch IS NULL OR LOWER(s.branch) = LOWER(:branch)) AND " +
            "(:minPercentage IS NULL OR s.percentage >= :minPercentage) AND " +
            "(:maxPercentage IS NULL OR s.percentage <= :maxPercentage)")
    List<Student> filterStudents(@Param("branch") String branch,
                                 @Param("minPercentage") Double minPercentage,
                                 @Param("maxPercentage") Double maxPercentage);

    // ✅ Used by StudentServiceImpl.getEligibleStudents() — ALL branches
    @Query("SELECT s FROM Student s WHERE s.percentage >= :minPercentage")
    List<Student> findEligibleByPercentage(@Param("minPercentage") Double minPercentage);

    // ✅ Used by StudentServiceImpl.getEligibleStudents() — specific branches
    @Query("SELECT s FROM Student s WHERE s.percentage >= :minPercentage AND s.branch IN :branches")
    List<Student> findEligibleByPercentageAndBranch(@Param("minPercentage") Double minPercentage,
                                                    @Param("branches") List<String> branches);

    // ✅ Used by ReportServiceImpl.countStudentsByBranch()
    @Query("SELECT s.branch, COUNT(s) FROM Student s GROUP BY s.branch ORDER BY s.branch")
    List<Object[]> countStudentsByBranch();
}