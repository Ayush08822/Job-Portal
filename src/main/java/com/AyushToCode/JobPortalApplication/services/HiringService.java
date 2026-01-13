package com.AyushToCode.JobPortalApplication.services;

import com.AyushToCode.JobPortalApplication.entity.JobPostActivity;
import com.AyushToCode.JobPortalApplication.entity.JobSeekerApply;
import com.AyushToCode.JobPortalApplication.repository.JobPostActivityRepository;
import com.AyushToCode.JobPortalApplication.repository.JobSeekerApplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class HiringService {

    private final JobPostActivityRepository jobRepository;
    private final JobSeekerApplyRepository applyRepository;

    public HiringService(JobPostActivityRepository jobRepository, JobSeekerApplyRepository applyRepository) {
        this.jobRepository = jobRepository;
        this.applyRepository = applyRepository;
    }

    @Transactional
    public void hireCandidate(int bidId) {
        // 1. Find the specific bid (application)
        JobSeekerApply selectedBid = applyRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        JobPostActivity job = selectedBid.getJob();

        // 2. Check for race conditions (Bonus 1): Ensure job is still open [cite: 37, 38]
        if ("assigned".equalsIgnoreCase(job.getStatus())) {
            throw new IllegalStateException("This gig has already been assigned to another freelancer.");
        }

        // 3. Logic: The Gig status must change from open to assigned [cite: 25]
        job.setStatus("assigned");
        jobRepository.save(job);

        // 4. Logic: The chosen Bid status becomes hired [cite: 27]
        selectedBid.setStatus("hired");
        applyRepository.save(selectedBid);

        // 5. Logic: All other Bids for that same Gig automatically marked as rejected [cite: 28]
        List<JobSeekerApply> otherBids = applyRepository.findByJob(job);
        for (JobSeekerApply bid : otherBids) {
            if (!bid.getId().equals(bidId)) {
                bid.setStatus("rejected");
            }
        }
        applyRepository.saveAll(otherBids);
    }
}