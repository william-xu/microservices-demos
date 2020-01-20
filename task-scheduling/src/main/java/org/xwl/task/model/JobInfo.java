package org.xwl.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(JobInfoKey.class)
public class JobInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	private String schedName;	
	@Id
	private String jobName;	
	@Id
	private String jobGroup;

	private String description;

	@Column(name = "job_class_name")
	private String jobClassName;
	
	@Column(name = "trigger_name")
	private String triggerName;
	
	@Column(name = "trigger_group")
	private String triggerGroup;
	
	@Column(name = "next_fire_time")
	private String nextFireTime;
	
	@Column(name = "cron_expression")
	private String cronExpression;	
	
}
