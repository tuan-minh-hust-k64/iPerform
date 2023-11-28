package com.platform.iperform.dataaccess.config.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "config")
public class ConfigEntity {
    @Id
    private UUID id;
    private boolean checkPoint;
    private boolean checkIn;
    private String guidCheckIn;
    private String guidCheckPoint;
    private String guidEks;
}
