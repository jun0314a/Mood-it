package com.example.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_calendar")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 감정 캘린더 항목")
public class CalendarEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Calendar_id")
    @Schema(description = "캘린더 항목 고유 ID", example = "101")
    private Long id;

    @Column(name = "User_id", nullable = false)
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Column(name = "Date", nullable = false)
    @Schema(description = "날짜 (yyyy-MM-dd)", example = "2025-05-18")
    private String date;

    @Column(name = "Emotion", nullable = true)
    @Schema(description = "감정을 나타내는 이모지", example = "😊")
    private String emoji;

    @Column(name = "note", nullable = true)
    @Schema(description = "감정 관련 코멘트", example = "기분이 너무 좋았음")
    private String comment;
}
