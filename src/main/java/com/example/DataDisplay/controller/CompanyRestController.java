package com.example.DataDisplay.controller;

import com.example.DataDisplay.model.CompanyInData;
import com.example.DataDisplay.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class CompanyRestController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/company-data")
    public List<Map<String, String>> getCompanyData() {
        List<CompanyInData> companies = companyService.getAllCompanies();

        // Extract only 'company' and 'erpTool' fields
        return companies.stream()
                .map(company -> Map.of(
                        "company", company.getCompany(),
                        "erpTool", company.getErpTool()))
                .collect(Collectors.toList());
    }
}

