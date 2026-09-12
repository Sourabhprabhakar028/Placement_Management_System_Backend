package com.placement.placement.repository;

import com.placement.placement.entity.Placement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlacementRepository extends JpaRepository<Placement, Long> {

    List<Placement> findByStudentId(Long studentId);
    List<Placement> findByCompanyId(Long companyId);
    List<Placement> findByStatus(String status);
    boolean existsByStudentIdAndCompanyId(Long studentId, Long companyId);
    Page<Placement> findAll(Pageable pageable);

    // ✅ Used by ReportServiceImpl.getFullReport()
    @Query("SELECT AVG(p.ctc) FROM Placement p WHERE p.ctc IS NOT NULL")
    Double findAverageCtc();

    @Query("SELECT s.branch, COUNT(p) FROM Placement p JOIN p.student s GROUP BY s.branch ORDER BY s.branch")
    List<Object[]> countPlacementsByBranch();

    @Query("SELECT FUNCTION('DATE_FORMAT', p.placementDate, '%Y-%m'), COUNT(p) " +
            "FROM Placement p GROUP BY FUNCTION('DATE_FORMAT', p.placementDate, '%Y-%m') " +
            "ORDER BY FUNCTION('DATE_FORMAT', p.placementDate, '%Y-%m')")
    List<Object[]> countPlacementsByMonth();

    @Query("SELECT p.status, COUNT(p) FROM Placement p GROUP BY p.status ORDER BY p.status")
    List<Object[]> countPlacementsByStatus();
}