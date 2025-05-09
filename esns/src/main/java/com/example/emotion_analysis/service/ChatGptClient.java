package com.example.emotion_analysis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Component
public class ChatGptClient {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String getEmotionFromText(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 바디 설정
        Map<String, Object> requestBody = Map.of(
            "model", "gpt-3.5-turbo",
            "messages", List.of(Map.of("role", "user", "content", prompt)),
            "temperature", 1.0
        );

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // 응답을 Map<String,Object> 으로 받도록 타입 지정
        ParameterizedTypeReference<Map<String, Object>> typeRef =
            new ParameterizedTypeReference<>() {};

        ResponseEntity<Map<String, Object>> response;
        try {
            response = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                requestEntity,
                typeRef
            );
        } catch (RestClientException ex) {
            throw new RuntimeException("OpenAI 호출 실패", ex);
        }

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("choices")) {
            throw new RuntimeException("OpenAI 응답이 올바르지 않습니다.");
        }

        // 안전하게 타입 변환
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> choices =
            (List<Map<String, Object>>) body.get("choices");

        if (choices.isEmpty()) {
            throw new RuntimeException("OpenAI가 응답을 반환하지 않았습니다.");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> message =
            (Map<String, Object>) choices.get(0).get("message");

        Object content = message.get("content");
        if (content == null) {
            throw new RuntimeException("OpenAI 응답에 content 가 없습니다.");
        }

        // 결과 문자열 정리
        return content.toString()
                      .trim()
                      .toLowerCase();
    }
}
