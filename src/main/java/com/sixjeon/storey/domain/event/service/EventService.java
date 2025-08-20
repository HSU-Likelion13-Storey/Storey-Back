package com.sixjeon.storey.domain.event.service;

import com.sixjeon.storey.domain.event.web.dto.SaveEventReq;
import com.sixjeon.storey.domain.event.web.dto.SaveEventRes;

public interface EventService {
    // 이벤트 조회
    SaveEventRes findStoreEvent(String OwnerLoginId);
    // 이벤트 생성 및 수정
    void createOrUpdateEvent(SaveEventReq saveEventReq, String ownerLoginId);
    // 이벤트 삭제
    void deleteEvent(String ownerLoginId);

}
