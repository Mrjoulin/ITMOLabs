package su.arlet.business1.schedule

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.quartz.PersistJobDataAfterExecution
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import su.arlet.business1.core.enums.AdPostStatus
import su.arlet.business1.repos.AdPostRepo
import java.time.LocalDateTime

@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
class ExpiredAdCheckJob @Autowired constructor(
    private val adPostRepo: AdPostRepo,
    @PersistenceContext
    private val entityManager: EntityManager
) : QuartzJobBean() {

    @Transactional
    override fun executeInternal(context: JobExecutionContext) {
        println("Start checking expired Ads")
        val publishedAdPosts = adPostRepo.findAllByStatus(AdPostStatus.PUBLISHED)

        val now = LocalDateTime.now()
        val expiredAds = publishedAdPosts.filter { adPost ->
            val publishDateTime = adPost.publishDate
            val expirationDateTime = publishDateTime?.plusHours(
                adPost.adRequest.lifeHours?.toLong() ?: 0
            )
            expirationDateTime?.isBefore(now) ?: false
        }

        if (expiredAds.isNotEmpty()) {
            entityManager.joinTransaction()

            expiredAds.forEach { it.status = AdPostStatus.EXPIRED }
            adPostRepo.saveAll(expiredAds)
        }

        println("End checking expired Ads, expired ${expiredAds.size} Ads")
    }
}
