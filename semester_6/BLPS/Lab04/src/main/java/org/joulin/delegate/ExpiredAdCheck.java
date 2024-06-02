package org.joulin.delegate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.joulin.core.AdPost;
import org.joulin.core.enums.AdPostStatus;
import org.joulin.repos.AdPostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExpiredAdCheck implements JavaDelegate {

    private final AdPostRepo adPostRepo;
    @PersistenceContext
    private final EntityManager entityManager;

    public ExpiredAdCheck(AdPostRepo adPostRepo, EntityManager entityManager) {
        this.adPostRepo = adPostRepo;
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Start checking expired Ads");
        List<AdPost> publishedAdPosts = adPostRepo.findAllByStatus(AdPostStatus.PUBLISHED);

        LocalDateTime now = LocalDateTime.now();
        List<AdPost> expiredAds = publishedAdPosts.stream()
                .filter(adPost -> {
                    LocalDateTime publishDateTime = adPost.getPublishDate();
                    Integer lifeHours = adPost.getAdRequest().getLifeHours();
                    LocalDateTime expirationDateTime = publishDateTime.plusHours(lifeHours != null ? lifeHours : 0);
                    return expirationDateTime.isBefore(now);
                })
                .collect(Collectors.toList());

        if (!expiredAds.isEmpty()) {
            entityManager.joinTransaction();

            expiredAds.forEach(adPost -> adPost.setStatus(AdPostStatus.EXPIRED));
            adPostRepo.saveAll(expiredAds);
        }

        System.out.println("End checking expired Ads, expired " + expiredAds.size() + " Ads");
    }
}