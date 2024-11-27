package com.AyushToCode.JobPortal.services;

import com.AyushToCode.JobPortal.entity.UsersType;
import com.AyushToCode.JobPortal.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeService {
    private final UserTypeRepository userTypeRepository;

    @Autowired
    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }
    public List<UsersType> getAll(){
        return userTypeRepository.findAll();
    }
}
