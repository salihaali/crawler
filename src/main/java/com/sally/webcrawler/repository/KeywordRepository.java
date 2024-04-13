package com.sally.webcrawler.repository;

import com.sally.webcrawler.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByName(String name);

    @Query("SELECT k, COUNT(u) as userCount FROM Keyword k JOIN k.users u GROUP BY k ORDER BY userCount DESC LIMIT 3")
    List<Object[]> findTop3KeywordsWithUserCount();
}
