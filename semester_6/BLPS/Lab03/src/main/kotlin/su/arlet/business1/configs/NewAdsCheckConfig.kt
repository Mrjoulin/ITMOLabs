package su.arlet.business1.configs

import org.quartz.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import su.arlet.business1.schedule.NewAdsCheckJob
import su.arlet.business1.schedule.SchedulerProperties

@Configuration
class NewAdsCheckConfig {
    @Bean
    fun newAdsCheckJobDetail(): JobDetail = JobBuilder
        .newJob(NewAdsCheckJob::class.java)
        .withIdentity("newAdsCheckJob", SchedulerProperties.PERMANENT_JOBS_GROUP_NAME)
        .storeDurably()
        .requestRecovery(true)
        .build()

    @Bean
    fun newAdsCheckTrigger(): Trigger = TriggerBuilder.newTrigger()
        .forJob(newAdsCheckJobDetail())
        .withIdentity("newAdsCheckJobTrigger", SchedulerProperties.PERMANENT_JOBS_GROUP_NAME)
        .withSchedule(CronScheduleBuilder.cronSchedule(SchedulerProperties.NEW_ADS_CHECK_JOB_CRON))
        .build()
}