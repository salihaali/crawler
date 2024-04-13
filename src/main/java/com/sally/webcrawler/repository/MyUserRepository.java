package com.sally.webcrawler.repository;

import com.sally.webcrawler.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByEmail(String email);

    @Query("SELECT u, COUNT(k) as keywordCount FROM MyUser u JOIN u.keywords k GROUP BY u ORDER BY keywordCount DESC LIMIT 3")
    List<Object[]> findTop3UsersWithKeywordCount();
}
