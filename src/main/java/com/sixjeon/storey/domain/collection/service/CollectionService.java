package com.sixjeon.storey.domain.collection.service;

import com.sixjeon.storey.domain.collection.web.dto.CollectionRes;

public interface CollectionService {
    CollectionRes getUserCollection(String userLoginId);
}
