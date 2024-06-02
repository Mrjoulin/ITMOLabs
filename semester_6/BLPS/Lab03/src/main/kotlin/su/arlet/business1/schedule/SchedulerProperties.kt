package su.arlet.business1.schedule

class SchedulerProperties {
    companion object {
        const val PERMANENT_JOBS_GROUP_NAME: String = "PERMANENT"

        // Every start of hour
        const val EXPIRED_AD_CHECK_JOB_CRON: String = "0 0 * * * ?"

        // Every start of day at 00:00:00
        const val NEW_ADS_CHECK_JOB_CRON: String = "0 0 0 * * ?"
    }
}
