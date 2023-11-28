package com.platform.iperform.common.valueobject;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Entity
@Setter
public class BaseEntityAllowComment {
    @Id
    private UUID id;
}
