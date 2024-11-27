package com.AyushToCode.JobPortal.services;

import com.AyushToCode.JobPortal.entity.RecruiterProfile;
import com.AyushToCode.JobPortal.entity.Users;
import com.AyushToCode.JobPortal.repository.RecruiterProfileRepository;
import com.AyushToCode.JobPortal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;


    @Autowired
    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UserRepository userRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
    }

    public Optional<RecruiterProfile> getOne(Integer id){
        return recruiterProfileRepository.findById(id);
    }

    public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
        return  recruiterProfileRepository.save(recruiterProfile);
    }

    public RecruiterProfile getCurrentRecruiterProfile() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentusername=authentication.getName();
       Users user =userRepository.findByEmail(currentusername).orElseThrow(() -> new UsernameNotFoundException("Could not found user"));
            Optional<RecruiterProfile> recruiterProfile= getOne(user.getUserId());
            return recruiterProfile.orElse(null);
        }else return null;
    }
}
