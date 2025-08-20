package com.sixjeon.storey.domain.store.service;

import com.sixjeon.storey.domain.auth.exception.UserNotFoundException;
import com.sixjeon.storey.domain.event.repository.EventRepository;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.repository.OwnerRepository;
import com.sixjeon.storey.domain.proprietor.service.ProprietorService;
import com.sixjeon.storey.domain.proprietor.web.dto.CheckProprietorReq;
import com.sixjeon.storey.domain.proprietor.web.dto.CheckProprietorRes;
import com.sixjeon.storey.domain.store.entity.Store;
import com.sixjeon.storey.domain.store.exception.AlreadyRegisterStoreException;
import com.sixjeon.storey.domain.store.exception.DuplicateBusinessNumberException;
import com.sixjeon.storey.domain.store.exception.InvalidBusinessNumberException;
import com.sixjeon.storey.domain.store.exception.NotFoundStoreException;
import com.sixjeon.storey.domain.store.repository.StoreRepository;
import com.sixjeon.storey.domain.store.web.dto.MapStoreRes;
import com.sixjeon.storey.domain.store.web.dto.RegisterStoreReq;
import com.sixjeon.storey.domain.store.web.dto.StoreDetailRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final ProprietorService proprietorService;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public void registerStore(RegisterStoreReq registerStoreReq, String ownerLoginId) {
        // 사장님 정보 확인
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);

        // 이미 등록되어있는 가게가 있는지 확인
        if (storeRepository.existsByOwner(owner)) {
            throw new AlreadyRegisterStoreException();
        }

        // 사업자 번호(businessNumber) 중복 체크
        if (storeRepository.existsByBusinessNumber(registerStoreReq.getBusinessNumber())) {
            throw new DuplicateBusinessNumberException();
        }

        // 외부 API로 사업자 번호 유효성 검증
        CheckProprietorReq checkReq = new CheckProprietorReq(registerStoreReq.getBusinessNumber());
        CheckProprietorRes checkRes = proprietorService.checkProprietorNumber(checkReq);
        if (!checkRes.pass()) {
            throw new InvalidBusinessNumberException();
        }


        // 가게 등록
        Store store = Store.builder()
                .owner(owner)
                .storeName(registerStoreReq.getStoreName())
                .representativeName(registerStoreReq.getRepresentativeName())
                .businessNumber(registerStoreReq.getBusinessNumber())
                .businessType(registerStoreReq.getBusinessType())
                .businessCategory(registerStoreReq.getBusinessCategory())
                .addressMain(registerStoreReq.getAddressMain())
                .addressDetail(registerStoreReq.getAddressDetail())
                .postalCode(registerStoreReq.getPostalCode())
                .build();
        // DB에 저장
        storeRepository.save(store);

    }

    @Override
    @Transactional
    public List<MapStoreRes> findAllStoresForMap() {
        return storeRepository.findAllWithEvent();
    }

    @Override
    @Transactional
    public StoreDetailRes findStoreDetail(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(NotFoundStoreException::new);

        String eventContent = eventRepository.findByStoreId(storeId)
                .map(event -> event.getContent())
                .orElse(null);

        return StoreDetailRes.builder()
                .storeId(store.getId())
                .storeName(store.getStoreName())
                .addressMain(store.getAddressMain())
                .detailAddress(store.getAddressDetail())
                .eventContent(eventContent)
                .build();
    }
}

