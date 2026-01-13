package com.AyushToCode.JobPortalApplication.Controller;
import com.AyushToCode.JobPortalApplication.entity.JobPostActivity;
import com.AyushToCode.JobPortalApplication.entity.JobSeekerProfile;
import com.AyushToCode.JobPortalApplication.entity.JobSeekerSave;
import com.AyushToCode.JobPortalApplication.entity.Users;
import com.AyushToCode.JobPortalApplication.services.JobPostActivityService;
import com.AyushToCode.JobPortalApplication.services.JobSeekerProfileService;
import com.AyushToCode.JobPortalApplication.services.JobSeekerSaveService;
import com.AyushToCode.JobPortalApplication.services.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekerSaveController {
    private  final UsersService usersService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerSaveService jobSeekerSaveService;

    public JobSeekerSaveController(UsersService usersService, JobSeekerProfileService jobSeekerProfileService, JobPostActivityService jobPostActivityService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id") int id, JobSeekerSave jobSeekerSave){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername=authentication.getName();
            Optional<Users> users=usersService.findByEmail(currentUsername);
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(users.get().getUserId());
            JobPostActivity jobPostActivity=jobPostActivityService.getOne(id);
            if(seekerProfile.isPresent() && jobPostActivity!=null){
                jobSeekerSave = new JobSeekerSave();
                jobSeekerSave.setJob(jobPostActivity);
                jobSeekerSave.setUserId(seekerProfile.get());
            }else{
                throw new RuntimeException("User not found");
            }
        }
        jobSeekerSaveService.addSave(jobSeekerSave);
        return "redirect:/Dashboard/";
    }

    @GetMapping("saved-jobs/")
    public String savedJobs(Model model){
        List<JobPostActivity> jobPost=new ArrayList<>();
        Object currentuserProfile =  usersService.getCurrentUserprofile();

        List<JobSeekerSave> jobSeekerSaveList= jobSeekerSaveService.getCandidateJob((JobSeekerProfile) currentuserProfile);
        for(JobSeekerSave jobSeekerSave: jobSeekerSaveList){
            jobPost.add(jobSeekerSave.getJob());
        }

        model.addAttribute("jobPost",jobPost);
        model.addAttribute("user",currentuserProfile);

        return "saved-jobs";
    }
}
