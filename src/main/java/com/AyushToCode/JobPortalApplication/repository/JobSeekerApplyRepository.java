package com.AyushToCode.JobPortalApplication.repository;
import com.AyushToCode.JobPortalApplication.entity.JobPostActivity;
import com.AyushToCode.JobPortalApplication.entity.JobSeekerApply;
import com.AyushToCode.JobPortalApplication.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply,Integer> {

    List<JobSeekerApply> findByUserId(JobSeekerProfile userId);

    List<JobSeekerApply> findByJob(JobPostActivity job);
}

