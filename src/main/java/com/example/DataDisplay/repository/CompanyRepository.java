package com.example.DataDisplay.repository;

import com.example.DataDisplay.model.CompanyInData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyInData, Long> {
    // Derived Query Method
    List<CompanyInData> findByLocationAndErpToolAndIndustry(String location, String erpTool, String industry);
    List<CompanyInData> findByLocationAndErpTool(String location,String erpTool);
    List<CompanyInData> findByErpToolAndIndustry(String erpTool,String industry);
    List<CompanyInData> findByLocationAndIndustry(String location,String industry);
    List<CompanyInData> findByLocation(String location);
    List<CompanyInData> findByErpTool(String erpTool);
    List<CompanyInData> findByIndustry(String industry);
    Optional<CompanyInData> findById(Long id);

    @Query(value = "SELECT DISTINCT industry FROM gcc_companies_list", nativeQuery = true)
    List<String> findDistinctIndustries();

    @Query(value = "SELECT DISTINCT locations FROM gcc_companies_list", nativeQuery = true)
    List<String> findDistinctLocations();
    List<CompanyInData> findByErpToolLike(String erpTool);
    List<CompanyInData> findByErpToolLikeAndIndustry(String s, String industry);
    List<CompanyInData> findByErpToolLikeAndLocation(String s, String location);
    List<CompanyInData> findByErpToolLikeAndLocationAndIndustry(String s, String location, String industry);
    List<CompanyInData> findByIndustryLike(String industry);
    List<CompanyInData> findByIndustryLikeAndLocation(String industry, String location);
    List<CompanyInData> findByErpToolAndIndustryLike(String erpTool, String industry);
    List<CompanyInData> findByErpToolLikeAndIndustryLike(String erpTool,String industry);
    List<CompanyInData> findByErpToolLikeAndLocationAndIndustryLike(String erpTool, String location, String industry);
    List<CompanyInData> findByErpToolAndLocationAndIndustryLike(String erpTool, String location, String industry);
}
