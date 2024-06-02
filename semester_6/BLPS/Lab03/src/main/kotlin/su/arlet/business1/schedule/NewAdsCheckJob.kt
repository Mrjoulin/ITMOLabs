package su.arlet.business1.schedule

import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.quartz.PersistJobDataAfterExecution
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component
import su.arlet.business1.core.enums.AdPostStatus
import su.arlet.business1.core.enums.UserRole
import su.arlet.business1.core.letters.NewAdsLetter
import su.arlet.business1.gateways.email.EmailGateway
import su.arlet.business1.repos.AdPostRepo
import su.arlet.business1.repos.UserRepo

@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
class NewAdsCheckJob @Autowired constructor(
    private val adPostRepo: AdPostRepo,
    private val userRepo: UserRepo,
    private val emailGateway: EmailGateway,
) : QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {
        println("Start checking new Ads")
        val dataMap = context.jobDetail.jobDataMap
        val previousAds = dataMap["previousAds"] as List<*>?

        val readyAdPosts = adPostRepo.findAllByStatus(AdPostStatus.READY_TO_PUBLISH)
        val newAdPosts = readyAdPosts.filter { adPost ->
            previousAds?.contains(adPost.id)?.not() ?: true
        }

        if (newAdPosts.isEmpty()) {
            println("End checking new Ads, new Ads not found")
            return
        }

        val editors = userRepo.findAllByRole(UserRole.EDITOR).filter { it.email != null }

        if (editors.isEmpty()) {
            println("End checking new Ads, no editors with email found")
            return
        }

        val letter = NewAdsLetter(newAdPosts)
        editors.forEach { editor ->
            emailGateway.sendEmail(editor.email!!, letter)
        }

        // Add new posts ids to prevent sending information about the same post several times
        val checkedAdsIds = previousAds?.toMutableList() ?: mutableListOf()
        checkedAdsIds.addAll(newAdPosts.map { it.id })
        dataMap["previousAds"] = checkedAdsIds

        println("End checking new Ads, found ${newAdPosts.size} new Ads, send emails to ${editors.size} editors")
    }
}
