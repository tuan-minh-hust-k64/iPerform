package com.platform.iperform.dataaccess.config.adapter;

import com.platform.iperform.common.exception.ConfigNotFoundException;
import com.platform.iperform.dataaccess.config.mapper.ConfigDataAccessMapper;
import com.platform.iperform.dataaccess.config.repository.ConfigJpaRepository;
import com.platform.iperform.model.Config;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConfigRepositoryImpl {
    public final ConfigDataAccessMapper configDataAccessMapper;
    public final ConfigJpaRepository configJpaRepository;

    public ConfigRepositoryImpl(ConfigDataAccessMapper configDataAccessMapper, ConfigJpaRepository configJpaRepository) {
        this.configDataAccessMapper = configDataAccessMapper;
        this.configJpaRepository = configJpaRepository;
    }

    public Optional<Config> getConfigPage() {
        return Optional.ofNullable(configDataAccessMapper.configEntityToConfig(configJpaRepository.findAll().stream().findFirst().orElseThrow(
                ConfigNotFoundException::new
        )));
    }
    public Config save(Config config) {
        return configDataAccessMapper.configEntityToConfig(
                configJpaRepository.save(configDataAccessMapper.configToConfigEntity(config))
        );
    }
}
