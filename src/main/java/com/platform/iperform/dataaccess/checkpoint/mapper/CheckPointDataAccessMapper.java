package com.platform.iperform.dataaccess.checkpoint.mapper;

import com.platform.iperform.dataaccess.checkpoint.entity.CheckPointEntity;
import com.platform.iperform.dataaccess.checkpoint.entity.CheckPointItemEntity;
import com.platform.iperform.dataaccess.eks.mapper.EksDataAccessMapper;
import com.platform.iperform.model.CheckPoint;
import com.platform.iperform.model.CheckPointItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
                .comments(eksDataAccessMapper.commentEntitiesToComments(checkPointEntity.getCommentEntities()))
                .lastUpdateAt(checkPointEntity.getLastUpdateAt())
                .createdAt(checkPointEntity.getCreatedAt())
                .build();
    }
    public CheckPointEntity checkPointToCheckPointEntity(CheckPoint checkPoint) {
        return CheckPointEntity.builder()
                .checkPointItemEntities(checkPointItemToCheckPointItemEntities(checkPoint.getCheckPointItems()))
                .title(checkPoint.getTitle())
                .userId(checkPoint.getUserId())
                .commentEntities(eksDataAccessMapper.commentsToCommentEntities(checkPoint.getComments()))
                .lastUpdateAt(checkPoint.getLastUpdateAt())
                .createdAt(checkPoint.getCreatedAt())
                .status(checkPoint.getStatus())
                .build();
    }

    private List<CheckPointItemEntity> checkPointItemToCheckPointItemEntities(List<CheckPointItem> checkPointItems) {
        return checkPointItems.stream().map(checkPointItem -> CheckPointItemEntity.builder()
                .commentEntities(eksDataAccessMapper.commentsToCommentEntities(checkPointItem.getComments()))
                .title(checkPointItem.getTitle())
                .lastUpdateAt(checkPointItem.getLastUpdateAt())
                .createdAt(checkPointItem.getCreatedAt())
                .content(checkPointItem.getContent())
                .build()).toList();
    }

    public List<CheckPointItem> checkPointItemEntitiesToCheckPointItems(List<CheckPointItemEntity> checkPointItemEntities) {
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
