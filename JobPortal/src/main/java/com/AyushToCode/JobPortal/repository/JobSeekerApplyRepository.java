package com.AyushToCode.JobPortal.repository;

import com.AyushToCode.JobPortal.entity.JobPostActivity;
import com.AyushToCode.JobPortal.entity.JobSeekerApply;
import com.AyushToCode.JobPortal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply,Integer> {

    List<JobSeekerApply> findByUserId(JobSeekerProfile userId);

    List<JobSeekerApply> findByJob(JobPostActivity job);
}
