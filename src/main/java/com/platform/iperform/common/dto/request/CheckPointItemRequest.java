package com.platform.iperform.common.dto.request;

import com.platform.iperform.model.CheckPointItem;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CheckPointItemRequest {
    private final CheckPointItem checkPointItemList;
}
