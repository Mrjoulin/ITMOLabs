package su.arlet.business1.configs

import org.quartz.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import su.arlet.business1.schedule.ExpiredAdCheckJob
import su.arlet.business1.schedule.SchedulerProperties

@Configuration
class ExpiredAdCheckConfig {
    @Bean
    fun expiredAdCheckJobDetail(): JobDetail = JobBuilder
        .newJob(ExpiredAdCheckJob::class.java)
        .withIdentity("expiredAdCheckJob", SchedulerProperties.PERMANENT_JOBS_GROUP_NAME)
        .storeDurably()
        .requestRecovery(true)
        .build()

    @Bean
    fun expiredAdCheckTrigger(): Trigger = TriggerBuilder.newTrigger()
        .forJob(expiredAdCheckJobDetail())
        .withIdentity("expiredAdCheckJobTrigger", SchedulerProperties.PERMANENT_JOBS_GROUP_NAME)
        .withSchedule(CronScheduleBuilder.cronSchedule(SchedulerProperties.EXPIRED_AD_CHECK_JOB_CRON))
        .build()
}