package com.sixjeon.storey.domain.event.service;

import com.sixjeon.storey.domain.event.entity.Event;
import com.sixjeon.storey.domain.event.repository.EventRepository;
import com.sixjeon.storey.domain.event.web.dto.SaveEventReq;
import com.sixjeon.storey.domain.event.web.dto.SaveEventRes;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.repository.OwnerRepository;
import com.sixjeon.storey.domain.store.entity.Store;
import com.sixjeon.storey.domain.store.exception.NotFoundStoreException;
import com.sixjeon.storey.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sixjeon.storey.domain.auth.exception.UserNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;

    // 가게 이벤트 조회
    @Override
    @Transactional
    public SaveEventRes findStoreEvent(String OwnerLoginId) {
        Store store = findStoreByOwnerLoginId(OwnerLoginId);


        return eventRepository.findByStoreId(store.getId())
                .map(event -> SaveEventRes.builder()
                        .eventId(event.getId())
                        .content(event.getContent())
                        .build())
                .orElse(null);

    }
    // 가게 이벤트 생성 및 수정
    @Override
    @Transactional
    public void createOrUpdateEvent(SaveEventReq saveEventReq, String ownerLoginId) {
        // 로그인한 사장님 가게 정보 조회
        Store store = findStoreByOwnerLoginId(ownerLoginId);

        // 가게 ID로 이벤트 조회 -> 존재 유무에 따라 다른 작업 수행
        eventRepository.findByStoreId(store.getId())
                .ifPresentOrElse(
                      // 이벤트 존재 -> 내용 업데이트 (JPA 더티 채킹)-> 새로운 필드가 만들어지는게 아님
                existingEvent -> existingEvent.updateContent(saveEventReq.getContent()),
                            // 이벤트 없음 -> 새 이벤트 작성
                            () -> {
                                Event newEvent = Event.builder()
                                        .content(saveEventReq.getContent())
                                        .store(store)
                                        .build();
                                eventRepository.save(newEvent);

                }
                );


    }

    @Override
    @Transactional
    public void deleteEvent(String ownerLoginId) {
        // 로그인한 사장님의 가게 정보를 조회
        Store store = findStoreByOwnerLoginId(ownerLoginId);
        // 가게 ID로 삭제할 이벤트 조회
        eventRepository.findByStoreId(store.getId())
                .ifPresent(event -> eventRepository.delete(event));

    }

    private Store findStoreByOwnerLoginId(String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);
        return storeRepository.findByOwner(owner)
                .orElseThrow(NotFoundStoreException::new);

    }
}
