package com.platform.iperform.dataaccess.config.mapper;

import com.platform.iperform.dataaccess.config.entity.ConfigEntity;
import com.platform.iperform.model.Config;
import org.springframework.stereotype.Component;

@Component
public class ConfigDataAccessMapper {
    public Config configEntityToConfig(ConfigEntity configEntity) {
        return Config.builder()
                .id(configEntity.getId())
                .checkIn(configEntity.isCheckIn())
                .checkPoint(configEntity.isCheckPoint())
                .guidCheckIn(configEntity.getGuidCheckIn())
                .guidCheckPoint(configEntity.getGuidCheckPoint())
                .guidEks(configEntity.getGuidEks())
                .build();
    }

    public ConfigEntity configToConfigEntity(Config config) {
        return ConfigEntity.builder()
                .id(config.getId())
                .guidEks(config.getGuidEks())
                .guidCheckPoint(config.getGuidCheckPoint())
                .checkIn(config.isCheckIn())
                .checkPoint(config.isCheckPoint())
                .build();
    }
}
