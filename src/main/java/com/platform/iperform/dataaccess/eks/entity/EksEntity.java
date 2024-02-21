package com.platform.iperform.dataaccess.eks.entity;

import com.platform.iperform.common.valueobject.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expectation")
public class EksEntity extends BaseEntityAllowComment {
    private UUID userId;
    private ZonedDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private CategoryEks category;
    @Enumerated(EnumType.STRING)
    private EksType type;
    private String content;
    private String description;
    private int process;
    @Enumerated(EnumType.STRING)
    private EksStatus status;
    private String timePeriod;
    private ZonedDateTime lastUpdateAt;
    private int ordinalNumber;

    @OneToMany(mappedBy = "eks", cascade = CascadeType.ALL)
    private List<KeyStepEntity> keyStepEntities;
//    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
//    private List<CommentEntity> commentEntities;
    @OneToMany(mappedBy = "eks", cascade = CascadeType.ALL)
    private List<CheckInEntity> checkInEntities;

    public EksEntity(UUID id) {
        super.setId(id);
    }

}
