package com.platform.iperform.model;

import com.platform.iperform.common.valueobject.CheckInStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CheckIn {
    private final UUID id;
    private final UUID eId;
    private final ZonedDateTime createdAt;
    private String content;
    private CheckInStatus status;
    private String type;
    private ZonedDateTime lastUpdateAt;

    private final List<Comment> comments;
}
