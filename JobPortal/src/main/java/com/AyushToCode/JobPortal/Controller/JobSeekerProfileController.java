package com.AyushToCode.JobPortal.Controller;

import com.AyushToCode.JobPortal.entity.JobSeekerProfile;
import com.AyushToCode.JobPortal.entity.Skills;
import com.AyushToCode.JobPortal.entity.Users;
import com.AyushToCode.JobPortal.repository.UserRepository;
import com.AyushToCode.JobPortal.services.JobSeekerProfileService;
import com.AyushToCode.JobPortal.util.FileDownloadUtil;
import com.AyushToCode.JobPortal.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {

    private final JobSeekerProfileService jobSeekerProfileService;
    private final UserRepository userRepository;

    @Autowired
    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, UserRepository userRepository) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String JobSeekerProfile(Model model){    
        JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        List<Skills> skills=new ArrayList<>();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users users = userRepository.findByEmail(authentication.getName()).orElseThrow(() ->new UsernameNotFoundException("User not found"));
               Optional<JobSeekerProfile> seekerProfile =  jobSeekerProfileService.getOne(users.getUserId());
               if(seekerProfile.isPresent()) {
                   jobSeekerProfile = seekerProfile.get();
                   if (jobSeekerProfile.getSkills().isEmpty()) {
                       skills.add(new Skills());
                       jobSeekerProfile.setSkills(skills);
                   }
               }
               model.addAttribute("skills" , skills);
               model.addAttribute("profile" , jobSeekerProfile);
        }
        return "job-seeker-profile";

    }

    @PostMapping("/addNew")
    public  String addNew(JobSeekerProfile jobSeekerProfile , @RequestParam("image")MultipartFile image , @RequestParam("pdf") MultipartFile pdf,Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            jobSeekerProfile.setUserId(user);
            jobSeekerProfile.setUserAccountId(user.getUserId());

            List<Skills> skillsList = jobSeekerProfile.getSkills();
            model.addAttribute("profile", jobSeekerProfile);
            model.addAttribute("skills", skillsList);

            for (Skills skills : skillsList) {
                skills.setJobSeekerProfile(jobSeekerProfile);
            }
        }

        String imageName="";
        String resumeName="";

        if(!Objects.equals(image.getOriginalFilename(),"")){
        imageName= StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        jobSeekerProfile.setProfilePhoto(imageName);
        }

        if(!Objects.equals(pdf.getOriginalFilename(),"")){
            resumeName= StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
            jobSeekerProfile.setResume(resumeName);
        }

       JobSeekerProfile SeekerProfile= jobSeekerProfileService.addNew(jobSeekerProfile);

        try{
            String uploadDir = "photos/candidate/"+SeekerProfile.getUserAccountId();
            if(!Objects.equals(image.getOriginalFilename(),"")) {
                FileUploadUtil.saveFile(uploadDir , imageName , image);
            }
            if(!Objects.equals(pdf.getOriginalFilename(),"")) {
                FileUploadUtil.saveFile(uploadDir , resumeName , pdf);
            }

        }catch(IOException ex){
            throw new RuntimeException(ex);
        }
        return "redirect:/Dashboard/";
    }

    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") int id,Model model){
       Optional<JobSeekerProfile> jobSeekerProfile= jobSeekerProfileService.getOne(id);

       model.addAttribute("profile",jobSeekerProfile.get());
       return "job-seeker-profile";

    }

    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam(value = "fileName") String fileName,@RequestParam(value = "userID") String userId){
        FileDownloadUtil fileDownloadUtil=new FileDownloadUtil();
        Resource resource=null;
        try{
resource=fileDownloadUtil.getFileAsResource("photos/candidate/"+userId,fileName);
        }catch(IOException ex){
return ResponseEntity.badRequest().build();
        }
        if (resource == null){
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
        String contentType="application/octet-stream";
        String headerValue="attachment; fileName=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION,headerValue).body(resource);
    }
}
