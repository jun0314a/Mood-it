package com.example.calendar;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
@Tag(name = "캘린더 API", description = "사용자별 감정 캘린더를 조회, 추가, 삭제하는 API")
public class CalendarController {

    private final CalendarRepository calendarRepository;

    @Operation(summary = "사용자 캘린더 조회", description = "해당 사용자의 모든 감정 캘린더 항목을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CalendarEntry.class))))
    @GetMapping("/{userId}")
    public ResponseEntity<List<CalendarEntry>> getUserCalendar(@PathVariable Long userId) {
        List<CalendarEntry> entries = calendarRepository.findByUserId(userId);
        return ResponseEntity.ok(entries);
    }

    @Operation(summary = "캘린더 항목 추가/수정", description = "사용자의 날짜별 감정 항목을 저장하거나 수정합니다.")
    @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = CalendarEntry.class)))
    @PostMapping("/{userId}")
    public ResponseEntity<CalendarEntry> addOrUpdateEntry(@PathVariable Long userId, @RequestBody CalendarEntry entry) {
        entry.setUserId(userId);
        CalendarEntry savedEntry = calendarRepository.save(entry);
        return ResponseEntity.ok(savedEntry);
    }

    @Operation(summary = "캘린더 항목 삭제", description = "해당 사용자와 날짜의 감정 항목을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "항목 없음")
    })
    @DeleteMapping("/{userId}/{date}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long userId, @PathVariable String date) {
        Optional<CalendarEntry> entry = calendarRepository.findByUserIdAndDate(userId, date);
        entry.ifPresent(calendarRepository::delete);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{userId}/{date}/comment")
    public ResponseEntity<CalendarEntry> updateComment(
        @PathVariable Long userId,
        @PathVariable String date,
        @RequestBody String comment) {

        Optional<CalendarEntry> entryOpt = calendarRepository.findByUserIdAndDate(userId, date);
        if (entryOpt.isEmpty()) return ResponseEntity.notFound().build();

        CalendarEntry entry = entryOpt.get();
        entry.setComment(comment);
        return ResponseEntity.ok(calendarRepository.save(entry));
    }
}
