package com.AyushToCode.JobPortalApplication.repository;
import com.AyushToCode.JobPortalApplication.entity.JobPostActivity;
import com.AyushToCode.JobPortalApplication.entity.JobSeekerProfile;
import com.AyushToCode.JobPortalApplication.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave , Integer> {
    public List<JobSeekerSave> findByUserId(JobSeekerProfile userAccountId);

    public List<JobSeekerSave> findByJob(JobPostActivity job);

}
