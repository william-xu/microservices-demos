package org.xwl.task.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronSequenceGenerator;


@EnableScheduling
@Configuration
@ConfigurationProperties("quartz.cust")
public class TaskConfig {
	ExecutorService executor = Executors.newFixedThreadPool(3);

	private String custCronExpr;
	
	public String getCustCronExpr() {
		return custCronExpr;
	}

	boolean changed = false;
	public void setCustCronExpr(String custCronExpr) {
		changed = !"".equalsIgnoreCase(this.custCronExpr) && !custCronExpr.equalsIgnoreCase(this.custCronExpr);
		this.custCronExpr = custCronExpr;
		if(changed) {
			executor.execute(r);
		}
	}

	ScheduledTaskRegistrar reg;
	final String CURR_JOB_ID = "CustJob_01";
	@SuppressWarnings("unchecked")
	Runnable r = () -> {		
		try {
			//移除指定ID的已安排定时任务
			if(reg == null) return;
			Field scheduledTasks = reg.getClass().getDeclaredField("scheduledTasks");
			scheduledTasks.setAccessible(true);
			//获取定时任务注册器实例中的定时任务集合，之所以通过反射获取是因为直接调用注册器的get方法返回的是不可修改的集合
			Set<ScheduledTask> tasks = (Set<ScheduledTask>)scheduledTasks.get(reg);
			ScheduledTask target = null;
			Iterator<ScheduledTask> it = tasks.iterator();			
			while(it.hasNext()) {
				target = it.next();
				//这里根据自定义Runnable找到目标类，并通过id字段来区别
				if(target.getTask().getRunnable() instanceof CustRunnable) {
					CustRunnable runnable = (CustRunnable)target.getTask().getRunnable();
					if(CURR_JOB_ID.equalsIgnoreCase(runnable.getId())){
						target.cancel();		//取消以安排定时任务的执行
						tasks.remove(target);	//将任务从已安排列表中移除										
						break;	
					}
				}
				target = null;
			}
			//使用新的表达式创建新的定时任务
			Field triggerTasks = reg.getClass().getDeclaredField("triggerTasks");
			triggerTasks.setAccessible(true);
			List<TriggerTask> trigTaskList = (List<TriggerTask>)triggerTasks.get(reg);
			TriggerTask currTask = null;
			for(int i=0;i<trigTaskList.size();i++) {
				if(trigTaskList.get(i).getRunnable() instanceof CustRunnable) {
					CustRunnable runnable = (CustRunnable)trigTaskList.get(i).getRunnable();
					if(CURR_JOB_ID.equals(runnable.getId())) {
						currTask = trigTaskList.get(i);
						break;
					}
				}
			}
			Method m = reg.getClass().getDeclaredMethod("addScheduledTask", new Class[] {ScheduledTask.class});
			m.setAccessible(true);
			m.invoke(reg, reg.scheduleTriggerTask(currTask));
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}		
	};	
	
	/**
	 * 通过实现SchedulingConfigurer接口创建定时任务
	 */
	@Bean
	public SchedulingConfigurer simpleTask() {
		CustRunnable runnable1 = new CustRunnable() {
			@Override
			public void run() {
				System.out.println(" Scheduled [ " + custCronExpr + " ] Task executing at -- " 
						+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
			@Override
			public String getId() {
				return CURR_JOB_ID;
			}			
		};
				
		Trigger trigger = (triggerContext) 
							-> { return new CronSequenceGenerator(custCronExpr).next(new Date()); };
		SchedulingConfigurer config = (taskRegistrar)  
							-> {
									this.reg = taskRegistrar;
									taskRegistrar.addTriggerTask(runnable1, trigger);
								};
		return config;
	}

}
