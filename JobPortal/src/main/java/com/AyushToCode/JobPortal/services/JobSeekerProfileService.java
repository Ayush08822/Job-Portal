package com.AyushToCode.JobPortal.services;

import com.AyushToCode.JobPortal.entity.JobSeekerProfile;
import com.AyushToCode.JobPortal.entity.RecruiterProfile;
import com.AyushToCode.JobPortal.entity.Users;
import com.AyushToCode.JobPortal.repository.JobSeekerProfileRepository;
import com.AyushToCode.JobPortal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UserRepository userRepository;

@Autowired
    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UserRepository userRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
    this.userRepository = userRepository;
}

    public Optional<JobSeekerProfile> getOne(Integer id){
        return jobSeekerProfileRepository.findById(id);
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {
    return jobSeekerProfileRepository.save(jobSeekerProfile);
    }

    public JobSeekerProfile getCurrentSeekerProfile() {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)){
                String currentusername=authentication.getName();
                Users user =userRepository.findByEmail(currentusername).orElseThrow(() -> new UsernameNotFoundException("Could not found user"));
                Optional<JobSeekerProfile> jobSeekerProfile= getOne(user.getUserId());
                return jobSeekerProfile.orElse(null);
            }else return null;
        }
    }

