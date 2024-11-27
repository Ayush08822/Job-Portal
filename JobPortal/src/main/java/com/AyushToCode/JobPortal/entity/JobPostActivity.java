package com.AyushToCode.JobPortal.entity;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
public class JobPostActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobPostId;

    @ManyToOne
    @JoinColumn(name = "postedById" , referencedColumnName = "userId")
    private Users postedById;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "jobLocationId" ,referencedColumnName = "id")
    private JobLocation jobLocationId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "jobCompanyId" , referencedColumnName = "id")
    private JobCompany jobCompanyId;

    @Transient
    private boolean isSaved;

    @Transient
    private boolean isActive;

    @Length(max=10000)
        private String descriptionOfJob;

    private String jobType;
    private String salary;
    private String remote;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date postedDate;
    private String jobTitle;

    public JobPostActivity() {
    }

    public JobPostActivity(Integer jobPostId, Users postedById, JobLocation jobLocationId, JobCompany jobCompanyId, boolean isSaved, boolean isActive, String descriptionOfJob, String jobType, String salary, String remote, Date postedDate, String jobTitle) {
        this.jobPostId = jobPostId;
        this.postedById = postedById;
        this.jobLocationId = jobLocationId;
        this.jobCompanyId = jobCompanyId;
        this.isSaved = isSaved;
        this.isActive = isActive;
        this.descriptionOfJob = descriptionOfJob;
        this.jobType = jobType;
        this.salary = salary;
        this.remote = remote;
        this.postedDate = postedDate;
        this.jobTitle = jobTitle;
    }

    public Integer getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Integer jobPostId) {
        this.jobPostId = jobPostId;
    }

    public Users getPostedById() {
        return postedById;
    }

    public void setPostedById(Users postedById) {
        this.postedById = postedById;
    }

    public JobLocation getJobLocationId() {
        return jobLocationId;
    }

    public void setJobLocationId(JobLocation jobLocationId) {
        this.jobLocationId = jobLocationId;
    }

    public JobCompany getJobCompanyId() {
        return jobCompanyId;
    }

    public void setJobCompanyId(JobCompany jobCompanyId) {
        this.jobCompanyId = jobCompanyId;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setisSaved(boolean saved) {
        isSaved = saved;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setisActive(boolean active) {
        isActive = active;
    }

    public String getDescriptionOfJob() {
        return descriptionOfJob;
    }

    public void setDescriptionOfJob(String descriptionOfJob) {
        this.descriptionOfJob = descriptionOfJob;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return "JobPostActivity{" +
                "jobPostId=" + jobPostId +
                ", postedById=" + postedById +
                ", jobLocationId=" + jobLocationId +
                ", jobCompanyId=" + jobCompanyId +
                ", isSaved=" + isSaved +
                ", isActive=" + isActive +
                ", descriptionOfJob='" + descriptionOfJob + '\'' +
                ", jobType='" + jobType + '\'' +
                ", salary='" + salary + '\'' +
                ", remote='" + remote + '\'' +
                ", postedDate=" + postedDate +
                ", jobTitle='" + jobTitle + '\'' +
                '}';
    }
}
