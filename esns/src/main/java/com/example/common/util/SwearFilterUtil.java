package com.example.common.util;

import java.util.Arrays;
import java.util.List;

public class SwearFilterUtil {

    // 욕설 전용 리스트 (대소문자, 변형 포함 확장 가능)
    private static final List<String> SWEAR_WORDS = Arrays.asList(
        "asshole", "bastard", "fuck", "fucking", "shit",
        "ㄱㅐ", "ㄲㅈ", "ㄷㅊ", "ㅁㅊ", "ㅁㅊ년", "ㅁㅊ놈",
        "ㅂㅅ", "ㅂㅅ같", "ㅄ", "ㅄ같", "ㅅㄲ", "ㅅㅂ", "ㅆㅂ",
        "ㅈㄴ", "ㅈㄹ", "ㅈ같", "ㅈ밥", "ㅉㅉ", "ㅊㄹ",
        "개같", "개노답", "개노잼", "개또라이", "개망",
        "꺼져", "닥쳐", "돌아이", "돌았냐", "뒈져", "미친", "미친놈",
        "병신", "새끼", "씨발", "염병", "정신병자", "좃", "좆", "좆같",
        "죽어", "죽을래", "지랄", "지롤"
    );

    // 욕설 포함 여부 필터링
    public static boolean containsSwearWords(String text) {
        if (text == null) return false;
        String lower = text.toLowerCase();
        return SWEAR_WORDS.stream().anyMatch(lower::contains);
    }
}