package com.platform.iperform.dataaccess.eks.entity;

import com.platform.iperform.common.valueobject.EksStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "key_step")
public class KeyStepEntity {
    @Id
    private UUID id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "e_id")
    private EksEntity eks;
    private ZonedDateTime createdAt;
    private String content;
    @Enumerated(EnumType.STRING)
    private EksStatus status;
    private ZonedDateTime lastUpdateAt;
    private int ordinalNumber;
}
