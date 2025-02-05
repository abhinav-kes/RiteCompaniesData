package com.example.DataDisplay.service;


import com.example.DataDisplay.model.CompanyInData;
import com.example.DataDisplay.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public List<CompanyInData> getAllCompanies() {
        return companyRepository.findAll();
    }

    public List<CompanyInData> getCompaniesByFilters(String location, String erpTool, String industry) {
        boolean locationAll = Objects.equals(location, "All India");
        boolean erpToolAll = Objects.equals(erpTool, "All");
        boolean industryAll = Objects.equals(industry, "All");

        boolean b = industry.equalsIgnoreCase("Manufacturing") || industry.equalsIgnoreCase("Finance") ||
                industry.equalsIgnoreCase("Business Services") || industry.equalsIgnoreCase("Transportation");

        boolean b1=erpTool.equalsIgnoreCase("Oracle")||erpTool.equalsIgnoreCase("SAP");

        if (locationAll && erpToolAll && industryAll) {
            return companyRepository.findAll();
        }

        if (erpToolAll) {
            if (locationAll) {
                if (b){
                    String x="%"+industry+"%";
                    return companyRepository.findByIndustryLike(x);
                }
                return companyRepository.findByIndustry(industry);
            }
            if (industryAll) {
                return companyRepository.findByLocation(location);
            }

            if (b){
                String x="%"+industry+"%";
                return companyRepository.findByIndustryLikeAndLocation(x,location);
            }
            return companyRepository.findByLocationAndIndustry(location, industry);
        }

        if (b1) {
            String y="%"+erpTool+"%";
            if (locationAll && industryAll) {
                return companyRepository.findByErpToolLike(y);
            }
            if (locationAll) {
                if (b){
                    String x="%"+industry+"%";
                    return companyRepository.findByErpToolLikeAndIndustryLike(y,x);
                }
                return companyRepository.findByErpToolLikeAndIndustry(y, industry);
            }
            if (industryAll) {
                return companyRepository.findByErpToolLikeAndLocation(y, location);
            }

            if(b){
                String x="%"+industry+"%";
                return companyRepository.findByErpToolLikeAndLocationAndIndustryLike(y,location,x);
            }
            return companyRepository.findByErpToolLikeAndLocationAndIndustry(y, location, industry);

        }

        if (locationAll && industryAll) {
            return companyRepository.findByErpTool(erpTool);
        }
        if (industryAll) {
            return companyRepository.findByLocationAndErpTool(location, erpTool);
        }
        if (locationAll) {
            if (b){
                String x="%"+industry+"%";
                return companyRepository.findByErpToolAndIndustryLike(erpTool,x);
            }
            return companyRepository.findByErpToolAndIndustry(erpTool, industry);
        }

        if (b){
            String x="%"+industry+"%";
            return companyRepository.findByErpToolAndLocationAndIndustryLike(erpTool,location,x);
        }
        return companyRepository.findByLocationAndErpToolAndIndustry(location, erpTool, industry);
    }

    public CompanyInData updateCompany(CompanyInData companyData) throws Exception {

        CompanyInData existingCompany = companyRepository.findById(companyData.getID())
                .orElseThrow(() -> new Exception("Company not found with id: " + companyData.getID()));

        existingCompany.setCompany(companyData.getCompany());
        existingCompany.setIndustry(companyData.getIndustry());
        existingCompany.setLocation(companyData.getLocation());
        existingCompany.setErpTool(companyData.getErpTool());
        existingCompany.setInt_ref(companyData.getInt_ref());
        existingCompany.setInt_ref_email(companyData.getInt_ref_email());
        existingCompany.setInt_ref_number(companyData.getInt_ref_number());
        existingCompany.setReachedOut(companyData.getReachedOut());

        return companyRepository.save(existingCompany);
    }

    public boolean deleteCompany(Long id) {
        Optional<CompanyInData> company = companyRepository.findById(id);
        if (company.isPresent()) {
            companyRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public CompanyInData createCompany(CompanyInData companyData) throws Exception {
        try {
            // Validation logic (if needed)
            CompanyInData newCompany = new CompanyInData();
            newCompany.setCompany(companyData.getCompany());
            newCompany.setIndustry(companyData.getIndustry());
            newCompany.setLocation(companyData.getLocation());
            newCompany.setErpTool(companyData.getErpTool());
            newCompany.setInt_ref(companyData.getInt_ref());
            newCompany.setInt_ref_email(companyData.getInt_ref_email());
            newCompany.setInt_ref_number(companyData.getInt_ref_number());
            newCompany.setReachedOut(companyData.getReachedOut());

            return companyRepository.saveAndFlush(newCompany);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating company", e);
        }
    }

    public List<String> getAllIndustries() {
        try {
            return companyRepository.findDistinctIndustries();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching industries", e);  // Rethrow or handle as needed
        }
    }

    public List<String> getAllLocations() {
        try {
            List<String> locations = companyRepository.findDistinctLocations();

            return locations.stream()
                    .map(String::trim)  // Trim leading and trailing spaces
                    .distinct()         // Remove duplicates
                    .collect(Collectors.toList()); // Collect back to a list
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching locations", e);  // Rethrow or handle as needed
        }
    }
}