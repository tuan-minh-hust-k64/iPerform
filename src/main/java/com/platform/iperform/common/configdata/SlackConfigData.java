package com.platform.iperform.common.configdata;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "slack-config")
public class SlackConfigData {
    private String userOauthToken;
    private String botUserOauthToken;
}
