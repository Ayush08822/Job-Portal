package com.AyushToCode.JobPortal.repository;

import com.AyushToCode.JobPortal.entity.UsersType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UsersType,Integer>{
}
