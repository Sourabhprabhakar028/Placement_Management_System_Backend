package com.placement.placement.repository;

import com.placement.placement.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByName(String name);
    Page<Company> findAll(Pageable pageable);

    // ✅ Used by CompanyServiceImpl.searchByName()
    @Query("SELECT c FROM Company c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Company> searchByName(@Param("name") String name);

    // ✅ Used by CompanyServiceImpl.filterCompanies()
    @Query("SELECT c FROM Company c WHERE " +
            "(:location IS NULL OR LOWER(c.location) = LOWER(:location)) AND " +
            "(:minPackage IS NULL OR c.packageOffered >= :minPackage) AND " +
            "(:maxPackage IS NULL OR c.packageOffered <= :maxPackage)")
    List<Company> filterCompanies(@Param("location") String location,
                                  @Param("minPackage") Double minPackage,
                                  @Param("maxPackage") Double maxPackage);

    // ✅ Used by ReportServiceImpl
    List<Company> findTop5ByOrderByPackageOfferedDesc();
}