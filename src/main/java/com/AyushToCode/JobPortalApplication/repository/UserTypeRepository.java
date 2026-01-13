package com.AyushToCode.JobPortalApplication.repository;
import com.AyushToCode.JobPortalApplication.entity.UsersType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UsersType,Integer>{
}
