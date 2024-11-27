package com.AyushToCode.JobPortal.services;

import com.AyushToCode.JobPortal.entity.JobPostActivity;
import com.AyushToCode.JobPortal.entity.JobSeekerApply;
import com.AyushToCode.JobPortal.entity.JobSeekerProfile;
import com.AyushToCode.JobPortal.repository.JobSeekerApplyRepository;
import com.AyushToCode.JobPortal.repository.JobSeekerProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerApplyService {
    private final JobSeekerApplyRepository jobSeekerApplyRepository;

    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }


    public List<JobSeekerApply> getCandidateJobs(JobSeekerProfile userAccountId){
        //finding the details of the jobs that have been applied by a candidate on the basis of the user id(foreign key).
        return jobSeekerApplyRepository.findByUserId(userAccountId);
    }

    public List<JobSeekerApply> getJobs(JobPostActivity job){
        return jobSeekerApplyRepository.findByJob(job);
    }

    public void addNew(JobSeekerApply jobSeekerApply) {
        jobSeekerApplyRepository.save(jobSeekerApply);
    }
}
