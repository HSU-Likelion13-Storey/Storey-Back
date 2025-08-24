package com.sixjeon.storey.domain.interview.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.sixjeon.storey.domain.interview.exception.EmptyResponseException;
import com.sixjeon.storey.domain.interview.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAiGateWay implements AiGateWay {

    @Value("${openai.api.gpt.key}")
    private String openaiApiKey;

    @Value("${openai.api.gpt.base.url}")
    private String chatUrl;

    @Value("${openai.api.image.url}")
    private String imageUrl;

    @Value("${openai.org.id:}")
    private String openaiOrgId;

    @Value("${openai.project.id:}")
    private String openaiProjectId;

    private final WebClient webClient;

    @Override
    public String firstQuestion(String storeMood, String businessType) {
        String prompt = buildFirstPrompt(storeMood, businessType);
        String raw = chat(prompt);
        return sanitizeQuestion(raw);
    }

    @Override
    public String nextQuestion(String lastAnswer) {
        String prompt = buildNextQuestionPrompt(lastAnswer);
        String raw = chat(prompt);
        return sanitizeQuestion(raw);
    }

    @Override
    public String oneLineSummary(String text) {
        String prompt = buildSummaryPrompt(text);
        String raw = chat(prompt);
        return raw == null || raw.isBlank() ? "가게의 따뜻한 마음을 전하는 공간" : raw.trim();
    }

    @Override
    public String buildCharacterImagePrompt(String oneLineSummary) {
        return buildCharacterImagePromptInternal(oneLineSummary);
    }

    @Override
    public byte[] generateImagePng(String imagePrompt, String size) {
        String promptSafe = (imagePrompt == null || imagePrompt.isBlank())
                ? "A friendly flat vector mascot, centered, white background"
                : imagePrompt;

        String finalSize = (size == null || size.isBlank()) ? "1024x1024" : size;

        Map<String, Object> body = new java.util.LinkedHashMap<>();
        body.put("model", "dall-e-3");
        body.put("prompt", promptSafe);
        body.put("size", finalSize);
        body.put("response_format", "b64_json");

        WebClient wc = webClient;
        JsonNode resp;
        try {
            resp = wc.post()
                    .uri(imageUrl)
                    .headers(h -> {
                        h.add("Authorization", "Bearer " + (openaiApiKey == null ? "" : openaiApiKey.trim()));
                        h.add("Content-Type", "application/json");
                        if (openaiOrgId != null && !openaiOrgId.isBlank()) h.add("OpenAI-Organization", openaiOrgId);
                        if (openaiProjectId != null && !openaiProjectId.isBlank()) h.add("OpenAI-Project", openaiProjectId);
                    })
                    .bodyValue(body)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(err -> Mono.error(new ResponseException()))
                    )
                    .bodyToMono(com.fasterxml.jackson.databind.JsonNode.class)
                    .block();
        } catch (RuntimeException ex) {
            throw ex;
        }

        if (resp == null) {
            throw new EmptyResponseException();
        }
        String b64 = resp.path("data").get(0).path("b64_json").asText(null);
        if (b64 == null || b64.isBlank()) {
            throw new RuntimeException("OpenAI images error: response missing 'b64_json'");
        }
        return java.util.Base64.getDecoder().decode(b64);
    }

    @Override
    public String sanitizeQuestion(String q) {
        if (q == null || q.isBlank())
            return "가게의 대표 메뉴와 그 메뉴에 담긴 이야기를 들려주세요?";

        String text = q.trim();

        if (text.contains("질문:")) {
            int idx = text.indexOf("질문:");
            text = text.substring(idx + "질문:".length()).trim();
        }

        if (text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1).trim();
        }
        if (text.startsWith("- ")) text = text.substring(2).trim();
        if (text.startsWith("• ")) text = text.substring(2).trim();

        if (!text.endsWith("?") && !text.endsWith("？")) text = text + "?";
        return text;
    }

    @Override
    public String generateCharacterName(String oneLineSummary) {
        String prompt = buildCharacterNamePrompt(oneLineSummary);
        String raw = chat(prompt);
        return raw == null || raw.isBlank() ? "스토리" : raw.trim();
    }




    @Override
    public String generateCharacterDescription(String oneLineSummary, String characterName) {
        String prompt = buildCharacterDescriptionPrompt(oneLineSummary, characterName);
        String raw = chat(prompt);
        return raw == null || raw.isBlank() ? "이 친구는 따듯한 마음을 가진 다정한 친구" : raw.trim();
    }

    @Override
    public String generateCharacterTagline(String oneLineSummary) {
        String prompt = buildCharacterTaglinePrompt(oneLineSummary);
        String raw = chat(prompt);
        return raw == null || raw.isBlank() ? " 오늘도 좋은 하루 보내세요! " : raw.trim();
    }

    private String chat(String userPrompt) {
        WebClient wc = webClient;

        Map<String, Object> body = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful branding assistant."),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "temperature", 0.7
        );

        return wc.post()
                .uri(chatUrl)
                .header("Authorization", "Bearer " + (openaiApiKey == null ? "" : openaiApiKey.trim()))
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.path("choices").get(0).path("message").path("content").asText())
                .block();
    }

    private String buildFirstPrompt(String storeMood, String businessType) {
        String mood = (storeMood == null || storeMood.isBlank()) ? "따뜻한" : storeMood.trim();
        String type = (businessType == null || businessType.isBlank()) ? "일반" : businessType.trim();

        return String.format("""
            당신은 소상공인 인터뷰어이자 브랜드 전략가입니다. 목표는 캐릭터 설계에 유용한 디테일을 끌어낼 **첫 질문 한 문장**을 만드는 것입니다.

            [가게 정보]
            - 업종: %s
            - 분위기: %s

            [출력 규칙]
            - 한국어로 질문 **1문장만**
            - 25~60자, 예/아니오 금지
            - 아래 각도 중 1개: 감정기억, 상징물/오브제, 공간·향·소리, 손님/타깃상, 철학·가치, 손동작·습관, 컬러·재질, 별명·호칭
            - 캐릭터 요소로 번역 가능한 명사 포함
            - 불릿, 따옴표, 접두어 없이 질문만
            """, type, mood);
    }

    private String buildNextQuestionPrompt(String answer) {
        return String.format("""
            당신은 소상공인 브랜드 전략가이자 인터뷰어입니다.
            아래 답변을 보고 캐릭터 설계 단서를 더 끌어낼 다음 질문 **1문장(25~60자)**을 출력하세요.
            예/아니오 금지, 불릿/접두어/따옴표 금지, 질문만 출력.

            [사장님 답변]
            %s
            """, answer);
    }

    private String buildSummaryPrompt(String answer) {
        return String.format("""
            당신은 브랜드 스토리텔러입니다.
            아래 대답을 읽고 '감정적 핵심 메시지'를 한 줄(25~40자)로 요약하세요.
            불필요한 설명·조언 금지. 결과만 출력.

            [대답]
            %s
            """, answer);
    }

    private String buildCharacterImagePromptInternal(String summary) {
        return String.format("""
            Create a mascot character illustration for a small business.
            Business concept (Korean): "%s".
            
            [Style]
            - friendly, cute, high-contrast
            - full body, front view
            - white or transparent background
            - poster/branding ready
            """, summary);
    }

    private String buildCharacterDescriptionPrompt(String summary, String characterName) {
        return String.format("""
                당신은 브랜드 캐릭터 설명을 작성하는 전문가입니다.
                아래 핵심 메시지를 바탕으로, %s의 성격과 특징을 표현하는 설명을 작성해주세요.
                
                작성 형식:
                - %s은/는 [성격이나 특징]한 캐릭터에요.
                - '[핵심 메시지와 관련된 좌우명이나 말버릇]' 이 좌우명이랍니다.
                - 총 50자 내외로 작성
                - 친근하고 따듯한 톤으로 작성
                
                예시:
                하루치는 따듯하고 말이 느린 아이에요.
                '버거는 패스트푸드가 아니다 정성이 담긴 슬로우푸드다'가 좌우명이랍니다.
                
            
                [핵심 메시지]
                %s
             
                """, characterName, characterName, summary);
    }

    private String buildCharacterNamePrompt(String summary) {
        return String.format("""
            당신은 사랑스러운 캐릭터 네이밍 전문가입니다.
            아래의 핵심 메시지를 바탕으로, 가게의 마스코트로 사용할 캐릭터의 이름을 지어주세요.
            
            [이름 생성 규칙]
            - 반드시 1~3단어 사이의 짧은 한글 이름이어야 합니다.
            - 귀엽고 부르기 쉬운 이름이어야 합니다.
            - '카페', '가게', '스토어' 같은 단어는 절대 포함하면 안 됩니다.
            - 이름만 간결하게 출력하고, 따음표나 다른 설명은 덧붙이지 마세요.
            
            [핵심 메시지]
            %s
            """, summary);
    }

    private String buildCharacterTaglinePrompt(String summary) {
        return String.format("""
            당신은 친근하고 따듯한 캐릭터의 말풍선 대사를 작성하는 전문가입니다.
  
           아래 가게의 핵심 메시지를 바탕으로, 캐릭터가 손님들에게 건네는 기분 좋은 한마디를 작성해주세요.
           
           [작성 규칙]
           - 10~25자 이내의 짧고 임팩트 있는 한 문장
           - 따듯하고 친근한 톤으로 작성
           - 손님을 반갑게 맞이하거나 기분 좋게 만드는 내용
           - 존댓말 사용 (안녕하세요, ~세요 등)
           - 따옴표나 추가 설명 없이 대사만 출력
           
           [예시]
           - "오늘도 좋은 하루 되세요!"
           - "따듯한 커피로 마음도 따듯하게!"
           - "행복한 순간을 만들어드릴게요!"
         
           [핵심 메시지]
           %s
           """, summary);
}
                
      }