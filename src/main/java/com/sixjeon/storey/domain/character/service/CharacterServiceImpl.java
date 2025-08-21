package com.sixjeon.storey.domain.character.service;

import com.sixjeon.storey.domain.auth.exception.AuthErrorCode;
import com.sixjeon.storey.domain.auth.exception.UserNotFoundException;
import com.sixjeon.storey.domain.character.entity.Character;
import com.sixjeon.storey.domain.character.exception.CharacterNotFoundException;
import com.sixjeon.storey.domain.character.repository.CharacterRepository;
import com.sixjeon.storey.domain.character.web.dto.CharacterDetailRes;
import com.sixjeon.storey.domain.character.web.dto.CharacterRes;
import com.sixjeon.storey.domain.interview.entity.InterviewSession;
import com.sixjeon.storey.domain.interview.repository.InterviewSessionRepository;
import com.sixjeon.storey.domain.interview.util.AiGateWay;
import com.sixjeon.storey.domain.interview.util.S3Uploader;
import com.sixjeon.storey.domain.interview.web.dto.InterviewReq;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.repository.OwnerRepository;
import com.sixjeon.storey.domain.store.entity.Store;
import com.sixjeon.storey.domain.store.exception.NotFoundStoreException;
import com.sixjeon.storey.domain.store.repository.StoreRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

    private final AiGateWay aiGateWay;
    private final S3Uploader s3Uploader;
    private final OwnerRepository ownerRepository;
    private final StoreRepository storeRepository;
    private final CharacterRepository characterRepository;
    private final InterviewSessionRepository interviewSessionRepository;

    @Override
    @Transactional
    public CharacterRes generateCharacter(@Valid InterviewReq interviewReq, String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);

        Store store = storeRepository.findByOwner(owner)
                .orElseThrow(NotFoundStoreException::new);

        String oneLine = aiGateWay.oneLineSummary(interviewReq.getAnswer());
        String name = aiGateWay.generateCharacterName(oneLine);
        String description = aiGateWay.generateCharacterDescription(oneLine);
        String imgPrompt = aiGateWay.buildCharacterImagePrompt(oneLine);
        byte[] png = aiGateWay.generateImagePng(imgPrompt, "1024x1024");


        String key = "characters/%s.png".formatted(java.util.UUID.randomUUID());
        String url = s3Uploader.uploadPngAndGetUrl(png, key);

        Character character = Character.builder()
                .name(name)
                .imageUrl(url)
                .tagline(null)
                .description(description)
   //             .narrativeSummary(oneLine)
                .store(store)
                .build();

        characterRepository.save(character);

        return new CharacterRes(url, null, name , description, oneLine); // CharacterRes 구조에 맞게 생성자/빌더 사용
    }

    @Override
    public CharacterDetailRes getCharacterDetail(Long characterId) {
        // 캐릭터 기본 정보 조회
        Character character = characterRepository.findById(characterId)
                .orElseThrow(CharacterNotFoundException::new);
        // 연관관계
        Store store = character.getStore();

        String narrativeSummary = interviewSessionRepository
                .findByStoreIdOrderByCreatedAtDesc(store.getId())
                .stream()
                .findFirst()
                .map(InterviewSession::getNarrativeSummary)
                .orElse(null);

        return new CharacterDetailRes(
                character.getId(),
                character.getImageUrl(),
                character.getTagline(),
                character.getName(),
                character.getDescription(),
                narrativeSummary,
                store.getStoreName(),
                store.getAddressMain(),
                store.getAddressDetail()
        );

    }
}
