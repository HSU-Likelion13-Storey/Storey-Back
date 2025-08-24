package com.sixjeon.storey.domain.character.service;

import com.sixjeon.storey.domain.auth.exception.UserNotFoundException;
import com.sixjeon.storey.domain.character.entity.Character;
import com.sixjeon.storey.domain.character.exception.AlreadyRegisterCharacterException;
import com.sixjeon.storey.domain.character.exception.CharacterNotFoundException;
import com.sixjeon.storey.domain.character.repository.CharacterRepository;
import com.sixjeon.storey.domain.character.web.dto.*;
import com.sixjeon.storey.domain.interview.entity.InterviewSession;
import com.sixjeon.storey.domain.interview.repository.InterviewSessionRepository;
import com.sixjeon.storey.domain.interview.util.AiGateWay;
import com.sixjeon.storey.domain.interview.util.S3Uploader;
import com.sixjeon.storey.domain.character.web.dto.CharacterCreateReq;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.repository.OwnerRepository;
import com.sixjeon.storey.domain.store.entity.Store;
import com.sixjeon.storey.domain.store.exception.NotFoundStoreException;
import com.sixjeon.storey.domain.store.repository.StoreRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public CharacterRes generateCharacter(@Valid CharacterCreateReq characterCreateReq, String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);

        Store store = storeRepository.findByOwner(owner)
                .orElseThrow(NotFoundStoreException::new);

        if (characterRepository.existsByStoreId(store.getId())) {
            throw new AlreadyRegisterCharacterException();
        }

        // 핵심 메시지 한줄 요약
        String oneLine = aiGateWay.oneLineSummary(characterCreateReq.getAnswer());

        // 최신 세션 조회 or 신규 생성
        InterviewSession interviewSession = interviewSessionRepository.findByStoreIdOrderByCreatedAtDesc(store.getId())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    InterviewSession newSession = new InterviewSession();
                    newSession.setStoreId(store.getId());
                    return newSession;
                });

        interviewSession.setNarrativeSummary(oneLine);
        InterviewSession savedSession = interviewSessionRepository.save(interviewSession);

        // 캐릭터 정보 생성
        String name = aiGateWay.generateCharacterName(oneLine);
        String description = aiGateWay.generateCharacterDescription(oneLine, name);

        // ✅ category까지 반영된 프롬프트 생성
        String imgPrompt = aiGateWay.buildCharacterImagePrompt(oneLine, characterCreateReq.getCategory());
        byte[] png = aiGateWay.generateImagePng(imgPrompt, "1024x1024");

        String key = "characters/%s.png".formatted(java.util.UUID.randomUUID());
        String url = s3Uploader.uploadPngAndGetUrl(png, key);
        String tagline = aiGateWay.generateCharacterTagline(oneLine);

        Character character = Character.builder()
                .name(name)
                .imageUrl(url)
                .tagline(tagline)
                .description(description)
                .store(store)
                .build();

        Character savedCharacter = characterRepository.save(character);

        return new CharacterRes(
                savedCharacter.getId(),
                savedSession.getId(),
                url,
                tagline,
                name,
                description,
                oneLine
        );
    }

    @Override
    @Transactional
    public CharacterRes regenerateCharacter(@Valid CharacterCreateReq characterCreateReq, String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);

        Store store = storeRepository.findByOwner(owner)
                .orElseThrow(NotFoundStoreException::new);

        Character character = characterRepository.findByStoreId(store.getId())
                .orElseGet(() -> {
                    Character newCharacter = new Character();
                    newCharacter.setStore(store);
                    return newCharacter;
                });

        // 한줄 요약
        String oneLine = aiGateWay.oneLineSummary(characterCreateReq.getAnswer());

        // 세션 조회 or 생성
        InterviewSession interviewSession = interviewSessionRepository.findByStoreIdOrderByCreatedAtDesc(store.getId())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    InterviewSession newSession = new InterviewSession();
                    newSession.setStoreId(store.getId());
                    return newSession;
                });

        interviewSession.setNarrativeSummary(oneLine);
        InterviewSession savedSession = interviewSessionRepository.save(interviewSession);

        // 캐릭터 정보 생성
        String name = aiGateWay.generateCharacterName(oneLine);
        String description = aiGateWay.generateCharacterDescription(oneLine, name);

        // ✅ category 반영된 프롬프트
        String imgPrompt = aiGateWay.buildCharacterImagePrompt(oneLine, characterCreateReq.getCategory());
        byte[] png = aiGateWay.generateImagePng(imgPrompt, "1024x1024");

        String key = "characters/%s.png".formatted(java.util.UUID.randomUUID());
        String url = s3Uploader.uploadPngAndGetUrl(png, key);
        String tagline = aiGateWay.generateCharacterTagline(oneLine);

        character.setName(name);
        character.setImageUrl(url);
        character.setTagline(tagline);
        character.setDescription(description);

        return new CharacterRes(
                character.getId(),
                savedSession.getId(),
                url,
                tagline,
                name,
                description,
                oneLine
        );
    }

    @Override
    public CharacterDetailRes getCharacterDetail(Long characterId) {
        // 캐릭터 기본 정보 조회
        Character character = characterRepository.findById(characterId)
                .orElseThrow(CharacterNotFoundException::new);
        // 연관관계
        Store store = character.getStore();

        Optional<InterviewSession> sessionOpt = interviewSessionRepository
                .findByStoreIdOrderByCreatedAtDesc(store.getId())
                .stream()
                .findFirst();

        String narrativeSummary = sessionOpt.map(InterviewSession::getNarrativeSummary).orElse(null);
        Long interviewSessionId = sessionOpt.map(InterviewSession::getId).orElse(null);

        return new CharacterDetailRes(
                character.getId(),
                store.getId(),
                interviewSessionId,
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

    @Override
    @Transactional
    public UpdateCharacterRes updateCharacter(UpdateCharacterReq updateCharacterReq, String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);

        Store store = storeRepository.findByOwner(owner)
                .orElseThrow(NotFoundStoreException::new);

        Character character = characterRepository.findByStoreId(store.getId())
                .orElseThrow(CharacterNotFoundException::new);

        character.setName(updateCharacterReq.getName());
        character.setDescription(updateCharacterReq.getDescription());
        character.setTagline(updateCharacterReq.getTagline());

        characterRepository.save(character);

        String finalNarrativeSummary = updateCharacterReq.getNarrativeSummary();
        if (finalNarrativeSummary != null && !finalNarrativeSummary.trim().isEmpty()) {
            // 기존 세션이 있으면 업데이트, 없으면 새로 생성 (임시방편)
            List<InterviewSession> sessions = interviewSessionRepository.findByStoreIdOrderByCreatedAtDesc(store.getId());

            if (!sessions.isEmpty()) {
                // 기존 세션이 있으면 업데이트
                InterviewSession session = sessions.get(0);
                session.setNarrativeSummary(finalNarrativeSummary);
            } else {
                // 세션이 없으면 임시로 생성 (파트너가 인터뷰 저장 로직 추가하기 전까지)
                InterviewSession newSession = new InterviewSession();
                newSession.setStoreId(store.getId());
                newSession.setNarrativeSummary(finalNarrativeSummary);
                interviewSessionRepository.save(newSession);
            }
        }

        return UpdateCharacterRes.builder()
                .characterId(character.getId())
                .name(character.getName())
                .description(character.getDescription())
                .tagline(character.getTagline())
                .narrativeSummary(finalNarrativeSummary)
                .build();
    }

    @Override
    @Transactional
    public MyCharacterRes getMyCharacter(String ownerLoginId) {
        Owner owner = ownerRepository.findByLoginId(ownerLoginId)
                .orElseThrow(UserNotFoundException::new);

        Store store = storeRepository.findByOwner(owner)
                .orElseThrow(NotFoundStoreException::new);

        // 캐릭터 존재 유무 확인
        Optional<Character> characterOptional = characterRepository.findByStoreId(store.getId());

        if (characterOptional.isEmpty()) {
            return MyCharacterRes.noCharacter();
        }

        Character character = characterOptional.get();

        String narrativeSummary = interviewSessionRepository
                .findByStoreIdOrderByCreatedAtDesc(store.getId())
                .stream()
                .findFirst()
                .map(InterviewSession::getNarrativeSummary)
                .orElse(null);


        return MyCharacterRes.builder()
                .hasCharacter(true)
                .characterId(character.getId())
                .imageUrl(character.getImageUrl())
                .tagline(character.getTagline())
                .name(character.getName())
                .description(character.getDescription())
                .narrativeSummary(narrativeSummary)
                .build();
    }
}
