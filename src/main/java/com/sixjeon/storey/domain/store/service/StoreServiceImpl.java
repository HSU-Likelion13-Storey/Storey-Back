package com.sixjeon.storey.domain.store.service;

import com.sixjeon.storey.domain.auth.exception.DuplicatePhoneNumberException;
import com.sixjeon.storey.domain.auth.exception.UserNotFoundException;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.repository.OwnerRepository;
import com.sixjeon.storey.domain.proprietor.service.ProprietorService;
import com.sixjeon.storey.domain.proprietor.web.dto.CheckProprietorReq;
import com.sixjeon.storey.domain.proprietor.web.dto.CheckProprietorRes;
import com.sixjeon.storey.domain.store.entity.Store;
import com.sixjeon.storey.domain.store.exception.InvalidBusinessNumberException;
import com.sixjeon.storey.domain.store.repository.StoreRepository;
import com.sixjeon.storey.domain.store.web.dto.RegisterStoreReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final ProprietorService proprietorService;

    @Override
    @Transactional
    public void registerStore(RegisterStoreReq registerStoreReq, String ownerLoginId) {
        // 사장님 정보 확인
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);
        // 사업자 번호(businessNumber) 중복 체크
        if (storeRepository.existsByBusinessNumber(registerStoreReq.getBusinessNumber())) {
            throw new DuplicatePhoneNumberException();
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
                .inActive(false)
                .build();
        // DB에 저장
        storeRepository.save(store);

    }
}

