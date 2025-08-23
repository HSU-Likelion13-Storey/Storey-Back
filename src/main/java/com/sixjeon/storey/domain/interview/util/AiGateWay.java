package com.sixjeon.storey.domain.interview.util;

public interface AiGateWay {
    // 첫 질문 생성
    String firstQuestion(String storeMood, String businessType);

    // 다음 질문 생성
    String nextQuestion(String lastAnswer);

    // 핵심 한줄 요약
    String oneLineSummary(String text);

    // 캐릭터용 이미지 프롬프트 만들기
    String buildCharacterImagePrompt(String oneLineSummary);

    // 이미지 생성 (PNG bytes 반환)
    byte[] generateImagePng(String imagePrompt, String size);

    // 문자열 정제
    String sanitizeQuestion(String content);

    // 캐릭터 이름 만들기
    String generateCharacterName(String oneLineSummary);
    
    // 캐릭터 설명 생성
    String generateCharacterDescription(String oneLineSummary);

    String generateCharacterTagline(String oneLineSummary);
}
