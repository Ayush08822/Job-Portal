package com.AyushToCode.JobPortal.services;

import com.AyushToCode.JobPortal.entity.JobPostActivity;
import com.AyushToCode.JobPortal.entity.JobSeekerProfile;
import com.AyushToCode.JobPortal.entity.JobSeekerSave;
import com.AyushToCode.JobPortal.repository.JobSeekerSaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerSaveService {
    private final JobSeekerSaveRepository jobSeekerSaveRepository;

    public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
        this.jobSeekerSaveRepository = jobSeekerSaveRepository;
    }

    public List<JobSeekerSave> getCandidateJob(JobSeekerProfile userAccountId){
        return jobSeekerSaveRepository.findByUserId(userAccountId);
    }
    public List<JobSeekerSave> getJobs(JobPostActivity job){
        return jobSeekerSaveRepository.findByJob(job);
    }

    public void addSave(JobSeekerSave jobSeekerSave) {
        jobSeekerSaveRepository.save(jobSeekerSave);
    }
}
