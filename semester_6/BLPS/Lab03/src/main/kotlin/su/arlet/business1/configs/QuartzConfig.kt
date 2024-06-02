package su.arlet.business1.configs

import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.impl.matchers.GroupMatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import su.arlet.business1.schedule.SchedulerProperties

@Configuration
class QuartzConfig {
    @Bean
    fun scheduler(triggers: List<Trigger>, jobDetails: List<JobDetail>, factory: SchedulerFactoryBean): Scheduler {
        factory.setWaitForJobsToCompleteOnShutdown(true)

        println("Prepare scheduler to start")
        val scheduler = factory.scheduler

        revalidateJobs(jobDetails, scheduler)
        rescheduleTriggers(triggers, scheduler)

        scheduler.start()

        println("Scheduler has started")

        return scheduler
    }

    fun rescheduleTriggers(triggers: List<Trigger>, scheduler: Scheduler) {
        triggers.forEach {
            if (!scheduler.checkExists(it.key)) {
                scheduler.scheduleJob(it)
            } else {
                scheduler.rescheduleJob(it.key, it)
            }
        }
    }

    fun revalidateJobs(jobDetails: List<JobDetail>, scheduler: Scheduler) {
        val jobKeys = jobDetails.map { it.key }
        scheduler.getJobKeys(GroupMatcher.jobGroupEquals(SchedulerProperties.PERMANENT_JOBS_GROUP_NAME)).forEach {
            if (it !in jobKeys) {
                scheduler.deleteJob(it)
            }
        }
    }
}
