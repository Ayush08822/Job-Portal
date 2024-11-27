package com.AyushToCode.JobPortal.Controller;

import com.AyushToCode.JobPortal.entity.*;
import com.AyushToCode.JobPortal.services.JobPostActivityService;
import com.AyushToCode.JobPortal.services.JobSeekerApplyService;
import com.AyushToCode.JobPortal.services.JobSeekerSaveService;
import com.AyushToCode.JobPortal.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class JobPostActivityController {
    private final UsersService usersService;
    private  final JobPostActivityService jobPostActivityService;

    private final JobSeekerApplyService jobSeekerApplyService;

    private final JobSeekerSaveService jobSeekerSaveService;

    @Autowired
    public JobPostActivityController(UsersService usersService, JobPostActivityService jobPostActivityService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }
    @GetMapping("/Dashboard/")
    public String searchjob(Model model, @RequestParam(value = "job", required = false) String job,
                            @RequestParam(value = "location", required = false) String location,
                            @RequestParam(value = "partTime", required = false) String partTime,
                            @RequestParam(value = "fullTime", required = false) String fullTime,
                            @RequestParam(value = "freelance", required = false) String freelance,
                            @RequestParam(value = "remoteOnly", required = false) String remoteOnly,
                            @RequestParam(value = "officeOnly", required = false) String officeOnly,
                            @RequestParam(value = "partialRemote", required = false) String partialRemote,
                            @RequestParam(value = "today", required = false) boolean today,
                            @RequestParam(value = "days7", required = false) boolean days7,
                            @RequestParam(value = "days30", required = false) boolean days30
    ) {
        model.addAttribute("partTime" , Objects.equals(partTime,"Part-Time"));
        model.addAttribute("fullTime" , Objects.equals(fullTime,"Full-Time"));
        model.addAttribute("freelance" , Objects.equals(partTime,"Freelance"));
        model.addAttribute("remoteOnly" , Objects.equals(remoteOnly,"Remote-Only"));
        model.addAttribute("officeOnly" , Objects.equals(officeOnly,"Office-Only"));
        model.addAttribute("partialRemote" , Objects.equals(partialRemote,"Partial-Remote"));
        model.addAttribute("today");
        model.addAttribute("days7");
        model.addAttribute("days30" );
        model.addAttribute("job",job);
        model.addAttribute("location",location);
        LocalDate searchDate=null;
        List<JobPostActivity> jobPost=null;
        boolean dateSearchFlag=true;
        boolean remote=true;
        boolean type=true;
        if(days30){
           searchDate=LocalDate.now().minusDays(30);
        } else if(days7){
            searchDate=LocalDate.now().minusDays(7);
        }else if(today){
            searchDate=LocalDate.now();
        }else{
            dateSearchFlag=false;
        }

        if(partTime==null && fullTime==null && freelance == null ){
            partTime="Part-Time";
            fullTime="Full-Time";
            freelance="Freelance";
            remote=false;
        }
        if(officeOnly == null && remoteOnly == null && partialRemote == null){
            officeOnly="Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote="Partial-Remote";
            type=false;
        }
        if(!dateSearchFlag && !remote && !type &&  !StringUtils.hasText(job) && !StringUtils.hasText(location)){
            jobPost=jobPostActivityService.getAll();
        }else{
            jobPost=jobPostActivityService.search(job,location, Arrays.asList(partTime,fullTime,freelance),
                    Arrays.asList(remoteOnly,officeOnly,partialRemote),searchDate);
        }
        Object currentUserProfile = usersService.getCurrentUserprofile();
       Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
       if(!(authentication instanceof AnonymousAuthenticationToken)){
           String currentUserName= authentication.getName();//Providing the email
           System.out.println(currentUserName);
           model.addAttribute("username",currentUserName);
           if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
               List<RecruiterJobsDto> recruiterJobsDtoList = jobPostActivityService.getRecruiterJobs(((RecruiterProfile) currentUserProfile).getUserAccountId());
               model.addAttribute("jobPost",recruiterJobsDtoList);
           }else{
List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getCandidateJobs((JobSeekerProfile) currentUserProfile);
List<JobSeekerSave> jobSeekerSaveList=jobSeekerSaveService.getCandidateJob((JobSeekerProfile) currentUserProfile);

               boolean exist;
               boolean saved;
               for(JobPostActivity jobActivity : jobPost){
                   exist=false;
                   saved=false;
                   for(JobSeekerApply jobSeekerApply : jobSeekerApplyList) {
                       if (Objects.equals(jobActivity.getJobPostId(), jobSeekerApply.getJob().getJobPostId())) {
                           jobActivity.setisActive(true);
                           exist = true;
                           break;
                       }
                   }
                       for(JobSeekerSave jobSeekerSave:jobSeekerSaveList){
                           if(Objects.equals(jobActivity.getJobPostId() , jobSeekerSave.getJob().getJobPostId())){
                               jobActivity.setisSaved(true);
                               saved=true;
                               break;
                           }
                       }
                       if(!exist) jobActivity.setisActive(false);

                       if(!saved) jobActivity.setisSaved(false);

                    model.addAttribute("jobPost",jobPost);
               }
           }
       }
model.addAttribute("user" , currentUserProfile);
        return "Dashboard";
    }

    @GetMapping("/Dashboard/add")
    public String addjobs(Model model){
        model.addAttribute("jobPostActivity" , new JobPostActivity());
        model.addAttribute("user" , usersService.getCurrentUserprofile());
        return "add-jobs";
    }

    @PostMapping("/Dashboard/addNew")
    public String addNew(JobPostActivity jobPostActivity , Model model){
        Users user=usersService.getCurrentUser();
        if(user!=null){
            jobPostActivity.setPostedById(user);
        }
        jobPostActivity.setPostedDate(new Date());
        model.addAttribute("jobPostActivity" , jobPostActivity);
        jobPostActivityService.addNew(jobPostActivity);
        return "redirect:/Dashboard/";
    }
 @PostMapping("dashboard/edit/{id}")
    public String editjob(@PathVariable("id") int id,Model model){
      JobPostActivity jobPostActivity =  jobPostActivityService.getOne(id);
      model.addAttribute("jobPostActivity",jobPostActivity);
      model.addAttribute("user" , usersService.getCurrentUserprofile());
        return "add-jobs";
    }

    @GetMapping("global-search/")
    public String globalSearch(Model model, @RequestParam(value = "job", required = false) String job,
                               @RequestParam(value = "location", required = false) String location,
                               @RequestParam(value = "partTime", required = false) String partTime,
                               @RequestParam(value = "fullTime", required = false) String fullTime,
                               @RequestParam(value = "freelance", required = false) String freelance,
                               @RequestParam(value = "remoteOnly", required = false) String remoteOnly,
                               @RequestParam(value = "officeOnly", required = false) String officeOnly,
                               @RequestParam(value = "partialRemote", required = false) String partialRemote,
                               @RequestParam(value = "today", required = false) boolean today,
                               @RequestParam(value = "days7", required = false) boolean days7,
                               @RequestParam(value = "days30", required = false) boolean days30){

        model.addAttribute("partTime" , Objects.equals(partTime,"Part-Time"));
        model.addAttribute("fullTime" , Objects.equals(fullTime,"Full-Time"));
        model.addAttribute("freelance" , Objects.equals(partTime,"Freelance"));
        model.addAttribute("remoteOnly" , Objects.equals(remoteOnly,"Remote-Only"));
        model.addAttribute("officeOnly" , Objects.equals(officeOnly,"Office-Only"));
        model.addAttribute("partialRemote" , Objects.equals(partialRemote,"Partial-Remote"));
        model.addAttribute("today");
        model.addAttribute("days7");
        model.addAttribute("days30" );
        model.addAttribute("job",job);
        model.addAttribute("location",location);


        LocalDate searchDate=null;
        List<JobPostActivity> jobPost=null;
        boolean dateSearchFlag=true;
        boolean remote=true;
        boolean type=true;

        if(days30){
            searchDate=LocalDate.now().minusDays(30);
        } else if(days7){
            searchDate=LocalDate.now().minusDays(7);
        }else if(today){
            searchDate=LocalDate.now();
        }else{
            dateSearchFlag=false;
        }

        if(partTime==null && fullTime==null && freelance == null ){
            partTime="Part-Time";
            fullTime="Full-Time";
            freelance="Freelance";
            remote=false;
        }
        if(officeOnly == null && remoteOnly == null && partialRemote == null){
            officeOnly="Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote="Partial-Remote";
            type=false;
        }
        if(!dateSearchFlag && !remote && !type &&  !StringUtils.hasText(job) && !StringUtils.hasText(location)){
            jobPost=jobPostActivityService.getAll();
        }else{
            jobPost=jobPostActivityService.search(job,location, Arrays.asList(partTime,fullTime,freelance),
                    Arrays.asList(remoteOnly,officeOnly,partialRemote),searchDate);
        }

        model.addAttribute("jobPost",jobPost);
        return "global-search";
    }
}
