package com.sixjeon.storey.domain.collection.service;

import com.sixjeon.storey.domain.auth.exception.UserNotFoundException;
import com.sixjeon.storey.domain.character.entity.Character;
import com.sixjeon.storey.domain.character.repository.CharacterRepository;
import com.sixjeon.storey.domain.store.entity.Store;
import com.sixjeon.storey.domain.store.exception.NotFoundStoreException;
import com.sixjeon.storey.domain.store.repository.StoreRepository;
import com.sixjeon.storey.domain.user.entity.User;
import com.sixjeon.storey.domain.user.repository.UserRepository;
import com.sixjeon.storey.domain.collection.web.dto.CollectedCharacterRes;
import com.sixjeon.storey.domain.collection.web.dto.CollectionRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CharacterRepository characterRepository;

    @Override
    @Transactional
    public CollectionRes getUserCollection(String userLoginId) {
        User user = userRepository.findByLoginId(userLoginId)
                .orElseThrow(UserNotFoundException::new);

        Set<Long> unlockedStoreIds = user.getUnlockedStoreIdSet();

        List<CollectedCharacterRes> collectedCharacters = unlockedStoreIds.stream()
                .map(storeId -> {
                    Store store = storeRepository.findById(storeId)
                            .orElseThrow(NotFoundStoreException::new);

                    Character character = characterRepository.findByStoreId(store.getId())
                            .orElse(null);

                    return CollectedCharacterRes.builder()
                            .characterId(character != null ? character.getId() : null)
                            .storeId(store.getId())
                            .storeName(store.getStoreName())
                            .characterImageUrl(character != null ? character.getImageUrl() : null)
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new CollectionRes(
                collectedCharacters,
                collectedCharacters.size()
                );
    }
}
