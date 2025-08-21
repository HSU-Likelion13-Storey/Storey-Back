package com.sixjeon.storey.domain.store.service;

import com.sixjeon.storey.domain.auth.exception.UserNotFoundException;
import com.sixjeon.storey.domain.character.entity.Character;
import com.sixjeon.storey.domain.character.exception.CharacterNotFoundException;
import com.sixjeon.storey.domain.character.repository.CharacterRepository;
import com.sixjeon.storey.domain.event.entity.Event;
import com.sixjeon.storey.domain.event.repository.EventRepository;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.repository.OwnerRepository;
import com.sixjeon.storey.domain.proprietor.service.ProprietorService;
import com.sixjeon.storey.domain.proprietor.web.dto.CheckProprietorReq;
import com.sixjeon.storey.domain.proprietor.web.dto.CheckProprietorRes;
import com.sixjeon.storey.domain.store.entity.Store;
import com.sixjeon.storey.domain.store.exception.*;
import com.sixjeon.storey.domain.store.repository.StoreRepository;
import com.sixjeon.storey.domain.store.web.dto.MapStoreRes;
import com.sixjeon.storey.domain.store.web.dto.RegisterStoreReq;
import com.sixjeon.storey.domain.store.web.dto.StoreDetailRes;
import com.sixjeon.storey.domain.store.web.dto.StoreQrRes;
import com.sixjeon.storey.domain.subscription.entity.Subscription;
import com.sixjeon.storey.domain.subscription.entity.enums.SubscriptionStatus;
import com.sixjeon.storey.domain.subscription.repository.SubscriptionRepository;
import com.sixjeon.storey.domain.user.entity.User;
import com.sixjeon.storey.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final ProprietorService proprietorService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final SubscriptionRepository subscriptionRepository;

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
        // 입력받은 사업자 번호에서 하이픈 제거
        String sanitizeBusinessNumber = registerStoreReq.getBusinessNumber().replaceAll("-", "");

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

        // 가게 등록 후 QR 코드 자동 생성
        store.generateQrCode();
        storeRepository.save(store);

    }

    @Override
    @Transactional
    public List<MapStoreRes> findAllStoresForUserMap(String userLoginId) {
        User user = userRepository.findByLoginId(userLoginId)
                .orElseThrow(UserNotFoundException::new);
        // 모든 가게 조회
        List<Store> allStores = storeRepository.findAll();

        // 사용자가 해금한 가게들
        Set<Long> unlockedStoreIds = user.getUnlockedStoreIdSet();

        return allStores.stream()
                .map(store -> {
                    boolean isUnlocked = unlockedStoreIds.contains(store.getId());

                    String eventContent = eventRepository.findByStoreId(store.getId())
                            .map(Event::getContent)
                            .orElse(null);
                    
                    // 캐릭터 정보 조회
                    Character character = characterRepository.findByStoreId(store.getId())
                            .orElse(null);

                    // 구독 상태 조회
                    SubscriptionStatus subscriptionStatus = subscriptionRepository.findByOwnerId(store.getOwner().getId())
                            .map(Subscription::getStatus)
                            .orElse(SubscriptionStatus.EXPIRED);

                    return MapStoreRes.builder()
                            .storeId(store.getId())
                            .storeName(store.getStoreName())
                            .addressMain(store.getAddressMain())
                            .latitude(store.getLatitude())
                            .longitude(store.getLongitude())
                            .eventContent(eventContent)
                            .isUnlocked(isUnlocked)
                            .characterImageUrl(character != null ? character.getImageUrl() : null)
                            .subscriptionStatus(subscriptionStatus)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StoreDetailRes findStoreDetailForUser(Long storeId, String userLoginId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(NotFoundStoreException::new);

        String eventContent = eventRepository.findByStoreId(storeId)
                .map(Event::getContent)
                .orElse(null);

        return StoreDetailRes.builder()
                .storeId(store.getId())
                .storeName(store.getStoreName())
                .addressMain(store.getAddressMain())
                .detailAddress(store.getAddressDetail())
                .eventContent(eventContent)
                .build();
    }

    @Override
    @Transactional
    public StoreQrRes getStoreQrCode(String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);
        Store store = storeRepository.findByOwner(owner)
                .orElseThrow(NotFoundStoreException::new);
        // qr이 없을 경우 생성하는 로직 -> 근데 가게 등록 때 생성 로직 있음
        if (store.getQrCode() == null || store.getQrCode().trim().isEmpty()) {
            store.generateQrCode();
            storeRepository.save(store);
        }

        Character character = characterRepository.findByStoreId(store.getId())
                .orElseThrow(CharacterNotFoundException::new);

        return StoreQrRes.builder()
                .qrCode(store.getQrCode())
                .characterId(character != null ? character.getId() : null)
                .build();
    }

    @Override
    @Transactional
    public void unlockStoreByQr(String qrCode, String userLoginId) {
        User user = userRepository.findByLoginId(userLoginId)
                .orElseThrow(UserNotFoundException::new);

        Store store = storeRepository.findByQrCode(qrCode)
                .orElseThrow(InvalidQrCodeException::new);

        // 구독 상태 확인 -> 사장님이 구독을 하지 않았다면 예외 발생
        SubscriptionStatus subscriptionStatus = subscriptionRepository.findByOwnerId(store.getOwner().getId())
                .map(Subscription::getStatus)
                .orElse(SubscriptionStatus.EXPIRED);
        // 구독하지 않은 가게 예외
        if (subscriptionStatus != SubscriptionStatus.ACTIVE && subscriptionStatus != SubscriptionStatus.CANCELED_REQUESTED) {
            throw new NotSubscribedException();
        }
        // 이미 해금된 가게 예외
        if (user.isStoreUnlocked(store.getId())){
            throw new StoreAlreadyUnlockedException();
        }

        user.addUnlockedStoreId(store.getId());
        userRepository.save(user);

    }

}

