package br.com.yaman.TesteQuartz;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.HashSet;
import java.util.Set;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Exemplo de Quartz utilizando Cron como a trigger para executar
 * o Job.
 * @author Troyner
 *
 */
public class QuartzApplication {

	public static void main(String[] args) {
		try {
			QuartzApplication example = new QuartzApplication();
			example.run();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() throws Exception {

		Set<Trigger> triggers = new HashSet<>();
		
		JobDetail job = newJob(MyJob.class).withIdentity("job1", "group1").build();
		
		// Ativa trigger a cada 10 segundos
		CronTrigger trigger1 = newTrigger().withIdentity("trigger1", "group1")
					.withSchedule(cronSchedule("0/10 * * * * ?")).build();
			
		triggers.add(trigger1);

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		
		sched.scheduleJob(job, triggers, false);

		sched.start();

		CronTrigger trigger2 = newTrigger().withIdentity("trigger2", "group1")
					.withSchedule(cronSchedule("0/01 * * * * ?")).build();

		triggers.add(trigger2);
		
		// Adiciona uma nova trigger enquando o schedule do job já está executando.
		sched.scheduleJob(job, triggers, true);
		
		Thread.sleep(120L * 1000L);
		
		sched.shutdown();

	}
	
}
