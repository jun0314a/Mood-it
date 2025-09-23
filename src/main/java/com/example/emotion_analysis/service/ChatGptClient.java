package com.example.emotion_analysis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class ChatGptClient {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * 시스템 및 유저 메시지 목록을 받아 Chat Completion을 호출하고
     * 결과 content를 문자열로 반환합니다.
     */
    public String chatCompletion(List<Map<String, String>> messages, double temperature) {
        Map<String, Object> requestBody = Map.of(
            "model", "gpt-3.5-turbo",
            "messages", messages,
            "temperature", temperature
        );
        HttpEntity<Map<String, Object>> requestEntity =
            new HttpEntity<>(requestBody, defaultHeaders());
        ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<>() {};

        ResponseEntity<Map<String, Object>> response;
        try {
            response = restTemplate().exchange(API_URL, HttpMethod.POST, requestEntity, typeRef);
        } catch (RestClientException ex) {
            throw new RuntimeException("OpenAI 호출 실패", ex);
        }

        Map<String, Object> body = response.getBody();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
        @SuppressWarnings("unchecked")
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return message.get("content").toString().trim();
    }

    /**
     * 감정 분포를 JSON 형태로 반환
     */
    public String getEmotionDistribution(String prompt) {
        List<Map<String, String>> messages = List.of(
            Map.of(
                "role", "system",
                "content", "당신은 감정 분포를 JSON 객체로만 반환하는 전문가입니다. 추가 텍스트나 설명 없이, 반드시 유효한 JSON만 응답하세요."
            ),
            Map.of("role", "user", "content", prompt)
        );
        return chatCompletion(messages, 0.0);
    }
}
