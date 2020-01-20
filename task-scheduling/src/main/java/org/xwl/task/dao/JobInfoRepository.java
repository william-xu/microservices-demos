package org.xwl.task.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xwl.task.model.JobInfo;
import org.xwl.task.model.JobInfoKey;

@Repository
public interface JobInfoRepository extends org.springframework.data.repository.Repository<JobInfo, JobInfoKey>{
	
	@Query(value=" select  	j.sched_name, j.job_name, j.job_group, j.description, j.job_class_name, " 
					+ " 	t.trigger_name, t.trigger_group, t.next_fire_time, c.cron_expression " 
					+ " from " 
					+ " 	qrtz_job_details j,  qrtz_triggers t,  qrtz_cron_triggers c " 
					+ " where " 
					+ " 	j.sched_name = t.sched_name and j.job_name = t.job_name and j.job_group = t.job_group " 
					+ " 	and t.sched_name = c.sched_name and t.trigger_name = c.trigger_name and t.trigger_group = c.trigger_group "
					+ " 	and j.sched_name= :schedName and j.job_name = :jobName and j.job_group = :jobGroup ",
			nativeQuery = true )
	public Optional<JobInfo> findByKey(@Param("schedName") String schedName, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

}
