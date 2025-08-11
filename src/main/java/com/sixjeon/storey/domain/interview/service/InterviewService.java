package com.sixjeon.storey.domain.interview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sixjeon.storey.domain.interview.web.dto.InterviewReq;
import com.sixjeon.storey.domain.interview.web.dto.InterviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InterviewService {

    @Value("${openai.api.gpt.key}")
    private String openaiApiKey;

    @Value("${openai.api.gpt.base.url}")
    private String openaiApiBaseUrl;

    private final WebClient.Builder webClientBuilder;

    public InterviewRes processInterview(InterviewReq interviewReq) {
        /**
         * 1. interviewReq 에서 답변 가져오기
         * 2. Prompt 구성
         * 3. OpenAI API 호출
         * 4. 다음 질문 return
         */
        String prompt = buildPrompt(interviewReq.getAnswer());

        String response = callOpenAI(prompt);

        String nextQuestion = parseNextQuestion(response);

        return new InterviewRes(nextQuestion);
    }

    private String buildPrompt(String answer) {
        return String.format(
                """
        당신은 소상공인 브랜드 전략가이자 인터뷰어입니다. 목표는
        "마스코트 캐릭터 설계에 필요한 핵심 스토리 단서"를 끌어내는 것입니다.

        [출력 규칙]
        - 한국어로 '단 하나의 질문'만 출력하세요.
        - 예/아니오로 답할 수 없게, 한 문장으로, 25자~60자 내외로 작성하세요.
        - 일반론/비교/평가/경영조언 금지. (예: "~비교하여", "~추가하신 점", "~변화시킨 점" 금지)
        - 반드시 아래 각도(중 1개)에 집중해 구체적으로 물으세요:
          감정기억, 상징물/오브제, 공간·향·소리, 손님/타깃상, 철학·가치,
          손동작·습관, 컬러·재질, 별명·호칭
        - 캐릭터화 단서(동물/사물/직업/시대감/말투/컬러·재질)로
          이어질 디테일을 유도하는 명사를 질문 안에 포함하세요.
        - 불릿, 따옴표, 접두어 없이 질문 문장만 출력하세요.

        [입력]
        사장님 첫 응답: "%s"

        [좋은 질문 예시]  # 형식 참고용, 내용 복사 금지
        - “어머니의 손맛을 떠올리게 하는 특정 냄새나 소리가 있다면 무엇이며, 그 순간의 장면을 묘사해주시겠어요?”
        - “가게를 상징하는 작은 오브제나 도구가 있다면 무엇이고, 그 물건에 얽힌 짧은 이야기는 무엇인가요?”
        - “단골 손님을 떠올리면 어떤 말투·표정·호칭이 떠오르며, 그 분위기를 한 장면처럼 설명해주시겠어요?”
        """, answer
        );
    }

    private String callOpenAI(String prompt) {
        WebClient webClient = webClientBuilder.build();

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful branding assistant."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        return webClient.post()
                .uri(openaiApiBaseUrl)
                .header("Authorization", "Bearer " + openaiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.path("choices").get(0).path("message").path("content").asText())
                .block();
    }

    private String parseNextQuestion(String content) {
        if (content == null || content.isBlank()) {
            return "가게의 대표 메뉴와 그 메뉴에 담긴 이야기를 들려주세요.";
        }
        String text = content.trim();

        if (!text.contains("질문:")) {
            return sanitizeQuestion(text);
        }
        int idx = text.indexOf("질문:");
        String after = text.substring(idx + "질문:".length()).trim();
        return sanitizeQuestion(after);
    }

    private String sanitizeQuestion(String q) {
        // 혹시 모를 널값 처리
        if (q == null) return "가게의 대표 메뉴와 그 메뉴에 담긴 이야기를 들려주세요.";

        // 양 끝 공백 제거
        String cleaned = q.trim();

        // 혹시 모름 따옴표 제거
        if (cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            cleaned = cleaned.substring(1, cleaned.length() - 1).trim();
        }

        // 가끔 이상한 거 붙임. 불릿, 대시 제거
        if (cleaned.startsWith("- ")) cleaned = cleaned.substring(2).trim();
        if (cleaned.startsWith("• ")) cleaned = cleaned.substring(2).trim();

        // 물음표 붙여서 리턴
        if (!cleaned.endsWith("?") && !cleaned.endsWith("?\u200b") && !cleaned.endsWith("？")) {
            cleaned = cleaned + "?";
        }

        return cleaned;
    }

}
