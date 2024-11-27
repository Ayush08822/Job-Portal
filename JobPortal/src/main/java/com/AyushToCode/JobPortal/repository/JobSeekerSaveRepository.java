package com.AyushToCode.JobPortal.repository;

import com.AyushToCode.JobPortal.entity.JobPostActivity;
import com.AyushToCode.JobPortal.entity.JobSeekerProfile;
import com.AyushToCode.JobPortal.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave , Integer> {
    public List<JobSeekerSave> findByUserId(JobSeekerProfile userAccountId);

    public List<JobSeekerSave> findByJob(JobPostActivity job);

}
