package org.joulin.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.joulin.core.AdPost;
import org.joulin.core.enums.AdPostStatus;
import org.joulin.repos.AdPostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoadAds implements JavaDelegate {
    @Autowired
    private final AdPostRepo adPostRepo;

    public LoadAds(AdPostRepo adPostRepo) {
        this.adPostRepo = adPostRepo;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<AdPost> ad_posts = adPostRepo.findAllByStatus(AdPostStatus.READY_TO_PUBLISH);
        List<SelectableAdPost> selectableAdPosts = ad_posts.stream().map(adPost -> new SelectableAdPost(adPost)).toList();

        ObjectValue ad_posts_value =
                Variables.objectValue(selectableAdPosts).serializationDataFormat("application/json").create();

        delegateExecution.setVariable("ad_posts", ad_posts_value);
        delegateExecution.setVariable("has_ad_posts", !ad_posts.isEmpty());
    }
}
