package org.joulin.delegate;

import kotlin.Suppress;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.impl.json.jackson.JacksonJsonNode;
import org.joulin.core.AdPost;
import org.joulin.core.AdRequest;
import org.joulin.core.Article;
import org.joulin.core.enums.AdPostStatus;
import org.joulin.core.enums.AdRequestStatus;
import org.joulin.core.enums.ArticleStatus;
import org.joulin.repos.AdPostRepo;
import org.joulin.repos.AdRequestRepo;
import org.joulin.repos.ArticleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class PublishArticle implements JavaDelegate {

    private final ArticleRepo articleRepo;
    private final AdPostRepo adPostRepo;
    private final AdRequestRepo adRequestRepo;

    public PublishArticle(ArticleRepo articleRepo, AdPostRepo adPostRepo, AdRequestRepo adRequestRepo) {
        this.articleRepo = articleRepo;
        this.adPostRepo = adPostRepo;
        this.adRequestRepo = adRequestRepo;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String title = (String) delegateExecution.getVariable("article_title");
        String text = (String) delegateExecution.getVariable("article_text");
        List<SelectableAdPost> ad_posts_arr = (List<SelectableAdPost>) delegateExecution.getVariable("chosen_ad_posts");

        List<Long> ad_posts_ids = new ArrayList<>();

        if (ad_posts_arr != null) {
            ad_posts_ids = ad_posts_arr.stream()
                    .filter(SelectableAdPost::getChosen)
                    .map(SelectableAdPost::getId).toList();
        }

        JacksonJsonNode img_links_json = (JacksonJsonNode) delegateExecution.getVariable("img_links");
        List<String> images = img_links_json.elements().stream()
                .map(map -> (String) map.prop("link").value()).toList();

        List<AdPost> adPosts = new ArrayList<>();
        for (Long ad_post_id: ad_posts_ids) {
            adPostRepo.findById(ad_post_id).ifPresent(adPosts::add);
        }

        // Update ad posts and ad requests statuses to published
        for (AdPost adPost: adPosts) {
            AdRequest adRequest = adPost.getAdRequest();
            adRequest.setStatus(AdRequestStatus.PUBLISHED);
            adRequestRepo.save(adRequest);

            adPost.setStatus(AdPostStatus.PUBLISHED);
            adPost.setPublishDate(LocalDateTime.now());
            adPostRepo.save(adPost);
        }

        Article article = new Article(
            0, title, text, images, adPosts, ArticleStatus.PUBLISHED, null, null
        );
        articleRepo.save(article);
    }
}
