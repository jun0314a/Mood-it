package com.example.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarEntry, Long> {

    List<CalendarEntry> findByUserId(Long userId);

    Optional<CalendarEntry> findByUserIdAndDate(Long userId, String date);
}
