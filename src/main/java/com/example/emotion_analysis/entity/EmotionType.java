package com.example.emotion_analysis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum EmotionType {
    joy, sadness, anger, calm, anxiety;

    /**
     * JSON → Enum 매핑
     */
    @JsonCreator
    public static EmotionType fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("EmotionType cannot be null");
        }
        // name()과 대소문자 구분 없이 비교
        return Arrays.stream(EmotionType.values())
                     .filter(e -> e.name().equalsIgnoreCase(value))
                     .findFirst()
                     .orElseThrow(() ->
                         new IllegalArgumentException("Unknown emotion type: " + value));
    }

    /**
     * Enum → JSON 직렬화
     */
    @JsonValue
    public String toValue() {
        // 소문자로 직렬화하고 싶다면 name().toLowerCase()
        return this.name();
    }
}