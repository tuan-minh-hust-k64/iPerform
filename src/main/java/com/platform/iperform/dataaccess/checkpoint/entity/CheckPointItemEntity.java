package com.platform.iperform.dataaccess.checkpoint.entity;

import com.platform.iperform.common.valueobject.BaseEntityAllowComment;
import com.platform.iperform.dataaccess.comment.entity.CommentEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "check_point_item")
public class CheckPointItemEntity extends BaseEntityAllowComment{
    @ManyToOne()
    @JoinColumn(name = "check_point_id")
    private CheckPointEntity checkPoint;
    private ZonedDateTime createdAt;
    private String title;
    private String content;
    private ZonedDateTime lastUpdateAt;

//    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
//    private List<CommentEntity> commentEntities;

    @Override
    public String toString() {
        return "CheckPointItemEntity{" +
                "createdAt=" + createdAt +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", lastUpdateAt=" + lastUpdateAt +

                '}';
    }
}
