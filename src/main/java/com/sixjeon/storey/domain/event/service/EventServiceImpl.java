package com.sixjeon.storey.domain.event.service;

import com.sixjeon.storey.domain.event.entity.Event;
import com.sixjeon.storey.domain.event.exception.EventNotFoundException;
import com.sixjeon.storey.domain.event.repository.EventRepository;
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
        Event event = eventRepository.findByStoreId(store.getId())
                .orElseThrow(EventNotFoundException::new);

        return SaveEventRes.builder()
                .content(event.getContent())
                .build();

    }

    private Store findStoreByOwnerLoginId(String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);
        return storeRepository.findByOwner(owner)
                .orElseThrow(NotFoundStoreException::new);

    }
}
