package com.example.DataDisplay.controller;

import com.example.DataDisplay.model.CompanyInData;
import com.example.DataDisplay.model.MeetingNote;
import com.example.DataDisplay.repository.CompanyRepository;
import com.example.DataDisplay.repository.MeetingNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MeetingNoteController {

    @Autowired
    private MeetingNoteRepository meetingNoteRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/reached-out-details/{companyId}")
    public String getCompanyDetails(@PathVariable("companyId") Long companyId, Model model) {
        CompanyInData company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        model.addAttribute("company", company);
        return "company-details";
    }

    @PostMapping("/reached-out-details/{companyId}/add-note")
    public String addMeetingNote(@PathVariable("companyId") Long companyId,
                                 @RequestParam("note") String noteContent) {
        CompanyInData company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        MeetingNote newNote = new MeetingNote(company, noteContent);
        meetingNoteRepository.save(newNote);

        company.addMeetingNote(newNote);
        companyRepository.save(company);

        return "redirect:/reached-out-details/" + companyId;
    }
}
