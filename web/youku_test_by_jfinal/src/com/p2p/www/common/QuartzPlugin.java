package com.p2p.www.common;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.p2p.www.jobs.VediosRefreshJob;

public class QuartzPlugin implements IPlugin {

	private static final Logger Log = Logger.getLogger(QuartzPlugin.class);
	
	private static final String CRON_EXPRESSION = "0 */1 * * * ? *";
	
	private SchedulerFactory sf = null;
	private Scheduler sched = null;

	@Override
	public boolean start() {
		sf = new StdSchedulerFactory();
		try {
			sched = sf.getScheduler();
			
			JobDetail job = JobBuilder.newJob(VediosRefreshJob.class)
					.withIdentity("refresh_job", "job_group").build();

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("refresh_trigger", "trigger_group")
					.withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION))
					.startNow().build();
			
			sched.scheduleJob(job , trigger);
			sched.start();
		} catch (SchedulerException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean stop() {
		try {
			sched.shutdown();
		} catch (SchedulerException e) {
			Log.error("shutdown error", e);
			return false;
		}
		return true;
	}
}