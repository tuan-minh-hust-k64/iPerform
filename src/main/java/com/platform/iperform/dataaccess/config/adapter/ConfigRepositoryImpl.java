package com.platform.iperform.dataaccess.config.adapter;

import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.dataaccess.config.entity.ConfigEntity;
import com.platform.iperform.dataaccess.config.mapper.ConfigDataAccessMapper;
import com.platform.iperform.dataaccess.config.repository.ConfigJpaRepository;
import com.platform.iperform.model.Config;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ConfigRepositoryImpl {
    public final ConfigDataAccessMapper configDataAccessMapper;
    public final ConfigJpaRepository configJpaRepository;

    public ConfigRepositoryImpl(ConfigDataAccessMapper configDataAccessMapper, ConfigJpaRepository configJpaRepository) {
        this.configDataAccessMapper = configDataAccessMapper;
        this.configJpaRepository = configJpaRepository;
    }

    public Optional<Config> getConfigPage() {
        return Optional.ofNullable(configDataAccessMapper.configEntityToConfig(configJpaRepository.findAll().stream().findFirst().orElse(ConfigEntity.builder().build())));
    }
    public Config save(Config config) {
        return configDataAccessMapper.configEntityToConfig(
                configJpaRepository.save(configDataAccessMapper.configToConfigEntity(config))
        );
    }
    public Optional<ConfigEntity> findById(UUID id) {
        return configJpaRepository.findById(id);
    }
}
