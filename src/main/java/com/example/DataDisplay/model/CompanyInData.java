package com.example.DataDisplay.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "gcc_companies_list")
public class CompanyInData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    @Column(name="company_name")
    private String Company;

    @Column(name="Industry")
    private String industry;
    @Column(name="Locations")
    private String location;

    @Column(name="current_erp_stack_zoominfo")
    private String erpTool;

    @Column(name="Internal Referral")
    private String int_ref;

    @Column(name="MailID")
    private String int_ref_email;

    @Column(name="Phone Number")
    private String int_ref_number;

    @Column(name="Reached Out?")
    private String reachedOut;

    @Version
    private int version;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingNote> meetingNotes = new ArrayList<>();

    public List<MeetingNote> getMeetingNotes() {
        return meetingNotes;
    }

    public void addMeetingNote(MeetingNote note) {
        this.meetingNotes.add(note);
        note.setCompany(this);
    }

    public void removeMeetingNote(MeetingNote note) {
        this.meetingNotes.remove(note);
        note.setCompany(null);
    }


    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getErpTool() {
        return erpTool;
    }

    public void setErpTool(String erpTool) {
        this.erpTool = erpTool;
    }

    public String getInt_ref() {
        return int_ref;
    }

    public void setInt_ref(String int_ref) {
        this.int_ref = int_ref;
    }

    public String getInt_ref_email() {
        return int_ref_email;
    }

    public void setInt_ref_email(String int_ref_email) {
        this.int_ref_email = int_ref_email;
    }

    public String getInt_ref_number() {
        return int_ref_number;
    }

    public void setInt_ref_number(String int_ref_number) {
        this.int_ref_number = int_ref_number;
    }

    public String getReachedOut() {
        return reachedOut;
    }

    public void setReachedOut(String reachedOut) {
        this.reachedOut = reachedOut;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
