package com.sally.webcrawler.service;

import com.sally.webcrawler.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class StatisticsService {

    private final NewsRepository newsRepository;

    public StatisticsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public Long getTodaysNewsCountForKeyword(String keyword) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime startOfNextDay = startOfDay.plusDays(1);

        return (long) newsRepository.countByTitleContainingKeywordAndDate(startOfDay, startOfNextDay, keyword);
    }

    public Long getYesterdaysNewsCountForKeyword(String keyword) {
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime startOfDay = endOfDay.minusDays(1);

        return (long) newsRepository.countByTitleContainingKeywordAndDate(startOfDay, endOfDay, keyword);
    }

    public Long getTheDayBeforeYesterdaysNewsCountForKeyword(String keyword) {
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(1);
        LocalDateTime startOfDay = endOfDay.minusDays(1);

        return (long) newsRepository.countByTitleContainingKeywordAndDate(startOfDay, endOfDay, keyword);
    }
}
