package com.example.DataDisplay.controller;

import com.example.DataDisplay.model.CompanyInData;
import com.example.DataDisplay.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/companies")
    public String showCompanies(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "companies";
    }


    @GetMapping("/chart")
    public String showChart() {
        return "company-chart"; // This refers to company-chart.html
    }

    @GetMapping("/companies/filter")
    public String getCompaniesByFilters(Model model,
                                        @RequestParam(value="location") String location,
                                        @RequestParam(value="erp") String erpTool,
                                        @RequestParam(value="industry") String industry) {

        model.addAttribute("companiesFilter", companyService.getCompaniesByFilters(location, erpTool, industry));
        return "companies";
    }

    @PutMapping("/companies/update")
    public ResponseEntity<?> updateCompany(@RequestBody CompanyInData companyData) {
        try {
            // Assuming the updateCompany method takes a CompanyInData object
            CompanyInData updatedCompany = companyService.updateCompany(companyData);
            return ResponseEntity.ok("Updated Successfully"); // Return 200 OK with the updated company data
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update company: " + e.getMessage());
        }
    }


    @DeleteMapping("/companies/delete/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            boolean isDeleted = companyService.deleteCompany(id);
            if (isDeleted) {
                return ResponseEntity.ok("Company deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Company not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete company: " + e.getMessage());
        }
    }


    @PostMapping("/companies/create")
    public ResponseEntity<?> createCompany(@RequestBody CompanyInData companyData) {
        try {
            // Assuming createCompany method adds a new company record
            CompanyInData newCompany = companyService.createCompany(companyData);
            return ResponseEntity.status(HttpStatus.CREATED).body("Company created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create company: " + e.getMessage());
        }
    }

    @GetMapping("/industries")
    @ResponseBody
    public ResponseEntity<List<String>> getIndustries() {
        List<String> industries = companyService.getAllIndustries();
        if (industries == null || industries.isEmpty()) {
            // If no industries found, return an empty list or an error message.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }
        return ResponseEntity.ok(industries);  // Return the list as JSON
    }

    @GetMapping("/locations")
    @ResponseBody
    public ResponseEntity<List<String>> getLocations() {
        List<String> locations = companyService.getAllLocations();
        if (locations == null || locations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }
        return ResponseEntity.ok(locations);  // Return the list as JSON
    }



}