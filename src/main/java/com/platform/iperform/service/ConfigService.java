package com.platform.iperform.service;

import com.platform.iperform.common.dto.request.ConfigRequest;
import com.platform.iperform.common.dto.response.ConfigResponse;
import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.dataaccess.config.adapter.ConfigRepositoryImpl;
import com.platform.iperform.dataaccess.config.entity.ConfigEntity;
import com.platform.iperform.dataaccess.config.mapper.ConfigDataAccessMapper;
import com.platform.iperform.model.Config;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ConfigService {
    private final ConfigRepositoryImpl configRepository;
    private final ConfigDataAccessMapper configDataAccessMapper;
    private final FunctionHelper functionHelper;

    public ConfigService(ConfigRepositoryImpl configRepository, ConfigDataAccessMapper configDataAccessMapper, FunctionHelper functionHelper) {
        this.configRepository = configRepository;
        this.configDataAccessMapper = configDataAccessMapper;
        this.functionHelper = functionHelper;
    }

    @Transactional
    public ConfigResponse createConfig(ConfigRequest configRequest) {
        Config result = configRepository.save(configRequest.getConfig());
        return ConfigResponse.builder()
                .config(result)
                .build();
    }
    @Transactional
    public ConfigResponse updateConfig(ConfigRequest configRequest) {
        if(configRequest.getConfig().getId() == null) {
            return this.createConfig(configRequest);
        } else {
            ConfigEntity configEntity = configRepository.findById(configRequest.getConfig().getId())
                    .orElseThrow(() -> new NotFoundException("Not Found Config with id: " + configRequest.getConfig().getId()));
            BeanUtils.copyProperties(
                    configRequest.getConfig(),
                    configEntity,
                    functionHelper.getNullPropertyNames(configRequest.getConfig())
            );
            Config result = configRepository.save(configDataAccessMapper.configEntityToConfig(configEntity));
            return ConfigResponse.builder()
                    .config(result)
                    .build();
        }
    }

    public ConfigResponse getConfig() {
        Config result = configRepository.getConfigPage()
                .orElse(Config.builder().build());
        return ConfigResponse.builder()
                .config(result)
                .build();
    }
}
