package org.xwl.task.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;

import lombok.Data;

@Data
public class JobInfoKey implements Serializable{
	private static final long serialVersionUID = 1L;

	@Column(name = "sched_name")
	private String schedName;
	
	@Column(name = "job_name")
	private String jobName;
	
	@Column(name = "job_group")
	private String jobGroup;
	
	public JobInfoKey() {
		
	}
	
	public JobInfoKey(String schedName, String jobName, String jobGroup){
		this.schedName = schedName;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobInfoKey key = (JobInfoKey) o;
        return schedName.equals(key.schedName) &&
        		jobName.equals(key.jobName)&&
        		jobGroup.equals(key.jobGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedName, jobName, jobGroup);
    }
}
