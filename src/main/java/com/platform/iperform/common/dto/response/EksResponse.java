package com.platform.iperform.common.dto.response;

import com.platform.iperform.model.Eks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EksResponse {
    private final List<Eks> eks;
    private final Eks data;
    private final String message;
}
