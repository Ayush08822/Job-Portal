package com.AyushToCode.JobPortalApplication.Controller;
import com.AyushToCode.JobPortalApplication.entity.RecruiterProfile;
import com.AyushToCode.JobPortalApplication.entity.Users;
import com.AyushToCode.JobPortalApplication.repository.UserRepository;
import com.AyushToCode.JobPortalApplication.services.RecruiterProfileService;
import com.AyushToCode.JobPortalApplication.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {
    private final UserRepository userRepository;
    private final RecruiterProfileService recruiterProfileService;

    @Autowired
    public RecruiterProfileController(UserRepository userRepository, RecruiterProfileService recruiterProfileService) {
        this.userRepository = userRepository;
        this.recruiterProfileService = recruiterProfileService;
    }


    @GetMapping("/")
    public String recruiterProfile(Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String username = authentication.getName();
            Users users = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user"));

            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(users.getUserId());

            if(!recruiterProfile.isEmpty()){
                model.addAttribute("profile" , recruiterProfile.get());
            }
        }
        return "recruiter_profile";

    }

    @PostMapping("/addNew")
    public String addNew(RecruiterProfile recruiterProfile , @RequestParam("image")MultipartFile multipartFile , Model model){

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof  AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users users = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user"));

            recruiterProfile.setUserId(users);
            recruiterProfile.setUserAccountId(users.getUserId());
        }
        model.addAttribute("profile" , recruiterProfile);
        String fileName = "";
        if(!multipartFile.getOriginalFilename().equals("")){
            fileName= StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiterProfile.setProfilePhoto(fileName);
        }
        System.out.println(fileName);
        RecruiterProfile savedUser=recruiterProfileService.addNew(recruiterProfile);

        String uploadDir = "photos/recruiter/"+savedUser.getUserAccountId();
        try{
            FileUploadUtil.saveFile(uploadDir , fileName , multipartFile);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/Dashboard/";
    }

}
