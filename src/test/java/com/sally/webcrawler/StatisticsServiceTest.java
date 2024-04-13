package com.sally.webcrawler;

import com.sally.webcrawler.repository.NewsRepository;
import com.sally.webcrawler.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class StatisticsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        statisticsService = new StatisticsService(newsRepository);
    }

    @Test
    void getTodaysNewsCountForKeyword() {
        String keyword = "test";
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime startOfNextDay = startOfDay.plusDays(1);

        when(newsRepository.countByTitleContainingKeywordAndDate(startOfDay, startOfNextDay, keyword)).thenReturn(5);

        assertEquals(5L, statisticsService.getTodaysNewsCountForKeyword(keyword));
    }

    @Test
    void getYesterdaysNewsCountForKeyword() {
        String keyword = "test";
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime startOfDay = endOfDay.minusDays(1);

        when(newsRepository.countByTitleContainingKeywordAndDate(startOfDay, endOfDay, keyword)).thenReturn(3);

        assertEquals(3L, statisticsService.getYesterdaysNewsCountForKeyword(keyword));
    }

    @Test
    void getTheDayBeforeYesterdaysNewsCountForKeyword() {
        String keyword = "test";
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(1);
        LocalDateTime startOfDay = endOfDay.minusDays(1);

        when(newsRepository.countByTitleContainingKeywordAndDate(startOfDay, endOfDay, keyword)).thenReturn(7);

        assertEquals(7L, statisticsService.getTheDayBeforeYesterdaysNewsCountForKeyword(keyword));
    }
}
