package com.sixjeon.storey.domain.event.service;

import com.sixjeon.storey.domain.event.web.dto.SaveEventRes;

public interface EventService {
    // 이벤트 조회
    SaveEventRes findStoreEvent(String OwnerLoginId);
}
