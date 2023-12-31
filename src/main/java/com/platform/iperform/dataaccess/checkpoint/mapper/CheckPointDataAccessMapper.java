package com.platform.iperform.dataaccess.checkpoint.mapper;

import com.platform.iperform.dataaccess.checkpoint.entity.CheckPointEntity;
import com.platform.iperform.dataaccess.checkpoint.entity.CheckPointItemEntity;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.model.CheckPoint;
import com.platform.iperform.model.CheckPointItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class CheckPointDataAccessMapper {
    public final EksDataAccessMapper eksDataAccessMapper;


    public CheckPointDataAccessMapper(EksDataAccessMapper eksDataAccessMapper) {
        this.eksDataAccessMapper = eksDataAccessMapper;
    }

    public CheckPoint checkPointEntityToCheckPoint(CheckPointEntity checkPointEntity) {
        return CheckPoint.builder()
                .checkPointItems(checkPointItemEntitiesToCheckPointItems(checkPointEntity.getCheckPointItemEntities()))
                .id(checkPointEntity.getId())
                .title(checkPointEntity.getTitle())
                .userId(checkPointEntity.getUserId())
                .status(checkPointEntity.getStatus())
//                .comments(eksDataAccessMapper.commentEntitiesToComments(checkPointEntity.getCommentEntities()))
                .lastUpdateAt(checkPointEntity.getLastUpdateAt())
                .createdAt(checkPointEntity.getCreatedAt())
                .build();
    }
    public CheckPointEntity checkPointToCheckPointEntity(CheckPoint checkPoint) {
        CheckPointEntity checkPointEntity = CheckPointEntity.builder()
                .checkPointItemEntities(checkPointItemsToCheckPointItemEntities(checkPoint.getCheckPointItems()))
                .title(checkPoint.getTitle())
                .userId(checkPoint.getUserId())
//                .commentEntities(eksDataAccessMapper.commentsToCommentEntities(checkPoint.getComments()))
                .lastUpdateAt(checkPoint.getLastUpdateAt())
                .createdAt(checkPoint.getCreatedAt())
                .status(checkPoint.getStatus())
                .build();
        checkPointEntity.getCheckPointItemEntities().forEach(item -> {
            item.setCheckPoint(checkPointEntity);
        });
        if(checkPoint.getId() == null){
            checkPointEntity.setId(UUID.randomUUID());
            checkPointEntity.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")));
            checkPointEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        } else {
            checkPointEntity.setId(checkPoint.getId());
            checkPointEntity.setCreatedAt(checkPoint.getCreatedAt());
            checkPointEntity.setLastUpdateAt(checkPoint.getLastUpdateAt());
        }
        return checkPointEntity;
    }

    public List<CheckPointItemEntity> checkPointItemsToCheckPointItemEntities(List<CheckPointItem> checkPointItems) {
        if(checkPointItems == null) return List.of();
        return checkPointItems.stream().map(checkPointItem -> {
                    CheckPointItemEntity checkPointItemEntity = CheckPointItemEntity.builder()
//                            .commentEntities(eksDataAccessMapper.commentsToCommentEntities(checkPointItem.getComments()))
                            .title(checkPointItem.getTitle())
                            .content(checkPointItem.getContent())
                            .createdAt(checkPointItem.getCreatedAt())
                            .checkPoint(new CheckPointEntity(checkPointItem.getCheckPointId()))
                            .build();
                    if(checkPointItem.getId() == null) {
                        checkPointItemEntity.setId(UUID.randomUUID());
                        checkPointItemEntity.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")));
                        checkPointItemEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
                    } else {
                        checkPointItemEntity.setId(checkPointItem.getId());
                        checkPointItemEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
                    }
                    return checkPointItemEntity;
                }
                ).toList();
    }

    public List<CheckPointItem> checkPointItemEntitiesToCheckPointItems(List<CheckPointItemEntity> checkPointItemEntities) {
        if(checkPointItemEntities == null || checkPointItemEntities.isEmpty()) return List.of();
        return checkPointItemEntities.stream().map(checkPointItemEntity -> CheckPointItem.builder()
                .checkPointId(checkPointItemEntity.getCheckPoint().getId())
                .title(checkPointItemEntity.getTitle())
                .id(checkPointItemEntity.getId())
                .lastUpdateAt(checkPointItemEntity.getLastUpdateAt())
                .createdAt(checkPointItemEntity.getCreatedAt())
                .content(checkPointItemEntity.getContent())
                .build()).toList();
    }

}
