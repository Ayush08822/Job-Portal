package com.AyushToCode.JobPortalApplication.services;
import com.AyushToCode.JobPortalApplication.entity.JobPostActivity;
import com.AyushToCode.JobPortalApplication.entity.JobSeekerProfile;
import com.AyushToCode.JobPortalApplication.entity.JobSeekerSave;
import com.AyushToCode.JobPortalApplication.repository.JobSeekerSaveRepository;
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
