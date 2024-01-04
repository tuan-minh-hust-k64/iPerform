package com.platform.iperform.service;

import com.platform.iperform.common.configdata.SlackConfigData;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.users.UsersLookupByEmailRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class SlackService {
    private final SlackConfigData slackConfigData;
    private final static Slack slack = Slack.getInstance();

    public SlackService(SlackConfigData slackConfigData) {
        this.slackConfigData = slackConfigData;
    }
    public void sendMessageDM(String email, String content){
        String userSlackId = getUserSlackIdByEmail(email);
        try {
            ChatPostMessageResponse response = slack.methods(slackConfigData.getBotUserOauthToken()).chatPostMessage(req -> req
                    .channel(userSlackId)
                    .text(content)
            );
        } catch (IOException | SlackApiException e) {
            log.error("Send DM Slack error with userSlackId: {}, content: {}, error: {}", userSlackId, content, e.getMessage());
        }
    }
    public String getUserSlackIdByEmail(String email) {
        try {
            UsersLookupByEmailResponse response = slack.methods(slackConfigData.getBotUserOauthToken()).usersLookupByEmail(UsersLookupByEmailRequest.builder()
                    .email(email)
                    .build());
            return response.getUser().getId();
        } catch (IOException | SlackApiException e) {
            log.error("Get user slack id by email: {}, error: {}", email, e.getMessage());
        }
        return null;
    }
}
