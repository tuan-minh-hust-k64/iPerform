package com.platform.iperform.common.dto.response;

import com.platform.iperform.model.CheckPointItem;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CheckPointItemResponse {
    private final CheckPointItem checkPointItemList;
}
