package com.AyushToCode.JobPortal.services;

import com.AyushToCode.JobPortal.entity.JobSeekerProfile;
import com.AyushToCode.JobPortal.entity.RecruiterProfile;
import com.AyushToCode.JobPortal.entity.Users;
import com.AyushToCode.JobPortal.repository.JobSeekerProfileRepository;
import com.AyushToCode.JobPortal.repository.RecruiterProfileRepository;
import com.AyushToCode.JobPortal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    private final UserRepository userRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;

    private final RecruiterProfileRepository recruiterProfileRepository;

    //This passwordencoder is storing an object of the BCRYPTPasswordEncoder
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UserRepository userRepository, JobSeekerProfileRepository jobSeekerProfileRepository, RecruiterProfileRepository recruiterProfileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users adduser(Users users) {
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        if(users.getPassword()!=null) users.setPassword(passwordEncoder.encode(users.getPassword()));
        System.out.println("hi");
        Users saveduser = userRepository.save(users);
        int userTypeId = users.getUserTypeId().getUserTypeId();
        System.out.println("hi");
        if (userTypeId == 1) {
            recruiterProfileRepository.save(new RecruiterProfile(saveduser));
        } else {
            jobSeekerProfileRepository.save((new JobSeekerProfile(saveduser)));
        }
        System.out.println("User is saved");
        return saveduser;
    }

    public Object getCurrentUserprofile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users users = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found the user/"));
            int userId = users.getUserId();

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                return recruiterProfileRepository.findById(userId).orElse(new RecruiterProfile());
            } else {
                return jobSeekerProfileRepository.findById(userId).orElse(new JobSeekerProfile());
            }

        }
        return null;
    }

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users users = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found the user/"));
            return users;
        }
        return null;
    }

    public Optional<Users> findByEmail(String currentUsername) {
        return userRepository.findByEmail(currentUsername);
    }
}
