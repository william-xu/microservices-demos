package org.xwl.task.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;

public class ScheduledTaskRegistrarHandler {
	static Logger log = LoggerFactory.getLogger(ScheduledTaskRegistrarHandler.class);
	
	static ExecutorService executor = Executors.newFixedThreadPool(3);

	public static void rescheduleTask(ScheduledTaskRegistrar registrar, String jobId) {
		try {
			Runnable r = () -> {
				boolean removed = removeJobById(registrar, jobId);
				if (removed) {
					createNewTask(registrar, jobId);
				}
			};
			executor.execute(r);
		} catch (Exception e) {
			log.error("重设任务[" + jobId + "]失败！异常原因：" + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean removeJobById(ScheduledTaskRegistrar registrar, String jobId) {
		boolean done = false;
		;
		// 移除指定ID的已安排定时任务
		if (registrar == null || jobId == null || jobId.trim().length() == 0)
			return done;
		Field scheduledTasks;
		try {
			scheduledTasks = registrar.getClass().getDeclaredField("scheduledTasks");
			scheduledTasks.setAccessible(true);
			// 获取定时任务注册器实例中的定时任务集合，之所以通过反射获取是因为直接调用注册器的get方法返回的是不可修改的集合
			Set<ScheduledTask> tasks = (Set<ScheduledTask>) scheduledTasks.get(registrar);
			ScheduledTask target = null;
			Iterator<ScheduledTask> it = tasks.iterator();
			while (it.hasNext()) {
				target = it.next();
				// 这里根据自定义Runnable找到目标类，并通过id字段来区别
				if (target.getTask().getRunnable() instanceof CustRunnable) {
					CustRunnable runnable = (CustRunnable) target.getTask().getRunnable();
					if (jobId.equalsIgnoreCase(runnable.getId())) {
						target.cancel(); // 取消以安排定时任务的执行
						tasks.remove(target); // 将任务从已安排列表中移除
						break;
					}
				}
				target = null;
			}
			done = true;
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return done;
	}

	@SuppressWarnings("unchecked")
	public static boolean createNewTask(ScheduledTaskRegistrar registrar, String jobId) {
		boolean created = false;
		try {
			// 使用新的表达式创建新的定时任务
			Field triggerTasks = registrar.getClass().getDeclaredField("triggerTasks");
			triggerTasks.setAccessible(true);
			List<TriggerTask> trigTaskList = (List<TriggerTask>) triggerTasks.get(registrar);
			TriggerTask currTask = null;
			for (int i = 0; i < trigTaskList.size(); i++) {
				if (trigTaskList.get(i).getRunnable() instanceof CustRunnable) {
					CustRunnable runnable = (CustRunnable) trigTaskList.get(i).getRunnable();
					if (jobId.equals(runnable.getId())) {
						currTask = trigTaskList.get(i);
						break;
					}
				}
			}
			Method m = registrar.getClass().getDeclaredMethod("addScheduledTask", new Class[] { ScheduledTask.class });
			m.setAccessible(true);
			m.invoke(registrar, registrar.scheduleTriggerTask(currTask));
			created = true;
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
		return created;
	}

}
