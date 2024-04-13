package com.sally.webcrawler.repository;


import com.sally.webcrawler.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByTitleContainingIgnoreCase(String keyword);

    @Query("SELECT COUNT(n) FROM News n WHERE n.dateTimeAdded >= :startOfDay AND n.dateTimeAdded < :startOfNextDay AND n.title LIKE %:keyword%")
    int countByTitleContainingKeywordAndDate(LocalDateTime startOfDay, LocalDateTime startOfNextDay, String keyword);
}
