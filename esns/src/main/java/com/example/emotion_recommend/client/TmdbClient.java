package com.example.emotion_recommend.client;

import com.example.emotion_recommend.dto.TmdbContentDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;  
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class TmdbClient {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private static final String TMDB_SEARCH_URL = "https://api.themoviedb.org/3/search/multi";

    public TmdbContentDetail searchContentByTitle(String title) {
        RestTemplate restTemplate = new RestTemplate();

        // UriComponentsBuilder 수정: fromHttpUrl -> fromUriString
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(TMDB_SEARCH_URL)
                .queryParam("api_key", apiKey)
                .queryParam("query", title)
                .queryParam("language", "ko");

        // GET 요청을 보내고, 바디를 Map<String,Object> 로 받습니다.
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            builder.toUriString(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );
     
        // ← 이 부분이 핵심: null 체크를 명시적으로 해줍니다.
        Map<String, Object> body = Objects.requireNonNull(
            response.getBody(),
            "TMDB API 응답 바디가 없습니다."
        );

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");

        if (results != null && !results.isEmpty()) {
            var content = results.get(0);
            String description = (String) content.getOrDefault("overview", "설명 없음");
            String posterPath = (String) content.get("poster_path");
            String imageUrl = posterPath != null
                    ? "https://image.tmdb.org/t/p/w500" + posterPath
                    : null;
            return new TmdbContentDetail(description, imageUrl);
        } else {
            return new TmdbContentDetail("설명 없음", null);
        }
    }
}