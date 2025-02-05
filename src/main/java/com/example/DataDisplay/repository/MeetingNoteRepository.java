package com.example.DataDisplay.repository;

import com.example.DataDisplay.model.MeetingNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetingNoteRepository extends JpaRepository<MeetingNote, Long> {

    @Query("SELECT mn FROM MeetingNote mn WHERE mn.company.id = :companyId")
    List<MeetingNote> findMeetingNotesWithCompanyName(@Param("companyId") Long companyId);


}
